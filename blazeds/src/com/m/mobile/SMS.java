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
				
				//���Űź�
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
				
				//�߼�ī��Ʈ
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
				
				//���Űź�
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
				
				//�߼�ī��Ʈ
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
	
	// sk mms �߼�
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
				
				//���Űź�
				if (Refuse.isRefuse(hashTable_refuse, vo.TR_PHONE)){
					insertMMSClientPqSetter_failSK(pq, vo,resultStateCode, "98", i, imagePath);
				} else if (hashTable.containsKey(vo.TR_PHONE)){
					insertMMSClientPqSetter_failSK(pq, vo,resultStateCode, "99", i, imagePath);
				}else {
					hashTable.put(vo.TR_PHONE, "");
					insertMMSClientPqSetterSK(pq, vo, i, imagePath);
				}
				
				pq.addBatch();
				
				//�߼�ī��Ʈ
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
				//���Űź�
				if (Refuse.isRefuse(hashTable_refuse, vo.TR_PHONE)){
					insertSMSClientPqSetter_fail(pq, vo,resultStateCode, "98");
				}					
				//�ߺ� ����
				else if (hashTable.containsKey(vo.TR_PHONE)){
					insertSMSClientPqSetter_fail(pq, vo,resultStateCode, "99");
				}else {
					hashTable.put(vo.TR_PHONE, "");
					insertSMSClientPqSetter(pq, vo);
				}
				
				pq.addBatch();								
				//�߼�ī��Ʈ
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
			throw new Exception("��ȣ��ȣ ����Ʈ�� �����ϴ�.");
		
		String [] temp = phoneAndNameArrayList.get(0);
		
		if ( temp.length < 1 || SLibrary.isNull(temp[0]))
			throw new Exception("��ȭ��ȣ ����Ʈ�� �����ϴ�.2");
			
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
			throw new Exception("��ȭ��ȣ ����Ʈ�� �����ϴ�.");				
		
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
			throw new Exception("��ȭ��ȣ ����Ʈ�� �����ϴ�.");				
		
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
			vo.setTR_MSG( bMerge ? SLibrary.replaceAll(message, "{�̸�}", name ):message  );
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
	 * ����� ������ �ѱ� �˻�
	 * @param message
	 * @return
	 */
	public String isMessage(String message) {
		
		StringBuffer buf = new StringBuffer();
		buf.append("�������������������������������������������������������������������°ðİŰưǰȰɰʰ˰̰ͰΰϰаѰҰӰ԰հְװذٰڰ۰ܰݰް߰������������������������������������������������������������������������������������������������������������±ñıűƱǱȱɱʱ˱̱ͱαϱбѱұӱԱձֱױرٱڱ۱ܱݱޱ߱������������������������������������������������������������������������������������������������������������²òĲŲƲǲȲɲʲ˲̲ͲβϲвѲҲӲԲղֲײزٲڲ۲ܲݲ޲߲������������������������������������������������������������������������������������������������������������³óĳųƳǳȳɳʳ˳̳ͳγϳгѳҳӳԳճֳ׳سٳڳ۳ܳݳ޳߳������������������������������������������������������������������������������������������������������������´ôĴŴƴǴȴɴʴ˴̴ʹδϴдѴҴӴԴմִ״شٴڴ۴ܴݴ޴ߴ������������������������������������������������������������������������������������������������������������µõĵŵƵǵȵɵʵ˵̵͵εϵеѵҵӵԵյֵ׵صٵڵ۵ܵݵ޵ߵ������������������������������������������������������������������������������������������������������������¶öĶŶƶǶȶɶʶ˶̶Ͷζ϶жѶҶӶԶնֶ׶ضٶڶ۶ܶݶ޶߶������������������������������������������������������������������������������������������������������������·÷ķŷƷǷȷɷʷ˷̷ͷηϷзѷҷӷԷշַ׷طٷڷ۷ܷݷ޷߷������������������������������������������������������������������������������������������������������������¸øĸŸƸǸȸɸʸ˸̸͸θϸиѸҸӸԸոָ׸ظٸڸ۸ܸݸ޸߸������������������������������������������������������������������������������������������������������������¹ùĹŹƹǹȹɹʹ˹̹͹ιϹйѹҹӹԹչֹ׹عٹڹ۹ܹݹ޹߹������������������������������������������������������������������������������������������������������������ºúĺźƺǺȺɺʺ˺̺ͺκϺкѺҺӺԺպֺ׺غٺںۺܺݺ޺ߺ������������������������������������������������������������������������������������������������������������»ûĻŻƻǻȻɻʻ˻̻ͻλϻлѻһӻԻջֻ׻ػٻڻۻܻݻ޻߻������������������������������������������������������������������������������������������������������������¼üļżƼǼȼɼʼ˼̼ͼμϼмѼҼӼԼռּ׼ؼټڼۼܼݼ޼߼������������������������������������������������������������������������������������������������������������½ýĽŽƽǽȽɽʽ˽̽ͽνϽнѽҽӽԽսֽ׽ؽٽڽ۽ܽݽ޽߽������������������������������������������������������������������������������������������������������������¾þľžƾǾȾɾʾ˾̾;ξϾоѾҾӾԾվ־׾ؾپھ۾ܾݾ޾߾������������������������������������������������������������������������������������������������������������¿ÿĿſƿǿȿɿʿ˿̿ͿοϿпѿҿӿԿտֿ׿ؿٿڿۿܿݿ޿߿���������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿������������������������������������������������������������������������������������������������������������������������������áâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ������������������������������������������������������������������������������������������������������������������������������ġĢģĤĥĦħĨĩĪīĬĭĮįİıĲĳĴĵĶķĸĹĺĻļĽľĿ������������������������������������������������������������������������������������������������������������������������������šŢţŤťŦŧŨũŪūŬŭŮůŰűŲųŴŵŶŷŸŹźŻżŽžſ������������������������������������������������������������������������������������������������������������������������������ơƢƣƤƥƦƧƨƩƪƫƬƭƮƯưƱƲƳƴƵƶƷƸƹƺƻƼƽƾƿ������������������������������������������������������������������������������������������������������������������������������ǡǢǣǤǥǦǧǨǩǪǫǬǭǮǯǰǱǲǳǴǵǶǷǸǹǺǻǼǽǾǿ������������������������������������������������������������������������������������������������������������������������������ȡȢȣȤȥȦȧȨȩȪȫȬȭȮȯȰȱȲȳȴȵȶȷȸȹȺȻȼȽȾȿ������������������������������������������������������������������������������������������������������������������������������");
		buf.append("����'�������������ޣߣ������������������������������?���������������������������ۣݣ�����������������������������������+����=�����������¡áġšΡϡСѡҡӡԡա֡������������������������������������������������ܣơǡȡɡʡˡ̡͢����������������������������������������������������������������������§çħŧƧǧȧɧʧ˧̧ͧΧϧЧѧҧӧԧէ֧קا٧ڧۧܧݧާߧ������������������������ء١ڡۡܡݡޡߡ�����������뢷���������������������¢âĢŢƢǢȢɢʢˢ̢͢΢ϢТѡ�ӢԢբ֢עآ٢ڢۢܢݢޢߢ�������ᦡ�����������������������������������������������������������������¦æĦŦƦǦȦɦʦ˦̦ͦΦϦЦѦҦӦԦզ֦צئ٦ڦۦܦݦަߦ����䨱���������������������������������¨èĨŨƨǨȨɨʨ˨̩����������������������������������©éĩũƩǩȩɩʩ˩̨ͨΨϨШѨҨӨԨը֨רب٨ڨۨܨݨިߨ������������������������ͩΩϩЩѩҩөԩթ֩שة٩ک۩ܩݩީߩ����������������������������������������������������������������������������������������������������������������������������������������������������������������������¤äĤŤƤǤȤɤʤˤ̤ͤΤϤФѤҤӤդ֤פؤ٤ڤۤܤݤޤߤ��������������������������������������������£ãģţƣǣȣɣʣˣ̣ͣΣϣУѣңӣԣգ֣ףأ٣ڣ�������������������������������������¥åĥťƥǥȥɥʥ˥̥ͥΥϥХѥҥӥԥե֥ץإ����������������������������");
		buf.append("��������������������������������������");
		buf.append("�����äŤǤˤ̤ФѤ�");
		buf.append("�����������������������������������������������������������������ëīƫȫ��«ūǫɫʫ˫̫ͫΫϫҫիث۫Ыӫ֫٫ܫѫԫ׫ګݫޫ߫�������������������������");
		buf.append("�����������������������������������������������������������������êĪƪȪ��ªŪǪɪʪ˪̪ͪΪϪҪժت۪ЪӪ֪٪ܪѪԪתڪݪުߪ��������������������");
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
			VbyP.accessLog(user_id+" >> ���� ��û : ��ȭ��ȣ " + Integer.toString(result.size()) + "�� Ȯ��! ");
			VbyP.debugLog(" >> getPhone -> loop end");
			
			if (groupKeyBuf.length() > 0) {
				
				VbyP.accessLog(user_id+" >> ���� ��û : �ּҷ�( " + groupKeyBuf.toString() + ") �׷� �������� ");
				// remove last `,`
				groupKeyBuf.setLength(groupKeyBuf.length()-1);
				getPhoneOfAddressGroup( conn, result, user_id, groupKeyBuf.toString());
				
			}
			VbyP.accessLog(user_id+" >> ���� ��û : ��ü ��ȭ��ȣ�Ǽ�  " + Integer.toString(result.size()) + "�� ");
			
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
