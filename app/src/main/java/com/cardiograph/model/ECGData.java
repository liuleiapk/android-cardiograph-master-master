package com.cardiograph.model;

import android.content.Context;

public class ECGData{
	private Context context;
	private int id;
	private int type;
	private String path;
	private String fileName;
	private String dateTime;
	
	public ECGData(Context context, String path, String fileName) {
		super();
		this.context = context;
		this.path = path;
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
