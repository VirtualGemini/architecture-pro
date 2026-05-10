package com.architecturepro.common.log;

/**
 * 日志常量
 */
public final class LogConstants {

    private LogConstants() {}

    /** MDC 请求追踪 ID Key */
    public static final String TRACE_ID = "traceId";

    /** MDC 用户 ID Key */
    public static final String USER_ID = "userId";

    /** MDC 客户端 IP Key */
    public static final String CLIENT_IP = "clientIp";

    /** 操作日志标记 */
    public static final String OPERATION_LOG_MARK = "OPERATION_LOG";
}
