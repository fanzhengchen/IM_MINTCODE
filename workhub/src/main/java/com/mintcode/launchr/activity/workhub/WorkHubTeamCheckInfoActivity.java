package com.mintcode.launchr.activity.workhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.IMAPI;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.LoginPOJO;
import com.mintcode.launchr.pojo.SignExistPOJO;
import com.mintcode.launchr.pojo.SignSuccessPOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.SignExistEntity;
import com.mintcode.launchr.pojo.entity.SignSuccessEntity;
import com.mintcode.launchr.pojo.entity.UserEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.PersonalInfoUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchrnetwork.OnResponseListener;
import com.way.view.gesture.CreateGesturePasswordActivity;
import com.way.view.gesture.UnlockGesturePasswordActivity;

import org.litepal.LitePalManager;

/**
 * Created by JulyYu on 2016/4/7.
 */
public class WorkHubTeamCheckInfoActivity extends BaseActivity implements OnResponseListener {
    private ImageView mIvBack;
    private TextView mTvPhone;
    private TextView mTvMail;
    private TextView mTvTeamNet;
    private TextView mTvTeamName;
    private TextView mTvName;
    private Button mBtnLogin;

    private String phone;
    private String name;
    private String teamNet;
    private String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workhub_check_info);
        initView();
    }

    private void initView(){
        teamName = getIntent().getStringExtra("team_name");
        name = getIntent().getStringExtra("name");
        teamNet = getIntent().getStringExtra("team_net");

        mIvBack = (ImageView)findViewById(R.id.iv_back);
        mTvPhone = (TextView)findViewById(R.id.tv_phone);
        mTvMail = (TextView)findViewById(R.id.tv_mail);
        mTvTeamNet = (TextView)findViewById(R.id.tv_team_net);
        mTvTeamName = (TextView)findViewById(R.id.tv_team_name);
        mTvName = (TextView)findViewById(R.id.tv_name);
        mBtnLogin = (Button)findViewById(R.id.btn_login);

        mIvBack.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);

        if(phone!=null){
            mTvPhone.setText(phone);
        }
        if(teamNet!=null){
            mTvTeamNet.setText("http://launchr.jp/a/" + teamNet);
        }
        if(name!=null){
            mTvName.setText(name);
        }
        if(teamName!=null){
            mTvTeamName.setText(teamName);
        }

        String userMail = AppUtil.getInstance(this).getValue(LauchrConst.KEY_TEMP_LOGINNAME);
        mTvMail.setText(userMail);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mIvBack){
            this.finish();
        }else if(v == mBtnLogin){
            startLogin();
        }
    }

    private void startLogin(){
        showLoading();
        UserApi.getInstance().companyIsExist(this, teamName, 0);
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        dismissLoading();
        if(response == null){
            showNoNetWork();
            return;
        }

        if(taskId.equals(UserApi.TaskId.TASK_URL_COMPANY_IS_EXIST)){
            handlerTeamIsExist(response);
        }else if(taskId.equals(UserApi.TaskId.TASK_URL_NEW_COMPANY)){
            handleCreateTeam(response);
        }
        // 判断是否是验证返回
        else if (taskId.equals(UserApi.TaskId.TASK_URL_USER_LOGIN)) {
            handleResultLogin(response);
            dismissLoading();
        }
        else if(taskId.equals( IMAPI.TASKID.LOGIN)){
            com.mintcode.LoginPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), com.mintcode.LoginPOJO.class);
            if (pojo.isResultSuccess()) {
                String token = pojo.getUserToken();
                AppUtil util = AppUtil.getInstance(this);
                util.saveIMToken(token);

                PersonalInfoUtil infoUtil = new PersonalInfoUtil(this);
                int state = infoUtil.getIntVaule(Const.KEY_GESTURE);
                Intent intent = null;
                if(state == 1){
                    intent = new Intent(this, UnlockGesturePasswordActivity.class);
                }else {
                    intent = new Intent(this, CreateGesturePasswordActivity.class);
                }
                startActivity(intent);
                LauchrConst.header = LauchrConst.getHeader(this);
                finish();
            }
        }
    }

    /**
     * 处理登录返回逻辑
     * @param response
     */
    private void handleResultLogin(Object response){
        LoginPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), LoginPOJO.class);
        if (pojo != null) {
            if (pojo.isResultSuccess()) {
                if(pojo.getBody().getResponse().getData() != null){
                    AppUtil util = AppUtil.getInstance(this);
                    //

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
                        LitePalManager.setDbName(show_ID + "_" + user.getC_CODE());
                        IMAPI.getInstance().Login(this, show_ID, LauchrConst.appName);
                    }else{
                        toast(pojo.getBody().getResponse().getReason());
                    }

                }else{
                    toast(pojo.getBody().getResponse().getReason());
                }
            }else{
                toast(pojo.getHeader().getReason());
            }
        }else{
            showNetWorkGetDataException();
        }
    }

    /** 企业是否存在*/
    private void handlerTeamIsExist(Object response) {
        SignExistPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), SignExistPOJO.class);
        if (pojo == null) {
            return;
        } else if (!pojo.isResultSuccess()) {
            toast(pojo.getHeader().getReason());
            return;
        } else if (pojo.getBody().getResponse().getData() != null) {
            SignExistEntity entity = pojo.getBody().getResponse().getData();
            if(entity.getIsExist() == 1){
               toast(getString(R.string.company_is_exist));
            }else{
                String userMail = AppUtil.getInstance(this).getValue(LauchrConst.KEY_TEMP_LOGINNAME);

                UserApi.getInstance().setNewComany(this, teamNet, userMail, name, teamName);
            }
        }
    }

    /** 创建企业*/
    private void handleCreateTeam(Object response){
        Log.i("--handleCreateTeam", response.toString());
        SignSuccessPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), SignSuccessPOJO.class);
        if(pojo == null){
            return;
        }else if(!pojo.isResultSuccess()){
            toast(pojo.getHeader().getReason());
            return;
        }else if(pojo.getBody().getResponse().getData()!=null){
            SignSuccessEntity entity = pojo.getBody().getResponse().getData();
            if(entity!=null){
                AppUtil util = AppUtil.getInstance(this);
                HeaderParam header = new HeaderParam();

                // 设置token
                header.setAuthToken(entity.getLAST_LOGIN_TOKEN());
                header.setCompanyCode(teamNet);
                header.setCompanyShowID(entity.getC_SHOW_ID());
                header.setUserName(entity.getU_TRUE_NAME());
                header.setLoginName(entity.getU_NAME());
                header.setLanguage("jp-ja");
                util.saveHeader(header);

                String show_ID = entity.getU_NAME();
                util.saveShowId(show_ID);
                LitePalManager.reset();
                LitePalManager.setDbName(show_ID + "_" + teamNet);
                Log.i("LitePalManager", "===loginSelectTeam===");
                Log.i("LitePalManager", show_ID + "_" + teamNet);
                IMAPI.getInstance().Login(this, show_ID, LauchrConst.appName);
            }
        }
    }
}
