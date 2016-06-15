package com.mintcode.im;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.litepal.LitePalApplication;
import org.litepal.LitePalManager;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.mintcode.im.database.KeyValueDBService;
import com.mintcode.im.database.MessageDBService;
import com.mintcode.im.database.SessionDBService;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.entity.SessionItem;
import com.mintcode.im.listener.MsgListenerManager;
import com.mintcode.im.listener.OnIMMessageListener;
import com.mintcode.im.receiver.CallbackHandler;
import com.mintcode.im.service.GuardService;
import com.mintcode.im.service.PushService;
import com.mintcode.im.util.IMConst;
import com.mintcode.im.util.JsonUtil;
import com.mintcode.im.util.Keys;
import com.mintcode.launchr.util.AppUtil;

public class IMManager {

	private static IMManager sIMManager;

	private Context context;

	private SessionDBService mSessionDBService;

	private MessageDBService mMessageDBService;

	private KeyValueDBService mKeyValueDBService;

	private String mUid;

	// 空参的初始化函数作为游客登录，不开启IM Service
	public IMManager() {
		LitePalApplication.initialize(context);
		mKeyValueDBService = KeyValueDBService.getInstance();
		mSessionDBService = SessionDBService.getInstance();
		mMessageDBService = MessageDBService.getInstance();
	}

	private IMManager(Context context, String appName, String token,
			String uid, long modified, Map<String, String> info, String ip,
			String httpIp) {
		this.context = context;
		LitePalApplication.initialize(context);
		KeyValueDBService.getNewInstance(context);
		AppUtil appUtil = AppUtil.getInstance(context);
		String userName = appUtil.getShowId();
		String companyCode = appUtil.getCompanyCode();
		if(userName != null && !userName.equals("")) {
			LitePalManager.reset();
			LitePalManager.setDbName(userName + "_" + companyCode);
		}
		mUid = uid;
		mKeyValueDBService = KeyValueDBService.getInstance();
		mKeyValueDBService.put(Keys.HTTP_IP, httpIp);
		String infoStr = JsonUtil.convertObjToJson(info);
		setLoginParams(appName, token, uid, modified, ip, infoStr);
		initNotificationParams();
		mSessionDBService = SessionDBService.getInstance();
		mMessageDBService = MessageDBService.getInstance();
		
		startPushService(context, PushService.ACTION_CONNECT);
	}

	private void startPushService(Context context, String action){
		Intent startServiceIntent = new Intent(context, PushService.class);
		if(action != null){
			startServiceIntent.setAction(action);
		}
		context.startService(startServiceIntent);

		// 启动守护进程service
		Intent intent = new Intent(context, GuardService.class);
		context.startService(intent);

	}
	private void setLoginParams(String appName, String token, String uid,
			long modified, String ip, String infoStr) {
		mKeyValueDBService.put(Keys.UID, uid);
		mKeyValueDBService.put(Keys.TOKEN, token);
		mKeyValueDBService.put(Keys.INFO, infoStr);
		mKeyValueDBService.put(Keys.APPNAME, appName);
		mKeyValueDBService.put(Keys.MODIFIED, modified);
		mKeyValueDBService.put(Keys.IP, ip);
	}

	private void initNotificationParams() {
		mKeyValueDBService.put(Keys.NOTIFICATION_TITLE, null);
		mKeyValueDBService.put(Keys.NOTIFICATION_ID, null);
		mKeyValueDBService.put(Keys.NOTIFICATION_ICON_ID, null);
		mKeyValueDBService.put(Keys.NOTIFICATION_TARGET, null);
	}

	public void setNotificationParams(Context context, String title,
			String text, int id, int iconId, Class<?> targetClass) {
		mKeyValueDBService.put(Keys.NOTIFICATION_TITLE, title);
		mKeyValueDBService.put(Keys.NOTIFICATION_ID, id);
		mKeyValueDBService.put(Keys.NOTIFICATION_ICON_ID, iconId);
		mKeyValueDBService.put(Keys.NOTIFICATION_TARGET, targetClass.getName());
	}

	/**
	 * 设置服务器时间，如果不设置，默认取设备时间
	 * 
	 * @param time
	 */
	public void setTime(long time) {
		mKeyValueDBService.put(Keys.TIME_GAP, time - System.currentTimeMillis()
				+ "");
	}

	public static IMManager getInstance() {
		return sIMManager;
	}

	public static IMManager getInstance(Context context, String appName,
			String token, String uid, long modified, Map<String, String> info,
			String ip, String httpIp) {
		sIMManager = new IMManager(context, appName, token, uid, modified,
				info, ip, httpIp);
		return sIMManager;
	}

	public void setOnIMMessageListener(OnIMMessageListener listener) {
		MsgListenerManager.getInstance().setMsgListener(listener);
	}

	public void removeOnIMMessageListener(OnIMMessageListener listener) {
		MsgListenerManager.getInstance().removeListener(listener);
	}

	/**
	 * 插入欢迎语
	 * 
	 * @param messageItem
	 */
	public void insertHelloMsg(MessageItem messageItem) {
		mMessageDBService.insert(messageItem);
		mSessionDBService.updateSession(messageItem);
		List<SessionItem> sections = mSessionDBService.getList();
		Message msg = Message.obtain();
		msg.obj = sections;
		msg.what = CallbackHandler.CALLBACK_SECTION;
		PushService.sCallbackHandler.sendMessage(msg);
	}

	/**
	 * 获得会话列表
	 * 
	 * @return
	 */
	public List<SessionItem> getSession() {
		if (mUid == null) {
			return new ArrayList<SessionItem>();
		}
		return mSessionDBService.getList();
	}

	/**
	 * 读了跟某人的所有对话。
	 * 
	 * @param uid
	 */
	public void readMessageByUid(String uid) {
		mSessionDBService.readMessage(uid);
		Intent startServiceIntent = new Intent(context, PushService.class);
		startServiceIntent.setAction(PushService.ACTION_SECTION_UPDATE);
		context.startService(startServiceIntent);
	}

	/** 更新收到的消息 一些字段 诸如 附件下载后的地址,语音消息的未读已读状态,消息的发送状态 */
	public void updateMsgState(MessageItem messageItem) {
		mMessageDBService.update(messageItem);
	}

	/**
	 * 获取最近消息内容
	 * 
	 * @param from
	 * @param to
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	public List<MessageItem> getMessagesByIndexForDesc(String from, String to,
			int beginIndex, int endIndex) {
		return mMessageDBService.getMessageByIndexForDesc(from, to, beginIndex,
				endIndex);
	}

	/**
	 * 发送文本消息
	 * 
	 * @param item
	 */
	public void sendText(MessageItem item) {
		item.setType(Command.TEXT);
		sendMessageItem(item);
	}

	/**
	 * 发送语音消息
	 * 
	 * @param item
	 */
	public void sendAudio(MessageItem item) {
		item.setType(Command.AUDIO);
		sendMessageItem(item);
	}

	/**
	 * 发送图片消息
	 * 
	 * @param item
	 */
	public void sendImage(MessageItem item) {
		item.setType(Command.IMAGE);
		sendMessageItem(item);
	}

	/**
	 * 发送消息item
	 * 
	 * @param item
	 */
	public void sendMessageItem(MessageItem item) {
		if (item.getType() == null || item.getType().equals("")) {
			item.setType(Command.TEXT);
		}
		String msgJson = JsonUtil.convertObjToJson(item);
		Intent startServiceIntent = new Intent(context, PushService.class);
		startServiceIntent.setAction(PushService.ACTION_SEND);
		startServiceIntent.putExtra(IMConst.KEY_MSG, msgJson);
		context.startService(startServiceIntent);
	}

	/**
	 * 重发消息
	 * 
	 * @param item
	 */
	public void resendMessageItem(MessageItem item) {
		//TODO 重发测试
		long clientMsgId = item.getClientMsgId();
		mMessageDBService.deletMsg(clientMsgId);
		Log.d("clientMsgId", item.getClientMsgId() + "second");
		item.setCreateDate(clientMsgId);
		//TODO 重发测试
//		item.setCreateDate(IMConst.getCurrentTime());
		Log.d("exist", "resendMsg_IM" + item.getClientMsgId());
		sendMessageItem(item);
	}

	/**
	 * 登出IM，停止心跳
	 */
	public void logout() {
		if (mUid == null) {
			return;
		}
		Intent logoutIntent = new Intent(context, PushService.class);
		logoutIntent.setAction(PushService.ACTION_LOGOUT);
		context.startService(logoutIntent);
		sIMManager = null;
	}
}
