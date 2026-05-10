package com.architecturepro.infrastructure.web.role.service;

import com.architecturepro.common.result.PageResult;
import com.architecturepro.infrastructure.web.role.dto.RoleListItemDTO;
import com.architecturepro.infrastructure.web.role.dto.RoleMenuPermissionUpdateCommand;
import com.architecturepro.infrastructure.web.role.dto.RoleQuery;
import com.architecturepro.infrastructure.web.role.dto.RoleSaveCommand;

import java.util.List;

public interface RoleService {

    PageResult<RoleListItemDTO> list(RoleQuery query);

    String create(RoleSaveCommand command);

    Boolean update(String roleId, RoleSaveCommand command);

    Boolean delete(String roleId);

    List<String> getRoleMenuPermissions(String roleId);

    Boolean updateRoleMenuPermissions(String roleId, RoleMenuPermissionUpdateCommand command);
}
