package com.velox.infrastructure.web.user.service;

import com.velox.domain.model.CurrentUserInfo;
import com.velox.infrastructure.web.user.dto.UserInfoDTO;
import com.velox.infrastructure.web.user.dto.UserPasswordUpdateCommand;
import com.velox.infrastructure.web.user.dto.UserProfileUpdateCommand;

public interface UserInfoService {

    CurrentUserInfo getCurrentUserInfo();

    UserInfoDTO getUserInfoDTO();

    Boolean updateCurrentUserProfile(UserProfileUpdateCommand command);

    Boolean updateCurrentUserPassword(UserPasswordUpdateCommand command);

    void updateCurrentUserAvatar(String avatarUrl);
}
