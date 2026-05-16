package com.velox.infrastructure.web.user.service;

import com.velox.common.result.PageResult;
import com.velox.infrastructure.web.user.dto.UserListItemDTO;
import com.velox.infrastructure.web.user.dto.UserQuery;
import com.velox.infrastructure.web.user.dto.UserSaveCommand;

public interface UserManageService {

    PageResult<UserListItemDTO> list(UserQuery query);

    String create(UserSaveCommand command);

    Boolean update(String userId, UserSaveCommand command);

    Boolean delete(String userId);
}
