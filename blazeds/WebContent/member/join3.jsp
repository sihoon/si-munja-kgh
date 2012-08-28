<%@page import="com.common.VbyP"%>
<%@page import="com.common.util.SLibrary"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%

if(!SLibrary.IfNull( (String)session.getAttribute("munja119JoinStep") ).equals("step0@Session")) {
	session.removeAttribute("munja119JoinStep");
	//out.println(SLibrary.alertScript("잘못된 접근 입니다.", "window.location.href='/';"));
	//return;
}
request.getSession().setAttribute("munja119JoinStep","step2@Session");
VbyP.accessLog("회원가입 페이지 요청 3단계>> " + request.getRemoteAddr());

String name = VbyP.getPOST(request.getParameter("name"));
String jumin1 = VbyP.getPOST(request.getParameter("jumin1"));
String jumin2 = VbyP.getPOST(request.getParameter("jumin2"));

try {
// 	if (SLibrary.isNull(name)) throw new Exception("이름이 없습니다.");
// 	if (SLibrary.isNull(jumin1)) throw new Exception("주민등록번호가 없습니다.");
// 	if (SLibrary.isNull(jumin2)) throw new Exception("주민등록번호가 없습니다.");


%>
<form name="form" method="post" target="nobody" action="member/_join.jsp">
<input type="hidden" name="jumin1" value="<%=jumin1 %>" />
<input type="hidden" name="jumin2" value="<%=jumin2 %>" />
<input type="hidden" name="id_ok" id="id_ok" value="" />

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

<div id="joinBox">
	<p class="joinTitle ti">회원가입</p>
	<p class="joinAd ti">업계최저가격 문자서비스</p>
	<img src="/images/m_03.gif" style="display:block;margin:10px 0px;" />
	<table class="inputTable" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td class="t">아이디</td>
			<td class="c">
				<input type="text" name="join_id" id="join_id" class="txt" value=""/>
				<span id="user_id_check">영, 숫자조합 (4~12자)</span>
			</td>
		</tr>
		<tr><td class="t">비밀번호</td><td class="c"><input type="password" name="passwd1" id="passwd1" class="txt" value="" /> <span id="passwd1_check">영, 숫자조합 (4~12자)</span></td></tr>
		<tr><td class="t">비밀번호확인</td><td class="c"><input type="password" name="passwd2" id="passwd2" class="txt" value=""/> <span id="passwd2_check">비밀번호를 다시 한번 입력해 주세요.</span></td></tr>
		<tr><td class="t">이름</td><td class="c"><input type="text" name="name" class="txt" value=""/> <span></span></td></tr>
		<tr><td class="t">이메일</td><td class="c"><input type="text" name="email" class="txt" value=""/> <span>*필수 : 메일수신이 가능한 이메일주소</span></td></tr>
		<tr><td class="t">핸드폰</td><td class="c"><input type="text" name="hp" class="txt" value=""/></td></tr>
		<tr><td class="t">메일수신여부</td>
			<td class="c"><input type="radio" name="emailok" id="emailok" /><label for="emailok">예</label>
		<input type="radio" name="emailok" id="emailno" checked /><label for="emailno">아니오</label> &nbsp;&nbsp;<span>이벤트, 제품 정보, 주문정보 등에 대한 메일링 서비스</span></td></tr>
		<tr><td class="t">SMS 수신여부</td>
			<td class="c"><input type="radio" name="smsok" id="smsok" /><label for="smsok">예</label>
		<input type="radio" name="smsok" id="smsno" checked /><label for="smsno">아니오</label> &nbsp;&nbsp;<span>이벤트, 제품 정보, 주문정보 등에 대한 메일링 서비스</span></td></tr>
	</table>
	<div class="confirmBox">
		<img src="images/btn_member1.gif" onclick="check_join()" />&nbsp;&nbsp;&nbsp;
		<img src="images/btn_mcancle.gif" onclick="window.location.href='?'" />
	</div>
</div>
</form>
<script type="text/javascript" src="js/member.js"></script>
<%
}catch(Exception e) {
	out.println(SLibrary.alertScript(e.getMessage(), "window.location.href='/';"));
}
%>
