package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.ApprovalTypeBody;

/**
 * Created by StephenHe on 2016/1/6.
 */
public class ApprovalTypePOJO extends BasePOJO{
    private ApprovalTypeBody Body;

    public ApprovalTypeBody getBody() {
        return Body;
    }

    @JsonProperty("Body")
    public void setBody(ApprovalTypeBody body) {
        Body = body;
    }
}
