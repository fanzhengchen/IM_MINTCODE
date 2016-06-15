package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.UserUpdateEntity;

/**
 * Created by StephenHe on 2016/3/21.
 */
public class UserUpdateResponse extends BaseResponse {
    private UserUpdateEntity Data;

    public UserUpdateEntity getData() {
        return Data;
    }

    @JsonProperty("Data")
    public void setData(UserUpdateEntity data) {
        Data = data;
    }
}
