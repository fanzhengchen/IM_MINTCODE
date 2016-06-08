package mintcode.com.workhub_im.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mark on 16-6-8.
 */
public class Request {
    @SerializedName("LoginName")
    private String loginName;

    @SerializedName("UserName")
    private String userName;

    @SerializedName("CompanyShowID")
    private String companyShowId;

    @SerializedName("AuthToken")
    private String authToken;

    @SerializedName("ResourceUri")
    private String resourceUri;

    @SerializedName("Async")
    private boolean async;

    @SerializedName("Type")
    private String type;

    @SerializedName("Language")
    private String language;

    @SerializedName("body")
    private LoginRequest loginBody;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LoginRequest getLoginBody() {
        return loginBody;
    }

    public void setLoginBody(LoginRequest loginBody) {
        this.loginBody = loginBody;
    }
}
