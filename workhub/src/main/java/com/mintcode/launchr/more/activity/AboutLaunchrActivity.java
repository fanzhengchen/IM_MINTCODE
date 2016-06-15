package com.mintcode.launchr.more.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.im.service.PushService;
import com.mintcode.launchr.R;
import com.mintcode.launchr.activity.LoginActivity;
import com.mintcode.launchr.activity.WebViewActivity;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.Const;
import com.squareup.okhttp.Request;

import im.fir.sdk.FIR;
import im.fir.sdk.callback.VersionCheckCallback;
import im.fir.sdk.version.AppVersion;

/*
 * create by Stephen He 2015/9/9
 */
public class AboutLaunchrActivity extends BaseActivity {

	/** 返回按钮 */
	private ImageView mIvBack;

	// 用户协议layout
	private RelativeLayout mRelUserAgreement;

	// 用户协议layout
	private RelativeLayout mRelPrivacyPolicy;

	/** 检查更新 */
	private RelativeLayout mRelCheckUpdate;

	/** 最新版本 */
	private TextView mTvLastVersion;

	/** 当前版本 */
	private  TextView mTvCurrentVersion;

	/** 更新开始 */
//	public static final String API_TOKEN = "20f853316a2a7f5d3b1e7ad18af9e366";
	public static final String API_TOKEN = "ac0b9f4bbe1acb4a429d27404dd5cb66";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_about_launchr);

		initView();

	}

	private void initView() {
		mIvBack = (ImageView) findViewById(R.id.iv_back);
		mRelUserAgreement = (RelativeLayout) findViewById(R.id.rel_user_agreement);
		mRelPrivacyPolicy = (RelativeLayout) findViewById(R.id.rel_privacy_policy);
		mRelCheckUpdate = (RelativeLayout) findViewById(R.id.rel_checke_update);
		mTvLastVersion = (TextView) findViewById(R.id.tv_last_version_num);
		mTvCurrentVersion = (TextView) findViewById(R.id.tv_current_num);

		mRelUserAgreement.setOnClickListener(this);
		mRelPrivacyPolicy.setOnClickListener(this);
		mRelCheckUpdate.setOnClickListener(this);
		mIvBack.setOnClickListener(this);

		if(LauchrConst.DEV_CODE == 3 || LauchrConst.DEV_CODE == 2){
			mRelCheckUpdate.setVisibility(View.VISIBLE);
		}else{
			mRelCheckUpdate.setVisibility(View.GONE);
		}


		autoCheckUpdate();
		initData();
	}


	private void initData(){
		Context context = this;
		PackageInfo p;
		try {
			p = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			String verName = p.versionName;
			mTvCurrentVersion.setText(verName);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

	}


	@Override
	public void onClick(View v) {
		if (v == mIvBack) {
			onBackPressed();
		}else if (v == mRelUserAgreement){
			// 用户协议
			String strAgreement = "https://www.launchr.jp/terms";
			Intent intent = new Intent(this,WebViewActivity.class);
			intent.putExtra(LauchrConst.KEY_URL, strAgreement);
			startActivity(intent);
		}else if (v == mRelPrivacyPolicy){
			// PrivacyPolicy
			String strPrivacyPolicy = "https://www.launchr.jp/privacy";
			Intent intent = new Intent(this,WebViewActivity.class);
			intent.putExtra(LauchrConst.KEY_URL, strPrivacyPolicy);
			startActivity(intent);
		}else if(v == mRelCheckUpdate){
			checkUpdate();
		}

	}

	/**
	 * 主动检查更新
	 */
	private void autoCheckUpdate(){
		FIR.checkForUpdateInFIR(API_TOKEN, new VersionCheckCallback() {
			@Override
			public void onSuccess(AppVersion appVersion, boolean b) {
				super.onSuccess(appVersion, b);
				String num = appVersion.getVersionName();
				mTvLastVersion.setText(num);
			}

			@Override
			public void onFail(Request request, Exception e) {
				super.onFail(request, e);

			}

			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onFinish() {
				super.onFinish();
			}
		});
	}

	/**
	 * 检测更新
	 */
	private void checkUpdate(){
		FIR.checkForUpdateInFIR(API_TOKEN, new VersionCheckCallback() {
			@Override
			public void onSuccess(AppVersion appVersion, boolean b) {
				super.onSuccess(appVersion, b);
				if(b){
					Context context = getApplicationContext();
					try {
						PackageInfo p = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
						int versionCode = p.versionCode;
						if(versionCode < appVersion.getVersionCode()){

							String url = appVersion.getUpdateUrl();
							Intent intent = new Intent();
							intent.setAction(Intent.ACTION_VIEW);
							Uri content_url = Uri.parse(url);
							intent.setData(content_url);
							startActivity(intent);
						}
					} catch (PackageManager.NameNotFoundException e) {
						e.printStackTrace();
					}
				}else {
					toast("已经是最新版");
				}
			}

			@Override
			public void onFail(Request request, Exception e) {
				super.onFail(request, e);
				Log.i("infos", "===onFail==="  + "----" + request);
			}

			@Override
			public void onStart() {
				super.onStart();
				Log.i("infos", "===onStart===" + "----" );
			}

			@Override
			public void onFinish() {
				super.onFinish();
				Log.i("infos", "===onFinish===" + "----" );
			}
		});
	}

	// 显示当前语言
	public void showCurrentLanguage() {

	}
	
	private void loginOut(){
		AppUtil.getInstance(this).deleteFile();
		AppUtil.getInstance(this).saveIntValue(Const.KEY_GESTURE, 0);
		stopService(new Intent(this, PushService.class));
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		setResult(RESULT_OK);
		finish();
	}
}
