package org.litepal;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.litepal.exceptions.ParseConfigurationFileException;
import org.litepal.util.Const;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class TableListWindow extends PopupWindow implements OnItemClickListener {

	private ListView mListView;

	private List<String> mList;

	private TableListAdapter mAdapter;
	
	private Context mContext;

	public TableListWindow(Context context) {
		LitePalApplication.initialize(context);
		mContext = context;
		mListView = new ListView(context);
		mList = new ArrayList<String>();
		populateMappingClasses();
		mAdapter = new TableListAdapter(context, 0, mList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		setContentView(mListView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(Utility.dp2px(context, 300));
		setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x000000);
		setBackgroundDrawable(dw);
	}

	
	private void populateMappingClasses() {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = factory.newPullParser();
			xmlPullParser.setInput(getInputStream(), "UTF-8");
			int eventType = xmlPullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String nodeName = xmlPullParser.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG: {
					if ("mapping".equals(nodeName)) {
						String className = xmlPullParser.getAttributeValue("", "class");
						mList.add(className);
					}
					break;
				}
				default:
					break;
				}
				eventType = xmlPullParser.next();
			}
		} catch (XmlPullParserException e) {
			throw new ParseConfigurationFileException(
					ParseConfigurationFileException.FILE_FORMAT_IS_NOT_CORRECT);
		} catch (IOException e) {
			throw new ParseConfigurationFileException(ParseConfigurationFileException.IO_EXCEPTION);
		}
	}

	private InputStream getInputStream() throws IOException {
		AssetManager assetManager = LitePalApplication.getContext().getAssets();
		String[] fileNames = assetManager.list("");
		if (fileNames != null && fileNames.length > 0) {
			for (String fileName : fileNames) {
				if (Const.LitePal.CONFIGURATION_FILE_NAME.equalsIgnoreCase(fileName)) {
					return assetManager.open(fileName, AssetManager.ACCESS_BUFFER);
				}
			}
		}
		throw new ParseConfigurationFileException(
				ParseConfigurationFileException.CAN_NOT_FIND_LITEPAL_FILE);
	}

	class TableListAdapter extends ArrayAdapter<String> {

		public TableListAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView;
			if (convertView == null) {
				textView = new TextView(getContext());
				textView.setTextSize(18);
				textView.setGravity(Gravity.CENTER);
				LayoutParams params = new LayoutParams(
						LayoutParams.MATCH_PARENT, Utility.dp2px(getContext(),
								50));
				textView.setLayoutParams(params);
			} else {
				textView = (TextView) convertView;
			}
			textView.setText(getItem(position));
			return textView;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		dismiss();
		String table = mList.get(position);
		Class<?> forName;
		try {
			forName = Class.forName(table);
			new TableWindow(mContext, forName).showAtLocation(mListView, Gravity.CENTER, 0, 0);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
		}
	}
	
	public void show(View v){
		showAtLocation(v, Gravity.CENTER, 0, 0);
	}
}
