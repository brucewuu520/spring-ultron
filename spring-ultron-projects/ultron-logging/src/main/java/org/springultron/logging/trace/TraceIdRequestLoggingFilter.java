package org.springultron.logging.trace;

import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * 链路追踪日志过滤器配置
 *
 * @author brucewuu
 * @date 2021/4/17 下午4:53
 */
public class TraceIdRequestLoggingFilter extends AbstractRequestLoggingFilter {

    @Override
    protected void beforeRequest(@NonNull HttpServletRequest request, @NonNull String message) {
        String traceId = request.getHeader(TraceIdContext.TRACE_ID);
        if (!StringUtils.hasText(traceId)) {
            traceId = TraceIdContext.generateTraceId();
        }
        TraceIdContext.setTraceId(traceId);
    }

    @Override
    protected void afterRequest(@NonNull HttpServletRequest request, @NonNull String message) {
        TraceIdContext.removeTraceId();
    }
}
