package com.elk.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import static com.elk.config.CommonConst.SPAN_ID;
import static com.elk.config.CommonConst.TRACE_ID;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContextHolder {

    public static void init(String traceId, String spanId) {
        MDC.put(TRACE_ID, traceId);
        MDC.put(SPAN_ID, spanId);
        // MDC.put(TARGET, target);
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    public static String getSpanId() {
        return MDC.get(SPAN_ID);
    }

    public static void clear() {
        MDC.clear();
    }
}
