package mintcode.com.workhub_im.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import mintcode.com.workhub_im.db.MessageItem;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MESSAGE_ITEM".
*/
public class MessageItemDao extends AbstractDao<MessageItem, Long> {

    public static final String TABLENAME = "MESSAGE_ITEM";

    /**
     * Properties of entity MessageItem.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property AppName = new Property(1, String.class, "appName", false, "APP_NAME");
        public final static Property UserName = new Property(2, String.class, "userName", false, "USER_NAME");
        public final static Property Modified = new Property(3, Long.class, "modified", false, "MODIFIED");
        public final static Property NickName = new Property(4, String.class, "nickName", false, "NICK_NAME");
        public final static Property MsgId = new Property(5, Long.class, "msgId", false, "MSG_ID");
        public final static Property FromLoginName = new Property(6, String.class, "fromLoginName", false, "FROM_LOGIN_NAME");
        public final static Property ToLoginName = new Property(7, String.class, "toLoginName", false, "TO_LOGIN_NAME");
        public final static Property Info = new Property(8, String.class, "info", false, "INFO");
        public final static Property Content = new Property(9, String.class, "content", false, "CONTENT");
        public final static Property ClientMsgId = new Property(10, Long.class, "clientMsgId", false, "CLIENT_MSG_ID");
        public final static Property CreateDate = new Property(11, Long.class, "createDate", false, "CREATE_DATE");
        public final static Property Type = new Property(12, String.class, "type", false, "TYPE");
        public final static Property Cmd = new Property(13, Integer.class, "cmd", false, "CMD");
        public final static Property TimeText = new Property(14, String.class, "timeText", false, "TIME_TEXT");
        public final static Property FileName = new Property(15, String.class, "fileName", false, "FILE_NAME");
        public final static Property FileSize = new Property(16, String.class, "fileSize", false, "FILE_SIZE");
        public final static Property IsRead = new Property(17, String.class, "isRead", false, "IS_READ");
        public final static Property ActionSend = new Property(18, Integer.class, "actionSend", false, "ACTION_SEND");
        public final static Property IsMarkPoint = new Property(19, Integer.class, "isMarkPoint", false, "IS_MARK_POINT");
        public final static Property IsResp = new Property(20, Integer.class, "isResp", false, "IS_RESP");
        public final static Property Send_time = new Property(21, Long.class, "send_time", false, "SEND_TIME");
        public final static Property ToNickName = new Property(22, String.class, "toNickName", false, "TO_NICK_NAME");
        public final static Property Percent = new Property(23, String.class, "percent", false, "PERCENT");
    };


    public MessageItemDao(DaoConfig config) {
        super(config);
    }
    
    public MessageItemDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MESSAGE_ITEM\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"APP_NAME\" TEXT," + // 1: appName
                "\"USER_NAME\" TEXT," + // 2: userName
                "\"MODIFIED\" INTEGER," + // 3: modified
                "\"NICK_NAME\" TEXT," + // 4: nickName
                "\"MSG_ID\" INTEGER," + // 5: msgId
                "\"FROM_LOGIN_NAME\" TEXT," + // 6: fromLoginName
                "\"TO_LOGIN_NAME\" TEXT," + // 7: toLoginName
                "\"INFO\" TEXT," + // 8: info
                "\"CONTENT\" TEXT," + // 9: content
                "\"CLIENT_MSG_ID\" INTEGER," + // 10: clientMsgId
                "\"CREATE_DATE\" INTEGER," + // 11: createDate
                "\"TYPE\" TEXT," + // 12: type
                "\"CMD\" INTEGER," + // 13: cmd
                "\"TIME_TEXT\" TEXT," + // 14: timeText
                "\"FILE_NAME\" TEXT," + // 15: fileName
                "\"FILE_SIZE\" TEXT," + // 16: fileSize
                "\"IS_READ\" TEXT," + // 17: isRead
                "\"ACTION_SEND\" INTEGER," + // 18: actionSend
                "\"IS_MARK_POINT\" INTEGER," + // 19: isMarkPoint
                "\"IS_RESP\" INTEGER," + // 20: isResp
                "\"SEND_TIME\" INTEGER," + // 21: send_time
                "\"TO_NICK_NAME\" TEXT," + // 22: toNickName
                "\"PERCENT\" TEXT);"); // 23: percent
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MESSAGE_ITEM\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, MessageItem entity) {
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
 
        Long modified = entity.getModified();
        if (modified != null) {
            stmt.bindLong(4, modified);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(5, nickName);
        }
 
        Long msgId = entity.getMsgId();
        if (msgId != null) {
            stmt.bindLong(6, msgId);
        }
 
        String fromLoginName = entity.getFromLoginName();
        if (fromLoginName != null) {
            stmt.bindString(7, fromLoginName);
        }
 
        String toLoginName = entity.getToLoginName();
        if (toLoginName != null) {
            stmt.bindString(8, toLoginName);
        }
 
        String info = entity.getInfo();
        if (info != null) {
            stmt.bindString(9, info);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(10, content);
        }
 
        Long clientMsgId = entity.getClientMsgId();
        if (clientMsgId != null) {
            stmt.bindLong(11, clientMsgId);
        }
 
        Long createDate = entity.getCreateDate();
        if (createDate != null) {
            stmt.bindLong(12, createDate);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(13, type);
        }
 
        Integer cmd = entity.getCmd();
        if (cmd != null) {
            stmt.bindLong(14, cmd);
        }
 
        String timeText = entity.getTimeText();
        if (timeText != null) {
            stmt.bindString(15, timeText);
        }
 
        String fileName = entity.getFileName();
        if (fileName != null) {
            stmt.bindString(16, fileName);
        }
 
        String fileSize = entity.getFileSize();
        if (fileSize != null) {
            stmt.bindString(17, fileSize);
        }
 
        String isRead = entity.getIsRead();
        if (isRead != null) {
            stmt.bindString(18, isRead);
        }
 
        Integer actionSend = entity.getActionSend();
        if (actionSend != null) {
            stmt.bindLong(19, actionSend);
        }
 
        Integer isMarkPoint = entity.getIsMarkPoint();
        if (isMarkPoint != null) {
            stmt.bindLong(20, isMarkPoint);
        }
 
        Integer isResp = entity.getIsResp();
        if (isResp != null) {
            stmt.bindLong(21, isResp);
        }
 
        Long send_time = entity.getSend_time();
        if (send_time != null) {
            stmt.bindLong(22, send_time);
        }
 
        String toNickName = entity.getToNickName();
        if (toNickName != null) {
            stmt.bindString(23, toNickName);
        }
 
        String percent = entity.getPercent();
        if (percent != null) {
            stmt.bindString(24, percent);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public MessageItem readEntity(Cursor cursor, int offset) {
        MessageItem entity = new MessageItem( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // appName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // userName
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // modified
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // nickName
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // msgId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // fromLoginName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // toLoginName
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // info
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // content
            cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10), // clientMsgId
            cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11), // createDate
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // type
            cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13), // cmd
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // timeText
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // fileName
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // fileSize
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // isRead
            cursor.isNull(offset + 18) ? null : cursor.getInt(offset + 18), // actionSend
            cursor.isNull(offset + 19) ? null : cursor.getInt(offset + 19), // isMarkPoint
            cursor.isNull(offset + 20) ? null : cursor.getInt(offset + 20), // isResp
            cursor.isNull(offset + 21) ? null : cursor.getLong(offset + 21), // send_time
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // toNickName
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23) // percent
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, MessageItem entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAppName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUserName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setModified(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setNickName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setMsgId(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setFromLoginName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setToLoginName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setInfo(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setContent(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setClientMsgId(cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10));
        entity.setCreateDate(cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11));
        entity.setType(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setCmd(cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13));
        entity.setTimeText(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setFileName(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setFileSize(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setIsRead(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setActionSend(cursor.isNull(offset + 18) ? null : cursor.getInt(offset + 18));
        entity.setIsMarkPoint(cursor.isNull(offset + 19) ? null : cursor.getInt(offset + 19));
        entity.setIsResp(cursor.isNull(offset + 20) ? null : cursor.getInt(offset + 20));
        entity.setSend_time(cursor.isNull(offset + 21) ? null : cursor.getLong(offset + 21));
        entity.setToNickName(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setPercent(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(MessageItem entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(MessageItem entity) {
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