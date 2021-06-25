/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springultron.captcha.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springultron.captcha.cache.CaptchaCache;
import org.springultron.captcha.cache.SpringCacheCaptchaCache;
import org.springultron.captcha.service.CaptchaService;
import org.springultron.captcha.service.impl.CaptchaServiceImpl;

/**
 * 验证码自动配置
 *
 * @author L.cm brucewuu
 * @date 2021/4/13 上午11:50
 */
@EnableCaching
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CaptchaProperties.class)
public class CaptchaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CaptchaCache captchaCache(CaptchaProperties properties, CacheManager cacheManager) {
        return new SpringCacheCaptchaCache(properties, cacheManager);
    }

    @Bean
    public CaptchaService captchaService(CaptchaProperties properties, CaptchaCache captchaCache) {
        return new CaptchaServiceImpl(properties, captchaCache);
    }

}
