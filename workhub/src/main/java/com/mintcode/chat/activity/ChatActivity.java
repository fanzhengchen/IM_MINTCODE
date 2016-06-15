package com.mintcode.chat.activity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.mintcode.DataPOJO;
import com.mintcode.HistoryMessagePOJO;
import com.mintcode.anim.MAnimController;
import com.mintcode.cache.CacheManager;
import com.mintcode.chat.ChatListAdapter;
import com.mintcode.chat.audio.AudioItem;
import com.mintcode.chat.audio.AudioPlayingAnimation;
import com.mintcode.chat.emoji.EmojiParser;
import com.mintcode.chat.emoji.ParseEmojiMsgUtil;
import com.mintcode.chat.emoji.SelectFaceHelper;
import com.mintcode.chat.emoji.SelectFaceHelper.OnFaceOprateListener;
import com.mintcode.chat.entity.HistoryMsgEntity;
import com.mintcode.chat.entity.Info;
import com.mintcode.chat.entity.MergeEntity;
import com.mintcode.chat.entity.ResendEntity;
import com.mintcode.chat.entity.User;
import com.mintcode.chat.image.AttachDetail;
import com.mintcode.chat.image.AttachItem;
import com.mintcode.chat.image.ImageGridAdapter;
import com.mintcode.chat.image.MutiSoundUpload;
import com.mintcode.chat.image.MutiTaskUpLoad;
import com.mintcode.chat.image.MutiTextUpload;
import com.mintcode.chat.pojo.SessionHistoryMessagePOJO;
import com.mintcode.chat.user.GroupInfo;
import com.mintcode.chat.user.GroupInfoDBService;
import com.mintcode.chat.user.GroupInfoPOJO;
import com.mintcode.chat.util.AudioRecordPlayUtil;
import com.mintcode.chat.util.Keys;
import com.mintcode.chat.util.UploadFileTask;
import com.mintcode.chat.widget.ChatListView;
import com.mintcode.chat.widget.ChatListView.OnActionDownListener;
import com.mintcode.chat.widget.ChooseDialog;
import com.mintcode.chat.widget.ResendDialogView;
import com.mintcode.im.Command;
import com.mintcode.im.IMManager;
import com.mintcode.im.database.KeyValueDBService;
import com.mintcode.im.database.MessageDBService;
import com.mintcode.im.database.SessionDBService;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.entity.SessionItem;
import com.mintcode.im.listener.OnIMMessageListener;
import com.mintcode.im.service.PushService;
import com.mintcode.im.util.JsonUtil;
import com.mintcode.im.util.MessageNotifyManager;
import com.mintcode.launchr.R;
import com.mintcode.launchr.activity.LoginActivity;
import com.mintcode.launchr.activity.LoginWorkHubActivity;
import com.mintcode.launchr.api.IMAPI;
import com.mintcode.launchr.api.IMNewApi;
import com.mintcode.launchr.app.meeting.activity.RemindActivity;
import com.mintcode.launchr.app.newApproval.activity.ApprovalMainActivity;
import com.mintcode.launchr.app.newSchedule.activity.ScheduleMainActivity;
import com.mintcode.launchr.app.newSchedule.view.SelectAddEventPopWindowView;
import com.mintcode.launchr.app.newTask.TaskUtil;
import com.mintcode.launchr.app.newTask.activity.TaskMainActivity;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.message.activity.ChooseMemberActivity;
import com.mintcode.launchr.message.activity.GroupSettingActivity;
import com.mintcode.launchr.message.activity.HistorySearchActivity;
import com.mintcode.launchr.message.activity.MessageRecordActivity;
import com.mintcode.launchr.message.activity.PersonSettingActivity;
import com.mintcode.launchr.message.view.ChatAddScheduleWindow;
import com.mintcode.launchr.message.view.ChatAddTaskWindow;
import com.mintcode.launchr.photo.activity.PhotoActivity;
import com.mintcode.launchr.pojo.entity.MessageEventTaskEntity;
import com.mintcode.launchr.pojo.entity.MessageInfoEntity;
import com.mintcode.launchr.pojo.entity.TaskProjectListEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.MixPanelUtil;
import com.mintcode.launchr.util.NetWorkUtil;
import com.mintcode.launchr.util.TTDensityUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.TimeFormatUtil;
import com.mintcode.launchr.view.ChatSettingPopWindow;
import com.mintcode.launchr.view.ChatSettingPopWindow.ChatSettingListener;
import com.mintcode.launchrnetwork.MTHttpParameters;
import com.mintcode.launchrnetwork.OnResponseListener;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//import org.xmlpull.v1.XmlPullParserFactory;

/**
 * 聊天
 * iv_group
 *
 * @author KevinQiao
 */
public class ChatActivity extends BaseActivity implements OnIMMessageListener,
        OnClickListener, OnRefreshListener<ListView>, SelectAddEventPopWindowView.PopWindowListener, LoaderManager.LoaderCallbacks<List<MessageItem>>, SensorEventListener,
        AdapterView.OnItemClickListener {

    private static final int REQ_FROM_GALLERY = 1001;// 来自本地图片
    private static final int REQ_FROM_CAMERA = 1002;// 来自拍照
    private static final int REQ_FROM_FILM = 1003;// 来自视频
    private static final int REQ_FROM_DEL_HISTORY = 1004;
    private static final int REQ_FROM_CHAT_HISTORY = 1005; //消息记录返回操作
    private static final int REQ_FROM_CHOOSE_NEXT_PEOPLE = 1006;  //选择下一联系人
    private static final int REQ_FROM_SEARCH_MESSAGE = 1007; //搜索消息记录
    private static final int REQ_FROM_CHOOSE_PROJECT = 1008; //选择项目
    private static final int REQ_FROM_CHOOSE_MEMBER = 1009; //参与人员
    private static final int REQ_FROM_CHOOSE_SEND_PERSON = 1010; //参与人员
    private static final int REQA_FROM_CHOOSE_REMIND_TYPE = 1011;//消息转日程提醒方式
    private static final int REQA_FROM_CHOOSE_MERGE_MEMBER = 1012; //消息转发选择联系人

    private String mPhotoPath;


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

    /**
     * 聊天输入框 容器
     */
    private RelativeLayout mRelInputContainer;
    /**
     * 输入框
     */
    private EditText mEtInput;
    /**
     * 发送按钮
     */
    private ImageView mIvSend;

    /**
     * 聊天相关的一些参数
     */
    private String mToken;
    private String mUid;
    // 存放昵称
    private IMManager manager;

    /**
     * 消息列表
     */
    private ChatListView mListView;
    private List<MessageItem> mListData = new ArrayList<>();
    /**
     * KEY-VALUE 内部存放uid和token等数据
     */
    private KeyValueDBService mValueDBService;

    /**
     * 聊天对象
     */
    private User mUser;
    public static String mTo = "";
    public static String toNickName;

    private ChatListAdapter mChatListAdapter;

    private ImageView mIvBack;
    private TextView mTvTitle;
    private ImageView mIvGroup;
    private ImageView mIvSearch;
    @Bind(R.id.tv_cancel)
    protected TextView mTvMoreCancel;

    private Context context;

    /**
     * 按住说话
     */
    private Button mBtnInputVoice;
    /**
     * 语音按钮
     */
    private CheckBox mBtnMicrophone;
    /**
     * 按下说话时的动画
     */
    private ImageView mImageViewRecordingIndicator;
    private View mViewRecordingContainer;

    /**
     * 输入框的两种状态
     */
    private enum InputState {
        Text, Voice
    }

    private InputState mInputState = InputState.Text;
//    private String pageActioin;
    /**
     * 更多按钮
     */
    private CheckBox mChMore;
    private LinearLayout mLlMoreContainer;
    private ImageView mIvPhoto;
    private ImageView mIvCamera;
    private ImageView mIvFilm;
    private ImageView mIvTask;
    /**
     * 未读数量
     */
    private TextView mTvUnReadNum;

    /**
     * 点击更多 也有两种状态 显示发送图片等，或者弹出键盘
     */
    private enum MoreState {
        More, Input
    }

    private MoreState mMoreState = MoreState.More;

    /**
     * 输入法管理器
     */
    private InputMethodManager inputMethodManager;

    /**
     * 录音管理工具
     */
    private AudioRecordPlayUtil mAudioRecordPlayerUtil;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;

    /**
     * 表情按钮
     */
    private ImageView mIvEmoji;

    private RelativeLayout mRelBottomTask;
    private RelativeLayout mRelBottomSchedule;
    private RelativeLayout mRelBottomApprovel;

    /**
     * 转发
     */
    private LinearLayout mLinMoreContent;
    private RelativeLayout mRelMarkList;
    private RelativeLayout mRelForward;

    /**
     * 1是合并转发，2是逐条转发
     */
    private int mergeMsg = 0;

    /**
     * 任务
     */
    private ChatAddTaskWindow addTaskWindow;

    /**
     * 消息转日程
     */
    private ChatAddScheduleWindow mChatAddSchedule;

    private SelectFaceHelper mFaceHelper;

    private View addFaceToolView;
    private boolean isVisbilityFace;

    private int start = 0;
    private int count = 50;
    private final static int STEP = 50;
    private int mListTopSelectionIndex = 0;

    private Animation mAnimZoomIn;
    private Animation mAnimZoomOut;
    private MAnimController mAnimControllerZoomIn;
    private MAnimController mAnimControllerZoomOut;
    private MessageItem mResendItem;
    private String appName;
    private String myName;

    private SharedPreferences mSharedPreferences;
    private int mInputType = 0;


    private HashSet<MessageInfoEntity> mSet = new HashSet<MessageInfoEntity>();
    long ss = 0;

    private boolean mIsRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsRunning = true;
        ss = System.currentTimeMillis();
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        context = ChatActivity.this;
        // 感光传感初始化
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);//TYPE_LIGHT
        // 屏幕设置
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(32, "MyPower");//第一个参数为电源锁级别，第二个是日志tag

        initViews();
        loadSettings();
        String myInfoStr = KeyValueDBService.getInstance().find(
                com.mintcode.im.util.Keys.INFO);
        Info myInfo = JsonUtil.convertJsonToCommonObj(myInfoStr, Info.class);
        myName = myInfo.getNickName();
        appName = KeyValueDBService.getInstance().find(
                com.mintcode.im.util.Keys.APPNAME);
        mUnReadNumHandler.sendEmptyMessage(MSG);
        MessageNotifyManager.cancelNotification();

        MutiSoundUpload.getInstance();

        if (savedInstanceState != null) {
            mPhotoPath = savedInstanceState.getString("photo_path");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("photo_path", mPhotoPath);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void loadSettings() {
        mSharedPreferences = getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
        mInputType = mSharedPreferences.getInt(Keys.INPUT_ENTER_TYPE, 0);
        if (mInputType == Keys.INPUT_ENTER_SEND) {
            mEtInput.setInputType(InputType.TYPE_CLASS_TEXT);
            mEtInput.setImeOptions(EditorInfo.IME_ACTION_SEND);
        }
//        else {
//			mEtInput.setImeOptions(EditorInfo.IME_ACTION_NONE);
//			mEtInput.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//        }
    }

    /**
     * 初始化一些变量 int =getRId(this,"");
     */
    private void initVars() {
        mAnimZoomIn = AnimationUtils.loadAnimation(this,
                R.anim.chat_button_zoom_in);
        mAnimZoomOut = AnimationUtils.loadAnimation(this,
                R.anim.chat_button_zoom_out);
        mAnimControllerZoomIn = new MAnimController(mAnimZoomIn);
        mAnimControllerZoomOut = new MAnimController(mAnimZoomOut);
    }

    /**
     * 初始化一些控件
     */
    private void initViews() {
        mRelInputContainer = (RelativeLayout) findViewById(R.id.rl_chat_container);
        mRelInputContainer.setVisibility(View.VISIBLE);
        mEtInput = (EditText) findViewById(R.id.chat_input);
        mEtInput.addTextChangedListener(mOnTextChanged);
        /** 监听软键盘回车事件 */
        mEtInput.setOnEditorActionListener(mOnEnter);
        mEtInput.setOnTouchListener(inputTouch);
        // 监听删除键
        mEtInput.setOnKeyListener(mOnKey);
        /** 显示未读数量 **/
        mTvUnReadNum = (TextView) findViewById(R.id.tv_unread_num);
        mTvUnReadNum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mIvBack = (ImageView) findViewById(R.id.img_back);
        mIvBack.setOnClickListener(this);
        mRelBottomTask = (RelativeLayout) findViewById(R.id.rel_bottom_task);
        mRelBottomTask.setOnClickListener(this);
        mRelBottomSchedule = (RelativeLayout) findViewById(R.id.rel_bottom_schedule);
        mRelBottomSchedule.setOnClickListener(this);
        mRelBottomApprovel = (RelativeLayout) findViewById(R.id.rel_bottom_approvel);
        mRelBottomApprovel.setOnClickListener(this);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvGroup = (ImageView) findViewById(R.id.iv_group);
        mIvSearch = (ImageView) findViewById(R.id.iv_find);
        mIvGroup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ChatSettingPopWindow chatSettingPopWindow = new ChatSettingPopWindow(context);
                //chatSettingPopWindow.showAsDropDown(mIvSearch);
                int y = TTDensityUtil.dip2px(ChatActivity.this, 5);
                chatSettingPopWindow.showAsDropDown(mIvSearch, 2, y);
                chatSettingPopWindow.setChatSettingListener(new ChatSettingListener() {

                    @Override
                    public void OnMsgHistoryClick() {
                        //  历史记录分类界面
                        Intent intent = new Intent(ChatActivity.this, MessageRecordActivity.class);
                        intent.putExtra("loginName", mUid);
                        intent.putExtra("toLoginName", mTo);
                        startActivityForResult(intent, REQ_FROM_CHAT_HISTORY);
                    }

                    @Override
                    public void OnChatSettingClick() {
                        Intent intent;
                        if (isGroup) {//是否为群聊对象
                            intent = new Intent(ChatActivity.this,
                                    GroupSettingActivity.class);
                        } else {
                            //聊天对象为同事
                            intent = new Intent(ChatActivity.this,
                                    PersonSettingActivity.class);
                        }
                        intent.putExtra("sessionName", mTo);
                        GroupInfo groupInfo = GroupInfoDBService
                                .getIntance().getGroupInfo(mTo);
                        intent.putExtra("groupInfo", groupInfo);
                        startActivityForResult(intent,
                                REQ_FROM_DEL_HISTORY);
                    }
                });
            }
        });
        mIvSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this,
                        HistorySearchActivity.class);
                intent.putExtra("loginName", mUid);
                intent.putExtra("toLoginName", mTo);
                startActivityForResult(intent, REQ_FROM_SEARCH_MESSAGE);
            }
        });
        mIvSend = (ImageView) findViewById(R.id.chat_text_send);
        mIvSend.setOnClickListener(this);

        /** 聊天列表 */
        mListView = (ChatListView) findViewById(R.id.chat_list);
        // 触摸listView，软键盘和more 将收回
        mListView.setOnActionDownListener(mOnActionDown);
        mListView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);

        mBtnInputVoice = (Button) findViewById(R.id.btn_sound);

        mBtnMicrophone = (CheckBox) findViewById(R.id.btn_chat_microphone);
        mBtnMicrophone.setOnClickListener(this);
        mBtnInputVoice.setOnTouchListener(mToSpeakListener);
        mImageViewRecordingIndicator = (ImageView) findViewById(R.id.chat_recording_indicator);
        mViewRecordingContainer = findViewById(R.id.chat_recording_container);

        mIvEmoji = (ImageView) findViewById(R.id.iv_chat_emoji);
        mIvEmoji.setOnClickListener(this);
        addFaceToolView = findViewById(R.id.add_tool);

        mChMore = (CheckBox) findViewById(R.id.iv_chat_more);
        mChMore.setOnClickListener(this);
        mLlMoreContainer = (LinearLayout) findViewById(R.id.chat_more_container);
        mIvPhoto = (ImageView) findViewById(R.id.chat_photo);
        mIvCamera = (ImageView) findViewById(R.id.chat_camera);
        mIvFilm = (ImageView) findViewById(R.id.chat_place);
        mIvTask = (ImageView) findViewById(R.id.chat_task);
        mIvPhoto.setOnClickListener(this);
        mIvCamera.setOnClickListener(this);
        mIvFilm.setOnClickListener(this);
        mIvTask.setOnClickListener(this);

        mLinMoreContent = (LinearLayout) findViewById(R.id.lin_chat_menu);
        mRelMarkList = (RelativeLayout) findViewById(R.id.rel_mark_list);
        mRelForward = (RelativeLayout) findViewById(R.id.rel_forword_list);
        mRelMarkList.setOnClickListener(this);
        mRelForward.setOnClickListener(this);

        inputMethodManager = (InputMethodManager) getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        resendDialogView = new ResendDialogView(context, this);

        mAudioRecordPlayerUtil = new AudioRecordPlayUtil(mOnAudioInfoListener,
                this, null);
        initDatas();
        initVars();
    }

    private boolean isGroup = false;

    /**
     * 初始化的一些参数
     */
    private void initDatas() {
        mValueDBService = KeyValueDBService.getInstance();
        mToken = mValueDBService.find(Keys.TOKEN);
        mUid = mValueDBService.find(Keys.UID);

        /** 来自两个渠道的聊天 */
        // 判断是否是从通讯录里点击进来的聊天
        mUser = (User) getIntent().getSerializableExtra("user");
        if (mUser != null) {
            toNickName = mUser.getNickName();
            mTo = mUser.getUid();
        } else {
            //
            // 从会话列表进入，获取信息
            SessionItem section = (SessionItem) getIntent().getParcelableExtra(
                    "section");
            if (section != null) {
                toNickName = section.getNickName();
                mTo = section.getOppositeName();
                if (section.getChatRoom() == 1) {
                    isGroup = true;
                }
                judgeIsApp(mTo);
            } else {
                String groupId = getIntent().getStringExtra("groupId");
                String groupName = getIntent().getStringExtra("groupName");
                toNickName = groupName;
                mTo = groupId;
                isGroup = true;
            }
        }
        mTvTitle.setText(toNickName);
        // 注册启动IM模块
        manager = IMManager.getInstance();
        manager.setOnIMMessageListener(this);
        mChatListAdapter = new ChatListAdapter(context, null,
                mAudioRecordPlayerUtil, mUIHandler, isGroup, mRelInputContainer, mTo);
        mChatListAdapter.setHandler(mUIHandler);
        mListView.setAdapter(mChatListAdapter);

        IMAPI.getInstance().getGroupInfo(groupInfoResponse, mUid, mToken, mTo);

    }

    /**
     * 显示最新消息
     */
    public void getBetweenMessage() {
        // 问诊会话列表进入
        SessionItem section = (SessionItem) getIntent().getParcelableExtra("section");
        if (section == null) {
            // 通讯录进入
            section = SessionDBService.getInstance().getSessionByUid(mUid, mTo);
        }
        if (section == null) {
            dismissLoading();
            return;
        }
        // 判断是否是有草稿
        String unicode = EmojiParser.getInstance(ChatActivity.this).parseEmoji(section.getDrafts());
        SpannableStringBuilder spannableString = new SpannableStringBuilder(ParseEmojiMsgUtil.getExpressionString(ChatActivity.this, unicode));
        int type = section.getType();
        if (type == 2) {
            mEtInput.setText(spannableString);
        }

        long time = section.getSessiontime();

        // 一分钟以内重新加入不需要加载离线历史
        if (time < System.currentTimeMillis() - 60 * 1000) {
            IMNewApi.getInstance().getHistoryMessage(this, mToken, mUid, section.getUserName(), section.getOppositeName(), STEP, -1);
        } else {
            dismissLoading();
        }
//        }

    }

    public void readSession(MessageItem item) {
        // 只对接收到的消息发送已读请求
        if (item.getCmd() == MessageItem.TYPE_RECV) {
            unreadMsgId.clear();
            Map map = getSendReadContent(item);
            unreadMsgId.add(map);
            IMNewApi.getInstance().readSession(this, mToken, mUid, mTo, unreadMsgId);
        }
    }

    public void judgeIsApp(String isAppItem) {
        if (isAppItem == null) {
            return;
        }
        //判断Session是任务、日程、审批
        if (isAppItem.contains("@APP")) {
            mRelInputContainer.setVisibility(View.GONE);
            mIvSearch.setVisibility(View.GONE);
            mIvGroup.setVisibility(View.GONE);
        }
        if (isAppItem.contains(com.mintcode.launchr.util.Const.SHOWID_TASK)) {
            mRelBottomTask.setVisibility(View.VISIBLE);
            MixPanelUtil.sendEvent(this, MixPanelUtil.HOME_TASK_EVENT);
        } else if (isAppItem.contains(com.mintcode.launchr.util.Const.SHOWID_APPROVE)) {
            mRelBottomApprovel.setVisibility(View.VISIBLE);
            MixPanelUtil.sendEvent(this, MixPanelUtil.HOME_APPROVE_EVENT);
        } else if (isAppItem.contains(com.mintcode.launchr.util.Const.SHOW_SCHEDULE)) {
            mRelBottomSchedule.setVisibility(View.VISIBLE);
            MixPanelUtil.sendEvent(this, MixPanelUtil.HOME_CALENDAR_EVENT);
        }
    }

    private List<Map> unreadMsgId = new ArrayList<Map>();

    @Override
    protected void onResume() {
        super.onResume();
        mChatListAdapter.notifyDataSetChanged();

        if (mIsRunning) {
            mIsRunning = false;
            getLoaderManager().restartLoader(12, null, this);
        }
    }

    OnResponseListener groupInfoResponse = new OnResponseListener() {

        @Override
        public void onResponse(Object response, String taskId, boolean rawData) {
            if (response == null) {
                return;
            }
            if (taskId.equals(IMAPI.TASKID.SESSION)) {
                GroupInfoPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), GroupInfoPOJO.class);
                if (pojo.isResultSuccess()) {
                    pojo.getData().setMemberJson(
                            JsonUtil.convertObjToJson(pojo.getData()
                                    .getMemberList()));
                    GroupInfoDBService.getIntance().put(pojo.getData());
                    mTvTitle.setText(pojo.getData().getNickName());
                }
            }
        }

        @Override
        public boolean isDisable() {
            return false;
        }
    };


    OnActionDownListener mOnActionDown = new OnActionDownListener() {
        @Override
        public void onActionDown(ChatListView view) {
            mLlMoreContainer.setVisibility(View.GONE);
            addFaceToolView.setVisibility(View.GONE);
            mBtnMicrophone.setChecked(false);
            displayInputMethod(false);
            mMoreState = MoreState.More;
        }
    };

    OnTouchListener inputTouch = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mLlMoreContainer.setVisibility(View.GONE);
            addFaceToolView.setVisibility(View.GONE);
            mLlMoreContainer.setVisibility(View.GONE);
            mBtnMicrophone.setChecked(false);
            mMoreState = MoreState.More;

            isVisbilityFace = false;
            addFaceToolView.setVisibility(View.GONE);
            mIvEmoji.setImageResource(R.drawable.icon_chat_emoji);
            return false;
        }
    };

    TextWatcher mOnTextChanged = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (count == 1) {
                String str = s.toString();
                String w = str.substring(str.length() - 1);
                if (w.equals("@") && isGroup) {
                    Intent intent = new Intent(ChatActivity.this,
                            GroupInfoListActivtiy.class);

                    intent.putExtra("sessionName", mTo);
                    GroupInfo groupInfo = GroupInfoDBService
                            .getIntance().getGroupInfo(mTo);
                    intent.putExtra("groupInfo", groupInfo);
                    startActivityForResult(intent,
                            REQ_FROM_CHOOSE_SEND_PERSON);
                }
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().trim().length() > 0) {
                mIvSend.setVisibility(View.VISIBLE);
                mChMore.setVisibility(View.INVISIBLE);
//                mAnimControllerZoomIn.play(mIvSend, View.VISIBLE);
//                mAnimControllerZoomOut.play(mChMore, View.INVISIBLE);
            } else {
                mIvSend.setVisibility(View.INVISIBLE);
                mChMore.setVisibility(View.VISIBLE);
//                mAnimControllerZoomIn.play(mIvSend, View.INVISIBLE);
//                mAnimControllerZoomOut.play(mChMore, View.VISIBLE);
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_back) {
            onBackPressed();
        } else if (id == R.id.btn_chat_microphone) {
            switchToMoreState();
        } else if (id == R.id.iv_chat_more) {
            switchToInputState();
        } else if (id == R.id.iv_chat_emoji) {
            sendEmoji();
        } else if (id == R.id.chat_photo) {
            showPopWindow();
        } else if (id == R.id.chat_camera) {
            startCamera();
//        } else if (id == R.id.chat_place) {
//			Intent intent = new Intent();
//			intent.setClass(this, MCameraActivity.class);
//			intent.putExtra("parentId", 3);
//			startActivityForResult(intent, REQ_FROM_FILM);
        } else if (id == R.id.chat_task) {
            addTask("");
        } else if (id == R.id.tv_ok) {
            resendMsg();
        } else if (id == R.id.chat_text_send) {
            sendTextMsg();
        } else if (id == R.id.rel_bottom_task) {
            Intent intent = new Intent(ChatActivity.this, TaskMainActivity.class);
            intent.putExtra(TaskMainActivity.GET_ALL_TASK_LIST, 3);
            startActivity(intent);
        } else if (id == R.id.rel_bottom_approvel) {
            Intent intent = new Intent(ChatActivity.this, ApprovalMainActivity.class);
            startActivity(intent);
        } else if (id == R.id.rel_bottom_schedule) {
            Intent intent = new Intent(ChatActivity.this, ScheduleMainActivity.class);
            startActivity(intent);
        } else if (id == R.id.rel_mark_list) {
            markMsgList();
        } else if (id == R.id.rel_forword_list) {
            forwordMsgToFriend();
        }
    }

    //显示相册和相机选择
    private void showPopWindow() {

        startGallery();
    }

    @OnClick(R.id.tv_cancel)
    void CancelMore() {
        if (mChatListAdapter.showMergeButton) {
            mListView.setScrollToBottom(true);
            mChatListAdapter.showMergeButton = false;
            mChatListAdapter.mChooseMergeData = null;
            mChatListAdapter.mListMergeData = null;
            mChatListAdapter.notifyDataSetChanged();
            mLinMoreContent.setVisibility(View.GONE);
            mTvMoreCancel.setVisibility(View.GONE);
            mTvUnReadNum.setVisibility(View.VISIBLE);
            mRelInputContainer.setVisibility(View.VISIBLE);
            mIvBack.setVisibility(View.VISIBLE);
            mIvGroup.setVisibility(View.VISIBLE);
            mIvSearch.setVisibility(View.VISIBLE);
        }
    }

    private void resendMsg() {
        if (!mResendItem.getType().equals(Command.TEXT) && !mResendItem.getType().equals(Command.EVENT)) {
            if (mResendItem.getSent() == Command.SEND_FAILED) {
                String filePath = mResendItem.getFileName();
                if (mResendItem.getType().equals(Command.IMAGE)) {
                    MutiTaskUpLoad.getInstance().sendMsg(getDetail(filePath),
                            filePath, context, mUIHandler, mResendItem);
                } else {
                    File file = new File(filePath);
                    MutiSoundUpload.getInstance().sendSound(getDetail(filePath),
                            file, context, mUIHandler, mResendItem);
                }
                resendDialogView.dismiss();
                mListData.remove(mResendItem);
                mResendItem.setSent(Command.STATE_SEND);
                mListData.add(mResendItem);
                mChatListAdapter.notifyDataSetChanged();
                new TimeCount(30000, 1000, mResendItem).start();
            }
        } else {
            resendDialogView.dismiss();
            mListData.remove(mResendItem);
            mResendItem.setSent(Command.STATE_SEND);
            mListData.add(mResendItem);
            Log.d("clientMsgId", mResendItem.getClientMsgId() + "first");
            mResendItem.setToNickName(toNickName);
            mResendItem.setNickName(myName);
//            MutiTextUpload.getInstance().sendText(mResendItem);
            manager.resendMessageItem(mResendItem);
            new TimeCount(30000, 1000, mResendItem).start();
            mChatListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 自动重发
     */
    private void autoResendMsg(MessageItem item) {
        item.setSent(Command.STATE_SEND);
        Log.d("clientMsgId", item.getClientMsgId() + "first");
        item.setToNickName(toNickName);
        item.setNickName(myName);
        mChatListAdapter.notifyDataSetChanged();

        new TimeCount(30000, 1000, mResendItem).start();

        if (item.getType().equals(Command.IMAGE)) {
            MutiTaskUpLoad.getInstance().startNextUpload(false);
        } else if (item.getType().equals(Command.AUDIO)) {
            MutiSoundUpload.getInstance().startFirstUpload(true);
        } else if (item.getType().equals(Command.TEXT)) {
            MutiTextUpload.getInstance().startFirstUpload();
        } else if (item.getType().equals(Command.EVENT)) {
            MutiTextUpload.getInstance().startFirstUpload();
        }
    }

    /**
     * 发送任务，消息转任务
     */
    public void addTask(String title) {
        if (isGroup) {
            addTaskWindow = new ChatAddTaskWindow(this, isGroup, mTo, "", title);
            addTaskWindow.showAtLocation(mEtInput, Gravity.CENTER, 0, 0);
        } else {
            addTaskWindow = new ChatAddTaskWindow(this, isGroup, toNickName, mTo, title);
            addTaskWindow.showAtLocation(mEtInput, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 消息转日程
     */
    public void changeMsgToSchedule(String title) {
        mChatAddSchedule = new ChatAddScheduleWindow(ChatActivity.this, title);
        mChatAddSchedule.showAtLocation(mEtInput, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onBackPressed() {
        manager.readMessageByUid(mTo);
        finish();
    }

    /**
     * 选择表情
     */
    private void sendEmoji() {
        if (null == mFaceHelper) {
            mFaceHelper = new SelectFaceHelper(this, addFaceToolView);
            mFaceHelper.setFaceOpreateListener(mOnFaceOprateListener);
        }
        if (isVisbilityFace) {
            isVisbilityFace = false;
            addFaceToolView.setVisibility(View.GONE);
            displayInputMethod(true);
            if (mEtInput.getText().length() <= 0) {
                setSendBtnVisible(false);
            }
            // TODO
            // 修改处
            mIvEmoji.setImageResource(R.drawable.icon_chat_emoji);
            // 修改处
        } else {
            isVisbilityFace = true;
            addFaceToolView.setVisibility(View.VISIBLE);
            displayInputMethod(false);
            setSendBtnVisible(true);
            // 修改处
            mIvEmoji.setImageResource(R.drawable.btn_chat_keyboard_pressed);
            mChMore.setChecked(false);
            mInputState = InputState.Text;
            // 修改处
        }
        mLlMoreContainer.setVisibility(View.GONE);
        mBtnMicrophone.setChecked(false);
        mMoreState = MoreState.More;
        // 修改处
        mEtInput.setVisibility(View.VISIBLE);
        // 修改处
        mBtnInputVoice.setVisibility(View.GONE);
        if (mEtInput.getText().length() <= 0) {
            mIvSend.setVisibility(View.GONE);
            mChMore.setVisibility(View.VISIBLE);
        }
    }

    private void setSendBtnVisible(boolean isText) {
        if (isText) {
            mIvSend.setVisibility(View.VISIBLE);
            mLlMoreContainer.setVisibility(View.GONE);
            mBtnMicrophone.setChecked(false);
        } else {
            mIvSend.setVisibility(View.GONE);
            mLlMoreContainer.setVisibility(View.VISIBLE);
            mBtnMicrophone.setChecked(false);
        }

    }

    OnFaceOprateListener mOnFaceOprateListener = new OnFaceOprateListener() {
        @Override
        public void onFaceSelected(SpannableString spanEmojiStr) {
            if (null != spanEmojiStr) {
                mEtInput.append(spanEmojiStr);
            }

        }

        @Override
        public void onFaceDeleted() {
            int selection = mEtInput.getSelectionStart();
            String text = mEtInput.getText().toString();
            if (selection > 0) {
                String text2 = text.substring(selection - 1);
                if ("]".equals(text2)) {
                    int start = text.lastIndexOf("[");
                    int end = selection;
                    mEtInput.getText().delete(start, end);
                    return;
                }
                mEtInput.getText().delete(selection - 1, selection);
            }
        }

    };

    private void switchToMoreState() {
        switch (mMoreState) {
            case More:
                mMoreState = MoreState.Input;
                isVisbilityFace = false;
                mLlMoreContainer.setVisibility(View.VISIBLE);
                mEtInput.setVisibility(View.VISIBLE);
                mBtnInputVoice.setVisibility(View.GONE);
                addFaceToolView.setVisibility(View.GONE);
                displayInputMethod(false);
                break;
            case Input:
                mMoreState = MoreState.More;
                mBtnInputVoice.setVisibility(View.GONE);
                mEtInput.setVisibility(View.VISIBLE);
                mLlMoreContainer.setVisibility(View.GONE);
                addFaceToolView.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    /**
     * 切换输入状态 语音、文字
     */
    private void switchToInputState() {
        mLlMoreContainer.setVisibility(View.GONE);
        addFaceToolView.setVisibility(View.GONE);
        isVisbilityFace = false;
        switch (mInputState) {
            case Text:
                mInputState = InputState.Voice;
                mBtnMicrophone.setChecked(false);
                mMoreState = MoreState.More;
                mEtInput.setVisibility(View.GONE);
                mBtnInputVoice.setVisibility(View.VISIBLE);
                displayInputMethod(false);
                break;
            case Voice:
                mInputState = InputState.Text;
                mBtnMicrophone.setChecked(false);
                mMoreState = MoreState.More;
                mEtInput.setVisibility(View.VISIBLE);
                mBtnInputVoice.setVisibility(View.GONE);
                displayInputMethod(true);
                break;

            default:
                break;
        }
        mIvEmoji.setImageResource(R.drawable.icon_chat_emoji);
    }

    /**
     * 是否打开软键盘
     *
     * @param isOpen
     */
    private void displayInputMethod(boolean isOpen) {
        if (isOpen) {
            inputMethodManager.showSoftInput(mEtInput, InputMethodManager.SHOW_FORCED);
        } else {
            inputMethodManager.hideSoftInputFromWindow(
                    mEtInput.getWindowToken(), 0);
        }
    }


    private View.OnKeyListener mOnKey = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
//				Log.i("infos","==del===");
                // 按下删除键监听
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    Editable editable = mEtInput.getText();
                    int index = mEtInput.getSelectionStart();
//					int end = mEtInput.getSelectionEnd();
                    int len = editable.length();
                    Log.i("infos", index + "==del===" + "---" + len);

                    // 判断单光标在否在输入文本的末尾
                    if (index == len) {
                        if (!mSet.isEmpty()) {
                            Iterator it = mSet.iterator();
                            HashSet<MessageInfoEntity> set = new HashSet<MessageInfoEntity>();
                            while (it.hasNext()) {
                                MessageInfoEntity entity = (MessageInfoEntity) it.next();
                                String nickName = entity.getNickName();
                                String nick = "@" + nickName + " ";
                                if (editable.toString().endsWith(nick)) {
                                    set.add(entity);
                                    int start = len - nick.length();
                                    editable.delete(start, len);
                                    break;
                                }
                            }
                            if (set.isEmpty()) {
                                return false;
                            } else {
                                mSet.removeAll(set);
                                return true;
                            }


                        }
                        return false;
                    } else {
                        return false;
                    }
                }
//				if(!mSet.isEmpty()){
//					String str = s.toString();
//					Iterator it = mSet.iterator();
//					isAdd = true;
//					HashSet<MessageInfoEntity> set = new HashSet<MessageInfoEntity>();
//					while (it.hasNext()){
//						MessageInfoEntity entity = (MessageInfoEntity)it.next();
//						String nickName = entity.getNickName();
//						String nick = "@" + nickName + " ";
//						if(str.endsWith(nick)){
//							set.add(entity);
//							int le = str.length();
//							int l = nick.length();
//							if(le == l){
//								mEtInput.setText("");
//							}else{
//								mEtInput.setText(str.substring(0,(le - l)));
//							}
//							mEtInput.setSelection(mEtInput.getText().length());
//
//						}
//					}
////					mSet.remove(str1);
//					mSet.removeAll(set);


            }
            return false;
        }
    };

    private TextView.OnEditorActionListener mOnEnter = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_NULL) {
                return false;
            } else if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendTextMsg();
                return true;
            } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                return false;
            } else {
                return false;
            }


        }
    };

    /**
     * 从相册中选择
     */
    private void startGallery() {
        Intent intent = new Intent(this, PhotoActivity.class);
        startActivityForResult(intent, REQ_FROM_GALLERY);
    }

    /**
     * 拍照选择
     */
    private void startCamera() {
        if (getExternalCacheDir() == null) {
            Toast.makeText(this, "外部存储不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPhotoPath = Environment.getExternalStorageDirectory() + "/launchr/"
                + System.currentTimeMillis() + ".jpg";
        try {
            File mPhotoFile = new File(mPhotoPath);
            if (!mPhotoFile.getParentFile().exists()) {
                mPhotoFile.getParentFile().mkdirs();
            }

            if (!mPhotoFile.exists()) {
                mPhotoFile.createNewFile();
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
            startActivityForResult(intent, REQ_FROM_CAMERA);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送语音
     */
    OnTouchListener mToSpeakListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    recordSound();
                    mBtnInputVoice.setText(getString(R.string.press_up_speak));
                    break;
                case MotionEvent.ACTION_UP:
                    sendSoundFile();
                    mBtnInputVoice.setText(getString(R.string.press_down_speak));
                default:
                    break;
            }
            return false;
        }
    };

    /**
     * 录制声音
     */
    private void recordSound() {
        // 应该要加入动画吧
        if (mAudioRecordPlayerUtil.isPlaying()) {
            mAudioRecordPlayerUtil.stopPlaying();
            AudioPlayingAnimation.stop();
        }
        mViewRecordingContainer.setVisibility(View.VISIBLE);
        AnimationDrawable drawable = (AnimationDrawable) mImageViewRecordingIndicator
                .getBackground();
        drawable.start();

        mAudioRecordPlayerUtil.setFileName(UUID.randomUUID().toString()
                + ".amr");
        mAudioRecordPlayerUtil.startRecording();
    }

    /**
     * 发送声音文件
     */
    private void sendSoundFile() {
        mListView.getRefreshableView().setSelection(mListData.size());

        mViewRecordingContainer.setVisibility(View.GONE);

        mAudioRecordPlayerUtil.stopRecording();
        String filePath = mAudioRecordPlayerUtil.getOutputFileName();
        File f = new File(filePath);
        if (f.exists()) {
            AttachDetail detail = getDetail(filePath);
            if (mAudioRecordPlayerUtil.getRecordMilliseconds() > 300) {
                int time = mAudioRecordPlayerUtil.getRecordMilliseconds() / 1000;
                if (time <= 0) {
                    time = 1;
                }
                MessageItem item = setupSendItem();
                item.setFileName(filePath);
                item.setTimeText(getMessageText(time));
                item.setType(Command.AUDIO);
                AudioItem audio = new AudioItem();
                audio.setAudioLength(Integer.parseInt(getMessageText(time)));
                audio.setFileUrl(filePath);
                item.setContent(audio.toString());
                mListData.add(item);
                mChatListAdapter.notifyDataSetChanged();

                // 开启上传语音文件 任务
                MutiSoundUpload.getInstance().sendSound(detail, f, context, mUIHandler, item);
            }
        }
    }

    public static String getMessageText(int time) {// 语音消息
        StringBuilder sb = new StringBuilder();
        sb.append(time);
        return sb.toString();
    }

    /**
     * 发送文本信息，并加入列表
     */
    private void sendTextMsg() {

        String message = ParseEmojiMsgUtil.convertDraToMsg(mEtInput.getText(),
                this);// 这里不要直接用mEditMessageEt.getText().toString();
        String sj = ParseEmojiMsgUtil.convertToMsg(message, this);

        if (!message.trim().isEmpty()) {
            MessageItem item = new MessageItem();
            item.setCmd(MessageItem.TYPE_SEND);
            item.setContent(message);
            item.setType(Command.TEXT);
            item.setSent(Command.STATE_SEND);
            item.setClientMsgId(System.currentTimeMillis());
            item.setCreateDate(System.currentTimeMillis());
            item.setFromLoginName(mUid);
            item.setToLoginName(mTo);
            item.setToNickName(toNickName);
            item.setNickName(myName);

            if (!mSet.isEmpty()) {
                Iterator it = mSet.iterator();
                List<String> list = new ArrayList<String>();
                while (it.hasNext()) {
                    MessageInfoEntity entity = (MessageInfoEntity) it.next();
                    list.add(entity.getUserName() + "@" + entity.getNickName());
                }
                MTHttpParameters p = new MTHttpParameters();
                p.setParameter("atUsers", list);
                String s = p.toJson();
                item.setInfo(p.toJson());
            }

            //
//            getSurprise(item.getContent(),mListView);

            manager.sendMessageItem(item);
//            MutiTextUpload.getInstance().sendText(item);
            // 发送计算器，超过5秒即为发送失败
            new TimeCount(10000, 1000, item).start();

            // 设置时间显示
            Log.i("setTimeOnMessage", item.getMsgId() + "----");
            item.setMsgId(item.getCreateDate());
            setTimeOnMessage(item);

            mListData.add(item);
            mChatListAdapter.setData(mListData);
            mEtInput.setText("");
            mListView.getRefreshableView().setSelection(mListData.size());
        } else {
            Toast.makeText(context, "消息不能为空，请输入", Toast.LENGTH_SHORT).show();
        }
        mSet.clear();

    }

    /**
     * 发送任务消息
     */
    public void sendTaskMessage(MessageEventTaskEntity.MessageEventTaskBackupEntity content, boolean isInGroup, String userId, String userName) {
        if (content != null) {
            MessageItem item = new MessageItem();
            item.setCmd(MessageItem.TYPE_SEND);
            item.setContent(TTJSONUtil.convertObjToJson(content));
            item.setType(Command.EVENT);
            item.setSent(Command.STATE_SEND);
            item.setClientMsgId(System.currentTimeMillis());
            item.setCreateDate(System.currentTimeMillis());
            item.setFromLoginName(mUid);
            item.setNickName(myName);
            item.setMsgId(System.currentTimeMillis());
            if (isGroup) {
                if (mUid.equals(userId) || isInGroup) {
                    item.setToLoginName(mTo);
                    item.setToNickName(toNickName);
                    mListData.add(item);
                } else {
                    item.setToLoginName(userId);
                    item.setToNickName(userName);
                }
            } else {
                if (mTo.equals(userId)) {
                    item.setToLoginName(mTo);
                    item.setToNickName(toNickName);
                    mListData.add(item);
                } else {
                    item.setToLoginName(userId);
                    item.setToNickName(userName);
                }
            }

//            MutiTextUpload.getInstance().sendText(item);
            manager.sendMessageItem(item);
            // 发送计算器，超过5秒即为发送失败
            new TimeCount(30000, 1000, item).start();
            mChatListAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 发送任务消息
     */
    public void sendTaskMessage(MessageEventTaskEntity content, boolean isInGroup, String userId, String userName) {
        if (content != null) {
            MessageItem item = new MessageItem();
            item.setCmd(MessageItem.TYPE_SEND);
            item.setContent(TTJSONUtil.convertObjToJson(content));
            item.setType(Command.EVENT);
            item.setSent(Command.STATE_SEND);
            item.setClientMsgId(System.currentTimeMillis());
            item.setCreateDate(System.currentTimeMillis());
            item.setFromLoginName(mUid);
            if (!isInGroup) {
                item.setToLoginName(userId);
                item.setToNickName(userName);
            } else {
                item.setToLoginName(mTo);
                item.setToNickName(toNickName);
            }
            item.setNickName(myName);
            item.setMsgId(System.currentTimeMillis());

//            MutiTextUpload.getInstance().sendText(item);
            manager.sendMessageItem(item);
            // 发送计算器，超过5秒即为发送失败
            new TimeCount(30000, 1000, item).start();

            // 设置时间显示
            setTimeOnMessage(item);

            mListData.add(item);
            mChatListAdapter.notifyDataSetChanged();
        }
    }

    private boolean selectMsg(MessageItem item) {
        if (mListData != null) {
            for (MessageItem messageItem : mListData) {
                long clientMsgId1 = messageItem.getClientMsgId();
                long clientMsgId2 = item.getClientMsgId();
                if (clientMsgId1 == clientMsgId2) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onMessage(List<MessageItem> items, int arg1) {
        if (items == null) {
            items = new ArrayList<MessageItem>();
        }
        for (MessageItem item : items) {
            if ((Command.REV).equals(item.getType())) {
                // 回执
                updateSendState(item);
                mChatListAdapter.notifyDataSetChanged();
            }
            // 判断item.getFromName 和当前聊天对象要一样
            if (mTo == null) {
                return;
            }
            if (mTo.equals(item.getFromLoginName()) || item.getType().equals(Command.CMD) || (mUid.equals(item.getUserName()) && mTo.equals(item.getToLoginName()))) {
//				if (selectMsg(item)
//						&& !(Command.READ_SESSION).equals(item.getType())) {
//					return;
//				}
                // 彩蛋
//                getSurprise(item.getContent(),mListView);


                if (item.getType().equals(Command.ALERT) && selectMsg(item)) {
                    return;
                }
                // Web端自己给自己发消息，手机端会出现两条消息
                if (handleMyMsg(item) && selectMsg(item)) {
                    return;
                }

                // 设置时间显示
                if ((Command.CMD).equals(item.getType())) {
                    HashMap<String, String> infoMap = TTJSONUtil.convertJsonToObj(
                            item.getContent(),
                            new TypeReference<HashMap<String, String>>() {
                            });
                    String action = infoMap.get("name");
                    if (!action.equals(Command.MARK) && !action.equals(Command.CANCEL_MARK)) {
                        setTimeOnMessage(item);
                    }
                } else {
                    setTimeOnMessage(item);
                }
                String action = "";
                Log.i("msgOnMessage", "TEXT  OnMessage:" + item.toString());
                if ((Command.TEXT).equals(item.getType())) {
                    Log.i("msg", "TEXT  OnMessage:" + item.toString());
                    handleRevTextMsg(item);
                } else if ((Command.IMAGE).equals(item.getType())) {
                    Log.i("msg", "IMAGE  OnMessage:" + item.toString());
                    handleRevImageMsg(item);
                } else if ((Command.AUDIO).equals(item.getType())) {
                    Log.i("msg", "AUDIO  OnMessage:" + item.toString());
                    handleRevAudioMsg(item);
                } else if ((Command.VIDEO).equals(item.getType())) {
                    Log.i("msg", "video  OnMessage:" + item.toString());
                    handleRevVideoMsg(item);
                } else if ((Command.ALERT).equals(item.getType())) {
                    Log.i("msg", "alert  OnMessage:" + item.toString());
                    handleRevAlertMsg(item);
                } else if ((Command.CMD).equals(item.getType())) {
                    String content = item.getContent();
                    HashMap<String, String> infoMap = TTJSONUtil.convertJsonToObj(
                            item.getContent(),
                            new TypeReference<HashMap<String, String>>() {
                            });
                    action = infoMap.get("name");
                    String value = infoMap.get("data");
                    long time = Long.valueOf(value);
                    MessageDBService instance = MessageDBService.getInstance();
                    if (action.equals(Command.OPEN)) {

                    } else if (action.equals(Command.READ_SESSION)) {
                        for (int i = mListData.size() - 1; i >= 0; i--) {
                            MessageItem messageItem = mListData.get(i);
                            if (messageItem.getMsgId() == time) {
                                messageItem.setIsRead(1);
                                break;
                            }
                        }
                    } else if (action.equals(Command.MARK)) {
                        for (int i = mListData.size() - 1; i >= 0; i--) {
                            MessageItem messageItem = mListData.get(i);
                            if (messageItem.getMsgId() == time) {
                                messageItem.setIsMarkPoint(1);
                                break;
                            }
                        }
                    } else if (action.equals(Command.CANCEL_MARK)) {
                        for (int i = mListData.size() - 1; i >= 0; i--) {
                            MessageItem messageItem = mListData.get(i);
                            if (messageItem.getMsgId() == time) {
                                messageItem.setIsMarkPoint(0);
                                break;
                            }
                        }
                    }
                } else if ((Command.EVENT).equals(item.getType())) {
                    Log.i("msg", "EVENT  OnMessage:" + item.toString());
                    handleRevEventMsg(item);
                } else if (Command.LOGIN_OUT.equals(item.getType())) {
                    handleRevLoginOutg(item);
                } else if ((Command.FILE).equals(item.getType())) {
                    handleRevFileMsg(item);
                } else if ((Command.RESEND).equals(item.getType())) {
                    handleReSend(item);
                    return;
                } else if (Command.MERGE.equals(item.getType()) && !mTo.equals(item.getFromLoginName())) {
                    handleMergeMsg(item);
                }

                // 根据modified字段判断要不要更新用户资料
                long modified = item.getModified();
                GroupInfo info = new GroupInfo();
                info.setUserName(mTo);
                boolean exist = GroupInfoDBService.getIntance().exist(info);
                if (!exist || modified == 0) {
                    IMAPI.getInstance().getGroupInfo(groupInfoResponse, mUid,
                            mToken, mTo);
                } else {
                    long modified2 = GroupInfoDBService.getIntance()
                            .getGroupInfo(mTo).getModified();
                    if (modified > modified2) {
                        IMAPI.getInstance().getGroupInfo(groupInfoResponse,
                                mUid, mToken, mTo);
                    }
                }


                mChatListAdapter.notifyDataSetChanged();
                mListView.getRefreshableView().setSelection(mListData.size());
            }

        }
//		mChatListAdapter.notifyDataSetChanged();

//        if (isRunning()) {
//        }

    }

    /**
     * 消息合并转发
     */
    private void handleMergeMsg(MessageItem item) {
        if (mListData != null && item != null) {
            mListData.add(item);
            mChatListAdapter.setData(mListData);
        }
    }

    /**
     * 消息撤回
     */
    private void handleReSend(MessageItem item) {
        for (int i = 0; i < mListData.size(); i++) {
            if (mListData.get(i).getClientMsgId() == item.getClientMsgId()) {
                mListData.set(i, item);
//                mChatListAdapter.setData(mListData);
//                mListView.getRefreshableView().setSelection(i);
                mChatListAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    /**
     * 处理消息显示时间
     *
     * @param item
     */
    private void setTimeOnMessage(MessageItem item) {
        if (mTo.contains(com.mintcode.launchr.util.Const.SHOWID_TASK)) {
            return;
        } else if (mTo.contains(com.mintcode.launchr.util.Const.SHOWID_APPROVE)) {
            return;
        } else if (mTo.contains(com.mintcode.launchr.util.Const.SHOW_SCHEDULE)) {
            return;
        }
        // 判断数据集是否为空
        if (mListData != null && !mListData.isEmpty()) {
            MessageItem msgItem = mListData.get(mListData.size() - 1);
            long msgId = item.getMsgId();
            long currentMsgId = msgItem.getMsgId();
            // 判断是否和前一条间隔时间相差五分钟
            if ((msgId - currentMsgId) >= 60 * 5 * 1000) {
                MessageItem msg = new MessageItem();
                msg.setMsgId(msgId - 1);
                msg.setContent(TimeFormatUtil.formatTime(msgId));
                msg.setType(Command.ALERT);

                msg.setFromLoginName(item.getFromLoginName());
                msg.setToLoginName(item.getToLoginName());
                msg.setClientMsgId(item.getClientMsgId() - 1);
                msg.setCreateDate(item.getCreateDate());
                msg.setModified(item.getModified());
                msg.setInfo(item.getInfo());
                // 判断消息是否是撤回
                if (Command.RESEND.equals(item.getType())) {
                    String content = item.getContent();
                    ResendEntity resendEntity = TTJSONUtil.convertJsonToCommonObj(content, ResendEntity.class);
                    msg.setMsgId(resendEntity.getMsgId() - 1);
                    msg.setClientMsgId(resendEntity.getClientMsgId() - 1);
                }

                mListData.add(msg);
            }

        }


    }

    private boolean handleMyMsg(MessageItem item) {
        boolean result = false;
        String userName = item.getFromLoginName();
        String toUserName = item.getToLoginName();
        if (userName.equals(toUserName)) {
            return true;
        }
        return result;
    }

    private void handleRevAlertMsg(MessageItem item) {
        if (mListData != null) {
            item.setSent(Command.STATE_SEND);
            mListData.add(item);
            IMAPI.getInstance().getGroupInfo(groupInfoResponse, mUid, mToken, mTo);

            //可能是更改群名的推送，更新session名
            SessionDBService.getInstance().update(item);
        }
    }

    /**
     * 处理收到的视频消息
     */
    private void handleRevVideoMsg(MessageItem item) {
        item.setSent(Command.STATE_SEND);
        mListData.add(item);
        MutiTaskUpLoad.getInstance().sendMsgToDownload(item, context,
                mUIHandler);
    }

    /**
     * 处理收到的语音消息
     */
    private void handleRevAudioMsg(MessageItem item) {
        item.setSent(Command.STATE_SEND);
        mListData.add(item);
        MutiSoundUpload.getInstance().sendSoundToDownload(item, context,
                mUIHandler);
    }

    /**
     * 处理收到的是图片消息
     */
    private void handleRevImageMsg(MessageItem item) {
        if (item.getFromLoginName().equals(mTo)) {
            item.setCmd(MessageItem.TYPE_RECV);
        } else {
            item.setCmd(MessageItem.TYPE_SEND);
        }
        mListData.add(item);
//        MutiTaskUpLoad.getInstance().sendMsgToDownload(item, context,
//                mUIHandler);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                mListView.getRefreshableView().setSelection(mListData.size() - 1);
            }
        };
        mChatListAdapter.notifyDataSetChanged();
        mUIHandler.removeCallbacks(r);
        mUIHandler.postDelayed(r, 300);
    }

    /**
     * 处理收到的文本消息
     */
    private void handleRevTextMsg(MessageItem item) {
        if (mListData != null && item != null) {
            mListData.add(item);
            // 判断activity是否显示，如果显示，则将消息置为已读
            if (isForeground() && !isGroup) {
                if (item.getCmd() == MessageItem.TYPE_RECV) {
                    unreadMsgId.clear();
//                unreadMsgId.add(String.valueOf(item.getMsgId()));
//                IMAPI.getInstance().readSession(readSessionResponse, mToken, mUid,
//                        mTo, unreadMsgId);
                    Map map = getSendReadContent(item);
                    unreadMsgId.add(map);
                    IMNewApi.getInstance().readSession(this, mToken, mUid, mTo, unreadMsgId);
                }
            }
        }
    }


    private Map getSendReadContent(MessageItem item) {
        MTHttpParameters p = new MTHttpParameters();
        p.setParameter("msgId", item.getMsgId());
        p.setParameter("from", item.getFromLoginName());
        return p.getMap();
    }

    private Map getSendReadContent(String msgId, String from) {
        MTHttpParameters p = new MTHttpParameters();
        p.setParameter("msgId", msgId);
        p.setParameter("from", from);
        return p.getMap();
    }


    /**
     * 处理收到的文件消息
     */
    private void handleRevFileMsg(MessageItem item) {
        mListData.add(item);

        // 判断activity是否显示，如果显示，则将消息置为已读
        if (isForeground() && !isGroup) {
            if (item.getCmd() == MessageItem.TYPE_RECV) {
                unreadMsgId.clear();
//            unreadMsgId.add(String.valueOf(item.getMsgId()));
//            IMAPI.getInstance().readSession(readSessionResponse, mToken, mUid, mTo, unreadMsgId);.
                Map map = getSendReadContent(item);
                unreadMsgId.add(map);
                IMNewApi.getInstance().readSession(this, mToken, mUid, mTo, unreadMsgId);
            }
        }
    }

    /**
     * 处理收到的事件
     */
    private void handleRevEventMsg(MessageItem item) {
        int index = mListView.getFirstVisiblePosition();
        for (int i = 0; i < mListData.size(); i++) {
            if (mListData.get(i).getClientMsgId() == item.getClientMsgId()) {
                mListData.set(i, item);
//                mChatListAdapter.setData(mListData);
                mChatListAdapter.notifyDataSetChanged();
//                selection(index);
                return;
            }
        }
        mListData.add(item);
        mChatListAdapter.notifyDataSetChanged();
        mChatListAdapter.setData(mListData);
//        IMAPI.getInstance().readSession(readSessionResponse, mToken, mUid, mTo, unreadMsgId);

        // 发送已读请求
        if (item.getCmd() == MessageItem.TYPE_RECV) {
            Map map = getSendReadContent(item);
            unreadMsgId.add(map);
            IMNewApi.getInstance().readSession(this, mToken, mUid, mTo, unreadMsgId);
        }
    }

    private void selection(final int index) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                mListView.getRefreshableView().setSelection(index);
            }
        };
        mUIHandler.postDelayed(r, 100);
    }

    /**
     * 处理LoginOut指令
     */
    private void handleRevLoginOutg(MessageItem item) {
        AppUtil.getInstance(this).deleteFile();
        AppUtil.getInstance(this).saveIntValue(com.mintcode.launchr.util.Const.KEY_GESTURE, 0);
        stopService(new Intent(ChatActivity.this, PushService.class));
        LauchrConst.PUSH_SERVICE_IS_LOGIN = false;
        Intent intent;//= new Intent(ChatActivity.this, LoginActivity.class);

        if (LauchrConst.IS_WORKHUB) {
            intent = new Intent(this, LoginWorkHubActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }

    private boolean isForeground() {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (cpn.getClassName().equals(
                    "com.mintcode.chat.activity.ChatActivity")) {
                return true;
            }
        }

        return false;
    }

    /**
     * 更新发送消息状态
     */
    private void updateSendState(MessageItem item) {
        for (int i = mListData.size() - 1; i >= 0; i--) {
            MessageItem messageItem = mListData.get(i);
            if (item.getClientMsgId() == messageItem.getClientMsgId()) {
                Log.i("msg", "onMessage item:" + item.getFileName());
                messageItem.setSent(Command.SEND_SUCCESS);
                messageItem.setCmd(MessageItem.TYPE_SEND);
                messageItem.setMsgId(item.getMsgId());
                manager.updateMsgState(messageItem);
                mListData.set(i, messageItem);

//                if(messageItem.getType().equals(Command.TEXT)){
//                    MutiTextUpload.getInstance().removeFirstUpload(item);
//                }else if(messageItem.getType().equals(Command.IMAGE)){
//                    MutiTaskUpLoad.getInstance().stopFirstUpload(item, false);
//                }else if(messageItem.getType().equals(Command.AUDIO)){
//                    MutiSoundUpload.getInstance().stopFirstUpload(item);
//                }else if(messageItem.getType().equals(Command.EVENT)){
//                    MutiTextUpload.getInstance().removeFirstUpload(item);
//                }
                break;
            }
        }
    }

    /**
     * 设置消息为发送失败
     */
    private void updateSendDefeat(MessageItem item) {
        Log.d("clientMsgId", item.getClientMsgId() + "third");
        item.setSent(Command.SEND_FAILED);
        item.setCmd(MessageItem.TYPE_SEND);
        manager.updateMsgState(item);
        for (int i = mListData.size() - 1; i >= 0; i--) {
            MessageItem messageItem = mListData.get(i);
            if (item.getClientMsgId() == messageItem.getClientMsgId()) {
                Log.i("msg", "onMessage item:" + item.getFileName());
                messageItem.setSent(Command.SEND_FAILED);
                break;
            }
        }
        mChatListAdapter.notifyDataSetChanged();
    }

    /**
     * 更新下载的状态 主要是附件
     */
    private void updateRevState(MessageItem item) {
        for (int i = mListData.size() - 1; i >= 0; i--) {
            MessageItem messageItem = mListData.get(i);
            if (item.getClientMsgId() == messageItem.getClientMsgId()) {
                messageItem.setSent(Command.SEND_SUCCESS);
                item.setSent(Command.SEND_SUCCESS);
                if (mUid.equals(item.getFromLoginName())) {
                    item.setCmd(MessageItem.TYPE_SEND);
                } else {
                    item.setCmd(MessageItem.TYPE_RECV);
                }
                mListData.set(i, messageItem);
                manager.updateMsgState(item);
                break;
            }
        }
    }

    @Override
    public void onStatusChanged(int arg0, String arg1) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String text = ParseEmojiMsgUtil.convertDraToMsg(mEtInput.getText(), this);
        ContentValues cv = new ContentValues();
        List<SessionItem> l = DataSupport.where("oppositeName = ? and userName = ?", mTo, mUid).find(SessionItem.class);
        if (l != null && !l.isEmpty()) {
            SessionItem item = l.get(0);
            if (!text.equals("")) {
                cv.put("drafts", text);
                cv.put("type", 2);
                if (item != null) {
                    DataSupport.update(SessionItem.class, cv, item.getId());
                }
            } else {
                cv.put("drafts", "");
                cv.put("type", 1);
                if (item != null) {
                    DataSupport.update(SessionItem.class, cv, item.getId());
                }
            }
        }


        mTo = "";
        toNickName = "";

        mIsRunning = false;
        manager.removeOnIMMessageListener(this);

        // 如果正在播放语音，则停止
        if (mAudioRecordPlayerUtil.isPlaying()) {
            mAudioRecordPlayerUtil.stopPlaying();
            AudioPlayingAnimation.stop();
        }
        // 注销传感器管理
        if (mSensorManager != null) {
//            mSensorManager.unregisterListener(this);
        }

        mTo = "";
    }

    /**
     * 更新sessionItem
     */
    private void setUpdateSession(HistoryMsgEntity msgItem) {
        ContentValues cv = new ContentValues();
        List<SessionItem> l = DataSupport.where("oppositeName = ? and userName = ?", mTo, mUid).find(SessionItem.class);
        if (l != null && !l.isEmpty()) {
            SessionItem item = l.get(0);
            cv.put("type", 1);
            cv.put("content", msgItem.getContent());
            DataSupport.update(SessionItem.class, cv, item.getId());
        }
    }

    private AudioRecordPlayUtil.OnInfoListener mOnAudioInfoListener = new AudioRecordPlayUtil.OnInfoListener() {

        @Override
        public void onVolume(int volume) {
            // mVoiceVolumeIndicator.setLevel(volume);
        }

        @Override
        public void onMaxFileSize() {
        }

        @Override
        public void onMaxDuration() {
        }

        @Override
        public void onPlayCompletion() {
            // mTextViewAudioMode.setVisibility(View.GONE);
            AudioPlayingAnimation.stop();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        displayInputMethod(false);
        if (RESULT_OK != resultCode) {
            ImageGridAdapter.mSelectedImage.clear();
            return;
        }
        switch (requestCode) {
            case REQ_FROM_GALLERY:
                // 处理聊天发送图片
//			HandleGallerySend();
                handleSelectPhoto(data);
                break;
            case REQ_FROM_CAMERA:
                // 处理照片发送
//                BitmapUtil.handleTakePhoto(this, mPhotoPath);

                HandleCameraSend();


                break;

            case REQ_FROM_FILM:
                HandleFilmSend(data.getStringExtra("camera_path"));
                break;
            case REQ_FROM_DEL_HISTORY:
//                mListData.clear();
//                mChatListAdapter.notifyDataSetChanged();
                break;
            case REQ_FROM_CHAT_HISTORY:
//                mListData.clear();
//                mListData = manager.getMessagesByIndexForDesc(mUid, mTo, 0, count);
//                mChatListAdapter.setData(mListData);
                break;
            case REQ_FROM_CHOOSE_NEXT_PEOPLE:
                mChatListAdapter.dealNextPeopleResult(data);
                break;
            case REQ_FROM_SEARCH_MESSAGE:
                mListData.clear();
                mListData = manager.getMessagesByIndexForDesc(mUid, mTo, 0, count);
                mChatListAdapter.setData(mListData);
                displayInputMethod(false);
                break;
            case REQ_FROM_CHOOSE_PROJECT:
                TaskProjectListEntity entity = (TaskProjectListEntity) data.getSerializableExtra(TaskUtil.KEY_TASK_PROJECT);
                addTaskWindow.setProjectResult(entity);
                break;
            case REQ_FROM_CHOOSE_MEMBER:
                List<String> userName = data.getStringArrayListExtra("userName");
                List<String> userId = data.getStringArrayListExtra("userId");
                addTaskWindow.setMemberResult(userId, userName);
                break;
            case REQ_FROM_CHOOSE_SEND_PERSON:
                // @选择人员返回
                GroupInfo info = (GroupInfo) data.getSerializableExtra("choose_person");
                if (info != null) {
                    Message msg = Message.obtain();
                    msg.what = HandleMsgType.TYPE_SEND_TO_TARGET;
                    MessageItem item = new MessageItem();

                    MessageInfoEntity mie = new MessageInfoEntity();
                    mie.setNickName(info.getNickName());
                    mie.setUserName(info.getUserName());
                    item.setInfo(TTJSONUtil.convertObjToJson(mie));
                    msg.obj = item;
                    mUIHandler.sendMessage(msg);
                }

                break;
            case REQA_FROM_CHOOSE_REMIND_TYPE:
                Bundle bundle = data.getExtras();
                String result = bundle.getString(RemindActivity.REMIND_INFO);
                int remindType = bundle.getInt(RemindActivity.REMINDTYPE, 0);
                mChatAddSchedule.setRemindResult(result, remindType);
                break;
            case REQA_FROM_CHOOSE_MERGE_MEMBER:
                if (data != null) {
                    //data.getStringArrayListExtra("userId")  data.getStringExtra("userId")
//                    startMergeMessage(data.getStringExtra("userId"), data.getStringExtra("userName"));
                    startMergeMessage(data.getStringArrayListExtra("userId"), data.getStringExtra("userName"));
                }
                break;
            default:
                break;
        }

    }

    /**
     * 得到一个
     *
     * @param path
     */
    private void HandleFilmSend(String path) {
        MessageItem item = setupSendItem();
        item.setType(Command.VIDEO);
        item.setContent(path);
        item.setFileName(path);

        AttachDetail detail = getDetail(path);

        UploadFileTask task = new UploadFileTask(context, detail,
                new File(path), mUIHandler, item);
        task.execute();
        mListData.add(item);
        mChatListAdapter.notifyDataSetChanged();
    }

    private void HandleCameraSend() {
        MessageItem item = setupSendItem();
        item.setContent(mPhotoPath);
        item.setFileName(mPhotoPath);
        MutiTaskUpLoad.getInstance().sendMsg(getDetail(mPhotoPath), mPhotoPath, context, mUIHandler, item);
        MutiTaskUpLoad.getInstance().startNextUpload(true);
        mListData.add(item);
//        new TimeCount(30000, 1000, item).start();
        mChatListAdapter.notifyDataSetChanged();
    }

    private void HandleGallerySend() {
        List<String> mSelectedImage = ImageGridAdapter.mSelectedImage;
        for (int i = 0; i < mSelectedImage.size(); i++) {
            final String filePath = mSelectedImage.get(i);
            MessageItem item = setupSendItem();
            item.setContent(filePath);
            item.setFileName(filePath);
            MutiTaskUpLoad.getInstance().sendMsg(getDetail(filePath), filePath,
                    context, mUIHandler, item);

            // 设置时间显示
            setTimeOnMessage(item);

            mListData.add(item);
            new TimeCount(30000, 1000, item).start();
            mChatListAdapter.notifyDataSetChanged();

        }
    }

    /**
     * 选择图片
     *
     * @param data
     */
    private void handleSelectPhoto(Intent data) {
        List<String> list = data.getStringArrayListExtra(PhotoActivity.SELECT_IMAGE_LIST);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                String url = list.get(i);
                MessageItem item = setupSendItem();
                item.setClientMsgId(System.currentTimeMillis() + i * 50);
                item.setContent(url);
                item.setFileName(url);
                MutiTaskUpLoad.getInstance().sendMsg(getDetail(url), url, context, mUIHandler, item);
                // 设置时间显示
                setTimeOnMessage(item);
                mListData.add(item);
                mChatListAdapter.notifyDataSetChanged();
            }
            MutiTaskUpLoad.getInstance().startNextUpload(false);
        }
//        else {
//			toast("select error");
//        }

    }

    private AttachDetail getDetail(String path) {
        File f = new File(path);
        AttachDetail detail = new AttachDetail();
        detail.setFileName(f.getName());
        detail.setFileStatus(1);
        detail.setSrcOffset(0);
        detail.setUserToken(mToken);
        detail.setUserName(mUid);
        detail.setAppName(appName);
        System.out.println(detail.toJson());
        return detail;
    }

    private MessageItem setupSendItem() {
        MessageItem item = new MessageItem();
        item.setCmd(MessageItem.TYPE_SEND);
        item.setType(Command.IMAGE);
        item.setSent(Command.STATE_SEND);
        item.setClientMsgId(System.currentTimeMillis());
        item.setCreateDate(System.currentTimeMillis());
        item.setFromLoginName(mUid);
        item.setToLoginName(mTo);

        return item;
    }

    Handler mUIHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Log.i("info", msg.what + "===");
            switch (msg.what) {
                case HandleMsgType.TYPE_MSG_1:

                    break;
                case HandleMsgType.TYPE_SEND_IMAGE:
                    MessageItem imageItem = (MessageItem) msg.obj;
                    if (imageItem.getSent() != Command.SEND_FAILED) {
                        AttachDetail detail = JsonUtil.convertJsonToCommonObj(
                                imageItem.getContent(), AttachDetail.class);
                        AttachItem attach = TTJSONUtil.convertJsonToCommonObj(imageItem.getContent(), AttachItem.class);
                        if (detail != null) {
                            attach.setFileSize(detail.getFileSize());
                            attach.setFileUrl(detail.getFileUrl());
                            attach.setThumbnail(detail.getThumbnail());
                        }
                        if (attach != null) {
                            imageItem.setContent(attach.toString());
                        }
                        imageItem.setActionSend(1);
                        imageItem.setToNickName(toNickName);
                        imageItem.setNickName(myName);
                        manager.sendImage(imageItem);
                    } else {
                        imageItem.setSent(Command.STATE_SEND);
                        mChatListAdapter.notifyDataSetChanged();
                        //                       MutiTaskUpLoad.getInstance().startNextUpload(false);
                    }
                    new TimeCount(30000, 1000, imageItem).start();
                    break;
                case HandleMsgType.TYPE_SEND_AUDIO:
                    MessageItem audioItem = (MessageItem) msg.obj;
                    if (audioItem.getSent() != Command.SEND_FAILED) {
                        AttachDetail audioDetail = null;
                        try {
                            audioDetail = JsonUtil.convertJsonToCommonObj(
                                    audioItem.getContent(), AttachDetail.class);

                        } catch (Exception e) {
                        }
                        AudioItem audio = new AudioItem();
                        if (audioDetail != null) {
                            audio.setFileUrl(audioDetail.getFileUrl());
                        }
                        audio.setAudioLength(Integer.parseInt(audioItem.getTimeText()));
                        audioItem.setContent(audio.toString());
                        audioItem.setActionSend(1);
                        audioItem.setToNickName(toNickName);
                        audioItem.setNickName(myName);
                        manager.sendAudio(audioItem);

                        // 设置时间显示
                        audioItem.setMsgId(audioItem.getCreateDate());
                        setTimeOnMessage(audioItem);
                        Log.i("------task-load", "handler");
                    } else {
                        audioItem.setSent(Command.STATE_SEND);
                        mChatListAdapter.notifyDataSetChanged();
                        //                       MutiSoundUpload.getInstance().startFirstUpload(true);
                    }
                    new TimeCount(30000, 1000, audioItem).start();
                    break;
                case HandleMsgType.TYPE_SEND_VIDEO:
                    MessageItem videoItem = (MessageItem) msg.obj;
                    AttachDetail videoDetail = JsonUtil.convertJsonToCommonObj(
                            videoItem.getContent(), AttachDetail.class);
                    AttachItem video = new AttachItem();
                    if (videoDetail != null) {
                        video.setFileSize(videoDetail.getFileSize());
                        video.setFileUrl(videoDetail.getFileUrl());
                        video.setThumbnail(videoDetail.getThumbnail());
                        videoItem.setContent(videoDetail.toString());
                    }
                    videoItem.setType(Command.VIDEO);
                    videoItem.setActionSend(1);
                    videoItem.setToNickName(toNickName);
                    videoItem.setNickName(myName);
                    manager.sendMessageItem(videoItem);
                    mChatListAdapter.notifyDataSetChanged();
                    break;
                case HandleMsgType.TYPE_RESEND_TEXT:
                    MessageItem item = (MessageItem) msg.obj;
                    resendMsg(item);
                    break;
                case HandleMsgType.TYPE_REVIMAGE:
                case HandleMsgType.TYPE_REVAUDIO:
                case HandleMsgType.TYPE_REVVIDEO:
                    MessageItem msgItem = (MessageItem) msg.obj;
                    updateRevState(msgItem);
                    mChatListAdapter.notifyDataSetChanged();
//                    mListView.getRefreshableView().setSelection(mListData.size() - 1);
                    break;
                case HandleMsgType.TYPE_FILE_UPLOAD:
                    // 处理更新上传进度显示
                    MessageItem mItem = (MessageItem) msg.obj;
                    if (TextUtils.equals(mItem.getType(), Command.IMAGE)) {
                        int position = mListData.indexOf(mItem);
                        if (position > -1) {
                            mListData.remove(position);
                            mListData.add(position, mItem);
                            mChatListAdapter.notifyDataSetChanged();
                        }

                    }

                    break;
                case HandleMsgType.TYPE_SEND_TO_TARGET:
                    // 长按头像@处理
                    MessageItem msgI = (MessageItem) msg.obj;
                    String info = msgI.getInfo();
                    //{"userName":"kVV1Kxem2qsQLOaV","nickName":"YmirYU","sessionName":"Mintcode-Group"}
                    if (info != null) {
//						Map<String,String> map = TTJSONUtil.convertJsonToObj(info,new TypeReference<Map<String,String>>(){});
//						MTHttpParameters p = new MTHttpParameters();
//						p.setParameter("param", info);
//						Map<String,Object> map = p.getMap();
                        MessageInfoEntity entity = TTJSONUtil.convertJsonToCommonObj(info, MessageInfoEntity.class);
                        if (entity != null) {

                            Log.i("infos", mSet.size() + "---看看有几个---");
                            // 屏蔽单聊@功能
                            if (!isGroup) {
                                StringBuilder builder = new StringBuilder(mEtInput.getText().toString());
                                String nickName = entity.getNickName();
                                builder.append(nickName);
                                mEtInput.setText(builder.toString());

                                mEtInput.requestFocus();
                                mEtInput.setSelection(mEtInput.getText().length());
                                displayInputMethod(true);

                            } else if (!mSet.contains(entity)) {
                                mSet.add(entity);
//								Iterator it = mSet.iterator();
                                StringBuilder builder = new StringBuilder(mEtInput.getText().toString());
//								while (it.hasNext()){
//									builder.append(it.next());
//								}
                                if (builder.toString().endsWith("@")) {
                                    builder.deleteCharAt(builder.length() - 1);
                                }
                                String nickName = entity.getNickName();
                                String str = "@" + nickName + " ";
                                builder.append(str);
                                mEtInput.setText(builder.toString());

                                mEtInput.requestFocus();
                                mEtInput.setSelection(mEtInput.getText().length());
                                displayInputMethod(true);
                            }


                        }
// 						mEtInput.setText("@" + msgI.getToNickName() + " ");
                    }

                    break;
                case HandleMsgType.TYPE_HANDLE_DATA_FINISH:
                    int index = (int) msg.obj;
                    mListView.onRefreshComplete();
                    mChatListAdapter.addDataWhenRefresh(mListData.size(), false);
                    mChatListAdapter.notifyDataSetChanged();
                    mListView.getRefreshableView().setSelection(index);
                    break;
                default:
                    break;
            }
        }

    };
    private ResendDialogView resendDialogView;

    /**
     * 重发消息
     *
     * @param item
     */
    private void resendMsg(final MessageItem item) {
        mResendItem = item;
        resendDialogView.showAtLocation(mIvSend, Gravity.CENTER, 0, 0);
    }

    //


    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        // 下拉加载历史逻辑
        count = count + STEP;
        start = start + STEP;
        // 判断网络是有网络
        boolean isAvalable = NetWorkUtil.checkNetWorkIsAvalable(this);
        if (isAvalable) {
            // 有，从服务端获取历史
            if (mListData != null && !mListData.isEmpty()) {
                MessageItem item = mListData.get(0);
                long endTimeStamp = -1;
                if (item.getFromLoginName().contains("@APP")) {
                    endTimeStamp = item.getMsgId();
                } else {
                    endTimeStamp = item.getCreateDate();
                }
                IMNewApi.getInstance().getHistoryMessage(this, mToken, mUid, mUid, mTo, STEP, endTimeStamp);
            } else {
                mListView.onRefreshComplete();
                mListView.hideHeaderView();
                return;
            }

        } else {
            // 没有加载本地数据
            List<MessageItem> list = manager.getMessagesByIndexForDesc(mUid, mTo, start, count);
            if (list != null && !list.isEmpty()) {
                mListData.addAll(0, list);
                mChatListAdapter.notifyDataSetChanged();
                mListView.getRefreshableView().setSelection(mListData.size() - list.size());
            } else {
                mListView.onRefreshComplete();
                mListView.hideHeaderView();
                return;
            }
        }

//        Runnable r = new Runnable() {
//            @Override
//            public void run() {
//                mListView.onRefreshComplete();
//            }
//        };
//        mUIHandler.postDelayed(r,1000);

//


//        count = count + STEP;
//        start = start + STEP;
//        List<MessageItem> list = manager.getMessagesByIndexForDesc(mUid, mTo,
//                start, count);
//        if (list.size() == 0) {
//            if (mListData != null && mListData.size() > 0) {
//                long endTimeStamp = mListData.get(0).getCreateDate();
//                if (isGroup) {
////                    IMAPI.getInstance().getHistoryMessage(historyResponse, mToken, mUid, mUid, mTo, STEP, endTimeStamp);
//                    IMNewApi.getInstance().getHistoryMessage(this,mToken, mUid, mUid, mTo, STEP, endTimeStamp);
//                } else {
//                    if (!mUid.contains("@APP") && !mTo.contains("@APP")) {
////                        IMAPI.getInstance().getHistoryMessage(historyResponse, mToken, mUid, mUid, mTo, STEP, endTimeStamp);
//                        IMNewApi.getInstance().getHistoryMessage(this,mToken, mUid, mUid, mTo, STEP, endTimeStamp);
//                    } else {
//                        mListView.hideHeaderView();
//                        return;
//                    }
//                }
//            }
//            return;
//        }
//
//        mListData.addAll(0, list);
//        mListTopSelectionIndex = list.size();
//
//        mChatListAdapter.notifyDataSetChanged();
//        Log.i("infosss", "----ss----");
//        mListView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mListView.onRefreshComplete();
//                Log.i("infosss", "=====ss===");
////				mListView.getRefreshableView().setSelection(mListData.size());
//                mListView.getRefreshableView().setSelection(
//                        mListTopSelectionIndex < 3 ? mListTopSelectionIndex
//                                : mListTopSelectionIndex - 3);
//            }
//        }, 500);
    }

    private OnResponseListener historyResponse = new OnResponseListener() {

        @Override
        public void onResponse(Object response, String taskId, boolean rawData) {
            if (response instanceof HistoryMessagePOJO) {
                HistoryMessagePOJO pojo = (HistoryMessagePOJO) response;
                if (pojo.isResultSuccess()) {
                    List<MessageItem> result = pojo.getMsg();
                    if (result == null || result.size() == 0) {
                        mListView.hideHeaderView();
                        return;
                    }
                    if (result.size() <= 1) {
                        mListView.hideHeaderView();
                        return;
                    }
//                    Debug.startMethodTracing("fucking_");
                    Log.i("infosssss", "===insert===reuslt==");
                    MessageDBService.getInstance().insert(result);
                    Log.i("infosssss", "===insert===success==");
                    mListData.clear();
                    count += result.size();
                    start += result.size();
                    mListData = manager.getMessagesByIndexForDesc(mUid, mTo, 0, count);
                    mChatListAdapter.setData(mListData);
//                    Debug.stopMethodTracing();
                    Log.i("infosssss", "===get===success==");
                    Log.i("infosssss", "===response==" + mListData.size());
                }

                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListView.onRefreshComplete();
//						mListView.getRefreshableView().setSelection(mListData.size());
                        Log.i("infosss", "===response=post=" + mListData.size());
//                        mListView.getRefreshableView().setSelection(
//                                mListTopSelectionIndex < 3 ? mListTopSelectionIndex
//                                        : mListTopSelectionIndex - 3);
                    }
                }, 000);
            }
        }

        @Override
        public boolean isDisable() {
            // TODO Auto-generated method stub
            return false;
        }
    };

    class TimeCount extends CountDownTimer {

        private MessageItem item;

        public TimeCount(long millisInFuture, long countDownInterval,
                         MessageItem item) {
            super(millisInFuture, countDownInterval);
            this.item = item;
        }

        @Override
        public void onFinish() {
            if (item != null && item.getSent() == Command.STATE_SEND) {
                if (item.getType().equals(Command.IMAGE) || item.getType().equals(Command.AUDIO)) {
                    autoResendMsg(item);
                } else {
                    updateSendDefeat(item);
                }
            }
        }

        @Override
        public void onTick(long arg0) {

        }

    }

    @Override
    public void onSession(List<SessionItem> sections) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("infosss", "===onPause===");
        CacheManager.getInstance(this).fluchCache();
        mUnReadNumHandler.removeMessages(MSG);
//        mSensorManager.unregisterListener(this);
    }

    private int MSG = 0x01;
    private int mUpdateTime = 2000;
    private Handler mUnReadNumHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == MSG) {
                setUnReadNum();
                mUnReadNumHandler.sendEmptyMessageDelayed(MSG, mUpdateTime);
            }
        }
    };

    private void setUnReadNum() {
        List<SessionItem> mSessionItems = IMManager.getInstance().getSession();
        int num = 0;
        for (SessionItem item
                : mSessionItems) {
            if (toNickName != null && !toNickName.equals(item.getNickName())) {
                // 屏蔽群消息标志设置为10000，判断是否是真实的未读消息数
                if (item.getUnread() != 10000) {
                    num += item.getUnread();
                }
            }
        }
        if (num == 0) {
            mTvUnReadNum.setText("");
        } else {
            String strNum;
            if (num > 99) {

                strNum = "( 99+ )";

            } else {
                strNum = "( " + num + " )";
            }
            mTvUnReadNum.setText(strNum);
        }
    }

    @Override
    public void getSecondOption() {
        startCamera();
    }

    @Override
    public void selectFristOption() {
        startGallery();
    }


    @Override
    public Loader<List<MessageItem>> onCreateLoader(int id, Bundle args) {
        Log.i("infosss", "===onCreateLoader==");
        AsyncTaskLoader loader = new AsyncTaskLoader(this) {
            @Override
            public Object loadInBackground() {
                List<MessageItem> list = manager.getMessagesByIndexForDesc(mUid, mTo, start, count);
                return list;
            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }
        };
//		List<MessageItem> loader = manager.getMessagesByIndexForDesc(mUid, mTo, start, count);
//		loader.
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<MessageItem>> loader, List<MessageItem> data) {
        if (mListData == null || mListData.isEmpty()) {
            showLoading();
            mListData = data;
            mChatListAdapter.setData(data);
            mListView.getRefreshableView().setSelection(data.size());

            List<MessageItem> unreadMsg = MessageDBService.getInstance()
                    .findUnReadRevMsgId(mTo, mUid);
            for (int i = 0; i < unreadMsg.size(); i++) {
//                long msgId = unreadMsg.get(i).getMsgId();
                MessageItem item = unreadMsg.get(i);
                // 收到的信息才做已读处理
                if (item.getCmd() == MessageItem.TYPE_RECV) {
                    // 发送已读请求
                    Map map = getSendReadContent(item);
                    unreadMsgId.add(map);
                }
//                unreadMsgId.add(String.valueOf(msgId));
            }
//            IMAPI.getInstance().readSession(readSessionResponse, mToken, mUid, mTo,
//                    unreadMsgId);
            // 发送已读
            IMNewApi.getInstance().readSession(this, mToken, mUid, mTo, unreadMsgId);


            //更新标志

            try {
                List<SessionItem> l = DataSupport.where("oppositeName = ? and userName = ?", mTo, mUid).find(SessionItem.class);
                if (l != null && !l.isEmpty()) {
                    SessionItem item = l.get(0);
                    if (item != null && !data.isEmpty()) {
                        ContentValues cv = new ContentValues();
                        MessageItem msg = data.get(data.size() - 1);
                        cv.put("type", 1);
                        DataSupport.update(SessionItem.class, cv, item.getId());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            Runnable r = new Runnable() {
                @Override
                public void run() {
                    getBetweenMessage();
                }
            };

            mUIHandler.postDelayed(r, 1000);

        }
//        else {
//            mListData = new ArrayList<>();
//        }


//        getParticleSystem(R.drawable.emoji_1f384, mListView);
    }

    @Override
    public void onLoaderReset(Loader<List<MessageItem>> loader) {
        mChatListAdapter.setData(null);
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        // 判断是否为null
        Log.i("taskId===", taskId + "------");
        if (response == null) {
            showNoNetWork();
            return;
        } else

            // 判断是否是设置已读返回
            if (taskId.equals(IMNewApi.TaskId.TASK_READ_SESSION)) {
                handleReadSession(response);
            } else

                // 判断是否是历史消息返回
                if (taskId.equals(IMNewApi.TaskId.TASK_HISTORYMESSAGE)) {
                    handleHistoryMsg(response);
                    dismissLoading();
                }

        // 判断是否是批量标记重点
        if (taskId.equals(IMNewApi.TaskId.TASK_ADD_BOOK_MARK_LIST)) {
            handleMarkMsgList(response);
        } else
            // 合并转发
            if (taskId.equals(IMNewApi.TaskId.TASK_FORWORD_MERGE_MESSAGE)) {
                handleMergeMsgLit(response);
            }

    }

    /**
     * 处理历史信息
     *
     * @param response
     */
    private void handleHistoryMsg(Object response) {
        SessionHistoryMessagePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), SessionHistoryMessagePOJO.class);
        if (pojo != null) {
            // 判断返回是否成功
            if (pojo.isResultSuccess()) {
                final List<HistoryMsgEntity> result = pojo.getMsg();
                if (result == null || result.isEmpty()) {
                    mListView.onRefreshComplete();
                    mListView.hideHeaderView();
                    return;
                }
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        handelData(result);
                    }
                };
                t.start();

                // 组装数据
//                List<MessageItem> list = null;
//                if(mTo.contains("@APPd")){
//                    // 过滤历史消息
////                    List<HistoryMsgEntity> filerList = getFilterMessageList(result);
////                    list = getMessageItemList(filerList);
//                }else {
//
//                }
//                list = getMessageItemList(result);
//
////                for(int i = 0 ; i < list.size() ; i++){
////                    MessageItem msg = list.get(i);
////                    Log.i("MessageDBService", msg.getContent() + "----" + msg.getMsgId());
////                }
//                // 将数据写入本地
//                MessageDBService.getInstance().insert(list);
//                int size = mListData.size();
//                mListData.clear();
////                count += result.size();
////                start += result.size();
////                List<MessageItem> l = manager.getMessagesByIndexForDesc(mUid, mTo, 0, list.size());
//                List<MessageItem> l = manager.getMessagesByIndexForDesc(mUid, mTo, 0, count);
//                mListData.addAll(l);
////                mChatListAdapter.setData(mListData);
//                mChatListAdapter.notifyDataSetChanged();
//                final int index = l.size() - size;
//                // 判断是不是第一次加载
//                if(STEP == count){
////                    setUpdateSession(result.get(0));
//                    mListView.getRefreshableView().setSelection(mListData.size() - 1);
//                }else {
////                    int visiableCount = mListView.getlastVisiblePosition() - mListView.getFirstVisiblePosition();
////                    mListView.getRefreshableView().setSelectionFromTop(mListData.size() - list.size(),0);
////                    int x = mListData.size() - list.size() + visiableCount;
//
////                    mListView.getRefreshableView().setse
//                    Runnable r = new Runnable() {
//                        @Override
//                        public void run() {
//                            mListView.getRefreshableView().setSelection(index + 1);
//                        }
//                    };
//                    mUIHandler.postDelayed(r,100);
//                }
//
            }
//
        } else {
            mListView.onRefreshComplete();
        }
    }

    private void handelData(List<HistoryMsgEntity> result) {
        // 组装数据
        List<MessageItem> list = null;
        list = getMessageItemList(result);

        // 将数据写入本地
        MessageDBService.getInstance().insert(list);
        int size = mListData.size();
        List<MessageItem> l = manager.getMessagesByIndexForDesc(mUid, mTo, 0, count);
        mListData.clear();
        mListData.addAll(l);
        Message msg = Message.obtain();
        msg.what = HandleMsgType.TYPE_HANDLE_DATA_FINISH;
        final int index = l.size() - size;
        // 判断是不是第一次加载
        if (STEP == count) {
            msg.obj = mListData.size() - 1;
        } else {
            msg.obj = index + 1;
        }
        mUIHandler.sendMessage(msg);
    }


    /**
     * 处理已读返回
     *
     * @param response
     */
    private void handleReadSession(Object response) {
        for (int i = 0; i < unreadMsgId.size(); i++) {
            Map map = unreadMsgId.get(i);
            Long time = (Long) map.get("msgId");
//                Long time = Long.valueOf(unreadMsgId.get(i));
            MessageDBService.getInstance().updateReadSession(mTo, mUid,
                    time);
        }
    }


    /**
     * 过滤应用历史消息
     *
     * @return
     */
    private List<HistoryMsgEntity> getFilterMessageList(List<HistoryMsgEntity> list) {
        List<HistoryMsgEntity> l = new ArrayList<>();
        String nickname = LauchrConst.header.getUserName();
        for (int i = 0; i < list.size(); i++) {
            HistoryMsgEntity entity = list.get(i);
            String info = entity.getInfo();
            if (info != null && !info.equals("")) {
                TypeReference<HashMap<String, String>> valueType = new TypeReference<HashMap<String, String>>() {
                };
                Map m = TTJSONUtil.convertJsonToObj(info, valueType);
                String nickName = (String) m.get("nickName");
                if (nickName.equals(nickname) || nickName.equals("System")) {
                    l.add(entity);
                } else {
                    Log.i("infosos", nickName + "===");
                }
            }
        }
        return l;
    }

    /**
     * 从历史消息中组装消息
     *
     * @param list
     * @return
     */
    private List<MessageItem> getMessageItemList(List<HistoryMsgEntity> list) {
        unreadMsgId.clear();
        List<MessageItem> l = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            MessageItem item = new MessageItem();
            HistoryMsgEntity entity = list.get(i);
            item.setMsgId(entity.getMsgId());
            item.setInfo(entity.getInfo());
            if (entity.getUnReadCount() == 0) {
                item.setIsRead(1);
            } else {
                item.setIsRead(0);
            }
            item.setContent(entity.getContent());
            item.setType(entity.getType());
            item.setFromLoginName(entity.getFromLoginName());
            item.setToLoginName(entity.getToLoginName());
            item.setClientMsgId(entity.getClientMsgId());
            item.setCreateDate(entity.getCreateDate());
            item.setModified(entity.getModified());
            item.setIsMarkPoint(entity.getBookMark());
            Log.i("getMessageItemList", entity.getContent() + "----" + entity.getMsgId());
            // 判断是否和前一条间隔时间相差五分钟
            if (i > 0) {
                HistoryMsgEntity e = list.get(i - 1);
                long msgId = e.getMsgId();
                long currentMsgId = entity.getMsgId();
                // 判断两条消息时间间隔是否大于5分钟
                long x = msgId - currentMsgId;

                if ((msgId - currentMsgId) >= 60 * 5 * 1000) {
                    MessageItem msgItem = new MessageItem();
                    msgItem.setMsgId(msgId - 1);
                    msgItem.setContent(TimeFormatUtil.formatTime(msgId));
                    msgItem.setType(Command.ALERT);

                    msgItem.setFromLoginName(e.getFromLoginName());
                    msgItem.setToLoginName(e.getToLoginName());
                    msgItem.setClientMsgId(e.getClientMsgId() - 1);
                    msgItem.setCreateDate(e.getCreateDate());
                    msgItem.setModified(e.getModified());
                    msgItem.setIsMarkPoint(e.getBookMark());
                    msgItem.setInfo(e.getInfo());

                    // 判断消息是否是撤回
                    if (Command.RESEND.equals(e.getType())) {
                        String content = e.getContent();
                        ResendEntity resendEntity = TTJSONUtil.convertJsonToCommonObj(content, ResendEntity.class);
                        msgItem.setMsgId(resendEntity.getMsgId() - 1);
                        msgItem.setClientMsgId(resendEntity.getClientMsgId() - 1);
                    }

                    if (!mTo.contains(com.mintcode.launchr.util.Const.SHOWID_TASK) &&
                            !mTo.contains(com.mintcode.launchr.util.Const.SHOWID_APPROVE) &&
                            !mTo.contains(com.mintcode.launchr.util.Const.SHOW_SCHEDULE)) {
                        l.add(msgItem);
                    }
                }
//                e.getMsgId()

            }

            l.add(item);

            // 发送已读
            if (item.getCmd() == MessageItem.TYPE_RECV) {
                Map map = getSendReadContent(item);
                unreadMsgId.add(map);
            }

        }

        // 发送已读
        IMNewApi.getInstance().readSession(this, mToken, mUid, mTo, unreadMsgId);

        return l;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float range = event.values[0];


        if (mAudioRecordPlayerUtil.isPlaying()) {
            if (range > mSensor.getMaximumRange() / 2) {
//                Toast.makeText(this, "正常模式", Toast.LENGTH_LONG).show();
                mAudioRecordPlayerUtil.setAudioModeNormal();
                if (mWakeLock != null) {
                    mWakeLock.setReferenceCounted(false);
                    mWakeLock.release(); // 释放设备电源锁
                }
            } else {
//                Toast.makeText(this, "听筒模式", Toast.LENGTH_LONG).show();
                mAudioRecordPlayerUtil.setAudioModeEarpiece();
                //把声音设定成Earpiece（听筒）出来，设定为正在通话中
                if (mWakeLock.isHeld()) {
                    return;
                } else {
                    mWakeLock.acquire();// 申请设备电源锁
                }
            }
            mAudioRecordPlayerUtil.startPlaying();
        } else {
            mAudioRecordPlayerUtil.setAudioModeNormal();
            if (mWakeLock != null) {
                mWakeLock.setReferenceCounted(false);
                mWakeLock.release(); // 释放设备电源锁
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 显示更多选项
     */
    public void showMoreContent() {
        int current = mListView.getRefreshableView().getFirstVisiblePosition();
        mTvMoreCancel.setVisibility(View.VISIBLE);
        mLinMoreContent.setVisibility(View.VISIBLE);
        mRelInputContainer.setVisibility(View.GONE);
        mTvUnReadNum.setVisibility(View.GONE);
        mIvBack.setVisibility(View.GONE);
        mIvGroup.setVisibility(View.GONE);
        mIvSearch.setVisibility(View.GONE);

        View view = mListView.getRefreshableView().getChildAt(0);
        if (view != null) {
            mListView.getRefreshableView().setSelectionFromTop(current, view.getTop());
        } else {
            mListView.getRefreshableView().setSelection(current);
        }

        mListView.setScrollToBottom(false);
    }

    /**
     * 逐条转发，合并转发
     */
    private void forwordMsgToFriend() {
        if (mChatListAdapter.mListMergeData.size() <= 0) {
            toast(getString(R.string.choose_msg));
            return;
        }

        final PressMessageDialog mPressMessageDialog = new PressMessageDialog(context);
        mPressMessageDialog.show();
        mPressMessageDialog.setMarkListener(R.string.chat_msg_list_forword, new OnClickListener() {
            @Override
            public void onClick(View v) {
                mergeMsg = 1;
                judgeMergeMessage();
                mPressMessageDialog.dismiss();
            }
        });
        mPressMessageDialog.setMsgTaskListener(context.getResources().getString(R.string.chat_msg_one_forword), new OnClickListener() {
            @Override
            public void onClick(View v) {
                mergeMsg = 2;
                judgeMergeMessage();
                mPressMessageDialog.dismiss();
            }
        });
    }

    /**
     * 判断是否包含不必要的消息
     */
    private void judgeMergeMessage() {
        boolean isIncludeMerge = false;
        for (int i = 0; i < mChatListAdapter.mListMergeData.size(); i++) {
            MessageItem item = mChatListAdapter.mListMergeData.get(i);
            if (item.getType().equals(Command.MERGE) || item.getType().equals(Command.AUDIO) || item.getType().equals(Command.EVENT)) {
                isIncludeMerge = true;
            }
        }
        if (isIncludeMerge) {
            final ChooseDialog dialog = new ChooseDialog(this);
            dialog.show();
            if (mergeMsg == 1) {
                dialog.setDialogTitle(getString(R.string.merge_msg_onebyone));
            } else {
                dialog.setDialogTitle(getString(R.string.merge_msg_onetime));
            }
            final TextView tvConfirm = (TextView) dialog.getWindow().findViewById(R.id.tv_confrim);
            tvConfirm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChatActivity.this, ChooseMemberActivity.class);
                    intent.putExtra(ChooseMemberActivity.SELECT_PERSON_TYPE, false);
                    startActivityForResult(intent, REQA_FROM_CHOOSE_MERGE_MEMBER);
                    dialog.dismiss();
                }
            });
        } else {
            Intent intent = new Intent(ChatActivity.this, ChooseMemberActivity.class);
            intent.putExtra(ChooseMemberActivity.SELECT_PERSON_TYPE, false);
            startActivityForResult(intent, REQA_FROM_CHOOSE_MERGE_MEMBER);
        }
    }

    /**
     * 开始进行转发
     */
    private void startMergeMessage(List<String> toUserId, String toUserName) { //List<String> toUserId ,List<String> toUserName
        mListView.setScrollToBottom(true);
        mChatListAdapter.showMergeButton = false;
        mChatListAdapter.mChooseMergeData = null;
        mLinMoreContent.setVisibility(View.GONE);
        mRelInputContainer.setVisibility(View.VISIBLE);
        mChatListAdapter.notifyDataSetChanged();

        List<MergeEntity> mergeList = new ArrayList<MergeEntity>();
        for (int i = 0; i < mChatListAdapter.mListMergeData.size(); i++) {
            MessageItem item = mChatListAdapter.mListMergeData.get(i);
            if (mergeMsg == 1 && (item.getType().equals(Command.MERGE) || item.getType().equals(Command.AUDIO) || item.getType().equals(Command.EVENT))) {
                // 如果是合并转发，并且是合并转发消息，跳过
                continue;
            } else if (mergeMsg == 2 && (item.getType().equals(Command.AUDIO) || item.getType().equals(Command.EVENT))) {
                // 如果是逐条转发，跳过
                continue;
            } else {
                MergeEntity entity = new MergeEntity();
                entity.setClientMsgId(System.currentTimeMillis());
                entity.setCreateDate(item.getCreateDate());
                entity.setContent(item.getContent());
                entity.setFrom(mUid);
                if (mergeMsg == 2) {
                    Info info = new Info();
                    info.setNickName(myName);
                    info.setUserName(mUid);
                    entity.setInfo(TTJSONUtil.convertObjToJson(info));
                } else {
                    entity.setInfo(item.getInfo());
                }
                entity.setModified(item.getModified());
                entity.setMsgId(item.getMsgId());
                entity.setType(item.getType());
                mergeList.add(entity);
            }
        }
        String token = KeyValueDBService.getInstance().find(com.mintcode.im.util.Keys.TOKEN);
        Info info = new Info();
        info.setNickName(myName);
        info.setUserName(mUid);
        String title = "";
        if (isGroup) {
            title = mTvTitle.getText().toString() + getString(R.string.chat_msg_record);
        } else {
            title = getString(R.string.somebodys_chatinfo).replace("#@1", myName).replace("#@2", toNickName);
        }
//        List<String> toList = new ArrayList<String>();
//        toList.add(toUserId);
        //toUserId
        if (mergeMsg == 1) {
            // 合并转发
            IMNewApi.getInstance().forwordMergeMessage(this, token, mUid, TTJSONUtil.convertObjToJson(info),
                    toUserId, mergeList, Command.MERGE, title, 0, System.currentTimeMillis());
        } else if (mergeMsg == 2) {
            // 逐条转发
            IMNewApi.getInstance().forwordMergeMessage(this, token, mUid, TTJSONUtil.convertObjToJson(info),
                    toUserId, mergeList, Command.MERGE, title, 1, System.currentTimeMillis());
        }
    }

    /**
     * 批量标记重点
     */
    private void markMsgList() {
        if (mChatListAdapter.mListMergeData.size() <= 0) {
            toast(getString(R.string.choose_msg));
            return;
        }

        mListView.setScrollToBottom(true);
        mChatListAdapter.showMergeButton = false;
        mChatListAdapter.mChooseMergeData = null;
        mLinMoreContent.setVisibility(View.GONE);
        mRelInputContainer.setVisibility(View.VISIBLE);
        mChatListAdapter.notifyDataSetChanged();

        List<Long> msgId = new ArrayList<Long>();
        for (int i = 0; i < mChatListAdapter.mListMergeData.size(); i++) {
            msgId.add(mChatListAdapter.mListMergeData.get(i).getMsgId());
        }
        String name = KeyValueDBService.getInstance().find(com.mintcode.im.util.Keys.UID);
        String token = KeyValueDBService.getInstance().find(com.mintcode.im.util.Keys.TOKEN);
        IMNewApi.getInstance().addBookMarkList(this, token, name, msgId, mTo);
    }

    /**
     * 批量标记重点
     */
    private void handleMarkMsgList(Object response) {
        DataPOJO pojo = com.mintcode.chat.util.JsonUtil.convertJsonToData(response.toString());
        if (pojo == null) {
            return;
        }
//        else if(pojo.isResultSuccess()){
//            toast(getString(R.string.msg_change__schedule_success));
//        }
    }

    /**
     * 消息转发
     */
    private void handleMergeMsgLit(Object response) {
        DataPOJO pojo = com.mintcode.chat.util.JsonUtil.convertJsonToData(response.toString());
        if (pojo == null) {
            return;
        } else if (pojo.isResultSuccess()) {
            toast(getString(R.string.msg_change__schedule_success));
        }
    }

    /**
     * 监听返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mChatListAdapter.showMergeButton) {
                mListView.setScrollToBottom(true);
                mChatListAdapter.showMergeButton = false;
                mChatListAdapter.mChooseMergeData = null;
                mChatListAdapter.mListMergeData = null;
                mChatListAdapter.notifyDataSetChanged();
                mLinMoreContent.setVisibility(View.GONE);
                mTvMoreCancel.setVisibility(View.GONE);
                mRelInputContainer.setVisibility(View.VISIBLE);
                mTvUnReadNum.setVisibility(View.VISIBLE);
                mIvBack.setVisibility(View.VISIBLE);
                mIvGroup.setVisibility(View.VISIBLE);
                mIvSearch.setVisibility(View.VISIBLE);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置当前listView的位置
     */
    public void setListViewPosition() {
        int current = mListView.getRefreshableView().getFirstVisiblePosition();

        mChatListAdapter.notifyDataSetChanged();
        View view = mListView.getRefreshableView().getChildAt(0);
        if (view != null) {
            mListView.getRefreshableView().setSelectionFromTop(current, view.getTop());
        } else {
            mListView.getRefreshableView().setSelection(current);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mChatListAdapter.showMergeButton) {
            MessageItem item = mListData.get(position - 1);
            if (item.getType().equals(Command.TEXT) || item.getType().equals(Command.IMAGE) || item.getType().equals(Command.AUDIO)
                    || item.getType().equals(Command.FILE) || item.getType().equals(Command.MERGE)) {
                mChatListAdapter.addMsgToMergeList(item, position - 1);
            }
        }
    }
}
