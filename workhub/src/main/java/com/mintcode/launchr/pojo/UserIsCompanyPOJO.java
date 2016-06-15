package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.UserIsCompanyBody;

/**
 * Created by JulyYu on 2016/4/6.
 */
public class UserIsCompanyPOJO extends BasePOJO {

    private UserIsCompanyBody body;

    public UserIsCompanyBody getBody() {
        return body;
    }
    @JsonProperty("Body")
    public void setBody(UserIsCompanyBody body) {
        this.body = body;
    }
}
