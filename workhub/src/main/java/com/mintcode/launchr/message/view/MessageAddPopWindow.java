package com.mintcode.launchr.message.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.message.activity.CreateGroupActivity;
import com.mintcode.launchr.more.activity.MySweepActivity;

public class MessageAddPopWindow extends PopupWindow implements View.OnClickListener {
    private View mContentView;

    private Context mContext;

    /** 新建聊天*/
    private TextView mTvNewChat;
    /** 扫一扫*/
    private TextView mTvChatSetting;
    /** 新建任务*/
//    private TextView mTvNewTask;
    /** 新建日程*/
//    private TextView mTvNewSchedule;

    private View mView;

    public MessageAddPopWindow(Context context, View parentView) {
        super(context);
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        mContentView = inflater.inflate(R.layout.popwindow_message_add,null);
        mView = parentView;
        setWindow();
        initView();
    }

    private void initView() {
        mTvNewChat = (TextView) mContentView.findViewById(R.id.tv_new_chat);
        mTvChatSetting = (TextView) mContentView.findViewById(R.id.tv_scan);
//      mTvNewTask = (TextView) mContentView.findViewById(R.id.tv_new_task);
//      mTvNewSchedule = (TextView) mContentView.findViewById(R.id.tv_new_schedule);
        mTvNewChat.setOnClickListener(this);
        mTvChatSetting.setOnClickListener(this);
//        mTvNewTask.setOnClickListener(this);
//        mTvNewSchedule.setOnClickListener(this);
    }

    private void setWindow() {
        // 设置PopupWindow的View
        this.setContentView(mContentView);
        // 设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为透明
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_new_chat:
                Intent intentChat = new Intent(mContext, CreateGroupActivity.class);
                mContext.startActivity(intentChat);
                dismiss();
                break;
            case R.id.tv_scan:
                Intent intentScan = new Intent(mContext, MySweepActivity.class);
                mContext.startActivity(intentScan);
                dismiss();
                break;
//            case R.id.tv_new_task:
//                Intent intentTask = new Intent(mContext, NewTaskActivity.class);
//                mContext.startActivity(intentTask);
//                MessageAddTaskPopWindow messageAddTaskPopWindow = new MessageAddTaskPopWindow(mContext);
//                messageAddTaskPopWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);
//                break;
//            case R.id.tv_new_schedule:
//                Intent intentSchedule = new Intent(mContext, AddScheduleEventActivity.class);
//                mContext.startActivity(intentSchedule);
//                ChatAddScheduleWindow chatAddScheduleWindow = new ChatAddScheduleWindow(mContext, "");
//                chatAddScheduleWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);
//                break;
            default:
                break;
        }
        dismiss();
    }

}
