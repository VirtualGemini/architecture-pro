package com.architecturepro.common.log;

/**
 * 操作类型枚举
 */
public enum OperationType {

    /** 查询 */
    QUERY,
    /** 新增 */
    CREATE,
    /** 修改 */
    UPDATE,
    /** 删除 */
    DELETE,
    /** 导出 */
    EXPORT,
    /** 导入 */
    IMPORT,
    /** 其他 */
    OTHER
}
