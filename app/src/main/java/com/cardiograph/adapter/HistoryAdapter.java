package com.cardiograph.adapter;


import java.util.List;

import com.cardiograph.model.HeartRate;
import com.cardiograph.view.MainActivity;
import com.example.cardiograph.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryAdapter extends BaseAdapter {
	private Context context;
	private List<HeartRate> lsthistory;
	public class ViewHolder{
		private TextView tvID;
		private TextView tvName;
		private TextView tvDateTime;
		private TextView tvBPM;
		private ImageButton btnDelete;
	}
	
	public HistoryAdapter(Context context, List<HeartRate> lsthistory) {
		super();
		this.context = context;
		this.lsthistory = lsthistory;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lsthistory.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lsthistory.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.listview_history_item, null);
			vh.tvID = (TextView) convertView.findViewById(R.id.tvID);
			vh.tvName = (TextView) convertView.findViewById(R.id.tvDateTime);
			vh.tvDateTime = (TextView) convertView.findViewById(R.id.tvName);
			vh.tvBPM = (TextView) convertView.findViewById(R.id.tvBPM);
			vh.btnDelete = (ImageButton) convertView.findViewById(R.id.btnDelete);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder)convertView.getTag();
		}
		vh.tvID.setText(String.valueOf(lsthistory.get(position).getId()));
		vh.tvName.setText(lsthistory.get(position).getName());
		vh.tvDateTime.setText(lsthistory.get(position).getDateTime());
		vh.tvBPM.setText(lsthistory.get(position).getRate()+"BPM");
		vh.btnDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView view = new TextView(context);
				view.setText("确认删除历史记录？");
				new AlertDialog.Builder(context)
					.setTitle("删除记录")
					.setIcon(R.drawable.symbol_cancel)
					.setView(view)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
					    	HeartRate hr = new HeartRate(context, lsthistory.get(position).getId());
                          	boolean deleteResult = hr.delete();
                 			if(deleteResult){
                 				Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                 			}else{
                 				Toast.makeText(context, "删除失败！", Toast.LENGTH_SHORT).show();
                 			}
                 			lsthistory.remove(position);
                 			notifyDataSetChanged();
							dialog.dismiss();
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
			});
		return convertView;
	}

}
