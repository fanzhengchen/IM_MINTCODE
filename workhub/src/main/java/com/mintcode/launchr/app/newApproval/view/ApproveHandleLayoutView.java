package com.mintcode.launchr.app.newApproval.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.app.newApproval.activity.ApproveDetailActivity;

/**
 * Created by JulyYu on 2016/4/18.
 */
public class ApproveHandleLayoutView extends RelativeLayout implements View.OnClickListener{

    protected Context mContext;
    private TextView mTvRefuse;
    private TextView mTvRecall;
    private TextView mTvPass;

    public ApproveHandleLayoutView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public ApproveHandleLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ApproveHandleLayoutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    protected void initView() {


        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setPadding(40, 20, 40, 20);
        linearLayout.setLayoutParams(linearParams);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1);
        layoutParams.setMargins(20,10,20,10);

        mTvRefuse = new TextView(mContext);
        mTvRefuse.setTextColor(mContext.getResources().getColor(R.color.task_priority_red));
        mTvRefuse.setTextSize(15);
        mTvRefuse.setBackgroundResource(R.drawable.shape_approval_refuse);
        mTvRefuse.setPadding(10, 5, 10, 5);
        mTvRefuse.setText(mContext.getString(R.string.accpect_refuse));
        mTvRefuse.setGravity(Gravity.CENTER);
        mTvRefuse.setOnClickListener(this);
        mTvRefuse.setLayoutParams(layoutParams);
        linearLayout.addView(mTvRefuse);

        mTvRecall = new TextView(mContext);
        mTvRecall.setTextColor(mContext.getResources().getColor(R.color.task_priority_yellow));
        mTvRecall.setTextSize(15);
        mTvRecall.setBackgroundResource(R.drawable.shape_approval_recall);
        mTvRecall.setPadding(10, 5, 10, 5);
        mTvRecall.setText(mContext.getString(R.string.accpect_recall));
        mTvRecall.setGravity(Gravity.CENTER);
        mTvRecall.setOnClickListener(this);
        mTvRecall.setLayoutParams(layoutParams);
        linearLayout.addView(mTvRecall);

        mTvPass = new TextView(mContext);
        mTvPass.setTextColor(mContext.getResources().getColor(R.color.green_launchr));
        mTvPass.setTextSize(15);
        mTvPass.setBackgroundResource(R.drawable.shape_approval_accept);
        mTvPass.setPadding(10, 5, 10, 5);
        mTvPass.setText(mContext.getString(R.string.accpect_pass));
        mTvPass.setGravity(Gravity.CENTER);
        mTvPass.setOnClickListener(this);
        mTvPass.setLayoutParams(layoutParams);
        linearLayout.addView(mTvPass);

        RelativeLayout.LayoutParams relatParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relatParams.addRule(CENTER_IN_PARENT);
        relatParams.setMargins(40,10,40,10);
        this.addView(linearLayout,relatParams);
    }

    @Override
    public void onClick(View v) {
        FormViewUtil.hideSoftInputWindow(mContext,this);
        if(v == mTvRefuse){
            refuseHandle();
        }else if(v == mTvPass){
            passHandle();
        }else if(v == mTvRecall){
            recalHandle();
        }
    }

    private void refuseHandle() {
            if(mContext instanceof ApproveDetailActivity){
                ApproveDetailActivity activity = ((ApproveDetailActivity)mContext);
                if(activity != null){
                    activity.refuseApply();
                }
            }
    }

    private void recalHandle() {
        if(mContext instanceof ApproveDetailActivity){
            ApproveDetailActivity activity = ((ApproveDetailActivity)mContext);
            if(activity != null){
                activity.reCallApply();
            }
        }
    }

    private void passHandle() {
        if(mContext instanceof ApproveDetailActivity){
            ApproveDetailActivity activity = ((ApproveDetailActivity)mContext);
            if(activity != null){
                activity.passApply();
            }
        }
    }
}
