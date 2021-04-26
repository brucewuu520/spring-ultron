package org.springultron.lock.config;

import jodd.util.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.Assert;
import org.springultron.core.spel.UltronExpressionEvaluator;
import org.springultron.core.utils.SpringUtils;
import org.springultron.lock.annotation.LockType;
import org.springultron.lock.annotation.RedisLock;
import org.springultron.lock.client.RedisLockClient;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁注解切面
 *
 * @author brucewuu
 * @date 2020/4/28 10:15
 */
@Aspect
public class RedisLockAspect {
    private static final Logger log = LoggerFactory.getLogger(RedisLockAspect.class);
    /**
     * spEl 表达式处理
     */
    private static final UltronExpressionEvaluator EVALUATOR = new UltronExpressionEvaluator();

    private final RedisLockClient redisLockClient;

    @Autowired
    public RedisLockAspect(RedisLockClient redisLockClient) {
        this.redisLockClient = redisLockClient;
    }

    /**
     * AOP 环切 注解 @RedisLock
     */
    @Around("@annotation(redisLock)")
    public Object aroundRedisLock(ProceedingJoinPoint point, RedisLock redisLock) throws Throwable {
        String lockName = redisLock.key();
        log.debug("--lockName: {}", lockName);
        Assert.hasText(lockName, "@RedisLock key must have length; it must not be null or empty");
        // el 表达式
        String lockParams = redisLock.params();
        log.debug("--lockParam: {}", lockParams);
        // 表达式不为空
        String lockKey;
        if (StringUtil.isNotBlank(lockParams)) {
            String evalAsText = evalLockParam(point, lockParams);
            lockKey = lockName + ":" + evalAsText;
        } else {
            lockKey = lockName;
        }
        log.debug("--lockKey: {}", lockKey);
        LockType lockType = redisLock.type();
        log.debug("--lockType: {}", lockType);
        long waitTime = redisLock.waitTime();
        long leaseTime = redisLock.leaseTime();
        TimeUnit timeUnit = redisLock.timeUnit();
        return redisLockClient.lock(lockKey, lockType, waitTime, leaseTime, timeUnit, point::proceed);
    }

    /**
     * 计算参数表达式
     *
     * @param point     ProceedingJoinPoint
     * @param lockParam lockParam
     * @return 结果
     */
    private String evalLockParam(ProceedingJoinPoint point, String lockParam) {
        MethodSignature ms = (MethodSignature) point.getSignature();
        Method method = ms.getMethod();
        Object[] args = point.getArgs();
        Object target = point.getTarget();
        Class<?> targetClass = target.getClass();
        EvaluationContext context = EVALUATOR.createContext(method, args, target, targetClass, SpringUtils.getContext());
        AnnotatedElementKey elementKey = new AnnotatedElementKey(method, targetClass);
        return EVALUATOR.evalAsText(lockParam, elementKey, context);
    }
}
