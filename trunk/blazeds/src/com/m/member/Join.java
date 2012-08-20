package com.m.member;

import java.sql.Connection;
import java.sql.SQLException;

import com.common.VbyP;
import com.common.db.PreparedExecuteQueryManager;
import com.common.util.SLibrary;

public class Join {
	
	public Join() {}
	
	public boolean idDupleCheck(String id) {
		
		boolean b = false;
		
		Connection conn = null;
		int count = 0;
		try {
			conn = VbyP.getDB();
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, VbyP.getSQL("joinIdDupleCheck") );
			pq.setString(1, SLibrary.IfNull(id));
			count = pq.ExecuteQueryNum();
		}catch(Exception e) {}
		finally {
			try {
				if ( conn != null )
					conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("Join >> conn.close() Exception!"); 
			}
		}
		
		if (count > 0)
			b = true;
		
		return b;
	}
	
	public boolean juminDupleCheck(String jumin) {
		
		boolean b = false;
		
		Connection conn = null;
		int count = 0;
		try {
			conn = VbyP.getDB();
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, VbyP.getSQL("joinJuminDupleCheck") );
			pq.setString(1, SLibrary.IfNull(jumin));
			count = pq.ExecuteQueryNum();
		}catch(Exception e) {}
		finally {
			try {
				if ( conn != null )
					conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("Join >> conn.close() Exception!"); 
			}
		}
		
		if (count > 0)
			b = true;
		
		return b;
	}
	
	public int insert(JoinVO vo) {
		
		Connection conn = null;
		int count = 0;

		try {
			conn = VbyP.getDB();
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, VbyP.getSQL("join") );
			pq.setString(1, SLibrary.IfNull(vo.getUser_id()));
			pq.setString(2, SLibrary.IfNull(vo.getPassword()));
			pq.setString(3, SLibrary.IfNull(vo.getName()));
			pq.setString(4, SLibrary.IfNull(vo.getJumin()));
			pq.setString(5, SLibrary.IfNull(vo.getHp()));
			pq.setString(6, SLibrary.IfNull(vo.getReturnPhone()));
			pq.setString(7, SLibrary.IfNull(vo.getEmail()));
			
			count = pq.executeUpdate();
			
			
		}catch(Exception e) {}
		finally {
			try {
				if ( conn != null )
					conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("Join >> conn.close() Exception!"); 
			}
		}
		
		
		return count;
	}
	
	public int update(JoinVO vo) {
		
		Connection conn = null;
		int count = 0;

		try {
			conn = VbyP.getDB();
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, VbyP.getSQL("modify") );
			
			pq.setString(1, SLibrary.IfNull(vo.getPassword()));
			pq.setString(2, SLibrary.IfNull(vo.getName()));
			pq.setString(3, SLibrary.IfNull(vo.getJumin()));
			pq.setString(4, SLibrary.IfNull(vo.getHp()));
			pq.setString(5, SLibrary.IfNull(vo.getReturnPhone()));
			pq.setString(6, SLibrary.IfNull(vo.getUser_id()));
			count = pq.executeUpdate();
			
			
		}catch(Exception e) {}
		finally {
			try {
				if ( conn != null )
					conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("update >> conn.close() Exception!"); 
			}
		}
		
		
		return count;
	}
	
	public int updateNew(JoinVO vo) {
		
		Connection conn = null;
		int count = 0;

		try {
			conn = VbyP.getDB();
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, VbyP.getSQL("modifyNew") );
			
			pq.setString(1, SLibrary.IfNull(vo.getPassword()));
			pq.setString(2, SLibrary.IfNull(vo.getHp()));
			pq.setString(3, SLibrary.IfNull(vo.getEmail()));
			pq.setString(4, SLibrary.IfNull(vo.getEmailYN()));
			pq.setString(5, SLibrary.IfNull(vo.getHpYN()));
			pq.setString(6, SLibrary.IfNull(vo.getUser_id()));
			count = pq.executeUpdate();
			
			
		}catch(Exception e) {}
		finally {
			try {
				if ( conn != null )
					conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("updateNew >> conn.close() Exception!"); 
			}
		}
		
		
		return count;
	}
}
