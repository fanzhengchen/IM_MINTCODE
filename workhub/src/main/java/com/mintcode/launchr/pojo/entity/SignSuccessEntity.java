package com.mintcode.launchr.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by JulyYu on 2016/4/11.
 */
public class SignSuccessEntity implements Serializable{

    private String SHOW_ID;
    private String U_NAME;
    private String U_TRUE_NAME;
    private String U_TRUE_NAME_C;
    private String U_HIRA;
    private String U_MAIL;
    private int U_STATUS;
    private String U_MOBILE;
    private int U_SORT;
    private long LAST_LOGIN_TIME;
    private String LAST_LOGIN_TOKEN;
    private int IS_ADMIN;
    private String C_SHOW_ID;
    private String CREATE_USER;
    private String CREATE_TIME;
    private String CREATE_USER_NAME;
    private String D_NAME;
    private String U_DEPT_ID;
    private String D_PARENTID_SHOW_ID;
    private String D_PATH_NAME;
    private String uValidatorToken;
    private String uValidatorCode;
    private String uValidatorId;
    private String url;
    private int isNewCompany;


    public String getSHOW_ID() {
        return SHOW_ID;
    }

    @JsonProperty("SHOW_ID")
    public void setSHOW_ID(String SHOW_ID) {
        this.SHOW_ID = SHOW_ID;
    }

    public String getU_NAME() {
        return U_NAME;
    }

    @JsonProperty("U_NAME")
    public void setU_NAME(String u_NAME) {
        U_NAME = u_NAME;
    }

    public String getU_TRUE_NAME() {
        return U_TRUE_NAME;
    }

    @JsonProperty("U_TRUE_NAME")
    public void setU_TRUE_NAME(String u_TRUE_NAME) {
        U_TRUE_NAME = u_TRUE_NAME;
    }

    public String getU_TRUE_NAME_C() {
        return U_TRUE_NAME_C;
    }

    @JsonProperty("U_TRUE_NAME_C")
    public void setU_TRUE_NAME_C(String u_TRUE_NAME_C) {
        U_TRUE_NAME_C = u_TRUE_NAME_C;
    }

    public String getU_HIRA() {
        return U_HIRA;
    }

    @JsonProperty("U_HIRA")
    public void setU_HIRA(String u_HIRA) {
        U_HIRA = u_HIRA;
    }

    public String getU_MAIL() {
        return U_MAIL;
    }

    @JsonProperty("U_MAIL")
    public void setU_MAIL(String u_MAIL) {
        U_MAIL = u_MAIL;
    }

    public int getU_STATUS() {
        return U_STATUS;
    }

    @JsonProperty("U_STATUS")
    public void setU_STATUS(int u_STATUS) {
        U_STATUS = u_STATUS;
    }

    public String getU_MOBILE() {
        return U_MOBILE;
    }

    @JsonProperty("U_MOBILE")
    public void setU_MOBILE(String u_MOBILE) {
        U_MOBILE = u_MOBILE;
    }

    public long getLAST_LOGIN_TIME() {
        return LAST_LOGIN_TIME;
    }

    @JsonProperty("LAST_LOGIN_TIME")
    public void setLAST_LOGIN_TIME(long LAST_LOGIN_TIME) {
        this.LAST_LOGIN_TIME = LAST_LOGIN_TIME;
    }

    public int getU_SORT() {
        return U_SORT;
    }

    @JsonProperty("U_SORT")
    public void setU_SORT(int u_SORT) {
        U_SORT = u_SORT;
    }

    public String getLAST_LOGIN_TOKEN() {
        return LAST_LOGIN_TOKEN;
    }

    @JsonProperty("LAST_LOGIN_TOKEN")
    public void setLAST_LOGIN_TOKEN(String LAST_LOGIN_TOKEN) {
        this.LAST_LOGIN_TOKEN = LAST_LOGIN_TOKEN;
    }

    public int getIS_ADMIN() {
        return IS_ADMIN;
    }

    @JsonProperty("IS_ADMIN")
    public void setIS_ADMIN(int IS_ADMIN) {
        this.IS_ADMIN = IS_ADMIN;
    }

    public String getC_SHOW_ID() {
        return C_SHOW_ID;
    }

    @JsonProperty("C_SHOW_ID")
    public void setC_SHOW_ID(String c_SHOW_ID) {
        C_SHOW_ID = c_SHOW_ID;
    }

    public String getCREATE_USER() {
        return CREATE_USER;
    }

    @JsonProperty("CREATE_USER")
    public void setCREATE_USER(String CREATE_USER) {
        this.CREATE_USER = CREATE_USER;
    }

    public String getCREATE_TIME() {
        return CREATE_TIME;
    }

    @JsonProperty("CREATE_TIME")
    public void setCREATE_TIME(String CREATE_TIME) {
        this.CREATE_TIME = CREATE_TIME;
    }

    public String getCREATE_USER_NAME() {
        return CREATE_USER_NAME;
    }

    @JsonProperty("CREATE_USER_NAME")
    public void setCREATE_USER_NAME(String CREATE_USER_NAME) {
        this.CREATE_USER_NAME = CREATE_USER_NAME;
    }

    public String getD_NAME() {
        return D_NAME;
    }

    @JsonProperty("D_NAME")
    public void setD_NAME(String d_NAME) {
        D_NAME = d_NAME;
    }

    public String getU_DEPT_ID() {
        return U_DEPT_ID;
    }

    @JsonProperty("U_DEPT_ID")
    public void setU_DEPT_ID(String u_DEPT_ID) {
        U_DEPT_ID = u_DEPT_ID;
    }

    public String getD_PARENTID_SHOW_ID() {
        return D_PARENTID_SHOW_ID;
    }

    @JsonProperty("D_PARENTID_SHOW_ID")
    public void setD_PARENTID_SHOW_ID(String d_PARENTID_SHOW_ID) {
        D_PARENTID_SHOW_ID = d_PARENTID_SHOW_ID;
    }

    public int getIsNewCompany() {
        return isNewCompany;
    }

    @JsonProperty("isNewCompany")
    public void setIsNewCompany(int isNewCompany) {
        this.isNewCompany = isNewCompany;
    }

    public String getD_PATH_NAME() {
        return D_PATH_NAME;
    }

    @JsonProperty("D_PATH_NAME")
    public void setD_PATH_NAME(String d_PATH_NAME) {
        D_PATH_NAME = d_PATH_NAME;
    }

    public String getuValidatorCode() {
        return uValidatorCode;
    }

    @JsonProperty("uValidatorCode")
    public void setuValidatorCode(String uValidatorCode) {
        this.uValidatorCode = uValidatorCode;
    }

    public String getuValidatorToken() {
        return uValidatorToken;
    }

    @JsonProperty("uValidatorToken")
    public void setuValidatorToken(String uValidatorToken) {
        this.uValidatorToken = uValidatorToken;
    }

    public String getuValidatorId() {
        return uValidatorId;
    }

    @JsonProperty("uValidatorId")
    public void setuValidatorId(String uValidatorId) {
        this.uValidatorId = uValidatorId;
    }

    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }
}
