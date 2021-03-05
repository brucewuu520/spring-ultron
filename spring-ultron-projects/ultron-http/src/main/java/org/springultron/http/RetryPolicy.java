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
