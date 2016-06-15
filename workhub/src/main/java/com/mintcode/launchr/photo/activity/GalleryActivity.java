package com.mintcode.launchr.photo.activity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mintcode.launchr.R;
import com.mintcode.launchr.app.newTask.TaskUtil;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.photo.zoom.ViewPagerFixed;
import com.mintcode.launchr.pojo.entity.ApproveCommentEntity;
import com.mintcode.launchr.pojo.entity.AttachmentListEntity;
import com.mintcode.launchr.util.HeadImageUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 图片浏览时的界面
 *
 */
public class GalleryActivity extends BaseActivity implements OnPageChangeListener{
	
	private Intent intent;
    /**
     * 返回按钮
     */
	@Bind(R.id.gallery_back)
	ImageView mIvBack;
	/**
	 * 发送按钮
	 */
	private Button mBtnSend;
	/**
	 * 删除按钮
	 */
	private ImageView mBtnDelect;

	/**
	 * 获取前一个activity传过来type
	 */
	private int mType;

	/** http url */
	public static final int HTTP_URL = 0x10;

	/** sdcard uri */
	public static final int STORAGE_URL = 0x11;
	/** commet Image uri */
	public static final int COMMET_URL = 0x18;
	/**
	 * 当前的位置
	 */
	private int mLocation = 0;
	/**
	 * 照片显示ViewPager
	 */
	private ViewPagerFixed mViewPagerFixed;
	/**
	 * 照片预览适配器
	 */
	private PreviewPicAdapter mPreviewPicAdapter;

	private  List<AttachmentListEntity> mPic = new ArrayList<>();
	private List<String> mPicUrl = new ArrayList<>();
	/** 图片地址key */
	public static final String KEY_PHOTO_URL = "key_photo_url";
	/** 图片url 标识 key */
	public static final String KEY_TYPE = "key_type";
	/** 当前图片显示位置 key */
	public static final String KEY_POSITION = "key_position";
	/** 是否可删除照片标志*/
	public static final String CAN_DELECT_IMAGE = "can_delect_image";
	/** 评论照片数组标志*/
	public static final String KEY_COMMET_PHOTO_URL = "key_commet_photo_url";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plugin_camera_gallery);// 切屏到主界面
		ButterKnife.bind(this);
		initViews();
		initData();
	}



	private void initViews() {

		mBtnSend = (Button) findViewById(R.id.send_button);
		mBtnDelect = (ImageView)findViewById(R.id.gallery_del);
		mViewPagerFixed = (ViewPagerFixed) findViewById(R.id.gallery01);
		//设置监听
		mBtnSend.setOnClickListener(this);
		mBtnDelect.setOnClickListener(this);

		mPreviewPicAdapter = new PreviewPicAdapter();
		mViewPagerFixed.setAdapter(mPreviewPicAdapter);
		mViewPagerFixed.setOnPageChangeListener(this);
	}

	private void initData() {

		Intent intent = getIntent();
		mType = intent.getIntExtra(KEY_TYPE,GalleryActivity.HTTP_URL);
		boolean mCanDelect = intent.getBooleanExtra(CAN_DELECT_IMAGE,true);
		if(!mCanDelect){
			mBtnDelect.setVisibility(View.GONE);
			mBtnSend.setVisibility(View.GONE);
			mIvBack.setVisibility(View.VISIBLE);
		}
		if(mType == HTTP_URL){
			mBtnDelect.setVisibility(View.GONE);
			mPic = (List<AttachmentListEntity>) intent.getSerializableExtra(KEY_PHOTO_URL);
			if(mPic != null){
				for(AttachmentListEntity entity : mPic){
					mPicUrl.add(entity.getPath());
				}
				mPreviewPicAdapter.changeData(mPicUrl);
				int position = intent.getIntExtra(KEY_POSITION,0);
				mLocation = position;
				mViewPagerFixed.setCurrentItem(position);
				mBtnSend.setText(getString(R.string.finish) + "(" + (position + 1)  + "/" + mPicUrl.size()+ ")");
			}
		}else if(mType == STORAGE_URL){
			mPicUrl = (List<String>)intent.getSerializableExtra(KEY_PHOTO_URL);
			if(mPicUrl != null ){
				mPreviewPicAdapter.changeData(mPicUrl);
				int position = intent.getIntExtra(KEY_POSITION,0);
				mViewPagerFixed.setCurrentItem(position);
				mBtnSend.setText(getString(R.string.finish) + "(" + (position + 1)  + "/" + mPicUrl.size()+ ")");
			}
		}else if(mType == COMMET_URL){
			List<ApproveCommentEntity> messageList = (List<ApproveCommentEntity>)intent.getSerializableExtra(KEY_COMMET_PHOTO_URL);
			if(messageList != null){
				for(ApproveCommentEntity entity : messageList){
					mPicUrl.add(entity.getFilePath());
				}
				mPreviewPicAdapter.changeData(mPicUrl);
				int position = intent.getIntExtra(KEY_POSITION,0);
				mLocation = position;
				mViewPagerFixed.setCurrentItem(position);
				mBtnSend.setText(getString(R.string.finish) + "(" + (position + 1)  + "/" + mPicUrl.size()+ ")");
			}
		}

	}

		// 删除按钮添加的监听器
		private void DelListener() {

			if(mType == HTTP_URL){
				if(mPic.size() == 1){
					setResult(RESULT_OK);
					finish();
				}else{
					mLocation = mViewPagerFixed.getCurrentItem();
					mViewPagerFixed.removeAllViews();
					mPic.remove(mLocation);
					mPicUrl.remove(mLocation);
					mPreviewPicAdapter.changeData(mPicUrl);
					mViewPagerFixed.setAdapter(mPreviewPicAdapter);
					if(mLocation == 0){
						mViewPagerFixed.setCurrentItem(0);
						mBtnSend.setText(getString(R.string.finish) + "(1/" + mPicUrl.size() + ")");
					}else{
						mViewPagerFixed.setCurrentItem(mLocation - 1);
						mBtnSend.setText(getString(R.string.finish) + "(" + mLocation + "/" + mPicUrl.size() + ")");
					}

				}
			}else if(mType == STORAGE_URL){

				if(mPicUrl.size() == 1){
					setResult(RESULT_OK);
					finish();
				}else{
					mLocation = mViewPagerFixed.getCurrentItem();
					mViewPagerFixed.removeAllViews();
					mPicUrl.remove(mLocation);
					mPreviewPicAdapter.changeData(mPicUrl);
					mViewPagerFixed.setAdapter(mPreviewPicAdapter);
					if(mLocation == 0){
						mViewPagerFixed.setCurrentItem(0);
						mBtnSend.setText(getString(R.string.finish) + "(1/" + mPicUrl.size() + ")");
					}else{
						mViewPagerFixed.setCurrentItem(mLocation - 1);
						mBtnSend.setText(getString(R.string.finish) + "(" + mLocation + "/" + mPicUrl.size() + ")");
					}
				}
			}
		}
	
	/**
	 * 照片适配器
	 * @author JulyYu
	 *
	 * d2015-8-27
	 */
	class PreviewPicAdapter extends PagerAdapter {

		private ImageView[] imags;

		private List<String> mUrlList = null;

		public PreviewPicAdapter(){
			initImageviewArrays();
		}

		private void initImageviewArrays(){
			imags = new ImageView[9];
			for (int i = 0;i < 9;i++){
				ImageView iv = new ImageView(GalleryActivity.this);
				iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
				imags[i] = iv;
			}
		}
		public void changeData(List<String> list){
			if (list != null) {
				mUrlList = list;
				notifyDataSetChanged();
			}
		}
		@Override
		public int getCount() {
			int count = mUrlList == null ? 0 : mUrlList.size();
			return count;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			int index = position % imags.length;
			ImageView iv = imags[index];
			ViewGroup parent = (ViewGroup) iv.getParent();
			if (parent != null) {
				parent.removeView(iv);
			}
			String url = mUrlList.get(position);
			// 根据类型判断是加载网络图片还是本地图片
			if (mType == HTTP_URL){
				if (LauchrConst.DEV_CODE == 2) {
					String s = LauchrConst.ATTACH_PATH + url.substring(18);
					HeadImageUtil.getInstance(GalleryActivity.this).setImageResuces(iv, s);
				}else {
					String s = url.replace("http://a.mintcode.comcom", LauchrConst.ATTACH_PATH);
					HeadImageUtil.getInstance(GalleryActivity.this).setImageResuces(iv, s);
				}

			}else if (mType == STORAGE_URL){
				File file = new File(url);
				Glide.with(GalleryActivity.this).load(file).into(iv);
			}else{
				HeadImageUtil.getInstance(GalleryActivity.this).setImageResuces(iv,url);
			}

			container.addView(iv);
			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			int index = position % imags.length;
			container.removeView(imags[index]);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gallery_del:
			DelListener();
			break;
		case R.id.send_button:
			if(mType == STORAGE_URL){
				Intent i = new Intent();
				i.putExtra(GalleryActivity.KEY_PHOTO_URL, (Serializable) mPicUrl);
				setResult(RESULT_OK,i);
				finish();
			}else{
				finish();
			}
			break;
		default:
			break;
		}
	}
	@OnClick(R.id.gallery_back)
	void onBack(){
		this.finish();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		mLocation = mViewPagerFixed.getCurrentItem();
		mBtnSend.setText(getString(R.string.finish) + "(" + (mLocation + 1) + "/" + mPicUrl.size() + ")");
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
//		mBtnSend.setText(getString(R.string.finish) + "(" + (mLocation + 1) + "/" + mPicUrl.size() + ")");
	}
	
}
