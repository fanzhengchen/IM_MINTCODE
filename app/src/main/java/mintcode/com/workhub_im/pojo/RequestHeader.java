package mintcode.com.workhub_im.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mark on 16-6-8.
 */
public class RequestHeader {

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

    @SerializedName("CompanyCode")
    private String companyCode;

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

    public static class Builder {

        private RequestHeader param;

        public Builder() {
            param = new RequestHeader();
        }

        public Builder setUserName(String userName) {
            param.setUserName(userName);
            return this;
        }

        public Builder setLoginName(String loginName) {
            param.setLoginName(loginName);
            return this;
        }

        public Builder setCompanyShowId(String showId) {
            param.setCompanyShowId(showId);
            return this;
        }

        public Builder setAuthToken(String token) {
            param.setAuthToken(token);
            return this;
        }

        public Builder setResourceUri(String uri) {
            param.setResourceUri(uri);
            return this;
        }

        public Builder setAsync(boolean async) {
            param.setAsync(async);
            return this;
        }

        public Builder setType(String type) {
            param.setType(type);
            return this;
        }

        public Builder setLanguage(String language) {
            param.setLanguage(language);
            return this;
        }

        public Builder setCompanyCode(String companyCode) {
            param.setCompanyCode(companyCode);
            return this;
        }

        public RequestHeader builder() {
            return param;
        }

    }
}
