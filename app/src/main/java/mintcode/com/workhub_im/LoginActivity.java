package mintcode.com.workhub_im;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mintcode.com.workhub_im.Http.APIService;
import mintcode.com.workhub_im.pojo.LoginRequest;
import mintcode.com.workhub_im.pojo.LoginResponse;
import mintcode.com.workhub_im.pojo.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mark on 16-6-8.
 */
public class LoginActivity extends Activity {


    @BindView(R.id.user_name)
    EditText userNameEditText;

    @BindView(R.id.password)
    EditText passwordEditText;

    private APIService service;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(LoginActivity.this);
        service = App.getRetrofit().create(APIService.class);
    }

    @OnClick(R.id.login)
    public void login() {
        String userName = userNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        userName = "markfan@mintcode.com";
        password = "xuejunzhongxue8";


        LoginRequest request = new LoginRequest();
        LoginRequest.HeaderBean headerBean = new LoginRequest.HeaderBean();

        headerBean.setResourceUri(APIService.LOGIN);
        headerBean.setUserName(userName);
        headerBean.setType("POST");
        headerBean.setLoginName(userName);
        headerBean.setCompanyCode("mintcode");
        headerBean.setAsync(false);

        LoginRequest.BodyBean bodyBean = new LoginRequest.BodyBean();
        LoginRequest.BodyBean.ParamBean paramBean = new LoginRequest.BodyBean.ParamBean();

        paramBean.setUserLoginName(userName);
        paramBean.setUserPassword(password);
        bodyBean.setParam(paramBean);

        request.setBody(bodyBean);
        request.setHeader(headerBean);

        String requestStr = JSON.toJSONString(request);
        Log.e("FUCK",requestStr);
        try {
            service.login(request).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    String rawString = response.body().toString();
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
