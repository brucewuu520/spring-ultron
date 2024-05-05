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

package org.springultron.redis;

import org.springframework.boot.convert.DurationStyle;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springultron.core.utils.StringUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * Redis Cache扩展扩展cache name
 * 支持 # 号分隔 cache name 和 超时 ttl(默认单位秒)。
 *
 * @author L.cm
 * @author brucewuu
 * @date 2019/11/10 18:05
 */
public class RedisAutoCacheManager extends RedisCacheManager {

    public RedisAutoCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, boolean allowRuntimeCacheCreation, Map<String, RedisCacheConfiguration> initialCacheConfigurations) {
        super(cacheWriter, defaultCacheConfiguration, allowRuntimeCacheCreation, initialCacheConfigurations);
    }

    @NonNull
    @Override
    protected RedisCache createRedisCache(@NonNull String name, @Nullable RedisCacheConfiguration cacheConfig) {
        String cacheName = name;
        if (StringUtils.isNotEmpty(name) && name.contains("#")) {
            String[] array = name.split("#");
            if (array.length > 1) {
                cacheName = array[0].trim();
                // 转换时间，支持时间单位例如：300ms，默认单位秒
                Duration ttl = DurationStyle.detectAndParse(array[1].trim(), ChronoUnit.SECONDS);
                if (cacheConfig != null && ttl.compareTo(cacheConfig.getTtlFunction().getTimeToLive(Object.class, null)) != 0) {
                    cacheConfig = cacheConfig.entryTtl(ttl);
                }
            }
        }
        return super.createRedisCache(cacheName, cacheConfig);
    }
}