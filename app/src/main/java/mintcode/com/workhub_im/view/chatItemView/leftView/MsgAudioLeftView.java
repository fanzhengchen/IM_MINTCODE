package mintcode.com.workhub_im.view.chatItemView.leftView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.view.chatItemView.baseView.BaseLeftChatView;


/**
 * Created by JulyYu on 2016/6/14.
 */
public class MsgAudioLeftView extends BaseLeftChatView {

    @BindView(R.id.tv_text)
    protected TextView mTvVoiceFrame;
    @BindView(R.id.tv_sound_time)
    protected TextView mTvSoundTime;
    @BindView(R.id.tv_sound_image)
    protected TextView mTvplayAnimation;
    @BindView(R.id.iv_read_mark)
    protected ImageView mIvReadMark;


    public MsgAudioLeftView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);
        String strContent = item.getContent();
//        AudioItem audio = JsonUtil.convertJsonToCommonObj(strContent,
//                AudioItem.class);
//        Drawable drawableVoice = ContextCompat.getDrawable(mContext,R.drawable.icon_playing_left);
//        drawableVoice.setBounds(0, 0, drawableVoice.getIntrinsicWidth(),
//                drawableVoice.getIntrinsicHeight());
//        mTvplayAnimation.setCompoundDrawables(drawableVoice,null,null,null);

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_chat_audio_left;
    }
}
