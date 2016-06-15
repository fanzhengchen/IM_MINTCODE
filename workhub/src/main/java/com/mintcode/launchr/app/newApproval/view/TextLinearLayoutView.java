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
import com.mintcode.launchr.app.newApproval.activity.ApproveDetailActivity;
import com.mintcode.launchr.pojo.entity.formData;
import com.mintcode.launchr.util.TTDensityUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.widget.CollapsibleTextView;

import java.util.List;


/**
 * Created by JulyYu on 2016/4/15.
 */
public class TextLinearLayoutView extends BaseLinearLayoutView{


    private boolean mBoolFirstEdit = false;

    private TextView mTvTitle;
    private TextView mTvContent;
    private CollapsibleTextView mTvPersonName;

    public TextLinearLayoutView(Context context, formData data) {
        super(context, data);
        initView();
    }

    public TextLinearLayoutView(Context context, formData data,String key) {
        super(context,data,key);
        initView();
    }

    public TextLinearLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextLinearLayoutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void initView(){

        String title = mFormData.getLabelText();

        int with = TTDensityUtil.dip2px(mContext,80);

        LinearLayout.LayoutParams textTitleLayout = new LinearLayout.LayoutParams(with, LinearLayout.LayoutParams.WRAP_CONTENT);
        mTvTitle =  new TextView(mContext);
        mTvTitle.setLayoutParams(textTitleLayout);
        mTvTitle.setTextSize(15);
        mTvTitle.setTextColor(mContext.getResources().getColor(R.color.meeting_gray));
        mTvTitle.setText(title);
        this.addView(mTvTitle);
        setFormJsonData();
    }

    private void addContentTextView(){
        LinearLayout.LayoutParams textLayout = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1);
        textLayout.setMargins(20,0,20,0);
        mTvContent = new TextView(mContext);
        mTvContent.setLayoutParams(textLayout);
        mTvContent.setTextSize(15);
        mTvContent.setTextColor(mContext.getResources().getColor(R.color.black));
        mTvContent.setGravity(Gravity.LEFT);
        mTvContent.setText("");
        this.addView(mTvContent);
    }

    private void addPersonTextView(){
        LinearLayout.LayoutParams textLayout = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1);
        textLayout.setMargins(20, 0, 20, 0);
        mTvPersonName = new CollapsibleTextView(mContext,null);
        mTvPersonName.setLayoutParams(textLayout);
        mTvPersonName.setGravity(Gravity.LEFT);
        this.addView(mTvPersonName);
    }
    //提取表单数据
    public void setFormJsonData(){
            String type = mFormData.getInputType();
            String content = "";
            if(type.equals(FormViewUtil.SINGLE_TEXT_INPUT) || type.equals(FormViewUtil.MUTIL_TEXT_INPUT)||type.equals(FormViewUtil.SINGLE_SELECT_INPUT)){
                addContentTextView();
                content = mStrFormJson;
                if(content != null && !"".equals(content)){
                    mTvContent.setText(content);
                }else {
                    this.setVisibility(View.GONE);
                }
            }else if(type.equals(FormViewUtil.MUTIL_SELECT_INPUT)){
                addContentTextView();
                content = FormViewUtil.getJsonListToString(mStrFormJson);
                if(content != null){
                    mTvContent.setText(content);
                }else {
                    this.setVisibility(View.GONE);
                }
            }else if(type.equals(FormViewUtil.APPROVE_PERSON_INPUT)){
                if(mContext instanceof ApproveDetailActivity){
                    addPersonTextView();
                    List<String>  person = ((ApproveDetailActivity) mContext).getApprovPerson();
                    String personStr = ((ApproveDetailActivity) mContext).getApprovPersonStr();
                    if(personStr != null && person != null && person.size() > 0){
                        mTvPersonName.setText(personStr,person);
                    }else {
                        this.setVisibility(View.GONE);
                    }
                }
            }else if(type.equals(FormViewUtil.CC_PERSON_INPUT)){
                if(mContext instanceof ApproveDetailActivity){
                    addPersonTextView();
                    List<String>  person = ((ApproveDetailActivity) mContext).getCCPerson();
                    String personStr = ((ApproveDetailActivity) mContext).getCCPersonStr();
                    if(personStr != null && person != null && person.size() > 0){
                        mTvPersonName.setText(personStr,person);
                    }else {
                        this.setVisibility(View.GONE);
                    }
                }
            }else if(type.equals(FormViewUtil.TIME_INPUT)){
                addContentTextView();
                String timeType = mFormData.getTimeType();
                if(timeType.equals("TimeSlot")){
                    FormTimesDataEntity.Timevalues time  = TTJSONUtil.convertJsonToCommonObj(mStrFormJson, FormTimesDataEntity.Timevalues.class);
                    if(time != null ){
                        long startTime = time.getStartTime();
                        long endTime = time.getEndTime();
                        FormViewUtil.setTimeDisplayFormat(startTime,endTime,mTvContent);
                    }else {
                        this.setVisibility(View.GONE);
                    }
                }else if(timeType.equals("TimePoint")){
                    if(Long.valueOf(mStrFormJson) != null && Long.valueOf(mStrFormJson) >0 ){
                        FormViewUtil.setSingeTimeDisplayFormat(Long.valueOf(mStrFormJson), mTvContent);
                    }else {
                        this.setVisibility(View.GONE);
                    }
                }
            }else if(type.equals(FormViewUtil.APPROVE_LIMIT_INPUT)){
                addContentTextView();
                if(mStrFormJson != null){
                    FormTimeDataEntity.Timevalues time  = TTJSONUtil.convertJsonToCommonObj(mStrFormJson, FormTimeDataEntity.Timevalues.class);
                    if(time != null){
                        long limitTime = time.getDeadline();
                        if(Long.valueOf(limitTime) != null && Long.valueOf(limitTime) >0 ){
                            FormViewUtil.setSingeTimeDisplayFormat(Long.valueOf(limitTime), mTvContent);
                        }else {
                            this.setVisibility(View.GONE);
                        }
                    }else {
                        this.setVisibility(View.GONE);
                    }
                }
            }
        }
}
