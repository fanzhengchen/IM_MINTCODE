package com.mintcode.chat.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 消息合并转发
 */
public class MergeEntity implements Serializable{
    private String type;
    private String from;
    private long createDate;
    private long clientMsgId;
    private long msgId;
    private String info;
    private String content;
    private long modified;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getClientMsgId() {
        return clientMsgId;
    }

    public void setClientMsgId(long clientMsgId) {
        this.clientMsgId = clientMsgId;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

    public static class MergeCard{
        private List<MergeEntity> msg;
        private String title;

        public List<MergeEntity> getMsg() {
            return msg;
        }

        public void setMsg(List<MergeEntity> msg) {
            this.msg = msg;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
