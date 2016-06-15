package com.mintcode.launchrnetwork;

/**各种异常的结构体
 * @author RobinLin
 *
 */
public class MTException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -572203719801196514L;
	
	private int mCode;
	
	public MTException() {
		super();
	}

	public MTException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public MTException(String detailMessage) {
		super(detailMessage);
	}

	public MTException(Throwable throwable) {
		super(throwable);
	}
	public MTException(String detailMessage,int result)
	{
		super(detailMessage);
		this.mCode = result;
	}
	
	public MTException(int result) {
		this.mCode = result;
	}

	public int getCode() {
		return this.mCode;
	}
}
