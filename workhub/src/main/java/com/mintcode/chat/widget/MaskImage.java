package com.mintcode.chat.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.mintcode.launchr.util.TTDensityUtil;

/**
 * @author sid'pc </br> 使用demo </br> MaskImage maskImage =
 *         (MaskImage)findViewById(R.id.img);</br>
 *         maskImage.setMaskResource(R.drawable.chat_pop);（此句用来设置背景图片，可不写）</br>
 *         Bitmap original =BitmapFactory.decodeResource(getResources(),
 *         R.drawable.lauch_bg);</br> maskImage.setImage(original);</br>
 */
public class MaskImage extends ImageView {

	private NinePatchDrawable drawable;

	private Context mContext;

	public MaskImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public void setImage(Bitmap original) {
		if (original == null) {
			return;
		}
		
		
		// 图片宽大于140dp，小雨265dp
		if(original.getWidth() < TTDensityUtil.dip2px(mContext, 140)){
			float x = (float)TTDensityUtil.dip2px(mContext, 140)/(float)original.getWidth();
			Matrix matrix = new Matrix();
			matrix.postScale(x, x);
			original = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
		}else if(original.getWidth() > TTDensityUtil.dip2px(mContext, 265)){
			float x = (float)TTDensityUtil.dip2px(mContext, 265)/(float)original.getWidth();
			Matrix matrix = new Matrix();
			matrix.postScale(x, x);
			original = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
		}
		
		// 实例化一个与资源图片一样大的空bitmap
		Bitmap result = Bitmap.createBitmap(original.getWidth(),
				original.getHeight(), Config.ARGB_8888);

		// 将空bitmap放在画布上
		Canvas canvas = new Canvas(result);

		// 设置将Drawable进行缩放
		drawable.setBounds(0, 0, original.getWidth(), original.getHeight());

		// 将缩放后的Drawable画在画布上，使画布上的bitmap为缩放后的遮罩图
		drawable.draw(canvas);

		// 将遮罩赋值给另一个变量，以便画布继续画图后能拿到遮罩图
		Bitmap bitmap = Bitmap.createBitmap(result);

		// 实例化画笔
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		// 设置两张图片相交时的模式
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

		// 在画布上画上原图
		canvas.drawBitmap(original, 0, 0, null);
		
		// 在画布上画上遮罩图
		canvas.drawBitmap(bitmap, 0, 0, paint);

		// 将画布上的图像赋值给ImageView
		setImageBitmap(result);

		// 设置图像显示方式
		setScaleType(ScaleType.CENTER);
	}

	public void setMaskResource(int resourceId) {
		try {
			drawable = (NinePatchDrawable) getResources().getDrawable(
					resourceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
