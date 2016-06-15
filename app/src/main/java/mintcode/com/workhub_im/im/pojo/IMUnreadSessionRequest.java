package mintcode.com.workhub_im.im.pojo;

/**
 * Created by mark on 16-6-15.
 */
public class IMUnreadSessionRequest {
    private String appName;
    private String userToken;
    private String userName;
    private int start;
    private int end;

    private IMUnreadSessionRequest(Builder builder) {
        setAppName(builder.appName);
        setUserToken(builder.userToken);
        setUserName(builder.userName);
        setStart(builder.start);
        setEnd(builder.end);
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }


    public static final class Builder {
        private String appName;
        private String userToken;
        private String userName;
        private int start;
        private int end;

        public Builder() {
        }

        public Builder appName(String val) {
            appName = val;
            return this;
        }

        public Builder userToken(String val) {
            userToken = val;
            return this;
        }

        public Builder userName(String val) {
            userName = val;
            return this;
        }

        public Builder start(int val) {
            start = val;
            return this;
        }

        public Builder end(int val) {
            end = val;
            return this;
        }

        public IMUnreadSessionRequest build() {
            return new IMUnreadSessionRequest(this);
        }
    }
}
