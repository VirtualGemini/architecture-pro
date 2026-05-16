package com.velox.infrastructure.id;

import cn.hutool.core.lang.UUID;
import org.springframework.stereotype.Component;

@Component
public class BusinessIdGenerator {

    public String nextUserId() {
        return next();
    }

    public String nextRoleId() {
        return next();
    }

    public String nextMenuId() {
        return next();
    }

    public String nextProfileId() {
        return next();
    }

    public String nextUserRoleId() {
        return next();
    }

    public String nextRoleMenuPermissionId() {
        return next();
    }

    public String nextFileConfigId() {
        return next();
    }

    public String nextFileId() {
        return next();
    }

    public String nextFileContentId() {
        return next();
    }

    private String next() {
        return UUID.fastUUID().toString(true);
    }
}
