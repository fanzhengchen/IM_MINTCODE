package com.mintcode.launchr.app.newApproval.Entity;

/**
 * Created by JulyYu on 2016/4/14.
 */
public class FormTimesDataEntity {

    private String key;
    private Timevalues value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Timevalues getValue() {
        return value;
    }

    public void setValue(Timevalues value) {
        this.value = value;
    }

    public static class Timevalues{
        long startTime;
        long endTime;

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }
    }
}
