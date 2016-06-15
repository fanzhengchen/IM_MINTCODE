package com.mintcode.launchr.pojo.response;

import com.mintcode.launchr.pojo.entity.WorkFlowEntity;

/**
 * Created by JulyYu on 2016/4/13.
 */
public class WorkFlowResponse extends BaseResponse{

    private WorkFlowEntity Data;

    public WorkFlowEntity getData() {
        return Data;
    }

    public void setData(WorkFlowEntity data) {
        Data = data;
    }
}
