package com.mintcode.launchr.pojo.body;

import com.mintcode.launchr.pojo.response.TaskStateResponse;

import java.io.Serializable;

/**
 * Created by StephenHe on 2016/2/24.
 */
public class TaskStateBody implements Serializable{
    private TaskStateResponse response;

    public TaskStateResponse getResponse() {
        return response;
    }

    public void setResponse(TaskStateResponse response) {
        this.response = response;
    }
}
