package com.mintcode.launchr.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by JulyYu on 2016/2/23.
 */
public class TaskCountEntity {



    private String type;

    private int count;

    @JsonProperty("count")
    public void setCount(int count) {
        this.count = count;
    }
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public int getCount() {
        return count;
    }
}
