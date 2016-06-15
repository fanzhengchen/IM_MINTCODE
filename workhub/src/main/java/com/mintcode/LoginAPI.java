package com.mintcode;

import android.content.Context;

import com.mintcode.im.database.KeyValueDBService;
import com.mintcode.im.util.Keys;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchrnetwork.MTHttpManager;
import com.mintcode.launchrnetwork.MTHttpParameters;
import com.mintcode.launchrnetwork.MTServerTalker;
import com.mintcode.launchrnetwork.OnResponseListener;

public class LoginAPI {
    protected MTServerTalker mServerTalker;

    public static final String clt = "clt";
    private String url;

    public LoginAPI(Context context) {
        mServerTalker = MTServerTalker.getInstance();
        url = KeyValueDBService.getInstance().find(Keys.HTTP_IP);
    }

    private static LoginAPI instance;

    public static LoginAPI getInstance(Context context) {
        instance = new LoginAPI(context);
        return instance;
    }

    public static final class TASKID {
        public static final String LOGIN = "login";
    }

    /**
     * 登录
     */
    public void Login(OnResponseListener listener, String name) {
        MTHttpParameters params = new MTHttpParameters();
        params.setParameter("appName", "launchr");
        params.setParameter("apptoken", "verify-code");
        params.setParameter("userName", name);
        params.setParameter("mobile", "13545678521");
        params.setParameter("deviceUUID", LauchrConst.DEVICE_UUID);
        params.setParameter("deviceName", LauchrConst.DEVICE_NAME);
        params.setParameter("os", LauchrConst.OS);
        params.setParameter("osVer", LauchrConst.OS_VER);
        params.setParameter("appVer", LauchrConst.APP_VER);
        mServerTalker.executeHttpMethod(listener, TASKID.LOGIN,
                url + "/api/login", MTHttpManager.HTTP_POST, params, false);
    }

}
