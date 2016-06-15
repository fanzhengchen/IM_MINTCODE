package mintcode.com.workhub_im.daohelper;

import android.database.sqlite.SQLiteDatabase;

import mintcode.com.workhub_im.App;
import mintcode.com.workhub_im.db.DaoMaster;
import mintcode.com.workhub_im.db.DaoSession;

/**
 * Created by mark on 16-6-15.
 */
public class BaseDaoHelper {

    protected DaoMaster.DevOpenHelper devOpenHelper;
    protected String dbName;
    protected SQLiteDatabase database;
    protected DaoMaster daoMaster;
    protected DaoSession daoSession;

    public BaseDaoHelper(String name) {
        this.dbName = dbName;
        devOpenHelper = new DaoMaster.DevOpenHelper(App.getGlobalContext(), dbName, null);
        database = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }
}
