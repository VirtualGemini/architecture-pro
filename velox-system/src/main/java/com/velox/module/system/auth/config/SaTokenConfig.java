package com.velox.module.system.auth.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.velox.framework.config.VeloxProperties;
import com.velox.framework.config.SecurityProperties;
import com.velox.module.system.auth.interceptor.ActiveUserHeartbeatInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    private final SecurityProperties securityProperties;
    private final VeloxProperties veloxProperties;
    private final ActiveUserHeartbeatInterceptor activeUserHeartbeatInterceptor;

    public SaTokenConfig(SecurityProperties securityProperties,
                         VeloxProperties veloxProperties,
                         ActiveUserHeartbeatInterceptor activeUserHeartbeatInterceptor) {
        this.securityProperties = securityProperties;
        this.veloxProperties = veloxProperties;
        this.activeUserHeartbeatInterceptor = activeUserHeartbeatInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludes = new ArrayList<>();
        addAuthExclude(excludes, "/login");
        addAuthExclude(excludes, "/login-roles");
        addAuthExclude(excludes, "/captcha");
        addAuthExclude(excludes, "/register");
        addAuthExclude(excludes, "/forgot-password/code");
        addAuthExclude(excludes, "/forgot-password/reset");
        addPublicFileDownloadExclude(excludes);

        if (securityProperties.isSwaggerPublicEnabled()) {
            excludes.add("/swagger-ui/**");
            excludes.add("/swagger-ui.html");
            excludes.add("/v3/api-docs/**");
            excludes.add("/doc.html");
            excludes.add("/webjars/**");
        }

        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
            .addPathPatterns("/**")
            .excludePathPatterns(excludes);

        registry.addInterceptor(activeUserHeartbeatInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludes);
    }

    private void addAuthExclude(List<String> excludes, String authSuffix) {
        excludes.add("/auth" + authSuffix);
        String apiPrefix = normalizePrefix(veloxProperties.getApiPrefix());
        if (!apiPrefix.isEmpty()) {
            excludes.add(apiPrefix + "/auth" + authSuffix);
        }
    }

    private void addPublicFileDownloadExclude(List<String> excludes) {
        excludes.add("/file/*/get/**");

        String apiPrefix = normalizePrefix(veloxProperties.getApiPrefix());
        if (!apiPrefix.isEmpty()) {
            excludes.add(apiPrefix + "/file/*/get/**");
        }
    }

    private void addCustomExclude(List<String> excludes, String path) {
        excludes.add(path);

        String apiPrefix = normalizePrefix(veloxProperties.getApiPrefix());
        if (!apiPrefix.isEmpty()) {
            excludes.add(apiPrefix + path);
        }
    }

    private String normalizePrefix(String prefix) {
        if (prefix == null || prefix.isBlank()) {
            return "";
        }
        String normalized = prefix.trim();
        if ("/".equals(normalized)) {
            return "";
        }
        if (!normalized.startsWith("/")) {
            normalized = "/" + normalized;
        }
        if (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }
}
