package mintcode.com.workhub_im.Http;

import mintcode.com.workhub_im.pojo.LoginRequest;
import mintcode.com.workhub_im.pojo.LoginResponse;
import mintcode.com.workhub_im.pojo.Request;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mark on 16-6-8.
 */
public interface APIService {

    public static final String LOGIN = "/Base-Module/CompanyUserLogin/CompanyUserValidate";

    @POST(LOGIN)
    Call<LoginResponse> login(@Body LoginRequest request);
}
