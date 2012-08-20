package com.m.member;

import java.io.Serializable;
import java.util.HashMap;

import com.common.util.SLibrary;

public class MemberVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8057778054918098249L;
	
	int idx;
	String user_id="";
	String passwd="";
	String user_name="";
	String jumin_no="";
	String email="";
	String phone_return="";
	String hp="";
	String unit_cost="";
	String line="";
	String memo="";
	String timeLogin="";
	String timeJoin="";
	String leaveYN="";
	int point=0;
	
	public void setHashMap(HashMap<String, String> hm) {
		
		if (hm != null) {
			setIdx(SLibrary.intValue(SLibrary.IfNull( hm,"idx" )));
			setUser_id(SLibrary.IfNull( hm,"user_id" ));
			setPasswd(SLibrary.IfNull( hm,"passwd" ));
			setUser_name(SLibrary.IfNull( hm,"user_name" ));
			setJumin_no(SLibrary.IfNull( hm,"jumin_no" ));
			setEmail(SLibrary.IfNull( hm,"email" ));
			setPhone_return(SLibrary.IfNull( hm,"phone_return" ));
			setHp(SLibrary.IfNull( hm,"hp" ));
			setEmail(SLibrary.IfNull( hm,"email" ));
			setUnit_cost(SLibrary.IfNull( hm,"unit_cost" ));
			setLine(SLibrary.IfNull( hm,"line" ));
			setMemo(SLibrary.IfNull( hm,"memo" ));
			setTimeLogin(SLibrary.IfNull( hm,"timeLogin" ));
			setTimeJoin(SLibrary.IfNull( hm,"timeJoin" ));
			setLeaveYN(SLibrary.IfNull( hm,"leaveYN" ));
			setPoint(SLibrary.intValue(SLibrary.IfNull( hm,"point" )));
		}
	}
	
	
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}


	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getJumin_no() {
		return jumin_no;
	}
	public void setJumin_no(String jumin_no) {
		this.jumin_no = jumin_no;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone_return() {
		return phone_return;
	}
	public void setPhone_return(String phone_return) {
		this.phone_return = phone_return;
	}
	public String getHp() {
		return hp;
	}
	public void setHp(String hp) {
		this.hp = hp;
	}
	public String getUnit_cost() {
		return unit_cost;
	}
	public void setUnit_cost(String unit_cost) {
		this.unit_cost = unit_cost;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getTimeLogin() {
		return timeLogin;
	}
	public void setTimeLogin(String timeLogin) {
		this.timeLogin = timeLogin;
	}
	public String getTimeJoin() {
		return timeJoin;
	}
	public void setTimeJoin(String timeJoin) {
		this.timeJoin = timeJoin;
	}
	public String getLeaveYN() {
		return leaveYN;
	}
	public void setLeaveYN(String leaveYN) {
		this.leaveYN = leaveYN;
	}


	public int getPoint() {
		return point;
	}


	public void setPoint(int point) {
		this.point = point;
	}
	
	

}
