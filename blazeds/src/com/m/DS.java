package com.m;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.common.VbyP;
import com.m.notic.NoticDAO;
import com.m.notic.NoticVO;

public class DS {

	/*
	public ArrayList<NoticVO> getNotic() {
		
		Connection conn = null;
		ArrayList<NoticVO> rslt = null;
		NoticDAO nd = null;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  공지사항 리스트 요청");
			
			rslt = nd.getList(conn);
			
		}catch (Exception e) {}	
		finally {
			try { 
				if ( conn != null ) 
					conn.close();
			}catch(SQLException e) { VbyP.errorLog("getNotic >> conn.close() Exception!"); }
		}
		
		return rslt;
	}
	
	public ArrayList<NoticVO> getNoticMain(int cnt) {
		
		Connection conn = null;
		ArrayList<NoticVO> rslt = null;
		NoticDAO nd = null;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  공지사항 리스트 요청(main)");
			
			rslt = nd.getList(conn,cnt);
			
		}catch (Exception e) {}	
		finally {
			try { 
				if ( conn != null ) 
					conn.close();
			}catch(SQLException e) { VbyP.errorLog("getNoticMain >> conn.close() Exception!"); }
		}
		
		return rslt;
	}
	
	public int insertNotic(NoticVO vo) {
		
		Connection conn = null;
		NoticDAO nd = null;
		int rslt = 0;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  공지사항 추가 요청");
			
			rslt = nd.insert(conn, vo);
			
		}catch (Exception e) {}	
		finally {
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("insertNotic >> conn.close() Exception!"); }
		}
		
		return rslt;
	}
	
	public int modifyNotic(NoticVO vo) {
		
		Connection conn = null;
		NoticDAO nd = null;
		int rslt = 0;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  공지사항 수정 요청");
			
			rslt = nd.modify(conn, vo);
			
		}catch (Exception e) {}	
		finally {
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("modifyNotic >> conn.close() Exception!"); }
		}
		
		return rslt;
	}
	
	public int deleteNotic(int idx) {
		
		Connection conn = null;
		NoticDAO nd = null;
		int rslt = 0;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  공지사항 삭제 요청");
			
			rslt = nd.delete(conn, idx);
			
		}catch (Exception e) {}	
		finally {
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("deleteNotic >> conn.close() Exception!"); }
		}
		
		return rslt;
	}
	
	public int updateCntNotic(int idx) {
		
		Connection conn = null;
		NoticDAO nd = null;
		int rslt = 0;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  공지사항 카운트 증가 요청");
			
			rslt = nd.updateCnt(conn, idx);
			
		}catch (Exception e) {}	
		finally {
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("updateCntNotic >> conn.close() Exception!"); }
		}
		
		return rslt;
	}
	*/
}
