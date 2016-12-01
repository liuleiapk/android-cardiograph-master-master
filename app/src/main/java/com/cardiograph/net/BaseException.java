package com.cardiograph.net;

public class BaseException extends Exception {
	
	private static final long serialVersionUID = -1108924730588366539L;
	
	private int exceptionCode = 0;
	private int statusCode = 0;
	
	/**
	 * 获取异常码
	 * @return
	 */
	public int getExceptionCode() {
		return exceptionCode;
	}

	/**
	 * 获取状态码
	 * @return
	 */
	public int getStatusCode() {
		return statusCode;
	}
	
	public BaseException(String msg,int type, int status)
	{
		super(msg);
		exceptionCode = type;
		this.statusCode = status;
	}
	
	public BaseException(String msg,int type,int status, Throwable throwable)
	{
		super(msg,throwable);
		exceptionCode = type;
		this.statusCode = status;
	}
	
	
}
