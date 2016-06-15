package com.mintcode.launchr.app.newSchedule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.ScheduleApi;
import com.mintcode.launchr.app.newSchedule.util.ScheduleEventUtil;
import com.mintcode.launchr.app.newSchedule.fragment.DayFragment;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.SchedulePOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.EventEntity;
import com.mintcode.launchr.util.TTDensityUtil;
import com.mintcode.launchr.util.TTJSONUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JulyYu on 2016/3/14.
 */
public class ScheduleOneDayActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    private ViewPager mViewPager;

    private DayPagerAdapter mAdapter;

    /** 名字*/
    private String mStrUserName;
    /** 用户名*/
    private String mUserName;
    /** 用户所在公司*/
    private String mUserCompany;
    /** 用户Id*/
    private String mUserId;

    /** 当月*/
    private int mMonth;
    /** 开始时间*/
    private long mStartTime;
    /** 结束时间*/
    private long mEndTime;

    private Calendar mTodayCalendar;
    private Calendar mCalendar;
    /** 日程事件集合 */
    private HashMap<String, List<EventEntity>> mEventDay = new HashMap<String, List<EventEntity>>();

    /** 初始年*/
    private int mOrgYear;
    /** 初始月 */
    private int mOrgMonth;
    /** 初始天*/
    private int mOrgDay;

    /** 现在所在位置*/
    private  int mNowSelectedPosition ;

    /** 显示的天数*/
    private final int DAYCOUNTS = 500;
    /** 获取时间标记*/
    public final static String TIME_KEY = "time";
    /** 获取他人ID标记*/
    public final static String OTHER_USER_ID = "other_user_id";
    /** 获取是否为自己标记*/
    public final static String IS_MYSCHEDULE = "is_myschedule";

    private boolean mIsMySchedule = true;
    private String mOtherUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_day);
        initView();
        initData();
    }



    private void initView(){
        mViewPager = (ViewPager)findViewById(R.id.vp_schedule_day);
        // pageCount设置红缓存的页面数
        mViewPager.setOffscreenPageLimit(3);
        // 设置2张图之前的间距
        int margin = TTDensityUtil.dip2px(this,10);
        mViewPager.setPageMargin(margin);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setOnTouchListener(new pagerTouchEvent());

    }

    private void initData() {

        mTodayCalendar = Calendar.getInstance();
        mCalendar = Calendar.getInstance();
        Intent Data = getIntent();
        long time = Data.getLongExtra(TIME_KEY, 0);
        mIsMySchedule = Data.getBooleanExtra(IS_MYSCHEDULE,true);
        mOtherUserId = Data.getStringExtra(OTHER_USER_ID);
        if(time > 0){
            mTodayCalendar.setTimeInMillis(time);
        }
        // 初始化今天的时间
        mOrgYear = mTodayCalendar.get(Calendar.YEAR);
        mOrgMonth = mTodayCalendar.get(Calendar.MONTH);
        mOrgDay = mTodayCalendar.get(Calendar.DAY_OF_MONTH);

        mNowSelectedPosition = DAYCOUNTS / 2;

        HeaderParam mHeaderParam = LauchrConst.getHeader(this);
        mUserName = mHeaderParam.getUserName();
        mUserCompany = mHeaderParam.getCompanyCode();
        mUserId = mHeaderParam.getLoginName();

        mStrUserName = LauchrConst.getHeader(this).getLoginName();

        mAdapter = new DayPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mNowSelectedPosition);
    }


    class DayPagerAdapter extends FragmentStatePagerAdapter {

        private DayFragment mFragment;

        public DayPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public int getCount() {
            return DAYCOUNTS;
        }

        @Override
        public Fragment getItem(int position) {
            mFragment = new DayFragment();
            mCalendar.set(mOrgYear,mOrgMonth,mOrgDay);
            mCalendar.set(Calendar.DAY_OF_MONTH, mOrgDay + (position - DAYCOUNTS/2));
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH) + 1;
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            String dayKey = ScheduleEventUtil.getAllTimeKey(mCalendar);
            List<EventEntity> entities = mEventDay.get(dayKey);
            mFragment.setDayData(year,month,day,entities);
            Log.i("time", year + " -  " + month + "-" + day );
            return mFragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        long now = System.currentTimeMillis();
        mStartTime = now - (long) 1000 * 3600 * 24 * 150;
        mEndTime = now + (long) 1000 * 3600 * 24 * 150;
        if(mIsMySchedule){
            showLoading();
            ScheduleApi.getInstance().getScheduleList(this, mStrUserName, mStartTime, mEndTime, null);
        }else {
            showLoading();
            ScheduleApi.getInstance().getScheduleList(this, mOtherUserId, mStartTime, mEndTime, null);
        }

    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        dismissLoading();
        if (response == null) {
            return;
        }
        if (taskId.equals(ScheduleApi.URL.GET_LIST)) {
            SchedulePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), SchedulePOJO.class);
            if (pojo != null && pojo.isResultSuccess()) {
                List<EventEntity> data = pojo.getBody().getResponse().getData();
                if (data != null) {
                    HashMap<String, List<EventEntity>> map = ScheduleEventUtil.sortDayEventList(data);
                    mEventDay.clear();
                    mEventDay.putAll(map);

//                    if(mAdapter == null){
                        mAdapter = new DayPagerAdapter(getSupportFragmentManager());
                        mViewPager.setAdapter(mAdapter);
                        mViewPager.setCurrentItem(mNowSelectedPosition);
//                    }else{
//                        mAdapter.
//                        mAdapter.notifyDataSetChanged();
//                        mViewPager.setCurrentItem(mNowSelectedPosition);
//                    }
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
            mNowSelectedPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class pagerTouchEvent implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) { //获取触摸事件消费
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //子视图未消费触摸事件，触摸事件在子视图外
        this.finish();
        return super.onTouchEvent(event);
    }
}
