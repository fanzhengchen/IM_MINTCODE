package mintcode.com.workhub_im.pojo;

/**
 * Created by mark on 16-6-20.
 */
public class HeartBeat {
    private long msgId;
    private String type;

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
