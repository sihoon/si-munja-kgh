<%@page import="com.m.common.PointManager"%>
<%@page import="com.m.member.JoinVO"%>
<%@page import="com.m.member.Join"%>
<%@page import="com.common.util.SLibrary"%>
<%@ page import="com.common.VbyP" %>
<%@ page import="java.sql.Connection" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%
	
	String session_id = SLibrary.IfNull((String)session.getAttribute("user_id"));

	String passwd1 = VbyP.getPOST(request.getParameter("passwd1"));
	String passwd2 = VbyP.getPOST(request.getParameter("passwd2"));
	
	String hp = VbyP.getPOST(request.getParameter("hp"));
	String email = VbyP.getPOST(request.getParameter("email"));
	String hpYN = VbyP.getPOST(request.getParameter("smsok"));
	String emailYN = VbyP.getPOST(request.getParameter("emailok"));

	try {
		Join join = new Join();
		
		if ( SLibrary.isNull(session_id)) throw new Exception("로그인 후 이용 가능합니다.");
		if ( SLibrary.isNull(hp)) throw new Exception("핸드폰을 입력하세요.");
		if ( SLibrary.isNull(email)) throw new Exception("이메일을 입력하세요.");
		if (passwd1.length() < 6) throw new Exception("비밀번호는 6자리 이상 입력하세요.");
		if (!passwd1.equals(passwd2)) throw new Exception("비밀번호 확인이 다릅니다.");
		
		
		JoinVO vo = new JoinVO();
		vo.setUser_id(session_id);
		vo.setPassword(passwd2);
		vo.setHp(hp);
		vo.setEmail(email);
		vo.setHpYN(hpYN);
		vo.setEmailYN(emailYN);
		
		int rslt = join.updateNew(vo);
		
		if (rslt < 1) {
			out.println(SLibrary.alertScript("회원수정에 실패 하였습니다.", ""));
		}else {
			out.println(SLibrary.alertScript("회원수정이 완료 되었습니다.", "parent.window.location.href='../';"));
		}
		
	} catch (Exception e) {
		VbyP.errorLog("member/_modify.jsp ==> " + e.toString());
		out.println(SLibrary.alertScript(e.getMessage(), ""));
		System.out.println(e.toString());
	} finally {}
	
%>
