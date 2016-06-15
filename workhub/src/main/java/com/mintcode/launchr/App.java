package com.mintcode.launchr;

import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.util.HeadImageUtil;

import org.litepal.LitePalApplication;

import im.fir.sdk.FIR;

//import com.baidu.mapapi.SDKInitializer;

/**
 * launchr application
 *
 * @author KevinQiao
 */
public class App extends LitePalApplication {

    /**
     * 手势工具类
     */
//	private LockPatternUtils mLockPatternUtils;

    private static App mInstance;

    public static App getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        FIR.init(this);

        //百度地图，建议该方法放在Application的初始化方法中
//        SDKInitializer.initialize(getApplicationContext());

        mInstance = this;
//		mLockPatternUtils = new LockPatternUtils(this);
        LauchrConst.initValues(this);
        HeadImageUtil.getInstance(this);
//        trustAllHosts();
    }

    /**
     * Trust every server - dont check for any certificate
     */
}
