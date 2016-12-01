package com.cardiograph.log;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

/**
 * 测试信息类
 * 追踪执行轨迹，获取异常位置，打印保存日志信息
 * @author 
 *
 */
public class Debugger {
	
	public  final String LAKALATAG = "lakalademotag";
	public  boolean logAvailable = true;
	
	/**
	 * 获取出现异常代码位置
	 * @param exception 异常
	 * @return   获取出现异常的代码位置（文件--类名--方法名--代码行）
	 */
	private  String getCodePosition(Exception exception){
		StackTraceElement entry= exception.getStackTrace()[0];
		String message = String.format("[%s]\n	FileName:%s\n	ClassName:%s\n	MethodName:%s\n	Line:%s\n",
					new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss ").format(new Date()),
					entry.getFileName(),
					entry.getClassName(),
					entry.getMethodName(),
					entry.getLineNumber()
					);
				
		return message;
	}
	
	
	/**
	 * 向logcat写入异常信息
	 * 并将异常保存到日志文件
	 * @see #getCodePosition(Exception)
	 */
	public  void log(Exception exception){
		try {
			String msg = getCodePosition(exception).concat("\n	"+exception.toString());
			Log.e(LAKALATAG,msg); 
			Logger.instance.logout(msg);
			exception.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 输出Log.i  ,输出代码位置 
	 * @see #getCodePosition(Exception)
	 */
	public  void log(Exception exception, String str){
		if (!logAvailable) return;
		
		String msg = getCodePosition( exception).concat(str);
		Log.i(LAKALATAG,msg);
	}
	
	
	/**
	 * 输出Log.i  ,输出代码位置 
	 * @see #getCodePosition(Exception)
	 */
	public  void log(Exception exception, int i){
		if (!logAvailable) return;
		String msg = getCodePosition( exception).concat(String.valueOf(i));
		Log.i(LAKALATAG,msg);
	}
	
	
	/**
	 * 输出Log.i  ,输出代码位置 
	 * @see #getCodePosition(Exception)
	 */
	public  void log(Exception exception, long l){
		if (!logAvailable) return;
		String msg = getCodePosition( exception).concat(String.valueOf(l));
		Log.i(LAKALATAG,msg);	
	}
	
	
	/**
	 * 输出Log.i  ,输出代码位置 
	 * @see #getCodePosition(Exception)
	 */
	public  void log(Exception exception, boolean b){
		if (!logAvailable) return;
		String msg = getCodePosition(exception).concat(String.valueOf(b));
		Log.i(LAKALATAG,msg);
	}
	
	
	/**
	 * 输出Log.i  ,输出代码位置 
	 * @see #getCodePosition(Exception)
	 */
	public  void log(Exception exception, Object obj){
		if (!logAvailable) return;
		String msg = getCodePosition( exception).concat(obj.toString());
		Log.i(LAKALATAG,msg);
	}
	
	/**
	 * 打印正在执行的方法名称
	 * @param traceElement 追踪元素
	 * 
	 */
	public  void log(StackTraceElement traceElement) {
		if (!logAvailable) return;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(traceElement.getClassName());
		stringBuilder.append(".");
		stringBuilder.append(traceElement.getMethodName());
		stringBuilder.append("()");
		Log.i(LAKALATAG,stringBuilder.toString());
	}
	
	/**
	 * 打印正在执行的方法名称，并附加信息
	 * @param traceElement 追踪元素
	 * @param message		附加信息 
	 */
	public  void log(StackTraceElement traceElement,String message) {
		if (!logAvailable) return;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(traceElement.getClassName());
		stringBuilder.append(".");
		stringBuilder.append(traceElement.getMethodName());
		stringBuilder.append("()");
		stringBuilder.append("->"+message);
		Log.i(LAKALATAG,stringBuilder.toString());
	}
}
