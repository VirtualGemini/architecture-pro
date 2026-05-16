package com.velox.infrastructure.framework.file.core.client.db;

import com.velox.infrastructure.framework.file.core.client.FileClientConfig;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotEmpty;

public class DBFileClientConfig implements FileClientConfig {

    @NotEmpty(message = "domain 不能为空")
    @URL(message = "domain 必须是 URL 格式")
    private String domain;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
