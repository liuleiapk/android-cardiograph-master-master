package com.cardiograph.view;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cardiograph.constance.Constance;
import com.cardiograph.control.UserOnClickListener;
import com.cardiograph.service.UartService;
import com.cardiograph.util.Tools;
import com.example.cardiograph.R;
import com.qihoo.linker.logcollector.LogCollector;
import com.cardiograph.ecg.*;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelUuid;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/************************************************************
 *  内容摘要	：心电主界面，实现蓝牙连接功能
 *
 *  作者	：叶金新
 *  创建时间	：2014-12-21 下午4:50:01 
 *  当前版本号：v1.0
 *  历史记录	:
 *  	日期	: 2014-12-21 下午4:50:01 	修改人：
 *  	描述	:
 ************************************************************/
/************************************************************
 *  内容摘要	：<p>
 *
 *  作者	：叶金新
 *  创建时间	：2014-12-21 下午4:54:18 
 *  当前版本号：v1.0
 *  历史记录	:
 *  	日期	: 2014-12-21 下午4:54:18 	修改人：
 *  	描述	:
 ************************************************************/
public class MainActivity extends Activity {
	private LinearLayout llStart,llProfile,llConnect,llUpload;
	private ImageView ibStart,ibProfile,ibHelp,ibInfo,ibHistory,
	ibConnect,ibManual,ibAutomatic,ibUpload,ibDisconnect;
	private Button btnBLE;
	private TextView tvStart,tvUserName,tvSex,tvAge,tvSymptom,
	tvDateTime,tvInfo,tvBPM,tvConnect;
	private ImageView ivBattery,ivPower,ivSound,ivInfo, ivSetting;
	private SurfaceView sfv;
	private SurfaceHolder sfh;
	private AudioManager mAudioManager;
	private Handler handler;
	private BroadcastReceiver batteryReceiver;
    /**
     *是否第一次画心电图
     */
    public static boolean isFirst = true;
    /**
     *是否移动
     */
    public static boolean isMove = false;
    /**
     * 线程控制
     */
    public static boolean isLoop = false;
    public static boolean isShow = true;
    public static boolean isLstRec = true;
    public static volatile boolean isDraw = false;
    public static int drawflag = Constance.DRAW_LST;
    public boolean isAdd = false;
    private ProgressDialog pd;
    private List<List<Integer>> lstBpm = new ArrayList<List<Integer>>();
    private List<Integer> lst = new ArrayList<Integer>();
    private List<Integer> lstData = new ArrayList<Integer>();
    private List<Integer> lstBuffer = new ArrayList<Integer>();
    private List<Integer> lstMax = new ArrayList<Integer>();
    private List<Integer> lstCalBpm = new ArrayList<Integer>();
    private int[] point = new int[360];
    private int width = 0, height = 0,length = 0;
    private int count = 0, min = 0, max = 0,avg = 0, bpm = 0, maxData = 0, index = 0; 
    private int frontMax = 0,frontMin = 0;
    private double t=0;
    //每屏画数据的个数
    public static int dataNum = 406;
    public static String dataDateTime;
/*    static{
    	System.loadLibrary("JNI_Interface");
    }*/
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        //初始化过滤对象
//		try {
//			filter = new MatlabFilter();
//		} catch (MWException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}  
        initData();
		initComponent();
		initView();
		service_init();
		registerListener();
        LogCollector.setDebugMode(true);
        LogCollector.init(this, "http://121.41.41.54:8801/", null);
	}

	private void initData() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//"yyyy-MM-dd HH:mm:ss" 
		dataDateTime = format.format(new Date());
//		dataDateTime = new Date().getTime()/1000+"";
        File file = new File(Environment.getExternalStorageDirectory() + "/data");
        // 判断文件目录是否存在
        if (!file.exists()) {
            file.mkdir();
        }
    }

	/**
	 * 
	 *  函数名称 : initComponent
	 *  功能描述 : 初始化控件
	 *  参数及返回值说明：
	 *
	 *  修改记录：
	 *  	日期 ：2014-12-21 下午4:54:36	修改人：yjx
	 *  	描述	：
	 *
	 */
	private void initComponent(){
		llStart = (LinearLayout) findViewById(R.id.llStart);
		llProfile = (LinearLayout) findViewById(R.id.llProfile);
		llConnect = (LinearLayout) findViewById(R.id.llConnect);
		llUpload = (LinearLayout) findViewById(R.id.llUpload);
		tvStart = (TextView) findViewById(R.id.tvStart);
		ibStart = (ImageView) findViewById(R.id.ibStart);
		ibProfile = (ImageView) findViewById(R.id.ibProfile);
//		ibHelp = (ImageButton) findViewById(R.id.ibHelp);
//		ibInfo = (ImageButton) findViewById(R.id.ibInfo);
//		ibHistory = (ImageButton) findViewById(R.id.ibHistory);
		ibConnect = (ImageView) findViewById(R.id.ibConnect);
		ibUpload = (ImageView) findViewById(R.id.ibUpload);
//		ibDisconnect = (ImageButton) findViewById(R.id.ibDisconnect);
		tvConnect = (TextView) findViewById(R.id.tvConnect);
//		ibManual = (ImageButton) findViewById(R.id.ibManual);
//		ibAutomatic = (ImageButton) findViewById(R.id.ibAutomatic);
		btnBLE = (Button) findViewById(R.id.btnBLE);
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvSex = (TextView) findViewById(R.id.tvSex);
		tvAge = (TextView) findViewById(R.id.tvAge);
		tvSymptom = (TextView) findViewById(R.id.tvSymptom);
		tvDateTime = (TextView) findViewById(R.id.tvDateTime);
		tvInfo = (TextView) findViewById(R.id.tvInfo);
		tvBPM = (TextView) findViewById(R.id.tvBPM);
		ivBattery = (ImageView) findViewById(R.id.ivBattery);
		ivPower = (ImageView) findViewById(R.id.ivPower);
		ivSound = (ImageView) findViewById(R.id.ivSound);
		ivInfo = (ImageView) findViewById(R.id.ivInfo);
		ivSetting = (ImageView) findViewById(R.id.ivSetting);
		sfv = (SurfaceView) findViewById(R.id.sfv);
		//SurfaceHolder：显示一个surface的抽象接口，使你可以控制surface的大小和格式， 以及在surface上编辑像素，和监视surace的改变。这个接口通常通过SurfaceView类实现。
		sfh = sfv.getHolder();
		// SurfaceHolder 的回调接口
		sfh.addCallback(new Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub

			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
//				drawBackground();
//				drawBackground();
				drawGrids();
			}
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// TODO Auto-generated method stub

			}
		});
		
		tvUserName.setText("姓名： "+getIntent().getStringExtra("userName"));
		tvSex.setText("性别： 男");
		tvAge.setText("年龄： 22岁");
		tvSymptom.setText("症状： 正常");
    }

	private void initView() {
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case Constance.MESSAGE_DATETIME_AUDIO:
					tvDateTime.setText(msg.obj.toString());
//					int current = mAudioManager.getStreamVolume( AudioManager.STREAM_SYSTEM );
					if(msg.arg1>0){
						ivSound.setImageResource(R.drawable.led_on);
					}else{
						ivSound.setImageResource(R.drawable.led_off);
					}
					break;
				case Constance.MESSAGE_CALCULATE_BPM:
					if (bpm < 0) {
						bpm = 0;
					}
					tvBPM.setText(bpm + "bpm");
					break;
				}
			}
		};
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				while(true){
					SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//"yyyy-MM-dd  HH:mm:ss"
					String dateTime=format.format(new Date());
					int current = mAudioManager.getStreamVolume( AudioManager.STREAM_SYSTEM );
					Message msg =new Message();
					msg.obj = dateTime;
					msg.arg1 = current;
					msg.what = Constance.MESSAGE_DATETIME_AUDIO;
					handler.sendMessage(msg);
				}
			}
		}.start();
		
		batteryReceiver=new BroadcastReceiver(){  
		 
		        @Override 
		        public void onReceive(Context context, Intent intent) {  
		        	//  level加%就是当前电量了  
		            int level = intent.getIntExtra("level", 0); 
//		            Toast.makeText(MainActivity.this, level+"%", Toast.LENGTH_LONG);
		            if(level>=0&&level<25){
		            	ivBattery.setBackgroundResource(R.drawable.battery1);
		            }else if(level>=25&&level<50){
		            	ivBattery.setBackgroundResource(R.drawable.battery2);
		            }else if(level>=50&&level<75){
		            	ivBattery.setBackgroundResource(R.drawable.battery3);	
		            }else{
		            	ivBattery.setBackgroundResource(R.drawable.battery4);
		            }
		            System.out.println(level+"%");
		        }  
		};  
		registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}
	
	private void registerListener() {
		llStart.setOnClickListener(new UserOnClickListener(this,ibStart, tvStart, ivPower,tvBPM,sfv,sfh));
		llProfile.setOnClickListener(new UserOnClickListener(this));
//		ibHelp.setOnClickListener(new UserOnClickListener(this));
//		ibInfo.setOnClickListener(new UserOnClickListener(this,tvInfo,tvBPM,ivInfo));
//		ibHistory.setOnClickListener(new UserOnClickListener(this));
		llConnect.setOnClickListener(new UserOnClickListener(this, mBtAdapter, tvConnect));
//		ibManual.setOnClickListener(new UserOnClickListener(this,tvBPM,sfv,sfh));
//		ibAutomatic.setOnClickListener(new UserOnClickListener(this,tvBPM,sfv,sfh));
		btnBLE.setOnClickListener(new UserOnClickListener(this, mBtAdapter, btnBLE));
		llUpload.setOnClickListener(new UserOnClickListener(this));
//		ibDisconnect.setOnClickListener(new UserOnClickListener(this));
		ivSetting.setOnClickListener(new UserOnClickListener(this));
	}
	
	
	public List<Integer> getLst() {
		return lst;
	}

	public List<Integer> getLstData() {
		return lstData;
	}
	
	public void setLst(List<Integer> lst) {
		this.lst = lst;
	}

	private void drawBackground(){
		Canvas canvas = null;
		// 返回一个 Canvas 对象，这个 Canvas 对象，就是用来在 Surface 上画图使用的
		canvas = sfh.lockCanvas();
		// 获取 res 目录下的 Bitmap 对象
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
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
//		int width = 0, height = 0,length = 0;
//		height = Tools.dip2px(120, this);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		System.out.println(dm.widthPixels+"--"+dm.heightPixels+"--"+dm.density);
		System.out.println(Environment.getExternalStorageDirectory());
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			width = (int) (dm.heightPixels*540.0/720);
			height = (int) (dm.widthPixels*240.0/1280);
		} else {
			width = (int) (dm.widthPixels*1080.0/1280);
			height = (int) (dm.heightPixels*240.0/720);
		}
		length = (int) (height/20.0);
		Canvas canvas = null;
		// 返回一个 Canvas 对象，这个 Canvas 对象，就是用来在 Surface 上画图使用的
		canvas = sfh.lockCanvas();
		Paint p = new Paint();
		p.setColor(getResources().getColor(R.color.sf_green_grid));
		p.setAntiAlias(true);
		p.setStrokeWidth(2);
		
		Paint px = new Paint();
		px.setColor(getResources().getColor(R.color.sf_green_grid));
		px.setAntiAlias(true);
		px.setStrokeWidth(1);
		px.setStyle(Paint.Style.STROKE);
		px.setPathEffect(new DashPathEffect(new float[]{5,5,5,5},1));
		
		Paint bp = new Paint();
		bp.setColor(getResources().getColor(R.color.sf_black_bg));
		canvas.drawRect(0, 0, width, height, bp);

		// 画横线
		for (int i = 0; i < 20+1; i++) {
			if(i%5==0){
				p.setStrokeWidth(2);
				canvas.drawLine(0, i*length, width, i*length, p);
			}else{
				p.setStrokeWidth(2);
				canvas.drawLine(0, i*length, width, i*length, px);
			}
//			canvas.drawLine(0, i*length, width, i*length, p);
		}
		// 画竖线
		for (int i = 0; i < width / length+1; i++) {
			if(i%5==0){
				p.setStrokeWidth(2);
				canvas.drawLine(i*length, 0, i*length, height, p);
			}else{
				p.setStrokeWidth(2);
				canvas.drawLine(i*length, 0, i*length, height, px);
			}
//			canvas.drawLine(i*length, 0, i*length, height, p);
		}
		// 结束 surface 的当前编辑，并将结果显示在屏幕上
		sfh.unlockCanvasAndPost(canvas);
	}
	
	public static final String TAG = "nRFUART";
    public static UartService mService = null;
    public static BluetoothDevice mDevice = null;
    private BluetoothAdapter mBtAdapter = null;
    private int mState = UART_PROFILE_DISCONNECTED;
    private static final int UART_PROFILE_CONNECTED = 20;
    private static final int UART_PROFILE_DISCONNECTED = 21;
    //UART service connected/disconnected
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
        	    isLoop = true;
        		mService = ((UartService.LocalBinder) rawBinder).getService();
        		Log.d(TAG, "onServiceConnected mService= " + mService);
        		if (!mService.initialize()) {
                    Log.e(TAG, "Unable to initialize Bluetooth");
                    finish();
                }

        }

        public void onServiceDisconnected(ComponentName classname) {
       ////     mService.disconnect(mDevice);
        		mService = null;
        }
    };
    
    //服务初始化
    private void service_init(){
        Intent bindIntent = new Intent(this, UartService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
  
        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }
    
    //广播接收器
    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            final Intent mIntent = intent;
           //*********************//
            if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
            	 runOnUiThread(new Runnable() {
                     public void run() {
                         	String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                             Log.d(TAG, "UART_CONNECT_MSG");
                             tvConnect.setText(R.string.ble_disconnect);
                             tvStart.setText(R.string.pause);
                             mState = UART_PROFILE_CONNECTED;
                     		 SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//"yyyy-MM-dd HH:mm:ss" 
                    		 dataDateTime = format.format(new Date());
//                    		 getUUID();
                     }
            	 });
            }
           
          //*********************//
            if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
            	 runOnUiThread(new Runnable() {
                     public void run() {
                    	 	 String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                             Log.d(TAG, "UART_DISCONNECT_MSG");
                             tvConnect.setText(R.string.ble_connect);
                             tvStart.setText(R.string.start);
                             mState = UART_PROFILE_DISCONNECTED;
                             mService.close();
                            //setUiState();
                         
                     }
                 });
            }
            
          
          //*********************//
            if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
             	 mService.enableTXNotification();
            }
          //*********************//
            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {
                 final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
                 runOnUiThread(new Runnable() {
                     public void run() {
                         try {
                 			if (isShow) {
                 				ibStart.setImageResource(R.drawable.ic_media_pause);
                				pd = new ProgressDialog(MainActivity.this);
                				pd.setIcon(android.R.drawable.ic_dialog_email);
                				pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                				pd.setMax(100);
                				pd.setMessage("读取数据中，请稍等......");
                				pd.setCancelable(true);
                				pd.show();
                				isShow = false;
                			}
							if (isLoop) {
								synchronized (this) {
									BLEDataReceiveHandle1(txValue);
									if (lstBpm.size() > 0) {
										if (!isDraw) {
											isDraw = true;
											System.out.println("+++++++++++++++");
											//										Tools.getInstance().writer(Environment.getExternalStorageDirectory() + "/data/"+dataDateTime+"lstBpm.txt", lstBpm.get(index));
											Tools.getInstance().drawWaveform1(MainActivity.this, sfh, lstBpm.get(0));
											lstBpm.remove(0);
//											index++;
//											Tools.getInstance().drawWaveform2(MainActivity.this, sfh, lst);
//											lst.clear();
										}
										pd.cancel();
									}
								}
								
							}
//                 			if(isLstRec){
//                 				BLEDataReceiveHandle1(txValue);
//                 				if(lst.size() == 216){
//                 					isLstRec = false;
//                 				}
//                 			}else{
//                 				BLEDataReceiveHandleBuffer(txValue);
//                 				if(lstBuffer.size() == 216){
//                 					isLstRec = true;
//                 				}
//                 			}
//                        	 if(isFirst){
//              					if (lst.size() == 216 && lstBuffer.size() == 216 ) {
//            						if(isLoop){
//            							System.out.println("+++++++++++++++");
//            							Tools.getInstance().drawWaveform1(MainActivity.this,sfh, lst);
//            						}
//            						pd.cancel();
//            						lst = new ArrayList<Integer>();
//            						isFirst = false;
//            					}
//                        	 }
//                    		 if(!isDraw){
//                    			isDraw = true;
//                    			switch (drawflag) {
// 								case Constance.DRAW_LST:
// 									Tools.getInstance().drawWaveform1(MainActivity.this, sfh, lst);
// 									lst = new ArrayList<Integer>();
// 									break;
// 								case Constance.DRAW_LSTBUFFER:
// 									Tools.getInstance().drawWaveform1(MainActivity.this, sfh, lstBuffer);
// 									lstBuffer = new ArrayList<Integer>();
// 									break;
// 								}
//                    		 }
                         } catch (Exception e) {
                             Log.e(TAG, e.toString());
                         }
                     }

					
                 });

             }
           //*********************//
            if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART)){
            	showMessage("Device doesn't support UART. Disconnecting");
            	mService.disconnect();
            }
            
            
        }
    };
    
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }
    
    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

        case Constance.REQUEST_SELECT_DEVICE:
        	//When the DeviceListActivity return, with the selected device address
            if (resultCode == Activity.RESULT_OK && data != null) {
                String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);
                Log.d(TAG, "... onActivityResultdevice.address==" + mDevice + "mserviceValue" + mService);
                ((TextView) findViewById(R.id.deviceName)).setText(mDevice.getName()+ " - connecting");
                mService.connect(deviceAddress);

            }
            break;
        case Constance.REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Bluetooth has turned on ", Toast.LENGTH_SHORT).show();

            } else {
                // User did not enable Bluetooth or an error occurred
                Log.d(TAG, "BT not enabled");
                Toast.makeText(this, "Problem in BT Turning ON ", Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
        default:
            Log.e(TAG, "wrong request code");
            break;
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
//        isMove = true;
        index = 0;
        txValueIndex = 0;
        lstBpm.clear();
        if (!mBtAdapter.isEnabled()) {
            Log.i(TAG, "onResume - BT not enabled yet");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, Constance.REQUEST_ENABLE_BT);
        }
//        service_init();
 
    }
    
    @Override
    public void onBackPressed() {
        if (mState == UART_PROFILE_CONNECTED) {
			MainActivity.isLoop = false;
			MainActivity.isDraw = false;
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            showMessage("nRFUART's running in background.\n             Disconnect to exit");
            System.exit(0);
        }
        else {
            new AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(R.string.popup_title)
            .setMessage(R.string.popup_message)
            .setPositiveButton(R.string.popup_yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                    ActivityManager am = (ActivityManager)getSystemService (Context.ACTIVITY_SERVICE);
//                    am.restartPackage(getPackageName());
   	                finish();
                }
            })
            .setNegativeButton(R.string.popup_no, null)
            .show();
        }
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	isMove = false;
    }
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
		tvConnect.setText(R.string.ble_connect);
//        try {
//        	LocalBroadcastManager.getInstance(this).unregisterReceiver(UARTStatusChangeReceiver);
//        } catch (Exception ignore) {
//            Log.e(TAG, ignore.toString());
//        } 
//        unbindService(mServiceConnection);
//        mService.stopSelf();
//        mService= null;
    }
    @Override
    public void onDestroy() {
    	 super.onDestroy();
        Log.d(TAG, "onDestroy()");
        
        try {
        	LocalBroadcastManager.getInstance(this).unregisterReceiver(UARTStatusChangeReceiver);
        } catch (Exception ignore) {
            Log.e(TAG, ignore.toString());
        } 
        unbindService(mServiceConnection);
        mService.stopSelf();
        mService= null;
        unregisterReceiver(batteryReceiver);
        System.exit(0);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	if(event.getAction()==KeyEvent.ACTION_DOWN){
    		switch (keyCode) {
			case KeyEvent.KEYCODE_HOME:
				finish();
				break;

			default:
				break;
			}
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    //补码转为原码
	private int dataParser(int x) 
	{
	    int y = x >> 8 ;
	    if(x>=0){
	    	return (((x^y)-y)|(y<<8));
	    }else{
	    	return Math.abs((((x^y)-y)|(y<<8)));
	    }
	}
	
	private int frontNum = 0, num = 0;
	public static int txValueIndex = 0;
	private List<String> lstLoseData = new ArrayList<String>();
	//蓝牙数据接收处理
	private void BLEDataReceiveHandle1(byte[] txValue) {
//		synchronized (this) {
//			String data17 = Integer.toHexString(dataParser(txValue[17])).toUpperCase();
//			Log.d("yjx", "----------"+data17);
			byte[] value = {txValue[0]};
			mService.writeRXCharacteristic(value);//双向传输
			try {
				Tools.getInstance().writer(Environment.getExternalStorageDirectory() + "/data/"+dataDateTime+"package.txt", txValue);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
//			num = (dataParser(txValue[0])<< 8)+dataParser(txValue[1]);
//			Log.d("yjx", "当前收到的蓝牙数据包号： "+num);
//			lstLoseData.add("当前收到的蓝牙数据包号： "+num);
//			if(num - frontNum > 1 && frontNum > 0){
//				for (int i = frontNum+1; i < num; i++) {
//					Log.d("yjx", "丢掉的蓝牙数据包号： "+i);
//					lstLoseData.add("丢掉的蓝牙数据包号： "+i);
//					String message = Integer.toHexString(i).toUpperCase();
//					byte data0 = Byte.parseByte(message.substring(0, 2));
//					byte data1 = Byte.parseByte(message.substring(2));
//	            	byte[] value = {data0,data1};
//					try {
//					//send data to service
//	//				value = message.getBytes("UTF-8");
//					mService.writeRXCharacteristic(value);
//					return;
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//			frontNum = num;
//			if(!data17.equals("FA")){
////				return;
//			}
			int data = 0;
//
//		for (int i = 2; i < txValue.length; i++) {
//
//			data = dataParser(txValue[i]);
//			List<Integer> lsData = new ArrayList<Integer>();
//			lsData.add(data);
//			try {
//				Tools.getInstance().writer(Environment.getExternalStorageDirectory() + "/data/" + dataDateTime + "1byte.txt", lsData, true);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
			for (int i = 2; i < txValue.length; i++) {
				//2个字节，共20个字节
				if(i % 2 == 0){
					data = dataParser(txValue[i]);
//					if(data==0)data=255;
				}else{
					data = (data << 8) + dataParser(txValue[i]);
					if(data > 32767){
						data = data- 65535 - 1;
					}
////				}

//				if(txValueIndex % 2 == 0){
//					data = dataParser(txValue[i]);
//				}else{
//					data = (data << 8) + dataParser(txValue[i]);
//				}
//				txValueIndex++;

				//3个字节,共17个字节
//				if (i % 3 == 2) {
//					data = dataParser(txValue[i]);
//				} else {
//					data = (data << 8) + dataParser(txValue[i]);
//				}
//				if (i % 3 == 1) {

//					System.out.println("yjx&&" + data);
//					if(lstData.size() < dataNum){
//						if(data>frontMax+5000){
//							data = frontMin;
//						}
					lstData.add(data);
//					List<Integer> lsData = new ArrayList<Integer>();
//					lsData.add(data);
//					try {
//						Tools.getInstance().writer(Environment.getExternalStorageDirectory() + "/data/"+dataDateTime+"2byte.txt", lsData,true);
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					}

					if (lstData.size() == dataNum && lst.size() == 0) {
				//		lstData=C2ECGUtil.ECGdenoise(lstData);
						long sum = 0;
						min = lstData.get(0);
						for (int j = 0; j < lstData.size(); j++) {
							sum += lstData.get(j);
							min = Math.min(min, lstData.get(j));
							max = Math.max(max, lstData.get(j));
							frontMax = max;
							frontMin = min;
						}
						avg = (int) (sum / (dataNum*1.0));
						sum = 0;
						System.out.println("yjx--" + avg);
						for (int j = 0; j < lstData.size(); j++) {
							if (max - avg > avg - min) {
								data = (int) (height - height * 0.75 / (max - min)*(lstData.get(j) - min) - height/8);
							} else if (max == min && max == avg) {
								data = 180;
							} else {
								data = (int) (height * 0.75 / (max - min)*(lstData.get(j) - min) + height/4 - height/8);
							}
//							System.out.println("yjx$$" + data);
							lst.add(data);
							sum += data;
							maxData = Math.max(maxData, data);
						}
						avg = (int) (sum / (dataNum*1.0));
						max = 0;
//						new Thread(new Runnable() {
//							public void run() {
								try {
//									Tools.getInstance().writer("/mnt/sdcard/data.txt", lstData);
									Tools.getInstance().writerInt(Environment.getExternalStorageDirectory() + "/data/"+dataDateTime+".dat", lstData,true);
									Tools.getInstance().writer16(Environment.getExternalStorageDirectory() + "/data/"+dataDateTime+"data16.txt", lstData,true);
//									Tools.getInstance().writer(Environment.getExternalStorageDirectory() + "/data/"+dataDateTime+"lst.txt", lst,true);
//									Tools.getInstance().writerLoseData(Environment.getExternalStorageDirectory() + "/data/"+dataDateTime+"losedata.txt", lstLoseData);
								} catch (IOException e) {
									e.printStackTrace();
								}
//							}
//						}).start();
								
						lstBpm.add(lst);
						System.out.println("lstBpm.size()==" + lstBpm.size());
						lstCalBpm = lst;
						lst = new ArrayList<Integer>();
						lstData.clear();
//						lstLoseData.clear();
//						lstData = new ArrayList<Integer>();

//						new Thread(new Runnable() {
//							public void run() {
								for (int j = 1; j < lstCalBpm.size() - 1; j++) {
									if (avg-lstCalBpm.get(j) > height*0.30 && lstCalBpm.get(j) < lstCalBpm.get(j-1)
											&& lstCalBpm.get(j) < lstCalBpm.get(j + 1)) {
										lstMax.add(j);//添加波峰点的X轴坐标
									}
								}
								//计算一屏波形第一个波峰和最后一个波峰的X轴距离
								int sum1 = lstMax.get(lstMax.size()-1) - lstMax.get(0);

								//第一个波峰和最后一个波峰间隔的数据点除于完整波形的个数（2个波峰之间才有一个完整波形，所以减1）,等于一个波形所需的平均数据点      
								count = (int) (sum1 * 1.0 / (lstMax.size() - 1));
								//一个波形所需的数据个数除于采样频率等于一个波形产生所需的时间秒数
								t = count * 1.0/125;//125采样频率（每秒采集的数据个数）
								//心率等于1min所跳动的次数，也就是完整波形的个数
//								bpm = (int) (60.0 / t);
								bpm = (int)(100-(t*100+5)+60);
								lstMax = new ArrayList<Integer>();
								handler.sendEmptyMessage(Constance.MESSAGE_CALCULATE_BPM);
//							}
//						}).start();
						
					}
				}
			}
//		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_settings:
			break;
		case R.id.action_file_manager:
//			fileManager();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void getUUID(){
		try {
//			Method getUuidsMethod = BluetoothAdapter.class.getDeclaredMethod("getUuids", null);
//			ParcelUuid[] uuids = (ParcelUuid[]) getUuidsMethod.invoke(mBtAdapter, null);
			ParcelUuid[] uuids = mDevice.getUuids();
			for (ParcelUuid uuid: uuids) {
			    Log.d(TAG, "UUID: " + uuid.getUuid().toString());
			    Toast.makeText(this, "UUID: " + uuid.getUuid().toString(), Toast.LENGTH_LONG).show();
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
