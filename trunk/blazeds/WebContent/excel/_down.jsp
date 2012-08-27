<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.net.URLDecoder"%>
<%@ page import="java.io.*" %>
<%
 try{

  //아래 getServletContext().getRealPath("/") 는 서버의 절대 경로를 받아온다
  //String path = getServletContext().getRealPath("/");
  
  String path = getServletContext().getRealPath("/");  // 스트링을 euc-kr 인코딩방식의 bytes로 불러와서 ISO-8895-1 인코딩으로 바꿈!
  // ISO-8895-1 인코딩은 대부분의 브라우저에 설정된 기본 문자셋 이라고 함
  // 그니깐 euc-kr로 가져온 바이트를 브라우저 설정 문자셋으로 바꾼것 
  String fileName = "excel/munja119.xls";//new String(request.getParameter("filename").getBytes("euc-kr"), "ISO-8859-1");
  
  File file = new File(path+fileName);
  
  // page의 contentType 등등을 동적으로 바꾸기 위해서는 response를 다 날려버린다.
  // 띄어쓰기 등도 모두 초기화 된다.  
  response.reset();
  
  response.setContentType("application/octer-stream");
  // 일반파라미터 application/x-www-form-urlencoded 디폴트로 설정 되있다!
  // 파일은 다르기 때문에 response 값을 바꾼것임
  
  // 해더의 이름에 내가 원하는 전송할 파일 이름을 넣은 것임
  // 파일이름은 브라우저 설정 문자셋으로 바뀐것으로 넣는다! 
  response.setHeader("Content-Disposition", "attachment;filename="+fileName+"");
  // 인코딩 설정값도 변경
  response.setHeader("Content-Transper-Encoding", "binary");
  // 사이즈도 알려줘야 한다

  response.setContentLength((int)file.length());
  // cache에 안 넣을 거다! 
  response.setHeader("Pargma", "no-cache");
  // Expires 는 또 무슨 뜻인가염 넹?!
  response.setHeader("Expires", "-1");
  
  byte[] data = new byte[1024 * 1024];
  BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
  // response가 사용하는 OutputStream()을 out 스트림으로 사용 
  BufferedOutputStream fos = new BufferedOutputStream(response.getOutputStream());
  
  int count = 0;
  while((count = fis.read(data)) != -1){
   fos.write(data);
  }
  
  if(fis != null) fis.close();
  if(fos != null) fos.close();
  
 }catch(Exception e){
  System.out.println("download error : " + e);
 }
 // jsp 에는 이미 내장객체로 out이 사용되고 있기 때문에 
 // outputstream을 사용하려면 비워 줘야 한다고 함! out.clear();
 // pageContext.pushBody() 이건 또 뭐란 말인가?
 out.clear();
 out = pageContext.pushBody();
%>
