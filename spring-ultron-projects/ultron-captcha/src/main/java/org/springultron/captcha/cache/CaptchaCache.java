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

import org.springframework.boot.convert.DurationStyle;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * 验证码缓存
 *
 * @author L.cm brucewuu
 * @date 2021/4/13 下午12:29
 */
public interface CaptchaCache {
    /**
     * 写入缓存
     *
     * @param cacheKey   缓存key
     * @param value      缓存value
     * @param expireTime 缓存过期时间
     */
    default void put(String cacheKey, String value, Duration expireTime) {

    }

    /**
     * 保存缓存
     *
     * @param cacheName 缓存空间名
     * @param cacheKey  缓存key
     * @param value     缓存value
     */
    default void put(String cacheName, String cacheKey, String value) {
        Duration expireTime;
        String[] cacheArray = cacheName.split("#");
        if (cacheArray.length < 2) {
            expireTime = Duration.ofMinutes(5);
        } else {
            expireTime = DurationStyle.detectAndParse(cacheArray[1].trim(), ChronoUnit.SECONDS);
        }
        String key = cacheArray[0].trim() + ":" + cacheKey;
        put(key, value, expireTime);
    }

    /**
     * 获取并删除缓存
     * 验证码不管成功只能验证一次
     *
     * @param cacheKey 缓存key
     * @return 验证码
     */
    @Nullable
    default String getAndRemove(String cacheKey) {
        return null;
    }

    /**
     * 获取并删除缓存，验证码不管成功只能验证一次
     *
     * @param cacheName 缓存空间名
     * @param cacheKey  验证码缓存key
     * @return 验证码
     */
    @Nullable
    default String getAndRemove(String cacheName, String cacheKey) {
        String[] cacheArray = cacheName.split("#");
        String key = cacheArray[0].trim() + ":" + cacheKey;
        return getAndRemove(key);
    }
}
