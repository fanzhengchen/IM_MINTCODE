package com.mintcode.launchr.app.newSchedule.util;

import com.mintcode.launchr.pojo.entity.EventEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JulyYu on 2016/3/11.
 */
public class ScheduleEventUtil {



    public static HashMap<String, List<EventEntity>> sortMonthEventList(List<EventEntity> data) {

        java.util.HashMap<String, List<EventEntity>> hashMap = new HashMap<String, List<EventEntity>>();
        Calendar calendar = Calendar.getInstance();
        sortListByTime(data);
        for(EventEntity entity : data){
            long startTime = entity.getStartTime();
            long endTime = entity.getEndTime() - 1000;
            String endTimekey = getKey(calendar, endTime);
            String startTimekey = getKey(calendar, startTime);
            if(startTimekey.equals(endTimekey)){
                addEventToMap(hashMap,entity,startTimekey);
            }else{
                while (Long.valueOf(startTimekey) <= Long.valueOf(endTimekey)){
                    addEventToMap(hashMap, entity, startTimekey);
                    int monthNum = calendar.get(Calendar.MONTH) + 1;
                    calendar.set(Calendar.MONTH ,monthNum);
                    startTimekey = getKey(calendar);
                }
//                addEventToMap(hashMap, entity, startTimekey);
//                do{
//                    int monthNum = calendar.get(Calendar.MONTH) + 1;
//                    calendar.set(Calendar.MONTH ,monthNum);
//                    startTimekey = getKey(calendar);
//                    addEventToMap(hashMap, entity, startTimekey);
//                }while (!startTimekey.equals(endTimekey));
            }
        }

        return hashMap;
    }



    public static  HashMap<String, List<EventEntity>> sortDayEventList(List<EventEntity> data) {
        java.util.HashMap<String, List<EventEntity>> hashMap = new HashMap<String, List<EventEntity>>();
        Calendar calendar = Calendar.getInstance();
        sortListByTime(data);
        for(EventEntity entity : data){
            long startTime = entity.getStartTime();
            long endTime = entity.getEndTime() - 1000;
            String endTimekey = getAllTimeKey(calendar, endTime);
            String startTimekey = getAllTimeKey(calendar, startTime);
            if(startTimekey.equals(endTimekey)){
                addEventToMap(hashMap,entity,startTimekey);
            }else{
                while (Long.valueOf(startTimekey) <= Long.valueOf(endTimekey)){
                    addEventToMap(hashMap, entity, startTimekey);
                    int monthNum = calendar.get(Calendar.DAY_OF_MONTH) + 1;
                    calendar.set(Calendar.DAY_OF_MONTH ,monthNum);
                    startTimekey = getAllTimeKey(calendar);
                }
//                addEventToMap(hashMap, entity, startTimekey);
//                do{
//                    int monthNum = calendar.get(Calendar.DAY_OF_MONTH) + 1;
//                    calendar.set(Calendar.DAY_OF_MONTH ,monthNum);
//                    startTimekey = getAllTimeKey(calendar);
//                    addEventToMap(hashMap, entity, startTimekey);
//                }while (!startTimekey.equals(endTimekey));
            }
        }
        return hashMap;
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


    /** 获取string类型时间*/
    private static String getKey(Calendar calendar, long startTime) {
        calendar.setTimeInMillis(startTime);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String key = year * 10000 + month * 100 + "";
        return key;
    }
    /** 获取string类型时间*/
    public static String getKey(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        String key = year * 10000 + month * 100 + "";
        return key;
    }

    /** 获取string类型时间*/
    public static String getKey( int year, int month) {
        String key = year * 10000 + month * 100 + "";
        return key;
    }

    /** 获取string类型时间*/
    public static String getKey( int year, int month,int day) {
        String key = year * 10000 + month * 100 + day + "";
        return key;
    }

    public static long getLongKey(int year, int month,int day ){
        long longKey = year * 10000 + month * 100 + day ;
        return longKey;
    }

    /** 获取string类型时间*/
    public static String getAllTimeKey(Calendar calendar,long time) {
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String key = year * 10000 + month * 100 + day +"";
        return key;
    }

    /** 获取string类型时间*/
    public static String getAllTimeKey(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String key = year * 10000 + month * 100 + day + "";
        return key;
    }
    /** 排序的方式添加事件到map中*/
    private static void addEventToMap(
            HashMap<String, List<EventEntity>> hashMap,
            EventEntity eventEntity, String key) {
        List<EventEntity> list = hashMap.get(key);
        if (list != null) {
            list.add(eventEntity);
        } else {
            list = new ArrayList<>();
            list.add(eventEntity);
            hashMap.put(key, list);
        }
    }

}
