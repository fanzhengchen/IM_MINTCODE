package com.mintcode;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mintcode.launchr.api.IMAPI.TASKID;

@JsonTypeName(TASKID.UN_READ_SESSION)
public class UnReadSessionPOJO extends BasePOJO {

    private String action;
    private int code;
    private String message;
    private long msgId;
    private List<Sessions> sessions;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public List<Sessions> getSessions() {
        return sessions;
    }

    public void setSessions(List<Sessions> sessions) {
        this.sessions = sessions;
    }

    public static class Sessions {
        public String sessionName;
        public int count;
        public LastMsg lastMsg;

        public String getSessionName() {
            return sessionName;
        }

        public void setSessionName(String sessionName) {
            this.sessionName = sessionName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public LastMsg getLastMsg() {
            return lastMsg;
        }

        public void setLastMsg(LastMsg lastMsg) {
            this.lastMsg = lastMsg;
        }

    }

    public static class LastMsg {
        public String from;
        public String to;
        public String content;
        public long clientMsgId;
        public long msgId;
        public String info;
        public long createDate;
        public String type;
        public int modified;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getModified() {
            return modified;
        }

        public void setModified(int modified) {
            this.modified = modified;
        }

    }
}
