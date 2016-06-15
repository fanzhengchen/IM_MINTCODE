package com.mintcode.launchr.app.account.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.mintcode.chat.util.Keys;
import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;


/*
 * create by Stephen He 2015/9/9
 */
public class SettingInputActivity extends BaseActivity {
	// 返回
	private ImageView mIvBack;

	private CheckBox mCheckBoxReturn;

	private CheckBox mCheckBoxSend;

	private View mRelReturn;
	private View mRelSend;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private int mInputType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_input);
		mSharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		mInputType = mSharedPreferences.getInt(Keys.INPUT_ENTER_TYPE, 0);
		initView();
		initSelect();
	}

	public void initView() {
		mIvBack = (ImageView) findViewById(R.id.iv_language_back);
		mRelReturn = findViewById(R.id.layout_input_green);
		mRelSend = findViewById(R.id.layout_input_blue);
		mCheckBoxReturn = (CheckBox) findViewById(R.id.cb_input_return);
		mCheckBoxSend = (CheckBox) findViewById(R.id.cb_input_send);

		mIvBack.setOnClickListener(this);
		mRelReturn.setOnClickListener(this);
		mRelSend.setOnClickListener(this);
	}
	
	private void initSelect() {
		switch (mInputType){
			case Keys.INPUT_ENTER_DEFAULT:
				setCheckBox(Keys.INPUT_ENTER_RETURN);
				break;
			case Keys.INPUT_ENTER_RETURN:
				setCheckBox(Keys.INPUT_ENTER_RETURN);
				break;
			case Keys.INPUT_ENTER_SEND:
				setCheckBox(Keys.INPUT_ENTER_SEND);
				break;
			default:
				break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mIvBack) {
			this.finish();
		} else if (v == mRelReturn) {
			setCheckBox(Keys.INPUT_ENTER_RETURN);
		} else if (v == mRelSend) {
			setCheckBox(Keys.INPUT_ENTER_SEND);
		}
	}

	// 设置CheckBox状态
	public void setCheckBox(int inputType) {
		switch (inputType){
			case Keys.INPUT_ENTER_DEFAULT:
				mCheckBoxReturn.setChecked(true);
				mCheckBoxSend.setChecked(false);
				mCheckBoxReturn.setBackgroundResource(R.drawable.input_selected);
				mCheckBoxSend.setBackgroundResource(0);
				break;
			case Keys.INPUT_ENTER_RETURN:
				mCheckBoxReturn.setChecked(true);
				mCheckBoxSend.setChecked(false);
				mCheckBoxReturn.setBackgroundResource(R.drawable.input_selected);
				mCheckBoxSend.setBackgroundResource(0);
				break;
			case Keys.INPUT_ENTER_SEND:
				mCheckBoxReturn.setChecked(false);
				mCheckBoxSend.setChecked(true);
				mCheckBoxReturn.setBackgroundResource(0);
				mCheckBoxSend.setBackgroundResource(R.drawable.input_selected);
				break;
			default:
				break;
		}
		mEditor.putInt(Keys.INPUT_ENTER_TYPE, inputType);
		mEditor.commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
