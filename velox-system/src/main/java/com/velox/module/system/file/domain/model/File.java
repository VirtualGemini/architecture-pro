package com.velox.module.system.file.domain.model;

import com.velox.domain.model.BaseEntity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Objects;

@TableName(value = "sys_file")
public class File extends BaseEntity {

    private String configId;

    private String name;

    private String path;

    private String url;

    private String type;

    private Long size;

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = normalizeIdentifier(configId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        File file = (File) o;
        return Objects.equals(configId, file.configId) &&
                Objects.equals(name, file.name) &&
                Objects.equals(path, file.path) &&
                Objects.equals(url, file.url) &&
                Objects.equals(type, file.type) &&
                Objects.equals(size, file.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), configId, name, path, url, type, size);
    }
}
