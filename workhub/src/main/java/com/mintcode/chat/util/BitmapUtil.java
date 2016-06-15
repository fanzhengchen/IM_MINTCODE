package com.mintcode.chat.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.mintcode.launchr.util.TTDensityUtil;

public class BitmapUtil {

	// 保存图片
	public static String saveBitmapAsJpeg(Bitmap bitmap, Context context) {
		if (bitmap == null) {
			return null;
		}
		boolean success = false;
		File dest = null;
		try {

			File file = getAvailableDir(context);
			dest = new File(file, UUID.randomUUID().toString() + ".jpg");
			dest.createNewFile();
			FileOutputStream out = new FileOutputStream(dest);
			success = bitmap.compress(CompressFormat.JPEG, 100, out);

			if (success) {
				File thumb = new File(dest.getAbsolutePath());
				thumb.createNewFile();
				out = new FileOutputStream(thumb);
				success = success
						&& Bitmap.createScaledBitmap(bitmap, 200,
								200 * bitmap.getHeight() / bitmap.getWidth(),
								false).compress(CompressFormat.JPEG, 100, out);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return success ? dest.getAbsolutePath() : null;
	}

	public static File getAvailableDir(Context context) {
		File file = null;
		if (file == null) {
			file = context.getExternalCacheDir();
		}
		if (file == null) {
			file = Environment.getExternalStorageDirectory();
		}
		if (file == null) {
			file = context.getCacheDir();
		}
		return file;
	}

	/**
	 * 根据本地路径获取bitmap
	 */
	public static Bitmap getBitmapByUrl(String url) {
		int degree = readImgDegree(url);
		Bitmap bmp = BitmapFactory.decodeFile(url);
		if (bmp == null) {
			bmp = BitmapFactory.decodeFile(url);
		}
		bmp = rotaingImageView(degree, bmp);
		return bmp;
	}
	
	@SuppressWarnings("null")
	public static byte[] decodeBitmap(String path) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
		BitmapFactory.decodeFile(path, opts);
//		opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);
		opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inDither = false;
		opts.inPurgeable = true;
		opts.inTempStorage = new byte[16 * 1024];
		FileInputStream is = null;
		Bitmap bmp = null;
		Bitmap bmp2 = null ;
		ByteArrayOutputStream baos = null;
		try {
			is = new FileInputStream(path);
			bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
			double scale = getScaling(opts.outWidth * opts.outHeight,
					1024 * 600);
			 bmp2 = Bitmap.createScaledBitmap(bmp,
					(int) (opts.outWidth * scale),
					(int) (opts.outHeight * scale), true);
			bmp.recycle();
			baos = new ByteArrayOutputStream();
			bmp2.compress(CompressFormat.JPEG, 100, baos);
			return baos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				baos.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			System.gc();
		}
		return baos.toByteArray();
	}
	
	private static double getScaling(int src, int des) {
		/**
		 * 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49
		 */
		double scale = Math.sqrt((double) des / (double) src);
		return scale;
	}
	
	/**
	 * 读取图片旋转角度
	 * 
	 * @param path
	 * @return
	 */
	public static int readImgDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * @Method : 图像压缩
	 * @Description :
	 * @auth : RamboCai
	 */

	// Path&&URl
	public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth,
			int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	// 将Uri转为Path
	public static String uri2StringPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	// Resource
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	// 缩放比例计算
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
	
	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	// 保存图片
	public static boolean saveBitmap(Bitmap bitmap,Context context,String path,int quality) {
		if (bitmap == null) {
			return false;
		}
		boolean success = false;
		try {

			File file = new File(path);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			success = bitmap.compress(CompressFormat.JPEG, quality, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}
		return success;
	}

	/** 在消息卡片的背景图片上写字*/
	public static Drawable writeCharBitmap(Context context, String text, int drawable, int color){
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawable).copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas(bitmap);

		Paint paint = new Paint();
		paint.setDither(true);
		paint.setFilterBitmap(true);

		Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		Rect dst = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		canvas.drawBitmap(bitmap, src, dst, paint);

		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
		if(text.length() > 3){
			textPaint.setTextSize(TTDensityUtil.sp2px(context,10));
		}else{
			textPaint.setTextSize(TTDensityUtil.sp2px(context, 22));
		}
		textPaint.setTypeface(Typeface.DEFAULT_BOLD); // 采用默认的宽度
		textPaint.setColor(context.getResources().getColor(color));
		Path path = new Path();
		if(text!=null && text.length()==3){
			path.moveTo(3*bitmap.getWidth() / 10, bitmap.getHeight());
			path.lineTo(bitmap.getWidth(), 3*bitmap.getHeight()/5);
		}else if(text!=null && text.length()==2){
			path.moveTo(2*bitmap.getWidth()/5, 9*bitmap.getHeight()/10);
			path.lineTo(bitmap.getWidth(), 5*bitmap.getHeight()/10);
		}else{
			path.moveTo(2*bitmap.getWidth()/5, 9*bitmap.getHeight()/10);
			path.lineTo(bitmap.getWidth(), 5*bitmap.getHeight()/10);
		}
		textPaint.setStyle(Paint.Style.FILL);
		canvas.drawTextOnPath(text, path, 0, 0, textPaint);

		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return (Drawable)new BitmapDrawable(context.getResources(), bitmap);
	}

	/** 部分手机会将图片旋转*/
	public static void handleTakePhoto(Context context, String path){
		int angle = readPictureDegree(path);
		if(angle == 0){   // 如果角度没变，则不旋转
			return;
		}

		Bitmap bitmap = BitmapFactory.decodeFile(path);
		if(bitmap != null){
			// 创建新的图片
			bitmap = rotaingImage(angle, bitmap);

			BitmapUtil.saveBitmap(bitmap, context, path, 100);
		}
	}

	public static Bitmap rotaingImage(int angle , Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	public static int readPictureDegree(String path) {
		int degree  = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
}