package com.mintcode.launchr.app.newApproval.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.photo.activity.GalleryActivity;
import com.mintcode.launchr.util.HeadImageUtil;
import com.mintcode.launchr.view.NoScrollTouchGridView;
import com.mintcode.launchr.app.newTask.TaskUtil;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.entity.AttachmentListEntity;
import com.mintcode.launchr.pojo.entity.formData;
import com.mintcode.launchr.util.TTDensityUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/4/18.
 */
public class FileShowLayoutView extends BaseLinearLayoutView implements AdapterView.OnItemClickListener{

    private TextView mTvTitle;
    private NoScrollTouchGridView mGdImage;
    /**  照片适配器 */
    private GridAdapter mGridAdapter;
    private String mStrTitle;
    private  List<AttachmentListEntity> mAttachmentListEntities;

    public FileShowLayoutView(Context context, formData data) {
        super(context, data);
        mStrTitle = mFormData.getLabelText();
        initView();
    }

    public FileShowLayoutView(Context context, formData data, String key) {
        super(context, data, key);
    }

    public FileShowLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FileShowLayoutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    protected void initView() {
        int with = TTDensityUtil.dip2px(mContext,70);
        LinearLayout.LayoutParams titleLayout = new LinearLayout.LayoutParams(with, LinearLayout.LayoutParams.WRAP_CONTENT);
        mTvTitle = new TextView(mContext);
        mTvTitle.setTextSize(15);
        mTvTitle.setTextColor(mContext.getResources().getColor(R.color.meeting_gray));
        mTvTitle.setText(mStrTitle);
        mTvTitle.setLayoutParams(titleLayout);
        this.addView(mTvTitle);

        LinearLayout.LayoutParams gridViewLayout = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1);
        mGdImage = new NoScrollTouchGridView(mContext);
        mGdImage.setPadding(15, 15, 15, 15);
        mGdImage.setVerticalSpacing(25);
        mGdImage.setNumColumns(3);
        mGdImage.setLayoutParams(gridViewLayout);
        mGridAdapter = new GridAdapter(mContext);
        mGdImage.setAdapter(mGridAdapter);
        mGdImage.setOnItemClickListener(this);
        this.addView(mGdImage);
    }


    public void setImageData( List<AttachmentListEntity> images){
        if(images != null && images.size() > 0){
            mGridAdapter.setImageData(images);
            this.setVisibility(View.VISIBLE);
        }else{
            this.setVisibility(View.GONE);
        }
    }

    /** 照片显示适配器*/
    public class GridAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        /** 照片地址*/
        private List<AttachmentListEntity> mPicUrl = new ArrayList<>();

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public  List<AttachmentListEntity> getPicUrl(){
            return mPicUrl;
        }

        public void setImageData( List<AttachmentListEntity> images){
            mPicUrl = images;
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {

            if(mPicUrl != null && mPicUrl.size() > 0){
                return mPicUrl.size();
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
                if (LauchrConst.DEV_CODE == 2) {
                    String s = LauchrConst.ATTACH_PATH + url.substring(18);
                    HeadImageUtil.getInstance(mContext).setImageResuces(holder.Image, s);
                }else {
                    String s = url.replace("http://a.mintcode.comcom", LauchrConst.ATTACH_PATH);
                    HeadImageUtil.getInstance(mContext).setImageResuces(holder.Image, s);
                }
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent == mGdImage){
            List<AttachmentListEntity>  picUrl = mGridAdapter.getPicUrl();
            if (picUrl.size() > 0) {//载入照片地址
                Intent PreView = new Intent(mContext,GalleryActivity.class);
                PreView.putExtra(GalleryActivity.KEY_PHOTO_URL, (Serializable) picUrl);
                PreView.putExtra(GalleryActivity.KEY_TYPE,GalleryActivity.HTTP_URL);
                PreView.putExtra(GalleryActivity.KEY_POSITION, position);
                PreView.putExtra(GalleryActivity.CAN_DELECT_IMAGE, false);
                mContext.startActivity(PreView);
//                startActivityForResult(PreView, PREVIEW_PIC);
            }
        }
    }
}
