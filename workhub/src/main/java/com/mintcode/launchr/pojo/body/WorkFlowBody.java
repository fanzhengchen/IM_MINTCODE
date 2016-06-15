package com.mintcode.launchr.pojo.body;

import com.mintcode.launchr.pojo.response.WorkFlowResponse;

import java.io.Serializable;

/**
 * Created by JulyYu on 2016/4/13.
 */
public class WorkFlowBody implements Serializable{

    private WorkFlowResponse response;

    public WorkFlowResponse getResponse() {
        return response;
    }

    public void setResponse(WorkFlowResponse response) {
        this.response = response;
    }
}
