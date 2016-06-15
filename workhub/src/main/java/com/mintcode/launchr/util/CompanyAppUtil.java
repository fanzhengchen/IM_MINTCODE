package com.mintcode.launchr.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by StephenHe on 2016/3/25.
 * 公司应用模块信息 */
public class CompanyAppUtil {
    private Context mContext;

    private static CompanyAppUtil mInstance = null;

    private SharedPreferences mSharedPreferences;

    private SharedPreferences.Editor mEditor;

    public static final String isGet = "app_msg_in";

    private CompanyAppUtil(Context context){
        mContext = context;
        mSharedPreferences = context.getSharedPreferences("CompanyApp", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static CompanyAppUtil getInstance(Context contect){
        if(mInstance == null){
            mInstance = new CompanyAppUtil(contect);
        }
        return mInstance;
    }

    /** 是否已存在数据*/
    public boolean getAppIn(){
        return getBooleanValue(isGet);
    }

    public void setAppIn(){
        saveBooleanValue(isGet, true);
    }


    /** 获取手机应用图标*/
    public String getAppIcon(String key){
        return getValue(key);
    }

    /** 保存手机应用图标*/
    public void saveAppIcon(String key, String value){
        saveValue(key, value);
    }

    private void saveValue(String key, String fileName){
        mEditor.putString(key, fileName);
        mEditor.commit();
    }

    private String getValue(String key){
        return mSharedPreferences.getString(key, null);
    }

    public void saveBooleanValue(String key, boolean value){
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public boolean getBooleanValue(String key){
        boolean retValue = mSharedPreferences.getBoolean(key, false);
        return retValue;
    }
}
