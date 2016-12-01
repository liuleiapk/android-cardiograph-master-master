package com.cardiograph.model;

import java.util.List;
import java.util.Map;

import com.cardiograph.dao.UserDao;

import android.content.Context;

public class User {
	private Context context;
	private int id;
	private String code;
	private String pwd;
	private String name;
	
	public final static int ACTIVE=1;//激活状态
	public final static int INACTIVE=0;//非激活状态
	
	/**
	 * 激活状态
	 * ACTIVE 激活状态
	 * INACTIVE 非激活状态
	 * */
	public int isState ; //激活状态
	
	//用户id
	public String userId  ="";
	
	//登录用户名
	public String userName="";
	
	//密码
	public String password;
	
	//登录令牌  
	public String token;
	
	public User() {
		super();
	}

	public User(Context context) {
		super();
		this.context = context;
	}
	
	public User(Context context, int id) {
		super();
		this.context = context;
		this.id = id;
	}

	public User(Context context, String code, String pwd)
	{
		this.context = context;
		this.code = code;
		this.pwd = pwd;
	}
	
	public User(Context context, String code, String pwd, String name) {
		super();
		this.context = context;
		this.code = code;
		this.pwd = pwd;
		this.name = name;
	}

	public User(Context context, int id, String name) {
		super();
		this.context = context;
	    this.id = id;
		this.name = name;
	}

	public User(Context context, String name) {
		super();
		this.context = context;
		this.name = name;
	}
	
	public Map<String, Object> login()
	{
		UserDao ud = new UserDao(this,context);
		return ud.login();
	}
	
	public boolean add()
	{
		UserDao ud = new UserDao(this,context);
		return ud.add();
	}
	
	public void update()
	{
		
	}
	
	public boolean delete()
	{
		UserDao ud = new UserDao(this,context);
		return ud.delete();
	}
	
	public User findUser(User us)
	{
		User user = null;
		return user;
	}
	
	public List<User> findUsers()
	{
		UserDao ud = new UserDao(this, context);
		return ud.findUsers();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
