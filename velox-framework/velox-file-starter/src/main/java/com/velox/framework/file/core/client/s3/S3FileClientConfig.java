package com.velox.framework.file.core.client.s3;

import cn.hutool.core.util.StrUtil;
import com.velox.framework.file.core.client.FileClientConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public class S3FileClientConfig implements FileClientConfig {

    public static final String ENDPOINT_QINIU = "qiniucs.com";
    public static final String ENDPOINT_ALIYUN = "aliyuncs.com";
    public static final String ENDPOINT_TENCENT = "myqcloud.com";
    public static final String ENDPOINT_VOLCES = "volces.com";

    @NotNull(message = "endpoint 不能为空")
    private String endpoint;

    @URL(message = "domain 必须是 URL 格式")
    private String domain;

    @NotNull(message = "bucket 不能为空")
    private String bucket;

    private String basePath;

    @NotNull(message = "accessKey 不能为空")
    private String accessKey;

    @NotNull(message = "accessSecret 不能为空")
    private String accessSecret;

    private String sessionToken;

    @NotNull(message = "enablePathStyleAccess 不能为空")
    private Boolean enablePathStyleAccess;

    @NotNull(message = "是否公开访问不能为空")
    private Boolean enablePublicAccess;

    private String region;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Boolean getEnablePathStyleAccess() {
        return enablePathStyleAccess;
    }

    public void setEnablePathStyleAccess(Boolean enablePathStyleAccess) {
        this.enablePathStyleAccess = enablePathStyleAccess;
    }

    public Boolean getEnablePublicAccess() {
        return enablePublicAccess;
    }

    public void setEnablePublicAccess(Boolean enablePublicAccess) {
        this.enablePublicAccess = enablePublicAccess;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @SuppressWarnings("RedundantIfStatement")
    @AssertTrue(message = "domain 不能为空")
    @JsonIgnore
    public boolean isDomainValid() {
        if (StrUtil.contains(endpoint, ENDPOINT_QINIU) && StrUtil.isEmpty(domain)) {
            return false;
        }
        return true;
    }
}
