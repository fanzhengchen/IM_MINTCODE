package com.mintcode.chat.image;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.mintcode.RM;
import com.mintcode.launchr.R;


public class ImageSelectedActivity extends Activity {
	private List<ImageBucket> dataList;
	private GridView gridView;
	private ImageBucketAdapter adapter;
	private AlbumHelper helper;
	private ImageView mIvCancel;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	public static final int REQ_SELECT_OK = 0x0001;
	public static final int REQ_SELECT_CANCEL = 0x0002;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=ImageSelectedActivity.this;

		setContentView(R.layout.activity_image_bucket);
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		initData();
		initView();

		mIvCancel = (ImageView) findViewById(R.id.iv_back);
		mIvCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		dataList = helper.getImagesBucketList(false);
		bimap = BitmapFactory.decodeResource(getResources(),R.drawable.icon_add);
	}

	/**
	 * 初始化view视图
	 */
	private void initView() {

		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new ImageBucketAdapter(this, dataList);
		gridView.setAdapter(adapter);
		
		

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ImageSelectedActivity.this,
						SelectPhotoActivity.class);
				intent.putExtra(ImageSelectedActivity.EXTRA_IMAGE_LIST,
						(Serializable) dataList.get(position).imageList);
				startActivityForResult(intent, REQ_SELECT_OK);
			}

		});
		
		openCamera();
	}

	private void openCamera() {
		int index = 0 ;
		for (int i = 0; i < dataList.size(); i++) {
			String bucketName = dataList.get(i).bucketName;
			if("Camera".equals(bucketName)){
				index = i ;
			}
		}
		Intent intent = new Intent(ImageSelectedActivity.this,
				SelectPhotoActivity.class);
		intent.putExtra(ImageSelectedActivity.EXTRA_IMAGE_LIST,
				(Serializable) dataList.get(index).imageList);
		startActivityForResult(intent, REQ_SELECT_OK);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {
		case REQ_SELECT_OK:
			List<String> mSelectedImage = ImageGridAdapter.mSelectedImage;
			if (mSelectedImage.size() > 0) {
				setResult(RESULT_OK);
				finish();
			}
			break;

		default:
			break;
		}

		// mSelectedImage.clear();
	}
}
