package com.mintcode.launchr.app.meeting.view;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import com.mintcode.launchr.pojo.entity.EventEntity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public class EventConflictView extends View{

	private int mWidth;
	private int mHeight;
	
	private List<EventEntity> mDatas;
	
	public EventConflictView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		init();
	}


	// 在构造器中初始化
	public EventConflictView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		setWillNotDraw(false);
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(Color.BLACK);
		mTextPaint.setTextSize(12);
		
		
		mGrayPaint = new Paint();
		mGrayPaint.setColor(Color.parseColor("#ffe9e9e9"));
		mGrayPaint.setStrokeWidth(1);
		mGrayPaint.setStyle(Paint.Style.FILL);
		
		mRedPaint = new Paint();
		mRedPaint.setColor(Color.RED);
		mRedPaint.setStrokeWidth(1);
		mRedPaint.setStyle(Paint.Style.FILL);
		
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed || mWidth < 0 || mHeight < 0) {
			mWidth = right - left;
			mHeight = bottom - top;
			refreshData();
		}
	}


	Rect mCenter = new Rect();
	private void refreshData() {
		mCenter.set(0, 0, mWidth, mHeight);
		
	}

	final int right = 0;
	final int up = 1;
	final int left = 2;
	final int down = 3;
	
	int direct = right;
	int cell = 5;
	int moveStep = 1;
	int moveCount = 2;
	private Paint mTextPaint;
	private Paint mGrayPaint;
	private Paint mRedPaint;
	@Override
	protected void onDraw(Canvas canvas) {

		if(mDayEnd <= 0 || mDayBegin <= 0 || mDayEnd < mDayBegin){
			//数据未初始化或者数据错误
			canvas.drawCircle(10, mCenter.centerY(), 10, mRedPaint);
			canvas.drawLine(10, mCenter.centerY(), mWidth, mCenter.centerY(), mRedPaint);
			return;
		}
		if(mDatas == null || mDatas.size() <= 0){
			canvas.drawCircle(10, mCenter.centerY(), 10, mRedPaint);
			canvas.drawLine(10, mCenter.centerY(), mWidth, mCenter.centerY(), mRedPaint);
			return;
		}
		super.onDraw(canvas);

		for(int i = 0; i < mDatas.size(); i++){
			EventEntity item = mDatas.get(i);
			long itemStart = item.getStartTime();
			long itemEnd = item.getEndTime();
			int startY = getConflictY(itemStart);
			int endY = getConflictY(itemEnd);
			canvas.drawRect(mWidth/2, startY, mWidth, endY, mGrayPaint);
		}
		canvas.drawCircle(10, mCenter.centerY(), 10, mRedPaint);
		canvas.drawLine(10, mCenter.centerY(), mWidth, mCenter.centerY(), mRedPaint);
		

	}
	
	
	private SmoothRollRunnable mSmoothRollRunnable;
	private static final int ANIM_DURATION = 200;
	private long mProgressValue = 0l;
	public void setProgress(long from, long to) {
		mProgressValue = from;
		smoothRollTo(to, ANIM_DURATION);
	}
	
	protected void smoothRollTo(long to, long duration){
		if(null != mSmoothRollRunnable){
			mSmoothRollRunnable.stop();
		}
		if(mProgressValue != to){
			mSmoothRollRunnable = new SmoothRollRunnable(mProgressValue, to, duration);
			post(mSmoothRollRunnable);
		}
	}
	
	
	class SmoothRollRunnable implements Runnable {
		static final int ANIMATION_DELAY = 10;
		
		private final Interpolator mInterpolator;
		private final long mFrom;
		private final long mTo;
		private final long mDuration;
		
		private boolean mContinueRunning = true;
		private long mStartTime = -1;
		
		public SmoothRollRunnable(long from, long to, long duration){
			mFrom = from;
			mTo = to;
			mInterpolator = new DecelerateInterpolator();
			mDuration = duration;
		}
		@Override
		public void run() {
			
			if (mStartTime == -1) {
				mStartTime = System.currentTimeMillis();
			} else {
				long duration = mDuration;
				if(duration == 0){
					duration = 1;
				}
				long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime)) / duration;
				normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

				final double deltaY = (mFrom - mTo)
						* mInterpolator.getInterpolation(normalizedTime / 1000f);
//				Log.d("", "mInterpolator.getInterpolation(normalizedTime / 1000f))="+(mInterpolator.getInterpolation(normalizedTime / 1000f))+",deltaY="+deltaY);
				mProgressValue = (long) (mFrom - deltaY);
				//todo:数值变化后，你要做什么？
				mAnimNow = mProgressValue;
				invalidate();
			}
			if (mContinueRunning && mTo != mProgressValue) {
				postDelayed(this, ANIMATION_DELAY);
			}
		}
		
		public double stop() {
			mContinueRunning = false;
			removeCallbacks(this);
//			mProgressValue = mTo;
//			invalidate();
			return mProgressValue;
		}
	}


	long mDayBegin;
	long mDayEnd;
	long mAnimNow;
	long mNow;
	public void setDay(Calendar mCalendar) {
		mAnimNow = mCalendar.getTimeInMillis();
		mNow = mCalendar.getTimeInMillis();
		int year = mCalendar.get(Calendar.YEAR);
		int dayOfYear = mCalendar.get(Calendar.DAY_OF_YEAR);
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.DAY_OF_YEAR, dayOfYear);
		mDayBegin = c.getTimeInMillis();
		mDayEnd = mDayBegin + 24 * 3600 * 1000;
	}
	
	private int getConflictY(long time){
		long deltaTime = time - mAnimNow;
		float deltaY = ((float)deltaTime / (mDayEnd - mDayBegin)) * (mHeight * 4f);
		int y = (int) (mCenter.centerY() + deltaY);
		return y;
	}

	public void setDatas(List<EventEntity> mDatas) {
		this.mDatas = mDatas;
		invalidate();
	}
	
	public void setNowTime(long now){
//		mNow = now;
		mNow = now;
		setProgress(mAnimNow, now);
//		invalidate();
	}
	
	HashSet<String> mConflictMember = new HashSet<String>();
	public int getConflictCount(){
		if(mDatas == null || mDatas.size() <= 0){
			return 0;
		}
		mConflictMember.clear();
		for(int i = 0; i < mDatas.size(); i++){
			EventEntity item = mDatas.get(i);
			long itemStart = item.getStartTime();
			long itemEnd = item.getEndTime();
			if(itemStart <= mNow && mNow <= itemEnd){
				mConflictMember.add(item.getCreateUser());
			}
		}
		return mConflictMember.size();
	}
}
