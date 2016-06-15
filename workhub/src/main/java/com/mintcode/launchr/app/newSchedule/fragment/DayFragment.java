package com.mintcode.launchr.app.newSchedule.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.ScheduleApi;
import com.mintcode.launchr.app.meeting.activity.MeetingDetailActivity;
import com.mintcode.launchr.app.meeting.activity.NewMeetingActivity;
import com.mintcode.launchr.app.newSchedule.activity.AddScheduleEventActivity;
import com.mintcode.launchr.app.newSchedule.activity.FestivalDetailActivity;
import com.mintcode.launchr.app.newSchedule.activity.ScheduleEventDetailActivity;
import com.mintcode.launchr.base.BaseFragment;
import com.mintcode.launchr.pojo.ScheduleDetailPOJO;
import com.mintcode.launchr.pojo.entity.EventEntity;
import com.mintcode.launchr.pojo.entity.ScheduleEntity;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.util.TimeFormatUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by JulyYu on 2016/3/14.
 */
public class DayFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    private View mView;

    private TextView mTvTime;
    private ListView mListView;
    private EventAdapter mAdapter;

    private TextView mTvNewEvent;
    private TextView mTvNewMeeting;

    private List<EventEntity> mEventList = new ArrayList<>();

    private int mYear;
    private int mMonth;
    private int mDay;

    private String mStrCantSee ;

    public final static String TODAY_TIME = "today_time";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_schedule_day,null);
        initView();
        return mView;
    }

    private void initView(){

        mStrCantSee  = getActivity().getString(R.string.you_connt_see);

        mTvTime = (TextView)mView.findViewById(R.id.tv_day_time);
        mListView = (ListView)mView.findViewById(R.id.lv_day_event);
        mTvNewEvent = (TextView)mView.findViewById(R.id.tv_new_event);
        mTvNewMeeting = (TextView)mView.findViewById(R.id.tv_new_meeting);

        mAdapter = new EventAdapter(getActivity());
        if(mEventList != null){
            mAdapter.setData(mEventList);
        }
        mListView.setAdapter(mAdapter);
        String year = getResources().getString(R.string.caleandar_year);
        String month = getResources().getString(R.string.caleandar_month);
        String day = getResources().getString(R.string.calendar_sunday);
        Calendar time = Calendar.getInstance();
        time.set(mYear,mMonth - 1,mDay);
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy" + year + "MM" + month + "dd" + day + "EE");
        mTvTime.setText(dayTime.format(time.getTimeInMillis()));

        mTvNewEvent.setOnClickListener(this);
        mTvNewMeeting.setOnClickListener(this);

        mListView.setOnItemClickListener(this);
    }

    public void setDayData(int year ,int month,int day,List<EventEntity> entity){

        mYear = year;
        mMonth = month;
        mDay = day;
        mEventList = entity;
        if(mAdapter != null){
            mAdapter.setData(mEventList);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mTvNewEvent){
            Calendar time = Calendar.getInstance();
            time.set(mYear,mMonth - 1,mDay);
            Intent newEvent = new Intent(getActivity(),AddScheduleEventActivity.class);
            newEvent.putExtra(TODAY_TIME,time.getTimeInMillis());
            startActivity(newEvent);
        }else if(v == mTvNewMeeting){
            Calendar time = Calendar.getInstance();
            time.set(mYear,mMonth - 1,mDay);
            Intent newMeeting = new Intent(getActivity(), NewMeetingActivity.class);
            newMeeting.putExtra(TODAY_TIME,time.getTimeInMillis());
            startActivity(newMeeting);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent == mListView){
            EventEntity entity = (EventEntity)mAdapter.getItem(position);
            String type = entity.getType();
            if(entity.isAllowSearch()){
                if(type.equals("meeting")){
                    Intent ToEventDetail = new Intent(getActivity(),MeetingDetailActivity.class);
                    ToEventDetail.putExtra(MeetingDetailActivity.KEY_RELATIVE_ID, entity.getRelateId());
                    ToEventDetail.putExtra(MeetingDetailActivity.KEY_FACT_START_TIME, entity.getStartTime());
                    startActivity(ToEventDetail);
                }else if(type.equals("event") || type.equals("event_sure")){
                    Intent ToEventDetail = new Intent(getActivity(),ScheduleEventDetailActivity.class);
                    ToEventDetail.putExtra("event_showid", entity.getShowId());
                    startActivity(ToEventDetail);

                }else if(type.equals("statutory_festival") || type.equals("company_festival")){
                    Intent ToEventDetail = new Intent(getActivity(),FestivalDetailActivity.class);
                    ToEventDetail.putExtra(FestivalDetailActivity.GET_DETAIL,entity);
                    startActivity(ToEventDetail);
                }
            }else{
                toast(mStrCantSee);
            }
        }
    }

    class EventAdapter extends BaseAdapter{

        private Context mContext;

        private List<EventEntity> mData;

        private int COLOR_RED_FESTIVAL;
        private int COLOR_BULE_MEETING;
        private int COLOR_GREEN_EVENT;
        private Drawable mDrawableEvent;

        private SimpleDateFormat allDayFormat;
        private SimpleDateFormat dayFormat;
        private SimpleDateFormat oneDayFormat;

        private String  mStrHour;
        private String mStrAllDay;

        private Calendar mTime = Calendar.getInstance();

        public EventAdapter(Context context){
            mContext = context;
            COLOR_RED_FESTIVAL = mContext.getResources().getColor(R.color.red_launchr);
            COLOR_BULE_MEETING = mContext.getResources().getColor(R.color.blue_launchr);
            COLOR_GREEN_EVENT = mContext.getResources().getColor(R.color.green_launchr);
            allDayFormat = new SimpleDateFormat("MM/dd");
            dayFormat = new SimpleDateFormat("MM/dd HH:mm");
            oneDayFormat = new SimpleDateFormat("HH:mm");
            mStrHour = mContext.getResources().getString(R.string.hour);
            mStrAllDay = getResources().getString(R.string.apply_allday);
        }


        public void setData( List<EventEntity> data){
            mData = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData == null ?0:mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData == null ? 0:mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HolderView holderView;
            if(convertView == null){
                holderView = new HolderView();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_schedule_oneday,null);
                holderView.ivType = (ImageView)convertView.findViewById(R.id.iv_item_type);
                holderView.tvTitle = (TextView)convertView.findViewById(R.id.tv_item_title);
                holderView.tvTime = (TextView)convertView.findViewById(R.id.tv_item_time);
                holderView.tvAllTime = (TextView)convertView.findViewById(R.id.tv_item_time_all);
                holderView.tvPlace = (TextView)convertView.findViewById(R.id.tv_item_place);
                holderView.llTime = (LinearLayout)convertView.findViewById(R.id.ll_item_time);
                holderView.llPlace = (LinearLayout)convertView.findViewById(R.id.ll_item_place);
                holderView.ivPlace = (ImageView)convertView.findViewById(R.id.iv_item_place);
                convertView.setTag(holderView);
            }else{
                holderView = (HolderView)convertView.getTag();
            }
            EventEntity entity = mData.get(position);
            if(entity != null){
                String type = entity.getType();
                long startTime = entity.getStartTime();
                long endTime = entity.getEndTime() - 1000;
                int allDay = entity.getIsAllDay();
                String palce = entity.getPlace();
                String y = entity.getLaty();
                String x = entity.getLngx();
                holderView.tvTitle.setText(entity.getTitle());
                if(entity.getIsVisible() == 0){
                    holderView.ivType.setImageResource(R.drawable.icon_private_event_white);
                    holderView.ivType.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(40, ViewGroup.LayoutParams.MATCH_PARENT);
                    holderView.ivType.setLayoutParams(imageParams);
                }
                if (type.equals("meeting")) { // 会议事件 蓝
                    holderView.ivType.setBackgroundColor(COLOR_BULE_MEETING);
                } else if (type.equals("event")) {// 事件 绿色
                    holderView.ivType.setBackgroundColor(COLOR_GREEN_EVENT);
                } else  if(type.equals("event_sure")){ // 未选择事件 虚线
                    if(entity.getIsVisible() == 0){
                        holderView.ivType.setBackgroundResource(R.drawable.bg_unselect_event_all);
                    }else{
                        holderView.ivType.setBackgroundResource(R.drawable.bg_unselect_event);
                    }
                } else if (type.equals("statutory_festival") || type.equals("company_festival")) {// 假期 红色
                    holderView.ivType.setBackgroundColor(COLOR_RED_FESTIVAL);
                }
                if(palce != null && !"".equals(palce)){ // 地点
                    holderView.llPlace.setVisibility(View.VISIBLE);
                    holderView.tvPlace.setText(palce);
                }else{
                    holderView.llPlace.setVisibility(View.GONE);
                }
                // 时间显示
                DayTimeDeal(mTime,holderView,startTime,endTime,allDay);

            }

            return convertView;
        }
        private void DayTimeDeal(Calendar time,HolderView holderView ,long startTime,long endTime,int isAllDay){

            time.setTimeInMillis(startTime);
            int startYear = time.get(Calendar.YEAR);
            int startMonth = time.get(Calendar.MONTH) + 1;
            int startDay = time.get(Calendar.DAY_OF_MONTH);
            time.setTimeInMillis(endTime);
            int endYear = time.get(Calendar.YEAR);
            int endMonth  = time.get(Calendar.MONTH) + 1;
            int endDay = time.get(Calendar.DAY_OF_MONTH);
            if(startYear == endYear && startMonth == endMonth && startDay == endDay){// 开始时间和结束时间是否为同一天
                if(startYear == mYear && startMonth == mMonth && startDay == mDay){// 时间为当天显示一个时间
                    if(isAllDay == 1){
                        holderView.tvTime.setText(allDayFormat.format(startTime));
                    }else{
                        holderView.tvTime.setText(oneDayFormat.format(startTime) + "~" + oneDayFormat.format(endTime));
                    }
                }else{
                    if(isAllDay == 1){
                        holderView.tvTime.setText(allDayFormat.format(startTime));
                    }else {
                        holderView.tvTime.setText(dayFormat.format(startTime) + "~" + oneDayFormat.format(endTime));
                    }
                }
            }else{
                if(isAllDay == 1){
                    holderView.tvTime.setText(allDayFormat.format(startTime) + "~" + allDayFormat.format(endTime));
                }else {
                    holderView.tvTime.setText(dayFormat.format(startTime) + "~" + dayFormat.format(endTime));
                }
            }
            holderView.tvAllTime.setText(TimeFormatUtil.setTimeInterval(mContext,endTime,startTime));
        }
    }

    class HolderView{
        ImageView ivType;
        TextView tvTitle;
        TextView tvTime;
        TextView tvAllTime;
        TextView tvPlace;
        LinearLayout llTime;
        LinearLayout llPlace;
        ImageView ivPlace;
    }



    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {
        super.onResponse(response, taskId, rawData);
        if (response == null) {
            return;
        }
        if (taskId.equals(ScheduleApi.URL.GET_SCHEDULE_DETAIL)) {
            ScheduleDetailPOJO pojo = TTJSONUtil.convertJsonToCommonObj(response.toString(), ScheduleDetailPOJO.class);
            if (pojo != null && pojo.isResultSuccess()) {
                ScheduleEntity data = pojo.getBody().getResponse().getDate();
                Intent ToEventDetail = new Intent(getActivity(), ScheduleEventDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ScheduleEventDetailActivity.DETAIL_CODE, data);
                ToEventDetail.putExtras(bundle);
                startActivity(ToEventDetail);
                dismissLoading();
            }
        }
    }
}
