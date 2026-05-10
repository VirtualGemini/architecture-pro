package com.architecturepro.infrastructure.web.auth.service;

import com.architecturepro.infrastructure.web.auth.dto.CaptchaDTO;
import com.architecturepro.infrastructure.web.auth.dto.LoginCommand;
import com.architecturepro.infrastructure.web.auth.dto.RegisterCommand;
import com.architecturepro.infrastructure.web.auth.dto.TokenDTO;

public interface LoginService {

    CaptchaDTO generateCaptcha();

    TokenDTO login(LoginCommand command);

    void register(RegisterCommand command);

    void logout();
}