package mintcode.com.workhub_im.view.chatItemView.rightView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import butterknife.BindView;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.view.chatItemView.ChatViewUtil;
import mintcode.com.workhub_im.view.chatItemView.baseView.BaseRightChatView;
import mintcode.com.workhub_im.widget.IMChatManager;


/**
 * Created by JulyYu on 2016/6/14.
 */
public class MsgTextRightView extends BaseRightChatView {

    @BindView(R.id.tv_text)
    protected TextView mTvText;

    public MsgTextRightView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);
        ChatViewUtil.setTextDisplay(mContext,item,mTvText,false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_chat_text_right;
    }
}
