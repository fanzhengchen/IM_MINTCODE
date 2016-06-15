package com.mintcode.launchr.message.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.ScheduleApi;
import com.mintcode.launchr.app.meeting.activity.RemindActivity;
import com.mintcode.launchr.app.newSchedule.activity.AddScheduleEventActivity;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.pojo.AddSchedulePOJO;
import com.mintcode.launchr.pojo.entity.ScheduleEntity;
import com.mintcode.launchr.timer.Timer;
import com.mintcode.launchr.timer.listener.OnTimerListener;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.TimeFormatUtil;
import com.mintcode.launchrnetwork.OnResponseListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 消息转日程
 */
public class ChatAddScheduleWindow extends PopupWindow implements
        CompoundButton.OnCheckedChangeListener, View.OnClickListener, OnTimerListener {
    private View mContentView;

    private LayoutInflater mInflater;

    private Context mContext;

    /** 时间控件 */
    private Timer mTimer;

    /** 开始，结束时间 */
    private Calendar startTime;
    private Calendar stopTime;

    /** 全天 */
    private CheckBox mCbAllDay;
    private TextView mTvTimeDis;

    /** 更多选项 */
    private TextView mTvMoreItem;

    /** 提醒 */
    private RelativeLayout mRelEnter;
    private TextView mTvMemberName;

    /** 提醒类型*/
    private int remindType = 0;

    /** 标题 */
    private EditText mEditTitle;
    private String mTitle;

    /** 确定 */
    private ImageButton mIvOk;

    public ChatAddScheduleWindow(Context context, String title) {
        super(context);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mContentView = mInflater.inflate(R.layout.popwindow_add_schedule, null);
        mTitle = title;

        setWindow();
        initView();
    }

    private void setWindow() {
        this.setContentView(mContentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        ColorDrawable color = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(color);
    }

    private void initView() {
        mTimer = (Timer) mContentView.findViewById(R.id.add_task_timer);
        mCbAllDay = (CheckBox) mContentView.findViewById(R.id.cb_is_allday);
        mTvTimeDis = (TextView) mContentView.findViewById(R.id.tv_time_display);
        mTvMoreItem = (TextView) mContentView.findViewById(R.id.tv_more);
        mRelEnter = (RelativeLayout) mContentView.findViewById(R.id.rel_enter);
        mEditTitle = (EditText) mContentView.findViewById(R.id.edit_title);
        mEditTitle.setText(mTitle);
        mIvOk = (ImageButton) mContentView.findViewById(R.id.btn_ok);
        mTvMemberName = (TextView) mContentView.findViewById(R.id.tv_member_name);
        mTvMemberName.setText(mContext.getString(R.string.calendar_not_remind));

        mCbAllDay.setOnCheckedChangeListener(this);
        mTvMoreItem.setOnClickListener(this);
        mRelEnter.setOnClickListener(this);
        mIvOk.setOnClickListener(this);
        mTimer.setTimeBeforeNow(false);
        mTimer.setOnTimerListener(this);
        mTimer.setConflictViewDisplay(false);


        startTime = Calendar.getInstance();
        stopTime = Calendar.getInstance();
        mTimer.setShowTime(startTime.getTimeInMillis(),stopTime.getTimeInMillis());
        mTimer.setStyle(Timer.ALL_DAY_TIME);
        mTvTimeDis.setText(TimeFormatUtil.setTime(startTime.getTimeInMillis(),stopTime.getTimeInMillis(),true));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            mTimer.setStyle(Timer.ALL_DAY_TIME);
            mTvTimeDis.setText(TimeFormatUtil.setTime(startTime.getTimeInMillis(),stopTime.getTimeInMillis(),true));
        }else{
            mTimer.setStyle(Timer.FIVE_POSITON);
            if(stopTime.getTimeInMillis() - startTime.getTimeInMillis() < 3600000){
                stopTime.set(Calendar.HOUR_OF_DAY,startTime.get(Calendar.HOUR_OF_DAY ) + 1);
                stopTime.set(Calendar.MINUTE,startTime.get(Calendar.MINUTE));
                mTimer.setShowTime(startTime.getTimeInMillis(),stopTime.getTimeInMillis());
            }
            mTvTimeDis.setText(TimeFormatUtil.setTime(startTime.getTimeInMillis(),stopTime.getTimeInMillis(),false));
        }
    }

    @Override
    public void OnTimeChangeListenner(View view, int year, int month, int day,
                                      int hour, int minute, int type, boolean isEnd) {
         startTime.setTimeInMillis(mTimer.getBeginTime());
         stopTime.setTimeInMillis(mTimer.getEndTime());
        if(type == Timer.FIVE_POSITON){
            mTvTimeDis.setText(TimeFormatUtil.setTime(startTime.getTimeInMillis(),stopTime.getTimeInMillis(),false));
        }else if(type == Timer.ALL_DAY_TIME){
            mTvTimeDis.setText(TimeFormatUtil.setTime(startTime.getTimeInMillis(),stopTime.getTimeInMillis(),true));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mTvMoreItem) {
            doMoreChoose();
        } else if (v == mRelEnter) {
            doRemindChoose();
        } else if (v == mIvOk) {
            sendMessage();
        }
    }

    /** 发送消息 */
    public void sendMessage() {
        if (mEditTitle.getText().length() <= 0) {
            Toast.makeText(mContext, mContext.getString(R.string.calendar_title_toast), Toast.LENGTH_SHORT).show();
            return;
        }

        if(startTime.getTimeInMillis() >  stopTime.getTimeInMillis()){
            Toast.makeText(mContext, mContext.getString(R.string.end_time_must_not_be_later_than_begin_time), Toast.LENGTH_SHORT).show();
            return;
        }

        List<Long> start = new ArrayList<Long>();
        start.add(startTime.getTimeInMillis());
        List<Long> stop = new ArrayList<Long>();
        stop.add(stopTime.getTimeInMillis());
        ScheduleEntity entity = new ScheduleEntity();
        entity.setTitle(mEditTitle.getText().toString());
        entity.setStart(start);
        entity.setEnd(stop);
        entity.setRemindType(remindType);
        entity.setType("event");
        if(mCbAllDay.isChecked()){
            entity.setIsAllDay(1);
        }else{
            entity.setIsAllDay(2);
        }
        ScheduleApi.getInstance().saveSchedule(addTaskResult, entity);
    }

    private OnResponseListener addTaskResult = new OnResponseListener() {

        @Override
        public void onResponse(Object response, String taskId, boolean rawData) {
            if (response == null) {
                return;
            } else if (taskId.equals(ScheduleApi.URL.SAVE_SCHEDULE)){
                // 判断是否是新建日程
                handleAddScheduleResult(response);
            }
        }

        @Override
        public boolean isDisable() {
            // TODO Auto-generated method stub
            return false;
        }
    };

    /**
     * 处理消息转日程返回
     * @param response
     */
    private void handleAddScheduleResult(Object response){
        AddSchedulePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), AddSchedulePOJO.class);
        if (pojo == null) {
            return;
        }else if(pojo.isResultSuccess() == false){
            Toast.makeText(mContext,pojo.getBody().getResponse().getReason(), Toast.LENGTH_SHORT).show();
            return;
        }else{
            Toast.makeText(mContext, mContext.getString(R.string.msg_change__schedule_success), Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    /** 更多选项 */
    private void doMoreChoose() {
        ScheduleEntity.Times times = new ScheduleEntity.Times();
        times.setStart(startTime.getTimeInMillis());
        times.setEnd(stopTime.getTimeInMillis());
        List<ScheduleEntity.Times> listTimes = new ArrayList<ScheduleEntity.Times>();
        listTimes.add(times);

        ScheduleEntity entity = new ScheduleEntity();
        entity.setTitle(mEditTitle.getText().toString());
        entity.setRemindType(remindType);
        int isAllDay = mCbAllDay.isChecked() == true ?1:0;
        entity.setIsAllDay(isAllDay);
        entity.setIsVisible(1);
        entity.setTimes(listTimes);

        Intent intent = new Intent(mContext, AddScheduleEventActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("event", entity);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
        dismiss();
    }

    /** 提醒 */
    private void doRemindChoose() {
        Intent intent = new Intent(mContext, RemindActivity.class);
        intent.putExtra("isAllDay",mCbAllDay.isChecked());
        intent.putExtra(RemindActivity.REMIND_INFO,mTvMemberName.getText());
        ((BaseActivity) mContext).startActivityForResult(intent, 1011);
    }

    /** 提醒选择结果 */
    public void setRemindResult(String result, int remindType) {
        mTvMemberName.setText(result);
        this.remindType = remindType;
    }

}
