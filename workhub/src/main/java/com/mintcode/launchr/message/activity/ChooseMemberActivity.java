package com.mintcode.launchr.message.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.contact.fragment.ContactFragment;
import com.mintcode.launchr.contact.fragment.ContactFragment.OnSelectContactListner;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchrnetwork.OnResponseListener;

public class ChooseMemberActivity extends BaseActivity implements OnSelectContactListner, OnResponseListener {
    private FragmentManager mFragManger;
	
	// 通讯录
	private ContactFragment mContactFragment;

	private List<String> mNameList;

	private List<String> mUserList;

	private List<UserDetailEntity> mAssignUserList;
	/** 选人模式 单个和多个*/
	public final static String SELECT_PERSON_TYPE = "select_person_type";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
		mFragManger = getSupportFragmentManager();
		mFragManger.beginTransaction().add(R.id.actvity_create_group, mContactFragment)
		                              .hide(mContactFragment).commitAllowingStateLoss();
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

			Intent data = new Intent();


//			data.putExtra("userId", userList.get(0).getName());
//			data.putExtra("userName", userList.get(0).getTrueName());
//			setResult(RESULT_OK, data);

			List<String> name = new ArrayList<>();
			List<String> trueName = new ArrayList<>();
			for(UserDetailEntity entity :userList){
				name.add(entity.getName());
				trueName.add(entity.getTrueName());
			}
			data.putExtra("userId", (Serializable)name);
			data.putExtra("userName",(Serializable)trueName);
			setResult(RESULT_OK, data);
		}
	}
	
	public void doCreateGroup(){
		boolean boolSingleType = getIntent().getBooleanExtra(SELECT_PERSON_TYPE,true);
		int intSelectState;
		if(boolSingleType){
			intSelectState = ContactFragment.SINGLE_SELECT_STATE;
		}else{
			intSelectState = ContactFragment.MULTI_SELECT_STATE;
		}

		if (mUserList != null && !mUserList.isEmpty()) {
			mFragManger.beginTransaction().show(mContactFragment).commitAllowingStateLoss();
			mContactFragment.setSelectState(intSelectState,
					mUserList);
		} else {
			mFragManger.beginTransaction().show(mContactFragment).commitAllowingStateLoss();
			mContactFragment.setSelectUser(intSelectState,
					mAssignUserList);
			
			if (!mNameList.isEmpty()) {
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		ContactFragment.mSelectState = 1;
//		super.onBackPressed();
		finish();
	}
}

