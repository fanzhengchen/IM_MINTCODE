package mintcode.com.workhub_im.Http;

import mintcode.com.workhub_im.pojo.HttpRequest;
import mintcode.com.workhub_im.pojo.HttpResponse;
import mintcode.com.workhub_im.pojo.LoginCompanyData;
import mintcode.com.workhub_im.pojo.LoginRequestBody;
import mintcode.com.workhub_im.pojo.LoginUserData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mark on 16-6-8.
 */
public interface APIService {

    public static final String USER_LOGIN = "/Base-Module/CompanyUserLogin/CompanyUserValidate";

    public static final String COMPANY_LOGIN = "/Base-Module/CompanyUserLogin";

    public static final String IM_LOGIN = "";


    @POST(USER_LOGIN)
    Call<HttpResponse<LoginCompanyData>> userLogin(@Body HttpRequest<LoginRequestBody> request);

    @POST(COMPANY_LOGIN)
    Call<HttpResponse<LoginUserData>> companyLogin(@Body HttpRequest<LoginRequestBody> request);

}
