package com.mintcode.launchr.app.newApproval.window;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.timer.Timer;
import com.mintcode.launchr.timer.listener.OnTimerListener;

public class AddTimePopWindowView extends PopupWindow implements OnClickListener, OnTimerListener {

    private View mContentView;


    //透明空白处
    private ImageView mIvTransparency;
    //透明空白处
    private TextView mIvNull;

    private LinearLayout mRlNull;
    /**
     * 监听器
     */
    private TimePopWindowListener mPopWindowListener;
    /**
     * 终日文字显示
     */
    private TextView mTvState;
    /**
     * 是否終日
     */
    private CheckBox mCbState;
    /**
     * 完成
     */
    private ImageButton mIvFinish;
    /**
     * 时间控件
     */
    private Timer mTimer;
    /**
     * 时间控件类型 true 为最后期限时间 /false请假时间
     */
    private boolean mType = true;

    private Long mStartTime;

    private Long mOverTime;

    private Long mOneDayTime;

    private Configuration mConfig;


    public AddTimePopWindowView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        mContentView = inflater.inflate(R.layout.popwindow_add_time, null);
        mConfig = context.getResources().getConfiguration();
        setWindow();
        initView();
    }

    public void initView() {


        mIvTransparency = (ImageView) mContentView.findViewById(R.id.iv_popwindow_tran);
        mIvNull = (TextView) mContentView.findViewById(R.id.iv_popwindow_null);
        mRlNull = (LinearLayout) mContentView.findViewById(R.id.ll_popwindow_null);
        mCbState = (CheckBox) mContentView.findViewById(R.id.cb_pop_add_time_state);
        mTvState = (TextView) mContentView.findViewById(R.id.tv_pop_time_title);
        mIvFinish = (ImageButton) mContentView.findViewById(R.id.ibn_pop_finish);

        mTimer = (Timer) mContentView.findViewById(R.id.timer_select);
        mTimer.setTimeBeforeNow(false);
        //設置監聽器
        mIvTransparency.setOnClickListener(this);
        mIvNull.setOnClickListener(this);
        mCbState.setOnClickListener(this);
        mIvFinish.setOnClickListener(this);
        mTimer.setOnTimerListener(this);
        mTimer.setConflictViewDisplay(false);
        isAllDay();
    }


    private void setWindow() {
        // 设置PopupWindow的View
        this.setContentView(mContentView);
        // 设置PopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置PopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置PopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.popupAnimation);
        // 实例化一个ColorDrawable颜色为透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    public void setBackgroundBlack() {
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    /**
     * 设置初始
     */
    public void setInit(boolean type, boolean state) {
        this.mType = type;
        mCbState.setChecked(state);
        isAllDay();
    }

    public void setInitTime(Long time1, Long time2, Long time3) {
        mOneDayTime = time1;
        mStartTime = time2;
        mOverTime = time3;
    }

    /**
     * 完成时间设置 通过接口获取时间参数
     */
    private void Finish() {

        if (mType == true) {
            mOneDayTime = mTimer.getBeginTime();
            mPopWindowListener.getLastTime(mCbState.isChecked(), mOneDayTime);
        } else {
            if (mOverTime == null || mOverTime <= mStartTime) {
                mOverTime = mStartTime + 60 * 60 * 1000l;
            }
            mPopWindowListener.getApplyTime(mCbState.isChecked(), mStartTime, mOverTime);
        }

    }

    /**
     * 设置时间选择类型 全天/非全天
     */
    private void isAllDay() {

        if (mType == false) {
            if (mCbState.isChecked() == true) {
                mTimer.setShowTime(mStartTime, mOverTime);
                mTimer.setStyle(Timer.ALL_DAY_TIME);
            } else {
                mTimer.setShowTime(mStartTime, mOverTime);
                mTimer.setStyle(Timer.FIVE_POSITON);
            }
        } else {
            if (mCbState.isChecked()) {
                mTimer.setBeginTime(mOneDayTime);
                mTimer.setStyle(Timer.SINGLE_TIME_All_DAY);
            } else {
                mTimer.setBeginTime(mOneDayTime);
                mTimer.setStyle(Timer.SINGLE_TIME_FIVE_POSITION);
            }
        }

    }

    /**
     * 切换是否全天设置时间选择类型
     */
    private void changeAllDay() {
//		this.mType = flag;
        if (mType == false) { // 设置请假时间
            if (mCbState.isChecked() == true) {
                if (mStartTime != null && mOverTime != null) {
                    mTimer.setShowTime(mStartTime, mOverTime);
                    mTimer.setStyle(Timer.ALL_DAY_TIME);
                } else {
                    mTimer.setStyle(Timer.ALL_DAY_TIME);
                }
            } else {
                if (mStartTime != null && mOverTime != null) {
                    mTimer.setShowTime(mStartTime, mOverTime);
                    mTimer.setStyle(Timer.FIVE_POSITON);
                } else {
                    mTimer.setStyle(Timer.FIVE_POSITON);
                }
            }
        } else { // 设置最后期限时间
            mTimer.setBeginTime(mOneDayTime);
            if (mCbState.isChecked()) {
                mTimer.setStyle(Timer.SINGLE_TIME_All_DAY);
            } else {
                mTimer.setStyle(Timer.SINGLE_TIME_FIVE_POSITION);
            }
        }

    }

    /**
     * 设置开始和结束时间
     *
     * @param end
     * @param time
     */
    private void setTime(boolean end, Long time) {
        if (end == false) {
            mStartTime = time;
        } else {
            mOverTime = time;
        }
    }


    /***
     * 字符串转long格式 获取时间毫秒值
     */
    public long changTime2Long(String time) {
        long result = 0;
        SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd HH:mm", mConfig.locale);
        try {
            result = Format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setTimePopWindowListener(TimePopWindowListener listener) {
        this.mPopWindowListener = listener;
    }

    public interface TimePopWindowListener {
        void getApplyTime(boolean state, Long time1, Long time2);

        void getLastTime(boolean state, Long time1);

        void canelGetTime();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //销毁popwindow
            case R.id.iv_popwindow_tran:
                break;
            //销毁popwindow
            case R.id.iv_popwindow_null:
                mPopWindowListener.canelGetTime();
                dismiss();
                //是否终日
            case R.id.cb_pop_add_time_state:
                changeAllDay();
                break;
            //完成
            case R.id.ibn_pop_finish:
                Finish();
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void OnTimeChangeListenner(View view, int year, int month, int day,
                                      int hour, int minute, int type, boolean isEnd) {
        String sDate = year + "-" + month + "-" + day + " " + hour + ":" + minute;
        Long lDate = changTime2Long(sDate);
        switch (type) {
            //一日时间
            case Timer.SINGLE_TIME_FIVE_POSITION:
                mOneDayTime = lDate;
                break;
            case Timer.SINGLE_TIME_All_DAY:
                mOneDayTime = lDate;
                break;
            //开始结束
            case Timer.FIVE_POSITON:
                setTime(isEnd, lDate);
                break;
            //全天时间
            case Timer.ALL_DAY_TIME:
                setTime(isEnd, lDate);
                break;
            default:
                break;
        }
    }

}
