package com.velox.module.system.menu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckLogin;
import com.velox.common.result.Result;
import com.velox.module.system.menu.dto.MenuSaveCommand;
import com.velox.module.system.menu.dto.MenuRouteDTO;
import com.velox.module.system.menu.service.MenuService;
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

import java.util.List;

@Tag(name = "菜单管理", description = "菜单管理相关接口")
@RestController
@RequestMapping("/v3/system/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @Operation(summary = "获取简化菜单列表")
    @SaCheckLogin
    @GetMapping("/simple")
    public Result<List<MenuRouteDTO>> getSimpleMenus() {
        return Result.ok(menuService.getSimpleMenus());
    }

    @Operation(summary = "新增菜单")
    @SaCheckPermission("system:menu:create")
    @PostMapping
    public Result<String> create(@Valid @RequestBody MenuSaveCommand command) {
        return Result.ok(menuService.create(command));
    }

    @Operation(summary = "编辑菜单")
    @SaCheckPermission("system:menu:update")
    @PutMapping("/{menuId}")
    public Result<Boolean> update(@PathVariable("menuId") String menuId, @Valid @RequestBody MenuSaveCommand command) {
        return Result.ok(menuService.update(menuId, command));
    }

    @Operation(summary = "删除菜单")
    @SaCheckPermission("system:menu:delete")
    @DeleteMapping("/{menuId}")
    public Result<Boolean> delete(@PathVariable("menuId") String menuId) {
        return Result.ok(menuService.delete(menuId));
    }

}
