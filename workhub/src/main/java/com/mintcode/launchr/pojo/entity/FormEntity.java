package com.mintcode.launchr.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by JulyYu on 2016/4/13.
 */
public class FormEntity {

    private String Id;
    private String formId;
    private List<formData> form;

    public String getId() {
        return Id;
    }
    @JsonProperty("id")
    public void setId(String id) {
        Id = id;
    }

    public String getFormId() {
        return formId;
    }
    @JsonProperty("formId")
    public void setFormId(String formId) {
        this.formId = formId;
    }

    public List<formData> getForm() {
        return form;
    }
    @JsonProperty("form")
    public void setForm(List<formData> form) {
        this.form = form;
    }


}



