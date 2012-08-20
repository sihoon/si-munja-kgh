/**
*  Class Name, 클래스 이름
*  Class Description, 클래스 설명
*  Version Information, 버전 정보
*  Make date, 작성일
*  Author, 작성자
*  Modify lists, 수정내역
*  Copyright, 저작권 정보
*/
package com.common.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.io.InputStream;
import java.io.StringReader;


import com.common.VbyP;


public class RunPrepared implements RunPreparedAble {
	
	Connection connection = null;
    PreparedStatement pstmt = null;
    String localSQL = null;
    boolean bUserConnection = false;
    ArrayList<Object> setDataArrayList = null;
    
    RunPrepared(){}
    
    private void init(){
    	connection = null;
        pstmt = null;
        localSQL = null;
        bUserConnection = false;
    }
    
    public void setPrepared(String sql)
    {
    	VbyP.debugLog(" >> setPrepared : "+sql);
    	init();
    	connection = VbyP.getDB();
    	this.localSQL = sql;
    	
        try
        {
            pstmt = connection.prepareStatement(sql);
        }
        catch(Exception e)
        {
        	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
        }
    }
    
    public void setPrepared(String strConn , String sql)
    {
    	VbyP.debugLog(" >> setPrepared : "+sql);
    	connection = VbyP.getDB();
    	this.localSQL = sql;
    	this.setDataArrayList = new ArrayList<Object>();
        try
        {
            pstmt = connection.prepareStatement(sql);
        }
        catch(Exception e)
        {
        	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
        }
    }
    
    public void setPrepared(Connection conn , String sql)
    {
    	VbyP.debugLog(" >> setPrepared : "+sql);
    	connection = conn;
    	this.localSQL = sql;
    	bUserConnection = true;
    	this.setDataArrayList = new ArrayList<Object>();
        try
        {
            pstmt = connection.prepareStatement(sql);
        }
        catch(Exception e)
        {
        	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
        }
    }
    
    public void setNull(int index, int sqlType){
    	try {
    		pstmt.setNull(index, sqlType);
    		this.setDataArrayList.add(index-1 , new Integer(sqlType));
    		
    	}catch(SQLException e){
    		
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
    	}
        
    }
    
    public void setBoolean(int index, boolean x) {
    	try {
        	pstmt.setBoolean(index, x);
        	this.setDataArrayList.add(index-1 , new Boolean(x));
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setByte(int index, byte x) {
    	try {
        	pstmt.setByte(index, x);
        	this.setDataArrayList.add(index-1 , new Byte(x));
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setShort(int index, short x) {
    	try {
	        pstmt.setShort(index, x);
	        this.setDataArrayList.add(index-1 , new Short(x));
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setInt(int index, int x) {
    	try {
        	pstmt.setInt(index, x);
        	this.setDataArrayList.add(index-1 , new Integer(x));
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setLong(int index, long x) {
    	try {
        	pstmt.setLong(index, x);
        	this.setDataArrayList.add(index-1 , new Long(x));
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setFloat(int index, float x) {
    	try {
        	pstmt.setFloat(index, x);
        	this.setDataArrayList.add(index-1 , new Float(x));
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setDouble(int index, double x) {
    	try {
        	pstmt.setDouble(index, x);
        	this.setDataArrayList.add(index-1 , new Double(x));
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setBigDecimal(int index, BigDecimal x) {
    	try {
        	pstmt.setBigDecimal(index, x);
        	this.setDataArrayList.add(index-1 , x);
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setString(int index, String x) {
    	try {
    	pstmt.setString(index, x);
    	this.setDataArrayList.add(index-1 , new String(x));
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setBytes(int index, byte x[]) {
    	try {
        	pstmt.setBytes(index, x);
        	this.setDataArrayList.add(index-1 , x);
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setDate(int index, Date x) {
    	try {
        	pstmt.setDate(index, x);
        	this.setDataArrayList.add(index-1 , x);
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setTime(int index, Time x) {
    	try {
        	pstmt.setTime(index, x);
        	this.setDataArrayList.add(index-1 , x);
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setTimestamp(int index, Timestamp x) {
    	try {
        	pstmt.setTimestamp(index, x);
        	this.setDataArrayList.add(index-1 , x);
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setAsciiStream(int index, InputStream x, int length) {
    	try {
        	pstmt.setAsciiStream(index, x, length);
        	this.setDataArrayList.add(index-1 , x);
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setBinaryStream(int index, InputStream x, int length) {
    	try {
        	pstmt.setBinaryStream(index, x, length);
        	this.setDataArrayList.add(index-1 , x);
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void clearParameters() {
    	try {
        	pstmt.clearParameters();
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setObject(int index, Object x, int target, int scale) {
    	try {
    	pstmt.setObject(index, x, target, scale);
    	this.setDataArrayList.add(index-1 , x);
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setObject(int index, Object x, int target) {
    	try {
        	pstmt.setObject(index, x, target);
        	this.setDataArrayList.add(index-1 , x);
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setObject(int index, Object x) {
    	try {
        	pstmt.setObject(index, x);
        	this.setDataArrayList.add(index-1 , x);
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setPrepared "+this.localSQL);
        	this.close();
		}
    }
    
    public void setCharacterStream(int index, StringReader sr, int length){ 
    	
    	try {
        	pstmt.setCharacterStream(index, sr, length);
        	this.setDataArrayList.add(index-1 , sr.toString());
	    }catch(SQLException e){
	    	
        	VbyP.errorLog("RunPrepared : "+e.toString()+" setCharacterStream "+this.localSQL);
        	this.close();
		}
    }
    
    public void close(){
    	try {
    		VbyP.debugLog(this.localSQL);
    		if (this.pstmt != null)
    			this.pstmt.close();
	    	
			//현재 인스턴스에서 연결을 생성 하였을경우 pstmt.executeQuery()
	    	if (!bUserConnection && connection != null)
				connection.close();		//conn 객체 반환
	    }catch(Exception e){
	    	try{
	    		if (connection != null)
	    			connection.close();
        	}catch(Exception s){VbyP.errorLog("CLOSEERROR : "+s.toString()+" setPrepared "+this.getResultQuery());}
			VbyP.errorLog("RunPrepared : "+ "close 에러 :"+e.toString());
		}
    }
    
    public String getResultQuery() {
    	
    	String result = null ; 	
	  	
		String strCheck = new String(this.localSQL);
		StringBuffer strBuf = new StringBuffer();
		int i = 0;
		
		while(strCheck.length() != 0) { 
			int begin = strCheck.indexOf("?");
			if(begin == -1) {
				strBuf.append(strCheck);
				break;
			} else {
				int end = begin + 1;
				strBuf.append(strCheck.substring(0, begin));
				if ( setDataArrayList.get(i) instanceof String ) {
					strBuf.append("'" + setDataArrayList.get(i) + "'");
				} else {
					strBuf.append(setDataArrayList.get(i));	 
				}				 
				strCheck = strCheck.substring(end);								
			}
			if( i < setDataArrayList.size()-1 ) i++;
		}		
		result = strBuf.toString();
		
	  return result ; 
    }
}
