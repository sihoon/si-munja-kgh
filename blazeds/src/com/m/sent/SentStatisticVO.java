package com.m.sent;

import java.io.Serializable;

public class SentStatisticVO implements Serializable {

	private static final long serialVersionUID = 1222179582713735612L;
	
	String returnPhone = "";
	String sendDate = "";
	String message = "";
	int standbyCount = 0;
	int sendingCount = 0;
	int successCount = 0;
	int failCount = 0;
	int wrongCount = 0;
	int totalCount = 0;
	


	public void setAll( String returnPhone, String sendDate, String message, int standbyCount, int sendingCount, int successCount, int failCount, int wrongCount, int totalCount ) {
		
		this.returnPhone = returnPhone;
		this.sendDate = sendDate;
		this.message = message;
		this.standbyCount = standbyCount;
		this.sendingCount = sendingCount;
		this.successCount = successCount;
		this.failCount = failCount;
		this.wrongCount = wrongCount;
		this.totalCount = totalCount;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStandbyCount() {
		return standbyCount;
	}

	public void setStandbyCount(int standbyCount) {
		this.standbyCount = standbyCount;
	}

	public int getSendingCount() {
		return sendingCount;
	}

	public void setSendingCount(int sendingCount) {
		this.sendingCount = sendingCount;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public int getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(int wrongCount) {
		this.wrongCount = wrongCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	
}
