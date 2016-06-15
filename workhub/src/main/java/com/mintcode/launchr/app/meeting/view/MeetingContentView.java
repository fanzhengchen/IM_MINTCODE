package com.mintcode.launchr.app.meeting.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.zip.Inflater;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.pojo.entity.EventEntity;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.TTDensityUtil;
import com.mintcode.launchr.util.TimeFormatUtil;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MeetingContentView extends ViewGroup{

	private Context mContext;
	private int mWidth;
	private int mHeight;
	String mHourStr;
	private TextPaint mTextPaint;
	private Paint mRedPaint;
	private Paint mGrayPaint;
	private Paint mBluePaint;
	private int mVisibleWidth;
	private int mVisibleHeight;
	private int mCellHeight;
	
	public MeetingContentView(Context context) {
		super(context);
		init(context);
	}

	public MeetingContentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MeetingContentView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}
	private void init(Context context) {
		mContext = context;
		setWillNotDraw(false);
		mTextPaint = new TextPaint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(Color.parseColor("#848484"));
		mTextPaint.setTextSize(24);
		
		mRedPaint = new TextPaint();
		mRedPaint.setColor(Color.parseColor("#ff3366"));
		mRedPaint.setStrokeWidth(1);
		
		mGrayPaint = new TextPaint();
		mGrayPaint.setColor(Color.parseColor("#cccccc"));
		mGrayPaint.setStrokeWidth(1);
		
		mBluePaint = new TextPaint();
		mBluePaint.setColor(Color.parseColor("#0099ff"));
		mBluePaint.setStrokeWidth(1);
		
		mHourStr = mContext.getResources().getString(R.string.time_hour);
		mCellHeight = TTDensityUtil.dip2px(mContext, 50);
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		if (changed || mWidth < 0 || mHeight < 0) {
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
	private List<EventEntity> mDatas;
	private float mHourCellWidth;
	private ArrayList<String> mPersonList;
	private ArrayList<String> mPersonTrueNameList;
	private HashMap<String, List<EventEntity>> mEventMap;
	private HashMap<String, String> mNameKey;
	private void refreshData() {
		mHourCellWidth = mWidth / 24f;
	}

	HashMap<Rect, List<EventEntity>> mRectEventMap = new HashMap<Rect, List<EventEntity>>();
	@Override
	protected void onDraw(Canvas canvas) {
		if(mPersonList == null || mPersonList.size() <= 0){
			return;
		}
		mRectEventMap.clear();
		for(int i = 0; i <= 24; i++){
			canvas.drawLine(mHourCellWidth*i + 1, 0, mHourCellWidth*i + 1, mHeight, mTextPaint);
		}
		//遍历所有人员的日程事件
		for(int i = 0; i < mPersonList.size(); i++){
			canvas.drawLine(1, mCellHeight * i, mWidth, mCellHeight * i, mTextPaint);
			//当前人员的日程事件
			List<EventEntity> list = mEventMap.get(mPersonList.get(i));
			//遍历当前人员当天的日程事件
			for(int j = 0;j < list.size(); j++){
				EventEntity item = list.get(j);
				Rect rect = new Rect();
				rect.top = mCellHeight * i + 10;
				rect.bottom = mCellHeight * (i+1) - 10;
				rect.left = (int) getConflictX(item.getStartTime());
				rect.right = (int) getConflictX(item.getEndTime());
				List<EventEntity> eventMap = mRectEventMap.get(rect);
				if(eventMap == null){
					eventMap = new ArrayList<>();
					eventMap.add(item);
					mRectEventMap.put(rect, eventMap);
				}else{
					eventMap.add(item);
				}
				if(item.getStartTime() <= mNow && mNow <= item.getEndTime()){
					canvas.drawRect(rect, mBluePaint);
				}else{
					canvas.drawRect(rect, mGrayPaint);
				}
			}
		}
		canvas.drawLine(1, mHeight, mWidth, mHeight, mTextPaint);
		float nowX = getConflictX(mNow);
		canvas.drawLine(nowX, 0, nowX, mHeight, mRedPaint);
		super.onDraw(canvas);
	}
	public void setEventDatas(List<EventEntity> datas, ArrayList<String> uname, ArrayList<String> tname,HashMap<String, String> nameKey){
		if(datas == null){
			return;
		}
		mDatas = datas;
		
		mPersonList = uname;
		mPersonTrueNameList = tname;
		mNameKey = nameKey;
		mEventMap = new HashMap<String, List<EventEntity>>();
		mRectEventMap.clear();
		for(int i = 0;i < uname.size(); i++){
			List<EventEntity> list = new ArrayList<EventEntity>();
			mEventMap.put(uname.get(i), list);
		}
		for(int i = 0;i < datas.size(); i++){
			EventEntity item = datas.get(i);
			String name = item.getCreateUser();
			if(!mEventMap.containsKey(name)){
				mPersonList.add(name);
				List<EventEntity> list = new ArrayList<EventEntity>();
				mEventMap.put(name, list);
			}
			List<EventEntity> mapList = mEventMap.get(name);
			mapList.add(item);
		}

		final int ViewHeight = mPersonList.size() * TTDensityUtil.dip2px(mContext, 50);
		
		post(new Runnable() {
			
			@Override
			public void run() {
				LayoutParams layoutParams = MeetingContentView.this.getLayoutParams();
				layoutParams.height = ViewHeight;
				MeetingContentView.this.setLayoutParams(layoutParams);
				
				if(mPersonView != null){
					LayoutParams personLayoutParams = mPersonView.getLayoutParams();
					personLayoutParams.height = ViewHeight;
					mPersonView.setLayoutParams(personLayoutParams);
					mPersonView.setPersonList(mPersonTrueNameList);
				}
				
			}
		});
	}
	
	int oldX;
	 int oldY;
	private MeetingDateView mDateView;
	private MeetingPersonView mPersonView;
	private long mNow;
	private long mDayBegin;
	private long mDayEnd;
	private boolean mIsMove;
	private int mTouchDownX;
	private int mTouchDownY;
	 @Override
	 public boolean onTouchEvent(MotionEvent event) {
	     switch(event.getAction()) {
	         case MotionEvent.ACTION_DOWN:
	        	 mIsMove = false;
	             oldX = (int) event.getX();
	             oldY = (int) event.getY();
	             mTouchDownX = oldX;
	             mTouchDownY = oldY;
	             break;
	         case MotionEvent.ACTION_MOVE:
	        	 float x = event.getX();
	        	 float y = event.getY();
	        	 if(!mIsMove){
	        		 if(Math.abs(x - mTouchDownX) > 50 || Math.abs(y - mTouchDownY) > 50){
	        			 mIsMove = true;
	        		 }
	        	 }
	        	 float sx = getScrollX();
	        	 float sy = getScrollY();
	             int dx = (int) (oldX - x);
	             int dy = (int) (oldY - y);
	             Log.d("", "dx="+dx+",dy="+dy);
	             
	             //限制左右滑动
	             if(sx + dx < 0){
	            	 dx = (int) (0-sx);
	             }else if(sx + mVisibleWidth + dx >= mWidth){
	            	 dx = (int) (mWidth - mVisibleWidth - sx);
	             }
	             
	             //限制上下滑动
	             if(sy + dy < 0){
	            	 dy = (int) (0-sy);
	             }else if(mVisibleHeight > mHeight){
	            	 dy = (int) (0 - sy);
	             }else if(sy + mVisibleHeight + dy >= mHeight){
	            	 dy = (int) (mHeight - mVisibleHeight - sy);
	             }
	             
	             if(mDateView!= null){
	            	 mDateView.scrollBy(dx, 0);
	             }
	             if(mPersonView != null){
	            	 mPersonView.scrollBy(0, dy);
	             }
	             scrollBy(dx, dy);
	             Log.d("", "sx="+sx+",sy="+sy);
	             oldX = (int) event.getX();
	             oldY = (int) event.getY();
	             break;
	         case MotionEvent.ACTION_UP:
	        	 if(!mIsMove){
		        	 int ux = ((int) event.getX()) + getScrollX();
		             int uy = ((int) event.getY()) + getScrollY();
		             List<EventEntity> eventEntitys = getEventEntityByPoint(ux, uy);
		             if(eventEntitys != null && eventEntitys.size() > 0){
		            	 showDetail(eventEntitys);
		            	 return false;
		             }
	        	 }
	             break;
	         default:
	             break;
	     }
	     return true;
	 }

	public void setMeetingDateView(MeetingDateView dateView) {
		mDateView = dateView;
	}
	public void setMeetingPersonView(MeetingPersonView personView) {
		mPersonView = personView;
	}
	
	public void setNow(long now){
		mDateView.setNow(now);
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
	/** 获取事件详情*/
	private List<EventEntity> getEventEntityByPoint(int x,int y){
		Set<Rect> keySet = mRectEventMap.keySet();
		List<EventEntity> events = new ArrayList<>();
		for (Rect rect : keySet) {
			if(rect.contains(x, y)){
				events.addAll(mRectEventMap.get(rect));
			}
		}
		return events;
	}

	private void showDetail(List<EventEntity> events){
		Dialog dialog  = new Dialog(mContext, R.style.my_dialog);
		dialog.show();
		Window window = dialog.getWindow();
		dialog.getWindow().setContentView(R.layout.dialog_show_meeting_sc);
		TextView tvName = (TextView) window.findViewById(R.id.tv_name);
		String schedule = mContext.getString(R.string.who_schedule);
		String name = events.get(0).getCreateUser();
		name = mNameKey.get(name);
		tvName.setText(name + schedule);
		ListView lvEvents = (ListView) window.findViewById(R.id.lv_events);
		EventAdapter adapter = new EventAdapter(mContext,events);
		lvEvents.setAdapter(adapter);
	}

	public class EventAdapter extends BaseAdapter{
		private  Context mContext;
		private List<EventEntity> mEvents;

		public  EventAdapter(Context context,List<EventEntity> events){

			mContext = context;
			mEvents = events;
		}

		@Override
		public int getCount() {
			return mEvents == null?0:mEvents.size();
		}

		@Override
		public Object getItem(int position) {
			return mEvents == null?0:mEvents.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HolderView holderView = new HolderView();
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_person_event_detail,null);
				holderView.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
				holderView.tvPlace = (TextView) convertView.findViewById(R.id.tv_place);
				holderView.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
				convertView.setTag(holderView);
			}else{
				holderView = (HolderView) convertView.getTag();
			}
			EventEntity entity = mEvents.get(position);
			String title = entity.getTitle();
			String place = entity.getPlace();
				if(!TextUtils.isEmpty(title)){
					holderView.tvTitle.setText(title);
				}else{
					holderView.tvTitle.setVisibility(View.GONE);
				}
				if(!TextUtils.isEmpty(place)){
					holderView.tvPlace.setText(place);
				}else{
					holderView.tvPlace.setVisibility(View.GONE);
				}
			holderView.tvTime.setText(TimeFormatUtil.getStartEndTimeFormat(entity.getStartTime(), entity.getEndTime()));
				return convertView;
			}
		public class HolderView{
			protected TextView tvTitle;
			protected TextView tvPlace;
			protected TextView tvTime;
		}
	}
}