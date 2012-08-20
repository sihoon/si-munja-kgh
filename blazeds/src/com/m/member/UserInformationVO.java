package com.m.member;

import java.io.Serializable;
import java.util.HashMap;

import com.common.util.SLibrary;

public class UserInformationVO implements Serializable {

	private static final long serialVersionUID = 1222179582713735629L;

	String user_id = "";
	String user_name = "";
	String phone_return = "";
	String line = "";
	String point = "";
	String levaeYN = "";
	int unit_cost = 0;
	String hp = "";
	String jumin_no = "";
	String email = "";
	String emailYN = "";
	String hpYN = "";

	public void setHashMap(HashMap<String, String> hm) {

		user_id = SLibrary.IfNull(hm, "user_id");
		user_name = SLibrary.IfNull(hm, "user_name");
		phone_return = SLibrary.IfNull(hm, "phone_return");
		line = SLibrary.IfNull(hm, "line");
		point = SLibrary.IfNull(hm, "point");
		levaeYN = SLibrary.IfNull(hm, "levaeYN");
		hp = SLibrary.IfNull(hm, "hp");
		jumin_no = SLibrary.IfNull(hm, "jumin_no");
		unit_cost = SLibrary.intValue(SLibrary.IfNull(hm, "unit_cost"));
		email = SLibrary.IfNull(hm, "email");
		emailYN = SLibrary.IfNull(hm, "emailYN");
		hpYN = SLibrary.IfNull(hm, "hpYN");
	}

	public String getJumin_no() {
		return jumin_no;
	}

	public void setJumin_no(String jumin_no) {
		this.jumin_no = jumin_no;
	}

	public String getHp() {
		return hp;
	}

	public void setHp(String hp) {
		this.hp = hp;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPhone_return() {
		return phone_return;
	}

	public void setPhone_return(String phone_return) {
		this.phone_return = phone_return;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getLevaeYN() {
		return levaeYN;
	}

	public void setLevaeYN(String levaeYN) {
		this.levaeYN = levaeYN;
	}

	public int getUnit_cost() {
		return unit_cost;
	}

	public void setUnit_cost(int unit_cost) {
		this.unit_cost = unit_cost;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String mail) {
		this.email = mail;
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
