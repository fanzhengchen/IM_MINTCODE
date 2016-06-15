package com.mintcode.launchr.view;

import java.lang.reflect.Field;

import android.content.Context;

import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * @name:ViewPagerScroller.java
 * @author:RamboCai
 * @time：2014-12-30 下午2:40:36
 * @description:改变ViewPager滑动速度
 */
public class ViewPagerScroller extends Scroller {

	/**
	 * ViewPager 滚动速度设置
	 * 
	 */

	private int mScrollDuration = 2000; // 滑动速度

	/**
	 * 设置速度速度
	 * 
	 */
	public void setScrollDuration(int duration) {
		this.mScrollDuration = duration;
	}

	public ViewPagerScroller(Context context) {
		super(context);
	}

	public ViewPagerScroller(Context context, Interpolator interpolator) {
		super(context, interpolator);
	}

	public ViewPagerScroller(Context context, Interpolator interpolator,
			boolean flywheel) {
		super(context, interpolator, flywheel);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy, int duration) {
		super.startScroll(startX, startY, dx, dy, mScrollDuration);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy) {
		super.startScroll(startX, startY, dx, dy, mScrollDuration);
	}

	public void initViewPagerScroll(ViewPager viewPager) {
		try {
			Field mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			mScroller.set(viewPager, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
