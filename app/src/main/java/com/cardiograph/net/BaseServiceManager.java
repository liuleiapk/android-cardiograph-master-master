package com.cardiograph.net;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.cardiograph.commom.ApplicationExtension;
import com.cardiograph.constance.Parameters;
import com.cardiograph.log.Debugger;
import com.cardiograph.util.Util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 服务管理器的基础类
 * 提供通用的工具方法
 * @author bob
 *
 */
public class BaseServiceManager {
	private final static char charTable[]=
		{'0','1','2','3','4','5','6','7','8','9',
		 'a','b','c','d','e','f','g','h','i','j',
		 'k','l','m','n','o','p','q','r','s','t',
		 'u','v','w','x','y','z',
		 'A','B','C','D','E','F','G','H','I','J',
		 'K','L','M','N','O','P','Q','R','S','T',
		 'U','V','W','X','Y','Z'};
	
	/**
	 * 唯一ID，客户端可根据手机平台的特性，可选取设备号，
	 * ESMI，或者其它可以唯一标示用户ID的字符串。
	 */
	private String uid = null;
	

	/**
	 * 验证码，uid+客户端密码 进行MD5消息摘要值。客户端密码为服务端
	 * 与每个手机平台客户端应用程序之间约定的密码。
	 */
	private String vercode = null;
	
	/**
	 * 生成验证码。
	 * @throws java.security.NoSuchAlgorithmException
	 */
	protected void generateVercode() throws NoSuchAlgorithmException
	{
		//随机产生 UID
		this.uid = generateUID(16);
		
		String origWord = "";
		origWord = uid + Parameters.androidClientID;
		
		//得到Md5 摘要算法实例
		MessageDigest md5 = MessageDigest.getInstance("MD5");
	
		md5.update(origWord.getBytes());
		
		//得到摘要数据
		byte[]  md5Word= md5.digest();
		
		//将摘要数据格式化成字符串并保存。
		this.vercode = String.format(
				"%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x",
				md5Word[0],
				md5Word[1],
				md5Word[2],
				md5Word[3],
				md5Word[4],
				md5Word[5],
				md5Word[6],
				md5Word[7],
				md5Word[8],
				md5Word[9],
				md5Word[10],
				md5Word[11],
				md5Word[12],
				md5Word[13],
				md5Word[14],
				md5Word[15]);
	}
	
	public String getUid() {
		return uid;
	}

	public String getVercode() {
		return vercode;
	}

	
	/**
	 * 将JSonObject 对象转换为 ResultForService对象
	 * 主要用于将RestWebService中postRequest返回的对象转换为业务层统一的返回对象ResultForService
	 * @param jo  JSONObject对象
	 * @return	  ResultForService 对象
	 * @throws org.json.JSONException
	 */
	public ResultForService json2ResultForService(JSONObject jo) throws JSONException{
		if (jo == null) {
			throw new  JSONException("json对象不能为null");
		}
		if (!jo.has("retStatus")|| !jo.getJSONObject("retStatus").has("retCode") || !jo.getJSONObject("retStatus").has("errMsg")) {
			throw new JSONException("json对象格式不正确");
		}
		ResultForService resultForService = new ResultForService();
		resultForService.retCode = jo.getJSONObject("retStatus").getString("retCode");
		resultForService.errMsg = jo.getJSONObject("retStatus").getString("errMsg");
		//尝试将 retData 转换成 JSONObject
		resultForService.retData = jo.optJSONObject("retData");
		//retData == null 可能返回的是一个JSONArray ，所以在次尝试将它转成 JSONArray
		if (resultForService.retData == null)
		{
			resultForService.retData = jo.optJSONArray("retData");
		}
		
		return resultForService;
	}
	
	/**
	 * 执行 Put 请求，并添加安全认证参数到请求中。
	 * @param url
	 * @param parameter
	 * @return
	 * @throws BaseException 
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService putRequest(String url,List<NameValuePair> parameter) throws ParseException, IOException, BaseException
	{
		ResultForService resultForService = null;
		
		//由于服务端put 方法不能从body体里取出数据，所以将vercode放在url上。
		List<NameValuePair> vercode = new ArrayList<NameValuePair> ();
		vercode = addVercode(vercode);
		filterUrlAndAppendMac(url, parameter);
		url = url.concat(RestWebService.toUrlParameter(vercode,HttpUtil.EncodingCharset));
		JSONObject json = RestWebService.putRequest(url, parameter,HttpUtil.EncodingCharset);
	
		try {
			resultForService = json2ResultForService(json);
			checkToken(resultForService.retCode);
		} catch (JSONException e) {
			// json 解析异常服务返回了错误的数据，在这返回自定义的异常类。
			throw new BaseException("",ExceptionCode.ServerResultDataError, 0);
		}

		return resultForService;
	}
	
	/**
	 * 执行 Get 请求，并添加安全认证参数到请求中。
	 * @param url
	 * @param parameter
	 * @return
	 * @throws BaseException 
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService getRequest(String url,List<NameValuePair> parameter) throws ParseException, IOException, BaseException
	{
		ResultForService resultForService = null;
		
		parameter = addVercode(parameter);
		filterUrlAndAppendMac(url, parameter);
		JSONObject json = RestWebService.getRequest(url, parameter, HttpUtil.EncodingCharset);
		if(Parameters.debug){
			System.out.println("url:"+url);
		}
		try {
			resultForService = json2ResultForService(json);
			checkToken(resultForService.retCode);
		} catch (JSONException e) {
			// json 解析异常服务返回了错误的数据，在这返回自定义的异常类。
			throw new BaseException("", ExceptionCode.ServerResultDataError, 0);
		}

		return resultForService;
	}
	
	/**
	 * 执行 Get 请求，并添加安全认证参数到请求中。
	 * @param url
	 * @param parameter
	 * @return 输入流
	 * @throws BaseException 
	 * @throws java.io.IOException
	 * @throws org.apache.http.client.ClientProtocolException
	 */
	public InputStream getRequestForStream(String url,List<NameValuePair> parameter) 
			throws BaseException, ClientProtocolException, IOException	{	
		parameter = addVercode(parameter);
		filterUrlAndAppendMac(url, parameter);
		InputStream stream = RestWebService.getRequestForStream(url, parameter,HttpUtil.EncodingCharset);
		
		return stream;
	}
	
	/**
	 * 执行 Post 请求，并添加安全认证参数到请求中。
	 * @param url
	 * @param parameter
	 * @return
	 * @throws BaseException 
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService postRequest(String url,List<NameValuePair> parameter) throws ParseException, IOException, BaseException
	{
		ResultForService resultForService = null;
		
		parameter = addVercode(parameter);
		filterUrlAndAppendMac(url, parameter);
		JSONObject json = RestWebService.postRequest(url, parameter,HttpUtil.EncodingCharset);
		Log.e(getClass().getName(), "Url = " +  url +  "\n\nParam = " + parameter.toString()+ "\n\nJSON = " + json);
        try {
			resultForService = json2ResultForService(json);
			checkToken(resultForService.retCode);
			
		} catch (JSONException e) {
			// json 解析异常服务返回了错误的数据，在这返回自定义的异常类。
			new Debugger().log(e);
			throw new BaseException("",ExceptionCode.ServerResultDataError, 0);
		}
		
		return resultForService;
	}
	
	/**
	 * 执行 Post 请求，并添加安全认证参数到请求中。
	 * @param url
	 * @return
	 * @throws BaseException 
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService postRequest(String url,MultiPartEntity entity,List<NameValuePair> parameter)
			throws ParseException, IOException, BaseException
	{
		ResultForService resultForService = null;

		//由于服务端 MutilPartPost 方法不能从body体里取出数据，所以将vercode放在url上。
		parameter = addVercode(parameter);
		filterUrlAndAppendMac(url, parameter);
		url = url.concat(RestWebService.toUrlParameter(parameter,HttpUtil.EncodingCharset));
		if(Parameters.debug){
			System.out.println("url:"+url);
		}
		JSONObject json = RestWebService.postRequest(url, entity);
		
		try {
			resultForService = json2ResultForService(json);
			checkToken(resultForService.retCode);
		} catch (JSONException e) {
			// json 解析异常服务返回了错误的数据，在这返回自定义的异常类。
			throw new BaseException("",ExceptionCode.ServerResultDataError, 0);
		}

		return resultForService;
	}
	
	/**
	 * 执行 Post 请求，并添加安全认证参数到请求中。
	 * @param url
	 * @return
	 * @throws BaseException 
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService postRequest(String url,MultiPartEntity entity)
			throws ParseException, IOException, BaseException
	{
		ResultForService resultForService = null;
		
		//由于服务端 MutilPartPost 方法不能从body体里取出数据，所以将vercode放在url上。
		List<NameValuePair> vercode = new ArrayList<NameValuePair> ();
		vercode = addVercode(vercode);
		url = url.concat(RestWebService.toUrlParameter(vercode,HttpUtil.EncodingCharset));
		
		JSONObject json = RestWebService.postRequest(url, entity);	
		try {
			resultForService = json2ResultForService(json);
			checkToken(resultForService.retCode);
		} catch (JSONException e) {
			// json 解析异常服务返回了错误的数据，在这返回自定义的异常类。
			throw new BaseException("",ExceptionCode.ServerResultDataError, 0);
		}
		
		return resultForService;
	}
	
	/**
	 * 执行 Delete 请求，并添加安全认证参数到请求中。
	 * @param url
	 * @param parameter
	 * @return
	 * @throws BaseException 
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService deleteRequest(String url,List<NameValuePair> parameter) throws ParseException, IOException, BaseException
	{
		ResultForService resultForService = null;
		
		parameter = addVercode(parameter);
		filterUrlAndAppendMac(url, parameter);
		JSONObject json = RestWebService.deleteRequest(url, parameter,HttpUtil.EncodingCharset);
	
		try {
			resultForService = json2ResultForService(json);
			checkToken(resultForService.retCode);
		} catch (JSONException e) {
			// json 解析异常服务返回了错误的数据，在这返回自定义的异常类。
			throw new BaseException("",ExceptionCode.ServerResultDataError, 0);
		}
		
		return resultForService;
	}
	
	/**
	 * 验证userToken是否已赋值，如果没有则抛出 BaseException 异常
	 * @throws BaseException
	 */
	public void UserTokenHavingIsValid () throws BaseException
	{
//		if (Parameters.user.token == null || Parameters.user.token =="")
//		{
//			BaseException e = new BaseException("Have not logged in.",ExceptionCode.HaveNotLoggedIn,0);
//			throw e;
//		}
	}
	
	/**
	 * 创建http请求参数对，如果参数 addUsertToken 为真则向参数对中添加userToken,termid
	 * @param addUsertToken
	 * @return
	 * @throws BaseException 
	 */
	public  List<NameValuePair> createNameValuePair(boolean addUsertToken) throws BaseException
	{
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
		if (addUsertToken)
		{
			//判断 userToken 是否存在。
			UserTokenHavingIsValid();
			
			nameValuePairs.add(new BasicNameValuePair("userToken", Parameters.user.token));	
			String pasmNO = "";
			if (Util.isEmpty(Parameters.swiperNo)) {//如果刷卡器串号为空，使用钱包id
//              pasmNO = Parameters.user.walletTerminalNO;
            }else {//刷卡器串号不为空，则使用刷卡器串号
              pasmNO = Parameters.swiperNo;
            }
			//如果不为空，则添加termid，否则后期url编码时报空指针异常（德阳银行用户过来后没有虚拟终端号）
			if (!Util.isEmpty(pasmNO)) {
				nameValuePairs.add(new BasicNameValuePair("termid", pasmNO));
			}
		}
		
		return nameValuePairs;
	}
	
	/**
	 * 向http参数列表中添加全局参数，该方法添加的参数如下:<br>
	 * ver<br>
	 * uid<br>
	 * vercode<br>
	 * debug<br>
	 * userToken<br> 
	 * mac<br>
	 * @param parameter				参数列表
	 * @param isAddLoginParameter   是否添加登录参数， 若用户未登录则此参数无效。
	 * @param isAddMac              是否添加 MAC 参数
	 */
	public void fillGlobalTransactionParameter(
			List<NameValuePair> parameter,
			boolean isAddLoginParameter,
			boolean isAddMac){
		
		parameter = addVercode(parameter);
		
//		if (isAddLoginParameter && !Util.isEmpty(Parameters.user.token)){
//			parameter.add(new BasicNameValuePair("userToken", Parameters.user.token));	
//		}
//		
//		if (isAddMac){
//			MAC.mac(parameter);
//		}
	}
	
	/**
	 * 向参数列表中添加安全认证参数
	 * @param parameter
	 * @return
	 */
	private List<NameValuePair> addVercode(List<NameValuePair> parameter)
	{
		if (parameter == null)
		{
			parameter = new ArrayList<NameValuePair>();
		}
		
		//调试标记
//		if (Parameters.debug)
//		{
//			parameter.add(new BasicNameValuePair("debug","1"));
//		}
		
		//App 内部版本号
		parameter.add(new BasicNameValuePair("ver",Util.getVersionCode()));
//		parameter.add(new BasicNameValuePair("ver","34"));
		
//		String time = sdf.format(System.currentTimeMillis());
//		parameter.add(new BasicNameValuePair("tdtm", time));
//		parameter.add(new BasicNameValuePair("p_v", Util.getAppVersionCode()));
		
		
		if (this.vercode == null || this.vercode.length() == 0)
		{
			//错误还没有设置安全认证码,在这里可以做一些处理例如“抛异常”。
			return parameter;
		}
		
		//添加安全认证参数
		parameter.add(new BasicNameValuePair("uid",this.uid));
		parameter.add(new BasicNameValuePair("vercode",this.vercode));
		System.out.println("Util.getVersionCode() = "+Util.getVersionCode()+", uid = "+this.uid);
		
		//添加客户级别标识。当登录后客户级别才有。
//		if (!Util.isEmpty(Parameters.user.custlev)){
//			parameter.add(new BasicNameValuePair("custlev",Parameters.user.custlev));
//		}
		
		return parameter;
	}
	
	private String generateUID(int length)
	{
		long t = System.nanoTime() ^ System.currentTimeMillis();
		Random random = new Random(t);
		char[] uid = new char[length];
		
		for (int i=0;i< uid.length;i++)
		{
			uid[i] =  charTable[random.nextInt(charTable.length)];
		}
		
		return String.valueOf(uid);
	}
	
	/**
	 * 登录token是否过期
	 */
	private void checkToken(String code){
		if (code.equals(Parameters.tokenOutOfDate)) {
            Parameters.user.token = "";
			if (Parameters.httpResponseAfterCheckToken){
				//充许显示Token 异常对话框，点击后跳转到首页。
				Context context= ApplicationExtension.getInstance();
//				Intent intent=new Intent(context, TokenOutOfDateActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(intent);
			}
			else{
				//不充许显示Token 异常对话框，直接清除登录信息。
				Parameters.clear();	
			}
		}
	}

	/**
	 * 根据url，过滤是否需要进行mac计算
	 * 20130609赵鲜阳修改
	 * 激活钱包时，设置支付密码使用两层加密，由于此接口（activeEWallet）不算mac，
	 * 后端认为是老的接口，只有一层加密，解不开，所以这里给这个接口加上mac计算
	 * @param url
	 * @return
	 */
	private boolean shouldUseMac(String url){
		return (url.contains("queryTrans")
				||url.contains("commitTransaction")
				||url.contains("queryRemitTrans")
				||url.contains("commitRemitTransaction")
				||url.contains("updateSZBItem")
				||url.contains("activeEWallet")
				||url.contains("resetPayPwd")
				||url.contains("payPwdSwitch"));	
				
	}
	
	/**
	 * 过滤url，并且添加mac字段
	 * 由于putRequest方法中的addVercode，并没有使用parameter参数，所以updateSZBItem的mac计算就漏掉了
	 * 此处将添加mac的逻辑单独封装一个方法，和addVercode独立开来，并且在每个addVercode方法之后使用此方法
	 * @param url
	 * @param parameter
	 */
	private void filterUrlAndAppendMac(String url,List<NameValuePair> parameter){
		if (shouldUseMac(url)) {
			
			//由于后端对金额是按照版本号处理的，所以updateSZBItem 接口，不管是刷卡还是发短信，
			//金额统一用分来处理,所以需要先取 mab，这个过程中将元转换为分 updateSZBItem 对账
			//单的所有操作都执行这个接口，如修改账单信息，刷卡支付，短信通知，手动完成等操作
			//此接口需要进一步过滤，只有刷卡支付才需要进行mac 验证
			if (url.contains("updateSZBItem")) {
				for (NameValuePair nameValuePair : parameter) {
					if("payType".equals(nameValuePair.getName()) && !"1".equals(nameValuePair.getValue())){
						//payType 为1时，为刷卡付款，需要做mac校验，如果不为 1，其他方式则不需要mac校验
						return;
					}
				}
			}
			
//			MAC.mac(parameter);
		}
	}
	
}
