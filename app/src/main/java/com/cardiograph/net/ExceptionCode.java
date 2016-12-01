package com.cardiograph.net;

public final class ExceptionCode {
	
	/**
	 * 服务器返回数据错误
	 */
	public static final int ServerResultDataError = 0x00001000;
	
	/**
	 * Http错误 ,该错误会在 BaseException 的statusCode 设置http错误码。
	 */
	public static final int ServerHttpError = 0x00001001;

	/**
	 * 用户还没有登录
	 */
	public static final int HaveNotLoggedIn = 0x00001002;
}
