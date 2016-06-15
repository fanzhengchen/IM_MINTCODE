package com.mintcode.launchr.api;

import com.mintcode.launchr.pojo.entity.ScheduleEntity;
import com.mintcode.launchrnetwork.MTHttpManager;
import com.mintcode.launchrnetwork.MTHttpParameters;
import com.mintcode.launchrnetwork.OnResponseListener;

import java.util.List;

public class ScheduleApi extends BaseAPI {

	private static ScheduleApi sInstance;

	private static final String ScheduleBaseUrl = "/Schedule-Module/";

	private ScheduleApi() {
	}

	public static ScheduleApi getInstance() {
		if (sInstance == null) {
			sInstance = new ScheduleApi();
		}
		return sInstance;
	}

	public static final class URL {
		public static final String GET_LIST = ScheduleBaseUrl + "Schedule/GetList";
		public static final String SAVE_SCHEDULE = ScheduleBaseUrl + "Schedule/Save";
		public static final String DELECT_SCHEDULE = ScheduleBaseUrl + "Schedule";
		public static final String SURE_SCHEDULE = ScheduleBaseUrl + "Schedule/Sure";
		public static final String EDIT_SCHEDULE = ScheduleBaseUrl + "Schedule/Edit";
		public static final String GET_SCHEDULE_DETAIL = ScheduleBaseUrl + "Schedule";
		public static final String GET_DETAIL_TASKID = ScheduleBaseUrl + "Schedule/Detail";
	}

	public void getScheduleList(OnResponseListener listener, String user,long startTime, long endTime, String type) {
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("user", user);
		params.setParameter("startTime", startTime);
		params.setParameter("endTime", endTime);
		if (type != null) {
			params.setParameter("type", type);
		}
		executeHttpMethod(listener, URL.GET_LIST, URL.GET_LIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}

	public void getScheduleList(OnResponseListener listener, List<String> user,long startTime, long endTime, String type) {
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("user", user);
		params.setParameter("startTime", startTime);
		params.setParameter("endTime", endTime);
		if (type != null) {
			params.setParameter("type", type);
		}
		params.setParameter("isCancel", 2);
		executeHttpMethod(listener, URL.GET_LIST, URL.GET_LIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	public void saveSchedule(OnResponseListener listener ,ScheduleEntity entity) {
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("title", entity.getTitle());
		params.setParameter("start", entity.getStart());
		params.setParameter("end", entity.getEnd());
		params.setParameter("type", entity.getType());
		params.setParameter("place", entity.getPlace());
		params.setParameter("lngx", entity.getLngx());
		params.setParameter("laty", entity.getLaty());
		params.setParameter("isImportant", entity.getIsImportant());
		params.setParameter("isAllDay", entity.getIsAllDay());
		params.setParameter("content", entity.getContent());
		params.setParameter("repeatType", entity.getRepeatType());
		params.setParameter("remindType", entity.getRemindType());
		params.setIntParameter("isVisible", entity.getIsVisible());
		executeHttpMethod(listener, URL.SAVE_SCHEDULE, URL.SAVE_SCHEDULE, POST_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	public void delSchedule(OnResponseListener listener,String showId,String relateId, Long initialStartTime,int saveType){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId", showId);
		params.setParameter("relateId", relateId);
		params.setParameter("initialStartTime", initialStartTime);
		params.setParameter("saveType", saveType);
		executeHttpMethod(listener, URL.DELECT_SCHEDULE, URL.DELECT_SCHEDULE, DELETE_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	public void sureSchedule(OnResponseListener listener,String showId){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId", showId);
		executeHttpMethod(listener, URL.SURE_SCHEDULE, URL.SURE_SCHEDULE, POST_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	public void editSchedule(OnResponseListener listener,ScheduleEntity entity ,Long initstartime,int saveType){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId", entity.getShowId());
		params.setParameter("title", entity.getTitle());
		params.setParameter("content", entity.getContent());
		params.setParameter("type", entity.getType());
		params.setParameter("start", entity.getStart());
		params.setParameter("end", entity.getEnd());		
		params.setParameter("place", entity.getPlace());
		params.setParameter("lngx", entity.getLngx());
		params.setParameter("laty", entity.getLaty());
		params.setParameter("isImportant", entity.getIsImportant());
		params.setParameter("isAllDay", entity.getIsAllDay());
		params.setParameter("repeatType", entity.getRepeatType());
		params.setParameter("remindType", entity.getRemindType());
		params.setParameter("initialStartTime",initstartime);
		params.setParameter("saveType",saveType);
		params.setIntParameter("isVisible", entity.getIsVisible());
		executeHttpMethod(listener, URL.EDIT_SCHEDULE, URL.EDIT_SCHEDULE, POST_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	public void getScheduleDetail(OnResponseListener listener,String showid,Long initialStartTime){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId", showid);
		params.setParameter("initialStartTime", initialStartTime);
		executeHttpMethod(listener, URL.GET_DETAIL_TASKID, URL.GET_SCHEDULE_DETAIL, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}

}
