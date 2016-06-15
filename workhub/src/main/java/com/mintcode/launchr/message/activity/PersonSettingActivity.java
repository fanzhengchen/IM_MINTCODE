package com.mintcode.launchr.message.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.CreateGroupPOJO;
import com.mintcode.chat.activity.ChatActivity;
import com.mintcode.chat.user.GroupInfo;
import com.mintcode.chat.user.GroupInfoDBService;
import com.mintcode.chat.user.GroupInfoPOJO;
import com.mintcode.chat.util.JsonUtil;
import com.mintcode.im.database.FriendDBService;
import com.mintcode.im.database.MessageDBService;
import com.mintcode.im.database.SessionDBService;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.IMAPI;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.api.UserApi.TaskId;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.contact.activity.FriendDetailActivity;
import com.mintcode.launchr.contact.fragment.ContactFragment;
import com.mintcode.launchr.contact.activity.PersonDetailActivity;
import com.mintcode.launchr.contact.fragment.ContactFragment.OnSelectContactListner;
import com.mintcode.launchr.message.view.DeptWindow.OnCompleteSelected;
import com.mintcode.launchr.pojo.ChatUserDetailPOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.ChatUserDetailEntity;
import com.mintcode.launchr.pojo.entity.FriendEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.pojo.response.ChatUserDetailResponse;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.widget.MyCheckBox;
import com.mintcode.launchrnetwork.OnResponseListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonSettingActivity extends BaseActivity implements
		OnResponseListener, OnCompleteSelected, OnSelectContactListner {
	@Bind(R.id.cb_dnd)
	protected MyCheckBox mcbDND;
	@Bind(R.id.tv_name)
	protected TextView mTvName;
	@Bind(R.id.iv_avatar)
	protected ImageView mIvAvatar;
	@Bind(R.id.tv_dept)
	protected TextView mTvDept;
	
	private String sessionName;
	private String userName;
	private String userToken;
	private String mGroupName;
	private GroupInfo mGroupInfo;
	private ChatUserDetailPOJO userDetail;
	
	private Context mContext;
	
    private FragmentManager mFragManger;
	
	// 通讯录
	private ContactFragment mContactFragment;

	private List<String> mNameList;

	private List<String> mUserList;

	private List<UserDetailEntity> mAssignUserList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sessionName = getIntent().getStringExtra("sessionName");
		mGroupInfo = (GroupInfo) getIntent().getSerializableExtra("groupInfo");
		userName = AppUtil.getInstance(this).getShowId();
		userToken = AppUtil.getInstance(this).getIMToken();
		mContext = getBaseContext();
		setContentView(R.layout.activity_person_setting);
		ButterKnife.bind(this);
		initData();
		UserApi.getInstance().getComanyUserInfo(this, sessionName);

	}

	private void initData(){
		mAssignUserList = new ArrayList<UserDetailEntity>();
		mNameList = new ArrayList<String>();
		mUserList = new ArrayList<String>();
		setHeaderImage(mIvAvatar);
		mContactFragment = new ContactFragment();
		mContactFragment.setOnSelectContactListner(this);
		mContactFragment.setSelectUser(ContactFragment.MULTI_SELECT_STATE, null);
		mFragManger = getSupportFragmentManager();
		mFragManger.beginTransaction().add(R.id.lin_person_create_group, mContactFragment)
				.hide(mContactFragment).commit();
		if (mGroupInfo == null) {
			IMAPI.getInstance().getGroupInfo(this, userName, userToken, sessionName);
		}else{
			mGroupName = mGroupInfo.getNickName();
			mTvName.setText(mGroupName);
			mcbDND.setChecked(mGroupInfo.getIsDND() == 1);
		}
	}
	
	/** 显示头像*/
	public void setHeaderImage(ImageView mIvAvatar){
		String mUserId = sessionName;

		HeadImageUtil.getInstance(this).setAvatarResourceWithUserId(mIvAvatar, mUserId, 2, 60, 60);
	}
	@OnClick(R.id.iv_back)
	protected  void Back(){
		onBackPressed();
	}
	@OnClick(R.id.tv_clear_history)
	protected void clearHistory(){
		MessageDBService.getInstance().delHistoryMsg(userName, sessionName);
		SessionDBService.getInstance().delHistorySession(sessionName);
		toast(getString(R.string.message_has_del));
		setResult(RESULT_OK);
	}
	@OnClick(R.id.tv_creat_group)
	protected void createGroup(){
		if (mGroupInfo != null) {
			String showId = mGroupInfo.getUserName();
			List<UserDetailEntity> entities = new ArrayList<UserDetailEntity>();
			UserDetailEntity detailEntity = new UserDetailEntity();
			entities.add(detailEntity);
			detailEntity.setShowId(showId);
			showFragmentForChoose();
		}
	}
	@OnClick(R.id.rl_person_detail)
	protected void userDetail(){
		Intent intent = null;
		String friendUrl = HeadImageUtil.getInstance(PersonSettingActivity.this).getValue(sessionName);
		FriendEntity info = FriendDBService.getInstance().getFriendInfo(sessionName);
		if(data != null){
			intent = new Intent(this, PersonDetailActivity.class);
			intent.putExtra(PersonDetailActivity.KEY_PERSONAL_ID, data.getSHOW_ID());
		}else if(info != null){ // 好友数据库查询失败
			intent = new Intent(PersonSettingActivity.this, FriendDetailActivity.class);
			intent.putExtra(FriendDetailActivity.KEY_PERSONAL_ID, info);
		}else{ //好友验证通过查询头像并组装好友数据
			info = new FriendEntity();
			info.setRelationAvatar(friendUrl);
			info.setRelationName(sessionName);
			info.setNickName(mGroupInfo.getNickName());
			intent = new Intent(PersonSettingActivity.this, FriendDetailActivity.class);
			intent.putExtra(FriendDetailActivity.KEY_PERSONAL_ID, info);
		}
		if(intent != null){
			startActivity(intent);
		}
	}

	List<String> groupUsers = new ArrayList<String>();
	private ChatUserDetailEntity data;
	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		//super.onResponse(response, taskId, rawData);
		if(response == null){
			return;
		}
		if (taskId.equals( IMAPI.TASKID.SESSION)) {
			GroupInfoPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),GroupInfoPOJO.class);
			if (pojo.isResultSuccess()) {
				pojo.getData().setMemberJson(
						JsonUtil.convertObjToJson(pojo.getData()
								.getMemberList()));
				mGroupInfo = pojo.getData();
				GroupInfoDBService.getIntance().put(mGroupInfo);
				mGroupName = pojo.getData().getNickName();
			}
		}else if(taskId.equals(TaskId.TASK_URL_GET_COMPANY_USER_INFO)){
			//获取到了个人详情
			if(response == null){
				return;
			}
			userDetail = TTJSONUtil.convertJsonToCommonObj(response.toString(), ChatUserDetailPOJO.class);
			if(userDetail == null){
				showNetWorkGetDataException();
			}else if(userDetail.isResultSuccess() == false){
				return;
			}else{
				ChatUserDetailResponse userDetailResponse = userDetail.getBody().getResponse();
				if (userDetailResponse.isIsSuccess()) {
					data = userDetailResponse.getData();
					if(data.getD_NAME().size() > 0){
						mTvDept.setText(data.getD_NAME().get(0));
					}
				}
			}
		}else if (taskId.equals( IMAPI.TASKID.CREATEGROUP)) {
			CreateGroupPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),CreateGroupPOJO.class);
			if (pojo.isResultSuccess()) {
				String userName = pojo.getData().getSessionName();
				Intent intent = new Intent(this,ChatActivity.class);
				intent.putExtra("groupId", userName);
				String groupName = "";
				for (int i = 0; i < groupUsers.size(); i++) {
					groupName = groupName + groupUsers.get(i)+",";
				}
				groupName.substring(0, groupName.length()-2);
				intent.putExtra("groupName", groupName);
				startActivity(intent);
				finish();
			}
		}
	}
	
	public void showFragmentForChoose(){
		if (mUserList != null && !mUserList.isEmpty()) {
			mFragManger.beginTransaction().show(mContactFragment).commitAllowingStateLoss();
			mContactFragment.setSelectState(ContactFragment.MULTI_SELECT_STATE,
					mUserList);
			setUnSelectUser();
		} else {
			mFragManger.beginTransaction().show(mContactFragment).commitAllowingStateLoss();
			mContactFragment.setSelectUser(ContactFragment.MULTI_SELECT_STATE,
					mAssignUserList);// (DeptFragment.EDITOR_SELECT_STATE,
										// mUserList,"");
			setUnSelectUser();
		}
	}

	/**
	 * 设置不可选择的人
	 */
	private void setUnSelectUser(){
		if(mGroupInfo != null ){
			List<UserDetailEntity> l = new ArrayList<>();
			UserDetailEntity u = new UserDetailEntity();
			u.setShowId(mGroupInfo.getUserName());
			l.add(u);

			UserDetailEntity u1 = new UserDetailEntity();
			u1.setShowId(LauchrConst.header.getLoginName());
			l.add(u1);


			mContactFragment.setCannotSelectUser(l);
		}
	}
	
	@Override
	public void onSelectContact(List<UserDetailEntity> userList) {
		mNameList.clear();
		mUserList.clear();
		if(userList!=null && userList.size()>0){
			selectRequireUser(userList);
		}
	}
	
	private void selectRequireUser(List<UserDetailEntity> userList) {
		if (userList != null) {
			mAssignUserList.clear();
			mAssignUserList.addAll(userList);
			
			String userName = AppUtil.getInstance(this).getShowId();
			String userToken = AppUtil.getInstance(this).getIMToken();
			List<String> groupShowId = new ArrayList<String>();
			groupShowId.add(userName);
			groupUsers.add(mGroupInfo.getNickName());
			groupShowId.add(mGroupInfo.getUserName());
			for (UserDetailEntity userDetailEntity : mAssignUserList) {
				groupUsers.add(userDetailEntity.getTrueName());
				groupShowId.add(userDetailEntity.getShowId());
			}
			IMAPI.getInstance().creatGroup(this, userName,userToken, groupShowId);
			showLoading();
		}
	}

	@Override
	public void onBackPressed() {
		if (mGroupInfo != null) {
			mGroupInfo.setIsDND(mcbDND.isChecked() ? 1 : 2);
			GroupInfoDBService.getIntance().update(mGroupInfo);
		}
		boolean hidden = mContactFragment.isHidden();
		if (hidden) {
			ContactFragment.mSelectState = 1;
			super.onBackPressed();
		} else {
			mFragManger.beginTransaction().hide(mContactFragment).commitAllowingStateLoss();
		}
	}

	@Override
	public void CompleteSelected(List<UserDetailEntity> users) {
		String userName = AppUtil.getInstance(this).getShowId();
		String userToken = AppUtil.getInstance(this).getIMToken();
		List<String> groupShowId = new ArrayList<String>();
		groupShowId.add(userName);
		groupUsers.add(mGroupInfo.getNickName());
		groupShowId.add(mGroupInfo.getUserName());
		for (UserDetailEntity userDetailEntity : users) {
			groupUsers.add(userDetailEntity.getTrueName());
			groupShowId.add(userDetailEntity.getShowId());
		}
		IMAPI.getInstance().creatGroup(this, userName,userToken, groupShowId);
	}
}
