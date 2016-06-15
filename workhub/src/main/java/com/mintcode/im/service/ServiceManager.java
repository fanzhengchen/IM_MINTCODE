package com.mintcode.im.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.codebutler.android_websockets.WebSocketClient;
import com.codebutler.android_websockets.WebSocketClient.Listener;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mintcode.OfflineMsgPOJO;
import com.mintcode.UnReadSessionPOJO;
import com.mintcode.UnReadSessionPOJO.LastMsg;
import com.mintcode.UnReadSessionPOJO.Sessions;
import com.mintcode.chat.activity.ChatActivity;
import com.mintcode.chat.entity.ResendEntity;
import com.mintcode.chat.image.MutiSoundUpload;
import com.mintcode.chat.image.MutiTaskUpLoad;
import com.mintcode.chat.image.MutiTextUpload;
import com.mintcode.chat.user.GroupInfo;
import com.mintcode.chat.user.GroupInfoDBService;
import com.mintcode.chat.user.GroupInfoPOJO;
import com.mintcode.im.Command;
import com.mintcode.im.crypto.AESUtil;
import com.mintcode.im.database.KeyValueDBService;
import com.mintcode.im.database.MessageDBService;
import com.mintcode.im.database.MsgIdDBService;
import com.mintcode.im.database.SessionDBService;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.entity.SessionItem;
import com.mintcode.im.receiver.CallbackHandler;
import com.mintcode.im.util.IMLog;
import com.mintcode.im.util.JsonUtil;
import com.mintcode.im.util.Keys;
import com.mintcode.im.util.MessageNotifyManager;
import com.mintcode.launchr.R;
import com.mintcode.launchr.activity.MainActivity;
import com.mintcode.launchr.api.IMAPI;
import com.mintcode.launchr.api.IMAPI.TASKID;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.entity.MessageEventEntity;
import com.mintcode.launchr.pojo.entity.MessageInfoEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.AudioTipHelper;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.TimeFormatUtil;
import com.mintcode.launchrnetwork.OnResponseListener;

public class ServiceManager implements OnResponseListener {
	private static final String TAG = PushService.class.getSimpleName();

	private MessageDBService mMsgService;
	private MsgIdDBService mMsgIdDBService;
	private SessionDBService mSectionDBService;
	private KeyValueDBService mKeyValueDBService;

	private Listener mMsgListener;

	private static ServiceManager sInstance;

	/**
	 * websocket 对象，用于连接、关闭连接等操作
	 */
	private static WebSocketClient mWebSocketClient = null;



	private Context mContext;

	private int mStart = 0;
	private int mEnd = 200;

	public ServiceManager(Context context, Listener msgListener) {
		mContext = context;
		mMsgListener = msgListener;

		if (mKeyValueDBService == null) {
			mKeyValueDBService = KeyValueDBService.getInstance();
		}

		mUri = mKeyValueDBService.find(Keys.IP);
		mUid = mKeyValueDBService.find(Keys.UID);
		mInfo = mKeyValueDBService.find(Keys.INFO);
		mToken = mKeyValueDBService.find(Keys.TOKEN);
		mAppName = mKeyValueDBService.find(Keys.APPNAME);
		try {
			mMidified = Long.valueOf(mKeyValueDBService.find(Keys.MODIFIED));
		} catch (Exception e) {
			mMidified = 0;
		}
		mCurrentTime = System.currentTimeMillis();

		if (mMsgService == null) {
			mMsgService = MessageDBService.getInstance();
		}
		if (mSectionDBService == null) {
			mSectionDBService = SessionDBService.getInstance();
		}
		if (mMsgIdDBService == null) {
			mMsgIdDBService = MsgIdDBService.getInstance();
		}
	}

	public static ServiceManager getInstance(Context context, Listener msgListener) {
		if (sInstance == null) {
			sInstance = new ServiceManager(context, msgListener);
		}
		sInstance.setListener(msgListener);
		return sInstance;
	}

	public static ServiceManager getNewInstance(Context context, Listener msgListener,
			Bundle bundle) {
		sInstance = new ServiceManager(context, msgListener);
		return sInstance;
	}

	public static ServiceManager getInstance() {
		return sInstance;
	}

	public void setListener(Listener listener){
		if (mWebSocketClient != null && listener != null) {
			mWebSocketClient.setListener(listener);
		}
	}

	/**
	 * 登录
	 */
	public void login() {
		// 从数据库中取得最后一条历史消息记录的服务器端id
		long clientMsgId = mCurrentTime;
		long msgId = mMsgIdDBService.getMsgId();

		String strMsgId = AppUtil.getInstance(mContext).getValue(LauchrConst.KEY_MAX_MSGID);
		if(strMsgId != null && !strMsgId.equals("")){
			long lMsgId = Long.parseLong(strMsgId);
			msgId = lMsgId + 1;
		}

		mUid = mKeyValueDBService.find(Keys.UID);
		mInfo = mKeyValueDBService.find(Keys.INFO);
		mToken = mKeyValueDBService.find(Keys.TOKEN);
		mAppName = mKeyValueDBService.find(Keys.APPNAME);
		String type = Command.LOGIN;
		MessageItem loginItem = new MessageItem();
		loginItem.setAppName(mAppName);
		loginItem.setUserName(mUid);
		loginItem.setContent(mToken);
		loginItem.setMsgId(msgId);
		loginItem.setClientMsgId(clientMsgId);
		loginItem.setType(type);
		loginItem.setInfo(mInfo);
		loginItem.setModified(mMidified);

		String json = JsonUtil.convertObjToJson(loginItem);
		IMLog.i(TAG, "onConnect :send json=" + json);
		// websocket连接 发出登录验证
		// 发送登录请求
		send(json);
	}

	/**
	 * 保持心跳
	 */
	public void keepBeat() {
		// 从数据库中取得最后一条历史消息记录的服务器端id
		long msgId = mMsgIdDBService.getMsgId();
		String type = Command.LOGIN_KEEP;
		HashMap<String, Object> heartbeatMap = new HashMap<String, Object>();
		heartbeatMap.put("msgId", msgId);
		heartbeatMap.put("type", type);
		String heartbeatJson = JsonUtil.convertObjToJson(heartbeatMap);
		IMLog.i(TAG, "keepLogin : send heartbeatJson=" + heartbeatJson);
		send(heartbeatJson,1);
	}

	/**
	 * 发送消息
	 *
	 * @param messageJson
	 */
	public void sendMsg(String messageJson) {
		MessageItem item = MessageItem.createMessageItem(messageJson);
		long msgId = mMsgIdDBService.getMsgId() + 1;
		item.setMsgId(msgId);
		item.setCmd(MessageItem.TYPE_SEND);
		item.setSent(Command.SEND_FAILED);
		mMsgService.insert(item);
		mSectionDBService.updateSession(item);
		updateSection();
		send(messageJson);
	}

	/**
	 * 将JSON加密并发送给服务器
	 */
	public void send(String msgJson,int ping) {
		if (mWebSocketClient == null) {
			Log.e("service", "socket已经断开,无法发送消息");
			BeetTimer.getInstance().stopBeet();
			return;
		}
		Log.i(TAG, "send: msgJson=" + msgJson);
		String AES_KEY = KeyValueDBService.getInstance().find(Keys.AES_KEY);
		if (AES_KEY != null) {
			msgJson = AESUtil.EncryptIM(msgJson, AES_KEY);
		}
		mWebSocketClient.send(msgJson, ping);
		waitForResponse(false);
		Log.i("onConnect", Thread.currentThread().getName());
	}

	/**
	 * 将JSON加密并发送给服务器
	 */
	public void send(String msgJson) {
		if (mWebSocketClient == null) {
			Log.e("service", "socket已经断开,无法发送消息");
			BeetTimer.getInstance().stopBeet();
			return;
		}
		Log.i(TAG, "send: msgJson=" + msgJson);
		String AES_KEY = KeyValueDBService.getInstance().find(Keys.AES_KEY);
		if (AES_KEY != null) {
			msgJson = AESUtil.EncryptIM(msgJson, AES_KEY);
		}
		mWebSocketClient.send(msgJson);
		waitForResponse(false);
		Log.i("onConnect", Thread.currentThread().getName());
	}

	/**
	 * 更新Section表UI层
	 */
	public void updateSection() {
		List<SessionItem> sections = mSectionDBService.getList();
		Message msg = Message.obtain();
		msg.obj = sections;
		msg.what = CallbackHandler.CALLBACK_SECTION;
		PushService.sCallbackHandler.sendMessage(msg);
	}

	public void connect(){
		// 切换账号的时候，这些值需要重置
		if(mKeyValueDBService != null){
			mUri = mKeyValueDBService.find(Keys.IP);
			mUid = mKeyValueDBService.find(Keys.UID);
			mInfo = mKeyValueDBService.find(Keys.INFO);
			mToken = mKeyValueDBService.find(Keys.TOKEN);
			mAppName = mKeyValueDBService.find(Keys.APPNAME);
			try {
				mMidified = Long.valueOf(mKeyValueDBService.find(Keys.MODIFIED));
			} catch (Exception e) {
				mMidified = 0;
			}
			mCurrentTime = System.currentTimeMillis();
		}

		IMAPI.getInstance().getUnreadSession(this, mAppName, mToken, mUid, mStart, mEnd);
	}


	/**
	 * 重新连接
	 */
	public void socketConnect() {
		if (mUri == null) {
			mUri = mKeyValueDBService.find(Keys.IP);
		}
		if (mUri == null) {
			Log.e("servic", "数据库没有存入IP地址");
			return;
		}
		URI uri = URI.create(mUri);
		if (mWebSocketClient != null) {
			synchronized (mWebSocketClient) {
				mWebSocketClient.disconnect();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mWebSocketClient.setListener(null);
				mWebSocketClient = null;
				mWebSocketClient = new WebSocketClient(mContext, uri, mMsgListener, null);
				mWebSocketClient.setListener(mMsgListener);
				mWebSocketClient.connect();
				IMLog.i("msgHandler", "重连====");
				return;
			}
		}else{
			mWebSocketClient = new WebSocketClient(mContext, uri, mMsgListener, null);
		}

		mWebSocketClient.setListener(mMsgListener);
		mWebSocketClient.connect();
		//TODO:先拉取离线消息，然后再做链接
		long msgId = mMsgIdDBService.getMsgId();
//		IMAPI.getInstance().getOfflineMsg(this, mToken, mUid, msgId);
		//TODO!!!!!mWebSocketClient.connect();

		mStart = 0;
		mEnd = 200;
		Log.i("infosss", "===getUnreadSession====" + msgId);

		// 发送广播
		sendConnectBroadCast(MainActivity.KEY_LOADING_ACTION);
//		IMAPI.getInstance().getUnreadSession(this, mAppName, mToken, mUid, mStart, mEnd);
	}

	/**
	 * 发送广播
	 * @param action
	 */
	private void sendConnectBroadCast(String action){
		Intent intent = new Intent(action);
		mContext.sendBroadcast(intent);
	}

	/**
	 * 断开连接
	 */
	public void outConnect() {
		Log.d("service", "logout!!!!!!!!!!!");
		BeetTimer.getInstance().stopBeet();
		if (mWebSocketClient != null) {
			mWebSocketClient.setListener(null);
			mWebSocketClient.disconnect();
			mWebSocketClient = null;
		}

	}

	/**
	 * = =时刻准备宣布跟服务器的链接超时，超时以后重新开一个websocket
	 */
	public void waitForResponse(boolean response) {
		IMLog.d(TAG, "waitForResponse handle ==" + msgHandler);
		msgHandler.removeMessages(WS_NEED_RECONNECT);
		IMLog.i("msgHandler", "startSend==" + (System.currentTimeMillis() - startSend));
		startSend = System.currentTimeMillis();
		if (!response) {
			IMLog.i("msgHandler", "----------");
//			IMLog.d(TAG, "waitForResponse delayed ==");
			msgHandler.sendEmptyMessageDelayed(WS_NEED_RECONNECT, 10000);
		}
	}
	public static long startSend = 0;
	private static final int WS_NEED_RECONNECT = 11;
	Handler msgHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WS_NEED_RECONNECT:
				IMLog.i("msgHandler", "startEnd==" + (System.currentTimeMillis() - startSend));
				connect();
				break;

			default:
				break;
			}
		};
	};

	private String mUid;

	private String mUri;

	private String mAppName;

	private String mToken;

	private long mCurrentTime;

	private long mMidified;

	private String mInfo;

	/**
	 * 收到回执后更新数据库
	 */
	public void update(MessageItem item) {
		mMsgService.update(item);
		long msgId = item.getMsgId();
		mMsgIdDBService.put(msgId);
	}

	/**
	 * 收到消息进行处理
	 */
	public void dealMsg(MessageItem item) {
		if (item.getType().equals(Command.READ_SESSION)) {
			//假如是已读标志 则不处理
			return;
		}
		MessageItem msgitem = null;
		// 判断是否是撤回
		if(Command.RESEND.equals(item.getType())){
			msgitem = getItem(item);
		}
		// 判断本地有没有
		mMsgService.insert(item);
		long msgId = item.getMsgId();
		mMsgIdDBService.put(msgId);
		try {
			String info = item.getInfo();
			if(info != null){
				MessageInfoEntity entity = TTJSONUtil.convertJsonToCommonObj(info,MessageInfoEntity.class);
				if (!mUid.equals(item.getFromLoginName())) {
					item.setNickName(entity.getNickName());
				}
			}else{
				item.setNickName("");
			}


//			TypeReference<HashMap<String, String>> valueType = new TypeReference<HashMap<String, String>>() {};
//			HashMap<String, String> infoMap = JsonUtil.convertJsonToObj(info,valueType);
//			if (!mUid.equals(item.getFromLoginName())) {
//				item.setNickName(infoMap.get("nickName"));
//			}
		} catch (Exception e) {
			item.setNickName("");
		}


		// 根据modified字段判断要不要更新用户资料
		long modified = item.getModified();
		GroupInfo info = new GroupInfo();
		String mTo = item.getToLoginName();
		info.setUserName(item.getToLoginName());
		boolean exist = GroupInfoDBService.getIntance().exist(info);
		OnResponseListener groupInfoResponse = new OnResponseListener(){

			@Override
			public void onResponse(Object response, String taskId,
					boolean rawData) {
				if(response == null){
					return;
				}
				if (taskId.equals(TASKID.SESSION)) {
					GroupInfoPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),GroupInfoPOJO.class);
					if (pojo.isResultSuccess()) {
						pojo.getData().setMemberJson(
								JsonUtil.convertObjToJson(pojo.getData()
										.getMemberList()));
						GroupInfoDBService.getIntance().put(pojo.getData());
						String nickName = pojo.getData().getNickName();
						String userName = pojo.getData().getUserName();
						MessageItem item = mSessionTaskMap.get(userName);
						item.setToNickName(nickName);
						mSectionDBService.updateSession(item);

						notifySessionListUpdate();
					}
				}
			}

			@Override
			public boolean isDisable() {
				return false;
			}

		};


		if (!exist) {
			mSessionTaskMap.put(mTo, item);
			IMAPI.getInstance().getGroupInfo(groupInfoResponse, mUid,
					mToken, mTo);
		} else {
			long modified2 = GroupInfoDBService.getIntance()
					.getGroupInfo(mTo).getModified();
			if (modified > modified2) {
				mSessionTaskMap.put(mTo, item);
				IMAPI.getInstance().getGroupInfo(groupInfoResponse,
						mUid, mToken, mTo);
			}else{
				//没有资料需要更新
				IMLog.d("session", "更新session表:---item" + item);
				// 判断是否是撤回
				if(Command.RESEND.equals(item.getType())){
					if(msgitem != null){
						mSectionDBService.updateSession(msgitem);
					}
				}else {
					mSectionDBService.updateSession(item);
				}


				notifySessionListUpdate();
			}
		}
	}

	private MessageItem getItem(MessageItem item){
		String msg = TTJSONUtil.convertObjToJson(item);
		MessageItem msgItem = TTJSONUtil.convertJsonToCommonObj(msg, MessageItem.class);
		return msgItem;
	}

	private HashMap<String,MessageItem> mSessionTaskMap = new HashMap<String, MessageItem>();
	public void cmd(MessageItem item) {
		HashMap<String, String> infoMap = TTJSONUtil.convertJsonToObj(
				item.getContent(),
				new TypeReference<HashMap<String, String>>() {
				});
		String action = infoMap.get("name");
		String value = infoMap.get("data");
		String fromId = item.getFromLoginName();
		String toId = item.getToLoginName();
		long msgId = 0l;
		try {
			msgId = Long.parseLong(value);
		} catch (Exception e) {
		}
		if(action.equals(Command.OPEN)){
			mSectionDBService.readMessage(fromId, toId);
			mSectionDBService.readMessage(toId, fromId);
			notifySessionListUpdate();
		}else if(action.equals(Command.READ_SESSION)){
			mMsgService.updateCommonReadSession(fromId, toId, msgId);
			mMsgService.updateCommonReadSession(toId, fromId, msgId);
			notifyMessageListUpdate(item);
		}else if(action.equals(Command.MARK)){
			mMsgService.setMark(fromId, toId, msgId, 1);
			notifyMessageListUpdate(item);
		}else if(action.equals(Command.CANCEL_MARK)){
			mMsgService.setMark(fromId, toId, msgId, 0);
			notifyMessageListUpdate(item);
		}else if(action.equals(Command.REMOVE)){
			// 移除会话
			mMsgService.delHistoryMsg(item.getFromLoginName(), item.getToLoginName());
			mSectionDBService.delGroup(item.getToLoginName());
			notifySessionListUpdate();
		}
	}

	public void notifySessionListUpdate(){
		Log.i("infoss", "=====update==");
		List<SessionItem> sections = mSectionDBService.getList();
		Message msg = Message.obtain();
		msg.what = CallbackHandler.CALLBACK_SECTION;
		msg.obj = sections;
		PushService.sCallbackHandler.sendMessage(msg);
	}

	public void notifyMessageListUpdate(MessageItem msgItem){
		List<MessageItem> msgs = new ArrayList<MessageItem>();
		msgs.add(msgItem);
		Message msg = Message.obtain();
		msg.what = CallbackHandler.CALLBACK_MESSAGE;
		msg.obj = msgs;
		PushService.sCallbackHandler.sendMessage(msg);
	}

	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		if(response == null){
			return;
		}
		if (taskId.equals(TASKID.GET_OFFLINE_MSG)) {
			OfflineMsgPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),OfflineMsgPOJO.class);
			List<MessageItem> msgs = pojo.getMsg();
			mMsgService.insert(msgs);
			long msgId = pojo.getMsgId();
			mMsgIdDBService.put(msgId);
			int remain = pojo.getRemain();
//
//			if(remain > 0){
//				IMAPI.getInstance().getOfflineMsg(this, mToken, mUid, msgId);
//			}else{
//				mWebSocketClient.connect();
//			}
		}else if(taskId.equals(TASKID.UN_READ_SESSION)){
			UnReadSessionPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),UnReadSessionPOJO.class);
			final List<Sessions> sessions = pojo.getSessions();
			if(sessions == null){
				socketConnect();
				return;
			}
			for(int i=0; i<sessions.size(); i++){
				Sessions session = sessions.get(i);
				if(session != null){
					LastMsg msg = session.getLastMsg();
					if(msg != null){
						if(i == 0){
							//未读消息的第一条消息如果数量>0,需要发出通知提醒
							if(session.getCount() > 0){
								if(msg.getClientMsgId() > mMsgIdDBService.getMsgId()){
									// 判断是否设置消息免打扰，设置了则不发通知
									GroupInfo groupInfo = GroupInfoDBService.getIntance().getGroupInfo(msg.getFrom());
									Log.i("infos", "消息免打扰");
									// 判断是否推送通知2
									// 1为免打扰状态  2为消息接受状态
									if(groupInfo!=null && groupInfo.getIsDND()==1){
										String info = msg.getInfo();
										if(info != null) {
											MessageInfoEntity entity = TTJSONUtil.convertJsonToCommonObj(info, MessageInfoEntity.class);

											if (entity != null) {
												List list = entity.getAtUsers();
												if (list != null && !list.isEmpty()) {
													sendNotice(msg);

												}
											}
										}
										return;
									}else{
										sendNotice(msg);
									}
								}
							}
						}
						updateSessionByLastMsg(msg);
						updateSessionReadCount(session);
					}
				}
			}
			notifySessionListUpdate();
			long msgId = mMsgIdDBService.getMsgId();
//			IMAPI.getInstance().getOfflineMsg(this, mToken, mUid, msgId);
			// 保存最大的msgId
			AppUtil util = AppUtil.getInstance(mContext);
			util.saveValue(LauchrConst.KEY_MAX_MSGID, pojo.getMsgId() + "");
			sendConnectBroadCast(MainActivity.KEY_CONNECT_ACTION);
			socketConnect();
		}

	}

	public void updateSessionReadCount(Sessions session){
		mSectionDBService.updateSessionReadCount(session);
	}

	public void updateSessionByLastMsg(LastMsg msg){
		MessageItem item = new MessageItem();
		item.setClientMsgId(msg.getClientMsgId());
		if(msg.getType().equals(Command.RESEND)){
			ResendEntity entity = TTJSONUtil.convertJsonToCommonObj(msg.getContent(), ResendEntity.class);
			item.setMsgId(entity.getMsgId());
//			item.setContent(entity.getContent());
			item.setContent(msg.getContent());      // 在更新数据库的时候替换content
		}else{
			item.setMsgId(msg.getMsgId());
			item.setContent(msg.getContent());
		}
		item.setCreateDate(msg.getCreateDate());
		item.setFromLoginName(msg.getFrom());
		item.setInfo(msg.getInfo());
		item.setModified(msg.getModified());
//		item.setMsgId(msg.getMsgId());
		item.setToLoginName(msg.getTo());
		item.setType(msg.getType());
		//TODO
		String to = null;
		if (mUid.equals(item.getFromLoginName())) {
			item.setCmd(MessageItem.TYPE_SEND);
			to = item.getToLoginName();
		}else{
			item.setCmd(MessageItem.TYPE_RECV);
			to = item.getFromLoginName();
		}
		GroupInfo info = new GroupInfo();
		info.setUserName(to);
		boolean exist = GroupInfoDBService.getIntance().exist(info);
		if(exist){
			info = GroupInfoDBService.getIntance().getGroupInfo(to);
			item.setNickName(info.getNickName());
		}else{
			IMAPI.getInstance().getGroupInfo(groupInfoResponse, mUid, mToken, to);
		}
		mSectionDBService.updateSession(item);
	}

	OnResponseListener groupInfoResponse = new OnResponseListener() {

		@Override
		public void onResponse(Object response, String taskId, boolean rawData) {
			if (taskId.equals(TASKID.SESSION)) {
				GroupInfoPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),GroupInfoPOJO.class);
				if (pojo.isResultSuccess()) {
					pojo.getData().setMemberJson(
							JsonUtil.convertObjToJson(pojo.getData()
									.getMemberList()));
					GroupInfoDBService.getIntance().put(pojo.getData());
					mSectionDBService.updateNickName(pojo.getData().getUserName(), pojo.getData().getNickName());
				}
			}
		}

		@Override
		public boolean isDisable() {
			return false;
		}
	};
	@Override
	public boolean isDisable() {
		return false;
	}

	/** 收到回执后，移除消息队列顶部的任务*/
	public void removeSendMsgTask(MessageItem item){
		MutiTextUpload.getInstance().removeFirstUpload(item);
		MutiTaskUpLoad.getInstance().stopFirstUpload(item, false);
		MutiSoundUpload.getInstance().stopFirstUpload(item);
	}


	/**
	 * 发送通知
	 * @param item
	 */
	private void sendNotice(LastMsg item){
		// 发送通知 ,判断本地是否存在

		// 注册通知栏推送
		try {
			KeyValueDBService keyValueDBService = KeyValueDBService
					.getInstance();

			//标题
			String title = keyValueDBService.find(Keys.NOTIFICATION_TITLE);
			if (title == null) {
				if(LauchrConst.IS_WORKHUB){
					title = mContext.getString(R.string.app_name_workhub);
				} else {
					title = mContext.getString(R.string.app_name);
				}

			}

			//通知id
			int id = 0;
			try {
				id = Integer.valueOf(keyValueDBService
						.find(Keys.NOTIFICATION_ID));
			} catch (Exception e) {
			}
			if(id == 0){
				id = 0x1ac2;
			}

			//通知的图标
			int icon_id = 0;
			try {
				icon_id = Integer.valueOf(keyValueDBService
						.find(Keys.NOTIFICATION_ICON_ID));
			} catch (Exception e) {
			}
			if(icon_id == 0){

			}
			if(LauchrConst.IS_WORKHUB){
				icon_id = R.drawable.icon_workhub;
			} else {
				icon_id = R.drawable.icon_launchr;
			}

			//点击的目标类
			String className = keyValueDBService.find(Keys.NOTIFICATION_TARGET);
			Class<?> targetClass = null;
			if(className == null || className.equals("")){
				targetClass = MainActivity.class;
			}else{
				targetClass = Class.forName(className);
			}

			//通知的正文
			String content;
			if (item.getType().equals(Command.AUDIO)) {
				content = LauchrConst.IM_SESSION_AUDIO;
			} else if (item.getType().equals(Command.IMAGE)) {
				content = LauchrConst.IM_SESSION_IMAGE;
			} else if (item.getType().equals(Command.VIDEO)) {
				content = LauchrConst.IM_SESSION_VIDEO;
			} else if(item.getType().equals(Command.EVENT)) {
				content = "";
			} else {
				content = item.getContent();
			}

			MessageInfoEntity entity = TTJSONUtil.convertJsonToCommonObj(item.getInfo(), MessageInfoEntity.class);
			String nickName = entity.getNickName();
			String text = "";
			if(nickName != null){
				text = nickName + ":" + content;
			}

			// 判断是不是应用消息
			if(item.getType().equals("Event")){
				MessageEventEntity eventEntity = TTJSONUtil.convertJsonToObj(item.getContent(), new TypeReference<MessageEventEntity>() {
				});
				if(eventEntity != null){
					// 判断是否是提醒
					if(eventEntity.getMsgTransType().equals("remindTask") || eventEntity.getMsgTransType().equals("remindSchedule")){
						String msgContent = "";
						String t = eventEntity.getMsgTitle();
						String time = eventEntity.getMsgRemark();
						if(time == null){
							time = "";
						}else {
							time = TimeFormatUtil.formatNowTime(Long.parseLong(time));
						}
						msgContent = mContext.getString(R.string.im_task_remind);
						msgContent = msgContent.replace("^@", t);
						msgContent = msgContent.replace("^#", time);
						text = "";
						text = nickName + ":" + msgContent;
					}else{
						// 判断是否是任务，日程
						text = "";
						text = nickName + ":" + eventEntity.getMsgTitle();
					}
				}

			}


			// 判断当前收到的消息是否是当前聊天的对象
			boolean isCurrent = false;
			String currentTo = ChatActivity.mTo;
			if(!currentTo.equals("")){
				String from = item.getFrom();
				if(from.equals(currentTo)){
					isCurrent = true;
				}else if(currentTo.contains("@ChatRoom")){
					isCurrent = true;
				}
				if(item.getType().equals(Command.RESEND)){
					if(item.getTo().equals(currentTo)){
						isCurrent = true;
					}
				}
			}
			if (title != null && id != 0 && icon_id != 0 && targetClass != null) {
				MessageNotifyManager.showMessageNotify(mContext, isCurrent,
						title, text, id, icon_id, targetClass);

				AudioTipHelper.openRemind(mContext);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
