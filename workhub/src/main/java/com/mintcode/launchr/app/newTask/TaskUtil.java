package com.mintcode.launchr.app.newTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mintcode.chat.util.BitmapUtil;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.util.GlideRoundTransform;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskUtil {

	
	public static final int PROJECT_TYPE = 1;
	
	public static final int TAG_TYPE = 2;
	
	public static final int FILTER_TYPE = 3;
	
	/** type key*/
	public static final String KEY_TYPE = "key_type";
	
	/** 过滤条件key */
	public static final String KEY_FILTER = "key_filter";
	
	/** 过滤条件postion */
	public static final String KEY_FILTER_POSITION = "key_filter_position";
	
	public static final String SHOWTYPE = "showtype";
	
	public static final String STATUS_TYPE = "status_type";
	
	/** 任务 项目 实体key */
	public static final String KEY_TASK_PROJECT = "key_task_project";
	
	/** 项目id */
	public static final String KEY_PROJECT_ID = "key_project_id";
	
	
	/** 白板状态等待 */
	public static final String WATING = "Wating";
	
	/** 白板状态进行中 */
	public static final String IN_PROGRESS = "In_Progress";
	
	/** 白板状态完成 */
	public static final String FINISH = "Finish";
	
	/** 优先级 高*/
	public static final String HIGH_PRIORITY = "HIGH";
	
	/** 优先级 中*/
	public static final String MEDIUM_PRIORITY = "MEDIUM";
	
	/** 优先级 低*/ 
	public static final String LOW_PRIORITY = "LOW";

	/** 优先级 无*/
	public static final String NONE_PRIORITY = "NONE";
	
	/** 任务key */
	public static final String KEY_TASK_ID = "key_task_id";
	
	/** 任务key parent_id */
	public static final String KEY_PARENT_TASK_ID = "key_parent_task_id";
	
	
	public static final String KEY_CHILD_PROJECT_ID = "key_child_project_id";
	
	public static final String KEY_CHILD_PROJECT_NAME = "key_child_project_name";
	
	/** 菜单显示 */
	public static final int SHOW_MENU = 0x10;
	
	public static final int SHOW_MENU_ITEM = 0x11;
	
	public static final int DISMISS_MENU = 0x12;
	
	public static final int UPDATE_STATE = 0x13;
	
//	public static final int 
	
	
	/**
	 * 隐藏键盘
	 * @param context
	 * @param editext
	 */
	public static void hideSoftInputWindow(Context context, EditText editext){
		if (editext.hasFocus()) {
			editext.clearFocus();
			InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editext.getWindowToken(), 0);
		}
	}
	public static String getTimeFormat(String type, long mills){
		SimpleDateFormat sf = new SimpleDateFormat(type);
		Date d = new Date(mills);
		return sf.format(d);
	}

}
