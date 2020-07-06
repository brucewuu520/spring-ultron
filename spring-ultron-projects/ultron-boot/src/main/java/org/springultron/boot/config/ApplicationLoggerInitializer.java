package org.springultron.boot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.lang.NonNull;

/**
 * APP初始化配置
 * 通过环境变量的形式注入 logging.file 自动维护 Spring Boot Admin Logger Viewer
 *
 * @author brucewuu
 * @date 2020/6/24 14:39
 */
public class ApplicationLoggerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger log = LoggerFactory.getLogger(ApplicationLoggerInitializer.class);

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String appName = environment.getProperty("spring.application.name", "ultron-server");
        log.info("---appName: {}", appName);
        String logFilePath = environment.getProperty("logging.file.path", "logs/" + appName);
        log.info("---logFilePath: {}", logFilePath);
        // spring boot admin 直接加载日志

        System.setProperty("logging.file.name", logFilePath + "/info.log");
    }
}
