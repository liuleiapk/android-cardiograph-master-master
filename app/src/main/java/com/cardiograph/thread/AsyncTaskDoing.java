package com.cardiograph.thread;

import android.os.AsyncTask;

/**
 * AsyncTask的简单抽象类，可用于简单的异步操作
 * 
 * <p>如果需要设置进度，需在<code>doInBackground()</code>中调用<code>publishProgress(Progress... values)</code>方法，
 * 然后在<code>progressUpdate(int progress)</code>中更新UI</p>
 * 
 * <p>设置完成后，在类对象后面调用<code>.execute()</code>即可执行异步操作</p>
 * @ClassName AsyncTaskDoing
 * @author ZhengWx
 * @date 2014年8月24日 上午8:52:03
 */
public abstract class AsyncTaskDoing extends AsyncTask<Void, Integer, Void>{
	
	/**
	 * 后台运行
	 * @author ZhengWx
	 * @date 2014年8月24日 上午8:47:31
	 * @since 1.0
	 */
	public abstract void doInBackground();
	/**
	 * 准备运行
	 * @author ZhengWx
	 * @date 2014年8月24日 上午8:47:26
	 * @since 1.0
	 */
	public abstract void preExecute();
	/**
	 * 更新进度
	 * @author ZhengWx
	 * @date 2014年8月24日 上午8:46:27
	 * @param progress 当前进度值
	 * @since 1.0
	 */
	public abstract void progressUpdate(int progress);
	/**
	 * 运行结束
	 * @author ZhengWx
	 * @date 2014年8月24日 上午8:47:37
	 * @since 1.0
	 */
	public abstract void postExecute();
	
	@Override
	protected void onPreExecute() {
		preExecute();
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress) {
		progressUpdate(progress[0]);
	}
	
	@Override
	protected void onPostExecute(Void result) {
		postExecute();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		doInBackground();
		return null;
	}
}
