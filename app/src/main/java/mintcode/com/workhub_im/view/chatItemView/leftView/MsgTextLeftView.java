package mintcode.com.workhub_im.view.chatItemView.leftView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import butterknife.BindView;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.view.chatItemView.baseView.BaseLeftChatView;
import mintcode.com.workhub_im.view.chatItemView.ChatViewUtil;

/**
 * Created by JulyYu on 2016/6/14.
 */
public class MsgTextLeftView extends BaseLeftChatView {

    @BindView(R.id.tv_text)
    protected TextView mTvText;

    public MsgTextLeftView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);
        ChatViewUtil.setTextDisplay(mContext,item,mTvText,true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_chat_text_left;
    }
}
