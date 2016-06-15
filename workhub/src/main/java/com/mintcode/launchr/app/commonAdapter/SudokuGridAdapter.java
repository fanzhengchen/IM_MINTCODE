package com.mintcode.launchr.app.commonAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mintcode.launchr.R;
import com.mintcode.launchr.app.newTask.TaskUtil;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.entity.AttachmentListEntity;
import com.mintcode.launchr.util.HeadImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/5/6.
 */
public class SudokuGridAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    /** 照片地址*/
    private List<AttachmentListEntity> mPicUrl = new ArrayList<>();
    private List<String> mSelectStrPicUrl = new ArrayList<>();
    public SudokuGridAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setStrPicList(List<String> str){
        mSelectStrPicUrl = str;
    }

    public  List<AttachmentListEntity> getPicUrl(){
        return mPicUrl;
    }



    @Override
    public int getCount() {

        if(mPicUrl.size() > 0){
            return mPicUrl.size();
        }else if(mSelectStrPicUrl.size() > 0){
            return mSelectStrPicUrl.size();
        }else{
            return 0;
        }
    }



    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_published_grida,parent, false);
            holder = new ViewHolder();
            holder.Image = (ImageView) convertView.findViewById(R.id.item_grida_image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(mPicUrl.size() > 0){
            String url = mPicUrl.get(position).getPath();
            String s = LauchrConst.ATTACH_PATH + url.substring(18);
            HeadImageUtil.getInstance(mContext).setImageResuces(holder.Image, s);
            return convertView;
        }else if(mSelectStrPicUrl.size() > 0){
            File file = new File(mSelectStrPicUrl.get(position));
            Glide.with(mContext).load(file).into(holder.Image);
            return convertView;
        }else{
            holder.Image.setVisibility(View.GONE);

        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView Image;
    }
}
