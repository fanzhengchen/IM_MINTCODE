package mintcode.com.workhub_im.view.chatItemView.leftView;

import android.content.Context;
import android.util.AttributeSet;

import butterknife.BindView;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.view.chatItemView.baseView.BaseLeftChatView;
import mintcode.com.workhub_im.view.custom.DirectImageView;


/**
 * Created by JulyYu on 2016/6/14.
 */
public class MsgImageLeftView extends BaseLeftChatView {

    @BindView(R.id.iv_image)
    protected DirectImageView mIvImage;

    public MsgImageLeftView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_chat_image_left;
    }
}
