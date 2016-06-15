package com.mintcode.launchr.app.newSchedule.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by JulyYu on 2016/3/22.
 */
public class NoTouchGridView  extends GridView{



    public NoTouchGridView(Context context) {
        super(context);
    }

    public NoTouchGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoTouchGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return super.onInterceptHoverEvent(event);
    }
}
