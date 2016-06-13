package mintcode.com.workhub_im.pojo;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Body;

/**
 * Created by mark on 16-6-13.
 */
public class HttpResponse<Data> {

    @SerializedName("Header")
    private ResponseHeader header;
    @SerializedName("Body")
    private ResponseBody<Data> body;

    public ResponseHeader getHeader() {
        return header;
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }

    public ResponseBody<Data> getBody() {
        return body;
    }

    public void setBody(ResponseBody<Data> body) {
        this.body = body;
    }
}
