package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.CompanyAppBody;

/**
 * Created by StephenHe on 2016/3/25.
 */
public class CompanyAppPOJO extends BasePOJO {
    private CompanyAppBody Body;

    public CompanyAppBody getBody() {
        return Body;
    }

    @JsonProperty("Body")
    public void setBody(CompanyAppBody body) {
        Body = body;
    }
}
