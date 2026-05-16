package com.velox.common.result;

import com.velox.common.exception.CommonErrorCode;
import com.velox.common.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一响应结果封装
 *
 * @param <T> 数据类型
 */
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 状态码 */
    private int code;

    /** 消息 */
    private String msg;

    /** 数据 */
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private T data;

    public Result() {
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // ==================== Success ====================

    public static <T> Result<T> ok() {
        return new Result<>(CommonErrorCode.SUCCESS.code(), CommonErrorCode.SUCCESS.message(), null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(CommonErrorCode.SUCCESS.code(), CommonErrorCode.SUCCESS.message(), data);
    }

    public static <T> Result<T> ok(String msg, T data) {
        return new Result<>(CommonErrorCode.SUCCESS.code(), msg, data);
    }

    // ==================== Fail ====================

    public static <T> Result<T> fail() {
        return new Result<>(CommonErrorCode.FAIL.code(), CommonErrorCode.FAIL.message(), null);
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<>(CommonErrorCode.FAIL.code(), msg, null);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    public static <T> Result<T> fail(ErrorCode errorCode) {
        return new Result<>(errorCode.code(), errorCode.message(), null);
    }

    public static <T> Result<T> fail(ErrorCode errorCode, String msg) {
        return new Result<>(errorCode.code(), msg, null);
    }

    public static <T> Result<T> fail(ErrorCode errorCode, T data) {
        return new Result<>(errorCode.code(), errorCode.message(), data);
    }

    // ==================== Build ====================

    public static <T> Result<T> build(int code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    // ==================== Helper ====================

    public boolean isSuccess() {
        return this.code == CommonErrorCode.SUCCESS.code();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
