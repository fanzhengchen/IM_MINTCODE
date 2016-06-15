package com.mintcode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mintcode.launchr.api.IMAPI;
import com.mintcode.launchr.pojo.entity.PersonEntity;
import com.mintcode.launchr.pojo.entity.RelationGroupEntity;

import java.util.List;


/**
 * Created by JulyYu on 2016/3/24.
 */
//@JsonTypeName(IMAPI.TASKID.FRIENDS_LIST)
public class RelationGroupPOJO extends DataPOJO {


    private List<RelationGroupEntity> data;


    public  List<RelationGroupEntity> getData() {
        return data;
    }
    @JsonProperty("data")
    public void setData( List<RelationGroupEntity> data) {
        this.data = data;
    }



}
