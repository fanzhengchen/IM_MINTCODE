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

    public static String getUserName() {
        return preferences.getString(USER_NAME, "");
    }

    public static String getPassword() {
        return preferences.getString(PASSWORD, "");
    }
}
