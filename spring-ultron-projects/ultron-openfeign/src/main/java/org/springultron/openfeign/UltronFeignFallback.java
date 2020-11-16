package org.springultron.openfeign;

import com.fasterxml.jackson.databind.JsonNode;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springultron.core.jackson.Jackson;
import org.springultron.core.result.ApiResult;
import org.springultron.core.result.ResultCode;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * 默认Feign Fallback
 *
 * @author brucewuu
 * @date 2020/7/26 19:40
 */
public class UltronFeignFallback<T> implements MethodInterceptor {
    private static final Logger log = LoggerFactory.getLogger(UltronFeignFallback.class);

    private final Class<T> targetType;
    private final String targetName;
    private final Throwable cause;

    public UltronFeignFallback(Class<T> targetType, String targetName, Throwable cause) {
        this.targetType = targetType;
        this.targetName = targetName;
        this.cause = cause;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        String errorMessage = cause.getMessage();
        log.error("UltronFeignFallback:[{}.{}] serviceId:[{}] message:[{}]", targetType.getName(), method.getName(), targetName, errorMessage);
        Class<?> returnType = method.getReturnType();
        // 集合类型反馈空集合
        if (List.class == returnType || Collection.class == returnType) {
            return Collections.emptyList();
        }
        if (Set.class == returnType) {
            return Collections.emptySet();
        }
        if (Map.class == returnType) {
            return Collections.emptyMap();
        }
        // 暂时不支持 Reactor，RxJava，异步等，返回值不是 ApiResult.class，直接返回 null。
        if (ApiResult.class != returnType) {
            return null;
        }
        // 非 FeignException，直接返回【100009】请求被拒绝
        if (!(cause instanceof FeignException)) {
            return ApiResult.fail(ResultCode.REQUEST_REJECT.getCode(), errorMessage);
        }
        FeignException exception = (FeignException) cause;
        Optional<ByteBuffer> byteBuffer = exception.responseBody();
        // 如果返回的数据为空
        if (!byteBuffer.isPresent()) {
            return ApiResult.fail(ResultCode.REQUEST_REJECT.getCode(), errorMessage);
        }
        // 转换成 jsonNode 读取，因为直接转换，可能 对方放回的并 不是 ApiResult 的格式。
        JsonNode resultNode = Jackson.readTree(byteBuffer.get().array());
        // 判断是否 ApiResult 格式 返回体
        if (resultNode.has("code")) {
            return Jackson.getInstance().convertValue(resultNode, ApiResult.class);
        }

        return ApiResult.fail(resultNode.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || this.getClass() != obj.getClass()) {
            return false;
        }

        UltronFeignFallback<?> that = (UltronFeignFallback<?>) obj;

        return this.targetType.equals(that.targetType);
    }
}
