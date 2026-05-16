package com.velox.infrastructure.config;

import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.stereotype.Component;

/**
 * MySQL 方言
 */
@Component
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
