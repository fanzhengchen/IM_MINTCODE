package com.mintcode.launchr.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by StephenHe on 2016/1/6.
 */
public class ApprovalTypeEntity implements Serializable{
    private String SHOW_ID;
    private String T_NAME;
    private String T_DES;
    private String IS_DEFAULT;
    private String T_STATUS;
    private String CREATE_USER;
    private String CREATE_USER_NAME;
    private long CREATE_TIME;
    private String WORK_FLOW_ID;
    private String FORM_ID;

    public String getWORK_FLOW_ID() {
        return WORK_FLOW_ID;
    }
    @JsonProperty("T_WORKFLOW_ID")
    public void setWORK_FLOW_ID(String WORK_FLOW_ID) {
        this.WORK_FLOW_ID = WORK_FLOW_ID;
    }

    public String getFORM_ID() {
        return FORM_ID;
    }
    @JsonProperty("FORM_ID")
    public void setFORM_ID(String FORM_ID) {
        this.FORM_ID = FORM_ID;
    }

    public String getSHOW_ID() {
        return SHOW_ID;
    }

    @JsonProperty("SHOW_ID")
    public void setSHOW_ID(String SHOW_ID) {
        this.SHOW_ID = SHOW_ID;
    }

    public String getT_NAME() {
        return T_NAME;
    }

    @JsonProperty("T_NAME")
    public void setT_NAME(String t_NAME) {
        T_NAME = t_NAME;
    }

    public String getT_DES() {
        return T_DES;
    }

    @JsonProperty("T_DES")
    public void setT_DES(String t_DES) {
        T_DES = t_DES;
    }

    public String getIS_DEFAULT() {
        return IS_DEFAULT;
    }

    @JsonProperty("IS_DEFAULT")
    public void setIS_DEFAULT(String IS_DEFAULT) {
        this.IS_DEFAULT = IS_DEFAULT;
    }

    public String getT_STATUS() {
        return T_STATUS;
    }

    @JsonProperty("T_STATUS")
    public void setT_STATUS(String t_STATUS) {
        T_STATUS = t_STATUS;
    }

    public String getCREATE_USER() {
        return CREATE_USER;
    }

    @JsonProperty("CREATE_USER")
    public void setCREATE_USER(String CREATE_USER) {
        this.CREATE_USER = CREATE_USER;
    }

    public String getCREATE_USER_NAME() {
        return CREATE_USER_NAME;
    }

    @JsonProperty("CREATE_USER_NAME")
    public void setCREATE_USER_NAME(String CREATE_USER_NAME) {
        this.CREATE_USER_NAME = CREATE_USER_NAME;
    }

    public long getCREATE_TIME() {
        return CREATE_TIME;
    }

    @JsonProperty("CREATE_TIME")
    public void setCREATE_TIME(long CREATE_TIME) {
        this.CREATE_TIME = CREATE_TIME;
    }
}
