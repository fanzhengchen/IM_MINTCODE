package com.mintcode.launchr.app.newApproval.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.FormCheckBoxEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JulyYu on 2016/4/20.
 */
public class MessageFormData implements Serializable {

    private String inputType;
    private String labelText;
    private boolean single;
    private String placeholder;
    private String required;
    private boolean selected;
    private String key;
    private String timeType;
    private List<FormCheckBoxEntity> data;

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public List<FormCheckBoxEntity> getData() {
        return data;
    }
    @JsonProperty("data")
    public void setData(List<FormCheckBoxEntity> data) {
        this.data = data;
    }

    public String isRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

}
