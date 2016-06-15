package com.mintcode.launchr.app.newApproval;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.app.newApproval.view.FormViewUtil;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.entity.ApproveEntity;
import com.mintcode.launchr.pojo.response.ApproveSearchResponse;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TimeFormatUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/4/21.
 */
public class ApproveSearchAdapter extends BaseExpandableListAdapter {

    private List<ApproveEntity> mData;
    private List<ApproveEntity> mApproveEntity;
    private List<ApproveEntity> mUserEntity;
    private String mSearchKey;

    private Context context;

    public static final int TYPE_APPLY = 0;
    public static final int TYPE_APPROVE = 1;
    private int[] mTextState;
    private String[] mTextStateStr;
    private String mLoginName;
    private LayoutInflater mInflater;

    public ApproveSearchAdapter(Context context, List<ApproveEntity> data,
                         String searchKey) {
        mData = data;
        mSearchKey = searchKey;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mLoginName = AppUtil.getInstance(context).getLoginName();

        mTextState = new int[] { R.drawable.text_state_color_red,
                R.drawable.text_state_color_green,
                R.drawable.text_state_color_orange,
                R.drawable.text_state_color_blue,
                R.drawable.text_state_color_gray };

        mTextStateStr = new String[] {
                context.getResources().getString(R.string.accpect_refuse),
                context.getResources().getString(R.string.accpect_pass),
                context.getResources().getString(R.string.accpect_recall),
                context.getResources().getString(R.string.accpect_accpecting),
                context.getResources().getString(R.string.accpect_unaccpect) };
        classifyApproveEntitys(data);
    }


   private  void classifyApproveEntitys(List<ApproveEntity> data){
        mApproveEntity = new ArrayList<>();
        mUserEntity = new ArrayList<>();
       String key = mSearchKey.toLowerCase();
       for(ApproveEntity entity : data){
           String approveName = entity.getA_APPROVE_NAME();
           String approveNamePath = entity.getA_APPROVE_PATH_NAME();

           if( approveName != null && approveName.toLowerCase().contains(key)){
                   mUserEntity.add(entity);
           }else if(approveNamePath != null && approveNamePath.toLowerCase().contains(key)) {
               mUserEntity.add(entity);
           }else{
               mApproveEntity.add(entity);
           }
       }
   }


    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 0) {
            return mApproveEntity == null? 0: mApproveEntity.size();
        }else{
            return mUserEntity == null?0:mUserEntity.size();
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition == 0) {
            return mApproveEntity == null? 0: mApproveEntity.get(childPosition);
        }else{
            return mUserEntity == null?0:mUserEntity.get(childPosition);
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (groupPosition == 0) {
            convertView = mInflater.inflate(R.layout.head_search1, null);
        } else {
            convertView = mInflater.inflate(R.layout.head_search2, null);
        }
        TextView tvKey = (TextView) convertView.findViewById(R.id.tv_key);

        if(groupPosition == 0){
            String key = context.getResources().getString(R.string.task_include_filed_approval, mSearchKey);
            tvKey.setText(Html.fromHtml(key));
        }else if(groupPosition == 1){
            tvKey.setText(mSearchKey);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        HolderView mHolderView;
        if (convertView == null) {
            mHolderView = new HolderView();
            convertView = getLayoutView(parent, mHolderView, convertView);
        } else {
            mHolderView = (HolderView) convertView.getTag();
        }
        ApproveEntity data = (ApproveEntity)getChild(groupPosition, childPosition);
        mHolderView.personName.setText(data.getCREATE_USER_NAME());
        mHolderView.promiseContent.setText(data.getA_TITLE());

        //发布时间
        mHolderView.time.setText(TimeFormatUtil.getTimeForSearch(data.getCREAT_TIME(), context));
        //是否有未读
        mHolderView.unread.setVisibility(View.GONE);
        mHolderView.speach.setVisibility(View.GONE);
        //是否有评论
        if(data.getHAS_COMMENT() == 0){
            mHolderView.speach.setVisibility(View.GONE);
        }else{
            mHolderView.speach.setVisibility(View.VISIBLE);
        }
        //审批的状态
        mHolderView.approvalContentStatus.setBackgroundResource(mTextState[FormViewUtil.getApprovalStatus(data.getA_STATUS().trim())]);
        mHolderView.approvalContentStatus.setText(mTextStateStr[FormViewUtil.getApprovalStatus(data.getA_STATUS().trim())]);
        //审批类别(请假/报销)
        mHolderView.approvalKind.setText(data.getT_NAME());
        //审批类型（in/out/cc抄送）
        mHolderView.approvalType.setText(data.getT_GET_TYPE());
        mHolderView.approvalType.setVisibility(View.GONE);
        //是否至急审批
        if (data.getA_IS_URGENT() == 1) {
            mHolderView.limiteTime.setText(R.string.accpect_state);
            mHolderView.limiteTime.setVisibility(View.VISIBLE);
        } else {
            mHolderView.limiteTime.setVisibility(View.GONE);
        }
        //是否有附件
        if(data.getHAS_FILE() == 1){
            mHolderView.attachment.setVisibility(View.VISIBLE);
        }else{
            mHolderView.attachment.setVisibility(View.GONE);
        }
        // 获取头像
        HeadImageUtil.getInstance(context).setAvatarResourceAppendUrl(mHolderView.headImage,data.getCREATE_USER(),0,80,80);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
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

    private  View getLayoutView(ViewGroup parent, HolderView mHolderView,
                                View convertView){
        convertView = mInflater.inflate(
                R.layout.item_promise_content, parent, false);
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
        return convertView;
    }

    public void updateData(List<ApproveEntity> data,String searchKey) {
        mSearchKey = searchKey;
        classifyApproveEntitys(data);
        notifyDataSetChanged();
    }
}
