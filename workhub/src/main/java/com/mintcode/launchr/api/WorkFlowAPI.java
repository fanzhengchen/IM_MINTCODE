package com.mintcode.launchr.api;

import com.mintcode.launchrnetwork.MTHttpManager;
import com.mintcode.launchrnetwork.MTHttpParameters;
import com.mintcode.launchrnetwork.OnResponseListener;

/**
 * Created by JulyYu on 2016/4/12.
 */
public class WorkFlowAPI extends BaseAPI{

    private static WorkFlowAPI sInstance;

    private static String WorkFlowBaseUrl = "/Workflow-Module/";
    private static String FormBaseUrl = "/Form-Module/";

    private WorkFlowAPI() {

    }

    public static WorkFlowAPI getInstance() {
        if (sInstance == null) {
            sInstance = new WorkFlowAPI();
        }
        return sInstance;
    }
    private final static String URL_GET_WORK_FLOW_BY_TRIGGER = WorkFlowBaseUrl + "GetWorkflowByStateTrigger";
    private final static String URL_GET_WORK_FLOW = WorkFlowBaseUrl + "Pull";
    private final static String URL_SAVE_WORK_FLOW = WorkFlowBaseUrl + "Save";
    private final static String URL_GET_FORM = FormBaseUrl + "Pull";
    private final static String URL_SAVE_FORM = FormBaseUrl + "SaveForm";


    public final static class TaskId{
        public final static String TASK_URL_GET_WORK_FLOW = "task_url_get_work_flow";
        public final static String TASK_URL_SAVE_WORK_FLOW = "task_url_save_work_flow";
        public final static String TASK_URL_GET_FORM = "task_url_get_form";
        public final static String TASK_URL_SAVE_FORM = "task_url_save_form";
        public final static String TASK_URL_GET_WORK_FLOW_BY_TRIGGER= "task_url_get_work_flow_by_trigger";
    }
    /** 获取工作流*/
    public void getWorkFlow(OnResponseListener listener,String showid){
        MTHttpParameters parameters = new MTHttpParameters();
        parameters.setParameter("workflowId", showid);
        executeHttpMethod(listener,TaskId.TASK_URL_GET_WORK_FLOW,URL_GET_WORK_FLOW,POST_TYPE, MTHttpManager.HTTP_POST, parameters, false);
    }
    /** 保存工作流*/
    public void saveWorkFlow(OnResponseListener listener,String id,String workflow,String typeId,String workflowId){
        MTHttpParameters parameters = new MTHttpParameters();
        parameters.setParameter("id", id);
        parameters.setParameter("workflow", workflow);
        parameters.setParameter("typeId", typeId);
        parameters.setParameter("workflowId", workflowId);
        executeHttpMethod(listener, TaskId.TASK_URL_SAVE_WORK_FLOW, URL_SAVE_WORK_FLOW, POST_TYPE, MTHttpManager.HTTP_POST, parameters, false);
    }
    public void getWorkPerson(OnResponseListener listener,String showid){
        MTHttpParameters parameters = new MTHttpParameters();
        parameters.setParameter("workflowId", showid);
        executeHttpMethod(listener,TaskId.TASK_URL_GET_WORK_FLOW_BY_TRIGGER,URL_GET_WORK_FLOW_BY_TRIGGER,POST_TYPE, MTHttpManager.HTTP_POST, parameters, false);
    }
    /** 获取表单*/
    public void getForm(OnResponseListener listener,String formId){
        MTHttpParameters parameters = new MTHttpParameters();
        parameters.setParameter("formId", formId);
        executeHttpMethod(listener,TaskId.TASK_URL_GET_FORM,URL_GET_FORM,POST_TYPE, MTHttpManager.HTTP_POST, parameters, false);
    }
    /** 保存表单*/
    public void saveForm(OnResponseListener listener,String id,String formDefinition,String typeId,String formId){
        MTHttpParameters parameters = new MTHttpParameters();
        parameters.setParameter("id", id);
        parameters.setParameter("formDefinition", formDefinition);
        parameters.setParameter("typeId", typeId);
        parameters.setParameter("formId", formId);
        executeHttpMethod(listener,TaskId.TASK_URL_SAVE_FORM,URL_SAVE_FORM,POST_TYPE, MTHttpManager.HTTP_POST, parameters, false);
    }

}






