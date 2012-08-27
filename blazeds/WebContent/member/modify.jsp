<%@page import="java.sql.SQLException"%>
<%@page import="com.m.member.SessionManagement"%>
<%@page import="com.m.member.UserInformationVO"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.common.VbyP"%>
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
<form name="loginForm" method="post" target="nobody" action="member/_login.jsp" >
<fieldset id="login" >
    <legend>로그인</legend>
    <label class="idlabel ti" for="user_id">아이디</label><input type="text" id="user_id" name="user_id" value="" />
    <label class="pwlabel ti" for="user_pw">비밀번호</label><input type="password" id="user_pw" name="user_pw" />
    <button class="loginBtn ti" onclick="logincheck()">로그인</button>
    <button class="joinBtn ti">회원가입</button>
    <button class="findBtn ti">아이디찾기</button>
</fieldset>
</form>
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
<form name="form" method="post" action="member/_modify.jsp">
<div id="joinBox">
	<p class="joinAd ti">업계최저가격 문자서비스</p>
	<p class="join3Title ti">정보입력</p>
	<table class="inputTable" cellpadding="0" cellspacing="0" border="0">
		<tr><td class="t">비밀번호</td><td class="c"><input type="password" name="passwd1" id="passwd1" class="txt" value="" /> <span id="passwd1_check">영, 숫자조합 (4~12자)</span></td></tr>
		<tr><td class="t">비밀번호확인</td><td class="c"><input type="password" name="passwd2" id="passwd2" class="txt" value=""/> <span id="passwd2_check">비밀번호를 다시 한번 입력해 주세요.</span></td></tr>
		<tr><td class="t">핸드폰</td><td class="c"><input type="text" name="hp" class="txt" value="<%=vo.getHp()%>"/></td></tr>
		<tr><td class="t">이메일</td><td class="c"><input type="text" name="email" class="txt" value="<%=vo.getEmail() %>"/> <span>메일수신이 가능한 이메일주소</span></td></tr>
		<tr><td class="t">메일수신여부</td>
			<td class="c"><input type="radio" name="emailok" value="Y" id="emailok" <%=vo.getEmailYN().equals("Y") ? "checked=\"checked\"" : "" %> /><label for="emailok">예</label>
		<input type="radio" name="emailok" value="N" id="emailno" <%=vo.getEmailYN().equals("N") ? "checked=\"checked\"" : "" %> /><label for="emailno">아니오</label> &nbsp;&nbsp;<span>이벤트, 제품 정보, 주문정보 등에 대한 메일링 서비스</span></td></tr>
		<tr><td class="t">SMS 수신여부</td>
			<td class="c"><input type="radio" name="smsok" value="Y" id="smsok" <%=vo.getHpYN().equals("Y") ? "checked=\"checked\"" : "" %> /><label for="smsok">예</label>
		<input type="radio" name="smsok" value="N" id="smsno" <%=vo.getHpYN().equals("N") ? "checked=\"checked\"" : "" %> /><label for="smsno">아니오</label> &nbsp;&nbsp;<span>이벤트, 제품 정보, 주문정보 등에 대한 메일링 서비스</span></td></tr>
	</table>
	<div class="confirmBox">
		<img src="images/btn_qaregist.gif" onclick="check_modify()" />&nbsp;&nbsp;&nbsp;
		<img src="images/btn_cancle.gif" onclick="window.location.href='?'" />
	</div>
</div>
</form>
<a id="cost" class="ti" href="">저렴하고 안정적인 문자서비스를 찾으십니까? 단가표 보기</a>
<p id="custom" class="ti">Custom Center : 070-7510-8489, Fax: 031)970-8489</p>
<script type="text/javascript" src="js/member.js"></script>

