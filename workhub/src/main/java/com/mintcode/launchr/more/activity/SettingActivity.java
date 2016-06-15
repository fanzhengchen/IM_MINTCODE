package com.mintcode.launchr.more.activity;

import com.mintcode.launchr.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.chat.util.Keys;
import com.mintcode.im.IMManager;
import com.mintcode.im.database.KeyValueDBService;
import com.mintcode.im.database.MessageDBService;
import com.mintcode.im.service.PushService;
import com.mintcode.launchr.activity.LoginActivity;
import com.mintcode.launchr.activity.LoginWorkHubActivity;
import com.mintcode.launchr.app.account.setting.SettingLanguageActivity;
import com.mintcode.launchr.app.account.setting.SettingMessageActivity;
import com.mintcode.launchr.activity.MainActivity;
import com.mintcode.launchr.activity.WebViewActivity;
import com.mintcode.launchr.api.IMNewApi;
import com.mintcode.launchr.app.account.setting.SettingInputActivity;
import com.mintcode.launchr.app.account.setting.SettingLanguageActivity;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.MixPanelUtil;
import com.mintcode.launchr.util.TTJSONUtil;

/*
 * create by Stephen He 2015/9/9
 */
public class SettingActivity extends BaseActivity {

	/** 返回按钮 */
	private ImageView mIvBack;

	// 语言
	private RelativeLayout mRelLanguage;
	// 显示当前语言
	private TextView mTvLanguageShow;

	// 语言
	private RelativeLayout mRelInput;

	// 消息通知
	private RelativeLayout mRelMessageOrder;

	// 设备管理
	private RelativeLayout mRelDeviceManage;

	// 清除缓存
	private RelativeLayout mRelClearCache;

	// 意见反馈
	private RelativeLayout mRelFeedBack;

	// 帮助中心
	private RelativeLayout mRelHelpCenter;

	// 关于Launchr
	private RelativeLayout mRelAboutLaunchr;

	// 删除账号
	private RelativeLayout mRelDeleteAccount;

	/** 新消息提醒view */
	private RelativeLayout mRelNewMsgRemind;

	// 退出当前账号
	private Button mBtnSignOut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		initView();

		showCurrentLanguage();
	}



	private void initView() {
		mIvBack = (ImageView) findViewById(R.id.iv_back);

		mTvLanguageShow = (TextView) findViewById(R.id.tv_language_middle);

		mRelMessageOrder = (RelativeLayout) findViewById(R.id.rel_setting_message_order);
		mRelLanguage = (RelativeLayout) findViewById(R.id.rel_setting_language);
		mRelInput = (RelativeLayout) findViewById(R.id.rel_setting_input);
		mRelDeviceManage = (RelativeLayout) findViewById(R.id.rel_setting_device_manage);
		mRelClearCache = (RelativeLayout) findViewById(R.id.rel_setting_clear_cache);
		mRelFeedBack = (RelativeLayout) findViewById(R.id.rel_setting_feed_back);
		mRelHelpCenter = (RelativeLayout) findViewById(R.id.rel_setting_help_center);
		mRelAboutLaunchr = (RelativeLayout) findViewById(R.id.rel_setting_about_software);
		mRelDeleteAccount = (RelativeLayout) findViewById(R.id.rel_setting_delete_account);
		mRelNewMsgRemind = (RelativeLayout) findViewById(R.id.rel_new_msg_remind);

		mBtnSignOut = (Button) findViewById(R.id.btn_sign_out);

		mIvBack.setOnClickListener(this);
		mRelMessageOrder.setOnClickListener(this);
		mRelLanguage.setOnClickListener(this);
		mRelInput.setOnClickListener(this);
		mRelDeviceManage.setOnClickListener(this);
		mRelClearCache.setOnClickListener(this);
		mRelFeedBack.setOnClickListener(this);
		mRelHelpCenter.setOnClickListener(this);
		mRelAboutLaunchr.setOnClickListener(this);
		mRelDeleteAccount.setOnClickListener(this);
		mBtnSignOut.setOnClickListener(this);
		mRelNewMsgRemind.setOnClickListener(this);
			TextView tvAbout = (TextView) findViewById(R.id.tv_about_software);
			tvAbout.setText(R.string.about_workhub);
	}

	@Override
	public void onClick(View v) {
		if (v == mIvBack) {
			onBackPressed();
		} else if (v == mRelMessageOrder) {
			// 消息通知
//			Intent intent = new Intent(SettingActivity.this,
//					SettingMessageActivity.class);
//			startActivity(intent);
		} else if (v == mRelLanguage) {
			// 设置语言
			Intent intent = new Intent(SettingActivity.this,
					SettingLanguageActivity.class);
			startActivity(intent);
		} else if(v == mRelInput){
			Intent intent = new Intent(SettingActivity.this,
					SettingInputActivity.class);
			startActivity(intent);
		}else if (v == mRelDeviceManage) {
			// 设备管理
//			Intent intent = new Intent(SettingActivity.this,
//					SettingDeviceActivity.class);
//			startActivity(intent);
		} else if (v == mRelClearCache) {
			// 清除缓存
		} else if (v == mRelFeedBack) {
			// 意见反馈
//			Intent intent = new Intent(SettingActivity.this,
//					SettingFeedBackActivity.class);
//			startActivity(intent);
		} else if (v == mRelHelpCenter) {
			// 帮助中心
			String strHelp = "";
			if(LauchrConst.IS_WORKHUB){
				strHelp = "https://workhub.zendesk.com";
			}else{
				strHelp = "https://launchr.zendesk.com";
			}
			Intent intent = new Intent(this,WebViewActivity.class);
			intent.putExtra(LauchrConst.KEY_URL, strHelp);
			startActivity(intent);
		} else if (v == mRelAboutLaunchr) {
			// 关于Launchr
			if(LauchrConst.IS_WORKHUB){
				Intent intent = new Intent(SettingActivity.this,
						AboutWorkHubActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(SettingActivity.this,
						AboutLaunchrActivity.class);
				startActivity(intent);
			}


		} else if (v == mRelDeleteAccount) {
			// 删除当前账号
		} else if (v == mBtnSignOut) {
			// 退出当前账号
			MixPanelUtil.sendEvent(this,MixPanelUtil.LOGOUT_EVENT);
			loginOut();
//			handleLoginOut();
		}else if(v == mRelNewMsgRemind){
			// 新消息提醒设置
			Intent intent = new Intent(this,NewMsgNoticeActivity.class);
			startActivity(intent);
		}
	}

	private void loginOut(){
		showLoading();
		KeyValueDBService mValueDBService = KeyValueDBService.getInstance();
		String mToken = mValueDBService.find(Keys.TOKEN);
		String mUid = mValueDBService.find(Keys.UID);
		IMNewApi.getInstance().loginOut(this,mToken,mUid);
	}

	// 显示当前语言
	public void showCurrentLanguage() {

	}

	private void handleLoginOut(){
		AppUtil.getInstance(this).deleteFile();
		AppUtil.getInstance(this).saveIntValue(Const.KEY_GESTURE, 0);

		Intent intent;

		if(LauchrConst.IS_WORKHUB){
			intent	= new Intent(this, LoginWorkHubActivity.class);
		} else {
			intent	= new Intent(this, LoginActivity.class);
		}

		MessageDBService.getInstance().destoryInstance();

//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//IM登出
		IMManager.getInstance().logout();

		Intent i = new Intent(this, PushService.class);
		stopService(i);

		LauchrConst.PUSH_SERVICE_IS_LOGIN = false;


		startActivity(intent);
		sendBoardCast();
		setResult(RESULT_OK);
		finish();
	}

	private void sendBoardCast(){
		Intent intent = new Intent(MainActivity.KEY_FINISH_ACTION);
		sendBroadcast(intent);
	}

	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		super.onResponse(response, taskId, rawData);
		if(response == null){
			showNoNetWork();
			return;
		}else

		// 判断是否是登出返回
		if(taskId.equals(IMNewApi.TaskId.TASK_URL_LOGIN_OUT)){
			handleLoginOut(response);
			dismissLoading();
		}
	}

	/**
	 * 登出返回处理
	 * @param response
	 */
	private void handleLoginOut(Object response){
//		IMBasePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),IMBasePOJO.class);
//		if(pojo.isResultSuccess()){

//		}else {
//			toast(pojo.getMessage());
//		}
		handleLoginOut();
	}

}
