package mintcode.com.workhub_im.im.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mark on 16-6-15.
 */
public class Session {
    private String nickName;
    private String sessionName;
    private String tag;
    private String avatar;
    private String muteNotification;
    @SerializedName("lastMsg")
    private Message message;
    private int count;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMuteNotification() {
        return muteNotification;
    }

    public void setMuteNotification(String muteNotification) {
        this.muteNotification = muteNotification;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
