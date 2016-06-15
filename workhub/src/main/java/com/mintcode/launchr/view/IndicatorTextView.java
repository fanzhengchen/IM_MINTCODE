package com.mintcode.launchr.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mintcode.launchr.R;

/**
 * Created by JulyYu on 2016/2/16.
 */
public class IndicatorTextView extends TextView {


    /**
     * 绘制三角形的画笔
     */
    private Paint mPaint;
    /**
     * path构成一个三角形
     */
    private Path mPath;

    private float mTranslationX;

    private int mTriangleWidth;
    private int mTriangleHeight;

    public IndicatorTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        createPaint();
    }
    public IndicatorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createPaint();
    }
    public IndicatorTextView(Context context) {
        super(context);
        createPaint();
    }

    private  void createPaint(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.white));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
//		// 画笔平移到正确的位置
        canvas.translate(mTranslationX, getHeight() + 1);
////		修改处 canvas.drawPath(mPath, mPaint);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth = getWidth();
//		// 初始化三角形
        initTriangle();
//		mInitTranslationX = 0;

    }

    private void initTriangle()
    {
        mPath = new Path();

//	修改处	mTriangleHeight = (int) (mTriangleWidth / 2 / Math.sqrt(2));
        mTriangleHeight = 10;
        mPath.moveTo(0, 0);

        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth, -mTriangleHeight);
        mPath.lineTo(0 , -mTriangleHeight);
        mPath.close();

    }

    public void freshLine(boolean bool){
        if(bool){
            mPaint.setColor(getResources().getColor(R.color.blue));
        }else{
            mPaint.setColor(getResources().getColor(R.color.white));
        }
        invalidate();
    }

}
