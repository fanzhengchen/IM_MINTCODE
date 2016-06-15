package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.WhiteBoardEntity;

/**
 * Created by StephenHe on 2016/1/18.
 */
public class AddWhiteBiardResponse extends BaseResponse {
    private WhiteBoardEntity Data;

    public WhiteBoardEntity getData() {
        return Data;
    }

    @JsonProperty("Data")
    public void setData(WhiteBoardEntity data) {
        Data = data;
    }
}
