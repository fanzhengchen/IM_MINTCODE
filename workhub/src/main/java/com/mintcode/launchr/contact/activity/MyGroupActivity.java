package com.mintcode.launchr.contact.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mintcode.chat.activity.ChatActivity;
import com.mintcode.im.IMManager;
import com.mintcode.im.database.SessionDBService;
import com.mintcode.im.entity.SessionItem;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.IMNewApi;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.activity.MainActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.ChatGroupPOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.ChatGroupEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTDensityUtil;
import com.mintcode.launchr.util.TTJSONUtil;

import java.util.List;

/**
 * Created by StephenHe on 2016/3/16.
 */
public class MyGroupActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    /** 返回*/
    private ImageView mIvBack;

    /** 群组列表*/
    private ListView mLvGroup;
    private List<ChatGroupEntity> mListData;
    private MyGroupAdapter mGroupAdapter;

    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group);
        initView();
    }

    private void initView(){
        mInflater = LayoutInflater.from(this);

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mLvGroup = (ListView) findViewById(R.id.lv_group);
        mIvBack.setOnClickListener(this);
        mLvGroup.setOnItemClickListener(this);
        mGroupAdapter = new MyGroupAdapter();
        mLvGroup.setAdapter(mGroupAdapter);
        String userName = AppUtil.getInstance(this).getShowId();
        IMNewApi.getInstance().getMyGroupList(this,userName);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mIvBack){
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent == mLvGroup){
            ChatGroupEntity sessionItem = mListData.get(position);
            Intent intent = new Intent(MyGroupActivity.this, ChatActivity.class);
            intent.putExtra("groupId", sessionItem.getUserName());
            intent.putExtra("groupName", sessionItem.getNickName());
            IMManager.getInstance().readMessageByUid(sessionItem.getUserName());
            startActivity(intent);
            SessionDBService.getInstance().updateSessionTime((int)sessionItem.getUid());
            MainActivity.showFirstFragment = 1;
            finish();
        }
    }

    private class MyGroupAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mListData == null ? 0:mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_contact, null);
                holder.ivHead = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvGroupName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvWord = (TextView) convertView.findViewById(R.id.tv_word);
                holder.tvDept = (TextView) convertView.findViewById(R.id.tv_dept);
                holder.tvNameOfDept = (TextView) convertView.findViewById(R.id.tv_name_of_dept);
                holder.tvNameOfDept.setVisibility(View.VISIBLE);
                holder.tvGroupName.setVisibility(View.GONE);
                holder.tvWord.setVisibility(View.GONE);
                holder.tvDept.setVisibility(View.GONE);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvNameOfDept.setSingleLine();
            holder.tvNameOfDept.setEllipsize(TextUtils.TruncateAt.END);
            holder.tvNameOfDept.setText(mListData.get(position).getNickName());
            holder.tvNameOfDept.setMaxWidth(TTDensityUtil.dip2px(MyGroupActivity.this, 200));
            HeadImageUtil.getInstance(MyGroupActivity.this).setAvatarResourceAppendUrl(holder.ivHead,mListData.get(position).getUserName(), 0, 60, 60);
//            setAvatar(mListData.get(position).getNickName(), MyGroupActivity.this, holder.ivHead, null);
            return convertView;
        }
    }

    private class ViewHolder{
        ImageView ivHead;
        TextView tvGroupName;
        TextView tvWord;
        TextView tvDept;
        TextView tvNameOfDept;
    }


    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        if(taskId.equals(IMNewApi.TaskId.TASK_URL_GET_MY_GROUP_LIST)){
            ChatGroupPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),ChatGroupPOJO.class);
            if(pojo != null){
                if(pojo.isResultSuccess()){
                    mListData =  pojo.getData();
                    mGroupAdapter.notifyDataSetChanged();
                }else{
                    toast(pojo.getMessage());
                }
            }
        }
    }
}
