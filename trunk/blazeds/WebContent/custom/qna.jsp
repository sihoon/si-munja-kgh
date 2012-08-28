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
	al = notic.getListPage(conn, startPage, endPage);
	tcnt = notic.totalCnt;

%>

<% if (vo == null) { %>
<form name="loginForm" method="post" target="nobody" action="member/_login.jsp" >
<fieldset id="login">
    <legend>로그인</legend>
    <label class="idlabel ti" for="user_id">아이디</label><input type="text" id="user_id" name="user_id" />
    <label class="pwlabel ti" for="user_pw">비밀번호</label><input type="password" id="user_pw" name="user_pw" />
    <button class="loginBtn ti"  onclick="logincheck()">로그인</button>
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

<p id="qnaTitle" class="ti">공지사항</p>

<div id="noticBox" class="" >
	<img alt="타이틀" src="images/notice_bar.gif"/>
	<table class="cntTable" width="100%" border="0" cellpadding="0" cellspacing="0" >
<!-- 		<tr> -->
<!-- 			<th>번호</th> -->
<!-- 			<th>제목</th> -->
<!-- 			<th>날짜</th> -->
<!-- 			<th>조회</th> -->
<!-- 		</tr> -->
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
		<tr onclick="visible(document.getElementById('c<%=p%>'))" style="cursor:pointer">
			<td <%=style %> style="width:70px;height:28px;"><%=SLibrary.IfNull(hm, "num") %></td>
			<td <%=style %> style="width:430px" style="text-align:left;"><%=SLibrary.IfNull(hm, "title") %></td>
			<td <%=style %> style="width:120px"><%=SLibrary.IfNull(hm, "timeWrite").substring(0, SLibrary.IfNull(hm, "timeWrite").length()-2)%></td>
			<td <%=style %>><%=SLibrary.addComma( SLibrary.IfNull(hm, "cnt") )%></td>
		</tr>
		<tr id="c<%=p%>" style="display:none;">
			<td colspan="4" style="text-align:left;width:721px;height:auto"><textarea readonly style="padding:10px 0px; padding-left:50px;border:1px solid gray;width:671px;height:100px;"><%=SLibrary.IfNull(hm, "content") %></textarea></td>
		</tr>
				<%
				
			}
		} else {
				%>
		<tr><td colspan="4">- 내역이 없습니다.</td></tr>
				<%
		}
		%>
		
		<tr><td colspan="5"><%
		//pageing
		Paging ppg = new Paging(nowPage , dateRowOfPage, dateRowOfPage , tcnt);
		
		ppg.pg = "pg";
		ppg.linkPage = "";
		ppg.queryString = url;
		
		ppg.firstOffLink = "<span style='color:gray'><< 처음</span>";
		ppg.firstBlockOffLink = "<span style='color:gray'>< 이전</span>";
		ppg.prevOffLink = "";   
		ppg.nextOffLink = "";
		ppg.lastBlockOffLink = "<span style='color:gray'>다음 ></span>";
		ppg.lastOffLink = "<span style='color:gray'>마지막 >></span>";

		ppg.firstLink = "<< 처음";
		ppg.firstBlockLink = "< 이전";
		ppg.prevLink = "";
		ppg.nextLink = "";
		ppg.lastBlockLink = "다음 >";
		ppg.lastLink = "마지막 >>";
		
		ppg.delimiter = "|";

		out.println( ppg.print() );
		%></td></tr>
	</table>
	
	
</div>
<a id="cost" class="ti" href="?content=billing">저렴하고 안정적인 문자서비스를 찾으십니까? 단가표 보기</a>
<p id="custom" class="ti">Custom Center : 070-7510-8489, Fax: 031)970-8489</p>

<%
	if (!idx.equals(""))
	out.println(SLibrary.alertScript("", "visible(document.getElementById('c"+Integer.toString(viexIdx)+"'));"));
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


