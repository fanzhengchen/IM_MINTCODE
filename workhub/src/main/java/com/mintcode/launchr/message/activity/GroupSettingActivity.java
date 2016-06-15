package com.mintcode.launchr.message.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mintcode.AddGroupUserPOJO;
import com.mintcode.DelGroupUserPOJO;
import com.mintcode.UpdateGroupNamePOJO;
import com.mintcode.chat.user.GroupInfo;
import com.mintcode.chat.user.GroupInfoDBService;
import com.mintcode.chat.user.GroupInfoPOJO;
import com.mintcode.chat.util.JsonUtil;
import com.mintcode.im.database.MessageDBService;
import com.mintcode.im.database.SessionDBService;
import com.mintcode.launchr.R;
import com.mintcode.launchr.activity.MainActivity;
import com.mintcode.launchr.api.IMAPI;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.contact.fragment.ContactFragment;
import com.mintcode.launchr.contact.fragment.ContactFragment.OnIsActivityListener;
import com.mintcode.launchr.contact.fragment.ContactFragment.OnSelectContactListner;
import com.mintcode.launchr.message.adapter.GroupPersonAdapter;
import com.mintcode.launchr.message.adapter.GroupPersonAdapter.OnGroupShowFragmentListener;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.widget.MyCheckBox;
import com.mintcode.launchrnetwork.OnResponseListener;

public class GroupSettingActivity extends BaseActivity implements
		OnResponseListener, OnSelectContactListner, OnGroupShowFragmentListener, OnIsActivityListener{

	private GridView mGvPerson;
	private ImageView mIvBack;
	private EditText mEdtName;
	private TextView mTvExit;
	private GroupPersonAdapter mAdapter;
	private String sessionName;
	private String userName;
	private String userToken;
	private String mGroupName;
	private GroupInfo mGroupInfo;
	private MyCheckBox mcbDND;
	private TextView mTvClearHistory;
	
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
		init();
		if (mGroupInfo == null) {
			getData();
		}
		initData();
	}

	public void getData() {
		IMAPI.getInstance()
				.getGroupInfo(this, userName, userToken, sessionName);
	}
	
	private void initData(){
		mAssignUserList = new ArrayList<UserDetailEntity>();
		mNameList = new ArrayList<String>();
		mUserList = new ArrayList<String>();
	}

	private void init() {
		setContentView(R.layout.activity_group_setting);
		mTvExit = (TextView) findViewById(R.id.tv_exit);
		mIvBack = (ImageView) findViewById(R.id.iv_back);
		mEdtName = (EditText) findViewById(R.id.edt_group_name);
		mGvPerson = (GridView) findViewById(R.id.gv_person);
		mcbDND = (MyCheckBox) findViewById(R.id.cb_dnd);
		mTvClearHistory = (TextView) findViewById(R.id.tv_clear_history);
		
		if (mGroupInfo != null) {
			String memberJson = mGroupInfo.getMemberJson();
			List<GroupInfo> obj = JsonUtil.convertJsonToObj(memberJson,
					new TypeReference<List<GroupInfo>>() {
					});
			mAdapter = new GroupPersonAdapter(this, obj, sessionName);
			mEdtName.setText(mGroupInfo.getNickName());
			mGroupName = mGroupInfo.getNickName();
			mcbDND.setChecked(mGroupInfo.getIsDND() == 1);
		} else {
			mAdapter = new GroupPersonAdapter(this, null, sessionName);
		}
		mGvPerson.setAdapter(mAdapter);
		mGvPerson.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mAdapter.setCanotDel();
				return false;
			}
		});
		mIvBack.setOnClickListener(this);
		mTvExit.setOnClickListener(this);
		mTvClearHistory.setOnClickListener(this);
		
		mAdapter.setOnGroupShowFragment(this);
		
		mContactFragment = new ContactFragment();
		mContactFragment.setOnSelectContactListner(this);
		mContactFragment.setOnIsActivityListener(this);
		mContactFragment.setSelectUser(ContactFragment.SINGLE_SELECT_STATE, null);
		mFragManger = getSupportFragmentManager();
		mFragManger.beginTransaction().add(R.id.lin_group_create_group, mContactFragment)
		                              .hide(mContactFragment).commit();
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
			
			List<String> groupUsers = new ArrayList<String>();
			for (UserDetailEntity entity : mAssignUserList) {
				String show_ID = entity.getShowId();
				groupUsers.add(show_ID);
			}
            
			IMAPI.getInstance().addGroupIser(this, userToken, userName, sessionName, groupUsers);
			showLoading();
		}
	}
	
	@Override
	public void onGroupShowFragment() {
		if (mUserList != null && !mUserList.isEmpty()) {
			mFragManger.beginTransaction().show(mContactFragment).commit();
			mContactFragment.setSelectState(ContactFragment.MULTI_SELECT_STATE,
					mUserList);
			setUnSelectUser();
		} else {
			mFragManger.beginTransaction().show(mContactFragment).commit();
			mContactFragment.setSelectUser(ContactFragment.MULTI_SELECT_STATE,
					mAssignUserList);// (DeptFragment.EDITOR_SELECT_STATE,
										// mUserList,"");
			setUnSelectUser();
			if (!mNameList.isEmpty()) {
			}
		}
	}

	/**
	 * 设置不可选择的人
	 */
	private void setUnSelectUser(){
		List<GroupInfo> list = mAdapter.getList();
		if(list != null && !list.isEmpty()){
			List<UserDetailEntity> l = new ArrayList<>();
			for(int i = 0;i < list.size();i ++){
				GroupInfo g = list.get(i);
				UserDetailEntity u = new UserDetailEntity();
				u.setShowId(g.getUserName());
				l.add(u);
			}
			mContactFragment.setCannotSelectUser(l);
		}
	}
	
	@Override
	public void onIsActivity() {
		boolean hidden = mContactFragment.isHidden();
		if (hidden) {
		} else {
			mFragManger.beginTransaction().hide(mContactFragment).commit();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_back:
			onBackPressed();
			break;
		case R.id.tv_clear_history:
			MessageDBService.getInstance().delHistoryMsg(userName, sessionName);
			SessionDBService.getInstance().delHistorySession(sessionName);
			toast(getString(R.string.message_has_del));
			setResult(RESULT_OK);
			break;
		case R.id.tv_exit:
			IMAPI.getInstance().delGroupUser(this, userToken, userName,
					sessionName, userName);
			break;

		default:
			break;
		}
	}

	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		super.onResponse(response, taskId, rawData);
		dismissLoading();
		if(response == null){
			return;
		}
		if (taskId.equals(IMAPI.TASKID.SESSION)) {
			GroupInfoPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),GroupInfoPOJO.class);
			if (pojo.isResultSuccess()) {
				pojo.getData().setMemberJson(JsonUtil.convertObjToJson(pojo.getData().getMemberList()));
				mGroupInfo = pojo.getData();
				GroupInfoDBService.getIntance().put(mGroupInfo);
				List<GroupInfo> memberList = mGroupInfo.getMemberList();
				mAdapter.updateData(memberList);
				mEdtName.setText(pojo.getData().getNickName());
				mGroupName = pojo.getData().getNickName();
			}
		} else if (taskId.equals(IMAPI.TASKID.UPDATE_GROUP_NAME)) {
			UpdateGroupNamePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),UpdateGroupNamePOJO.class);
			if (pojo != null && pojo.isResultSuccess()) {
				boolean hidden = mContactFragment.isHidden();
				if (hidden) {
					ContactFragment.mSelectState = 1;
					finish();
				} else {
					mFragManger.beginTransaction().hide(mContactFragment).commitAllowingStateLoss();
				}
			}
		} else if (taskId.equals( IMAPI.TASKID.DEL_GROUP_USER)) {
			DelGroupUserPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),DelGroupUserPOJO.class);
			if (pojo.isResultSuccess()) {
				// TODO 删除
			}
			MessageDBService.getInstance().delHistoryMsg(userName, sessionName);
			SessionDBService.getInstance().delGroup(sessionName);
			startActivity(new Intent(GroupSettingActivity.this,MainActivity.class));
		}else if(taskId.equals(IMAPI.TASKID.ADD_GROUP_USER)){
			AddGroupUserPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),AddGroupUserPOJO.class);
			if (pojo.isResultSuccess()) {
				getData();
			}
		}
	}

	@Override
	public void onBackPressed() {
		String nickName = mEdtName.getText().toString().trim();
		if (mGroupInfo != null) {
			mGroupInfo.setIsDND(mcbDND.isChecked()?1:2);
			GroupInfoDBService.getIntance().update(mGroupInfo);
		}
		if (!TextUtils.equals(nickName,mGroupName)&& !TextUtils.isEmpty(nickName)) {
			IMAPI.getInstance().updateGroupName(this, userToken, userName,
					sessionName, nickName);
		}else{
			boolean hidden = mContactFragment.isHidden();
			if (hidden) {
				ContactFragment.mSelectState = 1;
				finish();
			} else {
				mFragManger.beginTransaction().hide(mContactFragment).commitAllowingStateLoss();
			}
		}
	}

}
