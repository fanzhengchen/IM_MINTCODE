package mintcode.com.workhub_im.view.chatItemView.leftView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.BindView;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.view.chatItemView.baseView.BaseNormalChatView;


/**
 * Created by JulyYu on 2016/6/14.
 */
public class MsgTaskLeftView extends BaseNormalChatView {

    @BindView(R.id.tv_title)
    protected TextView mTvTitle;
    @BindView(R.id.tv_end)
    protected TextView mTvEndTime;
    @BindView(R.id.tv_project_name)
    protected TextView mTvProjectName;
    @BindView(R.id.tv_state_name)
    protected TextView mTvStateName;
    @BindView(R.id.tv_task_progress)
    protected TextView mTvTaskProgress;
    @BindView(R.id.tv_priority)
    protected TextView mTvPriorityState;
    @BindView(R.id.tv_priority_text)
    protected TextView mTvPriorityText;
    @BindView(R.id.tv_read)
    protected TextView mTvRead;
    @BindView(R.id.cb_merge_msg)
    protected CheckBox mCbMergeMsg;

    public MsgTaskLeftView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_chat_task_left;
    }
}
