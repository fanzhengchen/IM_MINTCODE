package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.CompanyAppEntity;

import java.util.List;

/**
 * Created by StephenHe on 2016/3/25.
 */
public class CompanyAppResponse extends BaseResponse {
    private List<CompanyAppEntity> Data;

    public List<CompanyAppEntity> getData() {
        return Data;
    }

    @JsonProperty("Data")
    public void setData(List<CompanyAppEntity> data) {
        Data = data;
    }
}
