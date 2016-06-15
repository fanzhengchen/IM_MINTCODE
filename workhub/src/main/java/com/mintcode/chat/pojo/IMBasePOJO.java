package com.mintcode.chat.pojo;

import java.io.Serializable;

/**
 * Created by KevinQiao on 2016/1/15.
 */
public class IMBasePOJO implements Serializable {

    /** api 动作码 */
    private String action;

    /** 状态码 */
    private int code;

    /** 状态信息 */
    private String message;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 判断返回结果是否为true
     * @return
     */
    public boolean isResultSuccess() {
        return code == 2000 ? true : false;
    }

}
