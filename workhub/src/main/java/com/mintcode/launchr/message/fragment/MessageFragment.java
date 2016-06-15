package com.mintcode.launchr.message.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mintcode.CreateGroupPOJO;
import com.mintcode.chat.activity.ChatActivity;
import com.mintcode.chat.image.MutiSoundUpload;
import com.mintcode.chat.image.MutiTaskUpLoad;
import com.mintcode.chat.user.GroupInfo;
import com.mintcode.chat.user.GroupInfoDBService;
import com.mintcode.chat.util.BitmapUtil;
import com.mintcode.im.Command;
import com.mintcode.im.IMManager;
import com.mintcode.im.database.FriendDBService;
import com.mintcode.im.database.SessionDBService;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.entity.SessionItem;
import com.mintcode.im.listener.IMMessageListenerConst;
import com.mintcode.im.listener.MsgListenerManager;
import com.mintcode.im.listener.OnIMMessageListener;
import com.mintcode.im.service.PushService;
import com.mintcode.launchr.R;
import com.mintcode.launchr.activity.LoginActivity;
import com.mintcode.launchr.activity.LoginWorkHubActivity;
import com.mintcode.launchr.activity.MainActivity;
import com.mintcode.launchr.api.IMAPI;
import com.mintcode.launchr.api.IMNewApi;
import com.mintcode.launchr.app.meeting.activity.RemindActivity;
import com.mintcode.launchr.app.newTask.TaskUtil;
import com.mintcode.launchr.app.newTask.activity.ProjectListActivity;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.message.view.ChatAddScheduleWindow;
import com.mintcode.launchr.message.view.DeptWindow.OnCompleteSelected;
import com.mintcode.launchr.message.activity.ChooseMemberActivity;
import com.mintcode.launchr.message.activity.HistorySearchActivity;
import com.mintcode.launchr.message.activity.VerifyFriendMessageActivity;
import com.mintcode.launchr.message.view.MessageAddPopWindow;
import com.mintcode.launchr.message.view.MessageAddTaskPopWindow;
import com.mintcode.launchr.message.view.OpeartionItemDialog;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.TaskProjectListEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.CompanyAppUtil;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.GlideRoundTransform;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTDensityUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.TimeFormatUtil;
import com.mintcode.launchrnetwork.OnResponseListener;

/**
 * 消息聊天模块
 * 
 * @author KevinQiao
 * 
 */
public class MessageFragment extends BaseFragment implements
		OnIMMessageListener, OnItemClickListener, OnResponseListener, AdapterView.OnItemLongClickListener {
	private static final int RESULT_FROM_CHOOSE_PROJECT = 1001;
	private static final int RESULT_FROM_CHOOSE_MEMBER = 1002;
	private static final int RESULT_FROM_CHOOSE_REMIND = 1003;

	private ListView mListView;
	private SessionAdapter mAdapter;
	private LayoutInflater mInflater;
	private List<SessionItem> mSessionItems;
	private Context mContext;
	
	private ImageView mIvSearch;
	private Handler mHandler = new Handler();
	private ImageView mIvAdd;

	/** 无聊天对话 */
	RelativeLayout mRelNoDataView;



	/** 更多*/
	private MessageAddPopWindow mMessageAddPopWindow;

	/** 新建任务弹窗*/
	private MessageAddTaskPopWindow mMessageAddTaskPopWindow;
	/** 新建日程弹窗*/
	private ChatAddScheduleWindow mChatAddScheduleWindow;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_message, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mContext = getActivity();
		mInflater = getActivity().getLayoutInflater();
		mListView = (ListView) view.findViewById(R.id.lv_section);
		mIvSearch = (ImageView) view.findViewById(R.id.iv_search);
		mIvAdd = (ImageView) view.findViewById(R.id.add_group);
		mRelNoDataView = (RelativeLayout) view.findViewById(R.id.rel_no_data_show);
		HeaderParam headerParam = LauchrConst.header;
		String companyCode = headerParam.getCompanyCode();
		String text = mContext.getString(R.string.first_chat_guide2).replace("@#",companyCode);
		((TextView)view.findViewById(R.id.tv_user_companyweb)).setText(text + "");

		mAdapter = new SessionAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);
		MsgListenerManager.getInstance().setMsgListener(this);
		if (IMManager.getInstance() != null) {
			mSessionItems = IMManager.getInstance().getSession();
			setNoDataView();
		}
		/** 如果session表中不存在日程、任务、申请，则向session表中插入*/
		String[] eventSession = new String[]{Const.SHOW_SCHEDULE, Const.SHOWID_TASK, Const.SHOWID_APPROVE,Const.SHOW_FRIEND};
		for(int i=0; i < eventSession.length; i++){
			boolean exist = false;
//			String d = eventSession[i];
			for(SessionItem item : mSessionItems){
				if(item.getOppositeName().contains(eventSession[i])) {
					exist = true;
				}
			}

			// 三个应用是否存在，不存在加入会话表！取消默认置顶，sort字段可以为空
			if(false){
//			if(!exist){
				HeaderParam mHeaderParam = LauchrConst.getHeader(mContext);
				String mUserName = mHeaderParam.getLoginName();

				SessionItem session = new SessionItem();
				session.setOppositeName(eventSession[i] + "@APP");
				session.setUserName(mUserName);
				if(session.getOppositeName().contains(Const.SHOW_SCHEDULE)){
//					session.setSort(99999);
					session.setSort(0);
				}else if(session.getOppositeName().contains(Const.SHOWID_APPROVE)){
//					session.setSort(99998);
					session.setSort(0);
				}else if(session.getOppositeName().contains(Const.SHOWID_TASK)){
//					session.setSort(99997);
					session.setSort(0);
				}
				session.setTime(System.currentTimeMillis());

				SessionDBService.getInstance().insert(session);
			}


		}

		if (mSessionItems==null && IMManager.getInstance() != null) {
			mSessionItems = IMManager.getInstance().getSession();
			setNoDataView();
			mAdapter.notifyDataSetChanged();
		}

		mIvAdd.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
//						DeptWindow deptWindow = new DeptWindow(context, completeSelected);
//						deptWindow.setType(DeptWindow.TYPE_MULTI_SELECT);
//						deptWindow.showAtLocation(mListView, Gravity.CENTER, 0, 0);
//						Intent intent = new Intent(getActivity(), CreateGroupActivity.class);
//						startActivity(intent);
						showMorePopWindow();
					}
				});
		
		mIvSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),HistorySearchActivity.class));
			}
		});
	}
	
	List<String> groupUsers = new ArrayList<String>();
	List<String> groupShowId = new ArrayList<String>();
	OnCompleteSelected completeSelected = new OnCompleteSelected() {
		
		@Override
		public void CompleteSelected(List<UserDetailEntity> users) {
			String userName = AppUtil.getInstance(getActivity()).getShowId();
			String userToken = AppUtil.getInstance(getActivity()).getIMToken();
			groupShowId.add(userName);
			for (UserDetailEntity userDetailEntity : users) {
				groupUsers.add(userDetailEntity.getTrueName());
				groupShowId.add(userDetailEntity.getShowId());
			}
			IMAPI.getInstance().creatGroup(MessageFragment.this, userName,userToken, groupShowId);
		}
	};
	
	public void onResponse(Object response, String taskId, boolean rawData) {
		if(response == null){
			return;
		}
		if (taskId.equals(IMAPI.TASKID.CREATEGROUP)) {
			CreateGroupPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),CreateGroupPOJO.class);
			if (pojo.isResultSuccess()) {
				String userName = pojo.getData().getSessionName();
				Intent intent = new Intent(getActivity(),ChatActivity.class);
				intent.putExtra("groupId", userName);
				String groupName = "";
				for (int i = 0; i < groupUsers.size(); i++) {
					groupName = groupName + groupUsers.get(i)+",";
				}
				groupName.substring(0, groupName.length()-2);//-2是因为要减掉最后的,
				intent.putExtra("groupName", groupName);
				startActivity(intent);
			}
		} else if(taskId.equals(IMNewApi.TaskId.DELETE_ONE_SESSION)){
			dismissLoading();
		}
	};

	private boolean mNeedRefresh = false;
	@Override
	public void onResume() {
		super.onResume();
		Runnable r = new Runnable() {
			@Override
			public void run() {
				if (mNeedRefresh && IMManager.getInstance() != null) {
					mSessionItems = IMManager.getInstance().getSession();
					setNoDataView();
					mAdapter.notifyDataSetChanged();
					mNeedRefresh = false;
				}
			}
		};
		mHandler.postDelayed(r,500);

	}

	/**
	 * 判断有没有对话，没有对话显示图标
	 */
	private void setNoDataView(){
		if(mSessionItems != null && !mSessionItems.isEmpty()){
			mRelNoDataView.setVisibility(View.GONE);
		}else {
			mRelNoDataView.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		mNeedRefresh = true;
	}

	class SessionAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mSessionItems == null ? 0 : mSessionItems.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mSessionItems.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			SectionViewHolder holder = null;
			if (convertView == null) {
//				int id = R.layout.item_session;
				convertView = mInflater.inflate(R.layout.item_session, null);
				holder = new SectionViewHolder();
				holder.ivAvatar = (ImageView) convertView.findViewById(R.id.message_head_iv);
				holder.tvNickName = (TextView) convertView.findViewById(R.id.message_name);
				holder.tvContent = (TextView) convertView.findViewById(R.id.message_content);
				holder.tvTime = (TextView) convertView.findViewById(R.id.message_time);
				holder.tvUnreadNum = (TextView) convertView.findViewById(R.id.message_unread_num);
				holder.ivNoInform = (ImageView) convertView.findViewById(R.id.message_no_inform);
				convertView.setTag(holder);
			} else {
				holder = (SectionViewHolder) convertView.getTag();
			}
			SessionItem sessionItem = mSessionItems.get(position);
			String oppositeName = sessionItem.getOppositeName();
			String nickName = sessionItem.getNickName();
			HashMap<String, String> infoMap = null;
			String avatar = null;
			boolean isApp = oppositeName.contains("@APP");
			String name = getSessionName(oppositeName, getActivity(), nickName);
			sessionItem.setNickName(name);
			holder.tvNickName.setText(name);
			setAvatar(sessionItem, getActivity(), holder.ivAvatar, avatar);
			// 判断是否有人@我，显示高亮2
			SpannableStringBuilder build = new SpannableStringBuilder();
			if(sessionItem.getType() == 3){
				String alter = getResources().getString(R.string.sb_send_to_me);
				build.append(alter);
				build.append(sessionItem.getContent());
				ForegroundColorSpan span=new ForegroundColorSpan(getResources().getColor(R.color.red_launchr));
				build.setSpan(span, 0, alter.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			}
			// 设置显示
			if(build.length() == 0){
				holder.tvContent.setText(sessionItem.getContent());
			}else{
				holder.tvContent.setText(build);
			}
			int num = sessionItem.getUnread();
			String unreadStr = num > 99 ? "..." : String.valueOf(num);
			holder.tvUnreadNum
					.setVisibility(num > 0 ? View.VISIBLE : View.INVISIBLE);
			holder.tvUnreadNum.setText(unreadStr);
			if(isApp){
				holder.tvUnreadNum.setText("");
				holder.tvUnreadNum.setBackgroundResource(R.drawable.icon_circle_red);
			}else{
				holder.tvUnreadNum.setBackgroundResource(R.drawable.icon_unread_num);
			}
			// 判断是否是屏蔽消息
			int recive = sessionItem.getRecieve();
			if(recive == 2){
				holder.tvUnreadNum.setText("");
				holder.tvUnreadNum.setBackgroundResource(R.drawable.icon_unread_num);
			}
			// 判断是否是草稿
			int type = sessionItem.getType();
			if(type == 2){
				String s = getResources().getString(R.string.msg_drafts);
				SpannableStringBuilder sp = new SpannableStringBuilder(s + sessionItem.getDrafts());
				ForegroundColorSpan span=new ForegroundColorSpan(getResources().getColor(R.color.green_launchr));
				sp.setSpan(span, 0, 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				holder.tvContent.setText(sp);
			}
			holder.tvTime.setText(TimeFormatUtil.getSessionTime(sessionItem.getTime(), mContext));
			GroupInfo groupInfo = GroupInfoDBService.getIntance().getGroupInfo(sessionItem.getOppositeName());
			if(groupInfo!=null && groupInfo.getIsDND()==1){
				holder.ivNoInform.setVisibility(View.VISIBLE);
			}else{
				holder.ivNoInform.setVisibility(View.GONE);
			}
			return convertView;
		}

		private void setAvatar(SessionItem sessionItem, Context context,
				ImageView ivAvatar, String avatar) {
			String name = sessionItem.getOppositeName();

			if(name.contains(Const.SHOW_SCHEDULE)){
				String appId = CompanyAppUtil.getInstance(context).getAppIcon(Const.SHOW_SCHEDULE);
				HeadImageUtil.getInstance(getActivity()).setChatAppIcon(ivAvatar, appId,0, 60, 60);
			}else if(name.contains(Const.SHOWID_APPROVE)){
				String appId = CompanyAppUtil.getInstance(context).getAppIcon(Const.SHOWID_APPROVE);
				HeadImageUtil.getInstance(getActivity()).setChatAppIcon(ivAvatar, appId,0, 60, 60);
			}else if(name.contains(Const.SHOWID_TASK)){
				String appId = CompanyAppUtil.getInstance(context).getAppIcon(Const.SHOWID_TASK);
				HeadImageUtil.getInstance(getActivity()).setChatAppIcon(ivAvatar, appId, 0, 60, 60);
			}else if(name.contains(Const.SHOW_FRIEND)){
				ivAvatar.setImageResource(R.drawable.icon_friend_message);
			} else if(name.contains("@ChatRoom")){
				String mUserId = name;
				HeadImageUtil.getInstance(getActivity()).setAvatarResourceAppendUrl(ivAvatar, mUserId, 0, 60, 60);
			} else {
				String mUserId = name;
				HeadImageUtil.getInstance(getActivity()).setAvatarResourceWithUserId(ivAvatar,mUserId, 0, 60, 60);
			}
		}
		private String getSessionName(String name, Context context, String defaultName) {
			if(name.contains(Const.SHOW_SCHEDULE)){
				return context.getString(R.string.app_schedule_notify);
			}else if(name.contains(Const.SHOWID_APPROVE)){
				return context.getString(R.string.app_apply_notify);
			}else if(name.contains(Const.SHOWID_TASK)){
				return context.getString(R.string.app_task_notify);
			}else if(name.contains(Const.SHOW_FRIEND)){
				return context.getString(R.string.friend_verify);
			}
			return defaultName;
		}

		class SectionViewHolder {
			ImageView ivAvatar;
			TextView tvNickName;
			TextView tvContent;
			TextView tvTime;
			TextView tvUnreadNum;
			ImageView ivNoInform;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		SessionItem sessionItem = mSessionItems.get(position);
		String name =  sessionItem.getOppositeName();
		if(name.contains(Const.SHOW_FRIEND)){ // 好友验证页面
			Intent intent = new Intent(mContext, VerifyFriendMessageActivity.class);
			intent.putExtra("section", sessionItem);
			IMManager.getInstance().readMessageByUid(sessionItem.getUserName());
			startActivity(intent);
			SessionDBService.getInstance().updateSessionTime(sessionItem.getId());
		}else{
			Intent intent = new Intent(mContext, ChatActivity.class);
			intent.putExtra("section", sessionItem);
			IMManager.getInstance().readMessageByUid(sessionItem.getUserName());
			startActivity(intent);
			SessionDBService.getInstance().updateSessionTime(sessionItem.getId());
		}

	}


	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
		if(parent == mListView){
			// 删除某条会话
			final OpeartionItemDialog opeartionItemDialog = new OpeartionItemDialog(getActivity());
			opeartionItemDialog.show();
			Window window = opeartionItemDialog.getWindow();
			TextView mTvDeleteSession = (TextView)window.findViewById(R.id.tv_delete_this_session);
			mTvDeleteSession.setText(getString(R.string.delete_this_session));
			mTvDeleteSession.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String userName = AppUtil.getInstance(getActivity()).getShowId();
					String userToken = AppUtil.getInstance(getActivity()).getIMToken();
					SessionItem item = mSessionItems.get(position);
					IMNewApi.getInstance().deleteOneSession(MessageFragment.this, userToken, userName, item.getOppositeName());
					opeartionItemDialog.dismiss();
					showLoading();
				}
			});
		}
		return true;
	}

	@Override
	public void onMessage(List<MessageItem> messages, int arg1) {
		if(messages == null){
			return;
		}
		for (MessageItem item : messages) {
			// 只需要处理附件
			if ((Command.IMAGE).equals(item.getType())) {
				MutiTaskUpLoad.getInstance().sendMsgToDownload(item, mContext,
						null);
			} else if ((Command.AUDIO).equals(item.getType())) {
				Log.i("msg", arg1 + "AUDIO  OnMessage:" + item.toString());
				MutiSoundUpload.getInstance().sendSoundToDownload(item, mContext,
						null);
			}
		}
	}

	@Override
	public void onSession(List<SessionItem> arg0) {
		if (mSessionItems != null) {
			mSessionItems.clear();
			mSessionItems.addAll(IMManager.getInstance().getSession());
			setNoDataView();
			if(isRunning()) {
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onStatusChanged(int result, String arg1) {
		if (result == IMMessageListenerConst.SUCCESS) {
			// IM登录成功
			mSessionItems = IMManager.getInstance().getSession();
			setNoDataView();
			mAdapter.notifyDataSetChanged();
		} else if (result == IMMessageListenerConst.FAILURE) {
			// IM登录失败
			toast(mContext.getString(R.string.logout_message));
			// 其他设备登录，返回到登录界面
			loginOut();
		}
	}
	
	private void loginOut(){

		//IM登出
		LauchrConst.PUSH_SERVICE_IS_LOGIN = false;

		Intent intentSeverice = new Intent(mContext, PushService.class);
		mContext.stopService(intentSeverice);
//		context.stopService(new Intent(context, GuardService.class));

		AppUtil.getInstance(mContext).deleteFile();
		AppUtil.getInstance(mContext).saveIntValue(Const.KEY_GESTURE, 0);

		Intent intent = new Intent(mContext, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);

		Intent i ;//= new Intent(context, LoginActivity.class);

		if(LauchrConst.IS_WORKHUB){
			i	= new Intent(mContext, LoginWorkHubActivity.class);
		} else {
			i	= new Intent(mContext, LoginActivity.class);
		}
		mContext.startActivity(i);

		((Activity) mContext).setResult(Activity.RESULT_OK);
		((Activity) mContext).finish();
	}

	/** 显示更多弹窗*/
	private void showMorePopWindow(){
		mMessageAddPopWindow = new MessageAddPopWindow(getActivity(), mIvAdd);
//		mMessageAddPopWindow.showAsDropDown(mIvAdd);
		int y = TTDensityUtil.dip2px(getActivity(),7);
		mMessageAddPopWindow.showAsDropDown(mIvAdd, 2, y);
//		mMessageAddPopWindow.showAtLocation(mIvAdd,Gravity.TOP|Gravity.RIGHT,0,0);


		TextView mTvAddTask = (TextView)mMessageAddPopWindow.getContentView().findViewById(R.id.tv_new_task);
		mTvAddTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showAddTaskPopWindow();
				mMessageAddPopWindow.dismiss();
			}
		});
		TextView mTvAddSchedule = (TextView)mMessageAddPopWindow.getContentView().findViewById(R.id.tv_new_schedule);
		mTvAddSchedule.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showAddSchedulePopWIndow();
				mMessageAddPopWindow.dismiss();
			}
		});
	}

	/** 显示新建任务弹窗*/
	private void showAddTaskPopWindow(){
		mMessageAddTaskPopWindow = new MessageAddTaskPopWindow(mContext);
		mMessageAddTaskPopWindow.showAtLocation(mIvAdd, Gravity.CENTER, 0, 0);
		RelativeLayout mRelProject = (RelativeLayout)mMessageAddTaskPopWindow.getContentView().findViewById(R.id.rel_project);
		mRelProject.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ProjectListActivity.class);
				startActivityForResult(intent, RESULT_FROM_CHOOSE_PROJECT);
			}
		});
		RelativeLayout mRelMember = (RelativeLayout)mMessageAddTaskPopWindow.getContentView().findViewById(R.id.rel_enter);
		mRelMember.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ChooseMemberActivity.class);
				startActivityForResult(intent, RESULT_FROM_CHOOSE_MEMBER);
			}
		});
	}

	/** 显示新建日程弹窗*/
	private void showAddSchedulePopWIndow(){
		mChatAddScheduleWindow = new ChatAddScheduleWindow(mContext, "");
		mChatAddScheduleWindow.showAtLocation(mIvAdd, Gravity.CENTER, 0, 0);
		RelativeLayout mRelRemind = (RelativeLayout)mChatAddScheduleWindow.getContentView().findViewById(R.id.rel_enter);
		mRelRemind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, RemindActivity.class);
				startActivityForResult(intent, RESULT_FROM_CHOOSE_REMIND);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != ((BaseActivity)mContext).RESULT_OK || data==null){
			return;
		}

		switch (requestCode){
			case RESULT_FROM_CHOOSE_PROJECT:
				TaskProjectListEntity entity = (TaskProjectListEntity) data.getSerializableExtra(TaskUtil.KEY_TASK_PROJECT);
				mMessageAddTaskPopWindow.setProjectResult(entity);
			break;
			case RESULT_FROM_CHOOSE_MEMBER:
				List<String> userName = data.getStringArrayListExtra("userName");
				List<String> userId = data.getStringArrayListExtra("userId");
				mMessageAddTaskPopWindow.setMemberResult(userId, userName);
				break;
			case RESULT_FROM_CHOOSE_REMIND:
				Bundle bundle = data.getExtras();
				String result = bundle.getString(RemindActivity.REMIND_INFO);
				int remindType = bundle.getInt(RemindActivity.REMINDTYPE, 0);
				mChatAddScheduleWindow.setRemindResult(result, remindType);
				break;
		}
	}

}