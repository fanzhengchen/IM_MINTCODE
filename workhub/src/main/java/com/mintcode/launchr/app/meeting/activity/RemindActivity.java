package com.mintcode.launchr.app.meeting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.app.meeting.adapter.ItemAdapter;
import com.mintcode.launchr.base.BaseActivity;

/**
 * 
 * @author FlandreYi remind（提醒）的页面，返回选择的内容
 */
public class RemindActivity extends BaseActivity implements OnItemClickListener {

	private TextView mTvMessageNum;
	private ImageView mIvBack;
	private ListView mLvResart;

	private String[] data;
	private int[] state = { 0, 100, 101, 102, 103, 104, 105, 106, 107, 108,
			0,200, 201, 202, 203 };
	public static final String REMIND_INFO = "RemindInfo";
	public static final String REMINDTYPE = "remindType";
	
	private boolean isAllDay;

	private String mStr = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* 复用了restart的layout */
		setContentView(R.layout.activity_restart);
		initData();
		initView();
	}

	private void initData() {
		isAllDay = getIntent().getBooleanExtra("isAllDay", false);
		if (!isAllDay) {
			data = new String[] {
					getResources().getString(R.string.calendar_not_remind),
					getResources().getString(R.string.calendar_before_5),
					getResources().getString(R.string.calendar_before_10),
					getResources().getString(R.string.calendar_before_15),
					getResources().getString(R.string.calendar_before_30),
					getResources().getString(R.string.calendar_before_one_hour),
					getResources().getString(R.string.calendar_before_two_hour),
					getResources().getString(R.string.calendar_the_day),
					getResources().getString(R.string.calendar_before_two_day),
					getResources().getString(R.string.calendar_before_one_week), };
		} else {
			data = new String[] {
					getResources().getString(R.string.calendar_not_remind),
					getResources().getString(R.string.calendar_the_day),
					getResources().getString(R.string.calendar_before_one_day),
					getResources().getString(R.string.calendar_before_two_week),
					getResources().getString(R.string.calendar_before_one_week) };
		}

		mStr = getIntent().getStringExtra(REMIND_INFO);
	}

	private void initView() {
		mTvMessageNum = (TextView) findViewById(R.id.tv_message_num);
		mIvBack = (ImageView) findViewById(R.id.iv_back);
		mLvResart = (ListView) findViewById(R.id.lv_restart);
		ItemAdapter adapter = new ItemAdapter(this, data);
		adapter.setStr(mStr);
		mLvResart.setAdapter(adapter);
		mLvResart.setOnItemClickListener(this);
		mIvBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v == mIvBack) {
			finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i = new Intent();
		i.putExtra(REMIND_INFO, data[position]);
		if (isAllDay) {
			position = position + 10;
		}
		i.putExtra(REMINDTYPE, state[position]);
		setResult(RESULT_OK, i);
		finish();
	}

}
