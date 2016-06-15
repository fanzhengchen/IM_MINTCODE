package com.mintcode.launchr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.activity.workhub.ForgetPasswordActivity;
import com.mintcode.launchr.activity.workhub.WorkHubMyTeamActivity;
import com.mintcode.launchr.activity.workhub.WorkHubSignActivity;
import com.mintcode.launchr.api.IMAPI;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.LoginPOJO;
import com.mintcode.launchr.pojo.LoginValidatePOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.CompanyEntity;
import com.mintcode.launchr.pojo.entity.LoginValidateEntity;
import com.mintcode.launchr.pojo.entity.UserEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.MixPanelUtil;
import com.mintcode.launchr.util.PersonalInfoUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchrnetwork.OnResponseListener;
import com.way.view.gesture.CreateGesturePasswordActivity;
import com.way.view.gesture.UnlockGesturePasswordActivity;

import org.litepal.LitePalManager;

import java.io.Serializable;
import java.util.List;

/**
 * Created by StephenHe on 2016/4/6.
 */
public class LoginWorkHubActivity extends BaseActivity implements OnResponseListener, View.OnFocusChangeListener {
    /**
     * 账号登录
     */
    private CheckBox mCbAccount;
    private LinearLayout mLinAccount;

    /**
     * 验证码登录
     */
    private CheckBox mCbIdentify;
    private LinearLayout mLinIdentify;

    /**
     * 忘记密码
     */
    private TextView mTvForgotPassword;

    /**
     * 显示登录密码
     */
    private CheckBox mCbShowPassword;

    /**
     * 注册账号
     */
    private TextView mTvAccount;

    /**
     * 登录
     */
    private Button mBtnLogin;

    /**
     * 账号
     */
    private EditText mEditMailAccount;

    /**
     * 登录密码
     */
    private EditText mEditPassword;

    /**
     * 验证码
     */
    private EditText mEditIdentify;
    private TextView mTvGetIdentify;

    /**
     * 手机号
     */
    private EditText mEditPhoneNumber;

    /**
     * 验证码2
     */
    private EditText mEditIdentify2;
    private TextView mTvGetIdentify2;

    private ScrollView mSc;

    /**
     * handler
     */
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new_workhub);

        initView();
    }

    private void initView() {
        mCbAccount = (CheckBox) findViewById(R.id.cb_login_account);
        mCbIdentify = (CheckBox) findViewById(R.id.cb_login_identify);
        mLinAccount = (LinearLayout) findViewById(R.id.lin_login_account);
        mLinIdentify = (LinearLayout) findViewById(R.id.lin_login_identity);
        mTvForgotPassword = (TextView) findViewById(R.id.tv_forgot_password);
        mTvAccount = (TextView) findViewById(R.id.tv_account);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mEditMailAccount = (EditText) findViewById(R.id.edt_user_acount);
        mEditPassword = (EditText) findViewById(R.id.edt_user_passwd);
        mEditIdentify = (EditText) findViewById(R.id.edt_user_identify);
        mEditPhoneNumber = (EditText) findViewById(R.id.edt_user_phone);
        mEditIdentify2 = (EditText) findViewById(R.id.edt_user_identify2);
        mTvGetIdentify = (TextView) findViewById(R.id.tv_get_identify);
        mTvGetIdentify2 = (TextView) findViewById(R.id.tv_get_identify2);
        mCbShowPassword = (CheckBox) findViewById(R.id.cb_show_password);
        mSc = (ScrollView) findViewById(R.id.sc);

        mEditMailAccount.setOnFocusChangeListener(this);
        mEditPassword.setOnFocusChangeListener(this);

        mCbAccount.setOnClickListener(this);
        mCbIdentify.setOnClickListener(this);
        mTvForgotPassword.setOnClickListener(this);
        mTvAccount.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mTvGetIdentify.setOnClickListener(this);
        mTvGetIdentify2.setOnClickListener(this);
        mCbShowPassword.setOnClickListener(this);

//        String hint = mEditMailAccount.getHint().toString() + "/" + getString(R.string.workhub_id);
//        mEditMailAccount.setHint(hint);
        mHandler = new Handler();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mCbAccount) {
            mLinAccount.setVisibility(View.VISIBLE);
            mLinIdentify.setVisibility(View.GONE);
            mCbAccount.setChecked(true);
            mCbIdentify.setChecked(false);
        } else if (v == mCbIdentify) {
            mLinAccount.setVisibility(View.GONE);
            mLinIdentify.setVisibility(View.VISIBLE);
            mCbAccount.setChecked(false);
            mCbIdentify.setChecked(true);
        } else if (v == mTvForgotPassword) {
            Intent intent = new Intent(this, ForgetPasswordActivity.class);
            startActivity(intent);
        } else if (v == mTvAccount) {
            Intent intent = new Intent(this, WorkHubSignActivity.class);
            startActivity(intent);
        } else if (v == mBtnLogin) {
            accessLogin();
        } else if (v == mTvGetIdentify) {

        } else if (v == mTvGetIdentify2) {

        } else if (v == mCbShowPassword) {
            showPassword();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v == mEditPassword) {
                move();
            }
        } else {

        }
    }

    /**
     * 上移
     */
    private void move() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Log.i("infos", mSc.getHeight() + "=====");
                mSc.scrollTo(0, mSc.getHeight());
            }
        };
        mHandler.postDelayed(r, 100);
    }

    /**
     * 登录处理逻辑
     */
    private void accessLogin() {
        // 获取用户名
        String account = mEditMailAccount.getText().toString().trim();

        if (TextUtils.isEmpty(account)) {
            toast(getResources().getString(R.string.toast_login_name_null));
            return;
        }

        // 获取密码
        String password = mEditPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            toast(getResources().getString(R.string.toast_pass_word_null));
            return;
        }
        MixPanelUtil.sendEvent(this, MixPanelUtil.LOGIN_EVENT);
        // 原登录操作
        UserApi api = UserApi.getInstance();
        api.loginValidate(this, account, password);
        showLoading();
    }

    /**
     * 显示新密码
     */
    private void showPassword() {
        if (mCbShowPassword.isChecked()) {
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

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        if (response == null) {
            showNoNetWork();
            return;
        }

        // 判断是否是登录返回
        if (taskId.equals(UserApi.TaskId.TASK_URL_USER_LOGIN)) {
            handleResultLogin(response);
            dismissLoading();
        } else
            // 判断是否是验证返回
            if (taskId.equals(UserApi.TaskId.TASK_URL_VALIDATE_LOGIN)) {
                handlValidateLogin(response);
                dismissLoading();
            } else if (response instanceof com.mintcode.LoginPOJO) {
                com.mintcode.LoginPOJO pojo = (com.mintcode.LoginPOJO) response;
            } else if (taskId.equals(IMAPI.TASKID.LOGIN)) {
                com.mintcode.LoginPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), com.mintcode.LoginPOJO.class);
                if (pojo.isResultSuccess()) {
                    String token = pojo.getUserToken();
                    AppUtil util = AppUtil.getInstance(this);
                    util.saveIMToken(token);
                    LauchrConst.header = LauchrConst.getHeader(this);

                    PersonalInfoUtil infoUtil = new PersonalInfoUtil(this);
                    int state = infoUtil.getIntVaule(Const.KEY_GESTURE);
                    Intent intent = null;
                    if (state == 1) {
                        intent = new Intent(this, UnlockGesturePasswordActivity.class);
                    } else {
                        intent = new Intent(this, CreateGesturePasswordActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }
            }
    }

    private void handlValidateLogin(Object response) {
        LoginValidatePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), LoginValidatePOJO.class);
        if (pojo != null) {
            if (pojo.isResultSuccess()) {
                LoginValidateEntity entity = pojo.getBody().getResponse().getData();
                if (entity != null) {
                    // 验证完成操作
                    List<CompanyEntity> comList = entity.getCompanyList();

                    if (comList != null) {
                        String account = mEditMailAccount.getText().toString().trim();
                        String password = mEditPassword.getText().toString().trim();
                        Intent intent = new Intent(this, WorkHubMyTeamActivity.class);
                        intent.putExtra("team_entity", (Serializable) comList);
                        intent.putExtra("login_username", account);
                        intent.putExtra("login_pwd", password);

                        AppUtil util = AppUtil.getInstance(this);
                        util.saveValue(LauchrConst.KEY_TEMP_LOGINNAME, account);
                        util.saveValue(LauchrConst.KEY_TEMP_PASSWORD, password);

                        startActivity(intent);
                        finish();
                    }
                } else {
                    toast(pojo.getBody().getResponse().getReason());
                }
            } else {
                toast(pojo.getHeader().getReason());
            }
        } else {
            showNetWorkGetDataException();
        }
    }

    /**
     * 处理登录返回逻辑
     *
     * @param response
     */
    private void handleResultLogin(Object response) {
        LoginPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), LoginPOJO.class);
        if (pojo != null) {
            if (pojo.isResultSuccess()) {
                if (pojo.getBody().getResponse().getData() != null) {
                    AppUtil util = AppUtil.getInstance(this);
                    UserEntity user = pojo.getBody().getResponse().getData();
                    if (user != null) {
                        HeaderParam header = new HeaderParam();

                        // 设置token
                        header.setAuthToken(user.getLAST_LOGIN_TOKEN());
                        header.setCompanyCode(user.getC_CODE());
                        header.setCompanyShowID(user.getC_SHOW_ID());
                        header.setUserName(user.getU_TRUE_NAME());
                        header.setLoginName(user.getU_NAME());
                        header.setLanguage("jp-ja");
                        util.saveHeader(header);

                        String show_ID = user.getU_SHOW_ID();
                        util.saveShowId(show_ID);
                        LitePalManager.reset();
                        LitePalManager.setDbName(show_ID);

                        IMAPI.getInstance().Login(this, show_ID, LauchrConst.appName);
                    } else {
                        toast(pojo.getBody().getResponse().getReason());
                    }
                } else {
                    toast(pojo.getBody().getResponse().getReason());
                }
            } else {
                toast(pojo.getHeader().getReason());
            }
        } else {
            showNetWorkGetDataException();
        }
    }


}
