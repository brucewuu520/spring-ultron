package org.springultron.boot.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.task.ThreadPoolTaskExecutorCustomizer;
import org.springframework.boot.task.ThreadPoolTaskSchedulerCustomizer;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务/定时任务使用自定义线程池配置
 * <p>
 * 官方提供的线程池配置 {@link TaskExecutionAutoConfiguration} {@link TaskSchedulingAutoConfiguration}
 * </p>
 *
 * @author brucewuu
 * @date 2024/05/03 17:39
 */
@AutoConfiguration(before = {TaskExecutionAutoConfiguration.class, TaskSchedulingAutoConfiguration.class})
public class UltronTaskAutoConfiguration {

    @Bean
    public ThreadPoolTaskExecutorCustomizer threadPoolTaskExecutorCustomizer() {
        return builder -> {
            builder.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        };
    }

    @Bean
    public ThreadPoolTaskSchedulerCustomizer threadPoolTaskSchedulerCustomizer() {
        return builder -> {
            builder.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        };
    }

}