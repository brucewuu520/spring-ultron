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
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.util.ReflectionUtils;
import org.springultron.core.config.UltronAutoConfiguration;
import org.springultron.core.utils.SpringUtils;
import org.springultron.core.utils.StringUtils;
import org.springultron.lock.client.RedisLockClient;
import org.springultron.lock.client.RedisLockClientImpl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 分布式锁自动配置
 *
 * @author brucewuu
 * @date 2020/4/27 21:12
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Redisson.class)
@AutoConfigureAfter(UltronAutoConfiguration.class)
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

        String username = redisProperties.getUsername();
        int database = redisProperties.getDatabase();
        String password = redisProperties.getPassword();

        Integer timeout = redisProperties.getTimeout() != null ? (int) redisProperties.getTimeout().toMillis() : null;

        Integer connectTimeout = redisProperties.getConnectTimeout() != null ? (int) redisProperties.getConnectTimeout().toMillis() : null;
        if (connectTimeout == null && timeout != null) {
            connectTimeout = timeout;
        }

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
        } else if (redisProperties.getSentinel() != null) {
            String[] nodes = {};
            String sentinelMaster = null;

            if (redisProperties.getSentinel() != null) {
                nodes = convert(prefix, redisProperties.getSentinel().getNodes());
                sentinelMaster = redisProperties.getSentinel().getMaster();
            }

            config = new Config()
                    .setUsername(username)
                    .setPassword(password);

            SentinelServersConfig c = config.useSentinelServers()
                                            .setMasterName(sentinelMaster)
                                            .addSentinelAddress(nodes)
                                            .setDatabase(database)
                                            .setClientName(redisProperties.getClientName());
            if (connectTimeout != null) {
                c.setConnectTimeout(connectTimeout);
            }
            if (timeout != null) {
                c.setTimeout(timeout);
            }
        } else if (redisProperties.getCluster() != null) {
            String[] nodes = {};
            if (redisProperties.getCluster() != null) {
                nodes = convert(prefix, redisProperties.getCluster().getNodes());
            }

            config = new Config().setUsername(username).setPassword(password);
            ClusterServersConfig c = config.useClusterServers()
                                           .addNodeAddress(nodes)
                                           .setClientName(redisProperties.getClientName());
            if (connectTimeout != null) {
                c.setConnectTimeout(connectTimeout);
            }
            if (timeout != null) {
                c.setTimeout(timeout);
            }
        } else {
            config = new Config()
                    .setUsername(username)
                    .setPassword(password);

            String singleAddr = prefix + redisProperties.getHost() + ":" + redisProperties.getPort();

            SingleServerConfig c = config.useSingleServer()
                                         .setAddress(singleAddr)
                                         .setDatabase(database)
                                         .setClientName(redisProperties.getClientName());
            if (connectTimeout != null) {
                c.setConnectTimeout(connectTimeout);
            }
            if (timeout != null) {
                c.setTimeout(timeout);
            }
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

    private String getPrefix() {
        if (redisProperties.isSsl()) {
            return RedisURI.REDIS_SSL_PROTOCOL;
        } else {
            return RedisURI.REDIS_PROTOCOL;
        }
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
