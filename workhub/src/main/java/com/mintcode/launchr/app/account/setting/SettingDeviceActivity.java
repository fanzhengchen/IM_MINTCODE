package com.mintcode.launchr.app.account.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;

/*
 * create by Stephen He 2015/9/10
 */
public class SettingDeviceActivity extends BaseActivity {
	// 返回操作
	private ImageView mIvBack;

	// 手机名字
	private TextView mTvPhoneName;
	// 手机正在使用
	private TextView mTvPhoneUse;
	// 手机正在使用
	private ImageView mIvPhoneUse;

	// 浏览器名字
	private TextView mTvBrowserName;
	// 浏览器正在使用
	private TextView mTvBrowserUse;
	// 浏览器正在使用
	private ImageView mIvBrowserUse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_device);

		initView();

		initState();
	}

	public void initView() {
		mIvBack = (ImageView) findViewById(R.id.iv_device_back);

		mTvPhoneName = (TextView) findViewById(R.id.tv_device_phone_name);
		mTvPhoneUse = (TextView) findViewById(R.id.tv_device_phone_use);
		mIvPhoneUse = (ImageView) findViewById(R.id.iv_device_phone_use);
		mTvBrowserName = (TextView) findViewById(R.id.tv_device_browser_name);
		mTvBrowserUse = (TextView) findViewById(R.id.tv_device_browser_use);
		mIvBrowserUse = (ImageView) findViewById(R.id.iv_device_browser_use);

		mIvBack.setOnClickListener(this);
		mTvPhoneUse.setOnClickListener(this);
		mIvPhoneUse.setOnClickListener(this);
		mTvBrowserUse.setOnClickListener(this);
		mIvBrowserUse.setOnClickListener(this);
	}

	// 初始化控件的状态
	public void initState() {
		mTvPhoneName.setText("iPhone6");
		mTvBrowserName.setText("Chrome 32.0.1.28");
		mTvPhoneUse.setVisibility(View.VISIBLE);
		mIvPhoneUse.setVisibility(View.GONE);
		mTvBrowserUse.setVisibility(View.GONE);
		mIvBrowserUse.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mIvBack) {
			// 返回操作
			finish();
		}
	}
}
