package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.TaskStateEntity;

/**
 * Created by StephenHe on 2016/2/24.
 */
public class TaskStateResponse extends BaseResponse{
    private TaskStateEntity Data;

    public TaskStateEntity getData() {
        return Data;
    }

    @JsonProperty("Data")
    public void setData(TaskStateEntity data) {
        Data = data;
    }
}
