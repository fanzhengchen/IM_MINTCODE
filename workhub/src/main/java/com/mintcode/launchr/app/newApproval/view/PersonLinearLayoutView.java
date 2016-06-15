package com.mintcode.launchr.app.newApproval.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.app.newApproval.activity.CreateNewApplyActivity;
import com.mintcode.launchr.pojo.entity.formData;
import com.mintcode.launchr.util.TTDensityUtil;

/**
 * Created by JulyYu on 2016/4/13.
 */
public class PersonLinearLayoutView extends BaseLinearLayoutView implements View.OnClickListener {


    private TextView mTvPersonTitle;
    private TextView mTvPerson;
    private ImageView mIvDirect;

    private String mStrTitle;
    public PersonLinearLayoutView(Context context,formData data) {
        super(context, data);
        mStrTitle = mFormData.getLabelText();
        initView();
    }


    public PersonLinearLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PersonLinearLayoutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void initView() {

        this.setOnClickListener(this);
        int with = TTDensityUtil.dip2px(mContext,100);
        LinearLayout.LayoutParams textTitleLayout = new LinearLayout.LayoutParams(with, LinearLayout.LayoutParams.WRAP_CONTENT);
        mTvPersonTitle =  new TextView(mContext);
        mTvPersonTitle.setLayoutParams(textTitleLayout);
        mTvPersonTitle.setTextSize(15);
        mTvPersonTitle.setTextColor(mContext.getResources().getColor(R.color.meeting_gray));
        mTvPersonTitle.setText(mStrTitle);
        this.addView(mTvPersonTitle);

        LinearLayout.LayoutParams textLayout = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1);
        textLayout.setMargins(20,0,20,0);
        mTvPerson = new TextView(mContext);
        mTvPerson.setLayoutParams(textLayout);
        mTvPerson.setTextSize(15);
        mTvPerson.setTextColor(mContext.getResources().getColor(R.color.black));
        mTvPerson.setGravity(Gravity.LEFT);
        mTvPerson.setText("");
        this.addView(mTvPerson);

        LinearLayout.LayoutParams imageLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mIvDirect = new ImageView(mContext);
        mIvDirect.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_right_on));
        mIvDirect.setLayoutParams(imageLayout);
        this.addView(mIvDirect);

    }

    public void setUserText(String text){
        mTvPerson.setText(text);
    }


    public  formData getUserInfo(){
        return mFormData;
    }

    @Override
    public void onClick(View v) {
        if(v == this){
            FormViewUtil.hideSoftInputWindow(mContext,this);
            if(mContext instanceof CreateNewApplyActivity){
                String type = mFormData.getInputType();
                String id = (String)this.getTag();
                if(type.equals(FormViewUtil.APPROVE_PERSON_INPUT)){
                    ((CreateNewApplyActivity)mContext).accessUser(CreateNewApplyActivity.PROMISE_TYPE,id);
                }else if(type.equals(FormViewUtil.CC_PERSON_INPUT)){
                    ((CreateNewApplyActivity)mContext).accessUser(CreateNewApplyActivity.CC_TYPE,id);
                }
            }
        }
    }
}
