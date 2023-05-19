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

package org.springultron.captcha.service.impl;

import org.springframework.util.StringUtils;
import org.springultron.captcha.core.Captcha;
import org.springultron.captcha.cache.CaptchaCache;
import org.springultron.captcha.config.CaptchaProperties;
import org.springultron.captcha.service.CaptchaService;

import java.io.OutputStream;

/**
 * 验证码服务
 *
 * @author L.cm brucewuu
 * @date 2021/4/13 下午12:06
 */
public class CaptchaServiceImpl implements CaptchaService {
    private final CaptchaProperties properties;
    private final CaptchaCache captchaCache;
    private final Captcha captcha;

    public CaptchaServiceImpl(CaptchaProperties properties, CaptchaCache captchaCache) {
        this.properties = properties;
        this.captchaCache = captchaCache;
        this.captcha = new Captcha(properties.getCaptchaType());
    }

    @Override
    public void generate(String cacheKey, OutputStream outputStream) {
        String captchaCode = captcha.generate(() -> outputStream);
        captchaCache.put(properties.getCacheName(), cacheKey, captchaCode);
    }

    @Override
    public boolean validate(String cacheKey, String userInputCaptcha) {
        String captchaCode = captchaCache.getAndRemove(properties.getCacheName(), cacheKey);
        if (!StringUtils.hasText(captchaCode)) {
            return false;
        }
        return captcha.validate(captchaCode, userInputCaptcha);
    }
}
