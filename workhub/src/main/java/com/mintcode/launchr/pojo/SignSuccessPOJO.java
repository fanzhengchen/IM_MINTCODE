package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.SignSuccessBody;

/**
 * Created by JulyYu on 2016/4/11.
 */
public class SignSuccessPOJO extends BasePOJO {

    private SignSuccessBody Body;

    public SignSuccessBody getBody() {
        return Body;
    }

    @JsonProperty("Body")
    public void setBody(SignSuccessBody body) {
        Body = body;
    }
}
