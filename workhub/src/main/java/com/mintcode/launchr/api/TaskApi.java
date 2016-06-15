package com.mintcode.launchr.api;

import java.util.ArrayList;
import java.util.List;

import org.litepal.util.Const.Model;

import com.mintcode.launchr.pojo.entity.TaskAddProjectEntity;
import com.mintcode.launchr.pojo.entity.TaskAddProjectEntity.TaskAddProjectMembersEntity;
import com.mintcode.launchrnetwork.MTHttpManager;
import com.mintcode.launchrnetwork.MTHttpParameters;
import com.mintcode.launchrnetwork.OnResponseListener;

/**
 * 任务访问接口
 * @author KevinQiao
 *
 */
public class TaskApi extends BaseAPI{

	private static TaskApi taskApi = new TaskApi();
	
	/**  搜索及获取任务列表*/
	private static final String URL_SEARCH_AND_GET_TASK_LIST = "/Task-Module/Task/GetTaskList";
	
	/** 新建任务*/
	private static final String URL_ADD_TASK = "/Task-Module/TaskV2/TaskAdd";
	
	/** 编辑任务*/
	public static final String URL_UPDATE_TASK = "/Task-Module/TaskV2/TaskUpdate";

	/** 切换任务状态*/
	public static final String URL_CHANGE_TASK_STATE = "/Task-Module/TaskV2/TaskChangeStatus";
	
	/** 更新任务转为父任务*/
	public static final String URL_UPDATE_TASK_TO_PARENT_TASK = "/Task-Module/Task/UpdateTask";
	
	/** 更新任务转为子任务*/
	public static final String URL_UPDATE_TASK_TO_SUN_TASK = "/Task-Module/Task/UpdateTask";
	
	/** 删除子任务*/
	public static final String URL_DELETE_SUB_TASK = "/Task-Module/TaskV2/TaskDelete";
	
	/** 编辑子任务*/
	public static final String URL_EDIT_SUB_TASK = "/Task-Module/Task/UpdateTask";
	
	/** 更新任务白板状态*/
	public static final String URL_UPDATE_TASK_WHITE_BOARD_STATUS = "/Task-Module/Task/UpdateTask";
	
	/** 删除任务*/
	public static final String URL_DELETE_TASK = "/Task-Module/TaskV2/TaskDelete";
	
	/** 获取任务详情*/
	public static final String URL_GET_TASK = "/Task-Module/TaskV2/TaskGet";
	
	/** 查询任务列表*/
	public static final String URL_GET_TASK_LIST_FOR_WHITE_BOARD = "/Task-Module/Task/GetTaskListForWhiteBoard";
	
	/** 新增项目*/
	public static final String URL_ADD_PROJECT = "/Task-Module/TaskV2/ProjectAdd";
	
	/** 删除项目*/
	public static final String URL_DELETE_PROJECT = "/Task-Module/TaskV2/ProjectDelete";
	
	/** 编辑项目*/
	public static final String URL_EDIT_PROJECT = "/Task-Module/TaskV2/ProjectEdit";
	
	/** 获取项目详情*/
	public static final String URL_GET_PROJECT_DETAIL = "/Task-Module/TaskV2/GetProjectDetail";
	
	/** 获取项目列表*/
	public static final String URL_GET_PROJECT_LIST = "/Task-Module/TaskV2/GetProjectList";
	
	/** 获取白板列表*/
	public static final String URL_GET_WHITE_BOARD_LIST = "/Task-Module/Task/GetWhiteBoardList";

	/**  新增白板*/
	public static final String URL_ADD_WHITE_BOARD = "/Task-Module/Task/WhiteboardAdd";

	/** 修改白板名称*/
	public static final String URL_CHANGE_WHITE_BOARD_NAME = "/Task-Module/Task/WhiteboardNameUpdate";
	
	/** 新增评论*/
	public static final String URL_ADD_MESSAGE = "/Base-Module/Comment";
	
	/** 获取评论*/
	public static final String URL_GET_MESSAGE_LIST = "/Base-Module/Comment/GetList";
	/** 获取项目的任务总数*/
	public static final String URL_GET_TASK_LIST_COUNT = "/Task-Module/TaskV2/GetTaskListCount";
	/** 获取项目的任务列表*/
	public static final String URL_GET_PROJECT_TASK_LIST = "/Task-Module/TaskV2/GetTaskList";

	/** 更新任务评论字段*/
	public static final String URL_UPDATE_TASK_COMMENT = "/Task-Module/TaskV2/TaskPostComment";

	public static final class TaskId{
		public static final String TASK_URL_SEARCH_AND_GET_TASK_LIST = "task_url_search_and_get_task_list";
		public static final String TASK_URL_ADD_TASK = "task_url_add_task";
		public static final String TASK_URL_UPDATE_TASK = "task_url_update_task";
		public static final String TASK_URL_UPDATE_TASK_TO_PARENT_TASK = "task_url_update_task_to_parent_task";
	    public static final String TASK_URL_UPDATE_TASK_TO_SUB_TASK = "task_url_update_task_to_sub_task";
	    public static final String TASK_URL_UPDATE_TASK_WHITE_BOARD_STATUS = "task_url_update_task_white_board_status";
	    public static final String TASK_URL_DELETE_TASK = "task_url_delete_task";
	    public static final String TASK_URL_GET_TASK = "task_url_get_task";
	    public static final String TASK_URL_GET_TASK_LIST_FOR_WHITE_BOARD = "task_url_get_task_list_for_white_board";
	    public static final String TASK_URL_ADD_PROJECT = "task_url_add_project";
	    public static final String TASK_URL_DELETE_PROJECT = "task_url_delete_project";
	    public static final String TASK_URL_EDIT_PROJECT = "task_url_edit_project";
	    public static final String TASK_URL_GET_PROJECT_DETAIL = "task_url_get_project_detail";
	    public static final String TASK_URL_GET_PROJECT_LIST = "task_url_get_project_list";
	    public static final String TASK_URL_GET_WHITE_BOARD_LIST = "task_url_get_white_board_list";
	    public static final String TASK_URL_EDIT_SUB_TASK = "task_url_update_sub_task";
	    public static final String TASK_URL_DELETE_SUB_TASK = "task_url_delete_sub_task";
	    public static final String TASK_URL_ADD_MESSAGE = "task_url_add_message";
	    public static final String TASK_URL_GET_MESSAGE_LIST = "task_url_get_message_list";
		public static final String TASK_URL_ADD_WHITE_BOARD = "task_url_add_white_board";
		public static final String TASK_URL_WHITE_BOARD_NAME = "task_url_white_board_name";
		public static final String TASK_URL_CHANGE_TASK_STATE = "task_url_change_task_state";
		public static final String TASK_URL_GET_TASK_COUNT = "task_url_get_task_count";
		public static final String TASK_URL_GET_PROJECT_TASK_LIST = "task_url_get_project_task_list";
		public static final String TASK_URL_UPDATE_TASK_COMMENT = "task_url_update_task_comment";
	}
	
	
	
	private TaskApi(){
		
	}
	
	public static TaskApi getInstance(){
		return taskApi;
	}
	
	/**
	 * 搜索及获取任务列表
	 * @param listener
	 * @param pageIndex
	 * @param pageSize
	 * @param searchKey
	 * @param type
	 * @param projectId
	 * @param statusType
	 * @param statusId
	 */	 
	public void searchAndGetTaskList(OnResponseListener listener, String pageIndex, String pageSize,String searchKey, int type, String projectId, String statusType, String statusId,String attendUser, String sendUser, long endStartTime, long endEndTime){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("pageIndex", pageIndex);
		params.setParameter("pageSize", pageSize);
		if (!"searchKey".equals("")) {
			params.setParameter("searchKey", searchKey);
		}
		params.setParameter("type", type);
		params.setParameter("projectId", projectId);
		params.setParameter("statusType", statusType);
		params.setParameter("statusId", statusId);
		
		if (!attendUser.equals("")) {
			params.setParameter("attendUser", attendUser);
		}

		if (!sendUser.equals("")) {
			params.setParameter("sendUser", sendUser);
		}
		
		if (endStartTime > 0) {
			params.setParameter("endStartTime", endStartTime);
		}
		
		if (endEndTime > 0) {
			params.setParameter("endEndTime", endEndTime);
		}
		executeHttpMethod(listener, TaskId.TASK_URL_SEARCH_AND_GET_TASK_LIST, URL_SEARCH_AND_GET_TASK_LIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 根据白板和状态获取任务列表
	 * @param listener
	 * @param pageIndex
	 * @param pageSize
	 * @param searchKey
	 * @param type
	 * @param projectId
	 * @param statusType
	 * @param statusId
	 */
	public void getTaskInBoard(OnResponseListener listener, String pageIndex, String pageSize,String searchKey, int type, String projectId, String statusType, String statusId){
		searchAndGetTaskList(listener, pageIndex, pageSize, "", type, projectId, statusType, statusId, "", "", -1, -1);
	}
	
	/**
	 * 根据过滤条件获取任务列表
	 * @param listener
	 * @param pageIndex
	 * @param pageSize
	 * @param type
	 * @param endTime
	 */
	public void getTaskInFilter(OnResponseListener listener, String pageIndex, String pageSize,int type, long endStartTime, long endTime){
		searchAndGetTaskList(listener, pageIndex, pageSize, "", type, "", "", "", "", "", endStartTime, endTime);
	}

	/**
	 * 新建任务(包括新建子任务)
	 */
	public void newTask(OnResponseListener listener, String pShowId, String tTitle, String tContent, Long tStartTime, Long tEndTime, int isStartTimeAllDay, int isEndTimeAllDay,
						 String users, String usersName, String priority, int remindType, String tParentShowId, List<String> filelist){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("pShowId", pShowId);
		if(tTitle != null){
			params.setParameter("tTitle", tTitle);
		}
		params.setParameter("tContent", tContent);
		if(tStartTime != null && tStartTime > 0){
			params.setIntParameter("isStartTimeAllDay", isStartTimeAllDay);
			params.setLongParameter("tStartTime", tStartTime);
		}
		if(tEndTime != null && tStartTime > 0){
			params.setLongParameter("tEndTime", tEndTime);
			params.setIntParameter("isEndTimeAllDay", isEndTimeAllDay);
		}
		params.setParameter("tUser", users);
		params.setParameter("tUserName", usersName);
		params.setParameter("tPriority", priority);
		params.setParameter("tRemindType", remindType);
		params.setParameter("tParentShowId", tParentShowId);
		if (filelist != null && !filelist.isEmpty()) {
			params.setParameter("fileShowIds", filelist);
		}
		executeHttpMethod(listener, TaskId.TASK_URL_ADD_TASK, URL_ADD_TASK, PUT_TYPE, MTHttpManager.HTTP_POST, params, false);
	}

	/**
	 * 更新任务
	 */
	public void updateTask(OnResponseListener listener, String showId,String pShowId, String tTitle, String tContent, Long tStartTime, Long tEndTime,
						   int isStartTimeAllDay, int isEndTimeAllDay, String tUser, String tUserName, String tPriority, int tRemindType, List<String> fileShowIds){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId", showId);
		params.setParameter("pShowId", pShowId);
		if(tTitle != null){
			params.setParameter("tTitle", tTitle);
		}
		params.setParameter("tContent", tContent);
		if(tStartTime != null && tStartTime > 0){
			params.setLongParameter("tStartTime", tStartTime);
			params.setIntParameter("isStartTimeAllDay", isStartTimeAllDay);
		}
		if(tEndTime != null && tStartTime > 0){
			params.setLongParameter("tEndTime", tEndTime);
			params.setIntParameter("isEndTimeAllDay", isEndTimeAllDay);
		}
		params.setParameter("tUser", tUser);
		params.setParameter("tUserName", tUserName);
		params.setParameter("tPriority", tPriority);
		params.setIntParameter("tRemindType", tRemindType);
		if (fileShowIds != null && !fileShowIds.isEmpty()) {
			params.setParameter("fileShowIds", fileShowIds);
		}
		executeHttpMethod(listener, TaskId.TASK_URL_UPDATE_TASK, URL_UPDATE_TASK, POST_TYPE, MTHttpManager.HTTP_POST, params, false);
	}

	/**
	 * 切换任务状态
	 * */
	public void changeTaskState(OnResponseListener listener, String showId,String sType){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId", showId);
		params.setParameter("sType", sType);
		executeHttpMethod(listener, TaskId.TASK_URL_CHANGE_TASK_STATE, URL_CHANGE_TASK_STATE, POST_TYPE, MTHttpManager.HTTP_POST, params, false);
	}

	/**
	 * 更新任务转为父任务
	 * @param listener
	 * @param title
	 * @param showId
	 * @param updateType
	 */
	public void updateTaskToParentTask(OnResponseListener listener, String showId, String updateType){
		MTHttpParameters params = new MTHttpParameters();
//		params.setParameter("TITLE", title);
		params.setParameter("SHOW_ID", showId);
		params.setParameter("UpdateType", updateType);
		executeHttpMethod(listener, TaskId.TASK_URL_UPDATE_TASK_TO_PARENT_TASK, URL_UPDATE_TASK_TO_PARENT_TASK, POST_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 更新任务转为子任务
	 * @param listener
	 * @param title
	 * @param showId
	 * @param parentShowId
	 * @param updateType
	 */
	public void updateTaskToSubTask(OnResponseListener listener, String title, String showId, String parentShowId, String updateType){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("TITLE", title);
		params.setParameter("SHOW_ID", showId);
		params.setParameter("T_PARENT_SHOW_ID", parentShowId);
		params.setParameter("UpdateType", updateType);
		executeHttpMethod(listener, TaskId.TASK_URL_UPDATE_TASK_TO_SUB_TASK, URL_UPDATE_TASK_TO_SUN_TASK, POST_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 更新任务白板状态
	 * @param listener
	 * @param title
	 * @param showId
	 * @param updateType
	 */
	public void updateTaskWhiteBoardStatus(OnResponseListener listener, String title, String showId, String statusId, String updateType){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("TITLE", title);
		params.setParameter("SHOW_ID", showId);
		params.setParameter("S_SHOW_ID", statusId);
		params.setParameter("UpdateType", updateType);
		executeHttpMethod(listener, TaskId.TASK_URL_UPDATE_TASK_WHITE_BOARD_STATUS, URL_UPDATE_TASK_WHITE_BOARD_STATUS, POST_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 删除任务
	 */
	public void deleteTask(OnResponseListener listener, String showId){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId", showId);
		executeHttpMethod(listener, TaskId.TASK_URL_DELETE_TASK, URL_DELETE_TASK, DELETE_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 获取任务详情
	 */
	public void getTask(OnResponseListener listener, String showId){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId", showId);
		executeHttpMethod(listener, TaskId.TASK_URL_GET_TASK, URL_GET_TASK, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 查询任务列表
	 * @param listener
	 * @param title
	 * @param showId
	 * @param pageIndex
	 * @param pageSize
	 * @param timeStamp
	 */
	public void getTaskListForWhiteBoard(OnResponseListener listener, String title, String showId, String pageIndex, String pageSize, long timeStamp){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("TITLE", title);
		params.setParameter("showId", showId);
		params.setParameter("pageIndex", pageIndex);
		params.setParameter("pageSize", pageSize);
		params.setLongParameter("timeStamp", timeStamp);
		executeHttpMethod(listener, TaskId.TASK_URL_GET_TASK_LIST_FOR_WHITE_BOARD, URL_GET_TASK_LIST_FOR_WHITE_BOARD, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 新增项目
	 * @param listener
	 * @param name
	 * @param members
	 */
	public void addProject(OnResponseListener listener, String name, ArrayList<TaskAddProjectMembersEntity> members){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("name", name);
		params.setParameter("members", members);
		executeHttpMethod(listener, TaskId.TASK_URL_ADD_PROJECT, URL_ADD_PROJECT, PUT_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 删除项目
	 * @param listener
	 * @param showId
	 */
	public void deleteProject(OnResponseListener listener, String showId){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId", showId);
		executeHttpMethod(listener, TaskId.TASK_URL_DELETE_PROJECT, URL_DELETE_PROJECT, DELETE_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 编辑项目
	 * @param listener
	 * @param showId
	 * @param name
	 */
	public void editProject(OnResponseListener listener, String showId, String name, List<String> deleteMembers, List<TaskAddProjectEntity.TaskAddProjectMembersEntity> addMembers){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId", showId);
		params.setParameter("name", name);
		params.setParameter("deleteMembers",deleteMembers);
		params.setParameter("addMembers",addMembers);
		executeHttpMethod(listener, TaskId.TASK_URL_EDIT_PROJECT, URL_EDIT_PROJECT, POST_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 获取项目详情
	 * @param listener
	 * @param showId
	 */
	public void getProjectDetail(OnResponseListener listener, String showId){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId", showId);		
		executeHttpMethod(listener, TaskId.TASK_URL_GET_PROJECT_DETAIL, URL_GET_PROJECT_DETAIL, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 获取项目列表
	 * @param listener
	 * @param pageIndex
	 * @param pageSize
	 * @param searchKey
	 */
	public void getProjectList(OnResponseListener listener, String pageIndex, String pageSize, String searchKey){
		MTHttpParameters params = new MTHttpParameters();
//		params.setParameter("pageIndex", pageIndex);
//		params.setParameter("pageSize", pageSize);
//		params.setParameter("searchkey", searchKey);
		executeHttpMethod(listener, TaskId.TASK_URL_GET_PROJECT_LIST, URL_GET_PROJECT_LIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 获取白板列表
	 * @param listener
	 * @param showId
	 */
	public void getWhiteBoardList(OnResponseListener listener, String showId){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId", showId);
		executeHttpMethod(listener, TaskId.TASK_URL_GET_WHITE_BOARD_LIST, URL_GET_WHITE_BOARD_LIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 删除子任务
	 * @param listener
	 * @param title
	 * @param showId
	 */
	public void deleteSubTask(OnResponseListener listener, String showId){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId", showId);
		executeHttpMethod(listener, TaskId.TASK_URL_DELETE_SUB_TASK, URL_DELETE_SUB_TASK, DELETE_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 编辑子任务
	 * @param listener
	 * @param title
	 * @param showId
	 * @param priority
	 * @param users
	 * @param usersName
	 * @param endTime
	 * @param isAnnex
	 * @param backUp
	 * @param updateType
	 */
	public void editSubTask(OnResponseListener listener, String title, String showId, String priority, String users, String usersName,  long endTime, int isAnnex, String backUp, String updateType){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("TITLE", title);
		params.setParameter("SHOW_ID", showId);
		params.setParameter("T_PRIORITY", priority);
		params.setParameter("T_USERS", users);
		params.setParameter("T_USERS_NAME", usersName);
		params.setLongParameter("T_END_TIME", endTime);
		params.setIntParameter("T_IS_ANNEX", isAnnex);
		params.setParameter("T_BACKUP", backUp);
		params.setParameter("UpdateType", updateType);
		executeHttpMethod(listener, TaskId.TASK_URL_EDIT_SUB_TASK, URL_EDIT_SUB_TASK, POST_TYPE, MTHttpManager.HTTP_POST, params, false);
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

	/** 新增白板*/
	public void addWhiteBoard(OnResponseListener listener, String pShowId, String preShowId, String name){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("pShowId", pShowId);
//		params.setParameter("preShowId", preShowId);
		params.setParameter("name", name);
		executeHttpMethod(listener, TaskId.TASK_URL_ADD_WHITE_BOARD, URL_ADD_WHITE_BOARD, PUT_TYPE, MTHttpManager.HTTP_POST, params, false);
	}

	/** 编辑白板名称*/
	public void editWhiteBoardName(OnResponseListener listener, String showId, String name){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId", showId);
		params.setParameter("name", name);
		executeHttpMethod(listener, TaskId.TASK_URL_WHITE_BOARD_NAME, URL_CHANGE_WHITE_BOARD_NAME, POST_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	/** 获取任务总数*/
	public void getTaskCount(OnResponseListener listener,long time){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("Time", time);
		executeHttpMethod(listener, TaskId.TASK_URL_GET_TASK_COUNT, URL_GET_TASK_LIST_COUNT, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	/** 获取项目的任务列表*/
	public void getProjectTaskList(OnResponseListener listener,int pageIndex,int pageSize,int type,long time,String projectId,String statusType){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("pageIndex", pageIndex);
		params.setParameter("pageSize", pageSize);
		params.setParameter("type", type);
		params.setParameter("time", time);
		if(type == 7){
			params.setParameter("projectId", projectId);
		}
		params.setParameter("statusType", statusType);
		executeHttpMethod(listener, TaskId.TASK_URL_GET_PROJECT_TASK_LIST, URL_GET_PROJECT_TASK_LIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}

	/** 更新任务评论字段*/
	public void updateTaskComment(OnResponseListener listener,String showId,int tIsComment){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId", showId);
		params.setParameter("tIsComment", tIsComment);
		executeHttpMethod(listener, TaskId.TASK_URL_UPDATE_TASK_COMMENT, URL_UPDATE_TASK_COMMENT, POST_TYPE, MTHttpManager.HTTP_POST, params, false);
	}

}
