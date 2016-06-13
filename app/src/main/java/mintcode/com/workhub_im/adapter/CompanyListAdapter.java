package mintcode.com.workhub_im.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.App;
import mintcode.com.workhub_im.Http.APIService;
import mintcode.com.workhub_im.Http.RequestProvider;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.activities.SessionActivity;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.pojo.CompanyEntity;
import mintcode.com.workhub_im.pojo.HttpResponse;
import mintcode.com.workhub_im.pojo.LoginResponse;
import mintcode.com.workhub_im.pojo.LoginUserData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mark on 16-6-8.
 */
public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.CompanyListViewHolder> {


    private static APIService service;
    private static String token = null;

    private ArrayList<CompanyEntity> companyEntities;

    public CompanyListAdapter(ArrayList<CompanyEntity> companyEntities) {
        this.companyEntities = companyEntities;
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
        CompanyEntity entity = companyEntities.get(position);
        holder.companyNameTV.setText(entity.getcName());
        holder.companyCodeTV.setText(entity.getcCode());
        holder.cCode = entity.getcCode();
        holder.showId = entity.getShowId();
    }

    @Override
    public int getItemCount() {
        return (companyEntities == null) ? 0 : companyEntities.size();
    }


    public static class CompanyListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.company_name)
        TextView companyNameTV;
        @BindView(R.id.company_code)
        TextView companyCodeTV;

        String showId;
        String cCode;


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
                    String IMUserName = response.body()
                            .getBody()
                            .getResponse()
                            .getData()
                            .getUserShowId();
                    UserPrefer.setShowId(IMUserName);
                    Context context = view.getContext();
                    context.startActivity(new Intent(context, SessionActivity.class));
                }

                @Override
                public void onFailure(Call<HttpResponse<LoginUserData>> call, Throwable t) {

                }
            });

        }


    }
}
