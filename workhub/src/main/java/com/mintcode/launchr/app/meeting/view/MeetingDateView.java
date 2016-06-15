package com.mintcode.launchr.app.meeting.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.mintcode.launchr.R;
import com.mintcode.launchr.util.TTDensityUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class MeetingDateView extends View{

	private Context mContext;
	private int mWidth;
	private int mHeight;
	String mHourStr;
	private TextPaint mTextPaint;
	private int mVisibleWidth;
	
	public MeetingDateView(Context context) {
		super(context);
		init(context);
	}

	public MeetingDateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MeetingDateView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}
	private void init(Context context) {
		mContext = context;
		setWillNotDraw(false);
		mTextPaint = new TextPaint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(Color.BLACK);
		mTextPaint.setTextSize(TTDensityUtil.dip2px(mContext, 15));
		
		mRedPaint = new TextPaint();
		mRedPaint.setAntiAlias(true);
		mRedPaint.setColor(Color.RED);
		mRedPaint.setTextSize(TTDensityUtil.dip2px(mContext, 13));
		
		mHourStr = mContext.getResources().getString(R.string.time_hour);
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed || mWidth < 0 || mHeight < 0) {
			Log.d("", "onLayout");
			mWidth = right - left;
			mHeight = bottom - top;
			int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
			mVisibleWidth = widthPixels - TTDensityUtil.dip2px(mContext, 100);
			refreshData();
		}
	}


	Rect mCentre = new Rect();
	private TextPaint mRedPaint;
	private void refreshData() {
		Log.d("", "mWidth="+mWidth+",mHeight="+mHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for(int i = 0; i <= 24; i++){
			canvas.drawText(i+mHourStr, mWidth * i / 24f + 1, mHeight - 5, mTextPaint);
		}
		
		float nowX = getConflictX(mNow);
		
		String time = getCurrentTime(nowX);
		float len = getTextWidth(time, mRedPaint);
		float height = getTextHeight(mRedPaint) / 2 + dip2px(2);
		if(nowX - len < 0){   //小球上边的时间，在最左边，最右边时，时间会被隐藏，要判断一下
		    canvas.drawText(time, 0, height, mRedPaint);
		}else if(nowX + len > mWidth){
			canvas.drawText(time, mWidth-len * 2, height, mRedPaint);
		}else{
			canvas.drawText(time, nowX-len, height, mRedPaint);
		}
		canvas.drawCircle(nowX, getTextHeight(mRedPaint) + dip2px(2), dip2px(4), mRedPaint);
		
		canvas.drawLine(nowX, getTextHeight(mRedPaint) + dip2px(5), nowX, mHeight, mRedPaint);
		//TODO:还有一个到前的时间没画
	}
	
	//得到当前时间
	public String getCurrentTime(float nowX){	
		Date date = new Date(mNow); 
		SimpleDateFormat aFormat = new SimpleDateFormat("HH:mm");
		return aFormat.format(date);
	}
	
	// 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	public int dip2px(float dpValue) {
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	
	//得到文字的宽度的一半
	public float getTextWidth(String str, Paint paint){
		TextPaint text = (TextPaint) paint;
		return text.measureText(str) / 2;
	}
	
	//得到文字的高度
	public float getTextHeight(Paint paint){
		FontMetrics fontMetrics = paint.getFontMetrics();
		return fontMetrics.bottom-fontMetrics.top;
	}
	
	int oldX;
	 int oldY;
	private MeetingContentView mContentView;
	private long mDayBegin;
	private long mDayEnd;
	private long mNow;
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
	             if(sx + dx < 0){
	            	 dx = (int) (0-sx);
	             }
	             if(sx + mVisibleWidth + dx >= mWidth){
	            	 dx = (int) (mWidth - mVisibleWidth - sx);
	             }
	             if(mContentView!= null){
	            	 mContentView.scrollBy(dx, 0);
	             }
	             scrollBy(dx,0);
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
		mContentView = contentView;
	}
	
	public void setNow(long now){
		if(now == 0){
			now = System.currentTimeMillis();
		}
		mNow = now;
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTimeInMillis(now);
		int year = nowCalendar.get(Calendar.YEAR);
		int dayOfYear = nowCalendar.get(Calendar.DAY_OF_YEAR);
		nowCalendar.clear();
		nowCalendar.set(Calendar.YEAR, year);
		nowCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
		mDayBegin = nowCalendar.getTimeInMillis();
		mDayEnd = mDayBegin + 24 * 3600 * 1000;
	}
	public float getConflictX(long time){
		long deltaTime = time - mDayBegin;
		float y = ((float)deltaTime / (mDayEnd - mDayBegin)) * (mWidth);
		return y;
	}
	
}