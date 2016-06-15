package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

/**
 * Created by StephenHe on 2016/2/23.
 */
public class TaskStateEntity implements Serializable{
    private int isSuccess;
    private String showId;
    private String tTitle;
    private String tContent;
    private long tStartTime;
    private long tEndTime;
    private String tUser;
    private String tUserName;
    private String tPriority;
    private int tLevel;
    private String sType;
    private String pName;
    private String createUser;
    private String createUserName;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String gettTitle() {
        return tTitle;
    }

    public void settTitle(String tTitle) {
        this.tTitle = tTitle;
    }

    public String gettContent() {
        return tContent;
    }

    public void settContent(String tContent) {
        this.tContent = tContent;
    }

    public long gettStartTime() {
        return tStartTime;
    }

    public void settStartTime(long tStartTime) {
        this.tStartTime = tStartTime;
    }

    public long gettEndTime() {
        return tEndTime;
    }

    public void settEndTime(long tEndTime) {
        this.tEndTime = tEndTime;
    }

    public String gettUser() {
        return tUser;
    }

    public void settUser(String tUser) {
        this.tUser = tUser;
    }

    public String gettUserName() {
        return tUserName;
    }

    public void settUserName(String tUserName) {
        this.tUserName = tUserName;
    }

    public String gettPriority() {
        return tPriority;
    }

    public void settPriority(String tPriority) {
        this.tPriority = tPriority;
    }

    public int gettLevel() {
        return tLevel;
    }

    public void settLevel(int tLevel) {
        this.tLevel = tLevel;
    }

    public String getsType() {
        return sType;
    }

    public void setsType(String sType) {
        this.sType = sType;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
}
