package mintcode.com.workhub_im.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.view.chatItemView.ChatViewUtil;


/**
 * Created by JulyYu on 2016/6/14.
 */
public class BaseChatAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {


    protected List<MessageItem> mListData = new ArrayList<>();


    public BaseChatAdapter(List<MessageItem> items) {
        if (items != null) {
            mListData = items;
        }
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(T holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    @Override
    public int getItemViewType(int position) {
//        return mListData.get(position);
        return ChatViewUtil.getChatViewType(mListData.get(position));
    }
}
