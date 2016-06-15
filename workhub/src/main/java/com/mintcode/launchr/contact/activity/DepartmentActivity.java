package com.mintcode.launchr.contact.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.contact.fragment.ContactFragment;
import com.mintcode.launchr.contact.fragment.ContactFragment.OnSelectContactListner;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;

public class DepartmentActivity extends BaseActivity implements OnSelectContactListner{

	public static final String SINGLE = "single";
	
	public static final String KEY_SINGLE = "key_single";
	
	public static final String KEY_USE_ENTITY = "key_use_entity";
	
	public static final String KEY_POSITION = "key_position";
	
	// 
	FragmentManager fmanager;
	ContactFragment contactFragment;
	
	private List<UserDetailEntity> mSelectUserList = new ArrayList<UserDetailEntity>();
	
//	private List<Integer> mList = new ArrayList<Integer>();
	
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_department);
		
		initData();
		
	}
	
	private void start(){
		
		
		fmanager.beginTransaction()//.setCustomAnimations(R.anim.slid_in_right, R.anim.slid_out_right)
				.add(R.id.ll_content, contactFragment).show(contactFragment).commit();
		
		mFragmentList.add(contactFragment);
	}
	
	private void initView(){
		
	}
	
	private void initData(){
		String key = getIntent().getStringExtra(KEY_SINGLE);
			// 
		contactFragment = new ContactFragment();
		contactFragment.setOnSelectContactListner(this);
		fmanager = getSupportFragmentManager();
		FragmentTransaction transaction = fmanager.beginTransaction();
		transaction.add(R.id.ll_content,contactFragment);
		transaction.show(contactFragment).commitAllowingStateLoss();
		
		if (key != null && key.equals(SINGLE)) {
			
			UserDetailEntity entity = (UserDetailEntity) getIntent().getSerializableExtra(KEY_POSITION);
			if (entity != null) {
				List<String> nameList = new ArrayList<String>();
				nameList.add(entity.getName());
				contactFragment.setSelectState(ContactFragment.SINGLE_SELECT_STATE,nameList);
			}else{
				contactFragment.setSelectUser(ContactFragment.SINGLE_SELECT_STATE,null);
			}
			mFragmentList.add(contactFragment);
			
		}
		
		
	}
	
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_cansel) {
			start();
		}
		
		
	}
	


	@Override
	public void onSelectContact(List<UserDetailEntity> userList) {
		mSelectUserList.clear();
		if (userList != null) {
			mSelectUserList.addAll(userList);
		}
	}
	
	
	public void removeFragment(){
		fmanager.beginTransaction().remove(contactFragment).commitAllowingStateLoss();
	}
	
	@Override
	public void onBackPressed() {


		if (!mSelectUserList.isEmpty()) {
			removeFragment();
			mFragmentList.clear();
			UserDetailEntity entity = mSelectUserList.get(0);
			Intent intent = new Intent();
			intent.putExtra(KEY_USE_ENTITY, entity);
			setResult(RESULT_OK, intent);
			this.finish();
		}else{
			removeFragment();
			mFragmentList.clear();
			this.finish();
		}
//		super.onBackPressed();
//		if (mFragmentList.isEmpty()) {
//		}else{
//			removeFragment();
//			mFragmentList.remove(d);
//		}
		
		
	}

	
	
	
}
