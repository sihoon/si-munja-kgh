package com.m.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.common.VbyP;
import com.common.util.SLibrary;
import com.m.member.UserInformationVO;
import com.m.mobile.LogVO;
import com.m.mobile.SMS;
import com.m.mobile.SMSClientVO;

import flex.messaging.FlexContext;

public class AdminSMS {
	
	static AdminSMS asms = new AdminSMS();
	private String returnPhone = "0119";
	
	public static AdminSMS getInstance() {
		return asms;
	}
	private AdminSMS(){};
	
	public BooleanAndDescriptionVO sendAdmin(Connection conn , String message) {
		
		Connection connSMS = null;
		SMS sms = SMS.getInstance();
		String user_id = null;
		UserInformationVO mvo = null;
		ArrayList<String[]> phoneAndNameArrayList = null;
		LogVO lvo = null;
		int sendCount = 0;
		ArrayList<SMSClientVO> alClientVO = null;
		int logKey = 0;
		String requestIp = null;
		
		int year = 0;
		int month = 0;
		String reservationDate = "";
		
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		try {
			
			/*###############################
			#		validity check			#
			###############################*/
			
			
			if ( !VbyP.getValue("alimSMS").equals("Y") )
				throw new Exception("관리자 알림 N");
			
			user_id = VbyP.getValue("adminId");
			requestIp = "";//FlexContext.getHttpRequest().getRemoteAddr();
			
			year = SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") );
			month = SLibrary.parseInt( SLibrary.getDateTimeString("MM") );
			reservationDate = SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss");
				
			if (year == 0 || month == 0)
				throw new Exception("Admin - 해당 년월을 가져 오지 못했습니다.");
			
			if (conn == null)
				throw new Exception("Admin - DB연결에 실패 하였습니다.");
			
			mvo = new UserInformationVO();
			mvo.setLine("kt");
			mvo.setUser_id(user_id);
			
			connSMS = VbyP.getDB(mvo.getLine());
								
			if (connSMS == null)
				throw new Exception("SMS DB연결에 실패 하였습니다.");
			
			/*###############################
			#		Process					#
			###############################*/
			VbyP.accessLog("Admin - 전송 요청 : " + requestIp );
			
			
			returnPhone = SLibrary.replaceAll(returnPhone, "-", "");
			phoneAndNameArrayList = this.getAdminPhones();
			sendCount = phoneAndNameArrayList.size();
			//step1
			lvo = sms.getLogVO( mvo, false, message, phoneAndNameArrayList, returnPhone, reservationDate, requestIp);
			logKey = sms.insertSMSLog(conn, lvo, year, month);
			if ( logKey == 0 )
				throw new Exception("전송내역 로그가 삽입 되지 않았습니다.");
			VbyP.accessLog( "Admin - 전송 요청 : 로그 삽입 성공 ("+logKey+")"+ "경과 시간 : ");
			
			//step2
			alClientVO = sms.getSMSClientVO(conn, mvo, false, logKey, message, phoneAndNameArrayList, returnPhone, reservationDate, requestIp);
			VbyP.accessLog(user_id+"Admin - 전송 요청 : getSMSClientVO 생성" + "경과 시간 : ");
			
			int clientResult = sms.insertSMSClient(connSMS, alClientVO,mvo.getLine());
			VbyP.accessLog(user_id+"Admin - 전송 요청 : 전송테이블 삽입 성공" + "경과 시간 : ");
			
			if ( clientResult != sendCount)
				throw new Exception("Admin - 전송테이블 입력 : "+ Integer.toString(clientResult)+" 발송데이터 : "+ Integer.toString( alClientVO.size() ) );
			else{
				rvo.setbResult(true);
				rvo.setstrDescription(Integer.toString(clientResult)+","+year+","+month+","+logKey+","+mvo.getLine());
			}
				
				
		}catch (Exception e) {
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
		}
		finally {
			
			try {
				//if ( conn != null ) conn.close();
				if ( connSMS != null ) connSMS.close();
			}catch(SQLException e) {
				VbyP.errorLog("sendSMS >> conn.close() or connSMS.close() Exception!"); 
			}
		}
		
		VbyP.accessLog(user_id+"Admin - 전송 요청 완료 : "+rvo.getstrDescription());
		return rvo;
	}
	
	public BooleanAndDescriptionVO sendAdmin(Connection conn , String message, String phone) {
		
		Connection connSMS = null;
		SMS sms = SMS.getInstance();
		String user_id = null;
		UserInformationVO mvo = null;
		ArrayList<String[]> phoneAndNameArrayList = null;
		LogVO lvo = null;
		int sendCount = 0;
		ArrayList<SMSClientVO> alClientVO = null;
		int logKey = 0;
		String requestIp = null;
		
		int year = 0;
		int month = 0;
		String reservationDate = "";
		
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		try {
			
			/*###############################
			#		validity check			#
			###############################*/
			
			
			if ( !VbyP.getValue("alimSMS").equals("Y") )
				throw new Exception("관리자 알림 N");
			
			user_id = VbyP.getValue("adminId");
			requestIp = FlexContext.getHttpRequest().getRemoteAddr();
			
			year = SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") );
			month = SLibrary.parseInt( SLibrary.getDateTimeString("MM") );
			reservationDate = SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss");
				
			if (year == 0 || month == 0)
				throw new Exception("Admin - 해당 년월을 가져 오지 못했습니다.");
			
			if (conn == null)
				throw new Exception("Admin - DB연결에 실패 하였습니다.");
			
			mvo = new UserInformationVO();
			mvo.setLine("sms1");
			mvo.setUser_id(user_id);
			
			connSMS = VbyP.getDB(mvo.getLine());
								
			if (connSMS == null)
				throw new Exception("SMS DB연결에 실패 하였습니다.");
			
			/*###############################
			#		Process					#
			###############################*/
			VbyP.accessLog("Admin - 전송 요청 : " + requestIp+" "+phone );
			
			
			returnPhone = SLibrary.replaceAll(returnPhone, "-", "");
			
			
			phoneAndNameArrayList = new ArrayList<String[]>();
			String [] temp = new String[2];
			temp[0] = phone;
			phoneAndNameArrayList.add(temp);

			sendCount = phoneAndNameArrayList.size();
			//step1
			lvo = sms.getLogVO( mvo, false, message, phoneAndNameArrayList, returnPhone, reservationDate, requestIp);
			logKey = sms.insertSMSLog(conn, lvo, year, month);
			if ( logKey == 0 )
				throw new Exception("전송내역 로그가 삽입 되지 않았습니다.");
			VbyP.accessLog( "Admin - 전송 요청 : 로그 삽입 성공 ("+logKey+")"+ "경과 시간 : ");
			
			//step2
			alClientVO = sms.getSMSClientVO(conn, mvo, false, logKey, message, phoneAndNameArrayList, returnPhone, reservationDate, requestIp);
			VbyP.accessLog(user_id+"Admin - 전송 요청 : getSMSClientVO 생성" + "경과 시간 : ");
			
			int clientResult = sms.insertSMSClient(connSMS, alClientVO,mvo.getLine());
			VbyP.accessLog(user_id+"Admin - 전송 요청 : 전송테이블 삽입 성공" + "경과 시간 : ");
			
			if ( clientResult != sendCount)
				throw new Exception("Admin - 전송테이블 입력 : "+ Integer.toString(clientResult)+" 발송데이터 : "+ Integer.toString( alClientVO.size() ) );
			else{
				rvo.setbResult(true);
				rvo.setstrDescription(Integer.toString(clientResult)+","+year+","+month+","+logKey+","+mvo.getLine());
			}
				
				
		}catch (Exception e) {
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
		}
		finally {
			
			try {
				//if ( conn != null ) conn.close();
				if ( connSMS != null ) connSMS.close();
			}catch(SQLException e) {
				VbyP.errorLog("sendSMS >> conn.close() or connSMS.close() Exception!"); 
			}
		}
		
		VbyP.accessLog(user_id+"Admin - 전송 요청 완료 : "+rvo.getstrDescription());
		return rvo;
	}
	
	public BooleanAndDescriptionVO sendAdmin(Connection conn , String message, String phone, String returnPhoneNumber) {
		
		Connection connSMS = null;
		SMS sms = SMS.getInstance();
		String user_id = null;
		UserInformationVO mvo = null;
		ArrayList<String[]> phoneAndNameArrayList = null;
		LogVO lvo = null;
		int sendCount = 0;
		ArrayList<SMSClientVO> alClientVO = null;
		int logKey = 0;
		String requestIp = null;
		
		int year = 0;
		int month = 0;
		String reservationDate = "";
		
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		try {
			
			/*###############################
			#		validity check			#
			###############################*/
			
			
			if ( !VbyP.getValue("alimSMS").equals("Y") )
				throw new Exception("관리자 알림 N");
			
			user_id = VbyP.getValue("adminId");
			requestIp = "";//FlexContext.getHttpRequest().getRemoteAddr();
			
			year = SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") );
			month = SLibrary.parseInt( SLibrary.getDateTimeString("MM") );
			reservationDate = SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss");
				
			if (year == 0 || month == 0)
				throw new Exception("Admin - 해당 년월을 가져 오지 못했습니다.");
			
			if (conn == null)
				throw new Exception("Admin - DB연결에 실패 하였습니다.");
			
			mvo = new UserInformationVO();
			mvo.setLine("sms1");
			mvo.setUser_id(user_id);
			
			connSMS = VbyP.getDB(mvo.getLine());
								
			if (connSMS == null)
				throw new Exception("SMS DB연결에 실패 하였습니다.");
			
			/*###############################
			#		Process					#
			###############################*/
			VbyP.accessLog("Admin - 전송 요청 : " + requestIp+" "+phone );
			
			
			returnPhoneNumber = SLibrary.replaceAll(returnPhoneNumber, "-", "");
			
			
			phoneAndNameArrayList = new ArrayList<String[]>();
			String [] temp = new String[2];
			temp[0] = phone;
			phoneAndNameArrayList.add(temp);

			sendCount = phoneAndNameArrayList.size();
			//step1
			lvo = sms.getLogVO( mvo, false, message, phoneAndNameArrayList, returnPhoneNumber, reservationDate, requestIp);
			logKey = sms.insertSMSLog(conn, lvo, year, month);
			if ( logKey == 0 )
				throw new Exception("전송내역 로그가 삽입 되지 않았습니다.");
			VbyP.accessLog( "Admin - 전송 요청 : 로그 삽입 성공 ("+logKey+")"+ "경과 시간 : ");
			
			//step2
			alClientVO = sms.getSMSClientVO(conn, mvo, false, logKey, message, phoneAndNameArrayList, returnPhoneNumber, reservationDate, requestIp);
			VbyP.accessLog(user_id+"Admin - 전송 요청 : getSMSClientVO 생성" + "경과 시간 : ");
			
			int clientResult = sms.insertSMSClient(connSMS, alClientVO,mvo.getLine());
			VbyP.accessLog(user_id+"Admin - 전송 요청 : 전송테이블 삽입 성공" + "경과 시간 : ");
			
			if ( clientResult != sendCount)
				throw new Exception("Admin - 전송테이블 입력 : "+ Integer.toString(clientResult)+" 발송데이터 : "+ Integer.toString( alClientVO.size() ) );
			else{
				rvo.setbResult(true);
				rvo.setstrDescription(Integer.toString(clientResult)+","+year+","+month+","+logKey+","+mvo.getLine());
			}
				
				
		}catch (Exception e) {
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
		}
		finally {
			
			try {
				//if ( conn != null ) conn.close();
				if ( connSMS != null ) connSMS.close();
			}catch(SQLException e) {
				VbyP.errorLog("sendSMS >> conn.close() or connSMS.close() Exception!"); 
			}
		}
		
		VbyP.accessLog(user_id+"Admin - 전송 요청 완료 : "+rvo.getstrDescription());
		return rvo;
	}
	
	public BooleanAndDescriptionVO sendAdmin(String message) {
		
		return null;
	}
	
	private ArrayList<String[]> getAdminPhones() {
		
		ArrayList<String[]> rslt = new ArrayList<String[]>();
		String strPhones = VbyP.getValue("adminPhones");
		
		String [] arrPhone = strPhones.split("\\,");
		
		String[] temp = null;
		for (int i = 0; i < arrPhone.length; i++) {
			
			if (arrPhone[i] != null) {
				temp = new String[2];
				temp[0] = arrPhone[i];
				rslt.add(temp);
			}
		}
		return rslt;
	}
	
	
}
