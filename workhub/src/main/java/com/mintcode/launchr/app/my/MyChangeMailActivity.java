package com.mintcode.launchr.app.my;

import android.os.Bundle;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;

/**
 * @author StephenHe 2015/9/17 修改邮箱
 */
public class MyChangeMailActivity extends BaseActivity {
	/** 返回 */
	private ImageView mIvBack;

	/** 登录密码 */
	private EditText mEditPassword;

	/** 新邮箱 */
	private EditText mEditNewMail;

	/** 验证码 */
	private EditText mEditIdentifyingCode;

	/** 确认修改 */
	private Button mBtnOkChange;

	/** 发送验证码 */
	private TextView mTvSendIdentifyingCode;

	/** 显示密码 */
	private CheckBox mIvShowPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_change_mail);

		initView();
	}

	public void initView() {
		mIvBack = (ImageView) findViewById(R.id.iv_title_back);

		mEditPassword = (EditText) findViewById(R.id.edit_logn_password);
		mEditNewMail = (EditText) findViewById(R.id.edit_new_mail);
		mEditIdentifyingCode = (EditText) findViewById(R.id.edit_identifying_code);
		mTvSendIdentifyingCode = (TextView) findViewById(R.id.tv_identifying_code_send);
		mBtnOkChange = (Button) findViewById(R.id.btn_ok_change);
		mIvShowPassword = (CheckBox) findViewById(R.id.iv_logn_password);

		mIvBack.setOnClickListener(this);

		mTvSendIdentifyingCode.setOnClickListener(this);
		mBtnOkChange.setOnClickListener(this);
		mIvShowPassword.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mIvBack) {
			finish();
		} else if (v == mBtnOkChange) {
			okChange();
		} else if (v == mTvSendIdentifyingCode) {
			sendIdentifyingCode();
		} else if (v == mIvShowPassword) {
			showpassword();
		}
	}

	/** 确认修改 */
	private void okChange() {

	}

	/** 发送验证码 */
	private void sendIdentifyingCode() {

	}

	/** 显示密码 */
	private void showpassword() {
		if(mIvShowPassword.isChecked()){
			mEditPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}else{
			mEditPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
		 CharSequence text = mEditPassword.getText();
		 if (text instanceof Spannable) {
		 Spannable spanText = (Spannable) text;
		 Selection.setSelection(spanText, text.length());// 将光标移动到最后
		 }
	}
}
