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
 * @author FlandreYi
 *	restart(重复)的页面返回选择的内容
 */
public class RestartActivity extends BaseActivity implements OnItemClickListener{
	
	private TextView mTvMessageNum;
	private ImageView mIvBack;
	private ListView mLvResart;
	
	private String[] data;
	public static final String RESTART_INFO ="RestartInfo";
	public static final String REPEATTYPE = "repeatType";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restart);
		initData();
		initView();
	}
	private void initData(){
		 data = new String[]{
					getResources().getString(R.string.calendar_never),
					getResources().getString(R.string.calendar_everyday),
					getResources().getString(R.string.calendar_everyweek),
					getResources().getString(R.string.calendar_eveymonth),
					getResources().getString(R.string.calendar_everyyear)
			};
//		String select = getIntent().getStringExtra(RESTART_INFO);
	}
	private void initView(){
		mTvMessageNum=(TextView) findViewById(R.id.tv_message_num);
		mIvBack=(ImageView) findViewById(R.id.iv_back);
		mLvResart=(ListView) findViewById(R.id.lv_restart);
		ItemAdapter adapter=new ItemAdapter(this, data);
		mLvResart.setAdapter(adapter);
		mLvResart.setOnItemClickListener(this);
		mIvBack.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if(v==mIvBack){
			finish();
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i=new Intent();
		i.putExtra(RESTART_INFO, data[position]);
		i.putExtra(REPEATTYPE, position);
		setResult(RESULT_OK, i);
		finish();
	}
	
}

















