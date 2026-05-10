package com.architecturepro.infrastructure.web.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.architecturepro.common.exception.ApiException;
import com.architecturepro.common.exception.BusinessErrorCode;
import com.architecturepro.domain.model.Profile;
import com.architecturepro.domain.model.Role;
import com.architecturepro.domain.model.User;
import com.architecturepro.domain.model.UserRole;
import com.architecturepro.infrastructure.config.SecurityProperties;
import com.architecturepro.infrastructure.id.BusinessIdGenerator;
import com.architecturepro.infrastructure.persistence.ProfileMapper;
import com.architecturepro.infrastructure.persistence.RoleMapper;
import com.architecturepro.infrastructure.persistence.UserRoleMapper;
import com.architecturepro.infrastructure.persistence.UserMapper;
import com.architecturepro.infrastructure.web.auth.dto.CaptchaDTO;
import com.architecturepro.infrastructure.web.auth.dto.LoginCommand;
import com.architecturepro.infrastructure.web.auth.dto.RegisterCommand;
import com.architecturepro.infrastructure.web.auth.dto.TokenDTO;
import com.architecturepro.infrastructure.web.auth.service.LoginService;
import com.architecturepro.infrastructure.web.auth.service.PasswordCipherService;
import com.wf.captcha.SpecCaptcha;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginServiceImpl implements LoginService {

    private static final Map<String, CaptchaEntry> CAPTCHA_CACHE = new ConcurrentHashMap<>();

    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordCipherService passwordCipherService;
    private final SecurityProperties securityProperties;
    private final BusinessIdGenerator businessIdGenerator;

    public LoginServiceImpl(UserMapper userMapper,
                            ProfileMapper profileMapper,
                            RoleMapper roleMapper,
                            UserRoleMapper userRoleMapper,
                            PasswordCipherService passwordCipherService,
                            SecurityProperties securityProperties,
                            BusinessIdGenerator businessIdGenerator) {
        this.userMapper = userMapper;
        this.profileMapper = profileMapper;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
        this.passwordCipherService = passwordCipherService;
        this.securityProperties = securityProperties;
        this.businessIdGenerator = businessIdGenerator;
    }

    @Override
    public CaptchaDTO generateCaptcha() {
        clearExpiredCaptcha();
        evictIfCacheTooLarge();

        CaptchaDTO dto = new CaptchaDTO();
        dto.setIsCaptchaOn(true);

        SpecCaptcha specCaptcha = new SpecCaptcha(120, 40, 4);
        String key = IdUtil.simpleUUID();
        long expireAt = System.currentTimeMillis() + securityProperties.getCaptcha().getTtlSeconds() * 1000L;
        CAPTCHA_CACHE.put(key, new CaptchaEntry(specCaptcha.text().toLowerCase(), expireAt));

        dto.setCaptchaCodeKey(key);
        dto.setCaptchaCodeImg(specCaptcha.toBase64());

        return dto;
    }

    @Override
    public TokenDTO login(LoginCommand command) {
        validateCaptchaIfPresent(command.getCaptchaCode(), command.getCaptchaCodeKey());

        String username = command.getUsername();
        String password = command.getPassword();

        if (username == null || username.isBlank()) {
            throw new ApiException(BusinessErrorCode.USER_NOT_FOUND);
        }

        if (password == null || password.isBlank()) {
            throw new ApiException(BusinessErrorCode.PASSWORD_ERROR);
        }

        User user = userMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getDeleted, 0)
                .eq(User::getUsername, username)
        );

        if (user == null) {
            throw new ApiException(BusinessErrorCode.USER_NOT_FOUND);
        }

        checkLoginLock(user);

        if (!passwordCipherService.matches(password, user.getPassword())) {
            increaseLoginFailCount(user);
            throw new ApiException(BusinessErrorCode.PASSWORD_ERROR);
        }

        if (Integer.valueOf(4).equals(user.getStatus())) {
            throw new ApiException(BusinessErrorCode.ACCOUNT_DISABLED);
        }

        resetLoginFailCount(user);
        upgradePasswordIfNeeded(user, password);

        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();

        return new TokenDTO(token, null);
    }

    @Override
    public void register(RegisterCommand command) {
        if (!command.getPassword().equals(command.getConfirmPassword())) {
            throw new ApiException(BusinessErrorCode.PASSWORD_MISMATCH);
        }

        User existUser = userMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getDeleted, 0)
                .eq(User::getUsername, command.getUsername())
        );

        if (existUser != null) {
            throw new ApiException(BusinessErrorCode.USER_ALREADY_EXISTS);
        }

        User user = new User();
        user.setId(businessIdGenerator.nextUserId());
        user.setUsername(command.getUsername());
        user.setPassword(passwordCipherService.encode(command.getPassword()));
        user.setStatus(1);
        user.setLoginFailCount(0);
        user.setDeleted(0);

        userMapper.insert(user);

        Profile profile = new Profile();
        profile.setId(businessIdGenerator.nextProfileId());
        profile.setUserId(user.getId());
        profile.setNickname(command.getUsername());
        profile.setAvatar(buildDefaultAvatar(command.getUsername()));
        profile.setGender(0);
        profile.setDeleted(0);
        profileMapper.insert(profile);

        Role defaultRole = roleMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Role>()
                .eq(Role::getDeleted, 0)
                .eq(Role::getRoleCode, "R_USER")
                .last("limit 1"));
        if (defaultRole != null && defaultRole.getId() != null) {
            UserRole userRole = new UserRole();
            userRole.setId(businessIdGenerator.nextUserRoleId());
            userRole.setUserId(user.getId());
            userRole.setRoleId(defaultRole.getId());
            userRole.setDeleted(0);
            userRoleMapper.insert(userRole);
        }
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    private void validateCaptchaIfPresent(String captchaCode, String key) {
        boolean captchaCodeBlank = captchaCode == null || captchaCode.isBlank();
        boolean keyBlank = key == null || key.isBlank();

        if (captchaCodeBlank && keyBlank) {
            return;
        }

        if (captchaCodeBlank || keyBlank) {
            throw new ApiException(BusinessErrorCode.CAPTCHA_ERROR);
        }

        CaptchaEntry entry = CAPTCHA_CACHE.remove(key);
        if (entry == null || entry.expireAt() < System.currentTimeMillis()) {
            throw new ApiException(BusinessErrorCode.CAPTCHA_EXPIRED);
        }

        if (!entry.answer().equalsIgnoreCase(captchaCode)) {
            throw new ApiException(BusinessErrorCode.CAPTCHA_ERROR);
        }
    }

    private void checkLoginLock(User user) {
        if (user.getLoginFailTime() == null) {
            return;
        }
        java.time.LocalDateTime now = java.time.LocalDateTime.now(java.time.ZoneOffset.UTC);
        if (user.getLoginFailTime().isAfter(now)) {
            throw new ApiException(BusinessErrorCode.ACCOUNT_LOCKED);
        }
        user.setLoginFailCount(0);
        user.setLoginFailTime(null);
        userMapper.updateById(user);
    }

    private void increaseLoginFailCount(User user) {
        int failCount = user.getLoginFailCount() == null ? 0 : user.getLoginFailCount();
        user.setLoginFailCount(failCount + 1);

        if (failCount + 1 >= securityProperties.getLogin().getMaxFailCount()) {
            user.setLoginFailTime(java.time.LocalDateTime.now(java.time.ZoneOffset.UTC)
                    .plusMinutes(securityProperties.getLogin().getLockMinutes()));
        }

        userMapper.updateById(user);
    }

    private void resetLoginFailCount(User user) {
        if (user.getLoginFailCount() != null && user.getLoginFailCount() > 0) {
            user.setLoginFailCount(0);
            user.setLoginFailTime(null);
            userMapper.updateById(user);
        }
    }

    private void upgradePasswordIfNeeded(User user, String rawPassword) {
        if (!passwordCipherService.needsUpgrade(user.getPassword())) {
            return;
        }
        user.setPassword(passwordCipherService.encode(rawPassword));
        userMapper.updateById(user);
    }

    private void clearExpiredCaptcha() {
        long now = System.currentTimeMillis();
        CAPTCHA_CACHE.entrySet().removeIf(entry -> entry.getValue().expireAt() < now);
    }

    private void evictIfCacheTooLarge() {
        int maxCacheSize = securityProperties.getCaptcha().getMaxCacheSize();
        if (maxCacheSize > 0 && CAPTCHA_CACHE.size() >= maxCacheSize) {
            CAPTCHA_CACHE.clear();
        }
    }

    private record CaptchaEntry(String answer, long expireAt) {
    }

    private String buildDefaultAvatar(String username) {
        String seed = username == null || username.isBlank() ? "user" : username.trim();
        return "https://api.dicebear.com/7.x/avataaars/svg?seed=" + seed;
    }
}
