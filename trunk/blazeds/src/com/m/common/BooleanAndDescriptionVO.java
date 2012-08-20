package com.m.common;

import java.io.Serializable;

public class BooleanAndDescriptionVO implements Serializable {

	static final long serialVersionUID = 103844514947365244L;
	private boolean bResult;
	private String strDescription;
	
	public BooleanAndDescriptionVO(){};
	public BooleanAndDescriptionVO(boolean b , String description) {
		
		this.bResult = b ;
		this.strDescription = description;
	}
	
	public void setbResult(boolean b) {this.bResult = b;}
	public void setstrDescription(String str) {this.strDescription = str;}
	
	public boolean getbResult(){return this.bResult;}
	public String getstrDescription(){return this.strDescription;}
}
