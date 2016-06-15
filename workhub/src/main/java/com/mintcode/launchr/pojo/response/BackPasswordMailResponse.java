package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.BackPasswordMailEntity;

/**
 * Created by StephenHe on 2016/4/11.
 */
public class BackPasswordMailResponse extends BaseResponse{
    private BackPasswordMailEntity Data;

    public BackPasswordMailEntity getData() {
        return Data;
    }

    @JsonProperty("Data")
    public void setData(BackPasswordMailEntity data) {
        Data = data;
    }
}
