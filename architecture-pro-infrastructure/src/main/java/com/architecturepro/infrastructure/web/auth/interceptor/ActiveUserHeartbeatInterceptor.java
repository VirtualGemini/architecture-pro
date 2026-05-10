package com.architecturepro.infrastructure.web.auth.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import com.architecturepro.infrastructure.web.auth.service.ActiveUserStatusService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ActiveUserHeartbeatInterceptor implements HandlerInterceptor {

    private final ActiveUserStatusService activeUserStatusService;

    public ActiveUserHeartbeatInterceptor(ActiveUserStatusService activeUserStatusService) {
        this.activeUserStatusService = activeUserStatusService;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        if (StpUtil.isLogin()) {
            activeUserStatusService.recordRequestActivity(StpUtil.getLoginIdAsString());
        }
        return true;
    }
}
