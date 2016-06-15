package com.mintcode.launchr.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by JulyYu on 2016/4/5.
 */
public class FriendEntity extends DataSupport implements Serializable{

    private int id;
    private String appName;
    /** 自己用户的Id*/
    private String userName;
    private String relationName;
    private String remark;
    private String tag;
    private int relationGroupId;
    private long createDate;
    private String isPass;
    private String nickName;
    private String relationAvatar;
    private long relationModified;
    private String mobile;
    private String imNumber;


    public String getImNumber() {
        return imNumber;
    }
    @JsonProperty("imNumber")
    public void setImNumber(String imNumber) {
        this.imNumber = imNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getRelationGroupId() {
        return relationGroupId;
    }

    public void setRelationGroupId(int relationGroupId) {
        this.relationGroupId = relationGroupId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getIsPass() {
        return isPass;
    }

    public void setIsPass(String isPass) {
        this.isPass = isPass;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRelationAvatar() {
        return relationAvatar;
    }

    public void setRelationAvatar(String relationAvatar) {
        this.relationAvatar = relationAvatar;
    }

    public long getRelationModified() {
        return relationModified;
    }

    public void setRelationModified(long relationModified) {
        this.relationModified = relationModified;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
