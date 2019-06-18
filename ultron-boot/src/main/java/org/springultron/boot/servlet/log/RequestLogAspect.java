package org.springultron.boot.servlet.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springultron.boot.enums.LogLevel;
import org.springultron.boot.props.UltronRequestLogProperties;
import org.springultron.core.utils.Jackson;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;

/**
 * @Auther: brucewuu
 * @Date: 2019-06-17 18:00
 * @Description:
 */
@Component
@Order
@Aspect
@EnableConfigurationProperties(UltronRequestLogProperties.class)
@ConditionalOnProperty(value = LogLevel.REQ_LOG_ENABLE, havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class RequestLogAspect {

    private static final Logger log = LoggerFactory.getLogger(RequestLogAspect.class);
    /**
     * 换行符
     */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private final UltronRequestLogProperties requestLogProperties;

    @Autowired
    public RequestLogAspect(UltronRequestLogProperties requestLogProperties) {
        this.requestLogProperties = requestLogProperties;
    }

    /**
     * 以自定义 @ApiLog 注解为切点
     */
    @Pointcut("@annotation(org.springultron.boot.servlet.log.ApiLog)")
    public void apiLog() {}

    /**
     * 在切点之前织入
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Before("apiLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        if (LogLevel.NONE.equals(requestLogProperties.getLevel())) {
            return;
        }
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
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
        if (LogLevel.HEADERS.equals(requestLogProperties.getLevel())) {
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

    @Around("apiLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (LogLevel.NONE.equals(requestLogProperties.getLevel())) {
            return proceedingJoinPoint.proceed();
        }
        final long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 打印出参
        log.info("Response Args  : {}", Jackson.toJson(result));
        // 执行耗时
        log.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * 在切点之后织入
     */
    @After("apiLog()")
    public void doAfter() {
        if (LogLevel.NONE.equals(requestLogProperties.getLevel())) {
            return;
        }
        // 接口结束后换行，方便分割查看
        log.info("=========================================== End ===========================================" + LINE_SEPARATOR);
    }

    /**
     * 获取切面注解的描述
     *
     * @param joinPoint 切点
     * @return 描述信息
     * @throws Exception
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
