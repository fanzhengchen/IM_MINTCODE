package mintcode.com.workhub_im.view.chatItemView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.view.chatItemView.baseView.BaseNormalChatView;


/**
 * Created by JulyYu on 2016/6/14.
 */
public class MsgApproveView extends BaseNormalChatView {

    @BindView(R.id.iv_accept)
    protected ImageView mIvAccpetState;
    @BindView(R.id.tv_title)
    protected TextView mTvTitle;
    @BindView(R.id.tv_type)
    protected TextView mTvType;
    @BindView(R.id.tv_end_time)
    protected TextView mTvEndTime;
    @BindView(R.id.rel_deadline)
    protected RelativeLayout mRlDeadLayout;
    @BindView(R.id.tv_deadline)
    protected TextView mTvDeadLineTime;
    @BindView(R.id.rel_cost)
    protected RelativeLayout mRlCostLayout;
    @BindView(R.id.tv_remark)
    protected TextView mTvRemark;
    @BindView(R.id.layout_bottom_buttons)
    protected LinearLayout mLlAccpetLayout;
    @BindView(R.id.btn_ok)
    protected Button mBtnOk;
    @BindView(R.id.btn_reject)
    protected Button mBtnReject;
    @BindView(R.id.btn_give_back)
    protected Button mBtnCallBack;
    @BindView(R.id.tv_read)
    protected TextView mTvRead;



    public MsgApproveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_msg_approve;
    }
}
