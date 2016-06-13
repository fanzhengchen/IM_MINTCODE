package mintcode.com.workhub_im.pojo;

import com.google.gson.annotations.SerializedName;

import java.lang.ref.PhantomReference;

/**
 * Created by mark on 16-6-13.
 */
public class UserLoginData {
    @SerializedName("U_SHOW_ID")
    private String userShowId;
    @SerializedName("LAST_LOGIN_TOKEN")
    private String lastLoginToken;
    @SerializedName("U_TRUE_NAME")
    private String userTrueName;
    @SerializedName("U_NAME")
    private String userName;
    @SerializedName("C_SHOW_ID")
    private String companyShowId;
    @SerializedName("IS_ADMIN")
    private boolean isAdmin;
    @SerializedName("C_CODE")
    private String companyCode;
    @SerializedName("loginName")
    private String loginName;
    @SerializedName("mail")
    private String mail;
    @SerializedName("job")
    private String job;
    @SerializedName("lang")
    private String language;

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUserShowId() {
        return userShowId;
    }

    public void setUserShowId(String userShowId) {
        this.userShowId = userShowId;
    }

    public String getLastLoginToken() {
        return lastLoginToken;
    }

    public void setLastLoginToken(String lastLoginToken) {
        this.lastLoginToken = lastLoginToken;
    }

    public String getUserTrueName() {
        return userTrueName;
    }

    public void setUserTrueName(String userTrueName) {
        this.userTrueName = userTrueName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompanyShowId() {
        return companyShowId;
    }

    public void setCompanyShowId(String companyShowId) {
        this.companyShowId = companyShowId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
