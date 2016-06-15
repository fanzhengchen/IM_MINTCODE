package com.mintcode.launchr.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by StephenHe on 2016/4/11.
 * 通过邮箱找回密码
 */
public class BackPasswordMailEntity implements Serializable{
    private int id;
    private String uEmail;
    private String uValidatorToken;
    private long uValidatorTokenTime;
    private String uValidatorCode;
    private String uValidatorStatus;
    private String uSendEmailStatus;
    private String uSendEmailError;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuValidatorToken() {
        return uValidatorToken;
    }

    public void setuValidatorToken(String uValidatorToken) {
        this.uValidatorToken = uValidatorToken;
    }

    public long getuValidatorTokenTime() {
        return uValidatorTokenTime;
    }

    public void setuValidatorTokenTime(long uValidatorTokenTime) {
        this.uValidatorTokenTime = uValidatorTokenTime;
    }

    public String getuValidatorCode() {
        return uValidatorCode;
    }

    public void setuValidatorCode(String uValidatorCode) {
        this.uValidatorCode = uValidatorCode;
    }

    public String getuValidatorStatus() {
        return uValidatorStatus;
    }

    public void setuValidatorStatus(String uValidatorStatus) {
        this.uValidatorStatus = uValidatorStatus;
    }

    public String getuSendEmailStatus() {
        return uSendEmailStatus;
    }

    public void setuSendEmailStatus(String uSendEmailStatus) {
        this.uSendEmailStatus = uSendEmailStatus;
    }

    public String getuSendEmailError() {
        return uSendEmailError;
    }

    public void setuSendEmailError(String uSendEmailError) {
        this.uSendEmailError = uSendEmailError;
    }

    public static class EmailEntity{
        private String uEmail;
        private String uEmailContent;
        private int isHtml;
        private String validatorId;

        public String getuEmail() {
            return uEmail;
        }

        public void setuEmail(String uEmail) {
            this.uEmail = uEmail;
        }

        public String getuEmailContent() {
            return uEmailContent;
        }

        public void setuEmailContent(String uEmailContent) {
            this.uEmailContent = uEmailContent;
        }

        public int getIsHtml() {
            return isHtml;
        }

        public void setIsHtml(int isHtml) {
            this.isHtml = isHtml;
        }

        public String getValidatorId() {
            return validatorId;
        }

        public void setValidatorId(String validatorId) {
            this.validatorId = validatorId;
        }
    }

    public static class EmailModel{
        private String Url;
        private String GoalLink;
        private String UserEmail;

        public String getUserEmail() {
            return UserEmail;
        }

        @JsonProperty("UserEmail")
        public void setUserEmail(String userEmail) {
            UserEmail = userEmail;
        }

        public String getUrl() {
            return Url;
        }

        @JsonProperty("Url")
        public void setUrl(String url) {
            Url = url;
        }

        public String getGoalLink() {
            return GoalLink;
        }

        @JsonProperty("GoalLink")
        public void setGoalLink(String goalLink) {
            GoalLink = goalLink;
        }
    }
}
