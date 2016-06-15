package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.BackPasswordMailBody;

/**
 * Created by StephenHe on 2016/4/11.
 */
public class BackPasswordMailPOJO extends BasePOJO{
    private BackPasswordMailBody Body;

    public BackPasswordMailBody getBody() {
        return Body;
    }

    @JsonProperty("Body")
    public void setBody(BackPasswordMailBody body) {
        Body = body;
    }
}
