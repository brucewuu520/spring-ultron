package org.springultron.lock.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.*;
import org.redisson.misc.RedisURI;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.boot.data.redis.autoconfigure.DataRedisConnectionDetails;
import org.springframework.boot.data.redis.autoconfigure.DataRedisProperties;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisOperations;
import org.springultron.lock.client.RedisLockClient;
import org.springultron.lock.client.RedisLockClientImpl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;

/**
 * 分布式锁自动配置
 *
 * @author brucewuu
 * @date 2020/4/27 21:12
 */
@AutoConfiguration(before = DataRedisAutoConfiguration.class)
@ConditionalOnClass({Redisson.class, RedisOperations.class, DataRedisAutoConfiguration.class})
@EnableConfigurationProperties({DataRedisProperties.class, RedissonProperties.class})
@Import({RedisLockAspect.class})
public class UltronLockAutoConfiguration {

    public static final String[] EMPTY = {};

    private final DataRedisProperties redisProperties;
    private final RedissonProperties redissonProperties;
    private final ObjectProvider<List<RedissonAutoConfigurationCustomizer>> redissonAutoConfigurationCustomizers;
    private final ApplicationContext context;

    @Autowired
    public UltronLockAutoConfiguration(DataRedisProperties redisProperties, RedissonProperties redissonProperties,
                                       ObjectProvider<List<RedissonAutoConfigurationCustomizer>> redissonAutoConfigurationCustomizers,
                                       ApplicationContext context) {
        this.redisProperties = redisProperties;
        this.redissonProperties = redissonProperties;
        this.redissonAutoConfigurationCustomizers = redissonAutoConfigurationCustomizers;
        this.context = context;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisLockClient redisLockClient(RedissonClient redisson) {
        return new RedisLockClientImpl(redisson);
    }

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redisson() throws IOException {
        Config config;
        String prefix = getPrefix();

        String username = redisProperties.getUsername();
        int database = redisProperties.getDatabase();
        String password = redisProperties.getPassword();
        String clientName = redisProperties.getClientName();

        boolean isSentinel = false;
        boolean isCluster = false;

        ObjectProvider<DataRedisConnectionDetails> provider = context.getBeanProvider(DataRedisConnectionDetails.class);
        DataRedisConnectionDetails connectionDetails = provider.getIfAvailable();
        if (connectionDetails != null) {
            password = connectionDetails.getPassword();
            username = connectionDetails.getUsername();
            isSentinel = connectionDetails.getSentinel() != null;
            isCluster = connectionDetails.getCluster() != null;
        }

        if (redissonProperties.getConfig() != null) {
            config = Config.fromYAML(redissonProperties.getConfig());
        } else if (redissonProperties.getFile() != null) {
            try (InputStream is = getConfigStream()) {
                config = Config.fromYAML(is);
            }
        } else if (redisProperties.getSentinel() != null || isSentinel) {
            config = buildSentinelConfig(prefix, username, password, database, clientName, connectionDetails);
        } else if (redisProperties.getCluster() != null || isCluster) {
            config = buildClusterConfig(prefix, username, password, clientName, connectionDetails);
        } else {
            config = buildSingleServerConfig(prefix, username, password, database, clientName, connectionDetails);
        }

        if (redissonAutoConfigurationCustomizers != null) {
            redissonAutoConfigurationCustomizers.ifAvailable(customizers -> {
                for (RedissonAutoConfigurationCustomizer customizer : customizers) {
                    customizer.customize(config);
                }
            });
        }
        return Redisson.create(config);
    }

    private Config buildSentinelConfig(String prefix, String username, String password, int database,
                                       String clientName, DataRedisConnectionDetails connectionDetails) {
        String[] nodes;
        String sentinelMaster;
        String sentinelUsername = null;
        String sentinelPassword = null;

        if (connectionDetails != null && connectionDetails.getSentinel() != null) {
            DataRedisConnectionDetails.Sentinel sentinel = connectionDetails.getSentinel();
            database = sentinel.getDatabase();
            sentinelMaster = sentinel.getMaster();
            nodes = convertNodes(prefix, sentinel.getNodes());
            sentinelUsername = sentinel.getUsername();
            sentinelPassword = sentinel.getPassword();
        } else {
            DataRedisProperties.Sentinel sentinel = redisProperties.getSentinel();
            if (sentinel != null) {
                nodes = convert(prefix, sentinel.getNodes());
                sentinelMaster = sentinel.getMaster();
            } else {
                nodes = EMPTY;
                sentinelMaster = null;
            }
        }

        Config config = new Config()
                .setUsername(username)
                .setPassword(password);

        SentinelServersConfig c = config.useSentinelServers()
                                        .setMasterName(sentinelMaster)
                                        .addSentinelAddress(nodes)
                                        .setSentinelPassword(sentinelPassword)
                                        .setSentinelUsername(sentinelUsername)
                                        .setDatabase(database)
                                        .setClientName(clientName);

        setTimeouts(c);
        initSSL(config);
        return config;
    }

    private Config buildClusterConfig(String prefix, String username, String password,
                                      String clientName, DataRedisConnectionDetails connectionDetails) {
        String[] nodes;

        if (connectionDetails != null && connectionDetails.getCluster() != null) {
            nodes = convertNodes(prefix, connectionDetails.getCluster().getNodes());
        } else {
            DataRedisProperties.Cluster cluster = redisProperties.getCluster();
            if (cluster != null) {
                nodes = convert(prefix, cluster.getNodes());
            } else {
                nodes = EMPTY;
            }
        }

        Config config = new Config()
                .setUsername(username)
                .setPassword(password);

        ClusterServersConfig c = config.useClusterServers()
                                       .addNodeAddress(nodes)
                                       .setClientName(clientName);

        setTimeouts(c);
        initSSL(config);
        return config;
    }

    private Config buildSingleServerConfig(String prefix, String username, String password, int database,
                                           String clientName, DataRedisConnectionDetails connectionDetails) {
        String singleAddr;

        if (connectionDetails != null && connectionDetails.getStandalone() != null) {
            DataRedisConnectionDetails.Standalone standalone = connectionDetails.getStandalone();
            database = standalone.getDatabase();
            singleAddr = prefix + standalone.getHost() + ":" + standalone.getPort();
        } else {
            singleAddr = prefix + redisProperties.getHost() + ":" + redisProperties.getPort();
        }

        Config config = new Config()
                .setUsername(username)
                .setPassword(password);

        SingleServerConfig c = config.useSingleServer()
                                     .setAddress(singleAddr)
                                     .setDatabase(database)
                                     .setClientName(clientName);

        setTimeouts(c);
        initSSL(config);
        return config;
    }

    private void setTimeouts(BaseConfig<?> c) {
        if (redisProperties.getConnectTimeout() != null) {
            c.setConnectTimeout((int) redisProperties.getConnectTimeout().toMillis());
        }
        if (redisProperties.getTimeout() != null) {
            c.setTimeout((int) redisProperties.getTimeout().toMillis());
        }
    }

    private void initSSL(Config config) {
        DataRedisProperties.Ssl ssl = redisProperties.getSsl();
        if (ssl.getBundle() == null) {
            return;
        }

        ObjectProvider<SslBundles> provider = context.getBeanProvider(SslBundles.class);
        SslBundles bundles = provider.getIfAvailable();
        if (bundles == null) {
            return;
        }

        SslBundle bundle = bundles.getBundle(ssl.getBundle());
        config.setSslCiphers(bundle.getOptions().getCiphers());
        config.setSslProtocols(bundle.getOptions().getEnabledProtocols());
        config.setSslTrustManagerFactory(bundle.getManagers().getTrustManagerFactory());
        config.setSslKeyManagerFactory(bundle.getManagers().getKeyManagerFactory());
    }

    private String getPrefix() {
        DataRedisProperties.Ssl ssl = redisProperties.getSsl();
        if (ssl.isEnabled()) {
            return RedisURI.REDIS_SSL_PROTOCOL;
        }
        return RedisURI.REDIS_PROTOCOL;
    }

    @SuppressWarnings("IllegalCatch")
    private String[] convertNodes(String prefix, List<?> nodesObject) {
        List<String> nodes = new ArrayList<>(nodesObject.size());
        try {
            // fixes JDK 8 record type compilation error
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            for (Object node : nodesObject) {
                MethodType hostType = MethodType.methodType(String.class);
                MethodHandle hostHandle = lookup.findVirtual(node.getClass(), "host", hostType);
                String host = (String) hostHandle.invoke(node);

                MethodType portType = MethodType.methodType(int.class);
                MethodHandle portHandle = lookup.findVirtual(node.getClass(), "port", portType);
                int port = (int) portHandle.invoke(node);

                nodes.add(prefix + host + ":" + port);
            }
        } catch (Throwable e) {
            throw new IllegalStateException("Failed to convert nodes", e);
        }
        return nodes.toArray(new String[0]);
    }

    private String[] convert(String prefix, List<String> nodesObject) {
        if (nodesObject == null) {
            return EMPTY;
        }
        return nodesObject.stream()
                          .map(node -> {
                              if (RedisURI.isValid(node)) {
                                  return node;
                              }
                              return prefix + node;
                          })
                          .toArray(String[]::new);
    }

    private InputStream getConfigStream() throws IOException {
        Resource resource = context.getResource(redissonProperties.getFile());
        return resource.getInputStream();
    }

}
