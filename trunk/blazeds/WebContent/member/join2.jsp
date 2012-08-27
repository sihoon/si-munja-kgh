<%@page import="com.common.VbyP"%>
<%@page import="com.common.util.SLibrary"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%

if(!SLibrary.IfNull( (String)session.getAttribute("munja119JoinStep") ).equals("step0@Session")) {
	session.removeAttribute("munja119JoinStep");
	out.println(SLibrary.alertScript("잘못된 접근 입니다.", "window.location.href='/';"));
	return;
}
request.getSession().setAttribute("munja119JoinStep","step1@Session");
VbyP.accessLog("회원가입 페이지 요청 2단계>> " + request.getRemoteAddr());
%>
<form name="form" method="post" action="?content=join3">

<fieldset id="login"><!-- 로그인 -->
    <legend>로그인</legend>
    <label class="idlabel ti" for="user_id">아이디</label><input type="text" id="user_id" name="user_id" />
    <label class="pwlabel ti" for="user_pw">비밀번호</label><input type="password" id="user_pw" name="user_pw" />
    <button class="loginBtn ti">로그인</button>
    <button class="joinBtn ti">회원가입</button>
    <button class="findBtn ti">아이디찾기</button>
</fieldset>
<div id="joinBox" style="height:500px;">
	<p class="joinAd ti">업계최저가격 문자서비스</p>
	<p class="join2Title ti">회원확인</p>
	
	<div class="juminBox">
		<img src="images/member_img03.gif"/>
		<input type="text" name="name" value="" style="width:150px;" />&nbsp;&nbsp;
		<img src="images/member_img04.gif"/>
		<input type="text" name="jumin1" id="jumin1" value=""  onkeyup="moveCursor(jumin1, jumin2, 6);" style="width:100px;" /> - 
		<input type="password" name="jumin2" id="jumin2" value="" style="width:100px;" />
		<span style="color:#CCC;font-style: italic;">(법인등록번호 가능)</span>
		
<pre>개정 "주민등록법"에 의해 태인의 주민등록번호를 부정사용하는 자는 3년 이하늬 징역 또는 1천만원.
이하의 벌금이 부과될 수 있습니다. 관련법률: 주민등록법 제37조(벌칙) 제10호(시행일:2009.04.01)
현재, 타인의 주민번호를 도용하여 온라인 회원가입을 하신 이용자분들은 지금 즉시 명의도용을 중단하십시요.! </pre>
	</div>
	<div class="confirmBox">
		<img src="images/btn_next.gif" id="juminCheck" style="cursor:pointer"/>&nbsp;&nbsp;&nbsp;
		<img src="images/btn_cancle.gif" onclick="window.location.href='?'" style="cursor:pointer" />
	</div>
</div>
<script type="text/javascript" src="js/member.js"></script>
<a id="cost" class="ti" href="">저렴하고 안정적인 문자서비스를 찾으십니까? 단가표 보기</a>
<p id="custom" class="ti">Custom Center : 070-7510-8489, Fax: 031)970-8489</p>
</form>