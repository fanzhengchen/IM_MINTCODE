package com.mintcode.launchr.activity.workhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.IMAPI;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.LoginPOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.CompanyEntity;
import com.mintcode.launchr.pojo.entity.UserEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.PersonalInfoUtil;
import com.mintcode.launchr.util.TTDensityUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.view.NoscrollListView;
import com.mintcode.launchrnetwork.OnResponseListener;
import com.way.view.gesture.CreateGesturePasswordActivity;
import com.way.view.gesture.UnlockGesturePasswordActivity;

import org.litepal.LitePalManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/4/7.
 */
public class WorkHubMyTeamActivity extends BaseActivity implements AdapterView.OnItemClickListener, OnResponseListener {
    private ImageView mIvBack;

    private TextView mTvTeamNum;

    private ListView mLvTeam;
    /** 适配器 */
    private TeamAdapter mAdapter;
    private List<CompanyEntity> mList;

    private Button mBtnCreateTeam;

    private String mUserName = "";

    private String mPwd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workhub_my_team);

        initData();
        initView();
    }

    private void initView(){

        mIvBack = (ImageView)findViewById(R.id.iv_back);
        mTvTeamNum = (TextView)findViewById(R.id.tv_my_team);
        mLvTeam = (ListView)findViewById(R.id.lv_my_team);
        mBtnCreateTeam = (Button)findViewById(R.id.btn_create_team);

        mAdapter=new TeamAdapter();
        mLvTeam.setAdapter(mAdapter);

        if(mList.size() > 0){
            String title = getString(R.string.my_my_team) + "(" + mList.size() + ")";
            mTvTeamNum.setText(title);
        }

        mBtnCreateTeam.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mLvTeam.setOnItemClickListener(this);

        setListViewHeightByTime();
    }

    private void initData(){
        mUserName = getIntent().getStringExtra("login_username");
        mPwd = getIntent().getStringExtra("login_pwd");
        mList = (List<CompanyEntity>) getIntent().getSerializableExtra("team_entity");
        if (mList == null) {
            mList = new ArrayList<CompanyEntity>();
        }
    }

    /** 动态设置*/
    private void setListViewHeightByTime(){
        int phoneHeight = getWindowManager().getDefaultDisplay().getHeight()- TTDensityUtil.dip2px(this, 150);

        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View listItem = mAdapter.getView(i, null, mLvTeam);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = mLvTeam.getLayoutParams();
        params.height = totalHeight + (mLvTeam.getDividerHeight() * (mAdapter.getCount()-1));
        for(int i=1; params.height>phoneHeight && i<mAdapter.getCount(); i++){
            params.height = getListViewHeight(mAdapter.getCount()-i);
        }

        ((ViewGroup.MarginLayoutParams)params).setMargins(10, 10, 10, 10);
        mLvTeam.setLayoutParams(params);
    }

    private int getListViewHeight(int count){
        int totalHeight = 0;
        for (int i = 0; i < count; i++) {
            View listItem = mAdapter.getView(i, null, mLvTeam);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        return totalHeight + (mLvTeam.getDividerHeight() * (count-1));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mIvBack){
            this.finish();
        }else if(v == mBtnCreateTeam){
            Intent i = new Intent(this,WorkHubCreateTeamActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 点击登录
        CompanyEntity entity = mList.get(position);
        UserApi api = UserApi.getInstance();

        // 设置comanycode
        AppUtil.getInstance(this).saveCompanyCode(entity.getcCode());
        HeaderParam p = LauchrConst.header;
        p.setCompanyCode(entity.getcCode());
        AppUtil.getInstance(this).saveValue(LauchrConst.KEY_TEMP_COMPANY_NAME, entity.getcName());

        LauchrConst.resetHeader(this);
        showLoading();
        api.login(this, mUserName, mPwd);
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        if(response == null){
            showNoNetWork();
            return;
        }

        // 判断是否是验证返回
        if (taskId.equals(UserApi.TaskId.TASK_URL_USER_LOGIN)) {
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

    class TeamAdapter extends BaseAdapter {

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
                convertView = getLayoutInflater().inflate(R.layout.item_team_workhub, null);
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
}
