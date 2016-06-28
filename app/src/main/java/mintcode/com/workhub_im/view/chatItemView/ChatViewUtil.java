package mintcode.com.workhub_im.view.chatItemView;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;

import java.util.List;

import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.im.Command;
import mintcode.com.workhub_im.pojo.MessageEventEntity;
import mintcode.com.workhub_im.pojo.MessageInfoEntity;
import mintcode.com.workhub_im.view.chatItemView.leftView.MsgAudioLeftView;
import mintcode.com.workhub_im.view.chatItemView.leftView.MsgFileLeftView;
import mintcode.com.workhub_im.view.chatItemView.leftView.MsgImageLeftView;
import mintcode.com.workhub_im.view.chatItemView.leftView.MsgMergeLeftView;
import mintcode.com.workhub_im.view.chatItemView.leftView.MsgTaskLeftView;
import mintcode.com.workhub_im.view.chatItemView.leftView.MsgTextLeftView;
import mintcode.com.workhub_im.view.chatItemView.rightView.MsgAudioRightView;
import mintcode.com.workhub_im.view.chatItemView.rightView.MsgFileRightView;
import mintcode.com.workhub_im.view.chatItemView.rightView.MsgImageRightView;
import mintcode.com.workhub_im.view.chatItemView.rightView.MsgMergeRightView;
import mintcode.com.workhub_im.view.chatItemView.rightView.MsgTextRightView;
import mintcode.com.workhub_im.widget.RoundBackgroundColorSpan;
import mintcode.com.workhub_im.widget.upload.AttachItem;

/**
 * Created by JulyYu on 2016/6/14.
 */
public class ChatViewUtil {

    /***
     * 接收
     */
    public static final int TYPE_RECV = 1;
    /***
     * 发送
     */
    public static final int TYPE_SEND = 0;

    //APP的固定字符串
    public static final String SHOWID_TASK = "PWP16jQLLjFEZXLe";
    public static final String SHOWID_APPROVE = "ADWpPoQw85ULjnQk";
    public static final String SHOW_SCHEDULE = "l6b3YdE9LzTnmrl7";
    public static final String SHOW_FRIEND = "Relation";

    /**
     * 文本消息 左
     */
    public static final int TYPE_MSG_TEXT_LEFT = 0x001;
    /**
     * 文本消息 右
     */
    public static final int TYPE_MSG_TEXT_RIGHT = 0x002;
    /**
     * 图片消息 左
     */
    public static final int TYPE_MSG_IMAGE_LEFT = 0x003;
    /**
     * 图片消息 右
     */
    public static final int TYPE_MSG_IMAGE_RIGHT = 0x004;
    /**
     * 语音消息 左
     */
    public static final int TYPE_MSG_AUDIO_LEFT = 0x005;
    /**
     * 语音消息 右
     */
    public static final int TYPE_MSG_AUDIO_RIGHT = 0x006;
    /**
     * 图片语音右
     */
    public static final int TYPE_MSG_VIDIO_LEFT = 0x007;
    /**
     * 图片语音 右
     */
    public static final int TYPE_MSG_VIDIO_RIGHT = 0x008;
    /**
     * 推送消息
     */
    public static final int TYPE_MSG_ALERT = 0x009;
    /**
     * 任务消息
     */
    public static final int TYPE_MSG_EVENT_TASK = 0x00a;
    /**
     * 日程消息
     */
    public static final int TYPE_MSG_EVENT_SCHEDULE = 0x00b;
    /**
     * 审批消息
     */
    public static final int TYPE_MSG_EVENT_APPROVE = 0x00c;
    /**
     * 消息
     */
    public static final int TYPE_MSG_EVENT_ALERT = 0x00d;
    /**
     * 文件接收
     */
    public static final int TYPE_MSG_FILE_LEFT = 0X00e;  //接受的文件
    /**
     * 文件发送
     */
    public static final int TYPE_MSG_FILE_RIGHT = 0x00f;  //发送文件
    /**
     * 消息转发 右
     */
    public static final int TYPE_MSG_MERGE_RIGHT = 0x010;  //消息合并转发 右
    /**
     * 消息转发 左
     */
    public static final int TYPE_MSG_MERGE_LEFT = 0x11;  // 消息合并左

    public static final int NUM_VIEW_TYPE = 0x012;// 总数必须必布局类型要大


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
    public static final int TYPE_HANDLE_DATA_FINISH = 0x0020;

    /**
     * 获取聊天类型
     */

    public static int getChatViewType(MessageItem item) {
        String type = item.getType();
        int cmd = item.getCmd();
        int viewType = 0;
        // 文本
        if (type.equals(Command.TEXT)) {// 消息
            switch (cmd) {
                case TYPE_RECV:
                    viewType = TYPE_MSG_TEXT_LEFT;
                    break;
                case TYPE_SEND:
                    viewType = TYPE_MSG_TEXT_RIGHT;
                    break;

                default:
                    break;
            }
        }
        // 图片
        else if (type.equals(Command.IMAGE)) {
            switch (cmd) {
                case TYPE_RECV:
                    viewType = TYPE_MSG_IMAGE_LEFT;
                    break;
                case TYPE_SEND:
                    viewType = TYPE_MSG_IMAGE_RIGHT;
                    break;

                default:
                    break;
            }
        }
        // 语音
        else if (type.equals(Command.AUDIO)) {
            switch (cmd) {
                case TYPE_RECV:
                    viewType = TYPE_MSG_AUDIO_LEFT;
                    break;
                case TYPE_SEND:
                    viewType = TYPE_MSG_AUDIO_RIGHT;
                    break;

                default:
                    break;
            }
        }

        // 视频
        else if (type.equals(Command.VIDEO)) {
            switch (cmd) {
                case TYPE_RECV:
                    viewType = TYPE_MSG_VIDIO_LEFT;
                    break;
                case TYPE_SEND:
                    viewType = TYPE_MSG_VIDIO_RIGHT;
                    break;

                default:
                    break;
            }
        }

        //文件
        else if (type.equals(Command.FILE)) {
            switch (cmd) {
                case TYPE_RECV:
                    viewType = TYPE_MSG_FILE_LEFT;
                    break;
                case TYPE_SEND:
                    viewType = TYPE_MSG_FILE_RIGHT;
                    break;
                default:
                    break;
            }
        } else if (type.equals(Command.EVENT)) {
            String content = item.getContent();
            MessageEventEntity eventEntity = JSON.parseObject(content, MessageEventEntity.class);
            if (eventEntity.getMsgType() == 0) {
                viewType = TYPE_MSG_EVENT_ALERT;
            } else {
                String name = eventEntity.getMsgAppShowID();
                if (name.contains(SHOWID_TASK)) {
                    viewType = TYPE_MSG_EVENT_TASK;
                } else if (name.contains(SHOW_SCHEDULE)) {
                    viewType = TYPE_MSG_EVENT_SCHEDULE;
                } else if (name.contains(SHOWID_APPROVE)) {
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
                case TYPE_RECV:
                    viewType = TYPE_MSG_MERGE_LEFT;
                    break;
                case TYPE_SEND:
                    viewType = TYPE_MSG_MERGE_RIGHT;
                    break;
                default:
                    break;
            }
        }
        return viewType;
    }


    /**
     * 获取聊天类型视图
     */
    public static View getChatTypeView(ViewGroup viewGroup, int viewType) {
        View view = null;
        switch (viewType) {
            // 文本 左
            case TYPE_MSG_TEXT_LEFT:
                view = new MsgTextLeftView(viewGroup.getContext(), null);
                break;
            // 文本 右
            case TYPE_MSG_TEXT_RIGHT:
                view = new MsgTextRightView(viewGroup.getContext(), null);
                break;
            // 图片 左
            case TYPE_MSG_IMAGE_LEFT:
                view = new MsgImageLeftView(viewGroup.getContext(), null);
                break;
            // 图片 右
            case TYPE_MSG_IMAGE_RIGHT:
                view = new MsgImageRightView(viewGroup.getContext(), null);
                break;
            // 语音 左
            case TYPE_MSG_AUDIO_LEFT:
                view = new MsgAudioLeftView(viewGroup.getContext(), null);
                break;
            // 语言 右
            case TYPE_MSG_AUDIO_RIGHT:
                view = new MsgAudioRightView(viewGroup.getContext(), null);
                break;
            //视频
            case TYPE_MSG_VIDIO_LEFT:
                break;
            case TYPE_MSG_VIDIO_RIGHT:
                break;
            //文件 左
            case TYPE_MSG_FILE_LEFT:
                view = new MsgFileLeftView(viewGroup.getContext(), null);
                break;
            //文件 右
            case TYPE_MSG_FILE_RIGHT:
                view = new MsgFileRightView(viewGroup.getContext(), null);
                break;
            // 消息框
            case TYPE_MSG_EVENT_ALERT:
                view = new EventAlertChatView(viewGroup.getContext(), null);
                break;
            // 任务消息
            case TYPE_MSG_EVENT_TASK:
                        view = new MsgTaskLeftView(viewGroup.getContext(),null);
                break;
            // 日程消息
            case TYPE_MSG_EVENT_SCHEDULE:
                        view = new MsgScheduleView(viewGroup.getContext(),null);
                break;
            // 审批消息
            case TYPE_MSG_EVENT_APPROVE:
                view = new MsgApproveView(viewGroup.getContext(), null);
                break;
            // 简单提醒消息
            case TYPE_MSG_ALERT:
                view = new MsgAlartView(viewGroup.getContext(), null);
                break;
            // 消息合并转发 左
            case TYPE_MSG_MERGE_LEFT:
                view = new MsgMergeLeftView(viewGroup.getContext(), null);
                break;
            // 消息合并转发 右
            case TYPE_MSG_MERGE_RIGHT:
                view = new MsgMergeRightView(viewGroup.getContext(), null);
                break;
            default:
                break;
        }

        return view;
    }

    /**
     * 设置文本显示
     */
    public static void setTextDisplay(Context context, MessageItem item, TextView textView, boolean left) {
        //文本内容
        String strtextContent = item.getContent();

        SpannableStringBuilder spannableString = new SpannableStringBuilder(strtextContent);
        String strInfo = item.getInfo();
        //@对象高亮
        if (!TextUtils.isEmpty(strInfo)) {
            MessageInfoEntity entity = JSON.parseObject(strInfo, MessageInfoEntity.class);
            if (entity != null) {
                List<String> list = entity.getAtUsers();
                // 判断是否有@对象是否为空
                if (list != null && !list.isEmpty()) {
                    boolean isMark = false;
                    int backColor;
                    if (left) {
                        backColor = ContextCompat.getColor(context, R.color.chat_below_text);
                    } else {
                        backColor = ContextCompat.getColor(context, R.color.blue_launchr);
                    }
                    int textColor = Color.WHITE;
                    for (int i = 0; i < list.size(); i++) {
                        String str = list.get(i);
                        // 判断@字符串是否为空
                        if (!TextUtils.isEmpty(str)) {
                            // 找到@人员的名称
                            int x = str.indexOf("@");
                            String nameString = str.substring(x);
                            int index = strtextContent.indexOf(nameString);
                            if (isMark) {
                                isMark = false;
                                index = strtextContent.lastIndexOf(nameString);
                            }
                            if (index != -1) {
                                spannableString.replace(index, index + nameString.length(), " " + nameString);
                                strtextContent = spannableString.toString();
                                RoundBackgroundColorSpan span = new RoundBackgroundColorSpan(backColor, textColor);
                                spannableString.setSpan(span, index, index + nameString.length() + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            }
                        } else {
                            // 是否有复制的文字
                            isMark = true;
                        }
                    }
                }
            }
        }
        //        Linkify.addLinks(mTvText, Linkify.WEB_URLS);
        //        RegularUtil.stripUnderlines(mTvText);
        textView.setText(spannableString);
    }

    public static void setChatAuido() {

    }

    public static void setChatImageDisplay(MessageItem item){

        String content = item.getContent();
        AttachItem attach = JSON.parseObject(content,AttachItem.class);
        String imgUrl = AppConsts.httpIp + "/" + AppConsts.APP_NAME + attach.getThumbnail();
        int placehodler = R.drawable.im_default_image;
        if(TYPE_RECV == item.getCmd()){

        }else{

        }


    }

}
