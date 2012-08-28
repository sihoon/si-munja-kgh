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
		VbyP.errorLog("modify.jsp >> conn.close() Exception!"); 
	}
	conn = null;
}

%>


<script type="text/javascript" src="flexlib/swfobject.js"></script>
<script type="text/javascript" src="member/member.js"></script>
<div id="phone_flex">
	<div id="flashContent" style="display:none;">
	    <p>
	        To view this page ensure that Adobe Flash Player version 
	        10.2.0 or greater is installed. 
	    </p>
	    <script type="text/javascript"> 
	        var pageHost = ((document.location.protocol == "https:") ? "https://" : "http://"); 
	        document.write("<a href='http://www.adobe.com/go/getflashplayer'><img src='" 
	                        + pageHost + "www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /></a>" ); 
	    </script> 
	</div>
</div>

<form name="form" method="post" action="member/_modify.jsp">
<div id="joinBox">
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
		<img src="images/btn_member1.gif" onclick="check_modify()" />&nbsp;&nbsp;&nbsp;
		<img src="images/btn_mcancle.gif" onclick="window.location.href='?'" />
	</div>
</div>
</form>

<script type="text/javascript" src="js/member.js"></script>

