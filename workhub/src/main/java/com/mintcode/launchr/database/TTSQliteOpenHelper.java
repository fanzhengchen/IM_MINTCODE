package com.mintcode.launchr.database;


import com.mintcode.launchr.database.TTDBSettings.Contacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * 
 * @author ChristLu
 * 
 */
public class TTSQliteOpenHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "tt.db";

	private static final int DB_VERSION = 9;

	private static TTSQliteOpenHelper sInstance;

	// 检查数据库是否被锁定
	private static String Lock = "dblock";

	private TTSQliteOpenHelper(Context context, String anotherName) {
		super(context, anotherName + "_" + DB_NAME, null, DB_VERSION);
	}

	public synchronized static TTSQliteOpenHelper getInstance(Context context,
			String anotherName) {
		sInstance = new TTSQliteOpenHelper(context, anotherName);

		return sInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Contacts.CREATE_TABLE);
//		db.execSQL(Message.CREATE_TABLE);
//		db.execSQL(MySection.CREATE_TABLE);
//		db.execSQL(MAILTable.CREATE_TABLE);
//		db.execSQL(BulletinTable.CREATE_TABLE);
//		db.execSQL(ToReadTable.CREATE_TABLE);
//		db.execSQL(BulletinsMark.CREATE_TABLE);
//		db.execSQL(Favorite.CREATE_TABLE);

		final int FIRST_DATABASE_VERSION = 1;
		onUpgrade(db, FIRST_DATABASE_VERSION, DB_VERSION);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		

	}

}
