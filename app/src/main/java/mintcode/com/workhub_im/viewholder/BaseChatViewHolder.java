package mintcode.com.workhub_im.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import mintcode.com.workhub_im.view.chatItemView.baseView.BaseChatView;


/**
 * Created by JulyYu on 2016/6/14.
 */
public class BaseChatViewHolder extends RecyclerView.ViewHolder {




    private BaseChatView Content;

    public View mView;

    public BaseChatViewHolder(View itemView) {
        super(itemView);

    }

    public BaseChatViewHolder(View itemView,int viewType){
        super(itemView);
        mView = itemView;
    }


}
