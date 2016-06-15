package com.mintcode.launchr.pojo.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.response.TaskListResponse;

import java.io.Serializable;

/**
 * Created by JulyYu on 2016/2/23.
 */
public class TaskListBody implements Serializable {

    public TaskListResponse getResponse() {
        return Response;
    }

    @JsonProperty("response")
    public void setResponse(TaskListResponse response) {
        Response = response;
    }

    private TaskListResponse Response;

}
