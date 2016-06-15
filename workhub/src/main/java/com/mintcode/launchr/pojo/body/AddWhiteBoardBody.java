package com.mintcode.launchr.pojo.body;

import com.mintcode.launchr.pojo.response.AddWhiteBiardResponse;

import java.io.Serializable;

/**
 * Created by StephenHe on 2016/1/18.
 */
public class AddWhiteBoardBody implements Serializable {
    private AddWhiteBiardResponse response;

    public AddWhiteBiardResponse getResponse() {
        return response;
    }

    public void setResponse(AddWhiteBiardResponse response) {
        this.response = response;
    }
}
