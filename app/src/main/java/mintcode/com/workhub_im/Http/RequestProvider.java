package mintcode.com.workhub_im.Http;

import mintcode.com.workhub_im.pojo.LoginCompanyData;
import mintcode.com.workhub_im.pojo.RequestHeader;
import mintcode.com.workhub_im.pojo.HttpRequest;
import mintcode.com.workhub_im.pojo.HttpResponse;
import mintcode.com.workhub_im.pojo.LoginParameters;
import mintcode.com.workhub_im.pojo.LoginRequestBody;
import mintcode.com.workhub_im.pojo.UserLoginData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mark on 16-6-13.
 */
public class RequestProvider {

    private static HttpProvider httpProvider = HttpProvider.getInstance();
    private static APIService apiService = httpProvider.getApiService();
    private static final String POST = "POST";
    private static final String GET = "GET";

    public static void userLogin(String userName, String password, Callback<HttpResponse<LoginCompanyData>> call) {
        HttpRequest<LoginRequestBody> loginReq = new HttpRequest<>();
        RequestHeader head = new RequestHeader.Builder()
                .setType(POST)
                .setResourceUri(APIService.USER_LOGIN)
                .builder();

        LoginParameters params = new LoginParameters();
        params.setUserLoginName(userName);
        params.setUserPassword(password);

        LoginRequestBody body = new LoginRequestBody();
        body.setParam(params);

        loginReq.setHeader(head);
        loginReq.setBody(body);
        apiService.userLogin(loginReq).enqueue(call);
    }
}
