package mintcode.com.workhub_im.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import mintcode.com.workhub_im.db.FriendEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "FRIEND_ENTITY".
*/
public class FriendEntityDao extends AbstractDao<FriendEntity, Long> {

    public static final String TABLENAME = "FRIEND_ENTITY";

    /**
     * Properties of entity FriendEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property AppName = new Property(1, String.class, "appName", false, "APP_NAME");
        public final static Property UserName = new Property(2, String.class, "userName", false, "USER_NAME");
        public final static Property RelationName = new Property(3, String.class, "relationName", false, "RELATION_NAME");
        public final static Property Remark = new Property(4, String.class, "remark", false, "REMARK");
        public final static Property Tag = new Property(5, String.class, "tag", false, "TAG");
        public final static Property RelationGroupId = new Property(6, Integer.class, "relationGroupId", false, "RELATION_GROUP_ID");
        public final static Property CreateDate = new Property(7, Long.class, "createDate", false, "CREATE_DATE");
        public final static Property IsPass = new Property(8, String.class, "isPass", false, "IS_PASS");
        public final static Property NickName = new Property(9, String.class, "nickName", false, "NICK_NAME");
        public final static Property RelationAvatar = new Property(10, String.class, "relationAvatar", false, "RELATION_AVATAR");
        public final static Property RelationModified = new Property(11, Long.class, "relationModified", false, "RELATION_MODIFIED");
        public final static Property Mobile = new Property(12, String.class, "mobile", false, "MOBILE");
        public final static Property ImNumber = new Property(13, String.class, "imNumber", false, "IM_NUMBER");
    };


    public FriendEntityDao(DaoConfig config) {
        super(config);
    }
    
    public FriendEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"FRIEND_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"APP_NAME\" TEXT," + // 1: appName
                "\"USER_NAME\" TEXT," + // 2: userName
                "\"RELATION_NAME\" TEXT," + // 3: relationName
                "\"REMARK\" TEXT," + // 4: remark
                "\"TAG\" TEXT," + // 5: tag
                "\"RELATION_GROUP_ID\" INTEGER," + // 6: relationGroupId
                "\"CREATE_DATE\" INTEGER," + // 7: createDate
                "\"IS_PASS\" TEXT," + // 8: isPass
                "\"NICK_NAME\" TEXT," + // 9: nickName
                "\"RELATION_AVATAR\" TEXT," + // 10: relationAvatar
                "\"RELATION_MODIFIED\" INTEGER," + // 11: relationModified
                "\"MOBILE\" TEXT," + // 12: mobile
                "\"IM_NUMBER\" TEXT);"); // 13: imNumber
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"FRIEND_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, FriendEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String appName = entity.getAppName();
        if (appName != null) {
            stmt.bindString(2, appName);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(3, userName);
        }
 
        String relationName = entity.getRelationName();
        if (relationName != null) {
            stmt.bindString(4, relationName);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(5, remark);
        }
 
        String tag = entity.getTag();
        if (tag != null) {
            stmt.bindString(6, tag);
        }
 
        Integer relationGroupId = entity.getRelationGroupId();
        if (relationGroupId != null) {
            stmt.bindLong(7, relationGroupId);
        }
 
        Long createDate = entity.getCreateDate();
        if (createDate != null) {
            stmt.bindLong(8, createDate);
        }
 
        String isPass = entity.getIsPass();
        if (isPass != null) {
            stmt.bindString(9, isPass);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(10, nickName);
        }
 
        String relationAvatar = entity.getRelationAvatar();
        if (relationAvatar != null) {
            stmt.bindString(11, relationAvatar);
        }
 
        Long relationModified = entity.getRelationModified();
        if (relationModified != null) {
            stmt.bindLong(12, relationModified);
        }
 
        String mobile = entity.getMobile();
        if (mobile != null) {
            stmt.bindString(13, mobile);
        }
 
        String imNumber = entity.getImNumber();
        if (imNumber != null) {
            stmt.bindString(14, imNumber);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public FriendEntity readEntity(Cursor cursor, int offset) {
        FriendEntity entity = new FriendEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // appName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // userName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // relationName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // remark
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // tag
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // relationGroupId
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // createDate
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // isPass
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // nickName
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // relationAvatar
            cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11), // relationModified
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // mobile
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13) // imNumber
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, FriendEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAppName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUserName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRelationName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRemark(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTag(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setRelationGroupId(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setCreateDate(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setIsPass(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setNickName(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setRelationAvatar(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setRelationModified(cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11));
        entity.setMobile(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setImNumber(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(FriendEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(FriendEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
