package com.architecturepro.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全相关配置
 */
@Component
@ConfigurationProperties(prefix = "architecture-pro.security")
public class SecurityProperties {

    private boolean swaggerPublicEnabled = false;

    private final Password password = new Password();

    private final Login login = new Login();

    private final Captcha captcha = new Captcha();

    private final Cors cors = new Cors();

    public boolean isSwaggerPublicEnabled() {
        return swaggerPublicEnabled;
    }

    public void setSwaggerPublicEnabled(boolean swaggerPublicEnabled) {
        this.swaggerPublicEnabled = swaggerPublicEnabled;
    }

    public Password getPassword() {
        return password;
    }

    public Login getLogin() {
        return login;
    }

    public Captcha getCaptcha() {
        return captcha;
    }

    public Cors getCors() {
        return cors;
    }

    public static class Password {

        /**
         * 可选：pbkdf2_sha512 / bcrypt / md5
         */
        private String algorithm = "bcrypt";

        /**
         * 登录成功后是否将旧加密算法升级为当前算法
         */
        private boolean upgradeOnLogin = true;

        /**
         * bcrypt 计算强度，范围建议 10~14
         */
        private int bcryptStrength = 12;

        /**
         * PBKDF2 迭代次数
         */
        private int pbkdf2Iterations = 210000;

        /**
         * PBKDF2 输出长度（bit）
         */
        private int pbkdf2KeyLength = 256;

        /**
         * PBKDF2 盐值长度（byte）
         */
        private int pbkdf2SaltLength = 16;

        public String getAlgorithm() {
            return algorithm;
        }

        public void setAlgorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public boolean isUpgradeOnLogin() {
            return upgradeOnLogin;
        }

        public void setUpgradeOnLogin(boolean upgradeOnLogin) {
            this.upgradeOnLogin = upgradeOnLogin;
        }

        public int getBcryptStrength() {
            return bcryptStrength;
        }

        public void setBcryptStrength(int bcryptStrength) {
            this.bcryptStrength = bcryptStrength;
        }

        public int getPbkdf2Iterations() {
            return pbkdf2Iterations;
        }

        public void setPbkdf2Iterations(int pbkdf2Iterations) {
            this.pbkdf2Iterations = pbkdf2Iterations;
        }

        public int getPbkdf2KeyLength() {
            return pbkdf2KeyLength;
        }

        public void setPbkdf2KeyLength(int pbkdf2KeyLength) {
            this.pbkdf2KeyLength = pbkdf2KeyLength;
        }

        public int getPbkdf2SaltLength() {
            return pbkdf2SaltLength;
        }

        public void setPbkdf2SaltLength(int pbkdf2SaltLength) {
            this.pbkdf2SaltLength = pbkdf2SaltLength;
        }
    }

    public static class Login {
        private int maxFailCount = 5;
        private int lockMinutes = 30;

        public int getMaxFailCount() {
            return maxFailCount;
        }

        public void setMaxFailCount(int maxFailCount) {
            this.maxFailCount = maxFailCount;
        }

        public int getLockMinutes() {
            return lockMinutes;
        }

        public void setLockMinutes(int lockMinutes) {
            this.lockMinutes = lockMinutes;
        }
    }

    public static class Captcha {
        private int ttlSeconds = 120;
        private int maxCacheSize = 10000;

        public int getTtlSeconds() {
            return ttlSeconds;
        }

        public void setTtlSeconds(int ttlSeconds) {
            this.ttlSeconds = ttlSeconds;
        }

        public int getMaxCacheSize() {
            return maxCacheSize;
        }

        public void setMaxCacheSize(int maxCacheSize) {
            this.maxCacheSize = maxCacheSize;
        }
    }

    public static class Cors {
        private List<String> allowedOriginPatterns = new ArrayList<>();
        private List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
        private List<String> allowedHeaders = List.of("*");
        private List<String> exposedHeaders = List.of("Authorization", "X-Trace-Id");
        private boolean allowCredentials = true;
        private long maxAge = 3600;

        public List<String> getAllowedOriginPatterns() {
            return allowedOriginPatterns;
        }

        public void setAllowedOriginPatterns(List<String> allowedOriginPatterns) {
            this.allowedOriginPatterns = allowedOriginPatterns;
        }

        public List<String> getAllowedMethods() {
            return allowedMethods;
        }

        public void setAllowedMethods(List<String> allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public List<String> getAllowedHeaders() {
            return allowedHeaders;
        }

        public void setAllowedHeaders(List<String> allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

        public List<String> getExposedHeaders() {
            return exposedHeaders;
        }

        public void setExposedHeaders(List<String> exposedHeaders) {
            this.exposedHeaders = exposedHeaders;
        }

        public boolean isAllowCredentials() {
            return allowCredentials;
        }

        public void setAllowCredentials(boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }

        public long getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(long maxAge) {
            this.maxAge = maxAge;
        }
    }
}
