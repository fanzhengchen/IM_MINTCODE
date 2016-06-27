package mintcode.com.workhub_im;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.mintcode.imkit.application.IMKitApplication;
import com.mintcode.imkit.util.IMUtil;
import com.orhanobut.logger.Logger;

import mintcode.com.workhub_im.Http.APIService;
import mintcode.com.workhub_im.Http.IMService;
import mintcode.com.workhub_im.Http.LogJsonInterceptor;
import mintcode.com.workhub_im.beans.GlobalBeans;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.im.IMAPIProvider;
import mintcode.com.workhub_im.util.DensityUtil;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mark on 16-6-8.
 */
public class App extends IMKitApplication {


    private static Retrofit retrofitClient = null;
    private static Retrofit imRetrofitClient = null;
    private static OkHttpClient httpClient = null;
    private static APIService apiService = null;
    private static IMService imService = null;
    private static Context context;
    private static GlobalBeans globalBeans = null;

    @Override
    public void onCreate() {
        super.onCreate();
        UserPrefer.init(getApplicationContext());
        IMUtil.getInstance().saveAppName("WorkHub-IM");
        context = getApplicationContext();
        IMAPIProvider.init(context);
        Logger.init();
        globalBeans = GlobalBeans.getInstance();
        httpClient = new OkHttpClient.Builder()
                .addInterceptor(new LogJsonInterceptor())
                .build();
        UserPrefer.setIP(AppConsts.ip);
        AppConsts.initValues(this);
        DensityUtil.init();
    }


    public static String getDeviceUUID(Context context) {
        int sdk = Build.VERSION.SDK_INT;
        String deviceUUID = null;
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        try {
            deviceUUID = telephonyManager.getDeviceId();// got imei
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(deviceUUID)) {
            if (sdk >= Build.VERSION_CODES.GINGERBREAD) {
                deviceUUID = Build.SERIAL;
            }
            if (deviceUUID == null || deviceUUID.equals("")) {
                deviceUUID = android.provider.Settings.Secure.ANDROID_ID;
            }
            if (deviceUUID == null || deviceUUID.equals("")) {
                deviceUUID = "guest";
            }
        }

        return deviceUUID;
    }

    /**
     * @return 设备名称
     */
    public static String getDeviceName() {
        return Build.MODEL;
    }

    /**
     * @return 版本号
     */
    public static String getAppVer(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * @return 系统版本
     */
    public static String getOsVer() {
        return Build.VERSION.RELEASE;
    }

    public static Context getGlobalContext() {
        return context;
    }

}
