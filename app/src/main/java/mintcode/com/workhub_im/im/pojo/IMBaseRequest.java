package mintcode.com.workhub_im.im.pojo;

/**
 * Created by mark on 16-6-20.
 */
public class IMBaseRequest {
    private String appName;
    private String userToken;
    private String userName;

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
}
