package com.mintcode.launchr.message.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.pojo.UserListPOJO;
import com.mintcode.launchr.pojo.entity.HeaderEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.view.DialogLoading;
import com.mintcode.launchrnetwork.OnResponseListener;

public class DeptWindow extends PopupWindow implements OnItemClickListener, OnClickListener, OnResponseListener {

	private Context context;
	private View mContentView;
	private TextView mTvSave;
	private ImageView mIvBack;
	private ListView mLvContent;
	private DialogLoading mLoading;

	private String mDeptId;

	private List<Boolean> mSelectTag = new ArrayList<Boolean>();
	private List<UserDetailEntity> mUserList = new ArrayList<UserDetailEntity>();
	private List<UserDetailEntity> mNoSelectData = new ArrayList<UserDetailEntity>();
	
	private UserAdapter adapter;
	private OnCompleteSelected listener;
	
	public static final int TYPE_RADIO = 0;
	public static final int TYPE_MULTI_SELECT = 1;
	public static final int TYPE_NO_ALL = 2;
	private int mType;
	
	public DeptWindow(Context context,OnCompleteSelected listener) {
		this.context = context;
		this.listener = listener;
		mLoading = DialogLoading.creatDialog(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		mContentView = inflater.inflate(R.layout.fragment_department, null);
		setWindow();
		initView();
	}
	
	public void setType(int type){
		mType = type;
	}
	
	public void SetNoAllDate(List<UserDetailEntity> entities){
		setType(TYPE_NO_ALL);
		mNoSelectData = entities;
	}
	
	private void setWindow() {
		// 设置PopupWindow的View
		this.setContentView(mContentView);
		// 设置PopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置PopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		// 设置PopupWindow弹出窗体可点击
		this.setFocusable(true);
	}

	private void initView() {
		mTvSave = (TextView) mContentView.findViewById(R.id.tv_save);
		mIvBack = (ImageView) mContentView.findViewById(R.id.iv_back);
		mLvContent = (ListView) mContentView.findViewById(R.id.lv_user);
		
		mTvSave.setVisibility(View.VISIBLE);

		adapter = new UserAdapter();
		mLvContent.setAdapter(adapter);

		mIvBack.setOnClickListener(this);
		mTvSave.setOnClickListener(this);
		mLvContent.setOnItemClickListener(this);

		if (mDeptId == null || mDeptId.equals("")) {
			mDeptId = "7a73dfe6424716a4f81";
		}
		UserApi.getInstance().getComanyUserList(this, mDeptId);
		mLoading.show();
	}
	
	
	public void show(View parent){
		showAtLocation(parent,  Gravity.CENTER, 0, 0);
	}
	
	@Override
	public void onClick(View v) {
		if (v == mIvBack) {
			dismiss();
		}else{
			List<UserDetailEntity> users = new ArrayList<UserDetailEntity>();
			if (listener != null) {
				for (int i = 0; i < mSelectTag.size(); i++) {
					if (mSelectTag.get(i)) {
						users.add(mUserList.get(i));
					}
				}
			}
			listener.CompleteSelected(users);
			dismiss();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mType == TYPE_RADIO) {
			for (int i = 0; i < mSelectTag.size(); i++) {
				mSelectTag.set(i, false);
			}
			mSelectTag.set(position, true);
		}else if(mType == TYPE_MULTI_SELECT ||mType == TYPE_NO_ALL){
			Boolean b = mSelectTag.get(position);
			mSelectTag.set(position, !b);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {

		if (taskId.equals(UserApi.TaskId.TASK_URL_GET_COMPANY_USER_LIST)) {
			handleResultUserList(response);
		}
		mLoading.dismiss();
	}

	private void handleResultUserList(Object response) {
		UserListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(
				response.toString(), UserListPOJO.class);
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				List<UserDetailEntity> list = pojo.getBody().getResponse()
						.getData();
				if (list != null) {
					mUserList.clear();
					mUserList.addAll(list);
					if (mType == TYPE_NO_ALL) {
						for (UserDetailEntity userDetailEntity : mNoSelectData) {
							String show_ID = userDetailEntity.getShowId();
							for (int i = 0; i < mUserList.size(); i++) {
								String show_ID2 = mUserList.get(i).getShowId();
								if (show_ID.equals(show_ID2)) {
									mUserList.remove(i);
									break;
								}
							}
						}
					}
					
					for (int i = 0; i < list.size(); i++) {
						mSelectTag.add(false);
					}
					adapter.notifyDataSetChanged();
				}
			} else {
				HeaderEntity entity = pojo.getHeader();

				Toast.makeText(context, entity.getReason(), Toast.LENGTH_LONG).show();
			}
		} 
	}

	private class UserAdapter extends BaseAdapter {

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
				convertView = LayoutInflater.from(context).inflate(R.layout.item_contact, null);
				hold = new ViewHold();
				hold.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				hold.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
				hold.cbCheck = (ImageView) convertView.findViewById(R.id.cb_check);
				hold.ivIcon.setBackgroundResource(R.drawable.icon_launchr);
				hold.cbCheck.setVisibility(View.VISIBLE);
				convertView.setTag(hold);
			} else {
				hold = (ViewHold) convertView.getTag();
			}

			UserDetailEntity entity = mUserList.get(position);
			
			boolean isChecked = mSelectTag.get(position);
			if (isChecked) {
				hold.cbCheck.setBackgroundResource(R.drawable.icon_green_checked);
			} else {
				hold.cbCheck.setBackgroundResource(R.drawable.icon_unchecked);
			}

			hold.tvName.setText(entity.getTrueName());

			return convertView;
		}
	}

	class ViewHold {
		TextView tvName;
		ImageView ivIcon;
		ImageView cbCheck;
	}
	
	public interface OnCompleteSelected{
		 void CompleteSelected(List<UserDetailEntity> users);
	}

	public void setOnCompleteSelectedListener(OnCompleteSelected listener){
		this.listener = listener;
	}

	@Override
	public boolean isDisable() {
		return false;
	}
}
