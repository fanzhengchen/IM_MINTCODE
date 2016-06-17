package mintcode.com.workhub_im.view.chatItemView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import butterknife.BindView;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.view.chatItemView.baseView.BaseNormalChatView;


/**
 * Created by JulyYu on 2016/6/14.
 */
public class EventAlertChatView extends BaseNormalChatView {

    @BindView(R.id.tv_title)
    protected TextView mTvTitle;
    @BindView(R.id.tv_comment)
    protected TextView mTvComment;
    public EventAlertChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_chat_event_alert;
    }
}
