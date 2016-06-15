package com.mintcode.launchr.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by JulyYu on 2016/4/6.
 */
public class UserIsCompanyResponse extends BaseResponse {

    private ComPanyPersonData data;


    public ComPanyPersonData getData() {
        return data;
    }
    @JsonProperty("Data")
    public void setData(ComPanyPersonData data) {
        this.data = data;
    }

    public class ComPanyPersonData{
        private String showId;
        private boolean exists;

        public String getShowId() {
            return showId;
        }
        public void setShowId(String showId) {
            this.showId = showId;
        }
        public boolean isExists() {
            return exists;
        }
        @JsonProperty("exists")
        public void setExists(boolean exists) {
            this.exists = exists;
        }
    }
}
