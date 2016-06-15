package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.SignExistBody;

/**
 * Created by JulyYu on 2016/4/11.
 */
public class SignExistPOJO extends BasePOJO{

    private SignExistBody Body;

    public SignExistBody getBody() {
        return Body;
    }
    @JsonProperty("Body")
    public void setBody(SignExistBody body) {
        Body = body;
    }
}
