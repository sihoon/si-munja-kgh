<%@ page  language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR" %>
<%@ page import="java.io.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.security.MessageDigest" %>

<%@ page import="com.common.VbyP,com.m.member.SessionManagement,com.m.member.UserInformationVO,com.common.util.SLibrary,com.common.UseFunction"%>

<%
	String user_id = "";
	UserInformationVO uvo = null;
	String amount = "0";
	SessionManagement sm = null;
try {
	
	sm = new SessionManagement();
	user_id = (String)session.getAttribute("user_id");
	if (SLibrary.isNull(user_id)) throw new Exception("로그인 되어 있지 않습니다.");
	
	amount = SLibrary.IfNull(request.getParameter("amount"));
	if (SLibrary.intValue(amount) == 0) throw new Exception("결제 금액이 없습니다.");
	
	session.setAttribute("amount", amount);
	uvo = sm.getUserInformation(user_id);
    /*
     * [결제 인증요청 페이지(STEP2-1)]
     *
     * 샘플페이지에서는 기본 파라미터만 예시되어 있으며, 별도로 필요하신 파라미터는 연동메뉴얼을 참고하시어 추가 하시기 바랍니다.
     */

    /*
     * 1. 기본결제 인증요청 정보 변경
     *
     * 기본정보를 변경하여 주시기 바랍니다.(파라미터 전달시 POST를 사용하세요)
     */
    String CST_PLATFORM         = "service";                 //LG데이콤 결제서비스 선택(test:테스트, service:서비스)
    String CST_MID              = "munja119";                      //LG데이콤으로 부터 발급받으신 상점아이디를 입력하세요.
    String LGD_MID              = ("test".equals(CST_PLATFORM.trim())?"t":"")+CST_MID;  //테스트 아이디는 't'를 제외하고 입력하세요.
                                                                                        //상점아이디(자동생성)
    String LGD_OID              = SLibrary.getDateTimeString("yyyyMMddHHmmss")+"_"+uvo.getUser_id();                      //주문번호(상점정의 유니크한 주문번호를 입력하세요)
    String LGD_AMOUNT           = SLibrary.IfNull(request.getParameter("amount"));                   //결제금액("," 를 제외한 결제금액을 입력하세요)
    String LGD_MERTKEY          = "5cc68c0e79ac7d9e59821804c062edd2";                  //상점MertKey(mertkey는 상점관리자 -> 계약정보 -> 상점정보관리에서 확인하실수 있습니다)
    String LGD_BUYER            = uvo.getUser_name();                    //구매자명
    String LGD_PRODUCTINFO      = "문자 충전";              //상품명
    String LGD_BUYEREMAIL       = "";               //구매자 이메일
    String LGD_TIMESTAMP        = SLibrary.getDateTimeString("yyyyMMddHHmmss");                //타임스탬프
    String LGD_CUSTOM_SKIN      = "blue";                                                //상점정의 결제창 스킨(red, blue, cyan, green, yellow)
    String req_smethod = SLibrary.IfNull(VbyP.getPOST(request.getParameter("smethod")));
    String LGD_CUSTOM_FIRSTPAY  = "";

	if (req_smethod.equals("카드")) LGD_CUSTOM_FIRSTPAY = "SC0010";
	else if (req_smethod.equals("계좌이체")) LGD_CUSTOM_FIRSTPAY = "SC0030";
	else LGD_CUSTOM_FIRSTPAY = "";
	
	
    /*
     * 가상계좌(무통장) 결제 연동을 하시는 경우 아래 LGD_CASNOTEURL 을 설정하여 주시기 바랍니다. 
     */    
    String LGD_CASNOTEURL		= "http://www.munja119.com/cas_noteurl.jsp";    

    /*
     *************************************************
     * 2. MD5 해쉬암호화 (수정하지 마세요) - BEGIN
     *
     * MD5 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
     *************************************************
     *
     * 해쉬 암호화 적용( LGD_MID + LGD_OID + LGD_AMOUNT + LGD_TIMESTAMP + LGD_MERTKEY )
     * LGD_MID          : 상점아이디
     * LGD_OID          : 주문번호
     * LGD_AMOUNT       : 금액
     * LGD_TIMESTAMP    : 타임스탬프
     * LGD_MERTKEY      : 상점MertKey (mertkey는 상점관리자 -> 계약정보 -> 상점정보관리에서 확인하실수 있습니다)
     *
     * MD5 해쉬데이터 암호화 검증을 위해
     * LG데이콤에서 발급한 상점키(MertKey)를 환경설정 파일(lgdacom/conf/mall.conf)에 반드시 입력하여 주시기 바랍니다.
     */
    StringBuffer sb = new StringBuffer();
    sb.append(LGD_MID);
    sb.append(LGD_OID);
    sb.append(LGD_AMOUNT);
    sb.append(LGD_TIMESTAMP);
    sb.append(LGD_MERTKEY);

    byte[] bNoti = sb.toString().getBytes();
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] digest = md.digest(bNoti);

    StringBuffer strBuf = new StringBuffer();
    for (int i=0 ; i < digest.length ; i++) {
        int c = digest[i] & 0xff;
        if (c <= 15){
            strBuf.append("0");
        }
        strBuf.append(Integer.toHexString(c));
    }

    String LGD_HASHDATA = strBuf.toString();
    String LGD_CUSTOM_PROCESSTYPE = "TWOTR";
    /*
     *************************************************
     * 2. MD5 해쉬암호화 (수정하지 마세요) - END
     *************************************************
     */
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>New All@Pay</title>
<script language=JavaScript charset='euc-kr' src="https://tx.allatpay.com/common/AllatPayRE.js"></script>
<script language=Javascript>
function ftn_approval(dfm) {
  var ret;
  ret = visible_Approval(dfm);//Function 내부에서 submit을 하게 되어있음.
  if( ret.substring(0,4)!="0000" && ret.substring(0,4)!="9999"){
    // 오류 코드 : 0001~9998 의 오류에 대해서 적절한 처리를 해주시기 바랍니다.
    alert(ret.substring(4,ret.length));     // Message 가져오기
  }
  if( ret.substring(0,4)=="9999" ){
    // 오류 코드 : 9999 의 오류에 대해서 적절한 처리를 해주시기 바랍니다.
    alert(ret.substring(8,ret.length));     // Message 가져오기
  }
}
</script>
</head>

<body onload="ftn_approval(document.fm);">
<p align=center class=title><u>New All@Pay™ 승인요청 예제페이지</u></p>

    <!------------- HTML : Form 설정 --------------//-->
    <form name="fm"  method=POST action="payres.jsp"> <!--승인요청 및 결과수신페이지 지정 //-->

    <table border=0 cellpadding=0 cellspacing=1 bgcolor="#606060" width=1152 align=center style="TABLE-LAYOUT: fixed;">
    <font color=red>◆ 필수정보 : <b>결제 필수 항목</b></font>
    &nbsp;&nbsp;&nbsp;&nbsp<a target=_new href="http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp"><b>[FAQ]</b></a>
    <tr>
        <td width="140" class="head">항목</td>
        <td width="160" class="body">예시 값</td>
        <td width="70"  class="body">&nbsp최대길이<br>(영문기준)</td>
        <td width="120"  class="body">변수명</td>
        <td class="body">변수 설명</td>
    </tr>
    <tr>
        <td class="head">상점 ID</td>
        <td class="body"><input type=text name="allat_shop_id" value="GM_social" size="19" maxlength=20></td>
        <td class="body">20</td>
        <td class="body">allat_shop_id</td>
        <td class="body">Allat에서 발급한 고유 상점 ID
          &nbsp;&nbsp<a target=_new href="http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#shop"><b>[설명]</b></a></td>
    </tr>
    <tr>
        <td class="head">주문번호</td>
        <td class="body"><input type=text name="allat_order_no" value="<%=LGD_OID %>" size="19" maxlength=70></td>
        <td class="body">70</td>
        <td class="body">allat_order_no</td>
        <td class="body">쇼핑몰에서 사용하는 고유 주문번호 : 공백,작은따옴표('),큰따옴표(") 사용 불가</td>
    </tr>
    <tr>
        <td class="head">승인금액</td>
        <td class="body"><input type=text name="allat_amt" value="<%=LGD_AMOUNT %>" size="19" maxlength=10></td>
        <td class="body">10</td>
        <td class="body">allat_amt</td>
        <td class="body">총 결제금액 : 숫자(0~9)만 사용가능</td>
    </tr>
    <tr>
        <td class="head">회원ID</td>
        <td class="body"><input type=text name="allat_pmember_id" value="<%=uvo.getUser_id() %>" size="19" maxlength=20></td>
        <td class="body">20</td>
        <td class="body">allat_pmember_id</td>
        <td class="body">쇼핑몰의 회원ID : 공백,작은따옴표('),큰따옴표(") 사용 불가</td>
    </tr>
    <tr>
        <td class="head">상품코드</td>
        <td class="body"><input type=text name="allat_product_cd" value="munjarang" size="19" maxlength=1000></td>
        <td class="body">1000</td>
        <td class="body">allat_product_cd</td>
        <td class="body">여러 상품의 경우 구분자 이용, 구분자('||':파이프 2개) : 공백,작은따옴표('),큰따옴표(") 사용 불가</td>
    </tr>
    <tr>
        <td class="head">상품명</td>
        <td class="body"><input type=text name="allat_product_nm" value="<%=LGD_PRODUCTINFO %>" size="19" maxlength=1000></td>
        <td class="body">1000</td>
        <td class="body">allat_product_nm</td>
        <td class="body">여러 상품의 경우 구분자 이용, 구분자('||':파이프 2개)</td>
    </tr>
    <tr>
        <td class="head">결제자성명</td>
        <td class="body"><input type=text name="allat_buyer_nm" value="<%=LGD_BUYER %>" size="19" maxlength=20></td>
        <td class="body">20</td>
        <td class="body">allat_buyer_nm</td>        
        <td class="body"></td>
    </tr>
    <tr>
        <td class="head">수취인성명</td>
        <td class="body"><input type=text name="allat_recp_nm" value="<%=LGD_BUYER %>" size="19" maxlength=20></td>
        <td class="body">20</td>
        <td class="body">allat_recp_nm</td>        
        <td class="body"></td>
    </tr>
    <tr>
        <td class="head">수취인주소</td>
        <td class="body"><input type=text name="allat_recp_addr" value="없음" size="19" maxlength=120></td>
        <td class="body">120</td>
        <td class="body">allat_recp_addr</td>        
        <td class="body"></td>
    </tr>
    <tr>
        <td class="head">주문정보암호화필드</td>
        <td class="body"><font color=red>값은 자동으로 설정됨</font></td>
        <td class="body">-</td>
        <td class="body">allat_enc_data</td>
        <td class="body"><font color=red>&ltinput type=hidden name=allat_enc_data value=''&gt<br>
                          ※hidden field로 설정해야함</font>
                          &nbsp;&nbsp<a target=_new href="http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#enc_data"><b>[설명]</b></a></td>
        <input type=hidden name=allat_enc_data value=''>
    </tr>
    </table>
    <br>
    <table border=0 cellpadding=0 cellspacing=1 bgcolor="#606060" width=1152 align=center style="TABLE-LAYOUT: fixed;" >
    <font color=blue><b>◆ 옵션정보</b>( 값이나 필드가 없을 경우 상점 속성이나 Default값이 반영됨 )</font>
    <tr>
        <td width=140 class="head">신용카드 결제<br>사용 여부</td>
        <td width=160 class="body"><input type=text name="allat_card_yn" value="<%=(req_smethod.equals("card"))?"Y":"" %>" size="19" maxlength=1></td>
        <td width=70  class="body">1</td>
        <td width=120 class="body">allat_card_yn</td>        
        <td class="body">사용(Y),사용하지 않음(N) - Default : 상점속성</td>
    </tr>
    <tr>
        <td class="head">계좌이체 결제<br>사용 여부</td>
        <td class="body"><input type=text name="allat_bank_yn" value="<%=(req_smethod.equals("계좌이체"))?"Y":"" %>" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_bank_yn</td>                
        <td class="body">사용(Y),사용하지 않음(N) - Default : 상점속성</td>
    </tr>
    <tr>
        <td class="head">무통장(가상계좌) 결제<br>사용 여부</td>
        <td class="body"><input type=text name="allat_vbank_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_vbank_yn</td>                        
        <td class="body">사용(Y),사용하지 않음(N) - Default : 상점속성</td>
    </tr>
    <tr>
        <td class="head">휴대폰 결제<br>사용 여부</td>
        <td class="body"><input type=text name="allat_hp_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_hp_yn</td>                        
        <td class="body">사용(Y),사용하지 않음(N) - Default : 상점속성</td>
    </tr>
    <tr>
        <td class="head">상품권 결제<br>사용 여부</td>
        <td class="body"><input type=text name="allat_ticket_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_ticket_yn</td>                        
        <td class="body">사용(Y),사용하지 않음(N) - Default : 상점속성</td>
    </tr>
    <tr>
        <td class="head">무통장(가상계좌)<br>인증 Key</td>
        <td class="body"><input type=text name="allat_account_key" value="" size="19" maxlength=20></td>
        <td class="body">20</td>
        <td class="body">allat_account_key</td>                        
        <td class="body">계좌 채번방식이 Key별 방식일 때만 사용함<br>
            <font color=blue>건별 채번방식일때 무시</font></td>
    </tr>
    <tr>
        <td class="head">과세여부</td>
        <td class="body"><input type=text name="allat_tax_yn" value="Y" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_tax_yn</td>                        
        <td class="body">Y(과세), N(비과세) - Default : Y</td>
    </tr>
    <tr>
        <td class="head">할부 사용여부</td>
        <td class="body"><input type=text name="allat_sell_yn" value="Y" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_sell_yn</td>                        
        <td class="body">할부사용(Y), 할부 사용않함(N) - Default : Y</td>
    </tr>
    <tr>
        <td class="head">일반/무이자 할부<br>사용여부</td>
        <td class="body"><input type=text name="allat_zerofee_yn" value="Y" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_zerofee_yn</td>                        
        <td class="body">일반(N), 무이자 할부(Y) - Default :N
          &nbsp;&nbsp<a target=_new href="http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#zerofee"><b>[설명]</b></a></td>
    </tr>
    <tr>
        <td class="head">카드 인증 여부</td>
        <td class="body"><input type=text name="allat_cardcert_yn" value="N" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_cardcert_yn</td>                        
        <td class="body">인증(Y), 인증 사용않음(N), 인증만 사용(X) - Default : N</td>
    </tr>
    <tr>
        <td class="head">포인트 사용 여부</td>
        <td class="body"><input type=text name="allat_bonus_yn" value="N" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_bonus_yn</td>                        
        <td class="body">사용(Y), 사용 않음(N) - Default : N
          &nbsp;&nbsp<a target=_new href="http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#point"><b>[설명]</b></a></td>
    </tr>
    <tr>
        <td class="head">현금 영수증 발급 여부</td>
        <td class="body"><input type=text name="allat_cash_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_cash_yn</td>                        
        <td class="body">사용(Y), 사용 않음(N) - Default : 상점속성
          &nbsp;&nbsp<a target=_new href="http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#cash"><b>[설명]</b></a></td>
    </tr>
    <tr>
        <td class="head">상품이미지 URL</td>
        <td class="body"><input type=text name="allat_product_img" value="http://" size="19" maxlength=256></td>
        <td class="body">256</td>
        <td class="body">all_product_img</td>                        
        <td class="body">PlugIn에 보여질 상품이미지 Full URL</td>
    </tr>
    <tr>
        <td class="head">결제 정보 수신 E-mail</td>
        <td class="body"><input type=text name="allat_email_addr" value="" size="19" maxlength=50></td>
        <td class="body">50</td>
        <td class="body">allat_email_addr</td>                        
        <td class="body"><font color=red>에스크로 서비스 사용시에 필수 필드.(결제창에서 E-Mail주소를 넣을 수도 있음)</font></td>
    </tr>
    <tr>
        <td class="head">테스트 여부</td>
        <td class="body"><input type=text name="allat_test_yn" value="N" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_test_yn</td>                        
        <td class="body">테스트(Y),서비스(N) - Default : N
          &nbsp;&nbsp<a target=_new href="http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#test"><b>[설명]</b></a></td>
    </tr>
    <tr>
        <td class="head">상품 실물 여부</td>
        <td class="body"><input type=text name="allat_real_yn" value="N" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_real_yn</td>                        
        <td class="body">상품이 실물일 경우 (Y), 상품이 실물이 아닐경우 (N) - Default : N<br>
            <font color=blue>상품이 실물이고, 10만원 이상 계좌이체시 에스크로 적용여부 이용</font>
              &nbsp;&nbsp<a target=_new href="http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#escrow"><b>[설명]</b></a></td>
    </tr>
    <tr>
        <td class="head">카드 에스크로<br>적용여부</td>
        <td class="body"><input type=text name="allat_cardes_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_cardes_yn</td>                        
        <td class="body">카드 결제에 대한 에스크로 적용여부 : 적용 (Y), 미적용 (N), 고객선택 : 값없음 - Default : 값없음<br>
            <font color=blue>에스크로 적용 대상 결제건에 대해서만 적용됨</font></td>
    </tr>
    <tr>
        <td class="head">계좌이체 에스크로<br>적용여부</td>
        <td class="body"><input type=text name="allat_bankes_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_bankes_yn</td>                        
        <td class="body">계좌이체 결제에 대한 에스크로 적용여부 : 적용 (Y), 미적용 (N), 고객선택 : 없음 - Default : 없음<br>
            <font color=blue>에스크로 적용 대상 결제건에 대해서만 적용됨</font></td>
    </tr>
    <tr>
        <td class="head">무통장(가상계좌) 에스<br>크로 적용여부</td>
        <td class="body"><input type=text name="allat_vbankes_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_vbankes_yn</td>                        
        <td class="body">가상계좌 결제에 대한 에스크로 적용여부 : 적용 (Y), 미적용 (N), 고객선택 : 없음 - Default : 없음<br>
            <font color=blue>에스크로 적용 대상 결제건에 대해서만 적용됨</font></td>
    </tr>
    <tr>
        <td class="head">휴대폰 에스크로<br>적용여부</td>
        <td class="body"><input type=text name="allat_hpes_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_hpes_yn</td>                        
        <td class="body">휴대폰 결제에 대한 에스크로 적용여부 : 적용 (Y), 미적용 (N), 고객선택 : 없음 - Default : 없음<br>
            <font color=blue>에스크로 적용 대상 결제건에 대해서만 적용됨</font></td>
    </tr>
    <tr>
        <td class="head">상품권 에스크로<br>적용여부</td>
        <td class="body"><input type=text name="allat_ticketes_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_ticketes_yn</td>                        
        <td class="body">상품권 결제에 대한 에스크로 적용여부 : 적용 (Y), 미적용 (N), 고객선택 : 없음 - Default : 없음<br>
            <font color=blue>에스크로 적용 대상 결제건에 대해서만 적용됨</font></td>
    </tr>
    <tr>
        <td class="head">주민번호</td>
        <td class="body"><input type=text name="allat_registry_no" value="" size="19" maxlength=13></td>
        <td class="body">1</td>
        <td class="body">allat_registry_no</td>                        
        <td class="body">
        <font color=blue> ISP     - 주민번호 13자리(ISP일때는 특정 사업자만 사용함.대부분 사용하지 않음)</font></td>
    </tr>
	<tr>
        <td class="head">성별</td>
        <td class="body"><input type=text name="allat_gender" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_gender</td>
        <td class="body">구매자 성별, 남자(M)/여자(F)</td>
    </tr>
    <tr>
        <td class="head">생년월일</td>
        <td class="body"><input type=text name="allat_birth_ymd" value="" size="19" maxlength=8></td>
        <td class="body">8</td>
        <td class="body">allat_birth_ymd</td>                                
        <td class="body">구매자의 생년월일 8자, YYYYMMDD형식</td>
    </tr>
    </table>
    <p align=center>
    <table border=0 cellpadding=0 cellspacing=1 width=1152 align=center>
    <tr><td align=center>
    <input type=button value="  결  제  " name=app_btn onClick="javascript:ftn_approval(document.fm);">
    </td></tr>
    </table>
    </p>
    </form>
</body>
<!---- 수동 설치 설명 Layer를 사용하지 않으시려면, 아래 스크립트를 삭제 해 주시기 바랍니다 ---->
<script language=Javascript>initCheckOB();</script>
</html>
<%
}catch(Exception e) {
	out.println(SLibrary.alertScript(e.getMessage(),"/*"+e.toString()+"*/"));
}
%>