package com.mintcode.launchr.app.newTask.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.pojo.entity.TaskProjectListEntity;

import java.util.List;

/**
 * Created by JulyYu on 2016/2/23.
 */
public class ProjectAdapter extends BaseAdapter {

    private Context mContext;


    private List<TaskProjectListEntity> mTaskProjectListEntity ;

    private int[] mDrawableLogo;

    private int mSelectedPosition = -1;

    private int mColorSelect ;
    private int mColorUnSelect;

    public ProjectAdapter(Context context, List<TaskProjectListEntity> taskProjectListEntity, int[] logo){
        mContext = context;
        mTaskProjectListEntity = taskProjectListEntity;
        mDrawableLogo = logo;
    }

    public ProjectAdapter(Context context){
        mContext = context;
        mColorSelect = mContext.getResources().getColor(R.color.task_menu_color_dark);
        mColorUnSelect = mContext.getResources().getColor(R.color.task_menu_color);
    }

    public void setTimeProjectData(List<TaskProjectListEntity> taskProjectListEntity,int[] logo){
        mTaskProjectListEntity = taskProjectListEntity;
        mDrawableLogo = logo;
        notifyDataSetChanged();
    }

    public void setData( List<TaskProjectListEntity> taskProjectListEntity){
        mTaskProjectListEntity = taskProjectListEntity;
        notifyDataSetChanged();
    }
    public void setSelectPosition(int position){
        mSelectedPosition = position;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mTaskProjectListEntity == null ? 0:mTaskProjectListEntity.size();
    }

    @Override
    public Object getItem(int position) {
        return mTaskProjectListEntity.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_task_project,null);
            viewHolder.mLlBg = (LinearLayout) convertView.findViewById(R.id.ll_item_bg);
            viewHolder.mIvLogo = (ImageView) convertView.findViewById(R.id.iv_item_task_logo);
            viewHolder.mTvName = (TextView) convertView.findViewById(R.id.tv_item_task_name);
            viewHolder.mTvNum = (TextView) convertView.findViewById(R.id.tv_item_task_num);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TaskProjectListEntity entity = mTaskProjectListEntity.get(position);
        viewHolder.mTvName.setText(entity.getName());
        viewHolder.mTvNum.setText((entity.getAllTask() - entity.getUnFinishedTask()) + "/" + entity.getAllTask());
        if(mDrawableLogo != null){
            Drawable logo = mContext.getResources().getDrawable(mDrawableLogo[position]);
            viewHolder.mIvLogo.setImageDrawable(logo);
            viewHolder.mTvNum.setText(entity.getAllTask() + "");
        }
        if(mSelectedPosition == position){
            viewHolder.mLlBg.setBackgroundColor(mColorSelect);
        }else{
            viewHolder.mLlBg.setBackgroundColor(mColorUnSelect);
        }
        return convertView;
    }
    class ViewHolder {
        ImageView mIvLogo;
        TextView mTvName;
        TextView mTvNum;
        LinearLayout mLlBg;
    }
}


