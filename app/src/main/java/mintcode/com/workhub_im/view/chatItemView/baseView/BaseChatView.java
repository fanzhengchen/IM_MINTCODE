package mintcode.com.workhub_im.view.chatItemView.baseView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import mintcode.com.workhub_im.db.MessageItem;


/**
 * Created by JulyYu on 2016/6/14.
 */
public class BaseChatView extends RelativeLayout implements View.OnLongClickListener,View.OnClickListener{


    protected MessageItem mMessageItem;
    protected Context mContext;

    public BaseChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }


    public void setData(MessageItem item){
        mMessageItem = item;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
