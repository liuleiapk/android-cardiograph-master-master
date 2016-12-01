package com.cardiograph.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cardiograph.commom.ApplicationExtension;
import com.cardiograph.constance.Parameters;
import com.example.cardiograph.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButtonsController;

/**
 * 通用工具类
 * 
 * <br>
 * 1.日期格式化</br> 2.文字ｔｏａｓｔ提示，隐藏显示键盘</br> 3.金额，字符格式化，数值计算，字体大小设置</br>
 * 4.单位格转换-----文字单位，金额单位</br> 5.校验字符合法性</br> 6.软件及相关信息</br> 7.设备硬件信息及网络信息</br>
 * 
 * @author xyz
 */
public class Util {
	private static Toast toast = null;
//	public static String filename = "filegouwuche";
//
//	/** 最后 点击时间 */
//	private static long lastClickTime;
//	private static ArrayList<View> views = new ArrayList<View>();
//
//	private static SimpleDateFormat dateForamt = new SimpleDateFormat();
//
//	/**
//	 * 是否重复点击 ，不区分前后是否同一个view。可以适当调整timeD间隔时间
//	 * 
//	 * @return
//	 */
//	public static boolean isFastDoubleClick() {
//		long time = System.currentTimeMillis();
//		long timeD = time - lastClickTime;
//		if (0 < timeD && timeD < 800) {
//			return true;
//		}
//		lastClickTime = time;
//		return false;
//	}
//
//	/**
//	 * 是否重复点击
//	 * 
//	 * @view 被点击view，如果前后是同一个view，则进行双击校验
//	 * @return
//	 */
//	public static boolean isFastDoubleClick(View view) {
//		long time = System.currentTimeMillis();
//		long timeD = time - lastClickTime;
//		if (views.size() == 0) {
//			views.add(view);
//		}
//		if (0 < timeD && timeD < 800 && views.get(0).getId() == view.getId()) {
//			return true;
//		}
//		lastClickTime = time;
//		views.clear();
//		views.add(view);
//		return false;
//	}
//
//	/******************************************* 日期格式化------开始 ********************************************/
//
//	/**
//	 * 返回日期字符串，格式为：yyyy-MM-dd HH:mm:ss
//	 * 
//	 * @deprecated 使用DateUtil类中的方法
//	 * @return 格式化后的日期字符串
//	 */
//	public static String date() {
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String date = formatter.format(new Date());
//		return date;
//	}

	/**
	 * 返回日期字符串，格式为：yyyy_MM_dd_hh_mm_ss
	 * 
	 * @deprecated 使用DateUtil类中的方法
	 * @return 格式化后的日期字符串
	 */
	public static String date2() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_MM_ss");
		String date = formatter.format(new Date());
		return date;
	}

//	/**
//	 * 返回日期字符串，格式为：yyyy-MM-dd HH:mm:ss
//	 * 
//	 * @deprecated 使用DateUtil类中的方法
//	 * @return 格式化后的日期字符串
//	 */
//	public static String date(Date d) {
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String date = formatter.format(d);
//		return date;
//	}
//
//	/**
//	 * 返回日期字符串，格式为：yyyy-MM-dd HH:mm:ss
//	 * 
//	 * @deprecated 使用DateUtil类中的方法
//	 * @return 格式化后的日期字符串
//	 */
//	public static String date2(Date d) {
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
//		String date = formatter.format(d);
//		return date;
//	}
//
//	/**
//	 * 返回日期字符串，格式为：yyyy年MM月dd HH:mm
//	 * 
//	 * @deprecated 使用DateUtil类中的方法
//	 * @param dateString
//	 *            毫秒字符串（纯数字字符串）
//	 * @return 格式化后的日期字符串
//	 */
//	public static String date(String dateString) {
//		long timeString = Long.parseLong(dateString);
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
//				"yyyy年MM月dd日 HH:mm:ss");
//		Calendar c = Calendar.getInstance();
//		c.setTimeInMillis(timeString);
//		String date = simpleDateFormat.format(c.getTime());
//		return date;
//	}
//
//	/**
//	 * 返回日期字符串，格式为：MMdd
//	 * 
//	 * @deprecated 使用DateUtil类中的方法
//	 * @param dateString
//	 *            毫秒字符串（纯数字字符串）
//	 * @return 格式化后的日期字符串
//	 */
//	public static String formatSignDate(String dateString) {
//		long timeString = Long.parseLong(dateString);
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMdd");
//		Calendar c = Calendar.getInstance();
//		c.setTimeInMillis(timeString);
//		String date = simpleDateFormat.format(c.getTime());
//		return date;
//	}
//
//	/**
//	 * 返回日期字符串，格式为：yyyy年MM月dd HH:mm
//	 * 
//	 * @deprecated 使用DateUtil类中的方法
//	 * @param dateString
//	 *            毫秒字符串（纯数字字符串）
//	 * @param pattern
//	 *            格式化格式 如：yyyy-MM-dd HH:mm ，yyyy MM dd HH:MM ,yyyy-MM-dd
//	 * @return 格式化后的日期字符串
//	 */
//	public static String date(String dateString, String pattern) {
//		long timeString = Long.parseLong(dateString);
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//		Calendar c = Calendar.getInstance();
//		c.setTimeInMillis(timeString);
//		String date = simpleDateFormat.format(c.getTime());
//		return date;
//	}
//
//	/**
//	 * 将cst格式化日期转换为毫秒格式时间
//	 * 
//	 * @param CSTDateString
//	 *            cst格式的时间 例如：Tue May 21 14:32:31 CST 2013
//	 * @return
//	 */
//	public static String dateString(String CSTDateString) {
//		if (!CSTDateString.contains("CST")) {
//			return CSTDateString;
//		}
//		CSTDateString = CSTDateString.replace("Jan", "01");
//		CSTDateString = CSTDateString.replace("Feb", "02");
//		CSTDateString = CSTDateString.replace("Mar", "03");
//		CSTDateString = CSTDateString.replace("Apr", "04");
//		CSTDateString = CSTDateString.replace("May", "05");
//		CSTDateString = CSTDateString.replace("Jun", "06");
//		CSTDateString = CSTDateString.replace("Jul", "07");
//		CSTDateString = CSTDateString.replace("Aug", "08");
//		CSTDateString = CSTDateString.replace("Sep", "09");
//		CSTDateString = CSTDateString.replace("Oct", "10");
//		CSTDateString = CSTDateString.replace("Nov", "11");
//		CSTDateString = CSTDateString.replace("Dec", "12");
//		CSTDateString = CSTDateString.replace("Mon", "");
//		CSTDateString = CSTDateString.replace("Tues", "");
//		CSTDateString = CSTDateString.replace("Wed", "");
//		CSTDateString = CSTDateString.replace("Thur", "");
//		CSTDateString = CSTDateString.replace("Fri", "");
//		CSTDateString = CSTDateString.replace("Sat", "");
//		CSTDateString = CSTDateString.replace("Sun", "");
//
//		SimpleDateFormat sdf = new SimpleDateFormat(
//				"EE MM dd HH:mm:ss 'CST' yyyy", Locale.US);
//		Date date = null;
//		String dateString2 = "";
//		try {
//			date = sdf.parse(CSTDateString);
//			dateString2 = date.getTime() + "";
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		return dateString2;
//	}
//
//	/**
//	 * 返回日期字符串，格式为：yyyy-MM-dd HH:mm
//	 * 
//	 * @deprecated 使用DateUtil类中的方法
//	 * @param dateString
//	 *            毫秒字符串（纯数字字符串）
//	 * @return 格式化后的日期字符串
//	 */
//	public static String date4(String dateString) {
//		long timeString = Long.parseLong(dateString);
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
//				"yyyy-MM-dd  HH:mm");
//		Calendar c = Calendar.getInstance();
//		c.setTimeInMillis(timeString);
//		String date = simpleDateFormat.format(c.getTime());
//		return date;
//	}
//
//	/**
//	 * 返回日期字符串，格式为：yyyy年MM月dd日
//	 * 
//	 * @deprecated 使用DateUtil类中的方法
//	 * @param dateString
//	 *            毫秒字符串（纯数字字符串）
//	 * @return 格式化后的日期字符串
//	 */
//	public static String date3(String dateString) {
//		long timeString = Long.parseLong(dateString);
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
//		Calendar c = Calendar.getInstance();
//		c.setTimeInMillis(timeString);
//		String date = simpleDateFormat.format(c.getTime());
//		return date;
//	}
//
//	/**
//	 * 返回分钟字符串，格式为：06,00
//	 * 
//	 * @param time
//	 *            分钟字符串（纯数字字符串）
//	 * @return 格式化后的时间字符串
//	 */
//	public static String timeFormat(int time) {
//		String pattern = "00";
//		DecimalFormat df = new DecimalFormat(pattern);
//		return df.format(time);
//	}
//
//	/**
//	 * 返回日期字符串，格式为20120130
//	 * 
//	 * @param date
//	 *            日期
//	 * @return 格式化后的日期字符串
//	 */
//	public static String dateFormatNum(String date) {
//		String dateString = "";
//		String[] temp = date.split("-");
//		dateString = temp[0] + dateNum(temp[1]) + dateNum(temp[2]);
//		return dateString;
//	}
//
//	/**
//	 * 判断日期或月份是否为个位，如果是个位补零。
//	 * 
//	 * @param num
//	 *            日期或月份值
//	 * @return 格式化后的日期字符串
//	 * */
//	private static String dateNum(String num) {
//		String date = "";
//		if (num.length() == 1) {
//			date = "0" + num;
//		} else {
//			date = num;
//		}
//		return date;
//	}
//
//	/**
//	 * 返回日期字符串，格式为01/30
//	 * 
//	 * @param date
//	 *            日期
//	 * @return 格式化后的日期字符串
//	 */
//	public static String dateFormatMonthDay(String time) {
//		String dateString = "";
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			Date date = sdf.parse(time);
//			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
//
//			String strDate = sdf.format(sqlDate);
//			String[] dateshuzu = strDate.split("-");
//			dateString = dateshuzu[1] + "/" + dateshuzu[2];
//		} catch (Exception e) {
//			new Debugger().log(e);
//		}
//		return dateString;
//	}
//
//	/**
//	 * 返回时间格式 hostDate:20130515 hostTime:11:33:00
//	 * 
//	 * @param hostDate
//	 * @param hostTime
//	 * @return 格式化时间格式 yyyy-MM-dd HH:mm:ss
//	 */
//	public static String formatData(String hostDate, String hostTime) {
//		if (hostDate == null || hostTime == null) {
//			return "";
//		}
//		try {
//			int year = Integer.parseInt(hostDate.substring(0, 4));
//			int month = Integer.parseInt(hostDate.substring(4, 6));
//			int day = Integer.parseInt(hostDate.substring(6));
//			int hourOfDay = Integer.parseInt(hostTime.substring(0, 2));
//			int minute = Integer.parseInt(hostTime.substring(2, 4));
//			int second = Integer.parseInt(hostTime.substring(4));
//			Calendar calendar = Calendar.getInstance();
//			calendar.set(year, month - 1, day, hourOfDay, minute, second);
//			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
//					"yyyy-MM-dd  HH:mm:ss");
//			return simpleDateFormat.format(calendar.getTime());
//		} catch (NumberFormatException e) {
//			new Debugger().log(e);
//		}
//		return "";
//	}
//
//	/**
//	 * 按照指定格式来格式系统时间
//	 * 
//	 * @deprecated 使用DateUtil类中的方法
//	 * @param pattern
//	 *            格式(yyMMdd HH:mm:ss ....)
//	 * @return
//	 */
//	public static String formateDate(String pattern) {
//		dateForamt.applyPattern(pattern);
//		String date = dateForamt.format(new Date());
//		return date;
//	}
//
//	/**
//	 * 钱包请求使用的时间格式
//	 * 
//	 * @return
//	 */
//	public static String dateForWallet() {
//		String date = formateDate("yyMMddHHmmss");
//		return date;
//	}
//
//	/**
//	 * 时间字符串从一种格式转换成另一种格式
//	 * 
//	 * @deprecated 使用DateUtil类中的方法
//	 * @param date
//	 *            20130516
//	 * @param pattern
//	 *            yyyyMMdd
//	 * @param targetPattern
//	 *            yyyy-MM-dd (2013-05-16)
//	 * @return
//	 */
//	public static String formatDateStrToPattern(String date, String pattern,
//			String targetPattern) {
//		dateForamt.applyPattern(pattern);
//		Date d;
//		try {
//			d = dateForamt.parse(date);
//			dateForamt.applyPattern(targetPattern);
//			return dateForamt.format(d);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		return date;
//	}
//
//	/****************************************** 日期格式化---结束 ***************************************/
//
//	/********************** 文字ｔｏａｓｔ提示，隐藏显示键盘 ------开始 ******************************/
//
	/**
	 * Toast信息提示
	 * 
	 * @param context
	 *            上下文
	 * @param strId
	 *            字符Id
	 */
	public static void toast(Context context, int strId) {
//		Context context = ApplicationExtension.getInstance()
//				.getApplicationContext();
		getToast(context, context.getString(strId), Toast.LENGTH_LONG).show();

	}

	/**
	 * Toast信息提示 居中
	 * 
	 * @param context
	 *            上下文
	 * @param strId
	 *            字符Id
	 */
	public static void toastCenter(Context context, int strId) {
//		Context context = ApplicationExtension.getInstance()
//				.getApplicationContext();
		Toast toast = getToast(context, context.getString(strId), Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static Toast getToast(Context context, String text, int showTime) {
//		Context context = ApplicationExtension.getInstance()
//				.getApplicationContext();
		// toast = Toast.makeText(context, text, showTime);
		if (toast == null) {
			toast = Toast.makeText(context, text, showTime);
		} else {
			toast.setDuration(showTime);
			toast.setText(text);
		}

		return toast;
	}

	/**
	 * Toast信息提示
	 * 
	 * @param context
	 *            上下文
	 * @param strId
	 *            字符Id
	 */
	public static void toast(Context context, int strId, boolean isShowShortTime) {
//		Context context = ApplicationExtension.getInstance()
//				.getApplicationContext();
		int showTime = 0;
		if (isShowShortTime) {
			showTime = Toast.LENGTH_SHORT;
		} else {
			showTime = Toast.LENGTH_LONG;
		}
		getToast(context, context.getString(strId), showTime).show();
	}

	/**
	 * Toast信息提示
	 * 
	 * @param str
	 *            提示信息
	 */
	public static void toast(Context context, String str) {
		if (str != null && !"无效的Token".equals(str)) {
			getToast(context, str, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Toast信息提示 居中
	 * 
	 * @param str
	 *            提示信息
	 */
	public static void toastCenter(Context context, String str) {
		if (str != null && !"无效的Token".equals(str)) {
			Toast toast = getToast(context, str, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	/**
	 * Toast信息提示
	 * 
	 * @param str
	 *            提示信息
	 * @param duration
	 *            信息延迟时间
	 */
	public static void toast(Context context, String str, int duration) {
		if (str != null) {
			getToast(context, str, duration).show();
		}
	}

	/**
	 * log日志打印
	 * 
	 * @param tag
	 * @param msg
	 *            打印信息
	 */
	public static void log(String tag, String msg) {
		if (Parameters.debug)
			Log.d(tag, msg);
	}
//
//	/**
//	 * log日志打印,统一使用相同的tag
//	 * 
//	 * @param msg
//	 *            打印信息
//	 */
//	public static void log(String msg) {
//		if (Parameters.debug)
//			Log.d(Parameters.TAG, msg);
//	}
//
//	/**
//	 * 隐藏键盘 点击键盘右下角按钮时隐藏键盘
//	 * 
//	 * */
//	public static void hideKeyboard(View view) {
//		InputMethodManager imm = (InputMethodManager) ApplicationExtension
//				.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.hideSoftInputFromWindow(view.getWindowToken(),
//				InputMethodManager.HIDE_NOT_ALWAYS);
//	}
//
//	/**
//	 * 显示键盘 点击键盘右下角按钮时隐藏键盘
//	 * 
//	 * */
//	public static void showKeyboard(View view) {
//		InputMethodManager imm = (InputMethodManager) ApplicationExtension
//				.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
//	}
//
//	/******************************** 文字ｔｏａｓｔ提示，，隐藏显示键盘-----结束 ******************************************/
//
//	// /**
//	// * 使icon点击时，出现闪烁效果
//	// *
//	// * @param view
//	// * 被点击的view
//	// * @param event
//	// * 事件
//	// */
//	// public static void makeIconBlink(View view, MotionEvent event) {
//	// if (view == null || event == null) {
//	// return;
//	// }
//	// if (event.getAction() == MotionEvent.ACTION_DOWN) {
//	// view.getBackground().setAlpha(100);
//	// view.invalidate();
//	// } else if (event.getAction() == MotionEvent.ACTION_UP
//	// || event.getAction() == MotionEvent.ACTION_MOVE
//	// || event.getAction() == MotionEvent.ACTION_CANCEL) {
//	// view.getBackground().setAlpha(255);
//	// view.invalidate();
//	// }
//	// }
//
//	/****************************************** 金额，字符格式化，数值计算，字体大小设置----开始 ***************************************/
//
//	/**
//	 * 金额格式化
//	 * 
//	 * @param s
//	 *            金额
//	 * @return 格式后的金额(###,###.##)
//	 */
//	public static String formatAmount(String s) {
//		return formatAmount(s, false);
//	}
//
//	public static String formatBigDecimalAmount(String s) {
//		String amount = "";
//		try {
//			BigDecimal bgAmount = new BigDecimal(s).setScale(2,
//					BigDecimal.ROUND_HALF_UP);
//			amount = bgAmount.toString();
//
//		} catch (Exception e) {
//			e.fillInStackTrace();
//		}
//		return amount;
//
//	}
//
//	/**
//	 * 金额格式化
//	 * 
//	 * @param s
//	 *            金额
//	 * @param isInitNull
//	 *            是否初始化为空字符
//	 * @return 格式后的金额(###,###.##)
//	 */
//	public static String formatAmount(String s, boolean isInitNull) {
//		String result = "";
//		if (Util.isEmpty(s))
//			return "";
//		try {
//			String temp = s;
//			s = formatString(s);// 去除string可能的分隔符
//			double num = 0.0;
//			try {
//				num = Double.parseDouble(s);
//			} catch (NumberFormatException e) {
//				new Debugger().log(e);
//			}
//			if (num == 0) {
//				if (isInitNull) {
//					return "";
//				} else {
//					return "0.00";
//				}
//			}
//			if (num < 1) {// 小于1情况特殊处理
//				if (s.length() == 4) {// 0.05
//					return temp;
//				} else if (s.length() == 3) {// 0.5
//					return temp + "0";
//				}
//			}
//			NumberFormat formater = new DecimalFormat("#,###.00");
//			result = formater.format(num);
//		} catch (Exception e) {
//			new Debugger().log(e);
//		}
//		if (result.startsWith(".")) {
//			result = "0" + result;
//		}
//		return result;
//	}
//
//	/**
//	 * 金额直接转换为double类型
//	 * 
//	 * @param amount
//	 *            金额是格式化后的,比如"123,456.00"
//	 * @return
//	 */
//	public static double parseStringToDouble(String amount) {
//		if (isEmpty(amount)) {
//			return Double.parseDouble("0.00");
//		}
//		StringBuilder stringBuilder = new StringBuilder();
//		char[] charArray = amount.toCharArray();
//
//		for (int i = 0; i < charArray.length; i++) {
//			if (',' != charArray[i]) {
//				stringBuilder.append(charArray[i]);
//			}
//		}
//		double result = Double.parseDouble(stringBuilder.toString());
//
//		return result;
//	}
//
	/**
	 * 去除字符中间的 "空格/-/," 等间隔符
	 * 
	 * @param string
	 *            要格式化的字符
	 * @return 格式化后的字符
	 */
	public static String formatString(String string) {
		if (string == null)
			return "";
		String newString = string.replaceAll(" ", "").replaceAll("-", "")
				.replaceAll(",", "");
		return newString;
	}
//
//	/**
//	 * 字符串后端加全角空格，对齐成5个汉字
//	 * 
//	 * @param string
//	 * @return
//	 */
//	public static String suffixSpaceToString(String string) {
//		return suffixSpaceToString(string, 5);
//	}
//
//	/**
//	 * 字符串后端加全角空格，对齐成指定数量个汉字
//	 * 
//	 * @param string
//	 * @param len
//	 *            指定对齐位数
//	 * @return
//	 */
//	public static String suffixSpaceToString(String string, int len) {
//		StringBuilder stringBuilder = new StringBuilder();
//		int length = string.length();
//		int appendCount = length < len ? len - length : 0;
//
//		for (int i = 0; i < appendCount; i++) {
//			stringBuilder.append("　");
//		}
//		return string + stringBuilder.toString();
//	}
//
	/**
	 * 字符串前端加全角空格，对齐成5个汉字
	 * 
	 * @param string
	 * @return
	 */
	public static String addSpaceToStringFront(String string) {
		return addSpaceToStringFront(string, 5);
	}

	/**
	 * 字符串前端加全角空格，对齐成指定数量个汉字
	 * 
	 * @param string
	 * @param len
	 *            指定对齐位数
	 * @return
	 */
	public static String addSpaceToStringFront(String string, int len) {
		StringBuilder stringBuilder = new StringBuilder();
		int length = string.length();
		int appendCount = length < len ? len - length : 0;

		for (int i = 0; i < appendCount; i++) {
			stringBuilder.append("　");
		}
		return stringBuilder.toString() + string;
	}

//	/**
//	 * 字符串后端加全角空格，对齐成指定数量个汉字
//	 * 
//	 * @param string
//	 * @param len
//	 *            指定对齐位数
//	 * @return
//	 */
//	public static String addSpaceToStringBack(String string, int len) {
//		StringBuilder stringBuilder = new StringBuilder();
//		int length = string.length();
//		int appendCount = length < len ? len - length : 0;
//
//		for (int i = 0; i < appendCount; i++) {
//			stringBuilder.append("　");
//		}
//		return string + stringBuilder.toString();
//	}
//	/**
//	 * 格式化卡号，四个一组，中间以空格隔开
//	 * 
//	 * @param cardNumber
//	 *            卡号，
//	 * @return 格式化之后的卡号
//	 */
//	public static String formatCardNumberWithSpace(String cardNumber) {
//
//		// if (!checkCardNumber(cardNumber)) {
//		// return cardNumber;
//		// }
//		cardNumber = cardNumber.replaceAll(" ", "");
//		char[] chars = cardNumber.toCharArray();
//		StringBuilder builder = new StringBuilder();
//		for (int i = 0; i < chars.length; i++) {
//			if (i != 0 && i % 4 == 0) {
//				builder.append(" ");
//			}
//			builder.append(chars[i]);
//		}
//
//		return builder.toString();
//	}
//
//	/**
//	 * 格式化卡号，前6后4，中间根据位数以星号显示
//	 */
//	public static String formatCardNumberWithStar(String cardNumber) {
//
//		return formatCardNumberWithStar(cardNumber, "*");
//	}
//
//	public static String formatCompanyAccount(String cardNumber) {
//
//		int length = cardNumber.length();
//		StringBuilder builder = new StringBuilder();
//		builder.append(cardNumber.substring(0, 1));
//		for (int i = 0; i < (length - 1); i++) {
//			builder.append("*");
//		}
//		builder.append(cardNumber.substring(length - 1, length));
//
//		return builder.toString();
//	}
//
//	/**
//	 * 格式化银行的格式 例子：邮政储蓄(023534) 返回邮政储蓄
//	 * 
//	 * @param bankWithBankCode
//	 * @return
//	 */
//	public static String formatBankWithBankCode(String bankWithBankCode) {
//		if (bankWithBankCode.indexOf("(") == -1) {
//			return bankWithBankCode;
//		}
//
//		return bankWithBankCode.substring(0, bankWithBankCode.indexOf("("));
//	}
//
//	/**
//	 * 格式化卡号，前6后4，中间根据位数以星号显示
//	 */
//	@Deprecated
//	public static String formatPersonAccount(String cardNumber) {
//		if (!checkCardNumber(cardNumber)) {
//			return cardNumber;
//		}
//		int length = cardNumber.length();
//		StringBuilder builder = new StringBuilder();
//		builder.append(cardNumber.substring(0, 1));
//		for (int i = 0; i < (length - 1); i++) {
//			builder.append("*");
//		}
//		builder.append(cardNumber.substring(length - 1, length));
//
//		return builder.toString();
//	}
//
//	/**
//	 * 前3个数字后4个数字
//	 * 
//	 * @param phoneNo
//	 * @return
//	 */
//	public static String formatPhoneStart3End4(String phoneNo) {
//		if (null == phoneNo)
//			return "";
//
//		int length = phoneNo.length();
//		StringBuilder builder = new StringBuilder();
//		builder.append(phoneNo.substring(0, 3));
//		for (int i = 0; i < (length - 7); i++) {
//			builder.append("*");
//		}
//		builder.append(phoneNo.substring(length - 4, length));
//		return builder.toString();
//	}
//
//	/**
//	 * 
//	 * @param phoneNo
//	 * @return
//	 */
//	public static String formatPhoneNo(String phoneNo) {
//
//		if (null == phoneNo) {
//			return "";
//		}
//		int length = phoneNo.length();
//		StringBuilder builder = new StringBuilder();
//		builder.append(phoneNo.substring(0, 4));
//		for (int i = 0; i < (length - 3); i++) {
//			builder.append("*");
//		}
//		builder.append(phoneNo.substring(length - 3, length));
//		return builder.toString();
//	}
//
//	/**
//	 * 格式化卡号，前6后4，中间根据位数以星号显示
//	 * 
//	 * @param cardNumber
//	 *            卡号
//	 * @param interval
//	 *            分隔符
//	 */
//	public static String formatCardNumberWithStar(String cardNumber,
//			String interval) {
//		if (!checkCardNumber(cardNumber)) {
//			return cardNumber;
//		}
//		int length = cardNumber.length();
//		StringBuilder builder = new StringBuilder();
//		builder.append(cardNumber.substring(0, 6));
//		for (int i = 0; i < (length - 10); i++) {
//			builder.append(interval);
//		}
//		builder.append(cardNumber.substring(length - 4, length));
//
//		return builder.toString();
//	}
//
//	/**
//	 * 格式化卡号，前6后4，中间根据4位数分隔符显示
//	 * 
//	 * @param cardNumber
//	 *            卡号
//	 * @param interval
//	 *            分隔符
//	 */
//	public static String formatCardNumberWith4Star(String cardNumber,
//			String interval) {
//		if (!checkCardNumber(cardNumber)) {
//			return cardNumber;
//		}
//		int length = cardNumber.length();
//		StringBuilder builder = new StringBuilder();
//		builder.append(cardNumber.substring(0, 6));
//		for (int i = 0; i < 4; i++) {
//			builder.append(interval);
//		}
//		builder.append(cardNumber.substring(length - 4, length));
//
//		return builder.toString();
//	}
//
//	/**
//	 * 半角转换为全角
//	 * 
//	 * @param input
//	 * @return
//	 */
//	public static String ToDBC(String input) {
//		char[] c = input.toCharArray();
//		for (int i = 0; i < c.length; i++) {
//			if (c[i] == 12288) {
//				c[i] = (char) 32;
//				continue;
//			}
//			if (c[i] > 65280 && c[i] < 65375)
//				c[i] = (char) (c[i] - 65248);
//		}
//		return new String(c);
//	}
//
//	/**
//	 * 含半角数字字符串转换为含全角数字字符串
//	 * 
//	 * @param input
//	 * @return
//	 */
//	public static String ToQuanJiaoString(String input) {
//		input = input.replaceAll("0", "０").replaceAll("1", "１")
//				.replaceAll("2", "２").replaceAll("3", "３").replaceAll("4", "４")
//				.replaceAll("5", "５").replaceAll("6", "６").replaceAll("7", "７")
//				.replaceAll("8", "８").replaceAll("9", "９");
//		return input;
//	}

	/**
	 * 去掉字符串首尾的空格，如果字符串是null，则返回""
	 * 
	 * @param s
	 * @return
	 */
	public static String trim(String s) {
		if (s == null || s.equals("null"))
			return "";
		else
			return s.trim();
	}

//	/**
//	 * 将分转换为元,带两个小数点
//	 * 
//	 * @param fenStr
//	 * @return
//	 */
//	public static String fen2Yuan(String fenStr) {
//
//		if (fenStr == null)
//			return "0.00";
//
//		// 过滤非数字的情况，服务端可能返回“null”字符串，此时返回“0”
//		boolean isAllDigit = Pattern.matches("[\\d.]+", fenStr);
//		if (!isAllDigit) {
//			return "0.00";
//		}
//		String yuanString = new BigDecimal(fenStr)
//				.divide(new BigDecimal("100"))
//				.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
//		return yuanString;
//	}
//
//	/**
//	 * 将金额由元转换为分
//	 * 
//	 * @param yuanStr
//	 * @return
//	 */
//	public static String yuan2Fen(String yuanStr) {
//		if (isEmpty(yuanStr)) {
//			return "0";
//		}
//		yuanStr = formatString(yuanStr);
//		String fenString = new BigDecimal(yuanStr)
//				.multiply(new BigDecimal("100")).setScale(0).toString();
//		return fenString;
//	}
//
//	/**
//	 * 比较一个数是否大于另外一个数
//	 * 
//	 * @param oneNumber
//	 *            数字
//	 * @param anotherNumber
//	 *            另一个数字
//	 * @return
//	 */
//	public static boolean max(String oneNumber, String anotherNumber) {
//		if (null == oneNumber || "".equals(oneNumber)) {
//			oneNumber = "0";
//		}
//		if (null == anotherNumber || "".equals(anotherNumber)) {
//			anotherNumber = "0";
//		}
//		int count01 = new BigDecimal(oneNumber).compareTo(new BigDecimal(
//				anotherNumber));
//		if (count01 == 1) {// 大于
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	/**
//	 * 省略用户资料身份证7-14位，用*号代替
//	 */
//	public static String formatCretifiNum(String cretifiNum) {
//		if (null == cretifiNum) {
//			return null;
//		}
//		StringBuilder sbBuilder = new StringBuilder(cretifiNum);
//		// sbBuilder.replace(6, 14, "********");//替换位区间左闭右开
//		for (int i = 1; i <= sbBuilder.length(); i++) {
//			if (i >= 7 && i <= 14) {
//				sbBuilder.replace(i - 1, i, "*");
//			}
//		}
//		return sbBuilder.toString();
//	}
//
//	/**
//	 * 省略用户资料身份证中间用八个*号代替
//	 */
//	public static String formatIDCardNum(String cretifiNum) {
//		if (null == cretifiNum) {
//			return null;
//		}
//		StringBuilder sbBuilder = new StringBuilder(cretifiNum);
//		if (sbBuilder.length() >= 3) {
//			sbBuilder.replace(1, sbBuilder.length() - 1, "********");
//		} else {
//			sbBuilder.replace(1, sbBuilder.length(), "********");
//		}
//		return sbBuilder.toString();
//	}
//
//	/**
//	 * 省略用户姓名名用*号代替
//	 */
//	public static String formatUserName(String username) {
//		if (null == username || username.equals("")) {
//			return null;
//		}
//		StringBuilder sbBuilder = new StringBuilder(username);
//		sbBuilder.replace(1, sbBuilder.length(), "*");
//		return sbBuilder.toString();
//	}
//
//	/**
//	 * 电子邮箱隐去@之前的部分内容，除首字母用星号代替
//	 * 
//	 * @param email
//	 * @return
//	 */
//	public static String formatEmailN1(String email) {
//		if (null == email) {
//			return null;
//		}
//		int index = email.indexOf("@");
//		StringBuilder sb = new StringBuilder(email);
//		if (index > 1) {
//			int len = email.length();
//
//			String str = "";
//			for (int i = 0; i < index - 1; i++) {
//				str += "*";
//			}
//			sb.replace(1, index, str);
//
//			// int count = len-4-(len-1-index);
//			// String str = "";
//			// for (int i = 0; i < count; i++) {
//			// str += "*";
//			// }
//			// sb.replace(1, index, str);
//		}
//		return sb.toString();
//	}
//
//	/**
//	 * 电子邮箱隐去@之前的部分内容，用星号代替
//	 * 
//	 * @前仅有3位时，不做处理，全文显示
//	 * @前有4位及以上时，从第4位起全部用星号代替
//	 */
//	public static String formatEmail(String email) {
//		if (null == email) {
//			return null;
//		}
//		int index = email.indexOf("@");
//		StringBuilder sb = new StringBuilder(email);
//		if (index > 2) {
//			int len = email.length();
//			int count = len - 4 - (len - 1 - index);
//			String str = "";
//			for (int i = 0; i < count; i++) {
//				str += "*";
//			}
//			sb.replace(3, index, str);
//		}
//		return sb.toString();
//	}
//
//	/**
//	 * 根据文字的长度设置文字的大小
//	 * 
//	 * @param strLen
//	 *            文字的长度
//	 * @param textView
//	 */
//	public static void positionSizeAdapter(int strLen, TextView textView) {
//		if (strLen == 1) {
//			textView.setTextSize(16);
//		} else if (strLen == 2) {
//			textView.setTextSize(13);
//		} else if (strLen == 3) {
//			textView.setTextSize(10);
//		} else if (strLen == 4) {
//			textView.setTextSize(8);
//		}
//	}
//
//	/**
//	 * 限制字符最大位以点代替
//	 * 
//	 * @param str
//	 * @param len
//	 *            字符最大限度
//	 * @return 长度为len + ... 的字符
//	 */
//	public static String appendDian(String str, int len) {
//		if (null != str && str.length() > len) {
//			str = str.substring(0, len);
//			str = str + "...";
//		}
//		return str;
//	}
//
//	/**
//	 * 限制字符最大位以点代替
//	 * 
//	 * @param str
//	 * @param len
//	 *            字符最大限度
//	 * @return 总长度为len的字符
//	 */
//	public static String appendDianLimit(String str, int len) {
//		if (null != str && str.length() > len) {
//			str = str.substring(0, len - 1);
//			str = str + "...";
//		}
//		return str;
//	}
//
//	/**
//	 * 过滤网页中&nbsp;与<br/>
//	 */
//	public static String filterSpaceString(String str) {
//		if (str == null)
//			return str;
//		str = str.replaceAll("&nbsp;", "\t").replaceAll("<br/>", "\n");
//		return str;
//	}
//
//	/**
//	 * 过滤电话号码中特殊格式
//	 * 
//	 * @param str
//	 * @return 第一个电话
//	 */
//	public static String getTelPhone(String str) {
//		if (str == null)
//			return str;
//		str = filterUnNumber(str);
//		int index = -1;
//		index = str.indexOf("-");
//		if (index == -1) {
//			index = str.indexOf("－");// 全角
//		}
//		if (index != -1 && index < 5 && str.length() >= 13) {
//			String startStr = str.substring(0, index);
//			String endStr = str.substring(index + 1, index + 9);
//			str = startStr + endStr;
//		} else if (index == -1 || index >= 5) {
//			str = str.substring(0, 9);
//		}
//		return str;
//	}
//
//	/**
//	 * 过滤除数字-－以外的字符
//	 * 
//	 * @param str
//	 * @return
//	 */
//	public static String filterUnNumber(String str) {
//		// 只允数字
//		String regEx = "[^0-9-－]";
//		Pattern p = Pattern.compile(regEx);
//		Matcher m = p.matcher(str);
//		// 替换与模式匹配的所有字符（即非数字的字符将被""替换）
//		return m.replaceAll("").trim();
//	}
//
//	/**
//	 * 获取当前日期
//	 */
//	public static String getCurrentDate() {
//		Date date = new Date();
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		String currentDate = dateFormat.format(date);
//		return currentDate;
//	}
//
//	/******************************* 金额，字符格式化，数值计算，字体大小设置-----结束 *****************************/
//
//	/*************************************** 单位格转换-----文字单位，金额单位----开始 ***********************************************/
//
//	/**
//	 * 将px值转换为dip或dp值，保证尺寸大小不变
//	 * 
//	 * @param pxValue
//	 *            像素值
//	 * @param scale
//	 *            （DisplayMetrics类中属性density）
//	 * @return dp值
//	 */
//	public static int px2dip(float pxValue, Context context) {
//		float scale = getDensity(context);
//		return (int) (pxValue / scale + 0.5f);
//	}
//
//	/**
//	 * 将dip或dp值转换为px值，保证尺寸大小不变
//	 * 
//	 * @param dipValue
//	 *            dip数值
//	 * @param scale
//	 *            （DisplayMetrics类中属性density）
//	 * @return 像素值
//	 */
//	public static int dip2px(float dipValue, Context context) {
//		float scale = getDensity(context);
//		return (int) (dipValue * scale + 0.5f);
//	}
//
//	/**
//	 * 将px值转换为sp值，保证文字大小不变
//	 * 
//	 * @param pxValue
//	 *            像素值
//	 * @param fontScale
//	 *            （DisplayMetrics类中属性scaledDensity）
//	 * @return 返回sp数值
//	 */
//	public static int px2sp(float pxValue, Context context) {
//		float scale = getDensity(context);
//
//		return (int) (pxValue / scale + 0.5f);
//	}
//
//	/**
//	 * 将sp值转换为px值，保证文字大小不变
//	 * 
//	 * @param spValue
//	 *            sp数值
//	 * @param fontScale
//	 *            （DisplayMetrics类中属性scaledDensity）
//	 * @return 返回像素值
//	 */
//	public static int sp2px(float spValue, Context context) {
//		float scale = getDensity(context);
//		return (int) (spValue * scale + 0.5f);
//	}
//
//	/**
//	 * 取得手机屏幕的密度
//	 * 
//	 * @param context
//	 *            上下文
//	 * @return 手机屏幕的密度
//	 */
//	public static float getDensity(Context context) {
//		float scale = context.getResources().getDisplayMetrics().density;
//		return scale;
//	}
//
//	/************************************* 单位格转换-----文字单位，金额单位----结束 *******************************************/
//
//	/*************************************** 校验字符合法性-----开始 **********************************/
//
//	/**
//	 * 检测用户是否登录
//	 * 
//	 * @return
//	 */
//	public static boolean isUserLogin() {
//		if (!Util.isEmpty(Parameters.user.userName)
//				&& !Util.isEmpty(Parameters.user.token)) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
	/**
	 * 检查字符串是否为空
	 * 
	 * @param string
	 * @return 空:true
	 */
	public static boolean isEmpty(String string) {
		if (string != null && string.length() > 0) {
			return false;
		}
		return true;
	}
//
//	/**
//	 * 检查金额是否有效
//	 * 
//	 * @param amountString
//	 *            金额
//	 * @return
//	 */
//	public static boolean isAmountVaild(String amountString) {
//		if (amountString != null && amountString.length() > 0) {
//			double amount = 0.0;
//			try {
//				amount = Double.parseDouble(amountString);
//			} catch (Exception e) {
//				new Debugger().log(e);
//				Util.toast(R.string.toast_money_format_error);
//			}
//			// 金额必须大于0
//			if (amount > 0.001) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	public static boolean isAmountCorrect(String amount) {
//
//		return true;
//	}
//
//	/**
//	 * 判断金额是否有效
//	 * 
//	 * @param amountString
//	 *            输入的金额
//	 * @param max
//	 *            该业务限制的最大金额
//	 * @return true，false
//	 */
//	public static boolean isAmountVaild(String amountString, double max) {
//		if (isAmountVaild(amountString)) {
//			try {
//				double amount = Double.parseDouble(amountString);
//				if (amount > max) {
//					return false;
//				} else {
//					return true;
//				}
//			} catch (Exception e) {
//				new Debugger().log(e);
//			}
//		}
//		return false;
//	}
//
//	/**
//	 * 诊断卡号是否合法
//	 * 
//	 * @param cardNumber
//	 * @return
//	 */
//	private static boolean checkCardNumber(String cardNumber) {
//		if (cardNumber != null && cardNumber.length() >= 15
//				&& cardNumber.length() <= 19) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	/**
//	 * 校验银行卡卡号(借记卡)
//	 * 
//	 * @param cardId
//	 * @return false 非法卡号 true 适合合法卡号
//	 */
//	public static boolean checkDebitCard(String cardId) {
//		char bit = getDebitCardCheckCode(cardId.substring(0,
//				cardId.length() - 1));
//		if (bit == 'f') {
//			return false;
//		}
//		return cardId.charAt(cardId.length() - 1) == bit;
//	}
//
//	/**
//	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
//	 * 
//	 * @param nonCheckCodeCardId
//	 *            不含校验位的银行卡卡号
//	 * @return char 校验位
//	 */
//	private static char getDebitCardCheckCode(String nonCheckCodeCardId) {
//		if (nonCheckCodeCardId == null
//				|| nonCheckCodeCardId.trim().length() == 0
//				|| !nonCheckCodeCardId.matches("^[0-9]+$")) {
//
//			return 'f';
//		}
//		char[] chs = nonCheckCodeCardId.trim().toCharArray();
//		int luhmSum = 0;
//		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
//			int k = chs[i] - '0';
//			if (j % 2 == 0) {
//				k *= 2;
//				k = k / 10 + k % 10;
//			}
//			luhmSum += k;
//		}
//		return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
//	}
//
//	/**
//	 * 校验信用卡卡号是否为正确卡号（防止输入错误卡号） Luhn 算法校验
//	 * 
//	 * @param cardNumber
//	 *            信用卡卡号
//	 * @return false 非法卡号 true 适合合法卡号
//	 */
//	public static boolean checkCreditCard(String cardNumber) {
//		String digitsOnly = getDigitsOnly(cardNumber);
//		int sum = 0;
//		int digit = 0;
//		int addend = 0;
//		boolean timesTwo = false;
//
//		for (int i = digitsOnly.length() - 1; i >= 0; i--) {
//			digit = Integer.parseInt(digitsOnly.substring(i, i + 1));
//			if (timesTwo) {
//				addend = digit * 2;
//				if (addend > 9) {
//					addend -= 9;
//				}
//			} else {
//				addend = digit;
//			}
//			sum += addend;
//			timesTwo = !timesTwo;
//		}
//
//		int modulus = sum % 10;
//		return modulus == 0;
//	}
//
//	/**
//	 * 过滤掉非数字字符
//	 * 
//	 * @param s
//	 *            字符串
//	 * @return 过滤后卡号字符
//	 */
//	private static String getDigitsOnly(String s) {
//		StringBuffer digitsOnly = new StringBuffer();
//		char c;
//		for (int i = 0; i < s.length(); i++) {
//			c = s.charAt(i);
//			if (Character.isDigit(c)) {
//				digitsOnly.append(c);
//			}
//		}
//		return digitsOnly.toString();
//	}
//
//	/**
//	 * 检测是否是座机
//	 * 
//	 * @param phoneNumber
//	 * @return
//	 */
//	public static boolean isPhoneNumberValid(String phoneNumber) {
//		boolean isValid = false;
//		String expression = "^(010|02\\d|0[3-9]\\d{2})?\\d{6,8}$";
//		CharSequence inputStr = phoneNumber;
//		Pattern pattern = Pattern.compile(expression);
//		Matcher matcher = pattern.matcher(inputStr);
//		if (matcher.matches()) {
//			isValid = true;
//		}
//		return isValid;
//	}
//
//	/**
//	 * 检测手机号是否合法 </br>含有空值和长度少于11位
//	 * 
//	 * @param number手机号码
//	 * @return true 合法 false 不合法
//	 */
//	public static boolean checkPhoneNumber(String number) {
//		if (number.equals("") || number.length() != 11) {
//			return false;
//		}
//		Pattern pattern = Pattern.compile("1[0-9]{10}");
//		Matcher matcher = pattern.matcher(number);
//		if (matcher.matches()) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	/**
//	 * 校验email地址的合法性
//	 * 
//	 * @param emailString
//	 *            电子邮箱地址
//	 * @return true 合法 false 不合法
//	 */
//	public static boolean checkEmailAddress(String emailString) {
//		// String regEx = "^[\\w\\d]+@[\\w\\d]+(\\.[\\w\\d]+)+$";
//		String regEx = "\\w+([-+._]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
//
//		Matcher matcherObj = Pattern.compile(regEx).matcher(emailString);
//
//		if (matcherObj.matches()) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	/**
//	 * 校验身份证号的合法性 <br>
//	 * （现在仅检查长度，已经包含内容合法性，不检验真实性）
//	 * 
//	 * @param idCard
//	 *            身份证号
//	 * 
//	 * @return true 合法 false 不合法
//	 */
//	public static boolean checkIdCard(String idCard) {
//		String regEx = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
//		Matcher matcherObj = Pattern.compile(regEx).matcher(idCard);
//
//		if (matcherObj.matches()) {
//			if (idCard.length() == 15) {
//				// 如果是15位身份证号，则认为验证成功。
//				return true;
//			}
//
//			// 如果是18位身份证号，现在计算校验码是否正确。
//			int sigma = 0;
//			// 系统数表
//			Integer[] coeTable = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5,
//					8, 4, 2 };
//			// 校验码表
//			String[] codeTable = { "1", "0", "X", "9", "8", "7", "6", "5", "4",
//					"3", "2" };
//
//			// 将身份证每一位乘以系数表中的系数，结果相加。
//			for (int i = 0; i < 17; i++) {
//				int ai = Integer.parseInt(idCard.substring(i, i + 1));
//				int wi = coeTable[i];
//				sigma += ai * wi;
//			}
//			// 结果取 11 的余数
//			int number = sigma % 11;
//			// 使用余数做索引，取校验码。
//			String check_number = codeTable[number];
//			if (idCard.substring(17).equalsIgnoreCase(check_number)) {
//				return true;
//			} else {
//				return false;
//			}
//		} else {
//			return false;
//		}
//	}
//
//	/**
//	 * 校验用户姓名输入合法性 <br>
//	 * （现在仅检查用户可输入英文、中文和点）
//	 * 
//	 * @param username
//	 *            用户姓名
//	 * 
//	 * @return true 合法 false 不合法
//	 */
//	public static boolean checkUserName(String username) {
//		String regEx = "[\\w\\u4e00-\\u9fa5\\. ]+";
//		Matcher matcherObj = Pattern.compile(regEx).matcher(username);
//
//		if (matcherObj.matches()) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	/**
//	 * 校验用户姓名输入合法性 <br>
//	 * （现在仅检查用户可输入中文和点）
//	 * 
//	 * @param username
//	 *            用户姓名
//	 * 
//	 * @return true 合法 false 不合法
//	 */
//	public static boolean checkChineseUserName(String username) {
//		String regEx = "[\\u4e00-\\u9fa5\\. ]+";
//		Matcher matcherObj = Pattern.compile(regEx).matcher(username);
//
//		if (matcherObj.matches()) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	public static boolean checkzipCode(String zipCode) {
//		String regEx = "([0-9]{6})+";
//		Matcher matcherObj = Pattern.compile(regEx).matcher(zipCode);
//
//		if (matcherObj.matches()) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
	/**
	 * 检测密码强度
	 * 
	 * @param password
	 * @return 好：GOOD 一般：GENERAL 坏：BAD
	 */
	public static String checkPWLevel(String password) {
		String pwLevel = null;
		int count = 0;
		if (Pattern.compile("(?i)[a-zA-Z]").matcher(password).find()) {
			count += 10;
		}
		if (Pattern.compile("(?i)[0-9]").matcher(password).find()) {
			count += 10;
		}
		if (Pattern
				.compile(
						"(?i)[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]")
				.matcher(password).find()) {
			count += 10;
		}
		if (count == 10) {
			pwLevel = "BAD";
		} else if (count == 20) {
			pwLevel = "GENERAL";
		} else if (count == 30) {
			pwLevel = "GOOD";
		}
		return pwLevel;
	}

//	/************************************ 校验字符合法性-----结束 *********************************/
//
//	/******************************************************* 软件及相关信息----开始 ***********************************************************/
//
//	/**
//	 * 生成指定长度的随机字母和数字组合的字符串，用于客户端自定义的token
//	 * 
//	 * @param length
//	 *            指定长度
//	 * @return
//	 */
//	public static String getCharAndNumrToken(int length) {
//		String val = "";
//
//		Random random = new Random();
//		for (int i = 0; i < length; i++) {
//			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字
//
//			if ("char".equalsIgnoreCase(charOrNum)) // 字符串
//			{
//				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
//				val += (char) (choice + random.nextInt(26));
//			} else if ("num".equalsIgnoreCase(charOrNum)) // 数字
//			{
//				val += String.valueOf(random.nextInt(10));
//			}
//		}
//
//		return val;
//	}
//
//	/**
//	 * 获取目前软件版本 用于升级
//	 * 
//	 * @return
//	 */
//	public static String getAppVersionCode() {
//		String versionCode = "";
//		try {
//			PackageManager pm = ApplicationExtension.getInstance()
//					.getPackageManager();
//
//			PackageInfo pi = pm.getPackageInfo(ApplicationExtension
//					.getInstance().getPackageName(), 0);
//			int ver = (pi.versionCode - 32);
//
//			versionCode = "skb_" + (ver < 10 ? ("0" + ver) : ("" + ver));// 从33版本开始计算的
//																			// 故p_v
//																			// 是
//																			// 当前版本减去32
//		} catch (Exception e) {
//		}
//		return versionCode;
//	}
//
	/**
	 * 获取交易版本
	 * 
	 * @return
	 */
	public static String getVersionCode() {
		String versionCode = "";
		try {
			PackageManager pm = ApplicationExtension.getInstance()
					.getPackageManager();

			PackageInfo pi = pm.getPackageInfo(ApplicationExtension
					.getInstance().getPackageName(), 0);

			versionCode = "" + pi.versionCode;
		} catch (Exception e) {
		}
		return versionCode;
	}
//
//	/**
//	 * 获取目前软件版本
//	 * 
//	 * @return
//	 */
//	public static String getAppVersionName() {
//		String versionName = "";
//		try {
//			PackageManager pm = ApplicationExtension.getInstance()
//					.getPackageManager();
//
//			PackageInfo pi = pm.getPackageInfo(ApplicationExtension
//					.getInstance().getPackageName(), 0);
//			versionName = pi.versionName + "";
//		} catch (Exception e) {
//		}
//		return versionName;
//	}
//
//	// 获取manifest中下载渠道信息
//	public static String getChanel() {
//		String CHANNELID = "0";
//		try {
//			ApplicationInfo ai = ApplicationExtension
//					.getInstance()
//					.getPackageManager()
//					.getApplicationInfo(
//							ApplicationExtension.getInstance().getPackageName(),
//							PackageManager.GET_META_DATA);
//			Object value = ai.metaData.get("CHANNEL");
//			if (value != null) {
//				CHANNELID = value.toString();
//			}
//		} catch (Exception e) {
//			new Debugger().log(e);
//		}
//		CHANNELID = trim(CHANNELID);
//
//		return CHANNELID;
//	}
//
//	/**
//	 * MD5加密
//	 * 
//	 * @param s
//	 * @return
//	 */
//	public final static String MD5(String s) {
//		final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
//				'9', 'a', 'b', 'c', 'd', 'e', 'f' };
//		try {
//			byte[] strTemp = s.getBytes();
//			// 使用MD5创建MessageDigest对象
//			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
//			mdTemp.update(strTemp);
//			byte[] md = mdTemp.digest();
//			int j = md.length;
//			char str[] = new char[j * 2];
//			int k = 0;
//			for (int i = 0; i < j; i++) {
//				byte b = md[i];
//				str[k++] = hexDigits[b >> 4 & 0xf];
//				str[k++] = hexDigits[b & 0xf];
//			}
//			return new String(str);
//		} catch (Exception e) {
//			return null;
//		}
//	}
//
//	/*********************************************** 软件及相关信息----结束 ********************************************************/
//
//	/*********************************************** 设备硬件信息及网络信息-----开始 ********************************************************/
	/**
	 * 获取手机imei串号
	 * 
	 * @return
	 */
	public static String getIMEI() {
		return PhoneUtils.getIMEI();
	}

	/**
	 * 获取IMSI号
	 * 
	 * @return
	 */
	public static String getIMSI() {
		return PhoneUtils.getIMSI();
	}

//	/**
//	 * 获取手机网络运营商类型
//	 * 
//	 * @return
//	 */
//	public static String getPhoneISP() {
//		return PhoneUtils.getPhoneISP();
//	}
//
//	/**
//	 * 获取手机系统版本
//	 * 
//	 * @return
//	 */
//	public static String getPhoneOSVersion() {
//		return PhoneUtils.getPhoneOSVersion();
//	}
//
//	/**
//	 * 获取手机标识 eg:ME860
//	 * 
//	 * @return
//	 */
//	public static String getPhoneModel() {
//		return PhoneUtils.getPhoneModel();
//	}
//
//	/**
//	 * 获取手机型号 eg:ME860_HKTW
//	 * 
//	 * @return
//	 */
//	public static String getPhoneType() {
//		return PhoneUtils.getPhoneType();
//	}
//
//	/**
//	 * 获取手机厂商 eg:motorola
//	 * 
//	 * @return
//	 */
//	public static String getPhonePhoneManuFacturer() {
//		return PhoneUtils.getPhonePhoneManuFacturer();
//	}
//
//	/**
//	 * 网络是否可用
//	 * 
//	 * @return true可用 </br> false不可用
//	 */
//	public static boolean isNetworkAvailable() {
//		return PhoneUtils.isNetworkAvailable();
//	}
//
//	/**
//	 * 返回当前手机接入的网络类型,
//	 * 
//	 * @return 返回值: 1.代表mobile(2G3G), 2代表wifi
//	 */
//	public static String getNetworkStat() {
//		return PhoneUtils.getNetworkStat();
//	}
//
//	/**
//	 * 检查gps是否可用
//	 * 
//	 * @return
//	 */
//	public static boolean isGpsAvaiable() {
//		return PhoneUtils.isGpsAvaiable();
//	}
//
//	/**
//	 * 打开gps和关闭gps
//	 * 
//	 * @param context
//	 */
//	public static void autoGps(Context context) {
//		PhoneUtils.autoGps(context);
//	}
//
//	/**
//	 * TextView 红色高亮
//	 * 
//	 * @param str
//	 *            需要高亮显示的字符串
//	 * @param from
//	 *            起始位置
//	 * @param to
//	 *            结束为止
//	 * @return
//	 */
//	public static SpannableStringBuilder buildRed(String str, int from, int to) {
//		SpannableStringBuilder style = new SpannableStringBuilder(str);
//		style.setSpan(new ForegroundColorSpan(Color.RED), from, to,
//				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		return style;
//	}
//
//	/**
//	 * TextView 文字高亮显示
//	 * 
//	 * @param color
//	 *            需要显示高亮的颜色
//	 * @param str
//	 *            需要高亮显示的字符串
//	 * @param from
//	 *            起始位置
//	 * @param to
//	 *            结束为止
//	 * @param isUnderline
//	 *            是否要下划线
//	 * @return
//	 */
//	public static SpannableStringBuilder buildColor(int color, String str,
//			int from, int to, boolean isUnderline) {
//		SpannableStringBuilder style = new SpannableStringBuilder(str);
//		style.setSpan(new ForegroundColorSpan(color), from, to,
//				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		if (isUnderline) {
//			style.setSpan(new UnderlineSpan(), from, to,
//					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}
//		return style;
//	}
//
//	/**
//	 * WebView支持多点缩放,去掉WebView 缩放按钮，兼容3.0一下用户使用反射
//	 * 
//	 * @param view
//	 */
//	// @TargetApi(11)
//	public static void setWebViewZoomControlGone(View view) {
//		boolean ISHONEYCOMB = Build.VERSION.SDK_INT >= 11;
//		if (ISHONEYCOMB) {
//			// 此方法需要3.0+ sdk，编译时 需要将sdk切换一下
//			// ((WebView)view).getSettings().setDisplayZoomControls(false);
//		} else {
//			Class<?> classType;
//			Field field;
//			try {
//				classType = WebView.class;
//				field = classType.getDeclaredField("mZoomButtonsController");
//				field.setAccessible(true);
//				ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(
//						view);
//				mZoomButtonsController.getZoomControls().setVisibility(
//						View.GONE);
//				try {
//					field.set(view, mZoomButtonsController);
//				} catch (IllegalArgumentException e) {
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					e.printStackTrace();
//				}
//			} catch (SecurityException e) {
//				e.printStackTrace();
//			} catch (NoSuchFieldException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * 判断促销时间等是否结束
//	 * 
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 */
//	public static boolean timeNotEnd(String startTime, String endTime) {
//		if (startTime == null || endTime == null)
//			return false;
//		if (startTime.equals("") || endTime.equals(""))
//			return false;
//		try {
//			long start = Long.parseLong(startTime);
//			long end = Long.parseLong(endTime);
//			if ((end - start) > 0) {
//				return true;
//			}
//		} catch (NumberFormatException e) {
//			new Debugger().log(e);
//		}
//		return false;
//	}
//
//	/**
//	 * 拦截返回事件
//	 * 
//	 * @param activity
//	 * @param channelCode
//	 *            第三方渠道号
//	 * @param callbackUrl
//	 *            第三方回调地址
//	 * @see #splitCallBack(String, String)
//	 */
//	public static void interceptBackEvent(Activity activity,
//			String channelCode, String callbackUrl) {
//
//		if (null == channelCode) {
//			channelCode = "";
//		}
//
//		if ("".equals(channelCode)) {
//			activity.finish();
//		} else {
//			if (!"".equals(callbackUrl)) {
//
//				String[] strings = splitCallBack(channelCode, callbackUrl);
//				String pkg = strings[0].trim();
//				String clazz = strings[1].trim();
//				Intent intent = new Intent();
//				intent.setComponent(new ComponentName(pkg, clazz));
//				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);// 根据德阳最新手机客户端修改返回德阳时启动Activity的属性
//				activity.startActivity(intent);
//			}
//			ApplicationExtension.getInstance().exit();
//		}
//	}
//
//	/**
//	 * 根据渠道号拆分callback
//	 * 
//	 * @param channelCode
//	 * @param callback
//	 * @return String[]
//	 */
//	public static String[] splitCallBack(String channelCode, String callback) {
//
//		if (null == callback || "".equals(callback)) {
//			throw new IllegalArgumentException("callback is null ");
//		}
//
//		String[] results = new String[] {};
//
//		// TODO 暂时还未实现其他第三方,用Map<code,prefix>保存 ??
//		results = callback.replaceAll("cmb://", "").split("\\|");
//		final int length = results.length;
//		if (2 != length) {
//			throw new RuntimeException("callback format is error");
//		}
//		return results;
//	}
//
//	/**
//	 * CanUseWithOutInput 判断金额是否超过100
//	 * 
//	 * @param mount
//	 *            金额 单位元
//	 * 
//	 * */
//	private boolean CanUseWithOutInput(int mount) {
//		boolean beyond = false;
//		if (mount >= 100) {
//			beyond = true;
//		}
//		return beyond;
//	}
//
//	/**
//	 * 创建发送方跟踪号(6为数字)Series,递增
//	 * 
//	 * @return
//	 */
//	public static String createSeries() {
//
//		LklSharedPreferences pref = LklSharedPreferences.getInstance();
//		int value = pref.getInt(UniqueKey.PREFERENCE_SERIES_KEY);
//		if (value < UniqueKey.PREFERENCE_SERIES_MAX_VALUE) {
//			value = value + 1;
//		} else {
//			value = 0;
//		}
//		String series = addMarkToStringFront(String.valueOf(value), "0", 6);
//		pref.putInt(UniqueKey.PREFERENCE_SERIES_KEY, value);
//		return series;
//	}
//
//	/**
//	 * 在字符串前添加指定的字符
//	 * 
//	 * @param string
//	 *            需要补全的字符
//	 * @param mark
//	 *            填充的字符
//	 * @param len
//	 *            补全后的字符长度
//	 * @return
//	 */
//	public static String addMarkToStringFront(String string, String mark,
//			int len) {
//		StringBuilder stringBuilder = new StringBuilder();
//		int length = string.length();
//		int appendCount = length < len ? len - length : 0;
//
//		for (int i = 0; i < appendCount; i++) {
//			stringBuilder.append(mark);
//		}
//		return stringBuilder.append(string).toString();
//	}
//
//	/**
//	 * 在字符串前添加指定的字符
//	 * 
//	 * @param string
//	 *            需要补全的字符
//	 * @param mark
//	 *            填充的字符
//	 * @param len
//	 *            补全后的字符长度
//	 * @return
//	 */
//	public static String addMarkToStringAfter(String string, String mark,
//			int len) {
//		StringBuilder stringBuilder = new StringBuilder();
//		int length = string.length();
//		int appendCount = length < len ? len - length : 0;
//		stringBuilder.append(string);
//		for (int i = 0; i < appendCount; i++) {
//			stringBuilder.append(mark);
//		}
//		return stringBuilder.toString();
//	}
//
//	/**
//	 * 根据手机model判断是否是小米2
//	 * 
//	 * @return
//	 */
//	public static boolean isMITwo() {
//		String model = getPhoneModel();
//		model = formatString(model);
//		if (UniqueKey.PHONE_MODEL.equals(model)) {
//			return true;
//		}
//		return false;
//	}
//
//	/**
//	 * 根据rom版与最低版本比较，如果手机rom版本高于指定版本，则通过广播关闭杜比
//	 * 
//	 * @return
//	 */
//	public static boolean isNewVersion() {
//		String osVersion = Build.VERSION.INCREMENTAL;
//		if (osVersion.startsWith(UniqueKey.STABLETAG)) { // 稳定版
//			// osVersion.compareTo(string) 这是使用有问题，是按照字符比对，JLB4.0会大于JLB16.0。
//			String replaceVerson = osVersion.replace(UniqueKey.STABLETAG, "");
//			String replaceTarget = UniqueKey.STABLEVERSION.replace(
//					UniqueKey.STABLETAG, "");
//			return strCompare(replaceVerson, replaceTarget);
//		} else {
//			return strCompare(osVersion, UniqueKey.ENGVERSION);
//		}
//	}
//
//	/**
//	 * 整形待分割符字符串比对
//	 * 
//	 * @param dist
//	 *            "3.5.8"
//	 * @param targetVersion
//	 *            "3.5.7"
//	 * @return "3.5.8">"3.5.7" "3.5.10">"3.5.7" ...
//	 */
//	private static boolean strCompare(String dist, String targetVersion) {
//
//		String[] osVersions = dist.split("\\.");
//		String[] targetVersions = targetVersion.split("\\.");
//		int osLength = osVersions.length;
//		int tarLength = targetVersions.length;
//		int length = Math.min(osLength, tarLength);
//		int isNew = 0;
//
//		for (int i = 0; i < length; i++) {
//			int version = Integer.parseInt(osVersions[i]);
//			int target = Integer.parseInt(targetVersions[i]);
//			if (version > target) {
//				isNew = version - target;
//				break;
//			} else if (version < target) {
//				isNew = version - target;
//				break;
//			} else {
//				isNew = 0;
//			}
//		}
//
//		if ((isNew == 0) && (osLength > tarLength)) {
//			isNew = osLength - tarLength;
//		}
//
//		return isNew >= 0;
//	}
//
//	/**
//	 * 广播检测杜比
//	 * 
//	 * @param context
//	 */
//	public static void checkDolbyIntent(Context context) {
//		Intent intent = new Intent();
//		intent.setAction(UniqueKey.CHECK_STATUS_ACTION);
//		context.sendBroadcast(intent);
//	}
//
//	/**
//	 * 广播更新杜比
//	 * 
//	 * @param context
//	 * @param bEnable
//	 *            1 开启; 0关闭
//	 */
//	public static void updateDolbyIntent(Context context, int bEnable) {
//		Intent intent = new Intent();
//		intent.setAction(UniqueKey.ACTION_DOLBY_UPDATE);
//		intent.putExtra("enable", bEnable);
//		context.sendBroadcast(intent);
//	}
//
//	/**
//	 * 跳转到主页面,需要设置flag参数
//	 * 
//	 * @param context
//	 * @param flags
//	 */
//	public static void startHomeActivity(Context context, int flags) {
//		// Intent intent = new Intent(context, MainActivity.class);
//		Intent intent = new Intent(context, ShouDanMainActivity.class);
//
//		if (flags != 0) {
//			intent.setFlags(flags);
//		}
//		context.startActivity(intent);
//	}
//
//	/**
//	 * 跳转到登录,需要设置flag参数
//	 * 
//	 * @param context
//	 * @param flags
//	 */
//	public static void startLoginActivity(Context context, int flags) {
//		Intent intent = new Intent(context, LoginActivity.class);
//
//		if (flags != 0) {
//			intent.setFlags(flags);
//		}
//		context.startActivity(intent);
//	}
//
//	/**
//	 * 跳转到主页面,不需要设置flag参数
//	 * 
//	 * @param context
//	 */
//	public static void startHomeActivity(Context context) {
//		startHomeActivity(context, 0);
//	}
//
//	/**
//	 * 获得跳转到主页的intent
//	 * 
//	 * @param context
//	 * @return
//	 */
//	public static Intent getHomeIntent(Context context) {
//		// return new Intent(context,MainActivity.class);
//		return new Intent(context, ShouDanMainActivity.class);
//
//	}
//
//	/**
//	 * 根据服务器返回code,判断是否成功.
//	 * 
//	 * @param retCode
//	 *            服务器返回的Code
//	 * @return
//	 */
//	public static boolean isProcessSuccess(final String retCode) {
//		return Parameters.successRetCode.equals(retCode);
//	}
//
//	public static String formatDisplayAmount(String amount) {
//		return formatAmount(amount) + "元";
//	}
//
//	public static String getNowTime() {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		return sdf.format(new Date());
//	}
//
//	public static String addAmount(String amt1, String amt2) {
//
//		return formatAmount(String.valueOf(Double.parseDouble(amt1)
//				+ Double.parseDouble(amt2)));
//
//	}
//
//	/**
//	 * 添加括号(value)
//	 * 
//	 * @param value
//	 * @return
//	 */
//	public static String addBrackets(String value) {
//		if (null == value)
//			return "";
//		return "(" + value + ")";
//	}
//
//	/**
//	 * 剩余容量单位转换
//	 * @author ZhengWx
//	 * @date 2014年9月11日 上午11:45:19
//	 * @param size 大小，单位bit
//	 * @return 转换后的单位大小，如：10.3MB
//	 * @since 1.0
//	 */
//	public static String convertSpaceSize(long size) {
//		String strUnit = "Bytes";
//		double intDivisor = 1;
//		StringBuilder sb = new StringBuilder();
//
//		try {
//			size /= 8;
//			if (size >= 1024 * 1024) {
//				strUnit = "MB";
//				intDivisor = 1024 * 1024;
//			} else if (size >= 1024) {
//				strUnit = "KB";
//				intDivisor = 1024;
//			}
//
//			if (intDivisor == 1) {
//				sb.append(size);
//				sb.append(" ");
//				sb.append(strUnit);
//			} else {
//				DecimalFormat df = new DecimalFormat("######0.00");
//
//				sb.append(df.format(size / intDivisor));
//				sb.append(" ");
//				sb.append(strUnit);
//			}
//		} catch (Exception e) {
//			sb.setLength(0);
//			sb.append("0.00");
//			sb.append(strUnit);
//		}
//
//		return sb.toString();
//	}
//	
//	/**
//	 * 判断输入的手机号是否为中国移动号码
//	 * @author Yejx
//	 * @date 2014年11月27日 下午3:39:20
//	 * @param phoneNo
//	 * @return boolean
//	 * @since 1.0
//	 */
//	public static boolean isChinaMobileNo(String phoneNo){
//		Pattern pattern1 = Pattern.compile("^134[0-8].*");
//		Matcher matcher1 = pattern1.matcher(phoneNo);
//		
//		Pattern pattern2 = Pattern.compile("^13[5-9].*");
//		Matcher matcher2 = pattern2.matcher(phoneNo);
//		
//		Pattern pattern3 = Pattern.compile("^15[0-2].*");
//		Matcher matcher3 = pattern3.matcher(phoneNo);
//		
//		Pattern pattern4 = Pattern.compile("^15[7-9].*");
//		Matcher matcher4 = pattern4.matcher(phoneNo);
//		
//		Pattern pattern5 = Pattern.compile("^18[2-3].*");
//		Matcher matcher5 = pattern5.matcher(phoneNo);
//		
//		Pattern pattern6 = Pattern.compile("^18[7-8].*");
//		Matcher matcher6 = pattern6.matcher(phoneNo);
//		
//		if(matcher1.matches() || matcher2.matches() || matcher3.matches() 
//				|| matcher4.matches() || matcher5.matches()|| matcher6.matches()
//				|| phoneNo.startsWith("147")){
//			return true;
//		}
//		return false;
//	}
//	
//	/**
//	 * 判断蓝牙设备是否为ME18设备
//	 * @author Yejx
//	 * @date 2014年11月28日 上午11:05:55
//	 * @param name
//	 * @return boolean
//	 * @since 1.0
//	 */
//	public static boolean bluetoothDeviceFilter(String name){
//		if(name != null && name.toLowerCase().contains("me18")){
//			return true;
//		}
//		return false;
//	}
//	
//	/**
//	 * 将JSONObject解析为Map对象
//	 * @author Yejx
//	 * @date 2014年12月9日 上午14:52:50
//	 * @param jsonObject
//	 * @return Map<String,Object>
//	 * @since 1.0
//	 */
//	public static Map<String,Object> getMap(JSONObject jsonObject){
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			Iterator<String> keyIter = jsonObject.keys();
//			while (keyIter.hasNext()) {
//				String key = (String) keyIter.next();
//				Object value = jsonObject.get(key);
//				map.put(key, value);
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return map;
//	}
//	
//	/**
//	 * 将jsonArray解析为List对象
//	 * @author Yejx
//	 * @date 2014年12月9日 上午14:52:50
//	 * @param jsonArray
//	 * @return List<byte[]>
//	 * @since 1.0
//	 */
//	public static List<byte[]> getList(JSONArray jsonArray){
//		List<byte[]>  list = new ArrayList<byte[]>();
//		try {
//			for (int i = 0; i < jsonArray.length(); i++) {
//				byte[] b = CodecUtils.hex2byte(jsonArray.getString(i));
//				list.add(b);
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return list;
//	}
//	
//	/**
//	 * 过滤有效Aid列表
//	 * @author Yejx
//	 * @date 2014年12月11日 上午11:13:18
//	 * @param aidList
//	 * @return List<byte[]>
//	 * @since 1.0
//	 */
//	public static List<byte[]> filterAidList(List<byte[]> aidList){
//		List<byte[]> aids = new ArrayList<byte[]>();
//		for (byte[] aid : aidList) {
//			String aidStr = CodecUtils.hexString(aid);
//			if("A0000003330101060048080000010000".equals(aidStr) 
//					|| "A0000003330101060048080000030000".equals(aidStr) 
//					|| "D156000015CCECB8AECDA8BFA800".equals(aidStr)
//					|| "D1560001018000000000000100000000".equals(aidStr)){
//				aids.add(aid);
//			}
//		}
//		return aids;
//	}
//	
//	/**
//	 * 判断输入的金额是否有误
//	 * @author Yejx
//	 * @date 2015年1月13日 下午11:53:30
//	 * @param amount
//	 * @return boolean
//	 * @since 1.0
//	 */
//	public static boolean isAmountError(String amount){
//		Pattern pattern = Pattern.compile("^([1-9]\\d*|0)(\\.\\d{1,2})?$");
//		Matcher matcher = pattern.matcher(amount);
//	    if(matcher.matches()){
//	    	return false;
//		}
//		return true;
//	}
	
	//卸载应用
	public void uninstall(Context context){
		
		Intent intent = new Intent();
	    intent.setAction(Intent.ACTION_DELETE);
	    intent.setData(Uri.parse("package:com.example.cardiograph"));
	    context.startActivity(intent);
	}
}
