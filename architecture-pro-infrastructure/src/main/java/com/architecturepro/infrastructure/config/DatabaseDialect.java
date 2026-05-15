package com.architecturepro.infrastructure.config;

import com.baomidou.mybatisplus.annotation.DbType;

/**
 * 数据库方言抽象
 */
public interface DatabaseDialect {

    /**
     * 数据库类型编码，例如 mysql / postgresql
     */
    String getType();

    /**
     * 默认 JDBC 驱动类
     */
    String getDefaultDriverClassName();

    /**
     * MyBatis Plus 数据库类型
     */
    DbType getMybatisDbType();
}
