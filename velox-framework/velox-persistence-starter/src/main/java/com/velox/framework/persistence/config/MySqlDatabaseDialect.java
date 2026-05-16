package com.velox.framework.persistence.config;

import com.baomidou.mybatisplus.annotation.DbType;

/**
 * MySQL 方言
 */
public class MySqlDatabaseDialect implements DatabaseDialect {

    @Override
    public String getType() {
        return "mysql";
    }

    @Override
    public String getDefaultDriverClassName() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    public DbType getMybatisDbType() {
        return DbType.MYSQL;
    }
}
