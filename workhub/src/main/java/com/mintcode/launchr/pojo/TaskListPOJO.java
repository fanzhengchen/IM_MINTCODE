package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.TaskListBody;

/**
 * Created by JulyYu on 2016/2/23.
 */
public class TaskListPOJO extends  BasePOJO {


    public TaskListBody getBody() {
        return Body;
    }
    @JsonProperty("Body")
    public void setBody(TaskListBody body) {
        Body = body;
    }

    private TaskListBody Body;

}
