package com.mintcode.launchr.pojo.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.response.UserIsCompanyResponse;

import java.io.Serializable;

/**
 * Created by JulyYu on 2016/4/6.
 */
public class UserIsCompanyBody implements Serializable{

    private UserIsCompanyResponse response;

    public UserIsCompanyResponse getResponse() {
        return response;
    }
    @JsonProperty("response")
    public void setResponse(UserIsCompanyResponse response) {
        this.response = response;
    }
}
