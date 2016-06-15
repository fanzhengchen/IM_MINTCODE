package com.mintcode.launchr.app.newTask.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.contact.activity.PersonDetailActivity;
import com.mintcode.launchr.pojo.ChatUserDetailPOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.ChatUserDetailEntity;
import com.mintcode.launchr.pojo.entity.TaskAddProjectEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchrnetwork.OnResponseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/2/25.
 */
public class ProjectPersonAdapter extends BaseAdapter implements
        OnResponseListener {

    private Context context;

    private List<TaskAddProjectEntity.TaskAddProjectMembersEntity> mPersonList = new ArrayList<>();

    private String mShowId;

    private boolean canDel = false;

    OnGroupShowFragmentListener mOnGroupShowFragmentListener;

    public ProjectPersonAdapter(Context context, List<TaskAddProjectEntity.TaskAddProjectMembersEntity> data) {
        this.context = context;
        mPersonList.addAll(data);
        mShowId = AppUtil.getInstance(context).getShowId();
    }

    public List<TaskAddProjectEntity.TaskAddProjectMembersEntity> getPersonList(){
        return mPersonList;
    }

    public void addPersonList(List<TaskAddProjectEntity.TaskAddProjectMembersEntity> List){
        if(List != null){
            mPersonList.addAll(List);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {

            return mPersonList == null ? 0 : mPersonList.size() + 2;
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
                if (mShowId.equals(mPersonList.get(0).getMemberName())) {
                    holder.ivDelete.setVisibility(View.GONE);
                    holder.tvName.setText(mPersonList.get(0).getMemberTrueName());
                    holder.ivAvatar.setTag(R.id.group_setting_id,mPersonList.get(0).getMemberName());
                    holder.ivAvatar.setOnClickListener(headerListener);
                    setHeaderImage(mPersonList.get(0).getMemberName(), holder.ivAvatar);
                }
            } else {
                holder.ivDelete.setTag(position);
                holder.ivDelete.setOnClickListener(listener);
                TaskAddProjectEntity.TaskAddProjectMembersEntity data = mPersonList.get(position);
                holder.tvName.setText(data.getMemberTrueName());
                holder.ivAvatar.setTag(R.id.group_setting_id,data.getMemberName());
                holder.ivAvatar.setOnClickListener(headerListener);
                setHeaderImage(data.getMemberName(), holder.ivAvatar);
            }
        return convertView;
    }

    class Holder {
        ImageView ivAvatar;
        ImageView ivDelete;
        TextView tvName;
    }

    /** 显示头像 */
    public void setHeaderImage(String name, ImageView mIvAvatar) {
        HeadImageUtil.getInstance(context).setAvatarResourceAppendUrl(mIvAvatar,name, 2,60,60);
    }
    //删除项目成员
    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            mPersonList.remove(position);
            notifyDataSetChanged();
        }
    };
    //显示删除键
    View.OnClickListener delListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            canDel = !canDel;
            notifyDataSetChanged();
        }
    };
   //添加项目成员
    View.OnClickListener addListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            doShowFragment();
        }
    };
    //点击头像查看成员详情
    private View.OnClickListener headerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String headerUserName = (String) v.getTag(R.id.group_setting_id);
            UserApi.getInstance().getComanyUserInfo(onResponseListener,
                    headerUserName);
        }
    };

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
//        if (response instanceof DelGroupUserPOJO) {
//            DelGroupUserPOJO pojo = (DelGroupUserPOJO) response;
//            if (pojo.isResultSuccess()) {
//                ((GroupSettingActivity) context).getData();
//            }
//        } else if (response instanceof AddGroupUserPOJO) {
//            AddGroupUserPOJO pojo = (AddGroupUserPOJO) response;
//            if (pojo.isResultSuccess()) {
//                ((GroupSettingActivity) context).getData();
//            }
//        }
    }

    private OnResponseListener onResponseListener = new OnResponseListener() {

        @Override
        public void onResponse(Object response, String taskId, boolean rawData) {
            // TODO Auto-generated method stub
            if(response == null){
                return;
            }
            if (taskId.equals(UserApi.TaskId.TASK_URL_GET_COMPANY_USER_INFO)) {
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

    public void setOnGroupShowFragment(OnGroupShowFragmentListener onGroupShowFragmentListener) {
        this.mOnGroupShowFragmentListener = onGroupShowFragmentListener;
    }

    public void doShowFragment() {
        if (mOnGroupShowFragmentListener != null) {
            mOnGroupShowFragmentListener.onGroupShowFragment();
        }
    }


}

