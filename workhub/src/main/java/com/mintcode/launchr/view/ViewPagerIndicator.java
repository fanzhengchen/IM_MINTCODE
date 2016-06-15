package com.mintcode.launchr.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintcode.launchr.R;
import com.mintcode.launchr.util.TTDensityUtil;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerIndicator extends LinearLayout
{
	/**
	 * 绘制三角形的画笔
	 */
	private Paint mPaint;


	/**
	 * 默认的Tab数量
	 */
	private static final int COUNT_DEFAULT_TAB = 3;
	/**
	 * tab数量
	 */
	private int mTabVisibleCount = COUNT_DEFAULT_TAB;

	/**
	 * tab上的内容
	 */
	private List<String> mTabTitles;
	/**
	 * 与之绑定的ViewPager
	 */
//	public ViewPager mViewPager;

	/**
	 * 标题正常时的颜色
	 */
	private static  int COLOR_TEXT_NORMAL = 0xafafaf;
	/**
	 * 标题选中时的颜色
	 */
	private static  int COLOR_TEXT_HIGHLIGHTCOLOR = 0x000000;


	private Context mContext;

	private List<TextView> mLvText;

	public ViewPagerIndicator(Context context)
	{
		this(context, null);
		mContext = context;
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		if (mTabVisibleCount < 0)
			mTabVisibleCount = COUNT_DEFAULT_TAB;
		COLOR_TEXT_NORMAL = mContext.getResources().getColor(R.color.gray);
		COLOR_TEXT_HIGHLIGHTCOLOR = mContext.getResources().getColor(R.color.black);
		// 初始化画笔
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(getResources().getColor(R.color.blue));
		mPaint.setStyle(Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));

	}


	/**
	 * 设置可见的tab的数量
	 * 
	 * @param count
	 */
	public void setVisibleTabCount(int count)
	{
		this.mTabVisibleCount = count;
	}

	/**
	 * 设置tab的标题内容 可选，可以自己在布局文件中写死
	 * 
	 * @param datas
	 */
	public void setTabItemTitles(List<String> datas)
	{
		// 如果传入的list有值，则移除布局文件中设置的view
		if (datas != null && datas.size() > 0)
		{
			this.removeAllViews();
			this.mTabTitles = datas;
			mLvText = new ArrayList<>();
			for (String title : mTabTitles)
			{
				// 添加view
				addView(generateTextView(title));
			}
			((IndicatorTextView)mLvText.get(0)).freshLine(true);
			// 设置item的click事件
			setItemClickEvent();
		}
		resetTextViewColor();
		highLightTextView(0);
	}
	
	

	/**
	 * 对外的ViewPager的回调接口
	 * 
	 * 
	 */
	public interface MessagePageChangeListener
	{

		public void onPageSelected(int position);

	}

	// 对外的ViewPager的回调接口
	private MessagePageChangeListener onPageChangeListener;

//	// 对外的ViewPager的回调接口的设置
	public void setOnPageChangeListener(MessagePageChangeListener pageChangeListener)
	{
		this.onPageChangeListener = pageChangeListener;
	}


	/**
	 * 高亮文本
	 * 
	 * @param position
	 */
	public void highLightTextView(int position)
	{
		View view = getChildAt(position);
		if (view instanceof TextView)
		{

			((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHTCOLOR);
		}
		int num = 0;
		for(TextView TvView : mLvText){
			if(num == position){
				if(mLvText.size() >= position + 1){
					((IndicatorTextView)TvView).freshLine(true);
				}
			}else{
				((IndicatorTextView)TvView).freshLine(false);
			}
			num ++;
		}
	}

	/**
	 * 重置文本颜色
	 */
	private void resetTextViewColor()
	{
		for (int i = 0; i < getChildCount(); i++)
		{
			View view = getChildAt(i);
			if (view instanceof TextView)
			{
				((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
			}
		}
	}

	/**
	 * 设置点击事件
	 */
	public void setItemClickEvent()
	{
		int cCount = getChildCount();
		for (int i = 0; i < cCount; i++)
		{
			final int j = i;
			View view = getChildAt(i);
			view.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					scroll(j,0);
					onPageChangeListener.onPageSelected(j);
				}
			});
		}
	}

	/**
	 * 根据标题生成我们的TextView
	 * 
	 * @param text
	 * @return
	 */
	private TextView generateTextView(String text)
	{

		IndicatorTextView tv = new IndicatorTextView(getContext());
		LayoutParams lp = new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		
//		修改处  lp.width = getScreenWidth() / mTabVisibleCount;
		int padding = TTDensityUtil.dip2px(mContext,10);
		tv.setPadding(padding, 0, padding,padding);
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(COLOR_TEXT_HIGHLIGHTCOLOR);
		tv.setText(text);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		tv.setLayoutParams(lp);
		mLvText.add(tv);
		return tv;
	}


	
	/**
	 * 指示器跟随手指滚动，以及容器滚动
	 * 
	 * @param position
	 * @param offset
	 */
	public void scroll(int position, float offset)
	{
		/**
		 * <pre>
		 *  0-1:position=0 ;1-0:postion=0;
		 * </pre>
		 */

		resetTextViewColor();
		highLightTextView(position);
	}

	/**
	 * 设置布局中view的一些必要属性；如果设置了setTabTitles，布局中view则无效
	 */
	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();

		int cCount = getChildCount();

		if (cCount == 0)
			return;

		for (int i = 0; i < cCount; i++)
		{
			View view = getChildAt(i);
			LayoutParams lp = (LayoutParams) view
					.getLayoutParams();
			lp.weight = 0;
			lp.width = getScreenWidth() / mTabVisibleCount;
			view.setLayoutParams(lp);
		}
		// 设置点击事件
		setItemClickEvent();

	}

	/**
	 * 获得屏幕的宽度
	 * 
	 * @return
	 */
	public int getScreenWidth()
	{
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

}
