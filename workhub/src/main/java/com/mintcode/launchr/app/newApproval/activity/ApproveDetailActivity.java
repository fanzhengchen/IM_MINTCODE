package com.mintcode.launchr.app.newApproval.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.ApproveAPI;
import com.mintcode.launchr.api.AttachmentApi;
import com.mintcode.launchr.api.WorkFlowAPI;
import com.mintcode.launchr.app.CommentsListView;
import com.mintcode.launchr.app.CommentsView;
import com.mintcode.launchr.app.meeting.view.HandleMeetingPopWindow;
import com.mintcode.launchr.app.meeting.MeetingDelectDialog;
import com.mintcode.launchr.app.newApproval.view.ApproveHandleLayoutView;
import com.mintcode.launchr.app.newApproval.view.FileShowLayoutView;
import com.mintcode.launchr.app.newApproval.view.FormViewUtil;
import com.mintcode.launchr.app.newApproval.view.TextLinearLayoutView;
import com.mintcode.launchr.app.newApproval.window.AgreePopWindow;
import com.mintcode.launchr.app.newApproval.window.WriteCommentPopWindow;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.contact.activity.DepartmentActivity;
import com.mintcode.launchr.photo.activity.PhotoActivity;
import com.mintcode.launchr.pojo.ApprovalDetailPOJO;
import com.mintcode.launchr.pojo.ApprovePOJO;
import com.mintcode.launchr.pojo.AttachmentListPOJO;
import com.mintcode.launchr.pojo.FormPOJO;
import com.mintcode.launchr.pojo.NormalPOJO;
import com.mintcode.launchr.pojo.entity.ApproveDetailEntity;
import com.mintcode.launchr.pojo.entity.AttachmentListEntity;
import com.mintcode.launchr.pojo.entity.FormEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.pojo.entity.formData;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.DialogHintUtil;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JulyYu on 2016/4/14.
 */
public class ApproveDetailActivity extends BaseActivity implements AgreePopWindow.ItemSelectListener ,WriteCommentPopWindow.startActivityLinsenter ,CommentsView.SendMessageListener {


    /**返回*/
    private ImageView mIvBack;
    /** 至急状态 */
    private TextView mTvState;
    /** 申请状态 */
    private TextView mTvApplyState;
    /** 申请者头像 */
    private ImageView mIvPersonHead;
    /** 申请者姓名 */
    private TextView mTvPersonName;
    /** 申请时间 */
    private TextView mTvCreateTime;

    /** 发送内容*/
    private EditText mEdtMessage;
    /**  操作键 */
    private ImageView mIvMoreDo;
    /** 评论发送页*/
    private LinearLayout mSendMessageView;
    private LinearLayout mCommentListView;

    private LinearLayout mLlEditLinearLayout;
    private LinearLayout mLlPersonLieanrLayout;
    private LinearLayout mLlOtherLineanrLayout;
    private FileShowLayoutView mFileLinearLayout;

    /** 选人转交*/
    private UserDetailEntity mUserEntity;


    private WriteCommentPopWindow mWriteCommentPopWindow;
    private AgreePopWindow mAgreePopWindow;

    private List<ApproveDetailEntity.Person> mApprova;
    private List<ApproveDetailEntity.Person> mCC;
    private String mStrApproveName = "";
    private String mStrApproveNameId;
    private String mStrCcName = "";
    private String mStrCcNameId;

    private List<String> mApproveList;
    private List<String> mApproveNameList;
    private List<String> mCcList;
    private List<String> mCcNameList;
    private List<String> mApprovePathList;
    private List<String> mApprovePathNameList;

    private List<String> mMessagePerson;
    private List<String> mMessagePersonName;
    /** 表单集合的json数据*/
    private HashMap<String,String> mJsonHashMap = new HashMap<>();
    private boolean mBoolUnGetForm = true;

    /** 申请状态颜色*/
    private int mTextState[] = { R.drawable.text_state_color_red,
            R.drawable.text_state_color_green,
            R.drawable.text_state_color_orange,
            R.drawable.text_state_color_blue, R.drawable.text_state_color_gray };
    /**申请状态文字*/
    private String mTextStateStr[];
    private ApproveDetailEntity mApprovalDetail;
    private String mStrApproveShowId;
    /** 审批详情**/
    public static final String APPLY_DETAIL = "apply_detail";
    /** 审批app应用 id**/
    public static final String APPROVE_APP_KEY = Const.SHOWID_APPROVE;
    /** 预览照片*/
    private static final int PREVIEW_PIC = 0x000002;
    /** 选人*/
    private static final int SELECT_USER = 0x000008;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_detail);
        initData();
        initViews();
    }

    private void initData() {

        mTextStateStr = new String[]{
                getResources().getString(R.string.accpect_refuse),
                getResources().getString(R.string.accpect_pass),
                getResources().getString(R.string.accpect_recall),
                getResources().getString(R.string.accpect_accpecting),
                getResources().getString(R.string.accpect_unaccpect)
        };
        Intent getDetail = getIntent();
        mStrApproveShowId = getDetail.getStringExtra(APPLY_DETAIL);
        showLoading();
        ApproveAPI.getInstance().getApproveDetailV2(this, mStrApproveShowId);
    }

    private void initViews() {


        mIvBack = (ImageView)findViewById(R.id.iv_back);
        mIvPersonHead = (ImageView) findViewById(R.id.iv_accpet_detail_person_head);
        mTvPersonName = (TextView) findViewById(R.id.tv_accpet_detail_name);
        mTvCreateTime = (TextView) findViewById(R.id.tv_acppet_detail_taketime);
        mTvState = (TextView) findViewById(R.id.tv_accpet_detail_state);
        mTvApplyState = (TextView) findViewById(R.id.tv_approval_detail_self_state);
        mLlEditLinearLayout = (LinearLayout)findViewById(R.id.ll_text_context);
        mLlPersonLieanrLayout = (LinearLayout)findViewById(R.id.ll_person_context);
        mLlOtherLineanrLayout = (LinearLayout)findViewById(R.id.ll_other_context);

        mEdtMessage = (EditText)findViewById(R.id.edt_approval_sendmessage);
        mIvMoreDo = (ImageView)findViewById(R.id.iv_more_handle);

        mSendMessageView = (LinearLayout)findViewById(R.id.ll_message_send);
        mCommentListView = (LinearLayout)findViewById(R.id.comments_list);


        mIvBack.setOnClickListener(this);
        mIvMoreDo.setOnClickListener(this);
        ((CommentsView)mSendMessageView).setMessageLisener(this);
    }

    public String getApprovPersonStr(){
        return mStrApproveName;
    }
    public List<String> getApprovPerson(){
        return mApproveNameList;
    }

    public String getCCPersonStr(){
        return  mStrCcName;
    }
    public List<String> getCCPerson(){
        return  mCcNameList;
    }
    public  void passApply() {
        String showId = mApprovalDetail.getSHOW_ID();
        mAgreePopWindow = new AgreePopWindow(this,showId,-1);
        mAgreePopWindow.setItemSelectListener(this);
        mAgreePopWindow.show(mSendMessageView);
    }

    public void refuseApply() {
        String showId = mApprovalDetail.getSHOW_ID();
        mWriteCommentPopWindow = new WriteCommentPopWindow(this,WriteCommentPopWindow.TYPE_RUFUSE,showId,0,2);
        mWriteCommentPopWindow.show(mSendMessageView);
    }

    public void reCallApply() {
        String showId = mApprovalDetail.getSHOW_ID();
        mWriteCommentPopWindow = new WriteCommentPopWindow(this,WriteCommentPopWindow.TYPE_RECALL,showId,0,2);
        mWriteCommentPopWindow.show(mSendMessageView);
    }

    public void approvalDeal() {
        toast(getString(R.string.approval_deal));
        setResult(RESULT_OK);
        finish();
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mIvBack){
            this.finish();
        }else if(v == mIvMoreDo){
            doMoreHandle();
        }

    }


    private void doMoreHandle(){
        final HandleMeetingPopWindow mHandleMeetingPopWindow = new HandleMeetingPopWindow(ApproveDetailActivity.this);
        mHandleMeetingPopWindow.showAsDropDown(mIvMoreDo);

        View window = mHandleMeetingPopWindow.getContentView();
        TextView handleEdit = (TextView)window.findViewById(R.id.tv_meeting_edit);
        View view = window.findViewById(R.id.view_line);
        handleEdit.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        TextView handleDelete = (TextView)window.findViewById(R.id.tv_meeting_delete);
        handleDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHintUtil.delectEvent(ApproveDetailActivity.this);
                mHandleMeetingPopWindow.dismiss();
            }
        });

        TextView handleSendOther = (TextView) window.findViewById(R.id.tv_meeting_send);
        handleSendOther.setVisibility(View.GONE);
    }

    public void delect() {
        String showid = mApprovalDetail.getSHOW_ID();
        showLoading();
        ApproveAPI.getInstance().delectApproveV2(this, showid);
    }


    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        dismissLoading();
        if (response == null) {
            showNoNetWork();
            return;
        }
        if (taskId.equals(ApproveAPI.TaskId.TASK_URL_GET_DETAIL_V2)) {
            //获得详情返回，刷新页面
            ApprovalDetailPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), ApprovalDetailPOJO.class);
            handleApprovaleDetail(pojo);
        } if (taskId.equals(ApproveAPI.TaskId.TASK_URL_DELECT_APPROVE_V2)) { // 删除审批
            ApprovePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), ApprovePOJO.class);
            handleApproveDelect(pojo);
        }else if(taskId.equals(AttachmentApi.TaskId.getList)){
            //附件列表
            AttachmentListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), AttachmentListPOJO.class);
            handleAttachmentList(pojo);
        }else if(taskId.equals(ApproveAPI.TaskId.TASK_URL_PROCESS_APPROVE_V2)){//处理审批接口
            NormalPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), NormalPOJO.class);
            NormalHandlePOJO(pojo);
        }else if(taskId.equals(WorkFlowAPI.TaskId.TASK_URL_GET_FORM)){//获取表单信息
            FormPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),FormPOJO.class);
            FormDataHandle(pojo);
//            ((CommentsListView)mCommentListView).updateComment();
        }
    }

    private void FormDataHandle(FormPOJO pojo) {
        if(pojo != null){
            if(pojo.isResultSuccess()){
                mBoolUnGetForm = false;
                FormEntity entity =  pojo.getBody().getResponse().getData();
                List<formData> forms = entity.getForm();
                if(forms != null && mJsonHashMap != null){
                    createFormContent(forms);
                    if(mLlEditLinearLayout.getChildCount() < 1){
                        mLlEditLinearLayout.setVisibility(View.GONE);
                    }else{
                        mLlEditLinearLayout.setVisibility(View.VISIBLE);
                    }
                    if(mLlPersonLieanrLayout.getChildCount() < 1){
                        mLlPersonLieanrLayout.setVisibility(View.GONE);
                    }else{
                        mLlPersonLieanrLayout.setVisibility(View.VISIBLE);
                    }
                    if(mLlOtherLineanrLayout.getChildCount() < 1){
                        mLlOtherLineanrLayout.setVisibility(View.GONE);
                    }else{
                        mLlOtherLineanrLayout.setVisibility(View.VISIBLE);
                    }
                }
            }else{
                toast(pojo.getBody().getResponse().getReason());
            }
        }
    }

    private void NormalHandlePOJO(NormalPOJO pojo){
        if(pojo == null){
            return;
        }
        if(pojo.getBody().getResponse().isIsSuccess() == false){
            toast(pojo.getBody().getResponse().getReason());
            return;
        }
        setResult(RESULT_OK);
        this.finish();
    }

    /** 获取附件 */
    private void handleAttachmentList(AttachmentListPOJO pojo){
        if(pojo == null){
            showNetWorkGetDataException();
            return ;
        }else if(pojo.getBody() == null){
            toast(pojo.getHeader().getReason());
            return ;
        }else if(!pojo.getBody().getResponse().isIsSuccess()){
            toast(pojo.getBody().getResponse().getReason());
            return ;
        }else{
            List<AttachmentListEntity> attachmentListEntities = pojo.getBody().getResponse().getDate();
            if(attachmentListEntities != null && attachmentListEntities.size() > 0){
                mApprovalDetail.setHAS_FILE(1);
            }
            mFileLinearLayout.setImageData(attachmentListEntities);
            ((CommentsListView)mCommentListView).updateComment();
        }
    }
    /**删除审批 */
    private void handleApproveDelect(ApprovePOJO pojo){
        if(pojo == null){
            return;
        }else if(pojo.getBody() == null){
            return;
        }else if(pojo.getBody().getResponse().isIsSuccess() == false){
            toast(pojo.getBody().getResponse().getReason());
            return;
        }else if(pojo.getBody().getResponse().getData() == null){
            return;
        }else{
            setResult(RESULT_OK);
            this.finish();
        }
    }

    /** 获取审批详情*/
    private void handleApprovaleDetail(ApprovalDetailPOJO pojo){
        if(pojo == null){
            return;
        }else if(pojo.getBody() == null){
            return;
        }else if(pojo.getBody().getResponse().isIsSuccess() == false){
            toast(pojo.getBody().getResponse().getReason());
            return;
        }else if(pojo.getBody().getResponse().getData() == null){
            return;
        }else{
            mApprovalDetail = pojo.getBody().getResponse().getData();
            if(mApprovalDetail != null){
            //申请标题
//            mTv.setText(mApprovalDetail.getA_TITLE());
            // 设置头像
            String userName = mApprovalDetail.getCREATE_USER();
            HeadImageUtil.getInstance(this).setAvatarResourceAppendUrl(mIvPersonHead,userName,2,60,60);
            //申请者姓名
            mTvPersonName.setText(mApprovalDetail.getCREATE_USER_NAME());
            //申请时间
            SimpleDateFormat create = new SimpleDateFormat("MM/dd(E) HH:mm", Const.getLocale());
            mTvCreateTime.setText(create.format(mApprovalDetail.getCREAT_TIME()));
            //申请状态
            int approve_Status = FormViewUtil.getApprovalStatus(mApprovalDetail.getA_STATUS());
            mTvApplyState.setBackgroundResource(mTextState[approve_Status]);
            mTvApplyState.setText(mTextStateStr[approve_Status]);
            //是否至急
            int isUrgent = mApprovalDetail.getA_IS_URGENT();
            if(isUrgent == 1){
                mTvState.setVisibility(View.VISIBLE);
            }else {
                mTvState.setVisibility(View.GONE);
            }
            //审批者
            mApprova = mApprovalDetail.getA_APPROVE();
            if(mApprova != null && mApprova.size() > 0){//获取审批者信息
                mApproveList = new ArrayList<String>();
                mApproveNameList = new ArrayList<String>();
                mStrApproveName = mApprova.get(0).getUSER_NAME();
                mStrApproveNameId += mApprova.get(0).getUSER();
                mApproveList.add(mApprova.get(0).getUSER());
                mApproveNameList.add(mApprova.get(0).getUSER_NAME());
                for(int i = 1 ;i < mApprova.size();i++){
                    mStrApproveNameId +=  "、" +mApprova.get(i).getUSER() ;
                    mStrApproveName += "、" + mApprova.get(i).getUSER_NAME();
                    mApproveList.add(mApprova.get(i).getUSER());
                    mApproveNameList.add(mApprova.get(i).getUSER_NAME());
                }
            }else{
                mStrApproveName = "";
            }
            //转交
            List<ApproveDetailEntity.Person> ApproveDetailEntity =  mApprovalDetail.getA_APPROVE_PATH();
            if(ApproveDetailEntity != null && ApproveDetailEntity.size() > 0){//获取转交信息
                mApprovePathList = new ArrayList<>();
                mApprovePathNameList = new ArrayList<>();
                if("".equals(mStrApproveName)){
                    mStrApproveName = ApproveDetailEntity.get(0).getUSER_NAME();
                    mApprovePathList.add(ApproveDetailEntity.get(0).getUSER());
                    mApprovePathNameList.add(ApproveDetailEntity.get(0).getUSER_NAME());
                    for(int i = 1 ;i < ApproveDetailEntity.size();i++){
                        mStrApproveName += " —> " + ApproveDetailEntity.get(i).getUSER_NAME();
                        mApprovePathList.add(ApproveDetailEntity.get(i).getUSER());
                        mApprovePathNameList.add(ApproveDetailEntity.get(i).getUSER_NAME());
                    }
                }else{
                    String deliver = "";
                    deliver += ApproveDetailEntity.get(0).getUSER_NAME();
                    mApprovePathList.add(ApproveDetailEntity.get(0).getUSER());
                    mApprovePathNameList.add(ApproveDetailEntity.get(0).getUSER_NAME());
                    for(int i = 1 ;i < ApproveDetailEntity.size();i++){
                        deliver += " —> " + ApproveDetailEntity.get(i).getUSER_NAME();
                        mApprovePathList.add(ApproveDetailEntity.get(i).getUSER());
                        mApprovePathNameList.add(ApproveDetailEntity.get(i).getUSER_NAME());
                    }
                    mStrApproveName = deliver + " —> " + mStrApproveName;
                }
            }
            //抄送者
            mCC = mApprovalDetail.getA_CC();
            if(mCC != null && mCC.size() > 0){
                mCcList = new ArrayList<>();
                mCcNameList = new ArrayList<>();
                mStrCcNameId += mCC.get(0).getUSER();
                mStrCcName += mCC.get(0).getUSER_NAME();
                mCcList.add(mStrCcNameId);
                mCcNameList.add(mStrCcName);
                for(int i = 1;i < mCC.size();i++){
                    mStrCcNameId += "、" + mCC.get(i).getUSER();
                    mStrCcName +=  "、" + mCC.get(i).getUSER_NAME();
                    mCcList.add(mCC.get(i).getUSER());
                    mCcNameList.add(mCC.get(i).getUSER_NAME());
                }
            }else{
                mStrCcName = "";
            }
                //
                mMessagePerson = new ArrayList<String>();
                mMessagePersonName = new ArrayList<String>();
                mMessagePerson.add(mApprovalDetail.getCREATE_USER());
                mMessagePersonName.add(mApprovalDetail.getCREATE_USER_NAME());
                if(mApproveList != null && mApproveNameList != null){
                    mMessagePerson.addAll(mApproveList);
                    mMessagePersonName.addAll(mApproveNameList);
                }
                if(mCcList != null && mCcNameList != null){
                    mMessagePerson.addAll(mCcList);
                    mMessagePersonName.addAll(mCcNameList);
                }
                if(mApprovePathList != null && mApprovePathNameList != null){
                    mMessagePerson.addAll(mApprovePathList);
                    mMessagePersonName.addAll(mApprovePathNameList);
                }
            //操作
            if(mApprovalDetail.getIS_CAN_APPROVE() == 1){
                ApproveHandleLayoutView handleLayoutView = new ApproveHandleLayoutView(this);
                mLlOtherLineanrLayout.addView(handleLayoutView);
                mIvMoreDo.setVisibility(View.GONE);
            }else{
                //可否删除
                String  name = mApprovalDetail.getCREATE_USER();
                String user  = LauchrConst.getHeader(this).getLoginName();
                if(name.equals(user) && mApprovalDetail.getIS_CAN_DELETE() == 1){
                    mIvMoreDo.setVisibility(View.VISIBLE);
                }
            }
                String formJsonData = mApprovalDetail.getA_FORM_DATA();
                //解析表单数据
                mJsonHashMap =  FormViewUtil.getFormDataJson(formJsonData);
                if(mApprovalDetail != null){
                    //获取表单组件
                    WorkFlowAPI.getInstance().getForm(this, mApprovalDetail.getA_FORM_INSTANCE_ID());
//                    ApproveAPI.getInstance().getCommentList(this, APPROVE_APP_KEY,mApprovalDetail.getSHOW_ID(), 0, 0, 0);
                }
                ((CommentsView)mSendMessageView).setAppInfo(APPROVE_APP_KEY,mApprovalDetail.getSHOW_ID(),
                        mApprovalDetail.getA_TITLE(),mApprovalDetail.getHAS_COMMENT(),mMessagePerson,mMessagePersonName);
                ((CommentsListView)mCommentListView).setAppInfo(APPROVE_APP_KEY,mApprovalDetail.getSHOW_ID());
            }
        }
    }

    /** 动态创建表单内容*/
    private void createFormContent(List<formData> forms) {
        for(formData form : forms){
            String type = form.getInputType();
            String key = form.getKey();
            String json = null;
            if(mJsonHashMap.get(key) != null){
                    json = mJsonHashMap.get(key);
            }
            if(type.equals(FormViewUtil.SINGLE_TEXT_INPUT)){ //单行输入框
                TextLinearLayoutView textView = new TextLinearLayoutView(this,form,json);
                addViewIntoForm(textView,FormViewUtil.SINGLE_TEXT_INPUT);
            }else if(type.equals(FormViewUtil.MUTIL_TEXT_INPUT)){//多行输入框
                TextLinearLayoutView textView = new TextLinearLayoutView(this,form,json);
                addViewIntoForm(textView,FormViewUtil.MUTIL_TEXT_INPUT);
            }else if(type.equals(FormViewUtil.TIME_INPUT)){//时间
                TextLinearLayoutView textView = new TextLinearLayoutView(this,form,json);
                addViewIntoForm(textView, FormViewUtil.TIME_INPUT);
            }else if(type.equals(FormViewUtil.APPROVE_PERSON_INPUT)){//审批者
                TextLinearLayoutView textView = new TextLinearLayoutView(this,form,json);
                addViewIntoForm(textView,FormViewUtil.APPROVE_PERSON_INPUT);
            }else if(type.equals(FormViewUtil.APPROVE_LIMIT_INPUT)){//审批期限
                TextLinearLayoutView textView = new TextLinearLayoutView(this,form,json);
                addViewIntoForm(textView,FormViewUtil.APPROVE_LIMIT_INPUT);
            }else if(type.equals(FormViewUtil.CC_PERSON_INPUT)){//抄送者
                TextLinearLayoutView textView = new TextLinearLayoutView(this,form,json);
                addViewIntoForm(textView,FormViewUtil.CC_PERSON_INPUT);
            }else if(type.equals(FormViewUtil.FILE_INPUT) ){//附件
                //获取附件
                if(mApprovalDetail.getHAS_FILE() == 1){
                    mFileLinearLayout = new FileShowLayoutView(this,form);
                    addViewIntoForm(mFileLinearLayout,FormViewUtil.FILE_INPUT);
                    AttachmentApi.getInstance().getAttachmentList(this, mApprovalDetail.getSHOW_ID(), APPROVE_APP_KEY);
                }else{
                    ((CommentsListView)mCommentListView).updateComment();
                }
            }else if(type.equals(FormViewUtil.SINGLE_SELECT_INPUT)){//单选框
                TextLinearLayoutView textView = new TextLinearLayoutView(this,form,json);
                addViewIntoForm(textView,FormViewUtil.SINGLE_SELECT_INPUT);
            }else if(type.equals(FormViewUtil.MUTIL_SELECT_INPUT)){//多选框
                TextLinearLayoutView textView = new TextLinearLayoutView(this,form,json);
                addViewIntoForm(textView,FormViewUtil.MUTIL_SELECT_INPUT);
            }
        }
    }
    /**添加表单内容到主视图*/
    private void addViewIntoForm(View view,String type){
        if(type.equals(FormViewUtil.APPROVE_PERSON_INPUT)){
            mLlPersonLieanrLayout.addView(view,0);
            mLlPersonLieanrLayout.addView(createLineView(),1);
        }else if(type.equals(FormViewUtil.CC_PERSON_INPUT)){
            mLlPersonLieanrLayout.addView(view);
            mLlPersonLieanrLayout.addView(createLineView());
        }else if(type.equals(FormViewUtil.FILE_INPUT)){
            mLlOtherLineanrLayout.addView(view,0);
            mLlOtherLineanrLayout.addView(createLineView(),1);
        }else{
            mLlEditLinearLayout.addView(view);
            mLlEditLinearLayout.addView(createLineView());
        }
    }
    /** 创建分割线*/
    private ImageView createLineView(){
        ImageView line = new ImageView(this);
        LinearLayout.LayoutParams imageParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1);
        line.setLayoutParams(imageParam);
        line.setBackgroundColor(getResources().getColor(R.color.grey_launchr));
        return line;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;
        }
        if(requestCode == CommentsView.TAKE_PICTURE){
            ((CommentsView)mSendMessageView).setImageMessage();
        }
        if(requestCode == CommentsView.GET_ALBUM){
            if(data == null){
                return;
            }
            List<String> imagePath = data.getStringArrayListExtra(PhotoActivity.SELECT_IMAGE_LIST);
            if(imagePath != null){
                ((CommentsView)mSendMessageView).setAublmImageMessage(imagePath.get(0));
            }
        }
        if(requestCode == SELECT_USER){
            mUserEntity = (UserDetailEntity) data.getSerializableExtra(DepartmentActivity.KEY_USE_ENTITY);
            mWriteCommentPopWindow.setCompleteSelected(mUserEntity,0);
        }
    }


    @Override
    public void agree(String showId, int position) {
        ApproveAPI.getInstance().processApprove(this, showId, "APPROVE","");
        mAgreePopWindow.dismiss();
    }

    @Override
    public void turnOver(String showId, int position) {
        mWriteCommentPopWindow = new WriteCommentPopWindow(this,WriteCommentPopWindow.TYPE_TURN_OVER, showId,-1, 2);
        mWriteCommentPopWindow.setstartActivityLinsenter(this);
        mWriteCommentPopWindow.show(mSendMessageView);
        mAgreePopWindow.dismiss();
    }

    @Override
    public void startDeptActiviy() {
        Intent in = new Intent(ApproveDetailActivity.this, DepartmentActivity.class);
        in.putExtra(DepartmentActivity.KEY_SINGLE, DepartmentActivity.SINGLE);
        if (mUserEntity != null) {
            in.putExtra(DepartmentActivity.KEY_POSITION, mUserEntity);
        }
        startActivityForResult(in, SELECT_USER);
    }

    @Override
    public void sendMessage(String message) {
        ((CommentsListView)mCommentListView).updateComment();
    }
}
