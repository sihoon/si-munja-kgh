<%@page import="java.sql.SQLException"%>
<%@page import="com.common.util.SLibrary"%>
<%@page import="com.m.common.AdminSMS"%>
<%@page import="com.common.VbyP"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="javax.mail.Message"%>
<%@page import="javax.mail.Transport"%>
<%@page import="javax.mail.internet.InternetAddress"%>
<%@page import="javax.mail.internet.MimeMessage"%>
<%@page import="javax.mail.Session"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.*"%>
<%@ page import="java.security.*"%>
<%@ page import="javax.crypto.*"%>
<%@ page import="javax.crypto.spec.DESKeySpec"%>
<%@ page import="javax.crypto.spec.DESedeKeySpec"%>


<%	
	
		
		Connection conn = null;
		String message = "대량모니터링 테스트";
		int sendCount = 100;
		String user_id="starwarssi";
    	try {
    		conn = VbyP.getDB();
    		AdminSMS asms = AdminSMS.getInstance();
    		String tempMessage = ( SLibrary.getByte( message ) > 15 )? SLibrary.cutBytes(message, 20, true, "...") : message ;
    		asms.sendAdmin(conn, 
    				"[대량발송]\r\n" + user_id + "\r\n"+Integer.toString( sendCount )+"건\r\n" 
    				+ tempMessage  );

    	}catch (Exception e) {
			
			System.out.println(e.toString());
		}
		finally {
			
			try {
				if ( conn != null ) conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("sendSMS >> finally conn.close() or connSMS.close() Exception!"+e.toString()); 
			}
			conn = null;
		}
%>