package com.mintcode.launchr.app.account.setting;

import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mintcode.launchr.R;
import com.mintcode.launchr.activity.MainActivity;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.util.AppUtil;


/*
 * create by Stephen He 2015/9/9
 */
public class SettingLanguageActivity extends BaseActivity {
	// 返回
	private ImageView mIvBack;

	// 中文
	private CheckBox mCheChinese;

	// 英文
	private CheckBox mCheEnglish;

	// 日文
	private CheckBox mCheJapan;
	
	//跟随系统
	private CheckBox mCheSystem;
	
	private RelativeLayout mRlSystem;
	
	private RelativeLayout mRlJaPan;
	
	private RelativeLayout mRlChiane;
	
	private RelativeLayout mRlEnglish;
	
	private String mType,mStatcType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_language);
		SharedPreferences sp = getSharedPreferences(getPackageName(),MODE_PRIVATE);
		mStatcType = mType = sp.getString("language", "");
		initView();
		initSelect();
	}

	

	public void initView() {
		mIvBack = (ImageView) findViewById(R.id.iv_language_back);

		mCheChinese = (CheckBox) findViewById(R.id.ch_language_Chinses);
		mCheEnglish = (CheckBox) findViewById(R.id.ch_language_english);
		mCheJapan = (CheckBox) findViewById(R.id.ch_language_japan);
		mCheSystem = (CheckBox) findViewById(R.id.ch_language_system);
		
		mRlSystem = (RelativeLayout) findViewById(R.id.rl_language_system);
		mRlChiane = (RelativeLayout) findViewById(R.id.rl_language_Chinese);
		mRlEnglish = (RelativeLayout) findViewById(R.id.rl_language_english);
		//TODO:暂时没有英文
		mRlEnglish.setVisibility(View.GONE);
		mRlJaPan = (RelativeLayout) findViewById(R.id.rl_language_japan);

		mIvBack.setOnClickListener(this);
		mRlSystem.setOnClickListener(this);
		mRlChiane.setOnClickListener(this);
		mRlEnglish.setOnClickListener(this);
		mRlJaPan.setOnClickListener(this);
		Locale.getDefault();
	}
	
	private void initSelect() {
		
		if (mType.equals("cn")) {
			// 设置语言为中文
			setCheckBox(false, false, true,false);
			mType = "cn";
			resetLanguage("zh-cn");
		} else if (mType.equals("en")) {
			// 设置语言为英文
			setCheckBox(false, true, false,false);
			mType = "en";
			resetLanguage("en-eu");
		} else if (mType.equals("jp")) {
			// 设置语言为日文
			setCheckBox(true, false, false,false);
			mType = "jp";
			resetLanguage("jp-ja");
		}else if (mType.equals("system")){
			setCheckBox(false, false, false,true);
			mType = "system";
			resetLanguage("jp-ja");
		}
	}

	private void resetLanguage(String language){
		LauchrConst.header.setLanguage(language);
		AppUtil.getInstance(this).saveValue(LauchrConst.KEY_LANGUAGE,language);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mIvBack) {
			if(mType.equals(mStatcType)){
				this.finish();
			}else{
				changeAppLanguage(this.getResources(),mType);
				reStartApp();
			}
			
		} else if (v == mRlChiane) {
			// 设置语言为中文
			setCheckBox(false, false, true,false);
			mType = "cn";
			resetLanguage("zh-cn");
		} else if (v == mRlEnglish) {
			// 设置语言为英文
			setCheckBox(false, true, false,false);
			mType = "en";
			resetLanguage("en-eu");
		} else if (v == mRlJaPan) {
			// 设置语言为日文
			setCheckBox(true, false, false,false);
			mType = "jp";
			resetLanguage("jp-ja");
		} else if(v == mRlSystem){
			// 设置语言为跟系统
			setCheckBox(false, false, false,true);
			mType = "system";
			resetLanguage("jp-ja");
		}
	}

	// 设置CheckBox状态
	public void setCheckBox(boolean b1, boolean b2, boolean b3,boolean b4) {
		mCheJapan.setChecked(b1);
		mCheEnglish.setChecked(b2);
		mCheChinese.setChecked(b3);
		mCheSystem.setChecked(b4);
	}
	
	
	
	// 语言设置后，需要重启应用
		private void reStartApp() {
			Intent it = new Intent(this,MainActivity.class); // MainActivity是你想要重启的activity
			it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(it);
		}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(mType.equals(mStatcType)){
				this.finish();
			}else{
				changeAppLanguage(this.getResources(),mType);
				reStartApp();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
