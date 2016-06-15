package com.mintcode.launchr.app.meeting.activity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.app.meeting.MeetingTimerDialog;
import com.mintcode.launchr.app.meeting.view.MeetingContentView;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.pojo.entity.EventEntity;
import com.mintcode.launchr.timer.listener.OnTimerListener;
import com.mintcode.launchr.util.TimeFormatUtil;

/**
 * 会议主界面
 * @author KevinQiao
 *
 */
public class MeetingActivity extends BaseActivity implements OnTimerListener{



	public static final String EVENT_DATA =  "event_data";
	public static final String USER_ID = "user_id";
	public static final String USER_NAME = "user_name";
	public static final String MEETING_TIME = "meeting_time";
	public static final String USER_HASHMAP = "user_hashmap";

	private ImageView mIvBack;
	private MeetingContentView mMeetingContentView;
	private long mNow;
	@Override

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_timer);
		Intent intent = getIntent();
		MeetingTimerDialog.IntentData data = (MeetingTimerDialog.IntentData) intent.getSerializableExtra(EVENT_DATA);
		final ArrayList<String> uname = intent.getStringArrayListExtra(USER_ID);
		final ArrayList<String> tname = intent.getStringArrayListExtra(USER_NAME);
		mNow = intent.getLongExtra(MEETING_TIME, 0);
		if(mNow > 0){
			SimpleDateFormat time = new SimpleDateFormat("MM/dd(E)");
			((TextView)findViewById(R.id.tv_title)).setText(time.format(mNow));
		}
		final List<EventEntity> list = data.list;

		Bundle bundle = getIntent().getExtras();
		Serializable nameKey = bundle.getSerializable(USER_HASHMAP);
		final HashMap<String,String> table = (HashMap<String, String>)nameKey;

		mMeetingContentView = (MeetingContentView)findViewById(R.id.meeting_content_view);
		mMeetingContentView.post(new Runnable() {
			
			@Override
			public void run() {
				mMeetingContentView.setNow(mNow);
				mMeetingContentView.setEventDatas(list,uname,tname,table);
			}
		});
		initView();
	}
	
	private void initView(){
		mIvBack = (ImageView) findViewById(R.id.iv_back);
		mIvBack.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		if (v == mIvBack) {
			onBackPressed();
		}
	}

	@Override
	public void OnTimeChangeListenner(View view, int year, int month, int day,
			int hour, int minute, int type, boolean isEnd) {
		// TODO Auto-generated method stub
		
	}
	
	

	
}
