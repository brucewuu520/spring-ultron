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

package org.springultron.captcha.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springultron.captcha.config.CaptchaProperties;

import java.util.Objects;

/**
 * 基于Spring Cache的验证码缓存实现
 *
 * @author L.cm brucewuu
 * @date 2021/4/13 下午1:23
 */
public class SpringCacheCaptchaCache implements CaptchaCache {
    private final Cache captchaCache;

    public SpringCacheCaptchaCache(CaptchaProperties properties, CacheManager cacheManager) {
        String cacheName = properties.getCacheName();
        Cache cache = cacheManager.getCache(cacheName);
        this.captchaCache = Objects.requireNonNull(cache, "captcha spring cache name " + cacheName + " is null.");
    }

    @Override
    public void put(String cacheName, String cacheKey, String value) {
        captchaCache.put(cacheKey, value);
    }

    @Override
    public String getAndRemove(String cacheName, String cacheKey) {
        final String value = captchaCache.get(cacheKey, String.class);
        if (value != null) {
            captchaCache.evict(cacheKey);
        }
        return value;
    }

}
