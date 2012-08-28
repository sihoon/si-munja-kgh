<%@page import="java.security.MessageDigest"%>
<%@page import="com.m.billing.Billing"%>
<%@page import="com.common.util.Paging"%>
<%@page import="com.m.point.PointHistory"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.common.VbyP"%>
<%@page import="com.m.member.SessionManagement"%>
<%@page import="com.m.member.UserInformationVO"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.common.util.SLibrary"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%!
	private String getAuthData(String tid) throws Exception {

		String CST_PLATFORM = "service";
		String CST_MID = "munja119";
		String LGD_MID = ("test".equals(CST_PLATFORM.trim()) ? "t" : "")
				+ CST_MID;
		String LGD_MERTKEY = "5cc68c0e79ac7d9e59821804c062edd2";
		String LGD_TID = tid;

		StringBuffer sb = new StringBuffer();
		sb.append(LGD_MID);
		sb.append(LGD_TID);
		sb.append(LGD_MERTKEY);

		byte[] bNoti = sb.toString().getBytes();
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] digest = md.digest(bNoti);

		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			int c = digest[i] & 0xff;
			if (c <= 15) {
				strBuf.append("0");
			}
			strBuf.append(Integer.toHexString(c));
		}

		String LGD_HASHDATA = strBuf.toString();
		String LGD_CUSTOM_PROCESSTYPE = "TWOTR";
		return LGD_HASHDATA;
	}%><%
	String user_id = SLibrary.IfNull((String) session
			.getAttribute("user_id"));
	Connection conn = null;
	UserInformationVO vo = null;
	SessionManagement ses = null;
	ArrayList<HashMap<String, String>> alPoint = null;
	ArrayList<HashMap<String, String>> alBilling = null;
	HashMap<String, String> hm = null;

	//페이징
	String purl = "";
	String burl = "";
	int dateRowOfPage = 5;
	int ptcnt = 0; // 포인트 전체개수
	int btcnt = 0; // 결제 전체개수
	int pnowPage = (SLibrary.isNull(request.getParameter("ppg"))) ? 1
			: SLibrary.intValue(SLibrary.IfNull(request
					.getParameter("ppg")));
	int bnowPage = (SLibrary.isNull(request.getParameter("bpg"))) ? 1
			: SLibrary.intValue(SLibrary.IfNull(request
					.getParameter("bpg")));
	int pstartPage = ((pnowPage - 1) * dateRowOfPage) + 1;//시작 index
	int pendPage = dateRowOfPage; //마지막 index
	int bstartPage = ((bnowPage - 1) * dateRowOfPage) + 1;//시작 index
	int bendPage = dateRowOfPage; //마지막 index

	purl = "content=my&bpg="
			+ SLibrary.IfNull(request.getParameter("bpg"));
	burl = "content=my&ppg="
			+ SLibrary.IfNull(request.getParameter("ppg"));

	try {
		conn = VbyP.getDB();

		ses = new SessionManagement();
		if (!SLibrary.IfNull((String) session.getAttribute("user_id"))
				.equals(""))
			vo = ses.getUserInformation(conn, SLibrary
					.IfNull((String) session.getAttribute("user_id")));

		if (vo == null) {
			out.println(SLibrary.alertScript("로그인 후 이용 하실 수 있습니다.",
					"window.location.href='?'"));
			return;
		}
		PointHistory ph = PointHistory.getInstance();
		alPoint = ph.getPointHistoryList(conn, vo.getUser_id(),
				pstartPage-1, pendPage);
		ptcnt = ph.totalCnt;

		Billing billing = Billing.getInstance();
		alBilling = billing.getBillingList(conn, vo.getUser_id(),
				bstartPage-1, bendPage);
		btcnt = billing.totalCnt;

	} catch (Exception e) {
	} finally {

		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
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

<p id="myTitle" class="ti">마이119</p>

<div id="myBox" class="" >
	<p><span><%=vo.getUser_name()%></span>님의 사용가능 건수는 <span><%=SLibrary.addComma(vo.getPoint())%></span>건 입니다.</p>
	<table class="cntTable" width="721" border="0" cellpadding="0" cellspacing="0" >
		<tr><td colspan="5"><img src="images/mypage01.gif" /></td></tr>
		<%
			String style = "";
			if (alPoint.size() > 0) {
				int cnt = alPoint.size();

				for (int p = 0; p < cnt; p++) {
					hm = alPoint.get(p);
					if (p % 2 == 0)
						style = " class=\"bg\"";
					else
						style = "";
		%>
		<tr>
			<td <%=style%> style="width:70px"><%=SLibrary.IfNull(hm, "num")%></td>
			<td <%=style%> style="width:210px"><%=SLibrary.IfNull(hm, "memo")%></td>
			<td <%=style%> style="width:65px"><%=SLibrary.addComma(SLibrary.IfNull(hm, "point"))%></td>
			<td <%=style%> style="width:110px"><%=SLibrary.addComma(SLibrary
							.IfNull(hm, "now_point"))%></td>
			<td <%=style%> style="width:256px"><%=SLibrary.IfNull(hm, "timeWrite").substring(0,
							SLibrary.IfNull(hm, "timeWrite").length() - 2)%></td>
		</tr>
				<%
					}
					} else {
				%>
		<tr><td colspan="5">- 내역이 없습니다.</td></tr>
				<%
					}
				%>
		
		<tr><td colspan="5"><%
					//pageing
					Paging ppg = new Paging(pnowPage, dateRowOfPage, dateRowOfPage,
							ptcnt);

					ppg.pg = "ppg";
					ppg.linkPage = "";
					ppg.queryString = purl;

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

					out.println(ppg.print());
				%></td></tr>
	</table>
	<p id="myBillingTitle" class="ti"> 결제내역 </p>
	<script language="JavaScript" src="http://pgweb.uplus.co.kr/WEB_SERVER/js/receipt_link.js"></script>
	<table class="cntTable" width="721" border="0" cellpadding="0" cellspacing="0" >
		<tr><td colspan="5"><img src="images/mypage02.gif" /></td></tr>
		<%
			if (alBilling.size() > 0) {
				int cnt = alBilling.size();
				String link = "";
				String authData = "";
				
				long now = SLibrary.getTime("2012-03-23 12:00:00", "yyyy-MM-dd HH:mm:ss");
				long bill = 0;
				
				

				for (int p = 0; p < cnt; p++) {
					hm = alBilling.get(p);
					if (p % 2 == 0)
						style = " class=\"bg\"";
					else
						style = "";
					
					bill = SLibrary.getTime(SLibrary.IfNull(hm, "timeWrite").substring(0,SLibrary.IfNull(hm, "timeWrite").length() - 2),
							"yyyy-MM-dd HH:mm:ss");
					
					if (now < bill) {

						if (SLibrary.IfNull(hm, "method").equals("cash"))
							link = "<a href='javascript:' onclick='return taxWindow("
									+ SLibrary.IfNull(hm, "idx")
									+ ");'>세금계산서신청</a>";
						else if (SLibrary.IfNull(hm, "method").equals("카드")) {

							authData = getAuthData(SLibrary.IfNull(hm, "tid"));
							link = "<a href=\"javascript:showReceiptByTID('munja119', '"
									+ SLibrary.IfNull(hm, "tid")
									+ "', '"
									+ authData + "')\">전표 출력</a>";
						} else {
							authData = getAuthData(SLibrary.IfNull(hm, "tid"));
							link = "<a href=\"javascript:showReceiptByTID('munja119', '"
									+ SLibrary.IfNull(hm, "tid")
									+ "', '"
									+ authData + "')\">영수증 출력</a>";
						}
					}else {
						link = "";
					}
					
		%>
		<tr>
			<td <%=style%> style="width:70px"><%=SLibrary.IfNull(hm, "num")%></td>
			<td <%=style%> style="width:100px"><%=SLibrary.IfNull(hm, "method").equals("cash") ? "무통장입금"
							: SLibrary.IfNull(hm, "method")%></td>
			<td <%=style%> style="width:70px"><%=SLibrary.addComma(SLibrary.IfNull(hm, "amount"))%></td>
			<td <%=style%> style="width:356px"><%=SLibrary.IfNull(hm, "timeWrite").substring(0,
							SLibrary.IfNull(hm, "timeWrite").length() - 2)%></td>
			<td <%=style%> style="width:125px"><%=link%></td>
		</tr>
				<%
					}
					} else {
				%>
		<tr><td colspan="5">- 내역이 없습니다.</td></tr>
				<%
					}
				%>
		<tr><td colspan="5"><%
			//pageing
			Paging bpg = new Paging(bnowPage, dateRowOfPage, dateRowOfPage,
					btcnt);

			bpg.pg = "bpg";
			bpg.linkPage = "";
			bpg.queryString = burl;

			bpg.firstOffLink = "<span style='color:gray'><< 처음</span>";
			bpg.firstBlockOffLink = "<span style='color:gray'>< 이전</span>";
			bpg.prevOffLink = "";
			bpg.nextOffLink = "";
			bpg.lastBlockOffLink = "<span style='color:gray'>다음 ></span>";
			bpg.lastOffLink = "<span style='color:gray'>마지막 >></span>";

			bpg.firstLink = "<< 처음";
			bpg.firstBlockLink = "< 이전";
			bpg.prevLink = "";
			bpg.nextLink = "";
			bpg.lastBlockLink = "다음 >";
			bpg.lastLink = "마지막 >>";

			bpg.delimiter = "|";

			out.println(bpg.print());
		%></td></tr>
	</table>
	
	


</div>


