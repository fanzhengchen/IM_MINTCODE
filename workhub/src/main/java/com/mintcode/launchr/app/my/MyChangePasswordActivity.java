package com.mintcode.launchr.app.my;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.api.UserApi.TaskId;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.pojo.UpdatePassWordPOJO;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.TTJSONUtil;

public class MyChangePasswordActivity extends BaseActivity implements TextWatcher  {
	
	/**原密码输入**/
	private EditText mEdtOriginalPassword;
	/**新密码输入**/
	private EditText mEdtNewPassword;
	/**确认密码输入**/
	private EditText mEdtConfirmPassword;
	/**原密码**/
	private CheckBox mCbShowOriginalPassword;
	/**新密码**/
	private CheckBox mCbShowNewPassword;
	/**确认密码**/
	private CheckBox mCbShowConfirmPassword;
	/**返回**/
	private ImageView mIvback;
	/**获取验证码**/
	private TextView mTvIdentifyingCode;
	/**确认修改**/
	private Button mBtnConfirmChange;
	
	/**小写字母**/
	private CheckBox mCbSmallChar;
	/**大写字母**/
	private CheckBox mCbBigChar;
	/**数字**/
	private CheckBox mCbNumberChar;
	/**特殊字符**/
	private CheckBox mCbSpecialChar;
	/**至少八位**/
	private CheckBox mCbLeast8;
	/**密码可输字符**/
	private String mDigits = "\"0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLIMNOPQRSTUVWXYZ~`!@#$%^&*()_-+={[}]|\\:;'<,.>?/";
	
	private String mNewPassWd = "";

	private ImageView mIvLeft;
	private ImageView mIvMiddle;
	private ImageView mIvRight;
	private int grayImageId;
	private int redImageId;
	private int yellowImageId;
	private int greenImageId;
	private int count = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_change_password);
		initViews();
	}
	private void initViews() {
		
		
		mIvback = (ImageView)findViewById(R.id.iv_title_back);
		mEdtOriginalPassword = (EditText)findViewById(R.id.edit_original_password);
		mEdtNewPassword = (EditText)findViewById(R.id.edit_new_password);
		mEdtConfirmPassword = (EditText)findViewById(R.id.edit_confirm_password);
		mCbShowOriginalPassword = (CheckBox)findViewById(R.id.cb_original_password);
		mCbShowNewPassword = (CheckBox)findViewById(R.id.cb_new_password);
		mCbShowConfirmPassword = (CheckBox)findViewById(R.id.cb_confirm_password);
		mTvIdentifyingCode = (TextView)findViewById(R.id.tv_identifying_code_get);
		mBtnConfirmChange = (Button)findViewById(R.id.btn_sign_out);
		
		mCbSmallChar = (CheckBox)findViewById(R.id.ch_new_password_small_char);
		mCbBigChar = (CheckBox)findViewById(R.id.ch_new_password_big_char);
		mCbNumberChar = (CheckBox)findViewById(R.id.ch_new_password_number_char);
		mCbLeast8 = (CheckBox)findViewById(R.id.ch_new_password_least_8);
		mCbSpecialChar = (CheckBox)findViewById(R.id.ch_new_password_special_char);
		mIvLeft = (ImageView) findViewById(R.id.iv_paddword_line1);
		mIvMiddle = (ImageView) findViewById(R.id.iv_paddword_line2);
		mIvRight = (ImageView) findViewById(R.id.iv_paddword_line3);
		
		mCbShowOriginalPassword.setOnClickListener(this);
		mCbShowNewPassword.setOnClickListener(this);
		mCbShowConfirmPassword.setOnClickListener(this);
		mIvback.setOnClickListener(this);
		mTvIdentifyingCode.setOnClickListener(this);
		mBtnConfirmChange.setOnClickListener(this);
		mEdtNewPassword.addTextChangedListener(this);

		grayImageId = R.drawable.shape_line_gray;
		redImageId =R.drawable.shape_line_red;
		yellowImageId = R.drawable.shape_line_yellow;
		greenImageId = R.drawable.shape_line_green;
	}
	
	@Override
	public void onClick(View v) {
		if(v == mCbShowOriginalPassword){
			showOriginalPassword();
		}else if(v == mCbShowNewPassword){
			showNewPassword();
		}else if(v == mCbShowConfirmPassword){
			showConfirmPassword();
		}else if(v == mIvback){
			this.finish();
		}else if(v == mTvIdentifyingCode){
			getIdentifyingCode();
		}else if(v == mBtnConfirmChange){
			confirmChange();
		}
	}
	/**
	 * 确认修改
	 */
	private void confirmChange() {
		String oldPassWd = mEdtOriginalPassword.getText().toString().trim();
		String newPassWd = mEdtNewPassword.getText().toString().trim();
		String confirmodWd = mEdtConfirmPassword.getText().toString().trim();
		if("".equals(oldPassWd)){
			toast(getString(R.string.toast_pass_word_null));
		}else if("".equals(newPassWd)){
			toast(getString(R.string.toast_pass_word_null));
		}else if("".equals(confirmodWd)){
			toast(getString(R.string.toast_pass_word_null));
		}else if(oldPassWd.equals(newPassWd)){
			toast(getString(R.string.toast_pass_cantsame));
		}else if(newPassWd.equals(confirmodWd)){
			showLoading();
			String userId = AppUtil.getInstance(this).getShowId();
			UserApi.getInstance().updatePassWd(this, userId, oldPassWd,newPassWd);
		}else{
			toast(getString(R.string.my_change_password_new_no_same));
		}
	}
	/**
	 * 获取验证码
	 */
	private void getIdentifyingCode() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 显示确认密码
	 */
	private void showConfirmPassword() {
		
		if(mCbShowConfirmPassword.isChecked()){
			mEdtConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}else{
			mEdtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
		 CharSequence text = mEdtConfirmPassword.getText();
		 if (text instanceof Spannable) {
		 Spannable spanText = (Spannable) text;
		 Selection.setSelection(spanText, text.length());// 将光标移动到最后
		 }
	}
	/**
	 * 显示新密码
	 */
	private void showNewPassword() {
		
		if(mCbShowNewPassword.isChecked()){
			mEdtNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}else{
			mEdtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
		 CharSequence text = mEdtNewPassword.getText();
		 if (text instanceof Spannable) {
		 Spannable spanText = (Spannable) text;
		 Selection.setSelection(spanText, text.length());// 将光标移动到最后
		 }
	}
	/**
	 * 显示初始密码
	 */
	private void showOriginalPassword() {
		
		if(mCbShowOriginalPassword.isChecked()){
			mEdtOriginalPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}else{
			mEdtOriginalPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
		 CharSequence text = mEdtOriginalPassword.getText();
		 if (text instanceof Spannable) {
		 Spannable spanText = (Spannable) text;
		 Selection.setSelection(spanText, text.length());// 将光标移动到最后
		 }
		
	}
	/**
	 * 数字
	 * @param s
	 * @return
	 */
	private boolean isNumeric(String s){	
		Pattern pattern =  Pattern.compile(".*?[0-9]+.*?");
		Matcher isNum = pattern.matcher(s);
		if (!isNum.matches()) {
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 大写
	 * @param s
	 * @return
	 */
	private boolean hasBigChar(String s){
		Pattern pattern = Pattern.compile(".*?[A-Z]+.*?");
		Matcher isNum = pattern.matcher(s);
		if (!isNum.matches()) {
			return true;
		}else{
			return false;
		}

	}
	/**
	 * 小写
	 * @param s
	 * @return
	 */
	private boolean hasSmallChar(String s){
		Pattern pattern = Pattern.compile(".*?[a-z]+.*?");
		Matcher isNum = pattern.matcher(s);
		if (!isNum.matches()) {
			return true;
		}else{
			return false;
		}

	}
	/**
	 * 特殊字符
	 * @param s
	 * @return
	 */
	private boolean hasSpecialChar(String s){
		
		Pattern pattern = Pattern.compile(".*?[^A-Za-z0-9]+.*?");
		Matcher isNum = pattern.matcher(s);
		if (!isNum.matches()) {
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		Log.i("afterTextChanged", s.toString());

		count = 0;

		//至少5位
		if(mNewPassWd==null || (mNewPassWd!=null && mNewPassWd.equals(""))){
			setLineState(grayImageId, grayImageId, grayImageId);
		}else if(mNewPassWd.length()>0 && mNewPassWd.length() < 5){
			setLineState(redImageId, grayImageId, grayImageId);
		}else{
			if(!isNumeric(mNewPassWd)){
				count += 1;
			}
			if(!hasSmallChar(mNewPassWd)){
				count += 1;
			}
			if(!hasBigChar(mNewPassWd)){
				count += 1;
			}
			if(!hasSpecialChar(mNewPassWd)){
				count += 1;
			}

			if( count==1){
				setLineState(redImageId, grayImageId, grayImageId);
			}else if( count==2){
				setLineState(yellowImageId, yellowImageId, grayImageId);
			}else if(count==3 || count==4){
				setLineState(greenImageId, greenImageId, greenImageId);
			}else{
				setLineState(grayImageId, grayImageId, grayImageId);
			}
		}
	}

	private void setLineState(int left, int middle, int right){
		mIvLeft.setImageDrawable(getResources().getDrawable(left));
		mIvMiddle.setImageDrawable(getResources().getDrawable(middle));
		mIvRight.setImageDrawable(getResources().getDrawable(right));
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		Log.i("beforeTextChanged", s.toString()+"  start" + start + " count" + count + " after" + after);
		mNewPassWd = s.toString();
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		Log.i("onTextChanged", s.toString() + "  start" + start + " count" + count + " before" + before);
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
		mEdtNewPassword.setText(mNewPassWd);
		mEdtNewPassword.setSelection(sb.length());
	}
	
	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		
		super.onResponse(response, taskId, rawData);
		if (response == null) {
			showNoNetWork();
			return;
		}
		dismissLoading();
		if(taskId.endsWith(TaskId.TASK_URL_UPDATE_USER_PASSWD)){
			UpdatePassWordPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), UpdatePassWordPOJO.class);
			handleUpdatePassWord(pojo);
		}
	}
	private void handleUpdatePassWord(UpdatePassWordPOJO pojo) {
		
		mEdtConfirmPassword.setText("");
		mEdtNewPassword.setText("");
		mEdtOriginalPassword.setText("");
		
		if(pojo == null){
			return;
		}else if(pojo.getBody() == null){
			return;
		}else if(pojo.getBody().getResponse().isIsSuccess() == false){
			toast(pojo.getBody().getResponse().getReason());
			return;
		}else {
			
			toast(getString(R.string.my_change_password_change_success));
			reStartApp();
		}
		
	}
	//回到登录界面
			private void reStartApp() {
				finish();
//				Intent it = new Intent(this,StartPageActivity.class);
//				it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				startActivity(it);
			}
	
	
}
