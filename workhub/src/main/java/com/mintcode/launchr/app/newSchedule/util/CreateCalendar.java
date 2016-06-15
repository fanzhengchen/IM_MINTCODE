package com.mintcode.launchr.app.newSchedule.util;

import java.util.Calendar;
import java.util.Locale;

import com.mintcode.launchr.R;
import com.mintcode.launchr.util.Const;

import android.content.Context;
import android.util.Log;

public class CreateCalendar {

	/**
	 * 判断是否为闰年
	 * @param year
	 * @return
	 */
	public boolean JudgeLeapYear(int year){
		if (year % 100 == 0 && year % 400 == 0) {
			return true;
		} else if (year % 100 != 0 && year % 4 == 0) {
			return true;
		}
		return false;		
	}
	
	public int MonthDays(int year ,int month){
		boolean isLeapYear = JudgeLeapYear(year);
		return MonthDays(isLeapYear, month);
	}
	/**
	 * 一个月的天数
	 * @param isLeapYear
	 * @param month
	 * @return
	 */
	public int MonthDays(boolean isLeapYear,int month){
		 int mDays = 0;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:			
		case 10:
		case 12:
			mDays = 31;
			break;		
		case 4:
		case 6:
		case 9:
		case 11:
			mDays = 30;
			break;
		case 2:
			if(isLeapYear) mDays =  29;
			else mDays = 28;
			break ;
		default:
			break;
		}
		return mDays;
	}
	/**
	 * 某月第一天是星期几
	 * @param year
	 * @param month
	 * @return
	 */
	public int FristDay(int year,int month){
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.set(year, month-1, 1);
		return mCalendar.get(Calendar.DAY_OF_WEEK);  
	}
	
	public int getTheMonthCount(int year, int month) {
		boolean isLeapyear = JudgeLeapYear(year);
		// 获取当月1号的星期数
		int firstInWeek = FristDay(year, month);
		// 获取当月天数
		int monthDays = MonthDays(isLeapyear, month);
		if ((monthDays == 31)) {
			if (firstInWeek < 6) {
				return 35;
			} else {
				return 42;
			}
		} else if ((monthDays == 30)) {
			if (firstInWeek < 7) {
				return 35;
			} else {
				return 42;
			}
		} else if (monthDays == 28) {
			if (firstInWeek == 1) {
				return 28;
			} else {
				return 35;
			}
		} else {
			return 35;
		}
	}


	/**
	 * 获取一周第一天
	 * @param year
	 * @param month
	 * @param days
	 * @param day
	 * @param dayofweek
	 */
	public int getFristDayOfWeek(int year, int month,int days,int day, int dayofweek) {
		int firstdayofweek;
		if (day - dayofweek + 1 <= 0) {
			if (month == 1) {
				month = 12;
				year = year - 1;
			} else {
				month = month - 1;
			}
			boolean isleap = JudgeLeapYear(year);
			days = MonthDays(isleap, month);
			firstdayofweek = day - dayofweek + 1 + days;
		} else {
			firstdayofweek = day - dayofweek + 1;
		}
		return firstdayofweek;
	}
	
	public int getFristDayOfWeek(int year, int month, int day){
		int days = MonthDays(year, month);
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month-1, day);
		int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);  
		return getFristDayOfWeek(year, month, days, day, dayofweek);
	}


}
