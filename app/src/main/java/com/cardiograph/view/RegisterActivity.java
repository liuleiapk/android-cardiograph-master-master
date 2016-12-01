package com.cardiograph.view;

import com.cardiograph.component.BtnWithTopLine;
import com.cardiograph.component.MyProgressDialog;
import com.cardiograph.component.NavigationBar;
import com.cardiograph.model.User;
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

public class RegisterActivity extends Activity implements OnClickListener {
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
	
	private String code;
	private String pwd;
	private String confirmPwd;
	
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
		setContentView(R.layout.activity_register);
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
		confirmBtn = (BtnWithTopLine) findViewById(R.id.register);
		
		tvCode.setText(Util.addSpaceToStringFront(getString(R.string.account),4));
		tvPwd.setText(Util.addSpaceToStringFront(getString(R.string.password),4));
		tvConfirmPwd.setText(getString(R.string.confirm_password));
		etCode.setHint(R.string.input_code);
		etCode.setInputType(InputType.TYPE_CLASS_TEXT);
		etCode.setLongClickable(false);
		etPwd.setHint(R.string.input_pwd);
		etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		etPwd.setLongClickable(false);
		etConfirmPwd.setHint(R.string.input_confirm_pwd);
		etConfirmPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		etConfirmPwd.setLongClickable(false);
		
		confirmBtn.setOnClickListener(this);
		
		mpd = new MyProgressDialog(this);
		mpd.setMessage("正在注册中...");
		mpd.setCancelable(false);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.register:
			if(isInputValid()){
				registerPwd();
			}
			break;
			
		default:
			break;
		}
	}

	private void registerPwd() {
		User user = new User(this, code, pwd, "李天一");
		boolean addResult = user.add();
		if(addResult){
			Toast.makeText(this, "恭喜你注册成功！", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra("code", code);
			setResult(1,intent);
			finish();
		}else{
			Toast.makeText(this, "注册失败，请重新输入。", Toast.LENGTH_LONG).show();
		}
	}


	protected boolean isInputValid() {
		code = etCode.getText().toString().trim();
		pwd = etPwd.getText().toString().trim();
		confirmPwd = etConfirmPwd.getText().toString().trim();
		if(TextUtils.isEmpty(code)){
			Util.toastCenter(this,R.string.code_not_null);
		}
		if ((pwd.length() < 6 || pwd.length() > 20) && (confirmPwd.length() < 6 || confirmPwd.length() > 20)) {
			Util.toastCenter(this,R.string.PW_illegal_content1);
			etPwd.setText("");
			etConfirmPwd.setText("");
			return false;
		}else if  (!pwd.equals(confirmPwd)) {
			Util.toastCenter(this,R.string.PW_illegal_error);
			etConfirmPwd.setText("");
			return false;
		}
		
		if (Util.checkPWLevel(pwd).equals("BAD")) {
            Util.toastCenter(this,R.string.PW_illegal_content0);
			etPwd.setText("");
			etConfirmPwd.setText("");
            return false;
        }
		
		return true;
	}
	
}
