package org.springultron.lock.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springultron.lock.client.RedisLockClient;
import org.springultron.lock.client.RedisLockClientImpl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.time.Duration;
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
@EnableConfigurationProperties({RedisProperties.class, RedissonProperties.class})
@Import({RedisLockAspect.class})
public class UltronLockAutoConfiguration {
    private static final String REDIS_PROTOCOL_PREFIX = "redis://";
    private static final String REDISS_PROTOCOL_PREFIX = "rediss://";

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
        Method clusterMethod = ReflectionUtils.findMethod(RedisProperties.class, "getCluster");
        Method usernameMethod = ReflectionUtils.findMethod(RedisProperties.class, "getUsername");
        Method timeoutMethod = ReflectionUtils.findMethod(RedisProperties.class, "getTimeout");
        Method connectTimeoutMethod = ReflectionUtils.findMethod(RedisProperties.class, "getConnectTimeout");
        Method clientNameMethod = ReflectionUtils.findMethod(RedisProperties.class, "getClientName");
        Object timeoutValue = ReflectionUtils.invokeMethod(timeoutMethod, redisProperties);

        Integer timeout = null;
        if (timeoutValue instanceof Duration) {
            timeout = (int) ((Duration) timeoutValue).toMillis();
        } else if (timeoutValue != null) {
            timeout = (Integer) timeoutValue;
        }

        Integer connectTimeout = null;
        if (connectTimeoutMethod != null) {
            Object connectTimeoutValue = ReflectionUtils.invokeMethod(connectTimeoutMethod, redisProperties);
            if (connectTimeoutValue != null) {
                connectTimeout = (int) ((Duration) connectTimeoutValue).toMillis();
            }
        } else {
            connectTimeout = timeout;
        }

        String clientName = null;
        if (clientNameMethod != null) {
            clientName = (String) ReflectionUtils.invokeMethod(clientNameMethod, redisProperties);
        }

        String username = null;
        if (usernameMethod != null) {
            username = (String) ReflectionUtils.invokeMethod(usernameMethod, redisProperties);
        }

        if (redissonProperties.getConfig() != null) {
            try {
                config = Config.fromYAML(redissonProperties.getConfig());
            } catch (IOException e) {
                try {
                    config = Config.fromJSON(redissonProperties.getConfig());
                } catch (IOException e1) {
                    e1.addSuppressed(e);
                    throw new IllegalArgumentException("Can't parse config", e1);
                }
            }
        } else if (redissonProperties.getFile() != null) {
            try {
                InputStream is = getConfigStream();
                config = Config.fromYAML(is);
            } catch (IOException e) {
                // trying next format
                try {
                    InputStream is = getConfigStream();
                    config = Config.fromJSON(is);
                } catch (IOException e1) {
                    e1.addSuppressed(e);
                    throw new IllegalArgumentException("Can't parse config", e1);
                }
            }
        } else if (redisProperties.getSentinel() != null) {
            Method nodesMethod = ReflectionUtils.findMethod(RedisProperties.Sentinel.class, "getNodes");
            Object nodesValue = ReflectionUtils.invokeMethod(nodesMethod, redisProperties.getSentinel());

            String[] nodes;
            if (nodesValue instanceof String) {
                nodes = convert(Arrays.asList(((String) nodesValue).split(",")));
            } else {
                nodes = convert((List<String>) nodesValue);
            }

            config = new Config();
            SentinelServersConfig c = config.useSentinelServers()
                                            .setMasterName(redisProperties.getSentinel().getMaster())
                                            .addSentinelAddress(nodes)
                                            .setDatabase(redisProperties.getDatabase())
                                            .setUsername(username)
                                            .setPassword(redisProperties.getPassword())
                                            .setClientName(clientName);
            if (connectTimeout != null) {
                c.setConnectTimeout(connectTimeout);
            }
            if (connectTimeoutMethod != null && timeout != null) {
                c.setTimeout(timeout);
            }
        } else if (clusterMethod != null && ReflectionUtils.invokeMethod(clusterMethod, redisProperties) != null) {
            Object clusterObject = ReflectionUtils.invokeMethod(clusterMethod, redisProperties);
            Method nodesMethod = ReflectionUtils.findMethod(clusterObject.getClass(), "getNodes");
            List<String> nodesObject = (List) ReflectionUtils.invokeMethod(nodesMethod, clusterObject);

            String[] nodes = convert(nodesObject);

            config = new Config();
            ClusterServersConfig c = config.useClusterServers()
                                           .addNodeAddress(nodes)
                                           .setUsername(username)
                                           .setPassword(redisProperties.getPassword())
                                           .setClientName(clientName);
            if (connectTimeout != null) {
                c.setConnectTimeout(connectTimeout);
            }
            if (connectTimeoutMethod != null && timeout != null) {
                c.setTimeout(timeout);
            }
        } else {
            config = new Config();
            String prefix = REDIS_PROTOCOL_PREFIX;
            Method method = ReflectionUtils.findMethod(RedisProperties.class, "isSsl");
            if (method != null && (Boolean) ReflectionUtils.invokeMethod(method, redisProperties)) {
                prefix = REDISS_PROTOCOL_PREFIX;
            }

            SingleServerConfig c = config.useSingleServer()
                                         .setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
                                         .setDatabase(redisProperties.getDatabase())
                                         .setUsername(username)
                                         .setPassword(redisProperties.getPassword())
                                         .setClientName(clientName);
            if (connectTimeout != null) {
                c.setConnectTimeout(connectTimeout);
            }
            if (connectTimeoutMethod != null && timeout != null) {
                c.setTimeout(timeout);
            }
        }
        List<RedissonConfigCustomizer> customizers = redissonConfigCustomizers.getIfAvailable();
        if (customizers != null) {
            for (RedissonConfigCustomizer customizer : customizers) {
                customizer.customize(config);
            }
        }
        return Redisson.create(config);
    }

    private String[] convert(List<String> nodesObject) {
        List<String> nodes = new ArrayList<>(nodesObject.size());
        for (String node : nodesObject) {
            if (!node.startsWith(REDIS_PROTOCOL_PREFIX) && !node.startsWith(REDISS_PROTOCOL_PREFIX)) {
                nodes.add(REDIS_PROTOCOL_PREFIX + node);
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
