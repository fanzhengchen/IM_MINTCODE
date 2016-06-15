package com.mintcode.launchr.app.commonAdapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mintcode.launchr.R;
import com.mintcode.launchr.app.newTask.TaskUtil;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.ApproveCommentEntity;
import com.mintcode.launchr.pojo.entity.rmDataEntity;
import com.mintcode.launchr.pojo.entity.rmMeetingData;
import com.mintcode.launchr.pojo.entity.rmTaskData;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.GlideRoundTransform;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.TimeFormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/4/15.
 */
public class MessageAdapter extends BaseAdapter {


    /** 评论列表*/
    List<ApproveCommentEntity> commentList;

    /** 评论全部列表*/
    List<ApproveCommentEntity> mCommentAllList;
    /***评论部分**/
    List<ApproveCommentEntity> mCommentSpeachList;
    /***附件部分**/
    List<ApproveCommentEntity> mCommentAttamentList;
    /***操作部分**/
    List<ApproveCommentEntity> mCommentOpearateList;
    private Context mContext;

    private  int colotGrayLaunchr;
    private int  colorBlack;
    private int colorBlueLaunchr;

    private String mStrAppType;

    public  MessageAdapter(Context context){
        mContext = context;
        colotGrayLaunchr = mContext.getResources().getColor(R.color.gray_launchr);
        colorBlack = mContext.getResources().getColor(R.color.black);
        colorBlueLaunchr = mContext.getResources().getColor(R.color.blue_launchr);
    }

    public  MessageAdapter(Context context, List<ApproveCommentEntity> list){
        mContext = context;
        mCommentAllList = list;
        colotGrayLaunchr = mContext.getResources().getColor(R.color.gray_launchr);
        colorBlack = mContext.getResources().getColor(R.color.black);
        colorBlueLaunchr = mContext.getResources().getColor(R.color.blue_launchr);
    }


    public void setCommentData(List<ApproveCommentEntity> commentList,String AppType){
        mCommentAllList = commentList;
        mStrAppType = AppType;
        CommentListClassification();
    }
    public List<ApproveCommentEntity> getMessageList(){
        return  mCommentAllList;
    }
    public void selectCommentShow(int position){
        switch (position){
            case 0:
                commentList = mCommentAllList;
                this.notifyDataSetChanged();
                break;
            case 1:
                commentList = mCommentSpeachList;
                this.notifyDataSetChanged();
                break;
            case 2:
                commentList = mCommentAttamentList;
                this.notifyDataSetChanged();
                break;
            case 3:
                commentList = mCommentOpearateList;
                this.notifyDataSetChanged();
                break;
            default:
                break;
        }

    }

    private void CommentListClassification(){
        mCommentSpeachList = new ArrayList<>();
        mCommentAttamentList = new ArrayList<>();
        mCommentOpearateList = new ArrayList<>();
        for(ApproveCommentEntity approveCommentEntity : mCommentAllList){
            if(approveCommentEntity.getTransType() != null && !"".equals(approveCommentEntity.getTransType())){
                mCommentOpearateList.add(approveCommentEntity);
            }else if(approveCommentEntity.getFilePath() != null && !"".equals(approveCommentEntity.getFilePath())){
                mCommentAttamentList.add(approveCommentEntity);
            }else{
                mCommentSpeachList.add(approveCommentEntity);
            }
        }
        commentList = mCommentAllList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return commentList == null ? 0:commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return commentList.size() - position - 1;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        HolderView mHolderView;
        if (convertview == null) {
            convertview = LayoutInflater.from(mContext).inflate(R.layout.item_speach_message, parent, false);
            mHolderView = new HolderView();
            mHolderView.headPic = (ImageView) convertview.findViewById(R.id.iv_item_message_head);
            mHolderView.name = (TextView) convertview.findViewById(R.id.item_message_name);
            mHolderView.message = (TextView) convertview.findViewById(R.id.item_message_content);
            mHolderView.time = (TextView) convertview.findViewById(R.id.item_message_time);
            mHolderView.pic = (ImageView)convertview.findViewById(R.id.item_message_pic_content);
            mHolderView.loadingPic = (ProgressBar)convertview.findViewById(R.id.item_pic_loading);
            convertview.setTag(mHolderView);
        } else {
            mHolderView = (HolderView) convertview.getTag();
        }
        // ///////////////////
        int reversePosition = commentList.size() - position - 1;
        if(commentList != null){
            String headUrl =  commentList.get(reversePosition).getCreatUser();
            setAvatarAndName(mHolderView,headUrl);
            if(commentList.get(reversePosition).getIsComment() == 1){//是评论
                mHolderView.message.setTextColor(colorBlack);
                if(commentList.get(reversePosition).getFilePath() == null || commentList.get(reversePosition).getFilePath().equals("")){//文字评论
                    mHolderView.message.setVisibility(View.VISIBLE);
                    mHolderView.pic.setVisibility(View.GONE);
                    mHolderView.name.setText(commentList.get(reversePosition).getCreatUserName());
                    //评论已删除显示提示
                    if(commentList.get(reversePosition).getIsDeleted() == 1){
                        mHolderView.message.setText(mContext.getString(R.string.message_is_delect));
                    }else{
                        ApproveCommentEntity entity = commentList.get(reversePosition);
                        if(entity.getTransType()!=null){//操作信息内容
                            mHolderView.message.setText(entity.getContent());
                        }else{//评论内容
                            mHolderView.message.setText(commentList.get(reversePosition).getContent());
                        }

                    }

                    mHolderView.time.setText(TimeFormatUtil.getSessionTime(commentList.get(reversePosition).getCreateTime(),mContext));

                }else{//图片评论
                    mHolderView.message.setVisibility(View.GONE);
                    mHolderView.pic.setVisibility(View.VISIBLE);
                    mHolderView.pic.setImageResource(R.drawable.icon_launchr);
                    mHolderView.name.setText(commentList.get(reversePosition).getCreatUserName());
                    //照片已删除提示
                    if(commentList.get(reversePosition).getIsDeleted() == 1){
                        mHolderView.message.setText(mContext.getString(R.string.message_pic_is_delect));
                        mHolderView.message.setVisibility(View.VISIBLE);
                        mHolderView.pic.setVisibility(View.GONE);
                    }else{
                        String picUrl = commentList.get(reversePosition).getFilePath();
                        HeadImageUtil.getInstance(mContext).setImageResuces(mHolderView.pic, picUrl);
                    }
                    mHolderView.time.setText(TimeFormatUtil.getSessionTime(commentList.get(reversePosition).getCreateTime(),mContext));
                }

            }else{//操作
                mHolderView.message.setTextColor(colorBlueLaunchr);
                ApproveCommentEntity entity = commentList.get(reversePosition);
                mHolderView.message.setVisibility(View.VISIBLE);
                mHolderView.loadingPic.setVisibility(View.GONE);
                mHolderView.pic.setVisibility(View.GONE);
                mHolderView.name.setText(commentList.get(reversePosition).getCreatUserName());
                String data = entity.getRmData();
                if(data != null){
                    if(mStrAppType.equals(Const.SHOWID_APPROVE)){
                        rmDataEntity mDataEntity = TTJSONUtil.convertJsonToCommonObj(data, rmDataEntity.class);
                        if(mDataEntity != null){
                            mHolderView.message.setText( setCommentMsg(entity.getTransType(),mDataEntity));
                        }
                    }else if(mStrAppType.equals(Const.SHOWID_TASK)){
                        rmTaskData mDataEntity = TTJSONUtil.convertJsonToCommonObj(data,rmTaskData.class);
                        if(mDataEntity != null){
                            mHolderView.message.setText( setTaskCommentMsg(entity.getTransType(), mDataEntity.getTitle()));
                        }
                    }else if(mStrAppType.equals(Const.SHOW_SCHEDULE)){
                        rmMeetingData mDataEntity = TTJSONUtil.convertJsonToCommonObj(data,rmMeetingData.class);
                        if(mDataEntity != null){
                            mHolderView.message.setText( setCommentMsg(entity.getTransType(), entity.getCreatUserName(),mDataEntity.getTitle(), mDataEntity));
                        }
                    }
                    mHolderView.time.setText(TimeFormatUtil.getSessionTime(commentList.get(reversePosition).getCreateTime(),mContext));
                }
            }
        }
        return convertview;
    }

    public class HolderView {
        TextView name;
        TextView message;
        TextView time;
        ImageView pic;
        ProgressBar loadingPic;
        ImageView headPic;
    }


    private void setAvatarAndName(MessageAdapter.HolderView holder, String  item) {

        HeaderParam mHeaderParam = LauchrConst.getHeader(mContext);
        String mUserCompany = mHeaderParam.getCompanyCode();
        String mHeadPicUrl = LauchrConst.ATTACH_PATH +"/Base-Module/Annex/Avatar?width=60&height=60&companyCode="
                + mUserCompany + "&userName=" + item;
        // 处理圆形图片
        RequestManager man = Glide.with(mContext);
        GlideRoundTransform g =  new GlideRoundTransform(mContext,2);
        man.load(mHeadPicUrl).transform(g).into(holder.headPic);
    }
    /** 任务评论*/
    private String setTaskCommentMsg(String type, String content){
        String msgContent = null;
        if(type.equals("createTaskV2")){
            msgContent = mContext.getString(R.string.task_create_v2);
        }else if(type.equals("taskV2ChangeStatus")){
            msgContent = mContext.getString(R.string.task_message_edit);
            msgContent = msgContent.replace("^@", "");
            msgContent = msgContent.replace("^#", content);
        }else if(type.equals("taskV2Update")){
            msgContent = mContext.getString(R.string.task_message_edit_define);
            msgContent = msgContent.replace("^@", "");
            msgContent = msgContent.replace("^#", content);
        }
        return msgContent;
    }

    /** 审批评论*/
    private SpannableStringBuilder setCommentMsg(String type,rmDataEntity data){
        String msgContent = null;
        if(type.equals("approvePass")){//通过审批
            msgContent = mContext.getString(R.string.approve_passit);
            msgContent = msgContent.replace("^#", data.getTitle());
            return getHighlightText(msgContent, data.getTitle());
        }else if(type.equals("approveBackDefinite")){//打回审批待消息
            msgContent = mContext.getString(R.string.approve_back_define_reason);
            msgContent = msgContent.replace("^#", data.getTitle()+"");
            msgContent = msgContent.replace("^%", data.getReason()+"");
            return getHighlightText(msgContent, data.getTitle()+"",data.getReason()+"");
        }else if(type.equals("approveBack")){//打回审批
            msgContent = mContext.getString(R.string.approve_back_define);
            msgContent = msgContent.replace("^#", data.getTitle());
            msgContent = msgContent.replace("^%", data.getReason());
            return getHighlightText(msgContent, data.getTitle()+"",data.getReason()+"");
        }else if(type.equals("approveRefuseDefinite")){//拒绝审批待消息
            msgContent = mContext.getString(R.string.approve_refuse_define_reason);
            msgContent = msgContent.replace("^#", data.getTitle()+"");
            msgContent = msgContent.replace("^%", data.getReason()+"");
            return getHighlightText(msgContent, data.getTitle()+"",data.getReason()+"");
        }else if(type.equals("approveRefuse")){//拒绝审批
            msgContent = mContext.getString(R.string.approval_not_agree);
            msgContent = msgContent.replace("^#", data.getTitle());
            return getHighlightText(msgContent, data.getTitle());
        }else if(type.equals("approveTranspondDefinite")){//转交审批待消息
            msgContent = mContext.getString(R.string.approve_transpond_reason);
            msgContent = msgContent.replace("^#", data.getContent()+"");
            msgContent = msgContent.replace("^%", data.getReason()+"");
            return getHighlightText(msgContent, data.getContent()+"",data.getReason()+"");
        }else if(type.equals("approveTranspond")){//转交审批
            msgContent = mContext.getString(R.string.approve_transpond);
            msgContent = msgContent.replace("^#", data.getContent()+"");
            msgContent = msgContent.replace("^%", data.getReason()+"");
            return getHighlightText(msgContent, data.getContent()+"",data.getReason()+"");
        }else if(type.equals("approvePut")){//发起审批
            msgContent = mContext.getString(R.string.approve_put);
        } else if(type.equals("approvePost")){//编辑审批
            msgContent = mContext.getString(R.string.approve_post);
        }else if(type.equals("approve")){
            msgContent = mContext.getString(R.string.approval_promise);
        }else if(type.equals("approveBackV2")){
            msgContent = mContext.getString(R.string.approvel_back);
        }
        return new SpannableStringBuilder(msgContent+"");
    }
    /** 会议操作*/
    private SpannableStringBuilder setCommentMsg(String type, String userName, String title, rmMeetingData data){
        String msgContent = null;
        if(type.equals("meetingAttend")){//参加
            msgContent = mContext.getString(R.string.meeting_message_attend);
            msgContent = msgContent.replace("^@", "");
            return getHighlightText(msgContent, userName);
        }else if(type.equals("meetingAttendDefinite")){//参加带标题
            msgContent = mContext.getString(R.string.meeting_message_attend_define);
            msgContent = msgContent.replace("^@", "");
            msgContent = msgContent.replace("^#", title);
            return getHighlightText(msgContent, userName+"",title+"");
        }else if(type.equals("meetingRefuseAttend")){//拒绝参加
            msgContent = mContext.getString(R.string.meeting_message_refuse_attend);
            msgContent = msgContent.replace("^@", "");
            msgContent = msgContent.replace("^#", title);
            return getHighlightText(msgContent, userName+"",title+"");
        }else if(type.equals("meetingRefuseAttendDefinite")){//拒绝参加带标题
            msgContent = mContext.getString(R.string.meeting_message_refuse_attend_define);
            msgContent = msgContent.replace("^@", "");
            msgContent = msgContent.replace("^#", title);
            return getHighlightText(msgContent, userName+"", title+"");
        }else if(type.equals("meeting")){
            msgContent = mContext.getString(R.string.app_meetting);
        }else if(type.equals("meetingCancelOne")){
            msgContent = mContext.getString(R.string.meeting_cancel_one);
        }else if(type.equals("meetingEdit")){
            msgContent = mContext.getString(R.string.meeting_edit_content);
        } else if (type.equals("meetingCancel")){
            msgContent = mContext.getString(R.string.meeting_cancel);
            msgContent = msgContent.replace("^@", data.getContent());
            return getHighlightText(msgContent, data.getReason());
        }
        return new SpannableStringBuilder(msgContent + "");
    }

    private SpannableStringBuilder getHighlightText(String context,String high,String high2){
        SpannableStringBuilder ssb = new SpannableStringBuilder(context);
        int index = getIndex(context, high);
        if(index > 0){
            ssb.setSpan(new ForegroundColorSpan(colorBlack),
                    index,index+high.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        index = getIndex(context, high2);
        if(index > 0){
            ssb.setSpan(new ForegroundColorSpan(colorBlack),
                    index,index+high2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssb;
    }

    private SpannableStringBuilder getHighlightText(String context,String high){
        SpannableStringBuilder ssb = new SpannableStringBuilder(context);
        int index = getIndex(context, high);
        if(index > 0){
            ssb.setSpan(colorBlack,
                    index,index+high.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssb;
    }
    //高亮字符串的索引
    private int getIndex(String content,String high) {
        int start=0;
        int index;
        String contentNoCase = content.toLowerCase();
        String serchWordNoCase = high.toLowerCase();
        index = contentNoCase.indexOf(serchWordNoCase, start);
        return index;
    }
}
