package org.springultron.logging.trace;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 链路追踪context
 *
 * @author brucewuu
 * @date 2021/4/17 下午4:44
 */
public class TraceIdContext {
    public static final String TRACE_ID = "traceId";

    /**
     * 生成 追踪ID
     *
     * @return 追踪ID
     */
    public static String generateTraceId() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextLong(), random.nextLong()).toString().replace("-", "");
    }

    /**
     * 写入最终ID
     *
     * @param traceId 追踪ID
     */
    public static void setTraceId(String traceId) {
        if (StringUtils.hasText(traceId)) {
            MDC.put(TRACE_ID, traceId);
        }
    }

    /**
     * 获取追踪ID
     *
     * @return 追踪ID
     */
    public static String getTraceId() {
        String traceId = MDC.get(TRACE_ID);
        return traceId == null ? "" : traceId;
    }

    /**
     * 移除追踪ID
     */
    public static void removeTraceId() {
        MDC.remove(TRACE_ID);
    }

    /**
     * 清空所有
     */
    public static void clearTraceId() {
        MDC.clear();
    }
}
