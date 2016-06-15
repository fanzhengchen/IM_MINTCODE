package com.mintcode.launchr.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;

/**
 * Created by JulyYu on 2016/4/11.
 */
public class SignExistDialog extends Dialog implements View.OnClickListener{

    private ImageView mIvIcon;
    private TextView mTvContent;
    private TextView mTvLogin;
    private TextView mTvMotify;

    private String mStrContent;
    private Context mContext;

    public SignExistDialog(Context context,String text) {
        super(context);
        mStrContent = text;
        mContext = context;
    }

    public SignExistDialog(Context context, int theme) {
        super(context, theme);
    }

    protected SignExistDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_sign_exist);
        initView();
    }

    private void initView() {
        mIvIcon = (ImageView)findViewById(R.id.iv_sign_icon);
        mTvContent = (TextView)findViewById(R.id.tv_sign_exist);
        mTvLogin = (TextView)findViewById(R.id.tv_login);
        mTvMotify = (TextView)findViewById(R.id.tv_motify);

        mTvLogin.setOnClickListener(this);
        mTvMotify.setOnClickListener(this);
        mTvContent.setText(mStrContent);
    }

    @Override
    public void onClick(View v) {
        if(v == mTvLogin){
            ((BaseActivity)mContext).finish();
        }else if(v == mTvMotify){
            this.dismiss();
        }
    }
}
