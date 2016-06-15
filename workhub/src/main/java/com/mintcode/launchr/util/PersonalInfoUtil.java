package com.mintcode.launchr.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.mintcode.launchr.consts.LauchrConst;

/**
 * 操作个人信息存储shareRefences，暂时存储手势
 * 未来需要保存个人信息
 * @author KevinQiao
 *
 */
public class PersonalInfoUtil {


	private PersonalInfoUtil mInstance = null;

	private Context mContext;

	/**
	 * 所有的数据都是通过{@link SharedPreferences} 来存在内部存储中的。
	 */
	private SharedPreferences mSharedPreferences;

	/**
	 * 用来改变{@link SharedPreferences} 中所存的值。
	 */
	private Editor mEditor;



	public PersonalInfoUtil(Context context) {
		mContext = context;
		String username = LauchrConst.getHeader(context).getLoginName();
		mSharedPreferences = context.getSharedPreferences(
				username + "_" + "launchr", Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}

	public PersonalInfoUtil(Context context,String username) {
		mContext = context;
		mSharedPreferences = context.getSharedPreferences(
				username + "_" + "launchr", Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}

	
//	public  PersonalInfoUtil getInstance(Context context){
//		mInstance = new PersonalInfoUtil(context);
//		return mInstance;
//	}
//
//	public  PersonalInfoUtil getInstance(Context context,String username){
//		mInstance = new PersonalInfoUtil(context,username);
//		return mInstance;
//	}



	/**
	 * 保存key
	 * @param key
	 * @param value
	 */
	public void saveValue(String key, String value){
		mEditor.putString(key, value);
		mEditor.commit();
	}
	
	/**传入KEY，得到对应的数据。
	 * @param key
	 * @return
	 */
	public String getValue(String key){
		String retValue = mSharedPreferences.getString(key, "");
		return retValue;
	}
	
	public void removeValue(String key){
		mEditor.putString(key, null);
		mEditor.commit();
	}
	
	/**传入KEY，得到对应的数据。
	 * @param key
	 * @return
	 */
	public boolean getBooleanValue(String key){
		boolean retValue = mSharedPreferences.getBoolean(key, false);
		return retValue;
	}
	
	/**
	 * 存储boolean value
	 * @param key
	 * @param value
	 */
	public void saveBooleanValue(String key, boolean value){
		mEditor.putBoolean(key, value);
		mEditor.commit();
	}

	/**
	 * 储存int value
	 * @param key
	 * @param value
	 */
	public void saveIntValue(String key, int value){
		mEditor.putInt(key, value);
		mEditor.commit();
	}
	
	/**
	 * 获取int value
	 * @return
	 */
	public int getIntVaule(String key){
		int retValue = mSharedPreferences.getInt(key, -1);
		return retValue;
	}
	
	
	/**
	 * 置空shareperfence
	 */
	public void deleteFile(){
		mEditor.clear();
		mEditor.commit();
	}
	

	
	
}
