<%@page import="java.util.ArrayList"%><%@page import="java.util.HashMap"%><%@page import="com.m.address.Address"%><%@page import="com.common.util.SLibrary"%><%@page import="com.common.util.ExcelManagerByPOI36"%><%@page import="com.common.VbyP"%><%@page import="java.sql.Connection"%><%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%

String[][] excelData = null;
String us = SLibrary.IfNull((String)session.getAttribute("user_id"));
Address addr = null;
String errorMsg = "";
Connection conn = null;

try {
	conn = VbyP.getDB();
	//test
	if (us == null) { throw new Exception("no login!"); }
	if (conn == null) { throw new Exception("db connection error!"); }

	VbyP.accessLog(us+" address excel call : "+ request.getRemoteAddr());
	
	addr = Address.getInstance();
	
	ArrayList<HashMap<String,String>> al=  addr.SelectMember(conn, us);
	
	int rowCount = al.size();
	
	excelData = new String[rowCount][];
	
	HashMap<String, String> data = null;

	//grpName, name, phone, memo
	for(int i = 0; i < rowCount; i++) {
		
		data = al.get(i);
		excelData[i] = new String[4];
		excelData[i][0] = data.get("grpName");
		excelData[i][1] = data.get("name");
		excelData[i][2] = data.get("phone");
		excelData[i][3] = data.get("memo");
		
	}
	
	ExcelManagerByPOI36 em = new ExcelManagerByPOI36();
	
	try {
		em.setTitle(new String[]{"그룹","이름" ,"전화번호","메모"}); 
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