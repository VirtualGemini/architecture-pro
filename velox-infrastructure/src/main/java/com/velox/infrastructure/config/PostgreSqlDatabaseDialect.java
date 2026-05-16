package com.velox.infrastructure.config;

import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.stereotype.Component;

/**
 * PostgreSQL 方言
 */
@Component
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
