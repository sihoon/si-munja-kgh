<%@page import="java.sql.SQLException"%>
<%@page import="com.common.db.PreparedExecuteQueryManager"%>
<%@page import="com.m.member.Join"%><%@page import="com.common.util.SLibrary"%><%@ page import="com.common.VbyP" %><%@ page import="java.sql.Connection" %><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%
	System.out.println(request.getParameter("user_id"));
	String user_id = VbyP.getPOST(request.getParameter("user_id"));
	String user_pw = VbyP.getPOST(request.getParameter("user_pw"));
	Connection conn = null;
	
	try {
		
		conn = VbyP.getDB();
		if ( SLibrary.isNull(user_id) )	throw new Exception("사용자 아이디를 입력하세요.");
		else if ( SLibrary.isNull(user_pw) ) throw new Exception("비밀번호를 입력하세요.");
		else {

			
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, VbyP.getSQL("login") );
			pq.setString(1, user_id);
			pq.setString(2, user_pw);
			
			int rslt = pq.ExecuteQueryNum();
			
			if (rslt != 1) {
				throw new Exception("로그인 정보가 일치 하지 않습니다.");
			}else {

				pq.setPrepared( conn, VbyP.getSQL("loginUpdateTime") );
				pq.setString(1, SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss"));
				pq.setString(2, user_id);
				rslt = pq.executeUpdate();
				
				session.setAttribute("user_id", user_id);
				VbyP.accessLog(user_id+" Login");
				out.println(SLibrary.alertScript("", "parent.window.location.href='../index.jsp?content=normal'"));
			}
		}

	}catch (Exception e) {
		out.println(SLibrary.alertScript(e.getMessage(), ""));		
	}
	finally {
		
		try { if ( conn != null )	conn.close(); }catch(SQLException e) {	VbyP.errorLog("login >> conn.close() Exception!");}		
		conn = null;
		
	}

	
%>
