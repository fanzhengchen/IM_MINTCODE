package com.mintcode.launchr.pojo.entity;

import java.io.Serializable;

/**
 * Created by StephenHe on 2016/3/21.
 */
public class UserUpdateEntity implements Serializable {
    private long lastUpadteTime;
    private String cShowId;
    private String showId;
    private String name;

    public long getLastUpadteTime() {
        return lastUpadteTime;
    }

    public void setLastUpadteTime(long lastUpadteTime) {
        this.lastUpadteTime = lastUpadteTime;
    }

    public String getcShowId() {
        return cShowId;
    }

    public void setcShowId(String cShowId) {
        this.cShowId = cShowId;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
