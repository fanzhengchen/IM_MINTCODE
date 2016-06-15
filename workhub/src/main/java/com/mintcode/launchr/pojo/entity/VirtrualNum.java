package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

/**
 * Created by KevinQiao on 2016/1/28.
 */
public class VirtrualNum implements Serializable{

    private String fieldId;

    private String fieldName;


    private String fieldValue;

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
