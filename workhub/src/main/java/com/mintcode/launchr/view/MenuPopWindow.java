package com.mintcode.launchr.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.mintcode.launchr.R;


public class MenuPopWindow extends PopupWindow  {
	
	
	private Context mContext;

	
	public MenuPopWindow(Context context){
		this.mContext = context;
	}
	public MenuPopWindow(Context context,View ContentView){
		this.mContext = context;
		setWindow(ContentView);
	}
	
	public MenuPopWindow(Context context,View ContentView,int width){
		this.mContext = context;
		setCustomWindow(ContentView,width);
	}
	

	private void setWindow(View ContentView) {
		// 设置PopupWindow的View
		this.setContentView(ContentView);
		// 设置PopupWindow弹出窗体的宽
		this.setWidth( ViewGroup.LayoutParams.WRAP_CONTENT);
		// 设置PopupWindow弹出窗体的高
		this.setHeight( android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		// 设置PopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置PopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style.popupAnimation);
//		 实例化一个ColorDrawable颜色为透明
//		ColorDrawable dw = new ColorDrawable(0xdfffffff);
//		 设置SelectPicPopupWindow弹出窗体的背景
//		this.setBackgroundDrawable(dw);
		//不加这一句 back键将失效
		this.setBackgroundDrawable(new BitmapDrawable());
	}
	
	private void setCustomWindow(View ContentView,int width) {
		// 设置PopupWindow的View
		this.setContentView(ContentView);
		// 设置PopupWindow弹出窗体的宽
		this.setWidth( width);
		// 设置PopupWindow弹出窗体的高
		this.setHeight( ViewGroup.LayoutParams.WRAP_CONTENT);
		// 设置PopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置PopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style.popupAnimation);
//		 实例化一个ColorDrawable颜色为透明
//		ColorDrawable dw = new ColorDrawable(mContext.getResources().getColor(R.color.white));
		ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
//		 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	}
	
	
	
	
	
	
}
