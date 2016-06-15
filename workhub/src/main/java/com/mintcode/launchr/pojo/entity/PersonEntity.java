package com.mintcode.launchr.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by JulyYu on 2016/3/24.
 */
public class PersonEntity implements Serializable{

    private int id;
    private int relationGroupId;
    private long createDate;

    private String appName;
    private String userName;
    private int relationId;
    private String remark;
    private String tag;
    private String mobile;
    private String avatar;
    private boolean ralation;
    private String imNumber;

    private String nickName;
    private String relationAvatar;
    private long relationModified;
    private String relationName;


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
    public String getTag() {
        return tag;
    }
    @JsonProperty("tag")
    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getNickName() {
        return nickName;
    }
    @JsonProperty("nickName")
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRelationAvatar() {
        return relationAvatar;
    }
    @JsonProperty("relationAvatar")
    public void setRelationAvatar(String relationAvatar) {
        this.relationAvatar = relationAvatar;
    }

    public long getRelationModified() {
        return relationModified;
    }
    @JsonProperty("relationModified")
    public void setRelationModified(long relationModified) {
        this.relationModified = relationModified;
    }

    public String getRelationName() {
        return relationName;
    }
    @JsonProperty("relationName")
    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getAppName() {
        return appName;
    }
    @JsonProperty("appName")
    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUserName() {
        return userName;
    }
    @JsonProperty("userName")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRemark() {
        return remark;
    }
    @JsonProperty("remark")
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getRelation() {
        return relationId;
    }
    @JsonProperty("relationId")
    public void setRelation(int relation) {
        this.relationId = relation;
    }

    public String getMobile() {
        return mobile;
    }
    @JsonProperty("mobile")
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }
    @JsonProperty("avatar")
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isRalation() {
        return ralation;
    }
    @JsonProperty("relation")
    public void setRalation(boolean ralation) {
        this.ralation = ralation;
    }

}
