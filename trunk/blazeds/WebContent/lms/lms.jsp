<%@page import="com.common.VbyP"%>
<%@page import="com.common.util.SLibrary"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="flexlib/swfobject.js"></script>
<script type="text/javascript" src="lms/lms.js"></script>
<div id="flashContent" style="display:none;border:1px solid red;">
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
<script type="text/javascript" >
function readyPhone() {
	<%
	String msg = SLibrary.replaceAll( SLibrary.IfNull(VbyP.getGET( request.getParameter("msg") )), "\"", "\\\"");
	if (!SLibrary.isNull(msg)) {
	%>
	var flex = document.getElementById("Normal");
	flex.phoneFlexFunction("setMessage", "<%=msg%>");
	<%}%>
}
</script>

