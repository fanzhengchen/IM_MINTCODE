package com.mintcode.launchr.api;

import com.mintcode.launchr.api.TaskApi.TaskId;
import com.mintcode.launchrnetwork.MTHttpManager;
import com.mintcode.launchrnetwork.MTHttpParameters;
import com.mintcode.launchrnetwork.OnResponseListener;

import java.util.List;

public class MessageAPI extends BaseAPI {

	
	
	
	private static MessageAPI taskApi = new MessageAPI();
	
	private  static final String URL_GET_MESSAGE_LIST = "/Base-Module/Message/MessageList";
	
	public static final class MessageId{
		public static final String MESSAGE_URL_GET_MESSAGE_LIST = "message_url_get_message_list";
		public static final String MESSAGE_URL_GET_SPEACH_MESSAGE_LIST = "message_url_get_speach_message_list";
	}
	
	public MessageAPI(){
		
	}
	
	public static MessageAPI getInstance(){
		return taskApi;
	}
	
	public void getMessageList(OnResponseListener listener,int pageIndex,int pagesize,Long timestamp,String appshowid,String searchkey,int readStaus,List<String> appMessageType,int handlestatus,int messagetype){
		MTHttpParameters params = new MTHttpParameters();//,int handlestatus,int messagetype,String messageapptype,int handlestatus
		params.setParameter("pageIndex", pageIndex);
		params.setParameter("pageSize", pagesize);
		params.setParameter("timeStamp",timestamp);
		params.setParameter("appShowID", appshowid);
		params.setParameter("searchKey", searchkey);
		params.setParameter("readStatus", readStaus);
		params.setParameter("appMessageType",appMessageType);
//		params.setParameter("messageType", 1);
//		params.setParameter("handleStatus", handlestatus);
//		params.setParameter("messageType", messagetype);
//		params.setParameter("messageAppType", messageapptype);
		executeHttpMethod(listener, MessageId.MESSAGE_URL_GET_MESSAGE_LIST,URL_GET_MESSAGE_LIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	
	}
	public void getSpeachMessageList(OnResponseListener listener,int pageIndex,int pagesize,Long timestamp,String appshowid,String searchkey,int readStaus,List<String> appMessageType){
		MTHttpParameters params = new MTHttpParameters();//,int handlestatus,int messagetype,String messageapptype,int handlestatus
		params.setParameter("pageIndex", pageIndex);
		params.setParameter("pageSize", pagesize);
		params.setParameter("timeStamp",timestamp);
		params.setParameter("appShowID", appshowid);
		params.setParameter("searchKey", searchkey);
		params.setParameter("readStatus", readStaus);
		params.setParameter("appMessageType",appMessageType);
//		params.setParameter("handleStatus", handlestatus);
//		params.setParameter("messageType", 1);
//		params.setParameter("messageAppType", messageapptype);
		executeHttpMethod(listener, MessageId.MESSAGE_URL_GET_SPEACH_MESSAGE_LIST,URL_GET_MESSAGE_LIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	
	}
	
}








