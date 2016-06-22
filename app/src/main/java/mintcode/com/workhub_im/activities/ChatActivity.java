package mintcode.com.workhub_im.activities;

import android.app.Activity;
import android.app.Service;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.adapter.UserChatAdapter;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.callback.ChatMessageListener;
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
public class ChatActivity extends Activity implements MsgSendView.OnMsgSendListener, SwipeRefreshLayout.OnRefreshListener {


    public static final String SESSION = "session";
    private static final int LIMIT = 20;
    private RecyclerView.ItemAnimator itemAnimator;

    @BindView(R.id.tool)
    Toolbar mTool;
    @BindView(R.id.chat_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.chat_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.view_send_bar)
    protected MsgSendView mSendBar;
    @BindView(R.id.iv_record_voice)
    protected ImageView mIvRecordVoice;

    UserChatAdapter mChatAdapter;
    List<MessageItem> mMessageItems = new ArrayList<>();

    String mStrUid;
    String mStrToken;
    String mStrMyName;
    String mStrTo;
    String mStrToNikeName;
    SessionItem mSesssionItem;
    long endTimeStamp = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        initData();
        fetchMessageItems();
        itemAnimator = new DefaultItemAnimator();
        refreshLayout.setOnRefreshListener(this);
        mChatAdapter = new UserChatAdapter(mMessageItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        recyclerView.setAdapter(mChatAdapter);
        recyclerView.setItemAnimator(itemAnimator);
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

        ServiceManager.getInstance().setChatMessageListener(new ChatMessageListener() {
            @Override
            public void receiveMessage(final MessageItem item) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMessageItems.add(0, item);
//                        mChatAdapter.notifyDataSetChanged();
                        mChatAdapter.notifyItemInserted(0);
                        recyclerView.scrollToPosition(0);
                    }
                });
            }
        });
    }

    private void fetchMessageItems() {
        IMAPIProvider.getHistoryMessage(mStrToken, mStrMyName, mStrUid, mStrTo, LIMIT, endTimeStamp, new Callback<IMMessageResponse>() {
            @Override
            public void onResponse(Call<IMMessageResponse> call, Response<IMMessageResponse> response) {
                handleMessage(response.body().getMsg());
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<IMMessageResponse> call, Throwable t) {

            }
        });
    }

    private void handleMessage(List<MessageItem> items) {
        if (items == null) {
            return;
        }
        for (MessageItem item : items) {
            if (TextUtils.equals(item.getFrom(), mStrUid)) {
                item.setCmd(ChatViewUtil.TYPE_SEND);
            } else {
                item.setCmd(ChatViewUtil.TYPE_RECV);
            }
            UserPrefer.updateMsgId(item.getMsgId());
        }

        int start = mMessageItems.size() + 1;
        int size = items.size();

        mMessageItems.addAll(items);
        mChatAdapter.notifyItemRangeInserted(start, size);
        if (endTimeStamp != -1) {
            recyclerView.smoothScrollToPosition(mChatAdapter.getItemCount() - 1);
        } else {
            recyclerView.smoothScrollToPosition(0);
        }
        endTimeStamp = items.get(size - 1).getCreateDate();
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
            mMessageItems.add(0, textMessage);
//            mChatAdapter.notifyDataSetChanged();
            mChatAdapter.notifyItemInserted(0);
            recyclerView.scrollToPosition(0);
        }
        Toast.makeText(ChatActivity.this, "sending", Toast.LENGTH_SHORT).show();
        textMessage.setUserName(UserPrefer.getUserName());
        textMessage.setFrom(UserPrefer.getImUsername());
        ServiceManager.getInstance().sendMsg(textMessage);

    }

    @Override
    public void voiceMsgSend(String path, String time) {
        if (TextUtils.isEmpty(path)) {
            Toast.makeText(ChatActivity.this, "时间太短无法录音，请重试", Toast.LENGTH_SHORT).show();
            return;
        }
        MessageItem AudioMessage = IMChatManager.getInstance().sendAudio(ChatActivity.this, path, time, mStrToken, mStrUid, mStrTo, AppConsts.APP_NAME);
        mMessageItems.add(AudioMessage);
        mChatAdapter.notifyDataSetChanged();
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
        if (isRecording) {
            mIvRecordVoice.setVisibility(View.VISIBLE);
            AnimationDrawable drawable = (AnimationDrawable) mIvRecordVoice.getBackground();
            drawable.start();
        } else {
            mIvRecordVoice.setVisibility(View.GONE);
        }

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

    @Override
    public void onRefresh() {
        fetchMessageItems();
    }
}
