package com.m.sent;

import java.util.HashMap;
import java.util.List;
import java.sql.Connection;
import java.util.ArrayList;
import com.common.VbyP;
import com.common.db.PreparedExecuteQueryManager;
import com.common.util.SLibrary;
import com.m.common.BooleanAndDescriptionVO;
import com.m.common.PointManager;
import com.m.member.UserInformationVO;

public class SentFactory implements SentFactoryAble {
	
	private final int CANCEL_GAP = 60*5;
	
	static SentFactory sf = new SentFactory();
	
	public static SentFactory getInstance() {
		return sf;
	}
	private SentFactory(){};

	@Override
	public List<SentVO> getSentList(Connection connSMS, String userId, String line, String sentGroupIndex) {

		
		ArrayList<SentVO> rslt = new ArrayList<SentVO>();
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		if (SLibrary.IfNull(line).equals("sk")|| SLibrary.IfNull(line).equals("skmms")) {
			pq.setPrepared( connSMS, VbyP.getSQL("selectSentDataSK") );
		}else if (SLibrary.IfNull(line).equals("kt") || SLibrary.IfNull(line).equals("ktmms")) {
			pq.setPrepared( connSMS, VbyP.getSQL("selectSentDataKT") );
		}else if (SLibrary.IfNull(line).equals("han")) {
			pq.setPrepared( connSMS, VbyP.getSQL("selectSentDataHN") );
		}else if (SLibrary.IfNull(line).equals("hanr")) {
			pq.setPrepared( connSMS, VbyP.getSQL("selectSentDataHNR") );
		}else if (SLibrary.IfNull(line).equals("it")) {
			pq.setPrepared( connSMS, VbyP.getSQL("selectSentDataIT") );
		}else
			pq.setPrepared( connSMS, VbyP.getSQL("selectSentData") );
		
		pq.setString(1, userId);
		pq.setString(2, sentGroupIndex);
		pq.setString(3, userId);
		pq.setString(4, sentGroupIndex);
		
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		al = pq.ExecuteQueryArrayList();
		
		int count = al.size();
		if (count > 0) {
			
			SentVO vo = new SentVO();
			HashMap<String, String> h = null;
			String strRslt = "";
			
			try {
				
				for (int i = 0; i < count; i++) {
					
					vo = new SentVO();
					h = al.get(i);
					
					if ( getSendStat(line, SLibrary.IfNull(h, "TR_SENDSTAT")).equals("1"))
						strRslt = "전송중";
					else 
						strRslt = getSendResult(line, SLibrary.IfNull(h, "TR_RSLTSTAT"));
					
					vo.setAll(
							SLibrary.parseInt( SLibrary.IfNull(h, "TR_ETC6") ),
							line,
							SLibrary.IfNull(h, "TR_SENDDATE"),
							SLibrary.IfNull(h, "TR_PHONE"),
							SLibrary.IfNull(h, "TR_ETC1"),
							SLibrary.IfNull(h, "TR_CALLBACK"),
							SLibrary.IfNull(h, "TR_MSG"),
							SLibrary.isNull(strRslt)?"실패":strRslt,
							SLibrary.IfNull(h, "TR_RSLTDATE"),
							getSendStat(line, SLibrary.IfNull(h, "TR_SENDSTAT")),
							SLibrary.IfNull(h, "TR_RSLTSTAT")
							);
					rslt.add(vo);
				}
			}catch(Exception e){System.out.println("getSentList Error!");}
			
			h = null;
			al = null;
		}
		
		return rslt;

	}
	
	private String getSendResult(String line, String code) {

		String rslt = "";
		if (SLibrary.IfNull(line).equals("sk") || SLibrary.IfNull(line).equals("skmms"))
			rslt = VbyP.getValue( "sk_"+code);
		else if (SLibrary.IfNull(line).equals("kt"))
			rslt = VbyP.getValue( "kt_"+code);
		else
			rslt = VbyP.getValue( "dacom_"+code);
		return rslt;
	}
	private String getSendStat(String line, String stat) {
		
		String rslt = stat;
		if (SLibrary.IfNull(line).equals("sk") || SLibrary.IfNull(line).equals("skmms")) {
			if (stat.equals("1")||stat.equals("2"))
				rslt = "1";
			else if (stat.equals("9"))
				rslt = "2";
		}
		
		if (SLibrary.IfNull(line).equals("kt")) {
			if (stat.equals("1")||stat.equals("2"))
				rslt = "1";
			else if (stat.equals("3") || stat.equals("-1"))
				rslt = "2";
		}
		
		return rslt;
	}
	public SentStatisticVO getSentStatistic(Connection connSMS, String userId, String sentClientName,
			 String sentGroupIndex) {
		
		HashMap<String, String> hm = null;
		SentStatisticVO vo = new SentStatisticVO();
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( connSMS, VbyP.getSQL("selectSentStatistic"));
		pq.setString(1, userId);
		pq.setString(2, sentGroupIndex);
		pq.setString(3, userId);
		pq.setString(4, sentGroupIndex);
		hm = pq.ExecuteQueryCols();
		
		vo.setFailCount( SLibrary.parseInt( SLibrary.IfNull(hm, "failCount") ) );
		vo.setMessage( SLibrary.IfNull(hm, "message") );
		vo.setReturnPhone( SLibrary.IfNull(hm, "returnPhone") );
		vo.setSendDate( SLibrary.IfNull(hm, "sendDate") );
		vo.setSendingCount( SLibrary.parseInt( SLibrary.IfNull(hm, "sendingCount") ) );
		vo.setStandbyCount( SLibrary.parseInt( SLibrary.IfNull(hm, "standbyCount") ) );
		vo.setSuccessCount( SLibrary.parseInt( SLibrary.IfNull(hm, "successCount") ) );
		vo.setWrongCount( SLibrary.parseInt( SLibrary.IfNull(hm, "wrongCount") ) );
		vo.setTotalCount( SLibrary.parseInt( SLibrary.IfNull(hm, "totalCount") ) );
		
		//replace
		vo.setMessage(SLibrary.replaceAll(vo.getMessage(), "\r\n", "\r"));
		return vo;

	}
	
	@Override
	public List<SentGroupVO> getSentGroupList(Connection conn, String user_id, String fromDate, String endDate, boolean bReservation) {
		
		ArrayList<SentGroupVO> rslt = new ArrayList<SentGroupVO>();
		
		StringBuffer buf = new StringBuffer();

		buf.append( (!bReservation)?VbyP.getSQL("selectSentLog"):VbyP.getSQL("selectSentLogRes") );
				
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, buf.toString() );
		pq.setString(1, user_id);
		pq.setString(2, fromDate+" 00:00:00");
		pq.setString(3, endDate+" 23:59:59");
		
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		al = pq.ExecuteQueryArrayList();
		
		int count = al.size();
		if (count > 0) {
			
			SentGroupVO vo = new SentGroupVO();
			HashMap<String, String> h = null;
			
			try {
				
				for (int i = 0; i < count; i++) {
					
					vo = new SentGroupVO();
					h = al.get(i);
					vo.setIdx(SLibrary.intValue( SLibrary.IfNull(h, "idx") ));
					vo.setTimeSend(SLibrary.IfNull(h, "timeSend"));
					vo.setLine(SLibrary.IfNull(h, "line"));
					vo.setReservation(SLibrary.IfNull(h, "reservation"));
					vo.setReturnPhone(SLibrary.IfNull(h, "returnPhone"));
					vo.setCount(SLibrary.intValue( SLibrary.IfNull(h, "count") ));
					vo.setMessage(SLibrary.IfNull(h, "message"));
					vo.setTimeWrite(SLibrary.IfNull(h, "timeWrite"));
					vo.setTranType(SLibrary.IfNull(h, "tranType"));
					
					rslt.add(vo);
				}
			}catch(Exception e){System.out.println("getSentGroupList Error!");}
			
			h = null;
			al = null;
		}
		
		return rslt;
	}
	
	public BooleanAndDescriptionVO deleteSentGroupList(Connection conn, Connection connSMS, String user_id, int idx, String line, UserInformationVO mvo) {
		
		VbyP.debugLog(user_id + " >> 내역삭제 시작 "+Integer.toString(idx));
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		int tranResultCount = 0;
		if(SLibrary.IfNull(line).equals("sk")||SLibrary.IfNull(line).equals("skmms")){
			tranResultCount = deleteSentDataOfTranTableSK(connSMS, user_id, idx);
		}else if(SLibrary.IfNull(line).equals("kt")){
			tranResultCount = deleteSentDataOfTranTableKT(connSMS, user_id, idx);
		}else if(SLibrary.IfNull(line).equals("han")){
			tranResultCount = deleteSentDataOfTranTableHN(connSMS, user_id, idx);
		}else if(SLibrary.IfNull(line).equals("hanr")){
			tranResultCount = deleteSentDataOfTranTableHNR(connSMS, user_id, idx);
		}else if(SLibrary.IfNull(line).equals("it")){
			tranResultCount = deleteSentDataOfTranTableIT(connSMS, user_id, idx);
		}else {
			tranResultCount = deleteSentDataOfTranTable(connSMS, user_id, idx);
		}
		
		if (tranResultCount > 0) {
			if (SLibrary.IfNull(line).equals("skmms"))
				cancelPointPutLMS(conn, mvo, tranResultCount);
			else
				cancelPointPut(conn, mvo, tranResultCount);
			
			rvo.setstrDescription("내역중 미발송 내역 "+Integer.toString(tranResultCount)+"건이 취소 되었습니다.");
		}else {
			int updateResultCount = updateSentGroup(conn, user_id, idx, "logdel");
			VbyP.debugLog(user_id + " >> 내역삭제  전송그룹테이블 업데이트 : "+Integer.toString(updateResultCount) );			
					
			if ( updateResultCount == 1 ) {
				
				rvo.setbResult(true);
				VbyP.debugLog(user_id + " >> 내역삭제 성공  " );
				
			} else {
				
				rvo.setstrDescription("내역이 삭제 되지 않았습니다.");
			}
		}
		
		
		
		return rvo;
	}
	
	public BooleanAndDescriptionVO deleteSentGroupList(Connection conn, String user_id, int idx) {
		
		VbyP.debugLog(user_id + " >> 내역삭제 시작 "+Integer.toString(idx));
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		int updateResultCount = updateSentGroup(conn, user_id, idx, "logdel");
		VbyP.debugLog(user_id + " >> 내역삭제  전송그룹테이블 업데이트 : "+Integer.toString(updateResultCount) );			
				
		if ( updateResultCount == 1 ) {
			
			rvo.setbResult(true);
			VbyP.debugLog(user_id + " >> 내역삭제 성공  " );
			
		} else {
			
			rvo.setstrDescription("내역이 삭제 되지 않았습니다.");
		}
		
		return rvo;
	}
	
	public BooleanAndDescriptionVO deleteSentGroupList(Connection conn, String user_id) {
		
		VbyP.debugLog(user_id + " >> 내역삭제 시작 ");
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		
		
		int updateResultCount = updateSentGroup(conn, user_id, "logdel");
		VbyP.debugLog(user_id + " >> 내역삭제  전송그룹테이블 업데이트 : "+Integer.toString(updateResultCount) );			
				
		if ( updateResultCount == 1 ) {
			
			rvo.setbResult(true);
			VbyP.debugLog(user_id + " >> 내역삭제 성공  " );
			
		} else {
			
			rvo.setstrDescription("내역이 삭제 되지 않았습니다.");
		}
		
		return rvo;
	}
	
	public BooleanAndDescriptionVO cancelSentGroupList(Connection conn, Connection connSMS, UserInformationVO mvo, int idx, String sendLine) throws Exception {
		
		VbyP.debugLog(mvo.getUser_id() + " >> 예약취소 시작 "+Integer.toString(idx));
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		try {
			
			String[] sentGroupInfo =  selectTimeAndCountSentGroupData(conn, mvo.getUser_id(), idx);
			VbyP.debugLog(sentGroupInfo[0]);
			VbyP.debugLog("@@@"+SLibrary.getTime(sentGroupInfo[0], "yyyy-MM-dd HH:mm:ss")+" "+Long.toString((SLibrary.parseLong( SLibrary.getUnixtimeStringSecond() ) + CANCEL_GAP)*1000));
			if (sentGroupInfo.length == 2 
					&& SLibrary.getTime(sentGroupInfo[0], "yyyy-MM-dd HH:mm:ss") >= (SLibrary.parseLong( SLibrary.getUnixtimeStringSecond() ))*1000
					&& SLibrary.getTime(sentGroupInfo[0], "yyyy-MM-dd HH:mm:ss") < (SLibrary.parseLong( SLibrary.getUnixtimeStringSecond() ) + CANCEL_GAP)*1000
					) {
				throw new Exception( "발송 "+CANCEL_GAP/60+"분전 예약은 취소 할 수 없습니다." );
			}else if (sentGroupInfo.length == 2 
					&& SLibrary.getTime(sentGroupInfo[0], "yyyy-MM-dd HH:mm:ss") < (SLibrary.parseLong( SLibrary.getUnixtimeStringSecond() ))*1000
					){
				rvo = deleteSentGroupList(conn, mvo.getUser_id(), idx);
			} else {
				
				int tranResultCount = 0;
				if(SLibrary.IfNull(sendLine).equals("sk")||SLibrary.IfNull(sendLine).equals("skmms")){
					tranResultCount = deleteSentDataOfTranTableSK(connSMS, mvo.getUser_id(), idx);
				}else if(SLibrary.IfNull(sendLine).equals("kt")){
					tranResultCount = deleteSentDataOfTranTableKT(connSMS, mvo.getUser_id(), idx);
				}else if(SLibrary.IfNull(sendLine).equals("han")){
					tranResultCount = deleteSentDataOfTranTableHN(connSMS, mvo.getUser_id(), idx);
				}else if(SLibrary.IfNull(sendLine).equals("hanr")){
					tranResultCount = deleteSentDataOfTranTableHNR(connSMS, mvo.getUser_id(), idx);
				}else if(SLibrary.IfNull(sendLine).equals("it")){
					tranResultCount = deleteSentDataOfTranTableIT(connSMS, mvo.getUser_id(), idx);
				}else {
					tranResultCount = deleteSentDataOfTranTable(connSMS, mvo.getUser_id(), idx);
				}
				
				VbyP.debugLog(mvo.getUser_id() + " >> 예약취소  전송테이블 삭제 : "+Integer.toString(tranResultCount) );			
				//int reservationResultCount = deleteSentDataOfReservationTable(connSMS, mvo.getUser_id(), idx);
				//VbyP.debugLog(mvo.getUser_id() + " >> 예약취소  예약테이블 삭제 : "+Integer.toString(reservationResultCount) );	
				int failResultCount = 0;
				if (SLibrary.IfNull(sendLine).equals("sk")||SLibrary.IfNull(sendLine).equals("skmms")){
					failResultCount = selectSentDataOfLogTableSK(connSMS, mvo.getUser_id(), idx);
				}else if (SLibrary.IfNull(sendLine).equals("kt")){
					failResultCount = selectSentDataOfLogTableKT(connSMS, mvo.getUser_id(), idx);
				}else if (SLibrary.IfNull(sendLine).equals("han")){
					failResultCount = selectSentDataOfLogTableHN(connSMS, mvo.getUser_id(), idx);
				}else if (SLibrary.IfNull(sendLine).equals("hanr")){
					failResultCount = selectSentDataOfLogTableHNR(connSMS, mvo.getUser_id(), idx);
				}else if (SLibrary.IfNull(sendLine).equals("it")){
					failResultCount = selectSentDataOfLogTableIT(connSMS, mvo.getUser_id(), idx);
				}
				else {
					failResultCount = selectSentDataOfLogTable(connSMS, mvo.getUser_id(), idx);
				}
				VbyP.debugLog(mvo.getUser_id() + " >> 예약취소  로그테이블 건수(수신거부,중복등등) : "+Integer.toString(failResultCount) );	
				
				if ( sentGroupInfo.length == 2 && SLibrary.parseInt(sentGroupInfo[1]) != (tranResultCount +  failResultCount) ) 
					throw new Exception( "삭제된 발송 테이터와 예약 건수가 달라 데이터 삭제만 되었습니다.("+Integer.toString(tranResultCount +  failResultCount)+"/"+sentGroupInfo[1]+") 연락 주세요." ); 
				
				int updateResultCount = updateSentGroup(conn, mvo.getUser_id(), idx, "cancel");
				VbyP.debugLog(mvo.getUser_id() + " >> 예약취소  전송그룹테이블 업데이트 : "+Integer.toString(updateResultCount) );
				
				if ( updateResultCount != 1 )
					throw new Exception( "취소상태가 변경 되지 않았습니다." );
				
				int rsltcnt = 0;
				if (SLibrary.IfNull(sendLine).equals("skmms"))
					rsltcnt = cancelPointPutLMS(conn, mvo, tranResultCount +  failResultCount);
				else
					rsltcnt = cancelPointPut(conn, mvo, tranResultCount +  failResultCount);
				
				if ( rsltcnt == 1 ) {
						rvo.setbResult(true);
						VbyP.debugLog(mvo.getUser_id() + " >> 예약취소  건수 추가 : "+Integer.toString(tranResultCount ) );					
				} else {
					throw new Exception( "예약 취소건수가 추가 되지 않았습니다.");
				} 
				
			}
				
			
			
			
		}catch (Exception e) {
			
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			
		}	finally {}
		
		return rvo;
	}
	
	private String[] selectTimeAndCountSentGroupData(Connection conn, String user_id, int idx ) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, VbyP.getSQL("selectDeleteTransferLog") );
		pq.setString(1, user_id);
		pq.setString(2, Integer.toString(idx) );
		
		return pq.ExecuteQueryCols(2);
	}
	
	private int deleteSentDataOfTranTable(Connection conn, String user_id, int idx) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, VbyP.getSQL("deleteSentDataTranTable"));
		pq.setString(1, user_id);
		pq.setString(2, Integer.toString(idx) );
		
		return pq.executeUpdate();
	}
	
	private int deleteSentDataOfTranTableHN(Connection conn, String user_id, int idx) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, VbyP.getSQL("deleteSentDataTranTableHN"));
		pq.setString(1, user_id);
		pq.setString(2, Integer.toString(idx) );
		
		return pq.executeUpdate();
	}
	
	private int deleteSentDataOfTranTableHNR(Connection conn, String user_id, int idx) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, VbyP.getSQL("deleteSentDataTranTableHNR"));
		pq.setString(1, user_id);
		pq.setString(2, Integer.toString(idx) );
		
		return pq.executeUpdate();
	}
	
	private int deleteSentDataOfTranTableIT(Connection conn, String user_id, int idx) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, VbyP.getSQL("deleteSentDataTranTableIT"));
		pq.setString(1, user_id);
		pq.setString(2, Integer.toString(idx) );
		
		return pq.executeUpdate();
	}
	
	private int deleteSentDataOfTranTableSK(Connection conn, String user_id, int idx) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, VbyP.getSQL("deleteSentDataTranTableSK"));
		pq.setString(1, user_id);
		pq.setString(2, Integer.toString(idx) );
		
		return pq.executeUpdate();
	}
	
	private int deleteSentDataOfTranTableKT(Connection conn, String user_id, int idx) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, VbyP.getSQL("deleteSentDataTranTableKT"));
		pq.setString(1, user_id);
		pq.setString(2, Integer.toString(idx) );
		
		return pq.executeUpdate();
	}
	/*
	private int deleteSentDataOfReservationTable(Connection conn, String user_id, int idx) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, VbyP.getSQL("deleteSentDataReservationTable"));
		pq.setString(1, user_id);
		pq.setString(2, Integer.toString(idx) );
		return pq.executeUpdate();
	}
	*/
	private int selectSentDataOfLogTable(Connection conn, String user_id, int idx) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn,VbyP.getSQL("selectSentDataLogtable") );
		pq.setString(1, user_id);
		pq.setString(2, Integer.toString(idx) );
		return pq.ExecuteQueryNum();
	}
	
	private int selectSentDataOfLogTableHN(Connection conn, String user_id, int idx) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn,VbyP.getSQL("selectSentDataLogtableHN") );
		pq.setString(1, user_id);
		pq.setString(2, Integer.toString(idx) );
		return pq.ExecuteQueryNum();
	}
	
	private int selectSentDataOfLogTableHNR(Connection conn, String user_id, int idx) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn,VbyP.getSQL("selectSentDataLogtableHNR") );
		pq.setString(1, user_id);
		pq.setString(2, Integer.toString(idx) );
		return pq.ExecuteQueryNum();
	}
	
	private int selectSentDataOfLogTableIT(Connection conn, String user_id, int idx) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn,VbyP.getSQL("selectSentDataLogtableIT") );
		pq.setString(1, user_id);
		pq.setString(2, Integer.toString(idx) );
		return pq.ExecuteQueryNum();
	}
	
	private int selectSentDataOfLogTableSK(Connection conn, String user_id, int idx) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn,VbyP.getSQL("selectSentDataLogtableSK") );
		pq.setString(1, user_id);
		pq.setString(2, Integer.toString(idx) );
		return pq.ExecuteQueryNum();
	}
	private int selectSentDataOfLogTableKT(Connection conn, String user_id, int idx) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn,VbyP.getSQL("selectSentDataLogtableKT") );
		pq.setString(1, user_id);
		pq.setString(2, Integer.toString(idx) );
		return pq.ExecuteQueryNum();
	}
	
	private int updateSentGroup(Connection conn, String user_id, int idx, String type) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, VbyP.getSQL("updateSentGroup") );
		pq.setString(1, type);
		pq.setString(2, SLibrary.getDateTimeString());
		pq.setString(3, user_id);
		pq.setString(4, Integer.toString(idx) );
		
		return pq.executeUpdate();
	}
	
	private int updateSentGroup(Connection conn, String user_id, String type) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, VbyP.getSQL("updateMonthSentGroup") );
		pq.setString(1, type);
		pq.setInt(2, SLibrary.parseInt( SLibrary.getUnixtimeStringSecond() ));
		pq.setString(3, user_id);
		
		return pq.executeUpdate();
	}
	
	private int cancelPointPut(Connection conn, UserInformationVO mvo, int cnt) {
		
		PointManager pm = PointManager.getInstance();
		
		return pm.insertUserPoint(conn, mvo, 16, cnt * PointManager.DEFULT_POINT);
	}
	
	private int cancelPointPutLMS(Connection conn, UserInformationVO mvo, int cnt) {
		
		PointManager pm = PointManager.getInstance();
		
		return pm.insertUserPoint(conn, mvo, 46, cnt * PointManager.DEFULT_POINT*3);
	}
}
