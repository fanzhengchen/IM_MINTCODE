package mintcode.com.workhub_im.daohelper;

import android.database.sqlite.SQLiteDatabase;

import mintcode.com.workhub_im.App;
import mintcode.com.workhub_im.db.DaoMaster;
import mintcode.com.workhub_im.db.DaoSession;

/**
 * Created by mark on 16-6-15.
 */
public class SessionDaoHelper {
    private static SessionDaoHelper sessionDaoHelper;

    private DaoMaster.DevOpenHelper devOpenHelper;
    private String dbName;
    private SQLiteDatabase database;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private SessionDaoHelper(String name) {
        this.dbName = dbName;
    }

    private void createDB() {
        devOpenHelper = new DaoMaster.DevOpenHelper(App.getGlobalContext(), dbName, null);
        database = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public static SessionDaoHelper getInstance(String dbName) {
        if (sessionDaoHelper == null) {
            sessionDaoHelper = new SessionDaoHelper(dbName);
            sessionDaoHelper.createDB();
        }
        return sessionDaoHelper;
    }
}
