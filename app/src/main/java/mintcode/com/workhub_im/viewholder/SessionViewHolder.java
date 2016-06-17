package mintcode.com.workhub_im.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.activities.ChatActivity;
import mintcode.com.workhub_im.db.SessionItem;

/**
 * Created by mark on 16-6-16.
 */
public class SessionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.session_portrait)
    ImageView portrait;
    @BindView(R.id.session_nickname)
    TextView nicknameTextView;
    @BindView(R.id.session_content)
    TextView contentTextView;

    public SessionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
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
    }

    public void accessChat(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }


}
