package com.mintcode.launchr.api;

import java.util.List;

import android.util.Log;

import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchrnetwork.MTHttpManager;
import com.mintcode.launchrnetwork.MTHttpParameters;
import com.mintcode.launchrnetwork.MTServerTalker;
import com.mintcode.launchrnetwork.OnResponseListener;

public class IMAPI {

	protected MTServerTalker mServerTalker;

	private final static String ip = LauchrConst.httpIp + "/launchr";//"http://192.168.1.251:20001/launchr";

	private static IMAPI instance = new IMAPI();

	private IMAPI() {
		mServerTalker = MTServerTalker.getInstance();
	}

	public static IMAPI getInstance() {
		return instance;
	}

	public static final class TASKID {
		public static final String LOGIN = "login";
		public static final String CREATEGROUP = "creategroup";
		public static final String SESSION = "session";
		public static final String DEL_GROUP_USER = "deletegroupuser";
		public static final String ADD_GROUP_USER = "addgroupuser";
		public static final String UPDATE_GROUP_NAME = "updategroupname";
		public static final String READ_SESSION = "readsession";
		public static final String GET_OFFLINE_MSG = "offlinemsg";
		public static final String UN_READ_SESSION = "unreadsession";
		public static final String HISTORY_MESSAGE = "historymessage";
		public static final String RESEND_MESSAGE = "resendmsg";

	}

	public void Login(OnResponseListener listener, String userName,
			String appName) {
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", appName);
		params.setParameter("appToken", "verify-code");
		params.setParameter("userName", userName);
		// params.setParameter("mobile", "13545678521");
		params.setParameter("deviceUUID", LauchrConst.DEVICE_UUID);
		params.setParameter("deviceName", LauchrConst.DEVICE_NAME);
		params.setParameter("os", LauchrConst.OS);
		params.setParameter("osVer", LauchrConst.OS_VER);
		params.setParameter("appVer", LauchrConst.APP_VER);
		mServerTalker.executeHttpMethod(listener, TASKID.LOGIN, LauchrConst.httpIp + "/" + appName
				+ "/api/login", MTHttpManager.HTTP_POST, params, false);
	}

	public void creatGroup(OnResponseListener listener, String userName,
			String userToken, List<String> groupUsers) {
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		params.setParameter("groupUsers", groupUsers);
		mServerTalker.executeHttpMethod(listener, TASKID.CREATEGROUP, ip
				+ "/api/creategroup", MTHttpManager.HTTP_POST, params, false);
	}

	public void getGroupInfo(OnResponseListener listener, String userName,
			String userToken, String sessionName) {
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		params.setParameter("sessionName", sessionName);
		mServerTalker.executeHttpMethod(listener, TASKID.SESSION, ip
				+ "/api/session", MTHttpManager.HTTP_POST, params, false);
	}

	public void delGroupUser(OnResponseListener listener, String userToken,
			String userName, String groupName, String toUserName) {
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		params.setParameter("sessionName", groupName);
		params.setParameter("toUserName", toUserName);
		mServerTalker.executeHttpMethod(listener, TASKID.DEL_GROUP_USER, ip
				+ "/api/deletegroupuser", MTHttpManager.HTTP_POST, params,
				false);
	}

	public void addGroupIser(OnResponseListener listener, String userToken,
			String userName, String groupName, List<String> groupUsers) {
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		params.setParameter("sessionName", groupName);
		params.setParameter("groupUsers", groupUsers);
		mServerTalker.executeHttpMethod(listener, TASKID.ADD_GROUP_USER, ip
				+ "/api/addgroupuser", MTHttpManager.HTTP_POST, params, false);
	}

	public void updateGroupName(OnResponseListener listener, String userToken,
			String userName, String sessionName, String nickName) {
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		params.setParameter("sessionName", sessionName);
		params.setParameter("groupName", nickName);
		mServerTalker.executeHttpMethod(listener, TASKID.UPDATE_GROUP_NAME, ip
				+ "/api/updategroupname", MTHttpManager.HTTP_POST, params,
				false);

	}

	public void readSession(OnResponseListener listener, String userToken,
			String userName, String sessionName, List<String> unreadMsgId) {
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		params.setParameter("sessionName", sessionName);
		params.setParameter("msgIds", unreadMsgId);
		mServerTalker.executeHttpMethod(listener, TASKID.READ_SESSION, ip
				+ "/api/readsession", MTHttpManager.HTTP_POST, params, false);

	}


	public void getOfflineMsg(OnResponseListener listener, String userToken,
			String userName, long msgId){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		params.setParameter("msgId", msgId);

		mServerTalker.executeHttpMethod(listener, TASKID.GET_OFFLINE_MSG, ip
				+ "/api/offlinemsg", MTHttpManager.HTTP_POST, params, false);
	}
	
	public void getUnreadSession(OnResponseListener listener, String appName, String userToken,
			String userName, int start, int end){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		params.setIntParameter("start", start);
		params.setIntParameter("end", end);
		mServerTalker.executeHttpMethod(listener, TASKID.UN_READ_SESSION, ip + "/api/unreadsession",
				MTHttpManager.HTTP_POST, params, false);
	}
	
	public void getHistoryMessage(OnResponseListener listener, String userToken, String userName,
			String from, String to, long limit, long endTimeStamp){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		params.setParameter("to", to);
		params.setParameter("from", from);
		params.setLongParameter("limit", limit);
		//params.setLongParameter("startTimestamp", startTimeStamp);
		params.setLongParameter("endTimestamp", endTimeStamp);
		
		mServerTalker.executeHttpMethod(listener, TASKID.HISTORY_MESSAGE, ip + "/api/historymessage",
				MTHttpManager.HTTP_POST, params, false);
	}

	/** 消息撤回*/
	public void resendMessage(OnResponseListener listener, String userToken, String userName, String type, String content,
							  String from, String to, long clientMsgId, long msgId, String info, long modified){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		params.setParameter("type", type);
		params.setParameter("content", content);
		params.setParameter("from", from);
		params.setParameter("to", to);
		params.setLongParameter("clientMsgId", clientMsgId);
		params.setLongParameter("msgId", msgId);
		params.setParameter("info", info);
		params.setLongParameter("modified", modified);


		String s = params.toJson();
		mServerTalker.executeHttpMethod(listener, TASKID.RESEND_MESSAGE, ip + "/api/resendmsg",
				MTHttpManager.HTTP_POST, params, false);
	}


}
