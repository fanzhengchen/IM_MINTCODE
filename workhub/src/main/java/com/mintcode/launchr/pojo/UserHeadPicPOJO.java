package com.mintcode.launchr.pojo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * 用户头像 模型
 * 
 * @author ChristLu
 * 
 */
@JsonTypeName("syncheadpicture")
public class UserHeadPicPOJO extends BasePOJO {
	private static final long serialVersionUID = 1L;
	private List<UserHeadPicModel> data;

	public List<UserHeadPicModel> getData() {
		return data;
	}

	public void setData(List<UserHeadPicModel> data) {
		this.data = data;
	}

	public static class UserHeadPicModel implements Serializable {
		private static final long serialVersionUID = 1L;
		private String anotherName;
		private String headPic;

		public String getAnotherName() {
			return anotherName;
		}

		public void setAnotherName(String anotherName) {
			this.anotherName = anotherName;
		}

		public String getHeadPic() {
			return headPic;
		}

		public void setHeadPic(String headPic) {
			this.headPic = headPic;
		}
	}
}
