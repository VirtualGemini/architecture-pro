package com.velox.module.system.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MenuSaveCommand {

    private String parentId;

    @NotBlank(message = "菜单类型不能为空")
    private String menuType;

    @NotBlank(message = "名称不能为空")
    @Size(max = 100, message = "名称长度不能超过 100 个字符")
    private String name;

    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题长度不能超过 100 个字符")
    private String title;

    @Size(max = 255, message = "路由地址长度不能超过 255 个字符")
    private String path;

    @Size(max = 255, message = "组件路径长度不能超过 255 个字符")
    private String component;

    @Size(max = 255, message = "重定向地址长度不能超过 255 个字符")
    private String redirect;

    @Size(max = 100, message = "图标长度不能超过 100 个字符")
    private String icon;

    @Size(max = 100, message = "权限标识长度不能超过 100 个字符")
    private String authMark;

    @NotNull(message = "启用状态不能为空")
    private Boolean isEnable;

    @NotNull(message = "排序不能为空")
    private Integer sort;

    private Boolean keepAlive;
    private Boolean isHide;
    private Boolean isHideTab;

    @Size(max = 255, message = "外部链接长度不能超过 255 个字符")
    private String link;

    private Boolean isIframe;
    private Boolean showBadge;

    @Size(max = 50, message = "文本徽章长度不能超过 50 个字符")
    private String showTextBadge;

    private Boolean fixedTab;

    @Size(max = 255, message = "激活路径长度不能超过 255 个字符")
    private String activePath;

    private Boolean isFullPage;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAuthMark() {
        return authMark;
    }

    public void setAuthMark(String authMark) {
        this.authMark = authMark;
    }

    public Boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Boolean isEnable) {
        this.isEnable = isEnable;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public Boolean getIsHide() {
        return isHide;
    }

    public void setIsHide(Boolean isHide) {
        this.isHide = isHide;
    }

    public Boolean getIsHideTab() {
        return isHideTab;
    }

    public void setIsHideTab(Boolean isHideTab) {
        this.isHideTab = isHideTab;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Boolean getIsIframe() {
        return isIframe;
    }

    public void setIsIframe(Boolean isIframe) {
        this.isIframe = isIframe;
    }

    public Boolean getShowBadge() {
        return showBadge;
    }

    public void setShowBadge(Boolean showBadge) {
        this.showBadge = showBadge;
    }

    public String getShowTextBadge() {
        return showTextBadge;
    }

    public void setShowTextBadge(String showTextBadge) {
        this.showTextBadge = showTextBadge;
    }

    public Boolean getFixedTab() {
        return fixedTab;
    }

    public void setFixedTab(Boolean fixedTab) {
        this.fixedTab = fixedTab;
    }

    public String getActivePath() {
        return activePath;
    }

    public void setActivePath(String activePath) {
        this.activePath = activePath;
    }

    public Boolean getIsFullPage() {
        return isFullPage;
    }

    public void setIsFullPage(Boolean isFullPage) {
        this.isFullPage = isFullPage;
    }
}
