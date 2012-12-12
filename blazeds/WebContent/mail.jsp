<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="javax.mail.Message"%>
<%@page import="javax.mail.Transport"%>
<%@page import="javax.mail.internet.InternetAddress"%>
<%@page import="javax.mail.internet.MimeMessage"%>
<%@page import="javax.mail.Session"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.*"%>
<%@ page import="java.security.*"%>
<%@ page import="javax.crypto.*"%>
<%@ page import="javax.crypto.spec.DESKeySpec"%>
<%@ page import="javax.crypto.spec.DESedeKeySpec"%>

<%!
  String MAIL_HOST = "imagemaker.co.kr";

  public void sendMail(String name, String fromm, String too, String title, String content) throws Exception
    {
		try{

		Properties props = new Properties();
		props.put("mail.smtp.host", "imagemaker.co.kr");
		props.put("mail.smtp.user", "newplus_plusnew");


		Session msgSession = Session.getDefaultInstance(props, null);

		MimeMessage msg = new MimeMessage(msgSession);
		InternetAddress from = new InternetAddress(fromm);
		msg.setFrom(from);

		InternetAddress to = new InternetAddress(too, name);
		InternetAddress[] toList = { to };
		msg.setRecipients(Message.RecipientType.TO, toList);

		msg.setSubject(title, "KSC5601");
		msg.setContent(content, "text/html; charset=EUC-KR");
		Transport.send(msg); 

		} catch (Exception ex){
			throw ex;
		}
    }

%>

<%	
		
		String fromName = "문자야";
		String fromEmail = "webmaster@munjaya.co.kr";
		String toName = "";
		String toEmail = "starwarssi@nate.com";
		String title = "[문자야] 회원가입을 축하 드립니다.";
		String content = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'><html xmlns='http://www.w3.org/1999/xhtml'><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /><title>문자야</title><style type='text/css'><!--.style1 {	color: #FF0000;	font-weight: bold;}--></style></head><body><table width='600' border='0' cellspacing='0' cellpadding='0'>  <tr>    <td><img src='http://www.munjaya.com/images/email_member1.jpg'></td>  </tr>  <tr>   <td height='80' background='http://www.munjaya.com/images/email_bg.jpg' align='center'><table width='250' border='0' cellspacing='0' cellpadding='0'>      <tr>        <td height='25'><strong>아이디</strong></td>        <td><span class='style1'>mjcoco</span></td>      </tr>      <tr>        <td height='25'><strong>패스워드</strong></td>        <td><span class='style1'>mj****</span></td>      </tr>    </table></td>  </tr>  <tr>    <td><img src='http://www.munjaya.com/images/email_member2.jpg'/></td>  </tr></table></body></html>";
		
    	try {
		

/*
				String fromName = arrMsg[0];
				String fromEmail = arrMsg[1];
				String toEmail = arrMsg[2];
				String title = arrMsg[3];
				String content = arrMsg[4];
*/
				out.println("------------ sendmail.jsp 메일 전송 Start--------------");
				out.println(fromName+"<br>");
				out.println(fromEmail+"<br>");
				out.println(toEmail+"<br>");
				out.println(title+"<br>");
				out.println(content+"<br>");

				sendMail(fromName, fromEmail, toEmail,title,content);
			
    	} catch (Exception e) {

    		out.println(e);
    	}
    	out.println("------------ sendmail.jsp 메일 전송 End --------------");
%>