package com.mintcode.launchr.activity.workhub;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;

/**
 * Created by StephenHe on 2016/4/8.
 */
public class GetBackPhoneActivity extends BaseActivity {
    /** 返回*/
    private ImageView mIvBack;

    /** 手机号*/
    private EditText mEditPhoneNumber;

    /** 验证码*/
    private EditText mEditIdentify;

    /** 新密码*/
    private EditText mEditNewPassword;

    /** 获取验证码*/
    private TextView mTvGetIdentify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_forget_back_phone);

        initView();
    }

    private void initView(){
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvGetIdentify = (TextView) findViewById(R.id.tv_get_identify);
        mEditPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);
        mEditIdentify = (EditText) findViewById(R.id.edit_identify);
        mEditNewPassword = (EditText) findViewById(R.id.edit_new_password);

        mIvBack.setOnClickListener(this);
        mTvGetIdentify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mIvBack){
            finish();
        }else if(v == mTvGetIdentify){

        }
    }
}
