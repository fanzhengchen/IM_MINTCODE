package com.mintcode.launchr.contact.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mintcode.im.database.ContactDBService;
import com.mintcode.im.database.ContactUserDB;
import com.mintcode.launchr.R;
import com.mintcode.launchr.activity.MainActivity;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.contact.fragment.ContactDeptFragment;
import com.mintcode.launchr.contact.fragment.ContactFragment;
import com.mintcode.launchr.pojo.DepartmentPOJO;
import com.mintcode.launchr.pojo.UserListPOJO;
import com.mintcode.launchr.pojo.UserUpdatePOJO;
import com.mintcode.launchr.pojo.entity.ContactUser;
import com.mintcode.launchr.pojo.entity.DepartmentEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.pojo.entity.UserUpdateEntity;
import com.mintcode.launchr.util.GlideRoundTransform;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TTDensityUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.UserUpdateUtil;

/**
 * 通讯录
 *
 * @author KevinQiao
 */
public class ContactPersonOfDeptActivity extends BaseActivity implements OnItemClickListener {

    public static final int NEW_ACTIVITY_REQCODE = 0x22;

    public static final String REFRESH_ACTION = "refresh_action";

    public static final int REQUEST_CODE = 0x11;

    public static final String SORT_CHAR_KEY = "letters";

    public static final String CONTACT_ENTITY_KEY = "contact_entity_key";

    public static final String KEY_DEPART_ID = "key_depart_id";

    public static final String KEY_DEPART_NAME = "key_depart_name";

    /**
     * 返回按钮
     */
    private ImageView mIvBack;

    /**
     * 部门名称
     */
    private TextView mTvTitle;

    /**
     * listview
     */
    private ListView mLvName;

    /**
     * listview
     */
    private ListView mLvDept;

    /**
     * 确定按钮
     */
    private TextView mTvConfirm;

    /**
     * 存放选中联系人布局
     */
    private LinearLayout mLinearContent;

    /**
     * 底部布局
     */
    private RelativeLayout mRelBottom;

    /**
     * 全选按钮
     */
    private ImageView mIvCheck;

    /** 数据集 */
//	private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();

    /**
     * 适配器
     */
    private ContactNameAdapter mContactNameAdapter;

    private DeptAdapter mDeptAdapter;

    private String mDepartId = "";

    private List<UserDetailEntity> mUserList = new ArrayList<UserDetailEntity>();

    private List<DepartmentEntity> mDeptList = new ArrayList<DepartmentEntity>();

    private List<List<DepartmentEntity>> mSecondList = new ArrayList<List<DepartmentEntity>>();

    private List<List<UserDetailEntity>> mSecondUserList = new ArrayList<List<UserDetailEntity>>();

    private Handler mHandler;

    private RefreshBroadcastReceiver mReceiver;

    private String strConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDepartId = getIntent().getStringExtra(KEY_DEPART_ID);
        setContentView(R.layout.activity_contact_person_of_dept);
        registerMyReceiver();

        initView();
        initData();

    }


    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_dept_name);

        mLvName = (ListView) findViewById(R.id.lv_person_of_dept);
        mLvDept = (ListView) findViewById(R.id.lv_second_depart);

        mTvConfirm = (TextView) findViewById(R.id.tv_confirm);
        mLinearContent = (LinearLayout) findViewById(R.id.ll_content);
        mRelBottom = (RelativeLayout) findViewById(R.id.rel_bottom);
        mIvCheck = (ImageView) findViewById(R.id.cb_check);


        mContactNameAdapter = new ContactNameAdapter();
        mLvName.setAdapter(mContactNameAdapter);

        mDeptAdapter = new DeptAdapter();
        mLvDept.setAdapter(mDeptAdapter);

        mLvName.setOnItemClickListener(this);
        mLvDept.setOnItemClickListener(this);
        mIvBack.setOnClickListener(this);
        mIvCheck.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);


        mHandler = new UserHandler();
    }


    private void initData() {
//		mList = new ArrayList<UserDetailEntity>();
        strConfirm = getString(R.string.confirm);

        String deptName = getIntent().getStringExtra(KEY_DEPART_NAME);
        if (deptName != null) {
            mTvTitle.setText(deptName);
        } else {
            mTvTitle.setText("");
        }

        mUserList.clear();
        mDeptList.clear();


        int count = 0;
        List<DepartmentEntity> depList = ContactDBService.getInstance().getSubDepartment(mDepartId);
        if (depList != null && depList.size() > 0) {
            count += 1;
            mDeptList.addAll(depList);
            mDeptAdapter.notifyDataSetChanged();
        }
        List<UserDetailEntity> userList = ContactDBService.getInstance().getUserList(mDepartId);
        if (userList != null && userList.size() > 0) {
            count += 2;

            mUserList.addAll(userList);
            mContactNameAdapter.notifyDataSetChanged();

            Thread t = new Thread() {
                @Override
                public void run() {
                    handleList();
                }
            };
            t.start();
        }
        if (count == 1 || count == 3) {
            UserApi.getInstance().deptLastUpdate(this, mDepartId);
        } else if (count == 2) {
            UserApi.getInstance().userLastUpdate(this, mDepartId);
        } else {
            loadDept();
        }


        // 判断是否选中状态被打开
        if (ContactFragment.mSelectState == ContactFragment.MULTI_SELECT_STATE || ContactFragment.mSelectState == ContactFragment.EDITOR_SELECT_STATE) {
            mRelBottom.setVisibility(View.VISIBLE);
            mIvCheck.setVisibility(View.VISIBLE);
            handleSelectUser();
        } else if (ContactFragment.mSelectState == ContactFragment.SINGLE_SELECT_STATE) {
            mRelBottom.setVisibility(View.VISIBLE);
            mIvCheck.setVisibility(View.GONE);
            handleSelectUser();
        } else {
            mRelBottom.setVisibility(View.GONE);
            mIvCheck.setVisibility(View.GONE);
        }

    }

    /**
     * 加载部门包含部门数据
     */
    private void loadDept() {
        showLoading();
        UserApi.getInstance().getComanyDeptList(this, mDepartId);
    }

    /**
     * 加载部门包含员工
     */
    private void loadUserOfDept() {
        UserApi.getInstance().getComanyUserList(this, mDepartId);
    }


    @Override
    public void onClick(View v) {
        if (v == mIvBack) {
            sendRefresh();
            finish();
        } else if (v == mTvConfirm) {
            sendConfirm();
            setResult(RESULT_OK);
            onBackPressed();
        } else if (mIvCheck == v) {
            checkAll();
        }
    }

    private void checkAll() {
        if (mUserList != null && !mUserList.isEmpty()) {
            List<UserDetailEntity> l = new ArrayList<>();
            l.addAll(mUserList);
            for (int i = 0; i < mUserList.size(); i++) {
                UserDetailEntity u = mUserList.get(i);
                boolean un = isUnSelectUser(u);
                if (un) {
                    l.remove(u);
                }
            }
            boolean b = ContactFragment.mSelectList.containsAll(l);
            if (b) {
                b = false;
                mIvCheck.setImageResource(R.drawable.icon_unchecked);
            } else {
                b = true;
                mIvCheck.setImageResource(R.drawable.icon_blue_checked);
            }
            addAll(b);
        } else {
            toast("no user");
        }
    }

    private void addAll(boolean b) {
        if (!b) {
            for (int i = 0; i < mUserList.size(); i++) {
                UserDetailEntity u = mUserList.get(i);
                boolean isExist = ContactFragment.mSelectList.contains(u);
                if (isExist) {
                    ContactFragment.mSelectList.remove(u);
                }
            }
        } else {
            for (int i = 0; i < mUserList.size(); i++) {
                UserDetailEntity u = mUserList.get(i);
                // 判断不可选人员
                boolean unCheck = isUnSelectUser(u);
                if (!unCheck) {
                    boolean isExist = ContactFragment.mSelectList.contains(u);
                    if (!isExist) {
                        ContactFragment.mSelectList.add(u);
                    }
                }
            }

        }
        sendRefresh();
        mContactNameAdapter.notifyDataSetInvalidated();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        if (parent == mLvDept) {
            DepartmentEntity entity = (DepartmentEntity) parent.getAdapter().getItem(position);
            Intent intent = new Intent(this, ContactPersonOfDeptActivity.class);
            intent.putExtra(ContactPersonOfDeptActivity.KEY_DEPART_ID, entity.getShowId());
            intent.putExtra(ContactPersonOfDeptActivity.KEY_DEPART_NAME, entity.getdName());
            startActivityForResult(intent, REQUEST_CODE);
        } else if (parent == mLvName) {
            UserDetailEntity entity = (UserDetailEntity) parent.getAdapter().getItem(position);
            // 多选模式
            if (ContactFragment.mSelectState == ContactFragment.MULTI_SELECT_STATE || ContactFragment.mSelectState == ContactFragment.EDITOR_SELECT_STATE) {
//				holder.cbCheck.setVisibility(View.VISIBLE);
                // 判断不可选人员
                boolean unCheck = isUnSelectUser(entity);
                if (!unCheck) {
                    boolean b = ContactFragment.mSelectList.contains(entity);
                    if (b) {
                        ContactFragment.mSelectList.remove(entity);
                    } else {
                        ContactFragment.mSelectList.add(entity);
                    }
                    sendBroadcast();
                    mHandler.sendEmptyMessage(1);
                }
            } else
                // 单选模式
                if (ContactFragment.mSelectState == ContactFragment.SINGLE_SELECT_STATE) {
                    // 判断不可选人员
                    boolean unCheck = isUnSelectUser(entity);
                    if (!unCheck) {
                        boolean b = ContactFragment.mSelectList.contains(entity);
                        if (b) {
                            ContactFragment.mSelectList.remove(entity);
                        } else {
                            ContactFragment.mSelectList.clear();
                            ContactFragment.mSelectList.add(entity);
                        }
                        sendBroadcast();
                        mHandler.sendEmptyMessage(1);
                    }
                } else {
                    Intent intent = new Intent(this, PersonDetailActivity.class);
                    intent.putExtra(PersonDetailActivity.KEY_PERSONAL_ID, entity.getShowId());
                    startActivityForResult(intent, 101);
                    p = position;
                }

            mContactNameAdapter.notifyDataSetChanged();
        }


    }

    public void sendRefresh() {
        Intent intent = new Intent(REFRESH_ACTION);
        sendBroadcast(intent);
    }

    public void sendConfirm() {
        Intent intent = new Intent(ContactFragment.CONFIRM_ACTION);
        sendBroadcast(intent);
    }

    public void sendBroadcast() {
        Intent intent = new Intent(ContactFragment.CONTACT_ACTION);
        sendBroadcast(intent);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                handleSelectUser();
            }
        };

        mHandler.postDelayed(r, 200);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (MainActivity.showFirstFragment == 1) {
            // 如果从通讯录发起聊天后，退出聊天后回到消息列表，则该activity要结束掉
            finish();
        }
    }




	class ContactNameAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mUserList.size();
		}

		@Override
		public Object getItem(int position) {
			return mUserList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
  
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.item_contact, null);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.cbCheck = (ImageView) convertView.findViewById(R.id.cb_check);
				holder.tvWord = (TextView) convertView.findViewById(R.id.tv_word);
				holder.tvDept = (TextView) convertView.findViewById(R.id.tv_dept);
				holder.tvNameOfDept = (TextView) convertView.findViewById(R.id.tv_name_of_dept);
				
				holder.tvName.setVisibility(View.GONE);
				holder.tvDept.setVisibility(View.GONE);
				holder.tvWord.setVisibility(View.GONE);
				holder.tvNameOfDept.setVisibility(View.VISIBLE);
				convertView.setTag(holder);
				
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			// 设置数据
			UserDetailEntity entity = mUserList.get(position);
			
			// 设置姓名
			holder.tvNameOfDept.setText(entity.getTrueName());


			// 设置头像
			HeadImageUtil.getInstance(ContactPersonOfDeptActivity.this).setAvatarResourceAppendUrl(holder.ivIcon,entity.getShowId(),2, 150, 150);

            // 设置选中状态
            // 多选模式
            if (ContactFragment.mSelectState == ContactFragment.MULTI_SELECT_STATE
                    || ContactFragment.mSelectState == ContactFragment.SINGLE_SELECT_STATE
                    || ContactFragment.mSelectState == ContactFragment.EDITOR_SELECT_STATE
                    ) {
                holder.cbCheck.setVisibility(View.VISIBLE);
                boolean isUnCheck = isUnSelectUser(entity);
                if (!isUnCheck) {
                    boolean b = ContactFragment.mSelectList.contains(entity);
                    if (b) {
                        holder.cbCheck.setBackgroundResource(R.drawable.icon_blue_checked);
                    } else {
                        holder.cbCheck.setBackgroundResource(R.drawable.icon_unchecked);
                    }
                } else {
                    holder.cbCheck.setBackgroundResource(R.drawable.icon_checked_gray);
                }

            } else {
                holder.cbCheck.setVisibility(View.GONE);
            }

            return convertView;
        }


    }

    int p = -1;

    /**
     * 判断是否是不可选择
     *
     * @param u
     * @return
     */
    private boolean isUnSelectUser(UserDetailEntity u) {
        boolean isExsit = false;
        if (!ContactFragment.mUnSelectList.isEmpty()) {
            for (int i = 0; i < ContactFragment.mUnSelectList.size(); i++) {
                UserDetailEntity e = ContactFragment.mUnSelectList.get(i);
                if (e.getShowId().equals(u.getShowId())) {
                    isExsit = true;
                    break;
                }
            }
        }
        return isExsit;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvDept;
        TextView tvWord;
        ImageView ivIcon;
        ImageView cbCheck;
        TextView tvNameOfDept;

        RelativeLayout relDept;
        TextView tvDeptName;
        TextView tvDeptNum;
    }


    class DeptAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDeptList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDeptList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_contact, null);
                holder.cbCheck = (ImageView) convertView.findViewById(R.id.cb_dept_check);
                holder.relDept = (RelativeLayout) convertView.findViewById(R.id.rel_dept);
                holder.tvDeptNum = (TextView) convertView.findViewById(R.id.tv_dept_num);
                holder.tvDeptName = (TextView) convertView.findViewById(R.id.tv_dept_name);

                convertView.findViewById(R.id.ll_name).setVisibility(View.GONE);
                holder.relDept.setVisibility(View.VISIBLE);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 设置数据
            DepartmentEntity entity = (DepartmentEntity) getItem(position);
            holder.tvDeptName.setText(entity.getdName());


            holder.tvDeptNum.setText("");


            return convertView;
        }


    }


    /**
     * 更新数据适配器
     */
    public void notifyAdater() {
        mUserList.clear();
        mDeptList.clear();

        for (int i = 0; i < ContactFragment.mDataList.size(); i++) {
            UserDetailEntity entity = ContactFragment.mDataList.get(i);
            String deptId = entity.getDeptId();
            if (mDepartId.equals(deptId)) {
                mUserList.add(entity);
            }
        }

        for (int i = 0; i < ContactDeptFragment.mDataList.size(); i++) {
            DepartmentEntity entity = ContactDeptFragment.mDataList.get(i);
            String deptId = entity.getdParentShowId();
            if (mDepartId.equals(deptId)) {
                mDeptList.add(entity);
            }
        }

        mContactNameAdapter.notifyDataSetChanged();

        // 筛选下一级部门的人数
        for (int i = 0; i < mDeptList.size(); i++) {
            List<DepartmentEntity> l = new ArrayList<DepartmentEntity>();
            DepartmentEntity entity = mDeptList.get(i);
            String deptId = entity.getShowId();
            for (int j = 0; j < ContactDeptFragment.mDataList.size(); j++) {
                DepartmentEntity e = ContactDeptFragment.mDataList.get(j);
                String name = e.getdName();
                String id = e.getdParentShowId();
                if (id.equals(deptId)) {
                    l.add(e);
                }
            }
            mSecondList.add(l);
            List<UserDetailEntity> ul = new ArrayList<UserDetailEntity>();
            for (int j = 0; j < ContactFragment.mDataList.size(); j++) {
                UserDetailEntity u = ContactFragment.mDataList.get(j);

                String ud = u.getDeptId();
                if (deptId.equals(ud)) {
                    ul.add(u);
                }
            }
            mSecondUserList.add(ul);

        }

        mDeptAdapter.notifyDataSetChanged();

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        } else if (requestCode == REQUEST_CODE) {
            setResult(RESULT_OK);
            finish();
        } else if (requestCode == 101) {
            mContactNameAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);

        // 判断是否为空
        if (response == null) {
            showNoNetWork();
            dismissLoading();
            return;
        } else
            // 判断是否是列表获取部门列表
            if (taskId.equals(UserApi.TaskId.TASK_URL_GET_COMANY_DEPT_LIST)) {
                handleDeptResult(response);
            } else
                // 判断是否是获取我的部门成员列表返回
                if (taskId.equals(UserApi.TaskId.TASK_URL_GET_COMPANY_USER_LIST)) {
                    handleResultUserList(response);
                }
                // 部门更新时间戳返回
                else if (taskId.equals(UserApi.TaskId.TASK_URL_DEPT_LAST_UPDATE)) {
                    handleDepartmentUpdate(response);
                }
                // 部门成员更新时间戳返回
                else if (taskId.equals(UserApi.TaskId.TASK_URL_USER_LAST_UPDATE)) {
                    handleUserUpdate(response);
                }

        dismissLoading();
    }

    /**
     * 部门成员更新时间戳返回
     */
    private void handleUserUpdate(Object response) {
        UserUpdatePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), UserUpdatePOJO.class);
        if (pojo == null) {
            return;
        } else if (!pojo.isResultSuccess()) {
            return;
        } else if (pojo.getBody().getResponse().getData() != null) {
            UserUpdateEntity entity = pojo.getBody().getResponse().getData();

            long updateTime = UserUpdateUtil.getInstance(this).getUpdateTime(entity.getShowId());
            if (entity.getLastUpadteTime() > updateTime) {
                UserUpdateUtil.getInstance(this).saveUpdateTime(entity.getShowId(), entity.getLastUpadteTime());

                loadDept();
            }
        }
    }

    /**
     * 部门更新时间戳返回
     */
    private void handleDepartmentUpdate(Object response) {
        UserUpdatePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), UserUpdatePOJO.class);
        if (pojo == null) {
            return;
        } else if (!pojo.isResultSuccess()) {
            return;
        } else if (pojo.getBody().getResponse().getData() != null) {
            UserUpdateEntity entity = pojo.getBody().getResponse().getData();

            long updateTime = UserUpdateUtil.getInstance(this).getUpdateTime(entity.getShowId());
            if (entity.getLastUpadteTime() > updateTime) {
                UserUpdateUtil.getInstance(this).saveUpdateTime(entity.getShowId(), entity.getLastUpadteTime());

                loadDept();
            } else {
                UserApi.getInstance().userLastUpdate(this, mDepartId);
            }
        }
    }

    /**
     * @param response
     */
    private void handleDeptResult(Object response) {
        DepartmentPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), DepartmentPOJO.class);

        // 判断是否是为空
        if (pojo != null) {
            if (pojo.isResultSuccess()) {
                mDeptList.clear();
                List<DepartmentEntity> list = pojo.getBody().getResponse().getData();
                if (list != null) {
                    // 保存数据库
                    ContactDBService.getInstance().deleteSubDepartment(list, mDepartId);

                    mDeptList.addAll(list);
                    mDeptAdapter.notifyDataSetChanged();
                    loadUserOfDept();
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
     * 处理我的部门返回
     *
     * @param response
     */
    private void handleResultUserList(Object response) {
        UserListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), UserListPOJO.class);
        if (pojo != null) {
            if (pojo.isResultSuccess()) {
                List<UserDetailEntity> list = pojo.getBody().getResponse().getData();
                if (list != null) {
                    // 保存数据库
                    ContactDBService.getInstance().deleteUserList(list, mDepartId);

                    mUserList.clear();
                    mUserList.addAll(list);
                    mContactNameAdapter.notifyDataSetChanged();


                    Thread t = new Thread() {
                        @Override
                        public void run() {
                            handleList();
                        }
                    };

                    t.start();


				}
			}
		}
	}



	/**
	 * 处理选中人员
	 */
	private void handleSelectUser(){
		mLinearContent.removeAllViews();
		for (int i = 0; i < ContactFragment.mSelectList.size(); i++) {
			UserDetailEntity entity = ContactFragment.mSelectList.get(i);
			ImageView iv = new ImageView(this);
			iv.setId(i);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			p.gravity = Gravity.CENTER_VERTICAL;
			int x = TTDensityUtil.dip2px(this, 36);
			int margin = TTDensityUtil.dip2px(this, 10);
			p.height = x;
			p.width = x;
			p.leftMargin = margin;

			HeadImageUtil.getInstance(this).setAvatarResourceAppendUrl(iv, entity.getShowId(),0,80,80);

			final UserDetailEntity uEntity = entity;
			// 点击头像移除当前选中状态
			View.OnClickListener clickListener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean b = ContactFragment.mSelectList.contains(uEntity);
					if(b){
						ContactFragment.mSelectList.remove(uEntity);
						mContactNameAdapter.notifyDataSetChanged();
						sendBroadcast();
					}
				}
			};
			iv.setOnClickListener(clickListener);
			mLinearContent.addView(iv,p);
			mContactNameAdapter.notifyDataSetChanged();
		}

		mTvConfirm.setText(strConfirm + "(" + ContactFragment.mSelectList .size() + ")");
    }


    private void handleList() {
        List<Integer> list = new ArrayList<>();
        List<UserDetailEntity> l = new ArrayList<>();
        for (int i = 0; i < mUserList.size(); i++) {
            UserDetailEntity u = mUserList.get(i);
            for (int j = 0; j < ContactFragment.mSelectList.size(); j++) {
                UserDetailEntity e = ContactFragment.mSelectList.get(j);
                if (u.getShowId() != null && u.getDeptId() != null && e.getShowId() != null && e.getDeptId() != null)
                    if (e.getShowId().equals(u.getShowId()) && e.getDeptId().equals(u.getDeptId())) {
                        list.add(i);
                        l.add(e);
                    }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            int u = list.get(i);
            UserDetailEntity e = l.get(i);
            mUserList.add(u, e);
            mUserList.remove(u + 1);

        }

        mHandler.sendEmptyMessage(1);


    }


    public class RefreshBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(REFRESH_ACTION, intent.getAction())) {
                handleSelectUser();
                mHandler.sendEmptyMessage(1);
            }
        }

    }

    /**
     * 注册广播
     */
    private void registerMyReceiver() {

        mReceiver = new RefreshBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(REFRESH_ACTION);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }


    private class UserHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mContactNameAdapter.notifyDataSetChanged();
                if (mUserList != null && !mUserList.isEmpty()) {
                    // 判断是否全被选中
                    List<UserDetailEntity> l = new ArrayList<>();
                    l.addAll(mUserList);
                    for (int i = 0; i < mUserList.size(); i++) {
                        UserDetailEntity u = mUserList.get(i);
                        boolean un = isUnSelectUser(u);
                        if (un) {
                            l.remove(u);
                        }
                    }

                    boolean b = ContactFragment.mSelectList.containsAll(l);
                    if (ContactFragment.mSelectList.isEmpty()) {
                        b = false;
                    }
                    if (!b) {
                        mIvCheck.setImageResource(R.drawable.icon_unchecked);
                    } else {
                        mIvCheck.setImageResource(R.drawable.icon_blue_checked);
                    }
                }
            } else if (msg.what == 2) {
                mContactNameAdapter.notifyDataSetChanged();
            }
        }
    }

}