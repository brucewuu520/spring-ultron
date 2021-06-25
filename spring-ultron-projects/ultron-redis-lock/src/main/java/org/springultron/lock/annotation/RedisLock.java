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

package org.springultron.lock.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁注解
 * 支持的锁的种类有：
 * <p>
 * 1. 公平锁（Fair Lock）
 * 2. 可重入锁（Reentrant Lock）
 * </p>
 *
 * @author L.cm
 * @date 2020/4/27 21:36
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {
    /**
     * 分布式锁key，必须保持唯一性
     *
     * @return key
     */
    @AliasFor("key")
    String value() default "";

    /**
     * 分布式锁key，必须保持唯一性
     *
     * @return key
     */
    @AliasFor("value")
    String key() default "";

    /**
     * 分布式锁参数，可选，支持 spring el # 读取方法参数和 @ 读取 spring bean
     *
     * @return params
     */
    String params() default "";

    /**
     * 等待锁超时时间，默认30
     *
     * @return long
     */
    long waitTime() default 30;

    /**
     * 自动解锁时间，自动解锁时间一定得大于方法执行时间，否则会导致锁提前释放，默认100
     *
     * @return long
     */
    long leaseTime() default 100;

    /**
     * 时间单位，默认:秒
     *
     * @return TimeUnit
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 锁类型，默认:公平锁
     *
     * @return LockType
     */
    LockType type() default LockType.FAIR;
}
