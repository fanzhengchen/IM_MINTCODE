package com.mintcode.launchr.app.newSchedule.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mintcode.launchr.R;
import com.mintcode.launchr.pojo.entity.ScheduleEntity.Times;
import com.mintcode.launchr.timer.Timer;
import com.mintcode.launchr.timer.listener.OnTimerListener;
import com.mintcode.launchr.util.Const;

public class NewTimeSelectDialog extends DialogFragment implements OnClickListener,OnTimerListener{
	/**
	 * 根view
	 */
	private View mView;

	private TextView mTvCancel;
	/**
	 * 完成及关闭dialog
	 */
	private TextView mIvFinish;
	/**
	 * 放置时间选择器外部布局
	 */
	private LinearLayout mLlContentView;
	/**
	 * 默认时间选择器1的可缩头
	 */
	private RelativeLayout mRlDefulatTimeSelectShrink;
	/**
	 * 默认时间选择器1的Textview
	 */
	private TextView mTvTimeTitle;
	/**
	 * 默认时间选择器1的时间显示
	 */
	private TextView mTvGetTime;
	/**
	 * 默认时间选择器1的可缩头按钮
	 */
	private ImageButton mIvTimeIcon;
	/**
	 * 默认时间选择器1的时间选择
	 */
	private Timer mTimer;
	/**
	 * 添加时间器的布局
	 */
	private RelativeLayout mRlAddTimer;
	/**
	 * 复制时间
	 */
	private TextView mTvCopy;
	/**
	 * 时间选择器List
	 */
	private LinkedList<Timer> mLinkTimerList;
	/**
	 * 时间选择器删除List
	 */
	private LinkedList<ImageButton> mLinkTimeIconList;
	/**
	 * 时间收起List
	 */
	private LinkedList<RelativeLayout> mLinkTimeSelectShrinkList;
	/**
	 * 时间选择器全部布局List
	 */
	private LinkedList<CandidateTimeSelectView> mLinkTimeSelectList;
	/**
	 * 标题头List
	 */
	private LinkedList<TextView> mLinkTimeTitle;
	/**
	 * 时间显示List
	 */
	private LinkedList<TextView> mLinkGetTime;

	private ArrayList<Long> mTimersList  = new ArrayList<Long>();

	private ColorStateList mBlack;
	private ColorStateList mGray;
	//接口监听
	private OnTimeDialogListener mTimeDialogListener;
	/**
	 * 添加的时间选择器个数
	 */
	private int mSum = 0;

	private boolean mIsAllDay;

	private CheckBox mCbAllDay;

	private final int[] mTimeName = { R.string.calendar_timepicker_date1,R.string.calendar_timepicker_date2,R.string.calendar_timepicker_date3};

	private long mTodayTime = 0;

	public static final String TODAT_TIME = "todat_time";
	public static final String TIME_List = "time_list";


	public NewTimeSelectDialog(){

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.dialog_add_time,container,false);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

		initView();

		//设置是否为全天事件
		// 开始时间标志重新设置
		mTimer.setStyle(mIsAllDay == true ? Timer.ALL_DAY_TIME : Timer.FIVE_POSITON);

		return mView;
	}

	private void initView() {
		//实例化LinkList
		mLinkTimeTitle = new LinkedList<TextView>();
		mLinkGetTime = new LinkedList<TextView>();
		mLinkTimerList = new LinkedList<Timer>();
		mLinkTimeIconList = new LinkedList<ImageButton>();
		mLinkTimeSelectShrinkList = new LinkedList<RelativeLayout>();
		mLinkTimeSelectList = new LinkedList<CandidateTimeSelectView>();

		//实例化 title所需颜色
		Resources resource = getActivity().getResources();
		mGray = resource.getColorStateList(R.color.gray_launchr);
		mBlack = resource.getColorStateList(R.color.black);

		//dialog
		mLlContentView = (LinearLayout) mView.findViewById(R.id.content_view);
		mTvCancel = (TextView)mView.findViewById(R.id.iv_schedule_time_selelct_canel);
		mIvFinish = (TextView)mView.findViewById(R.id.iv_schedule_time_selelct_finish);
		mRlAddTimer = (RelativeLayout)mView.findViewById(R.id.rl_schedule_time_add);
		mTvCopy = (TextView)mView.findViewById(R.id.tv_schedule_dialog_copy);

		//默认时间选择器1
		mTvTimeTitle = (TextView)mView.findViewById(R.id.tv_schedule_time_select_title);
		mTvGetTime = (TextView)mView.findViewById(R.id.tv_schedule_get_time);
		mRlDefulatTimeSelectShrink = (RelativeLayout)mView.findViewById(R.id.rl_schedule_time_select_shrink);
		mTimer = (Timer)mView.findViewById(R.id.timer);
		mIvTimeIcon = (ImageButton)mView.findViewById(R.id.ibn_schedule_time_select_icon);
		mCbAllDay = (CheckBox) mView.findViewById(R.id.cb_is_allday);
		mCbAllDay.setOnCheckedChangeListener(onCheckListener);

		//设置监听器
		mTvCancel.setOnClickListener(this);
		mIvFinish.setOnClickListener(this);
		mRlDefulatTimeSelectShrink.setOnClickListener(this);
		mRlAddTimer.setOnClickListener(this);
		mTvCopy.setOnClickListener(this);
		mTimer.setOnTimerListener(this);


		mTimer.setConflictViewDisplay(false);
		mTimer.setTimeBeforeNow(false);
		if(mTodayTime != 0){
			mTimer.setBeginTime(mTodayTime);
		}
		getArrayList();
		//初始化设置默认时间选择器下拉标识和时间 	不显示
		mIvTimeIcon.setVisibility(View.INVISIBLE);

		//初始化向LinkList添加默认时间选择器的各个控件
		mLinkTimeTitle.add(mTvTimeTitle);
		mLinkGetTime.add(mTvGetTime);
		mLinkTimerList.add(mTimer);
		mLinkTimeIconList.add(mIvTimeIcon);
		mLinkTimeSelectShrinkList.add(mRlDefulatTimeSelectShrink);

		//如果List不为空则加载数据
		if(mTimersList.size() > 0){
			int rsum = mTimersList.size() / 2;
			mTimer.setShowTime(mTimersList.get(0), mTimersList.get(1));
			mTimer.setStyle(mIsAllDay == true ? Timer.ALL_DAY_TIME:Timer.FIVE_POSITON);
			for(int i = 1;i < rsum;i++){
				addContent(true);
				ShrinkIt(true);
			}
		}else{//list为空 时间选择器未做操作 获取时间初始值
			if(mTodayTime != 0){
				mTimersList.add(mTodayTime);
				mTimersList.add(mTodayTime + 3600000);
			}else{
				mTimersList.add(mTimer.getBeginTime());
				mTimersList.add(mTimer.getEndTime());
			}
		}

		// 设置全天选择
		mCbAllDay.setChecked(mIsAllDay);

	}
	/**
	 * 获取activity传递的 list
	 */
	private void getArrayList(){

		Bundle bundle = getArguments();
		long time = bundle.getLong(TODAT_TIME);
		if(time> 0 && Long.valueOf(time) != null){
			mTodayTime = bundle.getLong(TODAT_TIME);
		}
		if(bundle.getSerializable(TIME_List) != null ){
			ArrayList<Long> timeList = (ArrayList<Long>) bundle.getSerializable(TIME_List);
			if(timeList.size()  > 0){
				mTimersList.clear();
				mTimersList.addAll(timeList);
//				mTvGetTime.setText(gettime(mTimersList,0,mIsAllDay));
			}
		}

	}

	/**
	 * 添加一组时间控件
	 * @param
	 */
	private void addContent(boolean flag) {
		//实例化一个时间控件
		CandidateTimeSelectView candidateTimeSelectView = new CandidateTimeSelectView(getActivity(),mTodayTime, null);
		//设置监听
		candidateTimeSelectView.getTimeIcon().setOnClickListener(this);
		candidateTimeSelectView.getTimeSelectShrink().setOnClickListener(this);
		candidateTimeSelectView.getTimer().setOnTimerListener(this);

		//向LinkList添加控件
		mLinkTimeTitle.add(candidateTimeSelectView.getTimeTitle());
		mLinkGetTime.add(candidateTimeSelectView.getTime());
		mLinkTimerList.add(candidateTimeSelectView.getTimer());
		mLinkTimeIconList.add(candidateTimeSelectView.getTimeIcon());
		mLinkTimeSelectShrinkList.add(candidateTimeSelectView.getTimeSelectShrink());
		mLinkTimeSelectList.add(candidateTimeSelectView);
		//判断添加第几个时间控件设置 title名称
		mTvTimeTitle.setText(R.string.calendar_timepicker_date1);
		if(mSum == 0){
			candidateTimeSelectView.getTimeTitle().setText(R.string.calendar_timepicker_date2);
		}else{
			candidateTimeSelectView.getTimeTitle().setText(R.string.calendar_timepicker_date3);
		}
		//向LinkList添加整个时间控件
		mLlContentView.addView(candidateTimeSelectView);

		mSum++;
		//隐藏添加的布局
		if(mSum == 2){
			mRlAddTimer.setVisibility(View.GONE);
		}
		//如果ArrayList中有数据则获取数据
		if(flag == true){
			candidateTimeSelectView.getTimer().setShowTime(mTimersList.get((mSum+1) * 2 - 2), mTimersList.get((mSum+1) * 2 - 1));
		}
		else{//未做操作 获取时间初始值
			mTimersList.add(candidateTimeSelectView.getTimer().getBeginTime());
			mTimersList.add(candidateTimeSelectView.getTimer().getEndTime());
		}
		candidateTimeSelectView.getTimer().setStyle(mIsAllDay == true ? Timer.ALL_DAY_TIME:Timer.FIVE_POSITON);
	}
	/**
	 * 张开时间控件
	 * @param v
	 */
	private void ShrinkIt(View v){
		if (v == null) {
			return;
		}
		//循环LinkTimeSelectShrinkList 判断哪个时间控件张开
		for(int i = 0;i < mLinkTimeSelectShrinkList.size();i++){
			if(mLinkTimeSelectShrinkList.get(i) != v){//未控件设置状态
				mLinkTimerList.get(i).setVisibility(View.GONE);
				mLinkTimeTitle.get(i).setTextColor(mGray);
				mLinkTimeIconList.get(i).setClickable(false);
				mLinkTimeIconList.get(i).setImageResource(R.drawable.icon_drop_down);
			}else{//所选控件张开并设置状态
				mLinkTimerList.get(i).setVisibility(View.VISIBLE);
				mLinkTimeTitle.get(i).setTextColor(mBlack);
				mLinkTimeIconList.get(i).setClickable(true);
				mLinkTimeIconList.get(i).setImageResource(R.drawable.icon_trash);
			}
		}
		//默认时间控件设置状态
		mIvTimeIcon.setVisibility(View.VISIBLE);
		mIvTimeIcon.setImageResource(R.drawable.icon_drop_down);
	}
	/**
	 * 张开时间控件2
	 * @param
	 */
	private void ShrinkIt(boolean flag){
		for(int i = 0;i < mLinkTimeSelectShrinkList.size();i++){
				mLinkTimerList.get(i).setVisibility(View.GONE);
				mLinkTimeTitle.get(i).setTextColor(mGray);
				mLinkTimeIconList.get(i).setClickable(false);
				mLinkTimeIconList.get(i).setImageResource(R.drawable.icon_drop_down);
				if(flag){
					mLinkTimeIconList.get(i).setVisibility(View.VISIBLE);
				}
			}
				mLinkTimerList.getLast().setVisibility(View.VISIBLE);
				mLinkTimeTitle.getLast().setTextColor(mBlack);
				mLinkTimeIconList.getLast().setClickable(true);
				mLinkTimeIconList.getLast().setImageResource(R.drawable.icon_trash);
	}
	/**
	 * 时间选择器收起 显示日期
	 * @param index
	 * @return
	 */
	public static String gettime(ArrayList<Long> time ,int index, boolean flag){
		//获取开始和结束时间 毫秒值
		Long lBeginTime = time.get((index + 1) * 2 - 2);
		Long lOverTime = time.get((index + 1) * 2 - 1);
		if(lOverTime <=  lBeginTime && flag == false){
			lOverTime = lBeginTime + 3600000l;
			time.set((index + 1) * 2 - 2, lBeginTime);
			time.set((index + 1) * 2 - 1, lOverTime);
		}else if(lOverTime <=  lBeginTime && flag == true){
			lOverTime = lBeginTime;
			time.set((index + 1) * 2 - 2, lBeginTime);
			time.set((index + 1) * 2 - 1, lOverTime);
		}
		SimpleDateFormat monthFormat = new SimpleDateFormat("M",Const.getLocale());
		SimpleDateFormat dayFormat = new SimpleDateFormat("d",Const.getLocale());
		//获取开始月日和结束月日
		String BeginMonth = monthFormat.format(lBeginTime);
		String BeginDay = dayFormat.format(lBeginTime);
		String OverMonth = monthFormat.format(lOverTime);
		String OverDay = dayFormat.format(lOverTime);
		SimpleDateFormat AllDayFormat = new SimpleDateFormat("M/d(E)",Const.getLocale());
		SimpleDateFormat OverOneDayFormat = new SimpleDateFormat("M/d(E) HH:mm",Const.getLocale());
		SimpleDateFormat OneDayFormat = new SimpleDateFormat("HH:mm",Const.getLocale());
		if(flag == true ){
			String sBeginTime  = AllDayFormat.format(lBeginTime);
			String sOverTime = AllDayFormat.format(lOverTime);
			return sBeginTime + "~" + sOverTime;
		}
		//设置时间显示格式 一天以上 或 一天
		if((Integer.valueOf(OverMonth)-Integer.valueOf(BeginMonth)!=0)||
				(Integer.valueOf(OverDay)-Integer.valueOf(BeginDay)!= 0)){
			String sBeginTime  = OverOneDayFormat.format(lBeginTime);
			String sOverTime  = OverOneDayFormat.format(lOverTime);
			return sBeginTime + "~" + sOverTime;
		}else{
			String sBeginTime  = OverOneDayFormat.format(lBeginTime);
			String sOverTime = OneDayFormat.format(lOverTime);
			return sBeginTime + "~" + sOverTime;
		}
	}
	/**
	 * 删除时间组件
	 * @param v
	 */
	private void delectContent(View v){
		if (v == null) {
			return;
		}
		//循环mLinkTimeIconList 判断哪个时间控件删除
		for(int i = 0;i < mLinkTimeIconList.size();i++){
			if(mLinkTimeIconList.get(i) == v){
				Log.i("timers", i + "");
				removeSelectLinkList(i);
				break;
			}
		}
		mSum--;
		if(mSum == 0){
			mTvTimeTitle.setText(R.string.calendar_schedule_time);
		}
		//显示添加的布局
		mRlAddTimer.setVisibility(View.VISIBLE);
	}
	/**
	 * 移除LinkList中所要删除的时间器内容并展开上一个时间器
	 * @param index
	 */
	private void removeSelectLinkList(int index){
		//从LinkList中移除
		mLinkTimeTitle.remove(index);
		mLinkGetTime.remove(index);
		mLinkTimerList.remove(index);
		mLinkTimeIconList.remove(index);
		mLinkTimeSelectShrinkList.remove(index);
		mLinkTimeSelectList.get(index - 1).removeAllViews();
		mLinkTimeSelectList.remove(index - 1);
		mTimersList.remove((index+1) * 2 - 1);
		mTimersList.remove((index+1) * 2 - 2);
		//张开上一个控件
		mLinkTimerList.get(index - 1).setVisibility(View.VISIBLE);
		mLinkTimeTitle.get(index - 1).setTextColor(mBlack);
//		mLinkGetTime.get(index - 1).setVisibility(View.INVISIBLE);
		//若上一个控件为默认时间选择器需做判断
		if(index -1 == 0){
			mLinkTimeIconList.get(index - 1).setVisibility(View.INVISIBLE);
		}else{
			mLinkTimeIconList.get(index - 1).setImageResource(R.drawable.icon_trash);
			mLinkTimeIconList.get(index - 1).setClickable(true);
		}
		//刷新标题
		updateTitle();
	}
	/**
	 * 刷新时间选择器标题头
	 */
	private void updateTitle(){
		int[] Title = {R.string.calendar_timepicker_date1,R.string.calendar_timepicker_date2,R.string.calendar_timepicker_date3};
		for(int i = 0;i < mLinkTimeTitle.size();i++){
			mLinkTimeTitle.get(i).setText(Title[i]);
		}
	}
	/**
	 * 字符串转long格式 获取时间毫秒值
	 * @param
	 * @return
	 */
	public long changTime2Long(String time) {
		long result = 0;
		SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd HH:mm",Const.getLocale());
		try {
			result = Format.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 设置监听接口
	 * @param listener
	 */
	public void setOnTimeDialogListener(OnTimeDialogListener listener){
		this.mTimeDialogListener = listener;
	}
	/**
	 * dialog接口 获取时间数据list
	 */
	public interface OnTimeDialogListener{
		void getTimeArrayList(ArrayList<Long> time, boolean flag);
	}
	/**
	 * 获取已有时间list
	 * @param list
	 */
	public void setList(ArrayList<Long> list){
		if(list != null && list.size() > 0){
			this.mTimersList.clear();
			this.mTimersList.addAll(list);
		}
	}
	/**
	 * 判断是否为全天事件
	 * @param flag
	 */
	public void getIsAllDay(boolean flag){
		this.mIsAllDay = flag;
	}
	/**
	 * 复制时间
	 */
	@SuppressLint("NewApi")
	private void copyTimeData(){
		ClipboardManager cmb = (ClipboardManager)getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
		String getStringTimes = "";
		for(int i = 0;i < mLinkTimerList.size();i++){
			getStringTimes += getText((mTimeName[i])) + " " + gettime(mTimersList,i,mIsAllDay) + "\n";
		}
		cmb.setText(getStringTimes);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//添加新时间选择器
		case R.id.rl_schedule_time_add:
			if(mSum<2){
				addContent(false);
				ShrinkIt(false);
				mIvTimeIcon.setVisibility(View.VISIBLE);
				mIvTimeIcon.setImageResource(R.drawable.icon_drop_down);
			}
			break;
		//选择默认第一个事件选择器
		case R.id.rl_schedule_time_select_shrink:
			ShrinkIt(v);
			mIvTimeIcon.setVisibility(View.INVISIBLE);
			break;
		// 完成时间选择
		case R.id.iv_schedule_time_selelct_finish:
			mSum = 0;
			mTimeDialogListener.getTimeArrayList(mTimersList,mIsAllDay);
			this.dismiss();
			break;
		case R.id.iv_schedule_time_selelct_canel:
			this.dismiss();
			break;
		//删除时间选择器
		case R.id.ibn_schedule_time_select_delect:
			delectContent(v);
			break;
		//张开选择的时间选择器
		case R.id.rl_schedule_time_select_null:
			ShrinkIt(v);
			break;
		//复制时间
		case R.id.tv_schedule_dialog_copy:
			copyTimeData();
			Toast.makeText(getActivity(), getString(R.string.calendar_timelist_copy), Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	private CompoundButton.OnCheckedChangeListener onCheckListener = new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked){
				if(mLinkTimerList!=null && mLinkTimerList.size()>0){
					for(int i=0; i<mLinkTimerList.size(); i++){
						mLinkTimerList.get(i).setStyle(Timer.ALL_DAY_TIME);
					}
				}
				mTimer.setStyle(Timer.ALL_DAY_TIME);
			}else{
				if(mLinkTimerList!=null && mLinkTimerList.size()>0){
					for(int i=0; i<mLinkTimerList.size(); i++){
						mLinkTimerList.get(i).setStyle(Timer.FIVE_POSITON);
					}
				}
				mTimer.setStyle(Timer.FIVE_POSITON);
			}
			mIsAllDay = isChecked;
			for(int i = 0;i < mLinkTimeSelectShrinkList.size();i++) {
				mLinkGetTime.get(i).setText(gettime(mTimersList, i, mIsAllDay));
			}
		}
	};

	@Override
	public void OnTimeChangeListenner(View view, int year, int month, int day,
			int hour, int minute, int type, boolean isEnd) {
		String sDate = year+ "-" + month + "-" + day + " " + hour + ":" + minute;
		Long lDate = changTime2Long(sDate);
		for(int i = 0;i < mLinkTimerList.size();i++){//遍历时间选择器
			if(mLinkTimerList.get(i) == view){//获取当前操作的时间选择器
				if(isEnd == false){
					//若开始时间大于结束时间 ，结束时间设置为比开始时间多一个小时
					if (lDate >= mTimer.getEndTime() && type == Timer.FIVE_POSITON) {
						mTimersList.set((i+1) * 2 - 1,lDate + 3600000l );
						mTimersList.set((i+1) * 2 - 2,lDate);
					}else{
						mTimersList.set((i+1) * 2 - 2,lDate);
					}
				}else{
					//若结束时间小于开始时间 ，开始时间设置为比结束时间少一个小时
					if (lDate <= mTimer.getBeginTime() && type == Timer.FIVE_POSITON) {
						mTimersList.set((i+1) * 2 - 1,lDate);
						mTimersList.set((i+1) * 2 - 2,lDate - 3600000l);
					}else{
						mTimersList.set((i+1) * 2 - 1,lDate);
					}

				}
			}
			mLinkGetTime.get(i).setText(gettime(mTimersList,i,mIsAllDay));
		}
	}
	public static String gettime(List<Times> mTimeList, int index,boolean flag) {
		//获取开始和结束时间 毫秒值

				Long lBeginTime = mTimeList.get(index).getStart();
				Long lOverTime = mTimeList.get(index).getEnd();
				Calendar beginTime = Calendar.getInstance();
				Calendar endTime = Calendar.getInstance();
				beginTime.setTimeInMillis(lBeginTime);
				endTime.setTimeInMillis(lOverTime);
				int mIntBeginYear = beginTime.get(Calendar.YEAR);
				int mIntBeginMonth = beginTime.get(Calendar.MONTH);
				int mIntBeginDay = beginTime.get(Calendar.DAY_OF_MONTH);
				int mIntEndYear = endTime.get(Calendar.YEAR);
				int mIntEndMonth = endTime.get(Calendar.MONTH);
				int mIntEndDay = endTime.get(Calendar.DAY_OF_MONTH);

				SimpleDateFormat AllDayFormat = new SimpleDateFormat("M/dd(E)",Const.getLocale());
				SimpleDateFormat OverOneDayFormat = new SimpleDateFormat("M/d(E) HH:mm",Const.getLocale());
				SimpleDateFormat OneDayFormat = new SimpleDateFormat("HH:mm",Const.getLocale());


				if(flag == true ){
					String sBeginTime  = AllDayFormat.format(lBeginTime);
					String sOverTime = AllDayFormat.format(lOverTime);
					if((mIntBeginMonth - mIntEndMonth != 0 )|| (mIntBeginDay - mIntEndDay != 0)){
						return sBeginTime + "~" + sOverTime;
					}else{
						return sBeginTime;
					}
				}
				//设置时间显示格式 一天以上 或 一天
				if((mIntBeginMonth - mIntEndMonth != 0 )|| (mIntBeginDay - mIntEndDay != 0)){
					String sBeginTime  = OverOneDayFormat.format(lBeginTime);
					String sOverTime  = OverOneDayFormat.format(lOverTime);
					return sBeginTime + "~" + sOverTime;
				}else{
					String sBeginTime  = OverOneDayFormat.format(lBeginTime);
					String sOverTime = OneDayFormat.format(lOverTime);
					return sBeginTime + "~" + sOverTime;
				}
	}
}
