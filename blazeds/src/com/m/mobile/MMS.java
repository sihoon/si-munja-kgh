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
				//���Űź�
				if (Refuse.isRefuse(hashTable_refuse, vo.PHONE)){
					
					insertClientPqSetter_fail(pq, vo, "98");
					
				}else if (hashTable.containsKey(vo.PHONE)){
					insertClientPqSetter_fail(pq, vo, "99"); 	
				}else {
					hashTable.put(vo.PHONE, "");
					insertClientPqSetter(pq, vo);
				}
				
				pq.addBatch();
				
				//�߼�ī��Ʈ
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
				
				//���Űź�
				if (Refuse.isRefuse(hashTable_refuse, vo.ID)){
					insertClientPqSetter_fail(pq, vo, "98");
				}					
				//�ߺ� ����
				else if (hashTable.containsKey(vo.PHONE)){
					insertClientPqSetter_fail(pq, vo, "99"); 	
				}else {
					hashTable.put(vo.PHONE, "");
					insertClientPqSetter(pq, vo);
				}
				
				pq.addBatch();								
				//�߼�ī��Ʈ
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
			throw new Exception("��ȣ��ȣ ����Ʈ�� �����ϴ�.");
		
		String [] temp = phoneAndNameArrayList.get(0);
		
		if ( temp.length < 1 || SLibrary.isNull(temp[0]))
			throw new Exception("��ȭ��ȣ ����Ʈ�� �����ϴ�.2");
			
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
			throw new Exception("��ȣ��ȣ ����Ʈ�� �����ϴ�.");
		
		String [] temp = phoneAndNameArrayList.get(0);
		
		if ( temp.length < 1 || SLibrary.isNull(temp[0]))
			throw new Exception("��ȭ��ȣ ����Ʈ�� �����ϴ�.2");
			
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
			throw new Exception("��ȭ��ȣ ����Ʈ�� �����ϴ�.");				
		
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
			throw new Exception("��ȭ��ȣ ����Ʈ�� �����ϴ�.");	
		
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
			vo.setMSG( bMerge ? SLibrary.replaceAll(message, "{�̸�}", name ):message );
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
			throw new Exception("��ȭ��ȣ ����Ʈ�� �����ϴ�.");	
		
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
			vo.setMSG( bMerge ? SLibrary.replaceAll(message, "{�̸�}", name ):message );
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
	
	private int getSMSLogKey(Connection conn) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("getSMSLogInsertKey") );
		return pq.ExecuteQueryNum();
				
	}

}
