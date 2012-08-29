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
	if (SLibrary.isNull(user_id)) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
	
	amount = SLibrary.IfNull(request.getParameter("amount"));
	if (SLibrary.intValue(amount) == 0) throw new Exception("���� �ݾ��� �����ϴ�.");
	
	session.setAttribute("amount", amount);
	uvo = sm.getUserInformation(user_id);
    /*
     * [���� ������û ������(STEP2-1)]
     *
     * ���������������� �⺻ �Ķ���͸� ���õǾ� ������, ������ �ʿ��Ͻ� �Ķ���ʹ� �����޴����� �����Ͻþ� �߰� �Ͻñ� �ٶ��ϴ�.
     */

    /*
     * 1. �⺻���� ������û ���� ����
     *
     * �⺻������ �����Ͽ� �ֽñ� �ٶ��ϴ�.(�Ķ���� ���޽� POST�� ����ϼ���)
     */
    String CST_PLATFORM         = "service";                 //LG������ �������� ����(test:�׽�Ʈ, service:����)
    String CST_MID              = "munja119";                      //LG���������� ���� �߱޹����� �������̵� �Է��ϼ���.
    String LGD_MID              = ("test".equals(CST_PLATFORM.trim())?"t":"")+CST_MID;  //�׽�Ʈ ���̵�� 't'�� �����ϰ� �Է��ϼ���.
                                                                                        //�������̵�(�ڵ�����)
    String LGD_OID              = SLibrary.getDateTimeString("yyyyMMddHHmmss")+"_"+uvo.getUser_id();                      //�ֹ���ȣ(�������� ����ũ�� �ֹ���ȣ�� �Է��ϼ���)
    String LGD_AMOUNT           = SLibrary.IfNull(request.getParameter("amount"));                   //�����ݾ�("," �� ������ �����ݾ��� �Է��ϼ���)
    String LGD_MERTKEY          = "5cc68c0e79ac7d9e59821804c062edd2";                  //����MertKey(mertkey�� ���������� -> ������� -> ���������������� Ȯ���ϽǼ� �ֽ��ϴ�)
    String LGD_BUYER            = uvo.getUser_name();                    //�����ڸ�
    String LGD_PRODUCTINFO      = "���� ����";              //��ǰ��
    String LGD_BUYEREMAIL       = "";               //������ �̸���
    String LGD_TIMESTAMP        = SLibrary.getDateTimeString("yyyyMMddHHmmss");                //Ÿ�ӽ�����
    String LGD_CUSTOM_SKIN      = "blue";                                                //�������� ����â ��Ų(red, blue, cyan, green, yellow)
    String req_smethod = SLibrary.IfNull(VbyP.getPOST(request.getParameter("smethod")));
    String LGD_CUSTOM_FIRSTPAY  = "";

	if (req_smethod.equals("ī��")) LGD_CUSTOM_FIRSTPAY = "SC0010";
	else if (req_smethod.equals("������ü")) LGD_CUSTOM_FIRSTPAY = "SC0030";
	else LGD_CUSTOM_FIRSTPAY = "";
	
	
    /*
     * �������(������) ���� ������ �Ͻô� ��� �Ʒ� LGD_CASNOTEURL �� �����Ͽ� �ֽñ� �ٶ��ϴ�. 
     */    
    String LGD_CASNOTEURL		= "http://www.munja119.com/cas_noteurl.jsp";    

    /*
     *************************************************
     * 2. MD5 �ؽ���ȣȭ (�������� ������) - BEGIN
     *
     * MD5 �ؽ���ȣȭ�� �ŷ� �������� �������� ����Դϴ�.
     *************************************************
     *
     * �ؽ� ��ȣȭ ����( LGD_MID + LGD_OID + LGD_AMOUNT + LGD_TIMESTAMP + LGD_MERTKEY )
     * LGD_MID          : �������̵�
     * LGD_OID          : �ֹ���ȣ
     * LGD_AMOUNT       : �ݾ�
     * LGD_TIMESTAMP    : Ÿ�ӽ�����
     * LGD_MERTKEY      : ����MertKey (mertkey�� ���������� -> ������� -> ���������������� Ȯ���ϽǼ� �ֽ��ϴ�)
     *
     * MD5 �ؽ������� ��ȣȭ ������ ����
     * LG�����޿��� �߱��� ����Ű(MertKey)�� ȯ�漳�� ����(lgdacom/conf/mall.conf)�� �ݵ�� �Է��Ͽ� �ֽñ� �ٶ��ϴ�.
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
     * 2. MD5 �ؽ���ȣȭ (�������� ������) - END
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
  ret = visible_Approval(dfm);//Function ���ο��� submit�� �ϰ� �Ǿ�����.
  if( ret.substring(0,4)!="0000" && ret.substring(0,4)!="9999"){
    // ���� �ڵ� : 0001~9998 �� ������ ���ؼ� ������ ó���� ���ֽñ� �ٶ��ϴ�.
    alert(ret.substring(4,ret.length));     // Message ��������
  }
  if( ret.substring(0,4)=="9999" ){
    // ���� �ڵ� : 9999 �� ������ ���ؼ� ������ ó���� ���ֽñ� �ٶ��ϴ�.
    alert(ret.substring(8,ret.length));     // Message ��������
  }
}
</script>
</head>

<body onload="ftn_approval(document.fm);">
<p align=center class=title><u>New All@Pay�� ���ο�û ����������</u></p>

    <!------------- HTML : Form ���� --------------//-->
    <form name="fm"  method=POST action="payres.jsp"> <!--���ο�û �� ������������� ���� //-->

    <table border=0 cellpadding=0 cellspacing=1 bgcolor="#606060" width=1152 align=center style="TABLE-LAYOUT: fixed;">
    <font color=red>�� �ʼ����� : <b>���� �ʼ� �׸�</b></font>
    &nbsp;&nbsp;&nbsp;&nbsp<a target=_new href="http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp"><b>[FAQ]</b></a>
    <tr>
        <td width="140" class="head">�׸�</td>
        <td width="160" class="body">���� ��</td>
        <td width="70"  class="body">&nbsp�ִ����<br>(��������)</td>
        <td width="120"  class="body">������</td>
        <td class="body">���� ����</td>
    </tr>
    <tr>
        <td class="head">���� ID</td>
        <td class="body"><input type=text name="allat_shop_id" value="GM_social" size="19" maxlength=20></td>
        <td class="body">20</td>
        <td class="body">allat_shop_id</td>
        <td class="body">Allat���� �߱��� ���� ���� ID
          &nbsp;&nbsp<a target=_new href="http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#shop"><b>[����]</b></a></td>
    </tr>
    <tr>
        <td class="head">�ֹ���ȣ</td>
        <td class="body"><input type=text name="allat_order_no" value="<%=LGD_OID %>" size="19" maxlength=70></td>
        <td class="body">70</td>
        <td class="body">allat_order_no</td>
        <td class="body">���θ����� ����ϴ� ���� �ֹ���ȣ : ����,��������ǥ('),ū����ǥ(") ��� �Ұ�</td>
    </tr>
    <tr>
        <td class="head">���αݾ�</td>
        <td class="body"><input type=text name="allat_amt" value="<%=LGD_AMOUNT %>" size="19" maxlength=10></td>
        <td class="body">10</td>
        <td class="body">allat_amt</td>
        <td class="body">�� �����ݾ� : ����(0~9)�� ��밡��</td>
    </tr>
    <tr>
        <td class="head">ȸ��ID</td>
        <td class="body"><input type=text name="allat_pmember_id" value="<%=uvo.getUser_id() %>" size="19" maxlength=20></td>
        <td class="body">20</td>
        <td class="body">allat_pmember_id</td>
        <td class="body">���θ��� ȸ��ID : ����,��������ǥ('),ū����ǥ(") ��� �Ұ�</td>
    </tr>
    <tr>
        <td class="head">��ǰ�ڵ�</td>
        <td class="body"><input type=text name="allat_product_cd" value="munjarang" size="19" maxlength=1000></td>
        <td class="body">1000</td>
        <td class="body">allat_product_cd</td>
        <td class="body">���� ��ǰ�� ��� ������ �̿�, ������('||':������ 2��) : ����,��������ǥ('),ū����ǥ(") ��� �Ұ�</td>
    </tr>
    <tr>
        <td class="head">��ǰ��</td>
        <td class="body"><input type=text name="allat_product_nm" value="<%=LGD_PRODUCTINFO %>" size="19" maxlength=1000></td>
        <td class="body">1000</td>
        <td class="body">allat_product_nm</td>
        <td class="body">���� ��ǰ�� ��� ������ �̿�, ������('||':������ 2��)</td>
    </tr>
    <tr>
        <td class="head">�����ڼ���</td>
        <td class="body"><input type=text name="allat_buyer_nm" value="<%=LGD_BUYER %>" size="19" maxlength=20></td>
        <td class="body">20</td>
        <td class="body">allat_buyer_nm</td>        
        <td class="body"></td>
    </tr>
    <tr>
        <td class="head">�����μ���</td>
        <td class="body"><input type=text name="allat_recp_nm" value="<%=LGD_BUYER %>" size="19" maxlength=20></td>
        <td class="body">20</td>
        <td class="body">allat_recp_nm</td>        
        <td class="body"></td>
    </tr>
    <tr>
        <td class="head">�������ּ�</td>
        <td class="body"><input type=text name="allat_recp_addr" value="����" size="19" maxlength=120></td>
        <td class="body">120</td>
        <td class="body">allat_recp_addr</td>        
        <td class="body"></td>
    </tr>
    <tr>
        <td class="head">�ֹ�������ȣȭ�ʵ�</td>
        <td class="body"><font color=red>���� �ڵ����� ������</font></td>
        <td class="body">-</td>
        <td class="body">allat_enc_data</td>
        <td class="body"><font color=red>&ltinput type=hidden name=allat_enc_data value=''&gt<br>
                          ��hidden field�� �����ؾ���</font>
                          &nbsp;&nbsp<a target=_new href="http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#enc_data"><b>[����]</b></a></td>
        <input type=hidden name=allat_enc_data value=''>
    </tr>
    </table>
    <br>
    <table border=0 cellpadding=0 cellspacing=1 bgcolor="#606060" width=1152 align=center style="TABLE-LAYOUT: fixed;" >
    <font color=blue><b>�� �ɼ�����</b>( ���̳� �ʵ尡 ���� ��� ���� �Ӽ��̳� Default���� �ݿ��� )</font>
    <tr>
        <td width=140 class="head">�ſ�ī�� ����<br>��� ����</td>
        <td width=160 class="body"><input type=text name="allat_card_yn" value="<%=(req_smethod.equals("card"))?"Y":"" %>" size="19" maxlength=1></td>
        <td width=70  class="body">1</td>
        <td width=120 class="body">allat_card_yn</td>        
        <td class="body">���(Y),������� ����(N) - Default : �����Ӽ�</td>
    </tr>
    <tr>
        <td class="head">������ü ����<br>��� ����</td>
        <td class="body"><input type=text name="allat_bank_yn" value="<%=(req_smethod.equals("������ü"))?"Y":"" %>" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_bank_yn</td>                
        <td class="body">���(Y),������� ����(N) - Default : �����Ӽ�</td>
    </tr>
    <tr>
        <td class="head">������(�������) ����<br>��� ����</td>
        <td class="body"><input type=text name="allat_vbank_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_vbank_yn</td>                        
        <td class="body">���(Y),������� ����(N) - Default : �����Ӽ�</td>
    </tr>
    <tr>
        <td class="head">�޴��� ����<br>��� ����</td>
        <td class="body"><input type=text name="allat_hp_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_hp_yn</td>                        
        <td class="body">���(Y),������� ����(N) - Default : �����Ӽ�</td>
    </tr>
    <tr>
        <td class="head">��ǰ�� ����<br>��� ����</td>
        <td class="body"><input type=text name="allat_ticket_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_ticket_yn</td>                        
        <td class="body">���(Y),������� ����(N) - Default : �����Ӽ�</td>
    </tr>
    <tr>
        <td class="head">������(�������)<br>���� Key</td>
        <td class="body"><input type=text name="allat_account_key" value="" size="19" maxlength=20></td>
        <td class="body">20</td>
        <td class="body">allat_account_key</td>                        
        <td class="body">���� ä������� Key�� ����� ���� �����<br>
            <font color=blue>�Ǻ� ä������϶� ����</font></td>
    </tr>
    <tr>
        <td class="head">��������</td>
        <td class="body"><input type=text name="allat_tax_yn" value="Y" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_tax_yn</td>                        
        <td class="body">Y(����), N(�����) - Default : Y</td>
    </tr>
    <tr>
        <td class="head">�Һ� ��뿩��</td>
        <td class="body"><input type=text name="allat_sell_yn" value="Y" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_sell_yn</td>                        
        <td class="body">�Һλ��(Y), �Һ� ������(N) - Default : Y</td>
    </tr>
    <tr>
        <td class="head">�Ϲ�/������ �Һ�<br>��뿩��</td>
        <td class="body"><input type=text name="allat_zerofee_yn" value="Y" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_zerofee_yn</td>                        
        <td class="body">�Ϲ�(N), ������ �Һ�(Y) - Default :N
          &nbsp;&nbsp<a target=_new href="http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#zerofee"><b>[����]</b></a></td>
    </tr>
    <tr>
        <td class="head">ī�� ���� ����</td>
        <td class="body"><input type=text name="allat_cardcert_yn" value="N" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_cardcert_yn</td>                        
        <td class="body">����(Y), ���� ������(N), ������ ���(X) - Default : N</td>
    </tr>
    <tr>
        <td class="head">����Ʈ ��� ����</td>
        <td class="body"><input type=text name="allat_bonus_yn" value="N" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_bonus_yn</td>                        
        <td class="body">���(Y), ��� ����(N) - Default : N
          &nbsp;&nbsp<a target=_new href="http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#point"><b>[����]</b></a></td>
    </tr>
    <tr>
        <td class="head">���� ������ �߱� ����</td>
        <td class="body"><input type=text name="allat_cash_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_cash_yn</td>                        
        <td class="body">���(Y), ��� ����(N) - Default : �����Ӽ�
          &nbsp;&nbsp<a target=_new href="http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#cash"><b>[����]</b></a></td>
    </tr>
    <tr>
        <td class="head">��ǰ�̹��� URL</td>
        <td class="body"><input type=text name="allat_product_img" value="http://" size="19" maxlength=256></td>
        <td class="body">256</td>
        <td class="body">all_product_img</td>                        
        <td class="body">PlugIn�� ������ ��ǰ�̹��� Full URL</td>
    </tr>
    <tr>
        <td class="head">���� ���� ���� E-mail</td>
        <td class="body"><input type=text name="allat_email_addr" value="" size="19" maxlength=50></td>
        <td class="body">50</td>
        <td class="body">allat_email_addr</td>                        
        <td class="body"><font color=red>����ũ�� ���� ���ÿ� �ʼ� �ʵ�.(����â���� E-Mail�ּҸ� ���� ���� ����)</font></td>
    </tr>
    <tr>
        <td class="head">�׽�Ʈ ����</td>
        <td class="body"><input type=text name="allat_test_yn" value="N" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_test_yn</td>                        
        <td class="body">�׽�Ʈ(Y),����(N) - Default : N
          &nbsp;&nbsp<a target=_new href="http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#test"><b>[����]</b></a></td>
    </tr>
    <tr>
        <td class="head">��ǰ �ǹ� ����</td>
        <td class="body"><input type=text name="allat_real_yn" value="N" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_real_yn</td>                        
        <td class="body">��ǰ�� �ǹ��� ��� (Y), ��ǰ�� �ǹ��� �ƴҰ�� (N) - Default : N<br>
            <font color=blue>��ǰ�� �ǹ��̰�, 10���� �̻� ������ü�� ����ũ�� ���뿩�� �̿�</font>
              &nbsp;&nbsp<a target=_new href="http://www.allatpay.com/servlet/AllatBiz/support/sp_install_guide_scriptapi.jsp#escrow"><b>[����]</b></a></td>
    </tr>
    <tr>
        <td class="head">ī�� ����ũ��<br>���뿩��</td>
        <td class="body"><input type=text name="allat_cardes_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_cardes_yn</td>                        
        <td class="body">ī�� ������ ���� ����ũ�� ���뿩�� : ���� (Y), ������ (N), ������ : ������ - Default : ������<br>
            <font color=blue>����ũ�� ���� ��� �����ǿ� ���ؼ��� �����</font></td>
    </tr>
    <tr>
        <td class="head">������ü ����ũ��<br>���뿩��</td>
        <td class="body"><input type=text name="allat_bankes_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_bankes_yn</td>                        
        <td class="body">������ü ������ ���� ����ũ�� ���뿩�� : ���� (Y), ������ (N), ������ : ���� - Default : ����<br>
            <font color=blue>����ũ�� ���� ��� �����ǿ� ���ؼ��� �����</font></td>
    </tr>
    <tr>
        <td class="head">������(�������) ����<br>ũ�� ���뿩��</td>
        <td class="body"><input type=text name="allat_vbankes_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_vbankes_yn</td>                        
        <td class="body">������� ������ ���� ����ũ�� ���뿩�� : ���� (Y), ������ (N), ������ : ���� - Default : ����<br>
            <font color=blue>����ũ�� ���� ��� �����ǿ� ���ؼ��� �����</font></td>
    </tr>
    <tr>
        <td class="head">�޴��� ����ũ��<br>���뿩��</td>
        <td class="body"><input type=text name="allat_hpes_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_hpes_yn</td>                        
        <td class="body">�޴��� ������ ���� ����ũ�� ���뿩�� : ���� (Y), ������ (N), ������ : ���� - Default : ����<br>
            <font color=blue>����ũ�� ���� ��� �����ǿ� ���ؼ��� �����</font></td>
    </tr>
    <tr>
        <td class="head">��ǰ�� ����ũ��<br>���뿩��</td>
        <td class="body"><input type=text name="allat_ticketes_yn" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_ticketes_yn</td>                        
        <td class="body">��ǰ�� ������ ���� ����ũ�� ���뿩�� : ���� (Y), ������ (N), ������ : ���� - Default : ����<br>
            <font color=blue>����ũ�� ���� ��� �����ǿ� ���ؼ��� �����</font></td>
    </tr>
    <tr>
        <td class="head">�ֹι�ȣ</td>
        <td class="body"><input type=text name="allat_registry_no" value="" size="19" maxlength=13></td>
        <td class="body">1</td>
        <td class="body">allat_registry_no</td>                        
        <td class="body">
        <font color=blue> ISP     - �ֹι�ȣ 13�ڸ�(ISP�϶��� Ư�� ����ڸ� �����.��κ� ������� ����)</font></td>
    </tr>
	<tr>
        <td class="head">����</td>
        <td class="body"><input type=text name="allat_gender" value="" size="19" maxlength=1></td>
        <td class="body">1</td>
        <td class="body">allat_gender</td>
        <td class="body">������ ����, ����(M)/����(F)</td>
    </tr>
    <tr>
        <td class="head">�������</td>
        <td class="body"><input type=text name="allat_birth_ymd" value="" size="19" maxlength=8></td>
        <td class="body">8</td>
        <td class="body">allat_birth_ymd</td>                                
        <td class="body">�������� ������� 8��, YYYYMMDD����</td>
    </tr>
    </table>
    <p align=center>
    <table border=0 cellpadding=0 cellspacing=1 width=1152 align=center>
    <tr><td align=center>
    <input type=button value="  ��  ��  " name=app_btn onClick="javascript:ftn_approval(document.fm);">
    </td></tr>
    </table>
    </p>
    </form>
</body>
<!---- ���� ��ġ ���� Layer�� ������� �����÷���, �Ʒ� ��ũ��Ʈ�� ���� �� �ֽñ� �ٶ��ϴ� ---->
<script language=Javascript>initCheckOB();</script>
</html>
<%
}catch(Exception e) {
	out.println(SLibrary.alertScript(e.getMessage(),"/*"+e.toString()+"*/"));
}
%>