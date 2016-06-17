package mintcode.com.workhub_im.view.chatItemView.leftView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.view.chatItemView.baseView.BaseLeftChatView;


/**
 * Created by JulyYu on 2016/6/14.
 */
public class MsgMergeLeftView extends BaseLeftChatView {

    @BindView(R.id.tv_title)
    protected TextView mTvTitle;
    @BindView(R.id.tv_content1)
    protected TextView mTvContent1;
    @BindView(R.id.tv_content2)
    protected TextView mTvContent2;
    @BindView(R.id.tv_content3)
    protected TextView mTvContent3;
    @BindView(R.id.iv_read)
    protected ImageView mIvRead;

    public MsgMergeLeftView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_chat_merge_left;
    }
}
