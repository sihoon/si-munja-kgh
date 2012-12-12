<%@page import="com.m.notic.NoticVO"%>
<%@page import="com.m.notic.NoticDAO"%>
<%@page import="com.common.util.Paging"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
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
HashMap<String, String> hm = null;
String idx = SLibrary.IfNull(request.getParameter("idx"));
int viexIdx = 0;


try {
	conn = VbyP.getDB();
	
	
	NoticDAO notic = new NoticDAO();
	if (!SLibrary.isNull(idx) && SLibrary.intValue(idx) > 0) {
		notic.updateCnt(conn, SLibrary.intValue(idx));
		hm = notic.getContent(conn, SLibrary.intValue(idx));
	}else {
		out.println(SLibrary.alertScript("키가 없습니다.", "window.location.href='?content=notic'"));
		return;
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

<table width="645" border="0" cellspacing="0" cellpadding="0" style="display:block;float:right">
  <tr>
    <td><img src="/images/notice_tit.gif"></td>
  </tr>
  <tr>
    <td height="70"><img src="/images/notice_img.gif"></td>
  </tr>
</table>
<table width="645" border="0" cellspacing="0" cellpadding="0" style="display:block;float:right">
  <tr>
    <td height=2 bgcolor="a54645"></td>
  </tr>
  <tr>
    <td height="40" style="padding-left:15px;"><span class="style1">이동통신사 스팸서비스관련 공지</span></td>
  </tr>
 <tr>
    <td height=2 bgcolor="a54645"></td>
  </tr>
   <tr>
    <td style="padding:15px;"><p><%=SLibrary.IfNull(hm, "title") %><br /></p>
    <%=SLibrary.replaceAll(SLibrary.IfNull(hm, "content"), "\r\n", "<br/>")  %>
    </td>
  </tr>
   <tr>
    <td height=1 bgcolor="bbbbbb"></td>
  </tr>
</table>
<table width="645" border="0" cellspacing="1" cellpadding="1" style="display:block;float:right">
  <tr>
    <td height="30">&nbsp;</td>
  </tr>
  
  <tr>
    <td align="center"><img src="/images/bbs_list.jpg" style="cursor:pointer" onclick="window.location.href='?content=notic'"/></td>
  </tr>
</table>

<%
	
}catch (Exception e) {}
finally {

try {
	if ( conn != null )	conn.close();
}catch(SQLException e) {
	VbyP.errorLog("notic.jsp >> conn.close() Exception!"); 
}
conn = null;
}

%>


