package com.velox.infrastructure.framework.file.core.client.sftp;

import com.velox.infrastructure.framework.file.core.client.FileClientConfig;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public class SftpFileClientConfig implements FileClientConfig {

    @NotEmpty(message = "基础路径不能为空")
    private String basePath;

    @URL(message = "domain 必须是 URL 格式")
    private String domain;

    @NotEmpty(message = "host 不能为空")
    private String host;

    @NotNull(message = "port 不能为空")
    private Integer port;

    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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
}
