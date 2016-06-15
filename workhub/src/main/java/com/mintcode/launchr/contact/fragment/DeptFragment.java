package com.mintcode.launchr.contact.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.contact.OnSelectUserListener;
import com.mintcode.launchr.contact.activity.PersonDetailActivity;
import com.mintcode.launchr.pojo.UserListPOJO;
import com.mintcode.launchr.pojo.entity.HeaderEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.GlideRoundTransform;
import com.mintcode.launchr.util.TTJSONUtil;

public class DeptFragment extends BaseFragment implements OnItemClickListener{

	OnSelectUserListener onSelectUserListener;

	public static final int NO_STATE = 1;
	
	public static final int SELECT_STATE = 2;
	
	public static final int EDITOR_SELECT_STATE = 3;
	
	public static final int SINGLE_SELECT_STATE = 4;
	
	private int mState = NO_STATE;
	
	private static final int GET_USER_SUCCESS = 1;
	
	private ImageView mIvBack;
	
	private TextView mTvSave;
	
	private View mRootView;
	
	private ListView mLvContent;
	
	
	private Handler mHandler;
	
	
	private List<UserDetailEntity> mUserList;
	
	private List<UserDetailEntity> mSelectUserList;
	
	private List<Boolean> mPositionList;
	
	private List<Integer> mLastSelectList = new ArrayList<Integer>();
	
	UserAdapter mUserAdapter;
	
	private boolean mRest = false;
	
	private List<String> mNameList;
	
	private String mDeptId = "";
	
	private int mPosition = -1;
	
	private Context mContext;
	
	public DeptFragment() {
		
	}
	
	public DeptFragment(String deptId){
		mDeptId = deptId;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_department, null);
		return mRootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		initData();
		initView();
		
	}
	
	private void initView(){
		mIvBack = (ImageView) mRootView.findViewById(R.id.iv_back);
		mTvSave = (TextView) mRootView.findViewById(R.id.tv_save);
		mLvContent = (ListView) mRootView.findViewById(R.id.lv_user);		
		
		mIvBack.setOnClickListener(this);
		mTvSave.setOnClickListener(this);
		
//		mContactAdapter = new ContactAdapter(getActivity(), null);
//		mLvContent.setAdapter(mContactAdapter);
		
		mUserAdapter = new UserAdapter();
		mLvContent.setAdapter(mUserAdapter);
		
		mLvContent.setOnItemClickListener(this);
		
		mHandler = new DeptHandler();
		
		mContext = getActivity();
	}
	
	private void initData(){
		mSelectUserList = new ArrayList<UserDetailEntity>();
		mUserList = new ArrayList<UserDetailEntity>();
		mPositionList = new ArrayList<Boolean>();
		mNameList = new ArrayList<String>();
//		mLastSelectList = new ArrayList<Integer>();
		
//		showLoading();
		
		if (mDeptId == null || mDeptId.equals("")) {
			mDeptId = "7a73dfe6424716a4f81";
		}
		UserApi.getInstance().getComanyUserList(this, mDeptId);
	}

	
	@Override
	public void onClick(View v) {
		if (v == mIvBack) {
			getActivity().onBackPressed();
		}else if (v == mTvSave) {
			if (onSelectUserListener != null) {
				accessSelectUser();
			}
			getActivity().onBackPressed();
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		UserDetailEntity entity = mUserList.get(position);
		if (mState == SELECT_STATE || mState == EDITOR_SELECT_STATE) {
			boolean b = mPositionList.get(position);
			if (b) {
				b = false;
			}else{
				b = true;
			}
			mPositionList.add(position, b);
			mPositionList.remove(position + 1);
			mUserAdapter.notifyDataSetChanged();
		}else if (mState == SINGLE_SELECT_STATE) {
			mPosition = position;
			mUserAdapter.notifyDataSetChanged();
		}else{
			Intent intent = new Intent(getActivity(), PersonDetailActivity.class);
//			intent.putExtra("detail", entity);
			intent.putExtra("detail", entity);
			startActivity(intent);
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
//					mContactAdapter.changeData(list);
					Log.i("infos", mState + "---");
					if (mState == EDITOR_SELECT_STATE) {
						mUserList.clear();
						resetNamePosition(list);
						mUserList.addAll(list);
						mTvSave.setVisibility(View.VISIBLE);
					}else if (mState == SINGLE_SELECT_STATE) {
						if (mState == SINGLE_SELECT_STATE) {
							mTvSave.setVisibility(View.VISIBLE);
						}
						
						mUserList.clear();
						mUserList.addAll(list);
					}else{
						mRest = true;
						mUserList.clear();
						resetState(list);
						mUserList.addAll(list);
						if (mState == SELECT_STATE) {
							mTvSave.setVisibility(View.VISIBLE);
						}
					}
					mUserAdapter.notifyDataSetChanged();
					
				}
				
			}else{
				HeaderEntity entity = pojo.getHeader();
				toast(entity.getReason());
			}
			
		}else{
			showNetWorkGetDataException();
		}
		
	}
	
	public void setState(int state, List<Integer> list){
		
		if (state == SELECT_STATE) {
			mState = SELECT_STATE;
			if (list != null) {
				mLastSelectList.clear();
				mLastSelectList.addAll(list);
				mRest = true;
				
			}
			
			if ((mUserList != null) && !mUserList.isEmpty()) {
				resetState(mUserList);
				mUserAdapter.notifyDataSetChanged();
			}
			
			if (mTvSave != null) {
				mTvSave.setVisibility(View.VISIBLE);
			}
		}else if (state == EDITOR_SELECT_STATE) {
			mState = EDITOR_SELECT_STATE;
			
		}else if (state == SINGLE_SELECT_STATE) {
			mState = SINGLE_SELECT_STATE;
			if (list == null || list.isEmpty()) {
				mPosition = -1;
			}else{
				mPosition = list.get(0);	
			}
			if (mUserAdapter != null) {
				mUserAdapter.notifyDataSetChanged();
			}
			
			if (mTvSave != null) {
				mTvSave.setVisibility(View.VISIBLE);
			}
			
		}else{
			if (mTvSave != null) {
				mTvSave.setVisibility(View.GONE);
			}
			
			mState = NO_STATE;
		}
	}
	
	/**
	 * 同上
	 * @param state
	 * @param listist
	 * @param reson  没有任何实际意义
	 */
	public void setState(int state, List<String> listist,String reson){
		if (state == EDITOR_SELECT_STATE) {
			mState = EDITOR_SELECT_STATE;
			if (listist != null) {
				if ((mUserList != null) && !mUserList.isEmpty()) {
					mNameList.clear();
					mNameList.addAll(listist);
					resetNamePosition(mUserList);
				}
				mUserAdapter.notifyDataSetChanged();
				
				if (mTvSave != null) {
					mTvSave.setVisibility(View.VISIBLE);
				}
			}
			
		}
	}
	
	
	String path = "http://121.40.30.204:10050/attachment/headPicAttachment/80x80";
	private class UserAdapter extends BaseAdapter{

		
		
		@Override
		public int getCount() {
			
			return mUserList.size();
		}

		
		@Override
		public Object getItem(int position) {
			return mUserList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
				ViewHold hold = null;
				if (convertView == null) {
					convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contact, null);
					hold = new ViewHold();
					hold.tvName = (TextView) convertView.findViewById(R.id.tv_name);
					hold.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
					hold.cbCheck = (ImageView) convertView.findViewById(R.id.cb_check);
					
					convertView.setTag(hold);
					
				}else{
					hold = (ViewHold) convertView.getTag();
				}
				
				String url = path + "/7ae0c298-4e51-49c1-a2e6-31509069dd32.jpg";;
				
				// 处理圆形图片
				RequestManager man = Glide.with(DeptFragment.this);
				GlideRoundTransform g =  new GlideRoundTransform(getActivity(),18);
				man.load(url).transform(g).into(hold.ivIcon);
				
				UserDetailEntity entity = mUserList.get(position);
				
				if ((mState == SELECT_STATE) || (mState == EDITOR_SELECT_STATE)) {
					hold.cbCheck.setVisibility(View.VISIBLE);
					boolean isChecked = mPositionList.get(position);
					if (isChecked) {
						hold.cbCheck.setBackgroundResource(R.drawable.icon_green_checked);
					}else{
						hold.cbCheck.setBackgroundResource(R.drawable.icon_unchecked);
					}
				}else if (mState == SINGLE_SELECT_STATE) {
					hold.cbCheck.setVisibility(View.VISIBLE);
					if (mPosition == position) {
						hold.cbCheck.setBackgroundResource(R.drawable.icon_green_checked);
					}else{
						hold.cbCheck.setBackgroundResource(R.drawable.icon_unchecked);
					}
					
				}else{
					hold.cbCheck.setVisibility(View.GONE);
				}
				
				
				hold.tvName.setText(entity.getTrueName());
				
				
			
			return convertView;
		}
		
		
	}
	
	class ViewHold{
		TextView tvName;
		ImageView ivIcon;
		ImageView cbCheck;
	}
	
	private class DeptHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == GET_USER_SUCCESS) {
				//  处理消息成功
			}
			
		}
	}
	
	private void resetState(List<UserDetailEntity> list){
		mPositionList.clear();
		if (mRest) {
			for (int i = 0; i < list.size(); i++) {
				boolean b = false;
				for (int j = 0; j < mLastSelectList.size(); j++) {
					int x = mLastSelectList.get(j);
					if (x == i) {
						b = true;
						break;
					}
					
				}
				mPositionList.add(b);
			}
			mRest = false;
		}else{
			for (int i = 0; i < list.size(); i++) {
				mPositionList.add(false);
			}
		}
		
	}
	
	/**
	 * 
	 * @param list
	 */
	private void resetNamePosition(List<UserDetailEntity> list){
		mPositionList.clear();
		if (mNameList != null) {
			for (int i = 0; i < list.size(); i++) {
				UserDetailEntity entity = list.get(i);
				boolean b = false;
				for (int j = 0; j < mNameList.size(); j++) {
					String name = mNameList.get(j);
					if (name.equals(entity.getName())) {
						b = true;
						break;		
					}		
				}
				mPositionList.add(b);
			}
	   }else{
		   for (int i = 0; i < list.size(); i++) {
			   mPositionList.add(false);
		   }
	   }
		
		
	}
	
	
	public OnSelectUserListener getOnSelectUserListener() {
		return onSelectUserListener;
	}

	public void setOnSelectUserListener(OnSelectUserListener onSelectUserListener) {
		this.onSelectUserListener = onSelectUserListener;
	}
	
	private void accessSelectUser(){
		if (mState == SELECT_STATE || mState == EDITOR_SELECT_STATE) {
			List<UserDetailEntity> list = new ArrayList<UserDetailEntity>();
			List<Integer> positionList = new ArrayList<Integer>();
			for (int i = 0; i < mPositionList.size(); i++) {
				boolean b = mPositionList.get(i);
				if (b) {
					UserDetailEntity u = mUserList.get(i);
					list.add(u);
					positionList.add(i);
				}
			}
			onSelectUserListener.onSelectUser(list, positionList);
		}else if (mState == SINGLE_SELECT_STATE) {
			List<UserDetailEntity> list = new ArrayList<UserDetailEntity>();
			List<Integer> positionList = new ArrayList<Integer>();
			if (mPosition != -1) {
				UserDetailEntity u = mUserList.get(mPosition);
				list.add(u);
				positionList.add(mPosition);
			}
			onSelectUserListener.onSelectUser(list, positionList);
		}
		
		
	}
	@Override
    public void onDetach() {
    	super.onDetach();
    	try {
    	    Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
    	    childFragmentManager.setAccessible(true);
    	    childFragmentManager.set(this, null);

    	} catch (NoSuchFieldException e) {
    	    throw new RuntimeException(e);
    	} catch (IllegalAccessException e) {
    	    throw new RuntimeException(e);
    	}
    
    }
}
