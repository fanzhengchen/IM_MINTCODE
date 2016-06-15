package com.mintcode.launchr.app.newApproval.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.mintcode.launchr.R;
import com.mintcode.launchr.app.newApproval.Entity.FormMutilDataEntity;
import com.mintcode.launchr.app.newApproval.Entity.FormSingleDataEntity;
import com.mintcode.launchr.app.newApproval.activity.CreateNewApplyActivity;
import com.mintcode.launchr.pojo.entity.FormCheckBoxEntity;
import com.mintcode.launchr.pojo.entity.formData;
import com.mintcode.launchr.util.TTJSONUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/4/14.
 */
public class SelectLinearLayoutView extends BaseLinearLayoutView implements View.OnClickListener{



    private TextView mTvSelectTitle;
    private TextView mTvSelected;
    private ImageView mIvDirect;

    private String mStrTitle;

    private List<FormCheckBoxEntity> mFormCheckBoxs = new ArrayList<>();

    public SelectLinearLayoutView(Context context,formData data) {
        super(context,data);
        mStrTitle = mFormData.getLabelText();
        initView();
    }

    public SelectLinearLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectLinearLayoutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    protected void initView() {

        this.setOnClickListener(this);
        this.setOrientation(HORIZONTAL);

        LinearLayout.LayoutParams textTitleLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mTvSelectTitle =  new TextView(mContext);
        mTvSelectTitle.setLayoutParams(textTitleLayout);
        mTvSelectTitle.setTextSize(15);
        mTvSelectTitle.setTextColor(mContext.getResources().getColor(R.color.meeting_gray));
        mTvSelectTitle.setText(mStrTitle);
        this.addView(mTvSelectTitle);

        LinearLayout.LayoutParams textLayout = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1);
        textLayout.setMargins(20,0,20,0);
        mTvSelected = new TextView(mContext);
        mTvSelected.setLayoutParams(textLayout);
        mTvSelected.setTextSize(15);
        mTvSelected.setTextColor(mContext.getResources().getColor(R.color.black));
        mTvSelected.setGravity(Gravity.LEFT);
        mTvSelected.setText("");
        this.addView(mTvSelected);

        LinearLayout.LayoutParams imageLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mIvDirect = new ImageView(mContext);
        mIvDirect.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_right_on));
        mIvDirect.setLayoutParams(imageLayout);
        this.addView(mIvDirect);

    }
    public void setSelectedText(String text){
        mTvSelected.setText(text);
    }
    public void setSeletedData(List<FormCheckBoxEntity> data){
        mFormCheckBoxs.clear();
        mFormCheckBoxs = data;
    }
    public String getJsonString(){

        String type = mFormData.getInputType();
        boolean required = mFormData.isRequired();
        if(mFormCheckBoxs != null && mFormCheckBoxs.size() > 0){
            if(type.equals(FormViewUtil.SINGLE_SELECT_INPUT)){
                mSingleEntity.setKey(mFormData.getKey());
                mSingleEntity.setValue(mFormCheckBoxs.get(0).getValue());
                return TTJSONUtil.convertObjToJson(mSingleEntity);
            }else if(type.equals(FormViewUtil.MUTIL_SELECT_INPUT)){
                mMutilEntity.setKey(mFormData.getKey());
                List<String> values = new ArrayList<>();
                for(FormCheckBoxEntity data : mFormCheckBoxs){
                    values.add(data.getValue());
                }
                mMutilEntity.setValue(values);
                return TTJSONUtil.convertObjToJson(mMutilEntity);
            }
        }else if(required){
                FormViewUtil.BOOL_NO_NULL = false;
             FormViewUtil.mustInputToast(mContext,mStrTitle + "IS NULL");
        }
        return null;
    }
    @Override
    public void onClick(View v) {
        if(v == this){
            if(mContext instanceof CreateNewApplyActivity){
              ((CreateNewApplyActivity)mContext).selectFormBox(this,mFormData,mFormCheckBoxs);
            }
        }
    }
}
