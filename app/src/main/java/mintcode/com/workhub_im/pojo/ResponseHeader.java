package mintcode.com.workhub_im.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mark on 16-6-13.
 */
public class ResponseHeader {

    @SerializedName("IsSuccess")
    private boolean isSuccess;
    @SerializedName("ResCode")
    private int resultCode;
    @SerializedName("Reason")
    private String reason;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
