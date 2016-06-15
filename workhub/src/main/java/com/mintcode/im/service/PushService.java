package com.mintcode.im.service;

import java.util.ArrayList;
import java.util.List;

import org.litepal.LitePalApplication;
import org.litepal.LitePalManager;
import org.litepal.crud.DataSupport;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.codebutler.android_websockets.WebSocketClient.Listener;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mintcode.chat.activity.ChatActivity;
import com.mintcode.chat.entity.MergeEntity;
import com.mintcode.chat.entity.ResendEntity;
import com.mintcode.chat.user.GroupInfo;
import com.mintcode.chat.user.GroupInfoDBService;
import com.mintcode.im.Command;
import com.mintcode.im.aidl.AIDLGuardServiceInterface;
import com.mintcode.im.crypto.AESUtil;
import com.mintcode.im.database.KeyValueDBService;
import com.mintcode.im.database.MessageDBService;
import com.mintcode.im.database.SessionDBService;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.entity.SessionItem;
import com.mintcode.im.listener.IMMessageListenerConst;
import com.mintcode.im.receiver.CallbackHandler;
import com.mintcode.im.receiver.NetState;
import com.mintcode.im.receiver.NetworkConnect;
import com.mintcode.im.util.IMConst;
import com.mintcode.im.util.IMLog;
import com.mintcode.im.util.Keys;
import com.mintcode.im.util.MessageNotifyManager;
import com.mintcode.launchr.R;
import com.mintcode.launchr.activity.MainActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.entity.MessageEventEntity;
import com.mintcode.launchr.pojo.entity.MessageInfoEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.AudioTipHelper;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.TimeFormatUtil;

public class PushService extends Service {
	public static final String GUARDSERVICE_NAME = "com.mintcode.im.service.GuardService";
	private NetState net;
	private NetworkConnect mReceiver;

	public static Context sContext;
	private ServiceManager mServiceManager;
	public static CallbackHandler sCallbackHandler;
	private static final String TAG = PushService.class.getSimpleName();

	public static final String ACTION_SEND = "action_send";
	public static final String ACTION_LOGOUT = "action_logout";
	public static final String ACTION_CONNECT = "action_connect";
	public static final String ACTION_SECTION_UPDATE = "action_section_update";
	public static final String ACTION_START_FOREGROUND = "action_start_foreground";
	public static final String ACTION_STOP_FOREGROUND = "action_stop_foreground";

	public AIDLGuardServiceInterface.Stub mAIDLGuardServiceInterface = new AIDLGuardServiceInterface.Stub() {

		@Override
		public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

		}

		@Override
		public void startService() throws RemoteException {

		}

		@Override
		public void keepBeet() throws RemoteException {

		}

		@Override
		public void stopService() throws RemoteException {
			Intent intent = new Intent(getApplicationContext(),GuardService.class);
			intent.setAction(GuardService.ACTION_STOP);
			getApplicationContext().startService(intent);
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("service", "onCreate");
		sContext = this;
		LitePalApplication.initialize(this);

		AppUtil appUtil = AppUtil.getInstance(this);
		String userName = appUtil.getShowId();
		String companyCode = appUtil.getCompanyCode();
		if(userName != null && !userName.equals("")) {
			LitePalManager.reset();
			Log.i("LitePalManager", "===pushserveice===");
			LitePalManager.setDbName(userName + "_" + companyCode);
		}
		net = new NetState();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(net, filter);
		net.onReceive(this, null);

		mReceiver = new NetworkConnect(mServiceManager);
		IntentFilter intentFilter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, intentFilter);
		
		if (sCallbackHandler == null) {
			sCallbackHandler = new CallbackHandler();
		}
		mServiceManager = ServiceManager.getNewInstance(this, msgListener, null);
		BeetTimer.getInstance();
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("service", "onStartCommand");
		if (intent == null) {
			Log.d("service", "intent == null =======>restart Service");
			BeetTimer.getInstance().stopBeet();
			mServiceManager.connect();
			return Service.START_STICKY;
		}
		String action = intent.getAction();
		Log.d("service", "action = " + action);
		if (ACTION_SECTION_UPDATE.equals(action)) {
			mServiceManager.updateSection();
		} else if (ACTION_LOGOUT.equals(action)) {
			mServiceManager.outConnect();
		} else if (ACTION_CONNECT.equals(action)) {
			mServiceManager.connect();
		} else if (ACTION_SEND.equals(action)) {
			mServiceManager.sendMsg(intent.getStringExtra(IMConst.KEY_MSG));
		}else  if(ACTION_START_FOREGROUND.equals(action)){

			String title = "";
			String name = "";
			int icon;
			 if(LauchrConst.IS_WORKHUB){
				title = "切换到后台运行";
				name = getString(R.string.app_name_workhub);
				icon = R.drawable.icon_workhub;
			} else {
				title = "切换到后台运行";
				name = getString(R.string.app_name);
				icon = R.drawable.icon_launchr;
			}
			Intent inte = new Intent(this,MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(this,0,inte,0);
//			Notification notification = new Notification(icon,name + title,System.currentTimeMillis());
			Notification notification = new Notification.Builder(sContext)
					.setContentTitle(name)
					.setContentText(name + title)
					.setSmallIcon(icon)
					.setContentIntent(pendingIntent)
					.build();
//			notification.setLatestEventInfo(this, name,title,pendingIntent);
			startForeground(10000, notification);

		}else if(ACTION_STOP_FOREGROUND.equals(action)){
			stopForeground(true);
		}else if(action.equals("testserverice")){
			mServiceManager.keepBeat();
			Log.i("GuardThead", "---keepBeat---");
			if(msgListener == null){
				Log.i("GuardThead", "---msgListener---");
			}
		}


//		startForeground();
		return Service.START_STICKY;
	}



	Listener msgListener = new Listener() {

		@Override
		public void onMessage(byte[] data) {
			Log.i(TAG, "ThreadName:" + Thread.currentThread().getName() + " "
					+ "onMessage(byte[]) " + new String(data));
			Log.i("infos", "Listener byte[] : " + new String(data));
		}

		@Override
		public void onMessage(String message) {
			Log.i("msg", "onMessage :---" + message);
			String AES_KEY = KeyValueDBService.getInstance().find(Keys.AES_KEY);
			if (AES_KEY != null) {
				message = AESUtil.DecryptIM(message, AES_KEY);
			}
			IMLog.d("msg", "onMessage Decode:---" + message);
			MessageItem item = MessageItem.createMessageItem(message);
			String type = item.getType();

			// 记得放开
			mServiceManager.waitForResponse(true);
			IMLog.d("msg-fsfdsfdsfd", "type:---" + type);
			if (type.equals(Command.LOGIN_SUCCESS)) {
				// 登录成功！
				Intent intent = new Intent(MainActivity.KEY_CONNECT_SUCCESS_ACTION);
				sendBroadcast(intent);

				notifyStatusChanged(IMMessageListenerConst.SUCCESS, null);
				// 开启心跳包监听，每隔一段时间发送一条请求
				BeetTimer.getInstance().startBeet();
			} else if (type.equals(Command.LOGIN_OUT)) {
				LauchrConst.PUSH_SERVICE_IS_LOGIN = false;
				// 登录失败！或者已经被登出
				notifyStatusChanged(IMMessageListenerConst.FAILURE, item.getContent());
				// 关闭心跳
				BeetTimer.getInstance().stopBeet();
				try {
					mAIDLGuardServiceInterface.stopService();
				}catch (Exception e){
					e.printStackTrace();
				}
				stopSelf();
			} else if (type.equals(Command.REV)) {
				// 消息收到回执
				item.setSent(Command.SEND_SUCCESS);
				item.setCmd(MessageItem.TYPE_SEND);
				mServiceManager.update(item);
				nofityMessageReceived(item);
				mServiceManager.removeSendMsgTask(item);
			} else if(type.equals(Command.CMD)){
				mServiceManager.cmd(item);
			}
			else if (isNormalMessage(type)) {
				// 接收普通消息类型
				dealMessage(item);
				AppUtil util = AppUtil.getInstance(getApplicationContext());
				util.saveValue(LauchrConst.KEY_MAX_MSGID, item.getMsgId() + "");
			}
		}

		private boolean isNormalMessage(String type) {
			return type.equals(Command.TEXT) || type.equals(Command.AUDIO)
					|| type.equals(Command.IMAGE) || type.equals(Command.ALERT)
					|| type.equals(Command.VIDEO) || type.equals(Command.READ_SESSION)
					|| type.equals(Command.EVENT) || type.equals(Command.FILE)
					|| type.equals(Command.RESEND) || type.equals(Command.MERGE);
		}

		@Override
		public void onError(Exception error) {
			Log.e(TAG, "ThreadName:" + Thread.currentThread().getName() + " "
					+ "onError " + error);
			IMLog.i("msgHandler", "onError----------" + error.getMessage());
			error.printStackTrace();
		}

		@Override
		public void onDisconnect(int code, String reason) {
			Log.w(TAG, "ThreadName:" + Thread.currentThread().getName() + " "
					+ "onDisConnect  code=" + code + " reason=" + reason);

			IMLog.i("msgHandler", "onDisconnect----------" + "onDisConnect  code=" + code + " reason=" + reason);
			BeetTimer.getInstance().stopBeet();
		}

		@Override
		public void onConnect() {
			mServiceManager.login();
		}
	};

	private void notifyStatusChanged(int statusResult, String message) {
		Message msg = Message.obtain();
		msg.what = CallbackHandler.CALLBACK_STATUS;
		msg.arg1 = statusResult;
		msg.obj = message;
		Log.d(TAG, "notifyStatusChanged : sCallbackHandler=" + sCallbackHandler);
		Log.d(TAG, "notifyStatusChanged : msg=" + msg);
		sCallbackHandler.sendMessage(msg);

	}

	private void nofityMessageReceived(MessageItem msgItem) {
		List<MessageItem> msgs = new ArrayList<MessageItem>();
		msgs.add(msgItem);

		Message msg = Message.obtain();
		msg.what = CallbackHandler.CALLBACK_MESSAGE;
		msg.obj = msgs;
		sCallbackHandler.sendMessage(msg);
	}

	private void dealMessage(MessageItem item) {
		// 判断重复通知问题
		boolean e  = false;//MessageDBService.getInstance().existMessageId(item);
		boolean exit = MessageDBService.getInstance().exist(item);
		item.setCmd(MessageItem.TYPE_RECV);
		String uid = KeyValueDBService.getInstance().find(Keys.UID);
		IMLog.d("msg", "接收普通消息类型:---uid" + uid);
		if (uid.equals(item.getFromLoginName())) {
			item.setCmd(MessageItem.TYPE_SEND);
		}
		item.setSent(Command.SEND_SUCCESS);
		mServiceManager.dealMsg(item);
		nofityMessageReceived(item);
		Log.i("infos", "content: " + item.getContent());
		// 判断是否设置消息免打扰，设置了则不发通知
		GroupInfo groupInfo = GroupInfoDBService.getIntance().getGroupInfo(item.getFromLoginName());
		Log.i("infos", "消息免打扰");

		// 判断是否推送通知2
//		boolean s = getLastMsgFromSession(item);

		// 1为免打扰状态  2为消息接受状态
		if(groupInfo!=null && groupInfo.getIsDND()==1){
			String info = item.getInfo();
			if(info != null) {
				MessageInfoEntity entity = TTJSONUtil.convertJsonToCommonObj(info, MessageInfoEntity.class);
				if (entity != null) {
					List list = entity.getAtUsers();
					if (list != null && !list.isEmpty()) {
//						item.setIsMarkPoint(3);
//						mServiceManager.update(item);
						if(!e){
							sendNotice(item);
						}
					}
				}
			}
			return;
		}else{
			if(!e ){
				sendNotice(item);
			}

		}


	}

	private boolean getLastMsgFromSession(MessageItem item){
		boolean b = false;
		if(Command.RESEND.equals(item.getType())){
			SessionDBService session = SessionDBService.getInstance();
			String oppositeName;
			if (item.getCmd() == MessageItem.TYPE_SEND) {
				oppositeName = item.getToLoginName();
			} else {
				oppositeName = item.getFromLoginName();
			}

			String loginName = LauchrConst.header.getLoginName();
			List<SessionItem> sessionItems = DataSupport.where(
					"oppositeName = ? and userName = ?", oppositeName,
					loginName).find(SessionItem.class);
			b = sessionItems.size() == 0 ? false : true;

			if(b){
				SessionItem s = sessionItems.get(0);
				if(s.getMsgId() == s.getClientMsgId()){
					ResendEntity entity1 = TTJSONUtil.convertJsonToCommonObj(item.getContent(), ResendEntity.class);
					ResendEntity entity2 = TTJSONUtil.convertJsonToCommonObj(s.getContent(), ResendEntity.class);

					long clientMsgId = entity1.getClientMsgId();
					if(clientMsgId == entity2.getClientMsgId()){
						b = false;
					}else {
						b = true;
					}

				}else {
					b = false;
				}
			}
		}
		return b;
	}

	/**
	 * 发送通知
	 * @param item
	 */
	private void sendNotice(MessageItem item){
		// 发送通知 ,判断本地是否存在

		// 自己给自己发消息，不出现通知
		String userId = KeyValueDBService.getInstance().find(Keys.UID);
		if(item.getFromLoginName().equals(userId)){
			return;
		}

		// 注册通知栏推送
		try {
			KeyValueDBService keyValueDBService = KeyValueDBService
					.getInstance();

			//标题
			String title = keyValueDBService.find(Keys.NOTIFICATION_TITLE);
			if (title == null) {
				if(LauchrConst.IS_WORKHUB){
					title = getString(R.string.app_name_workhub);
				} else {
					title = getString(R.string.app_name);
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
			} else if(item.getType().equals(Command.FILE)){
				content = LauchrConst.IM_SESSION_FILE;
			} else if(item.getType().equals(Command.EVENT)) {
				content = "";
			} else if(item.getType().equals(Command.MERGE)){
				MergeEntity.MergeCard card = TTJSONUtil.convertJsonToCommonObj(item.getContent(), MergeEntity.MergeCard.class);
				content = card.getTitle();
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
						msgContent = getString(R.string.im_task_remind);
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
				// 好友验证推送
			}else if(item.getFromLoginName().contains(com.mintcode.launchr.util.Const.SHOW_FRIEND)){
				text = item.getContent();
			}

			if (entity != null) {
				List list = entity.getAtUsers();
				if (list != null && !list.isEmpty()) {
					boolean isMarked = false;
					String userName = LauchrConst.header.getLoginName();
					for(int i = 0; i < list.size(); i++){
						String st = (String)list.get(i);
						if(st.contains(userName + "@") || st.contains("ALL@全体成员")){
							isMarked = true;
							break;
						}
					}
					if(isMarked){
						item.setIsMarkPoint(3);
						mServiceManager.update(item);
					}

				}
			}

			// 判断当前收到的消息是否是当前聊天的对象
			boolean isCurrent = false;
			String currentTo = ChatActivity.mTo;
			if(!currentTo.equals("")){
				String from = item.getFromLoginName();
				if(from.equals(currentTo)){
					isCurrent = true;
				}else if(currentTo.contains("@ChatRoom")){
					isCurrent = true;
				}
				if(item.getType().equals(Command.RESEND)){
					if(item.getToLoginName().equals(currentTo)){
						isCurrent = true;
					}
				}
			}
			if (title != null && id != 0 && icon_id != 0 && targetClass != null) {
				Log.i("textss", text + "===" + content);
				MessageNotifyManager.showMessageNotify(getApplicationContext(), isCurrent,
						title, text, id, icon_id, targetClass);

				AudioTipHelper.openRemind(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}





	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopForeground(true);
		unregisterReceiver(mReceiver);
		unregisterReceiver(net);
	};


}
