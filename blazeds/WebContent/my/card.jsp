<%@page import="java.sql.SQLException"%>
<%@page import="com.common.VbyP"%>
<%@page import="com.m.member.SessionManagement"%>
<%@page import="com.m.member.UserInformationVO"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.common.util.SLibrary"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%

String user_id = SLibrary.IfNull((String)session.getAttribute("user_id"));
Connection conn = null;
UserInformationVO vo = null;
SessionManagement ses = null;

try {
	conn = VbyP.getDB();
	
	ses = new SessionManagement();
	if ( !SLibrary.IfNull( (String)session.getAttribute("user_id") ).equals("") )
		vo = ses.getUserInformation(conn, SLibrary.IfNull( (String)session.getAttribute("user_id") ));
}catch (Exception e) {}
finally {
	
	try {
		if ( conn != null )	conn.close();
	}catch(SQLException e) {
		VbyP.errorLog("billing.jsp >> conn.close() Exception!"); 
	}
	conn = null;
}

%>

<% if (vo == null) { %>
<fieldset id="login">
    <legend>로그인</legend>
    <label class="idlabel ti" for="user_id">아이디</label><input type="text" id="user_id" name="user_id" />
    <label class="pwlabel ti" for="user_pw">비밀번호</label><input type="text" id="user_pw" name="user_pw" />
    <button class="loginBtn ti" >로그인</button>
    <button class="joinBtn ti">회원가입</button>
    <button class="findBtn ti">아이디찾기</button>
</fieldset>
<% } else { %>
<fieldset id="loginInfo">
    <legend>로그인정보</legend>
    <p><span class="name"><%=vo.getUser_name() %></span> 님 안녕하세요.</p>
   	<div><img src="images/usenum.gif" />&nbsp;<span class="cnt"><%=SLibrary.addComma( vo.getPoint() ) %></span><img src="images/cnt.gif" /></div>
   	<img src="images/btn_cashbuy.gif" class="hand" alt="충전하기" onclick="window.location.href='?content=billing'" />
    <div class="function"><img src="images/edit.gif" class="hand" alt="정보수정"/>&nbsp;<img src="images/logout.gif" onclick="window.location.href='member/_logout.jsp'" class="hand" alt="로그아웃" /></div>
    <div class="cuponBox"><input type="text" name="cupon" class="cuponInput" />&nbsp;&nbsp;<img src="images/btn_coupon.gif" class="hand" alt="쿠폰등록" /></div>
</fieldset>
<% }%>

<form name="formBilling" target="nobody" mehtod="post" action="billing/payreq.jsp" >
	<input type="hidden" name="smethod" value="" />
	<input type="hidden" name="amount" value="" />
</form>

<p id="companyTitle" class="ti">회사소개</p>

<div id="companyBox" class="ti" >좋은 회사 랍니다.</div>
<p id="free" class="ti">무료문자</p>
<a id="cost" class="ti" href="">저렴하고 안정적인 문자서비스를 찾으십니까? 단가표 보기</a>
<p id="custom" class="ti">Custom Center : 070-7510-8489, Fax: 031)970-8489</p>

