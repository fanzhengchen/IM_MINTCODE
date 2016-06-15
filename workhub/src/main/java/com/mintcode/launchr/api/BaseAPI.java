package com.mintcode.launchr.api;


import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchrnetwork.MTHttpParameters;
import com.mintcode.launchrnetwork.MTServerTalker;
import com.mintcode.launchrnetwork.OnResponseListener;

public abstract class BaseAPI {
	
	// 根据请求参数Header里的type不同，在接口里设置
	public static final String DELETE_TYPE = "DELETE";
	
	public static final String POST_TYPE = "POST";
	
	public static final String GET_TYPE = "GET";
	
	public static final String PUT_TYPE = "PUT";
	
	protected MTServerTalker mServerTalker;

	public BaseAPI() {
		mServerTalker = MTServerTalker.getInstance();
	}

	/**
	 * 
	 * @param listener
	 * @param taskId
	 * @param url
	 * @param httpMethod
	 * @param type       根据请求的type设置
	 * @param params
	 * @param getRawData
	 */
	protected void executeHttpMethod(OnResponseListener listener, String taskId, String url,String type, String httpMethod,MTHttpParameters params, boolean getRawData) {
		// 
		MTHttpParameters p = new MTHttpParameters();
		
		MTHttpParameters body = new MTHttpParameters();
		body.setParameter("param",params.getMap());
		p.setParameter("Body", body.getMap());
		
		HeaderParam entity = LauchrConst.header;
//		entity.setCompanyCode(LauchrConst.COMPANY_CODE);
		
//		Log.i("infos", entity.getCompanyCode() + "====");
		
		entity.setType(type);
		entity.setLanguage("zh-cn");
//		entity.setLanguage("ja-jp");
		entity.setResourceUri(url);
		p.setParameter("Header", entity);
		
//		String s = p.toJson();
		mServerTalker.executeHttpMethod(listener, taskId, LauchrConst.SERVER_PATH, httpMethod, p, getRawData);
	}
	
}

