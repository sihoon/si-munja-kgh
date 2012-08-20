package com.m.sent;

import java.util.List;
import java.sql.Connection;

import com.m.common.BooleanAndDescriptionVO;
import com.m.member.UserInformationVO;

public interface SentFactoryAble {

	List<SentGroupVO> getSentGroupList(Connection conn, String user_id, String fromDate, String endDate, boolean bReservation);
	List<SentVO> getSentList(Connection connSMS, String user_id, String line, String sentGroupIndex);
	
	SentStatisticVO getSentStatistic(Connection connSMS, String userId, String sentClientName, String sentGroupIndex);
	
	BooleanAndDescriptionVO deleteSentGroupList(Connection conn, Connection connSMS, String user_id, int idx, String line, UserInformationVO mvo );
	BooleanAndDescriptionVO deleteSentGroupList(Connection conn, String user_id);
	BooleanAndDescriptionVO cancelSentGroupList(Connection conn, Connection connSMS, UserInformationVO mvo, int idx, String sendLine) throws Exception ;
	
}
