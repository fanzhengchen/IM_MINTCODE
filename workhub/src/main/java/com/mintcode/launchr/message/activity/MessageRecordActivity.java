package com.mintcode.launchr.message.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.message.fragment.AppMsgFrament;
import com.mintcode.launchr.message.fragment.FileMsgFragment;
import com.mintcode.launchr.message.fragment.ImptMgsFragment;
import com.mintcode.launchr.message.fragment.PicMgsFragment;
import com.mintcode.launchr.message.fragment.SendToMeMgsFragment;

/**
 * 消息记录分类activity
 */
public class MessageRecordActivity extends BaseActivity {
    private static final int ENTER_SEARCH_RESULT = 1000;

    /**
     * 模块分类
     */
    private BaseFragment[] mClassArray;
    private TextView mTvImptMsg, mTvFileMsg, mTvPicMsg, mTvAppMsg;
    /**
     * 选择的fragment
     **/
    private int mShowFragmentId;

    /**
     * 回退按钮
     **/
    private ImageView mIvBack;

    private String loginName;

    private String toLoginName;

    /**
     * @我
     */
    private TextView mTvSendToMe;

    /**
     * 没有数据显示的view
     */
    private RelativeLayout mRelNoDataView;

    /**
     * 巨型icon
     */
    private ImageView mIvNoDataIcon;

    /**
     * 标题
     */
    private TextView mTvTitle;

    /**
     * 提示文字
     */
    private TextView mTvToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_record);
        initViews();

        this.setResult(RESULT_OK);
    }

    private void initViews() {
        loginName = getIntent().getStringExtra("loginName");
        toLoginName = getIntent().getStringExtra("toLoginName");

        mClassArray = new BaseFragment[]{
                new SendToMeMgsFragment(),
                new ImptMgsFragment(),
                new FileMsgFragment(),
                new PicMgsFragment(),
                new AppMsgFrament()};
        for (BaseFragment fragment : mClassArray) {
            fragment.setToLoginName(toLoginName);
            fragment.setLoginName(loginName);
        }
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvImptMsg = (TextView) findViewById(R.id.tv_impt_msg);
        mTvFileMsg = (TextView) findViewById(R.id.tv_file_msg);
        mTvPicMsg = (TextView) findViewById(R.id.tv_pic_msg);
        mTvAppMsg = (TextView) findViewById(R.id.tv_app_msg);
        mTvSendToMe = (TextView) findViewById(R.id.tv_send_to_me);
        mTvImptMsg.setOnClickListener(this);
        mTvFileMsg.setOnClickListener(this);
        mTvPicMsg.setOnClickListener(this);
        mTvAppMsg.setOnClickListener(this);
        mTvSendToMe.setOnClickListener(this);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

//		mShowFragmentId=R.id.tv_impt_msg;
//		mTvImptMsg.setSelected(true);
//		showFragment(0);


        mRelNoDataView = (RelativeLayout) findViewById(R.id.rel_no_data_show);
        mIvNoDataIcon = (ImageView) findViewById(R.id.iv_no_data_msg);
        mTvTitle = (TextView) findViewById(R.id.tv_no_data_msg);
        mTvToast = (TextView) findViewById(R.id.tv_no_data_msg_show);


        mShowFragmentId = R.id.tv_send_to_me;
        mTvSendToMe.setSelected(true);
        if (toLoginName != null && toLoginName.contains("@ChatRoom")) {
            showFragment(0);
        } else {
            showFragment(1);
        }
        noShowToMe();
    }

    /**
     * 是否显示@我
     */
    private void noShowToMe() {
        if (toLoginName != null && !toLoginName.contains("@ChatRoom")) {
            mTvSendToMe.setVisibility(View.GONE);
            mTvImptMsg.setSelected(true);
            mTvImptMsg.setBackgroundDrawable(getResources().getDrawable(R.drawable.msg_record_left_bg_color));
//			mTvImptMsg.setBackground(getResources().getDrawable(R.drawable.msg_record_left_bg_color));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mShowFragmentId) {
            return;
        } else {
            mShowFragmentId = id;
            resetViewSlelected();
            v.setSelected(true);
        }
        mRelNoDataView.setVisibility(View.GONE);
        switch (id) {
            case R.id.tv_impt_msg:
                showFragment(1);
                break;
            case R.id.tv_file_msg:
                showFragment(2);
                break;
            case R.id.tv_pic_msg:
                showFragment(3);
                break;
            case R.id.tv_app_msg:
                showFragment(4);
                break;
            case R.id.tv_send_to_me:
                showFragment(0);
            default:
                break;
        }
    }

    private void resetViewSlelected() {
        mTvImptMsg.setSelected(false);
        mTvFileMsg.setSelected(false);
        mTvPicMsg.setSelected(false);
        mTvAppMsg.setSelected(false);
        mTvSendToMe.setSelected(false);
    }

    private void showFragment(int i) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, mClassArray[i]);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == ENTER_SEARCH_RESULT) {
            mClassArray[1] = new ImptMgsFragment();
            mClassArray[1].setLoginName(loginName);
            mClassArray[1].setToLoginName(toLoginName);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, mClassArray[1]);
            transaction.commit();
        }
    }


    /**
     * 设置@我无数据view
     */
    public void setSendToMeNoData() {
        mRelNoDataView.setVisibility(View.VISIBLE);
        mIvNoDataIcon.setImageResource(R.drawable.icon_no_data_ask);
        mTvTitle.setText(R.string.message_no_data_send_to_me);
        mTvToast.setText(R.string.message_no_data_send_to_me_toast);
    }


    /**
     * 标记重点无数据view
     */
    public void setMarkPointNoData() {
        mRelNoDataView.setVisibility(View.VISIBLE);
        mIvNoDataIcon.setImageResource(R.drawable.icon_no_data_point);
        mTvTitle.setText(R.string.message_no_data_point);
        mTvToast.setText(R.string.message_no_data_point_toast);
    }


    /**
     * 文件无数据view
     */
    public void setFileNoData() {
        mRelNoDataView.setVisibility(View.VISIBLE);
        mIvNoDataIcon.setImageResource(R.drawable.icon_no_data_file);
        mTvTitle.setText(R.string.message_no_data_file);
        mTvToast.setText(R.string.message_no_data_file_toast);
    }

    /**
     * 图片无数据view
     */
    public void setPicNoData() {
        mRelNoDataView.setVisibility(View.VISIBLE);
        mIvNoDataIcon.setImageResource(R.drawable.icon_no_data_pic);
        mTvTitle.setText(R.string.message_no_data_pic);
        mTvToast.setText(R.string.message_no_data_pic_toast);
    }

    /**
     * 应用无数据viewbu
     */
    public void setAppNoData() {
        mRelNoDataView.setVisibility(View.VISIBLE);
        mIvNoDataIcon.setImageResource(R.drawable.icon_no_data_app);
        mTvTitle.setText(R.string.message_no_data_app);
        mTvToast.setText(R.string.message_no_data_app_toast);
    }


}












