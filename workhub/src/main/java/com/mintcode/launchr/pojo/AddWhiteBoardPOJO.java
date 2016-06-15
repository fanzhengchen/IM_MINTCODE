package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.AddWhiteBoardBody;

/**
 * Created by StephenHe on 2016/1/18.
 */
public class AddWhiteBoardPOJO extends BasePOJO {
    private AddWhiteBoardBody Body;

    public AddWhiteBoardBody getBody() {
        return Body;
    }

    @JsonProperty("Body")
    public void setBody(AddWhiteBoardBody body) {
        Body = body;
    }
}
