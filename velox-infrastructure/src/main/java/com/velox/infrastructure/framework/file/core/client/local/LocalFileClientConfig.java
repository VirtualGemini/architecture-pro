package com.velox.infrastructure.framework.file.core.client.local;

import com.velox.infrastructure.framework.file.core.client.FileClientConfig;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotEmpty;

public class LocalFileClientConfig implements FileClientConfig {

    @NotEmpty(message = "基础路径不能为空")
    private String basePath;

    @NotEmpty(message = "domain 不能为空")
    @URL(message = "domain 必须是 URL 格式")
    private String domain;

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
