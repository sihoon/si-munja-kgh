<%@page import="com.m.member.Join"%><%@page import="com.common.util.SLibrary"%><%@ page import="com.common.VbyP" %><%@ page import="java.sql.Connection" %><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%
	
	String userId = request.getParameter("user_id");
	Connection conn = null;
	String rslt = "no";
	try {
		Join join = new Join();
		
		if (!SLibrary.IfNull( (String)session.getAttribute("munja119JoinStep") ).equals("step2@Session")){
			rslt = "no";
		} else if (!join.idDupleCheck(userId)) {
			rslt = "yes";
		} 
		
	} catch (Exception e) {
		VbyP.errorLog("/member/id_check.jsp ==> " + e.toString());
		System.out.println(e.toString());
	} finally {
		out.println(rslt);
		System.out.println(rslt);
	}
	
%>
