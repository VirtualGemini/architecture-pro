package com.architecturepro.infrastructure.log;

import com.architecturepro.common.log.LogConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 请求日志拦截器
 * <p>
 * 记录每个 HTTP 请求的基本信息（方法、URI、耗时、状态码）
 */
@Component
public class RequestLogInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestLogInterceptor.class);
    private static final String START_TIME_ATTR = "requestStartTime";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                              @NonNull Object handler) {
        request.setAttribute(START_TIME_ATTR, System.currentTimeMillis());
        log.info(">>> {} {}", request.getMethod(), request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex) {
        Long startTime = (Long) request.getAttribute(START_TIME_ATTR);
        long duration = startTime != null ? System.currentTimeMillis() - startTime : 0;
        String traceId = MDC.get(LogConstants.TRACE_ID);

        if (ex != null) {
            log.error("<<< {} {} | status={} | duration={}ms | traceId={} | error={}",
                    request.getMethod(), request.getRequestURI(),
                    response.getStatus(), duration, traceId, ex.getMessage());
        } else {
            log.info("<<< {} {} | status={} | duration={}ms | traceId={}",
                    request.getMethod(), response.getStatus(), duration, traceId);
        }
    }
}
