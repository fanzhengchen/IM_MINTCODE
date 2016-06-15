package com.mintcode.launchr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.DataPOJO;
import com.mintcode.launchr.pojo.entity.ChatGroupEntity;

import java.util.List;

/**
 * Created by JulyYu on 2016/5/4.
 */
public class ChatGroupPOJO extends DataPOJO {

    private List<ChatGroupEntity> data;

    public List<ChatGroupEntity> getData() {
        return data;
    }
    @JsonProperty("data")
    public void setData(List<ChatGroupEntity> data) {
        this.data = data;
    }
}
