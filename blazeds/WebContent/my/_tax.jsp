<%@page import="java.util.HashMap"%>
<%@page import="com.m.billing.Billing"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.common.VbyP"%>
<%@page import="com.m.member.SessionManagement"%>
<%@page import="com.m.member.UserInformationVO"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.common.util.SLibrary"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Content-Script-Type" content="text/javascript" />
	<meta http-equiv="Content-Style-Type" content="text/css" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title> new 119 </title>
    <link type="text/css" rel="stylesheet" href="../css/base.css?ver=1.0"/>
    <link type="text/css" rel="stylesheet" href="../css/main.css?ver=1.2"/>
	<script type="text/javascript"  language="javascript" src="../js/jquery-1.7.1.min.js?ver=1.0"></script>
	<script type="text/javascript"  language="javascript" src="../js/common.js?ver=1.1"></script>
    
</head><body><%

int billing_idx = SLibrary.intValue( SLibrary.IfNull(request.getParameter("billing_idx")) );
String comp_name = VbyP.getPOST(SLibrary.IfNull(request.getParameter("comp_name")));
String comp_no = SLibrary.IfNull(request.getParameter("comp_no"));
String name = VbyP.getPOST(SLibrary.IfNull(request.getParameter("name")));
String addr = VbyP.getPOST(SLibrary.IfNull(request.getParameter("addr")));
String upte = VbyP.getPOST(SLibrary.IfNull(request.getParameter("upte")));
String upjong = VbyP.getPOST(SLibrary.IfNull(request.getParameter("upjong")));
String email = SLibrary.IfNull(request.getParameter("email"));


String user_id = SLibrary.IfNull((String)session.getAttribute("user_id"));
Connection conn = null;
UserInformationVO vo = null;
SessionManagement ses = null;

Billing billing = null;
HashMap<String, String> hm = null;
String session_id = null;

try {
	conn = VbyP.getDB();
	
	ses = new SessionManagement();
	session_id = SLibrary.IfNull( (String)session.getAttribute("user_id") );
	if ( session_id.equals("") ) throw new Exception("로그인 후 이용 가능 합니다.");
	
	if (billing_idx <= 0 || SLibrary.isNull(comp_name) 
			|| SLibrary.isNull(comp_no) 
			|| SLibrary.isNull(name) 
			|| SLibrary.isNull(addr)
			|| SLibrary.isNull(addr)
			|| SLibrary.isNull(upjong)
			|| SLibrary.isNull(email)
			) throw new Exception("필수 정보가 없습니다.");

	
	billing = Billing.getInstance();
	int rslt = billing.setTax(conn,
			 billing_idx, 
			 user_id, 
			 comp_name, 
			 comp_no, 
			 name, 
			 addr, 
			 upte, 
			 upjong, 
			 email, 
			 "N");
	
	if (rslt > 0) out.println(SLibrary.alertScript("요청 되었습니다.", "window.close()"));
%>

<%

}catch (Exception e) {
	out.println(SLibrary.alertScript(e.getMessage(), "window.history.back()"));
}
finally {

try {
	if ( conn != null )	conn.close();
}catch(SQLException e) {
	VbyP.errorLog("tax.jsp >> conn.close() Exception!"); 
}
conn = null;
}

%>
</body>
</html>

