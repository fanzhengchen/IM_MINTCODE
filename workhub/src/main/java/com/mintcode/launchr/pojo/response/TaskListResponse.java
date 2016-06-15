package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.TaskListEntity;

import java.util.List;

/**
 * Created by JulyYu on 2016/2/23.
 */
public class TaskListResponse extends BaseResponse {



    private List<TaskListEntity> taskDetailEntity;

    public List<TaskListEntity> getData() {
        return taskDetailEntity;
    }
    @JsonProperty("Data")
    public void setgetData(List<TaskListEntity> taskDetailEntity) {
        this.taskDetailEntity = taskDetailEntity;
    }
}
