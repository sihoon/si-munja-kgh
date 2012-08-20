package com.m.home;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.common.VbyP;
import com.common.db.PreparedExecuteQueryManager;
import com.common.util.SLibrary;


public class Home {
	
	static Home hm = new Home();
	
	public static Home getInstance() {
		return hm;
	}
	
	private Home(){};
	
	public ArrayList<HashMap<String, String>> getNotices(Connection conn) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("noticList") );
		
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		al = pq.ExecuteQueryArrayList();
		return al;

	}
	
	public String[] getMainEmt(Connection conn, String gubun, String cate, int from, int count) {
		
		String[] rslt = new String[count];
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("selectMainEmt") );
		pq.setString(1, gubun);
		pq.setString(2, cate);
		pq.setInt(3, from);
		pq.setInt(4, count);
		
		String[] temp = pq.ExecuteQuery();
		
		for (int i = 0; i < rslt.length; i++) {
			rslt[i] = SLibrary.IfNull( (temp.length > i)?temp[i]:"" );
		}
		return rslt;
	}
	
	public String[] getMainLMS(Connection conn, String gubun, String cate, int from, int count) {
		
		String[] rslt = new String[count];
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("selectMainLMS") );
		pq.setString(1, gubun);
		pq.setString(2, cate);
		pq.setInt(3, from);
		pq.setInt(4, count);
		
		String[] temp = pq.ExecuteQuery();
		
		for (int i = 0; i < rslt.length; i++) {
			rslt[i] = SLibrary.IfNull( (temp.length > i)?temp[i]:"" );
		}
		return rslt;
	}
	
	public ArrayList<HashMap<String, String>> getMainMms(Connection conn, String gubun, String cate, int from, int count) {
		
		ArrayList<HashMap<String, String>> rslt = null;
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("selectMainMms") );
		pq.setString(1, gubun);
		pq.setString(2, cate);
		pq.setInt(3, from);
		pq.setInt(4, count);
		
		rslt = pq.ExecuteQueryArrayList();
		
		return rslt;
	}
	
	public String[] getMainCate(Connection conn, String gubun) {
		
		String[] rslt = null;
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("selectMainEmtCate") );
		pq.setString(1, gubun);
		
		rslt = pq.ExecuteQuery();
		
		return rslt;
	}
	
	public String[] getMainCateLMS(Connection conn, String gubun) {
		
		String[] rslt = null;
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("selectMainLMSCate") );
		pq.setString(1, gubun);
		
		rslt = pq.ExecuteQuery();
		
		return rslt;
	}
	
	public String[] getMainMmsCate(Connection conn, String gubun) {
		
		String[] rslt = null;
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("selectMainMmsCate") );
		pq.setString(1, gubun);
		
		rslt = pq.ExecuteQuery();
		
		return rslt;
	}
	
	public String[] getLMSCate(Connection conn, String gubun) {
		
		String[] rslt = null;
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("selectMainEmtCateLMS") );
		pq.setString(1, gubun);
		
		rslt = pq.ExecuteQuery();
		
		return rslt;
	}
	
	public String[] getMMSCate(Connection conn, String gubun) {
		
		String[] rslt = null;
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("selectMainEmtCateMMS") );
		pq.setString(1, gubun);
		
		rslt = pq.ExecuteQuery();
		
		return rslt;
	}
}
