package com.mintcode.launchr.util;


import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.body.HeaderParam;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.ImageView;

/**
 * 操作用户信息存储shareRefences
 * @author KevinQiao
 *
 */
public class AppUtil {
	
	
	private static AppUtil mInstance = null;
	
	private Context mContext;
	
	/**
	 * 所有的数据都是通过{@link SharedPreferences} 来存在内部存储中的。
	 */
	private SharedPreferences mSharedPreferences;
	
	/**    
	 * 用来改变{@link SharedPreferences} 中所存的值。
	 */
	private Editor mEditor;
	
	
	
	private AppUtil(Context context) {
		mContext = context;
		mSharedPreferences = context.getSharedPreferences(
				"launchr", Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}


	
	public static AppUtil getInstance(Context context){
		if (mInstance == null) {
			mInstance = new AppUtil(context);
		}
		return mInstance;
	}



	/**
	 * 获取token
	 * @return
	 */
	public String getAuthToken(){
		return getValue(LauchrConst.KEY_AUTH_TOKEN);
	}
	
	
	
	/**
	 * 保存token
	 * @param token
	 */
	public void saveToken(String token){
		saveValue(LauchrConst.KEY_AUTH_TOKEN, token);
	}
	
	
	
	/**
	 * 存储header
	 * @param header
	 */
	public void saveHeader(HeaderParam header){
		saveLoginName(header.getLoginName());
		saveUserName(header.getUserName());
		saveCompanyShowID(header.getCompanyShowID());
		saveCompanyCode(header.getCompanyCode());
		saveToken(header.getAuthToken());
		saveAsync(header.isAsync());
		saveType(header.getType());
		saveLanguage(header.getLanguage());
		
	}
	
	public void saveShowId(String showId){
		saveValue(LauchrConst.KEY_SHOWID, showId);
	}
	
	public String getShowId(){
		return getValue(LauchrConst.KEY_SHOWID);
	}
	
	public void saveIMToken(String token){
		saveValue(LauchrConst.KEY_IMTOKEN, token);
	}
	
	public String getIMToken(){
		return getValue(LauchrConst.KEY_IMTOKEN);
	}
	
	/**
	 * 获取登录名
	 * @return
	 */
	public String getLoginName(){
		return getValue(LauchrConst.KEY_LOGINNAME);
	}
	
	/**
	 * 保存loginName
	 * @param loginName
	 */
	public void saveLoginName(String loginName){
		saveValue(LauchrConst.KEY_LOGINNAME, loginName);
	}
	
	/**
	 * 获取用户名
	 * @return
	 */
	public String getUserName(){
		return getValue(LauchrConst.KEY_USERNAME);
	}
	
	/**
	 * 保存username
	 * @param userName
	 */
	public void saveUserName(String userName){
		saveValue(LauchrConst.KEY_USERNAME, userName);
	}
	
	/**
	 * 获取公司显示名
	 * @return
	 */
	public String getCompanyShowID(){
		return getValue(LauchrConst.KEY_COMPANYSHOWID);
	}
	
	/**
	 * 保存companyShowId
	 * @param companyShowId
	 */
	public void saveCompanyShowID(String companyShowId){
		saveValue(LauchrConst.KEY_COMPANYSHOWID, companyShowId);
	}
	
	/**
	 * 获取公司代码
	 * @return
	 */
	public String getCompanyCode(){
		return getValue(LauchrConst.KEY_COMPANYCODE);
	}
	
	/**
	 * 保存公司代码
	 * @param companyCode
	 */
	public void saveCompanyCode(String companyCode){
		saveValue(LauchrConst.KEY_COMPANYCODE, companyCode);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getAsync(){
		return getBooleanValue(LauchrConst.KEY_ASYNC);
	}
	
	/**
	 * 
	 */
	public void saveAsync(boolean async){
		saveBooleanValue(LauchrConst.KEY_ASYNC, async);
	}
	
	/**
	 * 获取提交方式
	 * @return
	 */
	public String getType(){
		return getValue(LauchrConst.KEY_TYPE);
	}
	
	/**
	 * 保存提交方式
	 * @param type
	 */
	public void saveType(String type){
		saveValue(LauchrConst.KEY_TYPE, type);
	}
	
	/**
	 * 获取语言
	 * @return
	 */
	public String getLanguage(){
		return getValue(LauchrConst.KEY_LANGUAGE);
	}
	
	/**
	 * 保存语言
	 * @param language
	 */
	public void saveLanguage(String language){
		saveValue(LauchrConst.KEY_LANGUAGE, language);
	}
	
	
	
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
