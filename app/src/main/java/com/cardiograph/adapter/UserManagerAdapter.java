package com.cardiograph.adapter;


import java.util.List;
import java.util.Map;

import com.cardiograph.model.HeartRate;
import com.cardiograph.model.User;
import com.example.cardiograph.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserManagerAdapter extends BaseAdapter {
	private Context context;
	private List<User> lstUser;
	private List<Boolean> lstTick;
	private List<Map<String, Object>> lstData;
	public class ViewHolder{
		private ImageView ivTick;
		private TextView tvUser;
		private ImageButton ibDelete;
		private TextView tvDateTime;
		private TextView tvItems;
	}
	
	public UserManagerAdapter(Context context, List<Map<String, Object>> lstData, List<Boolean> lstTick) {
		super();
		this.context = context;
		this.lstData = lstData;
		this.lstTick = lstTick;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lstData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lstData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh = new ViewHolder();
		if(convertView==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.listview_user_manager_item, null);
			vh.ivTick = (ImageView) convertView.findViewById(R.id.ivTick);
			vh.tvUser = (TextView) convertView.findViewById(R.id.tvUser);
			vh.ibDelete = (ImageButton) convertView.findViewById(R.id.ibDelete);
			vh.tvDateTime = (TextView) convertView.findViewById(R.id.tvDateTime);
			vh.tvItems = (TextView) convertView.findViewById(R.id.tvItems);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder)convertView.getTag();
		}
//		if(lstTick.get(position)){
//			vh.ivTick.setImageResource(R.drawable.profile_tick_on);
//		}else{
//			vh.ivTick.setImageResource(R.drawable.profile_tick_off);
//		}
		vh.tvUser.setText(lstData.get(position).get("name").toString());
//		vh.ibDelete.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				TextView view = new TextView(context);
//				view.setText("确认删除该用户？");
//				new AlertDialog.Builder(context)
//					.setTitle("删除用户")
//					.setIcon(R.drawable.symbol_cancel)
//					.setView(view)
//					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//					    	User user = new User(context, lstUser.get(position).getId());
//                          	boolean deleteResult = user.delete();
//                 			if(deleteResult){
//                 				Toast.makeText(context, "删除用户成功！", Toast.LENGTH_SHORT).show();
//                 			}else{
//                 				Toast.makeText(context, "删除用户失败！", Toast.LENGTH_SHORT).show();
//                 			}
//                 			lstUser.remove(position);
//                 			notifyDataSetChanged();
//							dialog.dismiss();
//						}
//					})
//					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							dialog.dismiss();
//						}
//					})
//					.show();
//			}
//			});
		return convertView;
	}

}
