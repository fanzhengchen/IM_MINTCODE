package com.mintcode;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mintcode.LoginAPI.TASKID;

@JsonTypeName(TASKID.LOGIN)
public class LoginPOJO extends BasePOJO {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String userToken;

    public String getUserToken() {

        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

}
