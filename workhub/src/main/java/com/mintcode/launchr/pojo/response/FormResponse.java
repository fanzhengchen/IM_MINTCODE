package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.FormEntity;

/**
 * Created by JulyYu on 2016/4/13.
 */
public class FormResponse extends BaseResponse {

    private FormEntity Data;

    public FormEntity getData() {
        return Data;
    }
    @JsonProperty("Data")
    public void setData(FormEntity data) {
        Data = data;
    }
}
