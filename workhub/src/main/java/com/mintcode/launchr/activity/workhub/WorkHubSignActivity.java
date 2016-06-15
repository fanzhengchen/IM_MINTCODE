package com.mintcode.launchr.activity.workhub;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.activity.WebViewActivity;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.BasePOJO;
import com.mintcode.launchr.pojo.SignExistPOJO;
import com.mintcode.launchr.pojo.SignSuccessPOJO;
import com.mintcode.launchr.pojo.entity.SignEntity;
import com.mintcode.launchr.pojo.entity.SignExistEntity;
import com.mintcode.launchr.util.MixPanelUtil;
import com.mintcode.launchr.util.RegularUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.view.SignExistDialog;


/**
 * Created by JulyYu on 2016/4/8.
 */
public class WorkHubSignActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, TextWatcher {


    private ImageView mIvBack;
    private RadioGroup mRgSelect;
    private Button mBtnNext;
    private TextView mTvTitle;

    private LinearLayout mLlLoad;
    private ImageView mIvCricle1;
    private ImageView mIvLine1;
    private ImageView mIvCricle2;
    private ImageView mIvLine2;
    private ImageView mIvCricle3;
    private ImageView mIvLine3;
    private ImageView mIvCricle4;

    private View mViewPhoneSign;
    private LinearLayout mLlNation;
    private LinearLayout mLlPhone;
    private LinearLayout mLlMail;
    private TextView mTvNation;
    private EditText mEdtPhone;
    private EditText mEdtMail;

    private View mViewPhonePassword;
    private LinearLayout mLlCode;
    private EditText mEdtCode;
    private TextView mTvGetCode;
    private EditText mEdtPassWord;
    private CheckBox mCbSeePassWord;
    private ImageView mIvPassLow;
    private ImageView mIvPassMiddle;
    private ImageView mIvPassHeight;

    private View mViewTeamCreate;
    private TextView mTvAgreeService;
    private EditText mEdtTeamName;
    private EditText mEdtTeamNet;
    private EditText mEdtName;

    private View mViewCheckInfo;
    private TextView mTvPhone;
    private TextView mTvMail;
    private TextView mTvTeamNet;
    private TextView mTvTeamName;
    private TextView mTvName;
    /** 所在注册页码*/
    private int mIntNext = 1;
    /** 注册类型*/
    private int mIntSignType = 2;
    private Drawable mDrawGray;
    private Drawable mDrawRed;
    private Drawable mDrawYellow;
    private Drawable mDrawGreen;
    private Drawable mDrawBlue;
    private Drawable mDrawCircleGray;
    private Drawable mDrawCircleBlue;

    private String mNewPassWd = "";
    /**密码可输字符**/
    private String mDigits = "\"0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLIMNOPQRSTUVWXYZ~`!@#$%^&*()_-+={[}]|\\:;'<,.>?/";
    /** 个数*/
    private int mIntCount = 0;

    private SignEntity mSignEntity = new SignEntity();

    private String mStrPass;
    private String mStrTeam;
    private String mStrCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workhub_sign);
        initView();
    }

    private void initView(){

        mIvBack = (ImageView)findViewById(R.id.iv_back);
        mBtnNext = (Button)findViewById(R.id.btn_next);
        mRgSelect = (RadioGroup)findViewById(R.id.rg_sign_select);
        mTvTitle = (TextView)findViewById(R.id.tv_title);
        initSignLoad();
        initSignView();
        initPassWord();
        initTeam();
        initCheckInfo();

        mIvBack.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
        mRgSelect.setOnCheckedChangeListener(this);

        mStrPass = getResources().getString(R.string.password_setting);
        mStrTeam = getResources().getString(R.string.create_team);
        mStrCheck = getResources().getString(R.string.team_check_info);
    }

    private void initSignLoad(){

        mDrawGray = getResources().getDrawable(R.drawable.shape_line_gray);
        mDrawRed = getResources().getDrawable(R.drawable.shape_line_red);
        mDrawYellow = getResources().getDrawable(R.drawable.shape_line_yellow);
        mDrawGreen = getResources().getDrawable(R.drawable.shape_line_green);
        mDrawBlue = getResources().getDrawable(R.drawable.shape_line_blue);
        mDrawCircleGray = getResources().getDrawable(R.drawable.circle_gray);
        mDrawCircleBlue = getResources().getDrawable(R.drawable.circle_blue);

        mLlLoad = (LinearLayout)findViewById(R.id.ll_sign_load);
        mIvCricle1 = (ImageView)findViewById(R.id.iv_circle1);
        mIvCricle2 = (ImageView)findViewById(R.id.iv_circle2);
        mIvCricle3 = (ImageView)findViewById(R.id.iv_circle3);
        mIvCricle4 = (ImageView)findViewById(R.id.iv_circle4);
        mIvLine1 = (ImageView)findViewById(R.id.iv_line1);
        mIvLine2 = (ImageView)findViewById(R.id.iv_line2);
        mIvLine3 = (ImageView)findViewById(R.id.iv_line3);

        mIvCricle1.setImageDrawable(mDrawCircleBlue);
        mIvCricle2.setImageDrawable(mDrawCircleBlue);
        mIvLine1.setImageDrawable(mDrawBlue);
        mLlLoad.setVisibility(View.GONE);
    }

    private void initSignView(){
        mViewPhoneSign = findViewById(R.id.include_phone_sign);
        mLlNation = (LinearLayout)mViewPhoneSign.findViewById(R.id.ll_select_nation);
        mLlPhone = (LinearLayout)mViewPhoneSign.findViewById(R.id.ll_phone_sign);
        mLlMail = (LinearLayout)mViewPhoneSign.findViewById(R.id.ll_mail_sign);
        mTvNation = (TextView)mViewPhoneSign.findViewById(R.id.tv_nation_local);
        mEdtPhone = (EditText)mViewPhoneSign.findViewById(R.id.edt_phone);
        mEdtMail = (EditText)mViewPhoneSign.findViewById(R.id.edt_mail);
        mLlMail.setVisibility(View.VISIBLE);
        mLlPhone.setVisibility(View.GONE);
    }

    private void initPassWord(){

        mViewPhonePassword = findViewById(R.id.include_phone_password);
        mLlCode = (LinearLayout)mViewPhonePassword.findViewById(R.id.ll_ruth_code);
        mEdtCode = (EditText)mViewPhonePassword.findViewById(R.id.edt_auth_code);
        mTvGetCode = (TextView)mViewPhonePassword.findViewById(R.id.tv_get_code);
        mEdtPassWord = (EditText)mViewPhonePassword.findViewById(R.id.edt_password);
        mCbSeePassWord = (CheckBox)mViewPhonePassword.findViewById(R.id.cb_see_password);
        mIvPassLow = (ImageView)mViewPhonePassword.findViewById(R.id.iv_paddword_line1);
        mIvPassMiddle = (ImageView)mViewPhonePassword.findViewById(R.id.iv_paddword_line2);
        mIvPassHeight = (ImageView)mViewPhonePassword.findViewById(R.id.iv_paddword_line3);
        mViewPhonePassword.setVisibility(View.GONE);

        mCbSeePassWord.setOnClickListener(this);
        mEdtPassWord.addTextChangedListener(this);

    }

    private void initTeam(){

        mViewTeamCreate = findViewById(R.id.include_team);
        mEdtTeamName = (EditText)mViewTeamCreate.findViewById(R.id.edt_team_name);
        mEdtTeamNet = (EditText)mViewTeamCreate.findViewById(R.id.edt_team_net);
        mEdtName = (EditText)mViewTeamCreate.findViewById(R.id.edt_create_name);
        mTvAgreeService = (TextView)mViewTeamCreate.findViewById(R.id.tv_agree_service_privacy);
        SpannableString spanableInfo = new SpannableString(getString(R.string.i_am_agree_service_privacy));
        int firsStar = spanableInfo.toString().indexOf(getString(R.string.service_item));
        int firstEnd = firsStar + getString(R.string.service_item).length();
        int secondStar = spanableInfo.toString().indexOf(getString(R.string.privacy_item));
        int secondEnd = secondStar + getString(R.string.privacy_item).length();
        //  1-服务条款页面； 2-隐私政策页面
        spanableInfo.setSpan(new Clickable(1), firsStar,firstEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanableInfo.setSpan(new Clickable(2),secondStar,secondEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvAgreeService.setText(spanableInfo);
        //setMovementMethod()该方法必须调用，否则点击事件不响应
        mTvAgreeService.setMovementMethod(LinkMovementMethod.getInstance());
        mViewTeamCreate.setVisibility(View.GONE);
    }

    private void initCheckInfo(){

        mViewCheckInfo = findViewById(R.id.include_check_info);
        mTvPhone = (TextView)mViewCheckInfo.findViewById(R.id.tv_phone);
        mTvMail = (TextView)mViewCheckInfo.findViewById(R.id.tv_mail);
        mTvTeamNet = (TextView)mViewCheckInfo.findViewById(R.id.tv_team_net);
        mTvTeamName = (TextView)mViewCheckInfo.findViewById(R.id.tv_team_name);
        mTvName = (TextView)mViewCheckInfo.findViewById(R.id.tv_name);
        mViewCheckInfo.setVisibility(View.GONE);
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
            Intent intent = new Intent(WorkHubSignActivity.this,WebViewActivity.class);
            if (type == 1) {
                intent.putExtra(LauchrConst.KEY_URL, "https://www.workhub.jp/terms");
            } else {
                intent.putExtra(LauchrConst.KEY_URL, "https://www.workhub.jp/privacy");
            }
            startActivity(intent);
//            Uri uri = Uri.parse("https://www.workhub.jp/terms");
//            Intent it = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(it);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mIvBack){
            setPage(false);
        }else if(v == mBtnNext){
            setPage(true);
        }else if(v == mCbSeePassWord){
            showNewPassword();
        }
    }

    /**
     * 显密码
     */
    private void showNewPassword() {

        if(mCbSeePassWord.isChecked()){
            mEdtPassWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }else{
            mEdtPassWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        CharSequence text = mEdtPassWord.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());// 将光标移动到最后
        }
    }

    private boolean checkSign(boolean tag){
        if(tag){
            if(mIntSignType == 1){
                String phone  =  mEdtPhone.getText().toString();
                if(phone != null && !"".equals(phone)){
                    if(!RegularUtil.isMobile(phone)){

                    }else{
                        toast(getResources().getString(R.string.my_phone_is_error));
                        return  false;
                    }
                }else{
                    toast(getResources().getString(R.string.input_phone));
                    return  false;
                }
            }else if(mIntSignType == 2){
                String mail = mEdtMail.getText().toString();
                if(mail != null && !"".equals(mail)){
                    if(!RegularUtil.isEmail(mail)){
                        mSignEntity.setMail(mail);
                        showLoading();
                        UserApi.getInstance().mailIsExist(this, mail);
                    }else{
                        toast(getResources().getString(R.string.mail_address_error));
                        return  false;
                    }
                }else{
                    toast(getResources().getString(R.string.mail_not_space));
                    return  false;
                }
            }
        }
        return  true;
    }
    private void viewPassword(boolean tag){

        int gone = (tag == true ? View.GONE : View.VISIBLE);
        int visible = (tag == true ? View.VISIBLE : View.GONE);
        mViewPhoneSign.setVisibility(gone);
//        mRgSelect.setVisibility(gone);
        mViewPhonePassword.setVisibility(visible);
        mTvTitle.setVisibility(visible);
        mLlLoad.setVisibility(visible);
        mTvTitle.setText(mStrPass);
        if(mIntSignType == 1){
            mLlCode.setVisibility(View.VISIBLE);
        }else if(mIntSignType == 2){
            mLlCode.setVisibility(View.GONE);
        }

        if(tag){
            mIntNext ++;
        }else{
            mIntNext --;
        }
    }
    private boolean checkSignPassWd(boolean tag){
        if(tag){
            if(mIntSignType == 1){
                String pass = mEdtPassWord.getText().toString();
                if(pass != null && !"".equals(pass)){
                    if(mIntCount >4){
                        mSignEntity.setPassWord(pass);
                    }else{
                        toast(getResources().getString(R.string.toast_pass_word_not_safe));
                        return false;
                    }
                }else{
                    toast(getResources().getString(R.string.toast_pass_word_null));
                    return  false;
                }
            }else if(mIntSignType == 2){
                String pass = mEdtPassWord.getText().toString();
                if(pass != null && !"".equals(pass)){
                    if(mIntCount >4){
                        mSignEntity.setPassWord(pass);
                    }else{
                        toast(getResources().getString(R.string.toast_pass_word_not_safe));
                        return false;
                    }
                } else {
                    toast(getResources().getString(R.string.toast_pass_word_null));
                    return  false;
                }
            }
        }
        return  true;
    }

    private void viewTeamCreate(boolean tag){

        int gone = (tag == true ? View.GONE : View.VISIBLE);
        int visible = (tag == true ? View.VISIBLE : View.GONE);
        mViewPhonePassword.setVisibility(gone);
        mViewTeamCreate.setVisibility(visible);
        if(tag){
            mTvTitle.setText(mStrTeam);
            mIvCricle3.setImageDrawable(mDrawCircleBlue);
            mIvLine2.setImageDrawable(mDrawBlue);
            mIntNext ++;
        }else{
            mTvTitle.setText(mStrPass);
            mIvCricle3.setImageDrawable(mDrawCircleGray);
            mIvLine2.setImageDrawable(mDrawGray);
            mIntNext --;
        }
    }

    private boolean checkTeamInfo(boolean tag){
        if(tag){
            String teamName = mEdtTeamName.getText().toString();
            String teamNet =  mEdtTeamNet.getText().toString();
            String Name = mEdtName.getText().toString();
                if(teamName != null && !"".equals(teamName)){
                    mSignEntity.setTeamName(teamName);
                }else{
                    toast(getResources().getString(R.string.team_name_not_space));
                    return  false;
                }
                if(teamNet != null && !"".equals(teamNet)){ //TODO 公司code？
                    mSignEntity.setTeamCode(teamNet);
                }else{
                    toast(getResources().getString(R.string.team_net_not_space));
                    return  false;
                }
                if(Name != null && !"".equals(Name)){
                    mSignEntity.setUserName(Name);
                }else{
                    toast(getResources().getString(R.string.team_uName_not_space));
                    return  false;
                }
            showLoading();
            UserApi.getInstance().companyIsExist(this,teamNet,1);
        }
        return  true;
    }

    private void viewCheckInfo(boolean tag){
        int gone = (tag == true ? View.GONE : View.VISIBLE);
        int visible = (tag == true ? View.VISIBLE : View.GONE);
        mViewTeamCreate.setVisibility(gone);
        mViewCheckInfo.setVisibility(visible);
        if(tag){
            mTvTitle.setText(mStrCheck);
            mIvCricle4.setImageDrawable(mDrawCircleBlue);
            mIvLine3.setImageDrawable(mDrawBlue);
            mIntNext ++;
        }else{
            mTvTitle.setText(mStrTeam);
            mIvCricle4.setImageDrawable(mDrawCircleGray);
            mIvLine3.setImageDrawable(mDrawGray);
            mIntNext --;
        }
    }
    private void setCheckInfo(){

        String mail = mSignEntity.getMail();
        String teamName = mSignEntity.getTeamName();
        String teamCode = mSignEntity.getTeamCode();
        String name = mSignEntity.getUserName();

        mTvMail.setText(mail);
        mTvTeamName.setText(teamName);
        mTvTeamNet.setText(teamCode);
        mTvName.setText(name);

    }

    private void setPage(boolean tag) {
        if(tag){
            switch (mIntNext) {
                case 1:
                   checkSign(tag);
                    break;
                case 2:
                    if(checkSignPassWd(tag)){
                        viewTeamCreate(tag);
                    }
                    break;
                case 3:
                    checkTeamInfo(tag);
                    break;
                case 4:
                    showLoading();
                    String code = mSignEntity.getTeamCode();
                    String mail = mSignEntity.getMail();
                    String userName = mSignEntity.getUserName();
                    String passWord = mSignEntity.getPassWord();
                    String teamName = mSignEntity.getTeamName();
                    UserApi.getInstance().setNewAccount(this,code,mail,userName,passWord,teamName);
                    MixPanelUtil.signRecord(this,mail);
                    break;
                default:
                    break;
            }
        }else{
            switch (mIntNext) {
                case 1:
                    this.finish();
                    break;
                case 2:
                    viewPassword(tag);
                    break;
                case 3:
                    viewTeamCreate(tag);
                    break;
                case 4:
                    viewCheckInfo(tag);
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void onBackPressed() {
        setPage(false);
//        super.onBackPressed();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId == R.id.rbtn_phone){
            mLlMail.setVisibility(View.GONE);
            mLlPhone.setVisibility(View.VISIBLE);
            mIntSignType = 1;
        }else if(checkedId == R.id.rbtn_mail){
            mLlMail.setVisibility(View.VISIBLE);
            mLlPhone.setVisibility(View.GONE);
            mIntSignType = 2;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mNewPassWd = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String passWd = s.toString();

        if(passWd.equals(mNewPassWd)){
            return;
        }
        StringBuffer sb = new StringBuffer();

        for(int i = 0;i < passWd.length();i++){
            if(mDigits.indexOf(passWd.charAt(i)) >= 0){
                sb.append(passWd.charAt(i));
            }
        }
        mNewPassWd = sb.toString();
        mEdtPassWord.setText(mNewPassWd);
        mEdtPassWord.setSelection(sb.length());
    }

    @Override
    public void afterTextChanged(Editable s) {
        mIntCount = 0;
        //至少5位
        if(mNewPassWd == null || (mNewPassWd!=null && mNewPassWd.equals(""))) {
            setLineState(mDrawGray, mDrawGray, mDrawGray);
            return;
        }
//        }else if(mNewPassWd.length()>0 && mNewPassWd.length() < 5){
//            setLineState(mDrawRed, mDrawGray, mDrawGray);
//        }else{
            if(mNewPassWd.length() > 5) {
                mIntCount += 1;
            }
            if(!RegularUtil.isNumeric(mNewPassWd)){
                mIntCount += 1;
            }
            if(!RegularUtil.hasSmallChar(mNewPassWd)){
                mIntCount += 1;
            }
            if(!RegularUtil.hasBigChar(mNewPassWd)){
                mIntCount += 1;
            }
            if(!RegularUtil.hasSpecialChar(mNewPassWd)){
                mIntCount += 1;
            }
            if( mIntCount == 0){
                setLineState(mDrawGray, mDrawGray, mDrawGray);
            }else if( mIntCount == 1){
                setLineState(mDrawRed, mDrawGray, mDrawGray);
            }else if( mIntCount == 2){
                setLineState(mDrawYellow, mDrawYellow, mDrawGray);
            }else if(mIntCount == 3 || mIntCount == 4){
                setLineState(mDrawYellow, mDrawYellow, mDrawYellow);
            }else if(mIntCount == 5){
                setLineState(mDrawGreen, mDrawGreen, mDrawGreen);
            }
    }

    private void setLineState(Drawable left, Drawable middle, Drawable right){
        mIvPassLow.setImageDrawable(left);
        mIvPassMiddle.setImageDrawable(middle);
        mIvPassHeight.setImageDrawable(right);
    }



    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        dismissLoading();
        if(response == null){
            return;
        }
        if(taskId.equals(UserApi.TaskId.TASK_URL_MAIL_IS_EXIST)){
            mailIsExistHandle(response);
        }else if(taskId.equals(UserApi.TaskId.TASK_URL_COMPANY_IS_EXIST)){
            companyExistHandle(response);
        }else if(taskId.equals(UserApi.TaskId.TASK_URL_NEW_COMPANY)){
            newCompanyHandle(response);
        }else if(taskId.equals(UserApi.TaskId.TASK_URL_NEW_ACCOUNT)){
            newAccountHandle(response);
        }
    }

    private void newAccountHandle(Object response) {
        SignSuccessPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),SignSuccessPOJO.class);
        if(pojo != null){
            if(pojo.isResultSuccess()){
               toast(getResources().getString(R.string.sign_success));
                this.finish();
            }else{
                toast(pojo.getHeader().getReason());
            }
        }else{

        }
    }

    private void newCompanyHandle(Object response) {
        BasePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),BasePOJO.class);
        if(pojo != null){
            if(pojo.isResultSuccess()){
                viewCheckInfo(true);
                setCheckInfo();
            }else{

            }
        }else{

        }
    }

    private void companyExistHandle(Object response) {
        SignExistPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),SignExistPOJO.class);
        if(pojo != null){
            if(pojo.isResultSuccess()){
                SignExistEntity entity = pojo.getBody().getResponse().getData();
                if(entity != null){
                    if(entity.getIsExist() == 0){
                        viewCheckInfo(true);
                        setCheckInfo();
                    }else if(entity.getIsExist() == 1){
                        toast(getResources().getString(R.string.comany_isexist));
                    }
                }
            }else{
                toast(pojo.getBody().getResponse().getReason());
            }
        }else{

        }
    }

    private void mailIsExistHandle(Object response) {

        SignExistPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),SignExistPOJO.class);
        if(pojo != null){
            if(pojo.isResultSuccess()){
                SignExistEntity entity = pojo.getBody().getResponse().getData();
                if(entity != null){
                    if(entity.getUserCount() == 0){
                        viewPassword(true);
                    }else if(entity.getUserCount() > 0){
                        SignExistDialog dialog = new SignExistDialog(this,mSignEntity.getMail());
                        dialog.show();
                    }
                }
            }else{
                toast(pojo.getBody().getResponse().getReason());
            }
        }else{

        }
    }
}
