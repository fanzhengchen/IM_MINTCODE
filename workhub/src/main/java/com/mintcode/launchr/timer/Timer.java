package com.mintcode.launchr.timer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.mintcode.launchr.R;
import com.mintcode.launchr.app.meeting.view.EventConflictView;
import com.mintcode.launchr.pojo.entity.EventEntity;
import com.mintcode.launchr.timer.listener.OnTimerListener;
import com.mintcode.launchr.util.Const;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by JulyYu on 2016/3/4.
 * 时间控件工具类
 */
public class Timer extends FrameLayout implements RadioGroup.OnCheckedChangeListener,NumberPicker.OnValueChangeListener{


    private Context mContext;

    /** 单个时间全天*/
    public static final int SINGLE_TIME_All_DAY = 1;
    /** 单个时间分钟*/
    public static final int SINGLE_TIME_DATE = 2;
    /** 开始和结束时间 */
    public static final int BEGIN_END_TIME = 3;
    /** 全天时间 */
    public static final int ALL_DAY_TIME = 4;
    /** 五分钟间隔 */
    public static final int FIVE_POSITON = 10;
    /** 单个时间分钟*/
    public static final int SINGLE_TIME_FIVE_POSITION = 5;
    /** 时间控件模式选择*/
    private int mStyle = ALL_DAY_TIME;
    /** 开始时间和结束时间切换 开始时间 true/结束时间 false*/
    private boolean mBooleanTimeSelect = true;
    /** 开始时间是否可以早于当前 可以 false/不可以 true*/
    private boolean mBooleanBefoeNow = true;

    /** 冲突事件*/
    private EventConflictView mEventConflictView;
    /** 双时间布局 */
    private LinearLayout mLinearNotAllDay;
    /** 日期显示Picker   */
    private NumberPicker mDatePicker;
    /** 小时显示Picker */
    private NumberPicker mHourPicker;
    /** 分钟显示Picker */
    private NumberPicker mMinutePicker;
    /** 开始时间和结束时间选择 */
    private RadioGroup mRg;

    /** 全天时间布局 */
    private LinearLayout mLinearAllDay;
    /** 全天开始时间显示Picker */
    private NumberPicker mALLDayBeginPicker;
    /** 全天结束时间显示Picker */
    private NumberPicker mAllDayEndPicker;

    /** 当前所用时间*/
    private Calendar mNowCalendar;
    /** 开始时间 */
    private Calendar mBeginCalendar;
    /** 结束时间 */
    private Calendar mEndCalendar;
    /** 开始时间*/
    private long mBeginTime;
    /** 结束时间*/
    private long mEndTime;
    /** 小时*/
    private int mHour;
    /** 分钟*/
    private int mMint;

    /** 时间选择监听 **/
    private OnTimerListener mOnTimerListener;
    /** 全天开始日期显示数组 */
    private String[] mAllBegainDateDisplayValues = new String[7];
    /** 全天结束日期显示数组 */
    private String[] mAllEndDateDisplayValues = new String[7];
    /** 日期显示数组 */
    private String[] mDateDisplayValues = new String[7];
    /** 五分钟间隔显示数组 */
    private String[] mMintFiveDisplayValues = new String[12];

    /** 日期年格式显示*/
    private SimpleDateFormat mYearDateFormat;
    /** 日期月份格式显示*/
    private SimpleDateFormat mDateFormat;
    /** 日期显示格式*/
    private final String mStrDateFormat = "MM-dd E";
    /** 年月日显示格式*/
    private final String mStrYearDateFoemat = "yyyy-MM-dd E";
    /** 一小时*/
    private final int ONE_HOUR = 3600000;
    /** 五分钟*/
    private final int FIVE_MINT = 300000;
    /** 今年*/
    private int mIntYear;
    /** 本月*/
    private int mIntMonth;
    /** 今天*/
    private int mIntDay;
    /** 当天日期*/
    private Calendar mTodayCalendar;
    private Long mLongTodayTime;

    public Timer(Context context) {
        super(context);
        initView(context);
        initData();
    }

    public Timer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initData();
    }

    public Timer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
        initData();
    }

    /**初始化视图*/
    private void initView(Context context){
        mContext = context;
        mYearDateFormat = new SimpleDateFormat(mStrYearDateFoemat, Const.getLocale());
        mDateFormat = new SimpleDateFormat(mStrDateFormat,Const.getLocale());

        inflate(context, R.layout.view_date_timer_new, this);
        mEventConflictView = (EventConflictView)findViewById(R.id.event_conflict_view);
        mDatePicker = (NumberPicker) findViewById(R.id.np_date);
        mHourPicker = (NumberPicker) findViewById(R.id.np_hour);
        mMinutePicker = (NumberPicker) findViewById(R.id.np_minute);
        mLinearNotAllDay = (LinearLayout) findViewById(R.id.ll_not_all_day);
        mRg = (RadioGroup) findViewById(R.id.rg);

        mALLDayBeginPicker = (NumberPicker) findViewById(R.id.np_date_all_begin);
        mAllDayEndPicker = (NumberPicker) findViewById(R.id.np_date_all_end);
        mLinearAllDay = (LinearLayout) findViewById(R.id.ll_all_day);


        mDatePicker.setOnValueChangedListener(this);
        mHourPicker.setOnValueChangedListener(this);
        mMinutePicker.setOnValueChangedListener(this);
        mALLDayBeginPicker.setOnValueChangedListener(this);
        mAllDayEndPicker.setOnValueChangedListener(this);
        mRg.setOnCheckedChangeListener(this);
    }
    /** 初始化数据*/
    private void initData(){

        mTodayCalendar = Calendar.getInstance();
        mIntYear = mTodayCalendar.get(Calendar.YEAR);
        mIntMonth = mTodayCalendar.get(Calendar.MONTH);
        mIntDay = mTodayCalendar.get(Calendar.DAY_OF_MONTH);
        mTodayCalendar.set(mIntYear, mIntMonth, mIntDay);
        mLongTodayTime = mTodayCalendar.getTimeInMillis();
        mNowCalendar = Calendar.getInstance();
        mNowCalendar.setTimeInMillis(System.currentTimeMillis());
        mBeginCalendar = mNowCalendar;
        mEndCalendar = Calendar.getInstance();
        mEndCalendar.setTimeInMillis(mBeginCalendar.getTimeInMillis() + (long)ONE_HOUR);

        mBeginTime = mNowCalendar.getTimeInMillis();
        mEndTime = mBeginTime + ONE_HOUR;
        mHour = mNowCalendar.get(Calendar.HOUR_OF_DAY);
        mMint = mNowCalendar.get(Calendar.MINUTE);
        // 设置日期显示
        mDatePicker.setMaxValue(6);
        mDatePicker.setMinValue(0);
        updateDateControl();
        // 设置小时显示
        mHourPicker.setFormatter(new FormatListener());
        mHourPicker.setMaxValue(23);
        mHourPicker.setMinValue(0);
        mHourPicker.setValue(mHour);
        // 设置分钟显示
        mMinutePicker.setFormatter(new FormatListener());
        mMinutePicker.setMaxValue(59);
        mMinutePicker.setMinValue(0);
        mMinutePicker.setValue(mMint);
        // 设置全天开始日期显示
        mALLDayBeginPicker.setMaxValue(6);
        mALLDayBeginPicker.setMinValue(0);
        updateAllDayBeginControl();
        // 设置全天结束日期显示
        mAllDayEndPicker.setMaxValue(6);
        mAllDayEndPicker.setMinValue(0);
        updateAllDayEndControl();

        mEventConflictView.setDay(mNowCalendar);
    }

    /** 切换Time控件模式*/
    public void setStyle(int type){

        switch (type){
            case BEGIN_END_TIME:
                 mStyle = type;
                 mLinearNotAllDay.setVisibility(View.VISIBLE);
                 mRg.setVisibility(View.VISIBLE);
                 RadioButton button = (RadioButton)findViewById(R.id.rbtn_left);
                 button.setChecked(true);
                 mBooleanTimeSelect = true;
                 mLinearAllDay.setVisibility(View.GONE);
                 checkTimeRight();
                break;
            case ALL_DAY_TIME:
                mStyle = type;
                mLinearAllDay.setVisibility(View.VISIBLE);
                mAllDayEndPicker.setVisibility(View.VISIBLE);
                mRg.setVisibility(View.GONE);
                mLinearNotAllDay.setVisibility(View.GONE);
                checkTimeRight();
                break;
            case FIVE_POSITON:
                mStyle = type;
                mLinearNotAllDay.setVisibility(View.VISIBLE);
                mLinearAllDay.setVisibility(View.GONE);
                RadioButton button2 = (RadioButton)findViewById(R.id.rbtn_left);
                button2.setChecked(true);
                mBooleanTimeSelect = true;
                mRg.setVisibility(View.VISIBLE);
                updateMinControl();
                checkTimeRight();
                break;
            case SINGLE_TIME_All_DAY:
                mStyle = type;
                mNowCalendar = mBeginCalendar;
                mRg.setVisibility(View.GONE);
                mLinearNotAllDay.setVisibility(View.GONE);
                mAllDayEndPicker.setVisibility(View.GONE);
                mLinearAllDay.setVisibility(View.VISIBLE);
                mBooleanTimeSelect = true;
                checkTimeByAllDay();
                break;
            case SINGLE_TIME_DATE:
                mStyle = type;
                mNowCalendar = mBeginCalendar;
                mRg.setVisibility(View.GONE);
                mLinearAllDay.setVisibility(View.GONE);
                mLinearNotAllDay.setVisibility(View.VISIBLE);
                mBooleanTimeSelect = true;
                checkTimeRight();
                break;
            case SINGLE_TIME_FIVE_POSITION:
                mStyle = type;
                mNowCalendar = mBeginCalendar;
                mRg.setVisibility(View.GONE);
                mLinearAllDay.setVisibility(View.GONE);
                mLinearNotAllDay.setVisibility(View.VISIBLE);
                mBooleanTimeSelect = true;
                updateMinControl();
                checkTimeRight();
                break;
            default:
                break;
        }

    }
    /** 设置开始时间是否可以早于当前时间*/
    public void setTimeBeforeNow(boolean bool){
        mBooleanBefoeNow = bool;
    }
    /** 设置开始时间*/
    public void setShowTime(long beginTime ,long endTime){
        if(Long.valueOf(beginTime) != null && Long.valueOf(endTime) != null){
            mNowCalendar.setTimeInMillis(beginTime);
            mBeginCalendar.setTimeInMillis(beginTime);
            mEndCalendar.setTimeInMillis(endTime);
            RadioButton button = (RadioButton)findViewById(R.id.rbtn_left);
            button.setChecked(true);
        }
    }
    /** 设置开始时间*/
    public void setShowTime(Long beginTime ,Long endTime){
        if(beginTime != null && endTime != null){
            mNowCalendar.setTimeInMillis(beginTime);
            mBeginCalendar.setTimeInMillis(beginTime);
            mEndCalendar.setTimeInMillis(endTime);
            RadioButton button = (RadioButton)findViewById(R.id.rbtn_left);
            button.setChecked(true);
        }
    }

    /** 设置当前时间*/
    public void setNowCalendarTime(long nowTime){
        if(Long.valueOf(nowTime) != null){
            mNowCalendar.setTimeInMillis(nowTime);
        }
    }
    /** 设置开始时间*/
    public void setBeginTime(long beginTime){
        if(Long.valueOf(beginTime) != null){
            mBeginCalendar.setTimeInMillis(beginTime);
        }
    }
    /** 设置开始时间*/
    public void setBeginTime(Long beginTime){
        if(beginTime != null){
            mBeginCalendar.setTimeInMillis(beginTime);
        }
    }
    /** 设置结束时间*/
    public void setEndTime(long endTime){
        if(Long.valueOf(endTime) != null){
            mEndCalendar.setTimeInMillis(endTime);
        }
    }
    /** 获取开始时间*/
    public long getBeginTime(){
        if(mStyle == ALL_DAY_TIME){
            int mint = mBeginCalendar.get(Calendar.MINUTE);
            mint = mint/5 * 5;
            mBeginCalendar.set(Calendar.MINUTE,mint);
        }
        return mBeginCalendar.getTimeInMillis();
    }
    /** 获取结束时间*/
    public long getEndTime(){
        if(mStyle == ALL_DAY_TIME){
            int mint = mEndCalendar.get(Calendar.MINUTE);
            mint = mint/5 * 5;
            mEndCalendar.set(Calendar.MINUTE,mint);
        }
        return mEndCalendar.getTimeInMillis();
    }
    /** 获取当前时间*/
    public long getNowCalendarTime(){
        return mNowCalendar.getTimeInMillis();
    }
    /** 设置冲突事件*/
    public void setEventData(List<EventEntity> datas){
        mEventConflictView.setDatas(datas);
    }

    /** 给冲突事件设置时间*/
    public void setNowTime(long now){
        mEventConflictView.setNowTime(now);
    }
    /** 获取冲突事件*/
    public int getConflictCount(){
        return mEventConflictView.getConflictCount();
    }
    /** 设置冲突事件是否显示*/
    public void setConflictViewDisplay(boolean bool){
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.rl_event_conflict_view);
        if(bool){
            rl.setVisibility(View.VISIBLE);
        }else{
            rl.setVisibility(View.GONE);
        }
    }
    public void setOnTimerListener(OnTimerListener mOnTimerListener) {
        this.mOnTimerListener = mOnTimerListener;
    }

    /**更新全天开始时间 */
    private void updateAllDayBeginControl(){
        SimpleDateFormat dateFormat;
        if(mStyle == ALL_DAY_TIME){
            dateFormat = mDateFormat;
        }else{
            dateFormat = mYearDateFormat;
        }
        // 获取当前显示时间
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mBeginCalendar.getTimeInMillis());
        cal.add(Calendar.DAY_OF_YEAR, -7 / 2 - 1);
        mALLDayBeginPicker.setDisplayedValues(null);
        int len = mAllBegainDateDisplayValues.length;
        // 组装下一个显示的时间
        for (int i = 0; i < len; i++) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            if(mBooleanBefoeNow){ // 小于当前时间
                if(mStyle == SINGLE_TIME_All_DAY){
                    if((cal.get(Calendar.YEAR) <= mTodayCalendar.get(Calendar.YEAR)) && (cal.get(Calendar.DAY_OF_YEAR) < mTodayCalendar.get(Calendar.DAY_OF_YEAR))){
                        mAllBegainDateDisplayValues[i] = " ";
                    }else{
                        mAllBegainDateDisplayValues[i] = dateFormat.format(cal.getTime());
                    }
                }else {
                    if (cal.getTimeInMillis() - mLongTodayTime < -1000) {
                        mAllBegainDateDisplayValues[i] = " ";
                    } else {
                        mAllBegainDateDisplayValues[i] = dateFormat.format(cal.getTime());
                    }
                }
            }else{
                mAllBegainDateDisplayValues[i] = dateFormat.format(cal.getTime());
            }


        }
        // 设置显示
        mALLDayBeginPicker.setDisplayedValues(mAllBegainDateDisplayValues);
        mALLDayBeginPicker.setValue(len / 2);
        mALLDayBeginPicker.postInvalidate();
    }

    /**更新全天结束时间 */
    private void updateAllDayEndControl(){

        SimpleDateFormat dateFormat = new SimpleDateFormat(mStrDateFormat,Const.getLocale());
        // 获取当前显示时间
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mEndCalendar.getTimeInMillis());
        cal.add(Calendar.DAY_OF_YEAR, -7 / 2 - 1);
        mAllDayEndPicker.setDisplayedValues(null);
        int len = mAllEndDateDisplayValues.length;
        // 组装下一个显示的时间
        for (int i = 0; i < len; i++) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            if(mBooleanBefoeNow){ // 小于当前时间
                if(cal.getTimeInMillis() - mLongTodayTime < -1000){
                    mAllEndDateDisplayValues[i] = " ";
                }else{
                    mAllEndDateDisplayValues[i] = dateFormat.format(cal.getTime());
                }
            }else{
                mAllEndDateDisplayValues[i] = dateFormat.format(cal.getTime());
            }
        }
        // 设置显示
        mAllDayEndPicker.setDisplayedValues(mAllEndDateDisplayValues);
        mAllDayEndPicker.setValue(len / 2);
        mAllDayEndPicker.postInvalidate();
    }
    /**更新日期 */
    private void updateDateControl(){

        SimpleDateFormat dateFormat = new SimpleDateFormat(mStrDateFormat,Const.getLocale());
        // 获取当前显示时间
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mNowCalendar.getTimeInMillis());
        cal.add(Calendar.DAY_OF_YEAR, -7 / 2 - 1);
        mDatePicker.setDisplayedValues(null);
        int len = mDateDisplayValues.length;
        // 组装下一个显示的时间
        for (int i = 0; i < len; i++) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            if(!mBooleanBefoeNow){
                mDateDisplayValues[i] = dateFormat.format(cal.getTime());
            }else{
                if(mStyle == SINGLE_TIME_All_DAY){
                    if((cal.get(Calendar.YEAR) <= mTodayCalendar.get(Calendar.YEAR)) && (cal.get(Calendar.DAY_OF_YEAR) < mTodayCalendar.get(Calendar.DAY_OF_YEAR)) ){
                        mDateDisplayValues[i] = " ";
                    }else{
                        mDateDisplayValues[i] = dateFormat.format(cal.getTime());
                    }
                }else{
                    if(cal.getTimeInMillis() < mLongTodayTime){
                        mDateDisplayValues[i] = " ";
                    }else{
                        mDateDisplayValues[i] = dateFormat.format(cal.getTime());
                    }
                }

            }
        }
        // 设置显示
        mDatePicker.setDisplayedValues(mDateDisplayValues);
        mDatePicker.setValue(len / 2);
        mDatePicker.postInvalidate();
    }


    private void updateMinControl(){
        if(mStyle == FIVE_POSITON || mStyle == SINGLE_TIME_FIVE_POSITION){
            mMinutePicker.setDisplayedValues(null);
            for (int i = 0; i < mMintFiveDisplayValues.length; i ++ ){
                String s = "";
                int x = i * 5;
                if(x < 10){
                    s = "0" + x;
                }else {
                    s = "" + x;
                }
                mMintFiveDisplayValues[i] = s;
            }
            mMinutePicker.setMaxValue(mMintFiveDisplayValues.length - 1);
            mMinutePicker.setMinValue(0);
            mMinutePicker.setDisplayedValues(mMintFiveDisplayValues);
            int mint = mMint/5;
            if(mint < mMintFiveDisplayValues.length - 1){
                mMinutePicker.setValue(mint);
            }else{
                mMinutePicker.setValue(mint + 1);
            }
        }else{
            mMinutePicker.setDisplayedValues(null);
            mMinutePicker.setMaxValue(59);
            mMinutePicker.setMinValue(00);
            mMinutePicker.setValue(mMint);
        }

    }

    /** 验证时间显示正确*/
    private void checkTimeRight(){

        long nowTime = System.currentTimeMillis();
        long setTime = mNowCalendar.getTimeInMillis();
        long beginTime = mBeginCalendar.getTimeInMillis();
        long endTime = mEndCalendar.getTimeInMillis();
        switch (mStyle){
            case BEGIN_END_TIME:
                checkTimeByBeginEnd(setTime,nowTime,beginTime,endTime);
                break;
            case ALL_DAY_TIME:
                checkTimeByAllDay();
                break;
            case  FIVE_POSITON:
                checkTimeByBeginEnd(setTime, nowTime, beginTime, endTime);
                break;
            case  SINGLE_TIME_All_DAY:
                checkTimeByAllDay();
                break;
            case SINGLE_TIME_DATE:
                checkTimeByBeginEnd(setTime, nowTime, beginTime, endTime);
                break;
            case SINGLE_TIME_FIVE_POSITION:
                checkTimeByBeginEnd(setTime, nowTime, beginTime, endTime);
                break;
            default:
                break;
        }
        if(mOnTimerListener != null){
            sendTimeInfo();
        }
    }
    /** 通过接口发送时间*/
    private void sendTimeInfo(){
        int year;
        int month;
        int day;
        int hour;
        int minute;
        if(mBooleanTimeSelect){
            year = mBeginCalendar.get(Calendar.YEAR);
            month = mBeginCalendar.get(Calendar.MONTH);
            day = mBeginCalendar.get(Calendar.DAY_OF_MONTH);
            hour = mBeginCalendar.get(Calendar.HOUR_OF_DAY);
            minute = mBeginCalendar.get(Calendar.MINUTE);
        }else{
            year = mEndCalendar.get(Calendar.YEAR);
            month = mEndCalendar.get(Calendar.MONTH);
            day = mEndCalendar.get(Calendar.DAY_OF_MONTH);
            hour = mEndCalendar.get(Calendar.HOUR_OF_DAY);
            minute = mEndCalendar.get(Calendar.MINUTE);
        }
        switch(mStyle){
            case SINGLE_TIME_FIVE_POSITION:
            case  FIVE_POSITON:
            case BEGIN_END_TIME:
                mOnTimerListener.OnTimeChangeListenner(this,year,month + 1,day,hour,minute,mStyle,!mBooleanTimeSelect);
                break;
            case ALL_DAY_TIME:
//                mOnTimerListener.OnTimeChangeListenner(this,year,month + 1,day,0,0,mStyle,!mBooleanTimeSelect);
//                if(!mBooleanBefoeNow){
//                    mOnTimerListener.OnTimeChangeListenner(this,year,month + 1,day,0,0,mStyle,!mBooleanTimeSelect);
//                }else{
//                    mOnTimerListener.OnTimeChangeListenner(this,year,month + 1,day,0,0,mStyle,!mBooleanTimeSelect);
//                }
//                break;
            case  SINGLE_TIME_All_DAY:
                  mOnTimerListener.OnTimeChangeListenner(this, year, month + 1, day, 0, 0, mStyle, !mBooleanTimeSelect);
                break;
            case SINGLE_TIME_DATE:
                   mOnTimerListener.OnTimeChangeListenner(this, year, month + 1, day, hour, minute, mStyle, !mBooleanTimeSelect);
                break;
            default:
                break;
        }

    }
    /** 验证全天时间*/
    private void checkTimeByAllDay() {
        int beginYear = mBeginCalendar.get(Calendar.YEAR);
        int beginMonth = mBeginCalendar.get(Calendar.MONTH);
        int beginDay = mBeginCalendar.get(Calendar.DAY_OF_MONTH);
//        mBeginCalendar.set(Calendar.MINUTE,0);
//        mEndCalendar.set(Calendar.MINUTE,0);
//        mNowCalendar.set(Calendar.MINUTE,0);
        Calendar nowTime = Calendar.getInstance();
//        nowTime.set(Calendar.MINUTE,0);
        int nowYear = nowTime.get(Calendar.YEAR);
        int nowMonth = nowTime.get(Calendar.MONTH);
        int nowDay = nowTime.get(Calendar.DAY_OF_MONTH);

        boolean year = beginYear - nowYear < 0 ? true:false;
        boolean month = beginMonth - nowMonth < 0? true:false;
        boolean day = beginDay - nowDay < 0? true:false;
        if(year && mBooleanBefoeNow){// 年小或等于
            mBeginCalendar.set(nowYear,nowMonth,nowDay);
        }else if(month && mBooleanBefoeNow){// 月小
                mBeginCalendar.set(nowYear,nowMonth,nowDay);
        }else if((beginMonth - nowMonth == 0 ) && mBooleanBefoeNow){
                if(day && mBooleanBefoeNow){
                    mBeginCalendar.set(nowYear,nowMonth,nowDay);
                }
        }
        long beginTime = mBeginCalendar.getTimeInMillis();
        long endTime = mEndCalendar.getTimeInMillis();
        if(endTime < beginTime){
            mEndCalendar.setTimeInMillis(beginTime);
            mBooleanTimeSelect = false;
            if(mOnTimerListener != null){
                sendTimeInfo();
            }
            mBooleanTimeSelect = true;
        }
        updateAllDayBeginControl();
        updateAllDayEndControl();
    }

    /** 验证开始时间和结束时间*/
    private void checkTimeByBeginEnd(long setTime,long nowTime,long beginTime,long endTime) {

        if(mBooleanTimeSelect){ // 设置时间为开始时间
            if(setTime < nowTime && mBooleanBefoeNow){// 开始时间小于当前时间
                mNowCalendar.setTimeInMillis(nowTime + FIVE_MINT);
            }else if(setTime > endTime){// 开始时间大于结束时间
                mEndCalendar.setTimeInMillis(setTime + ONE_HOUR);
                mBooleanTimeSelect = false;
                if(mOnTimerListener != null){
                    sendTimeInfo();
                }
                mBooleanTimeSelect = true;
            }
            mBeginCalendar.setTimeInMillis(mNowCalendar.getTimeInMillis());
        }else{ // 设置时间为结束时间
            if(setTime <= beginTime){// 结束时间小于开始时间
                if(!mBooleanBefoeNow){
                    mBeginCalendar.setTimeInMillis(setTime - ONE_HOUR);
                    mNowCalendar.setTimeInMillis(setTime);
                    mBooleanTimeSelect = true;
                    if(mOnTimerListener != null){
                        sendTimeInfo();
                    }
                    mBooleanTimeSelect = false;
                }else{
                    mNowCalendar.setTimeInMillis(beginTime + ONE_HOUR);
                }
            }else{
                mNowCalendar.setTimeInMillis(setTime);
            }
            mEndCalendar.setTimeInMillis(mNowCalendar.getTimeInMillis());
        }
        setTimeDisplay();
    }
    /** 设置双时间的当前时间显示*/
    private void setTimeDisplay(){
        updateDateControl();
        mHour = mNowCalendar.get(Calendar.HOUR_OF_DAY);
        mMint = mNowCalendar.get(Calendar.MINUTE);
        mHourPicker.setValue(mHour);
        if (mStyle == FIVE_POSITON || mStyle == SINGLE_TIME_FIVE_POSITION){
            mMinutePicker.setValue(mMint / 5);
            mMint = mMint/5 * 5;
            mNowCalendar.set(Calendar.MINUTE,mMint);
        }else {
            mMinutePicker.setValue(mMint);
        }
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rbtn_left) { //开始时间
            mBooleanTimeSelect = true;
            mNowCalendar = mBeginCalendar;
        }else if (checkedId == R.id.rbtn_right) {//结束时间
            mBooleanTimeSelect = false;
            mNowCalendar = mEndCalendar;
        }
        setTimeDisplay();
        sendTimeInfo();
    }


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (picker == mDatePicker) {
            handleDateChange(oldVal, newVal);
        }else if (picker == mHourPicker) {
            handleHourChange(newVal);
        }else if (picker == mMinutePicker) {
            handleMinuteChange(newVal);
        }else if(picker == mALLDayBeginPicker){
            handleAllBeginChnage(oldVal, newVal);
        }else if(picker == mAllDayEndPicker){
            handleAllEndChnage(oldVal, newVal);
        }
    }
    /** 日期变化处理逻辑 */
    private void handleDateChange(int oldVal, int newVal){
        mNowCalendar.add(Calendar.DAY_OF_MONTH, newVal - oldVal);
        updateDateControl();
        checkTimeRight();
    }
    /** 小时变化处理逻辑*/
    private void handleHourChange(int newVal) {
        mNowCalendar.set(Calendar.HOUR_OF_DAY, newVal);
        checkTimeRight();
    }
    /** 分钟变化处理逻辑*/
    private void handleMinuteChange(int newVal) {
        if(mStyle == FIVE_POSITON || mStyle == SINGLE_TIME_FIVE_POSITION){
            mNowCalendar.set(Calendar.MINUTE, newVal * 5);
        }else{
            mNowCalendar.set(Calendar.MINUTE, newVal);
        }
        checkTimeRight();
    }
    /** 全天开始时间变化逻辑*/
    private void handleAllBeginChnage(int oldVal, int newVal){
        mBooleanTimeSelect = true;
        mBeginCalendar.add(Calendar.DAY_OF_MONTH, newVal - oldVal);
        updateAllDayBeginControl();
        checkTimeRight();
    }
    /** 全天结束时间变化逻辑*/
    private void handleAllEndChnage(int oldVal, int newVal){
        mBooleanTimeSelect = false;
        mEndCalendar.add(Calendar.DAY_OF_MONTH, newVal - oldVal);
        updateAllDayEndControl();
        checkTimeRight();
    }


    class FormatListener implements NumberPicker.Formatter{

        @Override
        public String format(int value) {
            String tmpStr = String.valueOf(value);
            if (value < 10) {
                tmpStr = "0" + tmpStr;
            }
            return tmpStr;
        }
    }
}
