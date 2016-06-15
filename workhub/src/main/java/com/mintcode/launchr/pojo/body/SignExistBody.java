package com.mintcode.launchr.pojo.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.response.SignExistResponse;

import java.io.Serializable;

/**
 * Created by JulyYu on 2016/4/11.
 */
public class SignExistBody implements Serializable {

    private SignExistResponse response;

    public SignExistResponse getResponse() {
        return response;
    }
    @JsonProperty("response")
    public void setResponse(SignExistResponse response) {
        this.response = response;
    }
}
