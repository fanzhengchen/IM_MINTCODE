package mintcode.com.workhub_im.Http;

import com.mintcode.imkit.pojo.LoginResultPOJO;

import mintcode.com.workhub_im.pojo.IMLoginRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HEAD;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mark on 16-6-13.
 */
public interface IMService {

    public static final String IM_LOGIN = "api/login";

    @POST(IM_LOGIN)
    public Call<LoginResultPOJO> IMLogin(@Body IMLoginRequest request);
}
