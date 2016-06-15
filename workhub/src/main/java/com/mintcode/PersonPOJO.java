package com.mintcode;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.PersonEntity;

import java.util.List;

/**
 * Created by JulyYu on 2016/3/24.
 */
//@JsonTypeName(IMAPI.TASKID.SEARCH_USER)
public class PersonPOJO extends DataPOJO{

    private List<PersonEntity> data;

    public List<PersonEntity> getData() {
        return data;
    }
    @JsonProperty("data")
    public void setData(List<PersonEntity> data) {
        this.data = data;
    }
}
