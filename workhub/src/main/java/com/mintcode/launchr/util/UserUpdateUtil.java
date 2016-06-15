package com.mintcode.launchr.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by StephenHe on 2016/3/21.
 * 通讯录部门，用户列表是否更新的时间戳
 */
public class UserUpdateUtil {
    private Context mContext;

    private static UserUpdateUtil mInstance = null;

    private SharedPreferences mSharedPreferences;

    private SharedPreferences.Editor mEditor;

    private UserUpdateUtil(Context context){
        mContext = context;
        mSharedPreferences = context.getSharedPreferences("userUpdate", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static UserUpdateUtil getInstance(Context contect){
        if(mInstance == null){
            mInstance = new UserUpdateUtil(contect);
        }
        return mInstance;
    }

    /** 获取用户更新时间戳*/
    public long getUpdateTime(String key){
        return getLongValue(key);
    }

    /** 保存用户更新的时间戳*/
    public void saveUpdateTime(String key, long value){
        saveLongValue(key, value);
    }

    private void saveLongValue(String key, long time){
        mEditor.putLong(key, time);
        mEditor.commit();
    }

    private long getLongValue(String key){
        return mSharedPreferences.getLong(key, -1);
    }
}
