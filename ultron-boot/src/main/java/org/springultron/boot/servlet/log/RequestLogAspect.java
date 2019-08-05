package org.springultron.boot.servlet.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springultron.boot.config.UltronAutoConfiguration;
import org.springultron.boot.enums.LogLevel;
import org.springultron.boot.props.UltronLogProperties;
import org.springultron.boot.servlet.WebUtils;
import org.springultron.core.result.Result;
import org.springultron.core.utils.ClassUtils;
import org.springultron.core.utils.Jackson;
import org.springultron.core.utils.Strings;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;

/**
 * AOP实现请求日志打印（只适用于SERVLET请求）
 *
 * @Auther: brucewuu
 * @Date: 2019-06-17 18:00
 * @Description:
 */
@Order
@Aspect
@Configuration
@AutoConfigureAfter(UltronAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(value = LogLevel.ULTRON_LOG_ENABLE, havingValue = "true", matchIfMissing = true)
public class RequestLogAspect {
    private static final Logger log = LoggerFactory.getLogger("RequestLogAspect");

    private final UltronLogProperties ultronLogProperties;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public RequestLogAspect(UltronLogProperties ultronLogProperties) {
        this.ultronLogProperties = ultronLogProperties;
    }

    /**
     * 以自定义注解 @ApiLog 为切点
     */
    @Pointcut("@annotation(org.springultron.boot.servlet.log.ApiLog)")
    public void apiLog() {}

    /**
     * 在切点之前植入
     *
     */
    @Before("apiLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        if (LogLevel.NONE.equals(ultronLogProperties.getLevel())) {
            return;
        }
        // 开始打印请求日志
        HttpServletRequest request = WebUtils.getRequest();
        if (null == request)
            return;
        // 获取 @ApiLog 注解的描述信息
        String methodDescription = getAspectLogDescription(joinPoint);
        // 打印请求相关参数
        log.info("========================================== Start ==========================================");
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求 url
        log.info("URL            : {}", request.getRequestURL().toString());
        // 打印描述信息
        log.info("Description    : {}", methodDescription);
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        if (LogLevel.HEADERS.equals(ultronLogProperties.getLevel())) {
            // 打印请求头
            Enumeration<String> headers = request.getHeaderNames();
            while (headers.hasMoreElements()) {
                String headerName = headers.nextElement();
                String headerValue = request.getHeader(headerName);
                log.info("HTTP Header    : {}={}", headerName, headerValue);
            }
        }
        // 打印请求的 IP
        log.info("IP             : {}", request.getRemoteAddr());
        // 打印请求入参
        log.info("Request Args   : {}", Jackson.toJson(joinPoint.getArgs()));
    }

    /**
     * 环绕
     */
    @Around("apiLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (LogLevel.NONE.equals(ultronLogProperties.getLevel())) {
            return proceedingJoinPoint.proceed();
        }
        final long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        try {
            if (result instanceof Result) {
                // 打印出参
                log.info("Response Body  : {}", Jackson.toJson(result));
            } else {
                if (ClassUtils.isPresent("org.reactivestreams.Publisher", null)) {
                    if (result instanceof Mono) {
                        //noinspection unchecked
                        ((Mono<Object>) result).subscribe(response -> {
                            // 打印出参
                            log.info("Response Body  : {}", Jackson.toJson(response));
                        });
                    } else if (result instanceof Flux) {
                        //noinspection unchecked
                        ((Flux<Object>) result).subscribe(response -> {
                            // 打印出参
                            log.info("Response Body  : {}", Jackson.toJson(response));
                        });
                    }
                } else {
                    // 打印出参
                    log.info("Response Body  : {}", Jackson.toJson(result));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        // 执行耗时
        log.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * 在切点之后植入
     */
    @After("apiLog()")
    public void doAfter() {
        if (LogLevel.NONE.equals(ultronLogProperties.getLevel())) {
            return;
        }
        // 接口结束后换行，方便分割查看
        log.info("=========================================== End ===========================================" + Strings.LINE_SEPARATOR);
    }

    /**
     * 获取切面注解的描述
     *
     * @param joinPoint 切点
     * @return 描述信息
     * @throws ClassNotFoundException 抛出异常
     */
    private static String getAspectLogDescription(JoinPoint joinPoint) throws ClassNotFoundException {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        StringBuilder description = new StringBuilder();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] classes = method.getParameterTypes();
                if (classes.length == arguments.length) {
                    description.append(method.getAnnotation(ApiLog.class).description());
                    break;
                }
            }
        }
        return description.toString();
    }
}
