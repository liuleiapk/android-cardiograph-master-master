package com.cardiograph.util;

import com.cardiograph.constance.Constance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBUtil
{
    /* 持有私有静态实例，防止被引用，此处赋值为null，目的是实现延迟加载 */ 
	private static DBUtil instance = null;
	private MySQLiteOpenHelper helper = null;
	private SQLiteDatabase sdb = null;
	public static Context context;
	/* 私有构造方法，防止被实例化 */
	private DBUtil()
	{
	}
	/* 静态工程方法，创建实例 */
	public static DBUtil getInstance()
	{
		 if (instance == null) {  
//			synchronized (instance) { 
				if(instance == null)
				{
					instance = new DBUtil();
				}
//			}
		 }
		return instance;
	}
	
	public SQLiteDatabase getSQLiteDatabase()
	{
		if(helper == null)
		{
			helper = new MySQLiteOpenHelper(context, Constance.DB_NAME, null, Constance.DB_VERSION);
		}
		if(sdb == null)
		{
			sdb = helper.getWritableDatabase();
		}
		return sdb;
	}
	
	public void closeDB()
	{
		if(sdb != null)
		{
			sdb.close();
			sdb = null;
		}
	}
	
	/* 如果该对象被用于序列化，可以保证对象在序列化前后保持一致 */  
	public Object readDBUtil() {  
	     return instance;  
 }  

}
