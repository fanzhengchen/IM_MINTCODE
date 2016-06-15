package com.mintcode.launchrnetwork;

public final class MTResultCode {

	// 82XX 成功
	public final static int SUCCESS = 8200;
	public final static int SUCCESS_REGIST = 8201;

	// 83XX 用户模块异常
	public final static int USER_NOT_EXSIT = 8300;// 用户名不存在
	public final static int ACCOUNT_IS_FORBIDDEN = 8301;// 帐号被禁用
	public final static int PWD_IS_ERROR = 8302;// 密码错误
	public final static int USER_FUTURE_TRADE_LOGIN_FAILED = 8303;// 读取远程用户失败
	public final static int TOKEN_IS_INVALID = 8306;// TOKEN 无效

	public final static int INNER_NET_SERVER_FAILED = 8307;// 内部服务器网络异常
	public final static int DATA_LOAD_FAILED = 8308;// 数据更新失败
	public final static int DEVICE_IS_UNUSABLE = 8309;// 设备不可用

}
