package com.mintcode.launchr.app.newSchedule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.timer.Timer;

public class CandidateTimeSelectView extends RelativeLayout  {
	/**
	 * 根视图
	 */
	private View mView;
	/**
	 * 时间选择器可缩头
	 */
	private RelativeLayout mRlDefulatTimeSelectShrink;
	/**
	 * 时间选择器名称
	 */
	private TextView mTvTimeTitle;
	/**
	 * 时间选择器的时间显示
	 */
	private TextView mTvGetTime;
	/**
	 * 认时间选择器的删除按钮
	 */
	private ImageButton mIvTimeIcon;
	/**
	 * 时间选择控件
	 */
	private Timer mTimer;
	private long mTodayTime = 0;
	public CandidateTimeSelectView(Context context,long time, AttributeSet attrs) {
		super(context, attrs);
		mView = LayoutInflater.from(context).inflate(R.layout.view_select_time_take_shrink, null);
		mTodayTime = time;
		initView();
		addView(mView);
	}

	private void initView() {
		
		mIvTimeIcon = (ImageButton)mView.findViewById(R.id.ibn_schedule_time_select_delect);
		mTvTimeTitle = (TextView)mView.findViewById(R.id.tv_schedule_time_title);
		mTvGetTime = (TextView)mView.findViewById(R.id.tv_schedule_time_get_title);
		mRlDefulatTimeSelectShrink = (RelativeLayout)mView.findViewById(R.id.rl_schedule_time_select_null);
		mTimer = (Timer)mView.findViewById(R.id.timer);
		mTimer.setConflictViewDisplay(false);
		mTimer.setTimeBeforeNow(false);
		if(mTodayTime != 0){
			mTimer.setBeginTime(mTodayTime);
		}
//		mIvTimeIcon.setOnClickListener(this);
	}	
	
	public ImageButton getTimeIcon(){
		return mIvTimeIcon;
	}
	public Timer getTimer(){
		return mTimer;
	}
	public RelativeLayout getTimeSelectShrink(){
		return mRlDefulatTimeSelectShrink;
	}
	public TextView getTimeTitle(){
		return mTvTimeTitle;
	}
	public TextView getTime(){
		return mTvGetTime;
	}
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.ibn_schedule_time_select_delect:
//			
//			break;
//
//		default:
//			break;
//		}
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
