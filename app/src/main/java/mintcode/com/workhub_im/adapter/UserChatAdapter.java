package mintcode.com.workhub_im.adapter;

import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.view.chatItemView.baseView.BaseChatView;
import mintcode.com.workhub_im.view.chatItemView.ChatViewUtil;
import mintcode.com.workhub_im.viewholder.BaseChatViewHolder;

/**
 * Created by JulyYu on 2016/6/14.
 */
public class UserChatAdapter extends BaseChatAdapter<BaseChatViewHolder>{

    public UserChatAdapter(List<MessageItem> items) {
        super(items);
    }

    @Override
    public BaseChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = ChatViewUtil.getChatTypeView(parent,viewType);

        return new BaseChatViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(BaseChatViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        MessageItem messageItem = mListData.get(position);
        ((BaseChatView)holder.mView).setData(messageItem);
    }
}
