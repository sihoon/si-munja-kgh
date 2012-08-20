package com.m.billing;

import java.io.Serializable;

public class BillingVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6951736162721678217L;
	
	int idx;
	String user_id = "";
	String method = "";
	int amount;
	String order_no = ""; 
	String unit_cost = "";
	int point;
	int remain_point; 
	String memo = ""; 
	String admin_id = "";
	String timeWrite = "";
	String tid = "";
	String timestamp = "";
	
	public int getIdx() {
		return idx;
	}
	public String getUser_id() {
		return user_id;
	}
	public String getMethod() {
		return method;
	}
	public int getAmount() {
		return amount;
	}
	public String getOrder_no() {
		return order_no;
	}
	public String getUnit_cost() {
		return unit_cost;
	}
	public int getRemain_point() {
		return remain_point;
	}
	public String getMemo() {
		return memo;
	}
	public String getAdmin_id() {
		return admin_id;
	}
	public String getTimeWrite() {
		return timeWrite;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public void setUnit_cost(String unit_cost) {
		this.unit_cost = unit_cost;
	}
	public void setRemain_point(int remain_point) {
		this.remain_point = remain_point;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public void setAdmin_id(String admin_id) {
		this.admin_id = admin_id;
	}
	public void setTimeWrite(String timeWrite) {
		this.timeWrite = timeWrite;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}



}
