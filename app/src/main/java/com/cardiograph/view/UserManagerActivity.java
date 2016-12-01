package com.cardiograph.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cardiograph.adapter.UserManagerAdapter;
import com.cardiograph.control.UserOnClickListener;
import com.cardiograph.model.HeartRate;
import com.cardiograph.model.User;
import com.cardiograph.util.FileUtils;
import com.example.cardiograph.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class UserManagerActivity extends Activity {
	private ListView lvUserManager;
	private ImageButton btnAddUser;
	private List<User> lstUser;
	private List<Map<String, Object>> lstData;
	private List<Boolean> lstTick;
	private UserManagerAdapter userManagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_manager);
		initData();
		initComponent();
		registerListener();
    }
	private void initData() {
		User user = new User(this);
		lstUser = user.findUsers();
		lstData = FileUtils.findFile(new File("/mnt/sdcard/data/"));
		for(User user1 : lstUser){
			System.out.println("User"+user1.getName());
		}
		lstTick = new ArrayList<Boolean>();
		for (int i = 0; i < lstData.size(); i++) {
			lstTick.add(false);
		}
	}
	private void initComponent() {
		lvUserManager = (ListView) findViewById(R.id.lvUserManager);
		btnAddUser = (ImageButton) findViewById(R.id.btnAddUser);
		userManagerAdapter = new UserManagerAdapter(this, lstData, lstTick);
		lvUserManager.setAdapter(userManagerAdapter);

	}
	private void registerListener() {
		lvUserManager.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				ImageView ivTick = (ImageView) ((LinearLayout)view).getChildAt(0);
				ImageView ivTick = (ImageView) view.findViewById(R.id.ivTick);
				if(lstTick.get(position)){
					ivTick.setImageResource(R.drawable.profile_tick_off);
					lstTick.set(position, false);
				}else{
					ivTick.setImageResource(R.drawable.profile_tick_on);
					lstTick.set(position, true);
				}
//				if(lstTick.get(position)){
//					lstTick.set(position, false);
//				}else{
//					lstTick.set(position, true);
//				}
//				userManagerAdapter.notifyDataSetChanged();
				System.out.println("position="+position);
			}
		});
		btnAddUser.setOnClickListener(new UserOnClickListener(this, lstUser, userManagerAdapter));
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.down_to_up_in, R.anim.down_to_up_out);
	}
}
