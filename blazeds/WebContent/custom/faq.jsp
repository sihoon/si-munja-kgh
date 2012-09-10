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

//페이징
String url = "";
int dateRowOfPage = 5;
int tcnt = 0; // 전체개수
int nowPage = (SLibrary.isNull(request.getParameter("pg")))? 1: SLibrary.intValue(SLibrary.IfNull(request.getParameter("pg")));
int startPage = ((nowPage -1) * dateRowOfPage)+1;//시작 index
int endPage = nowPage * dateRowOfPage;;//마지막 index

url = "content=notic";

ArrayList<HashMap<String, String>> al = null;

try {
	conn = VbyP.getDB();
	ses = new SessionManagement();
	if ( !SLibrary.IfNull( (String)session.getAttribute("user_id") ).equals("") )
		vo = ses.getUserInformation(conn, SLibrary.IfNull( (String)session.getAttribute("user_id") ));
	else {
		//throw new Exception("로그인 후 이용 가능 합니다.");
	}
	
	NoticDAO notic = new NoticDAO();
	if (!SLibrary.isNull(idx) && SLibrary.intValue(idx) > 0) {
		notic.updateCnt(conn, SLibrary.intValue(idx));
	}
	
	
	al = notic.getListFAQPage(conn, startPage, endPage);
	tcnt = notic.totalCnt;

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
    <td><img src="/images/faq_tit.gif"></td>
  </tr>
  <tr>
    <td height="70"><img src="/images/faq_img.gif"></td>
  </tr>
</table>
<table width="645" border="0" cellspacing="0" cellpadding="0" style="display:block;float:right" >
  <tr>
    <td width="130" height="30" align="center" background="/images/bbs_bg.gif"><b><font color="ffffff">번호</font></b></td>
    <td background="/images/bbs_bg.gif"><b><font color="ffffff">제목</font></b></td>
  </tr>
  <%
		String style = "";
		
		if (al.size() > 0) {
			int cnt = al.size();
			
			for( int p = 0; p < cnt; p++) {
				hm = al.get(p);
				if (p%2 == 0) style = " class=\"bg\"";
				else style = "";
				
				if (idx.equals(SLibrary.IfNull(hm, "idx"))) viexIdx = p;
				%>
		<tr>
			<td width="150" height="30" align="center"><%=SLibrary.IfNull(hm, "num") %></td>
			<td><a href="?content=notic_view&idx=<%=SLibrary.IfNull(hm, "num") %>"><%=SLibrary.IfNull(hm, "title") %></a></td>
		</tr>
		<tr>
		    <td height="1" colspan="2" bgcolor="bbbbbb"></td>
		</tr>
		<%
				
			}
		} else {
				%>
		<tr><td colspan="2">- 내역이 없습니다.</td></tr>
				<%
		}
		%>
 
</table>
<table width="645" border="0" cellspacing="1" cellpadding="1" style="display:block;float:right">
  <tr>
    <td height="30">&nbsp;</td>
  </tr>
  <tr>
    <td align="right"><table width="250" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>
          <input name="textfield" type="text" id="textfield" size="30" />
        </td>
        <td><img src="/images/bbs_search.gif"></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
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


