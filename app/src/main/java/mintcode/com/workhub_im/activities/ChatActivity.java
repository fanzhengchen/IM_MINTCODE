package mintcode.com.workhub_im.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.mintcode.imkit.consts.IMConst;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
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
import mintcode.com.workhub_im.im.ServiceManager;
import mintcode.com.workhub_im.im.image.AttachItem;
import mintcode.com.workhub_im.im.image.MultiTaskUpLoad;
import mintcode.com.workhub_im.im.pojo.AttachDetail;
import mintcode.com.workhub_im.im.pojo.IMMessageResponse;
import mintcode.com.workhub_im.view.chatItemView.ChatViewUtil;
import mintcode.com.workhub_im.view.custom.MsgSendView;
import mintcode.com.workhub_im.widget.IMChatManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mark on 16-6-17.
 */
public class ChatActivity extends Activity implements MsgSendView.OnMsgSendListener, SwipeRefreshLayout.OnRefreshListener {
    /**
     * msg type
     */
    public interface HandleMsgType {
        public static final int TYPE_MSG_1 = 0x0001;
        public static final int TYPE_SEND_IMAGE = 0x0002;
        public static final int TYPE_REVIMAGE = 0x0003;
        public static final int TYPE_SEND_AUDIO = 0x0004;
        public static final int TYPE_REVAUDIO = 0x0005;
        public static final int TYPE_RESEND_TEXT = 0x0006;
        public static final int TYPE_SEND_VIDEO = 0x0007;
        public static final int TYPE_REVVIDEO = 0x0008;
        public static final int TYPE_FILE_UPLOAD = 0x0009;
        public static final int TYPE_SEND_TO_TARGET = 0x0010;
        int TYPE_HANDLE_DATA_FINISH = 0x0020;
    }


    public static final String SESSION = "session";
    private static final int LIMIT = 10;
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            Logger.i("send message type " + msg.what);
            Logger.json(JSON.toJSONString(msg.obj));
            switch (what) {
                case HandleMsgType.TYPE_MSG_1: {
                    break;
                }
                case HandleMsgType.TYPE_SEND_IMAGE: {
                    MessageItem imageItem = (MessageItem) msg.obj;
                    if (imageItem.getSent() != Command.SEND_FAILED) {
                        AttachDetail detail = JSON.parseObject(imageItem.getContent(), AttachDetail.class);
                        AttachItem attach = JSON.parseObject(imageItem.getContent(), AttachItem.class);
                        if (detail != null) {
                            attach.setFileSize(detail.getFileSize());
                            attach.setFileUrl(detail.getFileUrl());
                            attach.setThumbnail(detail.getThumbnail());
                        }

                        if (attach != null) {
                            imageItem.setContent(JSON.toJSONString(attach));
                        }
                        imageItem.setActionSend(Command.SEND_SUCCESS);
                        imageItem.setToNickName(mStrTo);
                        imageItem.setNickName(mStrMyName);
                        ServiceManager.getInstance().sendMsg(imageItem);
                    }
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        initData();
        fetchMessageItems();
        UserPrefer.setSendToImUid(mStrTo);
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
                        addMessageItemToUi(item);
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

        if (!items.isEmpty()) {
            endTimeStamp = items.get(size - 1).getCreateDate() + 1;
        } else {
            endTimeStamp = -1;
        }
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
            addMessageItemToUi(textMessage);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == IMConst.REQ_SELECT_IMAGE) {
            ArrayList<String> selected = data.getStringArrayListExtra(IMConst.SELECT_IMAGE_LIST);
            Logger.json(JSON.toJSONString(selected));
            for (String path : selected) {
                MessageItem item = createMessageItem(path);
                AttachDetail attachDetail = createAttachDetail(path);
                item.setContent(JSON.toJSONString(attachDetail));
                MultiTaskUpLoad.getInstance().sendMsg(attachDetail, path, this, handler, item);
                addMessageItemToUi(item);
            }
            MultiTaskUpLoad.getInstance().startNextUpload(false);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private MessageItem createMessageItem(String path) {
        MessageItem item = new MessageItem();
        item.setCmd(ChatViewUtil.TYPE_SEND);
        item.setType(Command.IMAGE);
        item.setSent(Command.STATE_SEND);
        item.setClientMsgId(System.currentTimeMillis());
        item.setCreateDate(System.currentTimeMillis());
        item.setFrom(UserPrefer.getImUsername());
        item.setTo(UserPrefer.getSendToImUid());
        return item;
    }

    private AttachDetail createAttachDetail(String path) {
        File file = new File(path);
        AttachDetail attachDetail = new AttachDetail();
        attachDetail.setFileUrl(path);
        attachDetail.setUserToken(UserPrefer.getImToken());
        attachDetail.setUserName(UserPrefer.getImUsername());
        attachDetail.setSrcOffset(0);
        attachDetail.setFileStatus(1);
        attachDetail.setFileName(file.getName());
        attachDetail.setAppName(AppConsts.appName);
        return attachDetail;
    }

    private void addMessageItemToUi(MessageItem item) {
        mMessageItems.add(0, item);
        mChatAdapter.notifyItemInserted(0);
        recyclerView.smoothScrollToPosition(0);
    }
}
