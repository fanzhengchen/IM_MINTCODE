package com.mintcode.launchr.base;

import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;

import com.mintcode.im.service.PushService;
import com.mintcode.im.util.MessageNotifyManager;
import com.mintcode.launchr.R;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.CustomToast;
import com.mintcode.launchr.util.MixPanelUtil;
import com.mintcode.launchr.view.DialogLoading;
import com.mintcode.launchr.view.WorkHubDialogLoading;
import com.mintcode.launchrnetwork.OnResponseListener;

/**
 * 基础activity,所有activity继承它
 *
 * @author KevinQiao
 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener, OnResponseListener {


    /**
     * loading
     */
    private DialogLoading mLoading;

    private WorkHubDialogLoading mWorkHubLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences(getPackageName(),
                MODE_PRIVATE);
        String type = sp.getString("language", "");
        changeAppLanguage(getResources(), type);
        Const.mConfig = getResources().getConfiguration();
        initView();
        sendEvent();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 实例化view
     */
    private void initView() {
        if (LauchrConst.IS_WORKHUB && mWorkHubLoading == null) {
            mWorkHubLoading = WorkHubDialogLoading.creatDialog(this);
        } else if (mLoading == null) {
            mLoading = DialogLoading.creatDialog(this);
        }
    }

    /**
     * 显示loading框
     */
    public void showLoading() {
        if (LauchrConst.IS_WORKHUB && mWorkHubLoading != null) {
            if (mWorkHubLoading != null) {
                if (!mWorkHubLoading.isShowing()) {
                    mWorkHubLoading.show();
                    mWorkHubLoading.start();
                }
            } else if (mLoading != null) {
                if (!mLoading.isShowing()) {
                    mLoading.show();
                }
            }
        }

    }

    public void showTextLoading(String text) {
        if (LauchrConst.IS_WORKHUB && mWorkHubLoading != null) {
            if (!mWorkHubLoading.isShowing()) {
                mWorkHubLoading.show();
            }
        } else if (mLoading != null) {
            if (!mLoading.isShowing()) {
                mLoading.show();
            }
        }
    }

    /**
     * 隐藏loading
     */
    public void dismissLoading() {
        if (LauchrConst.IS_WORKHUB && mWorkHubLoading != null) {
            if (mWorkHubLoading.isShowing()) {
                mWorkHubLoading.dismiss();
            }
        } else if (mLoading != null) {
            if (mLoading.isShowing()) {
                mLoading.dismiss();
            }
        }
        if (mWorkHubLoading != null) {
            if (mWorkHubLoading.isShowing()) {
                mWorkHubLoading.dismiss();
            }
        }
    }

    /**
     * 弹出提示
     *
     * @param text
     */
    public void toast(String text) {
        if ((text != null) && !text.equals("")) {
            CustomToast.showToast(this, text, 2000);
        }
    }


    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        if (response == null) {
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsForebackground) {
            mIsForebackground = false;
            Intent intent = new Intent(this, PushService.class);
            intent.setAction(PushService.ACTION_STOP_FOREGROUND);
            startService(intent);
        }
    }

    public static boolean mIsForebackground = false;
    public Handler handler = new Handler();
    public Runnable r = new Runnable() {
        @Override
        public void run() {
            onPauseRunnig();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
//		pauseRunning();

        handler.removeCallbacks(r);
        handler.postDelayed(r, 500);
//		onPauseRunnig();
    }

    /**
     * 判断是否是处于后台
     */
    protected void onPauseRunnig() {
        boolean b = MessageNotifyManager.isAppOnForeground(this);
        if (!b) {
            mIsForebackground = true;
            String longName = AppUtil.getInstance(getApplicationContext()).getLoginName();
            if (longName != null && !longName.equals("")) {
                Intent intent = new Intent(this, PushService.class);
                intent.setAction(PushService.ACTION_START_FOREGROUND);
                startService(intent);
            }
        }
    }

    @Override
    public boolean isDisable() {
        return false;
    }


    @Override
    public void onClick(View v) {

    }

    /**
     * @param resources
     * @param lanAtr
     */
    public void changeAppLanguage(Resources resources, String lanAtr) {
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (lanAtr.equals("cn")) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else if (lanAtr.equals("en")) {
            config.locale = Locale.ENGLISH;
        } else if (lanAtr.equals("jp")) {
            config.locale = Locale.JAPANESE;// 将语言更改为日文
        } else {
            config.locale = Locale.getDefault();
            lanAtr = "system";
        }
        resources.updateConfiguration(config, dm);

        // 并存入配置文件
        SharedPreferences sp = getSharedPreferences(getPackageName(),
                MODE_PRIVATE);
        Editor edit = sp.edit();
        edit.putString("language", lanAtr);
        edit.commit();

    }

    /**
     * 数据获取异常提示
     *
     * @return
     */
    public void showNetWorkGetDataException() {
        String str = getString(R.string.network_exception);
        toast(str);
    }

    /**
     * 无网络访问提示
     */
    public void showNoNetWork() {
        String str = getString(R.string.no_network);
        toast(str);
    }

    @Override
    protected void onDestroy() {
        MixPanelUtil.flushEvent(this);
        super.onDestroy();
    }

    public void sendEvent() {
//		JSONObject props = new JSONObject();
//		try {
//			props.put("Activity-Name", this.getClass().getName());
//			mMixpanel.track("Name", props);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
