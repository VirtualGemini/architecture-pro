package com.velox.framework.persistence.autoconfigure;

import com.velox.framework.persistence.config.DatabaseDialect;
import com.velox.framework.persistence.config.PostgreSqlDatabaseDialect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * PostgreSQL 方言自动装配
 */
@AutoConfiguration
@ConditionalOnClass(name = "org.postgresql.Driver")
public class VeloxPostgreSqlAutoConfiguration {

    @Bean
    public DatabaseDialect postgreSqlDatabaseDialect() {
        return new PostgreSqlDatabaseDialect();
    }
}
