package com.velox.domain.service;

import com.velox.domain.model.CurrentUserInfo;

public interface UserInfoService extends BaseDomainService {

    CurrentUserInfo getCurrentUserInfo();
}
