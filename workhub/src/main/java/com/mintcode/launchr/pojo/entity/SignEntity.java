package com.mintcode.launchr.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by JulyYu on 2016/4/8.
 */
public class SignEntity {

    private String teamCode;
    private String mail;
    private String userName;
    private String passWord;
    private String teamName;

    public String getTeamCode() {
        return teamCode;
    }
    @JsonProperty("R_C_CODE")
    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getMail() {
        return mail;
    }
    @JsonProperty("U_MAIL")
    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassWord() {
        return passWord;
    }
    @JsonProperty("U_PASSWOED")
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserName() {
        return userName;
    }
    @JsonProperty("U_TRUE_NAME")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTeamName() {
        return teamName;
    }
    @JsonProperty("R_C_NAME")
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
