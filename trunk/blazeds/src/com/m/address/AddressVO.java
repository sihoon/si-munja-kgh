package com.m.address;

import java.io.Serializable;

import com.common.util.SLibrary;

public class AddressVO implements Serializable {
	
	private static final long serialVersionUID = 1322179582713735628L;
	
	public static final int GROUP_FLAG = 0;
	public static final int ADDRESS_FLAG = 1;
	
	
	int idx = 0;
	String user_id = "";
	int grp = 0;
	String grpName = "";
	String name = "";
	String phone = "";
	String memo = "";
	String writedate = "";
	
	boolean bModify = false;

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

	public int getGrp() {
		return grp;
	}

	public void setGrp(int grp) {
		this.grp = grp;
	}

	public String getGrpName() {
		return grpName;
	}

	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return SLibrary.replaceAll(phone.trim(), "-", "");
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getWritedate() {
		return writedate;
	}

	public void setWritedate(String writedate) {
		this.writedate = writedate;
	}

	public boolean isbModify() {
		return bModify;
	}

	public void setbModify(boolean bModify) {
		this.bModify = bModify;
	}
	
	
}
