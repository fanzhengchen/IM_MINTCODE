package com.mintcode.launchr.api;

import android.util.Log;

import com.mintcode.launchr.pojo.entity.BackPasswordMailEntity;
import com.mintcode.launchrnetwork.MTHttpManager;
import com.mintcode.launchrnetwork.MTHttpParameters;
import com.mintcode.launchrnetwork.OnResponseListener;

import java.util.List;

/**
 * 用户登录api
 * @author KevinQiao
 *
 */
public class UserApi extends BaseAPI{

	private static UserApi userApi = new UserApi();
	
	/** 用户登录 */
	private static final String URL_USER_LOGIN = "/Base-Module/CompanyUserLogin";
	
	/** 获取用户信息 */
	private static final String URL_GET_COMPANY_USER_INFO = "/Base-Module/CompanyUser";

	/** 获取用户列表 */
	private static final String URL_GET_COMPANY_USER_LIST = "/Base-Module/CompanyUser/GetList";
	
	/** 获取部门列表 */
	private static final String URL_GET_COMANY_DEPT_LIST = "/Base-Module/CompanyDept/GetList";
	/** 修改用户密码 **/
	private static final String URL_UPDATE_USER_PASSWD = "/Base-Module/CompanyUser/UpdatePassword";
	
	private static final String URL_UPDATE_USER_INFO = "/Base-Module/CompanyUser";
	
	/** 登录验证 */
	private static final String URL_VALIDATE_LOGIN = "/Base-Module/CompanyUserLogin/CompanyUserValidate";
	
	/**
	 * 二维码验证
	 */
	private static final String URL_QRCODE_CONNECTION = "/Base-Module/CompanyUserLogin/QRcodeConnection";

	/** 用户最后更新时间*/
	private static final String URL_USER_LAST_UPDATE = "/Base-Module/CompanyUser/GetLastUpdateTime";

	/** 部门最后更新时间*/
	private static final String URL_DEPT_LAST_UPDATE = "/Base-Module/CompanyDept/GetLastUpdateTime";

	/** 获取公司关联APP信息*/
	private static final String URL_GET_COMPANY_APP = "/Base-Module/App/GetCompanyApp";
	/** 判断用户是否为公司员工*/
	private static final String URL_GET_USER_ISCOMANY = "/Base-Module/CompanyUser/Exists";

	/** 通过邮件找回密码，第一步新增邮件验证器*/
	private static final String URL_GET_BACK_MAIL_PASSWORD = "/Base-Module/EmailValidator";
	/** 通过邮件找回密码，第二步通过http发送邮件*/
	private static final String URL_GET_BACK_MAIL_PASSWORD1 = "/Base-Module/EmailValidator/SendEmail";

	/** 企业是否存在*/
	private static final String URL_COMPANG_IS_EXIST = "/Base-Module/CompanyUserRegister/GetCompanyExist";
	/** 邮箱是否存在*/
	private static final String URL_MAIL_IS_EXIST = "/Base-Module/CompanyUserRegister/GetMailExist";
	/** 创建新公司*/
	private static final String URL_NEW_COMANY = "/Base-Module/CompanyUserRegister/PutCompanyUserRegister";
	/** 注册账号*/
	private static final String URL_NEW_SIGN_ACOUNT = "/Base-Module/CompanyUserRegister";

	public static final class TaskId{
		public static final String TASK_URL_USER_LOGIN = "task_url_user_login";
		public static final String TASK_URL_GET_COMPANY_USER_INFO = "task_url_get_company_user_info";
		public static final String TASK_URL_GET_COMPANY_USER_LIST = "task_url_get_compay_user_list";
		public static final String TASK_URL_GET_COMANY_DEPT_LIST = "task_url_get_comany_dept_list";
		public static final String TASK_URL_UPDATE_USER_PASSWD = "task_url_update_user_passwd";
		public static final String TASK_URL_UPDATE_USER_INFO = "task_url_update_user_info";
		public static final String TASK_URL_VALIDATE_LOGIN = "task_url_validate_login";
		public static final String TASK_URL_QRCODE_CONNECTION = "task_url_qrcode_connection";
		public static final String TASK_URL_SEARCH_USER = "task_url_search_user";
		public static final String TASK_URL_SEARCH_DEP = "task_url_search_dep";
		public static final String TASK_URL_USER_LAST_UPDATE = "task_url_user_last_update";
		public static final String TASK_URL_DEPT_LAST_UPDATE = "task_url_dept_last_update";
		public static final String TASK_URL_GET_COMPANY_APP = "task_url_get_company_app";
		public static final String TASK_URL_GET_USER_ISCOMPANY = "task_url_get_user_iscompany";
		public static final String TASK_URL_GET_BACK_MAIL_PASSWORD = "task_url_forget_password";
		public static final String TASK_URL_GET_BACK_MAIL_PASSWORD1 = "task_url_forget_password1";
		public static final String TASK_URL_COMPANY_IS_EXIST = "task_url_company_is_exist";
		public static final String TASK_URL_MAIL_IS_EXIST = "task_url_mail_is_exist";
		public static final String TASK_URL_NEW_COMPANY = "task_url_new_company";
		public static final String TASK_URL_NEW_ACCOUNT = "task_url_new_account";
	}
	
	
	
	private UserApi(){
		
	}
	
	public static UserApi getInstance(){
		return userApi;
	}
	
	
	public void updateUserInfo(OnResponseListener listener,String userNumber, String dept, String job, String mail, String showid,
							   String mobile, String truename,String telephone){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("U_NUMBER", userNumber);
		params.setParameter("U_DEPT_ID", dept);
		params.setParameter("U_JOB", job);
		params.setParameter("U_MAIL", mail);
		params.setParameter("SHOW_ID", showid);
		params.setParameter("U_TRUE_NAME", truename);	
		params.setParameter("U_TELEPHONE", telephone);
		params.setParameter("U_MOBILE", mobile);
		params.setParameter("U_STATUS", "1");
		executeHttpMethod(listener, TaskId.TASK_URL_UPDATE_USER_INFO, URL_UPDATE_USER_INFO, POST_TYPE, MTHttpManager.HTTP_POST, params, false);
	
	}
	/**
	 * 修改密码
	 * @param listener
	 * @param showid
	 * @param oldpassword
	 * @param newpassword
	 */
	public void updatePassWd(OnResponseListener listener,String showid,String oldpassword,String newpassword){
		MTHttpParameters params = new MTHttpParameters();
		
		params.setParameter("showId", showid);
		params.setParameter("oldPassword", oldpassword);	
		params.setParameter("newPassword", newpassword);	
		executeHttpMethod(listener,TaskId.TASK_URL_UPDATE_USER_PASSWD,URL_UPDATE_USER_PASSWD,POST_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 登录验证方法
	 * @param listener
	 * @param account
	 * @param pwd
	 */
	public void loginValidate(OnResponseListener listener, String account, String pwd){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("userLoginName", account);
		params.setParameter("userPassword", pwd);
		
		executeHttpMethod(listener, TaskId.TASK_URL_VALIDATE_LOGIN, URL_VALIDATE_LOGIN, POST_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 登录访问方法
	 * @param listener
	 * @param account
	 * @param pwd
	 */
	public void login(OnResponseListener listener, String account, String pwd){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("userLoginName", account);
		params.setParameter("userPassword", pwd);
		
		executeHttpMethod(listener, TaskId.TASK_URL_USER_LOGIN, URL_USER_LOGIN, POST_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 *  用户信息
	 * @param listener
	 * @param showId
	 */
	public void getComanyUserInfo(OnResponseListener listener, String showId){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId", showId);
		executeHttpMethod(listener, TaskId.TASK_URL_GET_COMPANY_USER_INFO, URL_GET_COMPANY_USER_INFO, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 获取用户列表(根据参数，也可以搜索用户)
	 * @param listener
	 * @param deptId
	 * @param IsContainChildDept
	 * @param currentPage
	 * @param pageSize
	 * @param searchKey
	 */
	public void getComanyUserList(OnResponseListener listener, String deptId, int IsContainChildDept,int currentPage, int pageSize, String searchKey){
		MTHttpParameters params = new MTHttpParameters();
		if (!deptId.equals("")) {
			params.setParameter("deptId", deptId);
		}
		
		
		if (IsContainChildDept >= 0) {
			params.setParameter("IsContainChildDept", IsContainChildDept);
		}
		
		if (currentPage >= 0) {
			params.setParameter("currentPage", currentPage);
		}
		
		if (pageSize >= 0) {
			params.setParameter("pageSize", pageSize);
		}
		
		if ((searchKey != null) && (!searchKey.equals(""))) {
			params.setParameter("searchKey", searchKey);
		}
		executeHttpMethod(listener, TaskId.TASK_URL_GET_COMPANY_USER_LIST, URL_GET_COMPANY_USER_LIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	/**
	 * 获取本部门人员列表
	 * @param listener
	 * @param deptId
	 */
	public void getComanyUserList(OnResponseListener listener, String deptId){
		
//		getComanyUserList(listener, deptId, 1, -1, -1, "");
		getComanyUserList(listener, deptId, 0, -1, -1, "");
	}
		
	/**
	 * 搜索条件
	 * @param listener
	 * @param searchKey
	 */
	public void getSearchUserList(OnResponseListener listener, String searchKey){
		getComanyUserList(listener, "", -1, -1, -1, searchKey);
	}
	
	/**
	 * 获取组织架构
	 * @param listener
	 */
	public void getComanyDeptList(OnResponseListener listener){
		MTHttpParameters params = new MTHttpParameters();
		executeHttpMethod(listener, TaskId.TASK_URL_GET_COMANY_DEPT_LIST, URL_GET_COMANY_DEPT_LIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}

	/**
	 * 获取组织架构,根据父部门获取子部门
	 * @param listener
	 */
	public void getComanyDeptList(OnResponseListener listener,String parentId){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("isContainChildDeptMember",0);
		params.setParameter("parentId",parentId);
		executeHttpMethod(listener, TaskId.TASK_URL_GET_COMANY_DEPT_LIST, URL_GET_COMANY_DEPT_LIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	
	public void loginQRCode(OnResponseListener listener, String id, String url, String action){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("action", action);
		params.setParameter("connectionId", id);
		params.setParameter("connectionUrl", url);
		executeHttpMethod(listener, TaskId.TASK_URL_QRCODE_CONNECTION, URL_QRCODE_CONNECTION, POST_TYPE, MTHttpManager.HTTP_POST, params, false);

		
	}

	/** 通讯录搜索成员,跟getSearchUserList()是一样的，因为返回值TaskId的原因，新开了一个方法*/
	public void searchUserByKeyword(OnResponseListener listener, String searchKey){
		MTHttpParameters params = new MTHttpParameters();
		if (searchKey != null && !searchKey.equals("")) {
			params.setParameter("searchKey", searchKey);
		}
		executeHttpMethod(listener, TaskId.TASK_URL_SEARCH_USER, URL_GET_COMPANY_USER_LIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}

    /** 通过关键词搜索部门*/
	public void searchDepByKetword(OnResponseListener listener, String searchKey){
		MTHttpParameters params = new MTHttpParameters();
		if (searchKey != null && !searchKey.equals("")) {
			params.setParameter("searchCompanyDeptName", searchKey);
		}
		executeHttpMethod(listener, TaskId.TASK_URL_SEARCH_DEP, URL_GET_COMANY_DEPT_LIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}

	/** 用户列表最后更新时间*/
	public void userLastUpdate(OnResponseListener listener, String deptId){
		MTHttpParameters params = new MTHttpParameters();
		if (deptId != null && !deptId.equals("")) {
			params.setParameter("deptId", deptId);
		}
		params.setIntParameter("isChildDept", 0);
		executeHttpMethod(listener, TaskId.TASK_URL_USER_LAST_UPDATE, URL_USER_LAST_UPDATE, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}

	/** 部门最后更新时间*/
	public void deptLastUpdate(OnResponseListener listener, String parentId){
		MTHttpParameters params = new MTHttpParameters();
		if (parentId != null && !parentId.equals("")) {
			params.setParameter("parentId", parentId);
		}
		params.setIntParameter("isChildDept", 0);
		executeHttpMethod(listener, TaskId.TASK_URL_DEPT_LAST_UPDATE, URL_DEPT_LAST_UPDATE, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}

	/** 获取公司关联APP信息*/
	public void getCompanyAppMsg(OnResponseListener listener){
		MTHttpParameters params = new MTHttpParameters();
		executeHttpMethod(listener, TaskId.TASK_URL_GET_COMPANY_APP, URL_GET_COMPANY_APP, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	/** 判断用户是否为公司成员*/
	public void getUserIsComanyUser(OnResponseListener listener,String showId){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("showId",showId);
		executeHttpMethod(listener, TaskId.TASK_URL_GET_USER_ISCOMPANY, URL_GET_USER_ISCOMANY, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}

	/** 用户通过邮件找回密码，第一步*/
	public void getUserForgetPassword(OnResponseListener listener,String userMail, String token, int type, String code, int minute){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("uEmail",userMail);
		params.setParameter("uValidatorToken",token);
		params.setIntParameter("uValidatorType", type);
		params.setParameter("uValidatorCode",code);
		params.setIntParameter("uValidatorMinutes", minute);
		executeHttpMethod(listener, TaskId.TASK_URL_GET_BACK_MAIL_PASSWORD, URL_GET_BACK_MAIL_PASSWORD, PUT_TYPE, MTHttpManager.HTTP_POST, params, false);
	}

	/** 用户通过邮件找回密码，第二步*/
	public void getUserForgetPassword1(OnResponseListener listener,List<BackPasswordMailEntity.EmailEntity> userMail, String emailServiceName,
									   String subject, String fromEmail, String serviceData, String name){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("emailModels",userMail);
		params.setParameter("emailServiceName",emailServiceName);
		params.setParameter("emailSubject",subject);
		params.setParameter("fromEmail",fromEmail);
		params.setParameter("emailServiceData",serviceData);
		params.setParameter("templateName",name);
		executeHttpMethod(listener, TaskId.TASK_URL_GET_BACK_MAIL_PASSWORD1, URL_GET_BACK_MAIL_PASSWORD1, POST_TYPE, MTHttpManager.HTTP_POST, params, false);
	}


	/** 公司是否存在*/
	public void companyIsExist(OnResponseListener listener,String key,int type){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("key",key);
		params.setParameter("type",type);
		executeHttpMethod(listener, TaskId.TASK_URL_COMPANY_IS_EXIST, URL_COMPANG_IS_EXIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	/** 邮箱是否存在*/
	public void mailIsExist(OnResponseListener listener,String mail){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("MAIL",mail);
		executeHttpMethod(listener, TaskId.TASK_URL_MAIL_IS_EXIST, URL_MAIL_IS_EXIST, GET_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
	/** 账号注册*/
	public void setNewAccount(OnResponseListener listener,String code,String mail,String name,String password,String companyName){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("R_C_CODE",code);
		params.setParameter("U_MAIL",mail);
		params.setParameter("U_TRUE_NAME",name);
		params.setParameter("U_PASSWOED",password);
		params.setParameter("R_C_NAME",companyName);
		executeHttpMethod(listener, TaskId.TASK_URL_NEW_ACCOUNT, URL_NEW_SIGN_ACOUNT, PUT_TYPE, MTHttpManager.HTTP_POST, params, false);
	}

	/** 创建新企业*/
	public void setNewComany(OnResponseListener listener,String code,String mail,String name,String companyName){
		MTHttpParameters params = new MTHttpParameters();
		params.setParameter("R_C_CODE",code);
		params.setParameter("U_MAIL",mail);
		params.setParameter("U_TRUE_NAME",name);
		params.setParameter("R_C_NAME",companyName);
		executeHttpMethod(listener, TaskId.TASK_URL_NEW_COMPANY, URL_NEW_COMANY, PUT_TYPE, MTHttpManager.HTTP_POST, params, false);
	}
}
