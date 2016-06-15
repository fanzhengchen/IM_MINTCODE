package com.mintcode.launchr.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mintcode.DataPOJO;
import com.mintcode.im.database.FriendDBService;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.IMNewApi;
import com.mintcode.launchr.contact.activity.FriendDetailActivity;
import com.mintcode.launchr.message.activity.VerifyFriendMessageActivity;
import com.mintcode.launchr.pojo.entity.FriendEntity;
import com.mintcode.launchr.pojo.entity.ValidateEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchrnetwork.OnResponseListener;

/**
 * Created by JulyYu on 2016/3/28.
 */
public class RemarkDialog extends Dialog implements View.OnClickListener,OnResponseListener{



    private Context mContext;

    private EditText mEdtRemark;
    private TextView mTvComfirm;

    private ValidateEntity mEntity;
    private FriendEntity mPersonEntity;
    private int mIntDealValidate = 0;

    private String mStrUserName;

    public RemarkDialog(Context context) {
        super(context);
        mContext = context;
    }

    public RemarkDialog(Context context,ValidateEntity entity,int validate) {
        super(context);
        mContext= context;
        mEntity = entity;
        mIntDealValidate = validate;
        mStrUserName = AppUtil.getInstance(mContext).getShowId();
    }

    public RemarkDialog(Context context,FriendEntity entity) {
        super(context);
        mContext= context;
        mPersonEntity = entity;
        if(mEdtRemark != null){
            mEdtRemark.setText(mPersonEntity.getRemark());
        }
        mStrUserName = AppUtil.getInstance(mContext).getShowId();
    }

    public void initView(){
        mEdtRemark = (EditText)findViewById(R.id.edt_remark);
        mTvComfirm = (TextView)findViewById(R.id.tv_confirm);
        mTvComfirm.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_person_remark);
        this.setCanceledOnTouchOutside(true);
        initView();
    }

    @Override
    public void onClick(View v) {
        if(v == mTvComfirm){
            String remark = mEdtRemark.getText().toString();
            if(!"".equals(remark)){
                if(mIntDealValidate == 0){
                    motifyFriendRemark();
                }else{
                    dealVerifyRequest();
                }
            }else{
                Toast.makeText(mContext,mContext.getResources().getString(R.string.remark_no_null),Toast.LENGTH_SHORT).show();
            }
        }
    }
    /** 修改好友备注*/
    private void motifyFriendRemark() {
        if(mPersonEntity != null){
            String relationName = mPersonEntity.getRelationName();
            String userName = AppUtil.getInstance(mContext).getShowId();
            String remark = mEdtRemark.getText().toString();
            IMNewApi.getInstance().motifyFriendRmark(this,userName,relationName,remark);
        }
    }
    /** 处理好友验证*/
    private void dealVerifyRequest() {


        if(mEntity != null){

            String fromNickname = mEntity.getFromNickName();
            String from = mEntity.getFrom();
            String fromVatar = mEntity.getFromAvatar();
            String to = mEntity.getTo();
            String reamrk = mEdtRemark.getText().toString();
            String content = "";
            int relationId = 167;
            int validate = mIntDealValidate;
            IMNewApi.getInstance().dealRelationValidata(this,to,fromNickname,from,fromVatar,
                    to,reamrk,content,relationId,validate);
        }

    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        if (response == null){
            return;
        }
        if(taskId.equals(IMNewApi.TaskId.TASK_DISPOSE_VALIDATA)){
            DataPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),DataPOJO.class);
            if(pojo != null){
                if(pojo.isResultSuccess()){
                    ((VerifyFriendMessageActivity)mContext).updateList();
                    String url = mEntity.getFromAvatar();
                    HeadImageUtil.getInstance(mContext).saveValue(mEntity.getUserName(),url);
                    this.dismiss();
                }else{
                    Toast.makeText(mContext,pojo.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }else if(taskId.equals(IMNewApi.TaskId.TASK_MOTIFY_REMARK)){
            DataPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),DataPOJO.class);
            if(pojo != null){
                if(pojo.isResultSuccess()){
                    String remark = mEdtRemark.getText().toString();
                    FriendDBService.getInstance().updateFriendRemark(mStrUserName,mPersonEntity.getRelationName(),remark);
                            ((FriendDetailActivity) mContext).motifyRemark(remark);
                    this.dismiss();
                }else{
                    Toast.makeText(mContext,pojo.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean isDisable() {
        return false;
    }
}
