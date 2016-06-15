package com.mintcode.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;

import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mintcode.DataPOJO;
import com.mintcode.cache.MImageCache;
import com.mintcode.chat.activity.ChatActivity.HandleMsgType;
import com.mintcode.chat.activity.ChatActivity;
import com.mintcode.chat.activity.MergeMsgActivity;
import com.mintcode.chat.activity.PressMessageDialog;
import com.mintcode.chat.activity.PreviewLargeImageActivity;
import com.mintcode.chat.audio.AudioItem;
import com.mintcode.chat.audio.AudioPlayingAnimation;
import com.mintcode.chat.emoji.EmojiParser;
import com.mintcode.chat.emoji.ParseEmojiMsgUtil;
import com.mintcode.chat.entity.Info;
import com.mintcode.chat.entity.MergeEntity;
import com.mintcode.chat.image.AttachItem;
import com.mintcode.chat.image.MutiSoundUpload;
import com.mintcode.chat.imgshow.FileShow;
import com.mintcode.chat.util.AudioRecordPlayUtil;
import com.mintcode.chat.util.BitmapUtil;
import com.mintcode.chat.util.JsonUtil;
import com.mintcode.chat.widget.DirectImageView;
import com.mintcode.im.Command;
import com.mintcode.im.database.KeyValueDBService;
import com.mintcode.im.database.MessageDBService;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.util.IMConst;
import com.mintcode.im.util.Keys;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.ApproveAPI;
import com.mintcode.launchr.api.IMAPI;
import com.mintcode.launchr.api.IMNewApi;
import com.mintcode.launchr.api.MeetingApi;
import com.mintcode.launchr.api.TaskApi;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.api.UserApi.TaskId;
import com.mintcode.launchr.app.meeting.activity.MeetingDetailActivity;
import com.mintcode.launchr.app.newApproval.Entity.MessageFormData;
import com.mintcode.launchr.app.newApproval.activity.ApproveDetailActivity;
import com.mintcode.launchr.app.newApproval.view.FormViewUtil;
import com.mintcode.launchr.app.newApproval.window.AgreePopWindow;
import com.mintcode.launchr.app.newApproval.window.WriteCommentPopWindow;
import com.mintcode.launchr.app.newTask.activity.TaskDetailActivity;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.contact.activity.DepartmentActivity;
import com.mintcode.launchr.contact.activity.PersonDetailActivity;
import com.mintcode.launchr.message.activity.FileDetailActivity;
import com.mintcode.launchr.pojo.ApprovalDetailPOJO;
import com.mintcode.launchr.pojo.ChatUserDetailPOJO;
import com.mintcode.launchr.pojo.MeetingDetailPOJO;
import com.mintcode.launchr.pojo.MeetingPOJO;
import com.mintcode.launchr.pojo.NormalPOJO;
import com.mintcode.launchr.pojo.TaskDetailPOJO;
import com.mintcode.launchr.pojo.entity.ChatUserDetailEntity;
import com.mintcode.launchr.pojo.entity.MessageEventAlertEntity;
import com.mintcode.launchr.pojo.entity.MessageEventApproveEntity;
import com.mintcode.launchr.pojo.entity.MessageEventEntity;
import com.mintcode.launchr.pojo.entity.MessageEventScheduleEntity;
import com.mintcode.launchr.pojo.entity.MessageEventTaskEntity;
import com.mintcode.launchr.pojo.entity.MessageInfoEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.CustomToast;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.MixPanelUtil;
import com.mintcode.launchr.util.RegularUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.TimeFormatUtil;
import com.mintcode.launchr.view.MTContainerPopWindow;
import com.mintcode.launchr.widget.RoundBackgroundColorSpan;
import com.mintcode.launchrnetwork.OnResponseListener;

import butterknife.ButterKnife;

/**
 * 聊天界面 适配器 聊天共有 文本，语音，图片等7中类型，所以需要根据消息类型复用convertView
 *
 * @author ChristLu
 */
public class ChatListAdapter extends BaseAdapter implements AgreePopWindow.ItemSelectListener, WriteCommentPopWindow.startActivityLinsenter {


    private Context mContext;

    private List<MessageItem> mListData = new LinkedList<MessageItem>();

    private LayoutInflater inflater;

    /**
     * 对方的信息显示的view 语音
     */
    private Drawable mDrawableLeft;
    /**
     * 自己的信息显示的view 语音
     */
    private Drawable mDrawableRight;

    /**
     * 消息类型布局
     */
    public static final int TYPE_MSG_TEXT_LEFT = 0x001;// 文本消息 左
    public static final int TYPE_MSG_TEXT_RIGHT = 0x002;// 文本消息 右
    public static final int TYPE_MSG_IMAGE_LEFT = 0x003;// 图片消息 左
    public static final int TYPE_MSG_IMAGE_RIGHT = 0x004;// 图片消息 右
    public static final int TYPE_MSG_AUDIO_LEFT = 0x005;// 图片语音右
    public static final int TYPE_MSG_AUDIO_RIGHT = 0x006;// 图片语音 右
    public static final int TYPE_MSG_VIDIO_LEFT = 0x007;// 图片语音右
    public static final int TYPE_MSG_VIDIO_RIGHT = 0x008;// 图片语音 右
    public static final int TYPE_MSG_ALERT = 0x009;// 推送消息
    public static final int TYPE_MSG_EVENT_TASK = 0x00a;
    public static final int TYPE_MSG_EVENT_SCHEDULE = 0x00b;
    public static final int TYPE_MSG_EVENT_APPROVE = 0x00c;
    public static final int TYPE_MSG_EVENT_ALERT = 0x00d;
    public static final int TYPE_MSG_FILE_LEFT = 0X00e;  //接受的文件
    public static final int TYPE_MSG_FILE_RIGHT = 0x00f;  //发送文件
    public static final int TYPE_MSG_MERGE_RIGHT = 0x010;  //消息合并转发 右
    public static final int TYPE_MSG_MERGE_LEFT = 0x11;  // 消息合并左

    public static final int NUM_VIEW_TYPE = 0x012;// 总数必须必布局类型要大
    private String myName = "";
    private String myAvatar;
    private AudioRecordPlayUtil mAudioRecordPlayUtil;

    private Handler mUIHandler;

    private String url;

    private boolean isGroup;

    private MTContainerPopWindow mContainerPopWindow;

    /**
     * 弹出框的容器
     */
    private RelativeLayout mRelInputContainer;

    /**
     * 申请的同意弹窗
     */
    private AgreePopWindow mAgreePopWindow;

    /**
     * 申请中拒绝、打回、通过，日程中拒绝并转交弹窗
     */
    private WriteCommentPopWindow mWriteCommentPopWindow;

    /**
     * 下一审批人
     */
    private UserDetailEntity mEntity;
    /**
     * 选人position
     */
    private int mIndex;

    /**
     * 点击了ListView中的那一项
     */
    private int number = 0;

    /**
     * 会话Id
     */
    private String sessionName;

    /**
     * 是否显示多选按钮
     */
    public boolean showMergeButton = false;
    public List<MessageItem> mListMergeData;
    public List<Boolean> mChooseMergeData;

    private List<MessageItem> photoList = null;

    private AudioManager audioManager;

    public ChatListAdapter(Context context, List<MessageItem> mListData, final AudioRecordPlayUtil mAudioRecordPlayUtil,
                           Handler handler, boolean isGroup, RelativeLayout mRelInputContainer, String sessionName) {
        this.isGroup = isGroup;
        this.mContext = context;
        this.mListData = mListData;
        mUIHandler = handler;
        inflater = LayoutInflater.from(context);
        this.mRelInputContainer = mRelInputContainer;
        this.sessionName = sessionName;

        this.mAudioRecordPlayUtil = mAudioRecordPlayUtil;
        String myInfoStr = KeyValueDBService.getInstance().find(Keys.INFO);
        Info myInfo = JsonUtil.convertJsonToCommonObj(myInfoStr, Info.class);
        myName = myInfo.getNickName();
        myAvatar = myInfo.getAvatar();
        url = KeyValueDBService.getInstance().find(Keys.HTTP_IP) + "/launchr";
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

//		getPhotoPath();
    }

    /**
     * 组装图片列表
     */
    private void getPhotoPath() {
        if (photoList != null) {
            photoList.clear();
        } else {
            photoList = new ArrayList<>();
        }

        Thread t = new Thread() {
            @Override
            public void run() {
                runPath();
            }
        };
        t.start();

    }

    private void runPath() {
        if (mListData != null && !mListData.isEmpty()) {
            for (int i = 0; i < mListData.size(); i++) {
                MessageItem item = mListData.get(i);
                String type = item.getType();
                if (type.equals(Command.IMAGE)) {
                    photoList.add(item);
                }
            }

        }
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            getPhotoPath();
        }
    };

    public void setData(List<MessageItem> mListData) {
        this.mListData = mListData;
        notifyDataSetChanged();
    }

    public void setData(int postion, MessageItem item) {
        mListData.set(postion, item);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (mUIHandler != null) {
            mUIHandler.removeCallbacks(r);
            mUIHandler.postDelayed(r, 500);
        }

        if (showMergeButton && mListData != null) {
            addDataWhenRefresh(mListData.size(), true);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position >= getCount())
            return null;
        ChatViewHolder holder = null;
        MessageItem item = mListData.get(position);
        number = position;
        int viewType = getItemViewType(position);
        int cmd = item.getCmd();
        if (convertView == null) {
            holder = new ChatViewHolder();
            switch (viewType) {
                case 0:
                case TYPE_MSG_TEXT_LEFT:
                case TYPE_MSG_TEXT_RIGHT:
                    /** 加载消息 控件-文本 */
                    convertView = setupTextViews(holder, cmd);
                    break;
                case TYPE_MSG_IMAGE_LEFT:
                case TYPE_MSG_IMAGE_RIGHT:
                    /** 加载消息 控件-图片 */
                    convertView = setupImageViews(holder, cmd);
                    break;
                case TYPE_MSG_AUDIO_LEFT:
                case TYPE_MSG_AUDIO_RIGHT:
                    /** 加载消息 控件-语音 */
                    convertView = setupAudioViews(holder, cmd);
                    break;
                case TYPE_MSG_VIDIO_LEFT:
                case TYPE_MSG_VIDIO_RIGHT:
                    /** 加载消息 控件-视频 */
                    convertView = setupVidioViews(holder, cmd);
                    break;
                case TYPE_MSG_EVENT_TASK:
                    /** 加载消息 任务 */
                    convertView = setupEventTaskViews(holder, cmd, item);
                    break;
                case TYPE_MSG_EVENT_SCHEDULE:
                    /** 加载消息 日程 */
                    convertView = setupEventScheduleViews(holder, cmd, item);
                    break;
                case TYPE_MSG_EVENT_APPROVE:
                    /** 加载消息 申请 */
                    convertView = setupEventApproveViews(holder, cmd, item);
                    break;
                case TYPE_MSG_EVENT_ALERT:
                    /** 加载消息 系统消息 */
                    convertView = setupEventAlertViews(holder, cmd, item);
                    break;
                case TYPE_MSG_FILE_LEFT:
                case TYPE_MSG_FILE_RIGHT:
                    /** 加载消息 文件*/
                    convertView = setupFileViews(holder, cmd, item);
                    break;
                case TYPE_MSG_ALERT:
                    convertView = setupAlertViews(holder);
                    break;
                case TYPE_MSG_MERGE_RIGHT:
                case TYPE_MSG_MERGE_LEFT:
                    /** 消息合并转发*/
                    convertView = setupMergeViews(holder, cmd);
                    break;
                default:
                    break;
            }


        } else {
            holder = (ChatViewHolder) convertView.getTag();
        }
        if (holder.ivMarkPoint != null) {
            if (item.getIsMarkPoint() == 1) {
                holder.ivMarkPoint.setVisibility(View.VISIBLE);
            } else {
                holder.ivMarkPoint.setVisibility(View.INVISIBLE);
            }
        }
        switch (viewType) {
            case TYPE_MSG_TEXT_LEFT:
            case TYPE_MSG_TEXT_RIGHT:
                setTextContent(holder, item, position);
                break;
            case TYPE_MSG_IMAGE_LEFT:
            case TYPE_MSG_IMAGE_RIGHT:
                // 设置聊天图片消息
                setChatImage(holder, item, position);
                break;
            case TYPE_MSG_AUDIO_LEFT:
            case TYPE_MSG_AUDIO_RIGHT:
                // 设置聊天语音消息
                setChatAudio(holder, item, position);
                break;
            case TYPE_MSG_VIDIO_LEFT:
            case TYPE_MSG_VIDIO_RIGHT:
                // 设置聊天视频消息
                setChatVidio(holder, item, position);
                break;
            case TYPE_MSG_EVENT_TASK:
                setEventTaskContent(holder, item, position);
                break;
            case TYPE_MSG_EVENT_SCHEDULE:
                setEventScheduleContent(holder, item);
                break;
            case TYPE_MSG_EVENT_APPROVE:
                setEventApproveContent(holder, item);
                break;
            case TYPE_MSG_EVENT_ALERT:
                setEventAlertContent(holder, item);
                break;
            case TYPE_MSG_ALERT:
                holder.time.setText(item.getContent());
                break;
            case TYPE_MSG_FILE_LEFT:
            case TYPE_MSG_FILE_RIGHT:
                setFileContent(holder, item, position);
                break;
            case TYPE_MSG_MERGE_RIGHT:
            case TYPE_MSG_MERGE_LEFT:
                setMergeMsgContent(holder, item, position);
                break;
            default:
                break;
        }
        if (viewType != TYPE_MSG_ALERT && viewType != TYPE_MSG_EVENT_ALERT && !item.getType().equals(Command.CMD)) {
            // 设置消息点击重发
            setOnClickListener(holder.ivFailed, item);

            setOnLongClickListener(holder, position);

            // 设置显示进度条
            setSendingProcess(holder, item);

            // 根据收发消息设置头像，由于事件的头像在content字段中，因此判断
            if (!item.getType().equals(Command.EVENT)) {
                setAvatarAndName(holder, item);
            }

            // 转发按钮
            if (!item.getType().equals(Command.EVENT)) {
                setShowMergeButton(holder, position);
            }

            /** 设置聊天时间 */
            if (!item.getType().equals(Command.EVENT)) {
                setChatTime(holder, item, position);
            }
        }

        // 设置长按头像@功能
        if (holder.ivAvatar != null) {
            clickHeadViewSendInfo(holder.ivAvatar, item);
        }


        return convertView;
    }

    /**
     * @param iv
     * @某个人发消息
     */
    private void clickHeadViewSendInfo(final ImageView iv, final MessageItem item) {

        iv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (v == iv) {
                    Message msg = Message.obtain();
                    msg.what = HandleMsgType.TYPE_SEND_TO_TARGET;
                    msg.obj = item;
                    mUIHandler.sendMessage(msg);
                    return true;
                }
                return false;
            }
        });

    }

    // 点击发送失败标志进行消息重发
    private void setOnClickListener(ImageView ivFailed, final MessageItem item) {
        if (ivFailed == null) {
            return;
        }
        ivFailed.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ConnectivityManager con = (ConnectivityManager) mContext
                        .getSystemService(mContext.CONNECTIVITY_SERVICE);
                boolean wifi = con
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                        .isConnectedOrConnecting();
                boolean internet = con.getNetworkInfo(
                        ConnectivityManager.TYPE_MOBILE)
                        .isConnectedOrConnecting();
                if (wifi | internet) {
                    Message msg = new Message();
                    msg.what = HandleMsgType.TYPE_RESEND_TEXT;
                    msg.obj = item;
                    mHandler.sendMessage(msg);
                } else {
                    Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setOnLongClickListener(final ChatViewHolder holder,
                                        final int position) {
        View view = null;
        int viewType = getItemViewType(position);
        final MessageItem item = mListData.get(position);
        switch (viewType) {
            case 0:
            case TYPE_MSG_TEXT_LEFT:
            case TYPE_MSG_TEXT_RIGHT:
                /** 加载消息 控件-文本 */
                view = holder.textView;
                break;
            case TYPE_MSG_IMAGE_LEFT:
            case TYPE_MSG_IMAGE_RIGHT:
                /** 加载消息 控件-图片 */
                view = holder.imageView;
                break;
            case TYPE_MSG_AUDIO_LEFT:
            case TYPE_MSG_AUDIO_RIGHT:
                /** 加载消息 控件-语音 */
                view = holder.textView;
                break;
            case TYPE_MSG_VIDIO_LEFT:
            case TYPE_MSG_VIDIO_RIGHT:
                /** 加载消息 控件-视频 */
                view = holder.ivVidio;
                break;
            case TYPE_MSG_EVENT_TASK:
                view = holder.textView;
            case TYPE_MSG_FILE_LEFT:
            case TYPE_MSG_FILE_RIGHT:
                /** 加载消息 控件-文件*/
                view = holder.relFileLayout;
                break;
            case TYPE_MSG_ALERT:
                return;
            case TYPE_MSG_MERGE_LEFT:
            case TYPE_MSG_MERGE_RIGHT:
                view = holder.container;
                break;
            default:
                break;
        }

        final View finalView = view;
        if (view == null) {
            return;
        }
        view.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (!showMergeButton) {
                    longTimePressMsg(holder, item, position);
                }
                return false;
            }
        });
    }

    /**
     * 消息长按
     */
    private void longTimePressMsg(final ChatViewHolder holder, final MessageItem item, final int position) {
        int rname = item.getIsMarkPoint() == 0 ? R.string.message_mark_point : R.string.message_unmark_point;
        final PressMessageDialog mPressMessageDialog = new PressMessageDialog(mContext);
        mPressMessageDialog.show();
        if (Command.TEXT.equals(item.getType())) {
            mPressMessageDialog.setMsgCopyListener(mContext.getResources().getString(R.string.calendar_copy),
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("simple text", item.getContent());
                            clipboard.setPrimaryClip(clip);
                            mPressMessageDialog.dismiss();
                        }
                    });

            // 长按装日程任务啥的
            mPressMessageDialog.setMsgTaskListener(mContext.getString(R.string.msg_change_to_task), new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPressMessageDialog.dismiss();
                    ((ChatActivity) mContext).addTask(mListData.get(position).getContent());
                }
            });
            mPressMessageDialog.setMsgScheduleListener(mContext.getString(R.string.msg_change_to_schedule), new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPressMessageDialog.dismiss();
                    ((ChatActivity) mContext).changeMsgToSchedule(mListData.get(position).getContent());
                }
            });

        } else if (Command.AUDIO.equals(item.getType())) {
            // 判断是否是语音长按
            AppUtil util = AppUtil.getInstance(mContext);
            final boolean isSpeakerphoneOn = util.getBooleanValue("key_speakerphone");
            String strKey = "";
            if (!isSpeakerphoneOn) {
                strKey = "使用听筒模式";
            } else {
                strKey = "使用扬声器模式";
            }
            mPressMessageDialog.setVoicePlayStyle(strKey, new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isSpeakerphoneOn) {
                        audioManager.setSpeakerphoneOn(true);
                        audioManager.setMode(AudioManager.MODE_NORMAL);
                        AppUtil.getInstance(mContext).saveBooleanValue("key_speakerphone", false);
                    } else {
                        audioManager.setSpeakerphoneOn(false);
                        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                        AppUtil.getInstance(mContext).saveBooleanValue("key_speakerphone", true);
                    }

                    mPressMessageDialog.dismiss();
                }
            });
        }
        if (item.getCmd() == MessageItem.TYPE_SEND) {
            // 个人发送的消息，撤回
            mPressMessageDialog.setMsgResendListener(mContext.getResources().getString(R.string.msg_resend), new OnClickListener() {
                @Override
                public void onClick(View v) {
                    resendMessage(item);
                    mPressMessageDialog.dismiss();
                }
            });
        }
        mPressMessageDialog.setMarkListener(rname, new OnClickListener() {
            @Override
            public void onClick(View v) {
                int markPoint = item.getIsMarkPoint() == 0 ? 1 : 0;
                String name = KeyValueDBService.getInstance().find(Keys.UID);
                String token = KeyValueDBService.getInstance().find(Keys.TOKEN);
                if (holder.ivMarkPoint != null) {
                    if (markPoint == 1) {
                        holder.ivMarkPoint.setVisibility(View.VISIBLE);
                        IMNewApi.getInstance().addBookMark(listenr, token, name, item.getMsgId(), sessionName);
                    } else {
                        holder.ivMarkPoint.setVisibility(View.INVISIBLE);
                        IMNewApi.getInstance().deleteBookMark(listenr, token, name, item.getMsgId(), sessionName);
                    }
                }
                mPressMessageDialog.dismiss();
            }
        });
        mPressMessageDialog.setMsgMoreListener(mContext.getString(R.string.show_more_text), new OnClickListener() {
            @Override
            public void onClick(View v) {
                mChooseMergeData = new ArrayList<Boolean>();
                mListMergeData = new ArrayList<MessageItem>();
                showMergeButton = true;

                for (int i = 0; i < mListData.size(); i++) {
                    if (i == position) {
                        mListMergeData.add(mListData.get(position));
                        mChooseMergeData.add(true);
                    } else {
                        mChooseMergeData.add(false);
                    }
                }

                ((ChatActivity) mContext).showMoreContent();
                notifyDataSetChanged();

                mPressMessageDialog.dismiss();
            }
        });
    }

    /**
     * 撤回消息
     */
    private void resendMessage(MessageItem item) {
        String name = KeyValueDBService.getInstance().find(Keys.UID);
        String token = KeyValueDBService.getInstance().find(Keys.TOKEN);
        String nickName = AppUtil.getInstance(mContext).getUserName();
        String content = null;
        String resendInfo = null;
        if (item.getCmd() == MessageItem.TYPE_RECV) {
            resendInfo = item.getInfo();
            Info info = TTJSONUtil.convertJsonToCommonObj(item.getInfo(), Info.class);
            content = info.getNickName() + mContext.getString(R.string.other_msg_resend_success);
        } else {
            Info info = new Info();
            info.setUserName(name);
            info.setNickName(nickName);
            resendInfo = TTJSONUtil.convertObjToJson(info);
            content = nickName + mContext.getString(R.string.other_msg_resend_success);
        }
        IMAPI.getInstance().resendMessage(resendListener, token, name, Command.ALERT, content, item.getFromLoginName(), item.getToLoginName(),
                item.getClientMsgId(), item.getMsgId(), resendInfo, item.getModified());
    }

    private OnResponseListener resendListener = new OnResponseListener() {
        @Override
        public void onResponse(Object response, String taskId, boolean rawData) {
            if (response == null) {
                return;
            }

            if (taskId.equals(IMAPI.TASKID.RESEND_MESSAGE)) {
                String res = TTJSONUtil.convertObjToJson(response);
                DataPOJO pojo = JsonUtil.convertJsonToData(res);
                if (pojo == null) {
                    return;
                }
            }
        }

        @Override
        public boolean isDisable() {
            return false;
        }
    };

    Handler mHandler;

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    private void setReadView(MessageItem item, TextView readView, boolean isGroup) {
        if (item.getCmd() == MessageItem.TYPE_SEND && !item.getType().equals(Command.EVENT)) {
            if (isGroup) {
                readView.setVisibility(View.GONE);
            } else {
                if (item.getSent() != Command.SEND_FAILED && item.getIsRead() == 1) {
                    readView.setVisibility(View.VISIBLE);
                    readView.setText(mContext.getString(R.string.msg_have_read));
                } else if (item.getSent() != Command.SEND_FAILED && item.getIsRead() == 0) {
                    readView.setVisibility(View.VISIBLE);
                    readView.setText(mContext.getString(R.string.msg_have_accept));
                }
            }
        }
    }

    /**
     * 设置文本
     *
     * @param holder
     * @param item
     */
    private void setTextContent(final ChatViewHolder holder,
                                final MessageItem item, final int position) {

        // 表情转码
        String content = item.getContent();
        String unicode = EmojiParser.getInstance(mContext).parseEmoji(content);
        SpannableStringBuilder spannableString = new SpannableStringBuilder(ParseEmojiMsgUtil.getExpressionString(mContext, unicode));


        // 判断是否高亮显示
        String info = item.getInfo();
        int backColor;
        int textColor;
        // 区分发送和接收显示不同的高亮
        if (item.getCmd() == MessageItem.TYPE_RECV) {
            backColor = mContext.getResources().getColor(R.color.chat_below_text);
            textColor = Color.WHITE;
        } else {
            backColor = Color.WHITE;
            textColor = mContext.getResources().getColor(R.color.blue_launchr);
        }

        if (info != null) {
            MessageInfoEntity entity = TTJSONUtil.convertJsonToCommonObj(info, MessageInfoEntity.class);
            if (entity != null) {
                List<String> list = entity.getAtUsers();
                // 判断是否有@对象是否为空
                if (list != null && !list.isEmpty()) {
                    boolean isMark = false;
                    for (int i = 0; i < list.size(); i++) {
                        String str = list.get(i);
                        // 判断@字符串是否为空
                        if (!str.equals("")) {
                            // 找到@人员的名称
                            int x = str.indexOf("@");
                            String nameString = str.substring(x);
                            int index = content.indexOf(nameString);
                            if (isMark) {
                                isMark = false;
                                index = content.lastIndexOf(nameString);
                            }
                            if (index != -1) {
                                spannableString.replace(index, index + nameString.length(), " " + nameString);
                                content = spannableString.toString();

                                RoundBackgroundColorSpan span = new RoundBackgroundColorSpan(backColor, textColor);
                                spannableString.setSpan(span, index, index + nameString.length() + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            }
                        } else {
                            // 是否有复制的文字
                            isMark = true;
                        }
//

                    }

                }

            }
        }
        holder.textView.setText(spannableString);
        Linkify.addLinks(holder.textView, Linkify.WEB_URLS);
        RegularUtil.stripUnderlines(holder.textView);

        holder.textView.setOnTouchListener(new OnTouchListener() {
            int count = 0;
            long firClick, secClick;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    count++;
                    long touchTime = System.currentTimeMillis();
                    if (touchTime - firClick > 1000 || count == 1) {
                        firClick = touchTime;
                        count = 1;
                    } else if (count == 2) {
                        secClick = touchTime;
                        if (secClick - firClick < 1000) {
                            //双击事件
//							Intent intent = new Intent(mContext, TextActivtiy.class);
//							intent.putExtra(TextActivtiy.KEY, item.getContent());
//							mContext.startActivity(intent);
                        }
                        count = 0;
                        firClick = 0;
                        secClick = 0;

                    }
                }
                return false;
            }
        });
        holder.textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showMergeButton) {
                    addMsgToMergeList(item, position);
                }
            }
        });
        setReadView(item, holder.ivRead, isGroup);


//		Patterns.WEB_URL.matcher()

    }

    /**
     * 应用消息的头像、名字在content字段中，需要解析之后才能显示
     */
    private void setChatAvatarAndName(final ChatViewHolder holder, String nickName, final String userName, MessageItem item) {
        holder.tvName.setVisibility(View.VISIBLE);
        holder.ivAvatar.setVisibility(View.VISIBLE);
        if (nickName != null) {
            holder.tvName.setText(nickName);
        }

        if (nickName != null && nickName.equals("System")) {
            holder.ivAvatar.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_system));
            holder.ivAvatar.setVisibility(View.VISIBLE);
        } else if (userName != null) {
            HeadImageUtil.getInstance(mContext).setAvatarResourceWithUserId(holder.ivAvatar, userName, 0, 60, 60);
            holder.ivAvatar.setVisibility(View.VISIBLE);
        }

        holder.ivAvatar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showMergeButton) {
                    Intent intent = new Intent(mContext, PersonDetailActivity.class);
                    intent.putExtra(PersonDetailActivity.KEY_PERSONAL_ID, userName);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    /**
     * 设置文本
     *
     * @param holder
     * @param item
     */
    private void setEventTaskContent(final ChatViewHolder holder,
                                     final MessageItem item, final int position) {
        // 表情转码

        final MessageEventTaskEntity eventEntity = TTJSONUtil.convertJsonToObj(item.getContent(), new TypeReference<MessageEventTaskEntity>() {
        });
        final MessageEventTaskEntity.EventTaskCard eventCard;
        MessageEventTaskEntity.MessageEventTaskBackupEntity eventEntity2 = null;
        String title = null;
        if (eventEntity == null) {
            eventEntity2 = TTJSONUtil.convertJsonToObj(item.getContent(), new TypeReference<MessageEventTaskEntity.MessageEventTaskBackupEntity>() {
            });
            eventCard = eventEntity2.getMsgInfo();
            title = eventCard.getTitle();
            holder.event_progress.setText(eventCard.getFinishTask() + "/" + eventCard.getAllTask());
        } else {
            String jsonStr = eventEntity.getMsgInfo();
            eventCard = TTJSONUtil.convertJsonToCommonObj(jsonStr, MessageEventTaskEntity.EventTaskCard.class);
            title = eventCard.getTitle();
            holder.event_progress.setText(eventCard.getFinishTask() + "/" + eventCard.getAllTask());
        }
        // 如果解析不出来，则return
        if (eventCard == null) {
            return;
        }
        String unicode = EmojiParser.getInstance(mContext).parseEmoji(
                title);
        SpannableString spannableString = ParseEmojiMsgUtil
                .getExpressionString(mContext, unicode);
        holder.textView.setText(spannableString);
        if (!sessionName.contains("@APP")) {
            holder.ivRead.setVisibility(View.GONE);
        } else {
            if (eventEntity != null && eventEntity.getMsgReadStatus() == 0) {
                holder.ivRead.setVisibility(View.VISIBLE);
            } else if (eventEntity2 != null && eventEntity2.getMsgReadStatus() == 0) {
                holder.ivRead.setVisibility(View.VISIBLE);
            } else {
                holder.ivRead.setVisibility(View.GONE);
            }
        }

        // 时间
        if (this.sessionName.contains(com.mintcode.launchr.util.Const.SHOWID_TASK)) {
            holder.time.setText(TimeFormatUtil.formatNowTime(item.getCreateDate()));
        } else {
            holder.time.setVisibility(View.GONE);
        }

        // 设置头像、名字
        Info info = null;
        if (item.getCmd() == MessageItem.TYPE_SEND) {
            holder.tvName.setVisibility(View.INVISIBLE);
            holder.ivAvatar.setVisibility(View.INVISIBLE);
            holder.container.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.icon_chat_card_task_right));
        } else if (eventEntity != null) {
            setChatAvatarAndName(holder, eventEntity.getMsgFrom(), eventEntity.getMsgFromID(), item);
        } else if (eventEntity != null) {
            setChatAvatarAndName(holder, eventEntity2.getMsgFrom(), eventEntity2.getMsgFromID(), item);
        } else if (item.getInfo() != null) {
            info = TTJSONUtil.convertJsonToCommonObj(item.getInfo(), Info.class);
            setChatAvatarAndName(holder, info.getNickName(), info.getUserName(), item);
        }

        // 时间
        if (eventCard.getEnd() > 0) {
            holder.event_end.setVisibility(View.VISIBLE);
            holder.event_end.setText(TimeFormatUtil.formatEventTimeAllDay(eventCard.getEnd()));
        } else {
            holder.event_end.setVisibility(View.GONE);
        }
        // 项目
        if (eventCard.getProjectName() != null && !eventCard.getProjectName().equals("")) {
            holder.event_project_name.setVisibility(View.VISIBLE);
            holder.event_project_name.setText(eventCard.getProjectName());
        } else {
            holder.event_project_name.setVisibility(View.GONE);
        }

        holder.tvFileName.setVisibility(View.VISIBLE);
        holder.event_priority.setVisibility(View.VISIBLE);
        if (eventCard.getPriority() != null && eventCard.getPriority().equals("LOW")) {
            holder.tvFileName.setBackgroundColor(mContext.getResources().getColor(R.color.task_priority_grey));
            holder.event_priority.setText(mContext.getString(R.string.task_rank_low));
//			holder.event_priority.setTextColor(mContext.getResources().getColor(R.color.task_priority_grey));
        } else if (eventCard.getPriority() != null && eventCard.getPriority().equals("MEDIUM")) {
            holder.tvFileName.setBackgroundColor(mContext.getResources().getColor(R.color.task_priority_yellow));
            holder.event_priority.setText(mContext.getString(R.string.task_rank_medium));
//			holder.event_priority.setTextColor(mContext.getResources().getColor(R.color.task_priority_yellow));
        } else if (eventCard.getPriority() != null && eventCard.getPriority().equals("HIGH")) {
            holder.tvFileName.setBackgroundColor(mContext.getResources().getColor(R.color.task_priority_red));
            holder.event_priority.setText(mContext.getString(R.string.task_rank_high));
//			holder.event_priority.setTextColor(mContext.getResources().getColor(R.color.task_priority_red));
        } else {
            holder.tvFileName.setVisibility(View.GONE);
            holder.event_priority.setVisibility(View.GONE);
        }
        if ((eventCard.getStateType() != null && eventCard.getStateType().equals("FINISH")) || (eventCard.getStateName() != null && eventCard.getStateName().equals("FINISH"))) {
            holder.event_state_name.setCompoundDrawablesRelativeWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_check_finish), null, null, null);
            holder.event_state_name.setText(mContext.getString(R.string.task_finish));
        } else {
            holder.event_state_name.setCompoundDrawablesRelativeWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_check_wait), null, null, null);
            holder.event_state_name.setText(mContext.getString(R.string.no_finish));
        }

        String name = item.getFromLoginName();

        holder.event_app_name.setText(R.string.app_task);
        holder.container.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!showMergeButton) {
                    TaskApi.getInstance().getTask(listenr, eventCard.getId());
                } else {
                    addMsgToMergeList(item, position);
                }
            }
        });

        // 转发选择任务
        setShowMergeButton(holder, position);
    }

    /**
     * 设置文本
     *
     * @param holder
     * @param item
     */
    private void setEventScheduleContent(final ChatViewHolder holder,
                                         final MessageItem item) {
        // 表情转码
        final MessageEventScheduleEntity eventEntity = TTJSONUtil.convertJsonToObj(item.getContent(), new TypeReference<MessageEventScheduleEntity>() {
        });
        if (eventEntity == null || eventEntity.getMsgInfo() == null) {
            return;
        }
        final MessageEventScheduleEntity.ScheduleCard eventCard = TTJSONUtil.convertJsonToCommonObj(eventEntity.getMsgInfo(), MessageEventScheduleEntity.ScheduleCard.class);
        // 如果解析不出来，则return
        if (eventCard == null) {
            return;
        }

        String unicode = EmojiParser.getInstance(mContext).parseEmoji(
                eventEntity.getMsgTitle());
        SpannableString spannableString = ParseEmojiMsgUtil
                .getExpressionString(mContext, unicode);
        holder.textView.setText(spannableString);

        String startTime = TimeFormatUtil.formatEventTimeAllDay(eventCard.getStart());
        String endTime = TimeFormatUtil.formatEventTimeAllDay(eventCard.getEnd());
        holder.event_end.setText(startTime + " - " + endTime);
        holder.event_place.setText(eventCard.getRoomName());
        holder.event_state_name.setText(TimeFormatUtil.setTimeInterval(mContext, eventCard.getEnd(), eventCard.getStart()));
        if (eventEntity.getMsgHandleStatus() == 1) {
            //应用消息已处理，隐藏底部按钮
            holder.event_layout_bottom_buttons.setVisibility(View.GONE);
            holder.ivFileIcon.setVisibility(View.VISIBLE);
            if (eventCard.getIsAgree() == 1) {
                holder.ivFileIcon.setImageDrawable(BitmapUtil.writeCharBitmap(mContext, mContext.getString(R.string.approval_enter),
                        R.drawable.icon_chat_card_accept, R.color.card_accept));
            } else {
                holder.ivFileIcon.setImageDrawable(BitmapUtil.writeCharBitmap(mContext, mContext.getString(R.string.approval_refuse),
                        R.drawable.icon_chat_card_refuse, R.color.card_refuse));
            }
        } else {
            holder.ivFileIcon.setVisibility(View.GONE);
            holder.event_layout_bottom_buttons.setVisibility(View.VISIBLE);
            holder.event_join.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    MeetingApi.getInstance().confirmMeeting(listenr, eventCard.getId(), 1, "");
                }
            });
            holder.event_refuse.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    refuseMeeting(eventCard.getId(), number);
                }
            });
        }
        if (eventEntity != null && eventEntity.getMsgReadStatus() == 0) {
            holder.ivRead.setVisibility(View.VISIBLE);
        } else {
            holder.ivRead.setVisibility(View.GONE);
        }

        // 设置头像、名字
        setChatAvatarAndName(holder, eventEntity.getMsgFrom(), eventEntity.getMsgFromID(), item);

        // 时间
        holder.time.setText(TimeFormatUtil.formatNowTime(item.getCreateDate()));

        String name = item.getFromLoginName();

        if (name.contains(com.mintcode.launchr.util.Const.SHOWID_TASK)) {
            holder.event_app_name.setText(R.string.app_task);
        } else if (name.contains(com.mintcode.launchr.util.Const.SHOW_SCHEDULE)) {
            holder.event_app_name.setText(R.string.app_schedule);
            holder.container.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent ToEventDetail = new Intent(mContext, MeetingDetailActivity.class);
                    ToEventDetail.putExtra(MeetingDetailActivity.KEY_RELATIVE_ID, eventCard.getId());
                    mContext.startActivity(ToEventDetail);
//					MeetingApi.getInstance().getMeeting(listenr, eventCard.getId(), 0L);
                }
            });
        } else if (name.contains(com.mintcode.launchr.util.Const.SHOWID_APPROVE)) {
            holder.event_app_name.setText(R.string.app_apply);
        }


    }

    /**
     * 设置文本
     *
     * @param holder
     * @param item
     */
    private void setEventApproveContent(final ChatViewHolder holder,
                                        final MessageItem item) {
        // 表情转码
        final MessageEventApproveEntity eventEntity = TTJSONUtil.convertJsonToObj(item.getContent(), new TypeReference<MessageEventApproveEntity>() {
        });
        final MessageEventApproveEntity.ApproveCard eventCard = TTJSONUtil.convertJsonToCommonObj(eventEntity.getMsgInfo(), MessageEventApproveEntity.ApproveCard.class);
        // 如果解析不出来，则return
        if (eventCard == null) {
            return;
        }
        // 标题
        String unicode = EmojiParser.getInstance(mContext).parseEmoji(
                eventEntity.getMsgTitle());
        SpannableString spannableString = ParseEmojiMsgUtil
                .getExpressionString(mContext, unicode);
        holder.textView.setText(spannableString);
        //审批类型
        String type = eventCard.getApprovalType();
        if (type != null) {
            holder.tvFileName.setText(type);
        }
        holder.form_layout.removeAllViews();
        List<MessageFormData> StrJsonForm = eventCard.getApprovalForm();
        Object StrJosnFormData = eventCard.getApprovalFormData();
        // 兼容旧数据，不显示旧数据内容
        if (StrJosnFormData != null || StrJsonForm != null) {
            String josnFormData = TTJSONUtil.convertObjToJson(StrJosnFormData);
            HashMap<String, String> jsonHashMap = FormViewUtil.getFormDataJson(josnFormData);
            Log.i("----JosnFormData---", josnFormData);
            //显示表单数据
            if (jsonHashMap != null && josnFormData != null) {
                FormViewUtil.createFormContent(mContext, holder.form_layout, jsonHashMap, StrJsonForm);
            }
        }
        if (eventEntity != null && eventEntity.getMsgReadStatus() == 0) {
            holder.ivRead.setVisibility(View.VISIBLE);
        } else {
            holder.ivRead.setVisibility(View.GONE);
        }

        // 设置头像、名字
        setChatAvatarAndName(holder, eventEntity.getMsgFrom(), eventEntity.getMsgFromID(), item);

        // 时间
        holder.time.setText(TimeFormatUtil.formatNowTime(item.getCreateDate()));

//		String remark = null;
//		if(eventCard.getApprovalShowID()!=null && eventCard.getApprovalShowID().contains("vEyVJ7K29qcovp3p")){
//			//请假
////			holder.event_layout_end.setVisibility(View.GONE);
//			remark = eventCard.getApprovalType();
//			holder.relFileLayout.setVisibility(View.GONE);
//		}else if(eventCard.getApprovalShowID()!=null && eventCard.getApprovalShowID().contains("BB1xoKW53kCPW7OP")){
//			//报销
//			holder.relEndTime.setVisibility(View.GONE);
//			holder.relFileLayout.setVisibility(View.VISIBLE);
////			holder.event_layout_end.setVisibility(View.VISIBLE);
//			holder.event_remark.setText(eventCard.getFee() + "");
//			remark = eventCard.getApprovalType();
//		}


        if (eventEntity.getMsgHandleStatus() == 1) {
            //应用消息已处理，隐藏底部按钮
            holder.event_layout_bottom_buttons.setVisibility(View.GONE);
            holder.ivFileIcon.setVisibility(View.VISIBLE);
            if (eventCard.getApprovalStatus() != null) {
                if (eventCard.getApprovalStatus().equals("WAITING")) {
                    holder.ivFileIcon.setImageDrawable(BitmapUtil.writeCharBitmap(mContext, mContext.getString(R.string.send_other_people),
                            R.drawable.icon_chat_card_accept, R.color.card_accept));
                } else if (eventCard.getApprovalStatus().equals("DENY")) {
                    holder.ivFileIcon.setImageDrawable(BitmapUtil.writeCharBitmap(mContext, mContext.getString(R.string.approval_not_agree),
                            R.drawable.icon_chat_card_refuse, R.color.card_refuse));
                } else if (eventCard.getApprovalStatus().equals("CALL_BACK")) {
                    holder.ivFileIcon.setImageDrawable(BitmapUtil.writeCharBitmap(mContext, mContext.getString(R.string.accpect_recall),
                            R.drawable.icon_chat_card_back, R.color.card_back));
                } else {
                    holder.ivFileIcon.setImageDrawable(BitmapUtil.writeCharBitmap(mContext, mContext.getString(R.string.approval_agree),
                            R.drawable.icon_chat_card_accept, R.color.card_accept));
                }
            }
            if (eventEntity.getMsgAppType() != null && eventEntity.getMsgAppType().equals("CC")) {
                holder.ivFileIcon.setVisibility(View.GONE);
            }
        } else {
            holder.ivFileIcon.setVisibility(View.GONE);
            holder.event_layout_bottom_buttons.setVisibility(View.VISIBLE);
            holder.event_give_back.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    reCallApply(eventCard.getId(), number);
                }
            });
            holder.event_reject.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    refuseApply(eventCard.getId(), number);
                }
            });
            holder.event_ok.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    passApply(eventCard.getId(), number);
                }
            });
        }

        if (eventEntity.getMsgReadStatus() == 0) {
            holder.ivRead.setVisibility(View.VISIBLE);
        } else if (eventEntity.getMsgReadStatus() == 1) {
            holder.ivRead.setVisibility(View.GONE);
        }

        String name = item.getFromLoginName();

        if (name.contains(com.mintcode.launchr.util.Const.SHOWID_TASK)) {
            holder.event_app_name.setText(R.string.app_task);
        } else if (name.contains(com.mintcode.launchr.util.Const.SHOW_SCHEDULE)) {
            holder.event_app_name.setText(R.string.app_schedule);
        } else if (name.contains(com.mintcode.launchr.util.Const.SHOWID_APPROVE)) {
            holder.event_app_name.setText(R.string.app_apply);
            holder.container.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ApproveAPI.getInstance().getApproveDetailV2(listenr, eventCard.getId());
                }
            });
        }
    }

    /**
     * 设置文件
     */
    private void setFileContent(final ChatViewHolder holder,
                                final MessageItem item, final int position) {
        AttachItem attach = JsonUtil.convertJsonToCommonObj(item.getContent(), AttachItem.class);
        if (attach != null) {
            holder.tvFileName.setText(attach.getFileName());
            holder.ivFileIcon.setImageResource(IMConst.getFileIcon(attach.getFileName()));
            holder.tvFileSize.setText(IMConst.FormetFileSize(attach.getFileSize()));

            holder.relFileLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!showMergeButton) {
                        Intent intent = new Intent(mContext, FileDetailActivity.class);
                        intent.putExtra("fileMessage", item);
                        mContext.startActivity(intent);
                    } else {
                        addMsgToMergeList(item, position);
                    }
                }
            });
        }

        setReadView(item, holder.ivRead, isGroup);
    }

    /**
     * 设置文本
     *
     * @param holder
     * @param item
     */
    private void setEventAlertContent(final ChatViewHolder holder,
                                      final MessageItem item) {
        // 表情转码
        final MessageEventAlertEntity eventEntity = TTJSONUtil.convertJsonToObj(item.getContent(), new TypeReference<MessageEventAlertEntity>() {
        });
        if (eventEntity == null) {
            return;
        }

        // 标题
        if (eventEntity.getMsgFrom() != null && eventEntity.getMsgFrom().equals("System")) {
            holder.textView.setText(eventEntity.getMsgTitle());
        } else {
            holder.textView.setText(eventEntity.getMsgContent());
        }

        // 内容
        holder.tvFileName.setVisibility(View.GONE);
        if (eventEntity.getMsgTransType() != null && eventEntity.getMsgTransType().equals("comment")) {
            holder.tvFileName.setVisibility(View.VISIBLE);
            MessageEventAlertEntity.Info comment = TTJSONUtil.convertJsonToCommonObj(eventEntity.getMsgInfo(), MessageEventAlertEntity.Info.class);
            if (comment != null) {
                holder.tvFileSize.setText(comment.getComment() + "");
            }
        } else if (eventEntity.getMsgInfo() != null) {
            MessageEventAlertEntity.Info info = TTJSONUtil.convertJsonToCommonObj(eventEntity.getMsgInfo(), MessageEventAlertEntity.Info.class);
            holder.tvFileSize.setText(setAlertEventMsg(eventEntity.getMsgTransType(), eventEntity.getMsgFrom(), eventEntity.getMsgContent(), info));
        }

        holder.relFileLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getFromLoginName().contains(com.mintcode.launchr.util.Const.SHOW_SCHEDULE) && eventEntity.getMsgRMShowID() != null) {
                    Intent ToEventDetail = new Intent(mContext, MeetingDetailActivity.class);
                    ToEventDetail.putExtra(MeetingDetailActivity.KEY_RELATIVE_ID, eventEntity.getMsgRMShowID());
                    mContext.startActivity(ToEventDetail);
                } else if (item.getFromLoginName().contains(com.mintcode.launchr.util.Const.SHOWID_APPROVE) && eventEntity.getMsgRMShowID() != null) {
                    Intent i = new Intent(mContext, ApproveDetailActivity.class);
                    i.putExtra(ApproveDetailActivity.APPLY_DETAIL, eventEntity.getMsgRMShowID());
                    mContext.startActivity(i);
//					ApproveAPI.getInstance().getApproveDetailV2(listenr, eventEntity.getMsgRMShowID());
                } else if (item.getFromLoginName().contains(com.mintcode.launchr.util.Const.SHOWID_TASK) && eventEntity.getMsgRMShowID() != null) {
                    TaskApi.getInstance().getTask(listenr, eventEntity.getMsgRMShowID());
                }
            }
        });

        // 设置头像、名字
        setChatAvatarAndName(holder, eventEntity.getMsgFrom(), eventEntity.getMsgFromID(), item);

        // 时间
        holder.time.setText(TimeFormatUtil.formatNowTime(item.getCreateDate()));


        // 判断是否是提醒
        if (eventEntity.getMsgTransType().equals("remindTask") || eventEntity.getMsgTransType().equals("remindSchedule")) {
            String msgContent = "";
            String title = eventEntity.getMsgTitle();
            String time = eventEntity.getMsgRemark();
            if (time == null) {
                time = "";
            } else {
                time = TimeFormatUtil.formatNowTime(Long.parseLong(time));
            }
//			DateUtils.get
            if (eventEntity.getMsgTransType().equals("remindTask")) {
                msgContent = mContext.getString(R.string.im_task_remind);
            } else if (eventEntity.getMsgTransType().equals("remindTaskV2") || eventEntity.getMsgTransType().equals("remindSchedule")) {
                msgContent = mContext.getString(R.string.im_task_schedule);
            }
            msgContent = msgContent.replace("^@", title);
            msgContent = msgContent.replace("^#", time);
//			String reminContent = eventEntity.get
            holder.tvFileSize.setText(msgContent);
        }
    }

    private String setAlertEventMsg(String type, String userName, String content, MessageEventAlertEntity.Info info) {
        String msgContent = "";
        if (type.equals("comment")) {
            msgContent = mContext.getString(R.string.im_comment);
            msgContent = msgContent.replace("^@", userName);
            msgContent = msgContent.replace("^#", content);
        } else if (type.equals("meetingAttend")) {
            msgContent = mContext.getString(R.string.im_meetingAttend);
            msgContent = msgContent.replace("^@", userName);
            msgContent = msgContent.replace("^#", content);
        } else if (type.equals("meetingAttendDefinite")) {
            msgContent = mContext.getString(R.string.meeting_enter);
            msgContent = msgContent.replace("^@", userName);
            msgContent = msgContent.replace("^#", content);
        } else if (type.equals("meetingRefuseAttend")) {
            msgContent = mContext.getString(R.string.im_meetingRefuseAttend);
            msgContent = msgContent.replace("^@", userName);
            msgContent = msgContent.replace("^#", info.getReason());
        } else if (type.equals("delete")) {
            msgContent = mContext.getString(R.string.im_delete);
            msgContent = msgContent.replace("^@", userName);
            msgContent = msgContent.replace("^#", content);
        } else if (type.equals("taskV2ChangeStatus") || type.equals("taskEditStatus")) {
            msgContent = mContext.getString(R.string.im_taskEditStatus);
            msgContent = msgContent.replace("^@", userName);
            msgContent = msgContent.replace("^#", content);
        } else if (type.equals("approvePass")) {
            msgContent = mContext.getString(R.string.approve_passit);
        } else if (type.equals("approveBack")) {
            msgContent = mContext.getString(R.string.im_approveBack);
            msgContent = msgContent.replace("^@", userName);
            msgContent = msgContent.replace("^#", content);
        } else if (type.equals("approveRefuse")) {
            msgContent = mContext.getString(R.string.approval_not_agree);
        } else if (type.equals("approveTranspond")) {
            msgContent = mContext.getString(R.string.im_approve_transpond_reason);
            String username = "";
            if (!TextUtils.isEmpty(info.getTransUserName())) {
                username = info.getTransUserName();
            }
            String reason = "";
            if (!TextUtils.isEmpty(info.getReason())) {
                reason = info.getReason();
            }
            msgContent = msgContent.replace("^#", username);
            msgContent = msgContent.replace("^%", reason);
        } else if (type.equals("approveTranspondDefinite")) {
            msgContent = mContext.getString(R.string.im_approve_transpond_reason);
            String username = "";
            if (!TextUtils.isEmpty(info.getTransUserName())) {
                username = info.getTransUserName();
            }
            String reason = "";
            if (!TextUtils.isEmpty(info.getReason())) {
                reason = info.getReason();
            }
            msgContent = msgContent.replace("^#", username);
            msgContent = msgContent.replace("^%", reason);
        } else if (type.equals("taskEditStatusDefinite")) {
            msgContent = mContext.getString(R.string.im_task_edit_status);
            msgContent = msgContent.replace("^@", userName);
            msgContent = msgContent.replace("^#", content);
        } else if (type.equals("ApprovePut")) {
            msgContent = mContext.getString(R.string.im_approve_put);
        } else if (type.equals("approvePost")) {
            msgContent = mContext.getString(R.string.im_approve_post);
        } else if (type.equals("taskDelete")) {
            msgContent = mContext.getString(R.string.im_delete);
            msgContent = msgContent.replace("^@", userName);
            msgContent = msgContent.replace("^#", content);
        } else if (type.equals("meetingRefuseAttendDefinite")) {
            msgContent = mContext.getString(R.string.refuse_attend);
            msgContent = msgContent.replace("^@", userName);
            msgContent = msgContent.replace("^#", content);
        } else if (type.equals("approveBackDefinite")) {
            msgContent = mContext.getString(R.string.approve_back_define_reason);
            msgContent = msgContent.replace("^%", info.getReason());
        } else if (type.equals("approveRefuseDefinite")) {
            msgContent = mContext.getString(R.string.approve_refuse_define_reason);
            msgContent = msgContent.replace("^%", info.getReason());
        } else if (type.equals("meetingEdit")) {
            msgContent = mContext.getString(R.string.meeting_edit_content);
        } else if (type.equals("meetingCancelOne")) {
            msgContent = mContext.getString(R.string.meeting_cancel_one);
        } else if (type.equals("meetingCancel")) {
            msgContent = mContext.getString(R.string.meeting_cancel);
            msgContent = msgContent.replace("^@", content);
        } else if (type.equals("taskV2ChangeDoneStatus")) {
            msgContent = mContext.getString(R.string.task_done_status);
        } else if (type.equals("taskV2ChangeDoingStatus")) {
            msgContent = mContext.getString(R.string.task_doing_status);
        } else if (type.equals("taskV2Update")) {
            msgContent = mContext.getString(R.string.task_update_content);
        } else if (type.equals("createTaskV2")) {
            msgContent = mContext.getString(R.string.task_create_v2);
        } else if (type.equals("approveBackV2")) {
            msgContent = mContext.getString(R.string.approvel_back);
        } else if (type.equals("approve")) {
            msgContent = mContext.getString(R.string.approvel_comment);
        } else if (type.equals("meeting")) {
            msgContent = mContext.getString(R.string.app_meetting);
        } else if (type.equals("adressListSelectedPerson")) {
            msgContent = mContext.getString(R.string.address_list_select_person);
            msgContent = msgContent.replace("^@", content);
        }
        return msgContent;
    }

    /**
     * 合并消息转发
     */
    private void setMergeMsgContent(final ChatViewHolder holder, final MessageItem item, final int position) {
        final MergeEntity.MergeCard mergeCard = TTJSONUtil.convertJsonToCommonObj(item.getContent(), MergeEntity.MergeCard.class);
        if (mergeCard == null) {
            return;
        }

        holder.textView.setText(mergeCard.getTitle());
        holder.event_project_name.setVisibility(View.GONE);
        holder.event_state_name.setVisibility(View.GONE);
        holder.event_app_name.setVisibility(View.GONE);
        int num = 0;
        String content = "";
        for (int i = 0; i < mergeCard.getMsg().size(); i++) {
            MergeEntity entity = mergeCard.getMsg().get(i);
            Info info = TTJSONUtil.convertJsonToCommonObj(entity.getInfo(), Info.class);
            // 由于宽度获取有时候会是零，无法做到字符串长度对比截取
            // 最后使用三个text内容显示
            if (entity.getType().equals(Command.IMAGE)) {
                content = info.getNickName() + ":" + LauchrConst.IM_SESSION_IMAGE;
                num++;
            } else if (entity.getType().equals(Command.AUDIO)) {
                content = info.getNickName() + ":" + LauchrConst.IM_SESSION_AUDIO;
                num++;
            } else if (entity.getType().equals(Command.FILE)) {
                content = info.getNickName() + ":" + LauchrConst.IM_SESSION_FILE;
                num++;
            } else {
                content = info.getNickName() + ":" + entity.getContent();
                num++;
            }
            if (content != null) {
                switch (num) {
                    case 1:
                        holder.event_project_name.setText(content);
                        holder.event_project_name.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        holder.event_state_name.setText(content);
                        holder.event_state_name.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        holder.event_app_name.setText(content);
                        holder.event_app_name.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        }

        holder.container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showMergeButton) {
                    Intent intent = new Intent(mContext, MergeMsgActivity.class);
                    intent.putExtra("merge_content", item.getContent());
                    intent.putExtra("merge_title", mergeCard.getTitle());
                    mContext.startActivity(intent);
                } else {
                    addMsgToMergeList(item, position);
                }
            }
        });
    }

    private View setupAlertViews(ChatViewHolder holder) {
        View convertView = inflater.inflate(R.layout.item_alart, null);
        holder.time = (TextView) convertView.findViewById(R.id.msg);

        convertView.setTag(holder);

        return convertView;

    }

    /**
     * 设置聊天 语音
     *
     * @param holder
     * @param item
     */
    private void setChatAudio(final ChatViewHolder holder,
                              final MessageItem item, final int position) {
        if (item.getCmd() == MessageItem.TYPE_RECV) { // left
            holder.textSoundView.setCompoundDrawables(mDrawableLeft, null,
                    null, null);
            if (item.getIsRead() == 0) {
                holder.ivReadMark.setVisibility(View.VISIBLE);
            }
        } else if (item.getCmd() == MessageItem.TYPE_SEND) { // right
            holder.textSoundView.setCompoundDrawables(null, null,
                    mDrawableRight, null);
        }

        String content = item.getContent();
        final AudioItem audio = JsonUtil.convertJsonToCommonObj(content,
                AudioItem.class);
        Log.d("audio", "content=" + content);
        Log.d("audio", "audio=" + audio);
        holder.textView.setText(formatSoundText(audio.getAudioLength() + ""));
        holder.tvSoundTime.setText(audio.getAudioLength() + "″");
        holder.textView.post(new Runnable() {
            @Override
            public void run() {
                float density = mContext.getResources().getDisplayMetrics().density;
                int width = (int) (70 * density + 0.5f);
                width = (int) (((audio.getAudioLength() - 1) / 10f + 1) * width);
                holder.textView.setWidth(width);
            }
        });
        setReadView(item, holder.ivRead, isGroup);

        /** 语音点击播放 */
        holder.textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showMergeButton) {
                    addMsgToMergeList(item, position);
                } else {
                    String filePath = item.getFileName();
                    holder.ivReadMark.setVisibility(View.GONE);
                    MessageDBService.getInstance().updateAudioReadSession(item.getFromLoginName(), item.getToLoginName(), item.getMsgId());
                    item.setIsRead(1);
                    if (filePath != null) {
                        if (!mAudioRecordPlayUtil.isPlaying()) {
                            mAudioRecordPlayUtil.setFileName(filePath);
                            mAudioRecordPlayUtil.startPlaying();
                            AudioPlayingAnimation.play(holder, item);
                        } else {
                            mAudioRecordPlayUtil.stopPlaying();
                            AudioPlayingAnimation.stop();
                        }
                    } else {
                        MutiSoundUpload.getInstance().sendSoundToDownload(item, mContext, mUIHandler);
                    }
                    if (!isGroup && mContext != null && mContext instanceof ChatActivity) {
                        ((ChatActivity) mContext).readSession(item);
                    }
                }
            }
        });

    }

    // 根据语音时长设置语音气泡长短
    private String formatSoundText(String timeText) {
        if ((timeText != null) && !timeText.equals("")) {
            int time = Integer.parseInt(timeText);
            if (time > 30) {
                time = 30;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < time; i++) {
                sb.append(" ");
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 设置聊天视频
     *
     * @param holder
     * @param item
     */
    private void setChatVidio(ChatViewHolder holder, MessageItem item, final int position) {
        if (MessageItem.TYPE_RECV == item.getCmd()) {
            // 接收
            String content = item.getContent();
            AttachItem attach = JsonUtil.convertJsonToCommonObj(content,
                    AttachItem.class);
            String str;
            str = attach.getThumbnail();
            String url2 = url + str;
            MImageCache cache = MImageCache.getInstance(mContext);
            Bitmap dst = cache.getBitmapAlbum(url2);
            if (dst != null) {
//				holder.imageView.setImage(dst);
            } else {
                holder.imageView.setImageResource(R.drawable.im_default_image);
            }
        } else {
            Bitmap bitmap = FileShow.getBitmap(item.getFileName());
            if (bitmap != null) {
//				holder.imageView.setImage(bitmap);
            } else {
                holder.imageView.setImageResource(R.drawable.im_default_image);
            }
        }

        setOnVidioClickListener(holder.imageView, item);
    }

    private void setOnVidioClickListener(DirectImageView imageView,
                                         final MessageItem item) {
        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int cmd = item.getCmd();
                String path = null;
                if (cmd == MessageItem.TYPE_RECV) {
                    AttachItem attach = JsonUtil.convertJsonToCommonObj(
                            item.getContent(), AttachItem.class);
                    path = url + attach.getFileUrl();
                } else {
                    path = item.getFileName();
                }
                Intent intent = new Intent();
                intent.putExtra("camera_path", path);
                intent.setClass(mContext, FileShow.class);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 设置聊天图片
     *
     * @param holder
     * @param item
     */
    private void setChatImage(ChatViewHolder holder, MessageItem item, int position) {
        String key = "";
        if (MessageItem.TYPE_RECV == item.getCmd()) {
            // 接收
            String content = item.getContent();
            AttachItem attach = JsonUtil.convertJsonToCommonObj(content,
                    AttachItem.class);

//			String url = LauchrConst.ATTACH_PATH + attach.getFileUrl();
            String url = LauchrConst.httpIp + "/launchr" + attach.getThumbnail();
            int placehodler = R.drawable.im_default_image;

            HeadImageUtil.getInstance(mContext).setChatImage(holder.imageView, url, placehodler, attach.getThumbnailWidth(), attach.getThumbnailHeight());

        } else {

            int placehodler = R.drawable.im_default_image;
            String filename = item.getFileName();
            AttachItem attach = JsonUtil.convertJsonToCommonObj(item.getContent(), AttachItem.class);
            if (filename != null && !filename.equals("")) {
                if (attach != null) {
                    HeadImageUtil.getInstance(mContext).setChatImageFile(holder.imageView, filename, placehodler, attach.getThumbnailWidth(), attach.getThumbnailHeight());
                } else {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    Bitmap bitmap = BitmapFactory.decodeFile(filename, options);// 此时返回的bitmap为null
                    HeadImageUtil.getInstance(mContext).setChatImageFile(holder.imageView, filename, placehodler, options.outWidth, options.outHeight);
                }
            } else {
                String url = LauchrConst.httpIp + "/launchr" + attach.getThumbnail();

                HeadImageUtil.getInstance(mContext).setChatImage(holder.imageView, url, placehodler, attach.getThumbnailWidth(), attach.getThumbnailHeight());
////
            }

//			String content = item.getContent();
//			AttachItem attach = JsonUtil.convertJsonToCommonObj(content, AttachItem.class);
//			String url = LauchrConst.httpIp + "/launchr" + attach.getThumbnail();
//			int placehodler = R.drawable.im_default_image;
//			HeadImageUtil.getInstance(mContext).setChatImage(holder.imageView,url,placehodler,attach.getThumbnailWidth(),attach.getThumbnailHeight());
//

            // 更新进度提示
            String percent = item.getPercent();
            if (percent == null || percent.equals("100")) {
                holder.tvPercent.setText("");
            } else {
                holder.tvPercent.setText(percent + "%");
            }

            if (isGroup) {
                holder.ivRead.setVisibility(View.GONE);
            } else {
                holder.ivRead.setSelected(item.getIsRead() == 0);
            }
        }

        Log.i("msg", "onMessage key:" + key);
        setOnImageClickListener(holder.imageView, item, position);
        setReadView(item, holder.ivRead, isGroup);
    }


    private void setOnImageClickListener(final DirectImageView imageView,
                                         final MessageItem item, final int position) {
        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!showMergeButton) {
                    Intent intent = new Intent(mContext, PreviewLargeImageActivity.class);//ImgPreviewActivity

                    if (photoList != null) {
                        int position = photoList.indexOf(item);
                        intent.putExtra("position", position);
                        intent.putExtra("item", item);
                        intent.putExtra("list", (Serializable) photoList);
                        mContext.startActivity(intent);
                        Activity a = (Activity) mContext;
                        a.overridePendingTransition(R.anim.scale_in, R.anim.scale_retain);
                    }

                } else {
                    addMsgToMergeList(item, position);
                }
            }
        });
    }

    /**
     * 设置显示发送进度
     *
     * @param holder
     * @param item
     */
    private void setSendingProcess(ChatViewHolder holder, MessageItem item) {
        if (holder.progressBarSending != null) { // visibility of the sendingBar
            int visibile = View.GONE;
            if (Command.SEND_SUCCESS == item.getSent()) {
                visibile = View.GONE;
            } else {
                visibile = View.VISIBLE;
            }
            if (holder.ivFailed != null) {

                if (Command.SEND_FAILED == item.getSent()) {
                    holder.ivFailed.setVisibility(View.VISIBLE);
                    visibile = View.GONE;
                } else {
                    holder.ivFailed.setVisibility(View.GONE);
                }
            }
            Log.i("msg", "ChatActivity  visibile:" + visibile);

            holder.progressBarSending.setVisibility(visibile);

        }
    }

    /**
     * 设置聊天头像名称
     *
     * @param holder
     * @param item
     */
    private void setAvatarAndName(ChatViewHolder holder, final MessageItem item) {
        if (isGroup) {
            holder.tvName.setVisibility(View.VISIBLE);
        }
        Info userInfo = null;
        final String info = item.getInfo();
        if (MessageItem.TYPE_RECV == item.getCmd()) {
            if (info != null) {
                userInfo = JsonUtil.convertJsonToCommonObj(info,
                        Info.class);
                holder.tvName.setText(userInfo.getNickName());
            }
            if (holder.ivAvatar != null && info != null) {
                HeadImageUtil.getInstance(mContext).setAvatarResourceWithUserId(holder.ivAvatar, userInfo.getUserName(), 2, 60, 60);
                holder.ivAvatar.setVisibility(View.VISIBLE);
            }
            holder.tvName.setVisibility(View.VISIBLE);
        } else {
        }

        if (holder.ivAvatar != null) {
            holder.ivAvatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info != null && !showMergeButton) {
                        UserApi.getInstance().getComanyUserInfo(listenr, JsonUtil.convertJsonToCommonObj(info,
                                Info.class).getUserName());
                    }
                }
            });
        }

    }

    private OnResponseListener listenr = new OnResponseListener() {

        @Override
        public void onResponse(Object response, String taskId, boolean rawData) {
            if (response == null) {
                return;
            }
            if (taskId.equals(TaskId.TASK_URL_GET_COMPANY_USER_INFO)) {
                ChatUserDetailPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), ChatUserDetailPOJO.class);
                if (pojo == null) {
                    return;
                } else if (pojo.getBody() == null) {
                    return;
                } else if (pojo.getBody().getResponse().getData() == null) {
                    return;
                } else {
                    ChatUserDetailEntity entity = pojo.getBody().getResponse().getData();
                    if (entity != null) {
                        Intent intent = new Intent(mContext, PersonDetailActivity.class);
                        intent.putExtra(PersonDetailActivity.KEY_PERSONAL_ID, entity.getSHOW_ID());
                        mContext.startActivity(intent);
                    }
                }
            } else if (taskId.equals(ApproveAPI.TaskId.TASK_URL_DEAL_APPROVE)) {
                NormalPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), NormalPOJO.class);
                handleApproveDeal(pojo);
            } else if (taskId.equals(MeetingApi.TaskId.TASK_URL_SRUE_MEETING)) {
                MeetingPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), MeetingPOJO.class);
                if (pojo == null) {
                    return;
                } else if (pojo.getBody().getResponse().isIsSuccess() == false) {
                    ((BaseActivity) mContext).toast(pojo.getBody().getResponse().getReason());
                }
            } else if (taskId.equals(IMNewApi.TaskId.ADD_BOOK_MARK)) {
                handleAddBookMark(response);
            } else if (taskId.equals(IMNewApi.TaskId.DELETE_BOOK_MARK)) {
                handlerDeleteBookMark(response);
            } else if (taskId.equals(TaskApi.TaskId.TASK_URL_GET_TASK)) {
                handleTaskDetail(response);
            } else if (taskId.equals(ApproveAPI.TaskId.TASK_URL_GET_DETAIL_V2)) {
                handleApprovelDetail(response);
            } else if (taskId.equals(MeetingApi.TaskId.TASK_URL_GET_MEETING)) {
                handleMeetingDetail(response);
            }
        }

        @Override
        public boolean isDisable() {
            return false;
        }
    };

    /**
     * 点击任务
     */
    private void handleTaskDetail(Object response) {
        TaskDetailPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), TaskDetailPOJO.class);
        if (pojo == null || pojo.getBody().getResponse().getData() == null) {
            Toast.makeText(mContext, mContext.getString(R.string.task_no_live), Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(mContext, TaskDetailActivity.class);
            intent.putExtra(TaskDetailActivity.KEY_TASK_ID, pojo.getBody().getResponse().getData().getShowId());
            mContext.startActivity(intent);
        }
    }

    /**
     * 点击审批
     */
    private void handleApprovelDetail(Object response) {
        ApprovalDetailPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), ApprovalDetailPOJO.class);
        if (pojo == null || pojo.getBody().getResponse().getData() == null) {
            Toast.makeText(mContext, mContext.getString(R.string.approvel_no_live), Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(mContext, ApproveDetailActivity.class);
            i.putExtra(ApproveDetailActivity.APPLY_DETAIL, pojo.getBody().getResponse().getData().getSHOW_ID());
            mContext.startActivity(i);
        }
    }

    /**
     * 点击会议
     */
    private void handleMeetingDetail(Object response) {
        MeetingDetailPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), MeetingDetailPOJO.class);
        if (pojo == null || pojo.getBody().getResponse().getData() == null) {
            Toast.makeText(mContext, mContext.getString(R.string.meeting_no_live), Toast.LENGTH_SHORT).show();
        } else {
            Intent ToEventDetail = new Intent(mContext, MeetingDetailActivity.class);
            ToEventDetail.putExtra(MeetingDetailActivity.KEY_RELATIVE_ID, pojo.getBody().getResponse().getData().getSHOW_ID());
            mContext.startActivity(ToEventDetail);
        }
    }

    /**
     * 标记重点，会通过Socket推送过来CMD
     */
    private void handleAddBookMark(Object response) {
        DataPOJO pojo = JsonUtil.convertJsonToData(response.toString());
        if (pojo == null) {
            return;
        } else if (pojo.isResultSuccess() == false) {
            CustomToast.showToast(mContext, mContext.getString(R.string.network_exception), Toast.LENGTH_SHORT);
        }
    }

    /**
     * 取消标记重点,会通过Socket推送过来CMD
     */
    private void handlerDeleteBookMark(Object response) {
        DataPOJO pojo = JsonUtil.convertJsonToData(response.toString());
        if (pojo == null) {
            return;
        } else if (pojo.isResultSuccess() == false) {
            CustomToast.showToast(mContext, mContext.getString(R.string.network_exception), Toast.LENGTH_SHORT);
        }
    }

    /**
     * 处理审批，通过
     */
    private void handleApproveDeal(NormalPOJO pojo) {
        if (pojo == null) {
            return;
        } else if (pojo.getBody() == null) {
            return;
        } else if (pojo.getBody().getResponse().isIsSuccess() == false) {
            CustomToast.showToast(mContext, pojo.getBody().getResponse().getReason(), 1000);
            return;
        } else {
        }
    }

    /**
     * 设置聊天时间 2分钟显示一次
     *
     * @param holder
     * @param item
     * @param position
     */
    private void setChatTime(ChatViewHolder holder, MessageItem item,
                             int position) {
        if (holder.time != null) {
            int visibility = View.VISIBLE;
            // 注释掉这些的原因是因为每条信息都要显示时间
            // long timeSeconds = 0;
            // MessageItem preItem = getPreItemWithDate(position);
            // if (preItem != null) {
            // long preDate = preItem.getCreateDate();
            // long dateCur = item.getCreateDate();
            // timeSeconds = (dateCur - preDate) / 1000;
            // }
            // if (timeSeconds < 120) {
            // visibility = View.GONE;
            // }
            holder.time.setVisibility(View.GONE);
        }
    }

    /**
     * 加载聊天 -语音控件
     *
     * @param holder
     * @param cmd
     * @return
     */
    private View setupAudioViews(ChatViewHolder holder, int cmd) {

        View convertView = inflater.inflate(
                cmd == MessageItem.TYPE_RECV ? R.layout.listitem_chat_left_audio : R.layout.listitem_chat_right_audio, null);

        ButterKnife.bind(holder, convertView);
        // 用来存放语音 textview内含图标和文本
        if (mDrawableLeft == null) {
            mDrawableLeft = ContextCompat.getDrawable(mContext,
                    R.drawable.icon_playing_left);
            mDrawableLeft.setBounds(0, 0, mDrawableLeft.getIntrinsicWidth(),
                    mDrawableLeft.getIntrinsicHeight());
        }
        if (mDrawableRight == null) {
            mDrawableRight = ContextCompat.getDrawable(mContext,
                    R.drawable.icon_playing_right);
            mDrawableRight.setBounds(0, 0, mDrawableRight.getIntrinsicWidth(),
                    mDrawableRight.getIntrinsicHeight());
        }
        convertView.setTag(holder);

        return convertView;
    }

    /**
     * 加载聊天-图片控件
     *
     * @param
     * @param holder
     * @param cmd
     */
    private View setupImageViews(ChatViewHolder holder, int cmd) {
        View convertView = inflater.inflate(
                cmd == MessageItem.TYPE_RECV ? R.layout.listitem_chat_left_image : R.layout.listitem_chat_right_image, null);
        ButterKnife.bind(holder, convertView);
        if (cmd == MessageItem.TYPE_RECV) {
            holder.imageView = (DirectImageView) convertView.findViewById(R.id.image);
        } else {
            holder.imageView = (DirectImageView) convertView.findViewById(R.id.image_v);
        }
        holder.ivVidio.setVisibility(View.GONE);
        if (cmd == MessageItem.TYPE_SEND) {
            holder.ivRead = (TextView) convertView.findViewById(R.id.iv_read);
            holder.tvPercent = (TextView) convertView.findViewById(R.id.tv_percent);
        }
        if (cmd == MessageItem.TYPE_RECV) {
            int x = R.drawable.bg_chat_bubble_left;
//			holder.imageView.setMaskResource( .getRId(mContext,
//					"R.drawable.bg_chat_bubble_left"));
        } else {
//			holder.imageView.setMaskResource( .getRId(mContext,
//					"R.drawable.bg_chat_bubble_right"));

        }

        convertView.setTag(holder);

        return convertView;

    }

    private View setupVidioViews(ChatViewHolder holder, int cmd) {

//		R.layout.listitem_chat_left_image
//		R.layout.listitem_chat_right_imag

        View convertView = inflater.inflate(
                cmd == MessageItem.TYPE_RECV ? R.layout.listitem_chat_left_image : R.layout.listitem_chat_right_image, null);
        holder.imageView = (DirectImageView) convertView.findViewById(R.id.image);
        holder.time = (TextView) convertView.findViewById(R.id.time);
        holder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_head_icon);
        holder.progressBarSending = (ProgressBar) convertView.findViewById(R.id.sending_bar);
        holder.ivVidio = (ImageView) convertView.findViewById(R.id.img_vidio);
        holder.tvName = (TextView) convertView.findViewById(R.id.tv_chat_name);
        holder.mergeMsg = (CheckBox) convertView.findViewById(R.id.cb_merge_msg);
        if (cmd == MessageItem.TYPE_RECV) {
//			holder.imageView.setMaskResource( .getRId(mContext,
//					"R.drawable.bg_chat_bubble_left"));
        } else {
//			holder.imageView.setMaskResource( .getRId(mContext,
//					"R.drawable.bg_chat_bubble_right"));

        }
        holder.ivMarkPoint = (ImageView) convertView
                .findViewById(R.id.mark_point);
        convertView.setTag(holder);

        return convertView;

    }

    /**
     * 加载聊天-文本控件
     *
     * @param
     * @param holder
     * @param cmd
     */
    private View setupEventTaskViews(ChatViewHolder holder, int cmd, MessageItem item) {
        View convertView = inflater.inflate(R.layout.listitem_chat_event_task, null);
        ButterKnife.bind(holder, convertView);
        holder.time = (TextView) convertView.findViewById(R.id.card_time);
        holder.container = (ViewGroup) convertView.findViewById(R.id.layout_card);
        holder.textView = (TextView) convertView.findViewById(R.id.title);
        holder.progressBarSending = (ProgressBar) convertView.findViewById(R.id.sending_bar);
        holder.ivFailed = (ImageView) convertView.findViewById(R.id.failed);
        holder.tvName = (TextView) convertView.findViewById(R.id.tv_chat_name);
        holder.event_end = (TextView) convertView.findViewById(R.id.end);
        holder.event_project_name = (TextView) convertView.findViewById(R.id.project_name);
        holder.event_priority = (TextView) convertView.findViewById(R.id.priority);
        holder.event_state_name = (TextView) convertView.findViewById(R.id.state_name);
        holder.event_app_name = (TextView) convertView.findViewById(R.id.app_name);
        holder.event_progress = (TextView) convertView.findViewById(R.id.task_progress);
        holder.ivRead = (TextView) convertView.findViewById(R.id.iv_read);
        holder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_head_icon);
        holder.tvFileName = (TextView) convertView.findViewById(R.id.tv_priority);
        holder.mergeMsg = (CheckBox) convertView.findViewById(R.id.cb_merge_msg);
        convertView.setTag(holder);
        return convertView;

    }

    private View setupEventScheduleViews(ChatViewHolder holder, int cmd, MessageItem item) {
        View convertView = inflater.inflate(R.layout.listitem_chat_event_schedule, null);
        holder.time = (TextView) convertView.findViewById(R.id.card_time);
        holder.container = (ViewGroup) convertView.findViewById(R.id.layout_card);
        holder.textView = (TextView) convertView.findViewById(R.id.title);
        holder.progressBarSending = (ProgressBar) convertView.findViewById(R.id.sending_bar);
        holder.ivFailed = (ImageView) convertView.findViewById(R.id.failed);
        holder.tvName = (TextView) convertView.findViewById(R.id.tv_chat_name);
        holder.event_end = (TextView) convertView.findViewById(R.id.end);
        holder.event_place = (TextView) convertView.findViewById(R.id.place);
        holder.event_join = (Button) convertView.findViewById(R.id.join);
        holder.event_refuse = (Button) convertView.findViewById(R.id.refuse);
        holder.event_app_name = (TextView) convertView.findViewById(R.id.app_name);
        holder.event_layout_bottom_buttons = (ViewGroup) convertView.findViewById(R.id.layout_bottom_buttons);
        holder.event_state_name = (TextView) convertView.findViewById(R.id.time_period);
        holder.ivRead = (TextView) convertView.findViewById(R.id.iv_read);
        holder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_head_icon);
        holder.ivFileIcon = (ImageView) convertView.findViewById(R.id.iv_accept);
        convertView.setTag(holder);
        return convertView;
    }

    private View setupEventApproveViews(ChatViewHolder holder, int cmd, MessageItem item) {
        View convertView = inflater.inflate(R.layout.listitem_chat_event_approve, null);
        holder.time = (TextView) convertView.findViewById(R.id.card_time);
        holder.container = (ViewGroup) convertView.findViewById(R.id.layout_card);
        holder.textView = (TextView) convertView.findViewById(R.id.title);
        holder.progressBarSending = (ProgressBar) convertView.findViewById(R.id.sending_bar);
        holder.ivFailed = (ImageView) convertView.findViewById(R.id.failed);
        holder.tvName = (TextView) convertView.findViewById(R.id.tv_chat_name);
        holder.event_end = (TextView) convertView.findViewById(R.id.end);
        holder.event_deadline = (TextView) convertView.findViewById(R.id.deadline);
        holder.event_remark = (TextView) convertView.findViewById(R.id.remark);
        holder.event_app_name = (TextView) convertView.findViewById(R.id.app_name);
        holder.event_give_back = (Button) convertView.findViewById(R.id.give_back);
        holder.event_reject = (Button) convertView.findViewById(R.id.reject);
        holder.event_ok = (Button) convertView.findViewById(R.id.ok);
        holder.event_layout_bottom_buttons = (ViewGroup) convertView.findViewById(R.id.layout_bottom_buttons);
        holder.event_layout_end = (ViewGroup) convertView.findViewById(R.id.layout_end);
        holder.form_layout = (ViewGroup) convertView.findViewById(R.id.lin_cost);
        holder.relEndTime = (RelativeLayout) convertView.findViewById(R.id.layout_end);
        holder.relDeadTime = (RelativeLayout) convertView.findViewById(R.id.rel_deadline);
        holder.ivRead = (TextView) convertView.findViewById(R.id.iv_read);
        holder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_head_icon);
        holder.ivFileIcon = (ImageView) convertView.findViewById(R.id.iv_accept);
        holder.tvFileName = (TextView) convertView.findViewById(R.id.tv_type);
        holder.relFileLayout = (RelativeLayout) convertView.findViewById(R.id.rel_cost);
        convertView.setTag(holder);
        return convertView;
    }

    private View setupFileViews(ChatViewHolder holder, int cmd, MessageItem item) {
        View convertView = inflater.inflate(
                cmd == MessageItem.TYPE_RECV ? R.layout.listitem_chat_left_file :
                        R.layout.listitem_chat_right_file, null);
        holder.time = (TextView) convertView.findViewById(R.id.time);
        holder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_head_icon);
        holder.progressBarSending = (ProgressBar) convertView.findViewById(R.id.sending_bar);
        holder.tvName = (TextView) convertView.findViewById(R.id.tv_chat_name);
        holder.ivMarkPoint = (ImageView) convertView.findViewById(R.id.mark_point);
        holder.ivFailed = (ImageView) convertView.findViewById(R.id.failed);
        holder.tvFileName = (TextView) convertView.findViewById(R.id.tv_file_name);
        holder.tvFileSize = (TextView) convertView.findViewById(R.id.tv_file_size);
        holder.ivFileIcon = (ImageView) convertView.findViewById(R.id.iv_file_image);
        holder.relFileLayout = (RelativeLayout) convertView.findViewById(R.id.rel_file);
        if (cmd == MessageItem.TYPE_SEND) {
            holder.ivRead = (TextView) convertView.findViewById(R.id.iv_read);
        }
        holder.mergeMsg = (CheckBox) convertView.findViewById(R.id.cb_merge_msg);
        convertView.setTag(holder);
        return convertView;
    }

    private View setupEventAlertViews(ChatViewHolder holder, int cmd, MessageItem item) {
        //TODO:
        View convertView = inflater.inflate(R.layout.listitem_chat_event_alert, null);
        holder.tvFileSize = (TextView) convertView.findViewById(R.id.msg);
        holder.time = (TextView) convertView.findViewById(R.id.card_time);
        holder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_head_icon);
        holder.tvName = (TextView) convertView.findViewById(R.id.tv_chat_name);
        holder.textView = (TextView) convertView.findViewById(R.id.text);
        holder.relFileLayout = (RelativeLayout) convertView.findViewById(R.id.rel_card);
        holder.tvFileName = (TextView) convertView.findViewById(R.id.tv_comment);
        convertView.setTag(holder);
        return convertView;
    }


    /**
     * 加载聊天-文本控件
     *
     * @param
     * @param holder
     * @param cmd
     */
    private View setupTextViews(ChatViewHolder holder, int cmd) {
        View convertView = inflater.inflate(
                cmd == MessageItem.TYPE_RECV ? R.layout.listitem_chat_left_text : R.layout.listitem_chat_right_text, null);
        holder.time = (TextView) convertView.findViewById(R.id.time);
        holder.textView = (TextView) convertView.findViewById(R.id.text);
        holder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_head_icon);
        holder.progressBarSending = (ProgressBar) convertView.findViewById(R.id.sending_bar);
        holder.ivFailed = (ImageView) convertView.findViewById(R.id.failed);
        holder.tvName = (TextView) convertView.findViewById(R.id.tv_chat_name);
        if (cmd == MessageItem.TYPE_SEND) {
            holder.ivRead = (TextView) convertView.findViewById(R.id.iv_read);
        }
        holder.mergeMsg = (CheckBox) convertView.findViewById(R.id.cb_merge_msg);
        holder.ivMarkPoint = (ImageView) convertView
                .findViewById(R.id.mark_point);
        convertView.setTag(holder);
        return convertView;

    }

    /**
     * 消息合并转发
     */
    private View setupMergeViews(ChatViewHolder holder, int cmd) {
        View convertView = inflater.inflate(
                cmd == MessageItem.TYPE_RECV ? R.layout.listitem_chat_left_merge : R.layout.listitem_chat_right_merge, null);
        holder.container = (ViewGroup) convertView.findViewById(R.id.layout_card);
        holder.textView = (TextView) convertView.findViewById(R.id.title);
        holder.progressBarSending = (ProgressBar) convertView.findViewById(R.id.sending_bar);
        holder.ivFailed = (ImageView) convertView.findViewById(R.id.failed);
        holder.tvName = (TextView) convertView.findViewById(R.id.tv_chat_name);
        holder.event_project_name = (TextView) convertView.findViewById(R.id.tv_content1);
        holder.event_state_name = (TextView) convertView.findViewById(R.id.tv_content2);
        holder.event_app_name = (TextView) convertView.findViewById(R.id.tv_content3);
        holder.ivRead = (TextView) convertView.findViewById(R.id.iv_read);
        holder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_head_icon);
        holder.mergeMsg = (CheckBox) convertView.findViewById(R.id.cb_merge_msg);
        holder.ivMarkPoint = (ImageView) convertView.findViewById(R.id.mark_point);
        convertView.setTag(holder);
        return convertView;
    }

    private MessageItem getPreItemWithDate(int position) {
        if (position <= 0 || position >= mListData.size()) {
            return null;
        }

        while (--position >= 0) {
            if (!TextUtils
                    .isEmpty(mListData.get(position).getCreateDate() + "")) {
                return mListData.get(position);
            }
        }

        return null;
    }

    @Override
    public int getCount() {
        return mListData == null ? 0 : mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= getCount())
            return 0;
        MessageItem item = mListData.get(position);
        String type = item.getType();
        int cmd = mListData.get(position).getCmd();
        int viewType = 0;
        // 文本
        if (type.equals(Command.TEXT)) {// 消息
            switch (cmd) {
                case MessageItem.TYPE_RECV:
                    viewType = TYPE_MSG_TEXT_LEFT;
                    break;
                case MessageItem.TYPE_SEND:
                    viewType = TYPE_MSG_TEXT_RIGHT;
                    break;

                default:
                    break;
            }
        }
        // 图片
        else if (type.equals(Command.IMAGE)) {
            switch (cmd) {
                case MessageItem.TYPE_RECV:
                    viewType = TYPE_MSG_IMAGE_LEFT;
                    break;
                case MessageItem.TYPE_SEND:
                    viewType = TYPE_MSG_IMAGE_RIGHT;
                    break;

                default:
                    break;
            }
        }
        // 语音
        else if (type.equals(Command.AUDIO)) {
            switch (cmd) {
                case MessageItem.TYPE_RECV:
                    viewType = TYPE_MSG_AUDIO_LEFT;
                    break;
                case MessageItem.TYPE_SEND:
                    viewType = TYPE_MSG_AUDIO_RIGHT;
                    break;

                default:
                    break;
            }
        }

        // 视频
        else if (type.equals(Command.VIDEO)) {
            switch (cmd) {
                case MessageItem.TYPE_RECV:
                    viewType = TYPE_MSG_VIDIO_LEFT;
                    break;
                case MessageItem.TYPE_SEND:
                    viewType = TYPE_MSG_VIDIO_RIGHT;
                    break;

                default:
                    break;
            }
        }

        //文件
        else if (type.equals(Command.FILE)) {
            switch (cmd) {
                case MessageItem.TYPE_RECV:
                    viewType = TYPE_MSG_FILE_LEFT;
                    break;
                case MessageItem.TYPE_SEND:
                    viewType = TYPE_MSG_FILE_RIGHT;
                    break;
                default:
                    break;
            }
        } else if (type.equals(Command.EVENT)) {
            String content = item.getContent();
            MessageEventEntity eventEntity = TTJSONUtil.convertJsonToObj(content, new TypeReference<MessageEventEntity>() {
            });
            if (eventEntity.getMsgType() == 0) {
                viewType = TYPE_MSG_EVENT_ALERT;
            } else {
                String name = eventEntity.getMsgAppShowID();
                if (name.contains(com.mintcode.launchr.util.Const.SHOWID_TASK)) {
                    viewType = TYPE_MSG_EVENT_TASK;
                } else if (name.contains(com.mintcode.launchr.util.Const.SHOW_SCHEDULE)) {
                    viewType = TYPE_MSG_EVENT_SCHEDULE;
                } else if (name.contains(com.mintcode.launchr.util.Const.SHOWID_APPROVE)) {
                    viewType = TYPE_MSG_EVENT_APPROVE;
                } else {
                    viewType = TYPE_MSG_EVENT_TASK;
                }
            }
        } else if (type.equals(Command.ALERT)) {
            viewType = TYPE_MSG_ALERT;
        } else if (type.equals(Command.RESEND)) {
            viewType = TYPE_MSG_ALERT;
        }
        // 消息合并转发
        else if (type.equals(Command.MERGE)) {
            switch (cmd) {
                case MessageItem.TYPE_RECV:
                    viewType = TYPE_MSG_MERGE_LEFT;
                    break;
                case MessageItem.TYPE_SEND:
                    viewType = TYPE_MSG_MERGE_RIGHT;
                    break;
                default:
                    break;
            }
        }

        return viewType;
    }

    @Override
    public int getViewTypeCount() {
        return NUM_VIEW_TYPE;
    }

    /**
     * 申请的同意弹窗
     */
    private void passApply(String showId, int position) {
        mAgreePopWindow = new AgreePopWindow(mContext, showId, position);
        mAgreePopWindow.setItemSelectListener(this);
        mAgreePopWindow.show(mRelInputContainer);
    }

    /**
     * 申请的同意弹窗接口
     */
    @Override
    public void agree(String showId, int position) {
        ApproveAPI.getInstance().processApprove(listenr, showId, "APPROVE", "");
        mAgreePopWindow.dismiss();
    }

    /**
     * 申请的同意并转交下一联系人弹窗接口
     */
    @Override
    public void turnOver(String showId, int position) {
        mWriteCommentPopWindow = new WriteCommentPopWindow(mContext, WriteCommentPopWindow.TYPE_TURN_OVER,
                showId, position, -1);
        mWriteCommentPopWindow.setstartActivityLinsenter(this);
        mWriteCommentPopWindow.show(mRelInputContainer);
        mAgreePopWindow.dismiss();
    }

    /**
     * 选择下一审批人的接口
     */
    @Override
    public void startDeptActiviy() {
        Intent intent = new Intent(mContext, DepartmentActivity.class);
        intent.putExtra(DepartmentActivity.KEY_SINGLE, DepartmentActivity.SINGLE);
        if (mEntity != null) {
            intent.putExtra(DepartmentActivity.KEY_POSITION, mEntity);
        }
        ((ChatActivity) mContext).startActivityForResult(intent, 1006);
    }

    /**
     * 选择下一审批人的返回结果
     */
    public void dealNextPeopleResult(Intent data) {
        mEntity = (UserDetailEntity) data.getSerializableExtra(DepartmentActivity.KEY_USE_ENTITY);
        mWriteCommentPopWindow.setCompleteSelected(mEntity, mIndex);
    }

    /**
     * 申请的拒绝弹窗
     */
    private void refuseApply(String showId, int position) {
        mWriteCommentPopWindow = new WriteCommentPopWindow(mContext, WriteCommentPopWindow.TYPE_RUFUSE,
                showId, position, -1);
        mWriteCommentPopWindow.show(mRelInputContainer);
    }

    /**
     * 申请的打回弹窗
     */
    private void reCallApply(String showId, int position) {
        mWriteCommentPopWindow = new WriteCommentPopWindow(mContext, WriteCommentPopWindow.TYPE_RECALL,
                showId, position, -1);
        mWriteCommentPopWindow.show(mRelInputContainer);
    }

    /**
     * 拒绝参加会议弹窗
     */
    private void refuseMeeting(String showId, int position) {
        mWriteCommentPopWindow = new WriteCommentPopWindow(mContext, WriteCommentPopWindow.TYPE_MEETING_REFUSE,
                showId, position, -1);
        mWriteCommentPopWindow.show(mRelInputContainer);
        TextView refuseTitle = (TextView) mWriteCommentPopWindow.getContentView().findViewById(R.id.tv_approve_comment);
        refuseTitle.setText(mContext.getString(R.string.refuse_reason));
    }

    /**
     * 设置是否显示转发按钮
     */
    private void setShowMergeButton(ChatViewHolder holder, int position) {
        // 转发
        if (showMergeButton) {
            holder.mergeMsg.setVisibility(View.VISIBLE);
            if (mChooseMergeData.get(position)) {
                holder.mergeMsg.setChecked(true);
            } else {
                holder.mergeMsg.setChecked(false);
            }
        } else {
            holder.mergeMsg.setVisibility(View.GONE);
        }
    }

    /**
     * 列表刷新时，增加数据
     */
    public void addDataWhenRefresh(int size, boolean top) {
        if (mChooseMergeData == null) {
            mChooseMergeData = new ArrayList<Boolean>();
            mListMergeData = new ArrayList<MessageItem>();
        }
        int beforeSize = mChooseMergeData.size();
        for (int i = 0; i < size - beforeSize; i++) {
            if (top) {
                mChooseMergeData.add(mChooseMergeData.size(), false);
            } else {
                mChooseMergeData.add(0, false);
            }
        }
    }

    /**
     * 添加、或移除从消息转发列表
     */
    public void addMsgToMergeList(MessageItem item, int position) {
        if (mChooseMergeData.get(position)) {
            mListMergeData.remove(item);
            mChooseMergeData.set(position, false);
        } else {
            mListMergeData.add(item);
            mChooseMergeData.set(position, true);

        }
        ((ChatActivity) mContext).setListViewPosition();
    }

}
