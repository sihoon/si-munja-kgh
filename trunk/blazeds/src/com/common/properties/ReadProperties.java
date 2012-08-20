/** 
* <B>readProperties Class</B>는 시스템 프로퍼티를 읽어 옵니다. </br>
* @author si hoon 
* @version 1.0 2008.10
*/

package com.common.properties;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.regex.Pattern;

public class ReadProperties implements ReadPropertiesAble
{
		static ReadPropertiesAble rp = null;
		
		public static ReadPropertiesAble getInstance(){
			
			if (rp == null) 
				rp = (ReadPropertiesAble)new ReadProperties();
			return rp;
		}
		
		
		/**
		 * common.properties 파일이 없을 경우 생성한다.
		 */
		public void createProperties(String fileName) {
			
			String path = propertiesPath(HeadDir);
			
			try {
				new BufferedWriter(new FileWriter(path+fileName));
				initProperties(fileName);//기본설정
			}catch(Exception e){				
				System.out.println("설정파일 생성에 실패 하였습니다. ->"+e.toString());
			}
		}
		
		/**
		 * common.properties에 기본속성들을 설정
		 */
		public void initProperties(String fileName) {
			
			if ( fileName != null && fileName.equals(COMMON_PROPERTIES)) {
				setProperties("service" , "on");//웹서비스 사용여부			
				setProperties("host" , "");//호스트 URL			
				setProperties("debugLog" , "Y");//디버그 로그 사용
				setProperties("debugLogPath" , "");			
				setProperties("errorLog" , "Y");//에러 로그 사용
				setProperties("errorLogPath" , "");			
				setProperties("getParameterDecode" , "");//GET방식 인코딩			
				setProperties("postParameterDecode" , "");//POST방식 인코딩			
				setProperties("fileDecode" , "");//파일 다운로드 인코딩			
				setProperties("dbInsertDecode" , "");//DB Insert 인코딩			
				setProperties("dbSelectDecode" , "");//DB Select 인코딩			
				setProperties("was" , "tomcat");//WAS 종류
				setProperties("JNDI" , "java:/comp/env");//JNDI resource
				setProperties("DataSource" , "jdbc/Oracle");//DataSource 명
				setProperties("max_size_excel_upload_MB","10");//엑셀로더 업로드 파일 크기
				setProperties("excel_content_type", "application/vnd.ms-excel|application/octet-stream");//엑셀 파일 형식
				setProperties("excel_upload","");//엑셀업로드 경로
				
				setProperties("mmsimage_upload_path","");//MMS이미지 업로드 경로
				setProperties("mmsimage_content_type","");//MMS이미지 파일 타입
				setProperties("max_size_mmsimage_upload_MB","");//MMS최대 용량
			}
			
		}
		
		/**
		 * 스레드에서 설정파일의 경로 습득
		 * @param file
		 * @return
		 * @throws NullPointerException
		 */
		public String getPath() throws NullPointerException	{
			
			String path = "";
			ClassLoader cl;
			//WEB-INF/classes 위치를 찾는다.
			cl = Thread.currentThread().getContextClassLoader();
			if (cl == null)	cl = ClassLoader.getSystemClassLoader();			
			
			URL url = cl.getResource(HeadDir);
			
			try {
				
				String OS = System.getProperty("os.name");
				if (Pattern.matches("Windows.*", OS)) {//윈도우용
					
					String windowPath = url.getPath();			
					path = windowPath.substring(1,windowPath.length());	
					
				}
				else {
					
					path = url.getPath();				
				}
			}catch(NullPointerException e) {
				System.out.println(HeadDir+"가 없습니다."+e.toString());
				throw e;
			}
			
			return path;
		}
		
		/**
		 * 스레드에서 설정파일의 경로 습득
		 * @param file
		 * @return
		 * @throws NullPointerException
		 */
		public String propertiesPath( String file ) throws NullPointerException	{
			
			String path = "";
			ClassLoader cl;
			//WEB-INF/classes 위치를 찾는다.
			cl = Thread.currentThread().getContextClassLoader();
			if (cl == null)	cl = ClassLoader.getSystemClassLoader();			
			
			URL url = cl.getResource(file);
			
			try {
				
				String OS = System.getProperty("os.name");
				if (Pattern.matches("Windows.*", OS)) {//윈도우용
					
					String windowPath = url.getPath();			
					path = windowPath.substring(1,windowPath.length());	
					
				}
				else {
					
					path = url.getPath();				
				}
			}catch(NullPointerException e) {
				System.out.println(file+"가 없습니다."+e.toString());
				createProperties(file);
				throw e;
			}
			
			return path;
		}
		
		/**  
		* common.properties key 의 값을 반환
		* @param key - 설정명
		* @return value - 설정값
		*/
		public String getValue( String key ) throws IOException , NullPointerException{

			Properties p = new Properties();
			String src = "";
			
			src = propertiesPath(HeadDir+COMMON_PROPERTIES);
			
			try
			{
				FileInputStream fis = new FileInputStream(src);
				BufferedInputStream bis = new BufferedInputStream(fis);
				p.load(bis);
				bis.close();
			}
			catch (IOException e)
			{
				System.out.println("설정파일 로딩 에러!!("+src+")=>"+e.toString());
				throw e;
			}
			
			String rslt = p.getProperty(key);			

			return rslt;
		}
		
		/**  
		* sql.properties 파일의 key 의 값을 반환
		* @param key - 설정명
		* @return value - 설정값
		*/
		public String getSQL( String key ) {

			Properties p = new Properties();
			String src = "";
			
			src = propertiesPath(HeadDir+SQL_PROPERTIES);
			
			try
			{
				FileInputStream fis = new FileInputStream(src);
				BufferedInputStream bis = new BufferedInputStream(fis);
				p.load(bis);
				bis.close();
			}
			catch (IOException e)
			{
				System.out.println("설정파일 로딩 에러!!("+src+")=>"+e.toString());
			}
			
			String rslt = p.getProperty(key);
						
			return rslt;
		}			
		
		/**
		 * common.properties 파일의 설정을 변경한다.
		 * @param key
		 * @param value
		 */
		public void setProperties(String key , String value) {
			
			try {
				Properties pro = new Properties();
				String src = propertiesPath(HeadDir+COMMON_PROPERTIES);
				FileInputStream fis = new FileInputStream(src);
				
				pro.load(new BufferedInputStream(fis));
				pro.setProperty(key, value);
				File file = new File(src);
				pro.store(new FileOutputStream(file) , "");
				
			}catch(Exception e){
				System.out.println(e.toString());
				
			}
		}
		
		/**
		 * 특정설정 파일의 값을 반환한다.
		 * @param fileName
		 * @param key
		 * @return
		 */
		public String getPropertiesFileValue(String fileName, String key) {
			
			Properties p = new Properties();
			String src = "";
			
			src = propertiesPath(HeadDir+fileName);
			
			try
			{
				FileInputStream fis = new FileInputStream(src);
				BufferedInputStream bis = new BufferedInputStream(fis);
				p.load(bis);
				bis.close();
			}
			catch (IOException e)
			{
				System.out.println("설정파일 로딩 에러!!("+src+")=>"+e.toString());
			}
			
			String rslt = p.getProperty(key);
						
			return rslt;
		}
		
		public Hashtable<String, String> getKeys(String fileName) {
			
			Hashtable<String, String> hashTable = new Hashtable<String, String>();
			
			Properties p = new Properties();
			String src = "";
			
			src = propertiesPath(HeadDir+fileName);
			
			try
			{
				FileInputStream fis = new FileInputStream(src);
				BufferedInputStream bis = new BufferedInputStream(fis);
				p.load(bis);
				bis.close();
			}
			catch (IOException e)
			{
				System.out.println("설정파일 로딩 에러!!("+src+")=>"+e.toString());
			}
			
			Enumeration<?> en = p.propertyNames();
			
			while(en.hasMoreElements()) {
				
				hashTable.put((String)en.nextElement(), "");
			}
						
			return hashTable;
		}
		
		public boolean isKey(String fileName, String key) {
			
			Hashtable<String, String> hashTable = getKeys(fileName);
			
			if (hashTable.containsKey(key))
				return true;
			else
				return false;
		}
		
		
};