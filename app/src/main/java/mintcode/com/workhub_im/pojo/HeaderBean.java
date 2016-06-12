package mintcode.com.workhub_im.pojo;

/**
 * Created by fanzhengchen on 6/10/16.
 */
public class HeaderBean {
    private boolean IsSuccess;
    private int ResCode;
    private String Reason;

    public boolean isIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(boolean IsSuccess) {
        this.IsSuccess = IsSuccess;
    }

    public int getResCode() {
        return ResCode;
    }

    public void setResCode(int ResCode) {
        this.ResCode = ResCode;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String Reason) {
        this.Reason = Reason;
    }
}
