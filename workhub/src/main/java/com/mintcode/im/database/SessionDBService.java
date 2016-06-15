package com.mintcode.im.database;

import java.util.List;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mintcode.UnReadSessionPOJO.Sessions;
import com.mintcode.chat.entity.Info;
import com.mintcode.chat.entity.MergeEntity;
import com.mintcode.chat.entity.ResendEntity;
import com.mintcode.chat.user.GroupInfo;
import com.mintcode.chat.user.GroupInfoDBService;
import com.mintcode.chat.util.JsonUtil;
import com.mintcode.im.Command;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.entity.SessionItem;
import com.mintcode.im.util.Keys;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.entity.MessageEventEntity;
import com.mintcode.launchr.pojo.entity.MessageInfoEntity;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.TTJSONUtil;

public class SessionDBService {

	private static SessionDBService sInstance;

	private static KeyValueDBService keyValueDBService;

	public SessionDBService() {
		keyValueDBService = KeyValueDBService.getInstance();
	}

	public synchronized static SessionDBService getInstance() {
		if (sInstance == null) {
			sInstance = new SessionDBService();
		}
		return sInstance;
	}

	/**
	 * 查看某用户是否存在
	 * 
	 * @param uid
	 * @return
	 */
	public boolean exist(String uid) {
		try {
			List<SessionItem> sessionItems = DataSupport.where(
					"oppositeName = ? and userName = ?", uid,
					keyValueDBService.find(Keys.UID)).find(SessionItem.class);
			return sessionItems.size() == 0 ? false : true;
		} catch (Exception e) {
			return false;
		}

	}

	public boolean isMsgExist(MessageItem item) {
		List<MessageItem> list = DataSupport.where("msgid=?",item.getMsgId() + "").find(MessageItem.class);
		if(list != null && list.size() == 1){
			return true;
		}else{
			return false;
		}
	}

	public int getUnReadFromMsg(MessageItem item){

		List<MessageItem> list = DataSupport.where("fromloginname = ? and tologinname = ? and isread = '0'", item.getFromLoginName(), item.getToLoginName())
		           .find(MessageItem.class);

		int len = list == null? 0 : list.size();
		return len;
	}


	private int getId(String uid){
		try {
			List<SessionItem> sessionItems = DataSupport.where(
					"oppositeName = ? and userName = ?", uid,
					keyValueDBService.find(Keys.UID)).find(SessionItem.class);
			
			return sessionItems.size() == 0 ? 0 : sessionItems.get(0).getId();
		} catch (Exception e) {
			return 0;
		}

	}
	
	/**
	 * 插入session信息
	 * 
	 * @param item
	 */
	public void insert(MessageItem item) {
		Log.d("session", "插入");
		String content;
		boolean isSystemMsg = false;
		if (item.getType().equals(Command.AUDIO)) {
			content = LauchrConst.IM_SESSION_AUDIO;
		} else if (item.getType().equals(Command.IMAGE)) {
			content = LauchrConst.IM_SESSION_IMAGE;
		} else if (item.getType().equals(Command.VIDEO)) {
			content = LauchrConst.IM_SESSION_VIDEO;
		} else if(item.getType().equals(Command.EVENT)){
			content = getTaskMessageContent(item);
		} else if(item.getType().equals(Command.FILE)){
			content = LauchrConst.IM_SESSION_FILE;
		} else if(item.getType().equals(Command.RESEND)){
			ResendEntity entity = TTJSONUtil.convertJsonToCommonObj(item.getContent(), ResendEntity.class);
			if(entity != null){
				content = entity.getContent();
			} else {
				content = item.getNickName();
			}
		} else if(item.getType().equals(Command.MERGE)){
			content = getMergeMsgTitle(item.getContent());
		}else {
			content = item.getContent();
			if (!item.getType().equals(Command.TEXT)) {
				isSystemMsg = true;
			}
		}
		SessionItem sessionItem = new SessionItem();
		String uid = keyValueDBService.find(Keys.UID);
		sessionItem.setContent(content);
		sessionItem.setUserName(uid);
		sessionItem.setInfo(item.getInfo());
		sessionItem.setTime(item.getCreateDate());
		sessionItem.setModified(item.getModified());
		sessionItem.setUnread(1);
		if (item.getCmd() == MessageItem.TYPE_RECV) {
			sessionItem.setOppositeName(item.getFromLoginName());
			if(item.getNickName()!=null && !item.getNickName().equals("")){
				sessionItem.setNickName(item.getNickName());
			}else{
				MessageInfoEntity entity = TTJSONUtil.convertJsonToCommonObj(item.getInfo(), MessageInfoEntity.class);
				if(item.getType().equals(Command.ALERT)){
					// 好友通过推送 nickName为空 SessionName为NickName
					sessionItem.setNickName(entity.getSessionName());
				}else{
					sessionItem.setNickName(entity.getNickName());
				}
			}
		} else {
			sessionItem.setOppositeName(item.getToLoginName());
			sessionItem.setNickName(item.getToNickName());
		}
		boolean b = false;
		try {
			b = sessionItem.getOppositeName().contains("@ChatRoom");
		} catch (Exception e) {
		}
		if (b) {
			sessionItem.setChatRoom(1);


			// 修复bell在2015-12-08 升级之后崩溃问题
			//TODO
			String info = item.getInfo();
			String nickName = "";
			String sessionName = "";
			if(info != null){
				MessageInfoEntity entity = TTJSONUtil.convertJsonToCommonObj(info,MessageInfoEntity.class);
				sessionName = entity.getSessionName();
				nickName = entity.getNickName();

			}

			// 设置nickname
			sessionItem.setNickName(sessionName);

			String sessionContent = null;
			if (nickName != null && !nickName.equals("") && !isSystemMsg) {
				sessionContent = nickName + ":" + sessionItem.getContent();
			} else {
				sessionContent = sessionItem.getContent();
			}
			sessionItem.setContent(sessionContent);
		}
//		setAppSessionSort(sessionItem);
		//应用排序
		sessionItem.save();
	}

	public void insert(List<SessionItem> items) {
		SQLiteDatabase db = Connector.getDatabase();
		db.beginTransaction();
		for (SessionItem sessionItem : items) {
			if (!exist(sessionItem.getOppositeName())) {
				insert(sessionItem);
			}
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void insert(SessionItem item) {
		item.save();
	}

	/**
	 * 获取某个用户的未读条数
	 * 
	 * @param uid
	 * @return
	 */
	public int getUnreadCount(String uid) {
		List<SessionItem> list = DataSupport
				.select("unread")
				.where("oppositeName = '" + uid + "' and userName = ?",
						keyValueDBService.find(Keys.UID))
				.find(SessionItem.class);
		if (list != null && list.size() > 0) {
			return list.get(0).getUnread();
		}
		return 0;
	}

	/**
	 * 根据用户的uid更新session信息
	 * 
	 * @param item
	 */
	public void update(MessageItem item) {
		Log.d("session", "更新");
		String content;
		boolean isSystemMsg = false;
		if (item.getType().equals(Command.AUDIO)) {
			content = LauchrConst.IM_SESSION_AUDIO;
		} else if (item.getType().equals(Command.IMAGE)) {
			content = LauchrConst.IM_SESSION_IMAGE;
		} else if (item.getType().equals(Command.VIDEO)) {
			content = LauchrConst.IM_SESSION_VIDEO;
		} else if(item.getType().equals(Command.EVENT)){
			content = getTaskMessageContent(item);
		} else if(item.getType().equals(Command.FILE)){
			content = LauchrConst.IM_SESSION_FILE;
		} else if(item.getType().equals(Command.MERGE)){
			content = getMergeMsgTitle(item.getContent());
		} else {
			content = item.getContent();
			if (!item.getType().equals(Command.TEXT)) {
				isSystemMsg = true;
			}
		}
		SessionItem sessionItem = new SessionItem();
		String uid_db = keyValueDBService.find(Keys.UID);
		sessionItem.setContent(content);
		sessionItem.setUserName(uid_db);
		sessionItem.setInfo(item.getInfo());
		sessionItem.setTime(item.getCreateDate());
		sessionItem.setModified(item.getModified());
		sessionItem.setClientMsgId(item.getClientMsgId());
		sessionItem.setMsgId(item.getMsgId());
		String her;

		//"oppositeName = ? and userName = ?", her,
			//	uid_db);
		if (item.getCmd() == MessageItem.TYPE_RECV) {
			// 收到的消息，oppositeName为getFromLoginName
			her = item.getFromLoginName();
			boolean isApp = her.contains("@APP");
			if(isApp){
				sessionItem.setUnread(1);
			}else {

				// 判断本地是否已经屏蔽消息，屏蔽设置10000,表示不显示角标
				GroupInfo groupInfo = GroupInfoDBService.getIntance().getGroupInfo(item.getFromLoginName());
				Log.i("infos", "消息免打扰");

				// 1为免打扰状态  2为消息接受状态
				if(groupInfo!=null && groupInfo.getIsDND()==1){
					sessionItem.setRecieve(2);
				}else {
					sessionItem.setRecieve(1);
				}


				sessionItem.setUnread(getUnreadCount(her) + 1);

			}
			sessionItem.setNickName(item.getNickName());
		} else {

			// 发出的消息，oppositeName为getToLoginName
			her = item.getToLoginName();
			sessionItem.setNickName(item.getToNickName());
		}

		boolean b = her.contains("@ChatRoom");
		if (b) {
			if (item.getCmd() == MessageItem.TYPE_RECV) {

				// 修复某些特定情况下infoMap为null，崩溃问题
				String info = item.getInfo();
				String nickName = "";
				String sessionName = "";
				if(info != null){
					MessageInfoEntity entity = TTJSONUtil.convertJsonToCommonObj(info,MessageInfoEntity.class);
					sessionName = entity.getSessionName();
					nickName = entity.getNickName();

				}

				// 设置
				sessionItem.setNickName(sessionName);
				String sessionContent = null;
				if (nickName != null && !nickName.equals("") && !isSystemMsg) {
					sessionContent = nickName + ":" + sessionItem.getContent();
				} else {
					sessionContent = sessionItem.getContent();
				}
				sessionItem.setContent(sessionContent);
			} else {
				String nickName = item.getNickName();
				String sessionContent = null;
				if (nickName != null && !nickName.equals("") && !isSystemMsg) {
					String info = item.getInfo();
					if(info != null){
						MessageInfoEntity msgEntity = TTJSONUtil.convertJsonToCommonObj(info,MessageInfoEntity.class);
						if(msgEntity != null){
							if(msgEntity.getNickName() != null){
								// 判断是否是发送的语音，未来需要重新整理
								if(item.getType().equals(Command.AUDIO) || item.getType().equals(Command.IMAGE)) {
									sessionContent = msgEntity.getNickName() + ":" + content;
								}else {
									sessionContent = msgEntity.getNickName() + ":" + sessionItem.getContent();
								}

							}else{
								sessionContent = nickName + ":" + sessionItem.getContent();
							}

						}else {
							sessionContent = sessionItem.getContent();
						}
					}else {
						// 判断是否是发送的语音，未来需要重新整理
						if(item.getType().equals(Command.AUDIO) || item.getType().equals(Command.IMAGE)){
							sessionContent = nickName + ":" + content;
						}else {
							sessionContent = nickName + ":" + sessionItem.getContent();
						}
					}
				} else {
					sessionContent = sessionItem.getContent();
				}
				sessionItem.setContent(sessionContent);
			}
		}

		// 判断是存储消息类型
		List<SessionItem> l = DataSupport.where("oppositeName = ? and userName = ?",her,uid_db).find(SessionItem.class);
		SessionItem si = null;
		if(l != null && !l.isEmpty()){
			si = l.get(0);
		}

		if(si != null){
			// 判断是否是消息撤回
			if(Command.RESEND.equals(item.getType())){
				ResendEntity entity = TTJSONUtil.convertJsonToCommonObj(item.getContent(), ResendEntity.class);
				if(entity == null){
					return;
				}
				long clientMsgId = si.getClientMsgId();
				long msgId = si.getMsgId();

				if(clientMsgId == entity.getClientMsgId()){
					sessionItem.setContent(entity.getContent());
				}else {
					return;
				}
			}

			// 判断类型
			int type = si.getType();
			if(type == 2 || type == 3){
				sessionItem.setType(type);
				sessionItem.setContent(si.getContent());
//				sessionItem.setInfo(si.getInfo());
			}else {
				sessionItem.setType(1);
			}

		}

		//
		// 判断消息是否是@
		boolean bo = isSendToMe(item.getInfo());
		if(bo){

			sessionItem.setType(3);
			sessionItem.setContent(item.getContent());
			String info = item.getInfo();
			if(info != null){
				MessageInfoEntity msgEntity = TTJSONUtil.convertJsonToCommonObj(info,MessageInfoEntity.class);
				if(msgEntity != null){
					sessionItem.setContent(msgEntity.getNickName() + ":" + item.getContent());
				}
			}

		}

//		setAppSessionSort(sessionItem);
		int i = sessionItem.updateAll("oppositeName = ? and userName = ?", her,
				uid_db);
		Log.d("session", "oppositeName =" + item.getToLoginName() + "i=" + i);
	}

	/**
	 * 判断消息是否是@
	 * @param info
	 * @return
	 */
	private boolean isSendToMe(String info){
		if(info != null){
			MessageInfoEntity entity = TTJSONUtil.convertJsonToCommonObj(info,MessageInfoEntity.class);
			if(entity != null){
				// 判断AtUser是否为空
				List list = entity.getAtUsers();
				if (list != null && !list.isEmpty()) {
					// 获取当前登录用户
					boolean isMarked = false;
					String loginName = LauchrConst.header.getLoginName();
					for(int i = 0; i < list.size(); i++) {
						String st = (String) list.get(i);
						if (st.contains(loginName + "@") || st.contains("ALL@全体成员")) {
							isMarked = true;
							break;
						}
					}
					if(isMarked){
						return true;
					}
				}
			}
		}
		return false;
	}


	public void updateSessionReadCount(Sessions session){
		String oppositeName = session.getSessionName();
		int id = getId(oppositeName);
		if(id > 0){
			ContentValues cv = new ContentValues();
			cv.put("unread", session.getCount());
			if(session.getCount() == 0){
				cv.put("type", 1);
			}
			DataSupport.update(SessionItem.class, cv, id);
		}
	}
	
	private void setAppSessionSort(SessionItem session){
		String name = session.getOppositeName();
		if(name == null){
			return;
		}
		if(name.contains(Const.SHOWID_TASK)){
			session.setSort(99997);
		}else if(name.contains(Const.SHOW_SCHEDULE)){
			session.setSort(99999);
		}else if(name.contains(Const.SHOWID_APPROVE)){
			session.setSort(99998);
		}
	}
	
	/**
	 * 将读取过问session的未读信息数置零
	 * 
	 * @param uid
	 */
	public void readMessage(String uid) {
		ContentValues cv = new ContentValues();
		cv.put("unread", 0);
		DataSupport.updateAll(SessionItem.class, cv, "oppositeName = ?", uid);
	}

	/**
	 * 将读取过问session的未读信息数置零
	 */
	public void readMessage(String fromId, String toId) {
		ContentValues cv = new ContentValues();
		cv.put("unread", 0);
		DataSupport.updateAll(SessionItem.class, cv, "username =? and oppositeName = ?", fromId, toId);
	}
	
	/**
	 * 更新session表
	 * 
	 * @param item
	 */
	public void updateSession(MessageItem item) {
		String oppositeName;
		if (item.getCmd() == MessageItem.TYPE_SEND) {
			oppositeName = item.getToLoginName();
		} else {
			oppositeName = item.getFromLoginName();
		}
		Log.d("session", "oppositeName =" + oppositeName + "  myName="
				+ keyValueDBService.find(Keys.UID));
//		if(item.getType().equals(Command.EVENT)){
//			//item.setContent(getTaskMessageContent(item));
//		}else if(item.getType().equals(Command.FILE)){
//			//item.setContent(getFileName(item) + LauchrConst.IM_SESSION_FILE);
//		}
		if (exist(oppositeName)) {
			// 对话表已有对方
			update(item);
		} else {
			insert(item);
		}

	}
	
	/** 
	 * 如果是任务消息，则session内容从，MessageItem的content中获取
	 */
	private String getTaskMessageContent(MessageItem item){
		String result = null;
//		MessageEventTaskEntity eventEntity = TTJSONUtil.convertJsonToObj(item.getContent(), new TypeReference<MessageEventTaskEntity>() {});
		MessageEventEntity eventEntity = TTJSONUtil.convertJsonToCommonObj(item.getContent(),MessageEventEntity.class);
		if(eventEntity != null){
			result = eventEntity.getMsgTitle();
		}
		return result;
	}
	
	/**
	 * 文件消息，得到发送文件的用户名
	 */
	private String getFileName(MessageItem item){
		Info userInfo = JsonUtil.convertJsonToCommonObj(item.getInfo(), Info.class);
		if(userInfo == null){
			return "";
		}
		return userInfo.getNickName() + ":";
	}

	/**
	 * 清空聊天记录后的session
	 */
	public void delHistorySession(String to) {
		ContentValues cv = new ContentValues();
		cv.put("content", "");
		DataSupport.updateAll(SessionItem.class, cv, "oppositeName = ?", to);
	}
	
	public void delGroup(String groupName){
		DataSupport.deleteAll(SessionItem.class, "oppositeName = ?", groupName);
	}

	/**
	 * 获取session列表
	 * 
	 * @return
	 */
	public List<SessionItem> getList() {
		return DataSupport
				.where("userName = ?", keyValueDBService.find(Keys.UID))
				.order("time desc").find(SessionItem.class);
		        // 只根据时间排序就好了！！
//				.order("sort desc, unread desc, time desc").find(SessionItem.class);
	}
	
	public void updateNickName(String uid, String nickName){
		ContentValues cv = new ContentValues();
		cv.put("nickname", nickName);
		DataSupport.updateAll(SessionItem.class, cv, "oppositeName = ?", uid);
	}
	
	/**
	 * 获取某个Session
	 * 
	 * @param uId
	 * @return
	 */
	public SessionItem getSessionByUid(String uId, String toId) {
		List<SessionItem> list = DataSupport
				.where("userName = ? and oppositeName = ?", uId, toId)
				.find(SessionItem.class);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}


	public void updateSessionTime(int id){
		ContentValues cv = new ContentValues();
		cv.put("sessiontime",System.currentTimeMillis());
		DataSupport.update(SessionItem.class, cv, id);
	}

	/** 得到消息合并转发的title*/
	private String getMergeMsgTitle(String content){
		MergeEntity.MergeCard card = TTJSONUtil.convertJsonToCommonObj(content, MergeEntity.MergeCard.class);
		return card.getTitle();
	}
}
