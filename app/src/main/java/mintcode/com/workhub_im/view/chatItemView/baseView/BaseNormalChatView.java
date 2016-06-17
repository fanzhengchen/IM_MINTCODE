package mintcode.com.workhub_im.view.chatItemView.baseView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;

/**
 * Created by JulyYu on 2016/6/14.
 */
public abstract class BaseNormalChatView extends BaseChatView {


    /** 名字*/
    @BindView(R.id.tv_chat_name)
     TextView mTvUserName;
    /** 头像*/
    @BindView(R.id.iv_head_icon)
     ImageView mIvUserHead;
    /** 时间*/
    @BindView(R.id.tv_time)
     TextView mTvTime;

    public BaseNormalChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(getLayoutId(),this);
        ButterKnife.bind(this,view);

    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);
    }

    public abstract int getLayoutId();
}
