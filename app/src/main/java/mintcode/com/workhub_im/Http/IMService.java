package mintcode.com.workhub_im.Http;

import com.mintcode.imkit.pojo.LoginResultPOJO;

import mintcode.com.workhub_im.im.pojo.IMLoginRequest;
import mintcode.com.workhub_im.im.pojo.IMLoginResponse;
import mintcode.com.workhub_im.im.pojo.IMSessionResponse;
import mintcode.com.workhub_im.im.pojo.IMUnreadSessionRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by mark on 16-6-13.
 */
public interface IMService {

    public static final String IM_LOGIN = "api/login";

    public static final String IM_UNREAD_SESSION = "api/unreadsession";

    @POST(IM_LOGIN)
    public Call<IMLoginResponse> IMLogin(@Body IMLoginRequest request);

    @POST(IM_UNREAD_SESSION)
    public Call<IMSessionResponse> getIMUnreadSession(@Body IMUnreadSessionRequest request);

}
