package com.mintcode.chat.entity;

import java.io.Serializable;

/**
 * Created by StephenHe on 2016/1/12.
 */
public class ResendEntity implements Serializable{
    private String type;
    private String content;
    private long msgId;
    private long clientMsgId;

    public long getClientMsgId() {
        return clientMsgId;
    }

    public void setClientMsgId(long clientMsgId) {
        this.clientMsgId = clientMsgId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }
}
