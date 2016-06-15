package com.mintcode.launchr.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by JulyYu on 2016/3/28.
 */
public class ValidateEntity {

    private int id;
    private String appName;
    private String userName;
    private long msgId;
    private String from;
    private String fromNickName;
    private String fromAvatar;
    private String to;
    private String content;
    private long createDate;
    private long modified;
    private int validateState;

    public long getMsgId() {
        return msgId;
    }
    @JsonProperty("msgId")
    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public int getId() {
        return id;
    }
    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
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

    public String getFrom() {
        return from;
    }
    @JsonProperty("from")
    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromNickName() {
        return fromNickName;
    }
    @JsonProperty("fromNickName")
    public void setFromNickName(String fromNickName) {
        this.fromNickName = fromNickName;
    }

    public String getFromAvatar() {
        return fromAvatar;
    }
    @JsonProperty("fromAvatar")
    public void setFromAvatar(String fromAvatar) {
        this.fromAvatar = fromAvatar;
    }

    public String getTo() {
        return to;
    }
    @JsonProperty("to")
    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }
    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    public long getModified() {
        return modified;
    }
    @JsonProperty("modified")
    public void setModified(long modified) {
        this.modified = modified;
    }

    public long getCreateDate() {
        return createDate;
    }
    @JsonProperty("createDate")
    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getValidateState() {
        return validateState;
    }
    @JsonProperty("validateState")
    public void setValidateState(int validateState) {
        this.validateState = validateState;
    }
}
