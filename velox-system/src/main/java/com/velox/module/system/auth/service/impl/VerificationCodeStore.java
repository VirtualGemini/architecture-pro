package com.velox.module.system.auth.service.impl;

import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.velox.framework.config.SecurityProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;

@Component
public class VerificationCodeStore {

    private static final String CAPTCHA_PREFIX = "auth:captcha:";
    private static final String RESET_PREFIX = "auth:reset:";
    private static final String RESET_SENT_PREFIX = "auth:reset:sent:";

    private final StringRedisTemplate stringRedisTemplate;
    private final SecurityProperties securityProperties;

    public VerificationCodeStore(StringRedisTemplate stringRedisTemplate, SecurityProperties securityProperties) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.securityProperties = securityProperties;
    }

    public void saveCaptcha(String key, String code) {
        stringRedisTemplate.opsForValue().set(
                CAPTCHA_PREFIX + key,
                digest(code),
                Duration.ofSeconds(securityProperties.getCaptcha().getTtlSeconds())
        );
    }

    public boolean consumeCaptcha(String key, String code) {
        String redisKey = CAPTCHA_PREFIX + key;
        String stored = stringRedisTemplate.opsForValue().get(redisKey);
        stringRedisTemplate.delete(redisKey);
        return stored != null && Objects.equals(stored, digest(code));
    }

    public boolean captchaExists(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(CAPTCHA_PREFIX + key));
    }

    public boolean canSendResetCode(String email) {
        return !Boolean.TRUE.equals(stringRedisTemplate.hasKey(RESET_SENT_PREFIX + email));
    }

    public void saveResetCode(String email, String code) {
        stringRedisTemplate.opsForValue().set(
                RESET_PREFIX + email,
                digest(code),
                Duration.ofSeconds(securityProperties.getVerification().getResetCodeTtlSeconds())
        );
        stringRedisTemplate.opsForValue().set(
                RESET_SENT_PREFIX + email,
                "1",
                Duration.ofSeconds(securityProperties.getVerification().getResetCodeResendIntervalSeconds())
        );
    }

    public VerificationResult verifyResetCode(String email, String code) {
        String redisKey = RESET_PREFIX + email;
        String stored = stringRedisTemplate.opsForValue().get(redisKey);
        if (stored == null) {
            return VerificationResult.EXPIRED;
        }
        if (!Objects.equals(stored, digest(code))) {
            return VerificationResult.INVALID;
        }
        stringRedisTemplate.delete(redisKey);
        return VerificationResult.MATCHED;
    }

    private String digest(String code) {
        String secret = securityProperties.getVerification().getSecret();
        HMac hMac = new HMac(HmacAlgorithm.HmacSHA256, secret.getBytes(StandardCharsets.UTF_8));
        return hMac.digestHex(code == null ? "" : code.trim().toLowerCase());
    }

    public enum VerificationResult {
        MATCHED,
        INVALID,
        EXPIRED
    }
}
