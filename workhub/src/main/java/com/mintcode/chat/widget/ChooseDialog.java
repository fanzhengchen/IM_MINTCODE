package com.mintcode.chat.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.mintcode.launchr.R;

/**
 * Created by StephenHe on 2016/4/8.
 */
public class ChooseDialog extends Dialog implements View.OnClickListener{
    private TextView mTvTitle;

    private TextView mTvCancel;

    public ChooseDialog(Context context) {
        super(context);
    }

    public ChooseDialog(Context context, int theme) {
        super(context, theme);
    }

    protected ChooseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_choose);

        initView();
    }

    private void initView(){
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvCancel = (TextView) findViewById(R.id.tv_canel);

        mTvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mTvCancel){
            dismiss();
        }
    }

    /** 设置标题*/
    public void setDialogTitle(String title){
        mTvTitle.setText(title);
    }
}
