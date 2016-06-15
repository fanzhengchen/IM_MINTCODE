package com.mintcode.launchr.activity;

import java.util.ArrayList;
import java.util.List;

import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.Const;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * @author KevinQiao
 *
 */
public class WelComActivity extends BaseActivity {

	/** handler */
	private Handler mHandler;
	
	/** 工具类 */
	private AppUtil mUtil;
	
	private ViewPager mVp;
	
	private int[] mResId = {R.drawable.v_1,R.drawable.v_2,R.drawable.v_3};

	private int[] mResIdCN = {R.drawable.v_cn_1,R.drawable.v_cn_2,R.drawable.v_cn_3};

	private List<ImageView> mList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcom);
		
		initView();
		
	}

	/**
	 * 实例化view
	 */
	private void initView(){
		mVp = (ViewPager) findViewById(R.id.vp_wel);
		
		mHandler = new Handler();
		mUtil = AppUtil.getInstance(this);
		WelAdapter adapter =new WelAdapter();
		mVp.setAdapter(adapter);
		
	}
	
	
	class WelAdapter extends PagerAdapter{

		private List<View> list = getListView();
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = list.get(position);
			
			container.addView(view);
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(list.get(position));
		}
		
		
		
	}

	private List<View> getListView(){
		List<View> list = new ArrayList<View>();
		mList = new ArrayList<ImageView>();
		for (int i = 0; i < 3; i++) {
			View view = getLayoutInflater().inflate(R.layout.view_last, null);
			ImageView iv = (ImageView) view.findViewById(R.id.iv_page);
			RelativeLayout mRel = (RelativeLayout) view.findViewById(R.id.rel_start);
			if (i != 2) {
				mRel.setVisibility(View.GONE);
			}else{
				mRel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mUtil.saveIntValue(Const.KEY_GESTURE, 0);
						Intent intent;
						if (LauchrConst.IS_WORKHUB) {
							intent = new Intent(WelComActivity.this, LoginWorkHubActivity.class);
						} else {
							intent = new Intent(WelComActivity.this, LoginActivity.class);
						}
						startActivity(intent);
						finish();
					}
				});
			}

			if(LauchrConst.DEV_CODE == 0){
				iv.setBackgroundResource(mResId[i]);
			}else{
				iv.setBackgroundResource(mResIdCN[i]);
			}
			mList.add(iv);
			list.add(view);
		}
		
		return list;
	}
	
}
