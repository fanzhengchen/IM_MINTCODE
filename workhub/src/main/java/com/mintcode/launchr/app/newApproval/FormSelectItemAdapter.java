package com.mintcode.launchr.app.newApproval;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.pojo.entity.FormCheckBoxEntity;
import com.mintcode.launchr.pojo.entity.formData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/4/13.
 */
public class FormSelectItemAdapter extends BaseAdapter {


    private List<FormCheckBoxEntity> mFormData;
    private List<FormCheckBoxEntity> mSelectedFormData = new ArrayList<>();

    private LayoutInflater mInflater;

    public FormSelectItemAdapter(Context context,List<FormCheckBoxEntity> data){
        this.mFormData = data;
        mInflater= LayoutInflater.from(context);
    }

    public void setSelectedData(List<FormCheckBoxEntity> data){
        mSelectedFormData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFormData == null ? 0:mFormData.size();
    }

    @Override
    public Object getItem(int position) {
        return mFormData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView =  mInflater.inflate(R.layout.item_form_box_select, parent,false);


        TextView text = (TextView)convertView.findViewById(R.id.tv_item_name);
        ImageView box = (ImageView)convertView.findViewById(R.id.cb_check);
        FormCheckBoxEntity data = mFormData.get(position);
        String value = data.getValue();
        text.setText(value);
        if(mSelectedFormData.contains(mFormData.get(position))){
            box.setImageResource(R.drawable.icon_blue_checked);
        }else{
            box.setImageResource(R.drawable.icon_unchecked);
        }
        return convertView;
    }


}
