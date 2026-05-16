package com.velox.infrastructure.web;

import com.velox.infrastructure.config.VeloxProperties;
import com.velox.infrastructure.config.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.method.HandlerTypePredicate;

import java.util.List;

/**
 * Web 配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final SecurityProperties securityProperties;
    private final VeloxProperties veloxProperties;

    public WebConfig(SecurityProperties securityProperties, VeloxProperties veloxProperties) {
        this.securityProperties = securityProperties;
        this.veloxProperties = veloxProperties;
    }

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> allowedOriginPatterns = securityProperties.getCors().getAllowedOriginPatterns();
        if (allowedOriginPatterns == null || allowedOriginPatterns.isEmpty()) {
            return;
        }

        registry.addMapping("/**")
                .allowedOriginPatterns(allowedOriginPatterns.toArray(new String[0]))
                .allowedMethods(securityProperties.getCors().getAllowedMethods().toArray(new String[0]))
                .allowedHeaders(securityProperties.getCors().getAllowedHeaders().toArray(new String[0]))
                .exposedHeaders(securityProperties.getCors().getExposedHeaders().toArray(new String[0]))
                .allowCredentials(securityProperties.getCors().isAllowCredentials())
                .maxAge(securityProperties.getCors().getMaxAge());
    }

    @Override
    public void configurePathMatch(org.springframework.web.servlet.config.annotation.PathMatchConfigurer configurer) {
        String normalizedPrefix = normalizePrefix(veloxProperties.getApiPrefix());
        if (!StringUtils.hasText(normalizedPrefix)) {
            return;
        }
        configurer.addPathPrefix(
                normalizedPrefix,
                HandlerTypePredicate.forBasePackage("com.velox")
                        .and(HandlerTypePredicate.forAnnotation(RestController.class))
        );
    }

    /**
     * 注册 TraceId 过滤器
     */
    @Bean
    public FilterRegistrationBean<TraceIdFilter> traceIdFilter() {
        FilterRegistrationBean<TraceIdFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TraceIdFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        registration.setName("traceIdFilter");
        return registration;
    }

    @Bean
    public FilterRegistrationBean<RequestTimeZoneFilter> requestTimeZoneFilter() {
        FilterRegistrationBean<RequestTimeZoneFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestTimeZoneFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(2);
        registration.setName("requestTimeZoneFilter");
        return registration;
    }

    private String normalizePrefix(String prefix) {
        if (!StringUtils.hasText(prefix)) {
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
