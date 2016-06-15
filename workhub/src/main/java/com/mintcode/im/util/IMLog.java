package com.mintcode.im.util;

import android.util.Log;

/**在Android原生Log基础上封装了一层Log类，前缀为IM,专门为IM服务
 * @author RobinLin
 *
 */
public class IMLog {
	/**
	 * 调试开关
	 */
	private static boolean DC = true;
	public static boolean D  = ( DC | IMConst.DBGALL );
	
	public static final String DTAG = "titans_fastlog";
	
	public static void v(String tag,String msg){
		if(D){
			Log.v(tag, msg);
		}
	}
	public static void v(String msg){
		if(D){
			Log.v(DTAG, msg);
		}
	}
	
	public static void d(String tag,String msg){
		if(D){
			Log.d(tag, msg);
		}
	}
	public static void d(String msg){
		if(D){
			Log.d(DTAG, msg);
		}
	}
	
	public static void i(String tag,String msg){
		if(D){
			Log.i(tag, msg);
		}
	}
	public static void i(String msg){
		if(D){
			Log.i(DTAG, msg);
		}
	}
	
	public static void w(String tag,String msg){
		if(D){
			Log.w(tag, msg);
		}
	}
	public static void w(String msg){
		if(D){
			Log.w(DTAG, msg);
		}
	}
	
	public static void e(String tag,String msg){
		if(D){
			Log.e(tag, msg);
		}
	}
	public static void e(String msg){
		if(D){
			Log.e(DTAG, msg);
		}
	}
}
