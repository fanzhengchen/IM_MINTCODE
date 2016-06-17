package mintcode.com.workhub_im.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.adapter.UserChatAdapter;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.daohelper.SessionItemDaoHelper;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.db.SessionItem;
import mintcode.com.workhub_im.im.IMManager;
import mintcode.com.workhub_im.view.custom.MsgSendView;
import mintcode.com.workhub_im.widget.IMChatManager;

/**
 * Created by mark on 16-6-17.
 */
public class ChatActivity extends Activity implements MsgSendView.OnMsgSendListener{


    public static final String SESSTION = "sesstion";

    @BindView(R.id.tool)
    Toolbar mTool;
    @BindView(R.id.chat_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.view_send_bar)
    protected MsgSendView mSendBar;

    UserChatAdapter mChatAdapter;
    List<MessageItem> mMessageItems = new ArrayList<>();

    String mStrUid;
    String mStrToken;
    String mStrMyName;
    String mStrTo;
    String mStrToNikeName;
    SessionItem mSesssionItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        initData();

        mChatAdapter = new UserChatAdapter(mMessageItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(mChatAdapter);
        mSendBar.setOnMsgSendListener(this);


    }

    private void initData() {
        mStrUid = UserPrefer.getShowId();
        mStrMyName = UserPrefer.getUserName();
        mStrToken = UserPrefer.getImToken();
        Long SessionId = getIntent().getLongExtra(SESSTION,0);
        SessionItem item = SessionItemDaoHelper.getInstance().getSession(SessionId);
        if(item != null){
            mStrTo = item.getOppositeName();
            mStrToNikeName = item.getNickName();
            mTool.setTitle(mStrToNikeName);
        }
    }

    @Override
    public void textMsgSend(String msg) {
        if(TextUtils.isEmpty(msg)){
            Toast.makeText(this,"消息为空",Toast.LENGTH_SHORT).show();
        }
        MessageItem textMessage = IMChatManager.getInstance().sendTextMsg(this,msg,mStrUid,mStrMyName,mStrTo,mStrToNikeName);
        if(TextUtils.isEmpty(msg)){
            Toast.makeText(this,"消息为空",Toast.LENGTH_SHORT).show();
        }else{
            mMessageItems.add(textMessage);
            mChatAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void voiceMsgSend(String path, String time) {

    }

    @Override
    public void taskMsgSend() {

    }

    @Override
    public void recordSound(boolean isRecording) {

    }

    @Override
    public void atPerson() {

    }

    @Override
    public void markImprotantList() {

    }

    @Override
    public void sendForwordList() {

    }
}
