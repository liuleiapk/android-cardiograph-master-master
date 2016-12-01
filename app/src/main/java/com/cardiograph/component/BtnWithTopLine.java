package com.cardiograph.component;


import com.example.cardiograph.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * 使用方法:
 * 1.xml中需要加入命名空间 xmlns:btnline="http://schemas.android.com/apk/res/com.example.androidtest"
 * 2.btnline可以随意，com.example.androidtest为包名称
 * 3.引用：btnline:属性
 * 可以设置的属性：
 *  btn_text
    line_color
    btn_text_color
    background
 * @author yjx
 *
 */
@SuppressLint("ResourceAsColor")
public class BtnWithTopLine extends LinearLayout{

	private Button btn;
	private View line;
	
	public BtnWithTopLine(Context context) {
		super(context);
	}
	public BtnWithTopLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}
	
	private void init(Context context,AttributeSet attrs){
		line = new View(context);
		btn = new Button(context);
		
		if(null != attrs){
			TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.BtnWithTopLine);
			String btnText = (String) typeArray.getText(R.styleable.BtnWithTopLine_btn_text);
			int btnTextColor = typeArray.getColor(R.styleable.BtnWithTopLine_btn_text_color, context.getResources().getColor(android.R.color.black));
			int lineColor = typeArray.getColor(R.styleable.BtnWithTopLine_line_color, context.getResources().getColor(android.R.color.black));
//			int viewBg = typeArray.getColor(R.styleable.BtnWithTopLine_view_bg, context.getResources().getColor(android.R.color.white));
			float btnTextSize = typeArray.getInteger(R.styleable.BtnWithTopLine_btn_text_size, 16);
			ColorStateList btnColorStateList = typeArray.getColorStateList(R.styleable.BtnWithTopLine_btn_text_color);
			
			
			line.setBackgroundColor(lineColor);
			
			btn.setText(btnText);
			btn.setTextColor(btnColorStateList);
			btn.setTextSize(btnTextSize);
			btn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			
			setBackgroundDrawable(typeArray.getDrawable(R.styleable.BtnWithTopLine_view_bg));
			
			typeArray.recycle();
		}
		
		setOrientation(LinearLayout.VERTICAL);
		addView(line,new LayoutParams(LayoutParams.WRAP_CONTENT, 1));
		
		LayoutParams btnParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		btnParams.gravity = Gravity.CENTER;
		btn.setClickable(false);
		addView(btn,btnParams);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		setLayoutParams(params);
		
	}
	
	public Button getBtn() {
		return btn;
	}
	public View getLine() {
		return line;
	}
	public void setBtnText(CharSequence cs){
		btn.setText(cs);
	}
	
	public void setText(int strId){
		btn.setText(strId);
	}
	

	public void setBtnColor(int colorId){
		btn.setTextColor(colorId);
	}
	
	public void setLineColor(int colorId){
		line.setBackgroundColor(colorId);
	}
	
	public void setBackground(int colorId){
		btn.setBackgroundColor(colorId);
		setBackground(colorId);
	}
	
	public void setBtnBackgroundDrawable(Drawable drawable){
		btn.setBackgroundDrawable(drawable);
	}
}
