package com.velox.framework.persistence.autoconfigure;

import com.velox.framework.persistence.config.DatabaseDialect;
import com.velox.framework.persistence.config.DatabaseDialectRegistry;
import com.velox.framework.persistence.config.VeloxDataSourceProperties;
import com.velox.framework.persistence.config.VeloxDataSourceProperties.DatabaseConnectionProperties;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.List;

/**
 * 数据源自动装配
 */
@AutoConfiguration
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(VeloxDataSourceProperties.class)
public class VeloxDataSourceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DatabaseDialectRegistry databaseDialectRegistry(List<DatabaseDialect> dialects) {
        return new DatabaseDialectRegistry(dialects);
    }

    @Bean
    @ConditionalOnMissingBean(DataSource.class)
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
