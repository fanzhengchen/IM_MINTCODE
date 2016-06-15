package com.mintcode.launchr.app.newSchedule.view;

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
import com.mintcode.launchr.app.meeting.activity.NewMeetingActivity;
import com.mintcode.launchr.app.newSchedule.activity.AddScheduleEventActivity;
import com.mintcode.launchr.app.newSchedule.activity.ScheduleMainActivity;

/**
 * Created by JulyYu on 2016/3/14.
 */
public class SchedulePopWindow extends PopupWindow implements View.OnClickListener{

    private View mContentView;

    private Context mContext;

    private TextView mTvNewMeeting;
    private TextView mTvNewEvent;
    private TextView mTvSeeOther;
    private TextView mTvGoogleSchedule;


    public SchedulePopWindow(Context context) {
        super(context);
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        mContentView = inflater.inflate(R.layout.popwindow_schedule_add,null);
        setWindow();
        initView();
    }

    private void initView() {
        mTvNewMeeting = (TextView) mContentView.findViewById(R.id.tv_new_meeting);
        mTvNewEvent = (TextView) mContentView.findViewById(R.id.tv_new_event);
       mTvSeeOther = (TextView) mContentView.findViewById(R.id.tv_see_other_schedule);
       mTvGoogleSchedule = (TextView) mContentView.findViewById(R.id.tv_google_schedule);
        mTvNewMeeting.setOnClickListener(this);
        mTvNewEvent.setOnClickListener(this);
        mTvSeeOther.setOnClickListener(this);
        mTvGoogleSchedule.setOnClickListener(this);
        mTvGoogleSchedule.setVisibility(View.GONE);
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
        if(v == mTvNewMeeting){
            Intent newMeeting = new Intent(mContext, NewMeetingActivity.class);
            if(mContext instanceof ScheduleMainActivity){
                ((ScheduleMainActivity)mContext).stopCastBoard();
            }
            mContext.startActivity(newMeeting);
        }else if(v == mTvNewEvent){
            Intent newEvent = new Intent(mContext,AddScheduleEventActivity.class);
            mContext.startActivity(newEvent);
        }else if(v == mTvSeeOther){
            ((ScheduleMainActivity)mContext).showFragmentForChoose();
        }else if(v == mTvGoogleSchedule){

        }
        this.dismiss();
    }

}
