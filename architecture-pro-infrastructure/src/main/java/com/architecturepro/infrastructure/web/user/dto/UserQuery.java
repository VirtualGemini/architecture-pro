package com.architecturepro.infrastructure.web.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户查询参数")
public class UserQuery {

    @Schema(description = "当前页", example = "1")
    private Long current = 1L;

    @Schema(description = "每页大小", example = "20")
    private Long size = 20L;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "性别")
    private String userGender;

    @Schema(description = "手机号")
    private String userPhone;

    @Schema(description = "邮箱")
    private String userEmail;

    @Schema(description = "状态")
    private String status;

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
