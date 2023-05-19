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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springultron.captcha.core.CaptchaType;

/**
 * 图形验证码配置项
 *
 * @author L.cm
 * @author brucewuu
 * @date 2021/4/13 下午2:15
 */
@ConfigurationProperties(prefix = "captcha")
public class CaptchaProperties {
    /**
     * 验证码类型，默认：随机数
     */
    private CaptchaType captchaType = CaptchaType.RANDOM;

    /**
     * 验证码cache名，默认：captcha:cache#5m，配合 ultron-redis 5分钟缓存
     */
    private String cacheName = "captcha:cache#5m";

    public CaptchaType getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(CaptchaType captchaType) {
        this.captchaType = captchaType;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }
}
