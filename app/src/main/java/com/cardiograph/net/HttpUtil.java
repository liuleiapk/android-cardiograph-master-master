package com.cardiograph.net;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.cardiograph.constance.Parameters;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUtil {
	
	/**
	 * 数据编码字符集
	 */
	public static final String EncodingCharset = HTTP.UTF_8;
	
	private static HttpClient customHttpClient;

	private HttpUtil() {
	}

	public static synchronized HttpClient getHttpClient() {
		if (customHttpClient == null) {
			//设置协议相关参数
	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, EncodingCharset);
	        HttpProtocolParams.setHttpElementCharset(params,EncodingCharset);
	        HttpProtocolParams.setUseExpectContinue(params, true);
	        HttpProtocolParams.setUserAgent(params, "Lakala Mobile/1.0.0.0");
	        //设置连接超时
	        HttpConnectionParams.setConnectionTimeout(params, 70000);
	        //设置socket超时
	        HttpConnectionParams.setSoTimeout(params, 70000);
	        //设置取连接池连接超时,默认是0
	        ConnManagerParams.setTimeout(params, 1000);
	        
	        //设置Scheme
	        SchemeRegistry schReg = new SchemeRegistry();

            SSLSocketFactory sf = SSLSocketFactory.getSocketFactory();
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                sf = new MySSLSocketFactory(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
	            schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

            }catch (Exception e){
                if(Parameters.debug){
                    Log.e("HtttpUtil", "err", e);
                }
            }

            if(!Parameters.userLanServer && !Parameters.useDeveloperURL){
                schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            }else{

                schReg.register(new Scheme("https", sf, 443));
            }

            ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params,schReg);

            customHttpClient = new DefaultHttpClient(conMgr, params);
		}
		else
		{
			//关闭连接池中无效的连接
			customHttpClient.getConnectionManager().closeExpiredConnections();
			//关闭连接池中空闲的连接
			customHttpClient.getConnectionManager().closeIdleConnections(60, TimeUnit.SECONDS);
		}

		return customHttpClient;
	}
	
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}


    public static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }



}
