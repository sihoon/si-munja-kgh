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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.InputStream;

import com.common.VbyP;


public class PreparedExecuteQueryManager extends RunPrepared implements ExecuteQueryManagerAble {
	
	public PreparedExecuteQueryManager(){}
	
	public ResultSet executeQuery() {
		ResultSet rs = null;
		try {
			rs = super.pstmt.executeQuery(); 
		}catch (SQLException e){
			
			VbyP.errorLog("PreparedExecuteQueryManager "+"executeQuery ResultSet 에러 : "+e.toString()+super.getResultQuery());
	    	
		}
		
		return rs;
    }
    
    public int executeUpdate() {
    	int cnt = 0;
    	try {
    		if ( VbyP.getValue("DBUpdate").equals("Y") )
				cnt = super.pstmt.executeUpdate();
    	}catch (SQLException e){
    		VbyP.errorLog("PreparedExecuteQueryManager "+"executeUpdate 에러 : "+e.toString()+super.getResultQuery());
        	
    	}
		finally { super.close(); }
		return cnt;
    }
    
    public int executeUpdateNoClose() {
    	
    	int cnt = 0;
    	try {
			cnt = super.pstmt.executeUpdate(); 
    	}catch (SQLException e){
    		VbyP.errorLog("PreparedExecuteQueryManager "+"executeUpdate 에러 : "+e.toString()+super.getResultQuery());
        	
    	}
    	return cnt;
    }
	
    
    public void addBatch() {
    	try {
    		super.pstmt.addBatch();
    	}catch (SQLException e){
    		super.close();
    		VbyP.errorLog("PreparedExecuteQueryManager"+"addBatch 에러 : "+e.toString()+super.getResultQuery());
    		
    	}
    }
    
    public int executeBatchNoClose() {
    	
    	int rslt[] = null;
    	try {
    		rslt = super.pstmt.executeBatch();
    		
    	}catch (SQLException e){
    		VbyP.errorLog("PreparedExecuteQueryManager"+"executeBatch 에러 : "+e.toString()+super.getResultQuery());
    		
    	}
    	
    	return arraySumCount(rslt);
    }
    
    public int executeBatch() {
    	
    	int rslt[] = null;
    	try {
    		rslt = super.pstmt.executeBatch();
    		
    	}catch (SQLException e){
    		VbyP.errorLog("PreparedExecuteQueryManager"+"executeBatch 에러 : "+e.toString()+super.getResultQuery());
    		
    	}
    	finally { super.close(); }
    	
    	return arraySumCount(rslt);
    }
	/**  
	* 매게변수 sql 을 통해 nColumnCount 수 만큼의 문자열 배열을 Vector로 반환
	* @param sql - Query
	* @param nColumncount - 검색 칼럼 수
	* @return Vector -> String[nColumncount]
	*/
	public Vector<String[]> ExecuteQuery( int nColumnCount){
		
		Vector<String[]> vtList = new Vector<String[]>();	
		ResultSet rs = this.executeQuery();

		String [] strTemp = null;	//검색 내용 임시 저장소
		int i=0;
		
		try {
			
			while (rs.next())
			{
				strTemp = new String[nColumnCount];
	
				for ( i = 0  ; i < nColumnCount ; i++ ) 
				{
					strTemp[i] = rs.getString(i+1);
					if (strTemp[i]==null || strTemp[i].equals("null")) strTemp[i] = "";
				}
				vtList.add(strTemp);
			}
			rs.close();
		}catch(SQLException e){
			VbyP.errorLog("PreparedExecuteQueryManager "+"Vector<String[]> ExecuteQuery 에러 : "+e.toString()+super.getResultQuery());
        	
		}
		finally { super.close(); }		
		return vtList;
	}
	
	public ArrayList<HashMap<String, String>> ExecuteQueryArrayList(){
		
		ArrayList<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> data = null;
		ResultSetMetaData rsmd = null;
		String[] columnNames = null;
		ResultSet rs = this.executeQuery();
		
		int columnCount = 0;
		int i=0;
		String temp = null;
		
		
		try {
			
			rsmd = rs.getMetaData();
			columnNames = this.getColumnName(rsmd);
			columnCount = columnNames.length;
			
			
			while (rs.next())
			{
				data = new HashMap<String, String>();
				for ( i = 0  ; i < columnCount ; i++ ) 
				{
					temp = rs.getString(i+1);
					if (temp == null || temp.equals("null")) temp = "";
					data.put(columnNames[i],temp);
				}
				aList.add(data);
			}
			rs.close();
		}catch(SQLException e){
			VbyP.errorLog("PreparedExecuteQueryManager "+"ExecuteQueryResultMap 에러 : "+e.toString()+super.getResultQuery());
        	
		}
		finally { super.close(); }
		return aList;
	}
	
	/**  
	* 매게변수 sql 을 통해 nColumnCount 수 만큼의 문자열 배열을 반환
	* @param sql - Query
	* @param nColumncount - 검색 칼럼 수
	* @return String[nColumncount]
	*/
	public String[] ExecuteQueryCols(int nColumnCount) {
		ResultSet rs = this.executeQuery();
		int i=0;
		int j=0;
		
		String [] strTemp = new String[nColumnCount];	//검색 내용 저장소
		for (int c=0;c < strTemp.length; c++){
			strTemp[c]="";
		}
		try {
			while (rs.next())
			{				
				for ( i = 0  ; i < nColumnCount ; i++ ) 
				{
					j = i + 1;
					strTemp[i] = rs.getString(j);
					
					if (strTemp[i]==null || strTemp[i].equals("null")) strTemp[i] = "";
				}
			}
			rs.close();
		}catch(SQLException e){
			VbyP.errorLog("PreparedExecuteQueryManager "+"String[] ExecuteQueryCols 에러 : "+e.toString()+super.getResultQuery());
        	
		}		
		finally { super.close(); }
		return strTemp;
	}
	
	public HashMap<String, String> ExecuteQueryCols() {
		
		HashMap<String, String> data = null;
		ResultSetMetaData rsmd = null;
		String[] columnNames = null;
		ResultSet rs = this.executeQuery();
		
		int columnCount = 0;
		int i=0;
		String temp = null;
		
		try {
			
			rsmd = rs.getMetaData();
			columnNames = this.getColumnName(rsmd);
			columnCount = columnNames.length;
			
			while (rs.next())
			{
				data = new HashMap<String, String>();
				for ( i = 0  ; i < columnCount ; i++ ) 
				{
					temp = rs.getString(i+1);
					if (temp == null || temp.equals("null")) temp = "";
					data.put(columnNames[i],temp);
				}
			}
			rs.close();
		}catch(SQLException e){
			VbyP.errorLog("PreparedExecuteQueryManager "+"ExecuteQueryColsMap 에러 : "+e.toString()+super.getResultQuery());
        	
		}
		finally { super.close(); }
		return data;
	}
	
	/**  
	* sql 결과를 문자열 배열로 반환
	* @param sql - Query
	* @return String[]
	*/
	public String[] ExecuteQuery(){
		ResultSet rs = this.executeQuery();
		
		ArrayList<String> arrList = new ArrayList<String>();
	
		String [] strTemp = new String[0];
		try{			
			while (rs.next())
			{					
				if (rs.getString(1)==null || rs.getString(1).equals("null")) arrList.add("");
				else arrList.add(rs.getString(1));
			}
			try	{
				rs.close();
			}
			catch (Exception e)
			{
				VbyP.errorLog("PreparedExecuteQueryManager"+"String[] ExecuteQuery() 에러 111: "+e.toString()+super.getResultQuery());
			}
			strTemp = new String[arrList.size()];
			int cnt = arrList.size();
			for (int i = 0; i < cnt; i++){
				strTemp[i] = arrList.get(i);
			}
		}
		catch (Exception e)
		{
			VbyP.errorLog("PreparedExecuteQueryManager"+"String[] ExecuteQuery() 에러 222: "+e.toString()+super.getResultQuery());
		}
		finally { super.close(); }
		return strTemp;
	}
	
	/**  
	* sql 결과를 문자열 배열로 반환
	* @param sql - Query
	* @return String[]
	*/
	public int[] ExecuteQueryInt(){
		ResultSet rs = this.executeQuery();
		
		ArrayList<Integer> arrList = new ArrayList<Integer>();
	
		int [] intTemp = null;
		try{			
			while (rs.next())
			{					
				arrList.add(rs.getInt(1));
			}
			try	{
				rs.close();
			}
			catch (Exception e)
			{
				VbyP.errorLog("PreparedExecuteQueryManager"+"String[] ExecuteQuery() 에러 111: "+e.toString()+super.getResultQuery());
			}
			intTemp = new int[arrList.size()];
			int cnt = arrList.size();
			for (int i = 0; i < cnt; i++){
				intTemp[i] = arrList.get(i);
			}
		}
		catch (Exception e)
		{
			VbyP.errorLog("PreparedExecuteQueryManager"+"String[] ExecuteQuery() 에러 222: "+e.toString()+super.getResultQuery());
		}
		finally { super.close(); }
		return intTemp;
	}
	
	

	/**  
	* 쿼리 결과 숫자 반환
	* @param sql - Query
	* @return int 
	*/
	public int ExecuteQueryNum() {
		int num = 0;
		ResultSet rs = this.executeQuery();
		
		try {
			while (rs.next())
			{
				num = rs.getInt(1);
			}
			rs.close();
		}catch(SQLException e){
			VbyP.errorLog("PreparedExecuteQueryManager "+"ExecuteQueryNum 에러 : "+e.toString()+super.getResultQuery());
        	
		}		
		finally { super.close(); }
		return num;
	}

	/**  
	* 쿼리 결과 문자열 반환
	* @param sql - Query
	* @return String 
	*/
	public String ExecuteQueryString() {
		String str = "";
		ResultSet rs = this.executeQuery();
		try {
			while (rs.next())
			{
				str = rs.getString(1);
				if (str==null || str.equals("null")) str = "";
			}
	
			rs.close();
		}catch(SQLException e){
			VbyP.errorLog("PreparedExecuteQueryManager "+"ExecuteQueryString 에러 : "+e.toString()+super.getResultQuery());
        	
		}			
		finally { super.close(); }
		return str;
	}
	
	/**  
	* 쿼리 결과 문자열 반환
	* @param sql - Query
	* @return String 
	*/
	public byte[] ExecuteQueryByte() {
		byte [] str = null;
		ResultSet rs = this.executeQuery();
		try {
			while (rs.next())
			{
				str = rs.getBytes(1);
			}
	
			rs.close();
		}catch(SQLException e){
			VbyP.errorLog("PreparedExecuteQueryManager "+"ExecuteQueryString 에러 : "+e.toString()+super.getResultQuery());
        	
		}			
		finally { super.close(); }
		return str;
	}
	
	/**  
	* 쿼리 결과 INPUTSTREAM
	* @param sql - Query
	* @return String 
	*/
	public InputStream ExecuteQueryInputStream() {
		InputStream str = null;
		ResultSet rs = this.executeQuery();
		try {
			while (rs.next())
			{
				str = rs.getBinaryStream(1);
			}
	
			rs.close();
		}catch(SQLException e){
			VbyP.errorLog("PreparedExecuteQueryManager "+"ExecuteQueryInputStream 에러 : "+e.toString()+super.getResultQuery());
        	
		}			
		finally { super.close(); }
		return str;
	}
	
	private int arraySumCount(int[] arr) {
		
		int rslt = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == -2)
				rslt +=1;
			else if (arr[i] > 0)
				rslt += arr[i];
		}
		return rslt;
	}
	
	private String[] getColumnName(ResultSetMetaData rsmd ) {
		
		String[] arrString = null;
		String temp = null;
		
		try {
			
			int columnCount = rsmd.getColumnCount();
			arrString = new String[columnCount];
			
		    for (int i = 1; i < columnCount + 1; i++) {
		    	
		    	temp = null;
		    	temp = rsmd.getColumnName(i);
		    	if (temp == null) temp = rsmd.getColumnLabel(i);
		    	arrString[i-1] = temp;
		    }
		    
		}catch (SQLException e) {
			
			VbyP.errorLog("PreparedExecuteQueryManager "+"getColumnName 에러 : "+e.toString()+super.getResultQuery());
        	
        	super.close();
		}
	   
		
		return arrString;
		
	}
}
