package mintcode.com.workhub_im.view.chatItemView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import butterknife.BindView;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.pojo.MessageEventEntity;
import mintcode.com.workhub_im.pojo.MsgInfoEntity;
import mintcode.com.workhub_im.util.HeadImageUtil;
import mintcode.com.workhub_im.view.chatItemView.baseView.BaseNormalChatView;


/**
 * Created by JulyYu on 2016/6/14.
 */
public class EventAlertChatView extends BaseNormalChatView {

    @BindView(R.id.tv_title)
    protected TextView mTvTitle;
    @BindView(R.id.tv_msg)
    protected TextView mTvComment;
    public EventAlertChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);
        String cotent = item.getContent();
        MessageEventEntity eventEntity = JSON.parseObject(cotent,MessageEventEntity.class);
        if(eventEntity != null){
            String content = eventEntity.getMsgContent();
            mTvTitle.setText(content);
            String msgInfo = eventEntity.getMsgInfo();
            MsgInfoEntity entity = JSON.parseObject(msgInfo,MsgInfoEntity.class);
            mTvComment.setText(entity.getComment());
            mTvUserName.setText(eventEntity.getMsgFrom());
            HeadImageUtil.getInstance().setAvatarResourceWithUserId(mIvUserHead,eventEntity.getMsgFromID(),0,60,60);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_chat_event_alert;
    }
}
