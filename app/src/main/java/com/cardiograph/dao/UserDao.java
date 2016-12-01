package com.cardiograph.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cardiograph.model.HeartRate;
import com.cardiograph.model.User;
import com.cardiograph.util.DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class UserDao
{
	private Context context;
	private User user;
	public UserDao(User user,Context context)
	{
		this.user = user;
		this.context = context;
	}
	
	public Map<String, Object> login()
	{
		//select count(1) total from User where USERNAME='?' and PASSWORD='?'
//		boolean bl = false;
		Map<String, Object> map = new HashMap<String, Object>();
		DBUtil.context = context;
		SQLiteDatabase sdb = DBUtil.getInstance().getSQLiteDatabase();
		StringBuffer sb = new StringBuffer();
		sb
			.append("select count(1) NUM, ID, USERNAME from T_USER where CODE='")
			.append(user.getCode())
			.append("' and PWD='")
			.append(user.getPwd())
			.append("'");
		
		Cursor c = sdb.rawQuery(sb.toString(), null);
		if(c != null && c.moveToFirst())
		{
			int id = c.getInt(c.getColumnIndex("ID"));
			String name = c.getString(c.getColumnIndex("USERNAME"));
			int num = c.getInt(c.getColumnIndex("NUM"));
			map.put("id", id);
			map.put("userName", name);
			map.put("num", num);
//			if(iTotal > 0)
//			{
//				bl = true;
//			}
		}
		return map;
	}
	
	public boolean add()
	{
		boolean ba = false;
		DBUtil.context = context;		
		int uID = 0;
		SQLiteDatabase sdb = DBUtil.getInstance().getSQLiteDatabase();
		ContentValues  cv = new ContentValues();	
		cv.put("CODE", user.getCode());
		cv.put("PWD", user.getPwd());
		cv.put("USERNAME", user.getName());
		long pkID = sdb.insert("T_USER", null, cv);
		Toast.makeText(context, "ID=" + String.valueOf(pkID),Toast.LENGTH_LONG).show();
		if(pkID != -1 )
		{			
            ba = true;
		}
		return ba;
	}
	
	public void update()
	{
		
	}
	
	public boolean delete()
	{
		boolean bd = false;
		DBUtil.context = context;
		SQLiteDatabase sdb = DBUtil.getInstance().getSQLiteDatabase();
        int iRows = sdb.delete("T_USER","ID="+user.getId(), null);
//		Toast.makeText(context, "iRows=" + String.valueOf(iRows),Toast.LENGTH_SHORT).show();
		if (iRows > 0) {
			bd = true;
		}
		return bd;
	}
	
	public User findUser(User us)
	{
		User user = null;
		return user;
	}
	
	public List<User> findUsers()
	{
		User user = null;
		List<User> lstUser = new ArrayList<User>();
		DBUtil.context = context;
		SQLiteDatabase sdb = DBUtil.getInstance().getSQLiteDatabase();
		StringBuffer sb = new StringBuffer();
		sb
			.append("select tu.[ID],tu.[USERNAME] from T_USER tu");
		Cursor c = sdb.rawQuery(sb.toString(), null);
		if(c != null && c.moveToFirst())
		{
			do {
				int id = c.getInt(c.getColumnIndex("ID"));
				String name = c.getString(c.getColumnIndex("USERNAME"));
				user = new User(context, id, name);	
				lstUser.add(user);
			} while (c.moveToNext());			
		}
		return lstUser;
	}
}
