package com.mintcode.launchr.activity.workhub;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.activity.WebViewActivity;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;

/**
 * Created by JulyYu on 2016/4/7.
 */
public class WorkHubCreateTeamActivity extends BaseActivity {
    private ImageView mIvBack;
    private EditText mEdtTeamName;
    private EditText mEdtTeamNet;
    private EditText mEdtName;

    private Button mBtnNext;

    private CheckBox mCbAgree;

    private CheckBox mCbAcceptMail;

    /** 服务条款*/
//    private TextView mTvServiceItem;
    /** 隐私政策*/
//    private TextView mTvPrivacyItem;
    /**
     * 服务条款  隐私政策
     */
    private TextView mTvAgreeService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workhub_team_create);
        initView();
    }

    private void initView() {

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mEdtTeamName = (EditText) findViewById(R.id.edt_team_name);
        mEdtTeamNet = (EditText) findViewById(R.id.edt_team_net);
        mEdtName = (EditText) findViewById(R.id.edt_create_name);
        mBtnNext = (Button) findViewById(R.id.btn_next);
        mCbAgree = (CheckBox) findViewById(R.id.cb_agree);
        mCbAcceptMail = (CheckBox) findViewById(R.id.cb_get_mail);
        mTvAgreeService = (TextView) findViewById(R.id.tv_agree_service_privacy);
        SpannableString spanableInfo = new SpannableString(getString(R.string.i_am_agree_service_privacy));
        int firsStar = spanableInfo.toString().indexOf(getString(R.string.service_item));
        int firstEnd = firsStar + getString(R.string.service_item).length();
        int secondStar = spanableInfo.toString().indexOf(getString(R.string.privacy_item));
        int secondEnd = secondStar + getString(R.string.privacy_item).length();
        //  1-服务条款页面； 2-隐私政策页面
        spanableInfo.setSpan(new Clickable(1), firsStar, firstEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanableInfo.setSpan(new Clickable(2), secondStar, secondEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvAgreeService.setText(spanableInfo);
        mTvAgreeService.setMovementMethod(LinkMovementMethod.getInstance());
//        mTvServiceItem = (TextView) findViewById(R.id.tv_service_item);
//        mTvPrivacyItem = (TextView) findViewById(R.id.tv_privacy_item);
//
//        mTvServiceItem.setOnClickListener(this);
//        mTvPrivacyItem.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
        mCbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && mCbAcceptMail.isChecked()) {
                    mBtnNext.setEnabled(true);
                    mBtnNext.setBackgroundResource(R.drawable.bg_blue_round);
                } else {
                    mBtnNext.setEnabled(false);
                    mBtnNext.setBackgroundResource(R.drawable.bg_gray_round);
                }
            }
        });
        mCbAcceptMail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && mCbAgree.isChecked()) {
                    mBtnNext.setEnabled(true);
                    mBtnNext.setBackgroundResource(R.drawable.bg_blue_round);
                } else {
                    mBtnNext.setEnabled(false);
                    mBtnNext.setBackgroundResource(R.drawable.bg_gray_round);
                }
            }
        });

        mBtnNext.setEnabled(true);
        mBtnNext.setBackgroundResource(R.drawable.bg_blue_round);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mIvBack) {
            this.finish();
        } else if (v == mBtnNext) {
            getTeamInfo();
        }
//        }else if(v == mTvServiceItem){
//            Intent intent = new Intent(this,WebViewActivity.class);
//            intent.putExtra(LauchrConst.KEY_URL, "https://www.workhub.jp/terms");
//            startActivity(intent);
//        }else if(v == mTvPrivacyItem){
//            Intent intent = new Intent(this,WebViewActivity.class);
//            intent.putExtra(LauchrConst.KEY_URL, "https://www.workhub.jp/privacy");
//            startActivity(intent);
//        }
    }

    public void getTeamInfo() {
        String teamName = mEdtTeamName.getText().toString();
        String teamNet = mEdtTeamNet.getText().toString();
        String name = mEdtName.getText().toString();
        if (teamName == null && "".equals(teamName)) {
            toast(getString(R.string.team_name_not_space));
        } else if (teamNet == null && "".equals(teamNet)) {
            toast(getString(R.string.team_net_not_space));
        } else if (name == null && "".equals(name)) {
            toast(getString(R.string.team_uName_not_space));
        } else if (mCbAgree.isChecked()) {
            Intent i = new Intent(this, WorkHubTeamCheckInfoActivity.class);
            i.putExtra("team_name", mEdtTeamName.getText().toString().trim());
            i.putExtra("team_net", mEdtTeamNet.getText().toString().trim());
            i.putExtra("name", mEdtName.getText().toString().trim());
            startActivity(i);
        }
    }

    class Clickable extends ClickableSpan {
        // 1-跳转到服务条款页面； 2-隐私政策页面
        private int type;

        public Clickable(int type) {
            super();
            this.type = type;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(getResources().getColor(R.color.blue_launchr));
            ds.setUnderlineText(true);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(WorkHubCreateTeamActivity.this, WebViewActivity.class);
            if (type == 1) {
                intent.putExtra(LauchrConst.KEY_URL, "https://www.workhub.jp/terms");
            } else {
                intent.putExtra(LauchrConst.KEY_URL, "https://www.workhub.jp/privacy");
            }
            startActivity(intent);
        }
    }
}
