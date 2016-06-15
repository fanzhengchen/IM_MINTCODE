package com.mintcode.launchr.app.newApproval.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mintcode.launchr.R;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.entity.ApproveEntity;
import com.mintcode.launchr.pojo.entity.MessageEntity;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.GlideRoundTransform;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TimeFormatUtil;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by JulyYu on 2016/1/15.
 */
public class ApprovalItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<ApproveEntity> mData;
    private List<MessageEntity> mMessage;
    private int mTextState[];
    private String mTextStateStr[] ;

    public ApprovalItemAdapter(Context context,List<ApproveEntity> data,List<MessageEntity> message) {
        this.mContext = context;
        mData = data;
        mMessage = message;
        mTextState	= new int[] {    R.drawable.text_state_color_red,
                R.drawable.text_state_color_green,
                R.drawable.text_state_color_orange,
                R.drawable.text_state_color_blue,
                R.drawable.text_state_color_gray,
        };
        mTextStateStr   = new String[]{
                mContext.getResources().getString(R.string.accpect_refuse),
                mContext.getResources().getString(R.string.accpect_pass),
                mContext.getResources().getString(R.string.accpect_recall),
                mContext.getResources().getString(R.string.accpect_accpecting),
                mContext.getResources().getString(R.string.accpect_unaccpect)
        };
    }
    public void setData( List<ApproveEntity> data,List<MessageEntity> message){
        mData = data;
        mMessage = message;
    }
    public void setDataAndUpdate( List<ApproveEntity> data){
        mData = data;
        notifyDataSetChanged();
    }
    public void setMessage(List<MessageEntity> message){
        mMessage = message;
    }
    public ApprovalItemAdapter(Context context) {
        this.mContext = context;
    //审批状态颜色
        mTextState	= new int[] {    R.drawable.text_state_color_red,
                R.drawable.text_state_color_green,
                R.drawable.text_state_color_orange,
                R.drawable.text_state_color_blue,
                R.drawable.text_state_color_gray,
        };
        //审批状态字
        mTextStateStr   = new String[]{
                mContext.getResources().getString(R.string.accpect_refuse),
                mContext.getResources().getString(R.string.accpect_pass),
                mContext.getResources().getString(R.string.accpect_recall),
                mContext.getResources().getString(R.string.accpect_accpecting),
                mContext.getResources().getString(R.string.accpect_unaccpect)
        };
    }

    @Override
    public int getCount() {

        if(mData == null){
            return 0;
        }
        if(mData.size() < 0){
            return 0;
        }
        return mData.size();


    }

    @Override
    public Object getItem(int position) {
        if(mData == null){
            return null;
        }
        if(mData.size() < 0){
            return null;
        }
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView  mHolderView;
        if (convertView == null) {
              mHolderView = new HolderView();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_promise_content, null);
            mHolderView.layout = (RelativeLayout) convertView.findViewById(R.id.ll_promise_item);
            mHolderView.headImage = (ImageView) convertView.findViewById(R.id.iv_promise_person_head);
            mHolderView.personName = (TextView) convertView.findViewById(R.id.tv_promise_person_name);
            mHolderView.promiseContent = (TextView) convertView.findViewById(R.id.tv_promise_content);
            mHolderView.limiteTime = (TextView) convertView.findViewById(R.id.tv_promise_limit_time);
            mHolderView.time = (TextView) convertView.findViewById(R.id.tv_promise_time);
            mHolderView.attachment = (ImageView) convertView.findViewById(R.id.iv_promise_attachment);
            mHolderView.speach = (ImageView) convertView.findViewById(R.id.iv_promise_speach);
            mHolderView.unread = (ImageView) convertView.findViewById(R.id.iv_unread_target);
            mHolderView.approvalContentStatus = (TextView) convertView.findViewById(R.id.tv_approval_content_state);
            mHolderView.approvalKind = (TextView) convertView.findViewById(R.id.tv_item_approval_type);
            mHolderView.approvalType = (TextView) convertView.findViewById(R.id.tv_item_approval_statut);
            convertView.setTag(mHolderView);
        } else {
            mHolderView = (HolderView) convertView.getTag();
        }
        mHolderView.limiteTime.setVisibility(View.INVISIBLE);
        ApproveEntity data = mData.get(position);
        mHolderView.personName.setText(data.getCREATE_USER_NAME());
        mHolderView.promiseContent.setText(data.getA_TITLE());


        //发布时间
        mHolderView.time.setText(TimeFormatUtil.getTimeForSearch(data.getCREAT_TIME(), mContext));
        mHolderView.unread.setVisibility(View.GONE);
        mHolderView.speach.setVisibility(View.GONE);
        //是否有评论
        if(mData.get(position).getHAS_COMMENT() == 0){
            mHolderView.speach.setVisibility(View.GONE);
        }else{
            mHolderView.speach.setVisibility(View.VISIBLE);
            mHolderView.speach.setImageResource(R.drawable.icon_speach);
        }
        //是否有未读
        if(mMessage != null && mMessage.size() > 0){
            for(int j = 0 ;j < mMessage.size();j++){
                if(mData.get(position).getSHOW_ID().equals(mMessage.get(j).getRmShowID())){
                   if(mMessage.get(j).getType() == 0) {
                       mHolderView.speach.setImageResource(R.drawable.icon_unspeach);
                   }else{
                       mHolderView.unread.setVisibility(View.VISIBLE);
                   }
            }
         }
        }
        //审批的状态
        int status = getApprovalStatus(data.getA_STATUS());
        int backresource = mTextState[status];
        String text = mTextStateStr[status];
        mHolderView.approvalContentStatus.setBackgroundResource(backresource);
        mHolderView.approvalContentStatus.setText(text);
        //审批类别（接收/发送/抄送）
        mHolderView.approvalKind.setText(data.getT_NAME());
        //审批类型
        mHolderView.approvalType.setText(data.getT_GET_TYPE());

        //是否至急审批
        if (data.getA_IS_URGENT() == 1) {
            mHolderView.limiteTime.setText(R.string.accpect_state);
            mHolderView.limiteTime.setVisibility(View.VISIBLE);
        } else {
            if (data.getA_DEADLINE() != 0
                    && String.valueOf(data.getA_DEADLINE()) != null) {
                //最后期限时间
                SimpleDateFormat time = new SimpleDateFormat("MM/dd(E)HH:mm",Const.getLocale());
                SimpleDateFormat timeAll = new SimpleDateFormat("MM/dd(E)",Const.getLocale());
                String dateTime ;
                if(data.getIS_DEADLINE_ALL_DAY() == 1){
                    dateTime  = timeAll.format(data.getA_DEADLINE());
                }else{
                    dateTime  = time.format(data.getA_DEADLINE());
                }
                mHolderView.limiteTime.setText(dateTime);
                mHolderView.limiteTime.setVisibility(View.VISIBLE);
            }
        }
        //是否有附件
        if(data.getHAS_FILE() == 1){
            mHolderView.attachment.setVisibility(View.VISIBLE);
        }else{
            mHolderView.attachment.setVisibility(View.GONE);
        }

        // 获取头像
        String userName =  data.getCREATE_USER();
        HeadImageUtil.getInstance(mContext).setAvatarResourceAppendUrl(mHolderView.headImage, userName, 2, 60, 60);

        return convertView;
    }

    private int getApprovalStatus(String text){
        if(text != null ){
            if(text.equals("APPROVE")){
                return 1;
            }
            if(text.equals("WAITING")){
                return 4;
            }
            if(text.equals("IN_PROGRESS")){
                return 3;
            }
            if(text.equals("DENY")){
                return 0;
            }
            if(text.equals("CALL_BACK")){
                return 2;
            }
        }
        return 0;
    }
    public class HolderView {
        RelativeLayout layout;
        ImageView headImage;
        TextView personName;
        TextView promiseContent;
        TextView limiteTime;
        TextView time;
        ImageView attachment;
        ImageView speach;
        ImageView unread;
        TextView approvalType;
        TextView approvalKind;
        TextView approvalContentStatus;

    }

}



