package com.mintcode.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mintcode.RM;
import com.mintcode.chat.image.AttachItem;
import com.mintcode.chat.util.BitmapUtil;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.util.TTDensityUtil;
import com.mintcode.launchr.util.TTJSONUtil;
import com.mintcode.launchr.view.MTContainerPopWindow;
import com.mintcode.launchr.widget.AlbumViewPager;
import com.mintcode.launchr.widget.MatrixImageView;
import com.polites.android.GestureImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PreviewLargeImageActivity extends BaseActivity implements ViewPager.OnPageChangeListener ,OnClickListener, OnLongClickListener,View.OnTouchListener {


	/** viewpager */
//	private ViewPager mVpPhoto;

	AlbumViewPager mVpPhoto;

	/** 适配器 */
	private ViewPagerAdapter mAdatpter;

	private ProgressBar mPbProgess;

	private boolean isSave = false;


	private int mWidth = -1;
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int id = R.layout.activity_preview_large;
		setContentView(R.layout.activity_preview_large);
		mWidth = TTDensityUtil.dip2px(this,140);
		initView();
		initData();


	}

	private void initView(){
		mPbProgess = (ProgressBar) findViewById(R.id.pb_progress);
		mVpPhoto = (AlbumViewPager) findViewById(R.id.vp_large_photot);
		mAdatpter = new ViewPagerAdapter();
		mVpPhoto.setAdapter(mAdatpter);

		mVpPhoto.setOnPageChangeListener(this);

		handler = new Handler();

		mVpPhoto.setOnClickListener(this);
	}
	int i = 0;
	@Override
	public void onClick(View v) {
		if(v == mVpPhoto){


		}
	}
	Runnable r = new Runnable() {
		@Override
		public void run() {
			finish();
			overridePendingTransition(R.anim.scale_retain,R.anim.scale_out);
		}
	};

	private void initData(){
		Intent intent = getIntent();
		List<MessageItem> list = (List<MessageItem>) intent.getSerializableExtra("list");
		int position = intent.getIntExtra("position",-1);
		if(list != null){
			mAdatpter.changeData(list);
			mVpPhoto.setCurrentItem(position);
		}

	}



	@Override
	public boolean onLongClick(View v) {
		return false;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	long x = 0;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		switch (action){
			case MotionEvent.ACTION_DOWN:
				x = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
				long y = System.currentTimeMillis();
				if(y - x < 100){
					i++;
					if(i % 2 == 0){
						handler.removeCallbacks(r);
					}else {
						handler.postDelayed(r,500);
					}
				}else {
					i = 0;
					handler.removeCallbacks(r);
					if(y - x > 1000){

					}
				}
                break;

            default:

                break;
		}

		return false;
	}

	class ViewPagerAdapter extends PagerAdapter {

		private MatrixImageView[] imags;

		private GestureImageView[] gestureImageViews;

		private List<MessageItem> mUrlList = null;

		public ViewPagerAdapter(){
			initImageviewArrays();
			initGestureImage();
//			initView();
		}

		private void initImageviewArrays(){
			imags = new MatrixImageView[5];
			for (int i = 0;i < 5;i++){
				MatrixImageView iv = new MatrixImageView(getApplicationContext(), null);
				iv.setDisplayType(MatrixImageView.DisplayType.FIT_TO_SCREEN);
				iv.setScaleType(ImageView.ScaleType.CENTER);
				imags[i] = iv;
			}
		}

		private void initGestureImage(){
			gestureImageViews = new GestureImageView[5];
			for (int i = 0;i < 5;i++){
//				LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				GestureImageView view = new GestureImageView(getApplicationContext());
				view.setId(i);
				gestureImageViews[i] = view;
			}
		}



		public void changeData(List<MessageItem> list){
			if (list != null) {
//				initGestureImage(list.size());
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
//			int index = position;// % gestureImageViews.length;
			int index = position % gestureImageViews.length;
//			int index = position % imags.length;

			GestureImageView iv = gestureImageViews[index];
//			MatrixImageView iv = imags[index];
			ViewGroup parent = (ViewGroup) iv.getParent();
			if (parent != null) {
				parent.removeView(iv);
			}
			iv.setImageBitmap(null);
			iv.setImageDrawable(null);

			MessageItem item = mUrlList.get(position);
			getImageUrl(item,iv);

			ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			iv.setLayoutParams(p);
			container.addView(iv);
			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
//			int index = position;
			int index = position % imags.length;
			GestureImageView iv = gestureImageViews[index];
//			MatrixImageView iv = imags[index];
//			iv.setImageDrawable(null);
//			Glide.clear(iv);
			container.removeView(iv);

		}


		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}



	private void getImageUrl(MessageItem item,final GestureImageView iv){

		String url = "";
		AttachItem attach = TTJSONUtil.convertJsonToCommonObj(item.getContent(), AttachItem.class);

		if(attach == null){
//			toast("hehe");
			return;
		}
		// 原始图片地址
		if (attach.getFileUrl() == null) {
//			toast("hehe");
			return;
		}

		mPbProgess.setVisibility(View.VISIBLE);
//		iv.setImageDrawable(null);

		String path = Environment.getExternalStorageDirectory() + "/Launchr" + attach.getFileUrl() + ".jpg";
		File file = new File(path);
		iv.setOnTouchListener(this);
		isSave = false;
		url = LauchrConst.httpIp + "/launchr" + attach.getFileUrl();
		if(!file.exists()){
			openLargeImage(iv, url, path);
		}else{
			setImageResuces(getApplicationContext(),iv,file);
			isSave = true;
			mPbProgess.setVisibility(View.GONE);
		}

		final String strPath = path;

		OnLongClickListener listener = new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				onLongClickSave(strPath,iv);
				return false;
			}
		};
		iv.setOnLongClickListener(listener);

//		GestureImageView
//		iv.setOnLongClickListener(detector);

//		url = LauchrConst.httpIp + "/launchr" + attach.getFileUrl();


//		final String username = LauchrConst.header.getLoginName();
//		GlideUrl glideUrl = new GlideUrl(url){
//			@Override
//			public Map<String, String> getHeaders() {
//				HashMap map = new HashMap();
//				map.put("Cookie", "AppName=launchr;UserName=" + username);
//				return map;
//			}
//		};

//		iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
//		iv.setDisplayType(MatrixImageView.DisplayType.FIT_TO_SCREEN);

//		Glide.with(this).load(glideUrl).into(iv);

	}



	private void openLargeImage(final ImageView iv, String url,final String path){
		final String username = LauchrConst.header.getLoginName();
		RequestQueue mQueue = Volley.newRequestQueue(this);
		Response.Listener<Bitmap> listener =new Response.Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap response) {
				boolean success = BitmapUtil.saveBitmap(response,getApplicationContext(),path,100);
				if(success){
					File file = new File(path);
					isSave = true;
					setImageResuces(getApplicationContext(),iv,file);
				}else{
					PreviewLargeImageActivity.this.toast("error save bitmap");
				}
//				mIvProgress.setVisibility(View.GONE);
//				dismissLoading();
//				iv.setImageBitmap(response);
				mPbProgess.setVisibility(View.GONE);
			}
		};

		Response.ErrorListener error = new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.i("infos", "====error===" + error);

			}
		};

		ImageRequest request = new ImageRequest(url,listener,0,0, Bitmap.Config.RGB_565,error){
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap map = new HashMap();
				map.put("Cookie", "AppName=launchr;UserName=" + username);
				return map;
			}
		};

		mQueue.add(request);
		mQueue.start();
	}

	public void setImageResuces(Context context,final ImageView iv, File file){
		RequestManager man = Glide.with(context);
//		man.load(file).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv);
		man.load(file).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(new SimpleTarget<Bitmap>() {
			@Override
			public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
				if(bitmap.getWidth()>4000 || bitmap.getHeight()>4000){
					iv.setImageBitmap(setBitmapCompress(bitmap));
				}else{
					iv.setImageBitmap(bitmap);
				}
			}
		});

//		InputStream in = new FileInputStream(in)
//		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//		iv.setImageBitmap(bitmap);
	}

	MTContainerPopWindow mContainerPopWindow;
	private void onLongClickSave(final String filePath,ImageView iv){
		mContainerPopWindow = new MTContainerPopWindow(this);
		int rname = R.string.calendar_save;
		mContainerPopWindow.addTextView(rname, new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (filePath != null || isSave) {
//					String path = BitmapUtil.saveBitmapAsJpeg(dst, ImgPreviewActivity.this);
//					if (path != null) {
//					}
					String fileStr = Environment.getExternalStorageDirectory() + "/Pictures/Launchr/IMG_" + System.currentTimeMillis() + ".jpg";
					Bitmap bitmap = BitmapFactory.decodeFile(filePath);
					boolean success = BitmapUtil.saveBitmap(bitmap, getApplicationContext(), fileStr, 100);
					if (success) {
						Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
						File f = new File(fileStr);
						Uri uri = Uri.fromFile(f);
						intent.setData(uri);
						sendBroadcast(intent);
						String show = getString(R.string.image_preview_save);
						Toast.makeText(getApplicationContext(), show, Toast.LENGTH_SHORT).show();
					} else {
						String show = getString(R.string.calendar_view_detail_fail);
						Toast.makeText(getApplicationContext(), show, Toast.LENGTH_SHORT).show();
					}

				} else {
					String show = getString(R.string.calendar_view_detail_fail);
					Toast.makeText(getApplicationContext(), show, Toast.LENGTH_SHORT).show();
				}
				mContainerPopWindow.dismiss();
			}
		});
		mContainerPopWindow.show(iv);
	}

	/** 图片太大压缩图片,图片按比例大小压缩方法*/
	private Bitmap setBitmapCompress(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 50, baos);  //这里压缩50%，把压缩后的数据存放到baos中

		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是1920*1080分辨率，所以高和宽我们设置为
		float hh = 1920f;
		float ww = 1080f;
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return bitmap;
	}
}
