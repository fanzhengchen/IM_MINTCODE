package com.mintcode.launchr.util;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.launchr.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by JulyYu on 2016/5/17.
 */
public class TimeFormatUtil {


    /**
     * 将long型时间转化成可以显示的string 显示时间 小时：分钟
     */
    public static String formatTime(long time) {
        Calendar ct = Calendar.getInstance();
        ct.setTimeInMillis(time);
        SimpleDateFormat sf = null;
        String timeStr = null;
        sf = new SimpleDateFormat("HH:mm");
        timeStr = sf.format(new Date(time));

        return timeStr;
    }
    /**
     * 将long型时间转化成可以显示的string 与当前时间的差
     */
     public static String formatNowTime(long time) {
        Calendar ct = Calendar.getInstance();
        ct.setTimeInMillis(time);
        int day = ct.get(Calendar.DAY_OF_YEAR);
        int year = ct.get(Calendar.YEAR);
        ct.setTimeInMillis(System.currentTimeMillis());
        int now_day = ct.get(Calendar.DAY_OF_YEAR);
        int now_year = ct.get(Calendar.YEAR);
        SimpleDateFormat sf = null;
        String timeStr = null;
        if (day == now_day && year == now_year) {
            sf = new SimpleDateFormat("HH:mm");
            timeStr = sf.format(new Date(time));
        } else if (year == now_year) {
            sf = new SimpleDateFormat("MM/dd HH:mm");
            timeStr = sf.format(new Date(time));
        } else {
            sf = new SimpleDateFormat("yyyy MM/dd");
            timeStr = sf.format(new Date(time));
        }

        return timeStr;
    }

    /** 比较日期时间大小 24小时制*/
    public static boolean compareTime(long time,long compTime) {
        Calendar ct = Calendar.getInstance();
        ct.setTimeInMillis(time);
        int fristTime = ct.get(Calendar.YEAR) + ct.get(Calendar.MONTH)*100 + ct.get(Calendar.DAY_OF_MONTH);
        ct.setTimeInMillis(compTime);
        int secondTime = ct.get(Calendar.YEAR) + ct.get(Calendar.MONTH)*100 + ct.get(Calendar.DAY_OF_MONTH);
        if(fristTime - secondTime > 0){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 返回时间 根据时间和现在时间间隔 显示日期/小时分钟
     *  */
    public static String getTimeForSearch(Long time,Context context){
        long NowTime =System.currentTimeMillis();
        Long duration = NowTime - time;
        SimpleDateFormat day = new SimpleDateFormat("dd", Locale.getDefault());
        int daynum = (Integer.valueOf(day.format(NowTime)) - Integer.valueOf(day.format(time)));
        if(daynum != 0){
            if(duration < 86400000l){
                return context.getString(R.string.timestamp_lastday);
            }else{
                String month=context.getString(R.string.month);
                String strday=context.getString(R.string.day);
                SimpleDateFormat date = new SimpleDateFormat("MM"+month+"dd"+strday, Locale.getDefault());
                return date.format(time);
            }
        }else{
            SimpleDateFormat today = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return today.format(time);
        }
    }
    /** 设置任务时间显示*/
    public static void setTaskTime(Context context, TextView tv, ImageView iv, long startTime, long endTime, int allDay){
        int colorRed = context.getResources().getColor(R.color.red_launchr);
        Drawable clock = context.getResources().getDrawable(R.drawable.red_clock);
        long nowTime = System.currentTimeMillis();
        if((Long.valueOf(endTime) != null && endTime > 0) && endTime - nowTime < 7200000 ){
            tv.setTextColor(colorRed);
            iv.setImageDrawable(clock);
        }
        if (Long.valueOf(startTime) != null && startTime > 0 && Long.valueOf(endTime) != null && endTime > 0) {
            tv.setText(setRemindTimeText(startTime) + "~" + setRemindTimeText(endTime));
        }else if(Long.valueOf(startTime) != null &&  startTime > 0 &&(Long.valueOf(endTime) == null || endTime == 0) ) {
            tv.setText(context.getString(R.string.calendar_timepicker_starttime) + setRemindTimeText(startTime));
        }else if((Long.valueOf(startTime) == null||startTime == 0) && Long.valueOf(endTime) != null && endTime > 0) {
            tv.setText(context.getString(R.string.end_time) + setRemindTimeText(endTime));
        }else{
            tv.setText(context.getString(R.string.no_start_end));
        }
    }

    /** 设置任务卡片时间显示*/
    public static void setAppTaskTime(Context context,TextView tv,ImageView iv,long endTime){
        int colorRed = context.getResources().getColor(R.color.red_launchr);
        Drawable clock = context.getResources().getDrawable(R.drawable.red_clock);
        Drawable blackClock = context.getResources().getDrawable(R.drawable.gray_clock);
        long nowTime = System.currentTimeMillis();
        if((Long.valueOf(endTime) != null && endTime > 0) && endTime - nowTime < 7200000 ){
            tv.setTextColor(colorRed);
            iv.setImageDrawable(clock);
        }else{
            tv.setTextColor(Color.BLACK);
            iv.setImageDrawable(blackClock);
        }
        if(Long.valueOf(endTime) != null && endTime > 0) {
            tv.setText(context.getString(R.string.end_time) + setRemindTimeText(endTime));
        }else{
            tv.setText(context.getString(R.string.no_end));
        }
    }

    /**
     * 显示session的时间
     * 当天显示几点几分，昨天显示昨天几点几分，后面显示几月几日，那一年
     * @return
     */
    public static String getSessionTime(long time, Context context){
        Calendar ct = Calendar.getInstance();
        ct.setTimeInMillis(time);
        int day = ct.get(Calendar.DAY_OF_YEAR);
        int year = ct.get(Calendar.YEAR);
        ct.setTimeInMillis(System.currentTimeMillis());
        int now_day = ct.get(Calendar.DAY_OF_YEAR);
        int now_year = ct.get(Calendar.YEAR);
        SimpleDateFormat sf = null;
        String timeStr = null;
        if (day == now_day && year == now_year) {
            sf = new SimpleDateFormat("HH:mm");
            timeStr = sf.format(new Date(time));
        }else if(day == now_day - 1 && year == now_year){
            sf = new SimpleDateFormat(context.getString(R.string.timestamp_lastday) + "HH:mm");
            timeStr = sf.format(new Date(time));
        } else if (year == now_year) {
            sf = new SimpleDateFormat("MM/dd");
            timeStr = sf.format(new Date(time));
        } else {
            sf = new SimpleDateFormat("yyyy");
            timeStr = sf.format(new Date(time));
        }

        return timeStr;
    }

    /** 提醒时间*/
    public static String setRemindTimeText(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int mm = calendar.get(Calendar.MINUTE);

        Date date = new Date();
        date.setTime(time);
        SimpleDateFormat sdf = null;
        if(h==0 && mm==0){
            sdf = new SimpleDateFormat("MM/dd", Locale.getDefault());
        }else{
            sdf = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());
        }
        return sdf.format(date);
    }
    /** 是否全天时间格式*/
    public static String setAllDayTimeText(Context context,int allDay ,TextView view,Long time){
        SimpleDateFormat sdf = null;
        if(time == null){
            view.setText(context.getString(R.string.some_day));
        }else if(allDay == 1){
            sdf = new SimpleDateFormat("MM/dd", Locale.getDefault());
        }else {
            sdf = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());
        }
        return sdf.format(time);
    }

    /** 开始时间和结束时间的间隔时间*/
    public  static String setTimeInterval(Context context,long endTime,long startTime){
        long intervalTime = endTime - startTime + 10000;
        long days = intervalTime/(24 * 3600 * 1000);
        long allMinutes = intervalTime%(24 * 3600 * 1000);
        long hours = allMinutes/(3600 * 1000);
        long minutes = allMinutes%(3600 * 1000)/60000;
        String timeDisplay = "";
        if(days > 0){
            timeDisplay += (int)days + ""+ context.getString(R.string.single_day);
        }
        if(hours > 0){
            timeDisplay += (int)hours + ""+ context.getString(R.string.hour);
        }
        if(minutes > 0){
            timeDisplay += (int)minutes + ""+ context.getString(R.string.timestamp_minutes);
        }
        return timeDisplay;
    }
    /** 开始时间和结束时间显示*/
    public static String getStartEndTimeFormat(long startTime,long overTime){

        Calendar beginTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        beginTime.setTimeInMillis(startTime);
        endTime.setTimeInMillis(overTime);

        int mIntBeginMonth = beginTime.get(Calendar.MONTH);
        int mIntBeginDay = beginTime.get(Calendar.DAY_OF_MONTH);
        int mIntEndYear = endTime.get(Calendar.YEAR);
        int mIntEndMonth = endTime.get(Calendar.MONTH);
        int mIntEndDay = endTime.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat OverOneDayFormat = new SimpleDateFormat("M/d(E) HH:mm",Const.getLocale());
        SimpleDateFormat OneDayFormat = new SimpleDateFormat("HH:mm",Const.getLocale());
        //设置时间显示格式 一天以上 或 一天
        if((mIntBeginMonth - mIntEndMonth != 0 )|| (mIntBeginDay - mIntEndDay != 0)){
            String sBeginTime  = OverOneDayFormat.format(startTime);
            String sOverTime  = OverOneDayFormat.format(overTime);
            return sBeginTime + "~" + sOverTime;
        }else{
            String sBeginTime  = OverOneDayFormat.format(startTime);
            String sOverTime = OneDayFormat.format(overTime);
            return sBeginTime + "~" + sOverTime;
        }
    }

    /** 根据是否全天设置开始时间和结束时间的显示*/
    public static String setTime(Long lBeginTime ,Long lOverTime,boolean flag){
        if(lBeginTime == null || lOverTime == null){
            return "";
        }
        SimpleDateFormat monthFormat = new SimpleDateFormat("M",Const.getLocale());
        SimpleDateFormat dayFormat = new SimpleDateFormat("d", Const.getLocale());
        //获取开始月日和结束月日
        String BeginMonth = monthFormat.format(lBeginTime);
        String BeginDay = dayFormat.format(lBeginTime);
        String OverMonth = monthFormat.format(lOverTime);
        String OverDay = dayFormat.format(lOverTime);
        SimpleDateFormat AllDayFormat = new SimpleDateFormat("M/d(E)",Const.getLocale());
        SimpleDateFormat OverOneDayFormat = new SimpleDateFormat("M/d(E) HH:mm",Const.getLocale());
        SimpleDateFormat OneDayFormat = new SimpleDateFormat("HH:mm",Const.getLocale());
        if(flag == true ){
            String sBeginTime  = AllDayFormat.format(lBeginTime);
            String sOverTime = AllDayFormat.format(lOverTime);
            return sBeginTime + "~" + sOverTime;
        }
        //设置时间显示格式 一天以上 或 一天
        if((Integer.valueOf(OverMonth)-Integer.valueOf(BeginMonth)!= 0)||
                (Integer.valueOf(OverDay)-Integer.valueOf(BeginDay)!= 0)){
            String sBeginTime  = OverOneDayFormat.format(lBeginTime);
            String sOverTime  = OverOneDayFormat.format(lOverTime);
            return sBeginTime + "~" + sOverTime;
        }else{
            String sBeginTime  = OverOneDayFormat.format(lBeginTime);
            String sOverTime = OneDayFormat.format(lOverTime);
            return sBeginTime + "~" + sOverTime;
        }
    }

    /** 显示全天和不全天时间*/
    public static String formatEventTimeAllDay(long time) {
        Calendar ct = Calendar.getInstance();
        ct.setTimeInMillis(time);
        int day = ct.get(Calendar.DAY_OF_YEAR);
        int year = ct.get(Calendar.YEAR);
        int hour = ct.get(Calendar.HOUR);
        int minute = ct.get(Calendar.MINUTE);
        ct.setTimeInMillis(System.currentTimeMillis());
        int now_day = ct.get(Calendar.DAY_OF_YEAR);
        int now_year = ct.get(Calendar.YEAR);
        SimpleDateFormat sf = null;
        String timeStr = null;
        if(hour==0 && minute==0){
            if (day == now_day && year == now_year) {
                sf = new SimpleDateFormat("MM/dd");
                timeStr = sf.format(new Date(time));
            } else if (year == now_year) {
                sf = new SimpleDateFormat("MM/dd");
                timeStr = sf.format(new Date(time));
            } else {
                sf = new SimpleDateFormat("yyyy MM/dd");
                timeStr = sf.format(new Date(time));
            }
        }else{
            if (day == now_day && year == now_year) {
                sf = new SimpleDateFormat("MM/dd HH:mm");
                timeStr = sf.format(new Date(time));
            } else if (year == now_year) {
                sf = new SimpleDateFormat("MM/dd HH:mm");
                timeStr = sf.format(new Date(time));
            } else {
                sf = new SimpleDateFormat("yyyy MM/dd HH:mm");
                timeStr = sf.format(new Date(time));
            }
        }

        return timeStr;
    }


}
