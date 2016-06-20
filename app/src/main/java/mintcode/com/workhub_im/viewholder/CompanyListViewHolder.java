package mintcode.com.workhub_im.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.Http.RequestProvider;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.activities.MainActivity;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.daohelper.BaseDaoHelper;
import mintcode.com.workhub_im.im.IMAPIProvider;
import mintcode.com.workhub_im.im.IMManager;
import mintcode.com.workhub_im.im.pojo.IMLoginResponse;
import mintcode.com.workhub_im.im.pojo.Info;
import mintcode.com.workhub_im.pojo.HttpResponse;
import mintcode.com.workhub_im.pojo.LoginUserData;
import mintcode.com.workhub_im.service.PushService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mark on 16-6-16.
 */
public class CompanyListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.company_name)
    public TextView companyNameTV;
    @BindView(R.id.company_code)
    public TextView companyCodeTV;

    public String showId;
    public String cCode;


    public CompanyListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyLogin(v);
            }
        });
    }

    public void companyLogin(final View view) {
        String userName = UserPrefer.getUserName();
        String password = UserPrefer.getPassword();
        RequestProvider.companyLogin(userName, password, cCode, new Callback<HttpResponse<LoginUserData>>() {
            @Override
            public void onResponse(Call<HttpResponse<LoginUserData>> call, Response<HttpResponse<LoginUserData>> response) {
                handleCompanyLoginResponse(response, view.getContext());
            }

            @Override
            public void onFailure(Call<HttpResponse<LoginUserData>> call, Throwable t) {

            }
        });
    }

    private void handleCompanyLoginResponse(Response<HttpResponse<LoginUserData>> response, Context context) {
        LoginUserData data = response.body()
                .getBody()
                .getResponse()
                .getData();

        Info info = new Info();
        info.setNickName(data.getUserTrueName());
        info.setUserName(data.getUserName());
        String infoStr = JSON.toJSONString(info);
        UserPrefer.setInfo(infoStr);

        String IMUserName = data.getUserShowId();
        String dbName = IMUserName + "_" + cCode;

        UserPrefer.setImUsername(IMUserName);
        UserPrefer.setCompanyCode(cCode);
        UserPrefer.setDbName(dbName);
        BaseDaoHelper.createDB(dbName);
        login(IMUserName, context);
    }

    private void login(String showId, final Context context) {
        IMAPIProvider.imLogin(showId, new Callback<IMLoginResponse>() {
            @Override
            public void onResponse(Call<IMLoginResponse> call, Response<IMLoginResponse> response) {
                if (TextUtils.equals(response.body().getMessage(), AppConsts.SUCCESS)) {
                    handleIMLoginResponse(response, context);
                }
            }

            @Override
            public void onFailure(Call<IMLoginResponse> call, Throwable t) {

            }
        });
    }

    private void handleIMLoginResponse(Response<IMLoginResponse> response, Context context) {
        String token = response.body().getUserToken();
        UserPrefer.setImToken(token);
//        IMManager.getInstance().startService(PushService.class, PushService.ACTION_CONNECT);
        IMManager.getInstance().startIM();
        startActivity(context, new Intent(context, MainActivity.class));
    }

    void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }

}
