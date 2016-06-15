package com.mintcode.launchr.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by JulyYu on 2016/1/22.
 */
public class ApprovalFieldEntity {



    private String F_KEY;
        private String F_NAME;
        private int IS_DEFAULT;
        private int F_SORT;

    public String getF_KEY() {
        return F_KEY;
    }
    @JsonProperty("F_KEY")
    public void setF_KEY(String f_KEY) {
        F_KEY = f_KEY;
    }

    public String getF_NAME() {
        return F_NAME;
    }
    @JsonProperty("F_NAME")
    public void setF_NAME(String f_NAME) {
        F_NAME = f_NAME;
    }

    public int getIS_DEFAULT() {
        return IS_DEFAULT;
    }
    @JsonProperty("IS_DEFAULT")
    public void setIS_DEFAULT(int IS_DEFAULT) {
        this.IS_DEFAULT = IS_DEFAULT;
    }

    public int getF_SORT() {
        return F_SORT;
    }
    @JsonProperty("F_SORT")
    public void setF_SORT(int f_SORT) {
        F_SORT = f_SORT;
    }
}
