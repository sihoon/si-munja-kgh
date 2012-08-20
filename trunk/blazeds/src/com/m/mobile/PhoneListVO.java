package com.m.mobile;

import java.io.Serializable;

public class PhoneListVO implements Serializable {
	
	static final long serialVersionUID = 1223179582713735628L;
	
	static final int SECTION_MEMBER = 0;
	static final int SECTION_GROUP = 1;
	
	int phoneSection = 0;
	int groupKey = 0;
	String phoneName = "";
	String phoneNumber = "";
	
	
	public int getPhoneSection() {
		return phoneSection;
	}
	public void setPhoneSection(int phoneSection) {
		this.phoneSection = phoneSection;
	}
	public int getGroupKey() {
		return groupKey;
	}
	public void setGroupKey(int groupKey) {
		this.groupKey = groupKey;
	}
	public String getPhoneName() {
		return phoneName;
	}
	public void setPhoneName(String phoneName) {
		this.phoneName = phoneName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
