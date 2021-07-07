package org.springultron.oss.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springultron.oss.http.OssEndpoint;
import org.springultron.oss.service.OssService;

/**
 * @author Nox
 * @Description aws 自动配置类
 * @date 2020/8/20
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({OssProperties.class})
public class OssAutoConfiguration {

	private final OssProperties properties;

	public OssAutoConfiguration(OssProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean(OssService.class)
	@ConditionalOnProperty(name = "ultron.oss.enable", havingValue = "true", matchIfMissing = true)
	public OssService ossTemplate() {
		return new OssService(properties);
	}

	@Bean
	@ConditionalOnWebApplication
	@ConditionalOnProperty(name = "ultron.oss.info", havingValue = "true")
	public OssEndpoint ossEndpoint(OssService service) {
		return new OssEndpoint(service);
	}

}
