package com.cardiograph.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.cardiograph.constance.Constance;

public class RequestTask extends AsyncTask<Void, Integer, String>{

	private String url;
	private String type;
	private List<NameValuePair> nvpList;
	private InputStream is;
	private String strContent;
	private ResponseCallBack callBack;

	public String getStrContent() {
		return strContent;
	}

	public RequestTask(String url, String type, List<NameValuePair> nvpList, ResponseCallBack callBack) {
		super();
		this.url = url;
		this.type = type;
		this.nvpList = nvpList;
		this.callBack = callBack;
	}

	public interface ResponseCallBack {
		void onExecuteResult(String response);
	}

	@Override
	protected String doInBackground(Void... params) {
		System.out.println("SendThread:1");
		System.out.println(nvpList);
		if(type.equals(Constance.POST)){
			try {
				HttpEntity httpEntity=new UrlEncodedFormEntity(nvpList, "gbk");
				HttpPost httpPost=new HttpPost(url);
				httpPost.setEntity(httpEntity);

				HttpClient httpClient=new DefaultHttpClient();

				httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
				httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);

				HttpResponse httpResponse=httpClient.execute(httpPost);
				int code=httpResponse.getStatusLine().getStatusCode();
				System.out.println("code:"+code);
				if(code==HttpStatus.SC_OK){
					System.out.println("SendThread:3");
					HttpEntity httpRespEntity=httpResponse.getEntity();
					strContent = EntityUtils.toString(httpRespEntity, "GBK");
					System.out.println("strContent="+strContent);
				}
			}catch (ConnectTimeoutException e) {
				e.printStackTrace();
			}catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				//HttpEntity httpEntity=new UrlEncodedFormEntity(nvpList);
				HttpGet httpGet=new HttpGet(url);
				//			httpPost.setEntity(httpEntity);

				HttpClient httpClient=new DefaultHttpClient();

				//			BasicHttpParams httpParameters = new BasicHttpParams();
				//			//connection
				//			HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
				//			//response
				//			HttpConnectionParams.setSoTimeout(httpParameters, 30000);

				httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
				httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);


				HttpResponse httpResponse=httpClient.execute(httpGet);

				int code=httpResponse.getStatusLine().getStatusCode();
				System.out.println("code:"+code);
				//System.out.println("SendThread:2");
				if(code==HttpStatus.SC_OK){
					System.out.println("httpGet~~~~~~~~");
					HttpEntity httpRespEntity=httpResponse.getEntity();
					strContent = EntityUtils.toString(httpRespEntity, "GBK");
					System.out.println("strContent="+strContent);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return strContent;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	}

    @Override
    protected void onPostExecute(String result) {
		callBack.onExecuteResult(result);
		nvpList.removeAll(nvpList);//避免数据产生冲突
    }
}
