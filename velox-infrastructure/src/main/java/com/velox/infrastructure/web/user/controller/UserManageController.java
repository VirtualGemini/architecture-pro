package com.velox.infrastructure.web.user.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.velox.common.result.Result;
import com.velox.infrastructure.web.user.dto.UserListItemDTO;
import com.velox.infrastructure.web.user.dto.UserQuery;
import com.velox.infrastructure.web.user.dto.UserSaveCommand;
import com.velox.infrastructure.web.user.service.UserManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.velox.common.result.PageResult;

@Tag(name = "用户管理", description = "用户管理相关接口")
@RestController
@RequestMapping("/user")
public class UserManageController {

    private final UserManageService userManageService;

    public UserManageController(UserManageService userManageService) {
        this.userManageService = userManageService;
    }

    @Operation(summary = "获取用户列表")
    @SaCheckPermission("system:user:query")
    @GetMapping("/list")
    public Result<PageResult<UserListItemDTO>> list(UserQuery query) {
        return Result.ok(userManageService.list(query));
    }

    @Operation(summary = "新增用户")
    @SaCheckPermission("system:user:create")
    @PostMapping
    public Result<String> create(@Valid @RequestBody UserSaveCommand command) {
        return Result.ok(userManageService.create(command));
    }

    @Operation(summary = "编辑用户")
    @SaCheckPermission("system:user:update")
    @PutMapping("/{userId}")
    public Result<Boolean> update(@PathVariable("userId") String userId, @Valid @RequestBody UserSaveCommand command) {
        return Result.ok(userManageService.update(userId, command));
    }

    @Operation(summary = "删除用户")
    @SaCheckPermission("system:user:delete")
    @DeleteMapping("/{userId}")
    public Result<Boolean> delete(@PathVariable("userId") String userId) {
        return Result.ok(userManageService.delete(userId));
    }
}
