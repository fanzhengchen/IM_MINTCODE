package com.mintcode.launchrnetwork;

public interface OnResponseListener {
	
	/** ServerTalker will callback this method after connect with server.
	 * @param response the data send by server.
	 * @param taskId taskId is the check code.
	 */
	public void onResponse(Object response, String taskId, boolean rawData);
	
	/**页面是否已关闭
	 * @return
	 */
	public boolean isDisable();
}    