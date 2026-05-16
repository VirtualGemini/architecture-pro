package com.velox.module.system.auth.config;

import cn.dev33.satoken.stp.StpInterface;
import com.velox.module.system.permission.service.PermissionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VeloxStpInterface implements StpInterface {

    private final PermissionService permissionService;

    public VeloxStpInterface(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        String userId = parseLoginId(loginId);
        if (userId == null || userId.isBlank()) {
            return List.of();
        }
        return permissionService.getUserPermissionMarks(userId);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String userId = parseLoginId(loginId);
        if (userId == null || userId.isBlank()) {
            return List.of();
        }
        return permissionService.getUserRoleCodes(userId);
    }

    private String parseLoginId(Object loginId) {
        if (loginId == null) {
            return null;
        }
        String value = String.valueOf(loginId).trim();
        return value.isEmpty() ? null : value;
    }
}
