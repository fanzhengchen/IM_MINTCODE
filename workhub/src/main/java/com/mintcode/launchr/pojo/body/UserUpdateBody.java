package com.mintcode.launchr.pojo.body;

import com.mintcode.launchr.pojo.response.UserUpdateResponse;

import java.io.Serializable;

/**
 * Created by StephenHe on 2016/3/21.
 */
public class UserUpdateBody implements Serializable {
    private UserUpdateResponse response;

    public UserUpdateResponse getResponse() {
        return response;
    }

    public void setResponse(UserUpdateResponse response) {
        this.response = response;
    }
}
