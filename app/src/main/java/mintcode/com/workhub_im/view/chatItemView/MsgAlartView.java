package mintcode.com.workhub_im.view.chatItemView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.im.Command;
import mintcode.com.workhub_im.pojo.MessageEventEntity;
import mintcode.com.workhub_im.view.chatItemView.baseView.BaseChatView;

/**
 * Created by JulyYu on 2016/6/16.
 */
public class MsgAlartView extends BaseChatView {

    @BindView(R.id.tv_msg)
    protected TextView mTvAlart;

    public MsgAlartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.item_msg_alart,this);
        ButterKnife.bind(this,view);
    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);
        mTvAlart.setText(item.getContent());
    }
}
