package com.mintcode.launchr.app;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.ApproveAPI;
import com.mintcode.launchr.app.commonAdapter.MessageAdapter;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.photo.activity.GalleryActivity;
import com.mintcode.launchr.pojo.ApproveCommentListPOJO;
import com.mintcode.launchr.pojo.DelectCommentPOJO;
import com.mintcode.launchr.pojo.entity.ApproveCommentEntity;
import com.mintcode.launchr.util.DialogHintUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.view.NoscrollListView;
import com.mintcode.launchr.view.ViewPagerIndicator;
import com.mintcode.launchrnetwork.OnResponseListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/4/22.
 */
public class CommentsListView extends LinearLayout implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener
        ,ViewPagerIndicator.MessagePageChangeListener,OnResponseListener{


    private Context mContext;
    /*** 评论列表指示**/
    private ViewPagerIndicator mViewPagerIndicator;
    private MessageAdapter mMessageAdapter;
    private NoscrollListView mLvCommentList;

    private String mDelectCommentId;
    private String mStrAppKey;
    private String mStrShowId;


    public CommentsListView(Context context) {
        super(context);
        mContext = context;
        initView();
    }
    public CommentsListView(Context context,String appKey,String showId) {
        super(context);
        mContext = context;
        mStrAppKey = appKey;
        mStrShowId = showId;
        initView();
    }
    public CommentsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public CommentsListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
    }


    public void setAppInfo(String appKey,String showId){
        mStrAppKey = appKey;
        mStrShowId = showId;
    }

    private void initView() {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_message_list,null);
        mViewPagerIndicator = (ViewPagerIndicator)view.findViewById(R.id.indicator_message_select);
        mLvCommentList = (NoscrollListView)view.findViewById(R.id.lv_approval_speach_list);

        mLvCommentList.setOnItemLongClickListener(this);
        mLvCommentList.setOnItemClickListener(this);
        mMessageAdapter = new MessageAdapter(mContext);
        mLvCommentList.setAdapter(mMessageAdapter);

        List<String> mMessageTitle = new ArrayList<String>();
        int [] title = {R.string.approval_message_all,R.string.approval_message_speach,R.string.approval_message_attament,R.string.approval_message_operate};
        for(int num :title){
            mMessageTitle.add(mContext.getString(num));
        }
        mViewPagerIndicator.setVisibleTabCount(4);
        mViewPagerIndicator.setTabItemTitles(mMessageTitle);
        mViewPagerIndicator.setOnPageChangeListener(this);
        addView(view);
    }

    public void setMessage(List<ApproveCommentEntity> commentList){
        mMessageAdapter.setCommentData(commentList,mStrAppKey);
        mViewPagerIndicator.highLightTextView(0);
        mViewPagerIndicator.scroll(0, 0);
        Log.i(" setMessage", "updateMessage");
    }



    public void delectComment(){
        ApproveAPI.getInstance().delectConmment(this, mDelectCommentId);
    }

    public void updateComment(){
        ApproveAPI.getInstance().getCommentList(this, mStrAppKey, mStrShowId, 0, 0, 0);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int mPosition = mMessageAdapter.getCount()  - position - 1;
        List<ApproveCommentEntity> messageList = mMessageAdapter.getMessageList();
        ApproveCommentEntity entity = (ApproveCommentEntity)mMessageAdapter.getItem(mPosition);
        if(entity.getIsDeleted() == 0  && entity.getFilePath() != null && !entity.getFilePath().equals("")){
            // 查看评论图片
            List<ApproveCommentEntity> list = new ArrayList();
            String selectPath =  entity.getFilePath();
            int selectPosition = 0;
            for(int i = 0; i < messageList.size(); i++){
                String filePath = messageList.get(i).getFilePath();
                if(messageList.get(i).getIsDeleted() == 0  && filePath != null && !filePath.equals("")){
                    // 主要是解决内网totoro域名解析不出来问题
                    if(filePath.equals(selectPath)){
                        selectPosition = list.size();
                    }
                    list.add(messageList.get(i));
                }
            }
            Intent intent = new Intent(mContext, GalleryActivity.class);
            intent.putExtra(GalleryActivity.KEY_COMMET_PHOTO_URL, (Serializable) list);
            intent.putExtra(GalleryActivity.KEY_TYPE,GalleryActivity.COMMET_URL);
            intent.putExtra(GalleryActivity.KEY_POSITION, selectPosition);
            intent.putExtra(GalleryActivity.CAN_DELECT_IMAGE, false);
            mContext.startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        int mPosition = mMessageAdapter.getCount()  - position - 1;
        ApproveCommentEntity entity = (ApproveCommentEntity)mMessageAdapter.getItem(mPosition);
        String loginName = LauchrConst.getHeader(mContext).getLoginName();
        String createUser = entity.getCreatUser();
        int isComment =  entity.getIsComment();
        int isDelect = entity.getIsDeleted();
        if(loginName.equals(createUser) && isComment == 1 && isDelect == 0){
            mDelectCommentId = entity.getShowID();
            DialogHintUtil.delectComment(mContext,this);
        }else{
            Toast.makeText(mContext,mContext.getString(R.string.apply_undelect_toast),Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public void onPageSelected(int position) {
        mMessageAdapter.selectCommentShow(position);
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        if (response == null) {
            return;
        }
        //删除评论
        if(taskId.equals(ApproveAPI.TaskId.TASK_URL_DELECT_APPROVE_COMMENT)){
            DelectCommentPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), DelectCommentPOJO.class);
            handleDelectComment(pojo);
        }
        //获取评论
        if(taskId.equals(ApproveAPI.TaskId.TASK_URL_GET_COMMENT_LIST)){
            handleGetCommentList(response);
        }
    }

    @Override
    public boolean isDisable() {
        return false;
    }

    /** 删除评论*/
    private  void handleDelectComment(DelectCommentPOJO pojo){

        if(pojo == null){

        }else if(pojo.getBody() == null){
            Toast.makeText(mContext,pojo.getHeader().getReason(),Toast.LENGTH_SHORT).show();
        }else if(!pojo.getBody().getResponse().isIsSuccess()){
            Toast.makeText(mContext,pojo.getBody().getResponse().getReason(),Toast.LENGTH_SHORT).show();
        }else if(pojo.getBody().getResponse().getData() != null){
            ApproveAPI.getInstance().getCommentList(this,mStrAppKey,mStrShowId, 0, 0, 0);
            Toast.makeText(mContext, mContext.getString(R.string.apply_comment_delect_success_toast), Toast.LENGTH_SHORT).show();
        }
    }
    /** 评论列表*/
    private void handleGetCommentList(Object response){
        ApproveCommentListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), ApproveCommentListPOJO.class);
        if(pojo == null){
            return ;
        }else if(pojo.getBody() == null){
            return ;
        }else if(pojo.getBody().getResponse().isIsSuccess() == false){
            Toast.makeText(mContext,pojo.getBody().getResponse().getReason(),Toast.LENGTH_SHORT).show();
        }else if(pojo.getBody().getResponse().getData() == null){
            return ;
        }else{

            List<ApproveCommentEntity> commentEntities = pojo.getBody().getResponse().getData();
            if( commentEntities != null){
                setMessage(commentEntities);
            }
        }
    }

}









