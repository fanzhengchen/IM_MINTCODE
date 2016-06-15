package com.mintcode.launchr.contact.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintcode.SendVerifyPOJO;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.IMNewApi;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.contact.fragment.RemarkFragment;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.PersonEntity;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;

/**
 * Created by JulyYu on 2016/3/24.
 */
public class FriendVerifyActivity extends BaseActivity {


    private TextView mTvCancel;
    private TextView mTvSend;

    private ImageView mIvHead;
    private TextView mTvName;
    private TextView mTvNumber;
    private LinearLayout mLlReMark;
    private TextView mTvRemark;
    private EditText mEdtMark;

    private Fragment mRemarkFragemt;

    private PersonEntity mPersonEntity;

    private String mStrRemark = "";


    public final static String PERSON_DETAIL = "person_detail";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_verify);
        initView();
        initData();
    }



    private void initView(){
        mTvCancel = (TextView)findViewById(R.id.tv_cancel);
        mTvSend = (TextView)findViewById(R.id.tv_send);
        mTvName = (TextView)findViewById(R.id.tv_name);
        mTvNumber = (TextView)findViewById(R.id.tv_number);
        mIvHead = (ImageView)findViewById(R.id.iv_icon);
        mTvRemark = (TextView)findViewById(R.id.tv_remark_name);
        mLlReMark = (LinearLayout)findViewById(R.id.ll_remark);
        mEdtMark = (EditText)findViewById(R.id.edt_mark);

        mLlReMark.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
        mTvSend.setOnClickListener(this);
    }
    private void initData() {
        Intent detail = getIntent();
        mPersonEntity = (PersonEntity)detail.getSerializableExtra(PERSON_DETAIL);
        if(mPersonEntity != null){
            // 设置姓名
            mTvName.setText(mPersonEntity.getNickName());
            // 设置电话
            mTvNumber.setText(mPersonEntity.getMobile());
            // 设置头像
            HeadImageUtil.getInstance(this).setAvatarResource(mIvHead,mPersonEntity.getUserName(),mPersonEntity.getRelationAvatar(), 2, 60, 60);
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mLlReMark){
            setRemark();
        }else if(v == mTvCancel){
           onBackPressed();
        }else if(v == mTvSend){
                sendFriendVerify();
        }
    }

    private void setRemark() {
        if(mRemarkFragemt == null){
            mRemarkFragemt = new RemarkFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fl_remark,mRemarkFragemt).show(mRemarkFragemt).commit();
        }else{
            getSupportFragmentManager().beginTransaction().show(mRemarkFragemt).commit();
        }
        ((RemarkFragment)mRemarkFragemt).setRemarkText(mStrRemark);
        findViewById(R.id.fl_remark).setVisibility(View.VISIBLE);
    }

    public void getRemark(){
        if(mRemarkFragemt != null){
            findViewById(R.id.fl_remark).setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().hide(mRemarkFragemt).commit();
            mStrRemark = ((RemarkFragment)mRemarkFragemt).getRemarkText();
            String remak = getResources().getString(R.string.remark) + "   " + mStrRemark;
            mTvRemark.setText(remak);
        }

    }
    /** 检测是否可以发送*/
    private boolean checkOk(){
        if("".equals(mStrRemark)){
            toast(getResources().getString(R.string.remark_no_null));
            return false;
        }
        if("".equals(mEdtMark.getText().toString())){
            toast(getResources().getString(R.string.verify_no_null));
            return false;
        }
        return true;
    }
    /** 发送好友验证*/
    private void sendFriendVerify() {
        HeaderParam mHeaderParam = LauchrConst.getHeader(this);
        String userShowId = mHeaderParam.getLoginName();
        String nickName = mHeaderParam.getUserName();
        String to = mPersonEntity.getUserName();
        String avatar = mPersonEntity.getAvatar();
        int groupId = 167;
        String remark = mStrRemark;
        String mark = mEdtMark.getText().toString();
        IMNewApi.getInstance().sendRelationValidate(this,userShowId,userShowId,nickName,avatar,to,mark,groupId,remark);
    }

    @Override
    public void onBackPressed() {
        if(mRemarkFragemt != null){
            boolean show = mRemarkFragemt.isVisible();
            if(show){
                findViewById(R.id.fl_remark).setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().hide(mRemarkFragemt).commit();
            }else{
                getSupportFragmentManager().beginTransaction().remove(mRemarkFragemt).commitAllowingStateLoss();
                this.finish();
            }
        }else{
            this.finish();
        }
    }
    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        dismissLoading();
        if(response == null){
            return;
        }

        if(taskId.equals(IMNewApi.TaskId.TASK_RELATION_VALIDATA)){
            SendVerifyPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),SendVerifyPOJO.class);
            if(pojo != null){
                if(pojo.isResultSuccess()){
                    onBackPressed();
                }
            }
        }
    }
}
