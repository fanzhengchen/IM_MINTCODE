package mintcode.com.workhub_im.view.chatItemView.rightView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;


import butterknife.BindView;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.view.chatItemView.baseView.BaseRightChatView;

/**
 * Created by JulyYu on 2016/6/14.
 */
public class MsgMergeRightView extends BaseRightChatView {

    @BindView(R.id.tv_title)
    protected TextView mTvTitle;
    @BindView(R.id.tv_content1)
    protected TextView mTvContent1;
    @BindView(R.id.tv_content2)
    protected TextView mTvContent2;
    @BindView(R.id.tv_content3)
    protected TextView mTvContent3;
    public MsgMergeRightView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_chat_merge_right;
    }
}
