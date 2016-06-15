package com.mintcode.launchr.app.newSchedule.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mintcode.launchr.R;
import com.mintcode.launchr.app.meeting.activity.MeetingDetailActivity;
import com.mintcode.launchr.app.newSchedule.activity.ScheduleEventDetailActivity;
import com.mintcode.launchr.app.newSchedule.util.ScheduleUtil;
import com.mintcode.launchr.pojo.entity.EventEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by StephenHe on 2016/3/10.
 */
public class NewDayLayout extends LinearLayout{
    private View mContentView;

    private LayoutInflater mInflater;

    private Context mContext;

    private LinearLayout mLinContent;

    private List<EventEntity> mEventList;

    /** 向右的箭头*/
    private ImageView mIvRightArrow;

    public NewDayLayout(Context context, List<EventEntity> list) {
        super(context);

        mEventList = list;
        initView(context);
    }

    public NewDayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NewDayLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mContentView = mInflater.inflate(R.layout.listview_new_day, null);
        addView(mContentView);
        mLinContent = (LinearLayout) mContentView.findViewById(R.id.lin_day_content);
        mIvRightArrow = (ImageView) mContentView.findViewById(R.id.iv_right);

        if(mEventList.size() < 5){
            mIvRightArrow.setVisibility(GONE);
        }else{
            mIvRightArrow.setVisibility(VISIBLE);
        }

        for(int i=0; i<mEventList.size(); i++){
            final EventEntity entity = mEventList.get(i);
            View view = mInflater.inflate(R.layout.listview_new_day_item, null);
            LinearLayout mRelContent = (LinearLayout) view.findViewById(R.id.rel_one_event);
            ImageView mIvTop = (ImageView) view.findViewById(R.id.iv_line);
            TextView mTvTitle = (TextView) view.findViewById(R.id.tv_title);
            TextView mTvTime = (TextView) view.findViewById(R.id.tv_time);
            if(entity != null){
                if(entity.getIsVisible() == 0 && !entity.getType().equals("company_festival") && !entity.getType().equals("statutory_festival")){
                    mIvTop.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.icon_private_event_white));
                    mIvTop.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,40);
                    mIvTop.setLayoutParams(imageParams);
                }
//                else{
//                    mIvTop.setImageBitmap(null);
//                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,10);
//                    mIvTop.setLayoutParams(imageParams);
//                }
                if(entity.getType().equals("event")){
                    if(entity.getIsAllDay() == 1){
                        mRelContent.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_week_green));
                    }else{
                        mRelContent.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_week_nobackground_green));
                    }
                    mIvTop.setBackgroundColor(mContext.getResources().getColor(R.color.green_launchr));
                }else if(entity.getType().equals("meeting")){
                    if(entity.getIsAllDay() == 1){
                        mRelContent.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_week_blue));
                    }else{
                        mRelContent.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_week_nobackground_blue));
                    }
                    mIvTop.setBackgroundColor(mContext.getResources().getColor(R.color.blue_launchr));
                }else if(entity.getType().equals("company_festival")){
                    mRelContent.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_week_red));
                    mIvTop.setBackgroundColor(mContext.getResources().getColor(R.color.improtant_red));
                }else if(entity.getType().equals("statutory_festival")){
                    mRelContent.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_week_red));
                    mIvTop.setBackgroundColor(mContext.getResources().getColor(R.color.improtant_red));
                }else if(entity.getType().equals("event_sure")){
                    if(entity.getIsVisible() == 0){
                        mIvTop.setBackgroundColor(mContext.getResources().getColor(R.color.green_launchr));
                    }
                }
                if(entity.getIsImportant() == 1){
                    mIvTop.setBackgroundColor(mContext.getResources().getColor(R.color.week_red));
                }
                mTvTitle.setText(entity.getTitle());
                if(entity.getIsAllDay() == 1){
                    mTvTime.setText(mContext.getString(R.string.is_all_day));
                }else{
                    mTvTime.setText(getScheduleTime(entity.getStartTime(), entity.getEndTime()));
                }


            }

            mRelContent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(entity.isAllowSearch()){
                        if(entity.getType().equals("event") || entity.getType().equals("event_sure")){
                            Intent intent = new Intent(mContext, ScheduleEventDetailActivity.class);
                            intent.putExtra("event_showid", entity.getShowId());
                            mContext.startActivity(intent);
                        }else if(entity.getType().equals("meeting")) {
                            Intent intent = new Intent(mContext, MeetingDetailActivity.class);
                            intent.putExtra(MeetingDetailActivity.KEY_RELATIVE_ID, entity.getRelateId());
                            mContext.startActivity(intent);
                        }else if(entity.getType().equals("company_festival")){

                        }else if(entity.getType().equals("statutory_festival")){

                        }
                    }else{
                        Toast.makeText(mContext, mContext.getString(R.string.you_connt_see), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            mLinContent.addView(view);
        }
    }

    /** 设置显示时间*/
    private String getScheduleTime(long start, long stop){
        String timeStr = "";
        stop += 60000;      // 时间减去了一分钟，要加上
        long time = stop - start;

        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        if(!ScheduleUtil.isTimeInDayStart(start)){
            timeStr += sf.format(new Date(start)) + "(";
        }else{
            timeStr += sf.format(new Date(stop)) + "(";
        }

        long hour = time / (60*60*1000);
        long minute = (time - hour*60*60*1000) / (60*1000);
        if(hour != 0){
            timeStr += (hour + mContext.getString(R.string.hour));
        }
        if(minute != 0){
            timeStr += (minute + mContext.getString(R.string.timestamp_minutes));
        }
        return timeStr+")";
    }
}
