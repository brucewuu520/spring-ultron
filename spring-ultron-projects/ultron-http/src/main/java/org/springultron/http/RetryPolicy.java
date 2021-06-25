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

package org.springultron.http;

import org.springframework.lang.Nullable;
import org.springframework.retry.policy.SimpleRetryPolicy;

import java.util.function.Predicate;

/**
 * 重试策略
 *
 * @author brucewuu
 * @date 2021/1/29 下午3:15
 */
public class RetryPolicy {

    public static final RetryPolicy INSTANCE = new RetryPolicy();

    private final int maxAttempts;
    private final long sleepMillis;
    @Nullable
    private final Predicate<ResponseSpec> respPredicate;

    public RetryPolicy() {
        this(null);
    }

    public RetryPolicy(int maxAttempts, long sleepMillis) {
        this(maxAttempts, sleepMillis, null);
    }

    public RetryPolicy(@Nullable Predicate<ResponseSpec> respPredicate) {
        this(SimpleRetryPolicy.DEFAULT_MAX_ATTEMPTS, 0, respPredicate);
    }

    public RetryPolicy(int maxAttempts, long sleepMillis, @Nullable Predicate<ResponseSpec> respPredicate) {
        this.maxAttempts = maxAttempts;
        this.sleepMillis = sleepMillis;
        this.respPredicate = respPredicate;
    }

    public static RetryPolicy getINSTANCE() {
        return INSTANCE;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public long getSleepMillis() {
        return sleepMillis;
    }

    @Nullable
    public Predicate<ResponseSpec> getRespPredicate() {
        return respPredicate;
    }
}
