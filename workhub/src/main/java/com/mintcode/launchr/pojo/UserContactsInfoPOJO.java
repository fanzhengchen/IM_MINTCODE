package com.mintcode.launchr.pojo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("usercontactsinfo")
public class UserContactsInfoPOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;
	private Content data;

	public Content getData() {
		return data;
	}

	public void setData(Content data) {
		this.data = data;
	}

	public static class Content implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int maxMember;
		private String realName;
		private String deptName;
		private String parentDeptName;
		private String loginName;
		private String mobilePhone;
		private String anotherName;
		private String officePhone;
		private String headPic;
		private String position;
		private List<User> memberList;
		private String virtualNumber;
		private String userMail;

		public int getMaxMember() {
			return maxMember;
		}

		public void setMaxMember(int maxMember) {
			this.maxMember = maxMember;
		}

		public String getDeptName() {
			return deptName;
		}

		public void setDeptName(String deptName) {
			this.deptName = deptName;
		}

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
		}

		public String getHeadPic() {
			return headPic;
		}

		public void setHeadPic(String headPic) {
			this.headPic = headPic;
		}

		public String getRealName() {
			return realName;
		}

		public void setRealName(String realName) {
			this.realName = realName;
		}

		public String getParentDeptName() {
			return parentDeptName;
		}

		public void setParentDeptName(String parentDeptName) {
			this.parentDeptName = parentDeptName;
		}

		public String getLoginName() {
			return loginName;
		}

		public void setLoginName(String loginName) {
			this.loginName = loginName;
		}

		public String getAnotherName() {
			return anotherName;
		}

		public void setAnotherName(String anotherName) {
			this.anotherName = anotherName;
		}

		public String getMobilePhone() {
			return mobilePhone;
		}

		public void setMobilePhone(String mobilePhone) {
			this.mobilePhone = mobilePhone;
		}

		public String getOfficePhone() {
			return officePhone;
		}

		public void setOfficePhone(String officePhone) {
			this.officePhone = officePhone;
		}

		public List<User> getMemberList() {
			return memberList;
		}

		public void setMemberList(List<User> memberList) {
			this.memberList = memberList;
		}

		public String getVirtualNumber() {
			return virtualNumber;
		}

		public void setVirtualNumber(String virtualNumber) {
			this.virtualNumber = virtualNumber;
		}

		public String getUserMail() {
			return userMail;
		}

		public void setUserMail(String userMail) {
			this.userMail = userMail;
		}

		public static class User {
			private String realName;
			private String departName;
			private String parentDeptName;
			private String loginName;
			private String mobilePhone;
			private String anotherName;
			private String officePhone;
			private String headPic;

			public String getRealName() {
				return realName;
			}

			public void setRealName(String realName) {
				this.realName = realName;
			}

			public String getDepartName() {
				return departName;
			}

			public void setDepartName(String departName) {
				this.departName = departName;
			}

			public String getParentDeptName() {
				return parentDeptName;
			}

			public void setParentDeptName(String parentDeptName) {
				this.parentDeptName = parentDeptName;
			}

			public String getLoginName() {
				return loginName;
			}

			public void setLoginName(String loginName) {
				this.loginName = loginName;
			}

			public String getMobilePhone() {
				return mobilePhone;
			}

			public void setMobilePhone(String mobilePhone) {
				this.mobilePhone = mobilePhone;
			}

			public String getAnotherName() {
				return anotherName;
			}

			public void setAnotherName(String anotherName) {
				this.anotherName = anotherName;
			}

			public String getOfficePhone() {
				return officePhone;
			}

			public void setOfficePhone(String officePhone) {
				this.officePhone = officePhone;
			}

			public String getHeadPic() {
				return headPic;
			}

			public void setHeadPic(String headPic) {
				this.headPic = headPic;
			}

		}
	}
}
