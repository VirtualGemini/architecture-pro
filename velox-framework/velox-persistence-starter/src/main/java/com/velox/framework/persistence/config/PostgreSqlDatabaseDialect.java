package com.velox.framework.persistence.config;

import com.baomidou.mybatisplus.annotation.DbType;

/**
 * PostgreSQL 方言
 */
public class PostgreSqlDatabaseDialect implements DatabaseDialect {

    @Override
    public String getType() {
        return "postgresql";
    }

    @Override
    public String getDefaultDriverClassName() {
        return "org.postgresql.Driver";
    }

    @Override
    public DbType getMybatisDbType() {
        return DbType.POSTGRE_SQL;
    }
}
