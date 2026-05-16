package com.velox.infrastructure.web.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "管理后台 - 文件创建 Request VO")
public class FileCreateReqVO {

    @Schema(description = "文件配置编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "文件配置编号不能为空")
    private String configId;

    @Schema(description = "文件路径", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "文件路径不能为空")
    private String path;

    @Schema(description = "文件 URL")
    private String url;

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
