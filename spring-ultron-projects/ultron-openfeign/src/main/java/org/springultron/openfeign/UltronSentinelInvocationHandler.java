package org.springultron.openfeign;

import com.alibaba.cloud.sentinel.feign.SentinelContractHolder;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import feign.*;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springultron.core.result.ApiResult;
import org.springultron.core.result.ResultCode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 支持自动降级注入 重写 {@link com.alibaba.cloud.sentinel.feign.SentinelInvocationHandler}
 *
 * @author brucewuu
 * @date 2020/10/3 上午10:38
 */
public class UltronSentinelInvocationHandler implements InvocationHandler {
    private final Target<?> target;
    private final Map<Method, InvocationHandlerFactory.MethodHandler> dispatch;
    private FallbackFactory<?> fallbackFactory;
    private Map<Method, Method> fallbackMethodMap;

    UltronSentinelInvocationHandler(Target<?> target, Map<Method, InvocationHandlerFactory.MethodHandler> dispatch, FallbackFactory<?> fallbackFactory) {
        this.target = Util.checkNotNull(target, "target");
        this.dispatch = Util.checkNotNull(dispatch, "dispatch");
        this.fallbackFactory = fallbackFactory;
        this.fallbackMethodMap = toFallbackMethod(dispatch);
    }

    UltronSentinelInvocationHandler(Target<?> target, Map<Method, InvocationHandlerFactory.MethodHandler> dispatch) {
        this.target = Util.checkNotNull(target, "target");
        this.dispatch = Util.checkNotNull(dispatch, "dispatch");
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if ("equals".equals(method.getName())) {
            try {
                Object otherHandler = args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
                return this.equals(otherHandler);
            } catch (IllegalArgumentException var21) {
                return false;
            }
        } else if ("hashCode".equals(method.getName())) {
            return this.hashCode();
        } else if ("toString".equals(method.getName())) {
            return this.toString();
        } else {
            InvocationHandlerFactory.MethodHandler methodHandler = this.dispatch.get(method);
            Object result;
            if (!(this.target instanceof Target.HardCodedTarget)) {
                result = methodHandler.invoke(args);
            } else {
                Target.HardCodedTarget<?> hardCodedTarget = (Target.HardCodedTarget<?>) this.target;
                MethodMetadata methodMetadata = SentinelContractHolder.METADATA_MAP.get(hardCodedTarget.type().getName() + Feign.configKey(hardCodedTarget.type(), method));
                if (methodMetadata == null) {
                    result = methodHandler.invoke(args);
                } else {
                    String resourceName = methodMetadata.template().method().toUpperCase() + ":" + hardCodedTarget.url() + methodMetadata.template().path();
                    Entry entry = null;
                    try {
                        ContextUtil.enter(resourceName);
                        entry = SphU.entry(resourceName, EntryType.OUT, 1, args);
                        result = methodHandler.invoke(args);
                    } catch (Throwable ex) {
                        // fallback handle
                        if (!BlockException.isBlockException(ex)) {
                            Tracer.trace(ex);
                        }
                        if (fallbackFactory != null) {
                            try {
                                return this.fallbackMethodMap.get(method).invoke(this.fallbackFactory.create(ex), args);
                            } catch (IllegalAccessException e) {
                                throw new AssertionError(e);
                            } catch (InvocationTargetException e) {
                                throw new AssertionError(e.getCause());
                            }
                        } else {
                            // 若是R类型 执行自动降级返回R
                            if (ApiResult.class == method.getReturnType()) {
                                System.err.println("feign 服务间调用异常: " + ex.getLocalizedMessage());
                                return ApiResult.fail(ResultCode.CALL_ERROR.getCode(), ex.getLocalizedMessage());
                            } else {
                                throw ex;
                            }
                        }
                    } finally {
                        if (entry != null) {
                            entry.exit(1, args);
                        }
                        ContextUtil.exit();
                    }
                }
            }

            return result;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UltronSentinelInvocationHandler) {
            UltronSentinelInvocationHandler other = (UltronSentinelInvocationHandler) obj;
            return target.equals(other.target);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return target.toString();
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

    static Map<Method, Method> toFallbackMethod(Map<Method, InvocationHandlerFactory.MethodHandler> dispatch) {
        Map<Method, Method> result = new LinkedHashMap<>();
        for (Method method : dispatch.keySet()) {
            method.setAccessible(true);
            result.put(method, method);
        }
        return result;
    }
}
