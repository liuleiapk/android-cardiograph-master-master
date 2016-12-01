package com.cardiograph.constance;

import android.content.Context;

public class Constance
{
	public static String DB_NAME = "cardiograph.db";
	public static int DB_VERSION = 1;
	public static Context context = null;
	public static final int REQUEST_SELECT_DEVICE = 1;
	public static final int REQUEST_ENABLE_BT = 2;
    public static final int DRAW_LST = 0;
    public static final int DRAW_LSTBUFFER = 1;
    public static final int MESSAGE_DATETIME_AUDIO = 0;
    public static final int MESSAGE_CALCULATE_BPM = 1;
	public static final String POST="POST";
	public static final String GET="GET";
	public static String URL_DOWNLOAD = "http://10.0.2.2:8080/PoliceServlet/DownloadServlet";
	public static String URL_UPLOAD = "http://172.16.3.32/m";
	public static String URL_LOGIN = "http://172.16.3.32/m";
	public static String URL_UPDATE = "http://172.16.3.32/m";
	public static final String RESULT_OK="OK";
	public static final String RESULT_AUTH_ERROR="AUTH_ERROR";
	public static final String RESULT_ERROR="ERROR";
	public static final String RESULT_ACTION_ERROR="ACTION_ERROR";
	public static final String RESULT_KEY_ERROR="KEY_ERROR";
	public static final String RESULT_KEY_EXPIRED="KEY_EXPIRED";
	public static final String RESULT_USER_ERROR="USER_ERROR";
	public static final String RESULT_FIELDS_ERROR="FIELDS_ERROR";
	public static final String RESULT_VERSION_ERROR="VERSION_ERROR";
	public static final String RESULT_UPDATE_FAILED="UPDATE_FAILED";
}
