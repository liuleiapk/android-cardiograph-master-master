package com.cardiograph.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户相关信息
 * @ClassName UserInfo
 * @author ZhengWx
 * @date 2014年10月9日 下午3:34:15
 */
public class UserInfo {
	private static UserInfo user = null;
	
	private UserInfo() {
	}
	
	public static UserInfo getInstance() {
		if (user == null) {
			user = new UserInfo();
		}
		return user;
	}
	
	public void destory(){
		if(user != null){
			user = null;
		}
	}
	
	private boolean isLogin = false;
	private List<byte[]> aidList = null;
//	private NLDevice connectedDevice = null;
	
	public boolean isLogin() {
		return isLogin;
	}
	
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	
	public List<byte[]> getAidList() {
		return aidList;
	}
	
	public void setAidList(List<byte[]> aidList) {
		this.aidList = aidList;
	}
	
	public void addAid(byte[] aid){
		if(aidList == null){
			aidList = new ArrayList<byte[]>();
		}
		aidList.add(aid);
	}

//	public NLDevice getConnectedDevice() {
//		return connectedDevice;
//	}

//	public void setConnectedDevice(NLDevice connectedDevice) {
//		this.connectedDevice = connectedDevice;
//	}
	
}
