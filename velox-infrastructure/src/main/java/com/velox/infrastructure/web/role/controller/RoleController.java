package com.velox.infrastructure.web.role.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.velox.common.result.Result;
import com.velox.infrastructure.web.role.dto.RoleListItemDTO;
import com.velox.infrastructure.web.role.dto.RoleMenuPermissionUpdateCommand;
import com.velox.infrastructure.web.role.dto.RoleQuery;
import com.velox.infrastructure.web.role.dto.RoleSaveCommand;
import com.velox.infrastructure.web.role.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.velox.common.result.PageResult;

@Tag(name = "角色管理", description = "角色管理相关接口")
@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "获取角色列表")
    @SaCheckPermission("system:role:query")
    @GetMapping("/list")
    public Result<PageResult<RoleListItemDTO>> list(RoleQuery query) {
        return Result.ok(roleService.list(query));
    }

    @Operation(summary = "新增角色")
    @SaCheckPermission("system:role:create")
    @PostMapping
    public Result<String> create(@Valid @RequestBody RoleSaveCommand command) {
        return Result.ok(roleService.create(command));
    }

    @Operation(summary = "编辑角色")
    @SaCheckPermission("system:role:update")
    @PutMapping("/{roleId}")
    public Result<Boolean> update(@PathVariable("roleId") String roleId, @Valid @RequestBody RoleSaveCommand command) {
        return Result.ok(roleService.update(roleId, command));
    }

    @Operation(summary = "删除角色")
    @SaCheckPermission("system:role:delete")
    @DeleteMapping("/{roleId}")
    public Result<Boolean> delete(@PathVariable("roleId") String roleId) {
        return Result.ok(roleService.delete(roleId));
    }

    @Operation(summary = "获取角色菜单权限")
    @SaCheckPermission("system:role:query")
    @GetMapping("/{roleId}/menu-permissions")
    public Result<List<String>> getRoleMenuPermissions(@PathVariable("roleId") String roleId) {
        return Result.ok(roleService.getRoleMenuPermissions(roleId));
    }

    @Operation(summary = "保存角色菜单权限")
    @SaCheckPermission("system:role:permission")
    @PutMapping("/{roleId}/menu-permissions")
    public Result<Boolean> updateRoleMenuPermissions(
            @PathVariable("roleId") String roleId,
            @Valid @RequestBody(required = false) RoleMenuPermissionUpdateCommand command
    ) {
        return Result.ok(roleService.updateRoleMenuPermissions(roleId, command));
    }
}
