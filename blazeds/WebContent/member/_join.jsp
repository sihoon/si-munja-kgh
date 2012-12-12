<%@page import="com.m.common.PointManager"%>
<%@page import="com.m.member.JoinVO"%>
<%@page import="com.m.member.Join"%>
<%@page import="com.common.util.SLibrary"%>
<%@ page import="com.common.VbyP" %>
<%@ page import="java.sql.Connection" %>
<%@page import="javax.mail.Message"%>
<%@page import="javax.mail.Transport"%>
<%@page import="javax.mail.internet.InternetAddress"%>
<%@page import="javax.mail.internet.MimeMessage"%>
<%@page import="javax.mail.Session"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%!
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

		msg.setSubject(title,"KSC5601");
		msg.setContent(content, "text/html; charset=EUC-KR");
		Transport.send(msg); 

		} catch (Exception ex){
			throw ex;
		}
    }

%><%
	
	if(!SLibrary.IfNull( (String)session.getAttribute("munja119JoinStep") ).equals("step2@Session")) {
		session.removeAttribute("munja119JoinStep");
		out.println(SLibrary.alertScript("잘못된 접근 입니다.", ""));
		return;
	}
	
	String user_id = VbyP.getPOST(request.getParameter("join_id"));
	String id_ok = VbyP.getPOST(request.getParameter("id_ok"));
	String passwd1 = VbyP.getPOST(request.getParameter("passwd1"));
	String passwd2 = VbyP.getPOST(request.getParameter("passwd2"));
	String user_name = VbyP.getPOST(request.getParameter("name"));
// 	String jumin1 = VbyP.getPOST(request.getParameter("jumin1"));
// 	String jumin2 = VbyP.getPOST(request.getParameter("jumin2"));

	String jumin1 = "######";
	String jumin2 = "#######";
	
	String hp = VbyP.getPOST(request.getParameter("hp"));
	String email = VbyP.getPOST(request.getParameter("email"));
	
	VbyP.accessLog("회원가입 페이지 요청 완료>> " + request.getRemoteAddr() +" "+user_id );

	try {
		Join join = new Join();
		
		if ( SLibrary.isNull(user_name)) throw new Exception("이름이 없습니다.");
// 		if ( SLibrary.isNull(jumin1) || SLibrary.isNull(jumin2)) throw new Exception("주민등록번호가 없습니다.");
		if ( SLibrary.isNull(user_id)) throw new Exception("아이디를 입력하세요.");
		if (join.idDupleCheck(user_id)) throw new Exception("가입된 아이디 입니다.");
		if (!user_id.equals(id_ok)) throw new Exception("아이디를 다시 입력하세요.");
		if (passwd1.length() < 6) throw new Exception("비밀번호는 6자리 이상 입력하세요.");
		if (!passwd1.equals(passwd2)) throw new Exception("비밀번호 확인이 다릅니다.");
		if ( SLibrary.isNull(email) ) throw new Exception("이메일 주소가 없습니다.");
		
		
		JoinVO vo = new JoinVO();
		vo.setUser_id(user_id);
		vo.setPassword(passwd2);
		vo.setName(user_name);
		vo.setJumin(jumin1+jumin2);
		vo.setHp(hp);
		vo.setEmail(email);
		int rslt = join.insert(vo);
		PointManager.getInstance().initPoint( user_id, 0);
		
		if (rslt < 1) {
			out.println(SLibrary.alertScript("회원가입에 실패 하였습니다.", ""));
		}else {
			out.println(SLibrary.alertScript("회원가입이 완료 되었습니다.\\r\\n\\r\\n로그인 후 사용하시기 바랍니다.", "parent.window.location.href='../';"));
		}
		
	} catch (Exception e) {
		VbyP.errorLog("member/_join.jsp ==> " + e.toString());
		out.println(SLibrary.alertScript(e.getMessage(), ""));
		System.out.println(e.toString());
	} finally {}

		

		
    	try {
			String tmp = passwd2.substring(0, 2);
		int cnt = passwd2.length()-2;
		for (int i = 0; i < cnt; i++) tmp += "*";

		String fromName = "문자야";
		String fromEmail = "webmaster@munjaya.co.kr";
		String toName = user_name;
		String toEmail = email;
		String title = "[문자야] 회원가입을 축하 드립니다.";
		String content = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'><html xmlns='http://www.w3.org/1999/xhtml'><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /><title>문자야</title><style type='text/css'><!--.style1 {	color: #FF0000;	font-weight: bold;}--></style></head><body><table width='600' border='0' cellspacing='0' cellpadding='0'>  <tr>    <td><img src='http://www.munjaya.com/images/email_member1.jpg'></td>  </tr>  <tr>   <td height='80' background='http://www.munjaya.com/images/email_bg.jpg' align='center'><table width='250' border='0' cellspacing='0' cellpadding='0'>      <tr>        <td height='25'><strong>아이디</strong></td>        <td><span class='style1'>"+user_id+"</span></td>      </tr>      <tr>        <td height='25'><strong>패스워드</strong></td>        <td><span class='style1'>"+tmp+"</span></td>      </tr>    </table></td>  </tr>  <tr>    <td><img src='http://www.munjaya.com/images/email_member2.jpg'/></td>  </tr></table></body></html>";
		
				System.out.println("------------ join mail Start--------------");
				System.out.println(fromName+"<br>");
				System.out.println(fromEmail+"<br>");
				System.out.println(toEmail+"<br>");
				System.out.println(title+"<br>");
				System.out.println(content+"<br>");

				sendMail(fromName, fromEmail, toEmail,title,content);
			
    	} catch (Exception e) {

    		System.out.println(e);
    	}
    	System.out.println("------------ join mail End --------------");
	
%>
