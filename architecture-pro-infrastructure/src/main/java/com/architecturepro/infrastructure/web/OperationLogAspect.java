package com.architecturepro.infrastructure.web;

import com.architecturepro.common.exception.ApiException;
import com.architecturepro.common.result.Result;
import com.architecturepro.common.log.LogConstants;
import com.architecturepro.common.log.OperationLog;
import com.architecturepro.common.log.OperationType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * 操作日志切面
 * <p>
 * 拦截标注了 @OperationLog 的方法，自动记录操作日志
 */
@Aspect
@Component
public class OperationLogAspect {

    private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("@annotation(com.architecturepro.common.log.OperationLog)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        OperationLog operationLog = method.getAnnotation(OperationLog.class);

        String operationDesc = operationLog.value();
        OperationType operationType = operationLog.type();

        // 获取请求信息
        HttpServletRequest request = getRequest();
        String methodStr = request != null ? request.getMethod() : "UNKNOWN";
        String uri = request != null ? request.getRequestURI() : "UNKNOWN";
        String traceId = LogConstants.TRACE_ID;

        long startTime = System.currentTimeMillis();
        Object result = null;
        boolean success = true;
        String errorMsg = null;

        try {
            result = point.proceed();
            return result;
        } catch (ApiException e) {
            success = false;
            errorMsg = e.getMessage();
            throw e;
        } catch (Exception e) {
            success = false;
            errorMsg = e.getMessage();
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            log.info("[{}] {} | type={} | method={} | uri={} | traceId={} | success={} | duration={}ms{}",
                    LogConstants.OPERATION_LOG_MARK,
                    operationDesc,
                    operationType,
                    methodStr,
                    uri,
                    traceId,
                    success,
                    duration,
                    errorMsg != null ? " | error=" + errorMsg : "");

            if (operationLog.saveResult() && result instanceof Result<?>) {
                Result<?> r = (Result<?>) result;
                log.debug("[{}] Response: code={}, msg={}", LogConstants.OPERATION_LOG_MARK, r.getCode(), r.getMsg());
            }
        }
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
}
