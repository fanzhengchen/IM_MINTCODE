package com.mintcode.chat.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;

public class PressMessageDialog extends Dialog {
    /**
     * 标记为重点
     */
    private TextView mTvMarkMsg;

    /**
     * 消息撤回
     */
    private TextView mTvResend;
    private ImageView mIvResend;

    /**
     * 消息复制
     */
    private TextView mTvCopy;
    private ImageView mIvCopy;

    /**
     * 消息传任务
     */
    private TextView mTvTask;
    private ImageView mIvTask;

    /**
     * 消息转日程
     */
    private TextView mTvSchedule;
    private ImageView mIvSchedule;

    /** 更多*/
    private TextView mTvMore;
    private ImageView mIvMore;

    /** 播放切换 */
    private TextView mTvChangePlay;

    public PressMessageDialog(Context context) {
        super(context);
    }

    public PressMessageDialog(Context context, int theme) {
        super(context, theme);
    }

    protected PressMessageDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_press_message);


        initView();

    }

    private void initView() {
        mTvMarkMsg = (TextView) findViewById(R.id.tv_msg_mark);
        mTvResend = (TextView) findViewById(R.id.tv_msg_resend);
        mTvCopy = (TextView) findViewById(R.id.tv_msg_copy);
        mTvTask = (TextView) findViewById(R.id.tv_msg_task);
        mTvSchedule = (TextView) findViewById(R.id.tv_msg_schedule);
        mIvCopy = (ImageView) findViewById(R.id.line_copy);
        mIvTask = (ImageView) findViewById(R.id.line_task);
        mIvSchedule = (ImageView) findViewById(R.id.line_schedule);
        mIvResend = (ImageView) findViewById(R.id.line_resend);
        mTvMore = (TextView) findViewById(R.id.tv_msg_more);
        mIvMore = (ImageView) findViewById(R.id.line_more);
        mTvChangePlay = (TextView) findViewById(R.id.tv_change_play);


    }



    /**
     * 标记重点
     */
    public void setMarkListener(int rId, View.OnClickListener listener) {
        mTvMarkMsg.setText(rId);
        mTvMarkMsg.setOnClickListener(listener);
    }

    /**
     * 消息撤回
     */
    public void setMsgResendListener(String name, View.OnClickListener listener) {
        mIvResend.setVisibility(View.VISIBLE);
        mTvResend.setVisibility(View.VISIBLE);
        mTvResend.setText(name);
        mTvResend.setOnClickListener(listener);
    }

    /**
     * 消息复制
     */
    public void setMsgCopyListener(String name, View.OnClickListener listener) {
        mIvCopy.setVisibility(View.VISIBLE);
        mTvCopy.setVisibility(View.VISIBLE);
        mTvCopy.setText(name);
        mTvCopy.setOnClickListener(listener);
    }

    /**
     * 消息转任务
     */
    public void setMsgTaskListener(String name, View.OnClickListener listener) {
        mIvTask.setVisibility(View.VISIBLE);
        mTvTask.setVisibility(View.VISIBLE);
        mTvTask.setText(name);
        mTvTask.setOnClickListener(listener);
    }

    /**
     * 消息转日程
     */
    public void setMsgScheduleListener(String name, View.OnClickListener listener) {
        mTvSchedule.setVisibility(View.VISIBLE);
        mTvSchedule.setText(name);
        mIvSchedule.setVisibility(View.VISIBLE);
        mTvSchedule.setOnClickListener(listener);
    }

    /**
     * 更多
     */
    public void setMsgMoreListener(String name, View.OnClickListener listener) {
        mTvMore.setVisibility(View.VISIBLE);
        mTvMore.setText(name);
        mIvMore.setVisibility(View.VISIBLE);
        mTvMore.setOnClickListener(listener);
    }

    /**
     * 判断语音播放模式
     * @param name
     * @param listener
     */
    public void setVoicePlayStyle(String name,View.OnClickListener listener){
        mTvChangePlay.setVisibility(View.VISIBLE);
        mTvChangePlay.setText(name);
        mTvChangePlay.setOnClickListener(listener);
    }

}


