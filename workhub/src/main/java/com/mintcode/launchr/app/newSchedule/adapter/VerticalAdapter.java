package com.mintcode.launchr.app.newSchedule.adapter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mintcode.launchr.app.newSchedule.util.ScheduleEventUtil;
import com.mintcode.launchr.app.newSchedule.view.NoTouchListView;
import com.mintcode.launchr.pojo.entity.EventEntity;


public class VerticalAdapter extends PagerAdapter {



	private Context mContext;

	public static int mStartYear;

	public static final int YEAR_SIEZ = 10;


	private HashMap<String, List<EventEntity>> mEvent = new HashMap<>();

	public VerticalAdapter() {

	}
	public VerticalAdapter(Context context) {
		mContext = context;

		this.mContext = context;
		Calendar calendar = Calendar.getInstance();
		int thisYear = calendar.get(Calendar.YEAR);
		mStartYear = thisYear - YEAR_SIEZ / 2;

	}

	public void setEventSets(HashMap<String, List<EventEntity>> event){
		mEvent = event;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return YEAR_SIEZ * 12;
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
//		LinearLayout linaer = new LinearLayout(mContext);
//		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//		linaer.setLayoutParams(params);
//		linaer.setBackgroundColor(Color.BLACK);
		NoTouchListView listView = new NoTouchListView(mContext);
//		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//		listView.setLayoutParams(params);
		listView.setBackgroundColor(Color.WHITE);
		listView.setOnTouchListener(new ListViewViewMoveListener());
		int month = getMonthByPosition(position);
		int year = getYearByPosition(position);
		MonthAdapter adapter;
		if(mEvent != null){
			String key = ScheduleEventUtil.getKey(year,month);
			List<EventEntity> entities = mEvent.get(key);
			if(entities != null){
				adapter = new MonthAdapter(mContext,year,month,entities);
			}else{
				adapter = new MonthAdapter(mContext,year,month);
			}
		}else{
			adapter = new MonthAdapter(mContext,year,month);
		}
		listView.setAdapter(adapter);
		container.addView(listView);
		return listView;
	}
	class ListViewViewMoveListener implements View.OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			return true;
		}
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
//		Log.d(TAG, "destroyItem:" + position);
		container.removeView((View) object);
	}

	public static int getMonthByPosition(int position) {
		int month = (position + 1) % 12;
		if ((position + 1) % 12 == 0) {
			month = 12;
		}
		return month;
	}

	public static int getYearByPosition(int position) {
		int year = mStartYear + (position + 1) / 12;
		if (getMonthByPosition(position) == 12) {
			year -= 1;
		}
		return year;
	}
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

}
