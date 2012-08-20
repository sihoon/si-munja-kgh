package com.m.mobile;

import java.sql.Connection;
import java.util.ArrayList;

import com.m.member.UserInformationVO;

public interface MMSAble {
	
	static final String TRAN_TYPE_CODE = "03";
	static final String RESERVATION_CODE = "res";
	static final String SITE_CODE = "smspia.com";
	static final String CLIENT_SENDSTAT = "0";
	static final String CLIENT_RSLTSTAT = "00";
	static final String CLIENT_MESSAGETYPE = "0";
	static final String APPLICATION_TYPE = "M";
	static final int LMS_POINT_COUNT = 3;
	static final int MMS_POINT_COUNT = 15;
	
	int insertClient(Connection conn, MMSClientVO vo);
	int insertLMSLog(Connection conn, LogVO vo, int year, int month);
	int insertMMSLog(Connection conn, LogVO vo, int year, int month);
	
	int insertClient(Connection conn, ArrayList<MMSClientVO> al, String via);
	int insertClientRefuse(Connection connMMS, ArrayList<MMSClientVO> al, String via);
	int sendLMSPointPut(Connection conn, UserInformationVO mvo, int cnt);
	int sendMMSPointPut(Connection conn, UserInformationVO mvo, int cnt);
	
	LogVO getLogVO( UserInformationVO mvo, Boolean bReservation, String message, ArrayList<String[]> phoneAndNameArrayList, String returnPhone, String reservationDate, String ip) throws Exception;
	LogVO getLogVOMearge( UserInformationVO mvo, Boolean bReservation, String message, String phone, String returnPhone, String reservationDate, int count, String ip) throws Exception;
	ArrayList<String[]> getPhone(Connection conn, String user_id, ArrayList<PhoneListVO> al);
}
