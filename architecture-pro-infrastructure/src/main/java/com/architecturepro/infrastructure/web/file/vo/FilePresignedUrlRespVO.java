package com.architecturepro.infrastructure.web.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "管理后台 - 文件预签名 URL Response VO")
public class FilePresignedUrlRespVO {

    @Schema(description = "配置编号")
    private String configId;

    @Schema(description = "上传 URL")
    private String uploadUrl;

    @Schema(description = "文件路径")
    private String path;

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
