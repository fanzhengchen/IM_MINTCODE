package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.body.ApproveFiledBody;

/**
 * Created by JulyYu on 2016/1/22.
 */
public class ApprovalFiledPOJO extends BasePOJO{

    private ApproveFiledBody Body;

    public ApproveFiledBody getBody() {
        return Body;
    }

    @JsonProperty("Body")
    public void setBody(ApproveFiledBody body) {
        Body = body;
    }
}
