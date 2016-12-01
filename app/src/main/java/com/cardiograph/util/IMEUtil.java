package com.cardiograph.util;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 绯荤粺杈撳叆娉曢敭鐩?妫?祴宸ュ叿
 * 
 * @author xyz
 * 
 */
public class IMEUtil {
	/**
	 * 闅愯棌閿洏
	 * @param context
	 */
	public static void hideIme(Activity context) {
		if (context == null)
			return;
		final View v = context.getWindow().peekDecorView();
		if (v != null && v.getWindowToken() != null) {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}
	
	/**
	 * 妫?煡绯荤粺閿洏鏄惁鏄剧ず
	 * @param context
	 * @return
	 */
	public static boolean  isSysKeyboardVisiable(Activity context) {
		final View v = context.getWindow().peekDecorView();
		if (v != null && v.getWindowToken() != null) {
			return true;
		}
		return false;
	}
}
