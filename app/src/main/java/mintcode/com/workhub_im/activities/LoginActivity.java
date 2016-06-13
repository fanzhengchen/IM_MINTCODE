package mintcode.com.workhub_im.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.Http.APIService;
import mintcode.com.workhub_im.Http.RequestProvider;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.pojo.CompanyEntity;
import mintcode.com.workhub_im.pojo.HttpResponse;
import mintcode.com.workhub_im.pojo.LoginCompanyData;
import mintcode.com.workhub_im.pojo.UserLoginData;
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
    private ArrayList<CompanyEntity> list = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(LoginActivity.this);
        userNameEditText.setText("markfan@mintcode.com");
        passwordEditText.setText("xuejunzhongxue8");
    }

    @OnClick(R.id.login)
    public void login() {
        String userName = userNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        RequestProvider.userLogin(userName, password, new Callback<HttpResponse<LoginCompanyData>>() {
            @Override
            public void onResponse(Call<HttpResponse<LoginCompanyData>> call, Response<HttpResponse<LoginCompanyData>> response) {
                list = response.body().
                        getBody().
                        getResponse().
                        getData().
                        getEntities();
                accessCompanyList();
                System.out.println("fuck");
            }

            @Override
            public void onFailure(Call<HttpResponse<LoginCompanyData>> call, Throwable t) {

            }
        });
    }

    private void accessCompanyList() {
        Intent intent = new Intent(this, CompanyListActivity.class);
        intent.putParcelableArrayListExtra(AppConsts.COMPANY_LIST, list);
        startActivity(intent);
    }
}
