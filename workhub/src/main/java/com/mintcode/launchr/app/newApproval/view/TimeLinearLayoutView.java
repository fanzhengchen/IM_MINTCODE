package com.mintcode.launchr.app.newApproval.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.app.newApproval.Entity.FormTimeDataEntity;
import com.mintcode.launchr.app.newApproval.Entity.FormTimesDataEntity;
import com.mintcode.launchr.app.newApproval.window.AddTimePopWindowView;
import com.mintcode.launchr.pojo.entity.formData;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.TTJSONUtil;

import java.text.SimpleDateFormat;



/**
 * Created by JulyYu on 2016/4/13.
 */
public class TimeLinearLayoutView extends BaseLinearLayoutView implements View.OnClickListener,AddTimePopWindowView.TimePopWindowListener {


    private TextView mTvTimeTitle;
    private TextView mTvTime;

    private String mStrTitle;
    private Long mLastTime;
    private Long mStartTime;
    private Long mOverTime;
    private boolean mBoolAllDay = true;


    public TimeLinearLayoutView(Context context,formData data) {
        super(context,data);
        mStrTitle = mFormData.getLabelText();
        initView();
    }


    public TimeLinearLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeLinearLayoutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void initView() {

        this.setOnClickListener(this);

        LinearLayout.LayoutParams timeTitleLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mTvTimeTitle = new TextView(mContext);
        mTvTimeTitle.setTextSize(15);
        mTvTimeTitle.setTextColor(mContext.getResources().getColor(R.color.meeting_gray));
        mTvTimeTitle.setText(mStrTitle);
        mTvTimeTitle.setLayoutParams(timeTitleLayout);
        this.addView(mTvTimeTitle);

        LinearLayout.LayoutParams timeLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mTvTime = new TextView(mContext);
        mTvTime.setLayoutParams(timeLayout);
        mTvTime.setTextSize(15);
        mTvTime.setTextColor(mContext.getResources().getColor(R.color.black));
        mTvTime.setText("");
        mTvTime.setGravity(Gravity.RIGHT);
        this.addView(mTvTime);
    }



    public String getJsonString(){
        String type = mFormData.getInputType();
        boolean require = mFormData.isRequired();
        if(type.equals(FormViewUtil.TIME_INPUT)){
            if(mFormData.getTimeType().equals("TimeSlot")) {
                if(mStartTime != null && mOverTime != null) {
                    mTimeDatasEntity.setKey(mFormData.getKey());
                    FormTimesDataEntity.Timevalues value = new FormTimesDataEntity.Timevalues();
                    value.setStartTime(mStartTime);
                    value.setEndTime(mOverTime);
                    mTimeDatasEntity.setValue(value);
                    return TTJSONUtil.convertObjToJson(mTimeDatasEntity);
                }else if(require){
                     FormViewUtil.BOOL_NO_NULL = false;
                    FormViewUtil.mustInputToast(mContext,mContext.getString(R.string.calendar_time_toast));
                }
            }else if(mFormData.getTimeType().equals("TimePoint")){
                if(mLastTime != null) {
                    mTimePointEntity.setKey(mFormData.getKey());
                    mTimePointEntity.setValue(mLastTime);
                    return TTJSONUtil.convertObjToJson(mTimePointEntity);
                }else if(require){
                    FormViewUtil.BOOL_NO_NULL = false;
                    FormViewUtil.mustInputToast(mContext,mContext.getString(R.string.calendar_time_toast));
                }
            }
        } else if (type.equals(FormViewUtil.APPROVE_LIMIT_INPUT)){
            if(mLastTime != null){
                mTimeDataEntity.setKey(mFormData.getKey());
                FormTimeDataEntity.Timevalues value = new FormTimeDataEntity.Timevalues();
                value.setDeadline(mLastTime);
                mTimeDataEntity.setValue(value);
                return TTJSONUtil.convertObjToJson(mTimeDataEntity);
            }else if(require){
                FormViewUtil.BOOL_NO_NULL = false;
                FormViewUtil.mustInputToast(mContext,mContext.getString(R.string.calendar_time_toast));
            }
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        if(v == this){
            showTime();
            FormViewUtil.hideSoftInputWindow(mContext,this);
        }
    }

    private void showTime() {

        AddTimePopWindowView  mPopWindowAdd =  new AddTimePopWindowView(mContext);
        mPopWindowAdd.setTimePopWindowListener(this);
        //设置控件类型
        String type =  mFormData.getInputType();
        if(type.equals(FormViewUtil.TIME_INPUT)){
            if(mFormData.getTimeType().equals("TimeSlot")) {
                mPopWindowAdd.setInitTime(null, mStartTime, mOverTime);
                mPopWindowAdd.setInit(false, mBoolAllDay);
            }else if(mFormData.getTimeType().equals("TimePoint")){
                mPopWindowAdd.setInitTime(mLastTime, null, null);
                mPopWindowAdd.setInit(true, mBoolAllDay);
            }
        }else if(type.equals(FormViewUtil.APPROVE_LIMIT_INPUT)){
            mPopWindowAdd.setInitTime(mLastTime, null, null);
            mPopWindowAdd.setInit(true, mBoolAllDay);
        }
        mPopWindowAdd.showAtLocation(this, Gravity.BOTTOM, 0, 0);
    }


    @Override
    public void getApplyTime(boolean state, Long time1, Long time2) {
        mBoolAllDay = state;
        mStartTime = time1;
        mOverTime = time2;
        FormViewUtil.setTimeDisplayFormat(state, time1, time2, mTvTime);
    }

    @Override
    public void getLastTime(boolean state, Long time1) {
        mBoolAllDay = state;
        mLastTime = time1;
        FormViewUtil.setSingeTimeDisplayFormat(state, time1, mTvTime);
    }

    @Override
    public void canelGetTime() {

    }
}
