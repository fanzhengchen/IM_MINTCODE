package com.mintcode.launchr.app.newSchedule.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.mintcode.launchr.pojo.entity.EventEntity;

public class ScheduleUtil {
	/** 处理从服务器获取的数据*/
	public static LinkedHashMap<Long, List<EventEntity>> getEventDataList(LinkedHashMap<Long, List<EventEntity>> result, List<EventEntity> mlistData){
		// 结束时间减1分钟，不要忘记加上
		for(EventEntity entity : mlistData){
			entity.setEndTime(entity.getEndTime() - 60*1000);
		}
		// 首先进行排序
		sortListByTime(mlistData);
		// 每次循环即一天，判断某个事件是否在这一天发生
		for(Long key : result.keySet()){
			List<EventEntity> list = result.get(key);

			Calendar keyTime = Calendar.getInstance();
			keyTime.setTimeInMillis(key);

			for(int i=0; i<mlistData.size(); i++){
				// TODO 为什么要这么做？
				EventEntity entity = copyEventEntity(mlistData.get(i));

				Calendar startTime = Calendar.getInstance();
				startTime.setTimeInMillis(entity.getStartTime());
				Calendar stopTime = Calendar.getInstance();
				stopTime.setTimeInMillis(entity.getEndTime());

				// 开始结束时间在同一天，直接添加到list中
				if(isInSameDay(keyTime, startTime) && isInSameDay(keyTime, stopTime)){
					list.add(entity);
				}
				// 开始结束时间不在同一天
				else if(isInSameDay(keyTime, startTime) && !isInSameDay(keyTime, stopTime)){
					// 设置结束时间
					Calendar end = Calendar.getInstance();
					end.set(keyTime.get(Calendar.YEAR), keyTime.get(Calendar.MONTH), keyTime.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
					entity.setEndTime(end.getTimeInMillis());
					list.add(entity);

					// 设置开始时间
					EventEntity startEntity = mlistData.get(i);
					Calendar start = Calendar.getInstance();
					start.set(keyTime.get(Calendar.YEAR), keyTime.get(Calendar.MONTH), keyTime.get(Calendar.DAY_OF_MONTH)+1, 0, 0, 0);
					startEntity.setStartTime(start.getTimeInMillis());
					mlistData.set(i, startEntity);
				}
			}
			result.put(key, list);
		}

		return result;
	}

	/** 判断两个时间是否在同一天*/
	private static boolean isInSameDay(Calendar first, Calendar second){
		boolean result = true;
		if(first.get(Calendar.YEAR)!=second.get(Calendar.YEAR)){
			result = false;
		}
		if(first.get(Calendar.MONTH)!=second.get(Calendar.MONTH)){
			result = false;
		}
		if(first.get(Calendar.DAY_OF_MONTH)!=second.get(Calendar.DAY_OF_MONTH)){
			result = false;
		}
		return result;
	}

	public static boolean isTimeInDayStart(long time){
		boolean result = false;
		Calendar b = Calendar.getInstance();
		b.setTimeInMillis(time);
		if((b.get(Calendar.HOUR)==0) && (b.get(Calendar.MINUTE)==0)){
			result = true;
		}
		return result;
	}

	/** 事件跨越的天数越长，排序越在前面*/
	private static void sortListByTime(List<EventEntity> mlistData){
		List<Long> time = new ArrayList<Long>();
		for(EventEntity entity : mlistData){
			time.add(Math.abs(entity.getEndTime() - entity.getStartTime()));
		}
		for(int i=0; i<time.size()-1; i++){
			for(int j=0; j<time.size()-i-1; j++){
				if(time.get(j) < time.get(j+1)){
					long temp = time.get(j);
					time.set(j, time.get(j+1));
					time.set(j+1, temp);

					EventEntity entity = mlistData.get(j);
					mlistData.set(j, mlistData.get(j+1));
					mlistData.set(j+1, entity);
				}
			}
		}
	}

	private static EventEntity copyEventEntity(EventEntity entity){
		EventEntity result = new EventEntity();
		result.setEndTime(entity.getEndTime());
		result.setCreateTime(entity.getCreateTime());
		result.setCreateUser(entity.getCreateUser());
		result.setShowId(entity.getShowId());
		result.setCreatUserName(entity.getCreatUserName());
		result.setIsAllDay(entity.getIsAllDay());
		result.setIsCancel(entity.getIsCancel());
		result.setIsImportant(entity.getIsImportant());
		result.setLaty(entity.getLaty());
		result.setLngx(entity.getLngx());
		result.setPlace(entity.getPlace());
		result.setType(entity.getType());
		result.setTitle(entity.getTitle());
		result.setStartTime(entity.getStartTime());
		result.setRelateId(entity.getRelateId());
		result.setIsAllowSearch(entity.isAllowSearch());
		result.setIsVisible(entity.getIsVisible());
		return result;
	}

	public static HashMap<String, List<EventEntity>> sortEventList(List<EventEntity> data) {
		HashMap<String, List<EventEntity>> hashMap = new HashMap<String, List<EventEntity>>();
		Calendar calendar = Calendar.getInstance();
		for (EventEntity eventEntity : data) {
			long startTime = eventEntity.getStartTime();
			long endTime = eventEntity.getEndTime();
			String key = getKey(calendar, startTime);
			String key1 = getKey(calendar, endTime);
			if (!key.equals(key1)) {
				addEventToMap(hashMap, eventEntity, key);
				startTime = startTime + 1000 * 3600 * 24;
				while (startTime < endTime) {
					String key2 = getKey(calendar, startTime);
					addEventToMap(hashMap, eventEntity, key2);
					startTime = startTime + 1000 * 3600 * 24;
				}
			}else{
				addEventToMap(hashMap, eventEntity, key);
			}
		}
		return hashMap;
	}

	private static String getKey(Calendar calendar, long startTime) {
		calendar.setTimeInMillis(startTime);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String key = year * 10000 + month * 100 + day + "";
		return key;
	}

	private static void addEventToMap(
			HashMap<String, List<EventEntity>> hashMap,
			EventEntity eventEntity, String key) {
		List<EventEntity> list = hashMap.get(key);
		if (list != null) {
			if(eventEntity.getIsAllDay() != 1){
				     long starttime = eventEntity.getStartTime();
				     int i ;
				     for(i = 0 ;i < list.size();i++){
						 if(list.get(i).getIsAllDay() != 1){
							 if(starttime > list.get(i).getStartTime()){
								 if(i == list.size()){
									 list.add(eventEntity);
									 i = list.size();
								 }
							 }else{
								 list.add(i,eventEntity);
								 i = list.size();
							 }
						 }
					 }
			}else if (eventEntity.getType().equals("statutory_festival") || eventEntity.getType().equals("company_festival")){//事件排序
				list.add(0,eventEntity);
			}else if(eventEntity.getIsAllDay() == 1){
				int i ;
				for(i = 0 ;i < list.size();i++){
					if(list.get(i).getIsAllDay() != 1){
						list.add(i,eventEntity);
						i = list.size();
					}
				}

			}
		} else {
			list = new ArrayList<EventEntity>();
			list.add(eventEntity);
			hashMap.put(key, list);
		}
	}
	

}
