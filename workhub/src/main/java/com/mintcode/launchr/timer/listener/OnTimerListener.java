package com.mintcode.launchr.timer.listener;

import android.view.View;

public interface OnTimerListener {
	/**
	 * 当type=2时，isEnd false为起始时间，true 为结束时间
	 * @param view
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param type
	 * @param isEnd
	 */
	void OnTimeChangeListenner(View view, int year, int month, int day, int hour, int minute, int type, boolean isEnd);
	
	
//	void OnTimeAllDayListener(View view, int year, int month, int day,boolean isEnd);
	
}
