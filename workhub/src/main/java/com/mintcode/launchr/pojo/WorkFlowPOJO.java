package com.mintcode.launchr.pojo;

import com.mintcode.launchr.pojo.body.WorkFlowBody;

/**
 * Created by JulyYu on 2016/4/13.
 */
public class WorkFlowPOJO extends BasePOJO{

    private WorkFlowBody Body;

    public WorkFlowBody getBody() {
        return Body;
    }

    public void setBody(WorkFlowBody body) {
        Body = body;
    }
}
