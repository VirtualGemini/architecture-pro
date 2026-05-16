package com.velox.infrastructure.framework.file.core.client.ftp;

import com.velox.infrastructure.framework.file.core.client.FileClientConfig;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;
import org.hibernate.validator.constraints.URL;

import java.util.Arrays;

public class FtpFileClientConfig implements FileClientConfig {

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

    /**
     * 连接模式
     *
     * 使用 {@link  cn.hutool.extra.ftp.FtpMode} 对应的字符串
     */
    @NotEmpty(message = "连接模式不能为空")
    private String mode;

    @AssertTrue(message = "连接模式仅支持 Active 或 Passive")
    public boolean isModeValid() {
        return mode != null && Arrays.stream(cn.hutool.extra.ftp.FtpMode.values())
                .anyMatch(item -> item.name().equals(mode));
    }

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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
