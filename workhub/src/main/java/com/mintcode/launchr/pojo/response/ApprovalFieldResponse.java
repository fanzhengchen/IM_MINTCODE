package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.ApprovalFieldEntity;

import java.util.List;

/**
 * Created by JulyYu on 2016/1/22.
 */
public class ApprovalFieldResponse extends BaseResponse {

    private List<ApprovalFieldEntity> Data;

    public List<ApprovalFieldEntity> getData() {
        return Data;
    }

    @JsonProperty("Data")
    public void setData(List<ApprovalFieldEntity> data) {
        Data = data;
    }
}
