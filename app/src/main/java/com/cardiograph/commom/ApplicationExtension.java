package com.cardiograph.commom;

import java.util.LinkedList;
import java.util.List;

import com.cardiograph.constance.Parameters;
import com.cardiograph.log.Debugger;
import com.cardiograph.model.UserInfo;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 自定义全局程序
 * @author bob
 * 
 *
 */
public class ApplicationExtension extends Application  {
	
	private static ApplicationExtension instance;

	private List<Activity> activities = new LinkedList<Activity>();
	public UserInfo user = null;

	public static ApplicationExtension getInstance() {
        return instance;
    }
    
    /**
     * 添加activity
     * @param activity
     */
    public void addActivity(Activity activity) {
		activities.add(activity);
	}
    
    /**
     * 移除activity
     * @param activity
     */
    public void removeActivity(Activity activity) {
    	if (activities.contains(activity)) {
    		activities.remove(activity);
		}
	}

    /**
     * 退出应用，关闭堆栈中Activity
     */
    public void exit() {
		user.destory();
    	
		for (Activity activity : activities) {
			activity.finish();
		}
		
//		//清理过期的缓存数据
//		FileCache fileCache = new FileCache(getApplicationContext());
//		fileCache.removeExpire();
//		DatabaseCache dbCache = new DatabaseCache(getApplicationContext());
//		dbCache.removeExpire();
		
		System.exit(0);  
	}
    	
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        user = UserInfo.getInstance();
        Parameters.initServerAddress();
//        //初始化crash处理器
//        CrashHandler.init();   
//        // This configuration tuning is custom. You can tune every option, you may tune some of them, 
//     		// or you can create default configuration by
//     		//  ImageLoaderConfiguration.createDefault(this);
//     		// method.
// 		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
// 			.threadPriority(Thread.NORM_PRIORITY - 2)
// 			.memoryCacheSize(2 * 1024 * 1024) // 2 Mb
// 			.denyCacheImageMultipleSizesInMemory()
// 			.discCacheFileNameGenerator(new Md5FileNameGenerator())
// 			.imageDownloader(new ExtendedImageDownloader(getApplicationContext()))
// 			.tasksProcessingOrder(QueueProcessingType.LIFO)
////     			.enableLogging() // Not necessary in common
// 			.build();
// 		// Initialize ImageLoader with configuration.
// 		ImageLoader.getInstance().init(config);
    }
    
    /**
     * 当系统内存过低时的事件
     */
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	/**
	 * 程序退出时的事件
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();
		user.destory();
	}
	
	/**
	 * 是否是第一次运行当前版本应用
	 * @return
	 */
	public boolean isFirstRun(){
		PackageManager packageManager = getPackageManager();
		boolean firstRun = false;
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			
//			firstRun = LklSharedPreferences.getInstance().getBoolean(
//					String.format("%s%04d",UniqueKey.firstRun,packageInfo.versionCode),
//					true);

		} catch (Exception e) {
			new Debugger().log(e);
		}
		
		return firstRun;
	}

    /**
     * 是否是第一次在当前手机登录
     */
	public boolean isFirstLogin(String loginName){
        PackageManager packageManager = getPackageManager();
        boolean firstRun = false;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

//            firstRun = LklSharedPreferences.getInstance().getBoolean(
//                    String.format("%s%04d",UniqueKey.firstRun,packageInfo.versionCode) + loginName,
//                    true);

        } catch (Exception e) {
            new Debugger().log(e);
        }

        return firstRun;
    }

    public void setFirtLogin(String loginName){

        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

//            LklSharedPreferences.getInstance().putBoolean(
//                    String.format("%s%04d",UniqueKey.firstRun,packageInfo.versionCode) + loginName,
//                    false);

        } catch (Exception e) {
            new Debugger().log(e);
        }
    }


	/**
	 * 设置firstRun标志，表示已经运行过当前版本应用
	 */
	public void setFirstRun(){
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			
//			LklSharedPreferences.getInstance().putBoolean(
//					String.format("%s%04d", UniqueKey.firstRun,packageInfo.versionCode),
//					false);

		} catch (Exception e) {
			new Debugger().log(e);
		}
	}

	public Activity getCurActivity(){
		return activities.get(activities.size()-1);
	}
}
