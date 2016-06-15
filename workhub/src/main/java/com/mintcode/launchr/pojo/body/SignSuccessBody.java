package com.mintcode.launchr.pojo.body;

import com.mintcode.launchr.pojo.response.SignSuccessResponse;

import java.io.Serializable;

/**
 * Created by JulyYu on 2016/4/11.
 */
public class SignSuccessBody implements Serializable{

    private SignSuccessResponse response;

    public SignSuccessResponse getResponse() {
        return response;
    }

    public void setResponse(SignSuccessResponse response) {
        this.response = response;
    }
}
