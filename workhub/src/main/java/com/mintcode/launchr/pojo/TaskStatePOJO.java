package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.TaskStateBody;

/**
 * Created by StephenHe on 2016/2/24.
 */
public class TaskStatePOJO extends BasePOJO{
    private TaskStateBody Body;

    public TaskStateBody getBody() {
        return Body;
    }

    @JsonProperty("Body")
    public void setBody(TaskStateBody body) {
        Body = body;
    }
}
