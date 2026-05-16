package com.velox.infrastructure.web.auth.service;

import com.velox.infrastructure.web.auth.dto.CaptchaDTO;
import com.velox.infrastructure.web.auth.dto.ForgotPasswordCodeCommand;
import com.velox.infrastructure.web.auth.dto.LoginCommand;
import com.velox.infrastructure.web.auth.dto.LoginRoleDTO;
import com.velox.infrastructure.web.auth.dto.RegisterCommand;
import com.velox.infrastructure.web.auth.dto.ResetPasswordCommand;
import com.velox.infrastructure.web.auth.dto.TokenDTO;

import java.util.List;

public interface LoginService {

    CaptchaDTO generateCaptcha();

    List<LoginRoleDTO> listLoginRoles();

    TokenDTO login(LoginCommand command);

    void register(RegisterCommand command);

    void sendResetPasswordCode(ForgotPasswordCodeCommand command);

    void resetPassword(ResetPasswordCommand command);

    void logout();
}
