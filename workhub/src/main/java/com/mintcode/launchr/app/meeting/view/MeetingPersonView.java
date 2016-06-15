package com.mintcode.launchr.app.meeting.view;

import java.util.List;

import com.mintcode.launchr.R;
import com.mintcode.launchr.app.meeting.activity.*;
import com.mintcode.launchr.pojo.entity.EventEntity;
import com.mintcode.launchr.util.TTDensityUtil;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class MeetingPersonView extends View{

	private Context mContext;
	private int mWidth;
	private int mHeight;
	String mHourStr;
	private TextPaint mTextPaint;
	private int mVisibleWidth;
	private int mVisibleHeight;
	private int mCellHeight;
	
	public MeetingPersonView(Context context) {
		super(context);
		init(context);
	}

	public MeetingPersonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MeetingPersonView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}
	private void init(Context context) {
		mContext = context;
		setWillNotDraw(false);
		mTextPaint = new TextPaint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(Color.BLACK);
		float fontSize = TTDensityUtil.dip2px(mContext, 15);
		mTextPaint.setTextSize(fontSize);
		mHourStr = mContext.getResources().getString(R.string.time_hour);
		mCellHeight = TTDensityUtil.dip2px(mContext, 50);
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed || mWidth < 0 || mHeight < 0) {
			Log.d("", "onLayout");
			mWidth = right - left;
			mHeight = bottom - top;
			if(mVisibleHeight <= 0){
				mVisibleHeight = mHeight;
			}
			int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
			mVisibleWidth = widthPixels - TTDensityUtil.dip2px(mContext, 100);
			refreshData();
		}
	}


	Rect mCentre = new Rect();
	private void refreshData() {
		Log.d("", "mWidth="+mWidth+",mHeight="+mHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(mPersonList == null){
			return;
		}
		
		for(int i = 0; i < mPersonList.size(); i++){
			String name = mPersonList.get(i);
			canvas.drawText(name, 10, (mCellHeight * i + mCellHeight*0.6f), mTextPaint);
		}
	}
	
	int oldX;
	 int oldY;
	private MeetingContentView mContentView;
	private List<String> mPersonList;
	 @Override
	 public boolean onTouchEvent(MotionEvent event) {
	     switch(event.getAction()) {
	         case MotionEvent.ACTION_DOWN:
	             oldX = (int) event.getX();
	             oldY = (int) event.getY();
	             break;
	         case MotionEvent.ACTION_MOVE:
	        	 float sx = getScrollX();
	        	 float sy = getScrollY();
	             int dx = (int) (oldX - event.getX());
	             int dy = (int) (oldY - event.getY());
	             Log.d("", "dx="+dx+",dy="+dy);
	           //限制上下滑动
	             if(sy + dy < 0){
	            	 dy = (int) (0-sy);
	             }else if(mVisibleHeight > mHeight){
	            	 dy = (int) (0 - sy);
	             }else if(sy + mVisibleHeight + dy >= mHeight){
	            	 dy = (int) (mHeight - mVisibleHeight - sy);
	             }
	             if(mContentView!= null){
	            	 mContentView.scrollBy(0, dy);
	             }
	             scrollBy(0, dy);
	             Log.d("", "sx="+sx+",sy="+sy);
	             oldX = (int) event.getX();
	             oldY = (int) event.getY();
	             break;
	         case MotionEvent.ACTION_UP:
	             break;
	         default:
	             break;
	     }
	     return true;
	 }

	public void setMeetingContentView(MeetingContentView contentView) {
		// TODO Auto-generated method stub
		mContentView = contentView;
	}

	public void setPersonList(List<String> personList) {
		mPersonList = personList;
	}
	
	
	
}