package com.m.mobile;

import java.sql.Connection;
import java.util.ArrayList;

import com.m.member.UserInformationVO;

public interface SMSAble {
	
	static final String TRAN_TYPE_CODE = "03";
	static final String RESERVATION_CODE = "Y";
	static final String SITE_CODE = "munja119.com";
	static final String CLIENT_SENDSTAT = "0";
	static final String CLIENT_RSLTSTAT = "00";
	static final String CLIENT_MESSAGETYPE = "0";
	static final String APPLICATION_TYPE = "M";
	
	int insertSMSClient(Connection conn, SMSClientVO vo);
	int insertSMSLog(Connection conn, LogVO vo, int year, int month);
	
	int insertSMSClient(Connection conn, ArrayList<SMSClientVO> al, String via);
	int insertSMSClientRefuse(Connection connSMS, ArrayList<SMSClientVO> al, String via);
	int sendPointPut(Connection conn, UserInformationVO mvo, int cnt);
	
	LogVO getLogVO( UserInformationVO mvo, Boolean bReservation, String message, ArrayList<String[]> phoneAndNameArrayList, String returnPhone, String reservationDate, String ip) throws Exception;
	ArrayList<String[]> getPhone(Connection conn, String user_id, ArrayList<PhoneListVO> al);
}
