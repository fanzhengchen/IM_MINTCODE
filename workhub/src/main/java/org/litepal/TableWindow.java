package org.litepal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.AbsListView.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

public class TableWindow extends PopupWindow {

	private ListView mListView;
	private ListView mLvTitle;
	private LinearLayout mLinearLayout;
	private List<List<String>> mList;
	private List<List<String>> mListTitle;
	private DataArrayAdapter mAdapter;
	private DataArrayAdapter mAdapterTitle;
	private HorizontalScrollView mContent;

	public TableWindow(Context context ,Class<?> from) {
		mContent = new HorizontalScrollView(context);
		mLvTitle = new ListView(context);
		mListView = new ListView(context);
		mLinearLayout = new LinearLayout(context);
		mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		mLinearLayout.addView(mLvTitle);
		mLinearLayout.addView(mListView);
		mList = new ArrayList<List<String>>();
		mListTitle = new ArrayList<List<String>>();
		mAdapter = new DataArrayAdapter(context, 0, mList);
		mAdapterTitle = new DataArrayAdapter(context, 0, mListTitle);
		mListView.setAdapter(mAdapter);
		mLvTitle.setAdapter(mAdapterTitle);
		mContent.addView(mLinearLayout);
		mContent.setPadding(Utility.dp2px(context, 15), 0, 0, 0);
		populateDataFromDB(from);
		setContentView(mContent);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(Utility.dp2px(context, 300));
		setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		setBackgroundDrawable(dw);
	}

	private void populateDataFromDB(Class<?> fromClass) {
		mList.clear();
		// 获取对象的属性列表
		Field[] fromFields = fromClass.getDeclaredFields();
		// 获取列名
		List<String> columnList = new ArrayList<String>();
		for (int i = 0; i < fromFields.length; i++) {
			Field field = fromFields[i];
			columnList.add(field.getName());
		}
		// 将列名添加到第一行
		mListTitle.add(columnList);
		// 从数据库获取数据
		List<?> list = DataSupport.findAll(fromClass);
		for (Object object : list) {
			List<String> stringList = new ArrayList<String>();
			for (int i = 0; i < fromFields.length; i++) {
				// 将各个字段里面的数据取出
				Field field = fromFields[i];
				try {
					field.setAccessible(true);
					stringList.add(field.get(object) + "");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 将数据加入到列表
			mList.add(stringList);
		}
		mAdapter.notifyDataSetChanged();
		mAdapterTitle.notifyDataSetChanged();
	}
}
