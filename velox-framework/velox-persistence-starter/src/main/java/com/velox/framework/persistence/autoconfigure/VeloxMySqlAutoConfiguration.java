package com.velox.framework.persistence.autoconfigure;

import com.velox.framework.persistence.config.DatabaseDialect;
import com.velox.framework.persistence.config.MySqlDatabaseDialect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * MySQL 方言自动装配
 */
@AutoConfiguration
@ConditionalOnClass(name = "com.mysql.cj.jdbc.Driver")
public class VeloxMySqlAutoConfiguration {

    @Bean
    public DatabaseDialect mySqlDatabaseDialect() {
        return new MySqlDatabaseDialect();
    }
}
