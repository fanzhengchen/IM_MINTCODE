package mintcode.com.workhub_im.view.chatItemView.baseView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.im.Command;

/**
 * Created by JulyYu on 2016/6/14.
 */
public abstract class BaseRightChatView extends BaseChatView {

    /** 文本已读状态*/
    @BindView(R.id.iv_read)
     TextView mTvRead;
    /** 重点标记*/
    @BindView(R.id.iv_mark_point)
     ImageView mIvMarkPoint;
    /** 提交失败*/
    @BindView(R.id.iv_failed)
     ImageView mIvFailed;
    /** 上传loading*/
    @BindView(R.id.pb_sending_bar)
     ProgressBar mPbLoading;
    /** 转发选择*/
    @BindView(R.id.cb_merge_msg)
     CheckBox mCbMergeMsg;


    public BaseRightChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(getLayoutId(),this);
        ButterKnife.bind(this,view);
    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);
        setReadView(item,mTvRead,false);
    }

    public abstract int getLayoutId();


    private void setReadView(MessageItem item, TextView readView, boolean isGroup) {
        if (!item.getType().equals(Command.EVENT)) {
            if (isGroup) {
                readView.setVisibility(View.GONE);
            } else {
//                if (item.getSent() != Command.SEND_FAILED && item.getIsRead() == 1) {
//                    readView.setVisibility(View.VISIBLE);
//                    readView.setText(mContext.getString(R.string.msg_have_read));
//                } else if (item.getSent() != Command.SEND_FAILED && item.getIsRead() == 0) {
//                    readView.setVisibility(View.VISIBLE);
//                    readView.setText(mContext.getString(R.string.msg_have_accept));
//                }
            }
        }
    }
}
