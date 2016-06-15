package com.mintcode.launchr.pojo.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by JulyYu on 2016/5/4.
 */
public class ChatGroupEntity extends DataSupport implements Serializable{
    private long uid;
    private String userName;
    private String nickName;
    private String type;
    private int receiveMode;
    private String avatar;
    private long modified;
    private String tag;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getReceiveMode() {
        return receiveMode;
    }

    public void setReceiveMode(int receiveMode) {
        this.receiveMode = receiveMode;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
