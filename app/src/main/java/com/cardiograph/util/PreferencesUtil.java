
package com.cardiograph.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * 此类用于记录和应用生命周期相关的一些参数 统一管理存入sp中的数据
 * 使用的key也要在此类中统一定义
 * @author xyz
 */
public class PreferencesUtil {

    /**
     * 存入LklPreferences的数据，key都在此类中定义
     */
    public static final String DEMO_KEY = "DEMO_KEY";
    public static final String KEY_LOGIN_NAME = "login_name";
    public static final String KEY_IS_SET_HEAD = "isSetHead";
    public static final String KEY_SAFE_SCORE = "safe_score";
    public static final String KEY_LOGIN_OUT = "login_out";
    public static final String KEY_IGNORE_SET_GESTURE = "igonre_set_gesture";
    public static final String KEY_SHOW_UPGRADE_TIME = "KEY_SHOW_UPGRADE_TIME";


    private static PreferencesUtil instance;

    private SharedPreferences sp;

    private Editor editor;

    private PreferencesUtil(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sp.edit();
    }

    public static synchronized PreferencesUtil getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesUtil(context);
        }
        return instance;
    }

    public void remove(String key){
    	editor.remove(key);
    	editor.commit();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void putFloat(String key,float value){
        editor.putFloat(key, value);
        editor.commit();
    }


    public void putLong(String key,long value){
        editor.putLong(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return sp.getString(key, "");
    }

    public String getString(String key,String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }

    public boolean getBoolean(String key,boolean devaultValue) {
        return sp.getBoolean(key, devaultValue);
    }
    
    public int getInt(String key) {
        return sp.getInt(key, 0);
    }

    public float getFloat(String key,float defaultValue){
        return sp.getFloat(key,defaultValue);
    }

    public long getLong(String key,long defaultValue){
        return sp.getLong(key,defaultValue);
    }
}
