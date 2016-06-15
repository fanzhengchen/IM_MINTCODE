package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.TaskCountEntity;

import java.util.List;

/**
 * Created by JulyYu on 2016/2/23.
 */
public class TaskCountResponse extends BaseResponse {


    private List<TaskCountEntity> Data;

    public List<TaskCountEntity> getData() {
        return Data;
    }

    @JsonProperty("Data")
    public void setData(List<TaskCountEntity> data) {
        Data = data;
    }
}
