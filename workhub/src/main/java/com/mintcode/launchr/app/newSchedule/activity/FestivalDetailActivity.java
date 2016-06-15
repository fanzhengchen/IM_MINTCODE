package com.mintcode.launchr.app.newSchedule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.pojo.entity.EventEntity;
import com.mintcode.launchr.util.TimeFormatUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JulyYu on 2016/5/4.
 */
public class FestivalDetailActivity extends BaseActivity {

    @Bind(R.id.tv_festival_name)
    TextView mTvName;
    @Bind(R.id.tv_time)
    TextView mTvTime;

    public static final String GET_DETAIL = "get_detail";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival_deatil);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Intent detail = getIntent();
        EventEntity entity = (EventEntity)detail.getSerializableExtra(GET_DETAIL);
        if(entity != null){
            String name = entity.getTitle();
            long startTime = entity.getStartTime();
            long endTime = entity.getEndTime();
            int allDay = entity.getIsAllDay();
            boolean boolAllDay;
            if(allDay == 1){
                boolAllDay = true;
            }else{
                boolAllDay = false;
            }
            mTvName.setText(name);
            mTvTime.setText(getString(R.string.calendar_schedule_time) + "  " + TimeFormatUtil.setTime(startTime,endTime,boolAllDay));
        }
    }

    @OnClick(R.id.iv_back)
    void back(){
        this.finish();
    }
}
