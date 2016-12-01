package com.cardiograph.view;

import com.example.cardiograph.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class HelpActivity extends Activity {
	private LinearLayout llHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		llHelp = (LinearLayout) findViewById(R.id.llHelp);
//		llHelp.addView(new WaveSurfaceView(this, true));
    }
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.right_to_left_in, R.anim.right_to_left_out);
	}
}
