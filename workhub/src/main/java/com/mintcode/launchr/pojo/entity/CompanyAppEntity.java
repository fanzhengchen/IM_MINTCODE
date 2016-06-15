package com.mintcode.launchr.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by StephenHe on 2016/3/25.
 */
public class CompanyAppEntity implements Serializable {
    private int id;
    private String showId;
    private String appCode;
    private String appName;
    private String appStatus;
    private String appDes;
    private String appIconWeb;
    private String appIconMobile;
    private String createUser;
    private String createUserName;
    private String createTime;

    public int getId() {
        return id;
    }

    @JsonProperty("ID")
    public void setId(int id) {
        this.id = id;
    }

    public String getShowId() {
        return showId;
    }

    @JsonProperty("SHOW_ID")
    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getAppCode() {
        return appCode;
    }

    @JsonProperty("APP_CODE")
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return appName;
    }

    @JsonProperty("APP_NAME")
    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppStatus() {
        return appStatus;
    }

    @JsonProperty("APP_STATUS")
    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public String getAppDes() {
        return appDes;
    }

    @JsonProperty("APP_DES")
    public void setAppDes(String appDes) {
        this.appDes = appDes;
    }

    public String getAppIconWeb() {
        return appIconWeb;
    }

    @JsonProperty("APP_ICON_WEB")
    public void setAppIconWeb(String appIconWeb) {
        this.appIconWeb = appIconWeb;
    }

    public String getAppIconMobile() {
        return appIconMobile;
    }

    @JsonProperty("APP_ICON_MOBILE")
    public void setAppIconMobile(String appIconMobile) {
        this.appIconMobile = appIconMobile;
    }

    public String getCreateUser() {
        return createUser;
    }

    @JsonProperty("CREATE_USER")
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    @JsonProperty("CREATE_USER_NAME")
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    @JsonProperty("CREATE_TIME")
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
