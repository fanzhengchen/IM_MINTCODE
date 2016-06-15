package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.FormBody;

/**
 * Created by JulyYu on 2016/4/13.
 */
public class FormPOJO extends BasePOJO {

    private FormBody Body;

    public FormBody getBody() {
        return Body;
    }
    @JsonProperty("Body")
    public void setBody(FormBody body) {
        Body = body;
    }
}
