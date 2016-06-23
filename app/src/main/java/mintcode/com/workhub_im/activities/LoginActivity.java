package mintcode.com.workhub_im.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.widget.EditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.Http.APIService;
import mintcode.com.workhub_im.Http.RequestProvider;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.pojo.CompanyEntity;
import mintcode.com.workhub_im.pojo.HttpResponse;
import mintcode.com.workhub_im.pojo.LoginCompanyData;
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

    private static final Pair<String, String> Stephen = new Pair<>("1712776213@qq.com", "admin");
    private static final Pair<String, String> MarkFan = new Pair<>("markfan@mintcode.com", "xuejunzhongxue8");
    private static final Pair<String, String> julytest = new Pair<>("327549647@qq.com", "admin");

    private static final Pair<String, String> User = Stephen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(LoginActivity.this);

        userNameEditText.setText(User.first);
        passwordEditText.setText(User.second);
        requirePermission();
    }

    @OnClick(R.id.login)
    public void login() {
        String userName = userNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        UserPrefer.setUserName(userName);
        UserPrefer.setPassword(password);
        RequestProvider.userLogin(userName, password, new Callback<HttpResponse<LoginCompanyData>>() {
            @Override
            public void onResponse(Call<HttpResponse<LoginCompanyData>> call, Response<HttpResponse<LoginCompanyData>> response) {
                list = response.body().
                        getBody().
                        getResponse().
                        getData().
                        getEntities();
                accessCompanyList();
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

    private void requirePermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED) {
//            if(ActivityCompat.sh)
//        }
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                AppConsts.REQUEST_CODE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
