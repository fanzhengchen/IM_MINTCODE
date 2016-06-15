package com.mintcode.launchr.app.newApproval.Entity;

import java.util.List;

/**
 * Created by JulyYu on 2016/4/14.
 */
public class FormMutilDataEntity {

    private String key;
   private List<String>  value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }
}
