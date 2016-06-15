package com.mintcode.launchr.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author StephenHe 2015/9/15
 * 
 */
public class TaskAddProjectEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String showId;
	
	private String name;
	
	private List<TaskAddProjectMembersEntity> members;

	private String createUser;

	public String getCreateUser() {
		return createUser;
	}
	@JsonProperty("createUser")
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getShowId() {
		return showId;
	}

	public void setShowId(String showId) {
		this.showId = showId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<TaskAddProjectMembersEntity> getMembers() {
		return members;
	}

	public void setMembers(List<TaskAddProjectMembersEntity> members) {
		this.members = members;
	}

	public static class TaskAddProjectMembersEntity implements Serializable {
		private static final long serialVersionUID = 1L;
		private String memberName;

		private String memberTrueName;

		public String getMemberName() {
			return memberName;
		}

		public void setMemberName(String memberName) {
			this.memberName = memberName;
		}

		public String getMemberTrueName() {
			return memberTrueName;
		}

		public void setMemberTrueName(String memberTrueName) {
			this.memberTrueName = memberTrueName;
		}
	}
}
