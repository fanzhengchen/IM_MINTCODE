package mintcode.com.workhub_im.view.chatItemView.rightView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.im.Command;
import mintcode.com.workhub_im.im.image.AttachItem;
import mintcode.com.workhub_im.im.image.MutiSoundUpload;
import mintcode.com.workhub_im.im.pojo.AttachDetail;
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
        String filename = item.getFileName();
        if(TextUtils.isEmpty(filename)){
            AttachItem attach = JSON.parseObject(item.getContent(), AttachItem.class);
            if(attach != null){
                String url = AppConsts.httpIp + "/launchr" + attach.getThumbnail();
                HeadImageUtil.getInstance().setChatImage(mIvImage,url, attach.getThumbnailWidth(), attach.getThumbnailHeight());
            }
        }else{
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(filename, options);// 此时返回的bitmap为null
            HeadImageUtil.getInstance().setChatImageFile(mIvImage, filename,options.outWidth, options.outHeight);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_chat_image_right;
    }
}
