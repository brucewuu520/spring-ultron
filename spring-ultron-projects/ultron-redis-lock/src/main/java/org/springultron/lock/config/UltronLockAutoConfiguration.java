package org.springultron.lock.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.redisson.misc.RedisURI;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionDetails;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
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
@AutoConfiguration
@ConditionalOnClass(Redisson.class)
@EnableConfigurationProperties({RedisProperties.class, RedissonProperties.class})
@Import({RedisLockAspect.class})
public class UltronLockAutoConfiguration {

    private final RedisProperties redisProperties;
    private final RedissonProperties redissonProperties;
    private final ObjectProvider<List<RedissonConfigCustomizer>> redissonConfigCustomizers;
    private final ApplicationContext context;

    @Autowired
    public UltronLockAutoConfiguration(RedisProperties redisProperties, RedissonProperties redissonProperties,
                                       ObjectProvider<List<RedissonConfigCustomizer>> redissonConfigCustomizers,
                                       ApplicationContext context) {
        this.redisProperties = redisProperties;
        this.redissonProperties = redissonProperties;
        this.redissonConfigCustomizers = redissonConfigCustomizers;
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

        String username;
        int database = redisProperties.getDatabase();
        String password = redisProperties.getPassword();
        boolean isSentinel = false;
        boolean isCluster = false;
        if (hasConnectionDetails()) {
            ObjectProvider<RedisConnectionDetails> provider = context.getBeanProvider(RedisConnectionDetails.class);
            RedisConnectionDetails b = provider.getIfAvailable();
            if (b != null) {
                password = b.getPassword();

                if (b.getSentinel() != null) {
                    isSentinel = true;
                }
                if (b.getCluster() != null) {
                    isCluster = true;
                }
            }
        }

        Integer timeout = redisProperties.getTimeout() != null ? (int) redisProperties.getTimeout().toMillis() : null;

        Integer connectTimeout = redisProperties.getConnectTimeout() != null ? (int) redisProperties.getConnectTimeout().toMillis() : null;
        if (connectTimeout == null && timeout != null) {
            connectTimeout = timeout;
        }

        String clientName = redisProperties.getClientName();

        username = redisProperties.getUsername();

        if (redissonProperties.getConfig() != null) {
            try {
                config = Config.fromYAML(redissonProperties.getConfig());
            } catch (Exception e) {
                throw new IllegalArgumentException("Can't parse config", e);
            }
        } else if (redissonProperties.getFile() != null) {
            try {
                InputStream is = getConfigStream();
                config = Config.fromYAML(is);
            } catch (Exception e) {
                throw new IllegalArgumentException("Can't parse config", e);
            }
        } else if (redisProperties.getSentinel() != null || isSentinel) {
            String[] nodes = {};
            String sentinelMaster = null;

            if (redisProperties.getSentinel() != null) {
                nodes = convert(prefix, redisProperties.getSentinel().getNodes());
                sentinelMaster = redisProperties.getSentinel().getMaster();
            }

            String sentinelUsername = null;
            String sentinelPassword = null;
            if (hasConnectionDetails()) {
                ObjectProvider<RedisConnectionDetails> provider = context.getBeanProvider(RedisConnectionDetails.class);
                RedisConnectionDetails b = provider.getIfAvailable();
                if (b != null && b.getSentinel() != null) {
                    database = b.getSentinel().getDatabase();
                    sentinelMaster = b.getSentinel().getMaster();
                    nodes = convertNodes(prefix, b.getSentinel().getNodes());
                    sentinelUsername = b.getSentinel().getUsername();
                    sentinelPassword = b.getSentinel().getPassword();
                }
            }

            config = new Config()
                    .setUsername(username)
                    .setPassword(password);

            SentinelServersConfig c = config.useSentinelServers()
                                            .setMasterName(sentinelMaster)
                                            .addSentinelAddress(nodes)
                                            .setSentinelPassword(sentinelPassword)
                                            .setSentinelUsername(sentinelUsername)
                                            .setDatabase(database)
                                            .setClientName(clientName);
            if (connectTimeout != null) {
                c.setConnectTimeout(connectTimeout);
            }
            if (timeout != null) {
                c.setTimeout(timeout);
            }
            initSSL(config);
        } else if (redisProperties.getCluster() != null || isCluster) {
            String[] nodes = {};
            if (redisProperties.getCluster() != null) {
                nodes = convert(prefix, redisProperties.getCluster().getNodes());
            }

            if (hasConnectionDetails()) {
                ObjectProvider<RedisConnectionDetails> provider = context.getBeanProvider(RedisConnectionDetails.class);
                RedisConnectionDetails b = provider.getIfAvailable();
                if (b != null && b.getCluster() != null) {
                    nodes = convertNodes(prefix, b.getCluster().getNodes());
                }
            }

            config = new Config().setUsername(username).setPassword(password);
            ClusterServersConfig c = config.useClusterServers().addNodeAddress(nodes).setClientName(clientName);
            if (connectTimeout != null) {
                c.setConnectTimeout(connectTimeout);
            }
            if (timeout != null) {
                c.setTimeout(timeout);
            }
            initSSL(config);
        } else {
            config = new Config()
                    .setUsername(username)
                    .setPassword(password);

            String singleAddr = prefix + redisProperties.getHost() + ":" + redisProperties.getPort();

            if (hasConnectionDetails()) {
                ObjectProvider<RedisConnectionDetails> provider = context.getBeanProvider(RedisConnectionDetails.class);
                RedisConnectionDetails b = provider.getIfAvailable();
                if (b != null && b.getStandalone() != null) {
                    database = b.getStandalone().getDatabase();
                    singleAddr = prefix + b.getStandalone().getHost() + ":" + b.getStandalone().getPort();
                }
            }

            SingleServerConfig c = config.useSingleServer()
                                         .setAddress(singleAddr)
                                         .setDatabase(database)
                                         .setClientName(clientName);
            if (connectTimeout != null) {
                c.setConnectTimeout(connectTimeout);
            }
            if (timeout != null) {
                c.setTimeout(timeout);
            }
            initSSL(config);
        }
        if (redissonConfigCustomizers != null) {
            redissonConfigCustomizers.ifAvailable(customizers -> {
                for (RedissonConfigCustomizer customizer : customizers) {
                    customizer.customize(config);
                }
            });
        }
        return Redisson.create(config);
    }

    private boolean hasConnectionDetails() {
        // try {
        //     Class.forName("org.springframework.boot.autoconfigure.data.redis.RedisConnectionDetails");
        //     return true;
        // } catch (ClassNotFoundException e) {
        //     return false;
        // }
        return true;
    }

    private void initSSL(Config config) {
        RedisProperties.Ssl ssl = redisProperties.getSsl();
        if (ssl.getBundle() == null) {
            return;
        }

        ObjectProvider<SslBundles> provider = context.getBeanProvider(SslBundles.class);
        SslBundles bundles = provider.getIfAvailable();
        if (bundles == null) {
            return;
        }
        SslBundle b = bundles.getBundle(ssl.getBundle());
        if (b == null) {
            return;
        }
        config.setSslCiphers(b.getOptions().getCiphers());
        config.setSslProtocols(b.getOptions().getEnabledProtocols());
        config.setSslTrustManagerFactory(b.getManagers().getTrustManagerFactory());
        config.setSslKeyManagerFactory(b.getManagers().getKeyManagerFactory());
    }

    private String getPrefix() {
        if (redisProperties.getSsl() != null && redisProperties.getSsl().isEnabled()) {
            return RedisURI.REDIS_SSL_PROTOCOL;
        } else {
            return RedisURI.REDIS_PROTOCOL;
        }
    }

    private String[] convertNodes(String prefix, List<?> nodesObject) {
        List<String> nodes = new ArrayList<>(nodesObject.size());
        try {
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
        List<String> nodes = new ArrayList<>(nodesObject.size());
        for (String node : nodesObject) {
            if (!RedisURI.isValid(node)) {
                nodes.add(prefix + node);
            } else {
                nodes.add(node);
            }
        }
        return nodes.toArray(new String[0]);
    }

    private InputStream getConfigStream() throws IOException {
        Resource resource = context.getResource(redissonProperties.getFile());
        return resource.getInputStream();
    }

}
