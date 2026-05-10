package com.architecturepro.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("sys_role_menu_permission")
public class RoleMenuPermission extends BaseEntity {

    private String roleId;
    private String menuId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
