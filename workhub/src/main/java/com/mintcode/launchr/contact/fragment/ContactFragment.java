package com.mintcode.launchr.contact.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mintcode.im.database.ContactDBService;
import com.mintcode.launchr.activity.MainActivity;
import com.mintcode.launchr.R;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.app.newTask.TaskUtil;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.contact.activity.ContactPersonOfDeptActivity;
import com.mintcode.launchr.contact.activity.PersonDetailActivity;
import com.mintcode.launchr.contact.activity.MyFriendActivity;
import com.mintcode.launchr.contact.activity.MyGroupActivity;
import com.mintcode.launchr.pojo.DepartmentPOJO;
import com.mintcode.launchr.pojo.UserListPOJO;
import com.mintcode.launchr.pojo.UserUpdatePOJO;
import com.mintcode.launchr.pojo.entity.DepartmentEntity;
import com.mintcode.launchr.pojo.entity.HeaderEntity;
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
public class ContactFragment extends BaseFragment implements OnItemClickListener, OnPageChangeListener {


    OnSelectContactListner mOnSelectContactListner;

    OnIsActivityListener mOnIsActivityListener;


    /**
     * action
     */
    public static final String CONTACT_ACTION = "contact_action";

    /**
     * confirm action
     */
    public static final String CONFIRM_ACTION = "confirm_action";

    /**
     * 无状态
     */
    public static final int NO_STATE = 1;

    /**
     * 单选状态打开
     */
    public static final int SINGLE_SELECT_STATE = 4;

    /**
     * 多选状态打开
     */
    public static final int MULTI_SELECT_STATE = 2;

    /**
     * 编辑状态打开
     */
    public static final int EDITOR_SELECT_STATE = 3;

    /**
     * 状态判断
     */
    public static int mSelectState = NO_STATE;//NO_STATE;

    /**
     * 根view
     */
    private View mView;

    /** 色块 */
//	private ImageView mIvColor;

    /**
     * viewpager
     */
    private ViewPager mVpContact;

    /**
     * 按姓名
     */
    private RadioButton mRbtnName;

    /**
     * 按部门
     */
    private RadioButton mRbtnDept;

    /**
     * 确定按钮
     */
    private TextView mTvConfirm;

    /**
     * 搜索按钮
     */
    private ImageView mIvSearch;

    /**
     * 搜索布局
     */
    private LinearLayout mLinearSearch;

    /**
     * 搜索
     */
    private EditText mEtSearch;

    /**
     * 搜索取消
     */
    private TextView mTvSearchCancel;

    /**
     * 标题布局
     */
    private RelativeLayout mRelTitle;

    /**
     * 选项布局
     */
    private LinearLayout mLinearBondTitle;

    /**
     * srcollview 搜索
     */
    private ScrollView mScrollView;

    /**
     * 搜索用户 listview
     */
    private ListView mLvContainUser;

    /**
     * 搜索部门listview
     */
    private ListView mLvContainDept;

    /**
     * 选中人员存放布局
     */
    private LinearLayout mLineaSelectUser;

    /**
     * 底部布局
     */
    private RelativeLayout mRelBottom;

    /**
     * fragment 数据集
     */
    private List<Fragment> mFragmentList;

    /**
     * 色块宽度
     */
    private int mWidth;
//	private 

    /**
     * 返回按钮
     */
    private ImageView mIvBack;

    private TextView mTvTitle;

    private ImageView mIvLeft;

    private ImageView mIvRight;

    /**
     * 数据集
     */
    public static List<UserDetailEntity> mDataList;


    /**
     * 选中数据集
     */
    public static List<UserDetailEntity> mSelectList = new ArrayList<UserDetailEntity>();

    /**
     * 不可选择数据集
     */
    public static List<UserDetailEntity> mUnSelectList = new ArrayList<UserDetailEntity>();

    private static List<String> mUserNameList = new ArrayList<String>();

    /**
     * 按姓名fragment
     */
    private ContactNameFragment mContactNameFragment;

    /**
     * 按部门fragment
     */
    private ContactDeptFragment mContactDeptFragment;

    private ContactNameAdapter mNameAdapter;

    /**
     * 广播类
     */
    private ContactBroadcastReceiver mReceiver = null;

    /**
     * 确定广播类
     */
    private ConfirmBroadcastReceiver mConfirmReceiver = null;


    private boolean mIsFirst = false;

    public static boolean mIsReset = false;

    private Handler mHandler = new Handler();


    private TextView mTvSearchUser;

    private TextView mTvSearchDept;

    private String mStrSearchUser;

    private String mStrSearchDept;

    private boolean mIsUserName = false;

    private String searchKeyword;

    private String strConfirm;

    /**
     * 我的群组
     */
    private LinearLayout mLinMyGroup;
    private RelativeLayout mRelMyGroup;

    /**
     * 我的好友
     */
    private LinearLayout mLinMyFriend;
    private RelativeLayout mRelMyFriend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_contact, null);
            mIsFirst = true;
        } else {
            mIsFirst = false;
        }

        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }


        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIsFirst || mIsReset) {
            if (mIsFirst) {
                initView();
                initData();
            }
            if (mIsFirst || mIsReset) {
                loadDataDB();
            }
            mIsFirst = false;
            mIsReset = false;
        }

        if (mSelectState != NO_STATE) {
            registerMyReceiver();
        }
    }


    private void initView() {
        mIvBack = (ImageView) mView.findViewById(R.id.iv_back);
        mVpContact = (ViewPager) mView.findViewById(R.id.vp_contact);
        mRbtnName = (RadioButton) mView.findViewById(R.id.rbt_name);
        mRbtnDept = (RadioButton) mView.findViewById(R.id.rbt_dept);
        mTvConfirm = (TextView) mView.findViewById(R.id.tv_confirm);
        mLineaSelectUser = (LinearLayout) mView.findViewById(R.id.ll_content_select);
        mRelBottom = (RelativeLayout) mView.findViewById(R.id.rel_bottom);
        mIvSearch = (ImageView) mView.findViewById(R.id.iv_search);
        mLinearSearch = (LinearLayout) mView.findViewById(R.id.ll_search);
        mEtSearch = (EditText) mView.findViewById(R.id.edt_search);
        mTvSearchCancel = (TextView) mView.findViewById(R.id.tv_cancel);
        mRelTitle = (RelativeLayout) mView.findViewById(R.id.rel_title);
        mScrollView = (ScrollView) mView.findViewById(R.id.scrollview);
        mLvContainUser = (ListView) mView.findViewById(R.id.lv_contain_user);
        mLvContainDept = (ListView) mView.findViewById(R.id.lv_contain_dept);
        mTvTitle = (TextView) mView.findViewById(R.id.tv_contact_title);

        mIvLeft = (ImageView) mView.findViewById(R.id.iv_color_left);
        mIvRight = (ImageView) mView.findViewById(R.id.iv_color_right);
        mLinMyGroup = (LinearLayout) mView.findViewById(R.id.lin_my_group);
        mRelMyGroup = (RelativeLayout) mView.findViewById(R.id.rel_my_group);
        mLinMyFriend = (LinearLayout) mView.findViewById(R.id.lin_my_friend);
        mRelMyFriend = (RelativeLayout) mView.findViewById(R.id.rel_my_friend);

        mRbtnName.setOnClickListener(this);
        mRbtnDept.setOnClickListener(this);
        mIvSearch.setOnClickListener(this);
        mTvSearchCancel.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mRelMyGroup.setOnClickListener(this);
        mRelMyFriend.setOnClickListener(this);

        mLvContainUser.setOnItemClickListener(this);
        mLvContainDept.setOnItemClickListener(this);

        ContactWatcher watcher = new ContactWatcher();
        mEtSearch.addTextChangedListener(watcher);

        if (mSelectState != NO_STATE) {
            mRelBottom.setVisibility(View.VISIBLE);
        } else {
            mRelBottom.setVisibility(View.GONE);
        }

        mTvSearchUser = (TextView) mView.findViewById(R.id.tv_contain_user);//getTextView();
        mTvSearchDept = (TextView) mView.findViewById(R.id.tv_contain_dept);
        mStrSearchUser = getResources().getString(R.string.search_user);
        mStrSearchDept = getResources().getString(R.string.search_dept);
//		mLvContainUser.addHeaderView(mTvSearchUser);

        if (getActivity() instanceof MainActivity) {
            mLinMyGroup.setVisibility(View.VISIBLE);
            mLinMyFriend.setVisibility(View.VISIBLE);
            mTvTitle.setText(getString(R.string.main_contact));
        }
    }


    private void initData() {

        mDataList = new ArrayList<UserDetailEntity>();
        mFragmentList = new ArrayList<Fragment>();
        mContactNameFragment = new ContactNameFragment();

        mContactDeptFragment = new ContactDeptFragment();

        mFragmentList.add(mContactDeptFragment);
        ContactPageAdapter contactPageAdapter = new ContactPageAdapter(getFragmentManager());
        mVpContact.setAdapter(contactPageAdapter);
        mVpContact.setOnPageChangeListener(this);

        // 设置适配器
        mNameAdapter = new ContactNameAdapter();
        mLvContainUser.setAdapter(mNameAdapter);
        ContactDeptAdapter contactDeptAdapter = new ContactDeptAdapter();
        mLvContainDept.setAdapter(contactDeptAdapter);


        if (mSelectState == NO_STATE) {
            mIvBack.setVisibility(View.GONE);
        } else {
            mIvBack.setVisibility(View.VISIBLE);
        }

        strConfirm = getString(R.string.confirm);
    }

    /**
     * 从数据库加载数据
     */
    private void loadDataDB() {
        String deptId = LauchrConst.header.getCompanyShowID();
        List<UserDetailEntity> userList = ContactDBService.getInstance().getUserList(deptId);
        if (userList != null && userList.size() > 0) {
            mDataList.clear();
            mDataList.addAll(userList);
            if (mIsUserName) {
                handleEditorUserData();
            }
            mContactDeptFragment.notifyAdater();
            mContactNameFragment.notifyAdater();

            UserApi.getInstance().userLastUpdate(this, deptId);
        } else {
            loadData();
        }
    }

    private void loadData() {
        String deptId = LauchrConst.header.getCompanyShowID();
        UserApi.getInstance().getComanyUserList(this, deptId);
    }

    class ContactWatcher implements TextWatcher {

        private String str = "";
        Runnable r = new Runnable() {

            @Override
            public void run() {
                handleSearch(str);
            }
        };

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            str = s.toString().trim();
            mHandler.removeCallbacks(r);
            mHandler.postDelayed(r, 500);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }

    /**
     * 处理搜索数据
     */
    private void handleSearch(String str) {

        // 通过网络接口获取，先搜索成员，返回结果后搜索部门
        if (!str.equals("")) {
            //String condition = str.toLowerCase();
            searchKeyword = str.toLowerCase();
            UserApi.getInstance().searchUserByKeyword(this, searchKeyword);
//			showLoading();

		}else{
			mTvSearchUser.setVisibility(View.GONE);
			mTvSearchDept.setVisibility(View.GONE);

			List<UserDetailEntity> list = new ArrayList<>();
			((ContactNameAdapter)mLvContainUser.getAdapter()).changeData(list);
			List<DepartmentEntity> depList = new ArrayList<>();
			((ContactDeptAdapter)mLvContainDept.getAdapter()).changeData(depList);
		}
	}
	//TODO 国际化会有问题
	private void setHeadText(String str){
		String keyWord = mEtSearch.getText().toString();
		String user = getString(R.string.search_in) + mEtSearch.getText().toString()
				+ getActivity().getResources().getString(R.string.search_user);
		String dept = getString(R.string.search_in) + mEtSearch.getText().toString()
		        + getActivity().getResources().getString(R.string.search_dept);
		ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.black));
		SpannableString userSpannable = new SpannableString(user);
		SpannableString deptSpannable = new SpannableString(dept);
		if(keyWord != null && keyWord.length()!=0){
			userSpannable.setSpan(foregroundColorSpan, 3, 3+keyWord.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			deptSpannable.setSpan(foregroundColorSpan, 3, 3+keyWord.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		}
		mTvSearchUser.setText(userSpannable);
		mTvSearchDept.setText(deptSpannable);
//		mTvSearchUser.setText(Html.fromHtml(user));
//		mTvSearchDept.setText(Html.fromHtml(dept));
    }

    class ContactPageAdapter extends FragmentPagerAdapter {

        public ContactPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            return mFragmentList.get(index);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

    }


    @Override
    public void onClick(View v) {
        if (v == mRbtnName) {
            resetCursor(0);
            mVpContact.setCurrentItem(0);
        } else if (v == mRbtnDept) {
            resetCursor(1);
            mVpContact.setCurrentItem(1);
        } else if (v == mIvSearch) {
            changeSearch();
        } else if (v == mTvSearchCancel) {
            cancelSearch();
        } else if (v == mTvConfirm) {
            confirm();
            mSelectState = NO_STATE;
            getActivity().onBackPressed();
        } else if (v == mIvBack) {
            getActivity().onBackPressed();
        } else if (v == mRelMyGroup) {// 我的群组
            Intent intent = new Intent(getActivity(), MyGroupActivity.class);
            startActivity(intent);
        } else if (v == mRelMyFriend) {// 我的好友
            Intent intent = new Intent(getActivity(), MyFriendActivity.class);
            startActivity(intent);
        }

    }

    /**
     * 选人确认操作
     */
    private void confirm() {
        if (mOnSelectContactListner != null && mSelectList != null && !mSelectList.isEmpty()) {
            mOnSelectContactListner.onSelectContact(mSelectList);
        }
        unregisterReceiver();
    }

    /**
     * 如果联系人Fragment在另一个Fragment内，则执行该接口的方法
     */
    public void hideFragment() {
        mOnIsActivityListener.onIsActivity();
    }

    /**
     * 切换搜索操作
     */
    private void changeSearch() {
        mLinearSearch.setVisibility(View.VISIBLE);
        mRelTitle.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);

        hideSoftInput(true);
    }

    /**
     * 取消搜索操作
     */
    private void cancelSearch() {
        mLinearSearch.setVisibility(View.GONE);
        mRelTitle.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.GONE);

        mEtSearch.setText("");
        hideSoftInput(false);
    }


    /**
     * 组织架构操作
     */
    private void accessOrgnizationDepartment() {
    }

    private void accessSearch() {
    }


    public void setSelectState(int state, List<String> list) {
        mIsUserName = true;
        mSelectState = state;
        mUserNameList.clear();
//		UserDetailEntity u = new UserDetailEntity();
//		String username = LauchrConst.header.getLoginName();
//		u.setSHOW_ID(username);
//		mUnSelectList.add(u);
        if (list != null) {
            mUserNameList.addAll(list);

            if (mContactNameFragment != null && mContactDeptFragment != null) {
//				handleEditorUserData();
//				List<String> showIdList = new ArrayList<>();
//				for(String showId :mUserNameList){
//					showIdList.add(showId);
//				}
                List<UserDetailEntity> entity = ContactDBService.getInstance().searchUserList(list);
                if (entity != null) {
                    mSelectList.clear();
                    mSelectList.addAll(entity);
                }
                handleSelectUser();
            }
        }

        refreshAdapter();
    }

    public void setSelectUser(int state, List<UserDetailEntity> list) {
//		UserDetailEntity u = new UserDetailEntity();
//		String username = LauchrConst.header.getLoginName();
//		u.setSHOW_ID(username);
//		mUnSelectList.add(u);

        mIsUserName = false;
        mSelectState = state;
        mSelectList.clear();
        mUnSelectList.clear();
        if (list != null) {
            mSelectList.addAll(list);
            handleSelectUser();
        }

        refreshAdapter();
    }

    /**
     * 设置不可选择人员列表
     *
     * @param list
     */
    public void setCannotSelectUser(List<UserDetailEntity> list) {
        mUnSelectList.clear();
        Log.i("infosmessage", list.size() + "----");
        if (list != null) {
            mUnSelectList.addAll(list);
        }
    }

    private void refreshAdapter() {

        if (mContactNameFragment != null) {
            mContactNameFragment.notifyAdapter();
        }
        if (mContactDeptFragment != null) {
            mContactDeptFragment.notifyAdapter();
        }
    }
//	
//	private void overridePendingTransition(int slidInRight, int slidOutRight) {
//		// TODO Auto-generated method stub
//		
//	}

    public void sendBroadcast() {
        Intent intent = new Intent(ContactFragment.CONTACT_ACTION);
        getActivity().sendBroadcast(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        if (parent == mLvContainUser) {
            UserDetailEntity entity = (UserDetailEntity) parent.getAdapter().getItem(position);
            // 多选模式
            if (ContactFragment.mSelectState == ContactFragment.MULTI_SELECT_STATE || ContactFragment.mSelectState == ContactFragment.EDITOR_SELECT_STATE) {
                boolean unCheck = isUnSelectUser(entity);
                if (!unCheck) {
                    boolean b = ContactFragment.mSelectList.contains(entity);
                    if (b) {
                        ContactFragment.mSelectList.remove(entity);
                    } else {
                        ContactFragment.mSelectList.add(entity);
                    }
                    sendBroadcast();
                    ((ContactNameAdapter) parent.getAdapter()).notifyDataSetChanged();
                }
            } else
                // 单选模式
                if (ContactFragment.mSelectState == ContactFragment.SINGLE_SELECT_STATE) {
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
                        ((ContactNameAdapter) parent.getAdapter()).notifyDataSetChanged();
                    }

                } else {
                    Intent intent = new Intent(getActivity(), PersonDetailActivity.class);
                    intent.putExtra(PersonDetailActivity.KEY_PERSONAL_ID, entity.getShowId());
                    startActivity(intent);
                    hideSoftInput(false);
                }
        } else if (parent == mLvContainDept) {
            DepartmentEntity entity = (DepartmentEntity) parent.getAdapter().getItem(position);
            Intent intent = new Intent(getActivity(), ContactPersonOfDeptActivity.class);
            intent.putExtra(ContactPersonOfDeptActivity.KEY_DEPART_ID, entity.getShowId());
            intent.putExtra(ContactPersonOfDeptActivity.KEY_DEPART_NAME, entity.getdName());
            startActivityForResult(intent, 13);
        }
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        // 判断返回是否为空
        if (response == null) {
            showNoNetWork();
            dismissLoading();
            return;
        } else

            // 判断是否是获取我的部门列表返回
            if (taskId.equals(UserApi.TaskId.TASK_URL_GET_COMPANY_USER_LIST)) {
                handleResultUserList(response);
                dismissLoading();
            }
            // 判断是否是搜索成员返回
            else if (taskId.equals(UserApi.TaskId.TASK_URL_SEARCH_USER)) {
                handleResultSearchUser(response);
                UserApi.getInstance().searchDepByKetword(this, searchKeyword);
                dismissLoading();
            }
            // 判断是否是搜索部门返回
            else if (taskId.equals(UserApi.TaskId.TASK_URL_SEARCH_DEP)) {
                handleResultSearchDep(response);
                dismissLoading();
            }
            // 部门成员更新时间戳返回
            else if (taskId.equals(UserApi.TaskId.TASK_URL_SEARCH_DEP)) {
                handleUserUpdate(response);
                dismissLoading();
            }
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

            long updateTime = UserUpdateUtil.getInstance(getActivity()).getUpdateTime(entity.getShowId());
            if (entity.getLastUpadteTime() > updateTime) {
                UserUpdateUtil.getInstance(getActivity()).saveUpdateTime(entity.getShowId(), entity.getLastUpadteTime());

                loadData();
            }
        }
    }

    /**
     * 处理搜索部门返回
     */
    private void handleResultSearchDep(Object response) {
        DepartmentPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), DepartmentPOJO.class);
        if (pojo == null) {
            return;
        } else if (pojo.isResultSuccess() == false) {
            return;
        } else if (pojo.getBody().getResponse().getData() != null) {
            List<DepartmentEntity> list = pojo.getBody().getResponse().getData();

            if (!list.isEmpty()) {
                setHeadText("");
                mTvSearchDept.setVisibility(View.VISIBLE);
            } else {
                mTvSearchDept.setVisibility(View.GONE);
            }

            ((ContactDeptAdapter) mLvContainDept.getAdapter()).changeData(list);
        }
    }

    /**
     * 处理搜索返回
     */
    private void handleResultSearchUser(Object response) {
        UserListPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), UserListPOJO.class);
        if (pojo == null) {
            return;
        } else if (pojo.isResultSuccess() == false) {
            return;
        } else if (pojo.getBody().getResponse().getData() != null) {
            List<UserDetailEntity> list = pojo.getBody().getResponse().getData();

            if (!list.isEmpty()) {
                setHeadText("");
                mTvSearchUser.setVisibility(View.VISIBLE);
            } else {
                mTvSearchUser.setVisibility(View.GONE);
            }

            ((ContactNameAdapter) mLvContainUser.getAdapter()).changeData(list);
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
            if (pojo.getBody().getResponse().getReason().equals("开始时间不能为空")) {
                Log.i("infos", "==----===" + pojo.getBody().getResponse().getReason());
                Log.i("infos", "==----===" + response.toString());
            }
            if (pojo.isResultSuccess()) {
                List<UserDetailEntity> list = pojo.getBody().getResponse().getData();
                if (list != null) {
                    // 保存数据库
                    String deptId = LauchrConst.header.getCompanyShowID();
                    ContactDBService.getInstance().deleteUserList(list, deptId);

                    mDataList.clear();
                    mDataList.addAll(list);

                    if (mIsUserName) {
                        handleEditorUserData();
                    }
                    mContactDeptFragment.notifyAdater();
                    mContactNameFragment.notifyAdater();
                } else {
                }
            } else {
                HeaderEntity entity = pojo.getHeader();
                toast(entity.getReason());
            }

        } else {
            showNetWorkGetDataException();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        } else if (requestCode == 12) {

        } else if (requestCode == 13) {

        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int index) {
        if (index == 0) {
            mIvLeft.setVisibility(View.VISIBLE);
            mIvRight.setVisibility(View.INVISIBLE);
            mRbtnName.setChecked(true);
        } else if (index == 1) {
            mIvRight.setVisibility(View.VISIBLE);
            mIvLeft.setVisibility(View.INVISIBLE);
            mRbtnDept.setChecked(true);
        }

    }

    private void resetCursor(int position) {
        TranslateAnimation animation = new TranslateAnimation(mWidth * position
                , mWidth * position, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(300);
    }

    /***
     * 处理编辑状态的用户信息显示
     */
    private void handleEditorUserData() {
        mSelectList.clear();
        for (int i = 0; i < mDataList.size(); i++) {
            UserDetailEntity entity = mDataList.get(i);
            boolean b = false;

            for (int j = 0; j < mUserNameList.size(); j++) {
                String name = mUserNameList.get(j);
                if (name.equals(entity.getName())) {
                    b = true;
                    break;
                }

            }
            if (b) {
                mSelectList.add(entity);
            }
        }
        handleSelectUser();
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    /**
     * 注册广播
     */
    private void registerMyReceiver() {
        if (mReceiver == null) {
            mReceiver = new ContactBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ContactFragment.CONTACT_ACTION);
            getActivity().registerReceiver(mReceiver, filter);
        }

        if (mConfirmReceiver == null) {
            mConfirmReceiver = new ConfirmBroadcastReceiver();
            IntentFilter confirmFilter = new IntentFilter();
            confirmFilter.addAction(ContactFragment.CONFIRM_ACTION);
            getActivity().registerReceiver(mConfirmReceiver, confirmFilter);
        }


    }

    @Override
    public void onPause() {
        super.onPause();


	}
	
	
	@Override
	public void onDestroy() {
		unregisterReceiver();
		super.onDestroy();
	}

	private void unregisterReceiver(){
		if(mSelectState != NO_STATE){
			if(mReceiver != null){
				getActivity().unregisterReceiver(mReceiver);
				mReceiver = null;
			}

			if(mConfirmReceiver != null){
				getActivity().unregisterReceiver(mConfirmReceiver);
				mConfirmReceiver = null;
			}
		}
	}
	/** 用于调用处停止广播接收用*/
	public void stopReciver(Context contenxt){
			if(mReceiver != null){
				contenxt.unregisterReceiver(mReceiver);
				mReceiver = null;
			}
			if(mConfirmReceiver != null){
				contenxt.unregisterReceiver(mConfirmReceiver);
				mConfirmReceiver = null;
			}
	}
	/** 用于调用处重启广播接接收用*/
	public void startReciver(Context contenxt){
		if(mReceiver == null){
			mReceiver = new ContactBroadcastReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(ContactFragment.CONTACT_ACTION);
			contenxt.registerReceiver(mReceiver, filter);
		}
		if(mConfirmReceiver == null){
			mConfirmReceiver = new ConfirmBroadcastReceiver();
			IntentFilter confirmFilter = new IntentFilter();
			confirmFilter.addAction(ContactFragment.CONFIRM_ACTION);
			contenxt.registerReceiver(mConfirmReceiver, confirmFilter);
		}
	}

	public interface OnSelectContactListner{
		void onSelectContact(List<UserDetailEntity> userList);
	}
	
	public interface OnIsActivityListener{
		void onIsActivity();
	}
	
	/**
	 * 处理选中人员
	 */
	private void handleSelectUser(){
		if(mLineaSelectUser != null) {
			mLineaSelectUser.removeAllViews();
			for (int i = 0; i < ContactFragment.mSelectList.size(); i++) {
				UserDetailEntity entity = ContactFragment.mSelectList.get(i);
				ImageView iv = new ImageView(getActivity());
				iv.setId(i);
				LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				p.gravity = Gravity.CENTER_VERTICAL;
				int x = TTDensityUtil.dip2px(getActivity(), 36);
				int margin = TTDensityUtil.dip2px(getActivity(), 10);
				p.height = x;
				p.width = x;
				p.leftMargin = margin;
				if (isAdded()) {

					// 设置头像
					HeadImageUtil.getInstance(getActivity()).setAvatarResourceAppendUrl(iv, entity.getShowId(), 2, 80, 80);
					final UserDetailEntity uEntity = entity;
					// 点击头像移除当前选中状态
					View.OnClickListener clickListener = new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							boolean b = ContactFragment.mSelectList.contains(uEntity);
							if (b) {
								ContactFragment.mSelectList.remove(uEntity);
								sendBroadcast();
							}
						}
					};
					iv.setOnClickListener(clickListener);
					mLineaSelectUser.addView(iv, p);
				}

				mTvConfirm.setText(strConfirm + "(" + mSelectList.size() + ")");
				mContactNameFragment.notifyAdapter();
				mContactDeptFragment.notifyAdapter();
				mNameAdapter.notifyDataSetChanged();
			}
		}
	}


	public void setImageResuces(ImageView iv, String url){
		// 处理圆形图片
		if (isAdded()) {
			RequestManager man = Glide.with(this);
			GlideRoundTransform g =  new GlideRoundTransform(getActivity(),3);
			man.load(url).transform(g).placeholder(R.drawable.icon_head_default).crossFade().into(iv);
		}
	}

	/**
	 * 处理选中人BroadcastReceiver
	 */
	public class ContactBroadcastReceiver extends BroadcastReceiver{
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (CONTACT_ACTION == intent.getAction()) {
				handleSelectUser();
			}
		}
		
	}

	/**
	 * 多层嵌套处理确定按钮BroadcastReceiver
	 */
	public class ConfirmBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if (CONFIRM_ACTION.equals(intent.getAction())) {
				confirm();
				mSelectState = NO_STATE;
				mSelectList.clear();
				getActivity().onBackPressed();
			}
		}

	}


	
	class ContactNameAdapter extends BaseAdapter {

		private List<UserDetailEntity> mList = new ArrayList<UserDetailEntity>();
		
		public void changeData(List<UserDetailEntity> list){
			mList.clear();
			if (list != null) {
				mList.addAll(list);
			}
			notifyDataSetChanged();
		}
		
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
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getActivity().getLayoutInflater().inflate(R.layout.item_contact, null);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.cbCheck = (ImageView) convertView.findViewById(R.id.cb_check);
				holder.tvWord = (TextView) convertView.findViewById(R.id.tv_word);
				holder.tvDept = (TextView) convertView.findViewById(R.id.tv_dept);
				convertView.setTag(holder);
				
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			// 设置数据
			UserDetailEntity entity = mList.get(position);
			
			
			// 设置姓名
			holder.tvName.setText(getHighlightText(entity.getTrueName()));
			// 设置部门
			holder.tvDept.setText(entity.getdName());
			
			holder.tvWord.setVisibility(View.GONE);
			
			
			// 设置头像
			HeadImageUtil.getInstance(getActivity()).setAvatarResourceAppendUrl(holder.ivIcon, entity.getShowId(), 2,80,80);
			// 设置选中状态
			// 多选模式
			if (ContactFragment.mSelectState == ContactFragment.MULTI_SELECT_STATE
				|| 	ContactFragment.mSelectState == ContactFragment.SINGLE_SELECT_STATE
				|| 	ContactFragment.mSelectState == ContactFragment.EDITOR_SELECT_STATE
					) {
				holder.cbCheck.setVisibility(View.VISIBLE);
				boolean isUnCheck = isUnSelectUser(entity);
				if(!isUnCheck){
					boolean b = ContactFragment.mSelectList.contains(entity);
					Log.i("mes", ContactFragment.mSelectList.size() + "======" + b);
					if (b) {
						holder.cbCheck.setBackgroundResource(R.drawable.icon_blue_checked);
						Log.i("infos", "======" + b);
					}else{
						holder.cbCheck.setBackgroundResource(R.drawable.icon_unchecked);
					}
				}else {
					holder.cbCheck.setBackgroundResource(R.drawable.icon_checked_gray);
				}

			}else{
				holder.cbCheck.setVisibility(View.GONE);
			}
			return convertView;
		}

		
		
		
	}

	/**
	 * 判断是否是不可选择
	 * @param u
	 * @return
	 */
	private boolean isUnSelectUser(UserDetailEntity u){
		boolean isExsit = false;
		if(!ContactFragment.mUnSelectList.isEmpty()){
			for (int i = 0; i < ContactFragment.mUnSelectList.size(); i ++ ){
				UserDetailEntity e = ContactFragment.mUnSelectList.get(i);
				if(e.getShowId().equals(u.getShowId())){
					isExsit = true;
					break;
				}
			}
		}
		return isExsit;
	}



	
	class ContactDeptAdapter extends BaseAdapter{

		private List<DepartmentEntity> mDeptList = new ArrayList<DepartmentEntity>();
		
		public void changeData(List<DepartmentEntity> list){
			mDeptList.clear();
			if (list != null) {
				mDeptList.addAll(list);
			}
			notifyDataSetChanged();
		}
		
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
				convertView = getActivity().getLayoutInflater().inflate(R.layout.item_contact, null);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.cbCheck = (ImageView) convertView.findViewById(R.id.cb_dept_check);
				holder.tvWord = (TextView) convertView.findViewById(R.id.tv_word);
				holder.tvDept = (TextView) convertView.findViewById(R.id.tv_dept);
				holder.relDept = (RelativeLayout) convertView.findViewById(R.id.rel_dept);
				holder.tvDeptNum = (TextView) convertView.findViewById(R.id.tv_dept_num);
				holder.tvDeptName = (TextView) convertView.findViewById(R.id.tv_dept_name);
				
				convertView.findViewById(R.id.ll_name).setVisibility(View.GONE);
				holder.relDept.setVisibility(View.VISIBLE);
				convertView.setTag(holder);
				
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
//			// 设置数据
            DepartmentEntity entity = mDeptList.get(position);
            // 设置部门名称
            if (entity.getdName() != null) {
                holder.tvDeptName.setText(getHighlightText(entity.getdName()));
            } else {
                holder.tvDeptName.setText("");
            }
            holder.tvDeptNum.setText(entity.getdAvailableCount() + "");

            // 判断选人状态是否打开
            // 设置选中状态
            if (ContactFragment.mSelectState == ContactFragment.MULTI_SELECT_STATE) {
                holder.cbCheck.setVisibility(View.VISIBLE);
                holder.cbCheck.setBackgroundResource(R.drawable.icon_unchecked);
            } else {
                holder.cbCheck.setVisibility(View.GONE);
            }


            return convertView;
        }


    }

    class ViewHolder {
        TextView tvName;
        TextView tvDept;
        TextView tvWord;
        ImageView ivIcon;
        ImageView cbCheck;
        RelativeLayout relDept;
        TextView tvDeptName;
        TextView tvDeptNum;
    }

    @SuppressLint("ResourceAsColor")
    private TextView getTextView() {
        TextView tv = new TextView(getActivity());
        int size = TTDensityUtil.dip2px(getActivity(), 15);
        tv.setTextSize(size);
        tv.setTextColor(getResources().getColor(R.color.gray));

        return tv;
    }

    public OnSelectContactListner getOnSelectContactListner() {
        return mOnSelectContactListner;
    }

    public void setOnSelectContactListner(OnSelectContactListner onSelectContactListner) {
        this.mOnSelectContactListner = onSelectContactListner;
    }

    public void setOnIsActivityListener(OnIsActivityListener isActivityListener) {
        this.mOnIsActivityListener = isActivityListener;
    }


    private SpannableStringBuilder getHighlightText(String context) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(context);
        for (int index : getAllIndex(context)) {
            ssb.setSpan(new BackgroundColorSpan(getActivity().getResources().getColor(R.color.highlight_text_color)),
                    index, index + mEtSearch.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssb;
    }

    //高亮字符串的索引
    private List<Integer> getAllIndex(String content) {
        List<Integer> list = new ArrayList<Integer>();
        int start = 0;
        int index;
        String contentNoCase = content.toLowerCase();
        String serchWordNoCase = mEtSearch.getText().toString().toLowerCase();
        while ((index = contentNoCase.indexOf(serchWordNoCase, start)) > -1) {
            start = index + 1;
            list.add(index);
        }
        return list;
    }

    /**
     * 隐藏输入法
     */
    private void hideSoftInput(boolean isOpen) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isOpen) {
            mEtSearch.requestFocus();
            inputMethodManager.showSoftInput(mEtSearch, InputMethodManager.SHOW_FORCED);
        } else {
            inputMethodManager.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);
        }
    }
}