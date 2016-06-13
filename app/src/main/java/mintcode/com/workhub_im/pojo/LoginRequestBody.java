package mintcode.com.workhub_im.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mark on 16-6-13.
 */
public class LoginRequestBody {
    @SerializedName("param")
    private LoginParameters param;

    public LoginParameters getParam() {
        return param;
    }

    public void setParam(LoginParameters param) {
        this.param = param;
    }


}
