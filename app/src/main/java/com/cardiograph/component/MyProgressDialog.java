package com.cardiograph.component;

import com.example.cardiograph.R;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


public class MyProgressDialog extends Dialog {
	private TextView tv;

	public MyProgressDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	public MyProgressDialog(Context context) {
		super(context, R.style.Theme_dialog);
		init();
	}

	public MyProgressDialog(Context context, String message) {
		super(context, R.style.Theme_dialog);
		init();
		if(TextUtils.isEmpty(message)){
			tv.setVisibility(View.GONE);
		}else{
			tv.setText(message);
		}
	}

	private void init() {
		setContentView(R.layout.progressdialog);
		getWindow().getAttributes().gravity = Gravity.CENTER;
		tv = (TextView) findViewById(R.id.message);
	}

	public void setMessage(String message) {
		tv.setVisibility(View.VISIBLE);
		tv.setText(message);
	}
}
