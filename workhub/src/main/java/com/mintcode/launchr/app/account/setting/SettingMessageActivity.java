package com.mintcode.launchr.app.account.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;

/*
 * create by Stephen He 2015/9/10
 */
public class SettingMessageActivity extends BaseActivity {
	// 返回
	private ImageView mIvBack;

	// 日程
	private CheckBox mCheSchedule;

	// 会议
	private CheckBox mCheMetting;

	// 审批
	private CheckBox mCheApproval;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_message);

		initView();

		// 获取消息设置控件的状态，并设置
		setToggleState();
	}

	public void initView() {
		mIvBack = (ImageView) findViewById(R.id.iv_message_back);

		mCheSchedule = (CheckBox) findViewById(R.id.ch_manage_schedule);
		mCheMetting = (CheckBox) findViewById(R.id.ch_manage_meeting);
		mCheApproval = (CheckBox) findViewById(R.id.ch_manage_approval);

		mIvBack.setOnClickListener(this);
		mCheSchedule.setOnClickListener(this);
		mCheMetting.setOnClickListener(this);
		mCheApproval.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mIvBack) {
			// 返回操作
			finish();
		} else if (v == mCheSchedule) {
			// 日程操作

		} else if (v == mCheMetting) {
			// 会议操作

		} else if (v == mCheApproval) {
			// 审批操作

		}
	}

	// 获取消息设置控件的状态，并设置
	public void setToggleState() {
		mCheSchedule.setChecked(false);
		mCheMetting.setChecked(true);
		mCheApproval.setChecked(false);
	}
}
