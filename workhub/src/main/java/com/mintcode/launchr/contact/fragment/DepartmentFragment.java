package com.mintcode.launchr.contact.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.contact.OnSelectUserListener;
import com.mintcode.launchr.contact.adapter.ContactAdapter;
import com.mintcode.launchr.pojo.UserListPOJO;
import com.mintcode.launchr.pojo.entity.HeaderEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.TTJSONUtil;

public class DepartmentFragment extends BaseFragment implements OnItemClickListener{

	OnSelectUserListener onSelectUserListener;
	
	private TextView mTvBack;
	
	private TextView mTvSave;
	
	private View mRootView;
	
	private ListView mLvContent;
	
	ContactAdapter mContactAdapter;
	
	private List<UserDetailEntity> mUserList;
	
	private List<UserDetailEntity> mSelectUserList;
	
	
	private List<Boolean> mBooleanList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mRootView = inflater.inflate(R.layout.fragment_department, null);
		
		return mRootView;
	}
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		initView();
		
		// 
		
		initData();
		
	}
	
	
	private void initView(){
		mTvBack = (TextView) mRootView.findViewById(R.id.tv_back);
		mTvSave = (TextView) mRootView.findViewById(R.id.tv_save);
		mLvContent = (ListView) mRootView.findViewById(R.id.lv_user);		
		
		mTvBack.setOnClickListener(this);
		mTvSave.setOnClickListener(this);
		
		mContactAdapter = new ContactAdapter(getActivity(), null);
		mLvContent.setAdapter(mContactAdapter);
		
		mLvContent.setOnItemClickListener(this);
	}
	
	
	private void initData(){
		mUserList = new ArrayList<UserDetailEntity>();
		mSelectUserList = new ArrayList<UserDetailEntity>();
		
		showLoading();
		UserApi.getInstance().getComanyUserList(this, "");
	}
	
	@Override
	public void onClick(View v) {
		if (v == mTvBack) {
//			getActivity().onBackPressed();
//			DepartmentActivity d = (DepartmentActivity) getActivity();
//			d.removeFragment();
			getActivity().onBackPressed();
		}else if (v == mTvSave) {
			if (onSelectUserListener != null) {
				onSelectUserListener.onSelectUser(mSelectUserList, getSelectionPostion());
//				DepartmentActivity d = (DepartmentActivity) getActivity();
//				d.removeFragment();
			}
			getSelectionPostion();
			getActivity().onBackPressed();
		}
	}

	
	public void setOnSelectUserListener(OnSelectUserListener onSelectUserListener) {
		this.onSelectUserListener = onSelectUserListener;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Log.i("infos", position + "--");
		mContactAdapter.setState(position);
		if (position < mBooleanList.size()) {
			boolean b = mBooleanList.get(position) ;
			if (b) {
				b = false;
				mBooleanList.add(position, b);
				mBooleanList.remove(position + 1);
			}else{
				b = true;
				mBooleanList.add(position, b);
				mBooleanList.remove(position + 1);
			}
		}
		
	}
	
	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		super.onResponse(response, taskId, rawData);
		// 判断返回是否为空
		if (response == null) {
			showNoNetWork();
			return;
		}
		
		// 判断是否是获取我的部门列表返回
		if (taskId.equals(UserApi.TaskId.TASK_URL_GET_COMPANY_USER_LIST)) {
			handleResultUserList(response);
			dismissLoading();
		}
		
		
	}
	
	/**
	 * 处理我的部门返回
	 * @param response
	 */
	private void handleResultUserList(Object response){
		UserListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), UserListPOJO.class);
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				List<UserDetailEntity> list = pojo.getBody().getResponse().getData();
				
				if (list != null) {
					resetState(list);
					mContactAdapter.changeData(list);
					mUserList.addAll(list);
				}
				
			}else{
				HeaderEntity entity = pojo.getHeader();
				toast(entity.getReason());
			}
			
		}else{
			showNetWorkGetDataException();
		}
		
	}
	
	public void setResetUser(List<Integer> positionList){
		if (!mUserList.isEmpty()) {
			mBooleanList = new ArrayList<Boolean>();
			for (int i = 0; i < mUserList.size(); i++) {
				boolean b = false;
				for (int j = 0; j < positionList.size(); j++) {
					int x = positionList.get(j);
					if (x == i) {
						b = true;
						break;
					}
					
				}
				mBooleanList.add(b);
			}
			mContactAdapter.changeSelectState(2, mBooleanList);
		}
	}
	
	private void resetState(List<UserDetailEntity> list){
		if (!list.isEmpty()) {
			mBooleanList = new ArrayList<Boolean>();
			for (int i = 0; i < list.size(); i++) {
				mBooleanList.add(false);
			}
		}
	}


	public ContactAdapter getmContactAdapter() {
		return mContactAdapter;
	}


	public void setmContactAdapter(ContactAdapter mContactAdapter) {
		this.mContactAdapter = mContactAdapter;
	}
	
	
	private List<Integer> getSelectionPostion(){
		List<Integer> list = new ArrayList<Integer>();
		mSelectUserList.clear();
		for (int i = 0; i < mBooleanList.size(); i++) {
			boolean b = mBooleanList.get(i);
			if (b) {
				Log.i("mes", i + "===");
				list.add(i);
				UserDetailEntity user = mUserList.get(i);
				mSelectUserList.add(user);
			}
		}
		
		return list;
	}
	
	
	
}
