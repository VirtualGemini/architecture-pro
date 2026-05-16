package com.velox.module.system.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 文件配置创建/修改 Request VO")
public class FileConfigSaveReqVO {

    @Schema(description = "编号")
    private String id;

    @Schema(description = "配置名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "配置名不能为空")
    private String name;

    @Schema(description = "存储器", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "存储器不能为空")
    private Integer storage;

    @Schema(description = "存储配置", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "存储配置不能为空")
    private String config;

    @Schema(description = "是否主配置")
    private Boolean master;

    @Schema(description = "是否启用")
    private Integer enabled;

    @Schema(description = "备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStorage() {
        return storage;
    }

    public void setStorage(Integer storage) {
        this.storage = storage;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Boolean getMaster() {
        return master;
    }

    public void setMaster(Boolean master) {
        this.master = master;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
