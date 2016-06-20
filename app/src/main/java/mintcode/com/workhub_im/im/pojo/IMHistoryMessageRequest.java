package mintcode.com.workhub_im.im.pojo;

/**
 * Created by mark on 16-6-20.
 */
public class IMHistoryMessageRequest {
    private String appName;
    private String userToken;
    private String userName;
    private String to;
    private String from;
    private long limit;
    private long endTimestamp;

    private IMHistoryMessageRequest(Builder builder) {
        setAppName(builder.appName);
        setUserToken(builder.userToken);
        setUserName(builder.userName);
        setTo(builder.to);
        setFrom(builder.from);
        setLimit(builder.limit);
        setEndTimestamp(builder.endTimestamp);
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

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }


    public static final class Builder {
        private String appName;
        private String userToken;
        private String userName;
        private String to;
        private String from;
        private long limit;
        private long endTimestamp;

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

        public Builder to(String val) {
            to = val;
            return this;
        }

        public Builder from(String val) {
            from = val;
            return this;
        }

        public Builder limit(long val) {
            limit = val;
            return this;
        }

        public Builder endTimestamp(long val) {
            endTimestamp = val;
            return this;
        }

        public IMHistoryMessageRequest build() {
            return new IMHistoryMessageRequest(this);
        }
    }
}
