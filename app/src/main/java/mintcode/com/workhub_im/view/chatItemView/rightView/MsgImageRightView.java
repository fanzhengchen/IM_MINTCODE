package mintcode.com.workhub_im.view.chatItemView.rightView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.pojo.AttachMsgContent;
import mintcode.com.workhub_im.util.HeadImageUtil;
import mintcode.com.workhub_im.view.chatItemView.baseView.BaseRightChatView;

/**
 * Created by JulyYu on 2016/6/14.
 */
public class MsgImageRightView extends BaseRightChatView {

    @BindView(R.id.iv_image)
    protected ImageView mIvImage;
    @BindView(R.id.tv_percent)
    protected TextView mTvPercent;

    public MsgImageRightView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(MessageItem item) {
        super.setData(item);
        AttachMsgContent msgContent = JSON.parseObject(item.getContent(), AttachMsgContent.class);
        String url = AppConsts.httpIp + "/launchr" + msgContent.getThumbnail();
        Logger.i(" head url " + url);
//        HeadImageUtil.setImageResuces(mIvImage.getContext(), mIvImage, msgContent.getThumbnailWidth(), msgContent.getThumbnailHeight());
//        Glide.with(mIvImage.getContext()).load(url).into(mIvImage);
        HeadImageUtil.getInstance().setChatImageFile(mIvImage, url, R.drawable.im_default_image,
                msgContent.getThumbnailWidth(), msgContent.getThumbnailHeight());
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_chat_image_right;
    }
}
