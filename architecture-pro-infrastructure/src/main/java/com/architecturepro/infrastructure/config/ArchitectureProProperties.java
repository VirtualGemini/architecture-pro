package com.architecturepro.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 应用属性配置
 */
@Component
@ConfigurationProperties(prefix = "architecture-pro")
public class ArchitectureProProperties {

    /** 应用名称 */
    private String name = "architecture-pro";

    /** 应用版本 */
    private String version = "1.0.0";

    /** API 前缀 */
    private String apiPrefix = "/api";

    /** 是否开启 Swagger */
    private boolean swaggerEnabled = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApiPrefix() {
        return apiPrefix;
    }

    public void setApiPrefix(String apiPrefix) {
        this.apiPrefix = apiPrefix;
    }

    public boolean isSwaggerEnabled() {
        return swaggerEnabled;
    }

    public void setSwaggerEnabled(boolean swaggerEnabled) {
        this.swaggerEnabled = swaggerEnabled;
    }
}
