package mintcode.com.workhub_im.Http;

import mintcode.com.workhub_im.pojo.LoginRequest;
import mintcode.com.workhub_im.pojo.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mark on 16-6-8.
 */
public interface APIService {

    public static final String USER_LOGIN = "/Base-Module/CompanyUserLogin/CompanyUserValidate";

    public static final String COMPANY_LOGIN = "/Base-Module/CompanyUserLogin";

    @POST(USER_LOGIN)
    Call<LoginResponse> userLogin(@Body LoginRequest request);

    @POST(COMPANY_LOGIN)
    Call<LoginResponse> companyLogin(@Body LoginRequest request);
}
