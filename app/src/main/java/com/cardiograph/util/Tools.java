/************************************************************
 *	版权所有  (c)2011,   郭源。<p>	
 *  文件名称	：Tools.java<p>
 *
 *  创建时间	：2011-11-23 上午9:57:23 
 *  当前版本号：v1.0
 ************************************************************/
package com.cardiograph.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cardiograph.constance.Constance;
import com.cardiograph.view.MainActivity;
import com.example.cardiograph.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.widget.Toast;

/************************************************************
 *  内容摘要	：<p>
 *
 *  作者	：叶金新
 *  创建时间	：2014-9-15 上午7:34:34 
 *  当前版本号：v1.0
 *  历史记录	:
 *  	日期	: 2014-9-15 上午7:34:34 	修改人：
 *  	描述	:
 ************************************************************/

public class Tools
{
	private static Tools instance;
	
	private Tools()
	{
	}
	
	public static Tools getInstance()
	{
		if(instance == null)
		{
			instance = new Tools();
		}
		return instance;
	}
	public Map<String,Object> getMap(JSONObject jsonObject){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Iterator<String> keyIter = jsonObject.keys();
			while (keyIter.hasNext()) {
				String key = (String) keyIter.next();
				Object value = jsonObject.get(key);
				map.put(key, value);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	public List<Map<String,Object>> getList(JSONArray jsonArray){
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Iterator<String> keyIter = jsonObject.keys();
				Map<String, Object> map = new HashMap<String, Object>();
				while (keyIter.hasNext()) {
					String key = (String) keyIter.next();
					Object value = jsonObject.get(key);
					map.put(key, value);
				}
				list.add(map);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
	
	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 *            像素值
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return dp值
	 */
	public static int px2dip(float pxValue, Context context) {
		float scale = getDensity(context);
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 *            dip数值
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return 像素值
	 */
	public static int dip2px(float dipValue, Context context) {
		float scale = getDensity(context);
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 *            像素值
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return 返回sp数值
	 */
	public static int px2sp(float pxValue, Context context) {
		float scale = getDensity(context);

		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 *            sp数值
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return 返回像素值
	 */
	public static int sp2px(float spValue, Context context) {
		float scale = getDensity(context);
		return (int) (spValue * scale + 0.5f);
	}

	/**
	 * 取得手机屏幕的密度
	 * 
	 * @param context
	 *            上下文
	 * @return 手机屏幕的密度
	 */
	public static float getDensity(Context context) {
		float scale = context.getResources().getDisplayMetrics().density;
		return scale;
	}
	
	public void writer(String path, List<Integer> lst, boolean append) throws IOException{
		
		
		//1 FileOutputStream
		
		//2 OutputStreamWriter
		
		//3 BufferedWriter
		BufferedWriter bw = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			/**
			 * 创建一个字节流 true 是否append
			 */

			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				// file.delete();
				// file.createNewFile();
			}
			fos = new FileOutputStream(path, append);//

			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			String s = "";

			for (int i = 0; i < lst.size(); i++) {
				s = String.valueOf(lst.get(i));
				bw.write(s);// 写入
//				System.out.println("-----------" + s);
				// System.getProperty("line.separator");
				bw.newLine();// 写入换行符
			}

			bw.flush();

		} finally {
			bw.close();
			osw.close();
			fos.close();
		}
		
	}
	
	public void writerInt(String path, List<Integer> lst, boolean append) throws IOException {
		FileOutputStream outFile = null;
		DataOutputStream out = null;
		try {
			outFile = new FileOutputStream(path, append);
			out = new DataOutputStream(outFile);
			int length;
			byte[] buf = new byte[1024];
			String str ="1234567890";
			byte[] words  = str.getBytes();
			for (int j = 0; j < lst.size(); j++) {
//				out.write(words , 0 , words.length);
//				out.writeInt(10000+j);
				out.writeShort(lst.get(j));
			}
		}finally{
			outFile.close();
			out.close();
		}
	}
	
	public void writerLoseData(String path, List<String> lst) throws IOException{
		
		
		//1 FileOutputStream
		
		//2 OutputStreamWriter
		
		//3 BufferedWriter
		BufferedWriter bw = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			/**
			 * 创建一个字节流 true 是否append
			 */

			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				// file.delete();
				// file.createNewFile();
			}
			fos = new FileOutputStream(path, true);//

			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			String s = "";

			for (int i = 0; i < lst.size(); i++) {
				s = lst.get(i);
				bw.write(s);// 写入
//				System.out.println("-----------" + s);
				// System.getProperty("line.separator");
				bw.newLine();// 写入换行符
			}

			bw.flush();

		} finally {
			bw.close();
			osw.close();
			fos.close();
		}
		
	}
	
	public void writer16(String path, List<Integer> lst, boolean append) throws IOException{
		
		
		//1 FileOutputStream
		
		//2 OutputStreamWriter
		
		//3 BufferedWriter
		BufferedWriter bw = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			/**
			 * 创建一个字节流 true 是否append
			 */

			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				// file.delete();
				// file.createNewFile();
			}
			fos = new FileOutputStream(path, append);//

			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			String s = "";

			for (int i = 0; i < lst.size(); i++) {
				s = Integer.toHexString(lst.get(i)).toUpperCase();
 				if(s.length()==1){
					s="0"+s;
				}
				bw.write(s);// 写入
//				System.out.println("-----------" + s);
				// System.getProperty("line.separator");
				bw.newLine();// 写入换行符
			}

			bw.flush();

		} finally {
			bw.close();
			osw.close();
			fos.close();
		}
		
	}
	
	public void writer(String path, byte[] txValue) throws IOException{


		//1 FileOutputStream

		//2 OutputStreamWriter

		//3 BufferedWriter
		BufferedWriter bw = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			/**
			 * 创建一个字节流 true 是否append
			 */

			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				// file.delete();
				// file.createNewFile();
			}
			fos = new FileOutputStream(path, true);//

			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			String s = "";

			for (int i = 0; i < txValue.length; i++) {
				s = Integer.toHexString(dataParser(txValue[i])).toUpperCase();
				if(s.length()==1){
					s="0"+s;
				}
				//				s = String.valueOf(dataParser(txValue[i]));
				bw.write(s+"   ");// 写入
//				System.out.println("-----------" + s);
				// System.getProperty("line.separator");
			}
			bw.newLine();// 写入换行符

			bw.flush();

		} finally {
			bw.close();
			osw.close();
			fos.close();
		}

	}


	private int dataParser(int x) 
	{
		int y = x >> 8 ;
			if(x>=0){
				return (((x^y)-y)|(y<<8));
			}else{
				return Math.abs((((x^y)-y)|(y<<8)));
			}
	} 

	/**
	 * 演示 字符流 套一个字节流
	 * @throws IOException
	 */
	public void reader(String path)throws IOException{
		
		//1 FileInputStream
		
		//2 InputStreamReader
		
		//3 BufferedReader
		
		BufferedReader bw = null;
		FileInputStream fos = null;
		InputStreamReader osw = null;
		try {
			fos = new FileInputStream(path);
			osw = new InputStreamReader(fos);
			bw = new BufferedReader(osw);
			int i = 0;
			String s = "";

			while((s=bw.readLine())!=null){
				
				System.out.println(s);
			}

			

		} finally {
			bw.close();
			fos.close();
		}
		
	}
	
	private int[][] point = {{0, 180, 100, 180},{100, 180, 110, 160},
			{110, 160, 128, 140},{128, 140, 140, 160},
			{140, 160, 145, 180},{145, 180, 185, 180},
			{185, 180, 190, 200},{190, 200, 210, 20},
			{210, 20, 225, 230},{225, 230, 232, 180},
			{232, 180, 300, 180},{300, 180, 310, 160},
			{310, 160, 320, 150},{320, 150, 337, 140},
			{337, 140, 350, 133},{350, 133, 373, 140},
			{373, 140, 380, 150},{380, 150, 390, 180},
			{390, 180, 421, 180},{421, 180, 430, 170},
			{430, 170, 440, 180},{440, 180, 550, 180}};
	private Canvas canvas = null;
	private Bitmap board = null;  
	private Canvas boardCanvas = null;
	private int mount = 0, num = 0, width = 0, height = 0, length = 0;
    /**
     * 波形位置起始坐标
     */
    private float x = 0;
	private Timer timer;
	public void drawWaveform(final Context context, final SurfaceHolder sfh, List<List<Integer>> lstBpm, int count) {
//		if(MainActivity.isLoop){
//			MainActivity.isMove = true;
//		}
		if(timer!=null){
			timer.cancel();
			mount = 0;
		}
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			width = 540;
			num = 22;
		} else {
			width = 1080;
			num = 22;
		}
		board = Bitmap.createBitmap(4*540, 240,Bitmap.Config.ARGB_8888);  
		boardCanvas = new Canvas(board);
        Paint p = new Paint(); 
        p.setColor(Color.BLACK);
        p.setAntiAlias(true);
		p.setStrokeWidth(3);
//		for (int j = 0; j < 4; j++) {
//			for (int i = 0; i < point.length; i++) {
//				boardCanvas.drawLine(point[i][0]+j*540, point[i][1], point[i][2]+j*540, point[i][3], p);
//			}
//		}
//		synchronized (context){
		for(int i = 0; i < lstBpm.size(); i++){
			List<Integer> list = lstBpm.get(i);
			for (int j = 0; j < list.size(); j++) {
//				boardCanvas.drawLine(540*3-i*540+j*10,180-list.get(j),540*3-i*540+j*10,180-list.get(j+1),p);
//				boardCanvas.drawLine(j,list.get(j).intValue(),(j+1),list.get(j+1).intValue(),p);
//				boardCanvas.drawLine(425-j,(Integer)point[j],425-(j+1),(Integer)point[j+1],p);
//				if(j==list.size()-1){
//					boardCanvas.drawLine(j*3, point[j], (j+1)*3, 120, p);
//				}else{
//					boardCanvas.drawLine(j*3, point[j], (j+1)*3, point[j+1], p);
//				}
				if(MainActivity.isFirst){
					if(j<list.size()-1){
//						boardCanvas.drawLine(1080-(j/count+1)*count+j%100*3,list.get(j).intValue(),1080-(j/count+1)*count+(j+1)%100*3,list.get(j+1).intValue(),p);
						boardCanvas.drawLine(1080-j*3,list.get(j).intValue(),1080-(j+1)*3,list.get(j+1).intValue(),p);
					}
				}else{
					if(j==list.size()-1){
						if(i==0){
							boardCanvas.drawLine(1080*(2-i)-j*3,list.get(j).intValue(),1080,lstBpm.get(1).get(0).intValue(),p);
//							boardCanvas.drawLine(1080*(2-i)-(j/count+1)*count+j%100*3,list.get(j).intValue(),1080,list.get(j+1).intValue(),p);
						}
					}else{
						boardCanvas.drawLine(1080*(2-i)-j*3,list.get(j).intValue(),1080*(2-i)-(j+1)*3,list.get(j+1).intValue(),p);
//						boardCanvas.drawLine(1080*(2-i)-(j/count+1)*count+j%100*3,list.get(j).intValue(),1080*(2-i)-(j/count+1)*count+(j+1)%100*3,list.get(j+1).intValue(),p);
					}
				}
//				System.out.println("***************"+list.get(j));
			}
		}
//		}
		System.out.println("***************yjx");
		new Thread(){
			public void run() {
				super.run();
				if(MainActivity.isFirst){
//					x = -3*540;
					x = -1080;
					MainActivity.isFirst=false;
				}else{
					x = -1080;
				}
//				MainActivity.isLoop = true;
				while (MainActivity.isMove) {
					canvas = sfh.lockCanvas();
			        Paint pg = new Paint(); 
			        pg.setColor(Color.RED);
			        pg.setAntiAlias(true);
					Paint pb = new Paint(); 
					pb.setColor(Color.WHITE);
					canvas.drawRect(0, 0, width, 240, pb);
					for (int i = 0; i < 20+1; i++) {
						if(i%5==0){
							pg.setStrokeWidth(3);
							if(i==10){
								pg.setStrokeWidth(2);
							}
						}else{
							pg.setStrokeWidth(2);
						}
						canvas.drawLine(0, i*12, width, i*12, pg);
					}
					for (int i = 0; i < width / 12; i++) {
						if(i%5==0){
							pg.setStrokeWidth(3);
						}else{
							pg.setStrokeWidth(2);
						}
						canvas.drawLine(i*12, 0, i*12, 240, pg);
					}
//			        Paint p = new Paint(); 
//			        p.setColor(Color.BLACK);
//			        p.setAntiAlias(true);
//					p.setStrokeWidth(3);
					System.out.println("............"+x);
					canvas.drawBitmap(board, x, 0, null);
					sfh.unlockCanvasAndPost(canvas);
	                if(x >= 0){
	                    x = -1080;
	                    MainActivity.isMove = false;
	                }else{
	                    x += 10;
	                }
	                try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
//				MainActivity.isLoop = false;
				MainActivity.isMove = true;
			}

		}.start();
		
	}
	
	//画波形
	public synchronized void drawWaveform1(final Context context, final SurfaceHolder sfh, final List<Integer> lstBpm) {
		final Resources resourse = context.getResources();
		DisplayMetrics dm = resourse.getDisplayMetrics();
		if(timer!=null){
			timer.cancel();
			mount = 0;
		}
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			width = (int) (dm.heightPixels*540.0/720);
			height = (int) (dm.widthPixels*240.0/1280);
		} else {
			width = (int) (dm.widthPixels*1080.0/1280);
			height = (int) (dm.heightPixels*240.0/720);
		}
		length = (int) (height/20.0);
//		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//			width = 540;
//			num = 22;
//		} else {
//			width = 1080;
//			num = 22;
//		}
		board = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);  
		boardCanvas = new Canvas(board);
        final Paint p = new Paint(); 
        p.setColor(resourse.getColor(R.color.sf_green_wave));
        p.setAntiAlias(true);
		p.setStrokeWidth(3);
		System.out.println("***************yjx");
		final long startTime = System.currentTimeMillis();
		new Thread(){
			private boolean isRunning = true;
			public void run() {
//				synchronized(context){
					int count = 3;
					int count1= 0;
//					while (MainActivity.isDraw && MainActivity.isLoop) {
					while (isRunning && MainActivity.isLoop) {
						canvas = sfh.lockCanvas();
						Paint pg = new Paint(); 
						pg.setColor(resourse.getColor(R.color.sf_green_grid));
						pg.setAntiAlias(true);
						
						Paint px = new Paint();
						px.setColor(resourse.getColor(R.color.sf_green_grid));
						px.setAntiAlias(true);
						px.setStrokeWidth(1);
						px.setStyle(Paint.Style.STROKE);
						px.setPathEffect(new DashPathEffect(new float[]{5,5,5,5},1));
						
						Paint pb = new Paint(); 
						pb.setColor(resourse.getColor(R.color.sf_black_bg));
						canvas.drawRect(0, 0, width, height, pb);
						for (int i = 0; i < 20+1; i++) {
							if(i%5==0){
								pg.setStrokeWidth(2);
//								if(i==10){
//									pg.setStrokeWidth(2);
//								}
								canvas.drawLine(0, i*length, width, i*length, pg);
							}else{
								pg.setStrokeWidth(2);
								canvas.drawLine(0, i*length, width, i*length, px);
							}
//							canvas.drawLine(0, i*length, width, i*length, pg);
						}
						for (int i = 0; i < width / length+1; i++) {
							if(i%5==0){
								pg.setStrokeWidth(2);
								canvas.drawLine(i*length, 0, i*length, height, pg);
							}else{
								pg.setStrokeWidth(2);
								canvas.drawLine(i*length, 0, i*length, height, px);
							}
//							canvas.drawLine(i*length, 0, i*length, height, pg);
						}
						if(count<lstBpm.size()){
							//						board = Bitmap.createBitmap(4*540, 240,Bitmap.Config.ARGB_8888);
							//						boardCanvas = new Canvas(board);
							for(int i = count1; i < count; i++){
								boardCanvas.drawLine(i*(width/405.0f),lstBpm.get(i).intValue(),(i+1)*(width/405.0f),lstBpm.get(i+1).intValue(),p);
							}
							count1 = count;
							count += 3;
							canvas.drawBitmap(board, 0, 0, null);
						}else{
//							try {
//								Thread.sleep(0);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//							MainActivity.isDraw = false;
							isRunning = false;
							//						if(MainActivity.drawflag == Constance.DRAW_LST){
							//							MainActivity.drawflag = Constance.DRAW_LSTBUFFER;
							//						}else{
							//							MainActivity.drawflag = Constance.DRAW_LST;
							//						}
						}
						sfh.unlockCanvasAndPost(canvas);
						try {
							Thread.sleep(6);
//							Thread.sleep(0);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					MainActivity.isDraw = false;
			        final long endTime = System.currentTimeMillis();
					((Activity)context).runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(context, ""+(endTime - startTime), Toast.LENGTH_LONG).show();
						}
					});
					//				MainActivity.isMove = true;
//				}
			}
		}.start();
	}
	
	private static int dataNum = 0;
	//画波形
	public synchronized void drawWaveform2(final Context context, final SurfaceHolder sfh, final List<Integer> lstBpm) {
		final Resources resourse = context.getResources();
		DisplayMetrics dm = resourse.getDisplayMetrics();
		if(timer!=null){
			timer.cancel();
			mount = 0;
		}
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			width = (int) (dm.heightPixels*540.0/720);
			height = (int) (dm.widthPixels*240.0/1280);
		} else {
			width = (int) (dm.widthPixels*1080.0/1280);
			height = (int) (dm.heightPixels*240.0/720);
		}
		length = (int) (height/20.0);
		
		if(dataNum == 0){
			board = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);  
			boardCanvas = new Canvas(board);
		}
		dataNum += lstBpm.size();
		if(dataNum == 400){
			dataNum = 0;
		}
        final Paint p = new Paint(); 
        p.setColor(resourse.getColor(R.color.sf_green_wave));
        p.setAntiAlias(true);
		p.setStrokeWidth(3);
		System.out.println("***************yjx");
		new Thread(){
			public void run() {
//				synchronized(context){
					int count = 1;
					int count1= 0;
					while (MainActivity.isDraw && MainActivity.isLoop) {
						canvas = sfh.lockCanvas();
						Paint pg = new Paint(); 
						pg.setColor(resourse.getColor(R.color.sf_green_grid));
						pg.setAntiAlias(true);
						
						Paint px = new Paint();
						px.setColor(resourse.getColor(R.color.sf_green_grid));
						px.setAntiAlias(true);
						px.setStrokeWidth(1);
						px.setStyle(Paint.Style.STROKE);
						px.setPathEffect(new DashPathEffect(new float[]{5,5,5,5},1));
						
						Paint pb = new Paint(); 
						pb.setColor(resourse.getColor(R.color.sf_black_bg));
						canvas.drawRect(0, 0, width, height, pb);
						for (int i = 0; i < 20+1; i++) {
							if(i%5==0){
								pg.setStrokeWidth(2);
								canvas.drawLine(0, i*length, width, i*length, pg);
							}else{
								pg.setStrokeWidth(2);
								canvas.drawLine(0, i*length, width, i*length, px);
							}
						}
						for (int i = 0; i < width / length+1; i++) {
							if(i%5==0){
								pg.setStrokeWidth(2);
								canvas.drawLine(i*length, 0, i*length, height, pg);
							}else{
								pg.setStrokeWidth(2);
								canvas.drawLine(i*length, 0, i*length, height, px);
							}
						}
						for(int i = 0; i < lstBpm.size()-1; i++){
							boardCanvas.drawLine(i*(width/400.0f),lstBpm.get(i).intValue(),(i+1)*(width/400.0f),lstBpm.get(i+1).intValue(),p);
						}
						canvas.drawBitmap(board, 0, 0, null);
						MainActivity.isDraw = false;
						sfh.unlockCanvasAndPost(canvas);
					}
//				}
			}
		}.start();
	}
	
	private void drawGrids(Context context, final SurfaceHolder sfh){
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			width = 540;
		} else {
			width = 1080;
		}
		// 返回一个 Canvas 对象，这个 Canvas 对象，就是用来在 Surface 上画图使用的
		canvas = sfh.lockCanvas();
        Paint p = new Paint(); 
        p.setColor(Color.RED);
        p.setAntiAlias(true);
		p.setStrokeWidth(2);
		Paint bp = new Paint(); 
		bp.setColor(Color.WHITE);
		canvas.drawRect(0, 0, width, 240, bp);
		for (int i = 0; i < 20+1; i++) {
			if(i%5==0){
				p.setStrokeWidth(3);
			}else{
				p.setStrokeWidth(2);
			}
			canvas.drawLine(0, i*12, width, i*12, p);
		}
		for (int i = 0; i < width / 12; i++) {
			if(i%5==0){
				p.setStrokeWidth(3);
			}else{
				p.setStrokeWidth(2);
			}
			canvas.drawLine(i*12, 0, i*12, 240, p);
		}
		// 结束 surface 的当前编辑，并将结果显示在屏幕上
		sfh.unlockCanvasAndPost(canvas);
	}
}
