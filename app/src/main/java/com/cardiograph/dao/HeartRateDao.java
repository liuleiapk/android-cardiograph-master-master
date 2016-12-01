package com.cardiograph.dao;

import java.util.ArrayList;
import java.util.List;

import com.cardiograph.model.HeartRate;
import com.cardiograph.model.User;
import com.cardiograph.util.DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class HeartRateDao
{
	private HeartRate heartRate;
	private Context context;
	public HeartRateDao(HeartRate heartRate,Context context)
	{
		this.heartRate = heartRate;
		this.context = context;
	}
	
	
	public boolean add()
	{
		boolean ba = false;
		DBUtil.context = context;		
		int uID = 0;
		SQLiteDatabase sdb = DBUtil.getInstance().getSQLiteDatabase();
		Cursor c = sdb.rawQuery("select ID from T_USER where USERNAME = '"+heartRate.getName()+"'", null);
		if(c != null && c.moveToFirst())
		{
			do{
				 uID = c.getInt(c.getColumnIndex("ID"));
			}while(c.moveToNext());
		}
		ContentValues  cv = new ContentValues();	
		cv.put("UID", uID);
		cv.put("DATE_TIME", heartRate.getDateTime());
		cv.put("HEARTRATE", heartRate.getRate());
		long pkID = sdb.insert("T_CARDIOGRAPH", null, cv);
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
        int iRows = sdb.delete("T_CARDIOGRAPH","ID="+heartRate.getId(), null);
//		Toast.makeText(context, "iRows=" + String.valueOf(iRows),Toast.LENGTH_SHORT).show();
		if (iRows > 0) {
			bd = true;
		}
		return bd;
	}
	
	public List<HeartRate> findHeartRates(){
		HeartRate hr = null;
		List<HeartRate> lsthistory = new ArrayList<HeartRate>();
		DBUtil.context = context;
		SQLiteDatabase sdb = DBUtil.getInstance().getSQLiteDatabase();
		StringBuffer sb = new StringBuffer();
		sb
			.append("select tc.[ID],tc.[DATE_TIME],tc.[HEARTRATE],tu.[USERNAME] from T_USER tu, T_CARDIOGRAPH tc " +
					"where tu.[ID]=tc.[UID]");
		Cursor c = sdb.rawQuery(sb.toString(), null);
		if(c != null && c.moveToFirst())
		{
			do {
				int id = c.getInt(c.getColumnIndex("ID"));
				String name = c.getString(c.getColumnIndex("USERNAME"));
				String dateTime = c.getString(c.getColumnIndex("DATE_TIME"));
				int heartRate = c.getInt(c.getColumnIndex("HEARTRATE"));
				hr = new HeartRate(context, id, name, dateTime, heartRate);	
				lsthistory.add(hr);
			} while (c.moveToNext());			
		}
		return lsthistory;
	}
	
	public List<User> findUsers(User us)
	{
		List<User> lstUser = null;
		return lstUser;
	}
}
