package com.mintcode.launchr.activity;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.PersonalInfoUtil;
import com.way.view.gesture.UnlockGesturePasswordActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * @author KevinQiao
 *
 */
public class StartPageActivity extends BaseActivity {

	/** handler */
	private Handler mHandler;
	
	/** 工具类 */
	private PersonalInfoUtil mUtil;

	private RelativeLayout mRelLaunchr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_page);
		initView();
		start();
	}

	/**
	 * 实例化view
	 */
	private void initView(){
		mHandler = new Handler();
		mUtil = new PersonalInfoUtil(this);
		mRelLaunchr = (RelativeLayout) findViewById(R.id.rel_launchr);

		if(LauchrConst.IS_WORKHUB){
			mRelLaunchr.setBackgroundResource(R.drawable.workhub_lauch_bg);
		} else {
			mRelLaunchr.setBackgroundResource(R.drawable.start_page_view);
		}

	}


	
	
	
	/**
	 * 起始页
	 */
	private void start(){
		Runnable r = new Runnable() {
			@Override
			public void run() {
				int index = mUtil.getIntVaule(Const.KEY_GESTURE);
				Intent intent;
				if (index == 0) {

					 if(LauchrConst.IS_WORKHUB){
						intent = new Intent(getThis(), LoginWorkHubActivity.class);
					} else {
						intent = new Intent(getThis(), LoginActivity.class);
					}
				}else if (index == 1) {
					intent = new Intent(getThis(), UnlockGesturePasswordActivity.class);
				}else if (index == 2) {
					intent = new Intent(getThis(), MainActivity.class);
				}else{

					if(LauchrConst.IS_WORKHUB){
						intent = new Intent(getThis(), LoginWorkHubActivity.class);
					} else {
						intent = new Intent(getThis(), WelComActivity.class);
					}

				}
				startActivity(intent);
				getThis().finish();
			}
		};
		
		mHandler.postDelayed(r, 2000);
	}
	
	/**
	 * 获取当前类对象
	 * @return
	 */
	private StartPageActivity getThis(){
		return this;
	}
	
}
