package com.velox.infrastructure.config;

import com.velox.infrastructure.config.VeloxDataSourceProperties.DatabaseConnectionProperties;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 数据源装配
 */
@Configuration
@EnableConfigurationProperties(VeloxDataSourceProperties.class)
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(VeloxDataSourceProperties properties,
                                 DatabaseDialectRegistry dialectRegistry) {
        String activeType = properties.getType();
        DatabaseDialect dialect = dialectRegistry.getRequiredDialect(activeType);
        DatabaseConnectionProperties activeConfig = properties.getActiveConfig();

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(activeConfig.getResolvedDriverClassName(dialect));
        dataSource.setJdbcUrl(activeConfig.getUrl());
        dataSource.setUsername(activeConfig.getUsername());
        dataSource.setPassword(activeConfig.getPassword());
        dataSource.setPoolName("velox-" + dialect.getType());
        activeConfig.getDataSourceProperties().forEach(dataSource::addDataSourceProperty);
        return dataSource;
    }
}
