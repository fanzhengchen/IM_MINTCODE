package com.mintcode.chat.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * create by kevin 历史会话消息实体
 */
public class HistoryMsgEntity implements Serializable{

    /** 服务端消息Id */
    private long msgId;

    /** 客户端用户信息 */
    private String info;

    /** 未读消息数(现在用来判断是否已读) */
    private int unReadCount;

    /** 消息内容 */
    private String content;

    /** 消息类型 */
    private String type;

    /** 消息发送者 */
    @JsonProperty(value="from")
    private String fromLoginName;

    /** 消息接收者 */
    @JsonProperty(value="to")
    private String toLoginName;

    /** 客户端消息Id */
    private long clientMsgId;

    /** 消息创建时间 */
    private long createDate;

    /** 客户端用户信息 */
    private long modified;

    /** 客户端用户信息 */
    private int bookMark;

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFromLoginName() {
        return fromLoginName;
    }

    public void setFromLoginName(String fromLoginName) {
        this.fromLoginName = fromLoginName;
    }

    public String getToLoginName() {
        return toLoginName;
    }

    public void setToLoginName(String toLoginName) {
        this.toLoginName = toLoginName;
    }

    public long getClientMsgId() {
        return clientMsgId;
    }

    public void setClientMsgId(long clientMsgId) {
        this.clientMsgId = clientMsgId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

    public int getBookMark() {
        return bookMark;
    }

    public void setBookMark(int bookMark) {
        this.bookMark = bookMark;
    }
}
