package mintcode.com.workhub_im.im.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mark on 16-6-15.
 */
public class Session {
    private String nickName;
    private String sessionName;
//    private String opp
    private String tag;
    private String avatar;
    private String muteNotification;
    private LastMsg lastMsg;
    private long modified;
    private int count;

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

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

    public LastMsg getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(LastMsg lastMsg) {
        this.lastMsg = lastMsg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
