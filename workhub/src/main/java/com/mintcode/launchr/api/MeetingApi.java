package com.mintcode.launchr.api;


import java.util.List;

import com.mintcode.launchrnetwork.MTHttpManager;
import com.mintcode.launchrnetwork.MTHttpParameters;
import com.mintcode.launchrnetwork.OnResponseListener;

/**
 * 用户登录api
 * @author KevinQiao
 *
 */
public class MeetingApi extends BaseAPI{
	
	
	
	private static MeetingApi userApi = new MeetingApi();
	
	/** 新建会议 */
	private static final String URL_NEW_MEETING = "/Schedule-Module/Meeting";
	/** 编辑会议*/
	private static final String URL_EDIT_MEETING = "/Schedule-Module/Meeting/PostMeeting";
	/** 删除会议*/
	private static final String URL_DELECT_MEETING = "/Schedule-Module/Meeting";
	/** 确认会议**/
	private static final String URL_SRUE_MEETING = "/Schedule-Module/Meeting/MeetingConfirm";
 	/**获取会议明细*/
	private static final String URL_GET_MEETING = "/Schedule-Module/Meeting";
	/**获取会议室列表**/
	private static final String URL_GET_MEETINGROOM_LIST = "/Schedule-Module/Meeting/GetMeetingRoom";
	/**获取选择人员的非空闲时间**/
	private static final String URL_GET_PERSON_NO_TIME = "/Schedule-Module/Schedule/GetUnFreeMeetingList";
	/**获取非空会议室**/
	private static final String URL_GET_NOFREE_MEETINGROOM = "/Schedule-Module/Schedule/GetUnFreeMeetingRoomList";
	
	/** 删除会议或者日程 */
	private static final String URL_DEL_MEETING_OR_SCHEDULE = "/Schedule-Module/Schedule";
	/** 新增评论*/
	public static final String URL_ADD_MESSAGE = "/Base-Module/Comment";
	/** 获取评论*/
	public static final String URL_GET_MESSAGE_LIST = "/Base-Module/Comment/GetList";
	/** 删除评论*/
	public static final String delectCommentList = "/Base-Module/Comment";
	/** 取消会议*/
	public static final String URL_CANCEL_MEETING = "/Schedule-Module/Meeting/CancelMeeting";

	public static final class TaskId{
		public static final String TASK_URL_NEW_MEETING = "task_url_new_meeting";
		public static final String TASK_URL_EDIT_MEETING = "task_url_edit_meeting";
		public static final String TASK_URL_DELECT_MEETING = "task_url_delect_meeting";
		public static final String TASK_URL_GET_MEETING = "task_url_get_meeting";
		public static final String TASK_URL_SRUE_MEETING = "task_url_srue_meeting";
		public static final String TASK_URL_GET_MEETINGROOM_LIST = "task_url_get_meetingroom_list";
		public static final String TASK_URL_GET_PERSON_NO_TIME = "task_url_get_person_no_time";
		public static final String TASK_URL_GET_NOFREE_MEETINGROOM = "task_url_get_nofree_meetingroom";
		public static final String TASK_URL_DEL_MEETING_OR_SCHEDULE = "task_url_del_meeting_or_schedule";
		public static final String TASK_URL_ADD_MESSAGE = "task_url_add_message";
	    public static final String TASK_URL_GET_MESSAGE_LIST = "task_url_get_message_list";
	    public static final String TASK_URL_DELETE_MESSAGE = "";
		public static final String TASK_URL_CANCEL_MEETING = "task_url_cancel_meeting";
	}
	
	
	
	private MeetingApi(){
		
	}
	
	public static MeetingApi getInstance(){
		return userApi;
	}
	
	/**
	 * 添加会议
	 * @param listener
	 * @param title
	 * @param content
	 * @param startTime
	 * @param endTime
	 * @param meetRoomId
	 * @param externalRoom
	 * @param lngx
	 * @param laty
	 * @param requireJoin
	 * @param requireJoinName
	 * @param join
	 * @param joinName
	 * @param reType
	 * @param reTypeTime
	 */
	public void addNewMeeting(OnResponseListener listener, String title, String content, long startTime, long endTime, String meetRoomId, String externalRoom
			, String lngx, String laty, String requireJoin,String requireJoinName, String join, String joinName,String reType,String reTypeTime,long entTime,int isVisible){
		MTHttpParameters params = new MTHttpParameters();
		
		params.setParameter("M_TITLE", title);
		params.setParameter("M_CONTENT", content);
		params.setParameter("M_START", startTime);
		params.setParameter("M_END", endTime);
		params.setParameter("R_SHOW_ID",meetRoomId );
		params.setParameter("M_EXTERNAL", externalRoom);
		params.setParameter("M_LNGX", lngx);
		params.setParameter("M_LATY", laty);
		params.setParameter("M_REQUIRE_JOIN", requireJoin);
		params.setParameter("M_REQUIRE_JOIN_NAME", requireJoinName);
		params.setParameter("M_JOIN", join);
		params.setParameter("M_JOIN_NAME", joinName);
		params.setParameter("M_RESTART_TYPE", reType);
		params.setParameter("M_REMIND_TYPE", reTypeTime);
		params.setParameter("M_IS_VISIBLE",isVisible);
		
		if (entTime > 0) {
			params.setParameter("endTime", endTime);
		}
		
		executeHttpMethod(listener, TaskId.TASK_URL_NEW_MEETING, URL_NEW_MEETING, PUT_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	/**
	 * 编辑会议
	 * @param listener
	 * @param title
	 * @param content
	 * @param startTime
	 * @param endTime
	 * @param meetRoomId
	 * @param externalRoom
	 * @param lngx
	 * @param laty
	 * @param requireJoin
	 * @param requireJoinName
	 * @param join
	 * @param joinName
	 * @param reType
	 * @param reTypeTime
	 */
	public void editMeeting(OnResponseListener listener,String showId,String title,String content,
							long startTime, long endTime, String meetRoomId, String externalRoom,
							String lngx, String laty,String requireJoin,String requireJoinName,
							String join,String joinName,String reType,String reTypeTime,int isVisible){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("SHOW_ID",showId);
		params.setParameter("M_TITLE", title);
		params.setParameter("M_CONTENT", content);
		params.setParameter("M_START", startTime);
		params.setParameter("M_END", endTime);
		params.setParameter("R_SHOW_ID",meetRoomId );
		params.setParameter("M_EXTERNAL", externalRoom);
		params.setParameter("M_LNGX", lngx);
		params.setParameter("M_LATY", laty);
		params.setParameter("M_REQUIRE_JOIN", requireJoin);
		params.setParameter("M_REQUIRE_JOIN_NAME", requireJoinName);
		params.setParameter("M_JOIN", join);
		params.setParameter("M_JOIN_NAME", joinName);
//		params.setParameter("M_RESTART_TYPE", reType);
		params.setParameter("M_REMIND_TYPE", reTypeTime);
		params.setParameter("M_IS_VISIBLE",isVisible);
		executeHttpMethod(listener, TaskId.TASK_URL_EDIT_MEETING, URL_EDIT_MEETING, POST_TYPE, MTHttpManager.HTTP_POST, params, false);
		
	}

	/**
	 * 删除会议
	 * @param listener
	 * @param showid
	 */
	public void delectMeeting(OnResponseListener listener,String showid){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("SHOW_ID", showid);
		executeHttpMethod(listener, TaskId.TASK_URL_DELECT_MEETING, URL_DELECT_MEETING, DELETE_TYPE, MTHttpManager.HTTP_POST, params, false);		
	}
	/**
	 * 获取会议
	 * @param listener
	 * @param showid
	 */
	public void getMeeting(OnResponseListener listener,String showid,Long time){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("SHOW_ID", showid);
		params.setParameter("FACT_START_TIME",time);
		executeHttpMethod(listener, TaskId.TASK_URL_GET_MEETING, URL_GET_MEETING, GET_TYPE, MTHttpManager.HTTP_POST, params, false);			
	}
	/**
	 * 确认会议
	 * @param listener
	 * @param showid
	 * @param attend
	 * @param reason
	 */
	public void confirmMeeting(OnResponseListener listener,String showid,int attend,String reason){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("M_SHOW_ID", showid);
		params.setParameter("O_IS_AGREE",attend);
		params.setParameter("O_REASON", reason);
		executeHttpMethod(listener, TaskId.TASK_URL_SRUE_MEETING, URL_SRUE_MEETING, POST_TYPE, MTHttpManager.HTTP_POST, params, false);					
	}
	/** 取消会议*/
	public void cancelMeeting(OnResponseListener listener,String showId,String reason){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId", showId);
		params.setParameter("reason",reason);
		executeHttpMethod(listener, TaskId.TASK_URL_CANCEL_MEETING,URL_CANCEL_MEETING, POST_TYPE, MTHttpManager.HTTP_POST, params, false);

	}
	/**
	 * 获取会议室列表
	 * @param listener
	 */
	public void getMeetingRoomList(OnResponseListener listener){
		MTHttpParameters params = new MTHttpParameters();
		executeHttpMethod(listener, TaskId.TASK_URL_GET_MEETINGROOM_LIST, URL_GET_MEETINGROOM_LIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);							
	}

	/**
	 * 获取选择人员的非空闲时间
	 * @param listener
	 * @param user
	 * @param starttime
	 * @param endtime
	 */
	public void getPersonNoTime(OnResponseListener listener,List<String> user,Long starttime,Long endtime){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("user", user);
		params.setParameter("startTime",starttime);
		params.setParameter("endTime", endtime);
		executeHttpMethod(listener, TaskId.TASK_URL_GET_PERSON_NO_TIME, URL_GET_PERSON_NO_TIME, GET_TYPE, MTHttpManager.HTTP_POST, params, false);							
	
	}
	/**
	 * 获取非空闲会议室
	 * @param listener
	 * @param starttime
	 * @param endtime
	 */
	public void getNoFreeMeetingRoom(OnResponseListener listener,Long starttime,Long endtime){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("startTime",starttime);
		params.setParameter("endTime", endtime);
		executeHttpMethod(listener, TaskId.TASK_URL_GET_NOFREE_MEETINGROOM, URL_GET_NOFREE_MEETINGROOM, GET_TYPE, MTHttpManager.HTTP_POST, params, false);								
	}
	
	/**
	 * 
	 * @param listener
	 * @param showId
	 * @param relatedId
	 * @param initialStartTime
	 * @param saveType
	 */
	public void delMeetingOrSchedule(OnResponseListener listener, String relatedId,long initialStartTime, int saveType ){
		
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("relateId", relatedId);
		params.setParameter("initialStartTime",initialStartTime);
		params.setParameter("saveType", saveType);
		executeHttpMethod(listener, TaskId.TASK_URL_DEL_MEETING_OR_SCHEDULE, URL_DEL_MEETING_OR_SCHEDULE, DELETE_TYPE, MTHttpManager.HTTP_POST, params, false);								
	
	}
	
	/**
	 * 得到评论列表
	 * @param listener
	 * @param appShowID
	 * @param rm_ShowID
	 * @param pageIndex
	 * @param pageSize
	 * @param timeStamp
	 */
	public void getMessageList(OnResponseListener listener, String appShowID, String rm_ShowID, int pageIndex, int pageSize, long timeStamp){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appShowID", appShowID);
		params.setParameter("rm_ShowID", rm_ShowID);
		params.setIntParameter("pageIndex", pageIndex);
		params.setIntParameter("pageSize", pageSize);
		params.setLongParameter("timeStamp", timeStamp);
		executeHttpMethod(listener, TaskId.TASK_URL_GET_MESSAGE_LIST, URL_GET_MESSAGE_LIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 新增评论
	 * @param listener
	 * @param appShowID
	 * @param rm_ShowID
	 * @param comment
	 * @param isNotComment
	 * @param fileShowID
	 * @param filePath
	 * @param Title
	 * @param messageAppType
	 * @param toUsers
	 * @param toUserNames
	 */
	public void addMessage(OnResponseListener listener, String appShowID, String rm_ShowID, String comment, int isNotComment, String fileShowID,
            String filePath, String Title, String messageAppType, List<String> toUsers, List<String> toUserNames){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appShowID", appShowID);
		params.setParameter("rm_ShowID", rm_ShowID);
		params.setParameter("comment", comment);
		params.setIntParameter("isNotComment", isNotComment);
		params.setParameter("fileShowID", fileShowID);
		params.setParameter("filePath", filePath);
		params.setParameter("Title", Title);
		params.setParameter("messageAppType", messageAppType);
		params.setParameter("toUsers", toUsers);
		params.setParameter("toUserNames", toUserNames);
		executeHttpMethod(listener, TaskId.TASK_URL_ADD_MESSAGE, URL_ADD_MESSAGE, PUT_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	/**
	 * 删除评论
	 * @param listener
	 * @param showID
	 */
	public void delectConmment(OnResponseListener listener, String showID){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showID", showID);
		executeHttpMethod(listener,"delectComment", delectCommentList, DELETE_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
}



















