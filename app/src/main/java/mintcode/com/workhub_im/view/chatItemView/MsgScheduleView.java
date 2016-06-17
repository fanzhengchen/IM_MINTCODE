package mintcode.com.workhub_im.view.chatItemView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.view.chatItemView.baseView.BaseNormalChatView;


/**
 * Created by JulyYu on 2016/6/14.
 */
public class MsgScheduleView extends BaseNormalChatView {

    @BindView(R.id.iv_accept)
    protected ImageView mIvAccpetState;
    @BindView(R.id.tv_title)
    protected TextView mTvTitle;
    @BindView(R.id.tv_end_time)
    protected TextView mTvMeetingTime;
    @BindView(R.id.tv_place)
    protected TextView mTvPlace;
    @BindView(R.id.layout_bottom_buttons)
    protected LinearLayout mLlAccpetLayout;
    @BindView(R.id.btn_join)
    protected Button mBtnJoin;
    @BindView(R.id.btn_refuse)
    protected Button mBtnRefuse;
    @BindView(R.id.iv_read)
    protected TextView mTvRead;

    public MsgScheduleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_msg_schedule;
    }
}
