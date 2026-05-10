package com.architecturepro.infrastructure.web.user.service;

import com.architecturepro.common.result.PageResult;
import com.architecturepro.infrastructure.web.user.dto.UserListItemDTO;
import com.architecturepro.infrastructure.web.user.dto.UserQuery;
import com.architecturepro.infrastructure.web.user.dto.UserSaveCommand;

public interface UserManageService {

    PageResult<UserListItemDTO> list(UserQuery query);

    String create(UserSaveCommand command);

    Boolean update(String userId, UserSaveCommand command);

    Boolean delete(String userId);
}
