package com.cardiograph.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.cardiograph.constance.Parameters;

/**
 * @author 金同宝
 * 
 *         <pre>
 * REST WebService 访问接口 
 * 应用所有与服务端的交互都要通过此接口
 * </pre>
 */
public class RestWebService {

	public RestWebService() {
	}

	/**
	 * Get 方式提交,用于统计
	 * 
	 * @param URL
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 */
	public static String getRequestWithStatistic(String URL)
			throws IOException, BaseException {
		String ret = null;
		HttpGet request = new HttpGet(URL);
		ret = ExecuteRequestForString(request);
		return ret;
	}

	/**
	 * Get 方式提交
	 * 
	 * @param URL
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public static JSONObject getRequest(String URL) throws ParseException,
			IOException, BaseException {
		JSONObject ret = null;
		HttpGet request = new HttpGet(URL);
		ret = ExecuteRequest(request);
		return ret;
	}

	/**
	 * Get 方式提交
	 * 
	 * @param URL
	 *            要请求的 URL
	 * @param nameValuePairs
	 *            参数列表
	 * @param charsetName
	 *            url 编码字符集名称，如 "UTF-8"
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public static JSONObject getRequest(String URL,
			List<NameValuePair> nameValuePairs, String charsetName)
			throws ParseException, IOException, BaseException {
		JSONObject ret = null;
		String parameter = toUrlParameter(nameValuePairs, charsetName);
		HttpGet request = new HttpGet(URL.concat(parameter));
		ret = ExecuteRequest(request);

		return ret;
	}

	/**
	 * Get 方式提交
	 * 
	 * @param URL
	 *            要请求的 URL
	 * @param nameValuePairs
	 *            参数列表
	 * @param charsetName
	 *            url 编码字符集名称，如 "UTF-8"
	 * @return 返回输入流
	 * @throws BaseException
	 * @throws org.apache.http.client.ClientProtocolException
	 * @throws java.io.IOException
	 */
	public static InputStream getRequestForStream(String URL,
			List<NameValuePair> nameValuePairs, String charsetName)
			throws BaseException, ClientProtocolException, IOException {
		InputStream ret = null;
		String parameter = toUrlParameter(nameValuePairs, charsetName);
		HttpGet request = new HttpGet(URL.concat(parameter));

		ret = ExecuteRequestForStream(request);

		return ret;
	}

	/**
	 * post方式提交,返回json 格式字符串
	 * 
	 * @param URL
	 * @param entity
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public static JSONObject postRequest(String URL, HttpEntity entity)
			throws ParseException, IOException, BaseException {
		JSONObject ret = null;
		HttpPost request = new HttpPost(URL);
		if (entity != null) {
			request.setEntity(entity);
		}

		ret = ExecuteRequest(request);
		return ret;
	}

	/**
	 * Post 方式提交
	 * 
	 * @param URL
	 *            要请求的 URL
	 * @param nameValuePairs
	 *            参数列表
	 * @param charsetName
	 *            url 编码字符集名称，如 "UTF-8"
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public static JSONObject postRequest(String URL,
			List<NameValuePair> nameValuePairs, String charsetName)
			throws ParseException, IOException, BaseException {
		JSONObject ret = null;
		HttpPost request = new HttpPost(URL);
		HttpEntity entity;
		entity = new UrlEncodedFormEntity(nameValuePairs, charsetName);
		request.setEntity(entity);

		// 发送请求
		ret = ExecuteRequest(request);
		return ret;
	}

	/**
	 * put方式提交
	 * 
	 * @param URL
	 * @param entity
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public static JSONObject putRequest(String URL, HttpEntity entity)
			throws ParseException, IOException, BaseException {
		JSONObject ret = null;
		HttpPut request = new HttpPut(URL);
		if (entity != null) {
			request.setEntity(entity);
		}
		request.setHeader("Content-Type", "application/x-www-form-urlencoded");
		// 发送请求
		ret = ExecuteRequest(request);
		return ret;
	}

	/**
	 * Put 方式提交
	 * 
	 * @param URL
	 *            要请求的 URL
	 * @param nameValuePairs
	 *            参数列表
	 * @param charsetName
	 *            url 编码字符集名称，如 "UTF-8"
	 * @return
	 * @throws BaseException
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 */
	public static JSONObject putRequest(String URL,
			List<NameValuePair> nameValuePairs, String charsetName)
			throws ParseException, IOException, BaseException {
		JSONObject ret = null;
		HttpPut request = new HttpPut(URL);
		HttpEntity entity;

		entity = new UrlEncodedFormEntity(nameValuePairs, charsetName);
		request.setEntity(entity);

		request.setHeader("Content-Type", "application/x-www-form-urlencoded");
		// 发送请求
		ret = ExecuteRequest(request);
		return ret;
	}

	/**
	 * deleteRequest 方式提交
	 * 
	 * @param URL
	 *            要请求的 URL
	 * @param nameValuePairs
	 *            参数列表
	 * @param charsetName
	 *            url 编码字符集名称，如 "UTF-8"
	 * @return
	 * @throws BaseException
	 * @throws org.apache.http.client.ClientProtocolException
	 * @throws java.io.IOException
	 */
	public static JSONObject deleteRequest(String URL,
			List<NameValuePair> nameValuePairs, String charsetName)
			throws ParseException, IOException, BaseException {
		JSONObject ret = null;

		String parameter = toUrlParameter(nameValuePairs, charsetName);
		HttpDelete request = new HttpDelete(URL.concat(parameter));

		// 发送请求
		ret = ExecuteRequest(request);
		return ret;
	}

	/**
	 * 将键值对转换成 Url 参数字符串
	 * 
	 * @param parameters
	 *            参数列表
	 * @param charsetName
	 * @return 返回如下形式的字符串 :?var1=1&var2=2
	 */
	public static String toUrlParameter(List<NameValuePair> parameters,
			String charsetName) {
		if (parameters == null)
			return null;

		StringBuffer urlEncode = new StringBuffer();
		int size = parameters.size();

		for (int index = 0; index < size; index++) {
			NameValuePair vp = parameters.get(index);

			if (index == 0)
				urlEncode.append('?');
			else
				urlEncode.append('&');

			urlEncode.append(vp.getName());
			urlEncode.append('=');

			try {
				urlEncode.append(URLEncoder.encode(vp.getValue(), charsetName));
			} catch (UnsupportedEncodingException e) {
				urlEncode.append(vp.getValue());
			}
		}

		return urlEncode.toString();
	}

	/**
	 * 执行 Http 请求。
	 * 
	 * @param request
	 * @return 返回 json 对象
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 * @throws BaseException
	 */
	private static JSONObject ExecuteRequest(HttpUriRequest request)
			throws ParseException, IOException, BaseException {
		JSONObject ret = null;

		// 强制在 http 头中指定charset ，以免服务器误解码。
		AddCharsetToHeader(request);

		// 执行http请求，返回应答数据。
		HttpResponse response = HttpUtil.getHttpClient().execute(request);

		// 判断服务器应该状态，如果状态正常则开始解析返回的json 串
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

			String retString = EntityUtils.toString(response.getEntity());

			// 解析返回的内容
			try {
				ret = new JSONObject(retString);
				if (Parameters.debug) {
					System.out.println("返回200，json:" + ret);
				}
			} catch (JSONException e) {
				// json 解析异常服务返回了错误的数据，在这返回自定义的异常类。
				throw new BaseException("",
						ExceptionCode.ServerResultDataError, 0);
			}
		} else {
			// 非200状态时，关闭连接
			try {
				request.abort();
			} catch (UnsupportedOperationException e) {
			}

			// 服务器返回了错误，抛出异常
			throw new BaseException(response.getStatusLine().getReasonPhrase(),
					ExceptionCode.ServerHttpError, response.getStatusLine()
							.getStatusCode());
		}

		return ret;
	}

	/**
	 * 执行 Http 请求,返回字符串。
	 * 
	 * @param request
	 * @return 返回字符串
	 * @throws java.io.IOException
	 * @throws org.apache.http.ParseException
	 * @throws BaseException
	 */
	private static String ExecuteRequestForString(HttpUriRequest request)
			throws IOException, BaseException {
		String ret = null;

		// 强制在 http 头中指定charset ，以免服务器误解码。
		AddCharsetToHeader(request);

		// 执行http请求，返回应答数据。
		HttpResponse response = HttpUtil.getHttpClient().execute(request);

		// 判断服务器应该状态，如果状态正常则开始解析返回的json 串
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			ret = EntityUtils.toString(response.getEntity());
		} else {
			// 非200状态时，关闭连接
			try {
				request.abort();
			} catch (UnsupportedOperationException e) {
			}

			// 服服器返回了错误，抛出异常
			throw new BaseException(response.getStatusLine().getReasonPhrase(),
					ExceptionCode.ServerHttpError, response.getStatusLine()
							.getStatusCode());
		}

		return ret;
	}

	/**
	 * 执行 Http 请求。
	 * 
	 * @param request
	 * @return 返回 json 输入流
	 * @throws java.io.IOException
	 * @throws BaseException
	 * @throws org.apache.http.client.ClientProtocolException
	 */
	private static InputStream ExecuteRequestForStream(HttpUriRequest request)
			throws BaseException, ClientProtocolException, IOException {
		InputStream ret = null;

		// 强制在 http 头中指定charset ，以免服务器误解码。
		AddCharsetToHeader(request);

		HttpClient httpclient = HttpUtil.getHttpClient();
		
		// 请求超时
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		// 读取超时
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				10000);

		// 执行http请求，返回应答数据。
		HttpResponse response = httpclient.execute(request);
		
		int statusCode = response.getStatusLine().getStatusCode();

		// 判断服务器应该状态，如果状态正常则返回输入流;
		if (statusCode == HttpStatus.SC_OK) {

			if (response.getEntity().isStreaming()) {
				return response.getEntity().getContent();
			} else {
				return ret;
			}
		} else {
			// 非200状态时，关闭连接
			try {
				request.abort();
			} catch (UnsupportedOperationException e) {
			}

			// 服服器返回了错误，抛出异常
			throw new BaseException(response.getStatusLine().getReasonPhrase(),
					ExceptionCode.ServerHttpError, response.getStatusLine()
							.getStatusCode());
		}
	}

	/**
	 * 强制在 http 头中指定charset ，以免服务器误解码。
	 * 
	 * @param request
	 */
	private static void AddCharsetToHeader(HttpUriRequest request) {
		Header ht = null;
		if (request instanceof HttpPost || request instanceof HttpPut) {
			HttpEntityEnclosingRequest hp = (HttpEntityEnclosingRequest) request;
			ht = hp.getEntity().getContentType();
		}

		String contentType = "";
		if (ht != null) {
			contentType = ht.getValue();
		}
		contentType += ";charset=" + HttpUtil.EncodingCharset;
		request.setHeader("Content-Type", contentType);
	}

}
