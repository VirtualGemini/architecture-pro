package com.velox.module.system.domain.service;

import com.velox.domain.service.BaseDomainService;
import com.velox.module.system.domain.model.CurrentUserInfo;

public interface UserInfoService extends BaseDomainService {

    CurrentUserInfo getCurrentUserInfo();
}
