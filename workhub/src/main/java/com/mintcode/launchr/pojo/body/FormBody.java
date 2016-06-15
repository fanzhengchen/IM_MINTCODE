package com.mintcode.launchr.pojo.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.response.FormResponse;

/**
 * Created by JulyYu on 2016/4/13.
 */
public class FormBody {

    private FormResponse response;

    public FormResponse getResponse() {
        return response;
    }
    @JsonProperty("response")
    public void setResponse(FormResponse response) {
        this.response = response;
    }
}
