package org.springultron.cloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.lang.annotation.*;

/**
 * Cloud启动注解配置
 *
 * @author brucewuu
 * @date 2021/4/7 上午11:05
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableDiscoveryClient
@SpringBootApplication
public @interface UltronCloudApplication {

}
