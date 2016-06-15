package com.mintcode.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.model.GlideUrl;
import com.mintcode.cache.CacheManager;
import com.mintcode.chat.image.AttachItem;
import com.mintcode.chat.util.BitmapUtil;
import com.mintcode.chat.util.JsonUtil;
import com.mintcode.im.database.KeyValueDBService;
import com.mintcode.im.entity.MessageItem;
import com.mintcode.im.util.Keys;
import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.view.MTContainerPopWindow;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class ImgPreviewActivity extends BaseActivity implements OnClickListener, OnLongClickListener {

	private ImageViewTouch mImageView;
	private ImageView mImgBack;
	private String originalUrl;
	private CacheManager cache;
	private ProgressBar mProgressDown;
	public static final int DOWN_IMG = 111;
	private String SERVER_PATH;
	
	private MTContainerPopWindow mContainerPopWindow;

	private ProgressBar mIvProgress;

	private boolean isSave = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		int id = R.layout.activity_img_preview;
		setContentView(R.layout.activity_img_preview);
		mImgBack = (ImageView) findViewById(R.id.img_back);
		mTvSave = (TextView) findViewById(R.id.tv_right);
		mProgressDown=(ProgressBar)findViewById(R.id.pb_down);
		LinearLayout layout = (LinearLayout) findViewById(R.id.ll_base);
		mIvProgress = (ProgressBar) findViewById(R.id.iv_progress);
		mImageView = new ImageViewTouch(this, null);
		LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layout.addView(mImageView, params);

		SERVER_PATH = KeyValueDBService.getInstance().find(Keys.HTTP_IP) + "/launchr";
		mTvSave.setOnClickListener(this);
		mImgBack.setOnClickListener(this);
		mImageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
		MessageItem item = (MessageItem) getIntent().getParcelableExtra(
				"item");
		cache = CacheManager.getInstance(this);
		int cmd = item.getCmd();
		if (cmd == MessageItem.TYPE_SEND) {
			// 发送消息，用本地地址作为key获取图片
			if(item.getFileName() != null){
				dst = cache.getBitmapByRealPath(item.getFileName());
				if(dst == null){
					dst = cache.getBitmapByKey(item.getFileName(), 1080*2, 1196*2);
				}
				mImageView.setImageBitmap(dst);
			}else{
				// 用url作为key获取图片
				AttachItem attach = JsonUtil.convertJsonToCommonObj(item.getContent(), AttachItem.class);
				// 原始图片地址
				if (attach.getFileUrl() == null) {
					return;
				}
				// 缩略图地址
				String thumbUrl = SERVER_PATH + attach.getThumbnail();
				// 首先设置缩略图作为背景
				Bitmap thumb = cache.getBitmapByKey(thumbUrl);
				if (thumb != null) {
					mImageView.setImageBitmap(thumb);
				}
				mImageView.setTag(originalUrl);
				originalUrl = SERVER_PATH + attach.getFileUrl();
				mImageView.setTag(originalUrl);
				cache.loadBitmap(originalUrl, mImageView,mProgressDown ,1080,1196);
				dst = cache.getBitmapByKey(originalUrl);
			}
		} else if (cmd == MessageItem.TYPE_RECV) {
			// 接受消息，用url作为key获取图片
			AttachItem attach = JsonUtil.convertJsonToCommonObj(
					item.getContent(), AttachItem.class);
			// 原始图片地址
			if (attach.getFileUrl() == null) {
				return;
			}

			// 判断本地是否有这个文件
			String path = Environment.getExternalStorageDirectory() + "/Launchr" + attach.getFileUrl() + ".jpg";
////			String path = Environment.getExternalStorageDirectory() + "/Pictures/Launchr/IMG_20160203_202748.jpg" ;
//			String path = Environment.getExternalStorageDirectory() + "/Pictures/Launchr"+attach.getFileUrl() + ".jpg" ;
////			file:///storage/emulated/0/Pictures/Telegram/IMG_20160203_202748.jpg
			File file = new File(path);
			filePath = path;
//			showLoading();
			mIvProgress.setVisibility(View.VISIBLE);
			String thumbUrl = SERVER_PATH + attach.getThumbnail();
			// 首先设置缩略图作为背景
			Bitmap thumb = cache.getBitmapByKey(thumbUrl);
			if (thumb != null) {
				mImageView.setImageBitmap(thumb);
			}

//			originalUrl = SERVER_PATH + attach.getFileUrl();
//			openImage(mImageView,originalUrl);

			if(!file.exists()){
				originalUrl = SERVER_PATH + attach.getFileUrl();
				openLargeImage(mImageView,originalUrl,path);
			}else{
//				TaskUtil.setImageResuces(getApplicationContext(),mImageView,file);
				Log.i("infos","Hai lo");
				mIvProgress.setVisibility(View.GONE);
				setImageResuces(this, mImageView, file);
				isSave = true;
			}


			// 缩略图地址
//			String thumbUrl = SERVER_PATH + attach.getThumbnail();
//			// 首先设置缩略图作为背景
//			Bitmap thumb = cache.getBitmapByKey(thumbUrl);
//			if (thumb != null) {
//				mImageView.setImageBitmap(thumb);
//			}
//			originalUrl = SERVER_PATH + attach.getFileUrl();
//			mImageView.setTag(originalUrl);
//			cache.loadBitmap(originalUrl, mImageView,mProgressDown ,1080,1196);
//			dst = cache.getBitmapByKey(originalUrl);
		}

		mImageView.setOnLongClickListener(this);
	}
	String filePath = "";
	public void setImageResuces(Context context,ImageView iv, File file){
		// 处理圆形图片
		RequestManager man = Glide.with(context);
		man.load(file).crossFade().into(iv);
//		dismissLoading();



	}
	
	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		mContainerPopWindow = new MTContainerPopWindow(ImgPreviewActivity.this);
		int rname = R.string.calendar_save;
		mContainerPopWindow.addTextView(rname, new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(dst != null || isSave){
//					String path = BitmapUtil.saveBitmapAsJpeg(dst, ImgPreviewActivity.this);
//					if (path != null) {
//					}
					String fileStr = Environment.getExternalStorageDirectory() + "/Pictures/Launchr/IMG_" + System.currentTimeMillis() + ".jpg";
					Bitmap bitmap = BitmapFactory.decodeFile(filePath);
					boolean success = BitmapUtil.saveBitmap(bitmap,getApplicationContext(),fileStr,100);
					if(success){
						Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
						File f = new File(fileStr);
						Uri uri = Uri.fromFile(f);
						intent.setData(uri);
						sendBroadcast(intent);
						String show = getString(R.string.image_preview_save);
						Toast.makeText(ImgPreviewActivity.this, show, Toast.LENGTH_SHORT).show();
					}else {
						String show = getString(R.string.calendar_view_detail_fail);
						Toast.makeText(ImgPreviewActivity.this, show, Toast.LENGTH_SHORT).show();
					}

				}else{
					String show = getString(R.string.calendar_view_detail_fail);
					Toast.makeText(ImgPreviewActivity.this, show, Toast.LENGTH_SHORT).show();
				}
				mContainerPopWindow.dismiss();
			}
		});
		mContainerPopWindow.show(mImageView);
		return false;
	}

	private void saveToGalley(File f){

	}

	private TextView mTvSave;
	private Bitmap dst;

	public void onClick(View v) {
		if (v == mTvSave && dst != null) {
			String path = BitmapUtil.saveBitmapAsJpeg(dst, this);
			if (path != null) {
				Toast.makeText(this, getResources().getString(R.string.image_preview_save), Toast.LENGTH_SHORT).show();
			}
		} else if (v == mImgBack) {
			onBackPressed();
		}
	};


	private void openImage(final ImageView iv, String url){
		final String username = LauchrConst.header.getLoginName();
		GlideUrl glideUrl = new GlideUrl(url){
			@Override
			public Map<String, String> getHeaders() {
				HashMap map = new HashMap();
				map.put("Cookie", "AppName=launchr;UserName=" + username);
				return map;
			}
		};
		Glide.with(getApplicationContext()).load(glideUrl).into(iv);
	}

	private void openLargeImage(final ImageView iv, String url,final String path){
		final String username = LauchrConst.header.getLoginName();
		RequestQueue mQueue = Volley.newRequestQueue(this);
		Response.Listener<Bitmap> listener =new Response.Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap response) {
//				Log.i("infos","===bitmap==" + response.getb);
				boolean success = BitmapUtil.saveBitmap(response,getApplicationContext(),path,100);
				if(success){
					File file = new File(path);
					isSave = true;
					Glide.with(ImgPreviewActivity.this).load(file).into(iv);
				}else{
					ImgPreviewActivity.this.toast("error save bitmap");
				}
				mIvProgress.setVisibility(View.GONE);
//				dismissLoading();
//				iv.setImageBitmap(response);
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

	@Override
	public void onBackPressed() {
		finish();
	}
}
