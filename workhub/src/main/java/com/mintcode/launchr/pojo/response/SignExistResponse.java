package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.SignExistEntity;

/**
 * Created by JulyYu on 2016/4/11.
 */
public class SignExistResponse extends BaseResponse {

    private SignExistEntity Data;

    public SignExistEntity getData() {
        return Data;
    }
    @JsonProperty("Data")
    public void setData(SignExistEntity data) {
        Data = data;
    }
}
