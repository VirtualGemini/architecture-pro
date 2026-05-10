package com.architecturepro.infrastructure.web.auth.service;

public interface PasswordCipherService {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);

    boolean needsUpgrade(String encodedPassword);
}