package com.mintcode.launchr.more.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.util.AudioTipHelper;
import com.mintcode.launchr.widget.SmoothCheckBox;

/**
 * Created by KevinQiao on 2016/2/18.
 */
public class NewMsgNoticeActivity extends BaseActivity implements SmoothCheckBox.OnCheckChangedListener {

    /** 返回按钮 */;
    private ImageView mIvBack;

    /** 声音 */
    private SmoothCheckBox mCbSound;

    /** 振动 */
    private SmoothCheckBox mCbVibration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_msg_notice);

        initView();
        initData();

    }

    /**
     * 实例化view
     */
    private void initView(){
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mCbSound = (SmoothCheckBox) findViewById(R.id.cb_sound);
        mCbVibration  = (SmoothCheckBox) findViewById(R.id.cb_vibration);

        mIvBack.setOnClickListener(this);
        mCbSound.setOnCheckChangedListener(this);
        mCbVibration.setOnCheckChangedListener(this);

    }

    private void initData(){
        boolean isSound =  AudioTipHelper.getSoundAction(this);// 是否开启铃声
        boolean isVibration =  AudioTipHelper.getVibrationAction(this);// 是否开启振动
        mCbSound.setChecked(isSound);
        mCbVibration.setChecked(isVibration);
    }

    @Override
    public void onClick(View v) {
        if(v == mIvBack){
            finish();
        }

    }


    @Override
    public void oncheckChange(View v, boolean b) {
        if(v == mCbSound){
            AudioTipHelper.saveSoundAction(this, b);
        }else if(v == mCbVibration){
            AudioTipHelper.saveVibrationAction(this, b);
        }
    }
}
