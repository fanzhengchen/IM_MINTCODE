package com.mintcode.launchr.app.newSchedule.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mintcode.launchr.R;
import com.mintcode.launchr.app.newSchedule.util.CreateCalendar;
import com.mintcode.launchr.app.newSchedule.view.NoTouchGridView;
import com.mintcode.launchr.app.newSchedule.view.ScheduleEventView;
import com.mintcode.launchr.pojo.entity.EventEntity;
import com.mintcode.launchr.util.TTDensityUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by JulyYu on 2016/3/10.
 */
public class MonthAdapter extends BaseAdapter{


    private Context mContext;

    private int mYear;
    private int mMonth;

    /** 坐标总数*/
    private int mItemCount;
    /** 一个月的日期*/
    private int mDayCount;
    /** 一个月第一天的坐标*/
    private int mFirstDay;
    /** 今年*/
    private int mThisYear;
    /** 今月*/
    private int mThisMonth;
    /** 今天*/
    private int mThisDay;
    /** 父高度*/
    private int mParentHeight;

    private Calendar mTodayStartTime;

    private int mColorBlue;

    private int num = 1;

    /** 一月日期数组*/
    private int [][] mDaySet;

    private List<EventEntity> mEventSets = new ArrayList<>();

    private int mDisplayHeight;

    public MonthAdapter(Context context){
        mContext = context;
    }

    public MonthAdapter(Context context, int year, int month){
        mContext = context;
        this.mYear = year;
        this.mMonth = month;
        mColorBlue = context.getResources().getColor(R.color.blue_launchr);
        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
        int displayHeight = mDisplayMetrics.heightPixels;
        mDisplayHeight = displayHeight - TTDensityUtil.dip2px(context,160);
        initDate();
    }

    public MonthAdapter(Context context, int year, int month,List<EventEntity> eventEntities){
        mContext = context;
        this.mYear = year;
        this.mMonth = month;
        mColorBlue = context.getResources().getColor(R.color.blue_launchr);
        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
        int displayHeight = mDisplayMetrics.heightPixels;
        mDisplayHeight = displayHeight - TTDensityUtil.dip2px(context,160);
        this.mEventSets = eventEntities;
        initDate();
    }

    public void setEventSets(List<EventEntity> eventEntities){
        this.mEventSets = eventEntities;
        notifyDataSetChanged();
    }

    public void setData(int year, int month,List<EventEntity> eventEntities){

        this.mYear = year;
        this.mMonth = month;
        mColorBlue = mContext.getResources().getColor(R.color.blue_launchr);
        DisplayMetrics mDisplayMetrics = mContext.getResources().getDisplayMetrics();
        int displayHeight = mDisplayMetrics.heightPixels;
        mDisplayHeight = displayHeight - TTDensityUtil.dip2px(mContext,160);
        if(eventEntities != null){
            this.mEventSets = eventEntities;
        }
        initDate();
        notifyDataSetChanged();
    }

    public List<EventEntity> getEventSets(){
        return  this.mEventSets;
    }

    public void initDate() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        mThisYear = calendar.get(Calendar.YEAR);
        mThisMonth = calendar.get(Calendar.MONTH) + 1;
        mThisDay = calendar.get(Calendar.DAY_OF_MONTH);

        CreateCalendar createCalendar = new CreateCalendar();
        boolean IsLeap = createCalendar.JudgeLeapYear(mYear);
        mFirstDay = createCalendar.FristDay(mYear, mMonth);
        mDayCount = createCalendar.MonthDays(IsLeap, mMonth);
        mItemCount = createCalendar.getTheMonthCount(mYear, mMonth);

        mTodayStartTime = Calendar.getInstance();
        mTodayStartTime.setTimeInMillis(System.currentTimeMillis());
        mTodayStartTime.set(Calendar.HOUR_OF_DAY, 0);
        mTodayStartTime.set(Calendar.MINUTE, 0);
        mTodayStartTime.set(Calendar.SECOND, 0);
        mTodayStartTime.set(Calendar.MILLISECOND, 0);

        mDaySet = new int[mItemCount/7][7];

        for(int i = 0;i < mItemCount/7; i++){
            if(i == 0){
                for(int j = mFirstDay - 1; j < 7;j++){
                    mDaySet[0][j] = num;
                    num++;
                }
            }else{
                for(int j = 0;j < 7; j++){
                    if(num <= mDayCount){
                        mDaySet[i][j] = num;
                        num++;
                    }else{
                        mDaySet[i][j] = 0;
                    }
                }
            }
        }
    }
    public int getYear(){
        return mYear;
    }

    public int getMonth(){
        return mMonth;
    }

    @Override
    public int getCount() {
        return mItemCount / 7;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HolderView view;
        if(convertView == null){
            view = new HolderView();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_month_week_event,null);
            view.gridView = (NoTouchGridView)convertView.findViewById(R.id.gv_calendar_week);
            view.eventView = (ScheduleEventView)convertView.findViewById(R.id.view_week_event);
            view.rlLayout = (RelativeLayout)convertView.findViewById(R.id.rl_month_view);
            DayAdapter dayAdapter = new DayAdapter(mContext,mYear,mMonth,mDaySet[position],mDisplayHeight / (mItemCount / 7));
            view.gridView.setAdapter(dayAdapter);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
            layoutParams.height = mDisplayHeight / (mItemCount / 7);
            view.rlLayout.setLayoutParams(layoutParams);
            convertView.setTag(view);
        }else{
            view = (HolderView)convertView.getTag();
            DayAdapter dayAdapter = (DayAdapter)view.gridView.getAdapter();
            dayAdapter.changeData(mDaySet[position]);
        }
        view.eventView.setEventData(mYear,mMonth,mDaySet[position],mEventSets,mDisplayHeight / (mItemCount / 7));
        return convertView;
    }

    class HolderView{
        RelativeLayout rlLayout;
        NoTouchGridView gridView;
        ScheduleEventView eventView;
    }

    class GridViewDayListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(mContext,position + "--",Toast.LENGTH_SHORT).show();
        }
    }

    class GridViewMoveListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }

}
