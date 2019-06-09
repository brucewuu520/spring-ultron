package org.springultron.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springultron.core.Jackson;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 日志AOP切面
 *
 * @Auther: brucewuu
 * @Date: 2019-06-08 11:28
 * @Description:
 */
@Aspect
@Component
@Profile({"dev", "test"})
public class ApiLogAspect {

    private static final Logger log = LoggerFactory.getLogger(ApiLogAspect.class);

    /**
     * 换行符
     */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * 以自定义的 @ApiLog 为切点
     */
    @Pointcut("@annotation(org.springultron.log.ApiLog)")
    public void apiLog() {}

    @Before("apiLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        // 获取 @ApiLog 注解的描述信息
        String methodDescription = getAspectLogDescription(joinPoint);

        // 打印请求相关参数
        log.info("========================================== Start ==========================================");
        // 打印请求 url
        log.info("URL            : {}", request.getRequestURL().toString());
        // 打印描述信息
        log.info("Description    : {}", methodDescription);
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("IP             : {}", request.getRemoteAddr());
        // 打印请求入参
        log.info("Request Args   : {}", Jackson.toJson(joinPoint.getArgs()));
    }

    /**
     * 定义 @Around 环绕，用于何时执行切点
     *
     * @param proceedingJoinPoint 切点
     * @return {Object}
     * @throws Throwable 异常
     */
    @Around("apiLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final long startTime = System.currentTimeMillis();
        // 执行切点
        Object result = proceedingJoinPoint.proceed();
        // 打印出参
        log.info("Response Result  :  {}", Jackson.toJson(result));
        // 执行耗时
        log.info("Time-Consuming   :  {} ms", System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * 在切点之后
     */
    @After("apiLog()")
    public void doAfter() {
        // 接口结束后换行，方便分割查看
        log.info("=========================================== End ===========================================" + LINE_SEPARATOR);
    }

    /**
     * 获取切面注解的描述
     *
     * @param joinPoint 切点
     * @return 描述信息
     * @throws ClassNotFoundException 抛出异常
     */
    private String getAspectLogDescription(JoinPoint joinPoint) throws ClassNotFoundException {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        StringBuilder description = new StringBuilder();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] cls = method.getParameterTypes();
                if (cls.length == arguments.length) {
                    description.append(method.getAnnotation(ApiLog.class).description());
                    break;
                }
            }
        }
        return description.toString();
    }
}
