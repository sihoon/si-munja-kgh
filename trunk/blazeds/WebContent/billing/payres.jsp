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
<%@ page import="lgdacom.XPayClient.XPayClient;"%>

<%
    /*
     * [최종결제요청 페이지(STEP2-2)]
     *
     * LG텔레콤으로 부터 내려받은 LGD_PAYKEY(인증Key)를 가지고 최종 결제요청.(파라미터 전달시 POST를 사용하세요)
     */

    String configPath = "/home/sms/webapps/WebContent/lgdacom";  //LG텔레콤에서 제공한 환경파일("/conf/lgdacom.conf,/conf/mall.conf") 위치 지정.
    
    /*
     *************************************************
     * 1.최종결제 요청 - BEGIN
     *  (단, 최종 금액체크를 원하시는 경우 금액체크 부분 주석을 제거 하시면 됩니다.)
     *************************************************
     */
    
    String CST_PLATFORM                 = request.getParameter("CST_PLATFORM");
    String CST_MID                      = request.getParameter("CST_MID");
    String LGD_MID                      = ("test".equals(CST_PLATFORM.trim())?"t":"")+CST_MID;
    String LGD_PAYKEY                   = request.getParameter("LGD_PAYKEY");

    //해당 API를 사용하기 위해 WEB-INF/lib/XPayClient.jar 를 Classpath 로 등록하셔야 합니다. 
    XPayClient xpay = new XPayClient();
   	boolean isInitOK = xpay.Init(configPath, CST_PLATFORM);   	

   	if( !isInitOK ) {
    	//API 초기화 실패 화면처리
        out.println( "결제요청을 초기화 하는데 실패하였습니다.<br>");
        out.println( "LG텔레콤에서 제공한 환경파일이 정상적으로 설치 되었는지 확인하시기 바랍니다.<br>");        
        out.println( "mall.conf에는 Mert ID = Mert Key 가 반드시 등록되어 있어야 합니다.<br><br>");
        out.println( "문의전화 LG텔레콤 1544-7772<br>");
        SLibrary.alertScript("결제요청을 초기화 하는데 실패하였습니다.","");
        return;
   	
   	}else{      
   		try{
   			/*
   	   	     *************************************************
   	   	     * 1.최종결제 요청(수정하지 마세요) - END
   	   	     *************************************************
   	   	     */
	    	xpay.Init_TX(LGD_MID);
	    	xpay.Set("LGD_TXNAME", "PaymentByKey");
	    	xpay.Set("LGD_PAYKEY", LGD_PAYKEY);
	    
	    	//금액을 체크하시기 원하는 경우 아래 주석을 풀어서 이용하십시요.
	    	//String DB_AMOUNT = "DB나 세션에서 가져온 금액"; //반드시 위변조가 불가능한 곳(DB나 세션)에서 금액을 가져오십시요.
	    	//xpay.Set("LGD_AMOUNTCHECKYN", "Y");
	    	//xpay.Set("LGD_AMOUNT", DB_AMOUNT);
	    	
    	}catch(Exception e) {
    		out.println("LG텔레콤 제공 API를 사용할 수 없습니다. 환경파일 설정을 확인해 주시기 바랍니다. ");
    		out.println(""+e.getMessage());  
    		SLibrary.alertScript("LG텔레콤 제공 API를 사용할 수 없습니다.","");
    		return;
    	}
   	}

    /*
     * 2. 최종결제 요청 결과처리
     *
     * 최종 결제요청 결과 리턴 파라미터는 연동메뉴얼을 참고하시기 바랍니다.
     */
     if ( xpay.TX() ) {
         //1)결제결과 화면처리(성공,실패 결과 처리를 하시기 바랍니다.)

         out.println( "결제요청이 완료되었습니다.  <br>");
         out.println( "TX 결제요청 Response_code = " + xpay.m_szResCode + "<br>");
         out.println( "TX 결제요청 Response_msg = " + xpay.m_szResMsg + "<p>");
         
         out.println("거래번호 : " + xpay.Response("LGD_TID",0) + "<br>");
         out.println("상점아이디 : " + xpay.Response("LGD_MID",0) + "<br>");
         out.println("상점주문번호 : " + xpay.Response("LGD_OID",0) + "<br>");
         out.println("결제금액 : " + xpay.Response("LGD_AMOUNT",0) + "<br>");
         out.println("결과코드 : " + xpay.Response("LGD_RESPCODE",0) + "<br>");
         out.println("결과메세지 : " + xpay.Response("LGD_RESPMSG",0) + "<p>");

         for (int i = 0; i < xpay.ResponseNameCount(); i++)
         {
             out.println(xpay.ResponseName(i) + " = ");
             for (int j = 0; j < xpay.ResponseCount(); j++)
             {
                 out.println("\t" + xpay.Response(xpay.ResponseName(i), j) + "<br>");
             }
         }
         out.println("<p>");
         
         if( "0000".equals( xpay.m_szResCode ) ) {
         	//최종결제요청 결과 성공 DB처리
         	//out.println("최종결제요청 결과 성공 DB처리하시기 바랍니다.<br>");            	
         	//최종결제요청 결과 성공 DB처리 실패시 Rollback 처리
         	boolean isDBOK = true; //DB처리 실패시 false로 변경해 주세요.
         	
/*##################################################################*/
			String session_id = (String)session.getAttribute("user_id");
         	Connection conn = null;
         	SessionManagement sm = null;
         	String pay_code = "";
         	String pay_name = "";
         	int amount = SLibrary.intValue(xpay.Response("LGD_AMOUNT",0));
         	BooleanAndDescriptionVO badvo = null;
         	
         	try {
         		sm = new SessionManagement();
             	pay_code = SLibrary.IfNull(xpay.Response("LGD_PAYTYPE",0));
             	
             	conn = VbyP.getDB();
             	if (conn == null)throw new Exception("DB연결에 실패 하였습니다.");
             	
	         	if (pay_code.equals("SC0010")) pay_name="카드";
	         	else if (pay_code.equals("SC0030")) pay_name="계좌이체";
	         	
	         	if (SLibrary.isNull(session_id)) throw new Exception("로그인이 필요 합니다.");
	         	if (SLibrary.isNull(pay_code) || SLibrary.isNull(pay_name)) throw new Exception("결제 방식이 없습니다.");

	         	
				BillingVO bvo = new BillingVO();
				bvo.setUser_id(session_id);
				bvo.setAdmin_id("PG");
				bvo.setAmount( amount );
				bvo.setMemo("");
				bvo.setMethod(pay_name);
				bvo.setOrder_no(xpay.Response("LGD_OID",0));
				bvo.setTid(xpay.Response("LGD_TID",0));
				bvo.setTimestamp(SLibrary.getDateTimeString("yyyyMMddHHmmss"));
				
				badvo = Billing.getInstance().setBilling(conn, bvo);
				
         	}catch(Exception e) {
         		out.println(SLibrary.alertScript(e.getMessage(), ""));
         		isDBOK = false;
         	}finally {
         		if (conn != null) {
         			try { if ( conn != null ) conn.close();
        			}catch(SQLException e) { VbyP.errorLog("payres >> conn.close() Exception!"); }
         		}
         	}
			
			if (isDBOK) {
				out.println(SLibrary.alertScript("결제가 완료 되었습니다.","parent.window.location.reload();"));
			}
/*##################################################################*/
         	
         	
         	
         	if( !isDBOK ) {
         		xpay.Rollback("상점 DB처리 실패로 인하여 Rollback 처리 [TID:" +xpay.Response("LGD_TID",0)+",MID:" + xpay.Response("LGD_MID",0)+",OID:"+xpay.Response("LGD_OID",0)+"]");
         		
                 out.println( "TX Rollback Response_code = " + xpay.Response("LGD_RESPCODE",0) + "<br>");
                 out.println( "TX Rollback Response_msg = " + xpay.Response("LGD_RESPMSG",0) + "<p>");
         		
                 if( "0000".equals( xpay.m_szResCode ) ) {
                 	out.println("자동취소가 정상적으로 완료 되었습니다.<br>");
                 }else{
         			out.println("자동취소가 정상적으로 처리되지 않았습니다.<br>");
                 }
         	}
         	
         }else{
         	//최종결제요청 결과 실패 DB처리
         	out.println("최종결제요청 결과 실패 DB처리하시기 바랍니다.<br>");            	
         }
     }else {
         //2)API 요청실패 화면처리
         out.println( "결제요청이 실패하였습니다.  <br>");
         out.println( "TX 결제요청 Response_code = " + xpay.m_szResCode + "<br>");
         out.println( "TX 결제요청 Response_msg = " + xpay.m_szResMsg + "<p>");
         
     	//최종결제요청 결과 실패 DB처리
     	out.println("최종결제요청 결과 실패 DB처리하시기 바랍니다.<br>");
     	SLibrary.alertScript("결제요청이 실패하였습니다.","");
     }
%>
