<%@page import="java.util.List"%><%@page import="com.m.sent.SentVO"%><%@page import="com.m.Web"%><%@page import="java.util.ArrayList"%><%@page import="java.util.HashMap"%><%@page import="com.m.address.Address"%><%@page import="com.common.util.SLibrary"%><%@page import="com.common.util.ExcelManagerByPOI36"%><%@page import="com.common.VbyP"%><%@page import="java.sql.Connection"%><%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%

String[][] excelData = null;
String us = SLibrary.IfNull((String)session.getAttribute("user_id"));
Web web = null;
String errorMsg = "";

int idx = 0;
String line = "";

try {
	
	VbyP.accessLog(us+" sent excel call : "+ request.getRemoteAddr());
	
	idx = SLibrary.intValue(request.getParameter("idx"));
	line = SLibrary.IfNull(request.getParameter("line"));
	
	
	if (us == null) { throw new Exception("no login!"); }
	if (idx == 0) { throw new Exception("idx zero error!"); }
	if (SLibrary.isNull(line)) { throw new Exception("line null error!"); }

	
	web = new Web();
	
	List<SentVO> al=  web.getSentList(idx, line);
	
	int rowCount = al.size();
	
	excelData = new String[rowCount][];
	
	SentVO data = null;

	//grpName, name, phone, memo
	for(int i = 0; i < rowCount; i++) {
		
		data = al.get(i);
		excelData[i] = new String[3];
		excelData[i][0] = data.getPhone();
		excelData[i][1] = data.getResult();
		excelData[i][2] = data.getResultDate();
		
	}
	
	ExcelManagerByPOI36 em = new ExcelManagerByPOI36();
	
	try {
		em.setTitle(new String[]{"전화번호","결과" ,"결과시간"}); 
		em.WriteAndDownLoad( response , VbyP.getFILE("munjaya-"+SLibrary.getDateTimeString("yyyy-MM-dd")) , excelData );
		
	}catch(Exception e ) {
		out.println(SLibrary.alertScript(e.toString() , ""));
	}
	
}catch (Exception e) {
	errorMsg = e.getMessage();
}
finally {
	
	
	if (!SLibrary.isNull(errorMsg)) {
		out.println(SLibrary.alertScript(errorMsg, ""));
	}
}
%>