package com.m.mobile;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import com.common.VbyP;
import com.common.db.PreparedExecuteQueryManager;
import com.common.util.SLibrary;
import com.common.util.StopWatch;
import com.m.M;
import com.m.common.PointManager;
import com.m.common.Refuse;
import com.m.member.UserInformationVO;

public class SMS implements SMSAble {
	
	static SMS sms = new SMS();
	
	public static SMS getInstance() {
		return sms;
	}
	private SMS(){};
	
	@Override
	public int insertSMSClient(Connection conn, SMSClientVO vo) {

		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("insertClient") );
				
		insertSMSClientPqSetter(pq, vo);
		
		return pq.executeUpdate();
	}

	@Override
	public int insertSMSLog(Connection conn, LogVO vo, int year, int month) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		
		pq.setPrepared( conn, VbyP.getSQL("insertSMSLog"));
		pq.setString(1, vo.getUser_id());
		pq.setString(2, vo.getLine());
		pq.setString(3, vo.getReservation());
		pq.setString(4, vo.getTranType());
		pq.setString(5, vo.getReturnPhone());
		pq.setInt(6, vo.getCount());
		pq.setString(7, vo.getMessage());
		pq.setString(8, vo.getUser_ip());
		pq.setString(9, vo.getTimeSend());
		pq.setString(10, vo.getTimeWrite());
		pq.setString(11, vo.getYnDel());
		pq.setString(12, vo.getDelType());
		pq.executeUpdate();
		
		return getSMSLogKey(conn);
	}
	
	@Override
	public int insertSMSClient(Connection connSMS, ArrayList<SMSClientVO> al, String via) {

		String sql = "";
		int resultCount = 0;
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		String resultStateCode = "2";
		if (SLibrary.IfNull(via).equals("sk")) {
			sql = VbyP.getSQL("insertClientSK");
			resultStateCode = "9";
		} else if (SLibrary.IfNull(via).equals("kt")) {
			sql = VbyP.getSQL("insertClientKT");
			resultStateCode = "3";
		}
		else if (SLibrary.IfNull(via).equals("han")) {
			sql = VbyP.getSQL("insertClientHN");
		}
		else if (SLibrary.IfNull(via).equals("hanr")) {
			sql = VbyP.getSQL("insertClientHNR");
		}
		else if (SLibrary.IfNull(via).equals("it")) {
			sql = VbyP.getSQL("insertClientIT");
		}
		else if (SLibrary.IfNull(via).equals("pp")) {
			sql = VbyP.getSQL("insertClientPP");
		}
		else {
			sql = VbyP.getSQL("insertClient");
			//pq.setPrepared( connSMS, VbyP.getSQL("insertClient") );
		}
		
		pq.setPrepared( connSMS, sql );
		
		int count = al.size();
		SMSClientVO vo = null;
		int maxBatch = SLibrary.parseInt( VbyP.getValue("executeBatchCount") );
		
		Hashtable<String, String> hashTable = new Hashtable<String, String>();
		Hashtable<String, String> hashTable_refuse = null;	
		
		if (count > 0) {
			hashTable_refuse = Refuse.getRefusePhoneFromDB();
			vo = al.get(0);
			//stopWatch play
			StopWatch sw = new StopWatch();
			sw.play();
			for (int i = 0; i < count; i++) {
				
				vo = new SMSClientVO();
				vo = al.get(i);
				
				//¼ö½Å°ÅºÎ
				if (Refuse.isRefuse(hashTable_refuse, vo.TR_PHONE)){
					
					if (SLibrary.IfNull(via).equals("sk")) 
						insertSMSClientPqSetter_failSK(pq, vo,resultStateCode, "98", i);
					else
						insertSMSClientPqSetter_fail(pq, vo,resultStateCode, "98");
					
				} else if (hashTable.containsKey(vo.TR_PHONE)){
					if (SLibrary.IfNull(via).equals("sk")) 
						insertSMSClientPqSetter_failSK(pq, vo,resultStateCode, "99", i);
					else
						insertSMSClientPqSetter_fail(pq, vo,resultStateCode, "99");
				}else {
					hashTable.put(vo.TR_PHONE, "");
					if (SLibrary.IfNull(via).equals("sk")) 
						insertSMSClientPqSetterSK(pq, vo, i);
					else
						insertSMSClientPqSetter(pq, vo);
				}
				
				pq.addBatch();
				
				//¹ß¼ÛÄ«¿îÆ®
				M.setState(vo.getTR_ETC2(), i+1);
				
				if (i >= maxBatch && (i%maxBatch) == 0 ) {
					
					System.out.println(i + " reDBConnection !");
					resultCount += pq.executeBatch();
					
					try { if ( connSMS != null ) connSMS.close(); } 
					catch(Exception e) {System.out.println( via+"connSMS close Error!!!!" + e.toString());}
					
					connSMS = VbyP.getDB(via);					
					if (connSMS != null) System.out.println(via+"connSMS connection!!!!");
					
					/*
					if (SLibrary.IfNull(via).equals("sk")) pq.setPrepared( connSMS, VbyP.getSQL("insertClientSK") );
					else if (SLibrary.IfNull(via).equals("kt")) {
						pq.setPrepared( connSMS, VbyP.getSQL("insertClientKT") );
					} else pq.setPrepared( connSMS, VbyP.getSQL("insertClient") );
					*/
					pq.setPrepared( connSMS, sql );
				}
				
			}
			resultCount += pq.executeBatch();
		}

		return resultCount;
	}
	

	public int insertLMSClient(Connection connSMS, ArrayList<SMSClientVO> al, String via) {

		String sql = "";
		int resultCount = 0;
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		String resultStateCode = "2";
		if (SLibrary.IfNull(via).equals("sk")) {
			sql = VbyP.getSQL("insertClientSKLMS") ;
			resultStateCode = "9";
		}
		else
			sql = VbyP.getSQL("insertClient") ;
		
		pq.setPrepared( connSMS, sql );
		
		int count = al.size();
		SMSClientVO vo = null;
		int maxBatch = SLibrary.parseInt( VbyP.getValue("executeBatchCount") );
		
		Hashtable<String, String> hashTable = new Hashtable<String, String>();
		Hashtable<String, String> hashTable_refuse = null;	
		
		if (count > 0) {
			hashTable_refuse = Refuse.getRefusePhoneFromDB();
			vo = al.get(0);
			//stopWatch play
			StopWatch sw = new StopWatch();
			sw.play();
			for (int i = 0; i < count; i++) {
				
				vo = new SMSClientVO();
				vo = al.get(i);
				
				//¼ö½Å°ÅºÎ
				if (Refuse.isRefuse(hashTable_refuse, vo.TR_PHONE)){
					
					if (SLibrary.IfNull(via).equals("sk")) 
						insertSMSClientPqSetter_failSK(pq, vo,resultStateCode, "98", i);
					else
						insertSMSClientPqSetter_fail(pq, vo,resultStateCode, "98");
					
				} else if (hashTable.containsKey(vo.TR_PHONE)){
					if (SLibrary.IfNull(via).equals("sk")) 
						insertSMSClientPqSetter_failSK(pq, vo,resultStateCode, "99", i);
					else
						insertSMSClientPqSetter_fail(pq, vo,resultStateCode, "99");
				}else {
					hashTable.put(vo.TR_PHONE, "");
					if (SLibrary.IfNull(via).equals("sk")) 
						insertSMSClientPqSetterSK(pq, vo, i);
					else
						insertSMSClientPqSetter(pq, vo);
				}
				
				pq.addBatch();
				
				//¹ß¼ÛÄ«¿îÆ®
				M.setState(vo.getTR_ETC2(), i+1);
				
				if (i >= maxBatch && (i%maxBatch) == 0 ) {
					
					System.out.println(i + " reDBConnection !");
					resultCount += pq.executeBatch();
					
					try { if ( connSMS != null ) connSMS.close(); } 
					catch(Exception e) {System.out.println( via+"connSMS close Error!!!!" + e.toString());}
					
					connSMS = VbyP.getDB(via);					
					if (connSMS != null) System.out.println(via+"connSMS connection!!!!");
					
					pq.setPrepared( connSMS, sql );

				}
				
			}
			resultCount += pq.executeBatch();
		}

		return resultCount;
	}
	
	// sk mms ¹ß¼Û
	public int insertMMSClientSK(Connection connSMS, ArrayList<SMSClientVO> al, String imagePath) {

		String sql = "";
		int resultCount = 0;
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		String resultStateCode = "9";
		sql = VbyP.getSQL("insertClientSKMMS") ;
		
		
		pq.setPrepared( connSMS, sql );
		
		int count = al.size();
		SMSClientVO vo = null;
		int maxBatch = SLibrary.parseInt( VbyP.getValue("executeBatchCount") );
		
		Hashtable<String, String> hashTable = new Hashtable<String, String>();
		Hashtable<String, String> hashTable_refuse = null;	
		
		if (count > 0) {
			hashTable_refuse = Refuse.getRefusePhoneFromDB();
			vo = al.get(0);
			//stopWatch play
			StopWatch sw = new StopWatch();
			sw.play();
			for (int i = 0; i < count; i++) {
				
				vo = new SMSClientVO();
				vo = al.get(i);
				
				//¼ö½Å°ÅºÎ
				if (Refuse.isRefuse(hashTable_refuse, vo.TR_PHONE)){
					insertMMSClientPqSetter_failSK(pq, vo,resultStateCode, "98", i, imagePath);
				} else if (hashTable.containsKey(vo.TR_PHONE)){
					insertMMSClientPqSetter_failSK(pq, vo,resultStateCode, "99", i, imagePath);
				}else {
					hashTable.put(vo.TR_PHONE, "");
					insertMMSClientPqSetterSK(pq, vo, i, imagePath);
				}
				
				pq.addBatch();
				
				//¹ß¼ÛÄ«¿îÆ®
				M.setState(vo.getTR_ETC2(), i+1);
				
				if (i >= maxBatch && (i%maxBatch) == 0 ) {
					
					System.out.println(i + " reDBConnection !");
					resultCount += pq.executeBatch();
					
					try { if ( connSMS != null ) connSMS.close(); } 
					catch(Exception e) {System.out.println( "connSMS close Error!!!!" + e.toString());}
					
					connSMS = VbyP.getDB("sk");					
					if (connSMS != null) System.out.println("connSMS connection!!!!");
					
					pq.setPrepared( connSMS, sql );

				}
				
			}
			resultCount += pq.executeBatch();
		}

		return resultCount;
	}
	
	@Override
	public int insertSMSClientRefuse(Connection connSMS, ArrayList<SMSClientVO> al, String via) {

		
		int resultCount = 0;
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		
		String resultStateCode = "2";
		if (SLibrary.IfNull(via).equals("sk")) {
			pq.setPrepared( connSMS, VbyP.getSQL("insertClientSK") );
			resultStateCode = "9";
		}
		else
			pq.setPrepared( connSMS, VbyP.getSQL("insertClient") );
		
		int count = al.size();
		SMSClientVO vo = null;
		int maxBatch = SLibrary.parseInt( VbyP.getValue("executeBatchCount") );
		
		Hashtable<String, String> hashTable = new Hashtable<String, String>();
		Hashtable<String, String> hashTable_refuse = null;		
		
		if (count > 0) {
			
			hashTable_refuse = Refuse.getRefusePhoneFromDB(al.get(0).getTR_ETC2());

			for (int i = 0; i < count; i++) {
				
				vo = new SMSClientVO();
				vo = al.get(i);
				//¼ö½Å°ÅºÎ
				if (Refuse.isRefuse(hashTable_refuse, vo.TR_PHONE)){
					insertSMSClientPqSetter_fail(pq, vo,resultStateCode, "98");
				}					
				//Áßº¹ ½ÇÆĞ
				else if (hashTable.containsKey(vo.TR_PHONE)){
					insertSMSClientPqSetter_fail(pq, vo,resultStateCode, "99");
				}else {
					hashTable.put(vo.TR_PHONE, "");
					insertSMSClientPqSetter(pq, vo);
				}
				
				pq.addBatch();								
				//¹ß¼ÛÄ«¿îÆ®
				M.setState(vo.getTR_ETC2(), i+1);
				
				if (i >= maxBatch && (i%maxBatch) == 0 ) {
					
					System.out.println(i + " reDBConnection !");
					resultCount += pq.executeBatch();
					
					try { if ( connSMS != null ) connSMS.close(); }
					catch(Exception e) {System.out.println( "connSMS close Error!!!!" + e.toString());}
					
					connSMS = VbyP.getDB(via);					
					if (connSMS != null) System.out.println("connSMS connection!!!!");
					pq.setPrepared( connSMS, VbyP.getSQL("insertClient") );
					System.out.println("connSMS connection!!!! >> pq.setPrepared");
				}
				
			}
			resultCount += pq.executeBatch();			
		}		

		return resultCount;
	}
	
	@Override
	public int sendPointPut(Connection conn, UserInformationVO mvo, int cnt) {

		PointManager pm = PointManager.getInstance();		
		return pm.insertUserPoint(conn, mvo, 11, cnt * PointManager.DEFULT_POINT);
	}
	
	public LogVO getLogVO( UserInformationVO mvo, Boolean bReservation, String message, ArrayList<String[]> phoneAndNameArrayList, String returnPhone, String reservationDate, String ip) throws Exception{
		
		LogVO vo = new LogVO();
		int count = phoneAndNameArrayList.size();
		if (count < 0)
			throw new Exception("ÀüÈ£¹øÈ£ ¸®½ºÆ®°¡ ¾ø½À´Ï´Ù.");
		
		String [] temp = phoneAndNameArrayList.get(0);
		
		if ( temp.length < 1 || SLibrary.isNull(temp[0]))
			throw new Exception("ÀüÈ­¹øÈ£ ¸®½ºÆ®°¡ ¾ø½À´Ï´Ù.2");
			
		vo.setIdx(0);
		vo.setUser_id( mvo.getUser_id() );
		vo.setLine(mvo.getLine());
		vo.setReservation((bReservation)?"Y":"N");
		vo.setTranType("");
		vo.setReturnPhone(returnPhone);
		vo.setCount(count);
		vo.setMessage(message);
		vo.setUser_ip( ip );
		vo.setTimeSend( (bReservation)?reservationDate:SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss") );
		vo.setTimeWrite( SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss") );
		vo.setYnDel( "N" );
		vo.setDelType( "none" );
		vo.setTimeDel( "" );
		
		return vo;
	}
	
	
	public ArrayList<SMSClientVO> getSMSClientVO( Connection conn, UserInformationVO mvo, Boolean bReservation, int SMSLogKey, String message, ArrayList<String[]> phoneAndNameArrayList, String returnPhone, String reservationDate, String ip) throws Exception{
		
		ArrayList<SMSClientVO> clientAl = new ArrayList<SMSClientVO>();
		SMSClientVO vo = new SMSClientVO();
		String [] temp = null;
		
		VbyP.debugLog(" >> getPhone");
		int count = phoneAndNameArrayList.size();
		if (count < 0)
			throw new Exception("ÀüÈ­¹øÈ£ ¸®½ºÆ®°¡ ¾ø½À´Ï´Ù.");				
		
		for (int i = 0; i < count; i++) {
			
			vo = new SMSClientVO();
			temp = phoneAndNameArrayList.get(i);
			vo.setTR_SENDDATE( reservationDate );
			vo.setTR_SERIALNUM( 0 );
			vo.setTR_ID( SITE_CODE );
			vo.setTR_SENDSTAT( CLIENT_SENDSTAT );
			vo.setTR_RSLTSTAT( CLIENT_RSLTSTAT );
			vo.setTR_MSGTYPE( CLIENT_MESSAGETYPE );
			vo.setTR_PHONE( (temp.length > 0)? SLibrary.IfNull(temp[0]):"" );
			vo.setTR_CALLBACK( returnPhone );
			vo.setTR_RSLTDATE( reservationDate );
			vo.setTR_MODIFIED( reservationDate );
			vo.setTR_MSG( message );
			vo.setTR_NET( "" );
			vo.setTR_ETC1( (temp.length == 2)?SLibrary.IfNull(temp[1]):"" );
			vo.setTR_ETC2( mvo.getUser_id() );
			vo.setTR_ETC3( ip );
			vo.setTR_ETC4( TRAN_TYPE_CODE );
			vo.setTR_ETC5( (bReservation)?"R":"I" );
			vo.setTR_ETC6( Integer.toString(SMSLogKey) );
			//vo.setTR_ETC7( getPayTypeCode(mvo.getPay_type())+"|"+SLibrary.getUnixtimeStringSecond());
			
			clientAl.add(vo);
		}
		VbyP.debugLog(" >> getPhone -> loop");
		return clientAl;
	}
	
	public ArrayList<SMSClientVO> getSMSClientVOMeargeAndInterval( Connection conn, UserInformationVO mvo, Boolean bReservation, int SMSLogKey, String message, ArrayList<String[]> phoneAndNameArrayList, String returnPhone, String reservationDate, String ip, int cnt, int minute, boolean bMerge) throws Exception{
		
		ArrayList<SMSClientVO> clientAl = new ArrayList<SMSClientVO>();
		SMSClientVO vo = new SMSClientVO();
		String [] temp = null;
		boolean bInterval = false;
		
		VbyP.debugLog(" >> getPhone");
		int count = phoneAndNameArrayList.size();
		if (count < 0)
			throw new Exception("ÀüÈ­¹øÈ£ ¸®½ºÆ®°¡ ¾ø½À´Ï´Ù.");				
		
		String name = "";
		
		if (cnt > 0 && minute > 0) bInterval = true;
		
		
		for (int i = 0; i < count; i++) {
			
			vo = new SMSClientVO();
			temp = phoneAndNameArrayList.get(i);
			
			name = (temp.length == 2)?SLibrary.IfNull(temp[1]):"";
			if (bInterval && i != 0 && (i+1)%cnt == 0) {
				reservationDate = SLibrary.getDateAddSecond(reservationDate, minute*60);
			}
			
			vo.setTR_SENDDATE( reservationDate );
			vo.setTR_SERIALNUM( 0 );
			vo.setTR_ID( SITE_CODE );
			vo.setTR_SENDSTAT( CLIENT_SENDSTAT );
			vo.setTR_RSLTSTAT( CLIENT_RSLTSTAT );
			vo.setTR_MSGTYPE( CLIENT_MESSAGETYPE );
			vo.setTR_PHONE( (temp.length > 0)? SLibrary.IfNull(temp[0]):"" );
			vo.setTR_CALLBACK( returnPhone );
			vo.setTR_RSLTDATE( reservationDate );
			vo.setTR_MODIFIED( reservationDate );
			vo.setTR_MSG( bMerge ? SLibrary.replaceAll(message, "{ÀÌ¸§}", name ):message  );
			vo.setTR_NET( "" );
			vo.setTR_ETC1( name );
			vo.setTR_ETC2( mvo.getUser_id() );
			vo.setTR_ETC3( ip );
			vo.setTR_ETC4( TRAN_TYPE_CODE );
			vo.setTR_ETC5( (bReservation || bInterval)?"R":"I" );
			vo.setTR_ETC6( Integer.toString(SMSLogKey) );
			//vo.setTR_ETC7( getPayTypeCode(mvo.getPay_type())+"|"+SLibrary.getUnixtimeStringSecond());
			
			clientAl.add(vo);
		}
		VbyP.debugLog(" >> getPhone -> loop");
		return clientAl;
	}
	
	/**
	 * ÀÌÅë»ç ¹ÌÀû¿ë ÇÑ±Û °Ë»ç
	 * @param message
	 * @return
	 */
	public String isMessage(String message) {
		
		StringBuffer buf = new StringBuffer();
		buf.append("°¡°¢°£°¤°¥°¦°§°¨°©°ª°«°¬°­°®°¯°°°±°²°³°´°µ°¶°·°¸°¹°º°»°¼°½°¾°¿°À°Á°Â°Ã°Ä°Å°Æ°Ç°È°É°Ê°Ë°Ì°Í°Î°Ï°Ğ°Ñ°Ò°Ó°Ô°Õ°Ö°×°Ø°Ù°Ú°Û°Ü°İ°Ş°ß°à°á°â°ã°ä°å°æ°ç°è°é°ê°ë°ì°í°î°ï°ğ°ñ°ò°ó°ô°õ°ö°÷°ø°ù°ú°û°ü°ı°ş±¡±¢±£±¤±¥±¦±§±¨±©±ª±«±¬±­±®±¯±°±±±²±³±´±µ±¶±·±¸±¹±º±»±¼±½±¾±¿±À±Á±Â±Ã±Ä±Å±Æ±Ç±È±É±Ê±Ë±Ì±Í±Î±Ï±Ğ±Ñ±Ò±Ó±Ô±Õ±Ö±×±Ø±Ù±Ú±Û±Ü±İ±Ş±ß±à±á±â±ã±ä±å±æ±ç±è±é±ê±ë±ì±í±î±ï±ğ±ñ±ò±ó±ô±õ±ö±÷±ø±ù±ú±û±ü±ı±ş²¡²¢²£²¤²¥²¦²§²¨²©²ª²«²¬²­²®²¯²°²±²²²³²´²µ²¶²·²¸²¹²º²»²¼²½²¾²¿²À²Á²Â²Ã²Ä²Å²Æ²Ç²È²É²Ê²Ë²Ì²Í²Î²Ï²Ğ²Ñ²Ò²Ó²Ô²Õ²Ö²×²Ø²Ù²Ú²Û²Ü²İ²Ş²ß²à²á²â²ã²ä²å²æ²ç²è²é²ê²ë²ì²í²î²ï²ğ²ñ²ò²ó²ô²õ²ö²÷²ø²ù²ú²û²ü²ı²ş³¡³¢³£³¤³¥³¦³§³¨³©³ª³«³¬³­³®³¯³°³±³²³³³´³µ³¶³·³¸³¹³º³»³¼³½³¾³¿³À³Á³Â³Ã³Ä³Å³Æ³Ç³È³É³Ê³Ë³Ì³Í³Î³Ï³Ğ³Ñ³Ò³Ó³Ô³Õ³Ö³×³Ø³Ù³Ú³Û³Ü³İ³Ş³ß³à³á³â³ã³ä³å³æ³ç³è³é³ê³ë³ì³í³î³ï³ğ³ñ³ò³ó³ô³õ³ö³÷³ø³ù³ú³û³ü³ı³ş´¡´¢´£´¤´¥´¦´§´¨´©´ª´«´¬´­´®´¯´°´±´²´³´´´µ´¶´·´¸´¹´º´»´¼´½´¾´¿´À´Á´Â´Ã´Ä´Å´Æ´Ç´È´É´Ê´Ë´Ì´Í´Î´Ï´Ğ´Ñ´Ò´Ó´Ô´Õ´Ö´×´Ø´Ù´Ú´Û´Ü´İ´Ş´ß´à´á´â´ã´ä´å´æ´ç´è´é´ê´ë´ì´í´î´ï´ğ´ñ´ò´ó´ô´õ´ö´÷´ø´ù´ú´û´ü´ı´şµ¡µ¢µ£µ¤µ¥µ¦µ§µ¨µ©µªµ«µ¬µ­µ®µ¯µ°µ±µ²µ³µ´µµµ¶µ·µ¸µ¹µºµ»µ¼µ½µ¾µ¿µÀµÁµÂµÃµÄµÅµÆµÇµÈµÉµÊµËµÌµÍµÎµÏµĞµÑµÒµÓµÔµÕµÖµ×µØµÙµÚµÛµÜµİµŞµßµàµáµâµãµäµåµæµçµèµéµêµëµìµíµîµïµğµñµòµóµôµõµöµ÷µøµùµúµûµüµıµş¶¡¶¢¶£¶¤¶¥¶¦¶§¶¨¶©¶ª¶«¶¬¶­¶®¶¯¶°¶±¶²¶³¶´¶µ¶¶¶·¶¸¶¹¶º¶»¶¼¶½¶¾¶¿¶À¶Á¶Â¶Ã¶Ä¶Å¶Æ¶Ç¶È¶É¶Ê¶Ë¶Ì¶Í¶Î¶Ï¶Ğ¶Ñ¶Ò¶Ó¶Ô¶Õ¶Ö¶×¶Ø¶Ù¶Ú¶Û¶Ü¶İ¶Ş¶ß¶à¶á¶â¶ã¶ä¶å¶æ¶ç¶è¶é¶ê¶ë¶ì¶í¶î¶ï¶ğ¶ñ¶ò¶ó¶ô¶õ¶ö¶÷¶ø¶ù¶ú¶û¶ü¶ı¶ş·¡·¢·£·¤·¥·¦·§·¨·©·ª·«·¬·­·®·¯·°·±·²·³·´·µ·¶···¸·¹·º·»·¼·½·¾·¿·À·Á·Â·Ã·Ä·Å·Æ·Ç·È·É·Ê·Ë·Ì·Í·Î·Ï·Ğ·Ñ·Ò·Ó·Ô·Õ·Ö·×·Ø·Ù·Ú·Û·Ü·İ·Ş·ß·à·á·â·ã·ä·å·æ·ç·è·é·ê·ë·ì·í·î·ï·ğ·ñ·ò·ó·ô·õ·ö·÷·ø·ù·ú·û·ü·ı·ş¸¡¸¢¸£¸¤¸¥¸¦¸§¸¨¸©¸ª¸«¸¬¸­¸®¸¯¸°¸±¸²¸³¸´¸µ¸¶¸·¸¸¸¹¸º¸»¸¼¸½¸¾¸¿¸À¸Á¸Â¸Ã¸Ä¸Å¸Æ¸Ç¸È¸É¸Ê¸Ë¸Ì¸Í¸Î¸Ï¸Ğ¸Ñ¸Ò¸Ó¸Ô¸Õ¸Ö¸×¸Ø¸Ù¸Ú¸Û¸Ü¸İ¸Ş¸ß¸à¸á¸â¸ã¸ä¸å¸æ¸ç¸è¸é¸ê¸ë¸ì¸í¸î¸ï¸ğ¸ñ¸ò¸ó¸ô¸õ¸ö¸÷¸ø¸ù¸ú¸û¸ü¸ı¸ş¹¡¹¢¹£¹¤¹¥¹¦¹§¹¨¹©¹ª¹«¹¬¹­¹®¹¯¹°¹±¹²¹³¹´¹µ¹¶¹·¹¸¹¹¹º¹»¹¼¹½¹¾¹¿¹À¹Á¹Â¹Ã¹Ä¹Å¹Æ¹Ç¹È¹É¹Ê¹Ë¹Ì¹Í¹Î¹Ï¹Ğ¹Ñ¹Ò¹Ó¹Ô¹Õ¹Ö¹×¹Ø¹Ù¹Ú¹Û¹Ü¹İ¹Ş¹ß¹à¹á¹â¹ã¹ä¹å¹æ¹ç¹è¹é¹ê¹ë¹ì¹í¹î¹ï¹ğ¹ñ¹ò¹ó¹ô¹õ¹ö¹÷¹ø¹ù¹ú¹û¹ü¹ı¹şº¡º¢º£º¤º¥º¦º§º¨º©ºªº«º¬º­º®º¯º°º±º²º³º´ºµº¶º·º¸º¹ººº»º¼º½º¾º¿ºÀºÁºÂºÃºÄºÅºÆºÇºÈºÉºÊºËºÌºÍºÎºÏºĞºÑºÒºÓºÔºÕºÖº×ºØºÙºÚºÛºÜºİºŞºßºàºáºâºãºäºåºæºçºèºéºêºëºìºíºîºïºğºñºòºóºôºõºöº÷ºøºùºúºûºüºıºş»¡»¢»£»¤»¥»¦»§»¨»©»ª»«»¬»­»®»¯»°»±»²»³»´»µ»¶»·»¸»¹»º»»»¼»½»¾»¿»À»Á»Â»Ã»Ä»Å»Æ»Ç»È»É»Ê»Ë»Ì»Í»Î»Ï»Ğ»Ñ»Ò»Ó»Ô»Õ»Ö»×»Ø»Ù»Ú»Û»Ü»İ»Ş»ß»à»á»â»ã»ä»å»æ»ç»è»é»ê»ë»ì»í»î»ï»ğ»ñ»ò»ó»ô»õ»ö»÷»ø»ù»ú»û»ü»ı»ş¼¡¼¢¼£¼¤¼¥¼¦¼§¼¨¼©¼ª¼«¼¬¼­¼®¼¯¼°¼±¼²¼³¼´¼µ¼¶¼·¼¸¼¹¼º¼»¼¼¼½¼¾¼¿¼À¼Á¼Â¼Ã¼Ä¼Å¼Æ¼Ç¼È¼É¼Ê¼Ë¼Ì¼Í¼Î¼Ï¼Ğ¼Ñ¼Ò¼Ó¼Ô¼Õ¼Ö¼×¼Ø¼Ù¼Ú¼Û¼Ü¼İ¼Ş¼ß¼à¼á¼â¼ã¼ä¼å¼æ¼ç¼è¼é¼ê¼ë¼ì¼í¼î¼ï¼ğ¼ñ¼ò¼ó¼ô¼õ¼ö¼÷¼ø¼ù¼ú¼û¼ü¼ı¼ş½¡½¢½£½¤½¥½¦½§½¨½©½ª½«½¬½­½®½¯½°½±½²½³½´½µ½¶½·½¸½¹½º½»½¼½½½¾½¿½À½Á½Â½Ã½Ä½Å½Æ½Ç½È½É½Ê½Ë½Ì½Í½Î½Ï½Ğ½Ñ½Ò½Ó½Ô½Õ½Ö½×½Ø½Ù½Ú½Û½Ü½İ½Ş½ß½à½á½â½ã½ä½å½æ½ç½è½é½ê½ë½ì½í½î½ï½ğ½ñ½ò½ó½ô½õ½ö½÷½ø½ù½ú½û½ü½ı½ş¾¡¾¢¾£¾¤¾¥¾¦¾§¾¨¾©¾ª¾«¾¬¾­¾®¾¯¾°¾±¾²¾³¾´¾µ¾¶¾·¾¸¾¹¾º¾»¾¼¾½¾¾¾¿¾À¾Á¾Â¾Ã¾Ä¾Å¾Æ¾Ç¾È¾É¾Ê¾Ë¾Ì¾Í¾Î¾Ï¾Ğ¾Ñ¾Ò¾Ó¾Ô¾Õ¾Ö¾×¾Ø¾Ù¾Ú¾Û¾Ü¾İ¾Ş¾ß¾à¾á¾â¾ã¾ä¾å¾æ¾ç¾è¾é¾ê¾ë¾ì¾í¾î¾ï¾ğ¾ñ¾ò¾ó¾ô¾õ¾ö¾÷¾ø¾ù¾ú¾û¾ü¾ı¾ş¿¡¿¢¿£¿¤¿¥¿¦¿§¿¨¿©¿ª¿«¿¬¿­¿®¿¯¿°¿±¿²¿³¿´¿µ¿¶¿·¿¸¿¹¿º¿»¿¼¿½¿¾¿¿¿À¿Á¿Â¿Ã¿Ä¿Å¿Æ¿Ç¿È¿É¿Ê¿Ë¿Ì¿Í¿Î¿Ï¿Ğ¿Ñ¿Ò¿Ó¿Ô¿Õ¿Ö¿×¿Ø¿Ù¿Ú¿Û¿Ü¿İ¿Ş¿ß¿à¿á¿â¿ã¿ä¿å¿æ¿ç¿è¿é¿ê¿ë¿ì¿í¿î¿ï¿ğ¿ñ¿ò¿ó¿ô¿õ¿ö¿÷¿ø¿ù¿ú¿û¿ü¿ı¿şÀ¡À¢À£À¤À¥À¦À§À¨À©ÀªÀ«À¬À­À®À¯À°À±À²À³À´ÀµÀ¶À·À¸À¹ÀºÀ»À¼À½À¾À¿ÀÀÀÁÀÂÀÃÀÄÀÅÀÆÀÇÀÈÀÉÀÊÀËÀÌÀÍÀÎÀÏÀĞÀÑÀÒÀÓÀÔÀÕÀÖÀ×ÀØÀÙÀÚÀÛÀÜÀİÀŞÀßÀàÀáÀâÀãÀäÀåÀæÀçÀèÀéÀêÀëÀìÀíÀîÀïÀğÀñÀòÀóÀôÀõÀöÀ÷ÀøÀùÀúÀûÀüÀıÀşÁ¡Á¢Á£Á¤Á¥Á¦Á§Á¨Á©ÁªÁ«Á¬Á­Á®Á¯Á°Á±Á²Á³Á´ÁµÁ¶Á·Á¸Á¹ÁºÁ»Á¼Á½Á¾Á¿ÁÀÁÁÁÂÁÃÁÄÁÅÁÆÁÇÁÈÁÉÁÊÁËÁÌÁÍÁÎÁÏÁĞÁÑÁÒÁÓÁÔÁÕÁÖÁ×ÁØÁÙÁÚÁÛÁÜÁİÁŞÁßÁàÁáÁâÁãÁäÁåÁæÁçÁèÁéÁêÁëÁìÁíÁîÁïÁğÁñÁòÁóÁôÁõÁöÁ÷ÁøÁùÁúÁûÁüÁıÁşÂ¡Â¢Â£Â¤Â¥Â¦Â§Â¨Â©ÂªÂ«Â¬Â­Â®Â¯Â°Â±Â²Â³Â´ÂµÂ¶Â·Â¸Â¹ÂºÂ»Â¼Â½Â¾Â¿ÂÀÂÁÂÂÂÃÂÄÂÅÂÆÂÇÂÈÂÉÂÊÂËÂÌÂÍÂÎÂÏÂĞÂÑÂÒÂÓÂÔÂÕÂÖÂ×ÂØÂÙÂÚÂÛÂÜÂİÂŞÂßÂàÂáÂâÂãÂäÂåÂæÂçÂèÂéÂêÂëÂìÂíÂîÂïÂğÂñÂòÂóÂôÂõÂöÂ÷ÂøÂùÂúÂûÂüÂıÂşÃ¡Ã¢Ã£Ã¤Ã¥Ã¦Ã§Ã¨Ã©ÃªÃ«Ã¬Ã­Ã®Ã¯Ã°Ã±Ã²Ã³Ã´ÃµÃ¶Ã·Ã¸Ã¹ÃºÃ»Ã¼Ã½Ã¾Ã¿ÃÀÃÁÃÂÃÃÃÄÃÅÃÆÃÇÃÈÃÉÃÊÃËÃÌÃÍÃÎÃÏÃĞÃÑÃÒÃÓÃÔÃÕÃÖÃ×ÃØÃÙÃÚÃÛÃÜÃİÃŞÃßÃàÃáÃâÃãÃäÃåÃæÃçÃèÃéÃêÃëÃìÃíÃîÃïÃğÃñÃòÃóÃôÃõÃöÃ÷ÃøÃùÃúÃûÃüÃıÃşÄ¡Ä¢Ä£Ä¤Ä¥Ä¦Ä§Ä¨Ä©ÄªÄ«Ä¬Ä­Ä®Ä¯Ä°Ä±Ä²Ä³Ä´ÄµÄ¶Ä·Ä¸Ä¹ÄºÄ»Ä¼Ä½Ä¾Ä¿ÄÀÄÁÄÂÄÃÄÄÄÅÄÆÄÇÄÈÄÉÄÊÄËÄÌÄÍÄÎÄÏÄĞÄÑÄÒÄÓÄÔÄÕÄÖÄ×ÄØÄÙÄÚÄÛÄÜÄİÄŞÄßÄàÄáÄâÄãÄäÄåÄæÄçÄèÄéÄêÄëÄìÄíÄîÄïÄğÄñÄòÄóÄôÄõÄöÄ÷ÄøÄùÄúÄûÄüÄıÄşÅ¡Å¢Å£Å¤Å¥Å¦Å§Å¨Å©ÅªÅ«Å¬Å­Å®Å¯Å°Å±Å²Å³Å´ÅµÅ¶Å·Å¸Å¹ÅºÅ»Å¼Å½Å¾Å¿ÅÀÅÁÅÂÅÃÅÄÅÅÅÆÅÇÅÈÅÉÅÊÅËÅÌÅÍÅÎÅÏÅĞÅÑÅÒÅÓÅÔÅÕÅÖÅ×ÅØÅÙÅÚÅÛÅÜÅİÅŞÅßÅàÅáÅâÅãÅäÅåÅæÅçÅèÅéÅêÅëÅìÅíÅîÅïÅğÅñÅòÅóÅôÅõÅöÅ÷ÅøÅùÅúÅûÅüÅıÅşÆ¡Æ¢Æ£Æ¤Æ¥Æ¦Æ§Æ¨Æ©ÆªÆ«Æ¬Æ­Æ®Æ¯Æ°Æ±Æ²Æ³Æ´ÆµÆ¶Æ·Æ¸Æ¹ÆºÆ»Æ¼Æ½Æ¾Æ¿ÆÀÆÁÆÂÆÃÆÄÆÅÆÆÆÇÆÈÆÉÆÊÆËÆÌÆÍÆÎÆÏÆĞÆÑÆÒÆÓÆÔÆÕÆÖÆ×ÆØÆÙÆÚÆÛÆÜÆİÆŞÆßÆàÆáÆâÆãÆäÆåÆæÆçÆèÆéÆêÆëÆìÆíÆîÆïÆğÆñÆòÆóÆôÆõÆöÆ÷ÆøÆùÆúÆûÆüÆıÆşÇ¡Ç¢Ç£Ç¤Ç¥Ç¦Ç§Ç¨Ç©ÇªÇ«Ç¬Ç­Ç®Ç¯Ç°Ç±Ç²Ç³Ç´ÇµÇ¶Ç·Ç¸Ç¹ÇºÇ»Ç¼Ç½Ç¾Ç¿ÇÀÇÁÇÂÇÃÇÄÇÅÇÆÇÇÇÈÇÉÇÊÇËÇÌÇÍÇÎÇÏÇĞÇÑÇÒÇÓÇÔÇÕÇÖÇ×ÇØÇÙÇÚÇÛÇÜÇİÇŞÇßÇàÇáÇâÇãÇäÇåÇæÇçÇèÇéÇêÇëÇìÇíÇîÇïÇğÇñÇòÇóÇôÇõÇöÇ÷ÇøÇùÇúÇûÇüÇıÇşÈ¡È¢È£È¤È¥È¦È§È¨È©ÈªÈ«È¬È­È®È¯È°È±È²È³È´ÈµÈ¶È·È¸È¹ÈºÈ»È¼È½È¾È¿ÈÀÈÁÈÂÈÃÈÄÈÅÈÆÈÇÈÈÈÉÈÊÈËÈÌÈÍÈÎÈÏÈĞÈÑÈÒÈÓÈÔÈÕÈÖÈ×ÈØÈÙÈÚÈÛÈÜÈİÈŞÈßÈàÈáÈâÈãÈäÈåÈæÈçÈèÈéÈêÈëÈìÈíÈîÈïÈğÈñÈòÈóÈôÈõÈöÈ÷ÈøÈùÈúÈûÈüÈıÈş");
		buf.append("¡¡£¡'£¬£®£¯£º£»£¿£Ş£ß£à£ü£ş¡¢¡£¡¤¡¥¡¦¡§¡¨¡©¡ª¡«¡¬¢¦¢¥?¢§¢¨¢©¢ª¢«¢¬¢­¢®¢¯¢°£¢£¨£©£Û£İ£û£ı¡®¡¯¡°¡±¡²¡³¡´¡µ¡¶¡·¡¸¡¹¡º¡»¡¼¡½+£­£¼=£¾¡¾¡¿¡À¡Á¡Â¡Ã¡Ä¡Å¡Î¡Ï¡Ğ¡Ñ¡Ò¡Ó¡Ô¡Õ¡Ö¡ì¡í¡î¡ï¡ğ¡ñ¡ò¡ó¡ô¡õ¡ö¡÷¡ø¡ù¡ú¡û¡ü¡ı¡ş¢¡¢¢¢£¢¤¢±¢²¢³£¤£¥£Ü£Æ¡Ç¡È¡É¡Ê¡Ë¡Ì¡Í¢´¢µ¢¶§¡§¢§£§¤§¥§¦§§§¨§©§ª§«§¬§­§®§¯§°§±§²§³§´§µ§¶§·§¸§¹§º§»§¼§½§¾§¿§À§Â§Ã§Ä§Å§Æ§Ç§È§É§Ê§Ë§Ì§Í§Î§Ï§Ğ§Ñ§Ò§Ó§Ô§Õ§Ö§×§Ø§Ù§Ú§Û§Ü§İ§Ş§ß§à§á§á§â§ã§ä§å§æ§ç§è§é§ê§ë§ì§í§î§ï£££¦£ª£À¡á¡Ø¡Ù¡Ú¡Û¡Ü¡İ¡Ş¡ß¡à¡á¡â¡ã¡ä¡å¡æ¡ç¡è¡é¡ê¡ë¢·¢¸¢¹¢º¢»¢¼¢½¢¾¢¿¢À¢Á¢Â¢Ã¢Ä¢Å¢Æ¢Ç¢È¢É¢Ê¢Ë¢Ì¢Í¢Î¢Ï¢Ğ¢Ñ¡á¢Ó¢Ô¢Õ¢Ö¢×¢Ø¢Ù¢Ú¢Û¢Ü¢İ¢Ş¢ß¢à¢á¢â¢ã¢ä¢å¡á¡á¦¡¦¢¦£¦¤¦¥¦¦¦§¦¨¦©¦ª¦«¦¬¦­¦®¦¯¦°¦±¦²¦³¦´¦µ¦¶¦·¦¸¦¹¦º¦»¦¼¦½¦¾¦¿¦À¦Á¦Â¦Ã¦Ä¦Å¦Æ¦Ç¦È¦É¦Ê¦Ë¦Ì¦Í¦Î¦Ï¦Ğ¦Ñ¦Ò¦Ó¦Ô¦Õ¦Ö¦×¦Ø¦Ù¦Ú¦Û¦Ü¦İ¦Ş¦ß¦à¦á¦â¦ã¦ä¨±¨²¨³¨´¨µ¨¶¨·¨¸¨¹¨º¨»¨¼¨½¨¾¨¿¨À¨Á¨Â¨Ã¨Ä¨Å¨Æ¨Ç¨È¨É¨Ê¨Ë¨Ì©±©²©³©´©µ©¶©·©¸©¹©º©»©¼©½©¾©¿©À©Á©Â©Ã©Ä©Å©Æ©Ç©È©É©Ê©Ë©Ì¨Í¨Î¨Ï¨Ğ¨Ñ¨Ò¨Ó¨Ô¨Õ¨Ö¨×¨Ø¨Ù¨Ú¨Û¨Ü¨İ¨Ş¨ß¨à¨á¨â¨ã¨ä¨å¨æ¨ç¨è¨é¨ê¨ë¨ì¨í¨î¨ï¨ğ¨ñ¨ò¨ó¨ô¨õ©Í©Î©Ï©Ğ©Ñ©Ò©Ó©Ô©Õ©Ö©×©Ø©Ù©Ú©Û©Ü©İ©Ş©ß©à©á©â©ã©ä©î©æ©ç©è©é©ê©ë©ì©í©å©ï©ğ©ñ©ò©ó©ô©õ¥¡¥¢¥£¥¤¥¥¥¦¥§¥¨¥©¥ª¥°¥±¥²¥³¥´¥µ¥¶¥·¥¸¥¹¨ö¨ø¨ø¨ù¨ú¨û¨ü¨ı¨ş©ö©÷©ø©ù©ú©û©ü©ı©ş¤¡¤¢¤£¤¤¤¥¤¦¤§¤¨¤©¤ª¤«¤¬¤­¤®¤¯¤°¤±¤²¤³¤´¤µ¤¶¤·¤¸¤¹¤º¤»¤¼¤½¤¾¤¿¤À¤Á¤Â¤Ã¤Ä¤Å¤Æ¤Ç¤È¤É¤Ê¤Ë¤Ì¤Í¤Î¤Ï¤Ğ¤Ñ¤Ò¤Ó¤Õ¤Ö¤×¤Ø¤Ù¤Ú¤Û¤Ü¤İ¤Ş¤ß¤à¤á¤â¤ã¤ä¤å¤æ¤ç¤è¤é¤ê¤ë¤ì¤í¤î¤ï¤ğ¤ñ¤ò¤ó¤ô¤õ¤ö¤÷¤ø¤ù¤ú¤û¤ü¤ı¤ş£Á£Â£Ã£Ä£Å£Æ£Ç£È£É£Ê£Ë£Ì£Í£Î£Ï£Ğ£Ñ£Ò£Ó£Ô£Õ£Ö£×£Ø£Ù£Ú£á£â£ã£ä£å£æ£ç£è£é£ê£ë£ì£í£î£ï£ğ£ñ£ò£ó£ô£õ£ö£÷£ø£ù£ú¤½¥Á¥Â¥Ã¥Ä¥Å¥Æ¥Ç¥È¥É¥Ê¥Ë¥Ì¥Í¥Î¥Ï¥Ğ¥Ñ¥Ò¥Ó¥Ô¥Õ¥Ö¥×¥Ø¥á¥â¥ã¥ä¥å¥æ¥ç¥è¥é¥ê¥ë¥ì¥í¥î¥ï¥ğ¥ñ¥ò¥ó¥ô¥õ¥ö¥÷¥ø");
		buf.append("¤¡¤¢¤¤¤§¤¨¤©¤±¤²¤³¤µ¤¶¤·¤¸¤¹¤º¤»¤¼¤½¤¾");
		buf.append("¤¿¤Á¤Ã¤Å¤Ç¤Ë¤Ì¤Ğ¤Ñ¤Ó");
		buf.append("«¡«£«¥«§«©«¢«¤«¦«¨«ª«««­«¯«±«³«¬«®«°«²«´«µ«·«¹«»«½«¶«¸«º«¼«¾«¿«Á«Ã«Ä«Æ«È«À«Â«Å«Ç«É«Ê«Ë«Ì«Í«Î«Ï«Ò«Õ«Ø«Û«Ğ«Ó«Ö«Ù«Ü«Ñ«Ô«×«Ú«İ«Ş«ß«à«á«â«ã«ä«å«æ«ç«è«é«ê«ë«ì«í«î«ï«ğ«ñ«ò«ó«ô«õ«ö");
		buf.append("ª¡ª£ª¥ª§ª©ª¢ª¤ª¦ª¨ªªª«ª­ª¯ª±ª³ª¬ª®ª°ª²ª´ªµª·ª¹ª»ª½ª¶ª¸ªºª¼ª¾ª¿ªÁªÃªÄªÆªÈªÀªÂªÅªÇªÉªÊªËªÌªÍªÎªÏªÒªÕªØªÛªĞªÓªÖªÙªÜªÑªÔª×ªÚªİªŞªßªàªáªâªãªäªåªæªçªèªéªêªëªìªíªîªïªğªñªòªó");
		String str = buf.toString();
		int count = message.length();
		char chr;
		
		String rslt = null;
		
		for (int i = 0; i < count; i++) {
			
			chr = message.charAt(i);
			
			if ( (int)message.charAt(i) > 127) {
					if ( str.indexOf( chr ) < 0 ) {
						Character cr = new Character(chr);
						rslt = cr.toString();
						break;
					}
			}
		}
		return rslt;
	}
	
	
	private int getSMSLogKey(Connection conn) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("getSMSLogInsertKey") );
		return pq.ExecuteQueryNum();
				
	}

	
	
	
	private void insertSMSClientPqSetter(PreparedExecuteQueryManager pq, SMSClientVO vo) {
		
		pq.setString(1, vo.getTR_SENDDATE());
		pq.setString(2, vo.getTR_ID());
		pq.setString(3, vo.getTR_PHONE());
		pq.setString(4, vo.getTR_CALLBACK());
		pq.setString(5, vo.getTR_MSG());
		pq.setString(6, vo.getTR_ETC1());
		pq.setString(7, vo.getTR_ETC2());
		pq.setString(8, vo.getTR_ETC3());
		pq.setString(9, vo.getTR_ETC4());
		pq.setString(10, vo.getTR_ETC5());
		pq.setString(11, vo.getTR_ETC6());
		//TR_SENDSTAT, TR_RSLTSTAT, TR_RSLTDATE, TR_MODIFIED
		pq.setString(12, "0");
		pq.setString(13, "00");
		pq.setString(14, vo.getTR_SENDDATE());
		pq.setString(15, vo.getTR_SENDDATE());
	}
	
	private void insertSMSClientPqSetterSK(PreparedExecuteQueryManager pq, SMSClientVO vo, int num) {
		
		pq.setString(1, vo.getTR_SENDDATE());
		pq.setString(2, vo.getTR_ID());
		pq.setString(3, vo.getTR_PHONE());
		pq.setString(4, vo.getTR_CALLBACK());
		pq.setString(5, vo.getTR_MSG());
		pq.setString(6, vo.getTR_ETC1());
		pq.setString(7, vo.getTR_ETC2());
		pq.setString(8, vo.getTR_ETC3());
		pq.setString(9, vo.getTR_ETC4());
		pq.setString(10, vo.getTR_ETC5());
		pq.setString(11, vo.getTR_ETC6());
		//TR_SENDSTAT, TR_RSLTSTAT, TR_RSLTDATE, TR_MODIFIED
		pq.setString(12, "0");
		pq.setString(13, "00");
		pq.setString(14, vo.getTR_SENDDATE());
		pq.setString(15, vo.getTR_SENDDATE());
		//pq.setString(16, vo.getTR_ETC6()+Integer.toString(num));
	}
	
	private void insertMMSClientPqSetterSK(PreparedExecuteQueryManager pq, SMSClientVO vo, int num, String imagePath) {
		
		int cnt = imagePath.split(";").length;
		String type = "text/plain;";
		for (int i = 0; i < cnt; i++) {
			type +="image/jpg;";
		}
		pq.setString(1, vo.getTR_SENDDATE());
		pq.setString(2, vo.getTR_ID());
		pq.setString(3, vo.getTR_PHONE());
		pq.setString(4, vo.getTR_CALLBACK());
		pq.setString(5, vo.getTR_MSG());
		pq.setString(6, vo.getTR_ETC1());
		pq.setString(7, vo.getTR_ETC2());
		pq.setString(8, vo.getTR_ETC3());
		pq.setString(9, vo.getTR_ETC4());
		pq.setString(10, vo.getTR_ETC5());
		pq.setString(11, vo.getTR_ETC6());
		//TR_SENDSTAT, TR_RSLTSTAT, TR_RSLTDATE, TR_MODIFIED
		pq.setString(12, "0");
		pq.setString(13, "00");
		pq.setString(14, vo.getTR_SENDDATE());
		pq.setString(15, vo.getTR_SENDDATE());
		pq.setInt(16, cnt+1);
		pq.setString(17, type);
		pq.setString(18, imagePath);
		//pq.setString(16, vo.getTR_ETC6()+Integer.toString(num));
	}
	
	private void insertSMSClientPqSetter_fail(PreparedExecuteQueryManager pq, SMSClientVO vo, String state, String code) {
		
		pq.setString(1, vo.getTR_SENDDATE());
		pq.setString(2, vo.getTR_ID());
		pq.setString(3, vo.getTR_PHONE());
		pq.setString(4, vo.getTR_CALLBACK());
		pq.setString(5, vo.getTR_MSG());
		pq.setString(6, vo.getTR_ETC1());
		pq.setString(7, vo.getTR_ETC2());
		pq.setString(8, vo.getTR_ETC3());
		pq.setString(9, vo.getTR_ETC4());
		pq.setString(10, vo.getTR_ETC5());
		pq.setString(11, vo.getTR_ETC6());
		//TR_SENDSTAT, TR_RSLTSTAT, TR_RSLTDATE, TR_MODIFIED
		pq.setString(12, state);
		pq.setString(13, code);
		pq.setString(14, vo.getTR_SENDDATE());
		pq.setString(15, vo.getTR_SENDDATE());
	}
	
	private void insertSMSClientPqSetter_failSK(PreparedExecuteQueryManager pq, SMSClientVO vo, String state, String code, int num) {
		
		pq.setString(1, vo.getTR_SENDDATE());
		pq.setString(2, vo.getTR_ID());
		pq.setString(3, vo.getTR_PHONE());
		pq.setString(4, vo.getTR_CALLBACK());
		pq.setString(5, vo.getTR_MSG());
		pq.setString(6, vo.getTR_ETC1());
		pq.setString(7, vo.getTR_ETC2());
		pq.setString(8, vo.getTR_ETC3());
		pq.setString(9, vo.getTR_ETC4());
		pq.setString(10, vo.getTR_ETC5());
		pq.setString(11, vo.getTR_ETC6());
		//TR_SENDSTAT, TR_RSLTSTAT, TR_RSLTDATE, TR_MODIFIED
		pq.setString(12, state);
		pq.setString(13, code);
		pq.setString(14, vo.getTR_SENDDATE());
		pq.setString(15, vo.getTR_SENDDATE());
		//pq.setString(16, vo.getTR_ETC6()+Integer.toString(num));
	}
	
	private void insertMMSClientPqSetter_failSK(PreparedExecuteQueryManager pq, SMSClientVO vo, String state, String code, int num, String imagePath) {
		
		int cnt = imagePath.split(";").length;
		String type = "text/plain;";
		for (int i = 0; i < cnt; i++) {
			type +="image/jpg;";
		}
		
		pq.setString(1, vo.getTR_SENDDATE());
		pq.setString(2, vo.getTR_ID());
		pq.setString(3, vo.getTR_PHONE());
		pq.setString(4, vo.getTR_CALLBACK());
		pq.setString(5, vo.getTR_MSG());
		pq.setString(6, vo.getTR_ETC1());
		pq.setString(7, vo.getTR_ETC2());
		pq.setString(8, vo.getTR_ETC3());
		pq.setString(9, vo.getTR_ETC4());
		pq.setString(10, vo.getTR_ETC5());
		pq.setString(11, vo.getTR_ETC6());
		//TR_SENDSTAT, TR_RSLTSTAT, TR_RSLTDATE, TR_MODIFIED
		pq.setString(12, state);
		pq.setString(13, code);
		pq.setString(14, vo.getTR_SENDDATE());
		pq.setString(15, vo.getTR_SENDDATE());
		pq.setInt(16, imagePath.split(";").length+1);
		pq.setString(17, type);
		pq.setString(18, imagePath);
		//pq.setString(16, vo.getTR_ETC6()+Integer.toString(num));
	}
	
	public ArrayList<String[]> getPhone(Connection conn, String user_id, ArrayList<PhoneListVO> al) {
		
		VbyP.debugLog(" >> getPhone -> start");
		ArrayList<String[]> result  = new ArrayList<String[]>();
		int count = al.size();
		
		if (count > 0) {
			
			PhoneListVO pvo = null;
			String [] temp = null;
			StringBuffer groupKeyBuf = new StringBuffer();
			VbyP.debugLog(" >> getPhone -> loop start");
			for (int i = 0; i < count; i++) {
				
				temp = new String[2];
				pvo = al.get(i);
				
				if ( pvo.getPhoneSection() == PhoneListVO.SECTION_GROUP ) {
					
					groupKeyBuf.append( "'"+ pvo.getPhoneName() + "',");
				}else {
					
					temp[0] = (pvo.getPhoneNumber() != null )?SLibrary.replaceAll(pvo.getPhoneNumber(), "-", ""):"";
					temp[1] = pvo.getPhoneName();
					if ( !SLibrary.isNull(temp[0]) )
						result.add(temp);
				}					
			}
			VbyP.accessLog(user_id+" >> Àü¼Û ¿äÃ» : ÀüÈ­¹øÈ£ " + Integer.toString(result.size()) + "°Ç È®ÀÎ! ");
			VbyP.debugLog(" >> getPhone -> loop end");
			
			if (groupKeyBuf.length() > 0) {
				
				VbyP.accessLog(user_id+" >> Àü¼Û ¿äÃ» : ÁÖ¼Ò·Ï( " + groupKeyBuf.toString() + ") ±×·ì °¡Á®¿À±â ");
				// remove last `,`
				groupKeyBuf.setLength(groupKeyBuf.length()-1);
				getPhoneOfAddressGroup( conn, result, user_id, groupKeyBuf.toString());
				
			}
			VbyP.accessLog(user_id+" >> Àü¼Û ¿äÃ» : ÀüÃ¼ ÀüÈ­¹øÈ£°Ç¼ö  " + Integer.toString(result.size()) + "°Ç ");
			
		}
		
		
		return result;
	}
	
	
	private ArrayList<HashMap<String, String>> getPhoneOfAddressGroup( Connection conn, ArrayList<String[]> alPointer, String user_id, String inKey) {
		
		VbyP.debugLog(" >> getPhoneOfAddressGroup -> start");
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.messageFormat(VbyP.getSQL("getPhoneOfAddressGroup"), new Object[]{inKey}) );
		pq.setString(1, user_id);
		
		ArrayList<HashMap<String, String>> rsltAl = pq.ExecuteQueryArrayList();
		
		VbyP.debugLog(" >> getPhoneOfAddressGroup -> ArrayList from DB");
		int count = rsltAl.size();
		HashMap<String, String> hm = null;
		
		for (int i = 0; i < count; i++) {
			
			hm = rsltAl.get(i);
			if ( !SLibrary.isNull( SLibrary.IfNull(hm, "phone") ) )
				alPointer.add(new String[]{SLibrary.IfNull(hm, "phone"), SLibrary.IfNull(hm, "name")});
		}
		
		
		return rsltAl;
		
	}
}
