package com.mintcode.chat.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.mintcode.launchr.util.TTDensityUtil;

/**
 *
 */
public class RoundDrawable extends Drawable {

	Drawable mDrawable;
	Drawable drawableHolder;
	public RoundDrawable (Drawable drawable,Drawable d){
		Bitmap bitmap = getmBitmapFromDrawable(drawable);
		mBitmap = bitmap;
		BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP);
		mBitmapHolde = getmBitmapFromDrawable(d);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setShader(bitmapShader);
//		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		mDrawable = drawable;
		drawableHolder = d;
		drawableHolder.setBounds(0,0,mBitmap.getWidth(),mBitmap.getHeight());
	}


	private Bitmap getmBitmapFromDrawable(Drawable drawable){
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);


		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
//		d.draw(canvas);
		return bitmap;
	}

	private Bitmap getmBitmapFromholder(Drawable drawable){
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);

//		d.draw(canvas);
		return bitmap;
	}


	private Paint mPaint;
	private Bitmap mBitmap;
	private Bitmap mBitmapHolde;

	private RectF rectF;
	private RectF r1;

	Path path = new Path();
	@Override
	public void setBounds(int left, int top, int right, int bottom)
	{
//		int x = 60;
		super.setBounds(left, top, right, bottom);
		rectF = new RectF(left + 40, top, right, bottom);
		path = new Path();
		path.moveTo(left + 40,top + 100);
		path.lineTo(left + 20, top + 80);
		path.lineTo(left + 40,top + 60);
		r1 = new RectF(left, top,left + 40, bottom);
	}

	@Override
	public void draw(Canvas canvas)
	{


//		canvas.drawRoundRect(rectF, 30, 30, mPaint);
//
////		drawableHolder.draw(canvas);
//		mPaint.reset();
//		mPaint.setColor(Color.RED);
//		canvas.drawRect(r1, mPaint);
//		mDrawable.draw(canvas);

		canvas.drawRoundRect(rectF, 30, 30, mPaint);
//		mPaint.reset();
//		mPaint.setColor(Color.RED);
		canvas.drawPath(path,mPaint);
//		drawableHolder.draw(canvas);
//
//		// 实例化画笔
//		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//
//		// 设置两张图片相交时的模式
//		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//
//		// 在画布上画上原图
//		canvas.drawBitmap(mBitmap, 0, 0, mPaint);
//
//		// 在画布上画上遮罩图
//		canvas.drawBitmap(mBitmapHolde, 0, 0, mPaint);
//		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
////		canvas.drawBitmap(rectF, 0, 0, mPaint);
//		canvas.drawRect(rectF,mPaint);
	}

	@Override
	public int getIntrinsicWidth()
	{
		return mBitmap.getWidth();
	}

	@Override
	public int getIntrinsicHeight()
	{
		return mBitmap.getHeight();
	}

	@Override
	public void setAlpha(int alpha)
	{
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf)
	{
		mPaint.setColorFilter(cf);
	}

	@Override
	public int getOpacity()
	{
		return PixelFormat.TRANSLUCENT;
	}
}
