package com.cardiograph.control;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import com.cardiograph.adapter.UserManagerAdapter;
import com.cardiograph.constance.Constance;
import com.cardiograph.model.User;
import com.cardiograph.service.UartService;
import com.cardiograph.thread.FileUpLoad;
import com.cardiograph.util.FileUtils;
import com.cardiograph.view.DeviceListActivity;
import com.cardiograph.view.HelpActivity;
import com.cardiograph.view.HistoryActivity;
import com.cardiograph.view.MainActivity;
import com.cardiograph.view.UpdateActivity;
import com.cardiograph.view.UserManagerActivity;
import com.example.cardiograph.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class UserOnClickListener implements OnClickListener {
	public static final String TAG = "nRFUART";
	private Context context;
	private TextView tvStart, tvInfo,tvBPM,tvConnect;
	private ImageView ivPower,ivSound,ivInfo;
	private Canvas canvas = null;
	private SurfaceView sfv;
	private SurfaceHolder sfh;
	private boolean isPower = true, isStart = false;
    private UartService mService = null;
    private BluetoothDevice mDevice = null;
	private BluetoothAdapter mBtAdapter = null;
	private ImageView ibStart;
	private Button btnBLE;
	private List<User> lstUser;
	private UserManagerAdapter userManagerAdapter;
	private Timer timer;
	private int type = 0;
	private List<File> lstFile = new ArrayList<File>();
	private List<Integer> addFile = new ArrayList<Integer>();

	public UserOnClickListener(Context context) {
		super();
		this.context = context;
	}

	public UserOnClickListener(Context context, TextView tvInfo) {
		super();
		this.context = context;
		this.tvInfo = tvInfo;
	}

	public UserOnClickListener(Context context, ImageView ibStart, TextView tvStart, ImageView ivPower, TextView tvBPM, SurfaceView sfv, SurfaceHolder sfh) {
		super();
		this.context = context;
		this.ibStart = ibStart;
		this.tvStart = tvStart;
		this.ivPower = ivPower;
		this.tvBPM = tvBPM;
		this.sfv = sfv;
		this.sfh = sfh;
	}

	public UserOnClickListener(Context context, List<User> lstUser, UserManagerAdapter userManagerAdapter) {
		super();
		this.context = context;
		this.lstUser = lstUser;
		this.userManagerAdapter = userManagerAdapter;
	}

	public UserOnClickListener(Context context, TextView tvInfo, TextView tvBPM,ImageView ivInfo) {
		super();
		this.context = context;
		this.tvInfo = tvInfo;
		this.tvBPM = tvBPM;
		this.ivInfo = ivInfo;
	}

	public UserOnClickListener(Context context, TextView tvBPM,SurfaceView sfv,SurfaceHolder sfh) {
		super();
		this.context = context;
		this.tvBPM = tvBPM;
		this.sfv = sfv;
		this.sfh = sfh;
	}
	
	public UserOnClickListener(Context context, BluetoothAdapter mBtAdapter, TextView tvConnect) {
		super();
		this.context = context;
		this.mBtAdapter = mBtAdapter;
		this.tvConnect = tvConnect;
	}

	public UserOnClickListener(Context context, BluetoothAdapter mBtAdapter, Button btnBLE) {
		super();
		this.context = context;
		this.mBtAdapter = mBtAdapter;
		this.btnBLE = btnBLE;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llStart:
			if(tvStart.getText().equals("开始")){
				ibStart.setImageResource(R.drawable.ic_media_pause);
				tvStart.setText(R.string.pause);
				isStart = false;
			}else{
				ibStart.setImageResource(R.drawable.ic_media_play);
				tvStart.setText(R.string.start);
				isStart = true;
			}
			if(isPower){
				ivPower.setImageResource(R.drawable.led_on);
				isPower = false;
			}else{
				ivPower.setImageResource(R.drawable.led_off);
				isPower = true;
			}
			if(MainActivity.isLoop){
				((MainActivity)context).getLst().clear();
				((MainActivity)context).getLstData().clear();
				MainActivity.isLoop = false;
				MainActivity.isDraw = false;
			}else{
				MainActivity.isLoop = true;
			}
			MainActivity.txValueIndex = 0;
			break;
		case R.id.llProfile:
			MainActivity.mService.disconnect();
			((MainActivity)context).getLst().clear();
			((MainActivity)context).getLstData().clear();
			MainActivity.isLoop = false;
			MainActivity.isDraw = false;
			Intent ProfileIntent = new Intent(context, UserManagerActivity.class);
			ProfileIntent.putExtra("manager","");
			ProfileIntent.putExtra("id", "");
			context.startActivity(ProfileIntent);
			((Activity)context).overridePendingTransition(R.anim.up_to_down_in, R.anim.up_to_down_out);
			break;
		case R.id.ibHelp:
			MainActivity.isLoop = false;
			MainActivity.isDraw = false;
			Intent helpIntent = new Intent(context, HelpActivity.class);
			helpIntent.putExtra("manager","");
			helpIntent.putExtra("id", "");
			context.startActivity(helpIntent);
			((Activity)context).overridePendingTransition(R.anim.left_to_right_in, R.anim.left_to_right_out);
			break;
		case R.id.ibInfo:
			if(tvInfo.getVisibility()==View.INVISIBLE){
				tvInfo.setVisibility(View.VISIBLE);
				tvBPM.setVisibility(View.INVISIBLE);
				ivInfo.setImageResource(R.drawable.led_on);
			}else{
				tvInfo.setVisibility(View.INVISIBLE);
				tvBPM.setVisibility(View.VISIBLE);
				ivInfo.setImageResource(R.drawable.led_off);
			}
			break;
		case R.id.ibHistory:
			MainActivity.isLoop = false;
			MainActivity.isDraw = false;
			Intent historyIntent = new Intent(context, HistoryActivity.class);
			historyIntent.putExtra("manager","");
			historyIntent.putExtra("id", "");
			context.startActivity(historyIntent);
			((Activity)context).overridePendingTransition(R.anim.right_to_left_in, R.anim.right_to_left_out);
			break;
		case R.id.ibManual:
			manualDrawWaveform();
			break;
		case R.id.ibAutomatic:
			int bpm = (int)(Math.random()*3)+69;
			tvBPM.setText(bpm+"bpm");
//			drawGrids();
//			drawGrids();
			drawWaveform();
			break;
		case R.id.llConnect:
		case R.id.btnBLE:
//			Intent bleIntent = new Intent(context, BLEActivity.class);
//			context.startActivity(bleIntent);
            if (!mBtAdapter.isEnabled()) {
                Log.i(TAG, "onClick - BT not enabled yet");
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ((Activity)context).startActivityForResult(enableIntent, Constance.REQUEST_ENABLE_BT);
            }
            else {
            	if (tvConnect.getText().equals("连接")){
            		
            		//Connect button pressed, open DeviceListActivity class, with popup windows that scan for devices
            		MainActivity.isLoop = true;
//            		MainActivity.isMove = true;
        			Intent newIntent = new Intent(context, DeviceListActivity.class);
        			((Activity)context).startActivityForResult(newIntent, Constance.REQUEST_SELECT_DEVICE);
    			} else {
    				//Disconnect button pressed
    				if (MainActivity.mDevice!=null)
    				{
    					MainActivity.mService.disconnect();
    					((MainActivity)context).getLst().clear();
    					((MainActivity)context).getLstData().clear();
    					MainActivity.isLoop = false;
    					MainActivity.isDraw = false;
    					
    				}
    			}
            }
            MainActivity.txValueIndex = 0;
			break;
		case R.id.btnAddUser:
			addUser();
			break;
		case R.id.llUpload:
			upload();
			break;
		case R.id.ivSetting:
			context.startActivity(new Intent(context, UpdateActivity.class));
			break;
		default:
			break;
		}

	}

	private void addUser() {
		final EditText userName = new EditText(context);
		userName.setHint("请输入姓名");
		new AlertDialog.Builder(context)
			.setTitle("添加用户")
			.setIcon(android.R.drawable.ic_menu_edit)
			.setView(userName)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String name = userName.getText().toString();
					if(name.equals("")){
						Toast.makeText(context, "姓名不能为空", Toast.LENGTH_SHORT);
					}else{
		              	User user = new User(context, "1111"+(int)(Math.random()*100), "222222", name);
		              	boolean addResult = user.add();
		     			if(addResult){
		     				Toast.makeText(context, "添加用户成功！", Toast.LENGTH_SHORT).show();
		     			}else{
		     				Toast.makeText(context, "添加用户失败！", Toast.LENGTH_SHORT).show();
		     			}
						lstUser.add(new User(context, name));
						userManagerAdapter.notifyDataSetChanged();
						dialog.dismiss();
					}
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			})
			.show();
	}

	private void manualDrawWaveform() {
		final EditText view = new EditText(context);
		view.setHint("请输入心率");
//			view.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		new AlertDialog.Builder(context)
			.setTitle("手动输入")
			.setIcon(android.R.drawable.ic_menu_edit)
			.setView(view)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if(view.getText().toString().equals("")){
						tvBPM.setText("0bpm");
					}else{
						tvBPM.setText(view.getText().toString().trim()+"bpm");
					}
//					drawGrids();
					drawWaveform();
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			})
			.show();
	}
	
	private List<List<Integer>> getWaveform(){
		List<List<Integer>> lstWaveform = new ArrayList<List<Integer>>();
		for (int i = 0; i < 22; i++) {
			List<Integer> lstPoint = new ArrayList<Integer>();	
		}
		return lstWaveform;
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
	Bitmap board = null;  
	Canvas boardCanvas = null; 
	private int mount = 0;
	private int num = 0;
    /**
     * 波形位置起始坐标
     */
    private float x = 0;
    private int width = 0;
/*	private float curX = 100;
	private float curY = 13;
	private float nextX = 0;
	private float nextY = 13;
	final Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what == 0x123)
			{
				if (nextX > 540)
				{
					curX = nextX = 0;
				}
				else
				{
					nextX += 10;
				}
//				nextY = curY + (float) (Math.random() * 10 - 5);
				TranslateAnimation anim = new TranslateAnimation(
					curX, nextX, curY, nextY);
				curX = nextX;
				curY = nextY;
				anim.setDuration(500);
				// 开始位移动画
				sfv.startAnimation(anim); 
			}
		}
	};*/
	
	
	private void drawWaveform(){
//		drawGrids();
//		System.out.println(sfv.getX()+"  "+sfv.getY());
		if(MainActivity.isLoop){
			MainActivity.isMove = false;
		}
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
//		timer = new Timer();
//		TimerTask task = new TimerTask() {
//			public void run() {
//				drawGrids();
//				//返回一个 Canvas 对象，这个 Canvas 对象，就是用来在 Surface 上画图使用的
//				//(注意：sfh.lockCanvas() 执行之后，在没有执行 sfh.unlockCanvasAndPost(canvas) 之前，不能执行 sfh.lockCanvas()，否则会报错)
//				canvas = sfh.lockCanvas();
//		        //创建画笔  
//		        Paint p = new Paint(); 
//		        //设置画笔为红色  
//		        p.setColor(Color.BLACK);
//		        //设置画笔的锯齿效果。 true 是去除锯齿
//		        p.setAntiAlias(true);
//				//设置画笔宽度
//				p.setStrokeWidth(3);
//				boardCanvas.drawLine(point[mount][0], point[mount][1], point[mount][2], point[mount][3], p);
//				canvas.drawBitmap(board, -width, 0, null);
///*				canvas.drawLine(0, 180, 100, 180, p);
//				canvas.drawLine(100, 180, 110, 160, p);
//				canvas.drawLine(110, 160, 128, 140, p);
//				canvas.drawLine(128, 140, 140, 160, p);
//				canvas.drawLine(140, 160, 145, 180, p);
//				canvas.drawLine(145, 180, 185, 180, p);
//				canvas.drawLine(185, 180, 190, 200, p);
//				canvas.drawLine(190, 200, 210, 20, p);
//				canvas.drawLine(210, 20, 225, 230, p);
//				canvas.drawLine(225, 230, 232, 180, p);
//				canvas.drawLine(232, 180, 300, 180, p);
//				canvas.drawLine(300, 180, 310, 160, p);
//				canvas.drawLine(310, 160, 320, 150, p);
//				canvas.drawLine(320, 150, 337, 140, p);
//				canvas.drawLine(337, 140, 350, 133, p);
//				canvas.drawLine(350, 133, 373, 140, p);
//				canvas.drawLine(373, 140, 380, 150, p);
//				canvas.drawLine(380, 150, 390, 180, p);
//				canvas.drawLine(390, 180, 421, 180, p);
//				canvas.drawLine(421, 180, 430, 170, p);
//				canvas.drawLine(430, 170, 440, 180, p);
//				canvas.drawLine(440, 180, 550, 180, p);*/
//		//        Path path = new Path();  
//		//        path.moveTo(0, 50);// 此点为多边形的起点  
//		//        path.lineTo(100, 50);  
//		//        path.lineTo(110, 60);  
//		//        path.lineTo(120, 20);  
//		//        path.lineTo(130, 80);  
//		//        path.lineTo(140, 50);  
//		//        path.lineTo(190, 50);   
//		//        path.close(); // 使这些点构成封闭的多边形  
//		//        canvas.drawPath(path, p);
//				//结束 surface 的当前编辑，并将结果显示在屏幕上
//				sfh.unlockCanvasAndPost(canvas);
////				handler.sendEmptyMessage(0x123);
//				if(mount<num-1){
//					mount++;
//				}else{
//					timer.cancel();
//					mount = 0;
//				}
//		       }
//		};
//		timer.schedule(task, 0 , 0);
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < point.length; i++) {
//	            drawGrids();
//				canvas = sfh.lockCanvas();
		        Paint p = new Paint(); 
		        p.setColor(Color.BLACK);
		        p.setAntiAlias(true);
				p.setStrokeWidth(3);
				boardCanvas.drawLine(point[i][0]+j*540, point[i][1], point[i][2]+j*540, point[i][3], p);
//				canvas.drawBitmap(board, -2*540, 0, null);
//				sfh.unlockCanvasAndPost(canvas);
			}
		}
		new Thread(){
			public void run() {
				super.run();
				x = -4*540;
				MainActivity.isLoop = true;
				while (MainActivity.isMove) {
//					drawGrids();
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
			        Paint p = new Paint(); 
			        p.setColor(Color.BLACK);
			        p.setAntiAlias(true);
					p.setStrokeWidth(3);
					canvas.drawBitmap(board, x, 0, null);
					sfh.unlockCanvasAndPost(canvas);
	                if(x > 0){
	                    x = -2*540;
//	        			for (int i = 0; i < point.length; i++) {
//	        		        Paint p1 = new Paint(); 
//	        		        p1.setColor(Color.BLACK);
//	        		        p1.setAntiAlias(true);
//	        				p1.setStrokeWidth(3);
//	        				boardCanvas.drawLine(point[i][0], point[i][1], point[i][2], point[i][3], p);
//	        			}
	                }else{
	                    x += 10;
	                }
	                try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				MainActivity.isLoop = false;
				MainActivity.isMove = true;
			}

		}.start();
		
	}
	private void drawBackground(){
		Canvas canvas = null;
		// 返回一个 Canvas 对象，这个 Canvas 对象，就是用来在 Surface 上画图使用的
		canvas = sfh.lockCanvas();
		// 获取 res 目录下的 Bitmap 对象
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.history_paper_right);
		//创建一个 Matrix 对象
		Matrix matrix = new Matrix();
		//设置缩放比例
		matrix.setScale((float)sfv.getWidth() / bitmap.getWidth(), (float)sfv.getHeight() / bitmap.getHeight());
		//得到缩放图片(方法一)
		Bitmap bitmapScale = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		
		//得到缩放图片(方法二)
//		Bitmap bitmapScale = Bitmap.createScaledBitmap(bitmap, sfv.getWidth(), sfv.getHeight(), true);
		
		//画图片
		canvas.drawBitmap(bitmapScale, 0, 0, new Paint());
		// 结束 surface 的当前编辑，并将结果显示在屏幕上
		sfh.unlockCanvasAndPost(canvas);
	}
	private void drawGrids(){
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
	
	private void upload() {
		final List<Map<String, Object>> lstData = FileUtils.findFile(new File("/mnt/sdcard/data/"));
//		final List<Map<String, Object>> lstData = FileUtils.findFile(new File("/mnt/sdcard/"));
		View viewUpLoad = LayoutInflater.from(context).inflate(R.layout.upload_data_layout, null);
		Spinner spinnerType=(Spinner) viewUpLoad.findViewById(R.id.spinnerType);
		ListView lvData=(ListView) viewUpLoad.findViewById(R.id.lvData);
		SimpleAdapter fileAdapter=new SimpleAdapter(context,lstData,R.layout.upload_data_item_layout,new String[]{"photo","name","cbk"},
		             new int[]{R.id.filePhoto,R.id.fileName,R.id.cbx}){
			public View getView(final int position,View convertView, ViewGroup parent) {
				if(convertView==null){
					convertView = LayoutInflater.from(context).inflate(R.layout.upload_data_item_layout, null);

				}
				CheckBox cbx = (CheckBox) convertView.findViewById(R.id.cbx);
				cbx.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked){
							addFile.add(position);
						}
					}
				});
				return super.getView(position, convertView, parent);
			}
		};
		lvData.setAdapter(fileAdapter);
		setTypeSpinner(spinnerType);
		new AlertDialog.Builder(context)
			.setTitle("请选择文件")
			.setIcon(android.R.drawable.btn_star_big_on)
			.setView(viewUpLoad)
			.setPositiveButton("上传",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					for(int i=0 ; i<addFile.size();i++){
						String fileName = lstData.get(addFile.get(i)).get("name").toString();
						lstFile.add(new File("/mnt/sdcard/data",fileName));
//						lstFile.add(new File("/mnt/sdcard",fileName));
					}
					FileUpLoad fileUpLoad = new FileUpLoad(context, lstFile);
					fileUpLoad.start();
				}
			})
			.setNegativeButton("取消",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.show();
	}
	
	private void setTypeSpinner(Spinner spinnerType) {
//		DBUtil.context = context;
//		SQLiteDatabase sdb = DBUtil.getInstance().getSQLiteDatabase();	
//		Cursor c = sdb.rawQuery("select datadictid,name from t_pf_datadict where type=10", null);
//		if(c != null && c.moveToFirst())
//		{
//			do {
//				int id = c.getInt(c.getColumnIndex("DATADICTID"));
//				String name = c.getString(c.getColumnIndex("NAME"));
//				lstType.add(name);
//			} while (c.moveToNext());	
//		}
		List<String> lstType = new ArrayList<String>();
		lstType.add("心跳");
		lstType.add("呼吸");
		final ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,lstType);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerType.setAdapter(typeAdapter);
		spinnerType.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(typeAdapter.getItem(position).equals("心跳")){
					type = 1;
				}else if(typeAdapter.getItem(position).equals("呼吸")){
					type = 2;
				}else {
					type = 0;
				}
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}
	
}
