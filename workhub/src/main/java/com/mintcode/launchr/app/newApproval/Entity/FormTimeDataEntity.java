package com.mintcode.launchr.app.newApproval.Entity;

/**
 * Created by JulyYu on 2016/4/15.
 */
public class FormTimeDataEntity {

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
        long deadline;

        public long getDeadline() {
            return deadline;
        }

        public void setDeadline(long deadline) {
            this.deadline = deadline;
        }
    }
}
