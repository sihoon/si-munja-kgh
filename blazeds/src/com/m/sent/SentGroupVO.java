package com.m.sent;

import java.io.Serializable;

public class SentGroupVO implements Serializable {
	
	static final long serialVersionUID = 1222179582713735628L;
	
	
	int idx = 0;
	String timeSend = "";
	String line = "";
	String reservation = "";
	String returnPhone = "";
	int count = 0;
	String message = "";
	String timeWrite = "";
	String tranType = "";
	
	public String getTimeSend() {
		return timeSend;
	}
	public void setTimeSend(String timeSend) {
		this.timeSend = timeSend;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public String getReservation() {
		return reservation;
	}
	public void setReservation(String reservation) {
		this.reservation = reservation;
	}
	public String getReturnPhone() {
		return returnPhone;
	}
	public void setReturnPhone(String returnPhone) {
		this.returnPhone = returnPhone;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTimeWrite() {
		return timeWrite;
	}
	public void setTimeWrite(String timeWrite) {
		this.timeWrite = timeWrite;
	}
	public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	

}
