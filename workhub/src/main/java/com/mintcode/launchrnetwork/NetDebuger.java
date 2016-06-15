package com.mintcode.launchrnetwork;

import java.util.HashMap;

/**供假数据调试使用。</br>
 * Demo code:</br>
 * <code>
 * NetDebuger.DEBUG = true;</br>
 * setDebugResponse("taskId","{\"test\":\"this is test string\"}";
 * </code>
 * 
 * @author RobinLin
 *
 */
public class NetDebuger {
	/**
	 * 是否开启假数据？
	 */
	public static boolean DEBUG = false;
	/**
	 * 假数据重复使用次数，暂时无用。
	 */
	public static int RESPONSE_COUNT = 1;
	/**
	 * 用来存假数据的Map
	 */
	private static HashMap<String, String> sResponseMap = new HashMap<String, String>();
	
	/**设置假数据，传入写好的假数据。
	 * @param taskId taskId为Key
	 * @param responseJson 假数据
	 */
	public static void setDebugResponse(String taskId,String responseJson){
		sResponseMap.put(taskId, responseJson);
	}
	
	/**根据taskId得到假数据。
	 * @param taskId API接口中的taskId
	 * @return
	 */
	public static String getResponse(String taskId){
		String res = sResponseMap.get(taskId);
		return res;
	}
	
	/**移除之前设置好的假数据。
	 * @param taskId Task Id
	 * @return 如果返回null，则表示{@link #sResponseMap}里没有这个key对应的value
	 */
	public static String removeResponse(String taskId){
		return sResponseMap.remove(taskId);
	}
}
