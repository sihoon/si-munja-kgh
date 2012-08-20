package com.m.member;

import java.io.Serializable;

public class JoinVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 122L;

	String user_id;
	String passwd;
	String user_name;
	String jumin_no;
	String hp;
	String phone_return;
	String email;
	String emailYN;
	String hpYN;
	
	
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getPassword() {
		return passwd;
	}
	public void setPassword(String password) {
		this.passwd = password;
	}
	public String getName() {
		return user_name;
	}
	public void setName(String name) {
		this.user_name = name;
	}
	public String getJumin() {
		return jumin_no;
	}
	public void setJumin(String jumin) {
		this.jumin_no = jumin;
	}
	public String getHp() {
		return hp;
	}
	public void setHp(String hp) {
		this.hp = hp;
	}
	public String getReturnPhone() {
		return phone_return;
	}
	public void setReturnPhone(String returnPhone) {
		this.phone_return = returnPhone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmailYN() {
		return emailYN;
	}
	public void setEmailYN(String emailYN) {
		this.emailYN = emailYN;
	}
	public String getHpYN() {
		return hpYN;
	}
	public void setHpYN(String hpYN) {
		this.hpYN = hpYN;
	}
	
	
}
