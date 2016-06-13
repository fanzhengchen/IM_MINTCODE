package mintcode.com.workhub_im.im.pojo;

/**
 * Created by mark on 16-6-13.
 */
public class IMLoginRequest {
    private String appName;
    private String appToken;
    private String userName;
    private String deviceUUID;
    private String deviceName;
    private String os;
    private String osVer;
    private String appVer;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeviceUUID() {
        return deviceUUID;
    }

    public void setDeviceUUID(String deviceUUID) {
        this.deviceUUID = deviceUUID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVer() {
        return osVer;
    }

    public void setOsVer(String osVer) {
        this.osVer = osVer;
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public static class Builder {
        IMLoginRequest request;

        public Builder() {
            request = new IMLoginRequest();
        }

        public Builder setAppName(String appName) {
            request.setAppName(appName);
            return this;
        }

        public Builder setAppToken(String appToken) {
            request.setAppToken(appToken);
            return this;
        }

        public Builder setUserName(String userName) {
            request.setUserName(userName);
            return this;
        }

        public Builder setDeviceUUID(String deviceUUID) {
            request.setDeviceUUID(deviceUUID);
            return this;
        }

        public Builder setDeviceName(String deviceName) {
            request.setDeviceName(deviceName);
            return this;
        }

        public Builder setOS(String os) {
            request.setOs(os);
            return this;
        }

        public Builder setOsVer(String osVer) {
            request.setOsVer(osVer);
            return this;
        }

        public Builder setAppVer(String appVer) {
            request.setAppVer(appVer);
            return this;
        }

        public IMLoginRequest builder() {
            return request;
        }
    }
}
