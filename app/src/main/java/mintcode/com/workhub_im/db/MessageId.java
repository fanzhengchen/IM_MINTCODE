package mintcode.com.workhub_im.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "MESSAGE_ID".
 */
public class MessageId {

    private Long id;
    private Long msgId;
    private String uid;

    public MessageId() {
    }

    public MessageId(Long id) {
        this.id = id;
    }

    public MessageId(Long id, Long msgId, String uid) {
        this.id = id;
        this.msgId = msgId;
        this.uid = uid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
