package com.m.common;

import com.common.VbyP;
import com.common.db.PreparedExecuteQueryManager;
import com.common.properties.ReadProperties;
import com.common.properties.ReadPropertiesAble;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

/**
 * spam.properties 에서 유효성 체크
 *  1. 메시지 필터
 *  2. 접근 IP 체크
 * @author si
 *
 */
public class Refuse {
	
	public static final String REFUSE_FILE="refuse.properties";
	public static final String DIV="\\|\\|";
	
	public static boolean isRefuse(Hashtable<String, String> ht , String phone) {
		
		if (ht.containsKey(phone.replaceAll("-", "").trim()))
			return true;
		else 
			return false; 
	}
	
	public static Hashtable<String, String> getRefusePhone(String key) {
		
		Hashtable<String, String> hashTable = new Hashtable<String, String>();
		ReadPropertiesAble rp = ReadProperties.getInstance();
		
		String strRefuse = rp.getPropertiesFileValue(REFUSE_FILE, key);
		String[] arrRefuse = strRefuse.split(DIV);
		int cnt = arrRefuse.length;
		
		for (int i = 0; i < cnt; i++) {
			
			hashTable.put(arrRefuse[i], "");			
		}
		
		return hashTable;
	}
	
	public static Hashtable<String, String> getRefusePhoneFromDB(String user_id) {
		
		Connection conn = null;
		Hashtable<String, String> hashTable = new Hashtable<String, String>();
		
		try {
			String sql = VbyP.getSQL("getRefuseList");
			conn = VbyP.getDB();
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared(conn, sql);
			pq.setString(1, user_id);
			String [] arrRefuse = pq.ExecuteQuery();
			
			if (arrRefuse != null){
				
				int cnt = arrRefuse.length;		
				for (int i = 0; i < cnt; i++)	
					hashTable.put(arrRefuse[i], "");			
			}
		}catch (Exception e) {
			
		}
		finally {
			
			try {
				if ( conn != null ) conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("getRefusePhoneFromDB >> finally conn.close() Exception!"+e.toString()); 
			}
		}
		
		return hashTable;
	}
	
	public static Hashtable<String, String> getRefusePhoneFromDB() {
		
		Connection conn = null;
		Hashtable<String, String> hashTable = new Hashtable<String, String>();
		
		try {
			String sql = VbyP.getSQL("getRefuse");
			conn = VbyP.getDB();
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared(conn, sql);
			//pq.setString(1, user_id);
			String [] arrRefuse = pq.ExecuteQuery();
			
			if (arrRefuse != null){
				
				int cnt = arrRefuse.length;		
				for (int i = 0; i < cnt; i++)	
					hashTable.put(arrRefuse[i], "");			
			}
		}catch (Exception e) {
			
		}
		finally {
			
			try {
				if ( conn != null ) conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("getRefusePhoneFromDB >> finally conn.close() Exception!"+e.toString()); 
			}
		}
		
		return hashTable;
	}
	
	public static boolean isRefuseID(String user_id) {
		
		ReadPropertiesAble rp = ReadProperties.getInstance();		
		if (rp.isKey(REFUSE_FILE, user_id))
			return true;
		else
			return false;		
	}
	
}
