package com.mintcode.launchr.app.newTask.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.api.TaskApi;
import com.mintcode.launchr.app.newTask.TaskUtil;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.entity.TaskListEntity;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.util.TimeFormatUtil;
import com.mintcode.launchrnetwork.OnResponseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/2/23.
 */
public class TaskExpandAdapter extends BaseExpandableListAdapter {


    private Context mContext;

    private ExpandableListView mExpandLvTask;

    private ChangeTaskStateOnResponse mChangeTaskStateOnResponse = new ChangeTaskStateOnResponse();

    private List<TaskListEntity> mGroupList = new ArrayList<>();
    private List<List<TaskListEntity>> mChildList = new ArrayList<>();

    private int colorGray;
    private int colorBlack;
    private int colorRed;

    public TaskExpandAdapter(Context context, ExpandableListView expandLvTask){
        mContext = context;
        mExpandLvTask = expandLvTask;
        colorBlack = mContext.getResources().getColor(R.color.black);
        colorGray = mContext.getResources().getColor(R.color.gray);
        colorRed = mContext.getResources().getColor(R.color.red_launchr);
    }

    public TaskExpandAdapter(Context context, List<TaskListEntity> groupList, List<List<TaskListEntity>> childList, ExpandableListView expandLvTask){
        mContext = context;
        mGroupList = groupList;
        mChildList = childList;
        mExpandLvTask = expandLvTask;
    }

    public void setData(List<TaskListEntity> groupList,List<List<TaskListEntity>> childList){
        mGroupList = groupList;
        mChildList = childList;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mGroupList == null ? 0:mGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList == null? 0:mChildList.get(groupPosition).size();
//        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded,View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_task_expand, null);
//        LinearLayout llGroup = (LinearLayout) convertView.findViewById(R.id.ll_group);
        LinearLayout llChild = (LinearLayout) convertView.findViewById(R.id.ll_task_child);
        ImageView ivClock = (ImageView) convertView.findViewById(R.id.iv_clock);
        ImageView ivdirection = (ImageView) convertView.findViewById(R.id.iv_direction);
        ImageView ivColor = (ImageView) convertView.findViewById(R.id.iv_color);
        ImageView ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
        TextView tvSize = (TextView) convertView.findViewById(R.id.tv_size);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tv_time);
        TextView tvProjectName = (TextView) convertView.findViewById(R.id.tv_for_project);
        ImageView ivAttament = (ImageView) convertView.findViewById(R.id.iv_attament);
        ImageView ivSpeach = (ImageView) convertView.findViewById(R.id.iv_speach);
        CheckBox cbFinish = (CheckBox) convertView.findViewById(R.id.cb_group_finish);


        if(isExpanded == true){
            ivdirection.setImageResource(R.drawable.icon_down_direction);
        }else{
            ivdirection.setImageResource(R.drawable.icon_left_direction);
        }
        // 设置数据
        final TaskListEntity entity = mGroupList.get(groupPosition);
        tvTitle.setText(entity.getTitle());
        // 任务状态
        String type = entity.getType();
        if(type.equals("FINISH")){
            tvTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
            tvTitle.getPaint().setAntiAlias(true);//抗锯齿
            tvTitle.setTextColor(colorGray);
            cbFinish.setChecked(true);
        }else{
            tvTitle.getPaint().setFlags(0);
            tvTitle.getPaint().setAntiAlias(true);//抗锯齿
            tvTitle.setTextColor(colorBlack);
            cbFinish.setChecked(false);
        }
        // 是否有评论
        int hasComment = entity.getIsComment();
        if(hasComment == 1){
            ivSpeach.setVisibility(View.VISIBLE);
        }else{
            ivSpeach.setVisibility(View.GONE);
        }
        // 是否有附件
        int hasAttament = entity.getIsAnnex();
        if(hasAttament == 1){
            ivAttament.setVisibility(View.VISIBLE);
        }else{
            ivAttament.setVisibility(View.GONE);
        }
        // 设置箭头显示
        int all = entity.getAllTask();
        if (all < 1) {
            ivdirection.setVisibility(View.GONE);
            tvSize.setVisibility(View.GONE);
        }else{
            ivdirection.setVisibility(View.VISIBLE);
            tvSize.setText(entity.getFinishedTask() + "/" + entity.getAllTask());
        }
        // 所属项目
        tvProjectName.setText(entity.getProjectName());
        // 时间
        TimeFormatUtil.setTaskTime(mContext, tvTime, ivClock,entity.getStartTime(),entity.getEndTime(), entity.getIsEndTimeAllDay());
        // 设置颜色
        String userName = entity.getUserName();
        HeadImageUtil.getInstance(mContext).setAvatarResourceAppendUrl(ivHead,userName,2,60,60);

        llChild.setVisibility(View.GONE);

        // 设置优先级
        String priority = entity.getPriority();
        if (TaskUtil.HIGH_PRIORITY.equals(priority)){
            ivColor.setImageResource(R.color.task_priority_red);
        }else if (TaskUtil.MEDIUM_PRIORITY.equals(priority)){
            ivColor.setImageResource(R.color.task_priority_yellow);
        }else{
            ivColor.setImageResource(R.color.task_priority_grey);
        }



        ivdirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(isExpanded == true){
                    mExpandLvTask.collapseGroup(groupPosition);
                }else{
                    mExpandLvTask.expandGroup(groupPosition);
                }
            }
        });
        cbFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean finish = ((CheckBox)v).isChecked();
                if(finish){
                    TaskApi.getInstance().changeTaskState(mChangeTaskStateOnResponse,entity.getShowId(),"FINISH");
                    entity.setType("FINISH");
                }else{
                    TaskApi.getInstance().changeTaskState(mChangeTaskStateOnResponse,entity.getShowId(),"WAITING");
                    entity.setType("WAITING");
                }

            }
        });
        convertView.setTag(R.id.group_id,groupPosition);
        convertView.setTag(R.id.child_id,-1);
        convertView.setTag(R.id.iv_direction,groupPosition);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_task_expand, null);
        LinearLayout llGroup = (LinearLayout) convertView.findViewById(R.id.ll_group);
//        LinearLayout llChild = (LinearLayout) convertView.findViewById(R.id.ll_task_child);
        ImageView ivColor = (ImageView) convertView.findViewById(R.id.iv_child_color);
        ImageView ivHead = (ImageView) convertView.findViewById(R.id.iv_child_head);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_child_title);
        CheckBox cbFinish = (CheckBox) convertView.findViewById(R.id.cb_child_finish);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tv_child_time);
        ImageView ivClock = (ImageView) convertView.findViewById(R.id.iv_child_clock);
        ImageView ivSpeach = (ImageView)convertView.findViewById(R.id.iv_child_speach);
        ImageView ivAttament = (ImageView)convertView.findViewById(R.id.iv_child_speach);
        llGroup.setVisibility(View.GONE);

        // 设置数据
        final TaskListEntity entity = mChildList.get(groupPosition).get(childPosition);
        tvTitle.setText(entity.getTitle());
        // 任务状态
        String type = entity.getType();
        if(type.equals("FINISH")){
            tvTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
            tvTitle.getPaint().setAntiAlias(true);//抗锯齿
            tvTitle.setTextColor(colorGray);
            cbFinish.setChecked(true);
        }else{
            tvTitle.getPaint().setFlags(0);
            tvTitle.getPaint().setAntiAlias(true);//抗锯齿
            tvTitle.setTextColor(colorBlack);
            cbFinish.setChecked(false);
        }
        // 时间
        TimeFormatUtil.setTaskTime(mContext,tvTime,ivClock,entity.getStartTime(),entity.getEndTime(),entity.getIsEndTimeAllDay());
        // 设置颜色

        HeadImageUtil.getInstance(mContext).setAvatarResourceAppendUrl(ivHead,entity.getUserName(), 2,60,60);
        // 设置优先级
        String priority = entity.getPriority();
        if (TaskUtil.HIGH_PRIORITY.equals(priority)){
            ivColor.setImageResource(R.color.task_priority_red);
        }else if (TaskUtil.MEDIUM_PRIORITY.equals(priority)){
            ivColor.setImageResource(R.color.task_priority_yellow);
        }else{
            ivColor.setImageResource(R.color.task_priority_grey);
        }
        // 是否有评论
        int hasComment = entity.getIsComment();
        if(hasComment == 1){
            ivSpeach.setVisibility(View.VISIBLE);
        }else{
            ivSpeach.setVisibility(View.GONE);
        }
        // 是否有附件
        int hasAttament = entity.getIsAnnex();
        if(hasAttament == 1){
            ivAttament.setVisibility(View.VISIBLE);
        }else{
            ivAttament.setVisibility(View.GONE);
        }

        cbFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean finish = ((CheckBox)v).isChecked();
                if(finish){
                    TaskApi.getInstance().changeTaskState(mChangeTaskStateOnResponse,entity.getShowId(),"FINISH");
                    entity.setType("FINISH");
                }else{
                    TaskApi.getInstance().changeTaskState(mChangeTaskStateOnResponse,entity.getShowId(),"WAITING");
                    entity.setType("WAITING");
                }

            }
        });


        convertView.setTag(R.id.group_id,groupPosition);
        convertView.setTag(R.id.child_id,childPosition);

        return convertView;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }



    private String getNumFormat(int i){
        String str = "0";
        if (i < 10) {
            return str + i;
        }
        return i + "";
    }

//    private void setImageResuces(ImageView iv, String url){
//        // 处理圆形图片
////        RequestManager man = Glide.with(mContext);
////        GlideRoundTransform g =  new GlideRoundTransform(mContext,3);
////        man.load(url).transform(g).into(iv);
//        HeadImageUtil.getInstance(mContext).setImageResuces(iv,);
//    }

    class ChangeTaskStateOnResponse implements OnResponseListener{

        @Override
        public void onResponse(Object response, String taskId, boolean rawData) {
            notifyDataSetChanged();
        }

        @Override
        public boolean isDisable() {
            return false;
        }
    }
}
