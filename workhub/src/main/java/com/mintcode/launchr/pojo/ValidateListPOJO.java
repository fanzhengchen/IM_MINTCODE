package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.*;
import com.mintcode.launchr.pojo.entity.ValidateEntity;

import java.util.List;

/**
 * Created by JulyYu on 2016/3/28.
 */
public class ValidateListPOJO extends DataPOJO{

    private List<ValidateEntity> data;

    public List<ValidateEntity> getData() {
        return data;
    }
    @JsonProperty("data")
    public void setData(List<ValidateEntity> data) {
        this.data = data;
    }
}
