package com.mintcode.launchr.message.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mintcode.CreateGroupPOJO;
import com.mintcode.chat.activity.ChatActivity;
import com.mintcode.chat.entity.User;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.IMAPI;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.contact.fragment.ContactFragment;
import com.mintcode.launchr.contact.fragment.ContactFragment.OnSelectContactListner;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchrnetwork.OnResponseListener;

public class CreateGroupActivity extends BaseActivity implements OnSelectContactListner, OnResponseListener {
    private FragmentManager mFragManger;
	
	// 通讯录
	private ContactFragment mContactFragment;

	private List<String> mNameList;

	private List<String> mUserList;

	private List<UserDetailEntity> mAssignUserList;
	
	private List<String> groupUsers = new ArrayList<String>();
	private List<String> groupShowId = new ArrayList<String>();
	
	private boolean isShowLoading = false;
	private boolean isOk = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);
		
		initData();
		
		initView();
	}
	

	private void initData(){
		mAssignUserList = new ArrayList<UserDetailEntity>();
		mNameList = new ArrayList<String>();
		mUserList = new ArrayList<String>();
	}
	
	public void initView(){
		mContactFragment = new ContactFragment();
		mContactFragment.setOnSelectContactListner(this);
		mContactFragment.setSelectUser(ContactFragment.NO_STATE, null);
		mFragManger = getSupportFragmentManager();
		mFragManger.beginTransaction().add(R.id.actvity_create_group, mContactFragment)
		                              .hide(mContactFragment).commit();
		doCreateGroup();
	}
	
	public void onSelectContact(List<UserDetailEntity> userList) {
		mNameList.clear();
		mUserList.clear();
		if(userList!=null && userList.size() > 0){
			selectRequireUser(userList);
		}
	};
	
	private void selectRequireUser(List<UserDetailEntity> userList) {
		if (userList != null) {
			mAssignUserList.clear();
			mAssignUserList.addAll(userList);
			
			String userName = AppUtil.getInstance(getBaseContext()).getShowId();
			String userToken = AppUtil.getInstance(getBaseContext()).getIMToken();
			groupShowId.add(userName);
			for (UserDetailEntity userDetailEntity : mAssignUserList) {
				groupUsers.add(userDetailEntity.getTrueName());
				groupShowId.add(userDetailEntity.getShowId());
			}
			if(groupShowId.size() == 2){
				Intent intent = new Intent(CreateGroupActivity.this, ChatActivity.class);
				User user = new User(groupUsers.get(0), groupShowId.get(1));
				intent.putExtra("user", user);
				finish();
				startActivity(intent);
			}else{
				IMAPI.getInstance().creatGroup(this, userName,userToken, groupShowId);
			}
			
			showLoading();
			isShowLoading = true;
			isOk = true;
		}
	}
	
	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		if(response == null){
			return;
		}
		if (taskId.equals( IMAPI.TASKID.CREATEGROUP)) {
			dismissLoading();
			CreateGroupPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),CreateGroupPOJO.class);
			if (pojo.isResultSuccess()) {

				final String userName = pojo.getData().getSessionName();
				getChatRoomImage(userName);
				Runnable r = new Runnable() {
					@Override
					public void run() {
						startChatActivity(userName);
					}
				};

				Handler handler = new Handler();
				handler.postDelayed(r,100);

			}else {
				String message = pojo.getMessage();
				if(message == null){
					message = "error ";
				}
				toast(message);
				finish();
			}

//			finish();
		}
	}

	private void startChatActivity(String sessionId){
		Intent intent = new Intent(getBaseContext(),ChatActivity.class);
		intent.putExtra("groupId", sessionId);
		String groupName = "";
		for (int i = 0; i < groupUsers.size(); i++) {
			groupName = groupName + groupUsers.get(i)+",";
		}
		groupName.substring(0, groupName.length() - 2);
		intent.putExtra("groupName", groupName);

		ContactFragment.mSelectState = 1;
		startActivity(intent);
		finish();
	}

	private void getChatRoomImage(final String sessionId){

		if(groupShowId != null){
			String companyCode = LauchrConst.header.getCompanyCode();

			StringBuilder b = new StringBuilder();
			for(int i = 0; i < groupShowId.size(); i++){
				if(i < 9){
					String str = groupShowId.get(i);
					b.append("&members=").append(str);
				}else {
					break;
				}
			}
			StringBuilder builder = new StringBuilder();
			builder.append(LauchrConst.ATTACH_PATH + "/Base-Module/Annex/ChatRoomAvatar?width").append("=")
					.append(60).append("&height=")
					.append(60).append("&companyCode=")
					.append(companyCode).append("&chatRoomID=")
					.append(sessionId)
				    .append(b.toString());


//			HeadImageUtil.getInstance(getApplicationContext()).setImageResuces();
			ImageView iv = (ImageView) findViewById(R.id.iv);
			Glide.with(getApplicationContext()).load(builder.toString()).into(iv);
		}

	}

	public void doCreateGroup(){
		if (mUserList != null && !mUserList.isEmpty()) {
			mFragManger.beginTransaction().show(mContactFragment).commit();
			mContactFragment.setSelectState(ContactFragment.MULTI_SELECT_STATE,
					mUserList);
		} else {
			mFragManger.beginTransaction().show(mContactFragment).commit();
			mContactFragment.setSelectUser(ContactFragment.MULTI_SELECT_STATE,
					mAssignUserList);
			
			if (!mNameList.isEmpty()) {
			}
		}
		List<UserDetailEntity> l = new ArrayList<>();
		UserDetailEntity u = new UserDetailEntity();
		u.setShowId(LauchrConst.header.getLoginName());
		l.add(u);
		mContactFragment.setCannotSelectUser(l);
	}
	
	@Override
	public void onBackPressed() {
		if(isShowLoading && isOk){
			// 按确定键后，对话框显示
			isOk = false;
			Log.i("infoss", "====isOk=====");
		}else if(isShowLoading && !isOk){
			//对话框正在显示，按返回键
			dismissLoading();
			isShowLoading = false;
			Log.i("infoss","====dismissLoading=====");
		}else if(!isOk && !isShowLoading){
			// 对话框没有显示，按返回键
			ContactFragment.mSelectState = 1;
			Log.i("infoss","====onBackPressed=====");
			finish();
		}
	}

//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//	}
}
