package mintcode.com.workhub_im.pojo;

/**
 * Created by mark on 16-6-8.
 */
public class LoginRequest {

    /**
     * Async : false
     * AuthToken : 41nbaz4LtN2Ke0fV5tGoTqOXY1Q=
     * CompanyCode : mt
     * CompanyShowID : 35b11f42f4522d8924
     * Language : zh-cn
     * LoginName : eeKKN9a7eLsPPObb
     * ResourceUri : /Base-Module/CompanyUserLogin/CompanyUserValidate
     * Type : POST
     * UserName : JulyTEST
     */

    private HeaderBean Header;
    /**
     * param : {"userPassword":"admin","userLoginName":"327549647@qq.com"}
     */

    private BodyBean Body;

    public HeaderBean getHeader() {
        return Header;
    }

    public void setHeader(HeaderBean Header) {
        this.Header = Header;
    }

    public BodyBean getBody() {
        return Body;
    }

    public void setBody(BodyBean Body) {
        this.Body = Body;
    }

    public static class HeaderBean {
        private boolean Async;
        private String AuthToken;
        private String CompanyCode;
        private String CompanyShowID;
        private String Language;
        private String LoginName;
        private String ResourceUri;
        private String Type;
        private String UserName;

        public boolean isAsync() {
            return Async;
        }

        public void setAsync(boolean Async) {
            this.Async = Async;
        }

        public String getAuthToken() {
            return AuthToken;
        }

        public void setAuthToken(String AuthToken) {
            this.AuthToken = AuthToken;
        }

        public String getCompanyCode() {
            return CompanyCode;
        }

        public void setCompanyCode(String CompanyCode) {
            this.CompanyCode = CompanyCode;
        }

        public String getCompanyShowID() {
            return CompanyShowID;
        }

        public void setCompanyShowID(String CompanyShowID) {
            this.CompanyShowID = CompanyShowID;
        }

        public String getLanguage() {
            return Language;
        }

        public void setLanguage(String Language) {
            this.Language = Language;
        }

        public String getLoginName() {
            return LoginName;
        }

        public void setLoginName(String LoginName) {
            this.LoginName = LoginName;
        }

        public String getResourceUri() {
            return ResourceUri;
        }

        public void setResourceUri(String ResourceUri) {
            this.ResourceUri = ResourceUri;
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }
    }

    public static class BodyBean {
        /**
         * userPassword : admin
         * userLoginName : 327549647@qq.com
         */

        private ParamBean param;

        public ParamBean getParam() {
            return param;
        }

        public void setParam(ParamBean param) {
            this.param = param;
        }

        public static class ParamBean {
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
    }
}
