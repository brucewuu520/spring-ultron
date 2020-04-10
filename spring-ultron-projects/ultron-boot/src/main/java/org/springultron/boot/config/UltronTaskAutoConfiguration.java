package org.springultron.boot.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springultron.boot.props.UltronTaskProperties;

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
@AutoConfigureBefore({TaskExecutionAutoConfiguration.class})
public class UltronTaskAutoConfiguration {

    private final UltronTaskProperties properties;

    @Autowired
    public UltronTaskAutoConfiguration(UltronTaskProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskExecutorBuilder taskExecutorBuilder(ObjectProvider<TaskDecorator> taskDecorator) {
        TaskExecutorBuilder builder = new TaskExecutorBuilder();
        builder = builder.corePoolSize(properties.getCorePoolSize());
        builder = builder.maxPoolSize(properties.getMaxPoolSize());
        builder = builder.queueCapacity(properties.getQueueCapacity());
        builder = builder.keepAlive(properties.getKeepAlive());
        builder = builder.threadNamePrefix(properties.getThreadNamePrefix() + "-task-");
        builder = builder.taskDecorator(taskDecorator.getIfUnique());
        builder = builder.customizers(customizer -> customizer.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()));
        return builder;
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskSchedulerBuilder taskSchedulerBuilder() {
        TaskSchedulerBuilder builder = new TaskSchedulerBuilder();
        builder = builder.poolSize(properties.getCorePoolSize());
        builder = builder.threadNamePrefix(properties.getThreadNamePrefix() + "-scheduling-");
        builder = builder.customizers(customizer -> customizer.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()));
        return builder;
    }

//    @ConditionalOnBean(name = {"org.springframework.context.annotation.internalScheduledAnnotationProcessor"})
//    @ConditionalOnMissingBean({SchedulingConfigurer.class, TaskScheduler.class, ScheduledExecutorService.class})
//    @Bean
//    public SchedulingConfigurer schedulingConfigurer() {
//        System.err.println("---schedulingConfigurer---");
//        return scheduledTaskRegistrar -> {
//            ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
//            taskScheduler.setPoolSize(properties.getCorePoolSize());
//            taskScheduler.setThreadNamePrefix(properties.getThreadNamePrefix() + "-scheduling-");
//            taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//            taskScheduler.initialize();
//            scheduledTaskRegistrar.setTaskScheduler(taskScheduler);
//        };
//    }
}