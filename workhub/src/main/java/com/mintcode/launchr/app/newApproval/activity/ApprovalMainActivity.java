package com.mintcode.launchr.app.newApproval.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.ApproveAPI;
import com.mintcode.launchr.api.MessageAPI;
import com.mintcode.launchr.app.newApproval.adapter.ApprovalItemAdapter;
import com.mintcode.launchr.app.newApproval.adapter.TypeAdapter;
import com.mintcode.launchr.app.newApproval.window.AgreePopWindow;
import com.mintcode.launchr.app.newApproval.window.ApproveEditMenuPopWindow;
import com.mintcode.launchr.app.newApproval.window.WriteCommentPopWindow;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.contact.activity.DepartmentActivity;
import com.mintcode.launchr.pojo.ApproveListPOJO;
import com.mintcode.launchr.pojo.MessageListPOJO;
import com.mintcode.launchr.pojo.NormalPOJO;
import com.mintcode.launchr.pojo.entity.ApprovalTypeEntity;
import com.mintcode.launchr.pojo.entity.ApproveEntity;
import com.mintcode.launchr.pojo.entity.MessageEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.MixPanelUtil;
import com.mintcode.launchr.util.TTDensityUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.view.MenuPopWindow;
import com.mintcode.launchrnetwork.OnResponseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/4/14.
 */
public class ApprovalMainActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,ApproveEditMenuPopWindow.ApprovePopWindowListener {

    private View mView;
    private Context mContext;
    /***返回  */
    private ImageView mIvBack;
    /*** 消息显示*/
    private TextView mTvMessage;
    /*** 搜索*/
    private ImageView mIvSearch;
    /****审批类型选择text****/
    private TextView mTvApprovalSelect;
    /****审批类型选择窗口**/
    private MenuPopWindow mSelectPopWindow;
    /** 追加*/
    private ImageView mIvAdd;
    private RelativeLayout mRlTitle;
    /**评语操作栏**/
    private WriteCommentPopWindow mWriteCommentPopWindow;
    /**审批快捷操作栏**/
    private ApproveEditMenuPopWindow mApproveEditMenuPopWindow;
    /**审批列表**/
    private ListView mLvApprove;
    private ApprovalItemAdapter mApprovalContentAdapter;

    /********/
    private TypeAdapter mApprovalTypeAdapter;
    private ListView mLvSelectList;
    /***全部审批**/
    private List<ApproveEntity> mApproveData;
    /**待审批**/
    private List<ApproveEntity> mWaitApprove;
    /**发出审批**/
    private List<ApproveEntity> mSendApprove;
    /**抄送审批**/
    private List<ApproveEntity> mCopyApprove;
    /**完成审批**/
    private List<ApproveEntity> mFinishApprove;
    /***消息列表*/
    private List<MessageEntity> mCommetMessage;
    /** */
    private List<String> mAppTypeList;


//    private String mStrApproval;
    /**审批类型选择**/
    private int mIntSelectType = 0;
    /**是否刷新界面**/
    public static boolean mBoolFreash = true;
    /**审批操作的position**/
    private int mPosition;
    /***选择转交的审批人**/
    private UserDetailEntity mUserEntity;

    private static final int NEW_APPLY = 2;
    private static final int CHOOSE_TYPE = 3;
    public static final int APPROVAL_DETAIL = 1;

    private static String APP_SORT_APPROVE = "APPROVE";
    private static String APP_SORT_CC = "CC";
    private static String APP_SORT_SEND = "SEND";
    private static String APP_APPROVE_COMMENT = "APPROVAL_COMMENT";
    private static String APP_APPROVE_CODE = Const.SHOWID_APPROVE;

    public final static String APPROVEL_TYPE = "approvel_type";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
        initViews();
        initData();
        MixPanelUtil.sendEvent(this,MixPanelUtil.APPCENTER_APPROVE_EVENT);
    }



    private void initViews() {
        mIvBack = (ImageView)findViewById(R.id.iv_approval_back);
        mTvMessage = (TextView)findViewById(R.id.tv_approval_message_num);
        mIvSearch = (ImageView)findViewById(R.id.iv_approval_search);
        mTvApprovalSelect = (TextView)findViewById(R.id.tv_approval_title_select);
        mIvAdd = (ImageView) findViewById(R.id.iv_approval_add);
        mRlTitle = (RelativeLayout)findViewById(R.id.title_approval_bar);
        mLvApprove = (ListView)findViewById(R.id.lv_approval_list);
        mApprovalContentAdapter = new ApprovalItemAdapter(this);

        List<String> strList = new ArrayList<>();
        int [] str = {R.string.approval_all,R.string.approval_should_do,R.string.approval_send_to
                ,R.string.approval_copy_me,R.string.approval_finish,};
        for(int num : str){
            strList.add(getResources().getString(num));
        }
        mApprovalTypeAdapter = new TypeAdapter(this,strList);

        mIvBack.setOnClickListener(this);
        mIvSearch.setOnClickListener(this);
        mIvAdd.setOnClickListener(this);
        mTvApprovalSelect.setOnClickListener(this);

        mView = getWindow().getDecorView().findViewById(android.R.id.content);
        mContext = this;

        mLvApprove.setAdapter(mApprovalContentAdapter);
        mLvApprove.setOnItemLongClickListener(this);
        mLvApprove.setOnItemClickListener(this);
    }

    private void initData() {
        mApproveData = new ArrayList<>();
        mCommetMessage = new ArrayList<>();
        mWaitApprove = new ArrayList<>();
        mSendApprove = new ArrayList<>();
        mCopyApprove = new ArrayList<>();
        mFinishApprove = new ArrayList<>();
        mAppTypeList = new ArrayList<>();
        mAppTypeList.add(APP_SORT_APPROVE);
        mAppTypeList.add(APP_SORT_CC);
        mAppTypeList.add(APP_SORT_SEND);
        MessageAPI.getInstance().getMessageList(this, -1, 2, null, APP_APPROVE_CODE, null, 0, mAppTypeList, 0, 0);
    }

    /**
     *新建审批
     */
    private void Add(){
        Intent intent = new Intent(ApprovalMainActivity.this, com.mintcode.launchr.app.newApproval.activity.ApprovalTypeActivity.class);
        startActivityForResult(intent, CHOOSE_TYPE);
    }
    /**初始化审批类型选择popwindow*/
    int downY = 10;
    private void initPopWindow() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View contentView = inflater.inflate(R.layout.dropdown_popwindow, null);
        mLvSelectList = (ListView)contentView.findViewById(R.id.lv_dropdown_popwindow);
        int [] str = {R.string.approval_all,R.string.approval_should_do,R.string.approval_send_to
                ,R.string.approval_copy_me,R.string.approval_finish,};
        List<String> strList = new ArrayList<>();
        for(int num : str){
            strList.add(getResources().getString(num));
        }
        mApprovalTypeAdapter = new TypeAdapter(this,strList);
        mLvSelectList.setAdapter(mApprovalTypeAdapter);
        mLvSelectList.setOnItemClickListener(this);
        mSelectPopWindow = new MenuPopWindow(this,contentView,mRlTitle.getWidth()/2);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        mSelectPopWindow.showAsDropDown(mRlTitle, screenWidth / 3, downY);

    }

    /****显示审批选择界面***/
    private void approvalSelect() {
        if(mSelectPopWindow == null){
            initPopWindow();
        }else{
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int screenWidth = dm.widthPixels;
            mSelectPopWindow.showAsDropDown(mRlTitle, screenWidth / 3, downY);
        }
        mApprovalTypeAdapter.setSelectText(mTvApprovalSelect.getText().toString());
    }
    /**刷新审批选择**/
    private void selectApproveType(int position) {
        mIntSelectType = position;
        String title =  (String)mApprovalTypeAdapter.getItem(mIntSelectType);
        mApprovalTypeAdapter.setSelectText(title);
        mTvApprovalSelect.setText(title);
        if(mSelectPopWindow != null){
            mSelectPopWindow.dismiss();
        }
        switch (mIntSelectType){
            case 0://加载所有审批
                mApproveData.clear();
                mApproveData.addAll(mWaitApprove);
                mApproveData.addAll(mSendApprove);
                mApproveData.addAll(mCopyApprove);
                mApproveData.addAll(mFinishApprove);
                mApprovalContentAdapter.setDataAndUpdate(mApproveData);
                break;
            case 1://加载待审批
                mApprovalContentAdapter.setDataAndUpdate(mWaitApprove);
                break;
            case 2://加载发出审批
                mApprovalContentAdapter.setDataAndUpdate(mSendApprove);
                break;
            case 3://加载抄送审批
                mApprovalContentAdapter.setDataAndUpdate(mCopyApprove);
                break;
            case 4://加载已完成审批
                mApprovalContentAdapter.setDataAndUpdate(mFinishApprove);
                break;
            default:
                break;
        }
    }
    /**打开审批**/
    private void openApprovel(int position) {
        ApproveEntity approveEntity = ((ApproveEntity)mApprovalContentAdapter.getItem(position));

        if(mCommetMessage != null && mCommetMessage.size() > 0){
            for(int j = 0 ;j < mCommetMessage.size();j++){
                if(approveEntity.getSHOW_ID().equals(mCommetMessage.get(j).getRmShowID())){
                    mCommetMessage.remove(j);
                    j--;
                }
            }
            mApprovalContentAdapter.setMessage(mCommetMessage);
            mApprovalContentAdapter.notifyDataSetChanged();
        }

        String approvalKind = approveEntity.getT_GET_TYPE();
        Intent i = new Intent(ApprovalMainActivity.this,ApproveDetailActivity.class);
        i.putExtra(ApproveDetailActivity.APPLY_DETAIL, approveEntity.getSHOW_ID());
        if(approvalKind.equals("in")){
            startActivityForResult(i, APPROVAL_DETAIL);
        }else if(approvalKind.equals("out")){
            startActivityForResult(i, APPROVAL_DETAIL);
        }else if(approvalKind.equals("cc")){
            startActivity(i);
        }
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //返回
            case R.id.iv_approval_back:
                finish();
                break;
            case R.id.iv_approval_add:
                Add();
                break;
            case R.id.iv_approval_search:
                startActivityForResult(new Intent(this, ApproveSearchActivity.class),APPROVAL_DETAIL);
                break;
            case R.id.tv_approval_title_select://审批类型选择
                approvalSelect();
                break;
            default:
                break;
        }

    }

    public void updateApproveList(){
        showLoading();
        MessageAPI.getInstance().getMessageList(this, -1, 2, null, APP_APPROVE_CODE, null, 0, mAppTypeList, 0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent == mLvSelectList){
            selectApproveType(position);
        }else if(parent == mLvApprove) {
            openApprovel(position);
        }
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        ApproveEntity approveEntity = (ApproveEntity)mApprovalContentAdapter.getItem(position);
        String approvalKind =  approveEntity.getT_GET_TYPE();
        if("in".equals(approvalKind)){
            final String showId = approveEntity.getSHOW_ID();
            int px = TTDensityUtil.dip2px(this, 60);
            mApproveEditMenuPopWindow = new ApproveEditMenuPopWindow(this,showId,position);
            mApproveEditMenuPopWindow.setApprovePopWindowListener(this);
            mApproveEditMenuPopWindow.showAsDropDown(view,0,-px, Gravity.RIGHT);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == 12){
            mUserEntity = (UserDetailEntity) data.getSerializableExtra(DepartmentActivity.KEY_USE_ENTITY);
            mWriteCommentPopWindow.setCompleteSelected(mUserEntity, -1);
        }else if(requestCode == CHOOSE_TYPE && data != null){
            ApprovalTypeEntity typeEntity = (ApprovalTypeEntity)data.getSerializableExtra(APPROVEL_TYPE);
            Intent i = new Intent(this,CreateNewApplyActivity.class);
            i.putExtra(CreateNewApplyActivity.APPLY_TYPE, typeEntity);
            startActivityForResult(i, NEW_APPLY);
        }else if(requestCode == NEW_APPLY){//新建审批返回刷新界面
            showLoading();
            ApproveAPI.getInstance().getApproveListV2(this, "", -1, 200, System.currentTimeMillis());
            mIntSelectType = 2;
        }else if(requestCode == APPROVAL_DETAIL){
            showLoading();
            mWaitApprove.clear();
            mSendApprove.clear();
            mCopyApprove.clear();
            mFinishApprove.clear();
            ApproveAPI.getInstance().getApproveListV2(this, "", -1, 200, System.currentTimeMillis());
        }
    }


    @Override
    public void reCall(String showId, int position) {
        mWriteCommentPopWindow = new WriteCommentPopWindow(this,WriteCommentPopWindow.TYPE_RECALL,showId,position,1);
        mWriteCommentPopWindow.show(mView);
    }

    @Override
    public void refuse(String showId, int position) {
        mWriteCommentPopWindow = new WriteCommentPopWindow(this,WriteCommentPopWindow.TYPE_RUFUSE,showId,position,1);
        mWriteCommentPopWindow.show(mView);
    }

    @Override
    public void pass(String showId, int position) {
        new AgreePopWindow(mContext, showId, position,new AgreePopWindow.ItemSelectListener() {

            @Override
            public void turnOver(String showId, int position) {
                mWriteCommentPopWindow = new WriteCommentPopWindow(mContext,WriteCommentPopWindow.TYPE_TURN_OVER,showId,position,1);
                mWriteCommentPopWindow.setstartActivityLinsenter(new WriteCommentPopWindow.startActivityLinsenter() {
                    @Override
                    public void startDeptActiviy() {
                        Intent in = new Intent(ApprovalMainActivity.this, DepartmentActivity.class);
                        in.putExtra(DepartmentActivity.KEY_SINGLE, DepartmentActivity.SINGLE);
                        if(mUserEntity != null){
                            in.putExtra(DepartmentActivity.KEY_POSITION,mUserEntity);
                        }
                        startActivityForResult(in, 12);
                    }
                });
                mWriteCommentPopWindow.show(mView);
            }

            @Override
            public void agree(String showId, int position) {
                mPosition = position;
                showLoading();
                ApproveAPI.getInstance().processApprove((OnResponseListener) mContext, showId, "APPROVE", "");
            }
        }).show(mView);    }


    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        dismissLoading();
        if (response == null) {
            showNoNetWork();
            return;
        }
        if(taskId.equals(MessageAPI.MessageId.MESSAGE_URL_GET_MESSAGE_LIST)){//消息列表
            MessageListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), MessageListPOJO.class);
            messageListHandlePOJO(pojo);
            mCommetMessage = pojo.getBody().getResponse().getData();
            ApproveAPI.getInstance().getApproveListV2(this, "", -1, 200, System.currentTimeMillis());
        }else if(taskId.equals(ApproveAPI.TaskId.TASK_URL_APPROVE_LIST_V2)){//获取审批列表
            ApproveListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), ApproveListPOJO.class);
            ApproveListHandlePOJO(pojo);
        }else if(taskId.equals(ApproveAPI.TaskId.TASK_URL_PROCESS_APPROVE_V2)){//处理审批
            NormalPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), NormalPOJO.class);
            NormalHandlePOJO(pojo);
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
        showLoading();
        MessageAPI.getInstance().getMessageList(this, -1, 2, null, APP_APPROVE_CODE, null, 0, mAppTypeList, 0, 0);
    }

    private void ApproveListHandlePOJO(ApproveListPOJO pojo) {
        if(pojo == null){
            return;
        }
        if(pojo.getBody().getResponse().isIsSuccess() == false){
            toast(pojo.getBody().getResponse().getReason());
            return;
        }
        if(pojo.getBody().getResponse().getData() == null){
            return;
        }
        List<ApproveEntity> approveEntityList = pojo.getBody().getResponse().getData();
        if(approveEntityList != null){
            classifyApproveData(approveEntityList);
        }
    }

    private void classifyApproveData(List<ApproveEntity> approveEntityList) {
        String user  = LauchrConst.getHeader(this).getLoginName();
        mSendApprove.clear();
        mCopyApprove.clear();
        mFinishApprove.clear();
        mWaitApprove.clear();
        for(ApproveEntity approveEntity :approveEntityList){
            String create = approveEntity.getCREATE_USER();
            String approve = approveEntity.getA_APPROVE();
            String approvePath = approveEntity.getA_APPROVE_PATH();
            String status = approveEntity.getA_STATUS();
            if(create.contains(user)){//发出审批
                approveEntity.setT_GET_TYPE("out");
                mSendApprove.add(approveEntity);
            }else if(approve.contains(user)){//接收的审批
                approveEntity.setT_GET_TYPE("in");
                if (status.equals("WAITING") || status.equals("PROGRESS")){//未处理
                   mWaitApprove.add(approveEntity);
               }else{//已处理
                   mFinishApprove.add(approveEntity);
               }
            }else if(approvePath != null && approvePath.contains(user)){
                    approveEntity.setT_GET_TYPE("in");
                    mFinishApprove.add(approveEntity);
            }else if(approveEntity.getA_CC().contains(user)){//抄送的审批
                approveEntity.setT_GET_TYPE("cc");
                mCopyApprove.add(approveEntity);
            }
        }
            mApprovalContentAdapter.setData(null, mCommetMessage);
            selectApproveType(mIntSelectType);
            mBoolFreash = false;
    }

    private void messageListHandlePOJO(MessageListPOJO pojo){
        if(pojo == null){
            return;
        }
        if(pojo.getBody().getResponse().isIsSuccess() == false){
            toast(pojo.getBody().getResponse().getReason());
            return;
        }
        if(pojo.getBody().getResponse().getData() == null){
            return;
        }
    }
}
