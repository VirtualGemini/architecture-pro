package com.velox.framework.web.autoconfigure;

import com.velox.framework.config.SecurityProperties;
import com.velox.framework.config.VeloxProperties;
import com.velox.framework.log.RequestLogInterceptor;
import com.velox.framework.web.RequestTimeZoneFilter;
import com.velox.framework.web.TraceIdFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@AutoConfiguration
@ConditionalOnBean({VeloxProperties.class, SecurityProperties.class})
public class VeloxWebMvcAutoConfiguration implements WebMvcConfigurer {

    private final SecurityProperties securityProperties;
    private final VeloxProperties veloxProperties;
    private final RequestLogInterceptor requestLogInterceptor;

    public VeloxWebMvcAutoConfiguration(SecurityProperties securityProperties,
                                        VeloxProperties veloxProperties,
                                        RequestLogInterceptor requestLogInterceptor) {
        this.securityProperties = securityProperties;
        this.veloxProperties = veloxProperties;
        this.requestLogInterceptor = requestLogInterceptor;
    }

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
    public void configurePathMatch(PathMatchConfigurer configurer) {
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLogInterceptor).addPathPatterns("/**");
    }

    @Bean
    @ConditionalOnBean(TraceIdFilter.class)
    public FilterRegistrationBean<TraceIdFilter> traceIdFilterRegistration(TraceIdFilter traceIdFilter) {
        FilterRegistrationBean<TraceIdFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(traceIdFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        registration.setName("traceIdFilter");
        return registration;
    }

    @Bean
    public FilterRegistrationBean<RequestTimeZoneFilter> requestTimeZoneFilterRegistration() {
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
