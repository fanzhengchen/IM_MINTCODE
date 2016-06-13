package mintcode.com.workhub_im.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mark on 16-6-13.
 */
public class CompanyEntity implements Parcelable {
    @SerializedName("showId")
    private String showId;
    @SerializedName("cCode")
    private String cCode;
    @SerializedName("cName")
    private String cName;

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.showId);
        dest.writeString(this.cCode);
        dest.writeString(this.cName);
    }

    public CompanyEntity() {
    }

    protected CompanyEntity(Parcel in) {
        this.showId = in.readString();
        this.cCode = in.readString();
        this.cName = in.readString();
    }

    public static final Creator<CompanyEntity> CREATOR = new Creator<CompanyEntity>() {
        @Override
        public CompanyEntity createFromParcel(Parcel source) {
            return new CompanyEntity(source);
        }

        @Override
        public CompanyEntity[] newArray(int size) {
            return new CompanyEntity[size];
        }
    };
}
