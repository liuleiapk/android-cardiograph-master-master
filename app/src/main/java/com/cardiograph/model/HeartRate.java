package com.cardiograph.model;

import java.util.List;

import com.cardiograph.dao.HeartRateDao;
import com.cardiograph.dao.UserDao;

import android.content.Context;

public class HeartRate{
	private Context context;
	private int id;
	private String name;
	private String dateTime;
	private int rate;
	
	public HeartRate(Context context) {
		super();
		this.context = context;
	}
	
	public HeartRate(Context context, int id) {
		super();
		this.context = context;
		this.id = id;
	}


	public HeartRate(Context context, String name, String dateTime, int rate) {
		super();
		this.context = context;
		this.name = name;
		this.dateTime = dateTime;
		this.rate = rate;
	}
	
	public HeartRate(Context context, int id, String name, String dateTime,
			int rate) {
		super();
		this.context = context;
		this.id = id;
		this.name = name;
		this.dateTime = dateTime;
		this.rate = rate;
	}

	public boolean add()
	{
		HeartRateDao hrd = new HeartRateDao(this,context);
		return hrd.add();
	}
	
	public void update()
	{
		
	}
	
	public boolean delete()
	{
		HeartRateDao hrd = new HeartRateDao(this,context);
		return hrd.delete();
	}
	
	public HeartRate findHeartRate()
	{
		HeartRate hr = null;
		return hr;
	}
	
	public List<HeartRate> findHeartRates()
	{
		HeartRateDao hrd = new HeartRateDao(this,context);
		return hrd.findHeartRates();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	
}
