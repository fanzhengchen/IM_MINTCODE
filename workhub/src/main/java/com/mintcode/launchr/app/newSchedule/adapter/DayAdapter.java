package com.mintcode.launchr.app.newSchedule.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.app.newSchedule.activity.ScheduleMainActivity;
import com.mintcode.launchr.app.newSchedule.activity.ScheduleOneDayActivity;
import com.mintcode.launchr.app.newSchedule.fragment.CalendarMonthFragment;
import com.mintcode.launchr.app.newSchedule.util.CreateCalendar;

import java.util.Calendar;

/**
 * Created by JulyYu on 2016/3/10.
 */
public class DayAdapter extends BaseAdapter{




    private int mYear;
    private int mMonth;
    private int itemCount;
    private int dayCount;
    private int firstDay;

    private int thisYear;
    private int thisMonth;
    private int thisDay;

    private int mColorWhite;
    private int mColorBlue;
    private int mColorGray;
    private int mColorBlack;
    private Context mContext;
    private Calendar todayStartTime;

    private int [] mDaySets;
    private int mItemHeight;

    public DayAdapter(Context context,int year,int month, int[] days,int height){
        this.mContext = context;
        mYear = year;
        mMonth = month;
        mDaySets = days;
        mItemHeight = height;
        mColorBlue = context.getResources().getColor(R.color.blue_launchr);
        mColorWhite = context.getResources().getColor(R.color.white);
        mColorGray = context.getResources().getColor(R.color.gray_launchr);
        mColorBlack = context.getResources().getColor(R.color.black);
        initDate();
    }

    public void initDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        thisYear = calendar.get(Calendar.YEAR);
        thisMonth = calendar.get(Calendar.MONTH) + 1;
        thisDay = calendar.get(Calendar.DAY_OF_MONTH);

        CreateCalendar createCalendar = new CreateCalendar();
        boolean IsLeap = createCalendar.JudgeLeapYear(mYear);
        firstDay = createCalendar.FristDay(mYear, mMonth);
        dayCount = createCalendar.MonthDays(IsLeap, mMonth);
        itemCount = createCalendar.getTheMonthCount(mYear, mMonth);

        todayStartTime = Calendar.getInstance();
        todayStartTime.setTimeInMillis(System.currentTimeMillis());
        todayStartTime.set(Calendar.HOUR_OF_DAY, 0);
        todayStartTime.set(Calendar.MINUTE, 0);
        todayStartTime.set(Calendar.SECOND, 0);
        todayStartTime.set(Calendar.MILLISECOND, 0);
    }

    public int getYear(){
        return mYear;
    }

    public int getMonth(){
        return mMonth;
    }

    public void changeData(int[] days){
        mDaySets = days;
        initDate();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDaySets.length;
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
        ViewHolder holder;
        gridOnitemClickListener clickListener = new gridOnitemClickListener(mDaySets[position]);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_month_day, null);
            holder = new ViewHolder();
            holder.tvDay = (TextView) convertView.findViewById(R.id.tv_grid_day);
            holder.llDay = (LinearLayout)convertView.findViewById(R.id.ll_grid_day);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
            layoutParams.height = mItemHeight;
            holder.llDay.setLayoutParams(layoutParams);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(mDaySets[position] != 0){
            holder.tvDay.setText(mDaySets[position]+"");
            holder.llDay.setOnClickListener(clickListener);
            holder.tvDay.setVisibility(View.VISIBLE);
        }else{
            holder.tvDay.setVisibility(View.INVISIBLE);
        }
        // 周末显示灰色
        if(position == 0 || position == 6){
            holder.tvDay.setTextColor(mColorGray);
        }else{
            holder.tvDay.setTextColor(mColorBlack);
        }
        // 当天日期显示高亮
        if ((mDaySets[position] == thisDay) && (mYear == thisYear)
                && (mMonth == thisMonth)) {
            holder.tvDay.setTextColor(mColorWhite);
            holder.tvDay.setBackgroundColor(mColorBlue);
        }
        return convertView;
    }

    public class ViewHolder {
        TextView tvDay;
        LinearLayout llDay;
    }

    class gridOnitemClickListener implements View.OnClickListener{

        private int position;

        public gridOnitemClickListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(mContext, mYear + "-"+ mMonth + "-" + this.position + "", Toast.LENGTH_SHORT).show();
//                    ((mContext) CalendarMonthFragment).showOnePopwindow();
            Intent i = new Intent(mContext, ScheduleOneDayActivity.class);
            Calendar time = Calendar.getInstance();
            time.set(mYear,mMonth - 1,position);
            i.putExtra(ScheduleOneDayActivity.TIME_KEY, time.getTimeInMillis());
            i.putExtra(ScheduleOneDayActivity.IS_MYSCHEDULE, ScheduleMainActivity.isMySchedule);
            i.putExtra(ScheduleOneDayActivity.OTHER_USER_ID, ScheduleMainActivity.mOtherUserId);
            mContext.startActivity(i);
        }
    }
}
