package com.velox.framework.persistence.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 数据源配置
 */
@Validated
@ConfigurationProperties(prefix = "velox.datasource")
public class VeloxDataSourceProperties {

    /**
     * 当前激活的数据源类型，默认推荐 PostgreSQL
     */
    private String type = "postgresql";

    /**
     * 可并存的多套数据库连接配置
     */
    @Valid
    private Map<String, DatabaseConnectionProperties> configs = new LinkedHashMap<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, DatabaseConnectionProperties> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, DatabaseConnectionProperties> configs) {
        this.configs = configs;
    }

    public DatabaseConnectionProperties getActiveConfig() {
        DatabaseConnectionProperties config = configs.get(type);
        if (config == null) {
            throw new IllegalStateException("Missing datasource config for database type: " + type);
        }
        return config;
    }

    @Validated
    public static class DatabaseConnectionProperties {

        /**
         * JDBC 驱动类名；为空时使用数据库类型默认驱动
         */
        private String driverClassName;

        /**
         * JDBC 连接串
         */
        @NotBlank
        private String url;

        /**
         * 数据库用户名
         */
        @NotBlank
        private String username;

        /**
         * 数据库密码
         */
        private String password;

        /**
         * 透传给底层数据源的额外参数，便于未来扩展数据库特性
         */
        private Map<String, String> dataSourceProperties = new LinkedHashMap<>();

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getResolvedDriverClassName(DatabaseDialect dialect) {
            return Objects.requireNonNullElse(driverClassName, dialect.getDefaultDriverClassName());
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Map<String, String> getDataSourceProperties() {
            return dataSourceProperties;
        }

        public void setDataSourceProperties(Map<String, String> dataSourceProperties) {
            this.dataSourceProperties = dataSourceProperties;
        }
    }
}
