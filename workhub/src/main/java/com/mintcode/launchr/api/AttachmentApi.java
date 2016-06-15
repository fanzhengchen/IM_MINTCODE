package com.mintcode.launchr.api;

import com.mintcode.launchrnetwork.MTHttpManager;
import com.mintcode.launchrnetwork.MTHttpParameters;
import com.mintcode.launchrnetwork.OnResponseListener;

import android.provider.CalendarContract.Attendees;

public class AttachmentApi extends BaseAPI {
	
	private static AttachmentApi sInstance;
	
	private static String AttachementBaseUrl = "/Base-Module/Annex";
	
	private AttachmentApi(){
		
	}
	
	public static AttachmentApi getInstance(){
		if(sInstance == null){
			sInstance = new AttachmentApi();
		}
		return sInstance;
	}
	
	public interface AttachmentURL{
		
		String getList = AttachementBaseUrl;
	}
	
	public static final class TaskId{
		public static final  String getList = "AttachmentApi_getlist";
	}
	
	public void getAttachmentList(OnResponseListener listener,String showid,String appshowid){
		MTHttpParameters parameters = new MTHttpParameters();
		parameters.setParameter("showID", showid);
		parameters.setParameter("appShowID", appshowid);
		executeHttpMethod(listener, TaskId.getList, AttachmentURL.getList, GET_TYPE,MTHttpManager.HTTP_POST, parameters, false);
	}
}












