package mintcode.com.workhub_im.im;

import android.content.Context;

import mintcode.com.workhub_im.App;
import mintcode.com.workhub_im.Http.HttpProvider;
import mintcode.com.workhub_im.Http.IMService;
import mintcode.com.workhub_im.im.pojo.IMLoginRequest;
import mintcode.com.workhub_im.im.pojo.IMLoginResponse;
import retrofit2.Callback;

/**
 * Created by mark on 16-6-13.
 */
public class IMAPIProvider {

    private static HttpProvider httpProvider = HttpProvider.getInstance();
    private static IMService imService = httpProvider.getImService();
    private static Context context;

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
}
