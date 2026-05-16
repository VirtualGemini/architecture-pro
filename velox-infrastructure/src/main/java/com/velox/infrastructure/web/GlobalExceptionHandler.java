package com.velox.infrastructure.web;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.exception.SaTokenException;
import com.velox.common.exception.*;
import com.velox.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * <p>
 * 统一拦截并处理所有 Controller 层抛出的异常，
 * 将异常转换为标准 Result 响应格式
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理认证异常
     */
    @ExceptionHandler(SaTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleSaTokenException(SaTokenException e, HttpServletRequest request) {
        log.warn("Unauthorized [{}] {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return Result.fail(ClientErrorCode.UNAUTHORIZED);
    }

    /**
     * 处理权限不足异常
     */
    @ExceptionHandler({NotPermissionException.class, NotRoleException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Void> handleForbiddenException(SaTokenException e, HttpServletRequest request) {
        log.warn("Forbidden [{}] {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return Result.fail(ClientErrorCode.FORBIDDEN);
    }

    /**
     * 处理业务异常 ApiException
     */
    @ExceptionHandler(ApiException.class)
    public Result<Void> handleApiException(ApiException e, HttpServletRequest request) {
        log.warn("Business exception [{}] {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        if (e.getPayload() != null) {
            log.warn("Exception payload: {}", e.getPayload());
        }
        return Result.fail(e.getErrorCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常 @Valid/@Validated
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("Validation error: {}", message);
        return Result.fail(ClientErrorCode.VALIDATION_ERROR, message);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(BindException e) {
        String message = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("Bind error: {}", message);
        return Result.fail(ClientErrorCode.VALIDATION_ERROR, message);
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMissingParamException(MissingServletRequestParameterException e) {
        log.warn("Missing parameter: {}", e.getParameterName());
        return Result.fail(ClientErrorCode.BAD_REQUEST, "缺少请求参数: " + e.getParameterName());
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Void> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        log.warn("Method not allowed: {} {}", e.getMethod(), e.getMessage());
        return Result.fail(ClientErrorCode.METHOD_NOT_ALLOWED);
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleNotFoundException(NoHandlerFoundException e) {
        log.warn("Not found: {} {}", e.getHttpMethod(), e.getRequestURL());
        return Result.fail(ClientErrorCode.NOT_FOUND);
    }

    /**
     * 处理 Spring Boot 3 静态资源风格的 404 异常
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        log.warn("Not found: {} {}", request.getMethod(), request.getRequestURI());
        return Result.fail(ClientErrorCode.NOT_FOUND);
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal argument: {}", e.getMessage());
        return Result.fail(ClientErrorCode.BAD_REQUEST, e.getMessage());
    }

    /**
     * 处理 S3 存储异常，根据错误码返回友好提示
     */
    @ExceptionHandler(S3Exception.class)
    public Result<Void> handleS3Exception(S3Exception e, HttpServletRequest request) {
        String errorCode = e.awsErrorDetails() != null ? e.awsErrorDetails().errorCode() : "Unknown";
        String friendlyMessage = resolveS3ErrorMessage(errorCode, e.getMessage());
        HttpStatus status = HttpStatus.resolve(e.statusCode());
        if (status == null) {
            status = HttpStatus.BAD_GATEWAY;
        }
        log.warn("S3 error [{}] {}: code={}, status={}, message={}",
                request.getMethod(), request.getRequestURI(), errorCode, e.statusCode(), e.getMessage());
        return Result.fail(BusinessErrorCode.FILE_STORAGE_ERROR, friendlyMessage);
    }

    /**
     * 兜底异常处理 - 所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("Unexpected error [{}{}]: {}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        return Result.fail(InternalErrorCode.INTERNAL_ERROR);
    }

    private String resolveS3ErrorMessage(String errorCode, String originalMessage) {
        return switch (errorCode) {
            case "AccessDenied" -> "S3 存储访问被拒绝，请检查 Access Key 和权限配置";
            case "UserDisable" -> "S3 存储账号已被禁用";
            case "InvalidAccessKeyId" -> "S3 存储 Access Key 无效，请检查配置";
            case "SignatureDoesNotMatch" -> "S3 存储签名不匹配，请检查 Access Secret 是否正确";
            case "NoSuchBucket" -> "S3 存储 Bucket 不存在，请检查 Bucket 配置";
            case "NoSuchKey" -> "S3 存储文件不存在";
            case "BucketAlreadyExists" -> "S3 存储 Bucket 已存在";
            case "InvalidBucketName" -> "S3 存储 Bucket 名称不合法";
            case "RequestTimeTooSkewed" -> "S3 请求时间偏差过大，请检查服务器时间是否正确";
            case "ServiceUnavailable" -> "S3 存储服务暂时不可用，请稍后重试";
            case "SlowDown" -> "S3 存储请求过于频繁，请稍后重试";
            case "InternalError" -> "S3 存储服务内部错误，请稍后重试";
            default -> "S3 存储异常: " + (originalMessage != null ? originalMessage : errorCode);
        };
    }
}
