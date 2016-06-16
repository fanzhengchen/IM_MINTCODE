package mintcode.com.workhub_im.daohelper;

import android.database.sqlite.SQLiteDatabase;

import mintcode.com.workhub_im.App;
import mintcode.com.workhub_im.db.DaoMaster;
import mintcode.com.workhub_im.db.DaoSession;

/**
 * Created by mark on 16-6-15.
 */
public class BaseDaoHelper {

    protected static DaoMaster.DevOpenHelper devOpenHelper;
    protected static String dbName;
    protected static SQLiteDatabase database;
    protected static DaoMaster daoMaster;
    protected static DaoSession daoSession;

    public static void createDB(String name) {
        dbName = name;
        devOpenHelper = new DaoMaster.DevOpenHelper(App.getGlobalContext(), dbName, null);
        database = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }
}
