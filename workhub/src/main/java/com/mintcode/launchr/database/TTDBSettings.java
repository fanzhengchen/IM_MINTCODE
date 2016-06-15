package com.mintcode.launchr.database;

import android.provider.BaseColumns;

/**
 * 关于数据库的表结构定义
 * 
 * @author RobinLin
 * 
 */
public class TTDBSettings {
	/*
	 * Remarks： SQLite基本类型如下： integer text boolean timestamp
	 */

	/**
	 * 信息表
	 * 
	 * @author RobinLin
	 * 
	 */
	public static interface Message extends BaseColumns {
		/**
		 * 表名
		 */
		static final String TABLE_NAME = "message";

		// TODO:列名注释稍后跟上...@RobinLin
		static final String MESSAGE_ID = "message_id";
		static final String LOGIN_NAME = "login_name";
		/**
		 * 服务端消息Id 被接收者服务端最大的msgId
		 */
		static final String MSG_ID = "msg_id";
		/**
		 * 客户端用户名 发消息的人
		 */
		static final String FROM_LOGIN_NAME = "from_login_name";
		/**
		 * 接收消息的人
		 */
		static final String TO_LOGIN_NAME = "to_login_name";
		/**
		 * 客户端用户信息
		 */
		static final String INFO = "info";
		/**
		 * 消息内容
		 */
		static final String CONTENT = "content";
		/**
		 * 组织成员
		 */
		static final String MEMBER_LIST = "member_list";
		/**
		 * 客户端消息Id 当前手机发送者时间戳
		 */
		static final String CLIENT_MSG_ID = "client_msg_id";
		/**
		 * creatDate 当前服务器时间戳
		 */
		static final String CREATE_DATE = "create_date";
		/**
		 * 消息类型id 包括个人消息，群消息，公告等
		 */
		static final String TYPE = "type";
		/**
		 * 是发送消息，还是接收消息 0：发送 1：接收
		 */
		static final String CMD = "cmd";

		/**
		 * 保存文件 ，图片，音频名字
		 */
		static final String FILE_NAME = "file_name";
		/**
		 * 保存文件大小
		 */
		static final String FILE_SIZE = "file_size";
		/**
		 * 语音 时间
		 */
		static final String TIME_TEXT = "time_text";
		/**
		 * 位置信息
		 */
		static final String LOCATION_INFO = "location_info";

		/**
		 * 消息发送状态 0 成功，1 失败
		 */
		static final String ACTION_SEND = "action_send";
		/**
		 * 语音消息已读未读
		 */
		static final String ISREAD = "isread";
		/**
		 * 发送状态 用于显示loading框
		 */
		static final String ISSENT = "issent";
		/**
		 * 判断是否收到回执
		 */
		static final String MARKED_RESP = "marked_resp";
		/**
		 * 记录消息刚发的时间戳
		 */
		static final String SEND_TIME = "send_time";

		/**
		 * 用于存储缩略图
		 */
		static final String THUMBNAIL = "thumbnail";

		/**
		 * 建表语句
		 */
		static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `"
				+ TABLE_NAME + "` (`" + _ID
				+ "` integer NOT NULL PRIMARY KEY ,`" + MESSAGE_ID
				+ "` integer ,`" + LOGIN_NAME + "` text,`" + MSG_ID
				+ "` integer ,`" + FROM_LOGIN_NAME + "` text,`" + TO_LOGIN_NAME
				+ "` text,`" + INFO + "` text,`" + CONTENT + "` text,`"
				+ MEMBER_LIST + "` integer ,`" + CLIENT_MSG_ID + "` integer ,`"
				+ CREATE_DATE + "` integer ,`" + TYPE + "` integer ," + CMD
				+ " integer ," + FILE_NAME + " text, " + TIME_TEXT + " text, "
				+ FILE_SIZE + " text, " + LOCATION_INFO + " text,"
				+ ACTION_SEND + " integer," + ISREAD + " integer ," + ISSENT
				+ " integer ," + SEND_TIME + " integer ," + MARKED_RESP
				+ " integer )";

	}

	/**
	 * 常用联系人表
	 * 
	 * @author ChristLu
	 * 
	 */
	public static interface Contacts extends BaseColumns {
		/**
		 * 表名
		 */
		static final String TABLE_NAME = "contacts";

		/**
		 * 联系人类型，是常用联系人还是普通联系人（通讯录）
		 */
		static final String CONTACTS_TYPE = "contacts_type";
		/**
		 * 登陆名
		 */
		static final String LOGINNAME = "loginname";
		/**
		 * 别名
		 */
		static final String ANOTHERNAME = "anothername";
		/**
		 * 真实名字
		 */
		static final String REALNAME = "realname";
		/**
		 * 所在部门
		 */
		static final String DEPTNAME = "deptname";
		/**
		 * 所在部门的上级部门
		 */
		static final String PARENTDEPTNAME = "parentdeptName";

		/** 职位 */
		static final String POSITION = "position";

		/**
		 * 手机号码
		 */
		static final String MOBLIEPHONE = "mobliePhone";
		/**
		 * 联系人头像
		 */
		static final String HEADPIC = "headpic";
		/**
		 * 办公电话
		 */
		static final String OFFICEPHONE = "officephone";
		/**
		 * 最近消息内容
		 */

		static final String CONTENT = "content";
		/**
		 * 未读消息数
		 */
		static final String NUM = "num";
		/**
		 * 时间
		 */
		static final String TIME = "time";
		/**
		 * 消息类型
		 */
		static final String TYPE = "type";
		/**
		 * 群成员信息
		 */
		static final String INFO = "info";

		/**
		 * 置顶字段，值越大，就排在前面
		 */
		static final String PRIORITY = "priority";

		/**
		 * 排序字段
		 */
		static final String ORDERTIME = "ordertime";

		/**
		 * 建表语句
		 */
		static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME + " (" + _ID + " integer NOT NULL PRIMARY KEY,"
				+ CONTACTS_TYPE + " text," + LOGINNAME + " text ,"
				+ ANOTHERNAME + " text," + REALNAME + " text," + DEPTNAME
				+ " text," + PARENTDEPTNAME + " text," + MOBLIEPHONE + " text,"
				+ POSITION + " text, " + HEADPIC + " text," + OFFICEPHONE
				+ " text, " + CONTENT + " text, " + NUM + " integer, " + TIME
				+ " text, " + INFO + " text, " + TYPE + " integer)";
	}

	/**
	 * 我的部门联系人表
	 */
	public static interface MySection extends BaseColumns {
		/**
		 * 表名
		 */
		static final String TABLE_NAME = "section";
		/**
		 * 上级部门
		 */
		static final String PARENTDEPTNAME = "parentDeptName";
		/**
		 * 部门
		 */
		static final String DEPTNAME = "deptName";
		/**
		 * 登录名
		 */
		static final String LOGINNAME = "loginName";
		/**
		 * 别名
		 */
		static final String ANOTHERNAME = "anotherName";
		/**
		 * 真实姓名
		 */
		static final String REALNAME = "realName";
		/**
		 * 手机号
		 */
		static final String MOBLIEPHONE = "mobliePhone";
		/**
		 * 头像地址
		 */
		static final String HEADPIC = "headPic";
		/**
		 * 公司电话
		 */
		static final String OFFICEPHONE = "officePhone";

		/** 职位 */
		static final String POSITION = "position";

		/**
		 * 建表语句
		 */
		static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME + " (" + _ID + " integer NOT NULL PRIMARY KEY,"
				+ PARENTDEPTNAME + " text," + DEPTNAME + " text," + LOGINNAME
				+ " text," + ANOTHERNAME + " text," + REALNAME + " text,"
				+ POSITION + " text," + MOBLIEPHONE + " text," + HEADPIC
				+ " text," + OFFICEPHONE + " text)";
	}

	/**
	 * @ClassName: MAILTable
	 * @author : KevinQiao
	 * @date 2014-9-17 上午11:44:51
	 * @Description: 邮件
	 */
	public static interface MAILTable extends BaseColumns {
		static final String TABLE_NAME = "mail_list";

		/**
		 * 信件所在文件夹标识
		 */
		static final String MAIL_ID = "mail_id";
		static final String FROM = "_from"; // 发送者
		static final String TO = "_to"; // 接收方
		static final String SUBJECT = "subject"; // 邮件标题
		static final String SENTDATE = "sentDate"; // 发送时间
		static final String PRIORITY = "priority"; // 邮件的优先级 0/1表示最高，//
													// 4/5表示最低，3表示普通
		static final String ISATTACHED = "isAttached"; //
		static final String TIMESTAMP = "timestamp"; //
		static final String TYPE = "type"; // 接收时间

		static final String MARKED = "marked";
		static final String ISREAD = "is_read";
		static final String ISFROMHISTORY = "is_from_history";

		static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME + "(" + _ID
				+ " integer NOT NULL PRIMARY KEY AUTOINCREMENT, " + MAIL_ID
				+ " text not null," + FROM + " text not null," + TO
				+ " text not null," + SUBJECT + " text ," + SENTDATE
				+ " text not null," + PRIORITY + " integer ," + ISATTACHED
				+ " text not null," + TIMESTAMP + " text not null," + TYPE
				+ " integer not null," + ISFROMHISTORY + " integer," + ISREAD
				+ " text not null," + MARKED + " text )";

	}

	/**
	 * @ClassName: NoticeTable
	 * @author : KevinQiao
	 * @date 2014-9-2 下午8:11:01
	 * @Description: 公告表
	 */
	public static interface BulletinTable extends BaseColumns {
		// 表名
		static final String TABLE_NAME = "bullet_list";
		// TODO 列名稍后注释
		static final String BULLETINID = "bulletinid";
		static final String BULLETINTITLE = "bulletintitle";
		static final String CREATETIME = "createtime";
		static final String HASATTACHMENT = "hasattachment";
		static final String ISREAD = "is_read";

		static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME + "(" + _ID
				+ " integer NOT NULL PRIMARY KEY AUTOINCREMENT," + BULLETINID
				+ " integer ," + BULLETINTITLE + " text ," + CREATETIME
				+ " text ," + HASATTACHMENT + " text ," + ISREAD + " text )";

	}

	/**
	 * 待阅表结构及建表语句
	 * 
	 * @author RobinLin
	 * 
	 */
	public static interface ToReadTable extends BaseColumns {
		// 表名
		static final String TABLE_NAME = "to_read_list";
		// TODO 列名稍后注释
		static final String ID = "to_read_id";
		static final String DEPTNAME = "deptName";
		static final String TYPE = "type";
		static final String TITLE = "title";
		static final String SENDER = "sender";
		static final String CREATETIME = "createTime";
		static final String TIMESTAMP = "timeStamp";
		static final String MARKED = "marked";
		static final String ISREAD = "is_read";
		static final String ISFROMHISTORY = "is_from_history";

		static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME + "(" + _ID
				+ " integer NOT NULL PRIMARY KEY AUTOINCREMENT," + ID
				+ " text not null," + DEPTNAME + " text ," + TYPE
				+ " integer ," + TITLE + " text ," + SENDER + " text ,"
				+ CREATETIME + " text ," + TIMESTAMP + " text ,"
				+ ISFROMHISTORY + " integer," + ISREAD + " text ," + MARKED
				+ " text )";

	}

	/**
	 * 公告表结构及建表语句 主要用于记录已读未读
	 * 
	 * @author ChristLu
	 * 
	 */
	public static interface BulletinsMark extends BaseColumns {
		// 表名
		static final String TABLE_NAME = "bulletins_mark";
		// TODO 列名稍后注释
		public static final String ID = "_id";
		public static final String BULLETINS_ID = "bulletins_id";

		static final String CREATE_TABLE = "CREATE TABLE if not exists "
				+ TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ BULLETINS_ID + " INTEGER NOT NULL " + ")";

	}

	public static interface Favorite extends BaseColumns {
		static final String TABLE_NAME = "favorite_table";

		public static final String ANOTHER_NAME = "another_name";
		static final String CREATE_TABLE = "CREATE TABLE if not exists "
				+ TABLE_NAME + "(" + _ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + ANOTHER_NAME
				+ " text unique " + ")";
	}

	public static interface CRM extends BaseColumns {
		public static final String TABLE_NAME = "CRM";
		public static final String ID = "_id";
		public static final String COMPANY = "company";
		public static final String RANK = "rank";
		public static final String COMPANY_TYPE = "company_typy";
		public static final String TEL = "tel";
		public static final String EMAIL = "email";
		public static final String URL = "url";
		public static final String ADDRESS = "address";
		public static final String INTRO = "intro";

		static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME + " (" + _ID + " integer NOT NULL PRIMARY KEY,"
				+ COMPANY + " text," + RANK + " integer ," + COMPANY_TYPE
				+ " text," + TEL + " text," + EMAIL + " text," + URL + " text,"
				+ ADDRESS + " text," + INTRO + " text)";
	}

	/**
	 * 消息设置字段 包括 铃声，振动，屏蔽等
	 * 
	 * @author ChristLu
	 * 
	 */
	public static interface MsgSetting extends BaseColumns {
		public static final String TABLE_NAME = "MsgSetting";
		public static final String ID = "_id";
		// 要设置消息的对象
		public static final String ANOTHERNAME = "anotherName";
		// remark
		public static final String REMARK = "remark";
		// 屏蔽 0 1
		public static final String SHIELD = "shield";
		// 显示昵称0 1
		public static final String SHOW_NAME = "show_name";

		static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME + " (" + _ID + " integer NOT NULL PRIMARY KEY,"
				+ ANOTHERNAME + " text," + REMARK + " text  ," + SHIELD
				+ " integer NOT NULL," + SHOW_NAME + " integer NOT NULL)";
	}

	/**
	 * msgId表，用于socket连接时将最大的msgId发送
	 */
	public static interface MsgId extends BaseColumns {
		public static final String TABLE_NAME = "MsgId";
		public static final String ID = "_id";
		public static final String MSGID = "msgId";
		static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME + " (" + _ID + " integer NOT NULL PRIMARY KEY,"
				+ MSGID + " integer NOT NULL)";

	}

	/**
	 * msgId表，用于socket连接时将最大的msgId发送
	 */
	public static interface Leave extends BaseColumns {
		public static final String TABLE_NAME = "table_for_leave";
		public static final String ID = "_id";
		// 三个模块 待我审批 我的假条 待我阅读
		public static final String TYPE = "_type";
		public static final String LEAVE_ID = "leave_id";
		// 请假标题
		public static final String TITLE = "leave_title";
		// 请假类型 病假 婚假等
		public static final String LEAVE_TYPE = "leave_type";
		// 创建者姓名
		public static final String CREATORREALNAME = "leave_creator_realname";
		// 创建者username
		public static final String CREATORLOGINNAME = "leave_creator_loginname";
		// 创建事件
		public static final String CREATETIME = "leave_creattime";
		// 请假内容
		public static final String CONTENT = "leave_content";
		// 发起者头像
		public static final String CREATORHEADPIC = "leave_creator_headpic";
		// 最后更新时间
		public static final String LASTTIME = "lasttime";
		// 扩展字段1-2
		public static final String EXTEND_1 = "_input";
		public static final String EXTEND_2 = "_help";
		public static final String EXTEND_3 = "_index";

		static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME + " (" + _ID + " integer NOT NULL PRIMARY KEY,"
				+ TYPE + " integer NOT NULL ," + LEAVE_ID
				+ " integer NOT NULL ," + TITLE + " text ," + LEAVE_TYPE
				+ " text ," + CREATORREALNAME + " text ," + CREATORLOGINNAME
				+ " text ," + CREATETIME + " integer NOT NULL ," + CONTENT
				+ " text ," + CREATORHEADPIC + " text ," + LASTTIME
				+ " integer NOT NULL ," + EXTEND_1 + " text ," + EXTEND_2
				+ " text ," + EXTEND_3 + " text  )";

	}

}