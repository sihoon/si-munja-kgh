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
<script type="text/javascript" src="/flexlib/swfobject.js"></script>
<script type="text/javascript" src="/member/member.js"></script>
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

<form name="formBilling" target="nobody" mehtod="post" action="billing/payreq.jsp" >
	<input type="hidden" name="smethod" value="" />
	<input type="hidden" name="amount" value="" />
</form>

<form name="formBillingCash" target="nobody" mehtod="post" action="billing/_cash.jsp" >
	<input type="hidden" name="smethod" value="" />
	<input type="hidden" name="amount" value="" />
	<input type="hidden" name="cash" value="" />
	<input type="hidden" name="cashName" value="" />
	
</form>

<form name="form" method="post" >
<div id="billingBox" >
	<img src="images/top4.jpg" />
	<h2 class="txtMethod ti">결제 수단</h2>
	<p class="selMethod">
		<input type="radio" id="card" name="method" value="card" onclick="billingMethod()" checked /><label for="card">신용카드</label>&nbsp;&nbsp;
<!-- 		<input type="radio" id="online" name="method" value="online" onclick="billingMethod()" /><label for="online">계좌이체</label>&nbsp;&nbsp; -->
<!-- 		<input type="radio" id="mobile" name="method" value="mobile" onclick="billingMethod()" /><label for="mobile">휴대폰</label>&nbsp;&nbsp; -->
		<input type="radio" id="cash" name="method" value="cash" onclick="billingMethod()" /><label for="cash">무통장입금</label>
	</p>
	<h2 class="txtAmount"></h2>
	<p style="float:right;width:100px;height:33px;background:url('images/vat.gif') no-repeat;"></p>
	<table width="645" border="0" cellpadding="0" cellspacing="0">
		<tr><td colspan="4" class="title">&nbsp;</td></tr>
		<tr onmouseover="this.style.backgroundColor='#fff0f0'" onmouseout="this.style.backgroundColor='#ffffff'">
			<td width="70"><input type="radio" name="amount" value="5500" /></td>
			<td width="210">5,000원</td>
			<td width="220">417건/포인트</td>
			<td>12원/건</td>
		</tr>
		<tr onmouseover="this.style.backgroundColor='#fff0f0'" onmouseout="this.style.backgroundColor='#ffffff'">
			<td><input type="radio" name="amount" value="11000" /></td>
			<td>10,000원</td>
			<td>833건/포인트</td>
			<td>12원/건</td>
		</tr>
		<tr onmouseover="this.style.backgroundColor='#fff0f0'" onmouseout="this.style.backgroundColor='#ffffff'">
			<td><input type="radio" name="amount" value="33000" checked /></td>
			<td>30,000원</td>
			<td>2,500건/포인트</td>
			<td>12원/건</td>
		</tr>
		<tr onmouseover="this.style.backgroundColor='#fff0f0'" onmouseout="this.style.backgroundColor='#ffffff'">
			<td><input type="radio" name="amount" value="55000" /></td>
			<td>50,000원</td>
			<td>4,167건/포인트</td>
			<td>12원/건</td>
		</tr>
		<tr onmouseover="this.style.backgroundColor='#fff0f0'" onmouseout="this.style.backgroundColor='#ffffff'">
			<td><input type="radio" name="amount" value="110000" /></td>
			<td>100,000원</td>
			<td>8,333건/포인트</td>
			<td>12원/건</td>
		</tr>
		<tr onmouseover="this.style.backgroundColor='#fff0f0'" onmouseout="this.style.backgroundColor='#ffffff'">
			<td><input type="radio" name="amount" value="330000" /></td>
			<td>300,000원</td>
			<td>25,000건/포인트</td>
			<td>12원/건</td>
		</tr>
		<tr onmouseover="this.style.backgroundColor='#fff0f0'" onmouseout="this.style.backgroundColor='#ffffff'">
			<td><input type="radio" name="amount" value="550000" /></td>
			<td>500,000원</td>
			<td>43,103건/포인트</td>
			<td>11.6원/건</td>
		</tr>
		<tr onmouseover="this.style.backgroundColor='#fff0f0'" onmouseout="this.style.backgroundColor='#ffffff'">
			<td><input type="radio" name="amount" value="1100000" /></td>
			<td>1,000,000원</td>
			<td>90,909건/포인트</td>
			<td>11원/건</td>
		</tr>
		<tr onmouseover="this.style.backgroundColor='#fff0f0'" onmouseout="this.style.backgroundColor='#ffffff'">
			<td><input type="radio" name="amount" value="3300000" /></td>
			<td>3,000,000원</td>
			<td>280,374건/포인트</td>
			<td>10.7원/건</td>
		</tr>
		<tr onmouseover="this.style.backgroundColor='#fff0f0'" onmouseout="this.style.backgroundColor='#ffffff'">
			<td><input type="radio" name="amount" value="5500000" /></td>
			<td>5,000,000원</td>
			<td>485,437건/포인트</td>
			<td>10.3원/건</td>
		</tr>
		<tr onmouseover="this.style.backgroundColor='#fff0f0'" onmouseout="this.style.backgroundColor='#ffffff'">
			<td><input type="radio" name="amount" value="11000000" /></td>
			<td>10,000,000원</td>
			<td>1,000,000건/포인트</td>
			<td>10원/건</td>
		</tr>
		<tr id="cashBox" style="display:none;">
			<td colspan="4" style="border:none;height:150px;text-align:left;">
				
				<p class="txtCash ti">계좌선택</p>
				<ul class="cashList">
					<li><input type="radio" name="cash" id="cash1" value="국민 - 김경희 831801-04-007336"  checked="checked" /><label for="cash1">국민 - 김경희 831801-04-007336</label></li>
				</ul>
				<div style="width:421px;text-align:center;margin:10px 0px">
				입금자명 <input type="text" name="cashName" /> 으로 <img src="images/reserve.gif" style="cursor:pointer" onclick="billingCashCheck()" />
				</div>
			</td>
		</tr>
		<tr id="etcBox"><td colspan="4" style="border:none;height:80px;"><img src="images/btn_payment.jpg" style="cursor:pointer" onclick="billingCheck()" /></td></tr>
	</table>
</div>
</form>

