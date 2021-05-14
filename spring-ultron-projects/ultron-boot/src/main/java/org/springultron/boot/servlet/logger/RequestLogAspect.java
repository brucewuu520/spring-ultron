package org.springultron.boot.servlet.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.InputStreamSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springultron.boot.enums.LogLevel;
import org.springultron.boot.props.UltronLogProperties;
import org.springultron.core.jackson.Jackson;
import org.springultron.core.pool.StringPool;
import org.springultron.core.utils.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;
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
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(value = LogLevel.ULTRON_LOG_ENABLE)
@EnableConfigurationProperties(UltronLogProperties.class)
public class RequestLogAspect {
    private static final Logger log = LoggerFactory.getLogger("RequestLogAspect");

    private final UltronLogProperties ultronLogProperties;

    @Autowired
    public RequestLogAspect(UltronLogProperties ultronLogProperties) {
        this.ultronLogProperties = ultronLogProperties;
    }

    /**
     * 环绕
     */
    @Around("@annotation(apiLog)")
    public Object doAround(ProceedingJoinPoint point, ApiLog apiLog) throws Throwable {
        if (LogLevel.NONE == ultronLogProperties.getLevel() || LogLevel.NONE == apiLog.level()) {
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
        // 打印调用 controller 的全路径以及执行方法
        reqLog.append("Class Method   : ")
                .append(point.getSignature().getDeclaringTypeName())
                .append(".")
                .append(point.getSignature().getName());
        reqLog.append(StringPool.LINE_SEPARATOR);
        // 打印描述信息
        reqLog.append("Description    : ").append(apiLog.description());
        reqLog.append(StringPool.LINE_SEPARATOR);
        // 打印请求 url
        reqLog.append("URL            : ").append(request.getRequestURL());
        reqLog.append(StringPool.LINE_SEPARATOR);
        // 打印 Http method
        reqLog.append("HTTP Method    : ").append(request.getMethod());
        reqLog.append(StringPool.LINE_SEPARATOR);
        if (LogLevel.HEADERS == ultronLogProperties.getLevel() || LogLevel.HEADERS == apiLog.level()) {
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
        reqLog.append("IP             : ").append(WebUtils.getRemoteIP(request));
        reqLog.append(StringPool.LINE_SEPARATOR);
        // 打印请求入参
        this.buildRequestArgs(point, reqLog);
        try {
            // 执行请求获取返回值
            Object result = point.proceed();
            if (LogLevel.BASIC != ultronLogProperties.getLevel() && LogLevel.BASIC != apiLog.level()) {
                // 打印返回值
                reqLog.append("ResponseBody   : ").append(Jackson.toJson(result));
            }
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

    private void buildRequestArgs(ProceedingJoinPoint point, StringBuilder reqLog) {
        Object[] args = point.getArgs();
        int length = ArrayUtils.getLength(args);
        if (length == 0) {
            return;
        }
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();

        // 请求参数处理
        Map<String, Object> paramsMap = Maps.newHashMap(8);

        Object reqBody = null;

        for (int i = 0; i < length; i++) {
            // 读取方法参数
            MethodParameter methodParam = ClassUtils.getMethodParameter(method, i);
            PathVariable pathVariable = methodParam.getParameterAnnotation(PathVariable.class);
            if (pathVariable != null) {
                continue;
            }
            RequestHeader requestHeader = methodParam.getParameterAnnotation(RequestHeader.class);
            if (requestHeader != null) {
                continue;
            }
            RequestBody requestBody = methodParam.getParameterAnnotation(RequestBody.class);
            Object value = args[i];
            if (requestBody != null) {
                reqBody = value;
                continue;
            }
            // 处理 参数
            if (value instanceof HttpServletRequest) {
                paramsMap.putAll(((HttpServletRequest) value).getParameterMap());
                continue;
            } else if (value instanceof WebRequest) {
                paramsMap.putAll(((WebRequest) value).getParameterMap());
                continue;
            } else if (value instanceof HttpServletResponse) {
                continue;
            } else if (value instanceof MultipartFile) {
                MultipartFile multipartFile = (MultipartFile) value;
                String name = multipartFile.getName();
                String fileName = multipartFile.getOriginalFilename();
                paramsMap.put(name, fileName);
                continue;
            }

            RequestParam requestParam = methodParam.getParameterAnnotation(RequestParam.class);
            String parameterName = methodParam.getParameterName();
            if (requestParam != null && StringUtils.isNotBlank(requestParam.value())) {
                parameterName = requestParam.value();
            }
            if (value == null) {
                paramsMap.put(parameterName, null);
            } else if (ClassUtils.isPrimitiveOrWrapper(value.getClass())) {
                paramsMap.put(parameterName, value);
            } else if (value instanceof InputStream) {
                paramsMap.put(parameterName, "InputStream");
            } else if (value instanceof InputStreamSource) {
                paramsMap.put(parameterName, "InputStreamSource");
            } else if (Jackson.canSerialize(value)) {
                // 判断模型能被 json 序列化，则添加
                paramsMap.put(parameterName, Jackson.toJson(value));
            } else {
                paramsMap.put(parameterName, "【注意】不能序列化为json");
            }
        }

        if (reqBody != null) {
            reqLog.append("RequestBody    : ").append(Jackson.toJson(reqBody));
            reqLog.append(StringPool.LINE_SEPARATOR);
        }

        if (!paramsMap.isEmpty()) {
            for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                reqLog.append("Request Args   :").append(entry.getKey()).append("=").append(entry.getValue());
                reqLog.append(StringPool.LINE_SEPARATOR);
            }
        }
    }
}