package mintcode.com.workhub_im.view.chatItemView.leftView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.JsonReader;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.pojo.AttachMsgContent;
import mintcode.com.workhub_im.util.HeadImageUtil;
import mintcode.com.workhub_im.view.chatItemView.baseView.BaseLeftChatView;
import mintcode.com.workhub_im.view.custom.DirectImageView;


/**
 * Created by JulyYu on 2016/6/14.
 */
public class MsgImageLeftView extends BaseLeftChatView {

    @BindView(R.id.iv_image)
    protected DirectImageView mIvImage;

    public MsgImageLeftView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);
        String content = item.getContent();
        AttachMsgContent msgContent = JSON.parseObject(content, AttachMsgContent.class);
        String url = AppConsts.httpIp + "/launchr" + msgContent.getThumbnail();
        HeadImageUtil.getInstance().setChatImageFile(mIvImage, url,
                msgContent.getThumbnailWidth(), msgContent.getThumbnailHeight());
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_chat_image_left;
    }
}
