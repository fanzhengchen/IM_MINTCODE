package mintcode.com.workhub_im.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.App;
import mintcode.com.workhub_im.Http.APIService;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.activities.SessionActivity;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.pojo.LoginRequest;
import mintcode.com.workhub_im.pojo.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mark on 16-6-8.
 */
public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.CompanyListViewHolder> {


    private List<LoginResponse.BodyBean.ResponseBean.DataBean.CompanyListBean> list;
    private LoginResponse.BodyBean.ResponseBean.DataBean.CompanyListBean bean;
    private static APIService service;
    private static String token = null;

    public CompanyListAdapter(List<LoginResponse.BodyBean.ResponseBean.DataBean.CompanyListBean> listBeen) {
        this.list = listBeen;
        service = App.getApiService();
    }

    @Override
    public CompanyListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(parent.getContext(), R.layout.item_company, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        CompanyListViewHolder holder = new CompanyListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CompanyListViewHolder holder, int position) {
        holder.companyNameTV.setText(list.get(position).getCName());
        holder.companyCodeTV.setText(list.get(position).getCCode());
        holder.showId = list.get(position).getShowId();
    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : list.size();
    }


    public static class CompanyListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.company_name)
        TextView companyNameTV;
        @BindView(R.id.company_code)
        TextView companyCodeTV;

        String showId;


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
            String userName = "markfan@mintcode.com";
            String password = "xuejunzhongxue8";


            LoginRequest request = new LoginRequest();
            LoginRequest.HeaderBean headerBean = new LoginRequest.HeaderBean();

            headerBean.setResourceUri(APIService.USER_LOGIN);
            headerBean.setUserName(userName);
            headerBean.setType("POST");
            headerBean.setLoginName(userName);
            headerBean.setCompanyCode(companyCodeTV.getText().toString());
            headerBean.setAsync(false);

            LoginRequest.BodyBean bodyBean = new LoginRequest.BodyBean();
            LoginRequest.BodyBean.ParamBean paramBean = new LoginRequest.BodyBean.ParamBean();

            paramBean.setUserLoginName(userName);
            paramBean.setUserPassword(password);
            bodyBean.setParam(paramBean);

            request.setBody(bodyBean);
            request.setHeader(headerBean);

            service.companyLogin(request).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    LoginResponse.BodyBean.ResponseBean.DataBean dataBean = response.body().
                            getBody().getResponse().getData();
                    token = dataBean.getValidatorToken();
                    Context context = view.getContext();
                    UserPrefer.setShowId(showId);
                    UserPrefer.setImToken(token);
                    context.startActivity(new Intent(context, SessionActivity.class));
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                }
            });
        }

//        private void imLogin() {
//            IMNewApi.getInstance().
//        }


    }
}
