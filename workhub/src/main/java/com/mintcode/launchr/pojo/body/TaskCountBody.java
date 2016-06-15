package com.mintcode.launchr.pojo.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.response.TaskCountResponse;
import com.mintcode.launchr.pojo.response.TaskResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JulyYu on 2016/2/23.
 */
public class TaskCountBody implements Serializable {



    private TaskCountResponse taskResponse;

    public TaskCountResponse getTaskResponse() {
        return taskResponse;
    }
    @JsonProperty("response")
    public void setTaskResponse(TaskCountResponse taskResponse) {
        this.taskResponse = taskResponse;
    }
}
