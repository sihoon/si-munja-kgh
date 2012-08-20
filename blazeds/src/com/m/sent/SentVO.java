package com.m.sent;

import java.io.Serializable;

public class SentVO implements Serializable {

	private static final long serialVersionUID = 1222179582713735628L;
	
	int sentGroupIndex = 0;
	String sentClientName = "";
	String sentDate = "";
	String phone = "";
	String name = "";
	String returnPhone = "";
	String message = "";
	String result = "";
	String result_code = "";	
	String resultDate = "";
	String state = "";
	
	public void setAll(int sentGroupIndex, String sentClientName, String sentDate, String phone, String name, String returnPhone, String message, String result, String resultDate, String state, String result_code ) {
		
		this.sentGroupIndex = sentGroupIndex;
		this.sentClientName = sentClientName;
		this.sentDate = sentDate;
		this.phone = phone;
		this.name = name;
		this.returnPhone = returnPhone;
		this.message = message;
		this.result = result;
		this.resultDate = resultDate;
		this.state = state;
		this.result_code = result_code;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getSentGroupIndex() {
		return sentGroupIndex;
	}
	public void setSentGroupIndex(int sentGroupIndex) {
		this.sentGroupIndex = sentGroupIndex;
	}
	public String getSentClientName() {
		return sentClientName;
	}
	public void setSentClientName(String sentClientName) {
		this.sentClientName = sentClientName;
	}
	public String getSentDate() {
		return sentDate;
	}
	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReturnPhone() {
		return returnPhone;
	}
	public void setReturnPhone(String returnPhone) {
		this.returnPhone = returnPhone;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getResultDate() {
		return resultDate;
	}
	public void setResultDate(String resultDate) {
		this.resultDate = resultDate;
	}
	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String resultCode) {
		result_code = resultCode;
	}
	
	
	
}
