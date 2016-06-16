package mintcode.com.workhub_im.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mintcode.com.workhub_im.Http.APIService;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.pojo.CompanyEntity;
import mintcode.com.workhub_im.viewholder.CompanyListViewHolder;

/**
 * Created by mark on 16-6-8.
 */
public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListViewHolder> {


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


}

