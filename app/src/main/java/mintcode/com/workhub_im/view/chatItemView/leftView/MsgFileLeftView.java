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
public class MsgFileLeftView extends BaseLeftChatView {

    @BindView(R.id.iv_file_image)
    protected ImageView mIvFile;
    @BindView(R.id.tv_file_name)
    protected TextView mTvFile;
    @BindView(R.id.tv_file_size)
    protected TextView mTvFileSize;


    public MsgFileLeftView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_chat_file_left;
    }



}
