package com.mintcode.launchr.pojo.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mintcode.launchr.pojo.entity.ApproveEntity;

public class ApproveSearchResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3046314668410238882L;

	private ApproveSearchData Data;

	public ApproveSearchData getData() {
		return Data;
	}

	@JsonProperty("Data")
	public void setData(ApproveSearchData data) {
		Data = data;
	}

	public static class ApproveSearchData implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7601354888818192978L;

		private List<ApproveEntity> ResultApproveList;

		private List<ApproveEntity> ResultTitleList;

		public List<ApproveEntity> getResultApproveList() {
			return ResultApproveList;
		}

		@JsonProperty("ResultApproveList")
		public void setResultApproveList(List<ApproveEntity> resultApproveList) {
			ResultApproveList = resultApproveList;
		}

		public List<ApproveEntity> getResultTitleList() {
			return ResultTitleList;
		}

		@JsonProperty("ResultTitleList")
		public void setResultTitleList(List<ApproveEntity> resultTitleList) {
			ResultTitleList = resultTitleList;
		}

	}
}
