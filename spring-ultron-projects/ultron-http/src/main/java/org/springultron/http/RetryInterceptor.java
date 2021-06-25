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

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.lang.NonNull;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.io.IOException;
import java.util.function.Predicate;

/**
 * Spring Retry
 *
 * @author brucewuu
 * @date 2021/1/29 下午3:13
 */
public class RetryInterceptor implements Interceptor {

    private final RetryPolicy retryPolicy;

    public RetryInterceptor(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        RetryTemplate retryTemplate = createRetryTemplate(retryPolicy);
        return retryTemplate.execute(retryContext -> {
            Response response = chain.proceed(request);
            Predicate<ResponseSpec> specPredicate = retryPolicy.getRespPredicate();
            if (specPredicate == null) {
                return response;
            }
            ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
            try (HttpResponse httpResponse = HttpResponse.of(response)) {
                if (specPredicate.test(httpResponse)) {
                    throw new IOException("Http Retry ResponsePredicate test Failure.");
                }
            }
            return response.newBuilder().body(responseBody).build();
        });
    }

    private static RetryTemplate createRetryTemplate(RetryPolicy policy) {
        RetryTemplate retryTemplate = new RetryTemplate();
        // 重试策略
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(policy.getMaxAttempts());
        retryTemplate.setRetryPolicy(retryPolicy);
        // 设置间隔策略
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(policy.getSleepMillis());
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }
}
