package org.springultron.job;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * xxl-job config
 *
 * @author brucewuu
 * @date 2021/4/7 下午6:33
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "xxl.job", value = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(XxlJobProperties.class)
public class XxlJobAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobProperties properties, Environment environment) {
        System.out.println(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobExecutor = new XxlJobSpringExecutor();
        XxlJobProperties.XxlJobAdminProps admin = properties.getAdmin();
        xxlJobExecutor.setAdminAddresses(admin.getAddress());
        xxlJobExecutor.setAccessToken(admin.getAccessToken());
        XxlJobProperties.XxlJobExecutorProps executor = properties.getExecutor();
        xxlJobExecutor.setAppname(getAppName(executor.getAppName(), environment));
        xxlJobExecutor.setAddress(executor.getAddress());
        xxlJobExecutor.setIp(executor.getIp());
        xxlJobExecutor.setPort(executor.getPort());
        xxlJobExecutor.setLogPath(getLogPath(executor.getLogPath(), environment));
        xxlJobExecutor.setLogRetentionDays(executor.getLogRetentionDays());
        return xxlJobExecutor;
    }

    private static String getAppName(String appName, Environment environment) {
        if (StringUtils.hasText(appName)) {
            return appName;
        }
        return environment.getProperty("spring.application.name", "");
    }

    private static String getLogPath(String logPath, Environment environment) {
        if (StringUtils.hasText(logPath)) {
            return logPath;
        }
        return environment.getProperty("logging.file.path", "/var/log").concat("/xxl-job");
    }
}
