package com.m.excel;

import java.io.Serializable;
import java.util.List;
import java.util.HashMap;

public class ExcelLoaderResultVO implements Serializable {

	static final long serialVersionUID = 103844524947365244L;
	private boolean bResult;
	private String strDescription;
	private List<HashMap<String, String>> list;
	
	public ExcelLoaderResultVO(){};
	public ExcelLoaderResultVO(boolean b , String description) {
		
		this.bResult = b ;
		this.strDescription = description;
	}
	
	public void setbResult(boolean b) {this.bResult = b;}
	public void setstrDescription(String str) {this.strDescription = str;}
	public void setList(List<HashMap<String, String>> al) {this.list = al;}
	
	public boolean getbResult(){return this.bResult;}
	public String getstrDescription(){return this.strDescription;}
	public List<HashMap<String, String>> getList() {return this.list;}
}
