package com.mintcode.chat.util;

public interface Keys {
	/** token*/
	public static final String TOKEN = "token";
	/** sysconf-push*/
	public static final String SYSCONF_PUSH = "sysconf-push";
	/** token 失效时间*/
	public static final String TOKEN_EXPIREAT = "token_expireAt";
	/** 服务器系统时间与本地时间�? */
	public static final String TIME_GAP = "time_gap";
	/** uid */
	public static final String UID = "uid";
	/** mobile */
	public static final String MOBILE = "mobile";

	/**
	 * 在医生界面用的病人的uid
	 */
	public static final String CID = "cid";

	// 个人资料
	/** 个人信息 */
	public static final String MY_INFO = "MY_INFO";
	/**医生个人个人信息*/
	public static final String DR_INFO = "DR_INFO";

	/** 生活处方 */
	public static final String SYSTEM_OPTION_CONF_REPORT = "system_option_conf_report";
	public static final String LAST_SYNC = "last_sync";
	/** */
	public static final String SUGAR_LOWEST = "sugar_lowest";
	/**  */
	public static final String SUGAR_HIGHEST_BEFOR_EAT = "sugar_highest_befor_eat";
	/**  */
	public static final String SUGAR_HIGHEST_AFTER_EAT = "sugar_highest_after_eat";

	public static final String INPUT_ENTER_TYPE = "input_enter_type";

	public static final int INPUT_ENTER_DEFAULT = 0;
	public static final int INPUT_ENTER_RETURN = 1;
	public static final int INPUT_ENTER_SEND = 2;
}
