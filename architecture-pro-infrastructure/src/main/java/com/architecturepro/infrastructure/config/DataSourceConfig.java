package com.architecturepro.infrastructure.config;

import com.architecturepro.infrastructure.config.ArchitectureProDataSourceProperties.DatabaseConnectionProperties;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 数据源装配
 */
@Configuration
@EnableConfigurationProperties(ArchitectureProDataSourceProperties.class)
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(ArchitectureProDataSourceProperties properties,
                                 DatabaseDialectRegistry dialectRegistry) {
        String activeType = properties.getType();
        DatabaseDialect dialect = dialectRegistry.getRequiredDialect(activeType);
        DatabaseConnectionProperties activeConfig = properties.getActiveConfig();

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(activeConfig.getResolvedDriverClassName(dialect));
        dataSource.setJdbcUrl(activeConfig.getUrl());
        dataSource.setUsername(activeConfig.getUsername());
        dataSource.setPassword(activeConfig.getPassword());
        dataSource.setPoolName("architecture-pro-" + dialect.getType());
        activeConfig.getDataSourceProperties().forEach(dataSource::addDataSourceProperty);
        return dataSource;
    }
}
