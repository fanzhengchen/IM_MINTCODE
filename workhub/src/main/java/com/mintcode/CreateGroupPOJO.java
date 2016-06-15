package com.mintcode;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mintcode.launchr.api.IMAPI.TASKID;

@JsonTypeName(TASKID.CREATEGROUP)
public class CreateGroupPOJO extends BasePOJO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7995784628884633042L;

	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data {
		private String sessionName;

		public String getSessionName() {
			return sessionName;
		}

		public void setSessionName(String sessionName) {
			this.sessionName = sessionName;
		}

	}
}
