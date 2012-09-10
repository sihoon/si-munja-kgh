<%@page import="com.common.VbyP"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.m.home.Home"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.common.util.SLibrary"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	String strContent = SLibrary.IfNull(request.getParameter("content"));
	String session_id = SLibrary.IfNull((String)session.getAttribute("user_id"));
	
	Home home = null;
	Connection conn = null;
	ArrayList<HashMap<String, String>> notihm = null;
	
	try {
		conn = VbyP.getDB();
		home = Home.getInstance();
		notihm = home.getNotices(conn);
		
	}catch (Exception e) {}
	finally {
		
		try {
			if ( conn != null )	conn.close();
		}catch(SQLException e) {
			VbyP.errorLog("getNotices >> conn.close() Exception!"); 
		}
		conn = null;
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Content-Script-Type" content="text/javascript" />
	<meta http-equiv="Content-Style-Type" content="text/css" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>문자야 :: High Performance, Low Price  </title>
    <link type="text/css" rel="stylesheet" href="css/base.css?ver=1.0"/>
    <link type="text/css" rel="stylesheet" href="css/main.css?ver=1.2"/>
	<script type="text/javascript"  language="javascript" src="js/jquery-1.7.1.min.js?ver=1.0"></script>
	<script type="text/javascript"  language="javascript" src="js/common.js?ver=1.0"></script>
    
</head>

<body>
<iframe src="" id="nobody" name="nobody" frameborder="0" width="0" height="0" style="display:none"></iframe>
<div class="top_bg" ></div>
<div id="wrapper">
<div class="context">
	
    <div id="topSub"><!--top-sub-->
        <%if (SLibrary.isNull(session_id)) { %><img src="/images/bu_login.gif" alt="로그인" class="hand"/><%}else {%><img src="/images/bu_logout.gif" onclick="window.location.href='member/_logout.jsp'" alt="로그아웃" class="hand"/><%} %> | 
        <%if (SLibrary.isNull(session_id)) { %><img src="/images/bu_member.gif" onclick="window.location.href='?content=join'" alt="회원가입" class="hand"/><%}else {%><img src="/images/bu_modify.gif" onclick="window.location.href='?content=modify'" alt="정보수정" class="hand"/><%} %> | 
        <img src="/images/bu_mypage.gif" onclick="window.location.href='?content=my'" alt="마이페이지" class="hand"/> | 
        <img src="/images/bu_add.gif" class="hand" onclick="_addFavorite('문자랑','http://www.munjarang.com')" />
    </div>

	<div id="logo">
		<img src="/images/logo.gif" alt="MUNJARANG" class="hand" onclick="window.location.href='?'"  />
		<pre style="display:inline">    </pre>
		<img src="/images/gif.gif" alt="naver search" class="vab" />
		<pre style="display:inline">      </pre>
		<img src="/images/banner.gif" alt="banner" class="vab" />
	</div>
    

    <div id="menu"><!--메뉴-->
        <a href="?content=normal" class="sms ti <%=(strContent.equals("normal"))?"over":""%>"  onfocus="this.blur();">단문문자보내기</a>
        <a href="?content=lms" class="lms ti <%=(strContent.equals("lms"))?"over":""%>" onfocus="this.blur();">장문문자보내기</a>
        <a href="?content=mms" class="mms ti <%=(strContent.equals("mms"))?"over":""%>" onfocus="this.blur();">포토(MMS)문자보내기</a>
        <a href="?content=billing" class="billing ti <%=(strContent.equals("billing"))?"over":""%>" onfocus="this.blur();">충전하기</a>
        <a href="?content=sent" class="sent ti <%=(strContent.equals("sent"))?"over":""%>" onfocus="this.blur();">전송내역</a>
        <a href="?content=excel" class="excel ti <%=(strContent.equals("excel"))?"over":""%>" onfocus="this.blur();">EXCEL/대량전송</a>
        <a href="?content=address" class="address ti <%=(strContent.equals("address"))?"over":""%>" onfocus="this.blur();">주소록관리</a>
    </div>
    
	<jsp:include page="body.jsp" flush="false"/>
	
	<div id="customer">
		<img alt="customer" src="/images/customercenter.jpg" style="display:block;float:left;margin-right:10px;" />
        <fieldset id="noti">
            <legend>공지사항</legend>
            <a href="?content=notic" class="more">more</a>
            <%
            	if (notihm != null) {
            		int size = notihm.size();
            		HashMap<String, String> hm = null;
            		for (int i = 0; i < size; i++) {
            			hm = notihm.get(i);
            			%>
            			<div class="content"><a href="?content=notic_view&idx=<%=SLibrary.IfNull(hm, "idx") %>" class="title"><%=SLibrary.IfNull(hm, "title") %></a><span class="notiDate"></span></div>
            			<%
            		}
            	}
            %>
        </fieldset>

        <div id="etc">
            <a href="?content=my" class="card">신용카드영수증출력</a>
            <a href="?content=faq" class="faq">자주하는 질문</a>
            <a href="?content=billing" class="cost">단가표</a>
        </div>
	</div><!-- customer -->
	
	<div id="copyright">
	    <a href="?content=company" class="company">회사소개</a>
	    <a href="?content=privcy" class="personal">개인정보보호정책</a>
	    <a href="?content=use" class="use">이용약관</a>
	    <a href="?content=spam" class="spam">광고스팸문자</a>
	    <div class="link">
	        <a href="" class="homeLink">HOME</a>
	        <a href="" class="topLink">Top</a>
	    </div>
	    <div class="copyRight">copy right</div>
	
<!-- 	    <select class="family"> -->
<!-- 			<option>::::패밀리싸이트::::</option> -->
<!-- 			<option>NS이스토어</option> -->
<!-- 			<option>QXL</option> -->
<!-- 		</select> -->
	</div>
    
</div>
<div id="sideBanner">배너</div>
</div>

<script type="text/javascript">
$(function() {
	$(window).scroll(function() {
		_top = $(document).scrollTop()+200;
		setTimeout(function() {
			$('#sideBanner').stop().animate({ top: _top }, 5);
		}, 5);
	});
});
</script>
</body>
</html>
