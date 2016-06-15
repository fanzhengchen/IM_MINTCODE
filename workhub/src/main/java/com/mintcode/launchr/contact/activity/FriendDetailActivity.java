package com.mintcode.launchr.contact.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.DataPOJO;
import com.mintcode.chat.activity.ChatActivity;
import com.mintcode.chat.entity.User;
import com.mintcode.im.database.FriendDBService;
import com.mintcode.launchr.R;
import com.mintcode.launchr.activity.MainActivity;
import com.mintcode.launchr.api.IMNewApi;
import com.mintcode.launchr.app.newSchedule.view.SelectAddEventPopWindowView;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.entity.FriendEntity;
import com.mintcode.launchr.pojo.entity.PersonEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.view.RemarkDialog;

/**
 * Created by JulyYu on 2016/3/29.
 * 好友详情页
 */

public class FriendDetailActivity extends BaseActivity implements SelectAddEventPopWindowView.PopWindowListener {

    /** 返回按钮 */
    private ImageView mIvBack;
    /** 返回按钮 */
    private ImageView mIvMore;
    /** 头像imageview */
    private ImageView mIvHeadView;
    /** 备注名 */
    private TextView mTvRemarkName;
    /** 对话布局*/
    private RelativeLayout mRelMessage;
    /** 手机 */
    private RelativeLayout mRelPhone;
    /** Launchr id */
    private TextView mTvLaunchrID;
    /** 手机 */
    private TextView mTvPhone;
    /** 邮箱 */
    private TextView mTvName;

    /** 用户信息key */
    public static final String KEY_PERSONAL_ID = "key_personal_id";

    private  FriendEntity mPersonDetail;

    private String mStrUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        initView();
        initData();
    }

    private void initView() {

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvMore = (ImageView) findViewById(R.id.iv_edit);
        mIvHeadView = (ImageView) findViewById(R.id.iv_head_image);
        mTvRemarkName = (TextView) findViewById(R.id.tv_head_name);
        mRelMessage = (RelativeLayout) findViewById(R.id.rel_send_message);
        mRelPhone = (RelativeLayout) findViewById(R.id.rel_my_phone);
        mTvLaunchrID = (TextView) findViewById(R.id.tv_launchr_id_text);

        mTvPhone = (TextView) findViewById(R.id.tv_my_phone_text);
        mTvName = (TextView) findViewById(R.id.tv_my_name_text);

        mIvBack.setOnClickListener(this);
        mRelMessage.setOnClickListener(this);
        mIvMore.setOnClickListener(this);
        mRelPhone.setOnClickListener(this);


    }

    private void initData(){

        mStrUserName = AppUtil.getInstance(this).getShowId();
        mPersonDetail = (FriendEntity)getIntent().getSerializableExtra(KEY_PERSONAL_ID);
        if(mPersonDetail != null){
            String remark = mPersonDetail.getRemark();
            String name = mPersonDetail.getNickName();
            if(!TextUtils.isEmpty(remark)){
                mTvRemarkName.setText(remark);
            }else{
                mTvRemarkName.setText(name);
            }
            mTvName.setText(name);
            mTvPhone.setText(mPersonDetail.getMobile());
            String imNumber = mPersonDetail.getImNumber();
            if(!TextUtils.isEmpty(imNumber)){
                mTvLaunchrID.setText(imNumber);
            }
            // 头像显示
            HeadImageUtil.getInstance(this).restartSetAvatarResource(mIvHeadView,mPersonDetail.getRelationName(),mPersonDetail.getRelationAvatar(),0, 150, 150);
            // 设置Id显示名称
            TextView tvID = (TextView) findViewById(R.id.tv_launchr_id);
            String strID = "";
            if(LauchrConst.IS_WORKHUB){
                strID = getResources().getString(R.string.workhub_id);
            }else {
                strID = getResources().getString(R.string.launchr_id);
            }
            tvID.setText(strID);
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mIvBack){
            this.finish();
        }else if(v == mRelMessage){
            sendMessage();
        }else if(v == mIvMore){
            getFriedndEdit(mIvMore);
        }else if(v == mRelPhone){
            String phone = mTvPhone.getText().toString().trim();
            initCallDialog(phone);
        }
    }

    // 发消息
    private void sendMessage() {
        if (mPersonDetail != null) {
            Intent intent = new Intent(FriendDetailActivity.this, ChatActivity.class);
            User user = new User(mPersonDetail.getNickName(),mPersonDetail.getRelationName());
            intent.putExtra("user", user);
            startActivity(intent);

            MainActivity.showFirstFragment = 1;

            finish();
        }

    }

    private void getFriedndEdit(View v) {
        SelectAddEventPopWindowView popWindow = new SelectAddEventPopWindowView(this,false);
        popWindow.setFirstTextView(R.string.make_remark);
        popWindow.setSeconedTextView(R.string.delect_friend_relation);
        popWindow.setPopWindowListener(this);
        popWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }


    private void initCallDialog(final String num){
        if(TextUtils.isEmpty(num)){
            toast("号码为空");
            return;
        }
        final Dialog dialog = new Dialog(this,R.style.my_dialog);
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_call);
        Window window = dialog.getWindow();
        final RelativeLayout relCall = (RelativeLayout) window.findViewById(R.id.rel_call);
        TextView tvNum = (TextView) window.findViewById(R.id.tv_num);
        tvNum.setText(num);
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(v == relCall){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + num));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        };
        relCall.setOnClickListener(listener);
    }

    public void motifyRemark(String remark){
        mPersonDetail.setRemark(remark);
        mTvRemarkName.setText(remark);
    }

    // 修改好友备注
    @Override
    public void getSecondOption() {
        RemarkDialog dilaog = new RemarkDialog(this,mPersonDetail);
        dilaog.show();
    }
    // 解除好友关系
    @Override
    public void selectFristOption() {
        String to = mPersonDetail.getRelationName();
        IMNewApi.getInstance().deleteRelation(this, mStrUserName, to);
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        if(response == null){
            return;
        }
        if(taskId.equals(IMNewApi.TaskId.TASK_URL_DELECT_FRIEND)){
            DataPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),DataPOJO.class);
            if(pojo != null){
                if(pojo.isResultSuccess()){
                    FriendDBService.getInstance().delectFriend(mStrUserName,mPersonDetail.getRelationName());
                    this.finish();
                }else{
                    toast(pojo.getMessage());
                }
            }
        }
    }
}
