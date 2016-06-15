package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

/**
 * Created by JulyYu on 2016/4/11.
 */
public class SignExistEntity implements Serializable{

    private int IsExist;
    private int userCount;
    private String companyShowId;
    private String companyCode;
    private String companyName;

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getIsExist() {
        return IsExist;
    }

    public void setIsExist(int isExist) {
        IsExist = isExist;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
