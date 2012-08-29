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

  String sCrossKey = "1c902a8986ee0b8ff454bb1a55bcf535"; //�����ʿ� [����Ʈ ���� - http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#shop]
  String sShopId   = "GM_social";   //�����ʿ�
  String sAmount   = (String)session.getAttribute("amount");               //���� �ݾ��� �ٽ� ����ؼ� ������ ��(��ŷ����)  ( session, DB ��� )


  String sEncData  = request.getParameter("allat_enc_data");
  String strReq = "";

  // ��û ������ ����
  //----------------------
  strReq  ="allat_shop_id="   +sShopId;
  strReq +="&allat_amt="      +sAmount;
  strReq +="&allat_enc_data=" +sEncData;
  strReq +="&allat_cross_key="+sCrossKey;

  // �þ� ���� ������ ���  : AllatUtil.approvalReq->����Լ�, HashMap->�����
  //-----------------------------------------------------------------------------
  AllatUtil util = new AllatUtil();
  HashMap hm     = null;
  hm = util.approvalReq(strReq, "SSL");

  // ���� ��� �� Ȯ��
  //------------------
  String sReplyCd     = (String)hm.get("reply_cd");
  String sReplyMsg    = (String)hm.get("reply_msg");

  System.out.println("billing rsltcode: "+sReplyCd);
  /* ����� ó��
  --------------------------------------------------------------------------
     ��� ���� '0000'�̸� ������. ��, allat_test_yn=Y �ϰ�� '0001'�� ������.
     ���� ����   : allat_test_yn=N �� ��� reply_cd=0000 �̸� ����
     �׽�Ʈ ���� : allat_test_yn=Y �� ��� reply_cd=0001 �̸� ����
  --------------------------------------------------------------------------*/
  if( sReplyCd.equals("0000") ){
    // reply_cd "0000" �϶��� ����
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

    System.out.println("����ڵ�               : " + sReplyCd          + "<br>");
    System.out.println("����޼���             : " + sReplyMsg         + "<br>");
    System.out.println("�ֹ���ȣ               : " + sOrderNo          + "<br>");
    System.out.println("���αݾ�               : " + sAmt              + "<br>");
    System.out.println("���Ҽ���               : " + sPayType          + "<br>");
    System.out.println("�����Ͻ�               : " + sApprovalYmdHms   + "<br>");
    System.out.println("�ŷ��Ϸù�ȣ           : " + sSeqNo            + "<br>");
    System.out.println("����ũ�� ���� ����     : " + sEscrowYn         + "<br>");
    System.out.println("==================== �ſ� ī�� ===================<br>");
    System.out.println("���ι�ȣ               : " + sApprovalNo       + "<br>");
    System.out.println("ī��ID                 : " + sCardId           + "<br>");
    System.out.println("ī���                 : " + sCardNm           + "<br>");
    System.out.println("�Һΰ���               : " + sSellMm           + "<br>");
    System.out.println("�����ڿ���             : " + sZerofeeYn        + "<br>");   //������(Y),�Ͻú�(N)
    System.out.println("��������               : " + sCertYn           + "<br>");   //����(Y),������(N)
    System.out.println("�����Ϳ���             : " + sContractYn       + "<br>");   //3�ڰ�����(Y),��ǥ������(N)
    System.out.println("���̺� ���� �ݾ�       : " + sSaveAmt          + "<br>");
    System.out.println("=============== ���� ��ü / ������� =============<br>");
    System.out.println("����ID                 : " + sBankId           + "<br>");
    System.out.println("�����                 : " + sBankNm           + "<br>");
    System.out.println("���ݿ����� �Ϸ� ��ȣ   : " + sCashBillNo       + "<br>");
    System.out.println("���ݿ����� ���� ��ȣ   : " + sCashApprovalNo   + "<br>");
    System.out.println("===================== ������� ===================<br>");
    System.out.println("���¹�ȣ               : " + sAccountNo        + "<br>");
    System.out.println("�Ա� ���¸�            : " + sIncomeAccNm      + "<br>");
    System.out.println("�Ա��ڸ�               : " + sAccountNm        + "<br>");
    System.out.println("�Աݱ�����             : " + sIncomeLimitYmd   + "<br>");
    System.out.println("�Աݿ�����             : " + sIncomeExpectYmd  + "<br>");
    System.out.println("���ݿ�������û ����    : " + sCashYn           + "<br>");
    System.out.println("===================== �޴��� ���� ================<br>");
    System.out.println("�̵���Ż籸��         : " + sHpId             + "<br>");
    System.out.println("===================== ��ǰ�� ���� ================<br>");
    System.out.println("��ǰ��ID               :" + sTicketId          + "<br>");
    System.out.println("��ǰ�� �̸�            :" + sTicketPayType     + "<br>");
    System.out.println("��������               :" + sTicketNm          + "<br>");
    
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
     	if (conn == null)throw new Exception("DB���ῡ ���� �Ͽ����ϴ�.");
     	
     	
     	if (SLibrary.isNull(session_id)) throw new Exception("�α����� �ʿ� �մϴ�.");
     	if (SLibrary.isNull(pay_name) || SLibrary.isNull(pay_name)) throw new Exception("���� ����� �����ϴ�.");
     	if (amount <= 0) throw new Exception("�����ݾ��� �����ϴ�.");

     	
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
		
		out.println(SLibrary.alertScript("������ �Ϸ� �Ǿ����ϴ�.","parent.window.location.reload();"));
		
 	}catch(Exception e) {
 		out.println(SLibrary.alertScript(e.getMessage()+" �����ڿ��� ���� �ϼ���.", ""));
 		
 	}finally {
 		if (conn != null) {
 			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("payres >> conn.close() Exception!"); }
 		}
 	}
	
 	

/*##################################################################*/

  }else{
    // reply_cd �� "0000" �ƴҶ��� ���� (�ڼ��� ������ �Ŵ�������)
    // reply_msg �� ���п� ���� �޼���
//     out.println("����ڵ�               : " + sReplyCd  + "<br>");
//     out.println("����޼���             : " + sReplyMsg + "<br>");
	  out.println(SLibrary.alertScript("������ �����Ͽ����ϴ�. �����ڿ��� ���� �ϼ���.", ""));
  }

%>
