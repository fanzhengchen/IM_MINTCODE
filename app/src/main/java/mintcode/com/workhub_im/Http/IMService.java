package mintcode.com.workhub_im.Http;

import com.mintcode.imkit.pojo.LoginResultPOJO;

import mintcode.com.workhub_im.im.pojo.IMLoginRequest;
import mintcode.com.workhub_im.im.pojo.IMLoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mark on 16-6-13.
 */
public interface IMService {

    public static final String IM_LOGIN = "api/login";

    @POST(IM_LOGIN)
    public Call<IMLoginResponse> IMLogin(@Body IMLoginRequest request);


}
