package com.mintcode.launchr.api;

import android.util.Log;

import com.mintcode.chat.entity.MergeEntity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.message.activity.VerifyFriendMessageActivity;
import com.mintcode.launchrnetwork.MTHttpManager;
import com.mintcode.launchrnetwork.MTHttpParameters;
import com.mintcode.launchrnetwork.MTServerTalker;
import com.mintcode.launchrnetwork.OnResponseListener;

import java.util.List;
import java.util.Map;

/**
 * IM Api ,未来逐渐替换IMApi
 * @author KevinQiao
 *
 */
public class IMNewApi{

	protected MTServerTalker mServerTalker;

	private static IMNewApi instance = new IMNewApi();

	/** ip */
	private static final String IP = LauchrConst.httpIp + "/launchr";


	/** 已读请求url */
	private static final String URL_READ_SESSION = IP + "/api/readsession";

	/** 获取会话历史信息 */
	private static final String URL_HISTORYMESSAGE = IP + "/api/historymessage";
	/** 标记重点*/
	private static final String ADD_BOOK_MARK = IP + "/api/addbookmark";

	/** 取消标记重点*/
	private static final String DELETE_BOOK_MARK = IP + "/api/deletebookmark";

	/** 删除会话*/
	private static final String DELETE_ONE_SESSION = IP + "/api/removesession";

	/** 用户登出 IM */
	private static final String URL_LOGIN_OUT = IP + "/api/loginout";
	/** 精确搜索用户*/
	private static final String URL_SEARCH_USER = IP + "/api/searchRelationUser";
	/** 模糊搜索*/
	private static final String URL_FUZZY_SEARCH_USER = IP + "/api/searchRelation";
	/** 获取好友列表*/
	private static final String URL_FRIEND_LIST = IP + "/api/relationGroupInfoList";
	/** 好友请求*/
	private static final String URL_RELATION_VALIDATA = IP + "/api/addRelationValidate";
	/** 好友验证处理*/
	private static final String URL_DISPOSE_VALIDATA = IP + "/api/disposeRelationValidate";
	/** 好友验证列表*/
	private static final String URL_GET_VALIDATE_LIST = IP + "/api/relationValidateList";
	/** 删除好友*/
	private static final String URL_DELECT_FRIEND = IP + "/api/deleteRelation";
	/** 修改好友备注名*/
	private static final String URL_MOTFY_REMARK = IP + "/api/remarkRelation";
	/** 增量好友分组*/
	private static final String URL_LOAD_RELATION_GROUP = IP + "/api/loadRelationGroupInfo";

	/** 批量添加重点*/
	private static final String URL_ADD_BOOK_MARK_LIST = IP + "/api/addbookmarklist";
	/** 消息合并转发*/
	private static final String URL_FORWORD_MERGE_MESSAGE = IP + "/api/forwardMergeMessage";
	/** 获取群列表*/
	private static final String URL_GET_MY_GROUP_LIST =  IP + "/api/grouplist";

	public static final class TaskId{
		public static final String TASK_READ_SESSION = "task_read_session";
		public static final String TASK_HISTORYMESSAGE = "task_historymessage";
		public static final String ADD_BOOK_MARK = "task_url_add_book_mark";
		public static final String DELETE_BOOK_MARK = "task_url_delete_mark";
		public static final String DELETE_ONE_SESSION = "task_url_delete_one_session";
		public static final String TASK_URL_LOGIN_OUT = "task_url_login_out";

		public static final String TASK_SEARCH_USER = "task_search_user";
		public static final String TASK_FUZZY_SEARCH_USER = "task_fuzzy_search_user";
		public static final String TASK_FRIENDS_LIST = "task_friends_list";
		public static final String TASK_RELATION_VALIDATA = "task_relation_validata";
		public static final String TASK_DISPOSE_VALIDATA = "task_dispose_validata";
		public static final String GET_VALIDATE_LISTTASK = "get_validate_listtask";
		public static final String TASK_URL_DELECT_FRIEND = "task_url_delect_friend";
		public static final String TASK_MOTIFY_REMARK = "task_motify_remark";
		public static final String TASK_LOAD_RELATION_INFO = "task_load_relation_info";

		public static final String TASK_ADD_BOOK_MARK_LIST = "task_add_book_mark_list";
		public static final String TASK_FORWORD_MERGE_MESSAGE = "task_forword_merge_message";
		public static final String TASK_URL_GET_MY_GROUP_LIST = "task_url_get_my_group_list";
	}


	private IMNewApi(){
		mServerTalker = MTServerTalker.getInstance();
	}
	
	public static IMNewApi getInstance(){
		return instance;
	}

	/**
	 * 消息已读
	 * @param listener
	 * @param userToken
	 * @param userName
	 * @param sessionName
	 * @param content
	 */
	public void readSession(OnResponseListener listener, String userToken,
							String userName, String sessionName, List<Map> content) {
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		params.setParameter("sessionName", sessionName);
		params.setParameter("content", content);
//		String s = params.toJson();
		mServerTalker.executeHttpMethod(listener, TaskId.TASK_READ_SESSION, URL_READ_SESSION, MTHttpManager.HTTP_POST, params, false);
	}

	/**
	 * 获取历史信息
	 * @param listener
	 * @param userToken
	 * @param userName
	 * @param from
	 * @param to
	 * @param limit
	 * @param endTimeStamp
	 */
	public void getHistoryMessage(OnResponseListener listener, String userToken, String userName,
								  String from, String to, long limit, long endTimeStamp){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		params.setParameter("to", to);
		params.setParameter("from", from);
		params.setLongParameter("limit", limit);
		//params.setLongParameter("startTimestamp", startTimeStamp);
		params.setLongParameter("endTimestamp", endTimeStamp);
		Log.i("userToken-ken", userToken + "---");
		String s = params.toJson();
		mServerTalker.executeHttpMethod(listener, TaskId.TASK_HISTORYMESSAGE, URL_HISTORYMESSAGE, MTHttpManager.HTTP_POST, params, false);
	}

	/**
	 * 标记重点
	 * @param listener
	 * @param userToken
	 * @param userName
	 * @param msgId
	 * @param sessionName
	 */
	public void addBookMark(OnResponseListener listener, String userToken,
							String userName, long msgId, String sessionName){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		params.setLongParameter("msgId", msgId);
		params.setParameter("sessionName", sessionName);
		mServerTalker.executeHttpMethod(listener, TaskId.ADD_BOOK_MARK, ADD_BOOK_MARK, MTHttpManager.HTTP_POST, params, false);
	}

	/**
	 * 取消标记重点
	 * @param listener
	 * @param userToken
	 * @param userName
	 * @param msgId
	 * @param sessionName
	 */
	public void deleteBookMark(OnResponseListener listener, String userToken,
							   String userName, long msgId, String sessionName){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		params.setLongParameter("msgId", msgId);
		params.setParameter("sessionName", sessionName);
		mServerTalker.executeHttpMethod(listener, TaskId.DELETE_BOOK_MARK, DELETE_BOOK_MARK, MTHttpManager.HTTP_POST, params, false);
	}

	/** 移除某条会话*/
	public void deleteOneSession(OnResponseListener listener, String userToken, String userName, String sessionName){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		params.setParameter("sessionName", sessionName);
		mServerTalker.executeHttpMethod(listener, TaskId.DELETE_ONE_SESSION, DELETE_ONE_SESSION, MTHttpManager.HTTP_POST, params, false);
	}

	/**
	 * 用户登出接口
	 * @param listener
	 * @param userToken
	 * @param userName
	 */
	public void loginOut(OnResponseListener listener, String userToken, String userName){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		mServerTalker.executeHttpMethod(listener, TaskId.TASK_URL_LOGIN_OUT, URL_LOGIN_OUT, MTHttpManager.HTTP_POST, params, false);
	}

	/** 精确搜索用户*/
	public void searchUser(OnResponseListener listener, String userName,String value){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userName", userName);
		params.setParameter("relationValue", value);
		mServerTalker.executeHttpMethod(listener, TaskId.TASK_SEARCH_USER,  URL_SEARCH_USER,
				MTHttpManager.HTTP_POST, params, false);
	}
	/** 模糊搜索用户*/
	public void fuzzySearchUser(OnResponseListener listener, String userName,String value,int limit){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userName", userName);
		params.setParameter("relationValue", value);
		params.setParameter("limit", limit);
//		params.setParameter("startTimestamp", startTimestamp);
		params.setParameter("endTimestamp", -1);
		mServerTalker.executeHttpMethod(listener, TaskId.TASK_FUZZY_SEARCH_USER,  URL_FUZZY_SEARCH_USER,
				MTHttpManager.HTTP_POST, params, false);
	}
	/** 获取好友列表*/
	public void getFriendList(OnResponseListener listener, String userName){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userName", userName);
		mServerTalker.executeHttpMethod(listener, TaskId.TASK_FRIENDS_LIST,URL_FRIEND_LIST,
				MTHttpManager.HTTP_POST, params, false);
	}
	/** 请求验证好友*/
	public void sendRelationValidate(OnResponseListener listener, String userName,String from,String nickName,
									 String fromAvatar,String to,String content,int relationGroupId,String remark){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userName", userName);
		params.setParameter("from", from);
		params.setParameter("fromNickName", nickName);
		params.setParameter("fromAvatar", fromAvatar);
		params.setParameter("to", to);
		params.setParameter("content", content);
		params.setParameter("relationGroupId", relationGroupId);
		params.setParameter("remark", remark);
		mServerTalker.executeHttpMethod(listener, TaskId.TASK_RELATION_VALIDATA, URL_RELATION_VALIDATA,
				MTHttpManager.HTTP_POST, params, false);
	}
	/** 好友验证处理*/
	public void dealRelationValidata(OnResponseListener listener,String userName,String fromNickName,String from,
									 String fromAvatar,String to,String remark,String content,int relationGroupId,int validateState){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userName",userName);
		params.setParameter("fromNickName",fromNickName);
		params.setParameter("from",from);
		params.setParameter("fromAvatar",fromAvatar);
		params.setParameter("to",to);
		params.setParameter("remark",remark);
		params.setParameter("content",content);
		if(validateState == VerifyFriendMessageActivity.DEAL_AGRESS){
			params.setParameter("relationGroupId",relationGroupId);
		}
		params.setParameter("validateState",validateState);
		mServerTalker.executeHttpMethod(listener, TaskId.TASK_DISPOSE_VALIDATA, URL_DISPOSE_VALIDATA,
				MTHttpManager.HTTP_POST, params, false);
	}
	/** 获得验证列表*/
	public void getValidataList(OnResponseListener listener,String userName,int limit,long endTimestamp){
		//,long startTimestamp
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userName", userName);
		params.setParameter("limit", limit);
//		params.setParameter("startTimestamp", startTimestamp);
		params.setParameter("endTimestamp",endTimestamp);
		mServerTalker.executeHttpMethod(listener, TaskId.GET_VALIDATE_LISTTASK, URL_GET_VALIDATE_LIST,
				MTHttpManager.HTTP_POST, params, false);
	}
	/** 删除好友*/
	public void deleteRelation(OnResponseListener listener,String userName,String to){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userName", userName);
		params.setParameter("to", to);
		mServerTalker.executeHttpMethod(listener, TaskId.TASK_URL_DELECT_FRIEND, URL_DELECT_FRIEND,
				MTHttpManager.HTTP_POST, params, false);
	}
	/** 修改好友备注*/
	public void motifyFriendRmark(OnResponseListener listener,String userName,String relationName,String remark){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userName", userName);
		params.setParameter("relationName", relationName);
		params.setParameter("remark", remark);
		mServerTalker.executeHttpMethod(listener, TaskId.TASK_MOTIFY_REMARK, URL_MOTFY_REMARK,
				MTHttpManager.HTTP_POST, params, false);
	}
	/** 好友增量*/
	public void loadRelationInfo(OnResponseListener listener,String userName,long time){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userName", userName);
		params.setParameter("msgId",time);
		mServerTalker.executeHttpMethod(listener, TaskId.TASK_LOAD_RELATION_INFO, URL_LOAD_RELATION_GROUP,
				MTHttpManager.HTTP_POST, params, false);
	}
	/** 批量标记重点*/
	public void addBookMarkList(OnResponseListener listener, String userToken,
								String userName, List<Long> msgIds, String sessionName){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		params.setParameter("msgIds", msgIds);
		params.setParameter("sessionName", sessionName);
		mServerTalker.executeHttpMethod(listener, TaskId.TASK_ADD_BOOK_MARK_LIST, URL_ADD_BOOK_MARK_LIST, MTHttpManager.HTTP_POST, params, false);
	}

	/** 消息合并转发,逐条转发*/
	public void forwordMergeMessage(OnResponseListener listener, String userToken, String userName, String info, List<String> to,
									List<MergeEntity> content, String type, String title, int oneByOne, long clientMsgId){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		params.setParameter("info", info);
		params.setParameter("to", to);
		params.setParameter("content", content);
		params.setParameter("type", type);
		params.setParameter("title", title);
		if(oneByOne == 1){
			params.setIntParameter("oneByOne", oneByOne);   // 逐条转发
		}
		if(clientMsgId != 0){
			params.setLongParameter("clientMsgId", clientMsgId);
		}
		mServerTalker.executeHttpMethod(listener, TaskId.TASK_FORWORD_MERGE_MESSAGE, URL_FORWORD_MERGE_MESSAGE, MTHttpManager.HTTP_POST, params, false);
	}
	/** 获取我的群组信息*/
	public void getMyGroupList(OnResponseListener listener, String userName){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("appName", LauchrConst.appName);
//		params.setParameter("userToken", userToken);
		params.setParameter("userName", userName);
		mServerTalker.executeHttpMethod(listener, TaskId.TASK_URL_GET_MY_GROUP_LIST, URL_GET_MY_GROUP_LIST, MTHttpManager.HTTP_POST, params, false);
	}
}
