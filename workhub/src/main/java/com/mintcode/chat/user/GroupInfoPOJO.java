package com.mintcode.chat.user;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mintcode.BasePOJO;
import com.mintcode.launchr.api.IMAPI.TASKID;

@JsonTypeName(TASKID.SESSION)
public class GroupInfoPOJO extends BasePOJO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5729504292129853239L;
	private GroupInfo data;

	public GroupInfo getData() {
		return data;
	}

	public void setData(GroupInfo data) {
		this.data = data;
	}

}
