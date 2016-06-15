package com.mintcode.launchr.app.meeting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.util.TTDensityUtil;


public class MySmoothView extends ViewGroup{

	/** item view 宽度 dp*/
	public static final int ITEM_WIDTH = 56;
	
	public static final int ITEM_HEIGHT = 52;
	
	public static final int MARGIN_TOP = 30;
	
	public static final int ITEM_LEN = 25;
	
	// 用来跟踪触摸速度的类
	private VelocityTracker mVelocityTracker;
		
	/** 表格宽度 */
	private int itemWidth;
	
	/** 表格高度 */
	private int itemHeight;
	
	/** 距离高 */
	private int mTopX;
	
	public  int State = 0;
	
	public float xPoint;
	
	public float yPoint;
	
	private float moveX;
	
	
	private int width;
	private int height;
	
	Paint mPaint;
	
	private int mIndex = 0;
	private boolean isRight = false;
	private float all;
	
	private String[] mStrArray = new String[]{"Kevin","Conan","Remon","July","Willam",""};
	
//	private Map<Integer, List<RectF>> map = new HashMap<Integer, List<RectF>>();
	List<RectF> rList; //= new ArrayList<RectF>();
	private Context context;
	private void getPaint(Context context){
		// 获取view宽高
		itemWidth = TTDensityUtil.dip2px(getContext(), ITEM_WIDTH);
		itemHeight = TTDensityUtil.dip2px(getContext(), ITEM_HEIGHT);
		mTopX = TTDensityUtil.dip2px(getContext(), MARGIN_TOP);
		mPaint = new Paint();
		mPaint.setColor(getResources().getColor(R.color.black));
		this.context = context;
	}
	public MySmoothView(Context context) {
		super(context);
		getPaint(context);
	}

	public MySmoothView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getPaint(context);
		
	}

	public MySmoothView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		getPaint(context);
		
	}

	// 
	
	
	

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mLeft = getLeft();
		int currentItem = TTDensityUtil.dip2px(getContext(), ITEM_WIDTH);
		all = mLeft + 24 * currentItem;
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = widthMeasureSpec;
		height = heightMeasureSpec;
	}
	
	
	float mLeft ;
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		rList = new ArrayList<RectF>();
		// 获取整个屏幕的宽度
//		int top = getTop();
		
//		int left = getLeft();
		
		int right = getRight();
		
//		int bottom = getBottom();
		
		mPaint.setTextSize(30);
		
		mPaint.setColor(white);
		RectF rect = new RectF(itemWidth, 0, right, itemHeight * 11 + mTopX);
		
		canvas.drawRect(rect, mPaint);
		mPaint.setColor(black);
		
		
		// 画线
		// 判断是否超过最左边
		if (moveX + allX < 0) {
			int len = ITEM_LEN * itemWidth;
//			len + moveX + allX   > right
			
			if (true) {//生肖
//				Log.i("infos", len + "===" +moveX + "=="+ allX );
				for (int i = 0; i < ITEM_LEN; i++) {
					float w;
					if (moveX + allX > getRight() - len) {
//						Log.i("infos", "===" + w);
					}else{
						allX = getRight() - len;
						moveX= 0;
//						Log.i("index", "===" + w);
					}
					
					w = i * itemWidth + moveX + allX + itemWidth;
					 
					float h = mTopX + (i + 1 ) * itemHeight ;
					float w1 = (i+1)  * itemWidth + moveX + allX  + itemWidth;
					
					float h1 = mTopX + itemHeight * (i + 1);
					Log.i("index", "==kaishi=" + w +"--"+ itemWidth);
					if (moveX + allX < 0) {
//						if (w > itemWidth) {
//							w = w;
//						}else{
//							w = w - itemWidth;
//						}
						if (w < itemWidth) {
							w = itemWidth;
						}else{
							
						}
						
						if (w1 > itemWidth) {
							canvas.drawText(i  + "时", w, mTopX, mPaint);
							canvas.drawLine(w, mTopX, w, 6 * itemHeight + mTopX, mPaint);
							
						}
						
						
						if (i >=23) {
//								Log.i("infos", w + "---900==" + allX  + "--" + moveX);
						}
						
						if (i < 6) {
							String str = mStrArray[i % 6];
							mPaint.setColor(blue);
							canvas.drawText(str, itemWidth - 100, h + 100 , mPaint);//(itemWidth - 100, h, right, h, mPaint);
							mPaint.setColor(black);
							canvas.drawLine(itemWidth, h, right, h, mPaint);
						}
						drawTimeLine(canvas, i, w, mTopX, 6 * itemHeight + mTopX);
						
					}
					
					
					// 画出行程
					if (i == 1) {
//						if (w > itemWidth) {
//							
//						}else{
//							w = itemWidth;
//						}
						if (w1 > itemWidth) {
							float l = w ;
							float t = h1 + itemHeight/4;
							float r = w1;
							float b = h1 + itemHeight/4 * 3;
							RectF re = new RectF(l, t, r, b);
							rList.add(re);
							canvas.drawRect(w , t, r, b, mPaint);
						}
						
//						Log.i("infos", "= 有="  );
					}else if(i == 2){
						if (w1 > itemWidth) {
							float l = w;
							float t = h1 + itemHeight/4;
							float r = w1;
							float b = h1 + itemHeight/4 * 3;
							RectF re = new RectF(l, t, r, b);
							rList.add(re);
							mPaint.setColor(gray);
							canvas.drawRect(w+1, t, r-1, b, mPaint);
							
							mPaint.setColor(black);
							mPaint.setTextSize(30);
							canvas.drawText("会议", (w + r) /2, (t + b) / 2, mPaint);
						}
						
//						mPaint.reset();
					}else if (i == 10) {
//						float l = w;
//						float t = h1 + itemHeight/4;
//						float r = w1;
//						float b = h1 + itemHeight/4 * 3;
//						RectF re = new RectF(l, t, r, b);
//						rList.add(re);
//						canvas.drawRect(w, t, r, b, mPaint);
					}
				}
			}else{
//				 int count = getRight() / itemWidth;
//				 allX =  ITEM_LEN * itemWidth - getRight();
//				 moveX = 0;
//				 Log.i("infos",  "==="  + "==");
//				 Log.i("infos", count + "===" +moveX + "=="+ allX );
//				 for (int i = 0; i < count; i++) {
//						float w = allX - itemWidth* i ;
//						float h = mTopX + (i + 1) * itemHeight ;
//						float w1 = allX - itemWidth* (i + 1) ;
//						float h1 = mTopX + itemHeight * (i + 1);
//						Log.i("infos",  "---"  + w + "----");
//						canvas.drawText(ITEM_LEN - i + "时", w, mTopX, mPaint);
//						canvas.drawLine(w, mTopX, w, 6 * itemHeight + mTopX, mPaint);
//						
//						if (i < 6) {
//							canvas.drawLine(0, h, right, h, mPaint);
//						}
//						
//					}
//						
					}
//				
			
			
		}else{
			allX = 0;
			moveX = 0;
			for (int i = 0; i < ITEM_LEN; i++) {
				float w = (i + 1) * itemWidth ;
				float h = mTopX + (i + 1) * itemHeight ;
				float w1 = (i+1+ 1)  * itemWidth ;
				
				float h1 = mTopX + itemHeight * (i + 1);
				
				canvas.drawText(i + "时", w, mTopX, mPaint);
				canvas.drawLine(w, mTopX, w, 6 * itemHeight + mTopX, mPaint);
				
				if (i < 6) {
					String str = mStrArray[i%6];
					mPaint.setColor(blue);
					canvas.drawText(str, itemWidth - 100, h + 100 , mPaint);//(itemWidth - 100, h, right, h, mPaint);
					mPaint.setColor(black);
					canvas.drawLine(itemWidth, h, right, h, mPaint);
					
				}
				
				
				// 画出行程
				if (i == 1) {
					
					float l = w ;
					float t = h1 + itemHeight/4;
					float r = w1;
					float b = h1 + itemHeight/4 * 3;
					RectF re = new RectF(l, t, r, b);
					rList.add(re);
//					mPaint.setColor(blue);
					canvas.drawRect(w , t, r, b, mPaint);
//					Log.i("infos", "= 有="  );
				}else if(i == 2){
					
					float l = w;
					float t = h1 + itemHeight/4;
					float r = w1;
					float b = h1 + itemHeight/4 * 3;
					RectF re = new RectF(l, t, r, b);
					rList.add(re);
					mPaint.setColor(gray);
					canvas.drawRect(w+1, t, r-1, b, mPaint);
					
					mPaint.setColor(black);
					mPaint.setTextSize(30);
					canvas.drawText("会议", (w + r) /2, (t + b) / 2, mPaint);
//					mPaint.reset();
				}else if (i == 10) {
//					float l = w;
//					float t = h1 + itemHeight/4;
//					float r = w1;
//					float b = h1 + itemHeight/4 * 3;
//					RectF re = new RectF(l, t, r, b);
//					rList.add(re);
//					canvas.drawRect(w, t, r, b, mPaint);
				}
			}
			
			
		}
		
		
		
		
//		mLeft = mLeft + moveX;
		
		
		
//		Canvas c = new 
//		mPaint = new 
		
		
		
	
	}
	
	int black = getResources().getColor(R.color.black);
	int white = getResources().getColor(R.color.white);
	int blue = getResources().getColor(R.color.blue_launchr);
	int gray = getResources().getColor(R.color.light_gray);
	int color = getResources().getColor(R.color.red_launchr);
	private void drawTimeLine(Canvas canvas, int index,float l, float t , float b){
		
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		Paint paint = new Paint();
		paint.setColor(color);
//		paint.setStrokeWidth(20);
		paint.setTextSize(30);
		float m = (float)min/60;
		float wT =  (m * itemWidth);
		
		if (index == hour) {
			Log.i("index", hour + "-"+min + "======wo hua l ===" + wT + "w" + m);
			canvas.drawLine(l + wT, t - 30, l + wT, b, paint);
			canvas.drawText(hour+":"+min, l + wT - 50, t - 50, paint);
			
//			paint.setColor(blue);
//			RectF rs = new RectF(l, t, r, b);
			drawCurrentMeeting(canvas, index, l + wT, t);
		}
		
		
	}
	
	private void drawCurrentMeeting(Canvas canvas,int index,float l, float t ){
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (index == hour) {
			Paint paint = new Paint();
			paint.setColor(blue);
//			float w = index * itemWidth ;
//			float h = mTopX + (3 + 1) * itemHeight ;
			float w1 = (index+1)  * itemWidth ;
			
			float h1 = mTopX + itemHeight * (3 + 1);
			float left = l ;
			float top = h1 + itemHeight/4;
			float right = l + itemWidth;
			float bottom = h1 + itemHeight/4 * 3;
			
			RectF rs = new RectF(left, top, right, bottom);
			Log.i("infos", left + "==" + top + "==" + right +"==" + bottom);
			rList.add(rs);
			canvas.drawRect(rs, paint);
		}
		
	}
	
	float oldX ;
	
	float allX;
	
	int count;
	
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (mVelocityTracker == null) {
			// 使用obtain方法得到VelocityTracker的一个对象
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		showDetails(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			oldX = event.getX();
			count = 0;
			break;
		case MotionEvent.ACTION_UP:
			State = 2;
			allX = allX + event.getX() - oldX;
			
			final VelocityTracker velocityTracker = mVelocityTracker;
			// 计算当前的速度
			velocityTracker.computeCurrentVelocity(4000);
			// 获得当前的速度
			float velocityX =  velocityTracker.getXVelocity();
			
			Log.i("index", velocityX + "-----");
			
			float deltaX = velocityX /10;
			
			smoothRollTo(allX + deltaX, 1000);
//		
			
			
			
			
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			
			moveX = 0;
			break;
		case MotionEvent.ACTION_MOVE:
			State = 1;
			float x = event.getX();
			moveX = x - oldX;
			
			
			int currentItem = TTDensityUtil.dip2px(getContext(), ITEM_WIDTH);
			float w = getLeft() + ITEM_LEN * currentItem ;
			postInvalidate();

			
			
			
			
			break;
			
			
		default:
			break;
		}
		
		return true;
		
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
		
		
	}
	
	float vX;
	float vy;
	private void showDetails(MotionEvent event){
		
		
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			vX = event.getX() ;
			vy = event.getY();
			
			break;
		case MotionEvent.ACTION_UP:
			float x = event.getX();
			float y = event.getY();
			
			// 设置
			if (!rList.isEmpty()) {
				for (RectF re : rList) {
//					Log.i("index", re.left  +"--"+ vX +"--"+ re.right);
					float left = re.left ;//- moveX - allX;
					float right = re.right;// - moveX - allX;
					float top = re.top ;//-moveX - allX;
					float bottom = re.bottom ;//- moveX - allX;
					
					
					if (left  < vX && vX < right ) {
						if (left  < x && x < right ) {
							if (top  < vy && vy < bottom) {
								if (top  < y && y < bottom) {
//									Log.i("infos", "===我点击了==");
//									CustomToast.showToast(getContext(), "吼吼", 1000);
									showDetail(top,bottom);
								}
							}
						}
					}
					
				}
			}
			
			
			
			
			break;
			
		case MotionEvent.ACTION_MOVE:
			
			break;
		default:
			break;
		}
		
		
	}
	SmoothRollRunnable mSmoothRollRunnable;
	protected void smoothRollTo(double process, long duration){
		if(null != mSmoothRollRunnable){
			mSmoothRollRunnable.stop();
		}
		if(allX != process){
			mSmoothRollRunnable = new SmoothRollRunnable(allX, process, duration);
			post(mSmoothRollRunnable);
		}
	}
	class SmoothRollRunnable implements Runnable {
		static final int ANIMATION_DELAY = 10;
		
		private final Interpolator mInterpolator;
		private final double mFrom;
		private final double mTo;
		private final long mDuration;
		
		private boolean mContinueRunning = true;
		private long mStartTime = -1;
		
		public SmoothRollRunnable(double from, double to, long duration){
			mFrom = from;
			mTo = to;
			mInterpolator = new DecelerateInterpolator();
			mDuration = duration;
		}
		@Override
		public void run() {
			
			if (mStartTime == -1) {
				mStartTime = System.currentTimeMillis();
			} else {
				long duration = mDuration;
				if(duration == 0){
					duration = 1;
				}
				long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime)) / duration;
				normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

				final double deltaY = (mFrom - mTo)
						* mInterpolator.getInterpolation(normalizedTime / 1000f);
				Log.d("", "mInterpolator.getInterpolation(normalizedTime / 1000f))="+(mInterpolator.getInterpolation(normalizedTime / 1000f))+",deltaY="+deltaY);
				allX = (float) (mFrom - deltaY);
				//todo:数值变化后，你要做什么？
//				setTextWithHead(mHead, mProgressValue);
				//
				invalidate();
			}
			if (mContinueRunning && mTo != allX) {
				postDelayed(this, ANIMATION_DELAY);
			}
		}
		
		public double stop() {
			mContinueRunning = false;
			removeCallbacks(this);
//			mProgressValue = mTo;
//			invalidate();
			return allX;
		}
	}
	
	private void showDetail(float old, float y){
		Dialog dialog  = new Dialog(context, R.style.my_dialog);
		dialog.show();
		Window window = dialog.getWindow();
		dialog.getWindow().setContentView(R.layout.dialog_show_meeting_sc);
		TextView tv = (TextView) window.findViewById(R.id.tv_name);
//		int h
		
		for (int i = 0; i < 6; i++) {
			int h = itemHeight * i;
			
			Log.i("infos", old + "==" + h + "==" + y);
			if (old < h && h < y ) {
				String str = mStrArray[i-2];
				tv.setText(str + " " + context.getString(R.string.who_schedule));
			}
			
		}
		
		
		
		
	}
}
