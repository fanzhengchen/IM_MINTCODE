package com.mintcode;



public class DataPOJO{
	/**
	 * 操作成功返回码
	 */
	public static final int RESULT_SUCCESS = 2000;

	/**
	 * 
	 */
	private String action;
	private int code;

	private String message;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isResultSuccess() {
		return code == RESULT_SUCCESS;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
