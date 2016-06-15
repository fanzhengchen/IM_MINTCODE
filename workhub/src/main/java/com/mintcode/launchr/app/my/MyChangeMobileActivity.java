package com.mintcode.launchr.app.my;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.UserDetailPOJO;
import com.mintcode.launchr.pojo.UserInfoNewPOJO;
import com.mintcode.launchr.pojo.entity.UserDetailTempEntity;
import com.mintcode.launchr.util.TTJSONUtil;

/**
 * @author StephenHe 2015/9/17 修改办公室电话号
 */
public class MyChangeMobileActivity extends BaseActivity {
    /**
     * 返回
     */
    private ImageView mIvBack;

    /**
     * 登录密码
     */
    private EditText mEditPassword;

    /**
     * 新手机号
     */
    private EditText mEditNewPhone;

    /**
     * 验证码
     */
    private EditText mEditIdentifyingCode;

    /**
     * 获取验证码
     */
    private TextView mTvGetIdentifyingCode;

    /**
     * 确认修改
     */
    private Button mBtnOk;

    /**
     * 显示登录密码
     */
    private CheckBox mIvShowPassword;

    private UserDetailTempEntity mEntity;

    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_change_mobile);

        initView();
    }

    public void initView() {
        mobile = getIntent().getStringExtra("mobile");

        mIvBack = (ImageView) findViewById(R.id.iv_back);

        mEditPassword = (EditText) findViewById(R.id.edit_login_password);
        mEditNewPhone = (EditText) findViewById(R.id.edit_new_mobile);
        mEditIdentifyingCode = (EditText) findViewById(R.id.edit_identifying_code);
        mTvGetIdentifyingCode = (TextView) findViewById(R.id.tv_identifying_code_get);
        mBtnOk = (Button) findViewById(R.id.btn_ok_change);
        mIvShowPassword = (CheckBox) findViewById(R.id.iv_login_password);

        mIvBack.setOnClickListener(this);

        mTvGetIdentifyingCode.setOnClickListener(this);
        mBtnOk.setOnClickListener(this);
        mIvShowPassword.setOnClickListener(this);

        mEditNewPhone.setText(mobile);

        String userId = LauchrConst.getHeader(this).getLoginName();
        UserApi.getInstance().getComanyUserInfo(getUserMessage, userId);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == mIvBack) {
            finish();
        } else if (v == mTvGetIdentifyingCode) {
            mTvGetIdentifyingCode.setEnabled(false);
            getIdentifyingCode();
        } else if (v == mBtnOk) {
            okChange();
        } else if (v == mIvShowPassword) {
            showPassword();
        }
    }

    com.mintcode.launchrnetwork.OnResponseListener getUserMessage = new com.mintcode.launchrnetwork.OnResponseListener() {
        @Override
        public void onResponse(Object response, String taskId, boolean rawData) {
            if (response == null) {
                return;
            }
            // 判断是否是获取个人详情返回
            if (taskId.equals(UserApi.TaskId.TASK_URL_GET_COMPANY_USER_INFO)) {
                handleUserDetail(response);
                dismissLoading();
            }
        }

        @Override
        public boolean isDisable() {
            return false;
        }
    };

    /**
     * 处理用户信息
     *
     * @param response
     */
    private void handleUserDetail(Object response) {
        UserDetailPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), UserDetailPOJO.class);
        if (pojo != null) {
            if (pojo.isResultSuccess()) {
                mEntity = pojo.getBody().getResponse().getData();
            }
        }
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        if (response == null) {
            showNoNetWork();
            return;
        }

        if (taskId.equals(UserApi.TaskId.TASK_URL_UPDATE_USER_INFO)) {
            UserInfoNewPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), UserInfoNewPOJO.class);
            handleUpdateUserInfo(pojo);
        }
    }

    private void handleUpdateUserInfo(UserInfoNewPOJO pojo) {


        if (pojo == null) {
            return;
        } else if (pojo.getBody() == null) {
            return;
        } else if (pojo.getBody().getResponse().isIsSuccess() == false) {
            toast(pojo.getBody().getResponse().getReason());
            return;
        } else {
            setResult(RESULT_OK);
            this.finish();
        }

    }

    /**
     * 获取验证码
     */
    private void getIdentifyingCode() {

        timer.start();
    }

    /**
     * 确认修改
     */
    private void okChange() {
        String newPhone = mEditNewPhone.getText().toString().trim();
        String userId = LauchrConst.getHeader(this).getLoginName();
        String userName = LauchrConst.getHeader(this).getUserName();
        String userJob = mEntity.getU_JOB();
        String mail = mEntity.getU_MAIL();
        String dept = mEntity.getU_DEPT_ID().get(0);
        String userNumber = mEntity.getU_NUMBER();
        LauchrConst.getHeader(this).getAuthToken();
        if (!"".equals(newPhone)) {
            UserApi.getInstance().updateUserInfo(this, userNumber, dept,userJob, mail, userId, null, userName,newPhone);
        } else {
            toast(getString(R.string.my_tele_is_null));
        }
    }

    /**
     * 显示登录密码
     */
    private void showPassword() {

        if (mIvShowPassword.isChecked()) {
            mEditPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            mEditPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        CharSequence text = mEditPassword.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());// 将光标移动到最后
        }

    }

    private CountDownTimer timer = new CountDownTimer(10000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {

            mTvGetIdentifyingCode.setText(millisUntilFinished / 1000 + "");
        }

        @Override
        public void onFinish() {
            mTvGetIdentifyingCode.setText(R.string.my_change_get_number);
            mTvGetIdentifyingCode.setEnabled(true);
        }
    };

}
