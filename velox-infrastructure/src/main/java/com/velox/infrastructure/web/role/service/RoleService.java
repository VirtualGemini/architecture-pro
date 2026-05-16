package com.velox.infrastructure.web.role.service;

import com.velox.common.result.PageResult;
import com.velox.infrastructure.web.role.dto.RoleListItemDTO;
import com.velox.infrastructure.web.role.dto.RoleMenuPermissionUpdateCommand;
import com.velox.infrastructure.web.role.dto.RoleQuery;
import com.velox.infrastructure.web.role.dto.RoleSaveCommand;

import java.util.List;

public interface RoleService {

    PageResult<RoleListItemDTO> list(RoleQuery query);

    String create(RoleSaveCommand command);

    Boolean update(String roleId, RoleSaveCommand command);

    Boolean delete(String roleId);

    List<String> getRoleMenuPermissions(String roleId);

    Boolean updateRoleMenuPermissions(String roleId, RoleMenuPermissionUpdateCommand command);
}
