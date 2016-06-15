package com.mintcode.launchr.more;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.mintcode.cache.CacheManager;
import com.mintcode.chat.util.BitmapUtil;
import com.mintcode.launchr.activity.MainActivity;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.api.UserApi.TaskId;
import com.mintcode.launchr.app.my.MyChangeMailActivity;
import com.mintcode.launchr.app.my.MyChangeMobileActivity;
import com.mintcode.launchr.app.my.MyChangePasswordActivity;
import com.mintcode.launchr.app.my.MyChangePhoneActivity;
import com.mintcode.launchr.app.my.MyChangeUserNameActivity;
import com.mintcode.launchr.app.my.MyQrDialog;
import com.mintcode.launchr.app.my.MyShareActivity;
import com.mintcode.launchr.app.my.MyTeamActivity;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.more.activity.MySweepActivity;
import com.mintcode.launchr.more.activity.SettingActivity;
import com.mintcode.launchr.photo.activity.PhotoActivity;
import com.mintcode.launchr.pojo.LoginValidatePOJO;
import com.mintcode.launchr.pojo.UserInfoNewPOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.CompanyEntity;
import com.mintcode.launchr.pojo.entity.LoginValidateEntity;
import com.mintcode.launchr.pojo.entity.UserDetailNewEntity;
import com.mintcode.launchr.pojo.entity.VirtrualNum;
import com.mintcode.launchr.pojo.response.UserInfoNewResponse;
import com.mintcode.launchr.upload_download.UploadFileTask.UploadOverLister;
import com.mintcode.launchr.upload_download.UploadPartAsyncTask;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.GlideRoundTransform;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.view.SelectPicTypeWindowView;
import com.way.view.gesture.AlterGesturePasswordActivity;

/**
 * 更多列表
 * @author KevinQiao
 *
 */
public class MoreFragment extends BaseFragment {

	public static final int SETTING_LOGIN_OUT_CODE = 0x11;

	public static final int CHANGE_USER_MESSAGE = 0x12;

	public static final int CHANGE_ME_TEAM = 0x14;

	/** 修改手势密码返回码 */
	public static final int CODE_ALTER_GESTURE_PWD = 0x13;
	/** rootview */

	private View mRootView;
	
	/** 设置 */
	private RelativeLayout mRelSetting;
	
	/** 我的团队*/
	private RelativeLayout mRelMyTeam;
	
	/** 手机号*/
	private RelativeLayout mRelChangePhone;
	
	/** 邮箱*/
	private RelativeLayout mRelChangeMail;
	
	/** 修改密码*/
	private RelativeLayout mRelChangePassword;
	/**生成二维码**/
	private RelativeLayout mRelMyQR;
	
	/** 分享*/
	private RelativeLayout mRelShare;
	/**头像**/
	private ImageView mIvHead;
	/**上传头像**/
	private ImageView mIvUpdateHead;
	/**用户名字**/
	private TextView mTvUserName;
	/***用户id**/
	private TextView mTvUserId;
	
	private ProgressBar mProgressBar;
	/** Launchr id */
	private TextView mTvLaunchrID;
	/***用户名字**/
	private TextView mTvUserNameShow;
	/***用户手机号**/
	private TextView mTvUserPhone;
	/***用户邮箱**/
	private TextView mTvUserMail;
	/***用户办公电话**/
	private TextView mTvUserTele;
	/***用户团队**/
	private TextView mTvUserTeam;
	/***二维码扫描**/
	private ImageView mIvQrScan;
	
	private HeaderParam mHeaderParam;
	/**用户姓名**/
	private String mUserName;
	/**用户id**/
	private String mUserId;
	/**用户所在公司**/
	private String mUserCompany;
	/**用户头像url***/
//	private String mHeadPicUrl;
	/** 头像照片路径*/
	private String mPhotoPath;

	private RelativeLayout mRelMobile;

	private RelativeLayout mRelUserName;

	/** 修改手势密码view */
	private RelativeLayout mRelAlterGesturePwd;

	/** 扩张字段view */
	private LinearLayout mLinearExtendfield;
	
	private UserDetailNewEntity mUserDetailEntity;
	
	/*** 拍照*/
	public static final int TAKE_PICTURE = 0x000004;
	
	/*** 打开相册选择照片*/
	public static final int GET_ALBUM = 0x000003;
	/***二维码**/
	public static final int SCAN_CODE = 6;


	private Handler mHandler = new Handler();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mRootView = inflater.inflate(R.layout.fragment_more, null);
		return mRootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if(savedInstanceState != null){
			mPhotoPath = savedInstanceState.getString("photo_path");
		}

		initView();
		initUserInfo();
	}
	
	
	

	private void initView(){

		mTvLaunchrID = (TextView) mRootView.findViewById(R.id.tv_launchr_id_text);
		mTvUserNameShow = (TextView)mRootView.findViewById(R.id.tv_my_real_name);
		mTvUserPhone = (TextView)mRootView.findViewById(R.id.tv_my_phone_text);
		mTvUserMail =(TextView)mRootView.findViewById(R.id.tv_my_mail_text);
		mTvUserTele = (TextView)mRootView.findViewById(R.id.tv_my_room_phone_text);
		mTvUserTeam = (TextView)mRootView.findViewById(R.id.iv_my_my_team_name);
		
		mRelSetting = (RelativeLayout) mRootView.findViewById(R.id.rel_body_my_setting);
		mRelMyTeam = (RelativeLayout) mRootView.findViewById(R.id.rel_my_my_team);
		mRelChangePhone = (RelativeLayout) mRootView.findViewById(R.id.rel_my_phone);
		mRelChangeMail = (RelativeLayout) mRootView.findViewById(R.id.rel_my_mail);
		mRelChangePassword = (RelativeLayout) mRootView.findViewById(R.id.rel_change_password);
		mRelMyQR = (RelativeLayout) mRootView.findViewById(R.id.rel_my_calling_card);
		
		mRelShare = (RelativeLayout) mRootView.findViewById(R.id.rel_my_share);		
		mIvHead = (ImageView)mRootView.findViewById(R.id.iv_head_image);
		mIvUpdateHead = (ImageView)mRootView.findViewById(R.id.iv_take_head);
		mProgressBar = (ProgressBar)mRootView.findViewById(R.id.item_pic_loading);
		mTvUserName = (TextView)mRootView.findViewById(R.id.tv_head_name);
		mIvQrScan = (ImageView)mRootView.findViewById(R.id.tv_head_scan_calling_card);
		mRelMobile = (RelativeLayout) mRootView.findViewById(R.id.rel_my_room_phone);
		mRelUserName = (RelativeLayout) mRootView.findViewById(R.id.rel_my_name);
		mRelAlterGesturePwd = (RelativeLayout) mRootView.findViewById(R.id.rel_change_gesture_password);
		mLinearExtendfield = (LinearLayout) mRootView.findViewById(R.id.ll_extendfield);

		mRelSetting.setOnClickListener(this);
		mRelMyTeam.setOnClickListener(this);


		mRelChangePassword.setOnClickListener(this);
		
		mIvQrScan.setOnClickListener(this);
		mRelMyQR.setOnClickListener(this);
		mRelShare.setOnClickListener(this);
		mIvHead.setOnClickListener(this);
		mIvUpdateHead.setOnClickListener(this);

//		mRelUserName.setOnClickListener(this);
		mRelAlterGesturePwd.setOnClickListener(this);
			mRelChangeMail.setOnClickListener(this);
			mRelMobile.setOnClickListener(this);
			mRelChangePhone.setOnClickListener(this);

			TextView tvID = (TextView) mRootView.findViewById(R.id.tv_launchr_id);
			String strID = "";
			if(LauchrConst.IS_WORKHUB){
				strID = getResources().getString(R.string.workhub_id);
			}else {
				strID = getResources().getString(R.string.launchr_id);
			}
			tvID.setText(strID);
	}
	
	private void initUserInfo() {
		
		mHeaderParam = LauchrConst.header;
		mUserName = mHeaderParam.getUserName();
		mUserCompany = mHeaderParam.getCompanyCode();
		mUserId = mHeaderParam.getLoginName();
		// 头像显示
		HeadImageUtil.getInstance(getActivity()).restartSetUserAvatarAppendUrl(mIvHead,mUserId,0,150,150);
		showLoading();
		UserApi.getInstance().getComanyUserInfo(this, mUserId);
	}


	@Override
	public void onClick(View v) {
		if (mRelSetting == v) {
			accessSetting();
		}else if(v == mRelMyTeam){
			accessMyTeam();
		}else if(v == mRelChangePhone){
			accessChangePhone();
		}else if(v == mRelChangeMail){
			//accessChnageMail();
		}else if(v == mRelChangePassword){
			accessChangePassword();
		}else if(v == mRelShare){
			accessShare();
		}else if(v == mIvHead){
			updateHeadPic();
		}else if(v == mIvUpdateHead){
			updateHeadPic();
		}else if(v == mRelMyQR){
//			getUserQR();
		}else if(v == mIvQrScan){
			getScanQR();
		}else if(v==mRelMobile){
			Intent intent = new Intent(getActivity(), MyChangeMobileActivity.class);
			intent.putExtra("mobile", mUserDetailEntity.getU_TELEPHONE());
			startActivityForResult(intent, CHANGE_USER_MESSAGE);
		}else if(v==mRelUserName){
			Intent intent = new Intent(getActivity(), MyChangeUserNameActivity.class);
			intent.putExtra("userName", mUserDetailEntity.getU_TRUE_NAME());
			startActivityForResult(intent, CHANGE_USER_MESSAGE);
		}else if (v == mRelAlterGesturePwd){
			accessChangeGesturePwd();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		// 设置团队名称
		String name = AppUtil.getInstance(getActivity()).getValue(LauchrConst.KEY_TEMP_COMPANY_NAME);
		mTvUserTeam.setText(name);
	}

	private void getScanQR(){
		
		Intent intent = new Intent(getActivity(), MySweepActivity.class);
		startActivityForResult(intent, SCAN_CODE);
	}
	
	/**
	 * 生成二维码名片
	 */
	private void getUserQR() {
		
		MyQrDialog myQrDialog = new MyQrDialog(getActivity());	
		myQrDialog.show();
		Window window = myQrDialog.getWindow();
		ImageView mIvUserHead = (ImageView)window.findViewById(R.id.iv_user_head);
		TextView mTvUserName = (TextView)window.findViewById(R.id.tv_user_name);
		TextView mTvUserId = (TextView)window.findViewById(R.id.tv_user_id);
		final ProgressBar mPbLoading = (ProgressBar)window.findViewById(R.id.pic_loading);
		final ImageView mIvQR = (ImageView)window.findViewById(R.id.iv_user_qr);
		final Bitmap mLog = BitmapFactory.decodeResource(getResources(), R.drawable.icon_launchr);
		String mHeadPicUrl = "";
		CacheManager.getInstance(getActivity()).loadBitmap(mHeadPicUrl, mIvUserHead);
		mTvUserName.setText(mUserName);
		final String filePath = getFileRoot(getActivity()) + File.separator
                + "qr_" + mUserName + ".jpg";
		Bitmap bitmap = BitmapFactory.decodeFile(filePath);
		if(bitmap == null){
			mPbLoading.setVisibility(View.VISIBLE);
			//二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
	        new Thread(new Runnable() {
	            @Override
	            public void run() {
	                boolean success = false;
					try {
//						success = createQRImage(mUserName+" "+mUserId,500, 500,mLog,filePath);
					} catch (Exception e) {
						e.printStackTrace();
					}

	                if (success) {
	                    getActivity().runOnUiThread(new Runnable() {
	                        @Override
	                        public void run() {
	                        	mPbLoading.setVisibility(View.GONE);
	                        	mIvQR.setImageBitmap(BitmapFactory.decodeFile(filePath));
	                        }
	                    });
	                }
	            }
	        }).start();
		}else {
			mPbLoading.setVisibility(View.GONE);
			mIvQR.setImageBitmap(bitmap);
		}
		
		
	}

	/**
	 * 上传头像
	 */
	private void updateHeadPic(){
		
		SelectPicTypeWindowView popWindow = new SelectPicTypeWindowView(getActivity());
		popWindow.showAtLocation(mRelSetting, Gravity.BOTTOM, 0, 0);
	}
	/**
	 * 设置操作
	 */
	private void accessSetting(){
		Intent intent = new Intent(getActivity(), SettingActivity.class);
		startActivityForResult(intent, SETTING_LOGIN_OUT_CODE);
	}
	
	/**
	 * 我的团队
	 */
	private void accessMyTeam(){

		AppUtil util = AppUtil.getInstance(getActivity());
		String account = util.getValue(LauchrConst.KEY_TEMP_LOGINNAME);
		String password = util.getValue(LauchrConst.KEY_TEMP_PASSWORD);

		UserApi api = UserApi.getInstance();
		api.loginValidate(this, account, password);


//		Intent intent = new Intent(getActivity(), MyTeamActivity.class);
//		startActivity(intent);
	}
	
	/**
	 * 修改手机号
	 */
	private void accessChangePhone(){
		Intent intent = new Intent(getActivity(), MyChangePhoneActivity.class);
		intent.putExtra("phone", mUserDetailEntity.getU_TELEPHONE());
		startActivityForResult(intent, CHANGE_USER_MESSAGE);
	}
	
	/** 修改邮箱*/
	private void accessChnageMail(){
		Intent intent = new Intent(getActivity(), MyChangeMailActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 修改密码
	 */
	private void accessChangePassword(){
		Intent intent = new Intent(getActivity(), MyChangePasswordActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 分享
	 */
	private void accessShare(){
		Intent intent = new Intent(getActivity(), MyShareActivity.class);
		startActivity(intent);
	}

	/**
	 * 修改手势密码
	 */
	private void accessChangeGesturePwd(){
		Intent intent = new Intent(getActivity(), AlterGesturePasswordActivity.class);
		startActivityForResult(intent, CODE_ALTER_GESTURE_PWD);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("photo_path", mPhotoPath);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			super.onActivityResult(requestCode, resultCode, data);
			return;
		}else

		if (requestCode == SETTING_LOGIN_OUT_CODE) {
			getActivity().finish();
		}else
		if (requestCode == TAKE_PICTURE){
			// 三星手机崩溃 TODO
			mPhotoPath = SelectPicTypeWindowView.mStrPicPath;
			/**  由于部分手机旋转了图片，这里处理一下*/
			BitmapUtil.handleTakePhoto(getActivity(), mPhotoPath);
			uploadHeadPic(mPhotoPath);

		}else
		if(requestCode == CHANGE_USER_MESSAGE){
			showLoading();
			UserApi.getInstance().getComanyUserInfo(this, mUserId);
		}else
		if (requestCode == GET_ALBUM){
			if(data == null){
				return;
			}
			 List<String> list = data.getStringArrayListExtra(PhotoActivity.SELECT_IMAGE_LIST);
			if(list != null){
				uploadHeadPic(list.get(0));
			}

		}else if(requestCode == CHANGE_ME_TEAM){
			getActivity().finish();

			/** 切换团队要将原来的MainActivity结束掉*/
			Intent intent = new Intent(getActivity(), MainActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * 上传头像
	 * @param path
	 */
	private void uploadHeadPic(String path){
		mProgressBar.setVisibility(View.VISIBLE);
		mIvHead.setVisibility(View.INVISIBLE);
		if(!path.equals("")){
			final UploadPartAsyncTask uploadPartAsyncTask = new UploadPartAsyncTask(getActivity(),path, new UploadOverLister() {

				@Override
				public void uploadOver(String path,String showid) {
					mProgressBar.setVisibility(View.GONE);
					mIvHead.setVisibility(View.VISIBLE);
					HeadImageUtil.getInstance(getActivity()).restartSetUserAvatarAppendUrl(mIvHead,mUserId,0,150,150);
				}
				@Override
				public void uploadError(String reason) {
					mProgressBar.setVisibility(View.GONE);
					mIvHead.setVisibility(View.VISIBLE);
					toast(reason);
				}
			});
			uploadPartAsyncTask.setType(3);

			Runnable r = new Runnable() {
				@Override
				public void run() {
					uploadPartAsyncTask.execute();
				}
			};

			File f = new File(path);
			if(f.length() == 0){
				mHandler.postDelayed(r, 9000);
			}else {
				uploadPartAsyncTask.execute();
			}

		}else{
			mProgressBar.setVisibility(View.GONE);
			mIvHead.setVisibility(View.VISIBLE);
			toast(getString(R.string.phoyo_update_fail));
		}
	}

	/**
     * 生成二维码Bitmap
     *
     * @param content   内容
     * @param widthPix  图片宽度
     * @param heightPix 图片高度
     * @param logoBm    二维码中心的Logo图标（可以为null）
     * @param filePath  用于存储二维码图片的文件路径
     * @return 生成二维码及保存文件是否成功
	 * @throws
     */
    public static boolean createQRImage(String content, int widthPix, int heightPix, Bitmap logoBm, String filePath) throws Exception {
        try {
            if (content == null || "".equals(content)) {
                return false;
            }
// 
//            //配置参数
//            Map<EncodeHintType,Object> hints = new HashMap<EncodeHintType, Object>();
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//            //容错级别
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//            //设置空白边距的宽度
//            hints.put(EncodeHintType.MARGIN, 2); //default is 4
// 
//            // 图像数据转换，使用了矩阵转换
//            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
//            int[] pixels = new int[widthPix * heightPix];
//            // 下面这里按照二维码的算法，逐个生成二维码的图片，
//            // 两个for循环是图片横列扫描的结果
//            for (int y = 0; y < heightPix; y++) {
//                for (int x = 0; x < widthPix; x++) {
//                    if (bitMatrix.get(x, y)) {
//                        pixels[y * widthPix + x] = 0xff000000;
//                    } else {
//                        pixels[y * widthPix + x] = 0xffffffff;
//                    }
//                }
//            }
// 
//            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
//            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
 
            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }
// 
//            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
// 
        return false;
    }
 
    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
 
        if (logo == null) {
            return src;
        }
 
        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
 
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
 
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }
 
        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
 
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
 
        return bitmap;
    }
  //文件存储根目录
    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }
 
        return context.getFilesDir().getAbsolutePath();
    }
    
    private void handleUserInfo(UserInfoNewPOJO userInfo){
    	if(userInfo != null){
    		UserInfoNewResponse infoResponse = userInfo.getBody().getResponse();
			if (infoResponse.isIsSuccess()) {
				mUserDetailEntity = infoResponse.getData();
				if(mUserDetailEntity != null){
					mTvUserNameShow.setText(mUserDetailEntity.getU_TRUE_NAME());
					mTvUserPhone.setText(mUserDetailEntity.getU_MOBILE());
					mTvUserMail.setText(mUserDetailEntity.getU_MAIL());
					mTvUserTele.setText(mUserDetailEntity.getU_TELEPHONE());
					mTvUserName.setText(mUserDetailEntity.getU_TRUE_NAME());
					mTvLaunchrID.setText(mUserDetailEntity.getLAUNCHR_ID()+"");
					List<VirtrualNum> l = mUserDetailEntity.getExtendField();
				}
			}else{
				toast(infoResponse.getReason());
			}
		}
    }

	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		dismissLoading();
		super.onResponse(response, taskId, rawData);
		if(response == null){
			showNoNetWork();
			return;
	}
		if(taskId.equals(TaskId.TASK_URL_GET_COMPANY_USER_INFO)){
			
			UserInfoNewPOJO userInfo = TTJSONUtil.convertJsonToCommonObj(response.toString(), UserInfoNewPOJO.class);
			handleUserInfo(userInfo);
			
		}else

		/* 这里切换团队暂时做成登录的流程，因为web没有提供接口 */
		// 判断是否是登录验证返回
		if (taskId.equals(TaskId.TASK_URL_VALIDATE_LOGIN)) {
			handlValidateLogin(response);
			dismissLoading();
		}

	}





	/**
	 *
	 * @param response SD
	 */
	private void handlValidateLogin(Object response){
		LoginValidatePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), LoginValidatePOJO.class);
		if (pojo != null) {
			if (pojo.isResultSuccess()) {
				LoginValidateEntity entity = pojo.getBody().getResponse().getData();
				if (entity != null) {
					// 验证完成操作
					List<CompanyEntity> comList = entity.getCompanyList();

					if (comList != null) {

						AppUtil util = AppUtil.getInstance(getActivity());
						String account = util.getValue(LauchrConst.KEY_TEMP_LOGINNAME);
						String password = util.getValue(LauchrConst.KEY_TEMP_PASSWORD);

						Intent intent = new Intent(getActivity(), MyTeamActivity.class);
						intent.putExtra("team_entity", (Serializable) comList);
						intent.putExtra("login_username", account);
						intent.putExtra("login_pwd", password);

						startActivityForResult(intent, CHANGE_ME_TEAM);
					}

//					finish();
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
	 * 处理扩张字段
	 * @param list
	 */
	private void initExtendField(List<VirtrualNum> list){
		if(list != null && !list.isEmpty()){
			mLinearExtendfield.removeAllViews();
			for (int i = 0; i < list.size(); i ++){
				VirtrualNum v = list.get(i);
				View view = getActivity().getLayoutInflater().inflate(R.layout.item_personal_detail, null);
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

}







