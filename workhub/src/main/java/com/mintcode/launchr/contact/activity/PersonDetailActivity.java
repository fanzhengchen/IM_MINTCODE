package com.mintcode.launchr.contact.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mintcode.chat.activity.ChatActivity;
import com.mintcode.chat.entity.User;
import com.mintcode.chat.util.BitmapUtil;
import com.mintcode.im.database.ContactDBService;
import com.mintcode.launchr.R;
import com.mintcode.launchr.activity.MainActivity;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.UserDetailPOJO;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.pojo.entity.UserDetailTempEntity;
import com.mintcode.launchr.pojo.entity.VirtrualNum;
import com.mintcode.launchr.util.GlideRoundTransform;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;

/**
 * 个人信息详情界面
 * @author KevinQiao
 *
 */
public class PersonDetailActivity extends BaseActivity {
	
	/** 用户信息key */
	public static final String KEY_PERSONAL_ID = "key_personal_id";

	/** 返回按钮 */
	private ImageView mIvBack;

	/** 头像imageview */
	private ImageView mIvHeadView;
	
	/** 名称显示 */
	private TextView mTvName;
	
	/** 名称布局*/
	private RelativeLayout mRelName;
	
	/** 手机 */
	private TextView mTvPhone;
	
	/** 邮箱 */
	private TextView mTvEmail;
	
	/** 办公电话 */
	private TextView mTvTel;
	
	/** 部门 */
	private TextView mTvDept;
	
	/** 职位 */
	private TextView mTvPostion;

	/** Launchr id */
	private TextView mTvLaunchrID;


	private UserDetailTempEntity mEntity;

	/** 手机 */
	private RelativeLayout mRelPhone;

	private RelativeLayout mRelRoomPhone;

	/** 虚拟号布局 */
	private RelativeLayout mRelVirtrual;

	/** 虚拟号显示 */
	private TextView mTvVirtrual;

	/** 扩张布局 */
	private LinearLayout mLinearExtendfield;

//	private 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_detail);

		initView();
		initData();
		
	}

	public void initView() {
		mIvBack = (ImageView) findViewById(R.id.iv_back);
		mIvHeadView = (ImageView) findViewById(R.id.iv_head_image);
		mTvName = (TextView) findViewById(R.id.tv_head_name);
		mRelName = (RelativeLayout) findViewById(R.id.rel_send_message);
		mTvPhone = (TextView) findViewById(R.id.tv_my_phone_text);
		mTvEmail = (TextView) findViewById(R.id.tv_my_mail_text);
		mTvTel = (TextView) findViewById(R.id.tv_my_tel_text);
		mTvDept = (TextView) findViewById(R.id.tv_my_my_team_name);
		mTvPostion = (TextView) findViewById(R.id.tv_position_name);
		mRelPhone = (RelativeLayout) findViewById(R.id.rel_my_phone);
		mRelRoomPhone = (RelativeLayout) findViewById(R.id.rel_my_room_phone);
//		mRelVirtrual = (RelativeLayout) findViewById(R.id.rel_my_virtual);
//		mTvVirtrual = (TextView) findViewById(R.id.tv_virtrual_num_text);
		mLinearExtendfield = (LinearLayout) findViewById(R.id.ll_extendfield);
		mTvLaunchrID = (TextView) findViewById(R.id.tv_launchr_id_text);


		mIvBack.setOnClickListener(this);
		mRelName.setOnClickListener(this);
		mRelPhone.setOnClickListener(this);
//		mRelRoomPhone.setOnClickListener(this);
//		mRelVirtrual.setOnClickListener(this);



		TextView tvID = (TextView) findViewById(R.id.tv_launchr_id);
		String strID;
		if(LauchrConst.IS_WORKHUB){
			strID = getResources().getString(R.string.workhub_id);
		}else {
			strID = getResources().getString(R.string.launchr_id);
		}
		tvID.setText(strID);
	}

	
	private void initData(){
		String personId = getIntent().getStringExtra(KEY_PERSONAL_ID);
		if (personId != null) {
			UserApi.getInstance().getComanyUserInfo(this, personId);
		}

		UserDetailEntity entity = ContactDBService.getInstance().getPersonDetail(personId);
		if(entity!=null){
			setUserData(changeBackToItem(entity));
		}


	}
	

	@Override
	public void onClick(View v) {
		
		if (v == mIvBack) {
			onBackPressed();
		}else if (v == mRelName) {
			sendMessage();
		}else if(v == mRelPhone){
			String phone = mTvPhone.getText().toString().trim();
			initCallDialog(phone);
		}else if(v == mRelRoomPhone){
			String tel = mTvTel.getText().toString().trim();
			initCallDialog(tel);
		}else if(v == mRelVirtrual){
			String virtrual = mTvVirtrual.getText().toString().trim();
			initCallDialog(virtrual);
		}
		
	}

	
	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		super.onResponse(response, taskId, rawData);
		if (response == null) {
			showNoNetWork();
			return;
		}else
			
		// 判断是否是获取个人详情返回	
		if (taskId.equals(UserApi.TaskId.TASK_URL_GET_COMPANY_USER_INFO)) {
			handleUserDetail(response);
			dismissLoading();
		}	
		
	}
	
	/**
	 * 处理用户信息
	 * @param response
	 */
	private void handleUserDetail(Object response){
		UserDetailPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), UserDetailPOJO.class);
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				mEntity = pojo.getBody().getResponse().getData();
				if (mEntity != null) {
					setUserData(mEntity);
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
	
	/**
	 * 设置数据
	 * @param entity
	 */
	private void setUserData(UserDetailTempEntity entity){
		// 设置手机号
		String phone = entity.getU_MOBILE();
		if (phone != null) {
			mTvPhone.setText(phone);
		}else{
			mTvPhone.setText("");
		}
		
		// 设置邮箱
		String email = entity.getU_MAIL();
		if (email != null) {
			mTvEmail.setText(email);
		}else{
			mTvEmail.setText("");
		}
		
		// 设置电话
		String  tel = entity.getU_TELEPHONE();
		if (tel != null) {
			mTvTel.setText(tel);
		}else{
			mTvTel.setText("");
		}
		
		// 设置名称
		String name = entity.getU_TRUE_NAME();
		if (name != null) {
			mTvName.setText(name);
		}else{
			mTvName.setText("");
		}
			
		// 设置部门
		List<String> deptList = entity.getD_NAME();
		StringBuilder build = new StringBuilder();
		if (deptList == null) {
			deptList = new ArrayList<String>();
		}
		for (int i = 0; i < deptList.size(); i++) {
			String dept = deptList.get(i);
			build.append(dept).append("、");
		}
		if (build.length() > 0) {
			build.deleteCharAt(build.length() - 1);
		}
		mTvDept.setText(build.toString());
		
		// 设置职位
		String position = entity.getU_JOB();
		if (position != null) {
			mTvPostion.setText(position);
		}else{
			mTvPostion.setText("");
		}

		// 设置虚拟号
		List<VirtrualNum> l = entity.getExtendField();
		// 设置Launchr ID
		long launchrId = entity.getLAUNCHR_ID();

		mTvLaunchrID.setText(launchrId + "");

		// 设置头像
		HeadImageUtil.getInstance(this).restartSetAvatarResourceWithUserId(mIvHeadView, entity.getSHOW_ID(), 40, 150, 150);
	}
	long px = System.currentTimeMillis();
	
	public void setImageResuces(ImageView iv, File file){
		// 处理圆形图片
		RequestManager man = Glide.with(this);
		GlideRoundTransform g =  new GlideRoundTransform(this,40);
		man.load(file).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).transform(g).into(iv);
	}


	// 发消息
	public void sendMessage() {
		if (mEntity != null) {
			Intent intent = new Intent(PersonDetailActivity.this, ChatActivity.class);
			User user = new User(mEntity.getU_TRUE_NAME(), mEntity.getSHOW_ID());
			intent.putExtra("user", user);
			startActivity(intent);

			MainActivity.showFirstFragment = 1;

			finish();
		}
		
	}

	private void initCallDialog(final String num){
		if(TextUtils.isEmpty(num)){
			toast("号码为空");
			return;
		}
		final Dialog dialog = new Dialog(this,R.style.my_dialog);
		dialog.show();
//		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setContentView(R.layout.dialog_call);
		Window window = dialog.getWindow();
		final RelativeLayout relCall = (RelativeLayout) window.findViewById(R.id.rel_call);
		final RelativeLayout relSendMsg = (RelativeLayout) window.findViewById(R.id.rel_send_msg);
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
				}else if(v == relSendMsg){
					Uri smsToUri = Uri.parse("smsto:" + num);
					Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
					startActivity(intent);
					dialog.dismiss();
				}
			}
		};
		relSendMsg.setOnClickListener(listener);
		relCall.setOnClickListener(listener);
	}


	private void initExtendField(List<VirtrualNum> list){
		if(list != null && !list.isEmpty()){
			mLinearExtendfield.removeAllViews();
			for (int i = 0; i < list.size(); i ++){
				VirtrualNum v = list.get(i);
				View view = getLayoutInflater().inflate(R.layout.item_personal_detail,null);
				RelativeLayout rel = (RelativeLayout) view.findViewById(R.id.rel_my_virtual);
				rel.setId(i + 100);
				TextView tv = (TextView) rel.findViewById(R.id.tv_virtrual_num_text);
				TextView tvName = (TextView) rel.findViewById(R.id.tv_virtrual_num);
				tvName.setTextColor(Color.BLACK);
				tv.setId(i + 10000);
				String name = v.getFieldName();
				String value = v.getFieldValue();
				if(name != null){
					tvName.setText(name);
				}

				if(value != null ){
					tv.setText(value);
				}

				mLinearExtendfield.addView(view);
			}
		}
	}

	@Override
	protected void onDestroy() {
		Glide.clear(mIvHeadView);
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra("lasttime",px + "");
		setResult(RESULT_OK,intent);
		super.onBackPressed();
	}

	/** 因为数据库存储的用户详情和显示的对象不同，将两个对象转换一下*/
	private UserDetailTempEntity changeBackToItem(UserDetailEntity entity){
		UserDetailTempEntity item = new UserDetailTempEntity();
		item.setSHOW_ID(entity.getShowId());
		item.setU_NAME(entity.getName());
		item.setU_TRUE_NAME(entity.getTrueName());
		item.setU_TRUE_NAME_C(entity.getTrueNameC());
		item.setU_HIRA(entity.getHira());
		item.setU_MAIL(entity.getMail());
		item.setU_STATUS(entity.getStatus());
		item.setU_MOBILE(entity.getMobile());
		item.setU_JOB(entity.getJob());
		item.setU_NUMBER(entity.getNumber());
		item.setU_SORT(entity.getSort());
		item.setLAST_LOGIN_TIME(entity.getLastLoginTime());
		item.setLAST_LOGIN_TOKEN(entity.getLastLoginToken());
		item.setIS_ADMIN(entity.getIsAdmin());
		item.setC_SHOW_ID(entity.getcShowId());
		item.setCREATE_USER(entity.getCreateUser());
		item.setCREATE_TIME(entity.getCreateTime());
		item.setCREATE_USER_NAME(entity.getCreateUserName());
		item.setU_TELEPHONE(entity.getTelephone());
		item.setLAUNCHR_ID(entity.getLaunchrId());

		List<String> pathId = new ArrayList<String>();
		pathId.add(entity.getPathName());
		item.setD_PATH_NAME(pathId);

		List<String> parentId = new ArrayList<String>();
		parentId.add(entity.getParentShowId());
		item.setD_PARENTID_SHOW_ID(parentId);

		List<String> dept = new ArrayList<String>();
		dept.add(entity.getdName());
		item.setD_NAME(dept);

		List<String> deptId = new ArrayList<String>();
		deptId.add(entity.getDeptId());
		item.setU_DEPT_ID(deptId);
		return item;
	}
}
