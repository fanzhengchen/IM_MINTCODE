package mintcode.com.workhub_im.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.R;

/**
 * Created by mark on 16-6-17.
 */
public class ChatActivity extends Activity {

    @BindView(R.id.chat_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
    }
}
