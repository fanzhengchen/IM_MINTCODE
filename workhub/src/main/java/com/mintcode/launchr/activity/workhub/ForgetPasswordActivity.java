package com.mintcode.launchr.activity.workhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;

/**
 * Created by StephenHe on 2016/4/7.
 */
public class ForgetPasswordActivity extends BaseActivity {
    /** 邮箱找回*/
    private RelativeLayout mRelMailBack;

    /** 手机找回*/
    private RelativeLayout mRelPhoneBack;

    /** 返回*/
    private ImageView mIvBack;

    private RelativeLayout mRelContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initView();
    }

    private void initView(){
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mRelMailBack = (RelativeLayout) findViewById(R.id.rel_back_mail);
        mRelPhoneBack = (RelativeLayout) findViewById(R.id.rel_back_phone);
        mRelContent = (RelativeLayout) findViewById(R.id.rel_fragment_content);

        mIvBack.setOnClickListener(this);
        mRelMailBack.setOnClickListener(this);
        mRelPhoneBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mIvBack){
            this.finish();
        }if(v == mRelMailBack){
            Intent intent = new Intent(this, GetBackMailActivity.class);
            startActivity(intent);

            this.finish();
        }else if(v == mRelPhoneBack){
            Intent intent = new Intent(this, GetBackPhoneActivity.class);
            startActivity(intent);

            this.finish();
        }
    }

}
