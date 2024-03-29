<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.m.home.Home"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.m.member.SessionManagement"%>
<%@page import="com.m.member.UserInformationVO"%>
<%@page import="com.common.VbyP"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.common.util.SLibrary"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%

	String user_id = SLibrary.IfNull((String)session.getAttribute("user_id"));
	Connection conn = null;
	UserInformationVO vo = null;
	SessionManagement ses = null;
	Home home = null;
	String[] arrEmt = null;
	String[] arrCate = null;
	String[] arrEmtlms = null;
	String[] arrCatelms = null;
	ArrayList<HashMap<String, String>> arrMms = null;
	String[] arrMmsCate = null;
	String gubun = SLibrary.IfNull( VbyP.getGET(request.getParameter("gubun")) );
	String cate = SLibrary.IfNull( VbyP.getGET(request.getParameter("cate")) );
	String url = "gubun="+gubun+"&cate="+cate;
	
	String gubunlms = SLibrary.IfNull( VbyP.getGET(request.getParameter("gubunlms")) );
	String catelms = SLibrary.IfNull( VbyP.getGET(request.getParameter("catelms")) );
	String urllms = "gubunlms="+gubunlms+"&catelms="+catelms;
	
	String mmscate = SLibrary.IfNull( VbyP.getGET(request.getParameter("mmscate")) );
	String urlmms = "mmscate="+mmscate;
	ArrayList<HashMap<String, String>> notihm = null;
	
	try {
		conn = VbyP.getDB();
		
		if ( SLibrary.isNull(gubun) ) gubun = "업종별문자";
		if ( SLibrary.isNull(gubunlms) ) gubunlms = "업종별문자";
		home = Home.getInstance();
	
		//arrEmt = home.getMainEmt(conn, gubun, "%"+cate+"%", 0, 15);
		//arrCate = home.getMainCate(conn, gubun);
		
		arrEmtlms = home.getMainLMS(conn, gubunlms, "%"+catelms+"%", 0, 10);
		arrCatelms = home.getMainCateLMS(conn, gubunlms);
		
		
		
		arrMmsCate = home.getMainMmsCate(conn, "%%");
		arrMms = home.getMainMms(conn, "%%", "%"+mmscate+"%", 0, 5);
		
		//notihm = home.getNotices(conn);
		
		ses = new SessionManagement();
		if ( !SLibrary.IfNull( (String)session.getAttribute("user_id") ).equals("") )
			vo = ses.getUserInformation(conn, SLibrary.IfNull( (String)session.getAttribute("user_id") ));
	}catch (Exception e) {
		System.out.println(e.toString());
	}
	finally {
		
		try {
			if ( conn != null )	conn.close();
		}catch(SQLException e) {
			VbyP.errorLog("getUserInformation >> conn.close() Exception!"); 
		}
		conn = null;
	}
	
%>
<div id="main"><!--main Start-->
        
		<div id="flashContent" style="display:none;border:1px solid red;"></div>
		<script type="text/javascript" src="flexlib/swfobject.js"></script>
		<script type="text/javascript" src="main/main.js"></script>
		
		<!-- lms -->
        <fieldset class="emoticon">
        	<img src="/images/tit02.gif" class="title_txt" />
            <ul class="title">
                <li class="<%=(gubunlms.equals("업종별문자"))?"businessover":"business" %>" onclick="window.location.href='?gubunlms=업종별문자<%="&"+url+"&"+urlmms%>'">업종별문자</li>
                <li class="<%=(gubunlms.equals("테마문자"))?"themaover":"thema" %>" onclick="window.location.href='?gubunlms=테마문자<%="&"+url+"&"+urlmms%>'">테마별문자</li>
                <li class="more" onclick="window.location.href='?content=lms'">더보기</li>
            </ul>
            <div class="middle">
                <div class="subTitle"><%
                
            	if (arrCatelms != null) {
            		int catCnt = arrCatelms.length;
            		for (int c = 0; c < catCnt; c++) {
            	%>
                	<a href="?gubunlms=<%=gubunlms %>&catelms=<%=arrCatelms[c] %>" class="<%=(arrCatelms[c].equals(cate))?"de":""%>"><%=arrCatelms[c] %></a>&nbsp;&nbsp;
                <%
                	}
                }
                %>
                </div>
                <div class="emtibox">
                    <div class="emtiLms"><textarea onclick="setMsg(this.value)" class="emtiLms_ta" readonly ><%=SLibrary.IfNull(arrEmtlms, 0) %></textarea></div>
                    <div class="emtiLms"><textarea onclick="setMsg(this.value)" class="emtiLms_ta" readonly ><%=SLibrary.IfNull(arrEmtlms, 1) %></textarea></div>
                    <div class="emtiLms"><textarea onclick="setMsg(this.value)" class="emtiLms_ta" readonly ><%=SLibrary.IfNull(arrEmtlms, 2) %></textarea></div>
                    <div class="emtiLms margright0"><textarea onclick="setMsg(this.value)" class="emtiLms_ta" readonly ><%=SLibrary.IfNull(arrEmtlms, 3) %></textarea></div>
                    <div class="emtiLms"><textarea onclick="setMsg(this.value)" class="emtiLms_ta" readonly ><%=SLibrary.IfNull(arrEmtlms, 4) %></textarea></div>
                    <div class="emtiLms"><textarea onclick="setMsg(this.value)" class="emtiLms_ta" readonly ><%=SLibrary.IfNull(arrEmtlms, 5) %></textarea></div>
                    <div class="emtiLms"><textarea onclick="setMsg(this.value)" class="emtiLms_ta" readonly ><%=SLibrary.IfNull(arrEmtlms, 6) %></textarea></div>
                    <div class="emtiLms margright0"><textarea onclick="setMsg(this.value)" class="emtiLms_ta" readonly ><%=SLibrary.IfNull(arrEmtlms, 7) %></textarea></div>
                </div>
            </div>
        </fieldset>
		
        <fieldset id="poto">
           <img src="/images/tit03.gif" class="title_txt" />
            <ul class="title">
                <li class="<%=(gubunlms.equals("업종별문자"))?"businessover":"business" %>" onclick="window.location.href='?gubunlms=업종별문자<%="&"+url+"&"+urlmms%>'">업종별문자</li>
                <li class="<%=(gubunlms.equals("테마문자"))?"themaover":"thema" %>" onclick="window.location.href='?gubunlms=테마문자<%="&"+url+"&"+urlmms%>'">테마별문자</li>
                <li class="more" onclick="window.location.href='?content=lms'">더보기</li>
            </ul>
            <div class="potoBox">
            	<div class="subTitle"><%
                
            	if (arrMmsCate != null) {
            		int catCnt = arrMmsCate.length;
            		for (int c = 0; c < catCnt; c++) {
            	%>
                	<a href="?gubunmms=&mmscate=<%=arrMmsCate[c] %>" class="<%=(arrMmsCate[c].equals(cate))?"de":""%>"><%=arrMmsCate[c] %></a>&nbsp;&nbsp;&nbsp;&nbsp;
                <%
                	}
                }
                %>
                </div>
            <% 
            	if (arrMms != null && arrMms.size() > 0) {
            		HashMap<String, String> hm = null;
            		for (int m = 0; m < arrMms.size(); m++) {
            			hm = arrMms.get(m);
            			if (hm != null && !SLibrary.isNull( SLibrary.IfNull(hm, "msg") )) {
            		%><div style="float:left;width:188px;text-align:center;padding-top:10px;">
							<img onclick="setPhoto(this.src)" src="<%= SLibrary.IfNull(hm, "msg") %>" class="potoimg" <%= m == (arrMms.size() -1) ? "style='margin-right:0px;'" : "" %> />
            				<p style="width:170px;overflow:hidden;height:20px" ><%= SLibrary.IfNull(hm, "title") %></p>
						</div><%
            			}
            		}
            	}
            %>
            </div>
        </fieldset>
		
    </div><!--main End-->

<script type="text/javascript">

	function logincheck() {
		var f = document.loginForm;
		if (!f.user_id.value) {
			alert("아이디를 입력하세요.");
			return;
		}else if (!f.user_pw.value) {
			alert("비밀번호를 입력하세요.");
			return;
		}else {
			f.submit();
		}
	}


</script>
<script type="text/javascript" >
function setMsg(msg) {
	
	var flex = document.getElementById("Mainflex");
	flex.phoneFlexFunction("setMessage", msg);
}
function setPhoto(msg) {
	
	var flex = document.getElementById("Mainflex");
	flex.phoneFlexFunction("setPhoto", msg);
}
</script>

