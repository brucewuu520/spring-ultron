package org.springultron.boot.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springultron.boot.props.UltronAsyncProperties;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * spring boot异步任务线程池配置
 *
 * @author brucewuu
 * @date 2019/11/11 14:52
 */
@Configuration(proxyBeanMethods = false)
public class UltronExecutorConfiguration extends AsyncConfigurerSupport {

    private final UltronAsyncProperties properties;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public UltronExecutorConfiguration(UltronAsyncProperties properties) {
        this.properties = properties;
    }

    @Bean
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(properties.getCorePoolSize());
        taskExecutor.setMaxPoolSize(properties.getMaxPoolSize());
        taskExecutor.setQueueCapacity(properties.getQueueCapacity());
        taskExecutor.setKeepAliveSeconds(properties.getKeepAliveSeconds());
        taskExecutor.setThreadNamePrefix(properties.getThreadNamePrefix());
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return taskExecutor;
    }

    @Nullable
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}