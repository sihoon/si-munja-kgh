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

public class SentLMSFactory implements SentFactoryAble {
	
	private final int CANCEL_GAP = 60*5;
	
	static SentLMSFactory sf = new SentLMSFactory();
	
	public static SentLMSFactory getInstance() {
		return sf;
	}
	private SentLMSFactory(){};
	
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
	public List<SentVO> getSentList(Connection connSMS, String userId, String line, String sentGroupIndex) {

		
		ArrayList<SentVO> rslt = new ArrayList<SentVO>();
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		if (SLibrary.IfNull(line).equals("ktmms")) {
			pq.setPrepared( connSMS, VbyP.getSQL("selectLMSSentDataKTUser") );
		}else if (SLibrary.IfNull(line).equals("ppmms")) {
			pq.setPrepared( connSMS, VbyP.getSQL("selectLMSSentDataPP") );
		}else
			pq.setPrepared( connSMS, VbyP.getSQL("selectLMSSentData") );
		
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
				
				String status = "0";
				for (int i = 0; i < count; i++) {
					
					vo = new SentVO();
					h = al.get(i);
					
					if ( SLibrary.IfNull(h, "STATUS").equals("1") || SLibrary.IfNull(h, "STATUS").equals("2") )
						strRslt = "������";
					else {
						if (SLibrary.IfNull(line).equals("ktmms")) strRslt = VbyP.getValue( "mmskt_"+SLibrary.IfNull(h, "RSLT"));
						else strRslt = VbyP.getValue( "mms_"+SLibrary.IfNull(h, "RSLT"));
					}
					
					if (SLibrary.IfNull(h, "STATUS").equals("0")) status = "0";
					else if (SLibrary.IfNull(h, "STATUS").equals("1")) status = "1";
					else if (SLibrary.IfNull(h, "STATUS").equals("2")) status = "1";
					else if (SLibrary.IfNull(h, "STATUS").equals("3")) status = "2";
					else if (SLibrary.IfNull(h, "STATUS").equals("-1")) status = "2";
					else status = "0";
						
						
					vo.setAll(
							SLibrary.parseInt( SLibrary.IfNull(h, "POST") ),
							line,
							SLibrary.IfNull(h, "REQDATE"),
							SLibrary.IfNull(h, "PHONE"),
							SLibrary.IfNull(h, "ETC1"),
							SLibrary.IfNull(h, "CALLBACK"),
							SLibrary.IfNull(h, "MSG"),
							SLibrary.isNull(strRslt)?"����":strRslt,
							SLibrary.IfNull(h, "RSLTDATE"),
							status,
							SLibrary.IfNull(h, "RSLT")
							);
					rslt.add(vo);
				}
			}catch(Exception e){System.out.println("getSentList Error!");}
			
			h = null;
			al = null;
		}
		
		return rslt;

	}
	
	public ArrayList<SentVO> getSentListTemp(Connection connSMS, String userId, String line, String sentGroupIndex) {

		
		ArrayList<SentVO> rslt = new ArrayList<SentVO>();
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		if (SLibrary.IfNull(line).equals("ktmms")) {
			pq.setPrepared( connSMS, VbyP.getSQL("selectLMSSentDataKTUser") );
		}else
			pq.setPrepared( connSMS, VbyP.getSQL("selectLMSSentData") );
		
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
				
				String status = "0";
				for (int i = 0; i < count; i++) {
					
					vo = new SentVO();
					h = al.get(i);
					
					if ( SLibrary.IfNull(h, "STATUS").equals("1") || SLibrary.IfNull(h, "STATUS").equals("2") )
						strRslt = "������";
					else {
						if (SLibrary.IfNull(line).equals("ktmms")) strRslt = VbyP.getValue( "mmskt_"+SLibrary.IfNull(h, "RSLT"));
						else strRslt = VbyP.getValue( "mms_"+SLibrary.IfNull(h, "RSLT"));
					}
					
					if (SLibrary.IfNull(h, "STATUS").equals("0")) status = "0";
					else if (SLibrary.IfNull(h, "STATUS").equals("1")) status = "1";
					else if (SLibrary.IfNull(h, "STATUS").equals("2")) status = "1";
					else if (SLibrary.IfNull(h, "STATUS").equals("3")) status = "2";
					else status = "0";
						
						
					vo.setAll(
							SLibrary.parseInt( SLibrary.IfNull(h, "POST") ),
							line,
							SLibrary.IfNull(h, "REQDATE"),
							SLibrary.IfNull(h, "PHONE"),
							SLibrary.IfNull(h, "ETC1"),
							SLibrary.IfNull(h, "CALLBACK"),
							SLibrary.IfNull(h, "MSG"),
							SLibrary.isNull(strRslt)?"����":strRslt,
							SLibrary.IfNull(h, "RSLTDATE"),
							status,
							SLibrary.IfNull(h, "RSLT")
							);
					rslt.add(vo);
				}
			}catch(Exception e){System.out.println("getSentList Error!");}
			
			h = null;
			al = null;
		}
		
		return rslt;

	}
	
	public List<SentVO> getSentListAdd(ArrayList<SentVO> argrslt, Connection connSMS, String userId, String line, String sentGroupIndex) {

		
		ArrayList<SentVO> rslt = argrslt;
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		if (SLibrary.IfNull(line).equals("ktmms")) {
			pq.setPrepared( connSMS, VbyP.getSQL("selectLMSSentDataKTUser") );
		}else
			pq.setPrepared( connSMS, VbyP.getSQL("selectLMSSentData") );
		
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
				
				String status = "0";
				for (int i = 0; i < count; i++) {
					
					vo = new SentVO();
					h = al.get(i);
					
					if ( SLibrary.IfNull(h, "STATUS").equals("1") || SLibrary.IfNull(h, "STATUS").equals("2") )
						strRslt = "������";
					else {
						if (SLibrary.IfNull(line).equals("ktmms")) strRslt = VbyP.getValue( "mmskt_"+SLibrary.IfNull(h, "RSLT"));
						else strRslt = VbyP.getValue( "mms_"+SLibrary.IfNull(h, "RSLT"));
					}
					
					if (SLibrary.IfNull(h, "STATUS").equals("0")) status = "0";
					else if (SLibrary.IfNull(h, "STATUS").equals("1")) status = "1";
					else if (SLibrary.IfNull(h, "STATUS").equals("2")) status = "1";
					else if (SLibrary.IfNull(h, "STATUS").equals("3")) status = "2";
					else status = "0";
						
						
					vo.setAll(
							SLibrary.parseInt( SLibrary.IfNull(h, "POST") ),
							line,
							SLibrary.IfNull(h, "REQDATE"),
							SLibrary.IfNull(h, "PHONE"),
							SLibrary.IfNull(h, "ETC1"),
							SLibrary.IfNull(h, "CALLBACK"),
							SLibrary.IfNull(h, "MSG"),
							SLibrary.isNull(strRslt)?"����":strRslt,
							SLibrary.IfNull(h, "RSLTDATE"),
							status,
							SLibrary.IfNull(h, "RSLT")
							);
					rslt.add(vo);
				}
			}catch(Exception e){System.out.println("getSentList Error!");}
			
			h = null;
			al = null;
		}
		
		return rslt;

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
	
	
	public BooleanAndDescriptionVO deleteSentGroupList(Connection conn, String user_id, String pay_type, int idx, int year, int month) {
		
		VbyP.debugLog(user_id + " >> �������� ���� "+pay_type+","+Integer.toString(idx)+","+Integer.toString(year)+","+Integer.toString(month));
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		int updateResultCount = updateSentGroup(conn, user_id, idx, "logdel");
		VbyP.debugLog(user_id + " >> ��������  ���۱׷����̺� ������Ʈ : "+Integer.toString(updateResultCount) );			
				
		if ( updateResultCount == 1 ) {
			
			rvo.setbResult(true);
			VbyP.debugLog(user_id + " >> �������� ����  " );
			
		} else {
			
			rvo.setstrDescription("������ ���� ���� �ʾҽ��ϴ�.");
		}
		
		return rvo;
	}
	
	public BooleanAndDescriptionVO deleteSentGroupList(Connection conn, Connection connSMS, String user_id, int idx, String line, UserInformationVO mvo) {
		
		VbyP.debugLog(user_id + " >> �������� ���� "+Integer.toString(idx));
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		int tranResultCount = SLibrary.IfNull(line).equals("ktmms") ?  deleteSentDataOfTranTableKT(connSMS, mvo.getUser_id(), idx) : deleteSentDataOfTranTable(connSMS, mvo.getUser_id(), idx);
		
		if (tranResultCount > 0) {
			
			cancelPointPut(conn, mvo, tranResultCount);
			rvo.setstrDescription("������ �̹߼� ���� "+Integer.toString(tranResultCount)+"���� ��� �Ǿ����ϴ�.");
			
		}else {
			int updateResultCount = updateSentGroup(conn, user_id, idx, "logdel");
			VbyP.debugLog(user_id + " >> ��������  ���۱׷����̺� ������Ʈ : "+Integer.toString(updateResultCount) );			
					
			if ( updateResultCount == 1 ) {
				
				rvo.setbResult(true);
				VbyP.debugLog(user_id + " >> �������� ����  " );
				
			} else {
				
				rvo.setstrDescription("������ ���� ���� �ʾҽ��ϴ�.");
			}
		}
		
		
		
		return rvo;
	}
	
	public BooleanAndDescriptionVO deleteSentGroupList(Connection conn, String user_id, int idx) {
		
		VbyP.debugLog(user_id + " >> �������� ���� "+Integer.toString(idx));
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		int updateResultCount = updateSentGroup(conn, user_id, idx, "logdel");
		VbyP.debugLog(user_id + " >> ��������  ���۱׷����̺� ������Ʈ : "+Integer.toString(updateResultCount) );			
				
		if ( updateResultCount == 1 ) {
			
			rvo.setbResult(true);
			VbyP.debugLog(user_id + " >> �������� ����  " );
			
		} else {
			
			rvo.setstrDescription("������ ���� ���� �ʾҽ��ϴ�.");
		}
		
		return rvo;
	}
	
	public BooleanAndDescriptionVO deleteSentGroupList(Connection conn, String user_id) {
		
		VbyP.debugLog(user_id + " >> �������� ���� ");
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		int updateResultCount = updateSentGroup(conn, user_id, "logdel");
		VbyP.debugLog(user_id + " >> ��������  ���۱׷����̺� ������Ʈ : "+Integer.toString(updateResultCount) );			
				
		if ( updateResultCount == 1 ) {
			
			rvo.setbResult(true);
			VbyP.debugLog(user_id + " >> �������� ����  " );
			
		} else {
			
			rvo.setstrDescription("������ ���� ���� �ʾҽ��ϴ�.");
		}
		
		return rvo;
	}
	
	public BooleanAndDescriptionVO cancelSentGroupList(Connection conn, Connection connSMS, UserInformationVO mvo, int idx, String sendLine) throws Exception {
		
		VbyP.debugLog(mvo.getUser_id() + " >> LMS ������� ���� "+Integer.toString(idx));
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
				throw new Exception( "�߼� "+CANCEL_GAP/60+"���� ������ ��� �� �� �����ϴ�." );
			}else if (sentGroupInfo.length == 2 
					&& SLibrary.getTime(sentGroupInfo[0], "yyyy-MM-dd HH:mm:ss") < (SLibrary.parseLong( SLibrary.getUnixtimeStringSecond() ))*1000
					){
				rvo = deleteSentGroupList(conn, mvo.getUser_id(), idx);
			} else {
				
				int tranResultCount = SLibrary.IfNull(sendLine).equals("ktmms") ?  deleteSentDataOfTranTableKT(connSMS, mvo.getUser_id(), idx) : deleteSentDataOfTranTable(connSMS, mvo.getUser_id(), idx);
				VbyP.debugLog(mvo.getUser_id() + " >> �������  �������̺� ���� : "+Integer.toString(tranResultCount) );			
				//int reservationResultCount = deleteSentDataOfReservationTable(connSMS, mvo.getUser_id(), idx);
				//VbyP.debugLog(mvo.getUser_id() + " >> �������  �������̺� ���� : "+Integer.toString(reservationResultCount) );	
				int failResultCount = SLibrary.IfNull(sendLine).equals("ktmms") ?selectSentDataOfLogTableKT(connSMS, mvo.getUser_id(), idx) : selectSentDataOfLogTable(connSMS, mvo.getUser_id(), idx);
				VbyP.debugLog(mvo.getUser_id() + " >> �������  �α����̺� �Ǽ�(���Űź�,�ߺ����) : "+Integer.toString(failResultCount));	
				
				if ( sentGroupInfo.length == 2 && SLibrary.parseInt(sentGroupInfo[1]) != (tranResultCount  + failResultCount) ) 
					throw new Exception( "������ �߼� �����Ϳ� ���� �Ǽ��� �޶� ������ ������ �Ǿ����ϴ�.("+Integer.toString(tranResultCount  + failResultCount)+"/"+sentGroupInfo[1]+") ���� �ּ���." ); 
				
				int updateResultCount = updateSentGroup(conn, mvo.getUser_id(), idx, "cancel");
				VbyP.debugLog(mvo.getUser_id() + " >> �������  ���۱׷����̺� ������Ʈ : "+Integer.toString(updateResultCount) );
				
				if ( updateResultCount != 1 )
					throw new Exception( "��һ��°� ���� ���� �ʾҽ��ϴ�." );
						
				if ( cancelPointPut(conn, mvo, tranResultCount +  failResultCount) == 1 ) {
						rvo.setbResult(true);
						VbyP.debugLog(mvo.getUser_id() + " >> �������  �Ǽ� �߰� : "+Integer.toString(tranResultCount ) );					
				} else {
					throw new Exception( "���� ��ҰǼ��� �߰� ���� �ʾҽ��ϴ�.");
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
		pq.setPrepared(conn, VbyP.getSQL("deleteLMSSentDataTranTable"));
		pq.setString(1, user_id);
		pq.setString(2, Integer.toString(idx) );
		
		return pq.executeUpdate();
	}
	
	private int deleteSentDataOfTranTableKT(Connection conn, String user_id, int idx) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, VbyP.getSQL("deleteLMSSentDataTranTableKT"));
		pq.setString(1, user_id);
		pq.setString(2, Integer.toString(idx) );
		
		return pq.executeUpdate();
	}
	
	private int selectSentDataOfLogTable(Connection conn, String user_id, int idx) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn,VbyP.getSQL("selectLMSSentDataLogtable") );
		pq.setString(1, user_id);
		pq.setString(2, Integer.toString(idx) );
		return pq.ExecuteQueryNum();
	}
	
	private int selectSentDataOfLogTableKT(Connection conn, String user_id, int idx) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn,VbyP.getSQL("selectLMSSentDataLogtableKT") );
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
		
		return pm.insertUserPoint(conn, mvo, 46, cnt * PointManager.DEFULT_POINT*3);
	}
}
