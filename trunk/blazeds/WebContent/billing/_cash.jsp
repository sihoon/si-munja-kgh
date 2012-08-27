<%@page import="com.m.member.SessionManagement"%>
<%@page import="com.m.member.UserInformationVO"%>
<%@page import="com.m.common.BooleanAndDescriptionVO"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.m.common.AdminSMS"%>
<%@page import="com.m.billing.Billing"%>
<%@page import="com.m.common.PointManager"%>
<%@page import="com.m.member.JoinVO"%>
<%@page import="com.m.member.Join"%>
<%@page import="com.common.util.SLibrary"%>
<%@ page import="com.common.VbyP" %>
<%@ page import="java.sql.Connection" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%

	Connection conn = null;
	Billing billing = null;
	String smethod = "";
	String amount = "";
	String cash = "";
	String cashName = "";
	
	UserInformationVO vo = null;
	
	try {
		String user_id = SLibrary.IfNull((String)session.getAttribute("user_id"));
		if (user_id == null || user_id.equals("")) throw new Exception("로그인 되어 있지 않습니다.");
		
		smethod = VbyP.getPOST(request.getParameter("smethod"));
		amount = VbyP.getPOST(request.getParameter("amount"));
		if ( SLibrary.isNull(amount)) throw new Exception("금액이 없습니다.");
		cash = VbyP.getPOST(request.getParameter("cash"));
		if ( SLibrary.isNull(cash)) throw new Exception("계좌선택이 없습니다.");
		cashName = VbyP.getPOST(request.getParameter("cashName"));
		if ( SLibrary.isNull(cash)) throw new Exception("입금자명이 없습니다.");
		
		conn = VbyP.getDB();
		if (conn == null) throw new Exception("DB연결이 되어 있지 않습니다.");
		billing = Billing.getInstance();
		
		VbyP.accessLog(" >> 무통장 예약 요청 "+ user_id);
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		
		rvo = billing.setCash( conn , user_id, cash, amount, smethod, cashName );
		
		if (!rvo.getbResult()) {
			out.println(SLibrary.alertScript("예약 실패"+rvo.getstrDescription(), ""));
			System.out.println(rvo.getstrDescription());
		}else {
			AdminSMS asms = AdminSMS.getInstance();
			String tempMessage = "[무통장예약] "+user_id+" , "+cashName+" , "+cash+" , "+amount+" , "+smethod;
			asms.sendAdmin(conn, tempMessage );
			
			vo = new SessionManagement().getUserInformation(conn, user_id);
			
			if (!SLibrary.isNull( vo.getHp() ) ) {
				
				String userMessage = "[munja119] \r\n "+cash+" 으로 입금예약 되었습니다.";
				VbyP.accessLog(" >> 무통장 예약 요청 발송("+vo.getHp()+") : "+ userMessage);
				asms.sendAdmin(conn, userMessage , vo.getHp() , "16000816");
			}
			out.println(SLibrary.alertScript("예약 되었습니다.", ""));
		}
		
		
		
	
	}catch (Exception e) {
		
		System.out.println(e.toString());
		
	}	finally {			
		try { if ( conn != null ) conn.close();
		}catch(SQLException e) { VbyP.errorLog("setCash >> conn.close() Exception!"); }
		conn = null;
	}

	
%>
