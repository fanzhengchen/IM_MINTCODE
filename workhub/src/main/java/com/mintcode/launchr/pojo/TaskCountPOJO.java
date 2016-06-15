package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.TaskCountBody;

/**
 * Created by JulyYu on 2016/2/23.
 */
public class TaskCountPOJO extends BasePOJO{




    private TaskCountBody body;

    public TaskCountBody getBody() {
        return body;
    }
    @JsonProperty("Body")
    public void setBody(TaskCountBody body) {
        this.body = body;
    }
}
