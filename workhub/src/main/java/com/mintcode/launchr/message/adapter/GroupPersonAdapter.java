package com.mintcode.launchr.message.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.AddGroupUserPOJO;
import com.mintcode.DelGroupUserPOJO;
import com.mintcode.chat.user.GroupInfo;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.IMAPI;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.api.UserApi.TaskId;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.contact.activity.PersonDetailActivity;
import com.mintcode.launchr.message.activity.GroupSettingActivity;
import com.mintcode.launchr.pojo.ChatUserDetailPOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.ChatUserDetailEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchrnetwork.OnResponseListener;

public class GroupPersonAdapter extends BaseAdapter implements
		OnResponseListener {

	private Context context;

	private List<GroupInfo> mData;

	private String mShowId;
	private String mUserToken;
	private String mGroupName;

	private boolean isAdmin = false;
	private boolean canDel = false;

	OnGroupShowFragmentListener mOnGroupShowFragmentListener;

	public GroupPersonAdapter(Context context, List<GroupInfo> data,
			String groupName) {
		this.context = context;
		mData = data;
		mGroupName = groupName;
		mShowId = AppUtil.getInstance(context).getShowId();
		mUserToken = AppUtil.getInstance(context).getIMToken();
		if (mData != null && mData.size() > 0) {
			isAdmin = mShowId.equals(mData.get(0).getUserName());
		}
	}

	public void updateData(List<GroupInfo> data) {
		mData = data;
		if (mData != null && mData.size() > 0) {
			isAdmin = mShowId.equals(mData.get(0).getUserName());
		}
		notifyDataSetChanged();
	}

	public void setCanotDel() {
		canDel = false;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (isAdmin) {
			return mData == null ? 0 : mData.size() + 2;
		} else {
			return mData == null ? 0 : mData.size() + 1;
		}
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_group_person, parent, false);
			holder = new Holder();
			holder.ivAvatar = (ImageView) convertView
					.findViewById(R.id.iv_avatar);
			holder.ivDelete = (ImageView) convertView
					.findViewById(R.id.iv_delete);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if (canDel) {
			holder.ivDelete.setVisibility(View.VISIBLE);
		} else {
			holder.ivDelete.setVisibility(View.GONE);
		}
		if (isAdmin) {
			// 假如是群管理员
			if (position == getCount() - 2) {
				holder.ivAvatar
						.setImageResource(R.drawable.icon_group_add);
				holder.tvName.setText("");
				holder.ivDelete.setVisibility(View.GONE);
				holder.ivAvatar.setOnClickListener(addListener);
			} else if (position == getCount() - 1) {
				holder.ivAvatar
						.setImageResource(R.drawable.icon_group_del);
				holder.tvName.setText("");
				holder.ivDelete.setVisibility(View.GONE);
				holder.ivAvatar.setOnClickListener(delListener);
			} else if (position == 0) {
				if (mShowId.equals(mData.get(0).getUserName())) {
					holder.ivDelete.setVisibility(View.GONE);
					holder.tvName.setText(mData.get(0).getNickName());
//					holder.ivAvatar.setTag(mData.get(0).getUserName());
					holder.ivAvatar.setTag(R.id.group_setting_id,mData.get(0).getUserName());
					holder.ivAvatar.setOnClickListener(headerListener);
					setHeaderImage(mShowId, holder.ivAvatar);
				}
			} else {
				holder.ivDelete.setTag(position);
				holder.ivDelete.setOnClickListener(listener);
				GroupInfo data = mData.get(position);
				holder.tvName.setText(data.getNickName());
				holder.ivAvatar.setTag(R.id.group_setting_id,data.getUserName());
				holder.ivAvatar.setOnClickListener(headerListener);
				setHeaderImage(data.getUserName(), holder.ivAvatar);
			}
		} else {
			// 假如不是群管理员
			if (position == getCount() - 1) {
				holder.ivAvatar
						.setImageResource(R.drawable.icon_group_add);
				holder.tvName.setText("");
				holder.ivDelete.setVisibility(View.GONE);
				holder.ivAvatar.setOnClickListener(addListener);
			} else {
				// TODO 头像
				holder.ivDelete.setTag(position);
				holder.ivDelete.setOnClickListener(listener);
				GroupInfo data = mData.get(position);
				holder.tvName.setText(data.getNickName());
				holder.ivDelete.setVisibility(View.GONE);
				holder.ivAvatar.setTag(R.id.group_setting_id,data.getUserName());
				holder.ivAvatar.setOnClickListener(headerListener);
				setHeaderImage(data.getUserName(), holder.ivAvatar);
			}
		}

		return convertView;
	}

	class Holder {
		ImageView ivAvatar;
		ImageView ivDelete;
		TextView tvName;
	}

	/** 显示头像 */
	public void setHeaderImage(String userId, ImageView mIvAvatar) {
		HeadImageUtil.getInstance(context).setAvatarResourceAppendUrl(mIvAvatar, userId, 2, 60, 60);
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			String toUserName = mData.get(position).getUserName();
			((GroupSettingActivity)context).showLoading();
			IMAPI.getInstance().delGroupUser(GroupPersonAdapter.this,
					mUserToken, mShowId, mGroupName, toUserName);
		}
	};

	OnClickListener delListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			canDel = !canDel;
			notifyDataSetChanged();
		}
	};

	OnClickListener addListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			List<UserDetailEntity> list = new ArrayList<UserDetailEntity>();
			for (int i = 0; i < mData.size(); i++) {
				UserDetailEntity entity = new UserDetailEntity();
				entity.setShowId(mData.get(i).getUserName());
				list.add(entity);
			}
			doShowFragment();
		}
	};
	private OnClickListener headerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String headerUserName = (String) v.getTag(R.id.group_setting_id);
			UserApi.getInstance().getComanyUserInfo(onResponseListener,
							headerUserName);
		}
	};

	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		((GroupSettingActivity)context).dismissLoading();
		if(response == null){
			return;
		}
		if (taskId.equals(IMAPI.TASKID.DEL_GROUP_USER) ){
			DelGroupUserPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),DelGroupUserPOJO.class);
			if (pojo.isResultSuccess()) {
				((GroupSettingActivity) context).getData();
			}
		} else if (taskId.equals(IMAPI.TASKID.ADD_GROUP_USER)) {
			AddGroupUserPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),AddGroupUserPOJO.class);
			if (pojo.isResultSuccess()) {
				((GroupSettingActivity) context).getData();
			}
		}
	}

	private com.mintcode.launchrnetwork.OnResponseListener onResponseListener = new com.mintcode.launchrnetwork.OnResponseListener() {

		@Override
		public void onResponse(Object response, String taskId, boolean rawData) {
			// TODO Auto-generated method stub
			if(response == null){
				return;
			}
			if (taskId.equals(TaskId.TASK_URL_GET_COMPANY_USER_INFO)) {
				ChatUserDetailPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), ChatUserDetailPOJO.class);
				if (pojo == null) {
					return;
				} else if (pojo.getBody() == null) {
					return;
				} else if (pojo.getBody().getResponse().getData() == null) {
					return;
				} else {
					ChatUserDetailEntity entity = pojo.getBody().getResponse()
							.getData();
					if (entity != null) {
						Intent intent = new Intent(context, PersonDetailActivity.class);
						intent.putExtra(PersonDetailActivity.KEY_PERSONAL_ID, entity.getSHOW_ID());
						context.startActivity(intent);
					}
				}
			}
		}

		@Override
		public boolean isDisable() {
			// TODO Auto-generated method stub
			return false;
		}
	};

	@Override
	public boolean isDisable() {
		return false;
	}

	public interface OnGroupShowFragmentListener {
		void onGroupShowFragment();
	}

	public void setOnGroupShowFragment(
			OnGroupShowFragmentListener onGroupShowFragmentListener) {
		this.mOnGroupShowFragmentListener = onGroupShowFragmentListener;
	}

	public void doShowFragment() {
		if (mOnGroupShowFragmentListener != null) {
			mOnGroupShowFragmentListener.onGroupShowFragment();
		}
	}

	public List<GroupInfo> getList(){
		return mData;
	}

}
