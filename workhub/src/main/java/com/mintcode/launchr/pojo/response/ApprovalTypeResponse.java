package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.ApprovalTypeEntity;

import java.util.List;

/**
 * Created by StephenHe on 2016/1/6.
 */
public class ApprovalTypeResponse extends BaseResponse{
    private List<ApprovalTypeEntity> Data;

    public List<ApprovalTypeEntity> getData() {
        return Data;
    }

    @JsonProperty("Data")
    public void setData(List<ApprovalTypeEntity> data) {
        Data = data;
    }
}
