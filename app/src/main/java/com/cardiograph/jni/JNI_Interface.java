package com.cardiograph.jni;

/************************************************************
 *  内容摘要	：<p>
 *
 *  作者	：叶金新
 *  创建时间	：2014-11-9 下午7:01:36 
 *  当前版本号：v1.0
 *  历史记录	:
 *  	日期	: 2014-11-9 下午7:01:36 	修改人：
 *  	描述	:
 ************************************************************/
public class JNI_Interface {
	//native：本地、原生态
	public static native int getCInt();
	public static native String getCString();
	public static native float trapper(float x, float fs);
}
