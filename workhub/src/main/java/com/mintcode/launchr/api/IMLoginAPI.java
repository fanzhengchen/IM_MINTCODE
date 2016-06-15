package com.mintcode.launchr.api;

import java.util.List;

import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchrnetwork.MTHttpManager;
import com.mintcode.launchrnetwork.MTHttpParameters;
import com.mintcode.launchrnetwork.MTServerTalker;
import com.mintcode.launchrnetwork.OnResponseListener;

public class IMLoginAPI {

	protected MTServerTalker mServerTalker;

	private final static String ip = "http://192.168.1.251:20001";

	private static IMLoginAPI instance = new IMLoginAPI();

	private IMLoginAPI() {
		mServerTalker = MTServerTalker.getInstance();
	}

	public static IMLoginAPI getInstance() {
		return instance;
	}

	public static final class TASKID {
		public static final String LOGIN = "login";
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
		mServerTalker.executeHttpMethod(listener, TASKID.LOGIN, ip +"/api/login",
				MTHttpManager.HTTP_POST, params, false);
	}
}
