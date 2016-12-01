package com.cardiograph.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.cardiograph.component.BtnWithTopLine;
import com.cardiograph.component.MyProgressDialog;
import com.cardiograph.component.NavigationBar;
import com.cardiograph.constance.Constance;
import com.cardiograph.model.User;
import com.cardiograph.thread.RequestTask;
import com.cardiograph.thread.RequestTask.ResponseCallBack;
import com.cardiograph.util.PreferencesUtil;
import com.cardiograph.util.Util;
import com.example.cardiograph.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 注册
 * 
 * @ClassName RegisterActivity
 * @author Yjx
 * @date 2014年12月17日 下午11:40:40
 */

public class UpdateActivity extends Activity implements OnClickListener {
    // 账号
	private TextView tvCode;
    private EditText etCode;
    // 密码
    private TextView tvPwd;
    private EditText etPwd;
    // 确认密码
    private TextView tvConfirmPwd;
    private EditText etConfirmPwd;
    //注册
	private BtnWithTopLine confirmBtn;
	
	private String phone;
	private String sex;
	private String birth;
	
	private SharedPreferences sp = null;
	private Editor editor = null;
	
	private String userName;
	private MyProgressDialog mpd;
	protected NavigationBar navigationBar; // 导航条
	private NavigationBar.OnNavBarClickListener onNavBarClickListener = new NavigationBar.OnNavBarClickListener() {
		@Override
		public void onNavItemClick(NavigationBar.NavigationBarItem navBarItem) {
			if (navBarItem == NavigationBar.NavigationBarItem.back) {
				setResult(1);
				finish();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_update);
		init();
	}
	
	
	private void init(){
		sp = PreferenceManager.getDefaultSharedPreferences(this);
//		userName = sp.getString("userName", "");
//		editor= sp.edit();
		// 标题
		navigationBar = (NavigationBar) findViewById(R.id.id_navigation_bar);
		navigationBar.setTitle(getResources().getString(R.string.register));
		navigationBar.setOnNavBarClickListener(onNavBarClickListener);
		tvCode = (TextView) findViewById(R.id.id_include1_code).findViewById(R.id.id_combinatiion_text_edit_text);
		tvPwd = (TextView) findViewById(R.id.id_include2_password).findViewById(R.id.id_combinatiion_text_edit_text);
		tvConfirmPwd = (TextView) findViewById(R.id.id_include3_confirm_password).findViewById(R.id.id_combinatiion_text_edit_text);
		etCode = (EditText) findViewById(R.id.id_include1_code).findViewById(R.id.id_combination_text_edit_edit);
		etPwd = (EditText) findViewById(R.id.id_include2_password).findViewById(R.id.id_combination_text_edit_edit);
		etConfirmPwd = (EditText) findViewById(R.id.id_include3_confirm_password).findViewById(R.id.id_combination_text_edit_edit);
		confirmBtn = (BtnWithTopLine) findViewById(R.id.btnOK);
		
		tvCode.setText(Util.addSpaceToStringFront("手机号码",4));
		tvPwd.setText(Util.addSpaceToStringFront("性别",4));
		tvConfirmPwd.setText("出生年月");
		etCode.setHint("输入手机号码");
		etCode.setInputType(InputType.TYPE_CLASS_TEXT);
		etCode.setLongClickable(false);
		etPwd.setHint("输入性别");
		etPwd.setInputType(InputType.TYPE_CLASS_TEXT);
		etPwd.setLongClickable(false);
		etConfirmPwd.setHint("输入出生年月");
		etConfirmPwd.setInputType(InputType.TYPE_CLASS_TEXT);
		etConfirmPwd.setLongClickable(false);
		
		confirmBtn.setOnClickListener(this);
		
		mpd = new MyProgressDialog(this);
		mpd.setMessage("正在更新中...");
		mpd.setCancelable(false);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnOK:
			update();
			break;
			
		default:
			break;
		}
	}

	private void update() {
		phone = etCode.getText().toString().trim();
		sex = etPwd.getText().toString().trim();
		birth = etConfirmPwd.getText().toString().trim();
		List<NameValuePair> nvpList = new ArrayList<NameValuePair>();
		nvpList.add(new BasicNameValuePair("action", "update"));
		String key = PreferencesUtil.getInstance(this).getString("key");
		nvpList.add(new BasicNameValuePair("key", key));
		if(!TextUtils.isEmpty(phone)){
			nvpList.add(new BasicNameValuePair("phone", phone));
		}
		if(!TextUtils.isEmpty(sex)){
			nvpList.add(new BasicNameValuePair("sex", sex));
		}
		if(!TextUtils.isEmpty(birth)){
			nvpList.add(new BasicNameValuePair("birth", birth));
		}
		new RequestTask(Constance.URL_UPDATE, Constance.POST, nvpList, new ResponseCallBack() {
			@Override
			public void onExecuteResult(String response) {
				try {
					Log.d("yjx",response);
					JSONObject jsonObject = new JSONObject(response);
					String result = jsonObject.getString("result");
					if(result.equals(Constance.RESULT_OK)){
						finish();
						Toast.makeText(UpdateActivity.this, "更新成功！", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(UpdateActivity.this, "更新失败！", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).execute();
	}
}
