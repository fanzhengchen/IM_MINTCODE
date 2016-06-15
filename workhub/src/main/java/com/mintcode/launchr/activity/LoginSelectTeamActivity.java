package com.mintcode.launchr.activity;


import java.util.ArrayList;
import java.util.List;

import org.litepal.LitePalManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.IMAPI;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.api.UserApi.TaskId;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.LoginPOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.CompanyEntity;
import com.mintcode.launchr.pojo.entity.UserEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.PersonalInfoUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchrnetwork.OnResponseListener;
import com.way.view.gesture.CreateGesturePasswordActivity;
import com.way.view.gesture.UnlockGesturePasswordActivity;


/**
 * 登录页面
 * @author KevinQiao
 *
 */
public class LoginSelectTeamActivity extends BaseActivity implements OnItemClickListener, OnResponseListener {
	
	/** 我的团队 */
	private ListView mLvTeam;
	
	/** 用户名显示 */
	private TextView mTvUserName;

	private TextView mTvTitle;

	/** 适配器 */
	private TeamAdapter mAdapter;
	
	private List<CompanyEntity> mList;
	
	private String mUserName = "";
	
	private String mPwd = "";

	private ImageView mIvBottom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_team);
		
		initData();
		initView();
		
	}
	
	
	
	private void initView(){
		mLvTeam = (ListView) findViewById(R.id.lv_team);
		mTvUserName = (TextView) findViewById(R.id.tv_name);
		mTvTitle = (TextView) findViewById(R.id.title);
		mIvBottom = (ImageView) findViewById(R.id.bottom_layout);
		mAdapter = new TeamAdapter();
		mLvTeam.setAdapter(mAdapter);
		
		mLvTeam.setOnItemClickListener(this);
		mTvUserName.setText(mUserName);
		mTvUserName.setText(mUserName);

		if(mList.size() > 0){
			String title = getString(R.string.my_my_team) + "(" + mList.size() + ")";
			mTvTitle.setText(title);
		}
		if(mList.size() == 1){
			// 点击登录
			CompanyEntity entity = mList.get(0);
			UserApi api = UserApi.getInstance();
//		LauchrConst.COMPANY_CODE = entity.getcCode();

			// 设置comanycode
			AppUtil.getInstance(this).saveCompanyCode(entity.getcCode());
			HeaderParam p = LauchrConst.header;
			p.setCompanyCode(entity.getcCode());

			LauchrConst.resetHeader(this);
//		KeyValueDBService.getInstance().put(Keys.COMPANY_CODE, LauchrConst.COMPANY_CODE);
			api.login(this, mUserName, mPwd);
			showLoading();
		}

		if(!LauchrConst.IS_WORKHUB){
			mIvBottom.setImageResource(R.drawable.icon_launchr_small_logo);
		}else{
			mIvBottom.setVisibility(View.GONE);
		}
	}
	
	private void initData(){
		mUserName = getIntent().getStringExtra("login_username");
		mPwd = getIntent().getStringExtra("login_pwd");
		mList = (List<CompanyEntity>) getIntent().getSerializableExtra("team_entity");
		if (mList == null) {
			mList = new ArrayList<CompanyEntity>();
		}
	}
	
	
	class TeamAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder hold = null;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.item_team, null);
				hold = new ViewHolder();
				hold.tvTeamIp = (TextView) convertView.findViewById(R.id.tv_team_ip);
				hold.tvTeamName = (TextView) convertView.findViewById(R.id.tv_team);
				convertView.setTag(hold);
			}else{
				hold = (ViewHolder) convertView.getTag();
			}
			
			// 绑定数据
			CompanyEntity entity = mList.get(position);
			hold.tvTeamName.setText(entity.getcName());
			hold.tvTeamIp.setText(entity.getcCode());
			return convertView;
		}
		
	}
	
	class ViewHolder {
		TextView tvTeamName;
		TextView tvTeamIp;
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 点击登录
		CompanyEntity entity = mList.get(position);
		UserApi api = UserApi.getInstance();
//		LauchrConst.COMPANY_CODE = entity.getcCode();
		
		// 设置comanycode
		AppUtil.getInstance(this).saveCompanyCode(entity.getcCode());
		HeaderParam p = LauchrConst.header;
		p.setCompanyCode(entity.getcCode());
		AppUtil.getInstance(this).saveValue(LauchrConst.KEY_TEMP_COMPANY_NAME, entity.getcName());

		LauchrConst.resetHeader(this);
//		KeyValueDBService.getInstance().put(Keys.COMPANY_CODE, LauchrConst.COMPANY_CODE);
		api.login(this, mUserName, mPwd);
		showLoading();
	}
	
	@Override
	public void onResponse(Object response, String taskId, boolean rawData) {
		
		// 判断是否为空
		if (response == null) {
			showNoNetWork();
			return;
		}
		// 判断是否是验证返回	
		if (taskId.equals(TaskId.TASK_URL_USER_LOGIN)) {
			handleResultLogin(response);
			dismissLoading();
		}	
		
		else if(taskId.equals(IMAPI.TASKID.LOGIN)){
			com.mintcode.LoginPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(),com.mintcode.LoginPOJO.class);
			if (pojo.isResultSuccess()) {
				String token = pojo.getUserToken();
				AppUtil util = AppUtil.getInstance(this);
				util.saveIMToken(token);

//				PersonalInfoUtil infoUtil = PersonalInfoUtil.getInstance(this);
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
						Log.i("LitePalManager", "===loginSelectTeam===");
	//					Intent intent = new Intent(this, CreateGesturePasswordActivity.class);
	//					startActivity(intent);
	//					LauchrConst.header = LauchrConst.getHeader(this);
	//					finish();
						Log.i("LitePalManager", show_ID + "_" + user.getC_CODE());
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
	
}
