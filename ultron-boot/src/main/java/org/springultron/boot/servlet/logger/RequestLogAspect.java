package org.springultron.boot.servlet.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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
import org.springultron.core.pool.StringPool;
import org.springultron.core.utils.IpUtils;
import org.springultron.core.utils.Jackson;
import org.springultron.core.utils.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

/**
 * AOP实现请求日志打印（只适用于SERVLET请求）
 *
 * @author brucewuu
 * @date 2019-06-17 18:00
 */
@Order
@Aspect
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(UltronAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(value = LogLevel.ULTRON_LOG_ENABLE, havingValue = "false")
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
    @Pointcut("@annotation(org.springultron.boot.servlet.logger.ApiLog)")
    public void apiLog() {}

    /**
     * 环绕
     */
    @Around("apiLog()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        if (LogLevel.NONE.equals(ultronLogProperties.getLevel())) {
            return point.proceed();
        }
        // 开始打印请求日志
        final HttpServletRequest request = WebUtils.getRequest();
        if (null == request) {
            return point.proceed();
        }
        final long startTime = System.nanoTime();
        // 构建成一条长日志，避免并发下日志错乱
        StringBuilder reqLog = new StringBuilder(300);
        reqLog.append(StringPool.LINE_SEPARATOR);
        reqLog.append("================ Start ================");
        reqLog.append(StringPool.LINE_SEPARATOR);
        // 获取 @ApiLog 注解的描述信息
        String methodDescription = getAspectLogDescription(point);
        // 打印调用 controller 的全路径以及执行方法
        reqLog.append("Class Method   : ")
                .append(point.getSignature().getDeclaringTypeName())
                .append(".")
                .append(point.getSignature().getName());
        reqLog.append(StringPool.LINE_SEPARATOR);
        // 打印请求 url
        reqLog.append("URL            : ").append(request.getRequestURL());
        reqLog.append(StringPool.LINE_SEPARATOR);
        // 打印描述信息
        reqLog.append("Description    : ").append(methodDescription);
        reqLog.append(StringPool.LINE_SEPARATOR);
        // 打印 Http method
        reqLog.append("HTTP Method    : ").append(request.getMethod());
        reqLog.append(StringPool.LINE_SEPARATOR);
        if (LogLevel.HEADERS.equals(ultronLogProperties.getLevel())) {
            // 打印请求头
            Enumeration<String> headers = request.getHeaderNames();
            while (headers.hasMoreElements()) {
                String headerName = headers.nextElement();
                String headerValue = request.getHeader(headerName);
                reqLog.append("HTTP Header    : ").append(headerName).append("=").append(headerValue);
                reqLog.append(StringPool.LINE_SEPARATOR);
            }
        }
        // 打印请求的 IP
        reqLog.append("IP             : ").append(IpUtils.getIP(request));
        reqLog.append(StringPool.LINE_SEPARATOR);
        // 打印请求入参
        reqLog.append("Request Args   : ").append(Jackson.toJson(point.getArgs()));
        reqLog.append(StringPool.LINE_SEPARATOR);
        try {
            // 执行请求获取返回值
            Object result = point.proceed();
            // 打印出参
            reqLog.append("Response Body  : ").append(Jackson.toJson(result));
            return result;
        } finally {
            reqLog.append(StringPool.LINE_SEPARATOR);
            // 执行耗时
            reqLog.append("Time-Consuming : ").append(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)).append(" ms");
            reqLog.append(StringPool.LINE_SEPARATOR);
            reqLog.append("================ End ================");
            reqLog.append(StringPool.LINE_SEPARATOR);
            log.info(reqLog.toString());
        }
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