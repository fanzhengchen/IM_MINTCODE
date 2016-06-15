package com.mintcode.launchr.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

/**
 * Created by KevinQiao on 2016/3/15.
 */
public class MatrixImageView extends ImageViewTouch {
    OnChildMovingListener mListner = null;
    public MatrixImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MatrixImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean b = super.dispatchTouchEvent(event);
        Log.i("MatrixImageView",b + "==dispatchTouchEvent==");
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean b = super.onTouchEvent(event);
        Log.i("MatrixImageView",b + "==onTouchEvent==");
        return b;
    }
}
