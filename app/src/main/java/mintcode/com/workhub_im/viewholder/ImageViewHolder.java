package mintcode.com.workhub_im.viewholder;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.beans.GlobalBeans;
import mintcode.com.workhub_im.callback.ChangeSelectedNumber;

/**
 * Created by mark on 16-6-23.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder {

    private static final String UPLOAD = AppConsts.httpIp + "/launchr/api/upload";
    @BindView(R.id.id_item_image)
    ImageView itemImageView;
    @BindView(R.id.id_item_select)
    ImageButton itemSelect;
    private Context context;
    private String path;
    private Handler handler = GlobalBeans.getInstance().getHandler();
    private String toUid;
    private Boolean isChecked;
    private HashSet<String> set;
    private ChangeSelectedNumber selectedNumber;

    public ImageViewHolder(View itemView, HashSet<String> set, ChangeSelectedNumber selectedNumber) {
        super(itemView);
        this.set = set;
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        this.selectedNumber = selectedNumber;
    }

    public void loadImage(String path) {
        Glide.with(context).load(path).into(itemImageView);
        this.path = path;
        if (set.contains(path)) {
            itemSelect.setImageResource(R.drawable.icon_blue_checked);
        } else {
            itemSelect.setImageResource(R.drawable.icon_unchecked);
        }
    }

    @OnClick({R.id.id_item_image, R.id.id_item_select})
    public void check() {
        if (set.contains(path)) {
            itemSelect.setImageResource(R.drawable.icon_unchecked);
            set.remove(path);
        } else {
            if (set.size() >= 9) {
                return;
            }
            set.add(path);
            itemSelect.setImageResource(R.drawable.icon_blue_checked);
        }
        selectedNumber.notifyNumberChanged(set.size());

    }


    private void uploadImage() {
        //        File file = new File(path);
//        AttachDetail attachDetail = new AttachDetail();
//        attachDetail.setAppName(AppConsts.appName);
//        attachDetail.setFileName(file.getName());
//        attachDetail.setFileStatus(1);
//        attachDetail.setSrcOffset(0);
//        attachDetail.setUserName(UserPrefer.getImUsername());
//        attachDetail.setUserToken(UserPrefer.getImToken());
//
//        MessageItem item = new MessageItem();
//        item.setCmd(ChatViewUtil.TYPE_SEND);
//        item.setType(Command.IMAGE);
//        item.setSent(Command.STATE_SEND);
//        item.setClientMsgId(System.currentTimeMillis());
//        item.setCreateDate(System.currentTimeMillis());
//        item.setFrom(UserPrefer.getImUsername());
//        item.setTo(UserPrefer.getSendToImUid());
//        item.setContent(path);
//        item.setFileName(path);
//
//
//        MultiTaskUpLoad.getInstance().sendMsg(attachDetail, path, context, handler, item);
//        MultiTaskUpLoad.getInstance().startNextUpload(false);
//        Uploader.uploadPic(UPLOAD, file, )
    }

}
