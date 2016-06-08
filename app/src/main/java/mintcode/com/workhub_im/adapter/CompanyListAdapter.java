package mintcode.com.workhub_im.adapter;

import android.service.voice.VoiceInteractionService;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.pojo.LoginResponse;

/**
 * Created by mark on 16-6-8.
 */
public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.CompanyListViewHolder> {


    private List<LoginResponse.BodyBean.ResponseBean.DataBean.CompanyListBean> list;


    public CompanyListAdapter(List<LoginResponse.BodyBean.ResponseBean.DataBean.CompanyListBean> listBeen) {
        this.list = listBeen;
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


        public CompanyListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
