package mintcode.com.workhub_im.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mark on 16-6-13.
 */
public class LoginParameters {
    private String userPassword;
    private String userLoginName;

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserLoginName() {
        return userLoginName;
    }

    public void setUserLoginName(String userLoginName) {
        this.userLoginName = userLoginName;
    }
}
