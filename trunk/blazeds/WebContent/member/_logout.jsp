<%@page import="com.common.util.SLibrary"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%
	
	session.invalidate();
	out.println(SLibrary.alertScript("로그아웃 되었습니다.", "window.location.href='../'"));
%>
