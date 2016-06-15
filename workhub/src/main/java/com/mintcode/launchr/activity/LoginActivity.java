package com.mintcode.launchr.activity;


import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.IMAPI;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.api.UserApi.TaskId;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.LoginPOJO;
import com.mintcode.launchr.pojo.LoginValidatePOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.CompanyEntity;
import com.mintcode.launchr.pojo.entity.LoginValidateEntity;
import com.mintcode.launchr.pojo.entity.UserEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.PersonalInfoUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchrnetwork.OnResponseListener;
import com.way.view.gesture.CreateGesturePasswordActivity;
import com.way.view.gesture.UnlockGesturePasswordActivity;

import org.litepal.LitePalManager;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 登录页面
 * @author KevinQiao
 *
 */
public class LoginActivity extends BaseActivity implements OnFocusChangeListener, OnResponseListener
{
	

	/** 用户名输入控件 */
	private EditText mEtUserName;
	
	/** 密码输入控件  */
	private EditText mEtPassword;
	
	/** 登录按钮 */
	private Button mBtnLogin;
	
	/** scrollview */
	private ScrollView mScrollView;
	
	/** handler */
	private Handler mHandler;
	
	/**launchr Logo***/
	private ImageView mIvLaunchrLogo;
	/**底部云朵**/
	private ImageView mIvIcloud;
	/**用户登录布局**/
	private LinearLayout mLlUserLogin;
	/**用户邮箱**/
	private EditText mEdtUserMail;
	/**用户密码**/
	private EditText mEdtUserPassWord;
	/**登录**/
	private Button mBtnLoginIn;

	/** 试用申请按钮 */
	private TextView mTvTryApply;

	/** 忘记密码 */
	private  TextView mTvForgetPwd;

	private CheckBox mCbShowPassWord;
	
	
	private ScrollView mSc;
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_new);
		ButterKnife.bind(this);
		initViews();
		setAnimation();
	}

	

	private void initViews() {
		
		mIvLaunchrLogo = (ImageView)findViewById(R.id.iv_launchr_logo);
		mIvIcloud = (ImageView)findViewById(R.id.iv_launchr_icloud);
		mLlUserLogin = (LinearLayout)findViewById(R.id.ll_user_login);
		mEdtUserMail = (EditText)findViewById(R.id.edt_user_acount);
		mEdtUserPassWord = (EditText)findViewById(R.id.edt_user_passwd);
		mBtnLoginIn = (Button)findViewById(R.id.btn_login);
		mCbShowPassWord = (CheckBox)findViewById(R.id.cb_show_password);
		mLlUserLogin.setVisibility(View.INVISIBLE);
		mSc = (ScrollView) findViewById(R.id.sc);
		mTvTryApply = (TextView) findViewById(R.id.tv_try_apply);
		mTvForgetPwd = (TextView) findViewById(R.id.tv_forget_password);


		mBtnLoginIn.setOnClickListener(this);
		mCbShowPassWord.setOnClickListener(this);
		mTvTryApply.setOnClickListener(this);
		mTvForgetPwd.setOnClickListener(this);

		mEdtUserMail.setOnFocusChangeListener(this);
		mEdtUserPassWord.setOnFocusChangeListener(this);
		
		mHandler = new Handler();


	}
	private void setAnimation() {
		
		
		Animation mLogoAnimation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_and_alpha_totop);
		Animation mIcloudAnimation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.traslate_and_todwon);
		Animation mUserLoginAnimation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.alpha);
		mLogoAnimation.setFillAfter(true);
		mIcloudAnimation.setFillAfter(true);
		mUserLoginAnimation.setFillAfter(true);
		
		mIvLaunchrLogo.startAnimation(mLogoAnimation);
		mIvIcloud.startAnimation(mIcloudAnimation);
		mLlUserLogin.startAnimation(mUserLoginAnimation);
	}

	/**
	 * 显示新密码
	 */
	@OnClick(R.id.cb_show_password)
	 void showPassword() {
		
		if(mCbShowPassWord.isChecked()){
			mEdtUserPassWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}else{
			mEdtUserPassWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
		 CharSequence text = mEdtUserPassWord.getText();
		 if (text instanceof Spannable) {
		 Spannable spanText = (Spannable) text;
		 Selection.setSelection(spanText, text.length());// 将光标移动到最后
		 }
	}
	/**
	 * 登录处理逻辑
	 */
	@OnClick(R.id.btn_login)
	void accessLogin(){
		// 获取用户名
		String account = mEdtUserMail.getText().toString().trim();
		if ((account == null) || account.equals("")) {
			String strText = getResources().getString(R.string.toast_login_name_null);
			toast(strText);
			return ;
		}
		// 获取密码
		String password = mEdtUserPassWord.getText().toString().trim();
		if ((password == null) || password.equals("")) {
			String strText = getResources().getString(R.string.toast_pass_word_null);
			toast(strText);
			return ;
		}
		// 原登录操作
		UserApi api = UserApi.getInstance();
		api.loginValidate(this, account, password);
		showLoading();
	}
	@OnClick(R.id.tv_try_apply)
	void tryApplyUse(){
		String strApplyUrl = "https://www.launchr.jp/home/contactus";
		Intent intent = new Intent(this,WebViewActivity.class);
		intent.putExtra(LauchrConst.KEY_URL, strApplyUrl);
		startActivity(intent);
	}
	@OnClick(R.id.tv_forget_password)
	void forgetPwd(){
		String strForget = "https://www.launchr.jp/companyuser/forgetpassword";
		Intent intent = new Intent(this,WebViewActivity.class);
		intent.putExtra(LauchrConst.KEY_URL, strForget);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}


	
	


	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			if (v == mEdtUserPassWord) {
				move();
			}
		}else{
			
		}
		
	}
	
	
	/**
	 * 上移
	 */
	private void move(){
		Runnable r = new Runnable() {
			@Override
			public void run() {
				mSc.scrollTo(0, mSc.getHeight());
			}
		};
		mHandler.postDelayed(r, 100);
	}

	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		super.onResponse(response, taskId, rawData);
		// 判断是否为空
		if (response == null) {
			showNoNetWork();
			return;
		}
		// 判断是否是登录返回
		if (taskId.equals(TaskId.TASK_URL_USER_LOGIN)) {
			handleResultLogin(response);
			dismissLoading();
		}else
		// 判断是否是验证返回
		if (taskId.equals(TaskId.TASK_URL_VALIDATE_LOGIN)) {
			handlValidateLogin(response);
			dismissLoading();
		} else if(response instanceof com.mintcode.LoginPOJO) {
			com.mintcode.LoginPOJO pojo = (com.mintcode.LoginPOJO) response;
		} else if(taskId.equals(IMAPI.TASKID.LOGIN)){
			com.mintcode.LoginPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),com.mintcode.LoginPOJO.class);
			if (pojo.isResultSuccess()) {
				String token = pojo.getUserToken();
				AppUtil util = AppUtil.getInstance(this);
				util.saveIMToken(token);
				LauchrConst.header = LauchrConst.getHeader(this);
				PersonalInfoUtil infoUtil = new PersonalInfoUtil(this);
				int state = infoUtil.getIntVaule(Const.KEY_GESTURE);
				Intent intent = null;
				if(state == 1){
					intent = new Intent(this, UnlockGesturePasswordActivity.class);
				}else {
					intent = new Intent(this, CreateGesturePasswordActivity.class);
				}
				startActivity(intent);
				finish();

			}
		}
		
	}
	
	/**
	 * 
	 * @param response
	 */
	private void handlValidateLogin(Object response){
		LoginValidatePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), LoginValidatePOJO.class);
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				LoginValidateEntity entity = pojo.getBody().getResponse().getData();
				if (entity != null) {
					// 验证完成操作
					List<CompanyEntity> comList = entity.getCompanyList();

					if (comList != null) {
						String account = mEdtUserMail.getText().toString().trim();
						String password = mEdtUserPassWord.getText().toString().trim();
						if(comList.size() == 1){
							// 只有一个团队的时候直接登录操作
							CompanyEntity companyEntity = comList.get(0);
							AppUtil.getInstance(this).saveCompanyCode(companyEntity.getcCode());
							HeaderParam p = LauchrConst.header;
							p.setCompanyCode(companyEntity.getcCode());


							AppUtil util = AppUtil.getInstance(this);
							util.saveValue(LauchrConst.KEY_TEMP_LOGINNAME,account);
							util.saveValue(LauchrConst.KEY_TEMP_PASSWORD, password);
							util.saveValue(LauchrConst.KEY_TEMP_COMPANY_NAME, companyEntity.getcName());

							LauchrConst.resetHeader(this);
							UserApi api = UserApi.getInstance();
							api.login(this, account, password);
						}else{

							Intent intent = new Intent(this, LoginSelectTeamActivity.class);
							intent.putExtra("team_entity", (Serializable) comList);
							intent.putExtra("login_username", account);
							intent.putExtra("login_pwd", password);

							AppUtil util = AppUtil.getInstance(this);
							util.saveValue(LauchrConst.KEY_TEMP_LOGINNAME,account);
							util.saveValue(LauchrConst.KEY_TEMP_PASSWORD,password);

							startActivity(intent);
							finish();
						}

					}
				}else{
					toast(pojo.getBody().getResponse().getReason());
				}

			}else{
				toast(pojo.getHeader().getReason());
			}

		}else{
			showNetWorkGetDataException();
		}

	}



	/**
	 * 处理登录返回逻辑
	 * @param response
	 */
	private void handleResultLogin(Object response){
		LoginPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), LoginPOJO.class);
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				if(pojo.getBody().getResponse().getData() != null){
					AppUtil util = AppUtil.getInstance(this);
					//

					UserEntity user = pojo.getBody().getResponse().getData();

					if (user != null) {
						HeaderParam header = new HeaderParam();

						// 设置token
						header.setAuthToken(user.getLAST_LOGIN_TOKEN());
						header.setCompanyCode(user.getC_CODE());
						header.setCompanyShowID(user.getC_SHOW_ID());
						header.setUserName(user.getU_TRUE_NAME());
						header.setLoginName(user.getU_NAME());
						header.setLanguage("jp-ja");
						util.saveHeader(header);

						String show_ID = user.getU_SHOW_ID();
						util.saveShowId(show_ID);
						LitePalManager.reset();
						LitePalManager.setDbName(show_ID);
						IMAPI.getInstance().Login(this, show_ID, LauchrConst.appName);
					}else{
						toast(pojo.getBody().getResponse().getReason());
					}

				}else{
					toast(pojo.getBody().getResponse().getReason());
				}
			}else{
				toast(pojo.getHeader().getReason());
			}
		}else{
			showNetWorkGetDataException();
		}

	}
}
