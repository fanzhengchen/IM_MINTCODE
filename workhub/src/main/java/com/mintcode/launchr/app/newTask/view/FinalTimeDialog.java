package com.mintcode.launchr.app.newTask.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mintcode.launchr.R;
import com.mintcode.launchr.pojo.entity.EventEntity;
import com.mintcode.launchr.timer.Timer;
import com.mintcode.launchr.timer.listener.OnTimerListener;
import com.mintcode.launchr.util.TimeFormatUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * create by Stephen He 2015/9/8
 */
public class FinalTimeDialog extends Dialog implements OnTimerListener,OnCheckedChangeListener {

	/** context */
	private Context mContext;
	@Bind(R.id.new_task_timer)
	protected Timer mTimer;
	@Bind(R.id.cb_is_allday)
	protected CheckBox mCbAllDay;
	/** 标题*/
	@Bind(R.id.tv_title)
	protected TextView mTvTitle;


	/** 时间*/
	private Calendar finalTime;
	/** 开始时间*/
	private Long mLongStart;
	/** 结束时间*/
	private Long mLongEnd;
	/** 标题*/
	private String mTitle;

	private int mIntTime = 1;

	public static final int START_TIME = 1;
	public static final int OVER_TIME = 2;

	private OnGetTimeListener mListenr;

	public FinalTimeDialog(Context context, String title) {
		super(context);
		mContext = context;
		this.mTitle = title;
		finalTime = Calendar.getInstance();
	}

	public FinalTimeDialog(Context context, int theme, String title) {
		super(context, theme);
		mContext = context;
		this.mTitle = title;
	}

	protected FinalTimeDialog(Context context, boolean cancelable,
							  OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		mContext = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		LayoutInflater inflat = LayoutInflater.from(mContext);
		View view = inflat.inflate(R.layout.dialog_new_task_finaltime, null);
		setContentView(view);
		ButterKnife.bind(this,view);
		initDate();
	}

	private void initDate() {
		//设置页面
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
		//设置标题
		mTvTitle.setText(this.mTitle);
		//初始化时间
		finalTime = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		finalTime.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		//设置时间控件
		mTimer.setOnTimerListener(this);
		mTimer.setStyle(Timer.SINGLE_TIME_All_DAY);
		mTimer.setBeginTime(finalTime.getTimeInMillis());
		mTimer.setNowCalendarTime(finalTime.getTimeInMillis());
		//设置全天按钮
		mCbAllDay.setOnCheckedChangeListener(this);
		mCbAllDay.setChecked(true);
	}


	/** 设置开始和结束时间*/
	public void setTime(Long beginMill, Long endMill) {
		mTimer.setShowTime(beginMill, endMill);
	}
	public void setCustomTitle(String title){
		this.mTitle = title;
		mTvTitle.setText(title);
	}
	/** 设置开始时间*/
	public void setStartTime(Long now) {
		if(now != null){
			finalTime.setTimeInMillis(now);
			mTimer.setBeginTime(now);
			mTimer.setNowCalendarTime(now);
		}
		mIntTime = START_TIME;
	}
	/** 设置结束时间*/
	public void setEndTime(Long now) {
		if(now != null){
			finalTime.setTimeInMillis(now);
			mTimer.setEndTime(now);
			mTimer.setNowCalendarTime(now);
		}
		mIntTime = OVER_TIME;
	}
	/*** 获取时间*/
	public Calendar getTime() {
		return finalTime;
	}

	public void setAllDay(boolean isChecked){
		if(isChecked){
			mTimer.setStyle(Timer.SINGLE_TIME_All_DAY);
		}else{
			mTimer.setStyle(Timer.SINGLE_TIME_FIVE_POSITION);
			finalTime.setTimeInMillis(mTimer.getNowCalendarTime());
		}
		mCbAllDay.setChecked(isChecked);
	}
	private int isAllDay(){
		if(mCbAllDay.isChecked()){
			return 1;
		}else{
			return 0;
		}
	}

	public void setTime(int allDay,Long startTime,Long finaTime){

//		SimpleDateFormat dateFormat;
//		mLStartTime = startTime;
//		mLFinalTime = finaTime;
//		if (allDay == 1) {
//			mStartTimeAllDay = 1;
//			mFinalTimeAllDay = 1;
//			dateFormat = new SimpleDateFormat("MM/dd");
//			if(mLFinalTime != null){
//				mTvFinalTimeText.setText(dateFormat.format(mLFinalTime));
//			}else{
//				mTvFinalTimeText.setText(getString(R.string.no_set_time));
//			}
//			if(mLStartTime != null){
//				mTvStartTime.setText(dateFormat.format(mLStartTime));
//			}else{
//				mTvStartTime.setText(getString(R.string.no_set_time));
//			}
//		}else{
//			mStartTimeAllDay = 0;
//			mFinalTimeAllDay = 0;
//			dateFormat = new SimpleDateFormat("MM/dd HH:mm");
//			if(mLFinalTime != null){
//				mTvFinalTimeText.setText(dateFormat.format(mLFinalTime));
//			}else{
//				mTvFinalTimeText.setText(getString(R.string.no_set_time));
//			}
//			if(mLStartTime != null){
//				mTvStartTime.setText(dateFormat.format(mLStartTime));
//			}else{
//				mTvStartTime.setText(getString(R.string.no_set_time));
//			}
//		}
	}
	/** 取消*/
	@OnClick(R.id.dialog_finaltime_cancel)
	protected void cancelDismiss(){
		this.dismiss();
	}
	/** 确认时间*/
	@OnClick(R.id.dialog_finaltime_ok)
	protected void saveTime(){
		String strTime = TimeFormatUtil.setRemindTimeText(finalTime.getTimeInMillis());
		if(mLongEnd != null  && mLongEnd <= mLongStart){
			Toast.makeText(mContext, mContext.getString(R.string.end_time_must_not_be_later_than_begin_time), Toast.LENGTH_SHORT).show();
			return;
		}
		if(mListenr != null){
			mListenr.getLongTime(mIntTime,finalTime.getTimeInMillis(),strTime,isAllDay());
		}
		this.dismiss();
	}
	/** 设置为无时间*/
	@OnClick(R.id.tv_one_day)
	protected void nullTime(){
		if(mListenr != null){
			mListenr.getLongTime(mIntTime,null,mContext.getString(R.string.no_set_time),isAllDay());
		}
		this.dismiss();
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			mTimer.setStyle(Timer.SINGLE_TIME_All_DAY);
		}else{
			mTimer.setStyle(Timer.SINGLE_TIME_FIVE_POSITION);
		}
	}
	@Override
	public void OnTimeChangeListenner(View view, int year, int month, int day,
									  int hour, int minute, int type, boolean isEnd) {
		finalTime.set(year, month - 1 , day, hour, minute, 0);
		mTimer.setBeginTime(finalTime.getTimeInMillis());
		if(mIntTime == START_TIME){
			mLongStart = finalTime.getTimeInMillis();
		}else if(mIntTime == OVER_TIME){
			mLongEnd = finalTime.getTimeInMillis();
		}
	}


	public void setGetTimeListener(OnGetTimeListener listener){
		mListenr = listener;
	}
	public interface OnGetTimeListener{
		void getLongTime(int timeFlag,Long time,String strTime,int allDay);
	}
}
