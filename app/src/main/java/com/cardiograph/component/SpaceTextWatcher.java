package com.cardiograph.component;


import android.text.Editable;
import android.text.TextWatcher;

/**
 * 去掉首个字符为空格
 * @author Administrator
 *
 */
public class SpaceTextWatcher implements TextWatcher{
	
	public SpaceTextWatcher() {
	}

	@Override
	public void afterTextChanged(Editable s) {
		if(s.length()>0){
			char ch = s.charAt(0);
			if(ch == ' '){
				s.replace(0, 1, "");
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}

}
