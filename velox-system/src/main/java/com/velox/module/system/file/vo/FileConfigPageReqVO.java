package com.velox.module.system.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "管理后台 - 文件配置分页 Request VO")
public class FileConfigPageReqVO {

    @Schema(description = "配置名")
    private String name;

    @Schema(description = "存储器")
    private Integer storage;

    @Schema(description = "页码", example = "1")
    private Integer page = 1;

    @Schema(description = "每页大小", example = "10")
    private Integer size = 10;

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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
