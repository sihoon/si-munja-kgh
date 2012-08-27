<%@ page  language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR" %>
<%@ page import="java.io.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="lgdacom.XPayClient.XPayClient"%>
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
    String LGD_PRODUCTINFO      = "����119 ����";              //��ǰ��
    String LGD_BUYEREMAIL       = "";               //������ �̸���
    String LGD_TIMESTAMP        = SLibrary.getDateTimeString("yyyyMMddHHmmss");                //Ÿ�ӽ�����
    String LGD_CUSTOM_SKIN      = "blue";                                                //�������� ����â ��Ų(red, blue, cyan, green, yellow)
    String req_smethod = SLibrary.IfNull(VbyP.getPOST(request.getParameter("smethod")));
    String LGD_CUSTOM_FIRSTPAY  = "";

    System.out.println(req_smethod);
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
<title>������ eCredit���� �����׽�Ʈ</title>
<script type="text/javascript">
/*
 * �������� ������û�� PAYKEY�� �޾Ƽ� �������� ��û.
 */
function doPay_ActiveX(){
    ret = xpay_check(document.getElementById('LGD_PAYINFO'), '<%= CST_PLATFORM %>');

    if (ret=="00"){     //ActiveX �ε� ����
        var LGD_RESPCODE        = dpop.getData('LGD_RESPCODE');       //����ڵ�
        var LGD_RESPMSG         = dpop.getData('LGD_RESPMSG');        //����޼���

        if( "0000" == LGD_RESPCODE ) { //��������
            var LGD_PAYKEY      = dpop.getData('LGD_PAYKEY');         //LG������ ����KEY
            var msg = "������� : " + LGD_RESPMSG + "\n";
            msg += "LGD_PAYKEY : " + LGD_PAYKEY +"\n\n";
            document.getElementById('LGD_PAYKEY').value = LGD_PAYKEY;
            alert(msg);
            document.getElementById('LGD_PAYINFO').submit();
        } else { //��������
            alert("������ �����Ͽ����ϴ�. " + LGD_RESPMSG);
            /*
             * �������� ȭ�� ó��
             */
        }
    } else {
        alert("LG U+ ���ڰ����� ���� ActiveX Control��  ��ġ���� �ʾҽ��ϴ�.");
        /*
         * �������� ȭ�� ó��
         */
    }      
}

function isActiveXOK(){
	if(lgdacom_atx_flag == true){
    	document.getElementById('LGD_BUTTON1').style.display='none';
        document.getElementById('LGD_BUTTON2').style.display='';
        doPay_ActiveX();
	}else{
		document.getElementById('LGD_BUTTON1').style.display='';
        document.getElementById('LGD_BUTTON2').style.display='none';	
	}
}
</script>
</head>

<body onload="isActiveXOK();">
<div id="LGD_ACTIVEX_DIV"/> <!-- ActiveX ��ġ �ȳ� Layer �Դϴ�. �������� ������. -->
<form method="post" id="LGD_PAYINFO" action="payres.jsp">
<table>
    <tr>
        <td>������ �̸� </td>
        <td><%= LGD_BUYER %></td>
    </tr>
    <tr>
        <td>��ǰ���� </td>
        <td><%= LGD_PRODUCTINFO %></td>
    </tr>
    <tr>
        <td>�����ݾ� </td>
        <td><%= LGD_AMOUNT %></td>
    </tr>
    <tr>
        <td>������ �̸��� </td>
        <td><%= LGD_BUYEREMAIL %></td>
    </tr>
    <tr>
        <td>�ֹ���ȣ </td>
        <td><%= LGD_OID %></td>
    </tr>
    <tr>
        <td colspan="2">* �߰� �� ������û �Ķ���ʹ� �޴����� �����Ͻñ� �ٶ��ϴ�.</td>
    </tr>
    <tr>
        <td colspan="2"></td>
    </tr>    
    <tr>
        <td colspan="2">
		<div id="LGD_BUTTON1">������ ���� ����� �ٿ� ���̰ų�, ����� ��ġ���� �ʾҽ��ϴ�. </div>
		<div id="LGD_BUTTON2" style="display:none"><input type="button" value="������û" onclick="doPay_ActiveX();"/> </div>        
        </td>
    </tr>    
</table>
<br>

<input type="hidden" name="CST_PLATFORM"                value="<%= CST_PLATFORM %>">                   <!-- �׽�Ʈ, ���� ���� -->
<input type="hidden" name="CST_MID"                     value="<%= CST_MID %>">                        <!-- �������̵� -->
<input type="hidden" name="LGD_MID"                     value="<%= LGD_MID %>">                        <!-- �������̵� -->
<input type="hidden" name="LGD_OID"                     value="<%= LGD_OID %>">                        <!-- �ֹ���ȣ -->
<input type="hidden" name="LGD_BUYER"                   value="<%= LGD_BUYER %>">                      <!-- ������ -->
<input type="hidden" name="LGD_PRODUCTINFO"             value="<%= LGD_PRODUCTINFO %>">                <!-- ��ǰ���� -->
<input type="hidden" name="LGD_AMOUNT"                  value="<%= LGD_AMOUNT %>">                     <!-- �����ݾ� -->
<input type="hidden" name="LGD_BUYEREMAIL"              value="<%= LGD_BUYEREMAIL %>">                 <!-- ������ �̸��� -->
<input type="hidden" name="LGD_CUSTOM_SKIN"             value="<%= LGD_CUSTOM_SKIN %>">                <!-- ����â SKIN -->
<input type="hidden" name="LGD_CUSTOM_PROCESSTYPE"      value="<%= LGD_CUSTOM_PROCESSTYPE %>">         <!-- Ʈ����� ó����� -->
<input type="hidden" name="LGD_TIMESTAMP"               value="<%= LGD_TIMESTAMP %>">                  <!-- Ÿ�ӽ����� -->
<input type="hidden" name="LGD_HASHDATA"                value="<%= LGD_HASHDATA %>">                   <!-- MD5 �ؽ���ȣ�� -->
<input type="hidden" name="LGD_CUSTOM_FIRSTPAY"			value="<%= LGD_CUSTOM_FIRSTPAY %>">
<input type="hidden" name="LGD_CUSTOM_USABLEPAY"			value="<%= LGD_CUSTOM_FIRSTPAY %>">
<input type="hidden" name="LGD_PAYKEY"                  id="LGD_PAYKEY">   							   <!-- LG������ PAYKEY(������ �ڵ�����)-->
<input type="hidden" name="LGD_VERSION"         		value="JSP_XPay_1.0">


<!-- �������(������) ���������� �Ͻô� ���  �Ҵ�/�Ա� ����� �뺸�ޱ� ���� �ݵ�� LGD_CASNOTEURL ������ LG �����޿� �����ؾ� �մϴ� . -->
<input type="hidden" name="LGD_CASNOTEURL"          	value="<%= LGD_CASNOTEURL %>" >                 <!-- ������� NOTEURL -->

</form>
</body>
<!--  xpay.js�� �ݵ�� body �ؿ� �νñ� �ٶ��ϴ�. -->
<!--  UTF-8 ���ڵ� ��� �ô� xpay.js ��� xpay_utf-8.js ��  ȣ���Ͻñ� �ٶ��ϴ�.-->
<script language="javascript" src="<%=request.getScheme()%>://xpay.lgdacom.net<%="test".equals(CST_PLATFORM)?(request.getScheme().equals("https")?":7443":":7080"):""%>/xpay/js/xpay.js" type="text/javascript">
</script>
</html>
<%
}catch(Exception e) {
	out.println(SLibrary.alertScript(e.getMessage(),"/*"+e.toString()+"*/"));
}
%>