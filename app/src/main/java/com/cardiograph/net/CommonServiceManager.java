package com.cardiograph.net;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;

import com.cardiograph.constance.Parameters;
import com.cardiograph.model.ECGData;
import com.cardiograph.util.Util;

import android.util.Log;

/**
 * 通用服务类接口 描述: 主要提供以下通用的接口
 * <p/>
 * 
 * <pre>
 * 		用户注册
 * 		用户开通
 * 		用户登录
 * 		获取省列表
 * 		获取区县列表
 * 		上传身份证照片
 * 		发送短信校验码
 * 		校验短信验证码
 *      设备激活数据上传
 * </pre>
 * <p/>
 * 在调用接口前必需调用 下列方法初始化 vercode:<br>
 * generateVercode(); <br>
 * 建议使用 getInstance 方法获取实例。
 * 
 * @author bob
 */
public class CommonServiceManager extends BaseServiceManager {

	private static CommonServiceManager instance = null;

	/**
	 * 构造方法私有化，只能通过静态方法getInstance方法获取
	 * 
	 * @see #getInstance()
	 */
	private CommonServiceManager() {

	}

	/**
	 * 创建 CommonServiceManager 类实例
	 * 
	 * @return
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static synchronized CommonServiceManager getInstance() throws NoSuchAlgorithmException {

		if (instance == null) {
			instance = new CommonServiceManager();
			instance.generateVercode();
		}
		return instance;
	}

	/******************************************** 用户中心相关 **************************************************/

	/**
	 * @param loginName
	 *            登录名
	 * @param password
	 *            口令
	 * @param pwdLevel
	 *            密码等级
	 * @param mobileNum
	 *            手机号
	 * @param realName
	 *            真实姓名
	 * @param email
	 *            Email
	 * @param idCardType
	 *            身份证：ID,军官证：MILITARY_ID,学生证：STUDENT_CARD,护照：PASSPORT,其他：OTHER
	 * @param idCardId
	 *            如果选择证件类型,则此项不能为空
	 * @param province
	 * @param city
	 * @param district
	 * @param homeAddr
	 * @param zipCode
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService register(String loginName, // 登录名
			String password, // 口令
			String pwdLevel, String mobileNum, // 手机号
			String realName, // 真实姓名
			String email, // email
			String idCardType, String idCardId, String province, String city, String district, String homeAddr, String zipCode) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("register/");
		url.append(loginName);
		url.append(".json");
		// 设置参数
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("loginName", loginName));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		nameValuePairs.add(new BasicNameValuePair("mobileNum", mobileNum));
		nameValuePairs.add(new BasicNameValuePair("realName", realName));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("pwdSecLevel", pwdLevel));
		nameValuePairs.add(new BasicNameValuePair("idCardType", idCardType));
		nameValuePairs.add(new BasicNameValuePair("idCardInfo.idCardId", idCardId));
		nameValuePairs.add(new BasicNameValuePair("address.province", province));
		nameValuePairs.add(new BasicNameValuePair("address.city", city));
		nameValuePairs.add(new BasicNameValuePair("address.district", district));
		nameValuePairs.add(new BasicNameValuePair("address.homeAddr", homeAddr));
		nameValuePairs.add(new BasicNameValuePair("address.zipCode", zipCode));
		nameValuePairs.add(new BasicNameValuePair("imei", Util.getIMEI()));
		nameValuePairs.add(new BasicNameValuePair("imsi", Util.getIMSI()));

		resultForService = this.postRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 用户登录
	 * 
	 * @param loginName
	 *            登录名称
	 * @param password
	 *            登录密码
	 * @return ResultForService
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	@SuppressWarnings("unused")
	public ResultForService getUserLoginState(String loginName, String password, String sigVerif) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getUserLoginState/");
		url.append(loginName);
		url.append(".json");
		// 设置参数
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("loginName", loginName));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		nameValuePairs.add(new BasicNameValuePair("imei", Util.getIMEI()));
		nameValuePairs.add(new BasicNameValuePair("imsi", Util.getIMSI()));
		int versionCode = Integer.parseInt(Util.getVersionCode());
		if (!Parameters.debug && versionCode >= 13) {
			nameValuePairs.add(new BasicNameValuePair("sigVerif", sigVerif));
		}

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 修改密码
	 * 
	 * @param loginName
	 *            登录名
	 * @param oldPwd
	 *            旧密码
	 * @param newPwd
	 *            新密码
	 * @param pwdLevel
	 *            密码等级
	 * @return ResultForService
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService updateUserPwd(String loginName, String password, String newPassword, String pwdSecLevel) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		// 设置参数
		List<NameValuePair> nameValuePairs = createNameValuePair(true);
		nameValuePairs.add(new BasicNameValuePair("loginName", loginName));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		nameValuePairs.add(new BasicNameValuePair("newPassword", newPassword));
		nameValuePairs.add(new BasicNameValuePair("pwdSecLevel", pwdSecLevel));

		resultForService = this.putRequest(Parameters.serviceURL.concat("updateUserPwd/" + loginName + ".json"), nameValuePairs);

		return resultForService;
	}

	// 修改手机号码,暂时不实现

	// 修改手机号码??需要修改手机号码?

	/**
	 * 检查用户是否可用状态
	 * 
	 * @param loginName
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService getLoginNameState(String loginName) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getLoginNameState/");
		url.append(loginName);
		url.append(".json");

		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**
	 * 根据参数名称获取参数值
	 * 
	 * @param key
	 *            参数名称
	 * @param dictCode
	 *            参数字典编号
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService getParameter(String key, String dictCode) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getParameter.json");

		// 设置参数
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("key", key));
		if (!dictCode.equals("")) {
			nameValuePairs.add(new BasicNameValuePair("dictCode", dictCode));
		}
		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param loginName
	 * @param verifyType
	 *            获取用户信息的类型 (password,vercode(验证码))
	 * @param key
	 *            下发短信时使用的关键字
	 * @param userToken
	 *            verifyType为2时值为短信验证码，verifyType为1时值为登录令牌
	 * @param token
	 *            verifyType为2时token值为接收短信时返回的token
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService getUserInfo(String loginName, String verifyType, String userToken, String token) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		// 判断 userToken 是否存在。
		UserTokenHavingIsValid();

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getUserInfo/");
		url.append(loginName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("verifyType", verifyType));
		nameValuePairs.add(new BasicNameValuePair("token", token));
		if (verifyType.equals("1")) {
			userToken = Parameters.user.token;
		}
		nameValuePairs.add(new BasicNameValuePair("userToken", Parameters.user.token));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/*************************************** 拉卡拉手机版业务 *******************************/

	/**
	 * 绑定硬件
	 * 
	 * @param loginName
	 *            登录名
	 * @param psamNo
	 *            卡号
	 * @param telecomOperators
	 *            运营商号段 //现值同为IMSI，服务商由服务端验证
	 * @param mobilePhoneModel
	 *            手机标识
	 * @param mobilePhoneProduct
	 *            手机型号
	 * @param mobilePhoneManuFacturer
	 *            手机厂商
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService bindPsamCardToUser(String loginName, String psamNo, String telecomOperators, String mobilePhoneModel, String mobilePhoneProduct, String mobilePhoneManuFacturer) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		// 判断 userToken 是否存在。
		UserTokenHavingIsValid();

		// 设置参数
		String deviceId = Util.getIMSI();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("loginName", loginName));
		nameValuePairs.add(new BasicNameValuePair("psamNo", psamNo));
		nameValuePairs.add(new BasicNameValuePair("deviceId", deviceId));
		nameValuePairs.add(new BasicNameValuePair("telecomOperators", telecomOperators));
		nameValuePairs.add(new BasicNameValuePair("mobilePhoneModel", mobilePhoneModel));
		nameValuePairs.add(new BasicNameValuePair("mobilePhoneProduct", mobilePhoneProduct));
		nameValuePairs.add(new BasicNameValuePair("mobilePhoneManuFacturer", mobilePhoneManuFacturer));

		nameValuePairs.add(new BasicNameValuePair("userToken", Parameters.user.token));

		Log.d("yjx", Parameters.serviceURL.concat("bindPsamCardToUser.json"));
        Log.d("yjx", nameValuePairs.toString());
		resultForService = this.postRequest(Parameters.serviceURL.concat("bindPsamCardToUser.json"), nameValuePairs);
		Log.d("yjx", Parameters.serviceURL.concat("bindPsamCardToUser.json"));
        Log.d("yjx", nameValuePairs.toString());
		return resultForService;
	}

	/**
	 * 检查卡号是否可用
	 * 
	 * @param psamNo
	 *            卡号
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService getPsamCardState(String psamNo) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getPsamCardState/");
		url.append(psamNo);
		url.append(".json");

		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**
	 * 获取绑定列表
	 * 
	 * @param loginName
	 *            登录名
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService getPsamCardBindList(String loginName) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		// 判断 userToken 是否存在。
		UserTokenHavingIsValid();

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userToken", Parameters.user.token));

		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getPsamCardBindList/");
		url.append(loginName);
		url.append(".json");

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/*************************************** 公共接口 ******************************************/

	/**
	 * 获取省份列表
	 * 
	 * @return ResultForService
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService getProvinceList() throws ParseException, IOException, BaseException {

		ResultForService resultForService = null;
		resultForService = this.getRequest(Parameters.serviceURL.concat("getProvinceList.json"), null);

		return resultForService;
	}

	/**
	 * 获取市列表
	 * 
	 * @param provinceId
	 *            省份 ID
	 * @return ResultForService
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService getCityListOfProvince(String provinceId) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getCityListOfProvince/");
		url.append(provinceId);
		url.append(".json");

		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**
	 * 获取区县列表
	 * 
	 * @param cityId
	 *            城市 ID
	 * @return ResultForService
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService getDistrictList(String cityId) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getDistrictList/");
		url.append(cityId);
		url.append(".json");

		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**
	 * 获取公益机构列表
	 * 
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService getNGOList() throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		resultForService = this.getRequest(Parameters.serviceURL.concat("getNGOList.json"), null);

		return resultForService;
	}

	/**
	 * 获取捐款项目列表
	 * 
	 * @param ngoId
	 *            公益机构 ID
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService getProjectListByNGO(String ngoId) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getProjectListByNGO/");
		url.append(ngoId);
		url.append(".json");

		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**
	 * 上传身份证照片
	 * 
	 * @param loginName
	 *            登录名
	 * @param idcard1
	 *            身份证照片文件数据1
	 * @param idcard2
	 *            身份证照片文件数据2
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService idCardImageUpload(String loginName, byte[] idcard1, byte[] idcard2) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		MultiPartEntity mpe = new MultiPartEntity();
		mpe.addPart("idcard1", "idcard1.jpg", idcard1);
		mpe.addPart("idcard2", "idcard2.jpg", idcard2);

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("idCardImageUpload/");
		url.append(loginName);
		url.append(".json");

		resultForService = this.postRequest(url.toString(), mpe);

		return resultForService;
	}

	/**
	 * 收款宝开通商户上传身份证照片
	 * 
	 * @param loginName
	 *            登录名
	 * @param idcard1
	 *            身份证照片文件数据1
	 * @param idcard2
	 *            身份证照片文件数据2
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService idCardImageUploadPos(String loginName, byte[] idcard1, byte[] idcard2, String userName, String cardType, String cardNo, String email) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		MultiPartEntity mpe = new MultiPartEntity();
		mpe.addPart("idcard1", "idcard1.jpg", idcard1);
		mpe.addPart("idcard2", "idcard2.jpg", idcard2);
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("idCardImageUploadPOS/");
		url.append(loginName);
		url.append(".json");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userName", userName));
		nameValuePairs.add(new BasicNameValuePair("idCardType", cardType));
		nameValuePairs.add(new BasicNameValuePair("idCardId", cardNo));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		resultForService = this.postRequest(url.toString(), mpe, nameValuePairs);

		return resultForService;
	}

	/**
	 * 上传日志文件
	 * 
	 * @param loginName
	 *            用户名
	 * @param imei
	 *            手机imei串号
	 * @param logLevel
	 *            日志等级----
	 * @param version
	 *            软件版本号---
	 * @param desc
	 *            日志描述
	 * @param logdata
	 *            日志文件数据
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService uploadErrorLog(String loginName, String imei, String logLevel, String version, String desc, String fileName, byte[] logdata) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		MultiPartEntity mpe = new MultiPartEntity();
		mpe.addPart("loginName", loginName);
		mpe.addPart("imei", imei);
		mpe.addPart("logLevel", logLevel);
		mpe.addPart("version", version);
		mpe.addPart("desc", desc);
		mpe.addPart("logdata", fileName + Util.date2() + ".txt", logdata);

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("uploadErrorLog.json");

		resultForService = this.postRequest(url.toString(), mpe);

		return resultForService;
	}

	/**
	 * @param imei
	 *            手机设备号
	 * @param infoStr
	 *            上传字符串
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService insertStaticesInfo(String imei, String infoStr) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		MultiPartEntity mpe = new MultiPartEntity();
		mpe.addPart("imei", imei);
		mpe.addPart("data", infoStr);

		StringBuffer url = new StringBuffer();
		url.append(Parameters.statisticsURL);
		// url.append("insertStaticesInfo.json");
		url.append("android/data/");

		resultForService = this.postRequest(url.toString(), mpe);

		return resultForService;
	}

	/**
	 * 发送短信校验码
	 * 
	 * @param phoneNumber
	 *            电话号码
	 * @param smsType
	 *            短信模板 = 1：普通验证码短信;2：重置密码短信;
	 * @return ResultForService
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService getMobileVerifyCode(String phoneNumber, String smsType) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("smsType", smsType));

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getMobileVerifyCode/");
		url.append(phoneNumber);
		url.append(".json");

		resultForService = this.getRequest(url.toString(), nameValuePairs);
		Log.d("yjx", url.toString());
        Log.d("yjx", nameValuePairs.toString());
		return resultForService;
	}

	/**
	 * 校验短信验证码
	 * 
	 * @param phoneNumber
	 *            电话号码
	 * @param token
	 *            认证令牌
	 * @param verCode
	 *            验证码
	 * @return ResultForService
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService getMobileVerifyState(String phoneNumber, String token, String verCode) throws ParseException, IOException, BaseException {

		ResultForService resultForService = null;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getMobileVerifyState/");
		url.append(token);
		url.append("/");
		url.append(verCode);
		url.append(".json");

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 根据易拉宝序列号获取绑定手机号
	 * 
	 * @param psamNo
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService getMobileByPsamNo(String psamNo) throws ParseException, IOException, BaseException {

		ResultForService resultForService = null;
		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getMobileByPsamNo/");
		url.append(psamNo);
		url.append(".json");

		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**
	 * 根据易拉宝序列号获取绑定手机号
	 * 
	 * @param psamNo
	 *            刷卡器卡psam号
	 * @param deviceId
	 *            IMSI串口号
	 * @param telecomOperators
	 *            运营商号段 //现值同为IMSI，服务商由服务端验证
	 * @param mobilePhoneModel
	 *            手机标识
	 * @param mobilePhoneProduct
	 *            手机型号
	 * @param mobilePhoneManuFacturer
	 *            手机厂商
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService checkUserDeviceInfo(String psamNo, String deviceId, String telecomOperators, String mobilePhoneModel, String mobilePhoneProduct, String mobilePhoneManuFacturer) throws ParseException, IOException, BaseException {

		ResultForService resultForService = null;
		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("checkUserDeviceInfo.json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("loginName", Parameters.user.userName));
		nameValuePairs.add(new BasicNameValuePair("psamNo", psamNo));
		nameValuePairs.add(new BasicNameValuePair("deviceId", deviceId));
		nameValuePairs.add(new BasicNameValuePair("telecomOperators", telecomOperators));
		nameValuePairs.add(new BasicNameValuePair("mobilePhoneModel", mobilePhoneModel));
		nameValuePairs.add(new BasicNameValuePair("mobilePhoneProduct", mobilePhoneProduct));
		nameValuePairs.add(new BasicNameValuePair("mobilePhoneManuFacturer", mobilePhoneManuFacturer));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取充值金额
	 * 
	 * @param mobile
	 *            充值手机号
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public ResultForService getRechargeAmount(String mobile) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getRechargeAmount.json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("mobile", mobile));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取充值手机历史记录
	 * 
	 * @param mobile
	 *            用户登录名
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getRechargeMobile(String mobile) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getRechargeMobile.json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("mobile", mobile));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 修改手机标识
	 * 
	 * @param mobile
	 *            用户登录名
	 * @param mobileNo
	 *            手机号---手机充值历史里的手机号
	 * @param mobileMark
	 *            手机标识----手机充值的标识
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService updateRechargeMobileMark(String mobile, String mobileNo, String mobileMark) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("updateRechargeMobileMark.json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
		nameValuePairs.add(new BasicNameValuePair("mobileNo", mobileNo));
		nameValuePairs.add(new BasicNameValuePair("mobileMark", mobileMark));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 删除历史充值手机
	 * 
	 * @param mobile
	 *            用户登录名
	 * @param mobileNo
	 *            手机充值历史里的手机号
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService deleteRechargeMobileHis(String mobile, String mobileNo) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("deleteRechargeMobileHis.json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
		nameValuePairs.add(new BasicNameValuePair("mobileNo", mobileNo));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取汇款银行列表
	 * 
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getBankListForRemittance() throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getBankListForRemittance.json");

		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**
	 * 获取银行快速支付列表
	 * 
	 * @param bankCode
	 *            银行code
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 * @see com.chinamobile.schebao.lakala.bll.service.CommonServiceManager#getBankListByKey(String,
	 *      String)
	 */
	public ResultForService getBankListForPay(String bankCode) throws ParseException, IOException, BaseException {
		ResultForService resultForService = getBankListForPay(bankCode, "");
		return resultForService;
	}

	/**
	 * 获取银行快速支付列表
	 * 
	 * @param bankCode
	 *            银行code
	 * @param busId
	 *            业务Id
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getBankListForPay(String bankCode, String busId) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getBankListForPay.json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("bankCode", bankCode));

		if (!Util.isEmpty(busId)) {
			/*
			 * 因为 2.2.0 以前版本中银行列表在显示一个本地没有图标的银行时会错误的显示银行图标， 所以从 3.0.0 版开始在 busid
			 * 后面添加"_1",这样就不会影响老版本的客户端。
			 */
			busId = busId.concat("_1");
			nameValuePairs.add(new BasicNameValuePair("busId", busId));
		}

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取收款方开户网点
	 * 
	 * @param keyWord
	 *            搜索关键字
	 * @param bankCode
	 *            银行Code
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getBankListByKey(String keyWord, String bankCode) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getBankListByKey.json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("keyWord", keyWord));
		nameValuePairs.add(new BasicNameValuePair("bankCode", bankCode));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取图片验证码
	 * 
	 * @param token
	 *            客户端生成一个会话id，用来标识图片验证码的所属对象.
	 * @return
	 * @throws org.apache.http.client.ClientProtocolException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public InputStream getVerCodeImg(String token) throws ClientProtocolException, BaseException, IOException {

		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getVerCodeImg/");
		url.append(token);
		url.append(".do");

		InputStream stream = this.getRequestForStream(url.toString(), null);

		return stream;
	}

	/**
	 * 验证图片验证码
	 * 
	 * @param token
	 *            会话Id
	 * @param verCode
	 *            验证码
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getImgVerCodeVerifyResult(String token, String verCode) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getImgVerCodeVerifyResult/");
		url.append(token);
		url.append("/");
		url.append(verCode);
		url.append(".json");
		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**
	 * 修改个人资料
	 * 
	 * @param loginName
	 * @param realName
	 * @param idCardType
	 *            身份证：ID,军官证：MILITARY_ID,学生证：STUDENT_CARD,护照：PASSPORT,其他：OTHER
	 * @param idCardId
	 *            如果选择证件类型,则此项不能为空
	 * @param email
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService updateUserInfo(String loginName, String realName, String idCardType, String idCardId, String email) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		// 判断 userToken 是否存在。
		UserTokenHavingIsValid();

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("updateUserInfo/");
		url.append(loginName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("loginName", loginName));
		nameValuePairs.add(new BasicNameValuePair("realName", realName));
		nameValuePairs.add(new BasicNameValuePair("idCardType", idCardType));
		nameValuePairs.add(new BasicNameValuePair("idCardId", idCardId));
		nameValuePairs.add(new BasicNameValuePair("email", email));

		nameValuePairs.add(new BasicNameValuePair("userToken", Parameters.user.token));

		resultForService = this.putRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取拉卡拉产品列表
	 * 
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getProductsList() throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getProductsList");
		url.append(".json");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 游戏点卡类型查询
	 * 
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getGameCard() throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getGameCard.json");

		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**
	 * 游戏点卡类型查询
	 * 
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getGameCardType() throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getGameCardType.json");

		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**
	 * 游戏点卡类型查询
	 * 
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getGameCardAmountByCard(String cardType) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getGameCardType.json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("cardType", cardType));
		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 用户卡信息查询
	 * 
	 * @param loginName
	 * @param cardId
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getUserCardRecord(String loginName, String cardId) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		// 判断 userToken 是否存在。
		UserTokenHavingIsValid();

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("cardId", cardId));

		nameValuePairs.add(new BasicNameValuePair("userToken", Parameters.user.token));

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getUserCardRecord/");
		url.append(loginName);
		url.append(".json");

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 用户卡信息查询
	 * 
	 * @param loginName
	 * @param cardId
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getWoYaoShouKuanUserCard() throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		// 判断 userToken 是否存在。
		UserTokenHavingIsValid();

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getUserCardRecord/");
		url.append("getUserCard.json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("loginName", Parameters.user.userName));
		nameValuePairs.add(new BasicNameValuePair("usage", "M50010"));
		nameValuePairs.add(new BasicNameValuePair("userToken", Parameters.user.token));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/*
	 * /** 用户卡信息添加
	 * 
	 * @param loginName 用户登录名
	 * 
	 * @param password 登录密码
	 * 
	 * @param accountName 银行卡账户名
	 * 
	 * @param bankName 银行名
	 * 
	 * @param cardNo 银行所在省
	 * 
	 * @param bankCity 银行
	 * 
	 * @param cardNo 卡号
	 * 
	 * @param bankCode 银行Code
	 * 
	 * @param cityCode 银行所在省Code
	 * 
	 * @param cardType 银行卡类型 001：借记卡、002：信用卡
	 * 
	 * @param subBankFullNameCode 下级银行全称Code，网点列表的sub_code
	 * 
	 * @param bankFullNameCode 银行全称Code，网点列表的 bank_code
	 * 
	 * @param bankFullName 银行全称，网点列表的bank_name
	 * 
	 * @return
	 * 
	 * @throws ParseException
	 * 
	 * @throws IOException
	 * 
	 * @throws BaseException
	 * 
	 * public ResultForService postUserCard( String loginName, String password,
	 * String accountName, String bankName, String bankProvince, String
	 * bankCity, String cardNo, String bankCode, String cityCode, String
	 * cardType, String subBankFullNameCode, String bankFullNameCode, String
	 * bankFullName) throws ParseException, IOException, BaseException {
	 * ResultForService resultForService = null;
	 * 
	 * List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	 * nameValuePairs.add(new BasicNameValuePair("accountName", accountName));
	 * nameValuePairs.add(new BasicNameValuePair("password", password));
	 * nameValuePairs.add(new BasicNameValuePair("bankName", bankName));
	 * nameValuePairs.add(new BasicNameValuePair("bankProvince", bankProvince));
	 * nameValuePairs.add(new BasicNameValuePair("bankCity", bankCity));
	 * nameValuePairs.add(new BasicNameValuePair("cardNo", cardNo));
	 * nameValuePairs.add(new BasicNameValuePair("bankCode", bankCode));
	 * nameValuePairs.add(new BasicNameValuePair("cityCode", cityCode));
	 * nameValuePairs.add(new BasicNameValuePair("cardType", cardType));
	 * nameValuePairs.add(new BasicNameValuePair("subBankFullNameCode",
	 * subBankFullNameCode)); nameValuePairs.add(new
	 * BasicNameValuePair("bankFullNameCode", bankFullNameCode));
	 * nameValuePairs.add(new BasicNameValuePair("bankFullName", bankFullName));
	 * 
	 * StringBuffer url = new StringBuffer(); url.append(Parameters.serviceURL);
	 * url.append("postUserCard/"); url.append(loginName); url.append(".json");
	 * 
	 * resultForService = this.postRequest(url.toString(), nameValuePairs);
	 * 
	 * return resultForService; }
	 */

	/**
	 * 用户卡信息更新
	 * 
	 * @param cardId
	 *            卡 ID
	 * @param loginName
	 *            用户登录名
	 * @param cardMemo
	 *            信用卡别名
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService updateUserCard(String cardId, String loginName, String cardMemo) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		// 判断 userToken 是否存在。
		UserTokenHavingIsValid();

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("cardMemo", cardMemo));

		nameValuePairs.add(new BasicNameValuePair("userToken", Parameters.user.token));

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("updateUserCard/");
		url.append(loginName);
		url.append("/");
		url.append(cardId);
		url.append(".json");

		resultForService = this.putRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 用户卡信息删除
	 * 
	 * @param cardId
	 *            卡 ID
	 * @param loginName
	 *            用户登录名
	 * @param password
	 *            登录密码
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService deleteUserCard(String cardId, String loginName) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		// 判断 userToken 是否存在。
		UserTokenHavingIsValid();

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userToken", Parameters.user.token));

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("deleteUserCard/");
		url.append(loginName);
		url.append("/");
		url.append(cardId);
		url.append(".json");

		resultForService = this.deleteRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 查询信用卡到账日期
	 * 
	 * @param creditcard
	 *            信用卡卡号
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getBankInfo(String creditcard) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		// 判断 userToken 是否存在。
		UserTokenHavingIsValid();

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userToken", Parameters.user.token));

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getBankInfo/");
		url.append(Util.formatString(creditcard));
		url.append(".json");

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 用户卡每日转账限额检查
	 * 
	 * @param amount
	 *            金额
	 * @param loginName
	 *            用户登录名
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService checkAmountLimit(String amount, String loginName) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		// 判断 userToken 是否存在。
		UserTokenHavingIsValid();

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userToken", Parameters.user.token));

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("checkAmountLimit/");
		url.append(loginName);
		url.append("/");
		url.append(amount);
		url.append(".json");

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 重置用户登录密码
	 * 
	 * @param loginName
	 * @param password
	 *            新登录密码
	 * @param re_password
	 *            新登录密码
	 * @param pwdSecLevel
	 *            密码等级
	 * @param token
	 *            认证令牌 由服务器下发
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService resetUserPassword(String loginName, String password, String re_password, String pwdSecLevel, String token) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("password", password));
		nameValuePairs.add(new BasicNameValuePair("re_password", re_password));
		nameValuePairs.add(new BasicNameValuePair("pwdSecLevel", pwdSecLevel));
		nameValuePairs.add(new BasicNameValuePair("token", token));

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("resetUserPassword/");
		url.append(loginName);
		url.append(".json");

		resultForService = this.putRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 交易类型查询
	 * 
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getTradeType() throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getTradeType.json");

		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**
	 * 交易记录查询
	 * 
	 * @param loginName
	 * @param startPage
	 *            起始页
	 * @param pageSize
	 *            每页显示条数,默认（20）
	 * @param startTime
	 *            开始时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @param endTime
	 *            结束时间 时间格式：yyyy-MM-dd HH:mm:ss
	 * @param tradeType
	 *            交易类型
	 * @param psamNo
	 *            刷卡器串号
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getTradeHisList(String loginName, String startPage, String pageSize, String startTime, String endTime, String tradeType, String psamNo) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		// 判断 userToken 是否存在。
		UserTokenHavingIsValid();

		List<NameValuePair> nameValuePairs = createNameValuePair(true);
		nameValuePairs.add(new BasicNameValuePair("startPage", startPage));
		nameValuePairs.add(new BasicNameValuePair("pageSize", pageSize));
		nameValuePairs.add(new BasicNameValuePair("startTime", startTime));
		nameValuePairs.add(new BasicNameValuePair("endTime", endTime));
		nameValuePairs.add(new BasicNameValuePair("tradeType", tradeType));
		nameValuePairs.add(new BasicNameValuePair("psamNo", psamNo));

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getTradeHisList/");
		url.append(loginName);
		url.append(".json");

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取转账汇款手续费
	 * 
	 * @param feeId
	 *            0：获取所有手续费 1：慢转账 2：快速转账 3：实时转账
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 * @type 业务Id false string
	 * @level 支付级别 false string
	 * @channel 支付渠道 false string
	 */
	public ResultForService getFee(String feeId, String type, String level, String channel) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getFee/");
		url.append(feeId);
		url.append(".json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("level", level));
		nameValuePairs.add(new BasicNameValuePair("channel", channel));
		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 款历史卡记录信息查询
	 * 
	 * @param loginName
	 * @param cardId
	 *            卡信息Id,用于单条记录查询
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getUserCardRecordForXYKHK(String loginName, String cardId) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		// 判断 userToken 是否存在。
		UserTokenHavingIsValid();

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getUserCardRecordForXYKHK/");
		url.append(loginName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("userToken", Parameters.user.token));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 版本更新检查
	 * 
	 * @param version
	 *            当前用户版本
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService checkAppUpdate(String version) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getShuakaqiList.json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("version", version));
		resultForService = this.getRequest(url.toString(), nameValuePairs);
		Log.d("yjx", url.toString());
        Log.d("yjx", nameValuePairs.toString());

		return resultForService;
	}

	// =======================================收帐宝功能=======================================

	/**
	 * 获取收账宝收款账号
	 * 
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getSZBAccounts() throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getSZBAccounts/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 添加收账宝收款账号
	 * 
	 * @param cardNo
	 *            银行卡号
	 * @param accountName
	 *            收款人姓名
	 * @param bankName
	 *            开户行行名
	 * @param subBankFullNameCode
	 *            开户行电子联行号
	 * @param bankFullNameCode
	 *            清算行电子联行号
	 * @param bankFullName
	 *            银行全称
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService addSZBAccount(String cardNo, String accountName, String bankName, String bankCode, String subBankFullNameCode, String bankFullNameCode, String bankFullName) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("addSZBAccount/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);

		nameValuePairs.add(new BasicNameValuePair("cardNo", Util.formatString(cardNo)));
		nameValuePairs.add(new BasicNameValuePair("accountName", accountName));
		nameValuePairs.add(new BasicNameValuePair("bankName", bankName));
		nameValuePairs.add(new BasicNameValuePair("bankCode", bankCode));
		nameValuePairs.add(new BasicNameValuePair("subBankFullNameCode", subBankFullNameCode));
		nameValuePairs.add(new BasicNameValuePair("bankFullNameCode", bankFullNameCode));
		nameValuePairs.add(new BasicNameValuePair("bankFullName", bankFullName));

		resultForService = this.postRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 修改收账宝收款账号
	 * 
	 * @param cardId
	 *            账号ID
	 * @param cardNo
	 *            银行卡号
	 * @param accountName
	 *            收款人姓名
	 * @param bankName
	 *            开户行行名
	 * @param subBankFullNameCode
	 *            开户行电子联行号
	 * @param bankFullNameCode
	 *            清算行电子联行号
	 * @param bankFullName
	 *            银行全称
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService updateSZBAccount(String cardId, String cardNo, String accountName, String bankName, String bankCode, String subBankFullNameCode, String bankFullNameCode, String bankFullName) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("updateSZBAccount/");
		url.append(Parameters.user.userName);
		url.append(".json");

		Util.log("err", "cardId   " + cardId + " cardNo " + Util.formatString(cardNo) + " accountName " + accountName + " bankName " + bankName + " bankCode " + bankCode + " subBankFullNameCode  " + subBankFullNameCode + " bankFullNameCode  " + bankFullNameCode + " bankFullName" + bankFullName);

		List<NameValuePair> nameValuePairs = createNameValuePair(true);

		nameValuePairs.add(new BasicNameValuePair("cardId", Util.formatString(cardId)));
		nameValuePairs.add(new BasicNameValuePair("cardNo", Util.formatString(cardNo)));
		nameValuePairs.add(new BasicNameValuePair("accountName", accountName));
		nameValuePairs.add(new BasicNameValuePair("bankName", bankName));
		nameValuePairs.add(new BasicNameValuePair("bankCode", bankCode));
		nameValuePairs.add(new BasicNameValuePair("subBankFullNameCode", subBankFullNameCode));
		nameValuePairs.add(new BasicNameValuePair("bankFullNameCode", bankFullNameCode));
		nameValuePairs.add(new BasicNameValuePair("bankFullName", bankFullName));

		resultForService = this.putRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取收账宝用户模板
	 * 
	 * @param templateId
	 *            模板Id，用于单条记录查询
	 * @param state
	 *            项目状态 0：已完成收款，1：未完成收款
	 * @param startPage
	 *            开始页，从第一页开始。
	 * @param pageSize
	 *            每页条数，默认20条
	 * @param orderBy
	 *            排序：1：完成时间、2：创建时间
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getSZBTemplateList(String templateId, String state, String startPage, String pageSize, String orderBy) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getSZBTemplateList/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);

		nameValuePairs.add(new BasicNameValuePair("templateId", templateId));
		nameValuePairs.add(new BasicNameValuePair("state", state));
		nameValuePairs.add(new BasicNameValuePair("startPage", startPage));
		nameValuePairs.add(new BasicNameValuePair("pageSize", pageSize));
		nameValuePairs.add(new BasicNameValuePair("orderBy", orderBy));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 添加收账宝用户模板
	 * 
	 * @param groupName
	 *            项目名
	 * @param totalAmount
	 *            总金额，默认0
	 * @param defaultItems
	 *            默认收帐人数，默认1
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService addSZBTemplate(String groupName, String totalAmount, String defaultItems) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("addSZBTemplate/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);

		nameValuePairs.add(new BasicNameValuePair("groupName", groupName));
		nameValuePairs.add(new BasicNameValuePair("totalAmount", totalAmount));
		nameValuePairs.add(new BasicNameValuePair("defaultItems", defaultItems));

		resultForService = this.postRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 修改收账宝用户模板
	 * 
	 * @param templateId
	 *            模板ID
	 * @param groupName
	 *            项目名总金额
	 * @param totalAmount
	 *            总金额，默认0
	 * @param defaultItems
	 *            默认收帐人数，默认1
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService updateSZBTemplate(String templateId, String groupName, String totalAmount, String defaultItems) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("updateSZBTemplate/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);

		nameValuePairs.add(new BasicNameValuePair("templateId", templateId));
		nameValuePairs.add(new BasicNameValuePair("groupName", groupName));
		nameValuePairs.add(new BasicNameValuePair("totalAmount", totalAmount));
		nameValuePairs.add(new BasicNameValuePair("defaultItems", defaultItems));

		resultForService = this.putRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 删除收账宝用户模板
	 * 
	 * @param templateId
	 *            模板ID
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService deleteSZBTemplate(String templateId) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("deleteSZBTemplate/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);

		nameValuePairs.add(new BasicNameValuePair("templateId", templateId));

		resultForService = this.deleteRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取收账宝收款项目
	 * 
	 * @param groupId
	 *            项目Id，用于单条记录查询。
	 * @param state
	 *            项目状态 0：已完成收款，1：未完成收款
	 * @param startPage
	 *            开始页，从第一页开始。
	 * @param pageSize
	 *            每页条数，默认20条
	 * @param orderBy
	 *            排序：1：完成时间、2：创建时间
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getSZBGroupList(String groupId, String state, String startPage, String pageSize, String orderBy) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getSZBGroupList/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);

		nameValuePairs.add(new BasicNameValuePair("groupId", groupId));
		nameValuePairs.add(new BasicNameValuePair("state", state));
		nameValuePairs.add(new BasicNameValuePair("startPage", startPage));
		nameValuePairs.add(new BasicNameValuePair("pageSize", pageSize));
		nameValuePairs.add(new BasicNameValuePair("orderBy", orderBy));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 添加收账宝收款项目
	 * 
	 * @param groupName
	 *            项目名总金额
	 * @param totalAmount
	 *            总金额，默认0
	 * @param defaultItems
	 *            默认收帐人数，默认1
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService addSZBGroup(String groupName, String cardId, String totalAmount, String defaultItems) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("addSZBGroup/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);

		nameValuePairs.add(new BasicNameValuePair("groupName", groupName));
		nameValuePairs.add(new BasicNameValuePair("cardId", cardId));
		nameValuePairs.add(new BasicNameValuePair("totalAmount", totalAmount));
		nameValuePairs.add(new BasicNameValuePair("defaultItems", defaultItems));

		resultForService = this.postRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 修改收账宝收款项目
	 * 
	 * @param groupId
	 *            项目索引
	 * @param cardId
	 *            收款账号ID
	 * @param totalAmount
	 *            总金额
	 * @param defaultItems
	 *            默认收帐人数
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService updateSZBGroup(String groupId, String cardId, String totalAmount, String defaultItems) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("updateSZBGroup/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);

		nameValuePairs.add(new BasicNameValuePair("groupId", groupId));
		nameValuePairs.add(new BasicNameValuePair("cardId", cardId));
		nameValuePairs.add(new BasicNameValuePair("totalAmount", totalAmount));
		nameValuePairs.add(new BasicNameValuePair("defaultItems", defaultItems));

		resultForService = this.putRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 完成收账宝收款
	 * 
	 * @param groupId
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService completeSZBGroup(String groupId, ArrayList<Map<String, String>> noPayList) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("completeSZBGroup/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);

		nameValuePairs.add(new BasicNameValuePair("groupId", groupId));
		String prefix = "tabSzbItems[index].";
		for (int i = 0; i < noPayList.size(); i++) {
			Map<String, String> map = noPayList.get(i);
			String itemId = map.get("itemId");
			String amount = map.get("amount");
			String state = map.get("state");
			String payType = map.get("payType");
			String itemDesc = map.get("itemDesc");
			String prefixString = prefix.replaceAll("index", i + "");
			nameValuePairs.add(new BasicNameValuePair(prefixString + "itemId", itemId));
			nameValuePairs.add(new BasicNameValuePair(prefixString + "amount", amount));
			nameValuePairs.add(new BasicNameValuePair(prefixString + "state", state));
			nameValuePairs.add(new BasicNameValuePair(prefixString + "payType", payType));
			nameValuePairs.add(new BasicNameValuePair(prefixString + "itemDesc", itemDesc));
		}

		resultForService = this.putRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取收款账单列表
	 * 
	 * @param groupId
	 *            项目索引
	 * @param itemId
	 *            条目索引
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getSZBItems(String groupId, String itemId) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getSZBItems/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);

		nameValuePairs.add(new BasicNameValuePair("groupId", groupId));
		nameValuePairs.add(new BasicNameValuePair("itemId", itemId));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 添加账单信息
	 * 
	 * @param groupId
	 *            项目索引
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService addSZBItem(String groupId, String amount) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("addSZBItem/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);

		nameValuePairs.add(new BasicNameValuePair("groupId", groupId));
		nameValuePairs.add(new BasicNameValuePair("amount", amount));

		resultForService = this.postRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 修改收账信息
	 * 
	 * @param groupId
	 *            项目索引
	 * @param itemId
	 *            账单索引
	 * @param amount
	 *            金额
	 * @param state
	 *            支付状态，0：已支付、1：未支付
	 * @param payType
	 *            支付类型，1：刷卡支付、2：短信支付、9：未指定
	 * @param termid
	 *            终端号，如果支付类型选择"1",则必须上传终端号
	 * @param itemDesc
	 *            描述信息
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService updateSZBItem(String groupId, String itemId, String amount, String state, String payType, String termid, String itemDesc, String mobile) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("updateSZBItem/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);

		nameValuePairs.add(new BasicNameValuePair("groupId", groupId));
		nameValuePairs.add(new BasicNameValuePair("itemId", itemId));
		nameValuePairs.add(new BasicNameValuePair("amount", amount));
		nameValuePairs.add(new BasicNameValuePair("state", state));
		nameValuePairs.add(new BasicNameValuePair("payType", payType));
		nameValuePairs.add(new BasicNameValuePair("termid", termid));
		nameValuePairs.add(new BasicNameValuePair("itemDesc", itemDesc));
		nameValuePairs.add(new BasicNameValuePair("busid", "M50011"));
		nameValuePairs.add(new BasicNameValuePair("mobile", mobile));

		resultForService = this.putRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 收账宝查询
	 * 
	 * @param groupId
	 *            项目索引
	 * @param itemId
	 *            账单索引
	 * @param amount
	 *            金额
	 * @param state
	 *            支付状态，0：已支付、1：未支付
	 * @param payType
	 *            支付类型，1：刷卡支付、2：短信支付、9：未指定
	 * @param termid
	 *            终端号，如果支付类型选择"1",则必须上传终端号
	 * @param itemDesc
	 *            描述信息
	 * @param tranType
	 *            转账方式 普通，快速，实时三种
	 * @param billno
	 *            收款卡号 false string 版本号>=17,该域为必须上送
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService querySZBItemTrans(String groupId, String itemId, String amount, String state, String payType, String termid, String itemDesc, String mobile, String tranType, String billno) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("updateSZBItem/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);

		nameValuePairs.add(new BasicNameValuePair("groupId", groupId));
		nameValuePairs.add(new BasicNameValuePair("itemId", itemId));
		nameValuePairs.add(new BasicNameValuePair("amount", amount));
		nameValuePairs.add(new BasicNameValuePair("state", state));
		nameValuePairs.add(new BasicNameValuePair("payType", payType));
		nameValuePairs.add(new BasicNameValuePair("billno", Util.formatString(billno)));
		nameValuePairs.add(new BasicNameValuePair("itemDesc", itemDesc));
		nameValuePairs.add(new BasicNameValuePair("busid", "M50011"));
		nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
		nameValuePairs.add(new BasicNameValuePair("tranType", tranType));

		resultForService = this.putRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 删除收账信息
	 * 
	 * @param groupId
	 *            条目索引
	 * @param itemId
	 *            条目索引
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService deleteSZBItem(String groupId, String itemId) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("deleteSZBItem/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);

		nameValuePairs.add(new BasicNameValuePair("groupId", groupId));
		nameValuePairs.add(new BasicNameValuePair("itemId", itemId));

		resultForService = this.deleteRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 检查手机号收款是否开通
	 * 
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService checkMobilePayAcct() throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("checkMobilePayAcct/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	public ResultForService openMobilePayAcct(String accountName, String bankName, String bankFullName, String cardNo, String bankCode, String bankFullNameCode, String subBankFullNameCode) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("openMobilePayAcct/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);
		nameValuePairs.add(new BasicNameValuePair("accountName", accountName));
		nameValuePairs.add(new BasicNameValuePair("bankName", bankName));
		nameValuePairs.add(new BasicNameValuePair("bankFullName", bankFullName));
		nameValuePairs.add(new BasicNameValuePair("cardNo", cardNo));
		nameValuePairs.add(new BasicNameValuePair("bankCode", bankCode));
		nameValuePairs.add(new BasicNameValuePair("bankFullNameCode", bankFullNameCode));
		nameValuePairs.add(new BasicNameValuePair("subBankFullNameCode", subBankFullNameCode));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 检查手机号收款是否开通
	 * 
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService sendSMS(String mobile, String content) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("sendSMS/");
		url.append(mobile);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(false);
		nameValuePairs.add(new BasicNameValuePair("content", content));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取收账宝短信模板
	 * 
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getSZBSMSTemplate(String amount) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getSZBSMSTemplate/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);
		nameValuePairs.add(new BasicNameValuePair("amount", amount));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 删除收账宝项目
	 * 
	 * @param groupId
	 *            项目Id
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService deleteSZBGroup(String groupId) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("deleteSZBGroup/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);
		nameValuePairs.add(new BasicNameValuePair("groupId", groupId));

		resultForService = this.deleteRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取身份证照片
	 * 
	 * @param loginName
	 *            用户名
	 * @param userToken
	 *            客户端生成一个会话id，用来标识图片验证码的所属对象.
	 * @param idCardImageName
	 *            身份证照片名称
	 * @return
	 * @throws org.apache.http.client.ClientProtocolException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public InputStream getIdcardImg(String loginName, String userToken, String idCardImageName) throws ClientProtocolException, BaseException, IOException {

		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getUserIdCardImage/");
		url.append(loginName);
		url.append(".do?");
		List<NameValuePair> nameValuePairs = createNameValuePair(true);
		nameValuePairs.add(new BasicNameValuePair("userToken", userToken));
		nameValuePairs.add(new BasicNameValuePair("idCardImageName", idCardImageName));
		InputStream stream = this.getRequestForStream(url.toString(), nameValuePairs);

		return stream;
	}

	/**
	 * 获取银行城市列表
	 * 
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getDictAddress() throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getDictAddress.json");

		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**
	 * 获取收账宝项目最大金额
	 * 
	 * @param busId
	 *            业务Id 此项不指定则查询所有业务的最大额列表
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getBusMaxAmount(String busId) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getBusMaxAmount.json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);
		nameValuePairs.add(new BasicNameValuePair("busId", busId));
		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取公缴城市列表
	 * 
	 * @param business
	 *            业务类型
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getCityList(String business) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getCityList/");
		url.append(business);
		url.append(".json");

		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**
	 * 获取缴费配置信息
	 * 
	 * @param business
	 *            业务类型
	 * @param city
	 *            缴费城市
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getBizConfig(String business, String city) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getBizConfig/");
		url.append(business);
		url.append("/");
		url.append(city);
		url.append(".json");

		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**
	 * 获取刷卡器列表
	 * 
	 * @param business
	 *            业务类型
	 * @param city
	 *            缴费城市
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getUnitList() throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getUnitList");
		url.append(".json");

		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**
	 * 新的获取刷卡器列表接口
	 * 
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getNewUnitList() throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getUnitListNew");
		url.append(".json");
		resultForService = this.getRequest(url.toString(), null);
		return resultForService;
	}

	/**************************************************************************
	 * 网点地图接口结束
	 ***************************************************************************/
	/**
	 * IP定位城市
	 * 
	 * @param ip
	 *            IP地址
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getUserPositionByIP(String ip) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getUserPositionByIP.json");

		List<NameValuePair> nameValuePairs = createNameValuePair(false);
		nameValuePairs.add(new BasicNameValuePair("ip", ip));
		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 分页显示用户附近的拉卡拉网点
	 * 
	 * @param swx
	 *            西南角经度
	 * @param swy
	 *            西南角纬度
	 * @param nex
	 *            东北角经度
	 * @param ney
	 *            东北角纬度
	 * @param centx
	 *            中心点经度
	 * @param centy
	 *            中心点纬度
	 * @param height
	 *            界面高度
	 * @param scale
	 *            比例尺
	 * @param pageSize
	 *            每页条数
	 * @param pageStart
	 *            起始页数
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getStoreInfos(String swx, String swy, String nex, String ney, String centx, String centy, String height, String scale, String pageSize, String pageStart) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getStoreInfos.json");

		List<NameValuePair> nameValuePairs = createNameValuePair(false);
		nameValuePairs.add(new BasicNameValuePair("swx", swx));
		nameValuePairs.add(new BasicNameValuePair("swy", swy));
		nameValuePairs.add(new BasicNameValuePair("nex", nex));
		nameValuePairs.add(new BasicNameValuePair("ney", ney));
		nameValuePairs.add(new BasicNameValuePair("centx", centx));
		nameValuePairs.add(new BasicNameValuePair("centy", centy));
		nameValuePairs.add(new BasicNameValuePair("height", height));
		nameValuePairs.add(new BasicNameValuePair("scale", scale));
		nameValuePairs.add(new BasicNameValuePair("pageSize", pageSize));
		nameValuePairs.add(new BasicNameValuePair("pageStart", pageStart));
		nameValuePairs.add(new BasicNameValuePair("phone", "phone"));
		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取网点城市列表
	 * 
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getMapCityList() throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getMapCityList");
		url.append(".json");

		List<NameValuePair> nameValuePairs = createNameValuePair(false);
		nameValuePairs.add(new BasicNameValuePair("hasSotre", "true"));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取网点详情
	 * 
	 * @param sno
	 *            网点编号
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getStoreInfo(String sno) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getStoreInfo/");
		url.append(sno);
		url.append(".json");

		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**
	 * 获取网点提示信息
	 * 
	 * @param sno
	 *            网点编号
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getStoreName(String sno) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getStoreName/");
		url.append(sno);
		url.append(".json");

		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**
	 * 获取网点路线图
	 * 
	 * @param sno
	 *            网点编号
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getStoreRoute(String sno) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getStoreRoute/");
		url.append(sno);
		url.append(".json");

		resultForService = this.getRequest(url.toString(), null);

		return resultForService;
	}

	/**************************************************************************
	 * 网点地图接口结束
	 ***************************************************************************/

	/**
	 * 获取业务介绍广告，用于介绍业务信息,返回HTML信息
	 * 
	 * @param busid
	 *            业务代码ID
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getBusiDesc(String busId) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getBusiDesc.json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("busId", busId));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取账户信息
	 * 
	 * @param billNo
	 *            交易单号
	 * @param mobile
	 *            手机号码
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getAccountInfo(String billNo, String mobile) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getAccountInfo/");
		url.append(billNo);
		url.append(".json");

		// 判断 userToken 是否存在。
		UserTokenHavingIsValid();

		List<NameValuePair> nameValuePairs = createNameValuePair(true);
		nameValuePairs.add(new BasicNameValuePair("mobile", mobile));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 根据银行卡号获取卡信息
	 * 
	 * @param cardNo
	 *            银行卡号
	 * @return
	 * @throws BaseException
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 */
	public ResultForService getbankByCardNo(String cardNo, String bankCode) throws BaseException, ParseException, IOException {
		ResultForService resultForService = null;
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getbankByCardNo/");
		url.append(Util.formatString(cardNo));
		url.append(".json");

		// 判断 userToken 是否存在。
		UserTokenHavingIsValid();

		List<NameValuePair> nameValuePairs = createNameValuePair(false);
		nameValuePairs.add(new BasicNameValuePair("bankCode_tl", bankCode));
		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**************************************************************************
	 * 账单分期接口开始
	 ***************************************************************************/

	/**
	 * 获取账单分期支持的银行列表
	 * 
	 * @param bankcode
	 *            银行code
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService getZDFQCredits(String bankcode) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getZDFQCredits.json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("bankcode", bankcode));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 获取创建的账单分期历史记录
	 * 
	 * @param startPage
	 *            开始页----开始页1。如果不填，则获取所有数据
	 * @param pageSize
	 *            每页条数 ----默认20条
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */

	public ResultForService getZDFQRecords(String startPage, String pageSize) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		// 判断 userToken 是否存在。
		UserTokenHavingIsValid();

		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("getZDFQRecords/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userToken", Parameters.user.token));
		nameValuePairs.add(new BasicNameValuePair("startPage", startPage));
		nameValuePairs.add(new BasicNameValuePair("pageSize", pageSize));

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 删除分期历史记录
	 * 
	 * @param recordId
	 *            记录索引
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService deleteZDFQRecord(String recordId) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		// 判断 userToken 是否存在。
		UserTokenHavingIsValid();
		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("deleteZDFQRecord/");
		url.append(Parameters.user.userName);
		url.append(".json");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userToken", Parameters.user.token));
		nameValuePairs.add(new BasicNameValuePair("recordId", recordId));

		resultForService = this.deleteRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**
	 * 分期费率查询
	 * 
	 * @param billno
	 *            账单号
	 * @param mobileno
	 *            通知手机号码
	 * @param bankcode
	 *            银行代码
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService queryTrans(String billno, String mobileno, String bankcode) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		// 设置参数
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("queryTrans.json");

		List<NameValuePair> nameValuePairs = createNameValuePair(true);
		nameValuePairs.add(new BasicNameValuePair("busid", "16L"));
		nameValuePairs.add(new BasicNameValuePair("mobile", Parameters.user.userName));
		nameValuePairs.add(new BasicNameValuePair("billno", billno));
		nameValuePairs.add(new BasicNameValuePair("mobileno", mobileno));
		nameValuePairs.add(new BasicNameValuePair("bankcode", bankcode));

		resultForService = this.postRequest(url.toString(), nameValuePairs);

		return resultForService;
	}

	/**************************************************************************
	 * 账单分期接口结束
	 ***************************************************************************/

	/**
	 * 获取手机刷卡器产品id
	 * 
	 * @return
	 * 
	 * @throws org.apache.http.ParseException
	 * 
	 * @throws java.io.IOException
	 * 
	 * @throws BaseException
	 */
	public ResultForService getShuaKaQiProductId() throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		StringBuffer url = new StringBuffer();
		url.append(Parameters.bianlitehuiServiceUrl);
		url.append("getValue");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("key", "lklskq.productid"));
		resultForService = this.getRequest(url.toString(), nameValuePairs);
		return resultForService;
	}

	/**
	 * 上传日志文件
	 * 
	 * @param loginName
	 *            用户名
	 * @param imei
	 *            手机imei串号
	 * @param logLevel
	 *            日志等级----
	 * @param version
	 *            软件版本号---
	 * @param desc
	 *            日志描述
	 * @param logdata
	 *            日志文件数据
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws BaseException
	 */
	public ResultForService uploadStatisticsLog(String imei, String fileName, byte[] logdata) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		MultiPartEntity mpe = new MultiPartEntity();
		mpe.addPart("imei", imei);
		mpe.addPart("ip", "");
		mpe.addPart("logdata", fileName + ".txt", logdata);

		StringBuffer url = new StringBuffer();
		url.append(Parameters.statisticsURL);
		// url.append("uploadCsvFile.json");
		url.append("android/file/");

		resultForService = this.postRequest(url.toString(), mpe);

		return resultForService;
	}

	/**
	 * 获取手机客户端Splash页面
	 * 
	 * @param resulation
	 *            手机屏幕分辨率，根据屏幕密度，指定一个对应的分辨率
	 * @return
	 * @throws BaseException
	 * @throws IOException
	 */
	public ResultForService getMobilehomePic(String resulation) throws BaseException, IOException {

		// 设置参数
		List<NameValuePair> nameValuePairs = createNameValuePair(false);
		nameValuePairs.add(new BasicNameValuePair("resulation", resulation));

		ResultForService resultForService = this.getRequest(Parameters.serviceURL.concat("getMobilehomePic.json"), nameValuePairs);

		return resultForService;
	}
	
	/**
	 * 设备激活数据上传
	 * 
	 * @param mobile
	 *            手机号
	 * @param seid
	 *            安全域ID
	 * @param imsi
	 *            移动用户识别码
	 * @return ResultForService
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService uploadDeviceData(String mobile, String seid, String imsi) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
		nameValuePairs.add(new BasicNameValuePair("SEID", seid));
		nameValuePairs.add(new BasicNameValuePair("IMSI", imsi));
		
		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("NFCActive.json");

		resultForService = this.getRequest(url.toString(), nameValuePairs);

		return resultForService;
	}
	
	/**
	 * 手机号校验
	 * 
	 * @param mobile
	 *            手机号
	 * @return ResultForService
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService isChinaMobile(String mobile) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
		
		resultForService = this.getRequest(
				Parameters.serviceURL.concat("isChinaMobile.json"), 
				nameValuePairs);
		Log.d("yjx", Parameters.serviceURL.concat("isChinaMobile.json"));
        Log.d("yjx", nameValuePairs.toString());
		return resultForService;
	}
	
	/**
	 * 上传心电数据
	 * 
	 * @param loginName
	 *            用户名
	 * @param imei
	 *            手机imei串号
	 * @param logLevel
	 *            日志等级----
	 * @param version
	 *            软件版本号---
	 * @param desc
	 *            日志描述
	 * @param logdata
	 *            日志文件数据
	 * @return
	 * @throws org.apache.http.ParseException
	 * @throws java.io.IOException
	 * @throws BaseException
	 */
	public ResultForService uploadECGData(String loginName, String imei, String logLevel, String version, String desc, List<ECGData> lstData) throws ParseException, IOException, BaseException {
		ResultForService resultForService = null;
		MultiPartEntity mpe = new MultiPartEntity();
		mpe.addPart("loginName", loginName);
		mpe.addPart("imei", imei);
		mpe.addPart("logLevel", logLevel);
		mpe.addPart("version", version);
		mpe.addPart("desc", desc);
//		mpe.addPart("logdata", fileName + Util.date2() + ".txt", logdata);

		StringBuffer url = new StringBuffer();
		url.append(Parameters.serviceURL);
		url.append("uploadErrorLog.json");

		resultForService = this.postRequest(url.toString(), mpe);

		return resultForService;
	}
}
