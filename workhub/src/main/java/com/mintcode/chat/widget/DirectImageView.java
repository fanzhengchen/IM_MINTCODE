package com.mintcode.chat.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.util.TTDensityUtil;

/**
 *
 */
public class DirectImageView extends ImageView {

	private Paint mPaint;

	private Paint maskPaint;
	private Paint pathPaint;

	private int direct = 1;

	private final RectF roundRect = new RectF();

	private int mWidth;

	private int mHeight;


	private Path mPath;

	private Context mContext;
	private Bitmap mBitmap;
	private int mIntBitWith;
	private int mIntBitHeight;

	public DirectImageView(Context context) {
		super(context);
		mContext = context;
		init();
	}


	public DirectImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();

	}


	private void init(){
		maskPaint = new Paint();
		maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

		pathPaint = new Paint();
//		pathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.WHITE);

		mPath = new Path();
		this.setScaleType(ScaleType.CENTER);
		mBitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.im_default_image);
		mIntBitWith =  mBitmap.getHeight()/2;
		mIntBitWith = mBitmap.getWidth()/2;
//		this.setImageResource(R.drawable.im_default_image);
	}

	int x = 20;
	public void setDirect(int direct){
		this.direct = direct;
		if(direct == 1){
			roundRect.set(0 + x, 0, mWidth, mHeight);
			mPath.moveTo(x, 2*x);
			mPath.lineTo(x / 2, x + x / 2);
			mPath.lineTo(x, x);
			invalidate();
		}else {
			roundRect.set(0,0,mWidth,mHeight - x);
		}

	}


	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mWidth = getWidth();
		mHeight = getHeight();
		roundRect.set(0, 0, mWidth, mHeight);
		setDirect(1);
	}


	@Override
	protected void onDraw(Canvas canvas) {
		int radius = 20;

		canvas.saveLayer(roundRect, mPaint, Canvas.ALL_SAVE_FLAG);
		canvas.drawRoundRect(roundRect, radius, radius, mPaint);
		if(direct == 1){
			pathPaint.setColor(Color.RED);
			canvas.drawPath(mPath,pathPaint);
		}else if(direct == 2){

		}

		canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
		canvas.drawBitmap(mBitmap,mWidth/2 - mIntBitWith,mHeight/2 - mIntBitHeight,pathPaint);
		super.onDraw(canvas);
		canvas.restore();
	}

	@Override
	public void setScaleType(ScaleType scaleType) {

		super.setScaleType(scaleType);
	}

}
