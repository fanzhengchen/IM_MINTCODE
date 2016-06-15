package com.mintcode.launchr.widget;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by KevinQiao on 2016/3/15.
 */
public class AlbumViewPager extends ViewPager {

    private boolean mEnable = true;

    public AlbumViewPager(Context context) {
        super(context);
    }

    public AlbumViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(mEnable)
        return super.onInterceptTouchEvent(ev);
        return false;
    }

    public void setEnable(boolean enable){
        this.mEnable = enable;
    }
}

