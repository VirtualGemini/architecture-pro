package com.architecturepro.infrastructure.web.user.service;

import com.architecturepro.domain.model.CurrentUserInfo;
import com.architecturepro.infrastructure.web.user.dto.UserInfoDTO;
import com.architecturepro.infrastructure.web.user.dto.UserPasswordUpdateCommand;
import com.architecturepro.infrastructure.web.user.dto.UserProfileUpdateCommand;

public interface UserInfoService {

    CurrentUserInfo getCurrentUserInfo();

    UserInfoDTO getUserInfoDTO();

    Boolean updateCurrentUserProfile(UserProfileUpdateCommand command);

    Boolean updateCurrentUserPassword(UserPasswordUpdateCommand command);

    void updateCurrentUserAvatar(String avatarUrl);
}
