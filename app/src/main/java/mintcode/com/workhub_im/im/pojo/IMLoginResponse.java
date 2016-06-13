package mintcode.com.workhub_im.im.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mark on 16-6-13.
 */
public class IMLoginResponse implements Parcelable {
    private String userToken;
    private String uid;
    private String code;
    private String action;
    private String message;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userToken);
        dest.writeString(this.uid);
        dest.writeString(this.code);
        dest.writeString(this.action);
        dest.writeString(this.message);
    }

    public IMLoginResponse() {
    }

    protected IMLoginResponse(Parcel in) {
        this.userToken = in.readString();
        this.uid = in.readString();
        this.code = in.readString();
        this.action = in.readString();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<IMLoginResponse> CREATOR = new Parcelable.Creator<IMLoginResponse>() {
        @Override
        public IMLoginResponse createFromParcel(Parcel source) {
            return new IMLoginResponse(source);
        }

        @Override
        public IMLoginResponse[] newArray(int size) {
            return new IMLoginResponse[size];
        }
    };
}
