package com.mintcode.launchr.app.newApproval.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.app.newApproval.activity.CreateNewApplyActivity;
import com.mintcode.launchr.pojo.entity.formData;
import com.mintcode.launchr.util.TTDensityUtil;

/**
 * Created by JulyYu on 2016/4/21.
 */
public class UrgencyLayoutView extends BaseLinearLayoutView implements CompoundButton.OnCheckedChangeListener{


    private TextView mTvTitle;
    private CheckBox mCbUrgency;

    public UrgencyLayoutView(Context context, formData data) {
        super(context, data);
        initView();
    }


    protected void initView(){



        LinearLayout.LayoutParams textTitleLayout = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1);
        mTvTitle =  new TextView(mContext);
        mTvTitle.setLayoutParams(textTitleLayout);
        mTvTitle.setTextSize(15);
        mTvTitle.setTextColor(mContext.getResources().getColor(R.color.meeting_gray));
        mTvTitle.setText(mContext.getString(R.string.accpect_state));
        this.addView(mTvTitle);

        int with = TTDensityUtil.dip2px(mContext,30);
        int height = TTDensityUtil.dip2px(mContext,25);
        LinearLayout.LayoutParams boxLayout = new LinearLayout.LayoutParams(with,height);
        mCbUrgency = new CheckBox(mContext);
        mCbUrgency.setChecked(false);
        mCbUrgency.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
        mCbUrgency.setBackgroundResource(R.drawable.checkbox_seletor_red);
        mCbUrgency.setOnCheckedChangeListener(this);
        mCbUrgency.setLayoutParams(boxLayout);
        this.addView(mCbUrgency);
    }

    public int getIsUrgency(){
        if(mCbUrgency.isChecked()){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(buttonView == mCbUrgency){
            if(mContext instanceof CreateNewApplyActivity){
                CreateNewApplyActivity activity = ((CreateNewApplyActivity) mContext);
                if(activity != null){
                    boolean bool =  mCbUrgency.isChecked();
                    activity.setLimitTime(bool);
                }
            }
        }
    }
}
