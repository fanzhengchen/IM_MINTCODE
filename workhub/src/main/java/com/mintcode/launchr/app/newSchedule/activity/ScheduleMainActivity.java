package com.mintcode.launchr.app.newSchedule.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.app.newSchedule.fragment.CalendarMonthFragment;
import com.mintcode.launchr.app.newSchedule.fragment.NewWeekFragment;
import com.mintcode.launchr.app.newSchedule.view.SchedulePopWindow;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.contact.fragment.ContactFragment;
import com.mintcode.launchr.pojo.entity.UserDetailEntity;
import com.mintcode.launchr.util.MixPanelUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by JulyYu on 2016/3/4.
 */
public class ScheduleMainActivity extends BaseActivity implements ContactFragment.OnSelectContactListner {


    /** 标题栏年 */
    private TextView mTvYear;
    /** 标题栏月 */
    private TextView mTvMonth;
    /** 返回 */
    private ImageView mIvBack;
    /** 操作菜单*/
    private ImageView mIvAdd;

    private Fragment mFragment;
    /** 操作菜单页面*/
    private SchedulePopWindow mPopWindow;
    /** 月视图*/
    private RadioButton mRbtMonth;
    /** 周视图*/
    private RadioButton mRbtWeek;
    /** 今日*/
    private RadioButton mRbtToday;
    /** 返回我的日程*/
    private LinearLayout mLlComeBack;
    /** 其他日程人的名字*/
    private TextView mTvOtherName;
    /** 月*/
    private RelativeLayout mRlMonth;
    /** 周*/
    private RelativeLayout mRlWeek;
    /** 今天*/
    private RelativeLayout mRlToday;
    /** 我的日程*/
    public static boolean isMySchedule = true;
    /** 其他人的userId*/
    public static String mOtherUserId;

    /** 通讯录*/
    private ContactFragment mContactFragment;
    private List<String> mNameList;
    private List<String> mUserList;
    private List<UserDetailEntity> mAssignUserList;

    private int mTodayYear;
    private int mTodayMonth;
    private int mTodayDay;
    private String mStrMonth;
    private String mStrDay;
    private SimpleDateFormat mMonthDayData;
    private long mLongToday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_main);
        initView();
        initDate();
        MixPanelUtil.sendEvent(this,MixPanelUtil.APPCENTER_CALENDAR_EVENT);
    }


    private void initView() {

        mTvYear = (TextView)findViewById(R.id.tv_year);
        mTvMonth = (TextView)findViewById(R.id.tv_month);
        mIvBack = (ImageView)findViewById(R.id.iv_back);
        mIvAdd = (ImageView)findViewById(R.id.iv_add);

        mRbtMonth = (RadioButton)findViewById(R.id.rd_month);
        mRbtWeek = (RadioButton)findViewById(R.id.rd_week);
        mRbtToday = (RadioButton)findViewById(R.id.rd_today);
        mLlComeBack = (LinearLayout)findViewById(R.id.ll_come_back);
        mTvOtherName = (TextView)findViewById(R.id.tv_name);
        mRlMonth = (RelativeLayout)findViewById(R.id.rl_month);
        mRlWeek = (RelativeLayout)findViewById(R.id.rl_week);
        mRlToday = (RelativeLayout)findViewById(R.id.rl_today);

        mIvBack.setOnClickListener(this);
        mIvAdd.setOnClickListener(this);
        mLlComeBack.setOnClickListener(this);
        mRlMonth.setOnClickListener(this);
        mRlWeek.setOnClickListener(this);
        mRlToday.setOnClickListener(this);
        mFragment = new CalendarMonthFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_pager,mFragment).show(mFragment).commit();
    }

    private void initDate() {

        mAssignUserList = new ArrayList<UserDetailEntity>();
        mNameList = new ArrayList<String>();
        mUserList = new ArrayList<String>();
        mStrMonth = getResources().getString(R.string.month);
        mStrDay = getResources().getString(R.string.day);
        mMonthDayData = new SimpleDateFormat("MM" + mStrMonth + "dd" + mStrDay);

        Calendar today = Calendar.getInstance();
        mLongToday = today.getTimeInMillis();
        mTodayYear = today.get(Calendar.YEAR);
        mTodayMonth = today.get(Calendar.MONTH) + 1;
        mTodayDay = today.get(Calendar.DAY_OF_MONTH);
        mTvMonth.setText(mTodayMonth + mStrMonth + "");
        mTvYear.setText(mTodayYear + "");
    }
    /** 初始化通讯录的内容*/
    private void initContant() {
        // 准备联系人

        Handler handler = new Handler();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                initUser();
            }
        };

        handler.postDelayed(r, 200);
    }

    private void initUser(){
        mContactFragment = new ContactFragment();
        mContactFragment.setOnSelectContactListner(this);
        mContactFragment.setSelectUser(ContactFragment.SINGLE_SELECT_STATE, null);
        getSupportFragmentManager().beginTransaction().add(R.id.lin_contact_content, mContactFragment)
                .hide(mContactFragment).commit();
    }

    public void showFragmentForChoose(){
        isFragmentAdd();
        if (mUserList != null && !mUserList.isEmpty()) {
            getSupportFragmentManager().beginTransaction().show(mContactFragment).commit();
            mContactFragment.setSelectState(ContactFragment.SINGLE_SELECT_STATE,
                    mUserList);
        } else {
            getSupportFragmentManager().beginTransaction().show(mContactFragment).commit();
            mContactFragment.setSelectUser(ContactFragment.SINGLE_SELECT_STATE,
                    mAssignUserList);// (DeptFragment.EDITOR_SELECT_STATE,
        }
        mContactFragment.startReciver(ScheduleMainActivity.this);
    }
    /** 判断fragment有没有加入,没有加入则加入 */
    private void isFragmentAdd(){
        boolean isAdd = mContactFragment.isAdded();
        if(!isAdd){
            getSupportFragmentManager().beginTransaction().add(R.id.lin_contact_content, mContactFragment)
                    .hide(mContactFragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mContactFragment == null){
            initContant();
        }else{
            mContactFragment.startReciver(ScheduleMainActivity.this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCastBoard();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopCastBoard();
    }

    /** 停止广播 避免与其他页面选人冲突*/
    public void stopCastBoard(){
        if(mContactFragment != null){
            mContactFragment.stopReciver(ScheduleMainActivity.this);
        }
    }


    @Override
    public void onClick(View v){
        super.onClick(v);
        if(v == mIvBack){ // 返回
            this.finish();
        }else if(v == mIvAdd){ // 操作栏
            if(mPopWindow == null){
                mPopWindow = new SchedulePopWindow(this);
            }
            mPopWindow.showAsDropDown(mIvAdd);
        }else if(v == mRlMonth){ // 月视图
            if(!mRbtMonth.isChecked()){
                mFragment = new CalendarMonthFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_pager, mFragment).show(mFragment).commitAllowingStateLoss();
                mRbtMonth.setChecked(true);
                mRbtWeek.setChecked(false);
            }
        }else if(v == mRlWeek){ // 周视图
            if(!mRbtWeek.isChecked()){
                mFragment = new NewWeekFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_pager, mFragment).show(mFragment).commitAllowingStateLoss();
                mRbtWeek.setChecked(true);
                mRbtMonth.setChecked(false);
            }
        }else if(v == mRlToday){// 今天
            if(mFragment instanceof CalendarMonthFragment){
                ((CalendarMonthFragment)mFragment).setPageInTodayMonth();
            }else if(mFragment instanceof NewWeekFragment){
                ((NewWeekFragment)mFragment).setTimeInTodayWeek();
            }
        }else if(v == mLlComeBack){// 返回我的日程
            mIvAdd.setVisibility(View.VISIBLE);
            mLlComeBack.setVisibility(View.GONE);
            if(mFragment instanceof CalendarMonthFragment){
                isMySchedule = true;
                ((CalendarMonthFragment)mFragment).getScheduleList();
            }else if(mFragment instanceof NewWeekFragment){
                isMySchedule = true;
                ((NewWeekFragment)mFragment).setBackMySchedule();
            }
        }
    }
    /** 设置显示年月日*/
    public void getYearMonth( int year,int month){
        if(year != mTodayYear || month != mTodayMonth){
            mRbtToday.setChecked(false);
        }else{
            mRbtToday.setChecked(true);
        }
        mTvMonth.setText(month  + mStrMonth + "");
        mTvYear.setText(year + "");
    }

    /** 周设置是否是今天*/
    public boolean getYearWeek( int year,int month, int day,long startTime,long endTime){
        boolean result = false;

        if(mLongToday <= endTime && mLongToday >= startTime){
            mRbtToday.setChecked(true);
        }else{
            mRbtToday.setChecked(false);
        }
        String strStartTime = mMonthDayData.format(startTime);
        String strEndTime = mMonthDayData.format(endTime);

        mTvMonth.setText(strStartTime + "~" + strEndTime);
        mTvYear.setText(year + "");
        return result;
    }

    @Override
    public void onSelectContact(List<UserDetailEntity> userList) {
        mNameList.clear();
        mUserList.clear();
        if(userList!=null && userList.size()>0){
            selectRequireUser(userList);
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        if(imm.isActive()){
            imm.hideSoftInputFromWindow(mRbtMonth.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    private void selectRequireUser(List<UserDetailEntity> userList) {

        UserDetailEntity entity= userList.get(0);

        String  otherUserId =  entity.getShowId();
        String otherUserName = entity.getTrueName();

        if(mFragment != null){
            if(mFragment instanceof CalendarMonthFragment){
                mLlComeBack.setVisibility(View.VISIBLE);
                mTvOtherName.setText(otherUserName);
                ScheduleMainActivity.isMySchedule = false;
                mOtherUserId = otherUserId;
                ((CalendarMonthFragment) mFragment).getScheduleList();
            }else if(mFragment instanceof NewWeekFragment){
                mLlComeBack.setVisibility(View.VISIBLE);
                mTvOtherName.setText(otherUserName);
                ScheduleMainActivity.isMySchedule = false;
                mOtherUserId = otherUserId;
                ((NewWeekFragment)mFragment).setBackMySchedule();
            }
            mIvAdd.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        boolean hidden = mContactFragment.isHidden();
        if (hidden) {
            ContactFragment.mSelectState = 1;
            mContactFragment.stopReciver(this);
            getSupportFragmentManager().beginTransaction().remove(mContactFragment).commitAllowingStateLoss();
            isMySchedule = true;
            finish();
        } else {
            getSupportFragmentManager().beginTransaction().hide(mContactFragment).commitAllowingStateLoss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isMySchedule = true;
        mOtherUserId = null;
    }

}





