package com.mintcode.launchr.app.newSchedule.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.ScheduleApi;
import com.mintcode.launchr.app.newSchedule.activity.ScheduleMainActivity;
import com.mintcode.launchr.app.newSchedule.adapter.VerticalAdapter;
import com.mintcode.launchr.app.newSchedule.adapter.VerticalViewPager;
import com.mintcode.launchr.app.newSchedule.util.ScheduleEventUtil;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.SchedulePOJO;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.pojo.entity.EventEntity;
import com.mintcode.launchr.util.TTJSONUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JulyYu on 2016/3/10.
 */
public class CalendarMonthFragment extends BaseFragment {


    /** 根view */
    private View mView;

    private VerticalViewPager mVerticalViewPager;
//
    private VerticalAdapter mVerticalAdapter;


    /** 当月*/
    private int mMonth;
    /** 开始时间*/
    private long mStartTime;
    /** 结束时间*/
    private long mEndTime;

    /** 名字*/
    private String mStrUserName;
    /** 用户名*/
    private String mUserName;
    /** 用户所在公司*/
    private String mUserCompany;
    /** 用户Id*/
    private String mUserId;

    /** 当前所在位置*/
    private int mNowPosition;

    // 切换用户之后的用户名
//    /** 其他用户的Id*/
//    public static String mOtherUserId;

    /** 其他用户姓名*/
//    private String mOtherUserName;

    /** 日程事件集合 */
    private HashMap<String, List<EventEntity>> mEventDay = new HashMap<String, List<EventEntity>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_calendar_month, null);
        Calendar calendar = Calendar.getInstance();
        mMonth = calendar.get(Calendar.MONTH) + 1;
        initView();
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initView(){

        HeaderParam mHeaderParam = LauchrConst.getHeader(getActivity());
        mUserName = mHeaderParam.getUserName();
        mUserCompany = mHeaderParam.getCompanyCode();
        mUserId = mHeaderParam.getLoginName();
        mStrUserName = LauchrConst.getHeader(getActivity()).getLoginName();

        mNowPosition = VerticalAdapter.YEAR_SIEZ * 6 + mMonth - 1;

        mVerticalViewPager = (VerticalViewPager)mView.findViewById(R.id.vertical_pager);
        PageChangeListener listener = new PageChangeListener();
        mVerticalViewPager.setOnPageChangeListener(listener);

    }


    /** 将页面翻到本月*/
    public void setPageInTodayMonth(){


        mNowPosition = VerticalAdapter.YEAR_SIEZ * 6 + mMonth - 1;
        mVerticalViewPager.setCurrentItem(VerticalAdapter.YEAR_SIEZ * 6 + mMonth - 1);

    }
    /** 获取列表*/
    public void getScheduleList() {

        long now = System.currentTimeMillis();
        mStartTime = now - (long) 1000 * 3600 * 24 * 150;
        mEndTime = now + (long) 1000 * 3600 * 24 * 150;
        if(ScheduleMainActivity.isMySchedule){
            showLoading();
            ScheduleApi.getInstance().getScheduleList(this, mStrUserName, mStartTime,mEndTime, null);
        }else if (!ScheduleMainActivity.isMySchedule){
            showLoading();
            ScheduleApi.getInstance().getScheduleList(this, ScheduleMainActivity.mOtherUserId, mStartTime,mEndTime, null);
        }
    }

    private void getMoreEvent(long time,boolean updown){

        if(updown){//往下加载更多
            mStartTime = time;
            mEndTime = time + (long) 1000 * 3600 * 24 * 150;
        }else{//往上加载更多
            mStartTime = time - (long) 1000 * 3600 * 24 * 150;
            mEndTime = time;
        }

        if(ScheduleMainActivity.isMySchedule){
            showLoading();
            ScheduleApi.getInstance().getScheduleList(this, mStrUserName, mStartTime,mEndTime, null);
        }else if (!ScheduleMainActivity.isMySchedule){
            showLoading();
            ScheduleApi.getInstance().getScheduleList(this, ScheduleMainActivity.mOtherUserId, mStartTime,mEndTime, null);
        }

    }
    public void setOtherUseerId(String id){
        long now = System.currentTimeMillis();
        mStartTime = now - (long) 1000 * 3600 * 24 * 150;
        mEndTime = now + (long) 1000 * 3600 * 24 * 150;
        showLoading();
        ScheduleApi.getInstance().getScheduleList(this,id, mStartTime,mEndTime, null);
    }


    @Override
    public void onResume() {
        super.onResume();
        getScheduleList();
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        dismissLoading();
        if(response == null){
            return;
        }
        if (taskId.equals(ScheduleApi.URL.GET_LIST) ) {
            SchedulePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), SchedulePOJO.class);
            if (pojo != null && pojo.isResultSuccess()) {
                List<EventEntity> data = pojo.getBody().getResponse().getData();
                if (data != null) {
                    HashMap<String, List<EventEntity>> map = ScheduleEventUtil.sortMonthEventList(data);
                    mEventDay.clear();
                    mEventDay.putAll(map);
//                    if(mVerticalAdapter == null){
                        mVerticalAdapter = new VerticalAdapter(getActivity());
                        mVerticalAdapter.setEventSets(mEventDay);
                        mVerticalViewPager.setAdapter(mVerticalAdapter);
                        mVerticalViewPager.setCurrentItem(mNowPosition);

//                    }else{
//                        mVerticalAdapter.setEventSets(mEventDay);
//                        mVerticalViewPager.setCurrentItem(mNowPosition);
//                    }

                }

            }

        }
    }

    class PageChangeListener implements VerticalViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
                int year = mVerticalAdapter.getYearByPosition(position);
                int month = mVerticalAdapter.getMonthByPosition(position);
                Activity activity =  getActivity();
                if(activity != null){
                    ((ScheduleMainActivity) getActivity()).getYearMonth(year, month);
                }
                mNowPosition = position;
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(mStartTime);
            int start = time.get(Calendar.YEAR) * 100 + time.get(Calendar.MONTH);
            time.setTimeInMillis(mEndTime);
            int end = time.get(Calendar.YEAR) * 100 + time.get(Calendar.MONTH);
            int pagetime = year * 100 + month;
            if(pagetime < start){ // 小于开始时间
                getMoreEvent(mStartTime,false);
            }else if(pagetime > end){ // 大于结束时间
                getMoreEvent(mEndTime,true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
