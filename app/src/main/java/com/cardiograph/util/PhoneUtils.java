package com.cardiograph.util;

import com.cardiograph.commom.ApplicationExtension;
import com.cardiograph.log.Debugger;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * 电话相关处理(是否需要将PhoneNumberInputWatcher弄进来)
 * 
 * @author jack
 * 
 */
public class PhoneUtils {

	public static final String SCHEME_TEL = "tel:";
	private static final String CMCC_ISP = "46000";// 中国移动
	private static final String CMCC2_ISP = "46002"; // 中国移动
	private static final String CU_ISP = "46001"; // 中国联通
	private static final String CT_ISP = "46003"; // 中国电信

    private static TelephonyManager telephonyManager;

    static {
        telephonyManager = (TelephonyManager) ApplicationExtension
				.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
    }

	/**
	 * 网络类型
	 */
	public enum NetWorkType {
		NONE(""),
		WIFI("2"), 
		MOBILE("1");
		
		String type;
		
		NetWorkType(String type) {
			this.type = type;
		}
		
		@Override
		public String toString() {
			return type;
		}
	}

	/**
	 * 拨打电话
	 * 
	 * @param context
	 * @param phoneNumber
	 *            电话号码
	 */
	public static void callPhone(final Context context, final String phoneNumber) {
		try {
			Uri uri = Uri.parse(SCHEME_TEL + phoneNumber);
			Intent intent = new Intent(Intent.ACTION_DIAL, uri);
			context.startActivity(intent);
		} catch (Exception e) {
			new Debugger().log(e);
		}
	}

	/**
	 * 获取手机imei串号
	 * 
	 * @return
	 */
	public static String getIMEI() {
		TelephonyManager manager = (TelephonyManager) ApplicationExtension
				.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
		String IMEI = manager.getDeviceId();
		IMEI = Util.trim(IMEI);
		return IMEI;
	}

	/**
	 * 获取IMSI号
	 * 
	 * @return
	 */
	public static String getIMSI() {
		TelephonyManager manager = (TelephonyManager) ApplicationExtension
				.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = manager.getSubscriberId();
		if (imsi == null) {
			imsi = "eeeeeeeeeeeeeee";
		}
		return imsi;
	}

	/**
	 * 获取手机网络运营商类型
	 * 
	 * @return
	 */
	public static String getPhoneISP() {
		TelephonyManager manager = (TelephonyManager) ApplicationExtension
				.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
		String teleCompany = "";
		/*
		 * MCC+MNC(mobile country code + mobile network code) 注意：仅当用户已在网络注册时有效。
		 * 在CDMA网络中结果也许不可靠。
		 */
		String np = manager.getNetworkOperator();// String
		if (np != null) {
			if (np.startsWith(CMCC_ISP) || np.startsWith(CMCC2_ISP)) {// 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
				// 中国移动
				teleCompany = "y";
			} else if (np.startsWith(CU_ISP)) {
				// 中国联通
				teleCompany = "l";
			} else if (np.startsWith(CT_ISP)) {
				// 中国电信
				teleCompany = "d";
			}
		}
		teleCompany = Util.trim(teleCompany);
		return teleCompany;
	}

	/**
	 * 获取手机标识 eg:ME860
	 * 
	 * @return
	 */
	public static String getPhoneModel() {
		String deviceModel = "";
		// ME860
		deviceModel = Build.MODEL;
		deviceModel = Util.trim(deviceModel);
		return deviceModel;
	}

	/**
	 * 获取手机型号 eg:ME860_HKTW
	 * 
	 * @return
	 */
	public static String getPhoneType() {
		String phoneType = "";
		// eg:ME860_HKTW
		phoneType = Build.PRODUCT;
		phoneType = Util.trim(phoneType);
		return phoneType;
	}

	/**
	 * 获取手机厂商 eg:motorola
	 * 
	 * @return
	 */
	public static String getPhonePhoneManuFacturer() {
		String phoneManufactuer = "";
		// eg:motorola
		phoneManufactuer = Build.MANUFACTURER;
		phoneManufactuer = Util.trim(phoneManufactuer);
		return phoneManufactuer;
	}

	/**
	 * 网络是否可用
	 * 
	 * @return true可用 </br> false不可用
	 */
	public static boolean isNetworkAvailable() {
		ConnectivityManager connectivity = (ConnectivityManager) ApplicationExtension
				.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 返回当前手机接入的网络类型,
	 * 
	 * @return 返回值: 1.代表mobile(2G3G), 2代表wifi
	 */
	public static String getNetworkStat() {
		NetWorkType netType = NetWorkType.NONE;
		try {
			ConnectivityManager connMgr = (ConnectivityManager) ApplicationExtension
					.getInstance().getSystemService(
							Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
			if (activeInfo != null && activeInfo.isConnected()) {
				if (activeInfo.getType() == ConnectivityManager.TYPE_WIFI) {
					netType = NetWorkType.WIFI;
				} else if (activeInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
					netType = NetWorkType.MOBILE;
				}
			}
		} catch (Exception e) {
			new Debugger().log(e);
		}
		return netType.toString();
	}

	/**
	 * 检查gps是否可用
	 * 
	 * @return
	 */
	public static boolean isGpsAvaiable() {
		LocationManager locationManager = (LocationManager) ApplicationExtension
				.getInstance().getSystemService(Context.LOCATION_SERVICE);
		boolean isEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		return isEnabled;
	}

	/**
	 * 打开gps和关闭gps
	 * 
	 * @param context
	 */
	public static void autoGps(Context context) {
		try {
			Intent GPSIntent = new Intent();// 代码自动打开gps
			GPSIntent.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
			GPSIntent.setData(Uri.parse("custom:3"));
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (CanceledException e) {
			new Debugger().log(e);
		}
	}

	/**
	 * 获取手机系统版本
	 * 
	 * @return
	 */
	public static String getPhoneOSVersion() {
		String osVersion = "";
		osVersion = String.valueOf(Build.VERSION.SDK_INT);
		osVersion = Util.trim(osVersion);
		return osVersion;
	}

    /**
     * 获取本机手机号码
     * @return 返回本机号码，如果无法获取手机号，则返回""空串
     */
    public static String getPhoneNumber(){
        String phoneNumber = "";
        phoneNumber = telephonyManager.getLine1Number();
        phoneNumber = phoneNumber == null ? "" : phoneNumber;
        return phoneNumber;
    }


    /**
     * 获取sim卡号
     * @return 返回sim卡号，如果无法获取，返回""空串
     */
    public static String getSimSerialNumber(){
        String simSerialNumber = "";
        simSerialNumber = telephonyManager.getSimSerialNumber();
        simSerialNumber = simSerialNumber == null ? "" : simSerialNumber;
        return simSerialNumber;
    }
    
    /**
     * 获取sim卡号
     * @return 返回sim卡号，如果无法获取，返回""空串
     */
    public static String getSimOperatorName(){
        String simOperatorName = "";
        simOperatorName = telephonyManager.getSimOperatorName();
        simOperatorName = simOperatorName == null ? "" : simOperatorName;
        return simOperatorName;
    }
}
