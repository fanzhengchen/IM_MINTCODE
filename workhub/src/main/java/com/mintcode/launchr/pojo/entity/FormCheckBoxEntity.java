package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

/**
 * Created by JulyYu on 2016/4/13.
 */
public class FormCheckBoxEntity implements Serializable{

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
