package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.UserUpdateBody;

/**
 * Created by StephenHe on 2016/3/21.
 */
public class UserUpdatePOJO extends BasePOJO {
    private UserUpdateBody Body;

    public UserUpdateBody getBody() {
        return Body;
    }

    @JsonProperty("Body")
    public void setBody(UserUpdateBody body) {
        Body = body;
    }
}
