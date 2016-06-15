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
import com.mintcode.launchr.pojo.entity.FriendEntity;
import com.mintcode.launchr.pojo.entity.PersonEntity;
import com.mintcode.launchr.util.HeadImageUtil;

import java.util.List;


/**
 * Created by JulyYu on 2016/3/24.
 */
public class FriendAdapter extends BaseAdapter{

    private Context mContext;
    private List<FriendEntity> mPersonList;
    private String mStrAdd;
    private String mStrMessage;

    public  FriendAdapter(Context context){
        mContext = context;
        mStrAdd = mContext.getResources().getString(R.string.my_friend_add);
        mStrMessage = mContext.getResources().getString(R.string.contacts_person_detail);
    }



    public void setData( List<FriendEntity> list){
        mPersonList = list;
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
        if(convertView == null){
            view = new HoderView();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_person,null);
            view.ivHead = (ImageView)convertView.findViewById(R.id.iv_icon);
            view.tvName = (TextView)convertView.findViewById(R.id.tv_name);
            view.tvNumber = (TextView)convertView.findViewById(R.id.tv_number);
            view.tvAdd = (TextView)convertView.findViewById(R.id.tv_add);
            view.tvAdd.setVisibility(View.GONE);
            view.tvNumber.setVisibility(View.GONE);
            convertView.setTag(view);
        }else{
            view = (HoderView)convertView.getTag();
        }
        FriendEntity entity = mPersonList.get(position);

        // 设置姓名
        view.tvName.setText(entity.getNickName());
        // 设置头像
        String name = entity.getRelationName();
        if(TextUtils.isEmpty(name)){
            name = entity.getUserName();
        }
        String headUrl = entity.getRelationAvatar();
        HeadImageUtil.getInstance(mContext).setAvatarResource(view.ivHead,name,headUrl,0,60,60);
        return convertView;
    }

    class HoderView{
        ImageView ivHead;
        TextView tvName;
        TextView tvNumber;
        TextView tvAdd;
    }


}
