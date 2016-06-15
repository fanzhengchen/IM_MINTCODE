package com.mintcode.launchr.pojo.body;

import com.mintcode.launchr.pojo.response.ApprovalTypeResponse;

import java.io.Serializable;

/**
 * Created by StephenHe on 2016/1/6.
 */
public class ApprovalTypeBody implements Serializable{
    private ApprovalTypeResponse response;

    public ApprovalTypeResponse getResponse() {
        return response;
    }

    public void setResponse(ApprovalTypeResponse response) {
        this.response = response;
    }
}
