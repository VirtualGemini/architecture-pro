package com.architecturepro.infrastructure.web.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "管理后台 - 文件分页 Request VO")
public class FilePageReqVO {

    @Schema(description = "文件路径", example = "upload/2024/01")
    private String path;

    @Schema(description = "文件类型", example = "image/jpeg")
    private String type;

    @Schema(description = "页码", example = "1")
    private Integer page = 1;

    @Schema(description = "每页大小", example = "10")
    private Integer size = 10;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
