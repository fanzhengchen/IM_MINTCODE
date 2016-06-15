package com.mintcode.launchr.consts;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.mintcode.im.IMManager;
import com.mintcode.launchr.R;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.util.AppUtil;

import org.litepal.LitePalManager;

import java.io.File;
import java.util.Locale;
import java.util.Map;

public class LauchrConst {

    /**
     * 服务器地址
     */
    public static String SERVER_PATH = "https://api.launchr.jp";
    /**
     * 服务器附件地址
     */
    public static String ATTACH_PATH = "https://a.launchr.jp";
    public final static String appName = "launchr";
    public static String ip = "ws://imws.launchr.jp:20000";
    public static String httpIp = "http://imhttp.launchr.jp:20001";
    /**
     * 日本环境
     */
    public final static int JP = 0;
    /**
     * 中国的每天Minteam
     */
    private final static int CN = 1;
    /**
     * 曼拓内网
     */
    private final static int MINTCODE = 2;

    /**
     * mintcode 公司版
     */
    public static final int MT = 3;
    /**
     * 日本测试环境
     */
    private static final int JPTEST = 4;

    private static final int BELL = 6;
    private static final int LARY = 7;
    /**
     * 最终版本
     */
    public static int DEV_CODE = MT;
    /**
     * WorkHub版本开关，开发Launchr时，确保为false
     */
    public static final boolean IS_WORKHUB = true;

    static {
        switch (DEV_CODE) {
            case JP:
                //日本环境
                SERVER_PATH = "https://api.workhub.jp";
                ATTACH_PATH = "https://a.workhub.jp";
                ip = "wss://imws.workhub.jp";
                httpIp = "https://imhttp.workhub.jp";
                break;
            case CN:
                //中国的每天Minteam
                SERVER_PATH = "http://api.mintcode.com";
                ATTACH_PATH = "http://a.mintcode.com";
                ip = "ws://imws.mintcode.com:20000";
                httpIp = "http://imhttp.mintcode.com";
                break;

            case MT:
                //Minteam 公司版
                SERVER_PATH = "http://api.mintcode.com";
                ATTACH_PATH = "http://a.mintcode.com";
                ip = "ws://imws.mintcode.com:20000";
                httpIp = "http://imhttp.mintcode.com";
                break;

            case MINTCODE:
                //曼拓内网
                SERVER_PATH = "http://192.168.1.249:6002";//http://Totoro:6002
                ATTACH_PATH = "http://192.168.1.249:6003";
                ip = "ws://192.168.1.251:20000";          //不带加密的
                httpIp = "http://192.168.1.251:20001";
                break;
            case JPTEST:
                //日本环境
                SERVER_PATH = "http://apitest.workhub.jp";
                ATTACH_PATH = "http://atest.workhub.jp";
                ip = "ws://imwstest.launchr.jp:20000";
                httpIp = "http://imhttptest.workhub.jp";
                break;

            case BELL:
                SERVER_PATH = "http://192.168.1.249:6002";//http://Totoro:6002
                ATTACH_PATH = "http://192.168.1.249:6003";
                ip = "ws://192.168.2.92:20000";          //不带加密的
                httpIp = "http://192.168.1.251:20001";
                break;
            case LARY:
                SERVER_PATH = "http://192.168.1.249:6002";//http://Totoro:6002
                ATTACH_PATH = "http://192.168.1.249:6003";
                ip = "ws://192.168.2.102:20000";          //不带加密的
                httpIp = "http://192.168.1.251:20001";
                break;
            default:
                break;
        }
    }

    /**
     * token key
     */
    public static final String KEY_AUTH_TOKEN = "key_auth_token";


    /**
     * 登录名 key
     */
    public static final String KEY_LOGINNAME = "key_loginname";

    /**
     * 用户名  key
     */
    public static final String KEY_USERNAME = "key_username";

    /**
     * 公司名 key
     */
    public static final String KEY_COMPANYSHOWID = "key_companyshowid";

    /**
     * 公司代码 key
     */
    public static final String KEY_COMPANYCODE = "key_companycode";

    /**
     * uri key
     */
    public static final String KEY_RESOURCEURI = "key_resourceuri";

    /**
     * 链接key
     */
    public static final String KEY_URL = "key_url";

    /**
     * 临时存放登录名
     */
    public static final String KEY_TEMP_LOGINNAME = "key_temp_loginname";

    /**
     * 临时存放密码
     */
    public static final String KEY_TEMP_PASSWORD = "key_temp_password";

    /**
     * 临时存放公司名称
     */
    public static final String KEY_TEMP_COMPANY_NAME = "key_temp_company_name";

    public static final String KEY_ASYNC = "key_async";

    public static final String KEY_TYPE = "key_type";

    public static final String KEY_LANGUAGE = "key_language";

    public static final String KEY_SHOWID = "key_showid";
    public static final String KEY_IMTOKEN = "key_IMToken";

    public static final String KEY_USERENTITY = "key_userentity";

    public static final String KEY_MAX_MSGID = "key_max_msgid";

    /**
     * 系统语言
     */
    public static String LANGUAGE;

    /**
     * header
     */
    public static HeaderParam header;

    public static String IM_SESSION_AUDIO;
    public static String IM_SESSION_IMAGE;
    public static String IM_SESSION_VIDEO;
    public static String IM_SESSION_FILE;

    public static String DOWNLOAD_FILE_PATH_DATA;
    public static String DOWNLOAD_FILE_PATH_SDCARD;
    public static String DOWNLOAD_AUDIO_PATH_SDCARD;


    /**
     * 获得UUID
     *
     * @param app
     */
    public static void initValues(Application app) {
        LANGUAGE = getLanguage(app);
        header = getHeader(app);

        DEVICE_UUID = getDeviceUUID(app);
        DEVICE_NAME = getDeviceName();
        VERSION_NAME = getDeviceName();
        APP_VER = getAppVer(app);
        OS_VER = getOsVer();

        IM_SESSION_AUDIO = app.getString(R.string.im_session_audio);
        IM_SESSION_IMAGE = app.getString(R.string.im_session_image);
        IM_SESSION_VIDEO = app.getString(R.string.im_session_video);
        IM_SESSION_FILE = app.getString(R.string.im_session_file);

        DOWNLOAD_FILE_PATH_DATA = app.getFilesDir().getAbsolutePath() + "/file/";
        DOWNLOAD_FILE_PATH_SDCARD = Environment.getExternalStorageDirectory().getPath() + "/mintcode/launchr/download/";

        if (app.getExternalCacheDir() != null) {
            DOWNLOAD_AUDIO_PATH_SDCARD = app.getExternalCacheDir().getAbsolutePath() + File.separator;
        } else {
            DOWNLOAD_AUDIO_PATH_SDCARD = app.getCacheDir().getAbsolutePath() + File.separator;
        }
    }

    public static String getOsVer() {
        return Build.VERSION.RELEASE;
    }

    public static String getDeviceName() {
        return Build.MODEL;
    }

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
     * 获取header实例
     *
     * @param context
     * @return
     */
    public static HeaderParam getHeader(Context context) {
        HeaderParam header = new HeaderParam();
        //
        AppUtil util = AppUtil.getInstance(context);
        String loginName = util.getLoginName();
        header.setLoginName(loginName);

        String userName = util.getUserName();
        header.setUserName(userName);

        String comanyShowId = util.getCompanyShowID();
        header.setCompanyShowID(comanyShowId);

        String comanyCode = util.getCompanyCode();
        header.setCompanyCode(comanyCode);

        String token = util.getAuthToken();
        header.setAuthToken(token);

        boolean async = util.getAsync();
        header.setAsync(async);

        String type = util.getType();
        header.setType(type);

        String language = util.getLanguage();
        header.setLanguage(language);

        return header;
    }

    public static void resetHeader(
            Context context) {
        header = getHeader(context);
    }

    public static String getLanguage(Context context) {
        //获取系统当前使用的语言
        String lan = Locale.getDefault().getLanguage();
        //获取区域
        String country = Locale.getDefault().getCountry();

        return lan + "-" + country;

    }

    /**
     * 获得设备标识码
     *
     * @param context
     * @return
     */
    public static String getDeviceUUID(Context context) {
        int sdk = Build.VERSION.SDK_INT;
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceUUID = telephonyManager.getDeviceId();// got imei
        if (deviceUUID == null || deviceUUID.equals("")) {
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

    public static String DEVICE_UUID;
    public static String DEVICE_NAME;
    public static String VERSION_NAME;
    public static String OS = "Android";
    public static String OS_VER;
    public static String APP_VER;

    public static void IMLogin(final Context context, final Map<String, String> info, final long modified) {
        AppUtil appUtil = AppUtil.getInstance(context);
        String token = appUtil.getIMToken();
        String userName = appUtil.getShowId();
        String companyCode = appUtil.getCompanyCode();
        LitePalManager.reset();
        LitePalManager.setDbName(userName + "_" + companyCode);
        IMManager.getInstance(context, appName, token, userName, modified, info, ip, httpIp);
    }

    public static boolean PUSH_SERVICE_IS_LOGIN = false;

}
