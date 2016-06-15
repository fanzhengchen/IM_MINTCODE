package com.mintcode.launchr.util;

import java.util.Locale;

import android.content.res.Configuration;

public class Const {

	/** 登录key */
	public static final String KEY_LOGIN = "key_login";
	
	/** 手势状态  1 表示已经设置手势，0表示没有*/
	public static final String KEY_GESTURE = "key_gesture";
	

	public static Configuration mConfig ;
	
	
	
	
	public static Locale getLocale(){
		return mConfig.locale;
	}

	//APP的固定字符串
	public static final  String SHOWID_TASK = "PWP16jQLLjFEZXLe";
	public static final String SHOWID_APPROVE = "ADWpPoQw85ULjnQk";
	public static final  String SHOW_SCHEDULE = "l6b3YdE9LzTnmrl7";
	public static final  String SHOW_FRIEND = "Relation";

	//地图选择
	public static final String STR_PLACE = "str_place";
	public static final String  LATITUDE= "latitude";
	public static final String LONGITUDE= "longitude";
	public static final String ADDRESS = "address";

}
























