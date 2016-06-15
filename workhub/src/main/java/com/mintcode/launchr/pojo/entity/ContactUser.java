package com.mintcode.launchr.pojo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.mintcode.launchr.util.TTJSONUtil;

import org.litepal.crud.DataSupport;

/**
 * Created by KevinQiao on 2016/3/5.
 */
public class ContactUser extends DataSupport implements Parcelable {


    private int id;

    private String userId;

    private String updateTime;

    private String userName;

    private String departId;

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(userId);
        dest.writeString(updateTime);
        dest.writeString(userName);
        dest.writeString(departId);
    }

    public ContactUser(){
        super();
    }
    private ContactUser(Parcel source){
        id = source.readInt();
        userId = source.readString();
        updateTime = source.readString();
        userName = source.readString();
        departId = source.readString();

    }

    public static final Creator<ContactUser> CREATOR = new Creator<ContactUser>() {
        @Override
        public ContactUser createFromParcel(Parcel source) {
            return new ContactUser(source);
        }

        @Override
        public ContactUser[] newArray(int size) {
            return new ContactUser[size];
        }
    };

    public static ContactUser createContactUser(String json){
        return TTJSONUtil.convertJsonToCommonObj(json,ContactUser.class);
    }

}
