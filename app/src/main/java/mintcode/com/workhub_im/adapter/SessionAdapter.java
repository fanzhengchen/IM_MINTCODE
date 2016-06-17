package mintcode.com.workhub_im.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.SessionItem;
import mintcode.com.workhub_im.util.DensityUtil;
import mintcode.com.workhub_im.viewholder.SessionViewHolder;

/**
 * Created by mark on 16-6-16.
 */
public class SessionAdapter extends RecyclerView.Adapter<SessionViewHolder> {


    private List<SessionItem> items;
    private final int cellHeight = DensityUtil.dip2px(70);


    public SessionAdapter(List<SessionItem> items) {
        this.items = items;
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_session, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, cellHeight));
        SessionViewHolder holder = new SessionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {
        SessionItem item = items.get(position);
        holder.fillData(item);

    }

    @Override
    public int getItemCount() {
        return (items == null) ? 0 : items.size();
    }
}
