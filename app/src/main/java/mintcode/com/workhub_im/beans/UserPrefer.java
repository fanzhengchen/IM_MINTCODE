package mintcode.com.workhub_im.beans;

import android.content.Context;
import android.content.SharedPreferences;

import retrofit2.Retrofit;
import retrofit2.http.PUT;

/**
 * Created by fanzhengchen on 6/10/16.
 */
public class UserPrefer {

    private static Context context;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;


    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";
    public static final String SHOW_ID = "show_id";
    public static final String IM_TOKEN = "im_token";
    public static final String IP_ADDRESS = "ip_address";
    public static final String COMPANY_CODE = "company_code";
    public static final String AES_KEY = "aes_key";
    public static final String IM_USERNAME = "im_username";
    public static final String LAST_MESSAGE_ID = "last_message_id";
    public static final String DB_NAME = "db_name";
    public static final String MODIFIED = "modified";
    public static final String INFO = "info";

    public static void init(Context ctx) {
        context = ctx;
        preferences = ctx.getSharedPreferences(ctx.getPackageName(), Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void setUserName(String userName) {
        editor.putString(USER_NAME, userName)
                .commit();
    }

    public static void setPassword(String password) {
        editor.putString(PASSWORD, password)
                .commit();
    }

    public static void setShowId(String showId) {
        editor.putString(SHOW_ID, showId)
                .commit();
    }

    public static void setImToken(String imToken) {
        editor.putString(IM_TOKEN, imToken)
                .commit();
    }

    public static void setIP(String ip) {
        editor.putString(IP_ADDRESS, ip)
                .commit();
    }

    public static void setCompanyCode(String companyCode) {
        editor.putString(COMPANY_CODE, companyCode)
                .commit();
    }

    public static void setAesKey(String aesKey) {
        editor.putString(AES_KEY, aesKey)
                .commit();
    }

    public static void setImUsername(String username) {
        editor.putString(IM_USERNAME, username)
                .commit();
    }

    public static void setLastMessageId(long messageId) {
        editor.putLong(LAST_MESSAGE_ID, messageId)
                .commit();
    }

    public static void setDbName(String dbName) {
        editor.putString(DB_NAME, dbName)
                .commit();
    }

    public static void setModified(long modified) {
        editor.putLong(MODIFIED, modified)
                .commit();
    }

    public static void setInfo(String info) {
        editor.putString(INFO, info)
                .commit();
    }

    public static void updateMsgId(long nowMsgId) {
        long oldMsgId = getLastMessageId();
        nowMsgId = Math.max(oldMsgId, nowMsgId);
        setLastMessageId(oldMsgId);
    }

    public static String getUserName() {
        return preferences.getString(USER_NAME, "");
    }

    public static String getPassword() {
        return preferences.getString(PASSWORD, "");
    }

    public static String getShowId() {
        return preferences.getString(SHOW_ID, "");
    }

    public static String getImToken() {
        return preferences.getString(IM_TOKEN, "");
    }

    public static String getIpAddress() {
        return preferences.getString(IP_ADDRESS, "");
    }

    public static String getCompanyCode() {
        return preferences.getString(COMPANY_CODE, "");
    }

    public static String getAesKey() {
        return preferences.getString(AES_KEY, "");
    }

    public static String getImUsername() {
        return preferences.getString(IM_USERNAME, "");
    }

    public static long getLastMessageId() {
        return preferences.getLong(LAST_MESSAGE_ID, -1);
    }

    public static String getDbName() {
        return preferences.getString(DB_NAME, "db_name");
    }

    public static long getModified() {
        return preferences.getLong(MODIFIED, 0);
    }

    public static String getInfo() {
        return preferences.getString(INFO, "");
    }
}
