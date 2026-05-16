package com.velox.common.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velox.common.exception.ApiException;
import com.velox.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
public class OperationLogAspect {

    private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);
    private static final String TRACE_ID_KEY = "traceId";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("@annotation(com.velox.common.log.OperationLog)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        OperationLog operationLog = method.getAnnotation(OperationLog.class);

        String operationDesc = operationLog.value();
        OperationType operationType = operationLog.type();
        HttpServletRequest request = getRequest();
        String methodStr = request != null ? request.getMethod() : "UNKNOWN";
        String uri = request != null ? request.getRequestURI() : "UNKNOWN";
        String traceId = MDC.get(TRACE_ID_KEY);

        long startTime = System.currentTimeMillis();
        Object result = null;
        boolean success = true;
        String errorMsg = null;

        try {
            result = point.proceed();
            return result;
        } catch (ApiException exception) {
            success = false;
            errorMsg = exception.getMessage();
            throw exception;
        } catch (Exception exception) {
            success = false;
            errorMsg = exception.getMessage();
            throw exception;
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

            if (operationLog.saveResult() && result instanceof Result<?> response) {
                log.debug("[{}] Response: code={}, msg={}",
                        LogConstants.OPERATION_LOG_MARK, response.getCode(), response.getMsg());
            }
        }
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
}
