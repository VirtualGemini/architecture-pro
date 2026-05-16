package com.velox.infrastructure.web.role.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collections;
import java.util.List;

@Schema(description = "角色菜单权限更新命令")
public class RoleMenuPermissionUpdateCommand {

    @Schema(description = "菜单ID集合", example = "[\"01JRF6YQ8J0N7N7N7N7N7N7N7N\"]")
    private List<String> menuIds = Collections.emptyList();

    public List<String> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<String> menuIds) {
        this.menuIds = menuIds;
    }
}
