package com.m.member;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.SQLException;

import com.common.VbyP;
import com.common.db.PreparedExecuteQueryManager;
import com.common.util.SLibrary;
import com.m.common.BooleanAndDescriptionVO;
import com.m.common.PointManager;

import flex.messaging.FlexSession;
import flex.messaging.FlexContext;

public class SessionManagement {
	
	private void setSession(String user_id) {
		
		Connection conn = null;
		int rslt = 0;
		try {
			conn = VbyP.getDB();
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, VbyP.getSQL("loginUpdateTime") );
			pq.setString(1, SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss"));
			pq.setString(2, user_id);
			rslt = pq.executeUpdate();
		}catch(Exception e) {}
		finally {
			try {
				if ( conn != null )
					conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("setSession >> conn.close() Exception!"); 
			}
		}
		
		if (rslt == 0)
			VbyP.accessLog(user_id+" >> 占싸깍옙占쏙옙 占시곤옙 占쏙옙占쏙옙占쏙옙트 占쏙옙占쏙옙");
		
		FlexSession session =  FlexContext.getFlexSession();
		session.setAttribute("user_id", user_id);
		VbyP.accessLog(user_id+" Login");
		
	}
	
	private void setSessionAdmin(String user_id) {
		
		Connection conn = null;
		int rslt = 0;
		try {
			conn = VbyP.getDB();
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, VbyP.getSQL("loginAdminUpdateTime") );
			pq.setString(1, SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss"));
			pq.setString(2, user_id);
			rslt = pq.executeUpdate();
		}catch(Exception e) {}
		finally {
			try {
				if ( conn != null )
					conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("setSession >> conn.close() Exception!"); 
			}
		}
		
		if (rslt == 0)
			VbyP.accessLog(user_id+" >> 占싸깍옙占쏙옙 占시곤옙 占쏙옙占쏙옙占쏙옙트 占쏙옙占쏙옙");
		
		FlexSession session =  FlexContext.getFlexSession();
		session.setAttribute("admin_id", user_id);
		VbyP.accessLog(user_id+" Admin Login");
		
	}
	
	public void session_logout() {
				
		FlexSession session =  FlexContext.getFlexSession();
		session.invalidate();
	}
	
	public String getSession() {
		
		FlexSession session = FlexContext.getFlexSession();
		
		if ( session.getAttribute("user_id") == null )
			return null;
		else
			return session.getAttribute("user_id").toString();

	}
	
	public String getAdminSession() {
		
		FlexSession session = FlexContext.getFlexSession();
		
		if ( session.getAttribute("admin_id") == null )
			return null;
		else
			return session.getAttribute("admin_id").toString();

	}
	
	public boolean bSession() {
		
		String user_id = getSession();
		if (user_id != null && !user_id.equals(""))
			return true;
		else
			return false;
	}
	
	public boolean bAdminSession() {
		
		String user_id = getAdminSession();
		if (user_id != null && !user_id.equals(""))
			return true;
		else
			return false;
	}
	
	public BooleanAndDescriptionVO login(Connection conn, String user_id, String password) {
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("login") );
		pq.setString(1, user_id);
		pq.setString(2, password);
		
		int rslt = pq.ExecuteQueryNum();
		
		if (rslt != 1) {
			
			rvo.setstrDescription("잘못된 정보 입니다.");
		}else {
			
			rvo.setbResult(true);
		}
		
		if (rvo.getbResult())
			this.setSession(user_id);
		
		
		return rvo;
	}
	
	public BooleanAndDescriptionVO loginSuper(Connection conn, String user_id, String password) {
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		
		pq.setPrepared( conn, VbyP.getSQL("loginSuper") );
		pq.setString(1, user_id);
		
		
		int rslt = pq.ExecuteQueryNum();
		
		if (rslt != 1) {
			
			rvo.setstrDescription("잘못된 정보 입니다.");
		}else {
			
			rvo.setbResult(true);
		}
		
		if (rvo.getbResult())
			this.setSession(user_id);
		
		
		return rvo;
	}
	
	public BooleanAndDescriptionVO loginAdmin(Connection conn, String user_id, String password) {
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("loginAdmin") );
		pq.setString(1, user_id);
		pq.setString(2, password);
		
		int rslt = pq.ExecuteQueryNum();
		
		if (rslt != 1) {
			
			rvo.setstrDescription("잘못된 정보입니다.");
		}else {
			
			rvo.setbResult(true);
		}
		
		if (rvo.getbResult())
			this.setSessionAdmin(user_id);
		
		
		return rvo;
	}
	
	
	public UserInformationVO getUserInformation(Connection conn) {
		
		UserInformationVO vo = new UserInformationVO();
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("userInformation") );
		pq.setString(1, this.getSession());
		HashMap<String, String> hm= pq.ExecuteQueryCols();
		
		hm.put("point", Integer.toString( PointManager.getInstance().getUserPoint( conn, this.getSession() ) ) );
		
		vo.setHashMap(hm);
		return vo;
	}
	
	public UserInformationVO getUserInformation(Connection conn, String user_id) {
		
		UserInformationVO vo = new UserInformationVO();
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("userInformation") );
		pq.setString(1, user_id);
		HashMap<String, String> hm= pq.ExecuteQueryCols();
		
		hm.put("point", Integer.toString( PointManager.getInstance().getUserPoint( conn, user_id ) ) );
		
		vo.setHashMap(hm);
		return vo;
	}
	
	public UserInformationVO getUserInformation(String user_id) {
		
		Connection conn = null;
		UserInformationVO vo = null;
		try {
			
			conn = VbyP.getDB();
			if ( !SLibrary.IfNull( user_id ).equals("") )
				vo = this.getUserInformation(conn, user_id);
		}catch (Exception e) {}
		finally {
			
			try {
				if ( conn != null )
					conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("getUserInformation >> conn.close() Exception!"); 
			}
		}
		
		return vo;
	}
}
