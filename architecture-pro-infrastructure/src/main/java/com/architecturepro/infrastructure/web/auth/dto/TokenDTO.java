package com.architecturepro.infrastructure.web.auth.dto;

public class TokenDTO {
    private String token;
    private Object userInfo;

    public TokenDTO() {
    }

    public TokenDTO(String token, Object userInfo) {
        this.token = token;
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Object userInfo) {
        this.userInfo = userInfo;
    }
}
