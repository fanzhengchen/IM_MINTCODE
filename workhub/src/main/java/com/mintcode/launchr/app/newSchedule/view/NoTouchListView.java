package com.mintcode.launchr.app.newSchedule.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by JulyYu on 2016/3/22.
 */
public class NoTouchListView extends ListView {

    /** 触摸时按下的点 **/
    PointF downP = new PointF();
    /** 触摸时当前的点 **/
    PointF curP = new PointF();

    /** 触摸时按下的点 **/
    PointF fdownP = new PointF();
    /** 触摸时当前的点 **/
    PointF fcurP = new PointF();

    public NoTouchListView(Context context) {
        super(context);
    }

    public NoTouchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoTouchListView(Context context, AttributeSet attrs, int defStyle) {
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
    public boolean onInterceptTouchEvent(MotionEvent ev) {

//        fcurP.x = ev.getX();
//        fcurP.y = ev.getY();
//        if(ev.getAction() == MotionEvent.ACTION_DOWN){
//            fdownP.x = ev.getX();
//            fdownP.y = ev.getY();
//        }
//
//        if (Math.abs(fdownP.x - fcurP.x) > 0 && Math.abs(fdownP.y - fcurP.y) > 0) {
//            if(ev.getAction() == MotionEvent.ACTION_MOVE){
//                return true;
//            }
//        }
//        if (ev.getAction() == MotionEvent.ACTION_UP) {
////            	Toast.makeText(getContext(), "up ", Toast.LENGTH_SHORT).show();
//                if (Math.abs(fdownP.x - fcurP.x) < 20 && Math.abs(fdownP.y - fcurP.y) < 20) {
//                     return false;
//                }else{
////                	return true;
//                }
//        }
        return super.onInterceptTouchEvent(ev);
    }
}
