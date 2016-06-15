package com.mintcode.launchr.contact.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
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

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.UserApi;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.contact.activity.ContactPersonOfDeptActivity;
import com.mintcode.launchr.contact.activity.PersonDetailActivity;
import com.mintcode.launchr.pojo.UserListPOJO;
import com.mintcode.launchr.pojo.entity.HeaderEntity;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.TTDensityUtil;
import com.mintcode.launchr.util.TTJSONUtil;

/**
 * 通讯录
 *
 * @author KevinQiao
 */
public class SelectContactFragment extends BaseFragment implements OnItemClickListener, OnPageChangeListener {


    OnSelectContactListner mOnSelectContactListner;


    /**
     * action
     */
    public static final String CONTACT_ACTION = "contact_action";

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
    public static int mSelectState = NO_STATE;

    /**
     * 根view
     */
    private View mView;

    /**
     * 色块
     */
    private ImageView mIvColor;

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
     * 数据集
     */
    public static List<UserDetailEntity> mDataList;

    /**
     * 选中数据集
     */
    public static List<UserDetailEntity> mSelectList = new ArrayList<UserDetailEntity>();

    private static List<String> mUserNameList = new ArrayList<String>();

    /**
     * 按姓名fragment
     */
    private ContactNameFragment mContactNameFragment;

    /**
     * 按部门fragment
     */
    private ContactDeptFragment mContactDeptFragment;

    /**
     * 广播类
     */
    private ContactBroadcastReceiver mReceiver;

    private boolean mIsFirst = false;

    private Handler mHandler = new Handler();


    private TextView mTvSearchUser;

    private TextView mTvSearchDept;

    private String mStrSearchUser;

    private String mStrSearchDept;

    private boolean mIsUserName = false;

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
        if (mIsFirst) {
            initView();
            initData();
            mIsFirst = false;
        }

    }


    private void initView() {
        mIvColor = (ImageView) mView.findViewById(R.id.iv_color);
        mVpContact = (ViewPager) mView.findViewById(R.id.vp_contact);
        mRbtnName = (RadioButton) mView.findViewById(R.id.rbt_name);
        mRbtnDept = (RadioButton) mView.findViewById(R.id.rbt_dept);
        mTvConfirm = (TextView) mView.findViewById(R.id.tv_confirm);
        mLineaSelectUser = (LinearLayout) mView.findViewById(R.id.ll_content);
        mRelBottom = (RelativeLayout) mView.findViewById(R.id.rel_bottom);
        mIvSearch = (ImageView) mView.findViewById(R.id.iv_search);
        mLinearSearch = (LinearLayout) mView.findViewById(R.id.ll_search);
        mEtSearch = (EditText) mView.findViewById(R.id.edt_search);
        mTvSearchCancel = (TextView) mView.findViewById(R.id.tv_cancel);
        mRelTitle = (RelativeLayout) mView.findViewById(R.id.rel_title);
        mScrollView = (ScrollView) mView.findViewById(R.id.scrollview);
        mLvContainUser = (ListView) mView.findViewById(R.id.lv_contain_user);
        mLvContainDept = (ListView) mView.findViewById(R.id.lv_contain_dept);


        mRbtnName.setOnClickListener(this);
        mRbtnDept.setOnClickListener(this);
        mIvSearch.setOnClickListener(this);
        mTvSearchCancel.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);

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
        ;
        mStrSearchUser = getResources().getString(R.string.search_user);
        mStrSearchDept = getResources().getString(R.string.search_dept);
//		mLvContainUser.addHeaderView(mTvSearchUser);
    }


    private void initData() {
        mDataList = new ArrayList<UserDetailEntity>();
        mFragmentList = new ArrayList<Fragment>();

        mContactNameFragment = new ContactNameFragment();

        mContactDeptFragment = new ContactDeptFragment();

        mFragmentList.add(mContactNameFragment);
        mFragmentList.add(mContactDeptFragment);
        ContactPageAdapter contactPageAdapter = new ContactPageAdapter(getFragmentManager());
        mVpContact.setAdapter(contactPageAdapter);
        mVpContact.setOnPageChangeListener(this);

        // 设置适配器
        ContactNameAdapter contactNameAdapter = new ContactNameAdapter();
        mLvContainUser.setAdapter(contactNameAdapter);
        ContactDeptAdapter contactDeptAdapter = new ContactDeptAdapter();
        mLvContainDept.setAdapter(contactDeptAdapter);


        // 设置色块的长度
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screen = dm.widthPixels;
        mWidth = screen / 2;

        mIvColor.getLayoutParams().width = mWidth;


        showLoading();
        UserApi.getInstance().getComanyUserList(this, "");


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
        List<UserDetailEntity> list = new ArrayList<UserDetailEntity>();
        List<List<UserDetailEntity>> deptList = new ArrayList<List<UserDetailEntity>>();
        String condition = str.toLowerCase();
        if (!str.equals("")) {
            for (int i = 0; i < mDataList.size(); i++) {
                UserDetailEntity entity = mDataList.get(i);
                if (entity.getTrueName().toLowerCase().contains(condition)) {
                    list.add(entity);
                }
            }

            for (int i = 0; i < ContactDeptFragment.mDeptList.size(); i++) {
                List<UserDetailEntity> l = ContactDeptFragment.mDeptList.get(i);
                UserDetailEntity entity = l.get(0);
                if (entity.getdName() != null && entity.getdName().toLowerCase().contains(condition)) {
                    deptList.add(l);
                }
            }

            if (!list.isEmpty()) {
                mTvSearchUser.setVisibility(View.VISIBLE);
            }
            if (!deptList.isEmpty()) {
                mTvSearchDept.setVisibility(View.VISIBLE);
            }

            setHeadText(str);
        } else {
            mTvSearchUser.setVisibility(View.GONE);
            mTvSearchDept.setVisibility(View.GONE);
        }


//		mLvContainUser.removeHeaderView(mTvSearchUser);
//		mLvContainUser.addHeaderView(mTvSearchUser);
//		HeaderViewListAdapter h = (HeaderViewListAdapter) mLvContainUser.getAdapter();
//		ContactNameAdapter contactNameAdapter = (ContactNameAdapter) h.getWrappedAdapter();
//		contactNameAdapter.changeData(list);

        ((ContactNameAdapter) mLvContainUser.getAdapter()).changeData(list);
        ((ContactDeptAdapter) mLvContainDept.getAdapter()).changeData(deptList);

    }

    private void setHeadText(String str) {
        String s = mStrSearchUser.substring(0, 2) + " " + str + " " + mStrSearchUser.substring(2);
        SpannableString spanString = new SpannableString(s);
        ForegroundColorSpan span1 = new ForegroundColorSpan(getResources().getColor(R.color.gray));
        ForegroundColorSpan span2 = new ForegroundColorSpan(getResources().getColor(R.color.gray));
        ForegroundColorSpan fs = new ForegroundColorSpan(getResources().getColor(R.color.black));
        spanString.setSpan(span1, 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spanString.setSpan(span2, 4 + str.length(), s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spanString.setSpan(fs, 3, 3 + str.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spanString.setSpan(new AbsoluteSizeSpan(15, true), 0, s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mTvSearchUser.setText(spanString);

        String sd = mStrSearchDept.substring(0, 2) + " " + str + " " + mStrSearchDept.substring(2);
        SpannableString spanStrDept = new SpannableString(sd);
        spanStrDept.setSpan(span1, 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spanStrDept.setSpan(span2, 4 + str.length(), s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spanStrDept.setSpan(fs, 3, 3 + str.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spanStrDept.setSpan(new AbsoluteSizeSpan(15, true), 0, s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mTvSearchDept.setText(spanStrDept);

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
            getActivity().onBackPressed();
//			super.onk
        }

    }

    /**
     * 选人确认操作
     */
    private void confirm() {
        if (mOnSelectContactListner != null) {
            mOnSelectContactListner.onSelectContact(mSelectList);
        }
    }

    /**
     * 切换搜索操作
     */
    private void changeSearch() {
        mLinearSearch.setVisibility(View.VISIBLE);
        mRelTitle.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
    }

    /**
     * 取消搜索操作
     */
    private void cancelSearch() {
        mLinearSearch.setVisibility(View.GONE);
        mRelTitle.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.GONE);

        mEtSearch.setText("");
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
        if (list != null) {
            mUserNameList.addAll(list);
            if (mContactNameFragment != null && mContactDeptFragment != null) {
                handleEditorUserData();
            }
        }

        refreshAdapter();
    }

    public void setSelectUser(int state, List<UserDetailEntity> list) {
        mIsUserName = false;
        mSelectState = state;
        mSelectList.clear();
        if (list != null) {
            mSelectList.addAll(list);
        }

        refreshAdapter();
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
        Intent intent = new Intent(SelectContactFragment.CONTACT_ACTION);
        getActivity().sendBroadcast(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        if (parent == mLvContainUser) {
            UserDetailEntity entity = (UserDetailEntity) parent.getAdapter().getItem(position);
            // 多选模式
            if (SelectContactFragment.mSelectState == SelectContactFragment.MULTI_SELECT_STATE || SelectContactFragment.mSelectState == SelectContactFragment.EDITOR_SELECT_STATE) {
//								holder.cbCheck.setVisibility(View.VISIBLE);
                boolean b = SelectContactFragment.mSelectList.contains(entity);
                if (b) {
                    SelectContactFragment.mSelectList.remove(entity);
                } else {
                    SelectContactFragment.mSelectList.add(entity);
                }
                sendBroadcast();
                ((ContactNameAdapter) parent.getAdapter()).notifyDataSetChanged();
            } else
                // 单选模式
                if (SelectContactFragment.mSelectState == SelectContactFragment.SINGLE_SELECT_STATE) {
                    boolean b = SelectContactFragment.mSelectList.contains(entity);
                    if (b) {
                        SelectContactFragment.mSelectList.remove(entity);
                    } else {
                        SelectContactFragment.mSelectList.clear();
                        SelectContactFragment.mSelectList.add(entity);
                    }
                    sendBroadcast();
                    ((ContactNameAdapter) parent.getAdapter()).notifyDataSetChanged();
                } else {

                    Intent intent = new Intent(getActivity(), PersonDetailActivity.class);
                    intent.putExtra(LauchrConst.KEY_USERENTITY, entity);
                    startActivity(intent);
                }
        } else if (parent == mLvContainDept) {
            List<UserDetailEntity> list = (List<UserDetailEntity>) parent.getAdapter().getItem(position);
            if (SelectContactFragment.mSelectState == SelectContactFragment.MULTI_SELECT_STATE) {
                boolean b = SelectContactFragment.mSelectList.containsAll(list);
                if (b) {
                    SelectContactFragment.mSelectList.removeAll(list);
                } else {
                    SelectContactFragment.mSelectList.addAll(list);
                }
                sendBroadcast();
                ((ContactDeptAdapter) parent.getAdapter()).notifyDataSetChanged();
            } else {
                ContactDeptFragment.mDeptPersonList.clear();
                ContactDeptFragment.mDeptPersonList.addAll(list);
                Intent intent = new Intent(getActivity(), ContactPersonOfDeptActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        // 判断返回是否为空
        if (response == null) {
            showNoNetWork();
            return;
        }

        // 判断是否是获取我的部门列表返回
        if (taskId.equals(UserApi.TaskId.TASK_URL_GET_COMPANY_USER_LIST)) {
            handleResultUserList(response);
            dismissLoading();
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
                    mDataList.clear();
                    mDataList.addAll(list);

//					contamFragmentList.get(0);
                    if (mIsUserName) {
                        handleEditorUserData();
                    }
                    mContactDeptFragment.notifyAdater();
                    mContactNameFragment.notifyAdater();
                } else {
//					toast(entity.getReason());
                }
                Log.i("infos", list.size() + "=");
            } else {
                HeaderEntity entity = pojo.getHeader();
                toast(entity.getReason());
            }

        } else {
            showNetWorkGetDataException();
        }

    }

    //	UserDetailEntity entity;
    int index = -1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        } else if (requestCode == 12) {
//			entity = (UserDetailEntity) data.getSerializableExtra(DepartmentActivity.KEY_USE_ENTITY);
//			index = data.getIntExtra(DepartmentActivity.KEY_POSITION, -1);
//			toast(entity.getU_TRUE_NAME());
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
        resetCursor(index);
        if (index == 0) {
            mRbtnName.setChecked(true);
        } else if (index == 1) {
            mRbtnDept.setChecked(true);
        }

    }

    private void resetCursor(int position) {
        TranslateAnimation animation = new TranslateAnimation(mWidth * position
                , mWidth * position, 0, 0);
        animation.setFillAfter(true);
//		mCurrentIndex = position;
//		Interpolator interpolator = new DecelerateInterpolator(2);
//		animation.setInterpolator(interpolator);
        animation.setDuration(300);
        Log.i("infos", "--------00------------");
        mIvColor.startAnimation(animation);
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
        mReceiver = new ContactBroadcastReceiver();
        SelectContactFragment f = new SelectContactFragment();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SelectContactFragment.CONTACT_ACTION);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    public interface OnSelectContactListner {
        void onSelectContact(List<UserDetailEntity> userList);
    }

    /**
     * 处理选中人员
     */
    private void handleSelectUser() {
        mLineaSelectUser.removeAllViews();
        for (int i = 0; i < SelectContactFragment.mSelectList.size(); i++) {
            ImageView iv = new ImageView(getActivity());
            iv.setId(i);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            p.gravity = Gravity.CENTER_VERTICAL;
            int x = TTDensityUtil.dip2px(getActivity(), 36);
            int margin = TTDensityUtil.dip2px(getActivity(), 10);
            p.height = x;
            p.width = x;
            p.leftMargin = margin;

            String url = "";
            if (i % 3 == 0) {
                url = "http://121.40.30.204:10050/attachment/headPicAttachment/80x80/df431690-b13c-4ab4-990b-fc811e679432.jpg";
            } else if (i % 3 == 1) {
                url = "http://121.40.30.204:10050/attachment/headPicAttachment/80x80/da219741-9840-438e-b298-a90d7ff5ff5d.jpg";
            } else if (i % 3 == 2) {
                url = "http://121.40.30.204:10050/attachment/headPicAttachment/80x80/5aa47427-b792-4cad-ae7d-0eaf4ea3a5a8.jpg";
            }
            if (isAdded()) {
                mContactNameFragment.setImageResuces(iv, url);
            }

            mLineaSelectUser.addView(iv, p);
        }

//		mLineaSelectUser.removeViewAt(index);
        mContactNameFragment.notifyAdapter();
        mContactDeptFragment.notifyAdapter();
    }


    public class ContactBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(CONTACT_ACTION, intent.getAction())) {
                handleSelectUser();
            }
        }

    }

    class ContactNameAdapter extends BaseAdapter {

        private List<UserDetailEntity> mList = new ArrayList<UserDetailEntity>();

        public void changeData(List<UserDetailEntity> list) {
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

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 设置数据
            UserDetailEntity entity = mList.get(position);


            // 设置姓名
            holder.tvName.setText(entity.getTrueName());
            // 设置部门
            holder.tvDept.setText(entity.getdName());

            holder.tvWord.setVisibility(View.GONE);


            // 设置头像
            if (position % 3 == 0) {
                String url = "http://121.40.30.204:10050/attachment/headPicAttachment/80x80/df431690-b13c-4ab4-990b-fc811e679432.jpg";
                mContactNameFragment.setImageResuces(holder.ivIcon, url);
            } else if (position % 3 == 1) {
                String url = "http://121.40.30.204:10050/attachment/headPicAttachment/80x80/da219741-9840-438e-b298-a90d7ff5ff5d.jpg";
                mContactNameFragment.setImageResuces(holder.ivIcon, url);
            } else if (position % 3 == 2) {
                String url = "http://121.40.30.204:10050/attachment/headPicAttachment/80x80/5aa47427-b792-4cad-ae7d-0eaf4ea3a5a8.jpg";
                mContactNameFragment.setImageResuces(holder.ivIcon, url);
            }

            // 设置选中状态
            // 多选模式
            if (SelectContactFragment.mSelectState == SelectContactFragment.MULTI_SELECT_STATE
                    || SelectContactFragment.mSelectState == SelectContactFragment.SINGLE_SELECT_STATE
                    || SelectContactFragment.mSelectState == SelectContactFragment.EDITOR_SELECT_STATE
                    ) {
                holder.cbCheck.setVisibility(View.VISIBLE);
                boolean b = SelectContactFragment.mSelectList.contains(entity);
                Log.i("mes", SelectContactFragment.mSelectList.size() + "======" + b);
                if (b) {
                    holder.cbCheck.setBackgroundResource(R.drawable.icon_blue_checked);
                    Log.i("infos", "======" + b);
                } else {
                    holder.cbCheck.setBackgroundResource(R.drawable.icon_unchecked);
                }
            } else {
                holder.cbCheck.setVisibility(View.GONE);
            }

//			Log.i("mes", ContactFragment.mSelectState + "-");
//			if (isChecked) {
//				hold.cbCheck.setBackgroundResource(R.drawable.icon_green_checked);
//			} else {
//				hold.cbCheck.setBackgroundResource(R.drawable.icon_unchecked);
//			}
            return convertView;
        }


    }


    class ContactDeptAdapter extends BaseAdapter {

        private List<List<UserDetailEntity>> mDeptList = new ArrayList<List<UserDetailEntity>>();

        public void changeData(List<List<UserDetailEntity>> list) {
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

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//			// 设置数据
            List<UserDetailEntity> list = mDeptList.get(position);
            UserDetailEntity entity = list.get(0);
            // 设置部门名称
            if (entity.getdName() != null) {
                holder.tvDeptName.setText(entity.getdName());
            } else {
                holder.tvDeptName.setText("");
            }
            holder.tvDeptNum.setText(list.size() + "");

            // 判断选人状态是否打开
            // 设置选中状态
            if (SelectContactFragment.mSelectState == SelectContactFragment.MULTI_SELECT_STATE) {
                holder.cbCheck.setVisibility(View.VISIBLE);
                boolean b = SelectContactFragment.mSelectList.containsAll(list);
                Log.i("infos", b + "===");
                if (b) {
                    holder.cbCheck.setBackgroundResource(R.drawable.icon_blue_checked);
                } else {
                    holder.cbCheck.setBackgroundResource(R.drawable.icon_unchecked);
                }
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

    private TextView getTextView() {
        TextView tv = new TextView(getActivity());
        int size = TTDensityUtil.dip2px(getActivity(), 15);
        tv.setTextSize(size);
        tv.setTextColor(getResources().getColor(R.color.gray));

        return tv;
    }

    //	OnKeyListener
    public OnSelectContactListner getmOnSelectContactListner() {
        return mOnSelectContactListner;
    }

    public void setOnSelectContactListner(OnSelectContactListner onSelectContactListner) {
        this.mOnSelectContactListner = onSelectContactListner;
    }
}