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

String user_id = SLibrary.IfNull((String)session.getAttribute("user_id"));
Connection conn = null;
UserInformationVO vo = null;
SessionManagement ses = null;

int idx = 0;
Billing billing = null;
HashMap<String, String> hm = null;

try {
	conn = VbyP.getDB();
	
	ses = new SessionManagement();
	if ( !SLibrary.IfNull( (String)session.getAttribute("user_id") ).equals("") )
		vo = ses.getUserInformation(conn, SLibrary.IfNull( (String)session.getAttribute("user_id") ));
	else {
		throw new Exception("로그인 후 이용 가능 합니다.");
	}
	idx = SLibrary.intValue(SLibrary.IfNull(request.getParameter("idx")));
	if (idx <= 0) throw new Exception("필수 정보가 없습니다.");
	
	billing = Billing.getInstance();
	hm = billing.getBilling(conn, idx);

%>
<form name="form" method="post" action="_tax.jsp">
<input type="hidden" name="billing_idx" value="<%=idx %>" />

<table border="1" width="100%" cellpadding="0" cellspacing="0">
	<tr><td>아이디</td><td><%=vo.getUser_id() %></td></tr>
	<tr><td>금액</td><td><%=SLibrary.addComma(SLibrary.IfNull(hm, "amount")) %></td></tr>
	<tr><td>결제일</td><td><%=SLibrary.IfNull(hm, "timeWrite") %></td></tr>
	<tr><td>사업자명</td><td><input type="text" name="comp_name" value="<%=SLibrary.IfNull(hm, "comp_name") %>" /></td></tr>
	<tr><td>사업자번호</td><td><input type="text" name="comp_no" value="<%=SLibrary.IfNull(hm, "name") %>" /></td></tr>
	<tr><td>대표자명</td><td><input type="text" name="name" value="<%=SLibrary.IfNull(hm, "comp_no") %>" /></td></tr>
	<tr><td>사업장주소</td><td><input type="text" name="addr" value="<%=SLibrary.IfNull(hm, "addr") %>" /></td></tr>
	<tr><td>업태</td><td><input type="text" name="upte" value="<%=SLibrary.IfNull(hm, "upte") %>" /></td></tr>
	<tr><td>종목</td><td><input type="text" name="upjong" value="<%=SLibrary.IfNull(hm, "upjong") %>" /></td></tr>
	<tr><td>발행메일주소</td><td><input type="text" name="email" value="<%=SLibrary.IfNull(hm, "email") %>" /></td></tr>
</table>
</form>
<button onclick="checkTax();">신청하기</button><button onclick="window.close()">취소</button>
<%

}catch (Exception e) {
	out.println(SLibrary.alertScript(e.getMessage(), "window.close()"));
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

