package com.mintcode.launchr.api;

import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import com.mintcode.launchr.pojo.entity.ApproveEntity;
import com.mintcode.launchrnetwork.MTHttpManager;
import com.mintcode.launchrnetwork.MTHttpParameters;
import com.mintcode.launchrnetwork.OnResponseListener;

public class ApproveAPI extends BaseAPI {

	private static ApproveAPI sInstance;

	private static String ApproveBaseUrl = "/Approve-Module/Approve";
	
	private ApproveAPI() {

	}

	public static ApproveAPI getInstance() {
		if (sInstance == null) {
			sInstance = new ApproveAPI();
		}
		return sInstance;
	}
	/** 审批接收列表*/
	private static final String URL_GET_RECEIVED_LIST = ApproveBaseUrl + "/GetApproveReceiveList";
	/** 审批详情*/
	private static final String URL_GET_DETAIL = ApproveBaseUrl;
	/** 处理审批操作*/
	private static final String URL_DEAL_APPROVE = ApproveBaseUrl + "/ApproveProcess";
	/** 新建审批*/
	private static final String URL_ADD_APPROVE = "/Approve-Module/Approve";
	/** 审批发出列表*/
	private static final String URL_GET_SEND_LIST = ApproveBaseUrl + "/GetApproveSendList";
	/** 删除审批*/
	private static final String URL_DELECT_APPROVE = ApproveBaseUrl;
	/** 编辑审批*/
	private static final String URL_EDIT_APPROVE = ApproveBaseUrl;
	/** 搜索审批*/
	private static final String URL_SEARCH_APPROVE = ApproveBaseUrl + "/ApproveSearch";
	/** 转交审批*/
	private static final String URL_TRANSMIT_APPROVE = ApproveBaseUrl + "/ApproveTransmit";
	/** 添加审批评论*/
	private static final String URL_ADD_COMMENT = "/Base-Module/Comment";
	/** 获取审批评论列表*/
	private static final String URL_GET_COMMENT_LIST = "/Base-Module/Comment/GetList";
	/** 删除审批评论*/
	private static final String URL_DELECT_COMMENT_LIST = "/Base-Module/Comment";
	/** 设置审批有评论*/
	private static final String URL_HAS_COMMENT = ApproveBaseUrl + "/PostComment";
	/** 获取审批类型列表*/
	private static final String URL_GET_APPROVAL_TYPE = ApproveBaseUrl + "/GetApproveTypeList";
	/** 获取审批类型对于字段*/
	private static final String URL_APPROVAL_TYPR_FIELD = ApproveBaseUrl + "/GetApproveTypeField";

	/** 新建审批*/
	private static final String URL_NEW_APPROVE_V2 = ApproveBaseUrl + "/PutV2";
	/** 编辑审批*/
	private static final String URL_EDIT_APPROVE_V2 = ApproveBaseUrl;
	/** 删除审批*/
	private static final String URL_DELECT_APPROVE_V2 = ApproveBaseUrl + "/DeleteV2";
	/** 审批详情*/
	private static final String URL_GET_DETAIL_V2 = ApproveBaseUrl + "/GetV2";
	/** 处理审批*/
	private static final String URL_PROCESS_APPROVE_V2 = ApproveBaseUrl + "/ApproveProcessV2";
	/** 转交审批*/
	private static final String URL_TRANSMIT_APPROVE_V2 = ApproveBaseUrl + "/ApproveTransmitV2";
	/** 审批列表*/
	private static final String URL_APPROVE_LIST_V2 = ApproveBaseUrl + "/GetApproveListV2";
	/** 获取审批类型*/
	private static final String URL_GET_APPROVE_TYPE_V2 = ApproveBaseUrl + "/GetApproveTypeList";
	/** 审批是否有评论修改*/
	private static final String URL_POST_COMMENT_V2 = ApproveBaseUrl + "/PostCommentV2";
	/** 获取上次审批人员*/
	private static final String URL_LAST_MEMBER_V2 = ApproveBaseUrl + "/GetLastApproveMemberV2";

	public  static final class TaskId {



		/** 获取接收审批列表*/
		public static final String TASK_URL_GET_LIST = "task_url_get_list";
		/** 获取审批详情*/
		public static final String TASK_URL_GET_DETAIL = "task_url_get_detail";
		/** 新建审批*/
		public static final String TASK_URL_NEW_APPROVE = "task_url_new_approve";
		/** 编辑审批*/
		public static final String TASK_URL_EDIT_APPROVE = "task_url_edit_approve";
		/** 删除审批*/
		public static final String TASK_URL_DELECT_APPROVE = "task_url_delect_approve";
		/** 删除审批评论*/
		public static final String TASK_URL_DELECT_APPROVE_COMMENT = "task_url_delect_approve_comment";
		/** 抄送的审批*/
		public static final String TASK_URL_GET_CC_LIST = "task_url_get_cc_list";
		/** 发出的审批*/
		public static final String TASK_URL_GET_SEND_LIST = "task_url_get_send_list";
		/** 接收的未处理审批*/
		public static final String TASK_URL_GET_RECEIVED_UNDEAL_LIST = "task_url_get_received_undeal_list";
		/** 接收的已处理审批*/
		public static final String TASK_URL_GET_RECEIVED_DEAL_LIST = "task_url_get_received_deal_list";
		/** 处理操作审批*/
		public static final String TASK_URL_DEAL_APPROVE = "task_url_deal_approve";
		/** 搜索审批*/
		public static final String TASK_URL_SEARCH_APPROVE = "task_url_search_approve";
		/** 转交审批*/
		public static final String TASK_URL_TRANSMIT_APPROVE = "task_url_transmit_approve";
		/** 审批添加评论*/
		public static final String TASK_URL_ADD_COMMENT = "task_url_add_comment";
		/** 审批评论列表*/
		public static final String TASK_URL_GET_COMMENT_LIST = "task_url_get_comment_list";
		/** 搜索审批*/
		public static final String TASK_URL_DELECT_COMMENT_LIST = "task_url_delect_comment_list";
		/** 设置审批有评论*/
		public static final String TASK_URL_HAS_COMMENT = "task_url_has_comment";
		/** 审批列表类型列表*/
		public static final String TASK_URL_GET_APPROVAL_TYPE = "task_url_get_approval_type";
		/** 获取审批类型对于字段*/
		public static final String TASK_URL_APPROVAL_TYPR_FIELD= "task_url_approval_typr_field";


		/** 新建审批V2*/
		public static final String TASK_URL_NEW_APPROVE_V2 = "task_url_new_approve_v2";
		/** 编辑审批V2*/
		public static final String TASK_URL_EDIT_APPROVE_V2 = "task_url_edit_approve_v2";
		/** 删除审批V2*/
		public static final String TASK_URL_DELECT_APPROVE_V2 = "task_url_delect_approve_v2";
		/** 审批详情V2*/
		public static final String TASK_URL_GET_DETAIL_V2 = "task_url_get_detail_v2";
		/** 处理审批V2*/
		public static final String TASK_URL_PROCESS_APPROVE_V2 = "task_url_process_approve_v2";
		/** 转交审批V2*/
		public static final String TASK_URL_TRANSMIT_APPROVE_V2 = "task_url_transmit_approve_v2";
		/** 审批列表V2*/
		public static final String TASK_URL_APPROVE_LIST_V2 = "task_url_approve_list_v2";
		/** 获取审批类型V2*/
		public static final String TASK_URL_GET_APPROVE_TYPE_V2 = "task_url_get_approve_type_v2";
		/** 审批是否有评论修改V2*/
		public static final String TASK_URL_POST_COMMENT_V2 = "task_url_post_comment_v2";
		/** 获取上次审批人员V2*/
		public static final String TASK_URL_LAST_MEMBER_V2 = "task_url_last_member_v2";
	}
	/**设置有评论 **/
	public void setHasComment(OnResponseListener listener, String showid,int hasComment ){
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("SHOW_ID", showid);
		parameters.setParameter("HAS_COMMENT", hasComment);
		executeHttpMethod(listener,TaskId.TASK_URL_HAS_COMMENT,URL_HAS_COMMENT,POST_TYPE,MTHttpManager.HTTP_POST, parameters, false);
	}
	/** 获取*/
	public void getApproveField(OnResponseListener listener, String showid){
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("T_SHOW_ID", showid);
		executeHttpMethod(listener,TaskId.TASK_URL_APPROVAL_TYPR_FIELD,URL_APPROVAL_TYPR_FIELD,GET_TYPE,MTHttpManager.HTTP_POST, parameters, false);
	}
	/** 获取审批列表*/
	public void getApproveList(OnResponseListener listener, String type,String which,int index,int size,Long time,int  iS_PROCESS) {
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("A_TYPE", type);
		parameters.setParameter("pageIndex", index);
		parameters.setParameter("pageSize", size);
//		parameters.setParameter("timeStamp", time);
		parameters.setParameter("IS_PROCESS", iS_PROCESS);
		executeHttpMethod(listener, which,URL_GET_RECEIVED_LIST, GET_TYPE, MTHttpManager.HTTP_POST, parameters, false);
	}
	/** 抄送的审批*/
	public void getCopyList(OnResponseListener listener, String type,int index,int size,Long time) {
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("A_TYPE", type);
		parameters.setParameter("pageIndex", index);
		parameters.setParameter("pageSize", size);
		parameters.setParameter("timeStamp", time);
		executeHttpMethod(listener,TaskId.TASK_URL_GET_CC_LIST,URL_GET_RECEIVED_LIST, GET_TYPE,MTHttpManager.HTTP_POST, parameters, false);
	}
	/** 获取审批详情*/
	public void getApproveDetail(OnResponseListener listener, String showId) {
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("SHOW_ID", showId);		
		executeHttpMethod(listener,TaskId.TASK_URL_GET_DETAIL,URL_GET_DETAIL, GET_TYPE,
				MTHttpManager.HTTP_POST, parameters, false);
	}

	/** 发出的审批*/
	public void getSendApproveList(OnResponseListener listener,int index,int size,Long time){


		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("pageIndex", index);
		parameters.setParameter("pageSize", size);
		parameters.setParameter("timeStamp", time);
		executeHttpMethod(listener,TaskId.TASK_URL_GET_SEND_LIST,URL_GET_SEND_LIST,GET_TYPE, MTHttpManager.HTTP_POST, parameters, false);

	}
	/** 新建审批*/
	public void addApprove(OnResponseListener listener, ApproveEntity entitiy) {
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("A_TITLE", entitiy.getA_TITLE());
		parameters.setParameter("T_SHOW_ID", entitiy.getT_SHOW_ID());
		parameters.setParameter("A_APPROVE", entitiy.getA_APPROVE());
		parameters.setParameter("A_APPROVE_NAME", entitiy.getA_APPROVE_NAME());
		parameters.setParameter("A_CC", entitiy.getA_CC());
		parameters.setParameter("A_CC_NAME", entitiy.getA_CC_NAME());
		parameters.setParameter("A_BACKUP", entitiy.getA_BACKUP());
		parameters.setParameter("A_START_TIME", entitiy.getA_START_TIME());
		parameters.setParameter("A_END_TIME", entitiy.getA_END_TIME());
		parameters.setParameter("A_FEE", entitiy.getA_FEE());
		parameters.setParameter("A_IS_URGENT", entitiy.getA_IS_URGENT());
		parameters.setParameter("A_DEADLINE", entitiy.getA_DEADLINE());
		parameters.setParameter("IS_TIMESLOT_ALL_DAY",entitiy.getIS_TIMESLOT_ALL_DAY());
		parameters.setParameter("IS_DEADLINE_ALL_DAY",entitiy.getIS_DEADLINE_ALL_DAY());
		parameters.setParameter("fileShowIds",entitiy.getFileShowIds());
		executeHttpMethod(listener, TaskId.TASK_URL_NEW_APPROVE, URL_ADD_APPROVE,
				PUT_TYPE, MTHttpManager.HTTP_POST, parameters, false);
	}
	/** 删除审批*/
	public void delectApproval(OnResponseListener listener, String showid) {
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("SHOW_ID", showid);
		executeHttpMethod(listener, TaskId.TASK_URL_DELECT_APPROVE,
				URL_DELECT_APPROVE, DELETE_TYPE,
				MTHttpManager.HTTP_POST, parameters, false);
	}
	/** 编辑审批*/
	public void editApproval(OnResponseListener listener, ApproveEntity entitiy) {
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("SHOW_ID", entitiy.getSHOW_ID());
		parameters.setParameter("A_TITLE", entitiy.getA_TITLE());
		parameters.setParameter("T_SHOW_ID", entitiy.getT_SHOW_ID());
		parameters.setParameter("A_CC", entitiy.getA_CC());
		parameters.setParameter("A_CC_NAME", entitiy.getA_CC_NAME());
		parameters.setParameter("A_BACKUP", entitiy.getA_BACKUP());
		parameters.setParameter("A_START_TIME", entitiy.getA_START_TIME());
		parameters.setParameter("A_END_TIME", entitiy.getA_END_TIME());
		parameters.setParameter("A_FEE", entitiy.getA_FEE());
		parameters.setParameter("A_IS_URGENT", entitiy.getA_IS_URGENT());
		parameters.setParameter("A_DEADLINE", entitiy.getA_DEADLINE());
		parameters.setParameter("IS_TIMESLOT_ALL_DAY",entitiy.getIS_TIMESLOT_ALL_DAY());
		parameters.setParameter("IS_DEADLINE_ALL_DAY",entitiy.getIS_DEADLINE_ALL_DAY());
		parameters.setParameter("fileShowIds",entitiy.getFileShowIds());
		executeHttpMethod(listener, TaskId.TASK_URL_EDIT_APPROVE, URL_EDIT_APPROVE,
				POST_TYPE, MTHttpManager.HTTP_POST, parameters, false);

	}
	/** 处理审批操作*/
	public void dealApprove(OnResponseListener listener, String showId,
			String status, String reason) {
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("SHOW_ID", showId);
		parameters.setParameter("A_STATUS", status);
		parameters.setParameter("A_REASON", reason);
		executeHttpMethod(listener,TaskId.TASK_URL_DEAL_APPROVE, URL_DEAL_APPROVE, POST_TYPE,
				MTHttpManager.HTTP_POST, parameters, false);
	}
	/** 搜索审批*/
	public void searchApprove(OnResponseListener listener, String keyWord) {
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("A_KEYWORD", keyWord);
		executeHttpMethod(listener, TaskId.TASK_URL_SEARCH_APPROVE, URL_SEARCH_APPROVE,
				GET_TYPE, MTHttpManager.HTTP_POST, parameters, false);
	}
	/** 转交审批*/
	public void TransmitApprove(OnResponseListener listener, String showId,
			String approve, String approveName, String reason) {
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("SHOW_ID", showId);
		parameters.setParameter("A_APPROVE", approve);
		parameters.setParameter("A_APPROVE_NAME", approveName);
		parameters.setParameter("A_REASON", reason);
		executeHttpMethod(listener,TaskId.TASK_URL_TRANSMIT_APPROVE, URL_TRANSMIT_APPROVE, POST_TYPE,
				MTHttpManager.HTTP_POST, parameters, false);
	}
	
	/** 新增评论*/
	public void addComment(OnResponseListener listener, String appShowID, String rm_ShowID, String comment, int isNotComment, String fileShowID,
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
		executeHttpMethod(listener, TaskId.TASK_URL_ADD_COMMENT,URL_ADD_COMMENT, PUT_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
    
	/** 获取评论列表*/
	public void getCommentList(OnResponseListener listener, String appShowID, String rm_ShowID,int pageIndex, int pageSize, long timeStamp){
		List<String> strings = new ArrayList<>();
		strings.add("Comment");
		strings.add("Attachment");
		strings.add("Operation");
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appShowID", appShowID);
		params.setParameter("rm_ShowID", rm_ShowID);
		params.setParameter("filterTypes",strings);
		params.setIntParameter("pageIndex", pageIndex);
		params.setIntParameter("pageSize", pageSize);
		params.setLongParameter("timeStamp", timeStamp);
		params.setParameter("sort","desc");
		executeHttpMethod(listener, TaskId.TASK_URL_GET_COMMENT_LIST, URL_GET_COMMENT_LIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	/** 删除评论*/
	public void delectConmment(OnResponseListener listener, String showID){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showID", showID);
		executeHttpMethod(listener,TaskId.TASK_URL_DELECT_APPROVE_COMMENT,URL_DELECT_COMMENT_LIST, DELETE_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	/** 获取审批类型*/
	public void getApprovalType(OnResponseListener listener){
		MTHttpParameters params = new MTHttpParameters();
		executeHttpMethod(listener, TaskId.TASK_URL_GET_APPROVAL_TYPE,URL_GET_APPROVAL_TYPE, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}


	/** V2新建审批*/
	public void newApproveV2(OnResponseListener listener,String title,String showId,String approve,
							 String approveName,String cc,String ccName,String workFlow,String form,
							 String formData,int isUrgent,List<String> fileShowIds) {
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("aTitle", title);
		parameters.setParameter("tShowId",showId);
		parameters.setParameter("aApprove",approve);
		parameters.setParameter("aApproveName",approveName);
		parameters.setParameter("aCc", cc);
		parameters.setParameter("aCcName", ccName);
		parameters.setParameter("tWorkflowId",workFlow);
		parameters.setParameter("aForm",form);
		parameters.setParameter("aFormData",formData);
		parameters.setParameter("aIsUrgent",isUrgent);
		parameters.setParameter("fileShowIds",fileShowIds);
		executeHttpMethod(listener,TaskId.TASK_URL_NEW_APPROVE_V2, URL_NEW_APPROVE_V2,
				PUT_TYPE, MTHttpManager.HTTP_POST, parameters, false);
	}
	/** V2编辑审批*/
	public void delectApproveV2(OnResponseListener listener,String showId){
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("showId",showId);
		executeHttpMethod(listener,TaskId.TASK_URL_DELECT_APPROVE_V2, URL_DELECT_APPROVE_V2,
				DELETE_TYPE, MTHttpManager.HTTP_POST, parameters, false);
	}
	/** V2获取审批详情*/
	public void getApproveDetailV2(OnResponseListener listener,String showId){
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("showId",showId);
		executeHttpMethod(listener,TaskId.TASK_URL_GET_DETAIL_V2,URL_GET_DETAIL_V2,
				GET_TYPE, MTHttpManager.HTTP_POST, parameters, false);
	}
	/** V2处理审批*/
	public void processApprove(OnResponseListener listener,String showId,String status,String reason){
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("showId",showId);
		parameters.setParameter("aStatus",status);
		parameters.setParameter("aReason",reason);
		executeHttpMethod(listener,TaskId.TASK_URL_PROCESS_APPROVE_V2,URL_PROCESS_APPROVE_V2,
				POST_TYPE, MTHttpManager.HTTP_POST, parameters, false);
	}
	/** V2转交审批*/
	public void transmitApproveV2(OnResponseListener listener,String showId,String approve,String approveName,String reason){
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("showId",showId);
		parameters.setParameter("aApprove",approve);
		parameters.setParameter("aApproveName",approveName);
		parameters.setParameter("aReason",reason);
		executeHttpMethod(listener,TaskId.TASK_URL_TRANSMIT_APPROVE_V2,URL_TRANSMIT_APPROVE_V2,
				POST_TYPE, MTHttpManager.HTTP_POST, parameters, false);
	}
	/** V2获取审批列表*/
	public void getApproveListV2(OnResponseListener listener,String key,int index,int size,long time){
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("aKeyword",key);
		parameters.setParameter("pageIndex",index);
		parameters.setParameter("pageSize",size);
//		parameters.setParameter("timeStamp",time);
		executeHttpMethod(listener,TaskId.TASK_URL_APPROVE_LIST_V2,URL_APPROVE_LIST_V2,
				GET_TYPE, MTHttpManager.HTTP_POST, parameters, false);
	}
	/** V2获取审批类型*/
	public void getApproveTypeList(OnResponseListener listener){
		MTHttpParameters parameters = new MTHttpParameters();
		executeHttpMethod(listener,TaskId.TASK_URL_GET_APPROVE_TYPE_V2,URL_GET_APPROVE_TYPE_V2,
				GET_TYPE, MTHttpManager.HTTP_POST, parameters, false);
	}
	/** V2审批有评论*/
	public void postCommentV2(OnResponseListener listener,String showId,int hasComment){
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("showId",showId);
		parameters.setParameter("hasComment",hasComment);
		executeHttpMethod(listener,TaskId.TASK_URL_POST_COMMENT_V2,URL_POST_COMMENT_V2,
				POST_TYPE, MTHttpManager.HTTP_POST, parameters, false);
	}
	/** V2获取上次审批人员*/
	public void getLastApproveMember(OnResponseListener listener){
		MTHttpParameters parameters = new MTHttpParameters();
		executeHttpMethod(listener,TaskId.TASK_URL_LAST_MEMBER_V2,URL_LAST_MEMBER_V2,
				GET_TYPE, MTHttpManager.HTTP_POST, parameters, false);
	}
}














