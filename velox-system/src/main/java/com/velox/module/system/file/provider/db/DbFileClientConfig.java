package com.velox.module.system.file.provider.db;

import com.velox.framework.file.spi.client.FileClientConfig;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public class DbFileClientConfig implements FileClientConfig {

    @NotEmpty(message = "domain cannot be blank")
    @URL(message = "domain must be a valid URL")
    private String domain;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
