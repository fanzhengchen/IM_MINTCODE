package mintcode.com.workhub_im.view.chatItemView.rightView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import butterknife.BindView;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.view.chatItemView.baseView.BaseRightChatView;


/**
 * Created by JulyYu on 2016/6/14.
 */
public class MsgAudioRightView extends BaseRightChatView {


    @BindView(R.id.tv_text)
    protected TextView mTvVoiceFrame;
    @BindView(R.id.tv_sound_time)
    protected TextView mTvSoundTime;
    @BindView(R.id.tv_sound_image)
    protected TextView mTvplayAnimation;

    public MsgAudioRightView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_chat_audio_right;
    }
}
