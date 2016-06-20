package mintcode.com.workhub_im.im;

import android.content.Context;

import mintcode.com.workhub_im.App;
import mintcode.com.workhub_im.AppConsts;
import mintcode.com.workhub_im.Http.HttpProvider;
import mintcode.com.workhub_im.Http.IMService;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.db.SessionItem;
import mintcode.com.workhub_im.im.pojo.IMHistoryMessageRequest;
import mintcode.com.workhub_im.im.pojo.IMLoginRequest;
import mintcode.com.workhub_im.im.pojo.IMLoginResponse;
import mintcode.com.workhub_im.im.pojo.IMMessageResponse;
import mintcode.com.workhub_im.im.pojo.IMSessionResponse;
import mintcode.com.workhub_im.im.pojo.IMUnreadSessionRequest;
import retrofit2.Callback;

/**
 * Created by mark on 16-6-13.
 */
public class IMAPIProvider {

    private static HttpProvider httpProvider = HttpProvider.getInstance();
    private static IMService imService = httpProvider.getImService();
    private static Context context;
    private static final int STEP = 50;
    private static final int mStart = 0;
    private static final int mEnd = 200;

    public static void init(Context ctx) {
        context = ctx;
    }


    public static void imLogin(String userName, Callback<IMLoginResponse> callback) {
        IMLoginRequest request = new IMLoginRequest.Builder()
                .setAppName("launchr")
                .setOS("android")
                .setDeviceUUID(App.getDeviceUUID(context))
                .setDeviceName(App.getDeviceName())
                .setAppToken("verify-code")
                .setUserName(userName)
                .setOsVer(App.getOsVer())
                .builder();
        imService.IMLogin(request).enqueue(callback);
    }

    public static void getUnreadSession(int start, int end, Callback<IMSessionResponse> callback) {
        IMUnreadSessionRequest request = new IMUnreadSessionRequest.Builder()
                .appName(AppConsts.APP_NAME)
                .userToken(UserPrefer.getImToken())
                .userName(UserPrefer.getImUsername())
                .start(start)
                .end(end)
                .build();
        imService.getIMUnreadSession(request).enqueue(callback);
    }

    public static void getHistoryMessage(String token, String userName, String from, String to,
                                         long limit, long endTimeStamp, Callback<IMMessageResponse> callback) {
        IMHistoryMessageRequest request = new IMHistoryMessageRequest.Builder()
                .appName(AppConsts.APP_NAME)
                .userToken(UserPrefer.getImToken())
                .userName(UserPrefer.getImUsername())
                .from(from)
                .to(to)
                .limit(limit)
                .endTimestamp(endTimeStamp)
                .build();
        imService.getHistoryMessage(request).enqueue(callback);
    }
}
