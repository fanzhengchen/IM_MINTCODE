package com.mintcode.launchr.activity.workhub;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.BackPasswordMailPOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.BackPasswordMailEntity;
import com.mintcode.launchr.util.TTJSONUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by StephenHe on 2016/4/8.
 */
public class GetBackMailActivity extends BaseActivity {
    /** 返回*/
    private ImageView mIvBack;

    /** 发送前的布局*/
    private RelativeLayout mRelBeforeContent;

    /** 发出后的布局*/
    private RelativeLayout mRelAfterContent;

    /** 发送邮件*/
    private Button mBtnSendMail;

    /** 重新发送*/
    private TextView mTvAgainSend;

    /** 用户邮箱*/
    private TextView mTvUserMail;

    /** 输入的邮箱*/
    private EditText mEditInputMail;

    private ImageView mIvMailIcon;

    private TimerCount mTimerCount;

    private List<BackPasswordMailEntity.EmailEntity> mEmailModels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_forget_back_mail);

        initView();
    }

    private void initView(){
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvMailIcon = (ImageView) findViewById(R.id.iv_mail_icon);
        mBtnSendMail = (Button) findViewById(R.id.btn_send_mail);
        mTvAgainSend = (TextView) findViewById(R.id.tv_again_send);
        mTvUserMail = (TextView) findViewById(R.id.tv_user_mail);
        mRelBeforeContent = (RelativeLayout) findViewById(R.id.rel_send_before);
        mRelAfterContent = (RelativeLayout) findViewById(R.id.rel_send_after);
        mEditInputMail = (EditText) findViewById(R.id.edit_input_mail);

        mIvBack.setOnClickListener(this);
        mBtnSendMail.setOnClickListener(this);
        mTvAgainSend.setOnClickListener(this);

        mTimerCount = new TimerCount(60000, 1000);

        mEmailModels = new ArrayList<BackPasswordMailEntity.EmailEntity>();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mIvBack){
            finish();
        }else if(v == mBtnSendMail){
            sendMailForBack();
        }else if(v == mTvAgainSend){
            sendMailAgain();
        }
    }

    /** 发送*/
    private void sendMailForBack(){
        if(mEditInputMail.getText().toString()==null || mEditInputMail.getText().toString().equals("")){
            toast(getString(R.string.mail_not_space));
            return;
        }

        if(mBtnSendMail.getText().toString().equals(getString(R.string.send_mail_back_password))){
            HeaderParam mHeaderParam = LauchrConst.getHeader(this);
            String code = "";
            if(mHeaderParam.getCompanyShowID()!=null && !mHeaderParam.getCompanyShowID().equals("")){
                code = getCharAndNumr(6) + "_" + mHeaderParam.getCompanyShowID();
//                code = getCharAndNumr(6) + getCharAndNumr(6) + "_" + "35b11f42f4522d8923";
            }else{
                code = getCharAndNumr(6) + getCharAndNumr(24);
            }
            UserApi.getInstance().getUserForgetPassword(this, mEditInputMail.getText().toString().trim(), getCharAndNumr(28), 2, code, 1440);

            showLoading();
        }else{
            finish();
        }
    }

    /** 重新发送*/
    private void sendMailAgain(){
        if(mTvAgainSend.getText().toString().equals(getString(R.string.again_send))){
            showLoading();

            HeaderParam mHeaderParam = LauchrConst.getHeader(this);
            String code = "";
            if(mHeaderParam.getCompanyShowID()!=null && !mHeaderParam.getCompanyShowID().equals("")){
//                code = getCharAndNumr(6) + getCharAndNumr(6) + "_" + "35b11f42f4522d8923";
                code = getCharAndNumr(6) + "_" + mHeaderParam.getCompanyShowID();
            }else{
                code = getCharAndNumr(6) + getCharAndNumr(24);
            }
            UserApi.getInstance().getUserForgetPassword(this, mTvUserMail.getText().toString().trim(), getCharAndNumr(28), 2, code, 1440);
        }
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        if(response == null){
            showNoNetWork();
            return;
        }

        if(UserApi.TaskId.TASK_URL_GET_BACK_MAIL_PASSWORD.equals(taskId)){
            handleForgetPassword(response);
        }else if(UserApi.TaskId.TASK_URL_GET_BACK_MAIL_PASSWORD1.equals(taskId)){
            dismissLoading();
            handleForgetPassword1(response);
        }
    }

    private void handleForgetPassword(Object response){
        Log.i("handleForgetPassword", response.toString());
        BackPasswordMailPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), BackPasswordMailPOJO.class);
        if(pojo==null){
            return;
        }else if(!pojo.isResultSuccess()){
            return;
        }else if(pojo.getBody().getResponse().getData()!=null){
            BackPasswordMailEntity entity = pojo.getBody().getResponse().getData();

            // 内网
            String goLink = "http://192.168.1.249:6006" + "/companyuser/setpassword?key=" +
                    entity.getuValidatorToken() + "_" + entity.getuValidatorCode() + "&type=forgetpassword";
            String url = "http://192.168.1.249:6006" + "/";

            // 外网
//            String goLink = LauchrConst.SERVER_PATH + "/companyuser/setpassword?key=" +
//                    entity.getuValidatorToken() + "_" + entity.getuValidatorCode() + "&type=forgetpassword";
//            String url =LauchrConst.SERVER_PATH + "/";
            BackPasswordMailEntity.EmailModel emailModel = new BackPasswordMailEntity.EmailModel();
            emailModel.setUrl(url);
            emailModel.setGoalLink(goLink);
            emailModel.setUserEmail(entity.getuEmail());

            BackPasswordMailEntity.EmailEntity emailEntity = new BackPasswordMailEntity.EmailEntity();
            emailEntity.setuEmail(entity.getuEmail());
            emailEntity.setuEmailContent(TTJSONUtil.convertObjToJson(emailModel));
            emailEntity.setIsHtml(1);
            emailEntity.setValidatorId(entity.getId()+"");

            mEmailModels.add(emailEntity);

            String emailSubject = getString(R.string.workhub_back_mail);
            String serviceName = "SMTP";
            // 外网
//            String fromEmail = "support@workhub.jp";
//            String serviceData = "{'fromPassword':'TGF1bmNocjIwMTUh','host':'smtp.gmail.com','port':587,'useSSL':1}";

            // 内网
            String fromEmail = "supportlaunchr@163.com";
            String serviceData = "{'fromPassword':'Z2VteXFqZGl3dXNpemZyYw==','host':'smtp.163.com','port':25,'useSSL':0}";
            UserApi.getInstance().getUserForgetPassword1(this, mEmailModels, serviceName, emailSubject, fromEmail, serviceData, "forgetpassword");
        }
    }

    private void handleForgetPassword1(Object response) {
        Log.i("handleForgetPassword1", response.toString());
        mTimerCount.start();

        mIvMailIcon.setImageResource(R.drawable.icon_login_back_mail1);
        mRelBeforeContent.setVisibility(View.GONE);
        mRelAfterContent.setVisibility(View.VISIBLE);
        mBtnSendMail.setText(getString(R.string.back_login));
        mTvUserMail.setText(mEditInputMail.getText().toString().trim());
    }

    /** 60秒后重新发送*/
    private class TimerCount extends CountDownTimer{
        public TimerCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTvAgainSend.setTextColor(getResources().getColor(R.color.chat_below_text));
            mTvAgainSend.setText(getString(R.string.send_mail_again).replace("xx",(millisUntilFinished/1000) + ""));
        }

        @Override
        public void onFinish() {
            mTvAgainSend.setTextColor(getResources().getColor(R.color.blue_launchr));
            mTvAgainSend.setText(getString(R.string.again_send));
        }
    }

    /**
     * java生成随机数字和字母组合
     */
    public static String getCharAndNumr(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

}
