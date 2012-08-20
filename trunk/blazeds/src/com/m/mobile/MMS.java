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

public class MMS implements MMSAble {
	
	static MMS mms = new MMS();
	
	public static MMS getInstance() {
		return mms;
	}
	private MMS(){};
	
	public LogVO getLogVOMearge( UserInformationVO mvo, Boolean bReservation, String message, String phone, String returnPhone, String reservationDate, int count, String ip) throws Exception{
		return null;
	}
	
	@Override
	public int insertClient(Connection conn, MMSClientVO vo) {

		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("insertMMSClient") );
				
		insertClientPqSetter(pq, vo);
		
		return pq.executeUpdate();
	}

	@Override
	public int insertLMSLog(Connection conn, LogVO vo, int year, int month) {

		
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
	public int insertMMSLog(Connection conn, LogVO vo, int year, int month) {

		
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
	public int insertClient(Connection connMMS, ArrayList<MMSClientVO> al, String via) {

		String sql = "";
		int resultCount = 0;
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		if (via.equals("ktmms")) sql =  VbyP.getSQL("insertMMSClientKT");
		else  sql = VbyP.getSQL("insertMMSClient") ;
		
		pq.setPrepared( connMMS, sql );
		
		int count = al.size();
		MMSClientVO vo = null;
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
				
				vo = new MMSClientVO();
				vo = al.get(i);
				//¼ö½Å°ÅºÎ
				if (Refuse.isRefuse(hashTable_refuse, vo.PHONE)){
					
					insertClientPqSetter_fail(pq, vo, "98");
					
				}else if (hashTable.containsKey(vo.PHONE)){
					insertClientPqSetter_fail(pq, vo, "99"); 	
				}else {
					hashTable.put(vo.PHONE, "");
					insertClientPqSetter(pq, vo);
				}
				
				pq.addBatch();
				
				//¹ß¼ÛÄ«¿îÆ®
				M.setState(vo.ID, i+1);
				
				if (i >= maxBatch && (i%maxBatch) == 0 ) {
					
					System.out.println(i + " reDBConnection !");
					resultCount += pq.executeBatch();
					
					try { if ( connMMS != null ) connMMS.close(); } 
					catch(Exception e) {System.out.println( "connMMS close Error!!!!" + e.toString());}
					
					connMMS = VbyP.getDB(via);					
					if (connMMS != null) System.out.println("connMMS connection!!!!");
					
					pq.setPrepared( connMMS, sql );
				}
				
			}
			resultCount += pq.executeBatch();
		}

		return resultCount;
	}
	
	@Override
	public int insertClientRefuse(Connection connMMS, ArrayList<MMSClientVO> al, String via) {

		
		int resultCount = 0;
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( connMMS, VbyP.getSQL("insertMMSClient") );
		
		int count = al.size();
		MMSClientVO vo = null;
		int maxBatch = SLibrary.parseInt( VbyP.getValue("executeBatchCount") );
		
		Hashtable<String, String> hashTable = new Hashtable<String, String>();
		Hashtable<String, String> hashTable_refuse = null;		
		
		if (count > 0) {
			
			hashTable_refuse = Refuse.getRefusePhoneFromDB(al.get(0).getID());

			for (int i = 0; i < count; i++) {
				
				vo = new MMSClientVO();
				vo = al.get(i);
				
				//¼ö½Å°ÅºÎ
				if (Refuse.isRefuse(hashTable_refuse, vo.ID)){
					insertClientPqSetter_fail(pq, vo, "98");
				}					
				//Áßº¹ ½ÇÆĞ
				else if (hashTable.containsKey(vo.PHONE)){
					insertClientPqSetter_fail(pq, vo, "99"); 	
				}else {
					hashTable.put(vo.PHONE, "");
					insertClientPqSetter(pq, vo);
				}
				
				pq.addBatch();								
				//¹ß¼ÛÄ«¿îÆ®
				M.setState(vo.ID, i+1);
				
				if (i >= maxBatch && (i%maxBatch) == 0 ) {
					
					System.out.println(i + " reDBConnection !");
					resultCount += pq.executeBatch();
					
					try { if ( connMMS != null ) connMMS.close(); }
					catch(Exception e) {System.out.println( "connMMS close Error!!!!" + e.toString());}
					
					connMMS = VbyP.getDB(via);					
					if (connMMS != null) System.out.println("connMMS connection!!!!");
					pq.setPrepared( connMMS, VbyP.getSQL("insertMMSClient") );
				}
				
			}
			resultCount += pq.executeBatch();			
		}		

		return resultCount;
	}
	
	@Override
	public int sendLMSPointPut(Connection conn, UserInformationVO mvo, int cnt) {

		
		PointManager pm = PointManager.getInstance();		
		return pm.insertUserPoint(conn, mvo, 41, cnt * MMS.LMS_POINT_COUNT * PointManager.DEFULT_POINT);
	}
	
	public int sendMMSPointPut(Connection conn, UserInformationVO mvo, int cnt) {

		
		PointManager pm = PointManager.getInstance();		
		return pm.insertUserPoint(conn, mvo, 21, cnt * MMS.MMS_POINT_COUNT * PointManager.DEFULT_POINT);
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
		vo.setTranType("LM");
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
	
	public LogVO getMMSLogVO( UserInformationVO mvo, Boolean bReservation, String message, ArrayList<String[]> phoneAndNameArrayList, String returnPhone, String reservationDate, String ip) throws Exception{
		
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
		vo.setTranType("MM");
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
	
	
	
	public ArrayList<MMSClientVO> getClientVO( Connection conn, UserInformationVO mvo, Boolean bReservation, int MMSLogKey, String message, ArrayList<String[]> phoneAndNameArrayList, String returnPhone, String reservationDate, String imagePath, String ip) throws Exception{
		
		ArrayList<MMSClientVO> clientAl = new ArrayList<MMSClientVO>();
		MMSClientVO vo = new MMSClientVO();
		String [] temp = null;
		
		VbyP.debugLog(" >> getPhone");
		int count = phoneAndNameArrayList.size();
		if (count < 0)
			throw new Exception("ÀüÈ­¹øÈ£ ¸®½ºÆ®°¡ ¾ø½À´Ï´Ù.");				
		
		for (int i = 0; i < count; i++) {
			
			vo = new MMSClientVO();
			temp = phoneAndNameArrayList.get(i);
			
			vo.setSUBJECT( (message.length() > 20)? message.substring(0,20) : message );
			vo.setPHONE((temp.length > 0)? SLibrary.IfNull(temp[0]):"");
			vo.setCALLBACK( returnPhone );
			vo.setSTATUS( CLIENT_SENDSTAT );
			vo.setREQDATE( reservationDate );
			vo.setMSG( message );
			vo.setFILE_CNT( (SLibrary.isNull(imagePath))? 0: 1 );
			vo.setFILE_CNT_REAL( (SLibrary.isNull(imagePath))? 0: 1 );
			vo.setFILE_PATH1( (SLibrary.isNull(imagePath))? "": imagePath );			
			vo.setTYPE( CLIENT_MESSAGETYPE );
			vo.setID( mvo.getUser_id() );
			vo.setPOST( Integer.toString(MMSLogKey) );
			vo.setETC1( (temp.length == 2)?SLibrary.IfNull(temp[1]):""  );
			vo.setETC2( ip );
			vo.setETC3( " " );
						
			clientAl.add(vo);
		}
		VbyP.debugLog(" >> getPhone -> loop");
		return clientAl;
	}
	
	public ArrayList<MMSClientVO> getMMSClientVOMeargeAndInterval( Connection conn, UserInformationVO mvo, Boolean bReservation, int MMSLogKey, String message, ArrayList<String[]> phoneAndNameArrayList, String returnPhone, String reservationDate, String imagePath, String ip, int cnt, int minute, boolean bMerge) throws Exception{
		
		ArrayList<MMSClientVO> clientAl = new ArrayList<MMSClientVO>();
		MMSClientVO vo = new MMSClientVO();
		String [] temp = null;
		boolean bInterval = false;
		String name = "";
		
		VbyP.debugLog(" >> getPhone");
		int count = phoneAndNameArrayList.size();
		if (count < 0)
			throw new Exception("ÀüÈ­¹øÈ£ ¸®½ºÆ®°¡ ¾ø½À´Ï´Ù.");	
		
		if (cnt > 0 && minute > 0) bInterval = true;
		
		for (int i = 0; i < count; i++) {
			
			vo = new MMSClientVO();
			temp = phoneAndNameArrayList.get(i);
			
			name = (temp.length == 2)?SLibrary.IfNull(temp[1]):"";
			
			if (bInterval &&  i != 0 && (i+1)%cnt == 0) {
				reservationDate = SLibrary.getDateAddSecond(reservationDate, minute*60);
			}
			
			vo.setSUBJECT( (message.length() > 20)? message.substring(0,20) : message );
			vo.setPHONE((temp.length > 0)? SLibrary.IfNull(temp[0]):"");
			vo.setCALLBACK( returnPhone );
			vo.setSTATUS( CLIENT_SENDSTAT );
			vo.setREQDATE( reservationDate );
			vo.setMSG( bMerge ? SLibrary.replaceAll(message, "{ÀÌ¸§}", name ):message );
			vo.setFILE_CNT( (SLibrary.isNull(imagePath))? 0: 1 );
			vo.setFILE_CNT_REAL( (SLibrary.isNull(imagePath))? 0: 1 );
			vo.setFILE_PATH1( (SLibrary.isNull(imagePath))? "": imagePath );			
			vo.setTYPE( CLIENT_MESSAGETYPE );
			vo.setID( mvo.getUser_id() );
			vo.setPOST( Integer.toString(MMSLogKey) );
			vo.setETC1( name  );
			vo.setETC2( ip );
			vo.setETC3( " " );
						
			clientAl.add(vo);
		}
		VbyP.debugLog(" >> getPhone -> loop");
		return clientAl;
	}
	
	public ArrayList<MMSClientVO> getMMSClientVOMeargeAndIntervalKT( Connection conn, UserInformationVO mvo, Boolean bReservation, int MMSLogKey, String message, ArrayList<String[]> phoneAndNameArrayList, String returnPhone, String reservationDate, String imagePath, String ip, int cnt, int minute, boolean bMerge) throws Exception{
		
		ArrayList<MMSClientVO> clientAl = new ArrayList<MMSClientVO>();
		MMSClientVO vo = new MMSClientVO();
		String [] temp = null;
		boolean bInterval = false;
		String name = "";
		String image = "";
		
		VbyP.debugLog(" >> getPhone");
		int count = phoneAndNameArrayList.size();
		if (count < 0)
			throw new Exception("ÀüÈ­¹øÈ£ ¸®½ºÆ®°¡ ¾ø½À´Ï´Ù.");	
		
		String [] arrImage = imagePath.split(";");
		int imgCnt = arrImage.length;
		if (arrImage != null && imgCnt > 0) {
			for (int i = 0; i < imgCnt; i++) {
				image += findFileName(arrImage[i])+"^1^"+Integer.toString(i)+"|";
			}
		}
		
		if (image.length() > 0) {
			image = image.substring(0, image.length()-1);
			System.out.println("#### KT MMS : "+image);
		}

		
		if (cnt > 0 && minute > 0) bInterval = true;
		
		for (int i = 0; i < count; i++) {
			
			vo = new MMSClientVO();
			temp = phoneAndNameArrayList.get(i);
			
			name = (temp.length == 2)?SLibrary.IfNull(temp[1]):"";
			
			if (bInterval &&  i != 0 && (i+1)%cnt == 0) {
				reservationDate = SLibrary.getDateAddSecond(reservationDate, minute*60);
			}
			
			vo.setSUBJECT( (message.length() > 20)? message.substring(0,20) : message );
			vo.setPHONE((temp.length > 0)? SLibrary.IfNull(temp[0]):"");
			vo.setCALLBACK( returnPhone );
			vo.setSTATUS( CLIENT_SENDSTAT );
			vo.setREQDATE( reservationDate );
			vo.setMSG( bMerge ? SLibrary.replaceAll(message, "{ÀÌ¸§}", name ):message );
			vo.setFILE_CNT( imgCnt );
			vo.setFILE_CNT_REAL( imgCnt );
			vo.setFILE_PATH1( image );			
			vo.setTYPE( CLIENT_MESSAGETYPE );
			vo.setID( mvo.getUser_id() );
			vo.setPOST( Integer.toString(MMSLogKey) );
			vo.setETC1( name  );
			vo.setETC2( ip );
			vo.setETC3( " " );
						
			clientAl.add(vo);
		}
		VbyP.debugLog(" >> getPhone -> loop");
		return clientAl;
	}
	
	private String findFileName(String path) {
		
		String rslt = "";
		if (path != null) {
			String temp[] = path.split(System.getProperty("file.separator"));
			rslt = temp[temp.length-1].toString();
		}
		return rslt;
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
	
	private void createMMSLogTable(Connection conn, int year, int month) {
		
		String logTableMonth = Integer.toString(year)+SLibrary.addTwoSizeNumber(month);
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.messageFormat( VbyP.getSQL("infoMMSLogTable"), new Object[]{Integer.toString(year)+SLibrary.addTwoSizeNumber(month)}) );
		
		if ( pq.ExecuteQueryString() == null ) {
			
			pq.setPrepared( conn, SLibrary.messageFormat( VbyP.getSQL("createPointLogTable"), new Object[]{logTableMonth}) );
			int rslt = pq.executeUpdate();
			
			if (rslt == 1)
				VbyP.accessLog("create MMSLog Table ....."+logTableMonth);
		}
	}
	
	private void createLMSLogTable(Connection conn, int year, int month) {
		
		String logTableMonth = Integer.toString(year)+SLibrary.addTwoSizeNumber(month);
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.messageFormat( VbyP.getSQL("infoLMSLogTable"), new Object[]{Integer.toString(year)+SLibrary.addTwoSizeNumber(month)}) );
		
		if ( pq.ExecuteQueryString() == null ) {
			
			pq.setPrepared( conn, SLibrary.messageFormat( VbyP.getSQL("createPointLogTable"), new Object[]{logTableMonth}) );
			int rslt = pq.executeUpdate();
			
			if (rslt == 1)
				VbyP.accessLog("create LMSLog Table ....."+logTableMonth);
		}
	}
	
	
	
	private int getLMSLogKey(Connection conn) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("getLogInsertKey") );
		return pq.ExecuteQueryNum();
				
	}
	
	private int getMMSLogKey(Connection conn) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("getLogInsertKey") );
		return pq.ExecuteQueryNum();
				
	}
	
	private String getPayTypeCode(String pay_type) {
		
		String result = "";
		if (SLibrary.IfNull(pay_type).equals("later"))
			result = "L";
		else if (SLibrary.IfNull(pay_type).equals("fixed"))
			result = "F";
		else
			result = "C";
		
		return result;
	}
	
	
	
	private void insertClientPqSetter(PreparedExecuteQueryManager pq, MMSClientVO vo) {
		
		pq.setString(1, vo.getSUBJECT() );
		pq.setString(2, vo.getPHONE() );
		pq.setString(3, vo.getCALLBACK() );
		pq.setString(4, vo.getSTATUS() );
		pq.setString(5, vo.getREQDATE() );
		pq.setString(6, vo.getMSG() );
		pq.setInt(7, vo.getFILE_CNT() );
		pq.setInt(8, vo.getFILE_CNT_REAL() );
		pq.setString(9, vo.getFILE_PATH1() );			
		pq.setString(10, vo.getTYPE() );
		pq.setString(11, vo.getID() );
		pq.setString(12, vo.getPOST() );
		pq.setString(13, vo.getETC1() );
		pq.setString(14, vo.getETC2() );
		pq.setString(15, vo.getETC3() );
		pq.setString(16, "0");
	}
	
	private void insertClientPqSetter_fail(PreparedExecuteQueryManager pq, MMSClientVO vo, String code) {
		
		pq.setString(1, vo.getSUBJECT() );
		pq.setString(2, vo.getPHONE() );
		pq.setString(3, vo.getCALLBACK() );
		pq.setString(4, "3" );
		pq.setString(5, vo.getREQDATE() );
		pq.setString(6, vo.getMSG() );
		pq.setInt(7, vo.getFILE_CNT() );
		pq.setInt(8, vo.getFILE_CNT_REAL() );
		pq.setString(9, vo.getFILE_PATH1() );			
		pq.setString(10, vo.getTYPE() );
		pq.setString(11, vo.getID() );
		pq.setString(12, vo.getPOST() );
		pq.setString(13, vo.getETC1() );
		pq.setString(14, vo.getETC2() );
		pq.setString(15, vo.getETC3() );
		pq.setString(16, code);
		
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
	
	private int getSMSLogKey(Connection conn) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("getSMSLogInsertKey") );
		return pq.ExecuteQueryNum();
				
	}

}
