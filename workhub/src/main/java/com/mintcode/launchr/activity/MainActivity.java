package com.mintcode.launchr.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mintcode.im.service.PushService;
import com.mintcode.im.util.MessageNotifyManager;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.app.AppFragment;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.contact.fragment.ContactFragment;
import com.mintcode.launchr.message.fragment.MessageFragment;
import com.mintcode.launchr.more.MoreFragment;
import com.mintcode.launchr.more.activity.AboutLaunchrActivity;
import com.mintcode.launchr.pojo.CompanyAppPOJO;
import com.mintcode.launchr.pojo.entity.CompanyAppEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.CompanyAppUtil;
import com.mintcode.launchr.util.MixPanelUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchrnetwork.OnResponseListener;
import com.squareup.okhttp.Request;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import im.fir.sdk.FIR;
import im.fir.sdk.callback.VersionCheckCallback;
import im.fir.sdk.version.AppVersion;

/**
 * 主activity
 * @author KevinQiao
 *
 */
public class MainActivity extends BaseActivity implements OnTabChangeListener{

	/** 结束action */
	public static final String KEY_FINISH_ACTION = "key_finish_action";

	/** 加载action */
	public static final String KEY_LOADING_ACTION = "key_loading_action";

	/** 连接action */
	public static final String KEY_CONNECT_ACTION = "key_connect_action";

	/** 连接成功 */
	public static final String KEY_CONNECT_SUCCESS_ACTION = "key_connect_success_action";

	/** 用来标记是否显示第一个Fragment*/
	/** 如果从通讯录发起聊天后，退出聊天后回到消息列表，相应activity要结束掉*/
	public static int showFirstFragment = 0;


	/** tabHost */
	private FragmentTabHost mTabHost;

	/** 模块分类 */
	private Class[] mClassArray;
	/** 资源图标*/
	private int[] mResImageArray;
	/** 模块名称资源*/
	private int[] mResStringArray;

	private boolean isUpdate = false;

	private boolean isExit = false;// 用于标识是否退出

	private String strOnBackMore = "";

	/** 连接广播 */
	private ConnectBroadcastReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initContentArray();
		initView();
		//IM登录
		Map<String, String> info = new HashMap<String, String>();
		AppUtil appUtil = AppUtil.getInstance(this);
		info.put("nickName", appUtil.getUserName());
		LauchrConst.IMLogin(this, info, 0);
		strOnBackMore = getResources().getString(R.string.onclick_more);
		registerConnectReciever();
		checkUpdate();
		// 是否要去加载 公司关联APP信息
		if(CompanyAppUtil.getInstance(this).getAppIn() == false){
			UserApi.getInstance().getCompanyAppMsg(listener);
		}
	}


	/**
	 * 实例化view
	 */
	@SuppressLint("NewApi")
	private void initView(){
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		for (int i = 0; i < mClassArray.length; i++) {
			View view = getTabItemView(i);
			TabSpec tabSpec = mTabHost.newTabSpec(i + "").setIndicator(view);
			// 获取系统版本号
			String model = android.os.Build.MODEL;
			int sdk_int = android.os.Build.VERSION.SDK_INT;
			// 3.0以下适配
			if (sdk_int >= 14) {
				mTabHost.getTabWidget().setShowDividers(Color.TRANSPARENT);
			}
			mTabHost.addTab(tabSpec, mClassArray[i], null);
	}
		mTabHost.setOnTabChangedListener(this);

		MessageNotifyManager.cancelNotification();
	}

	/**
	 * 获取底部tab view
	 * @param index
	 */
	private View getTabItemView(int index){
		LayoutInflater inflat = getLayoutInflater();
		View v = inflat.inflate(R.layout.item_tabhost, null);
		ImageView iv = (ImageView) v.findViewById(R.id.iv_icon);
		TextView tv = (TextView) v.findViewById(R.id.tv_icon_name);

		// 设置图标
		iv.setImageResource(mResImageArray[index]);
		// 设置文字
		tv.setText(mResStringArray[index]);

		return v;

	}


	/**
	 * 注册广播
	 */
	private void registerConnectReciever(){
		mReceiver = new ConnectBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(KEY_FINISH_ACTION);
		registerReceiver(mReceiver,filter);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
		Intent intent = new Intent(this, PushService.class);
		intent.setAction(PushService.ACTION_STOP_FOREGROUND);
		startService(intent);
	}




	@Override
	protected void onResume() {
		super.onResume();
		MessageNotifyManager.cancelNotification();
		if(showFirstFragment == 1){
			mTabHost.setCurrentTab(0);
			showFirstFragment = 0;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isUpdate) {
				return true;
			}else{
				if (!isExit) {
					isExit = true;
					toast(strOnBackMore);
					new Handler().postDelayed(new Runnable() {
						public void run() {
							isExit = false;
						}
					}, 2000);
					return false;
				} else {
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.addCategory(Intent.CATEGORY_HOME);
					startActivity(intent);
					return true;
				}
			}


		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public void onTabChanged(String tabId) {
		if (tabId.equals("1")) {
			ContactFragment.mSelectState = ContactFragment.NO_STATE;
			MixPanelUtil.sendEvent(this,MixPanelUtil.CONTACT_EVENT);
		}else if(tabId.equals("2")){
			MixPanelUtil.sendEvent(this,MixPanelUtil.APPCENTER_EVENT);
		}

	}


	class ConnectBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 判断是否是加载数据
			if(action.equals(KEY_LOADING_ACTION)){
				// 加载数据处理
				showToast("加载中");
			}else
			// 判断是否是	连接
			if(action.equals(KEY_CONNECT_ACTION)){
				// 连接处理
				showToast("连接中");
			}else
			// 判断是否是连接成功
			if(action.equals(KEY_CONNECT_SUCCESS_ACTION)){
				// 连接成功处理
				showToast("连接成功");
			}else

			if(action.equals(KEY_FINISH_ACTION)){
				finish();
			}


		}
	}


	private void showToast(CharSequence message) {
		Toast mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		mToast.setGravity(Gravity.CENTER, 0, 0);

		mToast.show();
	}


	/**
	 * 检测更新
	 */
	private void checkUpdate(){
		FIR.checkForUpdateInFIR(AboutLaunchrActivity.API_TOKEN, new VersionCheckCallback() {
			@Override
			public void onSuccess(AppVersion appVersion, boolean b) {
				super.onSuccess(appVersion, b);
				if (b) {
					Context context = getApplicationContext();
					try {
						PackageInfo p = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
						int versionCode = p.versionCode;
						if (versionCode < appVersion.getVersionCode()) {
							initUpdateDialog(appVersion);
						}
					} catch (PackageManager.NameNotFoundException e) {
						e.printStackTrace();
					}

				}
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



	private void initUpdateDialog(final AppVersion appVersion){
		final Dialog dialog = new Dialog(this,R.style.my_dialog);
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setContentView(R.layout.dialog_update);
		Window window = dialog.getWindow();
		final Button cancelBtn = (Button) window.findViewById(R.id.btn_cancel);
		final Button confirmBtn = (Button) window.findViewById(R.id.btn_confirm);
		TextView tvInfo = (TextView) window.findViewById(R.id.tv_info);

		// 设置更新信息
		String info = appVersion.getChangeLog();
		if (info != null) {
			tvInfo.setText(info);
		}



		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == cancelBtn) {
					dialog.dismiss();
				}else if (v == confirmBtn) {
//
					String url = appVersion.getUpdateUrl();
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					Uri content_url = Uri.parse(url);
					intent.setData(content_url);
					startActivity(intent);
				}

			}
		};

		cancelBtn.setOnClickListener(listener);
		confirmBtn.setOnClickListener(listener);

	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == MoreFragment.GET_ALBUM || requestCode == MoreFragment.TAKE_PICTURE){
			FragmentManager fragmentManager = getSupportFragmentManager();
			Fragment fragment =  fragmentManager.findFragmentByTag("3");
			if(fragment != null && fragment instanceof MoreFragment){
				fragment.onActivityResult(requestCode, resultCode, data);
			}
		}
	}
	/** 初始化页面内容*/
	private void initContentArray(){
			mClassArray = new Class[]{MessageFragment.class, ContactFragment.class, AppFragment.class, MoreFragment.class};//替换 MoreFragment.class
			mResImageArray = new int[]{R.drawable.bg_main_message,R.drawable.bg_main_contact,R.drawable.bg_main_app,R.drawable.bg_main_more};
			mResStringArray = new int[]{R.string.main_message,R.string.main_contact,R.string.main_app,R.string.main_more};
	}

	@Override
	public void finish() {
		super.finish();
	}


	private OnResponseListener listener = new OnResponseListener() {
		@Override
		public void onResponse(Object response, String taskId, boolean rawData) {
			if(response == null){
				return;
			}

			if(taskId.equals(UserApi.TaskId.TASK_URL_GET_COMPANY_APP)){
				handleGetCompanyApp(response);
			}
		}

		@Override
		public boolean isDisable() {
			return false;
		}
	};

	/** 获取公司关联APP信息*/
	private void handleGetCompanyApp(Object response){
		CompanyAppPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), CompanyAppPOJO.class);
		if(pojo == null){
			return;
		}else if(pojo.getBody().getResponse().getData() == null){
			return;
		}else{
			CompanyAppUtil.getInstance(this).setAppIn();

			List<CompanyAppEntity> entitys = pojo.getBody().getResponse().getData();
			for(int i=0; i<entitys.size(); i++){
				CompanyAppUtil.getInstance(this).saveAppIcon(entitys.get(i).getShowId(), entitys.get(i).getAppIconMobile());
			}
		}
	}
}
