package com.architecturepro.domain.service;

import com.architecturepro.domain.model.CurrentUserInfo;

public interface UserInfoService extends BaseDomainService {

    CurrentUserInfo getCurrentUserInfo();
}
