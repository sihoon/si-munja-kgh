/*
 *@(#)VbyP.java 1.0, 2009. 11. 5.
 *
 *Copyright(c) 2009 ehancast All rights reserved
 */
package com.common;

import java.sql.Connection;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.common.db.ConnectionFactory;
import com.common.log.Log;
import com.common.properties.*;
//import com.common.util.SLibrary;


public class VbyP {
	
	public static String[] getDecodeFromProperties() {
		
		String [] arrEncode = null;
		try {
			String rslt = ReadProperties.getInstance().getValue("propertiesDecode");
			arrEncode = rslt.split("\\>");
			
		}catch(Exception e){}
		
		
		return arrEncode;
	}
	/**
	 * sql.properties 파일의 key 의 값을 반환
	 * @param key
	 * @return
	 */
	public static String getSQL(String key) {
		
		String [] arrEncode = getDecodeFromProperties();
		
		String rslt = ReadProperties.getInstance().getSQL(key);
		if (arrEncode.length == 2) {
			
			try { rslt = new String(rslt.getBytes(arrEncode[0]), arrEncode[1]);}
			catch(Exception e) {}
		}
		return rslt;
	}
	
	/**
	 * common.properties 파일의 key 의 값을 반환
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		
		String [] arrEncode = getDecodeFromProperties();
		
		String rslt = "";
		if (arrEncode.length == 2) {
			
			try { 
				rslt = ReadProperties.getInstance().getValue(key).trim();
				rslt = new String(rslt.getBytes(arrEncode[0]), arrEncode[1]);}
			catch(Exception e) {}
		}
		return rslt;
	}
	
	/**
	 * common.properties 파일의 key , value 를 설정
	 * @param key
	 * @param value
	 */
	public static void setProperties(String key, String value) {
		
		String [] arrEncode = getDecodeFromProperties();
		
		String rslt = "";
		if (arrEncode.length == 2) {
			
			try { 
				rslt = new String(value.getBytes(arrEncode[1]), arrEncode[0]);
				ReadProperties.getInstance().setProperties(key, rslt);
			}
			catch(Exception e) {}
		}
		
	}
	
	/**
	 * HTTP GET 방식의 값을 인코딩
	 * @param GET
	 * @return
	 */
	public static String getGET(String GET) {
		
		String [] arrEncode = getValue("postParameterDecode").split("\\>");
		
		String rslt = GET;
		if (arrEncode.length == 2) {
			
			try { rslt = new String(GET.getBytes(arrEncode[0]), arrEncode[1]);}
			catch(Exception e) {}
		}
		return rslt;
	}
	
	/**
	 * HTTP POST 방식의 값을 인코딩
	 * @param POST
	 * @return
	 */
	public static String getPOST(String POST) {
		
		String [] arrEncode = getValue("postParameterDecode").split("\\>");
		
		String rslt = POST;
		if (arrEncode.length == 2) {
			
			try { rslt = new String(POST.getBytes(arrEncode[0]), arrEncode[1]);}
			catch(Exception e) {}
		}
		return rslt;
	}
	
	/**
	 * FILE 다운로드시 파일명 인코딩
	 * @param FILE
	 * @return
	 */
	public static String getFILE(String FILE) {
		String [] arrEncode = getValue("fileDecode").split("\\>");
		
		String rslt = "";
		if (arrEncode.length == 2) {
			
			try { rslt = new String(FILE.getBytes(arrEncode[0]), arrEncode[1]);}
			catch(Exception e) {}
		}
		return rslt;
	}
	
	/**
	 * 디버그용 로그를 기록 한다.
	 * @param log
	 */
	public static void debugLog( String log ) {
			
			String [] arrEncode =  getValue("logDecode").split("\\>");
			try {
				ReadPropertiesAble rp = ReadProperties.getInstance();
				if (rp.getValue("debugLog").equals("Y")) {
					
					String path = rp.getValue("debugLogPath");
					if (path == null || path.equals(""))
						path = rp.getPath();
					log = (arrEncode.length == 2)?new String(log.getBytes(arrEncode[0]), arrEncode[1]):log;
					System.out.println(Log.getInstance().logDate()+" : "+log);
					Log.getInstance().println(path+"degug.log", log );
				}
	
			}catch(Exception e){}
		
	}
	
	/**
	 * 접속 로그를 기록 한다.
	 * @param log
	 */
	public static void accessLog( String log ) {				
		
		String [] arrEncode =  getValue("logDecode").split("\\>");
		try {			
			ReadPropertiesAble rp = ReadProperties.getInstance();
			if (rp.getValue("accessLog").equals("Y")) {
								
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat();
				sdf.applyPattern("yyyy-MM-dd");
				String path = rp.getValue("accessLogPath");
				if (path == null || path.equals(""))
					path = rp.getPath();
				log = (arrEncode.length == 2)?new String(log.getBytes(arrEncode[0]), arrEncode[1]):log;
				System.out.println(Log.getInstance().logDate()+" : "+log);
				Log.getInstance().println(path+"access_"+sdf.format(cal.getTime()).toString()+".log", log );
			}

		}catch(Exception e){}
	}
	
	/**
	 * 에러용 로그를 기록 한다.
	 * @param log
	 */
	public static void errorLog( String log ) {
		
		
		String [] arrEncode =  getValue("logDecode").split("\\>");
		try {
			
			ReadPropertiesAble rp = ReadProperties.getInstance();
			
			if (rp.getValue("errorLog").equals("Y")) {
								
				String path = rp.getValue("errorLogPath");
				if (path == null || path.equals(""))
					path = rp.getPath();
				log = (arrEncode.length == 2)?new String(log.getBytes(arrEncode[0]), arrEncode[1]):log;
				System.out.println(Log.getInstance().logDate()+" : "+log);
				Log.getInstance().println(path+"error.log", log  );
			}

		}catch(Exception e){}
	}
	
	/**
	 * 에러용 로그를 기록 한다.
	 * @param log
	 */
	public static void errorLogDaily( String log ) {
		
		String [] arrEncode = getValue("logDecode").split("\\>");
		try {
						
			ReadPropertiesAble rp = ReadProperties.getInstance();
			if (rp.getValue("errorLog").equals("Y")) {
								
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat();
				sdf.applyPattern("yyyy-MM-dd");
				
				String path = rp.getValue("errorLogPath");
				if (path == null || path.equals(""))
					path = rp.getPath();
				log = (arrEncode.length == 2)?new String(log.getBytes(arrEncode[0]), arrEncode[1]):log;
				System.out.println(Log.getInstance().logDate()+" : "+log);
				Log.getInstance().println(path+"error_"+sdf.format(cal.getTime()).toString()+".log", log );
			}

		}catch(Exception e){}
	}
	
	/**
	 * DB Connection 객체를 반환한다.
	 * @return
	 */
	public static Connection getDB() {
		
		ReadPropertiesAble rp = ReadProperties.getInstance();
		Connection conn = null;
		
		try {
			
			if (rp.getValue("was").equals("weblogic")){
				
				try {conn = ConnectionFactory.getInstance().getConnectionWeblogic(rp.getValue("DataSource"));
				}catch(Exception e){errorLog("weblogic DB 연결에러 :"+e.toString());}
				
			}else if (rp.getValue("was").equals("tomcat")){
				
				try {
					conn = ConnectionFactory.getInstance().getConnectionTomcat(rp.getValue("JNDI"), rp.getValue("web"));					
					conn.setAutoCommit(true);
					
				}catch(Exception e){errorLog("tomcat DB 연결에러 :"+e.toString());}
			}
			
		}catch(Exception e){errorLog("설정파일 연결 에러 :"+e.toString());}
		
		return conn;
	}
	
	/**
	 * DB Connection 객체를 반환한다.
	 * @return
	 */
	public static Connection getDB(String key) {
		
		ReadPropertiesAble rp = ReadProperties.getInstance();
		Connection conn = null;
		
		try {
			
			if (rp.getValue("was").equals("weblogic")){
				
				try {conn = ConnectionFactory.getInstance().getConnectionWeblogic(rp.getValue(key));
				}catch(Exception e){errorLog("weblogic DB 연결에러 :"+e.toString());}
				
			}else if (rp.getValue("was").equals("tomcat")){
				
				try {
					conn = ConnectionFactory.getInstance().getConnectionTomcat(rp.getValue("JNDI"), rp.getValue(key));					
					conn.setAutoCommit(true);
					
				}catch(Exception e){errorLog("tomcat DB 연결에러 :"+e.toString());}
			}
			
		}catch(Exception e){errorLog("설정파일 연결 에러 :"+e.toString());}
		
		return conn;
	}
	
	/**
	 * 문자열에 Object배열을 Format하여 반환한다.
	 * */
	public static String messageFormat(String pattern , Object[] obj) {
		
		return MessageFormat.format(pattern, obj);
	}

}
