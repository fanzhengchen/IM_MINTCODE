package mintcode.com.workhub_im.beans;

import android.content.Context;
import android.content.SharedPreferences;

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
}
