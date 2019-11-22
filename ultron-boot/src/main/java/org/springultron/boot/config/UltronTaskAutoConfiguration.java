package org.springultron.boot.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springultron.boot.props.UltronTaskProperties;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务/定时任务使用自定义线程池配置
 * <p>
 * 不使用官方提供的线程池配置 {@link TaskExecutionAutoConfiguration}
 * </p>
 *
 * @author brucewuu
 * @date 2019/11/11 14:52
 */
@Configuration(proxyBeanMethods = false)
public class UltronTaskAutoConfiguration extends AsyncConfigurerSupport {

    private final UltronTaskProperties properties;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public UltronTaskAutoConfiguration(UltronTaskProperties properties) {
        this.properties = properties;
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(properties.getCorePoolSize());
        taskExecutor.setMaxPoolSize(properties.getMaxPoolSize());
        taskExecutor.setQueueCapacity(properties.getQueueCapacity());
        taskExecutor.setKeepAliveSeconds(properties.getKeepAliveSeconds());
        taskExecutor.setThreadNamePrefix(properties.getThreadNamePrefix());
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }

    @Nullable
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    @ConditionalOnMissingBean({SchedulingConfigurer.class, TaskScheduler.class, ScheduledExecutorService.class})
    @Bean
    public SchedulingConfigurer schedulingConfigurer() {
        return scheduledTaskRegistrar -> {
            ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
            taskScheduler.setPoolSize(properties.getCorePoolSize());
            taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            taskScheduler.setThreadNamePrefix("ultron-scheduling-");
            taskScheduler.afterPropertiesSet();
            scheduledTaskRegistrar.setTaskScheduler(taskScheduler);
        };
    }
}