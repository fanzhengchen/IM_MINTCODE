package com.mintcode.launchr.app.newSchedule.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.ScheduleApi;
import com.mintcode.launchr.app.newSchedule.activity.ScheduleMainActivity;
import com.mintcode.launchr.app.newSchedule.util.ScheduleUtil;
import com.mintcode.launchr.app.newSchedule.view.NewDayLayout;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.pojo.SchedulePOJO;
import com.mintcode.launchr.pojo.entity.EventEntity;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.TTJSONUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by StephenHe on 2016/3/10.
 */
public class NewWeekFragment extends BaseFragment implements AbsListView.OnScrollListener{
    private View mContentView;

    private LayoutInflater mInflater;

    private Context mContext;

    /** 日程列表*/
    private ListView mLvWeek;
    private WeekAdapter mWeekAdapter;
    private LinkedHashMap<Long, List<EventEntity>> mEventData;
    private  List<EventEntity> mListData;
    private List<Long> mEventTime;

    /** 原始为0，上滑为1，下滑为2*/
    private int slideState = 0;
    /** 屏幕是否在滚动*/
    private int scrollState = -1;

    /** 开始时间，结束时间*/
    private Calendar startCalendar;
    private Calendar stopCalendar;

    /** 是否正在加载数据，此时应该禁止第二次加载数据*/
    private boolean isGetData = false;

    /** 当前位置*/
    private int mCurrentPlace = 0;

    /** 是否新建任务了*/
    public static int newEventResult = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_new_week, null);

        initDate();
        initView();
        return mContentView;
    }

    /** 初始化时间*/
    private void initDate(){
        startCalendar = Calendar.getInstance();
        stopCalendar = Calendar.getInstance();
        startCalendar.set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)-15, 0, 0, 0);
        stopCalendar.set(stopCalendar.get(Calendar.YEAR), stopCalendar.get(Calendar.MONTH), stopCalendar.get(Calendar.DAY_OF_MONTH)+15, 0, 0, 0);
    }

    private void initView(){
        mContext = getActivity();
        mInflater = LayoutInflater.from(mContext);

        mLvWeek = (ListView) mContentView.findViewById(R.id.lv_week);

        mListData = new ArrayList<EventEntity>();
        mEventTime = new ArrayList<Long>();
        mEventData = initEventData();
        mWeekAdapter = new WeekAdapter();
        mLvWeek.setAdapter(mWeekAdapter);
        mLvWeek.setOnScrollListener(this);
        mWeekAdapter.notifyDataSetChanged();

        //  获取日程
        getScheduleWeekList(startCalendar.getTimeInMillis(), stopCalendar.getTimeInMillis());
    }

    private class WeekAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mEventData.size();
        }

        @Override
        public Object getItem(int position) {
            return mEventData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodler holder = null;
//            if(convertView == null){
                holder = new ViewHodler();
                convertView = mInflater.inflate(R.layout.listview_new_week, null);
                holder.tvTime = (TextView)convertView.findViewById(R.id.tv_time);
                holder.tvWeek = (TextView)convertView.findViewById(R.id.tv_week);
                holder.linContent = (LinearLayout) convertView.findViewById(R.id.lin_content);
//                convertView.setTag(holder);
//            }else{
//                holder = (ViewHodler)convertView.getTag();
//            }
            if(isTodayByTime(mEventTime.get(position))){
                holder.tvTime.setTextColor(getResources().getColor(R.color.white));
                holder.tvTime.setBackgroundColor(getResources().getColor(R.color.light_blue));
            }else{
                holder.tvTime.setTextColor(getResources().getColor(R.color.black));
                holder.tvTime.setBackgroundColor(getResources().getColor(R.color.white));
            }
            holder.tvTime.setText("" + getDayByTime(mEventTime.get(position)));
            holder.tvWeek.setText(getWeekByTime(mEventTime.get(position)) + "");
            List<EventEntity> mData = mEventData.get(mEventTime.get(position));
            if(mData!=null && mData.size()>0 && holder.linContent.getChildCount()<1){
                NewDayLayout dayFragment = new NewDayLayout(mContext, mData);
                holder.linContent.addView(dayFragment);
            }
            return convertView;
        }
    }

    private class ViewHodler{
        TextView tvTime;
        TextView tvWeek;
        LinearLayout linContent;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(scrollState == 1 || scrollState==0 ||scrollState==2){
            setTodayState(firstVisibleItem, visibleItemCount);

            if(firstVisibleItem < 2 && !isGetData){
                slideState = 1;
                isGetData = true;
                Calendar stop = Calendar.getInstance();
                stop.setTimeInMillis(startCalendar.getTimeInMillis());
                startCalendar.set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH) - 30, 0, 0, 0);
                getScheduleWeekList(startCalendar.getTimeInMillis(), stopCalendar.getTimeInMillis());
            }

            if(totalItemCount-firstVisibleItem-visibleItemCount < 2 && !isGetData){
                slideState = 2;
                isGetData = true;
                mCurrentPlace = mLvWeek.getFirstVisiblePosition();
                Calendar start = Calendar.getInstance();
                start.setTimeInMillis(stopCalendar.getTimeInMillis());
                stopCalendar.set(stopCalendar.get(Calendar.YEAR), stopCalendar.get(Calendar.MONTH), stopCalendar.get(Calendar.DAY_OF_MONTH) + 30, 0, 0, 0);
                getScheduleWeekList(startCalendar.getTimeInMillis(), stopCalendar.getTimeInMillis());
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }

    /** 首次进来实例化30天的数据*/
    private LinkedHashMap<Long, List<EventEntity>> initEventData(){
        LinkedHashMap<Long, List<EventEntity>> event = new LinkedHashMap<Long, List<EventEntity>>();
        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH) - 15, 0, 0, 0);
        for(int i=0; i<30; i++){
            List<EventEntity> entity = new ArrayList<EventEntity>();
            event.put(c.getTimeInMillis(), entity);
            mEventTime.add(c.getTimeInMillis());
            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)+1, 0, 0, 0);
        }
        return event;
    }

    /** 再添加30天*/
    private void addTopTime(){
        Calendar c = Calendar.getInstance();
        c.set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        int size = mEventData.size();
        mEventData = new LinkedHashMap<Long, List<EventEntity>>();
        mEventTime = new ArrayList<Long>();
        for(int i=0; i<size+30; i++){
            List<EventEntity> entity = new ArrayList<EventEntity>();
            mEventData.put(c.getTimeInMillis(), entity);
            mEventTime.add(c.getTimeInMillis());
            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)+1, 0, 0, 0);
        }
    }

    /** 删除，添加事件，刷新列表*/
    private void refershData(){
        int size = mEventData.size();
        mEventData = new LinkedHashMap<Long, List<EventEntity>>();
        mEventTime = new ArrayList<Long>();
        Calendar c = Calendar.getInstance();
        c.set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        for(int i=0; i<size; i++){
            List<EventEntity> entity = new ArrayList<EventEntity>();
            mEventData.put(c.getTimeInMillis(), entity);
            mEventTime.add(c.getTimeInMillis());
            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)+1, 0, 0, 0);
        }
    }

    /** 日期是那一天*/
    private int getDayByTime(long time){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /** 日期是周几*/
    private String getWeekByTime(long time){
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        return  weekDays[c.get(Calendar.DAY_OF_WEEK)-1];
    }

    /** 判断是否是今天*/
    private boolean isTodayByTime(long time){
        Calendar c = Calendar.getInstance();
        Calendar b = Calendar.getInstance();
        b.setTimeInMillis(time);
        long timeAttack = Math.abs(c.getTimeInMillis() - time);
        if(timeAttack < 86400000 && (b.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH))){
            return true;
        }else{
            return false;
        }
    }

    /** 设置今天按钮的状态*/
    private void setTodayState(int firstItem, int visibleItem){
        if(firstItem+visibleItem >= mEventTime.size()){
            return;
        }

        long fristTime = mEventTime.get(firstItem);
        long endTime = mEventTime.get(firstItem + visibleItem - 1);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(endTime);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        ((ScheduleMainActivity)getActivity()).getYearWeek(year, month, day, fristTime, endTime);
//        for(int i=firstItem; i<firstItem+visibleItem; i++){
//            Calendar c = Calendar.getInstance();
//            c.setTimeInMillis(mEventTime.get(i));
//            boolean result = ((ScheduleMainActivity)getActivity()).getYearWeek(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
//            if(result){
//                break;
//            }
//        }
    }

    /** 设置日期为今天*/
    public void setTimeInTodayWeek(){
        for(int i=0; i<mEventTime.size(); i++){
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(mEventTime.get(i));
            Calendar b = Calendar.getInstance();
            if((c.get(Calendar.YEAR)==b.get(Calendar.YEAR)) && (c.get(Calendar.MONTH)==b.get(Calendar.MONTH)) && (c.get(Calendar.DAY_OF_MONTH)==b.get(Calendar.DAY_OF_MONTH))){
                if(c.get(Calendar.DAY_OF_WEEK)-1 == 0){
                    mLvWeek.setSelection(i-6);
                }else  if(c.get(Calendar.DAY_OF_WEEK)-1 == 1){
                    mLvWeek.setSelection(i);
                }else  if(c.get(Calendar.DAY_OF_WEEK)-1 == 2){
                    mLvWeek.setSelection(i-1);
                }else  if(c.get(Calendar.DAY_OF_WEEK)-1 == 3){
                    mLvWeek.setSelection(i-2);
                }else  if(c.get(Calendar.DAY_OF_WEEK)-1 == 4){
                    mLvWeek.setSelection(i-3);
                }else  if(c.get(Calendar.DAY_OF_WEEK)-1 == 5){
                    mLvWeek.setSelection(i-4);
                }else  if(c.get(Calendar.DAY_OF_WEEK)-1 == 6){
                    mLvWeek.setSelection(i-5);
                }
                break;
            }
        }
    }

    /** 返回我的日程*/
    public void setBackMySchedule(){
        slideState = 0;
        initDate();
        mListData = new ArrayList<EventEntity>();
        mEventTime = new ArrayList<Long>();
        mEventData = initEventData();
        showLoading();
        getScheduleWeekList(startCalendar.getTimeInMillis(), stopCalendar.getTimeInMillis());
    }

    /** 获取日程*/
    private void getScheduleWeekList(long start, long stop){
        showLoading();
        if(ScheduleMainActivity.isMySchedule){
            String loginName = AppUtil.getInstance(mContext).getLoginName();
            ScheduleApi.getInstance().getScheduleList(this, loginName, start, stop, null);
        }else{
            ScheduleApi.getInstance().getScheduleList(this, ScheduleMainActivity.mOtherUserId, start, stop, null);
        }
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        if(response == null){
            return;
        }

        if(taskId.equals(ScheduleApi.URL.GET_LIST)){
            HandleDataAsyncTask handleDataAsyncTask = new HandleDataAsyncTask(response);
            handleDataAsyncTask.execute();
        }
    }

    /** 获取日程*/
    private void handleScheduleList(Object response){
        SchedulePOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), SchedulePOJO.class);
        if(pojo == null){
            return;
        }else if(!pojo.getBody().getResponse().isIsSuccess()){
            toast(pojo.getBody().getResponse().getReason());
        }else{
            if(slideState == 0){
                mListData = pojo.getBody().getResponse().getData();
            }else if(slideState == 1){
                mListData = pojo.getBody().getResponse().getData();
                addTopTime();
            }else if(slideState == 2){
                mListData = pojo.getBody().getResponse().getData();
                addTopTime();
            }else if(slideState == 3){
                mListData = pojo.getBody().getResponse().getData();
                refershData();
            }

            mEventData = ScheduleUtil.getEventDataList(mEventData, mListData);
        }
    }

    // 开一个子线程来处理数据
    public class HandleDataAsyncTask extends AsyncTask<Object, Object, Object>{
        private Object response;

        public HandleDataAsyncTask(Object object){
            response = object;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            mWeekAdapter.notifyDataSetChanged();
            if(slideState == 1){
                mLvWeek.setSelection(30);
            }else if(slideState == 2){
                mLvWeek.setSelection(mCurrentPlace);
            }else if(slideState == 0){
                mLvWeek.setSelection(14);
                int lastItem = mLvWeek.getLastVisiblePosition();
                int fristItem = mLvWeek.getFirstVisiblePosition();
                setTodayState(14,lastItem - fristItem);
                setTimeInTodayWeek();
            }else if(slideState == 3){
                mLvWeek.setSelection(mCurrentPlace);
            }
            //  数据加载完毕
            isGetData = false;
            dismissLoading();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            handleScheduleList(response);
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(newEventResult == 1){
            newEventResult = 0;
            slideState = 3;
            isGetData = true;
            mCurrentPlace = mLvWeek.getFirstVisiblePosition();
            getScheduleWeekList(startCalendar.getTimeInMillis(), stopCalendar.getTimeInMillis());
        }
    }

}
