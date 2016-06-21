package mintcode.com.workhub_im.activities;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.adapter.UserChatAdapter;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.daohelper.SessionItemDaoHelper;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.db.SessionItem;
import mintcode.com.workhub_im.im.Command;
import mintcode.com.workhub_im.im.IMAPIProvider;
import mintcode.com.workhub_im.im.IMManager;
import mintcode.com.workhub_im.im.ServiceManager;
import mintcode.com.workhub_im.im.pojo.AttachDetailResponse;
import mintcode.com.workhub_im.im.pojo.IMMessageResponse;
import mintcode.com.workhub_im.view.chatItemView.ChatViewUtil;
import mintcode.com.workhub_im.view.custom.MsgSendView;
import mintcode.com.workhub_im.widget.IMChatManager;
import mintcode.com.workhub_im.widget.auido.AudioItem;
import mintcode.com.workhub_im.widget.auido.MutiSoundUpload;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mark on 16-6-17.
 */
public class ChatActivity extends Activity implements MsgSendView.OnMsgSendListener {


    public static final String SESSION = "session";
    private static final int LIMIT = 20;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mChatAdapter);
        mSendBar.setOnMsgSendListener(this);
    }

    private void initData() {
        mStrUid = UserPrefer.getImUsername();
        mStrMyName = UserPrefer.getUserName();
        mStrToken = UserPrefer.getImToken();
        Long SessionId = getIntent().getLongExtra(SESSION, 0);
        SessionItem item = SessionItemDaoHelper.getInstance().getSession(SessionId);
        if (item != null) {
            mStrTo = item.getOppositeName();
            mStrToNikeName = item.getNickName();
            mTool.setTitle(mStrToNikeName);
        }

        IMAPIProvider.getHistoryMessage(mStrToken, mStrMyName, mStrUid, mStrTo, LIMIT, -1, new Callback<IMMessageResponse>() {
            @Override
            public void onResponse(Call<IMMessageResponse> call, Response<IMMessageResponse> response) {
                List<MessageItem> items = response.body().getMsg();
                for (MessageItem item : items) {
                    item.setCmd(ChatViewUtil.TYPE_RECV);
                    UserPrefer.updateMsgId(item.getMsgId());
                }
                mMessageItems.addAll(items);
                mChatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<IMMessageResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void textMsgSend(String msg) {
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(this, "消息为空", Toast.LENGTH_SHORT).show();
        }
        MessageItem textMessage = IMChatManager.getInstance().sendTextMsg(this, msg, mStrUid, mStrMyName, mStrTo, mStrToNikeName);
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(this, "消息为空", Toast.LENGTH_SHORT).show();
        } else {
            mMessageItems.add(textMessage);
            mChatAdapter.notifyDataSetChanged();
        }
        Toast.makeText(ChatActivity.this, "sending", Toast.LENGTH_SHORT).show();
        textMessage.setUserName(UserPrefer.getUserName());
        textMessage.setFrom(UserPrefer.getImUsername());
        ServiceManager.getInstance().sendMsg(textMessage);

    }

    @Override
    public void voiceMsgSend(String path, String time) {
        if(TextUtils.isEmpty(path)){
            Toast.makeText(ChatActivity.this,"时间太短无法录音，请重试", Toast.LENGTH_SHORT).show();
            return;
        }
//        mImageViewRecordingIndicator.setVisibility(View.GONE);
//        File f = new File(path);
//        if (f.exists()) {
//            AttachDetailResponse detail = getDetail(path);
//            MessageItem item = setupSendItem();
//            item.setFileName(path);
//            item.setTimeText(time);
//            item.setType(Command.AUDIO);
//            AudioItem audio = new AudioItem();
//            audio.setAudioLength(Integer.parseInt(time));
//            audio.setFileUrl(path);
//            item.setContent(audio.toString());
//            mListData.add(item);
//            mChatListAdapter.notifyDataSetChanged();
            // 开启上传语音文件 任务
//            MutiSoundUpload.getInstance().sendSound(detail, f, context, mUIHandler, item);
//        }
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
