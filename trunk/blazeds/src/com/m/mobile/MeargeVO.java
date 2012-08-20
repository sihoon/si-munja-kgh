package com.m.mobile;

import java.io.Serializable;

public class MeargeVO implements Serializable {
	
	static final long serialVersionUID = 1223179582713735628L;
	
	String phone;	
	String message;
	String returnPhone;
	String sendDate;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getReturnPhone() {
		return returnPhone;
	}
	public void setReturnPhone(String returnPhone) {
		this.returnPhone = returnPhone;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	
	
}
