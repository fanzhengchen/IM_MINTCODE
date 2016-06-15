package com.mintcode.launchr.app.newApproval.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mintcode.launchr.app.newApproval.Entity.FormMutilDataEntity;
import com.mintcode.launchr.app.newApproval.Entity.FormSingleDataEntity;
import com.mintcode.launchr.app.newApproval.Entity.FormTimeDataEntity;
import com.mintcode.launchr.app.newApproval.Entity.FormTimePointDataEntity;
import com.mintcode.launchr.app.newApproval.Entity.FormTimesDataEntity;
import com.mintcode.launchr.pojo.entity.formData;
import com.mintcode.launchr.util.TTDensityUtil;

/**
 * Created by JulyYu on 2016/4/15.
 */
public class BaseLinearLayoutView extends LinearLayout {

    protected String mStrFormJson;
    protected formData mFormData;
    protected Context mContext;
    protected FormSingleDataEntity mSingleEntity = new FormSingleDataEntity();
    protected FormMutilDataEntity mMutilEntity = new FormMutilDataEntity();
    protected FormTimesDataEntity mTimeDatasEntity = new FormTimesDataEntity();
    protected FormTimeDataEntity mTimeDataEntity = new FormTimeDataEntity();
    protected FormTimePointDataEntity mTimePointEntity = new FormTimePointDataEntity();
    
    public BaseLinearLayoutView(Context context,formData data) {
        super(context);
        mContext = context;
        mFormData = data;
        int LeftRightpadding = TTDensityUtil.dip2px(mContext,15);
        int TopBottomRightpadding = TTDensityUtil.dip2px(mContext,10);
        this.setOrientation(HORIZONTAL);
        LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(linearLayout);
        this.setPadding(LeftRightpadding, TopBottomRightpadding, LeftRightpadding, TopBottomRightpadding);
    }

    public BaseLinearLayoutView(Context context,formData data,String key) {
        super(context);
        mContext = context;
        mFormData = data;
        if(key != null){
            mStrFormJson = key;
        }else{
            mStrFormJson = "";
        }
        int LeftRightpadding = TTDensityUtil.dip2px(mContext,15);
        int TopBottomRightpadding = TTDensityUtil.dip2px(mContext,10);
        this.setOrientation(HORIZONTAL);
        LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(linearLayout);
        this.setPadding(LeftRightpadding, TopBottomRightpadding, LeftRightpadding, TopBottomRightpadding);
    }

    public BaseLinearLayoutView(Context context,formData data,String key,int hPadding,int vPadding) {
        super(context);
        mContext = context;
        mFormData = data;
        if(key != null){
            mStrFormJson = key;
        }else{
            mStrFormJson = "";
        }
        int LeftRightpadding = TTDensityUtil.dip2px(mContext,hPadding);
        int TopBottomRightpadding = TTDensityUtil.dip2px(mContext,vPadding);
        this.setOrientation(HORIZONTAL);
        LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(linearLayout);
        this.setPadding(LeftRightpadding, TopBottomRightpadding, LeftRightpadding, TopBottomRightpadding);
    }
    public BaseLinearLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLinearLayoutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void initView(){

    }

    public formData getmFormData() {
        return mFormData;
    }

    public void setmFormData(formData mFormData) {
        this.mFormData = mFormData;
    }
}
