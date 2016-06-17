package mintcode.com.workhub_im.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.activities.ChatActivity;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.db.SessionItem;
import mintcode.com.workhub_im.util.HeadImageUtil;

/**
 * Created by mark on 16-6-16.
 */
public class SessionViewHolder extends RecyclerView.ViewHolder {

    private static final String PATH = "/Base-Module/Annex/Avatar?";
    @BindView(R.id.session_portrait)
    ImageView portrait;
    @BindView(R.id.session_nickname)
    TextView nicknameTextView;
    @BindView(R.id.session_content)
    TextView contentTextView;

    private Context context;

    public SessionViewHolder(View itemView) {
        super(itemView);
//        System.currentTimeMillis();
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessChat(v.getContext());
            }
        });
    }

    public void fillData(SessionItem item) {
        nicknameTextView.setText(item.getNickName());
        contentTextView.setText(item.getContent());
        setAvatar(item);

//        String url = getUrl(item.getOppositeName());
//        Glide.with(context).load(url).into(portrait);
//        String url = AppConsts.ATTACH_PATH
    }

    public void accessChat(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }

    private void setAvatar(SessionItem item) {
//        String url = AppConsts.ATTACH_PATH + PATH;
//        url += "companyCode=" + UserPrefer.getCompanyCode() + "&";
//        url += "width=60&height=60&userName=" + userName + "&";
//        url += "t=" + System.currentTimeMillis();
        HeadImageUtil.getInstance().setAvatarResourceAppendUrl(portrait, item.getOppositeName(), 3, 60, 60);
    }


}
