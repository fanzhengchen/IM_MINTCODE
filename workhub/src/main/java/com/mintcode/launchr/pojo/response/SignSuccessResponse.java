package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.SignSuccessEntity;

/**
 * Created by JulyYu on 2016/4/11.
 */
public class SignSuccessResponse extends BaseResponse {

    private SignSuccessEntity Data;

    public SignSuccessEntity getData() {
        return Data;
    }

    @JsonProperty("Data")
    public void setData(SignSuccessEntity data) {
        Data = data;
    }
}
