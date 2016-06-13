package mintcode.com.workhub_im.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mark on 16-6-13.
 */
public class ResponseBody<Data> {

    @SerializedName("response")
    private Response<Data> response;

    public Response<Data> getResponse() {
        return response;
    }

    public void setResponse(Response<Data> response) {
        this.response = response;
    }
}
