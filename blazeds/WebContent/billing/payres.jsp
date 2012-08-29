<%@ page contentType="text/html; charset=EUC-KR" %>
<%@page import="com.m.billing.Billing"%>
<%@page import="com.m.common.BooleanAndDescriptionVO"%>
<%@page import="com.common.VbyP"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.m.member.UserInformationVO"%>
<%@page import="com.m.member.SessionManagement"%>
<%@page import="com.m.billing.BillingVO"%>
<%@page import="com.common.util.SLibrary"%>
<%@ page import="java.util.*,java.net.*,com.allat.util.AllatUtil" %>
<%
  //Request Value Define
  //----------------------

  String sCrossKey = "1c902a8986ee0b8ff454bb1a55bcf535"; //설정필요 [사이트 참조 - http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#shop]
  String sShopId   = "GM_social";   //설정필요
  String sAmount   = (String)session.getAttribute("amount");               //결제 금액을 다시 계산해서 만들어야 함(해킹방지)  ( session, DB 사용 )


  String sEncData  = request.getParameter("allat_enc_data");
  String strReq = "";

  // 요청 데이터 설정
  //----------------------
  strReq  ="allat_shop_id="   +sShopId;
  strReq +="&allat_amt="      +sAmount;
  strReq +="&allat_enc_data=" +sEncData;
  strReq +="&allat_cross_key="+sCrossKey;

  // 올앳 결제 서버와 통신  : AllatUtil.approvalReq->통신함수, HashMap->결과값
  //-----------------------------------------------------------------------------
  AllatUtil util = new AllatUtil();
  HashMap hm     = null;
  hm = util.approvalReq(strReq, "SSL");

  // 결제 결과 값 확인
  //------------------
  String sReplyCd     = (String)hm.get("reply_cd");
  String sReplyMsg    = (String)hm.get("reply_msg");

  System.out.println("billing rsltcode: "+sReplyCd);
  /* 결과값 처리
  --------------------------------------------------------------------------
     결과 값이 '0000'이면 정상임. 단, allat_test_yn=Y 일경우 '0001'이 정상임.
     실제 결제   : allat_test_yn=N 일 경우 reply_cd=0000 이면 정상
     테스트 결제 : allat_test_yn=Y 일 경우 reply_cd=0001 이면 정상
  --------------------------------------------------------------------------*/
  if( sReplyCd.equals("0000") ){
    // reply_cd "0000" 일때만 성공
    String sOrderNo        = (String)hm.get("order_no");
    String sAmt            = (String)hm.get("amt");
    String sPayType        = (String)hm.get("pay_type");
    String sApprovalYmdHms = (String)hm.get("approval_ymdhms");
    String sSeqNo          = (String)hm.get("seq_no");
    String sApprovalNo     = (String)hm.get("approval_no");
    String sCardId         = (String)hm.get("card_id");
    String sCardNm         = (String)hm.get("card_nm");
    String sSellMm         = (String)hm.get("sell_mm");
    String sZerofeeYn      = (String)hm.get("zerofee_yn");
    String sCertYn         = (String)hm.get("cert_yn");
    String sContractYn     = (String)hm.get("contract_yn");
    String sSaveAmt        = (String)hm.get("save_amt");
    String sBankId         = (String)hm.get("bank_id");
    String sBankNm         = (String)hm.get("bank_nm");
    String sCashBillNo     = (String)hm.get("cash_bill_no");
    String sCashApprovalNo = (String)hm.get("cash_approval_no");
    String sEscrowYn       = (String)hm.get("escrow_yn");
    String sAccountNo      = (String)hm.get("account_no");
    String sAccountNm      = (String)hm.get("account_nm");
    String sIncomeAccNm    = (String)hm.get("income_account_nm");
    String sIncomeLimitYmd = (String)hm.get("income_limit_ymd");
    String sIncomeExpectYmd= (String)hm.get("income_expect_ymd");
    String sCashYn         = (String)hm.get("cash_yn");
    String sHpId           = (String)hm.get("hp_id");
    String sTicketId       = (String)hm.get("ticket_id");
    String sTicketPayType  = (String)hm.get("ticket_pay_type");
    String sTicketNm       = (String)hm.get("ticket_nm");

    System.out.println("결과코드               : " + sReplyCd          + "<br>");
    System.out.println("결과메세지             : " + sReplyMsg         + "<br>");
    System.out.println("주문번호               : " + sOrderNo          + "<br>");
    System.out.println("승인금액               : " + sAmt              + "<br>");
    System.out.println("지불수단               : " + sPayType          + "<br>");
    System.out.println("승인일시               : " + sApprovalYmdHms   + "<br>");
    System.out.println("거래일련번호           : " + sSeqNo            + "<br>");
    System.out.println("에스크로 적용 여부     : " + sEscrowYn         + "<br>");
    System.out.println("==================== 신용 카드 ===================<br>");
    System.out.println("승인번호               : " + sApprovalNo       + "<br>");
    System.out.println("카드ID                 : " + sCardId           + "<br>");
    System.out.println("카드명                 : " + sCardNm           + "<br>");
    System.out.println("할부개월               : " + sSellMm           + "<br>");
    System.out.println("무이자여부             : " + sZerofeeYn        + "<br>");   //무이자(Y),일시불(N)
    System.out.println("인증여부               : " + sCertYn           + "<br>");   //인증(Y),미인증(N)
    System.out.println("직가맹여부             : " + sContractYn       + "<br>");   //3자가맹점(Y),대표가맹점(N)
    System.out.println("세이브 결제 금액       : " + sSaveAmt          + "<br>");
    System.out.println("=============== 계좌 이체 / 가상계좌 =============<br>");
    System.out.println("은행ID                 : " + sBankId           + "<br>");
    System.out.println("은행명                 : " + sBankNm           + "<br>");
    System.out.println("현금영수증 일련 번호   : " + sCashBillNo       + "<br>");
    System.out.println("현금영수증 승인 번호   : " + sCashApprovalNo   + "<br>");
    System.out.println("===================== 가상계좌 ===================<br>");
    System.out.println("계좌번호               : " + sAccountNo        + "<br>");
    System.out.println("입금 계좌명            : " + sIncomeAccNm      + "<br>");
    System.out.println("입금자명               : " + sAccountNm        + "<br>");
    System.out.println("입금기한일             : " + sIncomeLimitYmd   + "<br>");
    System.out.println("입금예정일             : " + sIncomeExpectYmd  + "<br>");
    System.out.println("현금영수증신청 여부    : " + sCashYn           + "<br>");
    System.out.println("===================== 휴대폰 결제 ================<br>");
    System.out.println("이동통신사구분         : " + sHpId             + "<br>");
    System.out.println("===================== 상품권 결제 ================<br>");
    System.out.println("상품권ID               :" + sTicketId          + "<br>");
    System.out.println("상품권 이름            :" + sTicketPayType     + "<br>");
    System.out.println("결제구분               :" + sTicketNm          + "<br>");
    
    /*##################################################################*/
	String session_id = (String)session.getAttribute("user_id");
 	Connection conn = null;
 	SessionManagement sm = null;
 	String pay_name = sPayType;
 	int amount = SLibrary.intValue((String)session.getAttribute("amount"));
 	BooleanAndDescriptionVO badvo = null;
 	
 	try {
 		sm = new SessionManagement();
     	
     	
     	conn = VbyP.getDB();
     	if (conn == null)throw new Exception("DB연결에 실패 하였습니다.");
     	
     	
     	if (SLibrary.isNull(session_id)) throw new Exception("로그인이 필요 합니다.");
     	if (SLibrary.isNull(pay_name) || SLibrary.isNull(pay_name)) throw new Exception("결제 방식이 없습니다.");
     	if (amount <= 0) throw new Exception("결제금액이 없습니다.");

     	
		BillingVO bvo = new BillingVO();
		bvo.setUser_id(session_id);
		bvo.setAdmin_id("PG");
		bvo.setAmount( amount );
		bvo.setMemo("");
		bvo.setMethod(pay_name);
		bvo.setOrder_no(sSeqNo);
		bvo.setTid(sApprovalNo);
		bvo.setTimestamp(SLibrary.getDateTimeString("yyyyMMddHHmmss"));
		
		badvo = Billing.getInstance().setBilling(conn, bvo);
		
		out.println(SLibrary.alertScript("결제가 완료 되었습니다.","parent.window.location.reload();"));
		
 	}catch(Exception e) {
 		out.println(SLibrary.alertScript(e.getMessage()+" 관리자에게 문의 하세요.", ""));
 		
 	}finally {
 		if (conn != null) {
 			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("payres >> conn.close() Exception!"); }
 		}
 	}
	
 	

/*##################################################################*/

  }else{
    // reply_cd 가 "0000" 아닐때는 에러 (자세한 내용은 매뉴얼참조)
    // reply_msg 가 실패에 대한 메세지
//     out.println("결과코드               : " + sReplyCd  + "<br>");
//     out.println("결과메세지             : " + sReplyMsg + "<br>");
	  out.println(SLibrary.alertScript("결제에 실패하였습니다. 관리자에게 문의 하세요.", ""));
  }

%>
