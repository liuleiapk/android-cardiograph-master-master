package com.cardiograph.log;

import java.io.File;
import java.io.FileOutputStream;

import com.cardiograph.commom.ApplicationExtension;
import com.cardiograph.constance.Parameters;

/**
 * 通用日志类
 * @author 金同宝
 *
 */
public class Logger {
	
	public static Logger instance = new Logger();
	private String logFileName = "/data/data/"+ ApplicationExtension.getInstance().getPackageName() + "/log/log.txt";
	//private String logFileName =  android.os.Environment.getExternalStorageDirectory().getPath().concat("/lakala/log.txt");
		
	private Logger(){
		File file = new File(logFileName);
		File dir = new File(file.getParent());
		if (!dir.exists()) {
			dir.mkdirs();
		}		
	}
	
	/**
	 * 将日志信息写入文件
	 * @param logContent   日志信息
	 */
	public void logout(String logContent){
		try {
			logContent="\n"+"userName:"+Parameters.user.userName+"\n"+logContent;
			FileOutputStream fos = new FileOutputStream(logFileName,true);
			
			fos.write(logContent.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
}
