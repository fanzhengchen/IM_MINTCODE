package mintcode.com.workhub_im.im.pojo;

/**
 * Created by mark on 16-6-15.
 */
public class IMBaseResponse {
    private String action;
    private String message;
    private int code;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
