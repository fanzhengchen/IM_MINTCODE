package com.mintcode.launchr.contact.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.chat.activity.ChatActivity;
import com.mintcode.chat.entity.User;
import com.mintcode.launchr.R;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.contact.activity.FriendVerifyActivity;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.PersonEntity;
import com.mintcode.launchr.util.HeadImageUtil;

import java.util.List;

/**
 * Created by JulyYu on 2016/4/5.
 */
public class PersonAdapter extends BaseAdapter {

    private Context mContext;
    private List<PersonEntity> mPersonList;
    /** 是否显示添加选择*/
    private boolean mBoolShow = true;
    /** 搜索设置选择 好友为true 陌生人为false*/
    private boolean mBoolSearchFriend = true;
    private String mStrAdd;
    private String mStrMessage;

    public  PersonAdapter(Context context){
        mContext = context;
        mStrAdd = mContext.getResources().getString(R.string.my_friend_add);
        mStrMessage = mContext.getResources().getString(R.string.contacts_person_detail);
    }

    public  PersonAdapter(Context context,boolean show){
        mContext = context;
        mBoolShow = show;
        mStrAdd = mContext.getResources().getString(R.string.my_friend_add);
        mStrMessage = mContext.getResources().getString(R.string.contacts_person_detail);
    }


    public void setData( List<PersonEntity> list){
        mPersonList = list;
        notifyDataSetChanged();
    }

    public void setData( List<PersonEntity> list,boolean friend){
        mPersonList = list;
        mBoolSearchFriend = friend;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPersonList == null?0:mPersonList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPersonList == null?null:mPersonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoderView view;
        AddClickListener clickListener;
        ChatClickListener chatClickListener;
        if(convertView == null){
            view = new HoderView();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_person,null);
            view.ivHead = (ImageView)convertView.findViewById(R.id.iv_icon);
            view.tvName = (TextView)convertView.findViewById(R.id.tv_name);
            view.tvNumber = (TextView)convertView.findViewById(R.id.tv_number);
            view.tvAdd = (TextView)convertView.findViewById(R.id.tv_add);
            convertView.setTag(view);
        }else{
            view = (HoderView)convertView.getTag();
        }
        PersonEntity entity = mPersonList.get(position);

        // 设置姓名
        view.tvName.setText(entity.getNickName());
        // 设置头像
        String name = entity.getRelationName();
        if(TextUtils.isEmpty(name)){
            name = entity.getUserName();
        }
        String headUrl = entity.getAvatar();
        HeadImageUtil.getInstance(mContext).setAvatarResource(view.ivHead,name,headUrl,0,60,60);
        //是否已经添加
        if(mBoolShow){
            // 设置电话
            view.tvNumber.setText(entity.getMobile());
            // 设置添加按钮
            if(mBoolSearchFriend){
                chatClickListener = new ChatClickListener(entity);
                view.tvAdd.setOnClickListener(chatClickListener);
                view.tvAdd.setText(mStrMessage);
            }else{
                clickListener = new AddClickListener(entity);
                view.tvAdd.setOnClickListener(clickListener);
                boolean relation = entity.isRalation();
                // 是否为好友关系
                if(relation){
                    view.tvAdd.setText(mStrMessage);
                }else{
                    view.tvAdd.setText(mStrAdd);
                }
            }
        }else{
            view.tvAdd.setVisibility(View.GONE);
            view.tvNumber.setVisibility(View.GONE);
        }
        return convertView;
    }

    class HoderView{
        ImageView ivHead;
        TextView tvName;
        TextView tvNumber;
        TextView tvAdd;
    }

    class AddClickListener implements View.OnClickListener{

        private  PersonEntity mEntity;

        public AddClickListener(PersonEntity entity ){
            mEntity = entity;
        }

        @Override
        public void onClick(View v) {
            if(mEntity.isRalation()){
                Intent intent = new Intent(mContext, ChatActivity.class);
                User user = new User(mEntity.getNickName(), mEntity.getUserName());
                intent.putExtra("user", user);
                mContext.startActivity(intent);

            }else{
                Intent verify = new Intent(mContext, FriendVerifyActivity.class);
                verify.putExtra(FriendVerifyActivity.PERSON_DETAIL,mEntity);
                mContext.startActivity(verify);
            }

        }
    }

    class ChatClickListener implements View.OnClickListener{

        private  PersonEntity mEntity;

        public ChatClickListener(PersonEntity entity ){
            mEntity = entity;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ChatActivity.class);
            User user = new User(mEntity.getNickName(), mEntity.getUserName());
            intent.putExtra("user", user);
            mContext.startActivity(intent);
        }
    }

}
