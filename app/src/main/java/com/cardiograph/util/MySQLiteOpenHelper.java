package com.cardiograph.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.example.cardiograph.R;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper
{
	private Context context;
	
	/**
	 * 构造函数：
	 * 函数功能:
	 * 参数说明：
	 * 		@param context	上下文环境
	 * 		@param name		数据库名称
	 * 		@param factory	cursor工厂
	 * 		@param version	数据库的版本号
	 */
	public MySQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version)
	{
		super(context, name, factory, version);
		this.context = context;
	}

	/**
	 *  函数名称 ：onCreate
	 *  功能描述 ：是在数据库还没有被创建的时候，才会被调用  
	 *  参数说明 ：
	 *  	@param db
	 *  返回值：
	 *  	
	 *  修改记录：
	 *  日期 ：2013-10-25 下午7:00:10	修改人：gy
	 *  描述 ：
	 * 					
	 */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		System.out.println("调用了 onCreate 方法");
		//做数据库的初始化的操作
		Resources res = context.getResources();
		BufferedReader br = null;
		br = new BufferedReader(
				new InputStreamReader(res.openRawResource(R.raw.cardiograph)));
		String strSql = null;
		try
		{
			while((strSql = br.readLine()) != null)
			{
				db.execSQL(strSql);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 *  函数名称 ：onUpgrade
	 *  功能描述 ： 数据库已经被创建过了，并且数据库的版本和原来的数据库版本不一致时，才调用这个方法 
	 *  参数说明 ：
	 *  	@param db
	 *  	@param oldVersion
	 *  	@param newVersion
	 *  返回值：
	 *  	
	 *  修改记录：
	 *  日期 ：2013-10-25 下午7:00:10	修改人：gy
	 *  描述 ：
	 * 					
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		System.out.println("调用了 onUpgrade 方法");
		System.out.println("调用了 onCreate 方法");
		//做数据库的初始化的操作
		Resources res = context.getResources();
		BufferedReader br = null;
		br = new BufferedReader(
				new InputStreamReader(res.openRawResource(R.raw.cardiograph)));
		String strSql = null;
		try
		{
			while((strSql = br.readLine()) != null)
			{
				db.execSQL(strSql);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
