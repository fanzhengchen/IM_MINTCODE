package mintcode.com.workhub_im;

import android.app.Application;
import android.os.Environment;

import java.io.File;

import mintcode.com.workhub_im.im.codebutler.HybiParser;
import retrofit2.http.PUT;

/**
 * Created by mark on 16-6-13.
 */
public class AppConsts {

    public static final String COMPANY_LIST = "company_list";

    public static final String SUCCESS = "Success";

    public static final String IP = "ip_address";

    public static final String LAUNCHR = "launchr";

    public static final String WORKHUB = "workhub";

    public static String SERVER_PATH = "https://api.launchr.jp";
    /**
     * 服务器附件地址
     */
    public static String ATTACH_PATH = "https://a.launchr.jp";

    public final static String appName = "launchr";

    public static String ip = "ws://imws.launchr.jp:20000";

    public static String httpIp = "http://imhttp.launchr.jp:20001";

    public static final int REQUEST_CODE = 0;
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
    public static int DEVICE_CODE = MT;
    public static final boolean IS_WORKHUB = true;

    public static String DOWNLOAD_FILE_PATH_DATA;
    public static String DOWNLOAD_FILE_PATH_SDCARD;
    public static String DOWNLOAD_AUDIO_PATH_SDCARD;

    public static void initValues(Application app){

        DOWNLOAD_FILE_PATH_DATA = app.getFilesDir().getAbsolutePath() + "/mintcode/file/";
        DOWNLOAD_FILE_PATH_SDCARD = Environment.getExternalStorageDirectory().getPath() + "/mintcode/download/";
        if (app.getExternalCacheDir() != null) {
            DOWNLOAD_AUDIO_PATH_SDCARD = app.getExternalCacheDir().getAbsolutePath() + File.separator;
        } else {
            DOWNLOAD_AUDIO_PATH_SDCARD = app.getCacheDir().getAbsolutePath() + File.separator;
        }
    }


    static {
        switch (DEVICE_CODE) {
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

    public static final String APP_NAME = (DEVICE_CODE == MT) ? LAUNCHR : WORKHUB;


    public static final String CHAT_ROOM = "@ChatRoom";

    public static final String SUPER_GROUP = "@SuperGroup";
}
