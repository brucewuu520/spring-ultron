package org.springultron.logging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.LogFile;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springultron.logging.utils.LoggingUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * APP初始化配置
 * 通过环境变量的形式注入 logging.file.name 自动维护 Spring Boot Admin Logger Viewer
 *
 * @author brucewuu
 * @date 2020/6/24 14:39
 */
public class LoggingInitializer implements EnvironmentPostProcessor, Ordered {
    public static final String LOGGING_PROPERTY_SOURCE_NAME = "ultron-logging-property-source";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (!environment.containsProperty(LogFile.FILE_NAME_PROPERTY)) {
            // 读取配置文件日志目录
            String logDir = environment.getProperty(LogFile.FILE_PATH_PROPERTY);
            if (null == logDir || "".equals(logDir)) {
                logDir = LoggingUtils.DEFAULT_LOG_DIR + File.separator + LoggingUtils.DEFAULT_APP_NAME;
            }
            String logFileName = logDir + File.separator + LoggingUtils.FILE_INFO_LOG;
            System.out.println(logFileName);
            Map<String, Object> hashMap = new HashMap<>(1);
            hashMap.put(LogFile.FILE_NAME_PROPERTY, logFileName);
            MapPropertySource propertySource = new MapPropertySource(LOGGING_PROPERTY_SOURCE_NAME, hashMap);
            environment.getPropertySources().addLast(propertySource);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
