package com.mintcode.launchr.message.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mintcode.cache.CacheManager;
import com.mintcode.chat.activity.ChatActivity;
import com.mintcode.chat.entity.User;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.UserListPOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.TTJSONUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StephenHe on 2016/1/18.
 */
public class LookMoreFriendsActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ImageView mIvBack;

    /** 联系人*/
    private ListView mLvFiends;
    private FriendAdapter mFriendAdapter;
    private List<UserDetailEntity> mFriendData;

    private String mSearchWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_more_friends);

        initView();
    }

    private void initView(){
        mSearchWord = getIntent().getStringExtra("key_word");

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mLvFiends = (ListView) findViewById(R.id.lv_friend);

        mFriendData = new ArrayList<>();
        mFriendData = HistorySearchActivity.mFriendData;
        mFriendAdapter = new FriendAdapter();
        mLvFiends.setAdapter(mFriendAdapter);
        mLvFiends.setOnItemClickListener(this);

        if(mSearchWord != null){
//            UserApi.getInstance().getSearchUserList(LookMoreFriendsActivity.this, mSearchWord);
        }
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
        if(parent == mLvFiends){
            User user = new User(mFriendData.get(position).getTrueName(), mFriendData.get(position).getShowId());
            Intent intent = new Intent(LookMoreFriendsActivity.this, ChatActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        if(response == null){
            return;
        }

        if(taskId.equals(UserApi.TaskId.TASK_URL_GET_COMPANY_USER_LIST)){
            UserListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), UserListPOJO.class);
            if(pojo == null){
                return;
            }else if(pojo.isResultSuccess() == false){
                return;
            }else if(pojo.getBody().getResponse().getData()!=null){
                mFriendData = pojo.getBody().getResponse().getData();
                mFriendAdapter.notifyDataSetChanged();
            }
        }
    }

    private class FriendAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mFriendData.size();
        }

        @Override
        public Object getItem(int position) {
            return mFriendData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if(convertView == null){
                holder = new Holder();
                convertView =  LayoutInflater.from(LookMoreFriendsActivity.this).inflate(R.layout.item_chat_search, parent, false);
                holder.ivAvatar = (ImageView) convertView.findViewById(R.id.message_head_iv);
                holder.tvContent = (TextView) convertView.findViewById(R.id.message_content);
                holder.tvName = (TextView) convertView.findViewById(R.id.message_name);
                holder.tvTime = (TextView) convertView.findViewById(R.id.message_time);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
            UserDetailEntity entity = mFriendData.get(position);
            holder.tvName.setText(entity.getTrueName());
            holder.tvContent.setText(entity.getdName());
            setHeaderImage(holder.ivAvatar, entity.getShowId());
            return convertView;
        }

        class Holder {
            ImageView ivAvatar;
            TextView tvName;
            TextView tvContent;
            TextView tvTime;
            ImageView ivIsMark;
        }
    }

    /** 显示头像*/
    public void setHeaderImage(ImageView mIvAvatar, String mUserId){
        HeaderParam mHeaderParam = LauchrConst.getHeader(this);
        String mUserCompany = mHeaderParam.getCompanyCode();
        String mHeadPicUrl = LauchrConst.ATTACH_PATH +"/Base-Module/Annex/Avatar?width=60&height=60&companyCode="
                + mUserCompany + "&userName=" + mUserId;
        CacheManager.getInstance(this).loadBitmap(mHeadPicUrl, mIvAvatar);
    }
}
