package com.cardiograph.net;
/**
 * 业务层服务返回的对象
 * 描述:
 * 	 {
 * 		//返回状态
 * 		retStatus:{ retCode:0,   //返回状态码
 * 					errMsg},	 //返回状态码描述
 * 		//返回数据
 * 		retData: Object  //具体的业务数据,如果无返回值则为null 
 *   }
 * @author bob
 *
 */
public class ResultForService {
	public ResultForService(){		
	}
	public String retCode;	//状态码
	public String errMsg;	//状态码描述
	public Object retData;	//返回数据,主要根据返回值的类型 JsonObject ,JsonArray 来进行进一步的处理
}
