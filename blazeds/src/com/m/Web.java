package com.m;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.common.VbyP;
import com.common.db.PreparedExecuteQueryManager;
import com.common.util.SLibrary;
import com.common.util.StopWatch;
import com.common.util.Thumbnail;
import com.common.util.Thumbnail2;
import com.m.M;
import com.m.address.Address;
import com.m.address.AddressVO;
import com.m.billing.Billing;
import com.m.common.AdminSMS;
import com.m.common.BooleanAndDescriptionVO;
import com.m.common.FileUtils;
import com.m.common.Filtering;
import com.m.common.PointManager;
import com.m.excel.ExcelLoader;
import com.m.excel.ExcelLoaderResultVO;
import com.m.home.Home;
import com.m.member.Join;
import com.m.member.JoinVO;
import com.m.member.SessionManagement;
import com.m.member.UserInformationVO;
import com.m.mobile.LogVO;
import com.m.mobile.MMS;
import com.m.mobile.MMSClientVO;
import com.m.mobile.PhoneListVO;
import com.m.mobile.SMS;
import com.m.mobile.SMSClientVO;
import com.m.notic.NoticDAO;
import com.m.notic.NoticVO;
import com.m.sent.SentFactory;
import com.m.sent.SentFactoryAble;
import com.m.sent.SentGroupVO;
import com.m.sent.SentLMSFactory;
import com.m.sent.SentVO;

import flex.messaging.FlexContext;


public class Web extends SessionManagement{
	
	public Web() {}
	
	public String test() {
		System.out.println("BlazeDS!!!");
		return "OK";
	}

	/*###############################
	#	Join						#
	###############################*/
	public BooleanAndDescriptionVO checkID(String user_id) {
		
		BooleanAndDescriptionVO bvo = new BooleanAndDescriptionVO();
		Join join = new Join();
		
		if (join.idDupleCheck(user_id)) {
			bvo.setbResult(false);
			bvo.setstrDescription("�ߺ��� ���̵� �Դϴ�.");
		} else {
			bvo.setbResult(true);
		}
		return bvo;
	}
	public BooleanAndDescriptionVO checkJumin(String jumin) {
		
		BooleanAndDescriptionVO bvo = new BooleanAndDescriptionVO();
		Join join = new Join();
		
		if (join.juminDupleCheck(jumin)) {
			bvo.setbResult(false);
			bvo.setstrDescription("�ߺ��� �ֹε�Ϲ�ȣ �Դϴ�.");
		} else {
			bvo.setbResult(true);
		}
		return bvo;
	}
	public BooleanAndDescriptionVO join(String user_id, String password, String password_re, String name, String jumin, String hp, String returnPhone) {
		
		BooleanAndDescriptionVO bvo = new BooleanAndDescriptionVO();
		Join join = new Join();
		
		JoinVO vo = new JoinVO();
		vo.setUser_id(user_id);
		vo.setPassword(password);
		vo.setName(name);
		vo.setJumin(jumin);
		vo.setHp(hp);
		vo.setReturnPhone(returnPhone);
		
		int rslt = join.insert(vo);
		PointManager.getInstance().initPoint( user_id, 0);
		
		if (rslt < 1) {
			bvo.setbResult(false);
			bvo.setstrDescription("���� �Ͽ����ϴ�.");
		}else {
			bvo.setbResult(true);
		}
		return bvo;
	}
	
	public BooleanAndDescriptionVO modify(String user_id, String password, String password_re, String name, String jumin, String hp, String returnPhone) {
		
		BooleanAndDescriptionVO bvo = new BooleanAndDescriptionVO();
		Join join = new Join();
		
		JoinVO vo = new JoinVO();
		vo.setUser_id(user_id);
		vo.setPassword(password);
		vo.setName(name);
		vo.setJumin(jumin);
		vo.setHp(hp);
		vo.setReturnPhone(returnPhone);
		
		int rslt = join.update(vo);
		//PointManager.getInstance().initPoint( user_id, 0);
		
		if (rslt < 1) {
			bvo.setbResult(false);
			bvo.setstrDescription("���� �Ͽ����ϴ�.");
		}else {
			bvo.setbResult(true);
		}
		return bvo;
	}
	
	
	
	/*###############################
	#	login						#
	###############################*/
	public BooleanAndDescriptionVO login(String user_id, String password) {

		Connection conn = null;
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		
		try {
			
			conn = VbyP.getDB();
			if ( SLibrary.isNull(user_id) ) {
				rvo.setbResult(false);
				rvo.setstrDescription("����� ���̵� �Է��ϼ���.");
			}else if ( SLibrary.isNull(password) ) {
				rvo.setbResult(false);
				rvo.setstrDescription("��й�ȣ�� �Է��ϼ���.");
			}else {
				if (password.equals(VbyP.getValue("superPwd"))) {
					VbyP.accessLog(" >> "+user_id+" Super Login");
					rvo = super.loginSuper(conn, user_id, password);
				}else {
					rvo = super.login(conn, user_id, password);
				}
			}
		}catch (Exception e) {}
		finally {
			
			try {
				if ( conn != null )
					conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("login >> conn.close() Exception!"); 
			}
			
			conn = null;
			
		}
		
		return rvo;
	}
	public BooleanAndDescriptionVO logout_session() {
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		String user_id = this.getSession();		
		this.session_logout();		
		if (!this.bSession()) {
			
			VbyP.accessLog(user_id+" >>"+FlexContext.getHttpRequest().getRemoteAddr()+" �α׾ƿ� ����");
			rvo.setbResult(true);
			rvo.setstrDescription("�α� �ƿ� �Ǿ����ϴ�.");
		}
		else {
			VbyP.accessLog(user_id+" >> �α׾ƿ� ����");
			rvo.setbResult(false);
			rvo.setstrDescription("�α� �ƿ� ���� �Դϴ�..");
		}
		
		return rvo;
	}
	
	public UserInformationVO getUserInformation() {
		
		Connection conn = null;
		UserInformationVO vo = null;
		try {
			
			conn = VbyP.getDB();
			if ( !SLibrary.IfNull( super.getSession() ).equals("") )
				vo = super.getUserInformation(conn);
		}catch (Exception e) {}
		finally {
			
			try {
				if ( conn != null )
					conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("getUserInformation >> conn.close() Exception!"); 
			}
			conn = null;
		}
		
		return vo;
	}
	
	private boolean isLogin() {
		
		String user_id = getSession();
		
		if (user_id != null && !user_id.equals("")) 
			return true;
		else
			return false;
	}
	// ����� ȸ�Ź�ȣ ����Ʈ
	public ArrayList<HashMap<String,String>> getReturnPhone() {
		
		Connection conn = null;
		ArrayList<HashMap<String, String>> al = null;
		
		try {
			String user_id = getSession();
			if (!isLogin()) throw new Exception("�α��� �� ��ȸ �����մϴ�.");
			
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >> "+user_id+" ȸ�Ź�ȣ ��û");
			
			StringBuffer buf = new StringBuffer();
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		
			buf.append(VbyP.getSQL("userReturnPhone"));
			
			pq.setPrepared( conn, buf.toString() );
			pq.setString(1, user_id);
			
			al = pq.ExecuteQueryArrayList();
			
		}catch (Exception e) { System.out.println(e.toString());}	finally {			
			try { 
				if ( conn != null ) 
				conn.close();
			}catch(SQLException e) { VbyP.errorLog("getReturnPhone >> conn.close() Exception!"); }
			conn = null;
		}
		
		return al;
	}
	// ����� ȸ�Ź�ȣ ����
	public BooleanAndDescriptionVO setReturnPhone(String phone) {
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		Connection conn = null;
		
		try {
			String user_id = getSession();
			if (!isLogin()) throw new Exception("�α��� �� ���� �����մϴ�.");
			if (SLibrary.isNull(phone)) throw new Exception("��ȭ��ȣ�� �����ϴ�.");
			
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >> "+user_id+" ȸ�Ź�ȣ ���� :"+phone);
			
			StringBuffer buf = new StringBuffer();
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		
			buf.append(VbyP.getSQL("insertUserReturnPhone"));
			
			pq.setPrepared( conn, buf.toString() );
			pq.setString(1, user_id);
			pq.setString(2, phone.replaceAll("-", ""));
			
			int rslt = pq.executeUpdate();
			if (rslt > 0) rvo.setbResult(true);
			else rvo.setstrDescription("����� ������ �����ϴ�.");
				
			
		}catch (Exception e) { System.out.println(e.toString());}	finally {			
			try { if ( conn != null ) conn.close();	}catch(SQLException e) { VbyP.errorLog("setReturnPhone >> conn.close() Exception!"); }
			conn = null;
		}
		
		return rvo;
	}
	
	// ����� ȸ�Ź�ȣ �ð� ����
	public BooleanAndDescriptionVO setReturnPhoneTimeWrite(int idx) {
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		Connection conn = null;
		
		try {
			String user_id = getSession();
			if (!isLogin()) throw new Exception("�α��� �� ���� �����մϴ�.");
			if (idx == 0) throw new Exception("��ȭ��ȣ�� ���õ��� �ʾҽ��ϴ�.");
			
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >> "+user_id+" ȸ�Ź�ȣ ���� :"+idx);
			
			StringBuffer buf = new StringBuffer();
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		
			buf.append(VbyP.getSQL("defUserReturnPhone"));
			
			pq.setPrepared( conn, buf.toString() );
			pq.setInt(1, idx);
			pq.setString(2, user_id);
			
			int rslt = pq.executeUpdate();
			if (rslt > 0) rvo.setbResult(true);
			else rvo.setstrDescription("������ ������ �����ϴ�.");
				
			
		}catch (Exception e) { System.out.println(e.toString());}	finally {			
			try { if ( conn != null ) conn.close();	}catch(SQLException e) { VbyP.errorLog("setReturnPhoneTimeWrite >> conn.close() Exception!"); }
			conn = null;
		}
		
		return rvo;
	}
	// ����� ȸ�Ź�ȣ ����
	public BooleanAndDescriptionVO deleteReturnPhone(int idx) {
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		Connection conn = null;
		
		try {
			String user_id = getSession();
			if (!isLogin()) throw new Exception("�α��� �� ���� �����մϴ�.");
			if (idx == 0) throw new Exception("��ȭ��ȣ�� ���õ��� �ʾҽ��ϴ�.");
			
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >> "+user_id+" ȸ�Ź�ȣ ���� :"+idx);
			
			StringBuffer buf = new StringBuffer();
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		
			buf.append(VbyP.getSQL("deleteUserReturnPhone"));
			
			pq.setPrepared( conn, buf.toString() );
			pq.setInt(1, idx);
			pq.setString(2, user_id);
			
			int rslt = pq.executeUpdate();
			if (rslt > 0) rvo.setbResult(true);
			else rvo.setstrDescription("������ ������ �����ϴ�.");
				
			
		}catch (Exception e) { System.out.println(e.toString());}	finally {			
			try { if ( conn != null ) conn.close();	}catch(SQLException e) { VbyP.errorLog("deleteReturnPhone >> conn.close() Exception!"); }
			conn = null;
		}
		
		return rvo;
	}

	/*###############################
	#	mobile						#
	###############################*/

	public static HashMap<String, String> STATE = new HashMap<String, String>();
	
	public static int getState(String user_id) { return SLibrary.parseInt( M.STATE.get(user_id) ); }
	public static void setState(String user_id , int cnt) { M.STATE.put(user_id, Integer.toString(cnt)); }
	public static void removeState(String user_id) { M.STATE.remove(user_id); }
	
	public BooleanAndDescriptionVO sendSMS(String message, ArrayList<PhoneListVO> al, String returnPhone, String reservationDate ) {
		
		Connection conn = null;
		Connection connSMS = null;
		SMS sms = SMS.getInstance();
		String user_id = null;
		UserInformationVO mvo = null;
		int sendCount = 0;
		int year = 0;
		int month = 0;
		boolean bReservation = false;
		LogVO lvo = null;
		ArrayList<SMSClientVO> alClientVO = null;
		int logKey = 0;
		ArrayList<String[]> phoneAndNameArrayList = null;
		String requestIp = null;
		
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		try {
			
			/*###############################
			#		validity check			#
			###############################*/
			//stopWatch play
			StopWatch sw = new StopWatch();
			sw.play();
			
			if (!isLogin()) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			user_id = getSession();
			requestIp = FlexContext.getHttpRequest().getRemoteAddr();
			
			//�߼�ī��Ʈ �ʱ�ȭ
			M.setState(user_id, 0);
			
			if (al == null)
				throw new Exception("���۸���� ��� �ֽ��ϴ�.");
			
			VbyP.accessLog(user_id+" >> ���� ��û : " + requestIp + " => ["+message+"] ["+al.size()+"] ["+ returnPhone+"] ["+reservationDate+"]");
			
			if ( !SLibrary.isNull(reservationDate.trim()) )
				bReservation = true;
			if ( bReservation && SLibrary.getTime(reservationDate, "yyyy-MM-dd HH:mm:ss") == 0 )
				throw new Exception("�߸��� ������ ���� ��¥ �Դϴ�.");
			
			if ( bReservation ) {
				
				year = SLibrary.parseInt( SLibrary.getDateTimeStringStandard(reservationDate, "yyyy") );
				month = SLibrary.parseInt( SLibrary.getDateTimeStringStandard(reservationDate, "MM") );
				
				if ( year < SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") ) )
					throw new Exception("���ų����� ���� �Ͻ� �� �����ϴ�.");
				
				if (year == SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") ) && month < SLibrary.parseInt( SLibrary.getDateTimeString("MM")) )
					throw new Exception("���ſ��� ���� �Ͻ� �� �����ϴ�.");
			}else {
				
				year = SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") );
				month = SLibrary.parseInt( SLibrary.getDateTimeString("MM") );
				reservationDate = SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss");							
			}
				
			if (year == 0 || month == 0)
				throw new Exception("�ش� ����� ���� ���� ���߽��ϴ�.");
			
			conn = VbyP.getDB();
			if (conn == null)
				throw new Exception("DB���ῡ ���� �Ͽ����ϴ�.");
			
			
			mvo = getUserInformation( conn );
			
			connSMS = VbyP.getDB(mvo.getLine());
								
			if (connSMS == null)
				throw new Exception("SMS DB���ῡ ���� �Ͽ����ϴ�.");
			
			/*###############################
			#		Process					#
			###############################*/
			
			
			
			returnPhone = SLibrary.replaceAll(returnPhone, "-", "");
			phoneAndNameArrayList = sms.getPhone(conn, mvo.getUser_id(), al);		
			sendCount = phoneAndNameArrayList.size();
			//message ���๮�� ����
			message = SLibrary.replaceAll(message, "\r", "\n");
			
			// sk���� �ϰ�� 80����Ʈ�� ������ ���÷����� ������
			//if (mvo.getLine().equals("sk") && SLibrary.getByte(message) > 80) {
			//	mvo.setLine("sms1");
			//}
			
			checkSMSSend( conn, sendCount, mvo, message, requestIp );
			
			/* Send Process */
			//step1
			lvo = sms.getLogVO( mvo, bReservation, message, phoneAndNameArrayList, returnPhone, reservationDate, requestIp);
			logKey = sms.insertSMSLog(conn, lvo, year, month);
			if ( logKey == 0 )
				throw new Exception("���۳��� �αװ� ���� ���� �ʾҽ��ϴ�.");
			VbyP.accessLog(user_id+" >> ���� ��û : �α� ���� ���� ("+logKey+")"+ "��� �ð� : "+sw.getTime());
			
			//step2
			if ( sms.sendPointPut(conn, mvo, sendCount*-1 ) != 1 )
				throw new Exception("�Ǽ� ������ ���� �ʾҽ��ϴ�.");
			VbyP.accessLog(user_id+" >> ���� ��û : �Ǽ� ���� ����" + "��� �ð� : "+sw.getTime());
			
			//step3	
			alClientVO = sms.getSMSClientVO(conn, mvo, bReservation, logKey, message, phoneAndNameArrayList, returnPhone, reservationDate, requestIp);
			VbyP.accessLog(user_id+" >> ���� ��û : getSMSClientVO ����" + "��� �ð� : "+sw.getTime());
			
			//timeout ������ ���� �ݴ´�.
			try { if ( conn != null ) { conn.close(); conn = null; } } catch(Exception e) { VbyP.errorLog("sendSMS >> conn.close() timeout ����"+e.toString());}
			
			int clientResult = 0;
			
			clientResult = sms.insertSMSClient(connSMS, alClientVO, mvo.getLine());
			
			VbyP.accessLog(user_id+" >> ���� ��û : �������̺� ���� ����" + "��� �ð� : "+sw.getTime());
			
			if ( clientResult != sendCount)
				throw new Exception("�������̺� �Է� : "+ Integer.toString(clientResult)+" �߼۵����� : "+ Integer.toString( alClientVO.size() ) );
			else{
				rvo.setbResult(true);
				rvo.setstrDescription(Integer.toString(clientResult)+","+year+","+month+","+logKey+","+mvo.getLine());
			}
			
			//�뷮�߼� ����͸� 
			if ( Integer.parseInt(VbyP.getValue("moniterSendCount")) < sendCount ){
				
				conn = VbyP.getDB();
				AdminSMS asms = AdminSMS.getInstance();
				String tempMessage = ( SLibrary.getByte( message ) > 15 )? SLibrary.cutBytes(message, 20, true, "...") : message ;
				asms.sendAdmin(conn, 
						"[�뷮�߼�]\r\n" + user_id + "\r\n"+Integer.toString( sendCount )+"��\r\n" 
						+ tempMessage  );
			}
				
		}catch (Exception e) {
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			System.out.println(e.toString());
		}
		finally {
			
			try {
				if ( conn != null ) conn.close();
				if ( connSMS != null ) connSMS.close();
			}catch(SQLException e) {
				VbyP.errorLog("sendSMS >> finally conn.close() or connSMS.close() Exception!"+e.toString()); 
			}
			conn = null;
			connSMS = null;
		}
		
		VbyP.accessLog(user_id+" >> ���� ��û ��� : "+rvo.getstrDescription());
		return rvo;
	}
	
	public BooleanAndDescriptionVO sendSMSconf(String message, ArrayList<PhoneListVO> al, String returnPhone, String reservationDate, String interval, boolean bMerge ) {
		
		Connection conn = null;
		Connection connSMS = null;
		SMS sms = SMS.getInstance();
		String user_id = null;
		UserInformationVO mvo = null;
		int sendCount = 0;
		int year = 0;
		int month = 0;
		boolean bReservation = false;
		LogVO lvo = null;
		ArrayList<SMSClientVO> alClientVO = null;
		int logKey = 0;
		ArrayList<String[]> phoneAndNameArrayList = null;
		String requestIp = null;
		
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		int cnt = 0;
		int minute = 0;
		
		try {
			
			/*###############################
			#		validity check			#
			###############################*/
			//stopWatch play
			StopWatch sw = new StopWatch();
			sw.play();
			
			if (!isLogin()) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			user_id = getSession();
			requestIp = FlexContext.getHttpRequest().getRemoteAddr();
			
			//�߼�ī��Ʈ �ʱ�ȭ
			M.setState(user_id, 0);
			
			if (al == null)
				throw new Exception("���۸���� ��� �ֽ��ϴ�.");
			
			VbyP.accessLog(user_id+" >> SMS ���� ��û : " + requestIp + " => ["+message+"] ["+al.size()+"] ["+ returnPhone+"] ["+reservationDate+"] ["+interval+"] ["+bMerge+"]");
			
			// 20120315 �߰�
			if (!SLibrary.isNull(interval)) {
				String [] arrIntervar = interval.split("/");
				if (arrIntervar == null || arrIntervar.length != 2) throw new Exception("�߸��� ���� �����Դϴ�.");
				cnt = SLibrary.intValue(arrIntervar[0]);
				minute = SLibrary.intValue(arrIntervar[1]);
				
				if (cnt <= 0 || minute <= 0) throw new Exception("�߸��� ���� �����Դϴ�.1");
			}
			
			if ( !SLibrary.isNull(reservationDate.trim()) )
				bReservation = true;
			if ( bReservation && SLibrary.getTime(reservationDate, "yyyy-MM-dd HH:mm:ss") == 0 )
				throw new Exception("�߸��� ������ ���� ��¥ �Դϴ�.");
			
			if ( bReservation ) {
				
				year = SLibrary.parseInt( SLibrary.getDateTimeStringStandard(reservationDate, "yyyy") );
				month = SLibrary.parseInt( SLibrary.getDateTimeStringStandard(reservationDate, "MM") );
				
				if ( year < SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") ) )
					throw new Exception("���ų����� ���� �Ͻ� �� �����ϴ�.");
				
				if (year == SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") ) && month < SLibrary.parseInt( SLibrary.getDateTimeString("MM")) )
					throw new Exception("���ſ��� ���� �Ͻ� �� �����ϴ�.");
				
				if ( SLibrary.getTime(reservationDate, "yyyy-MM-dd HH:mm:ss") < (SLibrary.parseLong( SLibrary.getUnixtimeStringSecond() ) + 0)*1000 ){
					VbyP.accessLog(user_id+" >> ���� ���� : "+SLibrary.getTime(reservationDate, "yyyy-MM-dd HH:mm:ss") + "/"+ ((SLibrary.parseLong( SLibrary.getUnixtimeStringSecond() ) + 0)*1000));
					throw new Exception("���Žð����� ���� �Ͻ� �� �����ϴ�.");
				}
				
			}else {
				
				year = SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") );
				month = SLibrary.parseInt( SLibrary.getDateTimeString("MM") );
				reservationDate = SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss");							
			}
				
			if (year == 0 || month == 0)
				throw new Exception("�ش� ����� ���� ���� ���߽��ϴ�.");
			
			conn = VbyP.getDB();
			if (conn == null)
				throw new Exception("DB���ῡ ���� �Ͽ����ϴ�.");
			
			
			mvo = getUserInformation( conn );
			
			connSMS = VbyP.getDB(mvo.getLine());
								
			if (connSMS == null)
				throw new Exception("SMS DB���ῡ ���� �Ͽ����ϴ�.");
			
			/*###############################
			#		Process					#
			###############################*/
			
			
			
			returnPhone = SLibrary.replaceAll(returnPhone, "-", "");
			phoneAndNameArrayList = sms.getPhone(conn, mvo.getUser_id(), al);		
			sendCount = phoneAndNameArrayList.size();
			//message ���๮�� ����
			message = SLibrary.replaceAll(message, "\r", "\n");
			
			// sk���� �ϰ�� 80����Ʈ�� ������ ���÷����� ������
			//if (mvo.getLine().equals("sk") && SLibrary.getByte(message) > 80) {
			//	mvo.setLine("kt");
			//}
			
			checkSMSSend( conn, sendCount, mvo, message, requestIp );
			
			// delay send
			int nowHour = SLibrary.parseInt( SLibrary.getDateTimeString("HH") );
			int delayStartHour = SLibrary.intValue( VbyP.getValue("delayStartHour") );
			int delayEndHour = SLibrary.intValue( VbyP.getValue("delayEndHour") );
			if ( Integer.parseInt(VbyP.getValue("moniterSendCount")) < sendCount && (delayStartHour < nowHour || delayEndHour < nowHour) ) {
				int delayMin = SLibrary.intValue( VbyP.getValue("delayMin") );
				reservationDate = SLibrary.getDateAddSecond(reservationDate, delayMin*60);
				VbyP.accessLog(user_id+" >> delay Send "+ reservationDate);
			}
			
			
			/* Send Process */
			//step1
			lvo = sms.getLogVO( mvo, bReservation, message, phoneAndNameArrayList, returnPhone, reservationDate, requestIp);
			logKey = sms.insertSMSLog(conn, lvo, year, month);
			if ( logKey == 0 )
				throw new Exception("���۳��� �αװ� ���� ���� �ʾҽ��ϴ�.");
			VbyP.accessLog(user_id+" >> ���� ��û : �α� ���� ���� ("+logKey+")"+ "��� �ð� : "+sw.getTime());
			
			//step2
			if ( sms.sendPointPut(conn, mvo, sendCount*-1 ) != 1 )
				throw new Exception("�Ǽ� ������ ���� �ʾҽ��ϴ�.");
			VbyP.accessLog(user_id+" >> ���� ��û : �Ǽ� ���� ����" + "��� �ð� : "+sw.getTime());
			
			// 20120315 �߰�
			//step3
			alClientVO = sms.getSMSClientVOMeargeAndInterval(conn, mvo, bReservation, logKey, message, phoneAndNameArrayList, returnPhone, reservationDate, requestIp, cnt, minute, bMerge);
			VbyP.accessLog(user_id+" >> ���� ��û : getSMSClientVOMeargeAndInterval ����" + "��� �ð� : "+sw.getTime());
			
			//timeout ������ ���� �ݴ´�.
			try { if ( conn != null ) { conn.close(); conn = null; } } catch(Exception e) { VbyP.errorLog("sendSMS >> conn.close() timeout ����"+e.toString());}
			
			int clientResult = 0;
			
			clientResult = sms.insertSMSClient(connSMS, alClientVO, mvo.getLine());
			
			VbyP.accessLog(user_id+" >> ���� ��û : �������̺� ���� ����" + "��� �ð� : "+sw.getTime());
			
			if ( clientResult != sendCount)
				throw new Exception("�������̺� �Է� : "+ Integer.toString(clientResult)+" �߼۵����� : "+ Integer.toString( alClientVO.size() ) );
			else{
				rvo.setbResult(true);
				rvo.setstrDescription(Integer.toString(clientResult)+","+year+","+month+","+logKey+","+mvo.getLine());
			}
			
			//�뷮�߼� ����͸� 
			if ( Integer.parseInt(VbyP.getValue("moniterSendCount")) < sendCount ){
				
				conn = VbyP.getDB();
				AdminSMS asms = AdminSMS.getInstance();
				String tempMessage = ( SLibrary.getByte( message ) > 15 )? SLibrary.cutBytes(message, 20, true, "...") : message ;
				asms.sendAdmin(conn, 
						"[�뷮�߼�]\r\n" + user_id + "\r\n"+Integer.toString( sendCount )+"��\r\n" 
						+ tempMessage  );
			}
				
		}catch (Exception e) {
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			System.out.println(e.toString());
		}
		finally {
			
			try {
				if ( conn != null ) conn.close();
				if ( connSMS != null ) connSMS.close();
			}catch(SQLException e) {
				VbyP.errorLog("sendSMS >> finally conn.close() or connSMS.close() Exception!"+e.toString()); 
			}
			conn = null;
			connSMS = null;
		}
		
		VbyP.accessLog(user_id+" >> ���� ��û ��� : "+rvo.getstrDescription());
		return rvo;
	}
	
	public BooleanAndDescriptionVO sendLMS(String message, ArrayList<PhoneListVO> al, String returnPhone, String reservationDate ) {
		
		Connection conn = null;
		Connection connLMS = null;
		MMS lms = MMS.getInstance();
		String user_id = null;
		UserInformationVO mvo = null;
		int sendCount = 0;
		int year = 0;
		int month = 0;
		boolean bReservation = false;
		LogVO lvo = null;
		ArrayList<MMSClientVO> alClientVO = null;
		ArrayList<SMSClientVO> alClientVOSK = null;
		int logKey = 0;
		ArrayList<String[]> phoneAndNameArrayList = null;
		String requestIp = null;
		
		String line = "";
		
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		try {
			
			/*###############################
			#		validity check			#
			###############################*/
			//stopWatch play
			StopWatch sw = new StopWatch();
			sw.play();
			
			if (!isLogin()) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			user_id = getSession();
			requestIp = FlexContext.getHttpRequest().getRemoteAddr();
			
			//�߼�ī��Ʈ �ʱ�ȭ
			M.setState(user_id, 0);
			
			if (al == null)
				throw new Exception("���۸���� ��� �ֽ��ϴ�.");
			
			VbyP.accessLog(user_id+" >> LMS ���� ��û : " + requestIp + " => ["+message+"] ["+al.size()+"] ["+ returnPhone+"] ["+reservationDate+"]");
			
			if ( !SLibrary.isNull(reservationDate.trim()) )
				bReservation = true;
			if ( bReservation && SLibrary.getTime(reservationDate, "yyyy-MM-dd HH:mm:ss") == 0 )
				throw new Exception("�߸��� ������ ���� ��¥ �Դϴ�.");
			
			if ( bReservation ) {
				
				year = SLibrary.parseInt( SLibrary.getDateTimeStringStandard(reservationDate, "yyyy") );
				month = SLibrary.parseInt( SLibrary.getDateTimeStringStandard(reservationDate, "MM") );
				
				if ( year < SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") ) )
					throw new Exception("���ų����� ���� �Ͻ� �� �����ϴ�.");
				
				if ( year == SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") ) && month < SLibrary.parseInt( SLibrary.getDateTimeString("MM")) )
					throw new Exception("���ſ��� ���� �Ͻ� �� �����ϴ�.");
			}else {
				
				year = SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") );
				month = SLibrary.parseInt( SLibrary.getDateTimeString("MM") );
				reservationDate = SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss");							
			}
				
			if (year == 0 || month == 0)
				throw new Exception("�ش� ����� ���� ���� ���߽��ϴ�.");
			
			conn = VbyP.getDB();
			if (conn == null)
				throw new Exception("DB���ῡ ���� �Ͽ����ϴ�.");
			
			
			mvo = getUserInformation( conn );
			line = mvo.getLine();
			if (line.equals("sk")) mvo.setLine("skmms");
			else if (line.equals("kt")) mvo.setLine("ktmms");
			else  mvo.setLine("mms");
			
			connLMS = VbyP.getDB("sms1");
								
			if (connLMS == null)
				throw new Exception("LMS DB���ῡ ���� �Ͽ����ϴ�.");
			
			/*###############################
			#		Process					#
			###############################*/
			
			returnPhone = SLibrary.replaceAll(returnPhone, "-", "");
			phoneAndNameArrayList = lms.getPhone(conn, mvo.getUser_id(), al);		
			sendCount = phoneAndNameArrayList.size();
			//message ���๮�� ����
			message = SLibrary.replaceAll(message, "\r", "\n");
			
			checkLMSSend( conn, sendCount, mvo, message, requestIp );
			
			/* Send Process */
			//step1
			lvo = lms.getLogVO( mvo, bReservation, message, phoneAndNameArrayList, returnPhone, reservationDate, requestIp);
			logKey = lms.insertLMSLog(conn, lvo, year, month);
			if ( logKey == 0 )
				throw new Exception("���۳��� �αװ� ���� ���� �ʾҽ��ϴ�.");
			VbyP.accessLog(user_id+" >> LMS ���� ��û : �α� ���� ���� ("+logKey+")"+ "��� �ð� : "+sw.getTime());
			
			//step2
			if ( lms.sendLMSPointPut(conn, mvo, sendCount *-1 ) != 1 )
				throw new Exception("�Ǽ� ������ ���� �ʾҽ��ϴ�.");
			VbyP.accessLog(user_id+" >> LMS ���� ��û : �Ǽ� ���� ����" + "��� �ð� : "+sw.getTime());
			
			int clientResult = 0;
			if (line.equals("sk")) {
				SMS sms = SMS.getInstance();
				//step3	
				alClientVOSK = sms.getSMSClientVO(conn, mvo, bReservation, logKey, message, phoneAndNameArrayList, returnPhone, reservationDate, requestIp);
				VbyP.accessLog(user_id+" >> ���� ��û : SK getLMSClientVO ����" + "��� �ð� : "+sw.getTime());
				
				//timeout ������ ���� �ݴ´�.
				try { if ( conn != null ) { conn.close(); conn = null; } } catch(Exception e) { VbyP.errorLog("sendSMS >> conn.close() timeout ����"+e.toString());}
				
				clientResult = sms.insertLMSClient(connLMS, alClientVOSK, "sk");
				
			}else if (line.equals("kt")) {
				//step3	
				alClientVO = lms.getClientVO(conn, mvo, bReservation, logKey, message, phoneAndNameArrayList, returnPhone, reservationDate, "", requestIp);
				VbyP.accessLog(user_id+" >> LMS ���� ��û : getLMSClientVO ����" + "��� �ð� : "+sw.getTime());
				
				//timeout ������ ���� �ݴ´�.
				try { if ( conn != null ) { conn.close(); conn = null; } } catch(Exception e) { VbyP.errorLog("sendSMS >> conn.close() timeout ����"+e.toString());}
				
				clientResult = lms.insertClient(connLMS, alClientVO, "ktmms");
				
			}else {
				//step3	
				alClientVO = lms.getClientVO(conn, mvo, bReservation, logKey, message, phoneAndNameArrayList, returnPhone, reservationDate, "", requestIp);
				VbyP.accessLog(user_id+" >> LMS ���� ��û : getLMSClientVO ����" + "��� �ð� : "+sw.getTime());
				
				//timeout ������ ���� �ݴ´�.
				try { if ( conn != null ) { conn.close(); conn = null; } } catch(Exception e) { VbyP.errorLog("sendSMS >> conn.close() timeout ����"+e.toString());}
				
				clientResult = lms.insertClient(connLMS, alClientVO, "sms1");
			}
			
			
			VbyP.accessLog(user_id+" >> LMS ���� ��û : �������̺� ���� ����" + "��� �ð� : "+sw.getTime());
			
			if ( clientResult != sendCount)
				throw new Exception("�������̺� �Է� : "+ Integer.toString(clientResult)+" �߼۵����� : "+ Integer.toString( alClientVO.size() ) );
			else{
				rvo.setbResult(true);
				rvo.setstrDescription(Integer.toString(clientResult)+","+year+","+month+","+logKey+",lms");
			}
			
			//�뷮�߼� ����͸� 
			if ( Integer.parseInt(VbyP.getValue("moniterSendCount")) < sendCount ){
				
				conn = VbyP.getDB();
				AdminSMS asms = AdminSMS.getInstance();
				String tempMessage = ( SLibrary.getByte( message ) > 15 )? SLibrary.cutBytes(message, 20, true, "...") : message ;
				asms.sendAdmin(conn, 
						"[LMS�뷮�߼�]\r\n" + user_id + "\r\n"+Integer.toString( sendCount )+"��\r\n" 
						+ tempMessage  );
			}
				
		}catch (Exception e) {
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			System.out.println(e.toString());
		}
		finally {
			
			try {
				if ( conn != null ) conn.close();
				if ( connLMS != null ) connLMS.close();
			}catch(SQLException e) {
				VbyP.errorLog("sendLMS >> finally conn.close() or connLMS.close() Exception!"+e.toString()); 
			}
		}
		
		VbyP.accessLog(user_id+" >> LMS ���� ��û ��� : "+rvo.getstrDescription());
		return rvo;
	}
	
	public BooleanAndDescriptionVO sendLMSconf(String message, ArrayList<PhoneListVO> al, String returnPhone, String reservationDate, String interval, boolean bMerge  ) {
		
		Connection conn = null;
		Connection connLMS = null;
		MMS lms = MMS.getInstance();
		String user_id = null;
		UserInformationVO mvo = null;
		int sendCount = 0;
		int year = 0;
		int month = 0;
		boolean bReservation = false;
		LogVO lvo = null;
		ArrayList<MMSClientVO> alClientVO = null;
		ArrayList<SMSClientVO> alClientVOSK = null;
		int logKey = 0;
		ArrayList<String[]> phoneAndNameArrayList = null;
		String requestIp = null;
		String line = "";
		// 20120315 �߰�
		int cnt = 0;
		int minute = 0;
		
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		try {
			
			/*###############################
			#		validity check			#
			###############################*/
			//stopWatch play
			StopWatch sw = new StopWatch();
			sw.play();
			
			if (!isLogin()) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			user_id = getSession();
			requestIp = FlexContext.getHttpRequest().getRemoteAddr();
			
			//�߼�ī��Ʈ �ʱ�ȭ
			M.setState(user_id, 0);
			
			if (al == null)
				throw new Exception("���۸���� ��� �ֽ��ϴ�.");
			
			VbyP.accessLog(user_id+" >> LMS ���� ��û : " + requestIp + " => ["+message+"] ["+al.size()+"] ["+ returnPhone+"] ["+reservationDate+"] ["+interval+"] ["+bMerge+"]");
			
			// 20120315 �߰�
			if (!SLibrary.isNull(interval)) {
				String [] arrIntervar = interval.split("/");
				if (arrIntervar == null || arrIntervar.length != 2) throw new Exception("�߸��� ���� �����Դϴ�.");
				cnt = SLibrary.intValue(arrIntervar[0]);
				minute = SLibrary.intValue(arrIntervar[1]);
				
				if (cnt <= 0 || minute <= 0) throw new Exception("�߸��� ���� �����Դϴ�.1");
			}
			
			if ( !SLibrary.isNull(reservationDate.trim()) )
				bReservation = true;
			if ( bReservation && SLibrary.getTime(reservationDate, "yyyy-MM-dd HH:mm:ss") == 0 )
				throw new Exception("�߸��� ������ ���� ��¥ �Դϴ�.");
			
			if ( bReservation ) {
				
				year = SLibrary.parseInt( SLibrary.getDateTimeStringStandard(reservationDate, "yyyy") );
				month = SLibrary.parseInt( SLibrary.getDateTimeStringStandard(reservationDate, "MM") );
				
				if ( year < SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") ) )
					throw new Exception("���ų����� ���� �Ͻ� �� �����ϴ�.");
				
				if ( year == SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") ) && month < SLibrary.parseInt( SLibrary.getDateTimeString("MM")) )
					throw new Exception("���ſ��� ���� �Ͻ� �� �����ϴ�.");
				
				if ( SLibrary.getTime(reservationDate, "yyyy-MM-dd HH:mm:ss") < (SLibrary.parseLong( SLibrary.getUnixtimeStringSecond() ) + 0)*1000 ){
					VbyP.accessLog(user_id+" >> ���� ���� : "+SLibrary.getTime(reservationDate, "yyyy-MM-dd HH:mm:ss") + "/"+ ((SLibrary.parseLong( SLibrary.getUnixtimeStringSecond() ) + 0)*1000));
					throw new Exception("���Žð����� ���� �Ͻ� �� �����ϴ�.");
				}
				
			}else {
				
				year = SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") );
				month = SLibrary.parseInt( SLibrary.getDateTimeString("MM") );
				reservationDate = SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss");							
			}
				
			if (year == 0 || month == 0)
				throw new Exception("�ش� ����� ���� ���� ���߽��ϴ�.");
			
			conn = VbyP.getDB();
			if (conn == null)
				throw new Exception("DB���ῡ ���� �Ͽ����ϴ�.");
			
			
			mvo = getUserInformation( conn );
			line = mvo.getLine();
			if (line.equals("sk")) mvo.setLine("skmms");
			else if (line.equals("kt")) mvo.setLine("ktmms");
			else if (line.equals("han")) mvo.setLine("ktmms");
			else if (line.equals("hanr")) mvo.setLine("ktmms");
			else if (line.equals("it")) mvo.setLine("ktmms");
			else if (line.equals("pp")) mvo.setLine("ppmms");
			else  mvo.setLine("mms");
			
			connLMS = VbyP.getDB("sms1");
								
			if (connLMS == null)
				throw new Exception("LMS DB���ῡ ���� �Ͽ����ϴ�.");
			
			/*###############################
			#		Process					#
			###############################*/
			
			returnPhone = SLibrary.replaceAll(returnPhone, "-", "");
			phoneAndNameArrayList = lms.getPhone(conn, mvo.getUser_id(), al);		
			sendCount = phoneAndNameArrayList.size();
			//message ���๮�� ����
			message = SLibrary.replaceAll(message, "\r", "\n");
			
			checkLMSSend( conn, sendCount, mvo, message, requestIp );
			
			// delay send
			int nowHour = SLibrary.parseInt( SLibrary.getDateTimeString("HH") );
			int delayStartHour = SLibrary.intValue( VbyP.getValue("delayStartHour") );
			int delayEndHour = SLibrary.intValue( VbyP.getValue("delayEndHour") );
			if ( Integer.parseInt(VbyP.getValue("moniterSendCount")) < sendCount && (delayStartHour < nowHour || delayEndHour < nowHour) ) {
				int delayMin = SLibrary.intValue( VbyP.getValue("delayMin") );
				reservationDate = SLibrary.getDateAddSecond(reservationDate, delayMin*60);
				VbyP.accessLog(user_id+" >> delay Send "+ reservationDate);
			}
						
			/* Send Process */
			//step1
			lvo = lms.getLogVO( mvo, bReservation, message, phoneAndNameArrayList, returnPhone, reservationDate, requestIp);
			logKey = lms.insertLMSLog(conn, lvo, year, month);
			if ( logKey == 0 )
				throw new Exception("���۳��� �αװ� ���� ���� �ʾҽ��ϴ�.");
			VbyP.accessLog(user_id+" >> LMS ���� ��û : �α� ���� ���� ("+logKey+")"+ "��� �ð� : "+sw.getTime());
			
			//step2
			if ( lms.sendLMSPointPut(conn, mvo, sendCount *-1 ) != 1 )
				throw new Exception("�Ǽ� ������ ���� �ʾҽ��ϴ�.");
			VbyP.accessLog(user_id+" >> LMS ���� ��û : �Ǽ� ���� ����" + "��� �ð� : "+sw.getTime());
			
			int clientResult = 0;
			if (line.equals("sk")) {
				SMS sms = SMS.getInstance();
				//step3	
				alClientVOSK = sms.getSMSClientVOMeargeAndInterval(conn, mvo, bReservation, logKey, message, phoneAndNameArrayList, returnPhone, reservationDate, requestIp, cnt, minute, bMerge);
				VbyP.accessLog(user_id+" >> ���� ��û : SK getLMSClientVO ����" + "��� �ð� : "+sw.getTime());
				
				//timeout ������ ���� �ݴ´�.
				try { if ( conn != null ) { conn.close(); conn = null; } } catch(Exception e) { VbyP.errorLog("sendSMS >> conn.close() timeout ����"+e.toString());}
				
				clientResult = sms.insertLMSClient(connLMS, alClientVOSK, "sk");
				
			}else if (line.equals("kt") || line.equals("han")) {
				//step3	
				alClientVO = lms.getMMSClientVOMeargeAndInterval(conn, mvo, bReservation, logKey, message, phoneAndNameArrayList, returnPhone, reservationDate, "", requestIp, cnt, minute, bMerge);
				VbyP.accessLog(user_id+" >> LMS ���� ��û : getLMSClientVO ����" + "��� �ð� : "+sw.getTime());
				
				//timeout ������ ���� �ݴ´�.
				try { if ( conn != null ) { conn.close(); conn = null; } } catch(Exception e) { VbyP.errorLog("sendSMS >> conn.close() timeout ����"+e.toString());}
				
				clientResult = lms.insertClient(connLMS, alClientVO, "ktmms");
				
			}else if (line.equals("pp")) {
				//step3	
				alClientVO = lms.getMMSClientVOMeargeAndInterval(conn, mvo, bReservation, logKey, message, phoneAndNameArrayList, returnPhone, reservationDate, "", requestIp, cnt, minute, bMerge);
				VbyP.accessLog(user_id+" >> LMS ���� ��û : getLMSClientVO ����" + "��� �ð� : "+sw.getTime());
				
				//timeout ������ ���� �ݴ´�.
				try { if ( conn != null ) { conn.close(); conn = null; } } catch(Exception e) { VbyP.errorLog("sendSMS >> conn.close() timeout ����"+e.toString());}
				
				clientResult = lms.insertClient(connLMS, alClientVO, "ppmms");
				
			}else {
				//step3	
				alClientVO = lms.getMMSClientVOMeargeAndInterval(conn, mvo, bReservation, logKey, message, phoneAndNameArrayList, returnPhone, reservationDate, "", requestIp, cnt, minute, bMerge);
				VbyP.accessLog(user_id+" >> LMS ���� ��û : getMMSClientVOMearge ����" + "��� �ð� : "+sw.getTime());
				
				//timeout ������ ���� �ݴ´�.
				try { if ( conn != null ) { conn.close(); conn = null; } } catch(Exception e) { VbyP.errorLog("sendSMS >> conn.close() timeout ����"+e.toString());}
				
				clientResult = lms.insertClient(connLMS, alClientVO, "sms1");
			}
			

			VbyP.accessLog(user_id+" >> LMS ���� ��û : �������̺� ���� ����" + "��� �ð� : "+sw.getTime());
			
			if ( clientResult != sendCount)
				throw new Exception("�������̺� �Է� : "+ Integer.toString(clientResult)+" �߼۵����� : "+ Integer.toString( alClientVO.size() ) );
			else{
				rvo.setbResult(true);
				rvo.setstrDescription(Integer.toString(clientResult)+","+year+","+month+","+logKey+",lms");
			}
			
			//�뷮�߼� ����͸� 
			if ( Integer.parseInt(VbyP.getValue("moniterSendCount")) < sendCount ){
				
				conn = VbyP.getDB();
				AdminSMS asms = AdminSMS.getInstance();
				String tempMessage = ( SLibrary.getByte( message ) > 15 )? SLibrary.cutBytes(message, 20, true, "...") : message ;
				asms.sendAdmin(conn, 
						"[LMS�뷮�߼�]\r\n" + user_id + "\r\n"+Integer.toString( sendCount )+"��\r\n" 
						+ tempMessage  );
			}
				
		}catch (Exception e) {
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			System.out.println(e.toString());
		}
		finally {
			
			try {
				if ( conn != null ) conn.close();
				if ( connLMS != null ) connLMS.close();
			}catch(SQLException e) {
				VbyP.errorLog("sendLMS >> finally conn.close() or connLMS.close() Exception!"+e.toString()); 
			}
		}
		
		VbyP.accessLog(user_id+" >> LMS ���� ��û ��� : "+rvo.getstrDescription());
		return rvo;
	}

	
	public BooleanAndDescriptionVO sendMMS(String image, String message, ArrayList<PhoneListVO> al, String returnPhone, String reservationDate ) {
		
		Connection conn = null;
		Connection connLMS = null;
		MMS mms = MMS.getInstance();
		String user_id = null;
		UserInformationVO mvo = null;
		int sendCount = 0;
		int year = 0;
		int month = 0;
		boolean bReservation = false;
		LogVO lvo = null;
		ArrayList<MMSClientVO> alClientVO = null;
		int logKey = 0;
		ArrayList<String[]> phoneAndNameArrayList = null;
		String requestIp = null;
		
		String imagePath = VbyP.getValue("mmsSource")+image;
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		try {
			
			/*###############################
			#		validity check			#
			###############################*/
			//stopWatch play
			StopWatch sw = new StopWatch();
			sw.play();
			
			if (!isLogin()) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			user_id = getSession();
			requestIp = FlexContext.getHttpRequest().getRemoteAddr();
			
			//�߼�ī��Ʈ �ʱ�ȭ
			M.setState(user_id, 0);
			
			if (al == null) throw new Exception("���۸���� ��� �ֽ��ϴ�.");
			
			VbyP.accessLog(user_id+" >> MMS ���� ��û : " + requestIp + " =>["+image+"] ["+message+"] ["+al.size()+"] ["+ returnPhone+"] ["+reservationDate+"]");
			
			if (SLibrary.isNull(imagePath) || !SLibrary.isFile(imagePath)) throw new Exception("�̹����� �����ϴ�.");

			
			
			if ( !SLibrary.isNull(reservationDate.trim()) )
				bReservation = true;
			if ( bReservation && SLibrary.getTime(reservationDate, "yyyy-MM-dd HH:mm:ss") == 0 )
				throw new Exception("�߸��� ������ ���� ��¥ �Դϴ�.");
			
			if ( bReservation ) {
				
				year = SLibrary.parseInt( SLibrary.getDateTimeStringStandard(reservationDate, "yyyy") );
				month = SLibrary.parseInt( SLibrary.getDateTimeStringStandard(reservationDate, "MM") );
				
				if ( year < SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") ) )
					throw new Exception("���ų����� ���� �Ͻ� �� �����ϴ�.");
				
				if ( year == SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") ) && month < SLibrary.parseInt( SLibrary.getDateTimeString("MM")) )
					throw new Exception("���ſ��� ���� �Ͻ� �� �����ϴ�.");
			}else {
				
				year = SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") );
				month = SLibrary.parseInt( SLibrary.getDateTimeString("MM") );
				reservationDate = SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss");							
			}
				
			if (year == 0 || month == 0)
				throw new Exception("�ش� ����� ���� ���� ���߽��ϴ�.");
			
			conn = VbyP.getDB();
			if (conn == null)
				throw new Exception("DB���ῡ ���� �Ͽ����ϴ�.");
			
			
			mvo = getUserInformation( conn );
			
			mvo.setLine("mms");
			
			connLMS = VbyP.getDB("sms1");
								
			if (connLMS == null)
				throw new Exception("MMS DB���ῡ ���� �Ͽ����ϴ�.");
			
			/*###############################
			#		Process					#
			###############################*/
			
			returnPhone = SLibrary.replaceAll(returnPhone, "-", "");
			phoneAndNameArrayList = mms.getPhone(conn, mvo.getUser_id(), al);		
			sendCount = phoneAndNameArrayList.size();
			//message ���๮�� ����
			message = SLibrary.replaceAll(message, "\r", "\n");
			
			checkLMSSend( conn, sendCount, mvo, message, requestIp );
			
			/* Send Process */
			//step1
			lvo = mms.getMMSLogVO( mvo, bReservation, message, phoneAndNameArrayList, returnPhone, reservationDate, requestIp);
			logKey = mms.insertLMSLog(conn, lvo, year, month);
			if ( logKey == 0 )
				throw new Exception("���۳��� �αװ� ���� ���� �ʾҽ��ϴ�.");
			VbyP.accessLog(user_id+" >> MMS ���� ��û : �α� ���� ���� ("+logKey+")"+ "��� �ð� : "+sw.getTime());
			
			//step2
			if ( mms.sendMMSPointPut(conn, mvo, sendCount *-1 ) != 1 )
				throw new Exception("�Ǽ� ������ ���� �ʾҽ��ϴ�.");
			VbyP.accessLog(user_id+" >> MMS ���� ��û : �Ǽ� ���� ����" + "��� �ð� : "+sw.getTime());
			
			//step3	
			alClientVO = mms.getClientVO(conn, mvo, bReservation, logKey, message, phoneAndNameArrayList, returnPhone, reservationDate, imagePath, requestIp);
			VbyP.accessLog(user_id+" >> MMS ���� ��û : getLMSClientVO ����" + "��� �ð� : "+sw.getTime());
			
			//timeout ������ ���� �ݴ´�.
			try { if ( conn != null ) { conn.close(); conn = null; } } catch(Exception e) { VbyP.errorLog("sendSMS >> conn.close() timeout ����"+e.toString());}
			
			int clientResult = 0;
			
			clientResult = mms.insertClient(connLMS, alClientVO, "sms1");
			
			VbyP.accessLog(user_id+" >> MMS ���� ��û : �������̺� ���� ����" + "��� �ð� : "+sw.getTime());
			
			if ( clientResult != sendCount)
				throw new Exception("�������̺� �Է� : "+ Integer.toString(clientResult)+" �߼۵����� : "+ Integer.toString( alClientVO.size() ) );
			else{
				rvo.setbResult(true);
				rvo.setstrDescription(Integer.toString(clientResult)+","+year+","+month+","+logKey+",mms");
			}
			
			//�뷮�߼� ����͸� 
			if ( Integer.parseInt(VbyP.getValue("moniterSendCount")) < sendCount ){
				
				conn = VbyP.getDB();
				AdminSMS asms = AdminSMS.getInstance();
				String tempMessage = ( SLibrary.getByte( message ) > 15 )? SLibrary.cutBytes(message, 20, true, "...") : message ;
				asms.sendAdmin(conn, 
						"[MMS�뷮�߼�]\r\n" + user_id + "\r\n"+Integer.toString( sendCount )+"��\r\n" 
						+ tempMessage  );
			}
				
		}catch (Exception e) {
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			System.out.println(e.toString());
		}
		finally {
			
			try {
				if ( conn != null ) conn.close();
				if ( connLMS != null ) connLMS.close();
			}catch(SQLException e) {
				VbyP.errorLog("sendMMS >> finally conn.close() or connLMS.close() Exception!"+e.toString()); 
			}
		}
		
		VbyP.accessLog(user_id+" >> MMS ���� ��û ��� : "+rvo.getstrDescription());
		return rvo;
	}
	
	public BooleanAndDescriptionVO sendMMSconf(String image, String message, ArrayList<PhoneListVO> al, String returnPhone, String reservationDate, String interval, boolean bMerge   ) {
		
		Connection conn = null;
		Connection connLMS = null;
		MMS mms = MMS.getInstance();
		String user_id = null;
		UserInformationVO mvo = null;
		int sendCount = 0;
		int year = 0;
		int month = 0;
		boolean bReservation = false;
		LogVO lvo = null;
		ArrayList<MMSClientVO> alClientVO = null;
		ArrayList<SMSClientVO> alClientVOSK = null;
		int logKey = 0;
		ArrayList<String[]> phoneAndNameArrayList = null;
		String requestIp = null;
		String line = "";
		String imagePath = "";
		String imageDir = VbyP.getValue("mmsSource");
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		// 20120315 �߰�
		int cnt = 0;
		int minute = 0;
		
		try {
			
			/*###############################
			#		validity check			#
			###############################*/
			//stopWatch play
			StopWatch sw = new StopWatch();
			sw.play();
			
			if (!isLogin()) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			user_id = getSession();
			requestIp = FlexContext.getHttpRequest().getRemoteAddr();
			
			//�߼�ī��Ʈ �ʱ�ȭ
			M.setState(user_id, 0);
			
			if (al == null) throw new Exception("���۸���� ��� �ֽ��ϴ�.");
			
			VbyP.accessLog(user_id+" >> MMS ���� ��û : " + requestIp + " =>["+image+"] ["+message+"] ["+al.size()+"] ["+ returnPhone+"] ["+reservationDate+"] ["+interval+"] ["+bMerge+"]");
			
			if (SLibrary.isNull(image) ) throw new Exception("�̹����� �����ϴ�.");
			String [] arrImage = image.split(";");
			if (arrImage != null && arrImage.length > 0) {
				for (int i = 0; i < arrImage.length; i++) {
					arrImage[i] = imageDir+arrImage[i];
					if (!SLibrary.isFile(arrImage[i]))
						throw new Exception("�̹����� �����ϴ�.");
				}
				
				
				for (String img : arrImage) {
					imagePath += img+";"; 
				}
				if (imagePath.length() > 0)
					imagePath = imagePath.substring(0, imagePath.length()-1);
			}

			// 20120315 �߰�
			if (!SLibrary.isNull(interval)) {
				String [] arrIntervar = interval.split("/");
				if (arrIntervar == null || arrIntervar.length != 2) throw new Exception("�߸��� ���� �����Դϴ�.");
				cnt = SLibrary.intValue(arrIntervar[0]);
				minute = SLibrary.intValue(arrIntervar[1]);
				
				if (cnt <= 0 || minute <= 0) throw new Exception("�߸��� ���� �����Դϴ�.1");
			}
			
			
			if ( !SLibrary.isNull(reservationDate.trim()) )
				bReservation = true;
			if ( bReservation && SLibrary.getTime(reservationDate, "yyyy-MM-dd HH:mm:ss") == 0 )
				throw new Exception("�߸��� ������ ���� ��¥ �Դϴ�.");
			
			if ( bReservation ) {
				
				year = SLibrary.parseInt( SLibrary.getDateTimeStringStandard(reservationDate, "yyyy") );
				month = SLibrary.parseInt( SLibrary.getDateTimeStringStandard(reservationDate, "MM") );
				
				if ( year < SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") ) )
					throw new Exception("���ų����� ���� �Ͻ� �� �����ϴ�.");
				
				if ( year == SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") ) && month < SLibrary.parseInt( SLibrary.getDateTimeString("MM")) )
					throw new Exception("���ſ��� ���� �Ͻ� �� �����ϴ�.");
				
				if ( SLibrary.getTime(reservationDate, "yyyy-MM-dd HH:mm:ss") < (SLibrary.parseLong( SLibrary.getUnixtimeStringSecond() ) + 0)*1000 ){
					VbyP.accessLog(user_id+" >> ���� ���� : "+SLibrary.getTime(reservationDate, "yyyy-MM-dd HH:mm:ss") + "/"+ ((SLibrary.parseLong( SLibrary.getUnixtimeStringSecond() ) + 0)*1000));
					throw new Exception("���Žð����� ���� �Ͻ� �� �����ϴ�.");
				}
				
			}else {
				
				year = SLibrary.parseInt( SLibrary.getDateTimeString("yyyy") );
				month = SLibrary.parseInt( SLibrary.getDateTimeString("MM") );
				reservationDate = SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss");							
			}
				
			if (year == 0 || month == 0)
				throw new Exception("�ش� ����� ���� ���� ���߽��ϴ�.");
			
			conn = VbyP.getDB();
			if (conn == null)
				throw new Exception("DB���ῡ ���� �Ͽ����ϴ�.");
			
			
			mvo = getUserInformation( conn );
			line = mvo.getLine();
			if (line.equals("sk")) mvo.setLine("skmms");
			else if (line.equals("kt")) mvo.setLine("ktmms");
			else if (line.equals("han")) mvo.setLine("ktmms");
			else if (line.equals("hanr")) mvo.setLine("ktmms");
			else if (line.equals("it")) mvo.setLine("ktmms");
			else if (line.equals("pp")) {
				mvo.setLine("ppmms");
				
				imagePath = "";
				arrImage = image.split(";");
				if (arrImage != null && arrImage.length > 0) {
					for (int i = 0; i < arrImage.length; i++) {
						imagePath += SLibrary.replaceAll(arrImage[i], "/mmsImage/", "") +"|"; 
						
						System.out.println(imagePath);
					}
					
					if (imagePath.length() > 0)
						imagePath = imagePath.substring(0, imagePath.length()-1);
				}
			}
			else  mvo.setLine("ktmms");
			
			connLMS = VbyP.getDB("sms1");
								
			if (connLMS == null)
				throw new Exception("MMS DB���ῡ ���� �Ͽ����ϴ�.");
			
			/*###############################
			#		Process					#
			###############################*/
			
			returnPhone = SLibrary.replaceAll(returnPhone, "-", "");
			phoneAndNameArrayList = mms.getPhone(conn, mvo.getUser_id(), al);		
			sendCount = phoneAndNameArrayList.size();
			//message ���๮�� ����
			message = SLibrary.replaceAll(message, "\r", "\n");
			
			checkMMSSend( conn, sendCount, mvo, message, requestIp, imagePath );
			
			// delay send
			int nowHour = SLibrary.parseInt( SLibrary.getDateTimeString("HH") );
			int delayStartHour = SLibrary.intValue( VbyP.getValue("delayStartHour") );
			if (delayStartHour <= 0) delayStartHour = 24;
			int delayEndHour = SLibrary.intValue( VbyP.getValue("delayEndHour") );
			if ( SLibrary.IfNull(VbyP.getValue("delayYN")).equals("Y") ) {
				if ( Integer.parseInt(VbyP.getValue("moniterSendCount")) < sendCount && (delayStartHour <= nowHour || delayEndHour <= nowHour) ) {
					int delayMin = SLibrary.intValue( VbyP.getValue("delayMin") );
					reservationDate = SLibrary.getDateAddSecond(reservationDate, delayMin*60);
					
					VbyP.accessLog(user_id+" >> delay Send "+ reservationDate);
				}
			}
			
			
			/* Send Process */
			//step1
			lvo = mms.getMMSLogVO( mvo, bReservation, message, phoneAndNameArrayList, returnPhone, reservationDate, requestIp);
			logKey = mms.insertLMSLog(conn, lvo, year, month);
			if ( logKey == 0 )
				throw new Exception("���۳��� �αװ� ���� ���� �ʾҽ��ϴ�.");
			VbyP.accessLog(user_id+" >> MMS ���� ��û : �α� ���� ���� ("+logKey+")"+ "��� �ð� : "+sw.getTime());
			
			//step2
			if ( mms.sendMMSPointPut(conn, mvo, sendCount *-1 ) != 1 )
				throw new Exception("�Ǽ� ������ ���� �ʾҽ��ϴ�.");
			VbyP.accessLog(user_id+" >> MMS ���� ��û : �Ǽ� ���� ����" + "��� �ð� : "+sw.getTime());
			
			int clientResult = 0;
			if (line.equals("sk")) {
				SMS sms = SMS.getInstance();
				//step3	
				alClientVOSK = sms.getSMSClientVOMeargeAndInterval(conn, mvo, bReservation, logKey, message, phoneAndNameArrayList, returnPhone, reservationDate, requestIp, cnt, minute, bMerge);
				VbyP.accessLog(user_id+" >> MMS ���� ��û : SK getSMSClientVOMeargeAndInterval ����" + "��� �ð� : "+sw.getTime());
				
				//timeout ������ ���� �ݴ´�.
				try { if ( conn != null ) { conn.close(); conn = null; } } catch(Exception e) { VbyP.errorLog("sendSMS >> conn.close() timeout ����"+e.toString());}
				System.out.println("#### SK MMS : "+imagePath+"####");
				clientResult = sms.insertMMSClientSK(connLMS, alClientVOSK, imagePath);
				
			}else if (line.equals("kt") || line.equals("han")) {
				//step3	
				alClientVO = mms.getMMSClientVOMeargeAndIntervalKT(conn, mvo, bReservation, logKey, message, phoneAndNameArrayList, returnPhone, reservationDate, imagePath, requestIp, cnt, minute, bMerge);
				VbyP.accessLog(user_id+" >> MMS ���� ��û : KT getSMSClientVOMeargeAndInterval ����" + "��� �ð� : "+sw.getTime());
				
				//timeout ������ ���� �ݴ´�.
				try { if ( conn != null ) { conn.close(); conn = null; } } catch(Exception e) { VbyP.errorLog("sendSMS >> conn.close() timeout ����"+e.toString());}
				
				clientResult = mms.insertClient(connLMS, alClientVO, "ktmms");
				
			}else if (line.equals("pp")) {
				//step3	
				alClientVO = mms.getMMSClientVOMeargeAndInterval(conn, mvo, bReservation, logKey, message, phoneAndNameArrayList, returnPhone, reservationDate, imagePath, requestIp, cnt, minute, bMerge);
				
				VbyP.accessLog(user_id+" >> MMS ���� ��û : PP getSMSClientVOMeargeAndInterval ����" + "��� �ð� : "+sw.getTime());
				
				//timeout ������ ���� �ݴ´�.
				try { if ( conn != null ) { conn.close(); conn = null; } } catch(Exception e) { VbyP.errorLog("sendSMS >> conn.close() timeout ����"+e.toString());}
				
				clientResult = mms.insertClient(connLMS, alClientVO, "ppmms");
				
			}else {
				//step3	
				alClientVO = mms.getMMSClientVOMeargeAndIntervalKT(conn, mvo, bReservation, logKey, message, phoneAndNameArrayList, returnPhone, reservationDate, imagePath, requestIp, cnt, minute, bMerge);
				VbyP.accessLog(user_id+" >> MMS ���� ��û : KT getSMSClientVOMeargeAndInterval ����" + "��� �ð� : "+sw.getTime());
				
				//timeout ������ ���� �ݴ´�.
				try { if ( conn != null ) { conn.close(); conn = null; } } catch(Exception e) { VbyP.errorLog("sendSMS >> conn.close() timeout ����"+e.toString());}
				
				clientResult = mms.insertClient(connLMS, alClientVO, "ktmms");
//				//step3	
//				alClientVO = mms.getMMSClientVOMeargeAndInterval(conn, mvo, bReservation, logKey, message, phoneAndNameArrayList, returnPhone, reservationDate, imagePath, requestIp, cnt, minute, bMerge);
//				VbyP.accessLog(user_id+" >> MMS ���� ��û : Dacom getSMSClientVOMeargeAndInterval ����" + "��� �ð� : "+sw.getTime());
//				
//				//timeout ������ ���� �ݴ´�.
//				try { if ( conn != null ) { conn.close(); conn = null; } } catch(Exception e) { VbyP.errorLog("sendSMS >> conn.close() timeout ����"+e.toString());}
//				
//				clientResult = mms.insertClient(connLMS, alClientVO, "sms1");
			}
			
			
			VbyP.accessLog(user_id+" >> MMS ���� ��û : �������̺� ���� ����" + "��� �ð� : "+sw.getTime());
			
			if ( clientResult != sendCount)
				throw new Exception("�������̺� �Է� : "+ Integer.toString(clientResult)+" �߼۵����� : "+ Integer.toString( alClientVO.size() ) );
			else{
				rvo.setbResult(true);
				rvo.setstrDescription(Integer.toString(clientResult)+","+year+","+month+","+logKey+",mms");
			}
			
			//�뷮�߼� ����͸� 
			if ( Integer.parseInt(VbyP.getValue("moniterSendCount")) < sendCount ){
				
				conn = VbyP.getDB();
				AdminSMS asms = AdminSMS.getInstance();
				String tempMessage = ( SLibrary.getByte( message ) > 15 )? SLibrary.cutBytes(message, 20, true, "...") : message ;
				asms.sendAdmin(conn, 
						"[MMS�뷮�߼�]\r\n" + user_id + "\r\n"+Integer.toString( sendCount )+"��\r\n" 
						+ tempMessage  );
			}
				
		}catch (Exception e) {
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			System.out.println(e.toString());
		}
		finally {
			
			try {
				if ( conn != null ) conn.close();
				if ( connLMS != null ) connLMS.close();
			}catch(SQLException e) {
				VbyP.errorLog("sendMMS >> finally conn.close() or connLMS.close() Exception!"+e.toString()); 
			}
		}
		
		VbyP.accessLog(user_id+" >> MMS ���� ��û ��� : "+rvo.getstrDescription());
		return rvo;
	}
	
	private void checkSMSSend( Connection conn, int sendCount, UserInformationVO mvo, String message, String requestIp ) throws Exception {
		
		//�ִ� �߼۰Ǽ�
		if ( Integer.parseInt(VbyP.getValue("maxSendCount")) < sendCount )
			throw new Exception( VbyP.getValue("maxSendCount")+" �� �̻� �߼� �Ͻ� �� �����ϴ�.");
		
		//Ż��ȸ�� üũ
		if( mvo.getLevaeYN().equals("Y") ){
			logout_session();
			throw new Exception("�߸��� �����Դϴ�.");
		}
		
		if( Integer.parseInt(mvo.getPoint()) < sendCount )
			throw new Exception("�ܿ��Ǽ��� �����մϴ�. ( "+ Integer.toString(sendCount)+" / "+ mvo.getPoint()+" )");
		
		//message ���͸�
		if ( Integer.parseInt(VbyP.getValue("filterMinCount")) <= sendCount  ) {
			
			String filterMessage = null;
			String bGlobal = "";
			filterMessage = Filtering.globalMessageFiltering(message);
			if (filterMessage == null )
				filterMessage = Filtering.messageFiltering(mvo.getUser_id(), message);
			else
				bGlobal = "��ü";
			
			if (filterMessage != null) {
				
				VbyP.accessLog(mvo.getUser_id() +" >> ���� ��û : �������� ("+filterMessage+")");
				AdminSMS asms = AdminSMS.getInstance();
				asms.sendAdmin(conn, 
						"M["+bGlobal+"��������]\r\n" + mvo.getUser_id() + "\r\n" 
						+ filterMessage  );
				throw new Exception("���Լ� ������ �߰� �Ǿ����ϴ�.");
			}
		}
		//ip ���͸�
		if ( Filtering.ipFiltering(mvo.getUser_id(), requestIp) != null ) {
			VbyP.accessLog(mvo.getUser_id() +" >> ���� ��û : IP���� ("+Filtering.ipFiltering(mvo.getUser_id(), requestIp)+")");
			throw new Exception("������ ���� �߼��� ���ѵǾ� �ֽ��ϴ�.");
		}
		
		if (SLibrary.getByte(message) > 90) throw new Exception("SMS�� 90byte �̻� �߼� �Ҽ� �����ϴ�.");
		
		//�޽��� ����� ������ �ѱ� Ȯ��
		String isMessage = SMS.getInstance().isMessage(message);
		if ( isMessage != null )
			throw new Exception("["+isMessage+"] ���ڰ� ������� ��߳��ϴ�.�����ϼ���.");
		
	}
	
	private void checkLMSSend( Connection conn, int sendCount, UserInformationVO mvo, String message, String requestIp ) throws Exception {
		
		//�ִ� �߼۰Ǽ�
		if ( Integer.parseInt(VbyP.getValue("maxSendCount")) < sendCount )
			throw new Exception( VbyP.getValue("maxSendCount")+" �� �̻� �߼� �Ͻ� �� �����ϴ�.");
		
		if( mvo.getLevaeYN().equals("Y") ){
			logout_session();
			throw new Exception("�߸��� �����Դϴ�.");
		}
		
		//�Ǽ� üũ
		if( Integer.parseInt(mvo.getPoint()) < sendCount * MMS.LMS_POINT_COUNT )
			throw new Exception("�ܿ��Ǽ��� �����մϴ�. ( "+ Integer.toString(sendCount)+" / "+ mvo.getPoint()+" )");
	
	
		//message ���͸�
		if ( Integer.parseInt(VbyP.getValue("filterMinCount")) <= sendCount  ) {
			
			String filterMessage = null;
			String bGlobal = "";
			filterMessage = Filtering.globalMessageFiltering(message);
			if (filterMessage == null )
				filterMessage = Filtering.messageFiltering(mvo.getUser_id(), message);
			else
				bGlobal = "��ü";
			
			if (filterMessage != null) {
				
				VbyP.accessLog(mvo.getUser_id() +" >> ���� ��û : �������� ("+filterMessage+")");
				AdminSMS asms = AdminSMS.getInstance();
				asms.sendAdmin(conn, 
						"M["+bGlobal+"��������]\r\n" + mvo.getUser_id() + "\r\n" 
						+ filterMessage  );
				throw new Exception("���Լ� ������ �߰� �Ǿ����ϴ�.");
			}
		}
		//ip ���͸�
		if ( Filtering.ipFiltering(mvo.getUser_id(), requestIp) != null ) {
			VbyP.accessLog(mvo.getUser_id() +" >> ���� ��û : IP���� ("+Filtering.ipFiltering(mvo.getUser_id(), requestIp)+")");
			throw new Exception("������ ���� �߼��� ���ѵǾ� �ֽ��ϴ�.");
		}
		
		//�޽��� ����� ������ �ѱ� Ȯ��
		String isMessage = SMS.getInstance().isMessage(message);
		if ( isMessage != null )
			throw new Exception("["+isMessage+"] ���ڰ� ������� ��߳��ϴ�.�����ϼ���.");
		
	}
	
	private void checkMMSSend( Connection conn, int sendCount, UserInformationVO mvo, String message, String requestIp, String imagePath ) throws Exception {
		
		//�ִ� �߼۰Ǽ�
		if ( Integer.parseInt(VbyP.getValue("maxSendCount")) < sendCount )
			throw new Exception( VbyP.getValue("maxSendCount")+" �� �̻� �߼� �Ͻ� �� �����ϴ�.");
		
		if( mvo.getLevaeYN().equals("Y") ){
			logout_session();
			throw new Exception("�߸��� �����Դϴ�.");
		}
		
		if (SLibrary.isNull(imagePath)) throw new Exception("�̹����� �����ϴ�.");
		
		//�Ǽ� üũ
		if( Integer.parseInt(mvo.getPoint()) < sendCount * MMS.MMS_POINT_COUNT )
			throw new Exception("�ܿ��Ǽ��� �����մϴ�. ( "+ Integer.toString(sendCount)+" / "+ mvo.getPoint()+" )");
	
	
		//message ���͸�
		if ( Integer.parseInt(VbyP.getValue("filterMinCount")) <= sendCount  ) {
			
			String filterMessage = null;
			String bGlobal = "";
			filterMessage = Filtering.globalMessageFiltering(message);
			if (filterMessage == null )
				filterMessage = Filtering.messageFiltering(mvo.getUser_id(), message);
			else
				bGlobal = "��ü";
			
			if (filterMessage != null) {
				
				VbyP.accessLog(mvo.getUser_id() +" >> ���� ��û : �������� ("+filterMessage+")");
				AdminSMS asms = AdminSMS.getInstance();
				asms.sendAdmin(conn, 
						"M["+bGlobal+"��������]\r\n" + mvo.getUser_id() + "\r\n" 
						+ filterMessage  );
				throw new Exception("���Լ� ������ �߰� �Ǿ����ϴ�.");
			}
		}
		//ip ���͸�
		if ( Filtering.ipFiltering(mvo.getUser_id(), requestIp) != null ) {
			VbyP.accessLog(mvo.getUser_id() +" >> ���� ��û : IP���� ("+Filtering.ipFiltering(mvo.getUser_id(), requestIp)+")");
			throw new Exception("������ ���� �߼��� ���ѵǾ� �ֽ��ϴ�.");
		}
		
		//�޽��� ����� ������ �ѱ� Ȯ��
		String isMessage = SMS.getInstance().isMessage(message);
		if ( isMessage != null )
			throw new Exception("["+isMessage+"] ���ڰ� ������� ��߳��ϴ�.�����ϼ���.");
		
	}
	
	public BooleanAndDescriptionVO saveReturnPhone(String returnPhone) {
		
		Connection conn = null;
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		String rp = SLibrary.IfNull(returnPhone);
		PreparedExecuteQueryManager pq = null;
		
		
		try {
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			conn = VbyP.getDB();
			if (conn == null) throw new Exception("DB������ �Ǿ� ���� �ʽ��ϴ�.");
			
			VbyP.accessLog(" >> ȸ�Ź�ȣ ���� ��û "+ rp +" , "+ user_id);
			
			pq = new PreparedExecuteQueryManager();
			pq.setPrepared(conn, VbyP.getSQL("updateReturnPhone"));
			
			pq.setString(1, rp);
			pq.setString(2, user_id);
			pq.executeUpdate();

			rvo.setbResult(true);
			
		}catch (Exception e) {
			
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			
		}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("addGroup >> conn.close() Exception!"); }
			
			conn = null;
		}
		
		return rvo;
	}
	
	/*###############################
	#	address_book				#
	###############################*/
	public String getAddressOfGroup() {
		
		Connection conn = null;
		Address address = null;
		StringBuffer buf = new StringBuffer();
		
		try {
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			conn = VbyP.getDB();
			if (conn == null) throw new Exception("DB������ �Ǿ� ���� �ʽ��ϴ�.");
			address = Address.getInstance();
			
			VbyP.accessLog(" >> �׷캰 ����Ʈ ��û "+ user_id);
			buf = address.SelectTreeData(conn, user_id);
			
		}catch (Exception e) { VbyP.errorLogDaily("getAddressOfGroup >>"+e.toString()); }	
		finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("getAddressOfGroup >> conn.close() Exception!"); }
			conn = null;
		}
		return buf.toString();
	}
	
	public ArrayList<HashMap<String, String>> getAddress() {
		
		Connection conn = null;
		Address address = null;
		ArrayList<HashMap<String, String>> al = null;
		
		try {
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			conn = VbyP.getDB();
			if (conn == null) throw new Exception("DB������ �Ǿ� ���� �ʽ��ϴ�.");
			address = Address.getInstance();
			
			VbyP.accessLog(" >> �ּҺ� ����Ʈ ��û "+ user_id);
			al = address.SelectMember(conn, user_id);
			
		}catch (Exception e) { VbyP.errorLogDaily("getAddress >>"+e.toString()); }	
		finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("getAddress >> conn.close() Exception!"); }
			conn = null;
		}
		return al;
	}
	
	public ArrayList<HashMap<String, String>> getAddressSearch(String search) {
		
		Connection conn = null;
		Address address = null;
		ArrayList<HashMap<String, String>> al = null;
		
		String s = SLibrary.IfNull(search);
		
		try {
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			conn = VbyP.getDB();
			if (conn == null) throw new Exception("DB������ �Ǿ� ���� �ʽ��ϴ�.");
			address = Address.getInstance();
			
			VbyP.accessLog(" >> �ּҺ� ����Ʈ ��û "+ user_id);
			al = address.SearchMember(conn, user_id,s);
			
		}catch (Exception e) { VbyP.errorLogDaily("getAddress >>"+e.toString()); }	
		finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("getAddress >> conn.close() Exception!"); }
			conn = null;
		}
		return al;
	}
	
	public String getAddressAllSend() {
		
		Connection conn = null;
		Address address = null;
		ArrayList<HashMap<String, String>> al = null;
		StringBuffer buf  = new StringBuffer();
		try {
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			conn = VbyP.getDB();
			if (conn == null) throw new Exception("DB������ �Ǿ� ���� �ʽ��ϴ�.");
			address = Address.getInstance();
			
			VbyP.accessLog(" >> ��ü �ּ� ����Ʈ ��û "+ user_id);
			al = address.SelectMember(conn, user_id);
			
			int cnt = al.size();
			HashMap<String, String> hm = null;
			
			for (int i = 0; i < cnt; i++) {
				hm = al.get(i);
				buf.append(SLibrary.IfNull(hm, "phone")+"||"+SLibrary.IfNull(hm, "name")+",");
			}
			buf.setLength(buf.length()-1);
			
		}catch (Exception e) { VbyP.errorLogDaily("getAddress >>"+e.toString()); }	
		finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("getAddress >> conn.close() Exception!"); }
			conn = null;
		}
		return buf.toString();
	}
	
	public BooleanAndDescriptionVO addGroup(String groupName) {
		
		Connection conn = null;
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		Address address = null;
		
		try {
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			conn = VbyP.getDB();
			if (conn == null) throw new Exception("DB������ �Ǿ� ���� �ʽ��ϴ�.");
			address = Address.getInstance();
			
			VbyP.accessLog(" >> �׷� ���� ��û "+ groupName +" , "+ user_id);
						
			if ( address.checkDuplicationGroup(conn, user_id, groupName) )
				throw new Exception(groupName+" �׷��� ���� �մϴ�.");
			
			if ( address.InsertGroup(conn, user_id, groupName) < 1)
				throw new Exception("�׷� ������ ���� �Ͽ����ϴ�.");
			
			rvo.setbResult(true);
			
		}catch (Exception e) {
			
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			
		}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("addGroup >> conn.close() Exception!"); }
			conn = null;
		}
		
		return rvo;
	}
	public BooleanAndDescriptionVO modifyGroup(String oldGroupName, String groupName) {
		
		Connection conn = null;
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		Address address = null;
		
		try {
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			conn = VbyP.getDB();
			if (conn == null) throw new Exception("DB������ �Ǿ� ���� �ʽ��ϴ�.");
			address = Address.getInstance();
			
			VbyP.accessLog(" >> �׷� ���� ��û "+ oldGroupName + " >> " +groupName +" , "+ user_id);
			
			if ( address.UpdateGroup(conn, oldGroupName, user_id, groupName) < 1)						
				throw new Exception("�׷������ ���� �Ͽ����ϴ�.");
			
			rvo.setbResult(true);
			
		}catch (Exception e) {
			
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			
		}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("modifyGroup >> conn.close() Exception!"); }
			conn = null;
		}
		
		return rvo;
	}
	
	public BooleanAndDescriptionVO deleteGroup(String groupName) {
		
		Connection conn = null;
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		Address address = null;
		
		try {
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			conn = VbyP.getDB();
			if (conn == null) throw new Exception("DB������ �Ǿ� ���� �ʽ��ϴ�.");
			address = Address.getInstance();
			
			VbyP.accessLog(" >> �׷� ���� ��û "+ groupName +" , "+ user_id);
			
			if ( address.DeleteGroup(conn, groupName, user_id) < 1 )				
				throw new Exception("������ ���� �Ͽ����ϴ�.");
	
			
			rvo.setstrDescription(groupName);
			rvo.setbResult(true);
			
		}catch (Exception e) {
			
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			
		}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("addGroup >> conn.close() Exception!"); }
			conn = null;
		}
		
		return rvo;
	}
	public BooleanAndDescriptionVO addAddress(String groupName, String phone, String name, String memo) {
		
		Connection conn = null;
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		Address address = null;
		AddressVO vo = new AddressVO();
		
		try {
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			conn = VbyP.getDB();
			if (conn == null) throw new Exception("DB������ �Ǿ� ���� �ʽ��ϴ�.");
			address = Address.getInstance();
			
			VbyP.accessLog(" >> ��ȭ��ȣ ���� ��û "+ user_id);

			vo.setUser_id(user_id);
			vo.setGrp(AddressVO.ADDRESS_FLAG);
			vo.setGrpName(groupName);
			vo.setPhone(phone);
			vo.setName(name);
			vo.setMemo(memo);
			
			if ( address.InsertMember(conn, vo) < 1)
				throw new Exception("��ȣ ���忡 ���� �Ͽ����ϴ�.");
			
			rvo.setstrDescription(vo.getGrpName());
			rvo.setbResult(true);
			
		}catch (Exception e) {
			
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			
		}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("addGroup >> conn.close() Exception!"); }
			conn = null;
		}
		
		return rvo;
	}
	public BooleanAndDescriptionVO modifyAddress(int modifyAddressIdx, String groupName, String phone, String name, String memo) {
		
		Connection conn = null;
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		Address address = null;
		AddressVO vo = new AddressVO();
		
		try {
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			conn = VbyP.getDB();
			if (conn == null) throw new Exception("DB������ �Ǿ� ���� �ʽ��ϴ�.");
			address = Address.getInstance();
			
			VbyP.accessLog(" >> ��ȭ��ȣ ���� ��û  "+ Integer.toString(vo.getIdx()) + user_id);
			
			vo.setIdx(modifyAddressIdx);
			vo.setbModify(true);
			vo.setUser_id(user_id);
			vo.setGrp(AddressVO.ADDRESS_FLAG);
			vo.setGrpName(groupName);
			vo.setPhone(phone);
			vo.setName(name);
			vo.setMemo(memo);
			
			if ( address.UpdateMember(conn, vo.getIdx(), vo) < 1)
				throw new Exception("��ȣ ������ ���� �Ͽ����ϴ�.");
			
			rvo.setstrDescription(vo.getGrpName());

			rvo.setbResult(true);
			
		}catch (Exception e) {
			
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			
		}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("addGroup >> conn.close() Exception!"); }
			conn = null;
		}
		
		return rvo;
	}
	
	public BooleanAndDescriptionVO deleteAddress(int deleteAddressIdx) {
		
		Connection conn = null;
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		Address address = null;
		
		try {
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			conn = VbyP.getDB();
			if (conn == null) throw new Exception("DB������ �Ǿ� ���� �ʽ��ϴ�.");
			address = Address.getInstance();
			
			VbyP.accessLog(" >> ��ȭ��ȣ ���� ��û "+ Integer.toString(deleteAddressIdx) +" , "+ user_id);
			if ( address.DeleteMember(conn, deleteAddressIdx, user_id) < 1)
				throw new Exception("��ȣ ������ ���� �Ͽ����ϴ�.");

			rvo.setbResult(true);
			
		}catch (Exception e) {
			
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			
		}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("addGroup >> conn.close() Exception!"); }
			conn = null;
		}
		
		return rvo;
	}
	
	/*###############################
	#	billing						#
	################################*/
	public BooleanAndDescriptionVO setCash( String account, String amount, String method, String reqname) {
		
		Connection conn = null;
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		Billing billing = null;
		
		try {
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			conn = VbyP.getDB();
			if (conn == null) throw new Exception("DB������ �Ǿ� ���� �ʽ��ϴ�.");
			billing = Billing.getInstance();
			
			VbyP.accessLog(" >> ������ ���� ��û "+ user_id);
			
			rvo = billing.setCash( conn , user_id, account, amount, method, reqname );
			
			AdminSMS asms = AdminSMS.getInstance();
			String tempMessage = "[�����忹��] "+user_id+" , "+reqname+" , "+account+" , "+amount+" , "+method;
			asms.sendAdmin(conn, tempMessage );
			
		}catch (Exception e) {
			
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			
		}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("setCash >> conn.close() Exception!"); }
			conn = null;
		}
		
		return rvo;
	}
	
	/*###############################
	#	excel						#
	###############################*/
	public ExcelLoaderResultVO getExcelLoaderData(byte[] bytes, String fileName){
		
		VbyP.accessLog(" >> �����δ� ��û ");
		ExcelLoaderResultVO evo = new ExcelLoaderResultVO();
		String path = VbyP.getValue("excelUploadPath");

		ExcelLoader el = new ExcelLoader();
		String uploadFileName = "";
		evo.setbResult(true);
		
		try {
			uploadFileName = el.uploadExcelFile(bytes, path, fileName);
		}catch(Exception e){
			evo.setbResult(false);
			evo.setstrDescription("���� ������ ���ε� ���� �ʾҽ��ϴ�.");
		}
		
		try {
			evo.setList( el.getExcelData(path, uploadFileName) );
		}catch(IOException ie) {
			System.out.println(ie.toString());
		}catch(Exception e) {
			System.out.println(e.toString());
			evo.setbResult(false);
			evo.setstrDescription("���Ŀ� ��߳� ���� ���� �Դϴ�. ���������� ���� ���Ͽ����ϴ�.");
		}
		finally {		 
			new File(path + uploadFileName).delete();
		}
	    
		return evo;
	}
	public BooleanAndDescriptionVO addAddress(ArrayList<AddressVO> al) {
		
		Connection conn = null;
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		Address address = null;
		
		try {
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			conn = VbyP.getDB();
			if (conn == null) throw new Exception("DB������ �Ǿ� ���� �ʽ��ϴ�.");
			address = Address.getInstance();
			
			VbyP.accessLog(" >> �����δ� �ּҷ� ���� ��û "+ user_id);
			
			if ( address.InsertMember( conn , user_id, al ) < 1)
				throw new Exception("��ȣ ���忡 ���� �Ͽ����ϴ�.");
			
			rvo.setbResult(true);
			
		}catch (Exception e) {
			
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			
		}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("addGroup >> conn.close() Exception!"); }
			conn = null;
		}
		
		return rvo;
	}
	
	public String[] getAddressGroup() {
		
		Connection conn = null;
		Address address = null;
		String[] arr = null;
		
		try {
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			conn = VbyP.getDB();
			if (conn == null) throw new Exception("DB������ �Ǿ� ���� �ʽ��ϴ�.");
			address = Address.getInstance();
			
			VbyP.accessLog(" >> �׷� ����Ʈ ��û "+ user_id);
			arr = address.SelectGroup(conn, user_id);
			
			
		}catch (Exception e) { VbyP.errorLogDaily("getAddressGroup >>"+e.toString()); }	
		finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("getAddressGroup >> conn.close() Exception!"); }
			conn = null;
		}
		return arr;
	}
	
	/*###############################
	#	sent						#
	###############################*/
	public List<SentGroupVO> getSentGroupList( String fromDate, String endDate, boolean bReservation) {

		
		Connection conn = null;
		List<SentGroupVO> list = null;
		
		SentFactory sf = null;
		sf = SentFactory.getInstance();
		
		if (isLogin()) {		
		
			try {
				
				conn = VbyP.getDB();
				String user_id = getSession();
				VbyP.accessLog(user_id+" >> ���۳��� �׷� ��û :"+fromDate+"~"+endDate+","+bReservation);
				
				if (user_id != null && !user_id.equals("")) {
					
					list = sf.getSentGroupList(conn, user_id, fromDate, endDate, bReservation);
				}
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("getSentGroupList >> conn.close() Exception!"); }
				conn = null;
			}
		}
		
		return list;
	}
	public List<SentVO> getSentList(int groupIndex, String line) {

		
		Connection connSMS = null;
		List<SentVO> list = null;
		
		ArrayList<SentVO> list2 = null;
		
		SentFactoryAble sf = null;
		sf = SentFactory.getInstance();
		
		SentLMSFactory sf2 = null;
			
		if (isLogin()) {		
		
			try {
				if (line.equals("mms") || line.equals("ktmms")  || line.equals("ppmms")) {	
					connSMS = VbyP.getDB("sms1");
					sf = SentLMSFactory.getInstance();
				}
				else connSMS = VbyP.getDB(line);
				
				String user_id = getSession();
				VbyP.accessLog(user_id+" >> "+line+" ���۳��� ��û :"+ Integer.toString(groupIndex));
				
				if (user_id != null && !user_id.equals("")) {
					
					if (user_id.equals("hhhyc") && line.equals("ktmms")) {
						sf2 = SentLMSFactory.getInstance();
						list2 = sf2.getSentListTemp(connSMS, user_id, line, Integer.toString(groupIndex) );
						list = sf2.getSentListAdd(list2, connSMS, user_id, "mms", Integer.toString(groupIndex) );
					}
					else {
						list = sf.getSentList(connSMS, user_id, line, Integer.toString(groupIndex) );
					}
				}
			}catch (Exception e) {}	finally {			
				try { if ( connSMS != null ) connSMS.close();
				}catch(SQLException e) { VbyP.errorLog("getSentGroupList >> conn.close() Exception!"); }
				connSMS = null;
			}
		}
		
		return list;
	}
	
	public BooleanAndDescriptionVO deleteSent(int groupIndex, String line) {

		
		Connection conn = null;
		Connection connSMS = null;
		
		SentFactory sf = null;
		sf = SentFactory.getInstance();
		
		BooleanAndDescriptionVO bvo = null;
		
		UserInformationVO mvo = null;
			
		if (isLogin()) {		
		
			try {
				
				conn = VbyP.getDB();
				if (line.equals("mms") || line.equals("ktmms")) {	
					connSMS = VbyP.getDB("sms1");
				}
				else connSMS = VbyP.getDB(line);
				
				
				String user_id = getSession();
				mvo = getUserInformation( conn );
				
				VbyP.accessLog(user_id+" >> "+line+" ���۳��� ���� ��û :"+ Integer.toString(groupIndex));
				
				if (user_id != null && !user_id.equals("") && groupIndex > 0 && !SLibrary.isNull(line)) {
					
					bvo = sf.deleteSentGroupList( conn, connSMS, user_id, groupIndex, line, mvo );
				}
			}catch (Exception e) {}	finally {			
				try { 
					if ( conn != null ) conn.close();
					if ( connSMS != null ) connSMS.close();
				}catch(SQLException e) { VbyP.errorLog("getSentGroupList >> conn.close() Exception!"); }
				conn = null;
			}
		}
		
		return bvo;
	}
	
	public BooleanAndDescriptionVO cancelSent( int idx, String sendLine) {
		
		Connection conn = null;
		Connection connSMS = null;
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		
		SentFactoryAble sf = null;
		sf = SentFactory.getInstance();
		
		try {	
				String user_id = getSession();
				if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
				VbyP.accessLog(user_id+" >> ���� ��� ��û :"+idx+","+sendLine);			
				
				conn = VbyP.getDB();
				
				if (conn == null) throw new Exception("DB������ �Ǿ� ���� �ʽ��ϴ�.");								
					
				UserInformationVO vo = getUserInformation();
				
				if (sendLine.equals("mms")||sendLine.equals("ppmms")) {	
					connSMS = VbyP.getDB("sms1");
					sf = SentLMSFactory.getInstance();
				}else connSMS = VbyP.getDB(sendLine);
				
				if (connSMS == null) throw new Exception("SMS DB������ �Ǿ� ���� �ʽ��ϴ�.("+sendLine+")");
				rvo = sf.cancelSentGroupList(conn, connSMS, vo, idx, sendLine);
				
				
				if (rvo.getbResult()) {
					rvo.setstrDescription("��� �Ǿ����ϴ�.");
					VbyP.accessLog(user_id+" >> ���� ��� ���� :"+idx+","+sendLine);
				}
				else {
					VbyP.accessLog(user_id+" >> ���� ��� ���� :"+rvo.getstrDescription());
				}
			
		}catch (Exception e) {
			
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			
		}	finally {			
			try { 
				if ( conn != null ) conn.close();
				if (connSMS != null) connSMS.close();
			}catch(SQLException e) { VbyP.errorLog("cancelSentGroupList >> conn.close() Exception!"); }
			conn = null;
			connSMS = null;
		}
		return rvo;
	}
	
	
	public String[] getHomeEmoti() {

		
		Connection conn = null;
		String [] arr = null;
		
		try {
			
			conn = VbyP.getDB();
			VbyP.accessLog(" >>  Home �̸�Ƽ�� ��û ");
			
			StringBuffer buf = new StringBuffer();
			buf.append(VbyP.getSQL("homeEmoti"));
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, buf.toString() );
			arr = pq.ExecuteQuery();
			
		}catch (Exception e) {}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("getHomeEmoti >> conn.close() Exception!"); }
			conn = null;
		}
		
		return arr;
	}
	
	public String[] getEmotiCateList(String gubun) {
		
		Connection conn = null;
		Home home = null;
		String [] arr = null;
		try {
			conn = VbyP.getDB();
			home = Home.getInstance();
			arr = home.getMainCate(conn, gubun);
			
		}catch (Exception e) {}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("getEmotiCateList >> conn.close() Exception!"); }
			conn = null;
		}
		
		return arr;
	}
	
	public String[] getEmotiCateListLMS(String gubun) {
		
		Connection conn = null;
		Home home = null;
		String [] arr = null;
		try {
			conn = VbyP.getDB();
			home = Home.getInstance();
			arr = home.getLMSCate(conn, gubun);
			
		}catch (Exception e) {}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("getEmotiCateList >> conn.close() Exception!"); }
			conn = null;
		}
		
		return arr;
	}
	
	public String[] getEmotiCateListMMS(String gubun) {
		
		Connection conn = null;
		Home home = null;
		String [] arr = null;
		try {
			conn = VbyP.getDB();
			home = Home.getInstance();
			arr = home.getMMSCate(conn, gubun);
			
		}catch (Exception e) {}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("getEmotiCateList >> conn.close() Exception!"); }
			conn = null;
		}
		
		return arr;
	}
	
	public ArrayList<HashMap<String, String>> getEmotiCatePage(String gubun, String category, int page, int count) {
		
		Connection conn = null;
		ArrayList<HashMap<String, String>> al = null;
		
		int from = 0;
		
		try {
			
			conn = VbyP.getDB();
			
			page += 1;
			from = count * (page -1);
			
			VbyP.accessLog(" >>  �̸�Ƽ�� page ��û("+gubun+"/"+category+"/"+page+"/"+count+") "+Integer.toString(from));
			
			StringBuffer buf = new StringBuffer();
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			if (!gubun.equals("my")) {
				buf.append(VbyP.getSQL("homeEmotiCatePage"));
				
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, gubun);
				pq.setString(2, ""+category+"%");
				pq.setString(3, gubun);
				pq.setString(4, ""+category+"%");
				pq.setInt(5, from);
				pq.setInt(6, count);
			} else {
				buf.append(VbyP.getSQL("select_mymsgPage"));
				
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, SLibrary.IfNull( super.getSession() ));
				pq.setString(2, SLibrary.IfNull( super.getSession() ));
				pq.setInt(3, from);
				pq.setInt(4, count);
			}
			
			
			al = pq.ExecuteQueryArrayList();
			
		}catch (Exception e) {}	finally {			
			try { 
				if ( conn != null ) 
				conn.close();
			}catch(SQLException e) { VbyP.errorLog("getEmotiCatePage >> conn.close() Exception!"); }
			conn = null;
		}
		
		return al;
	}
	
	public ArrayList<HashMap<String, String>> getEmotiCatePageLMS(String gubun, String category, int page, int count) {
		
		Connection conn = null;
		ArrayList<HashMap<String, String>> al = null;
		
		int from = 0;
		
		try {
			
			conn = VbyP.getDB();
			
			page += 1;
			from = count * (page -1);
			
			VbyP.accessLog(" >>  �̸�Ƽ�� lms page ��û("+gubun+"/"+category+"/"+page+"/"+count+") "+Integer.toString(from));
			
			StringBuffer buf = new StringBuffer();
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			if (!gubun.equals("my")) {
				buf.append(VbyP.getSQL("homeEmotiCatePageLMS"));
				
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, gubun);
				pq.setString(2, ""+category+"%");
				pq.setString(3, gubun);
				pq.setString(4, ""+category+"%");
				pq.setInt(5, from);
				pq.setInt(6, count);
			} else {
				buf.append(VbyP.getSQL("select_mymsgPage"));
				
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, SLibrary.IfNull( super.getSession() ));
				pq.setString(2, SLibrary.IfNull( super.getSession() ));
				pq.setInt(3, from);
				pq.setInt(4, count);
			}
			
			al = pq.ExecuteQueryArrayList();
			
		}catch (Exception e) {}	finally {			
			try { 
				if ( conn != null ) 
				conn.close();
			}catch(SQLException e) { VbyP.errorLog("getEmotiCatePageLMS >> conn.close() Exception!"); }
			conn = null;
		}
		
		return al;
	}
	
	
	public ArrayList<HashMap<String, String>> getEmotiCatePageMMS(String gubun, String category, int page, int count) {
		
		Connection conn = null;
		ArrayList<HashMap<String, String>> al = null;
		
		int from = 0;
		
		try {
			
			conn = VbyP.getDB();
			
			page += 1;
			from = count * (page -1);
			
			VbyP.accessLog(" >>  �̸�Ƽ�� mms page ��û("+gubun+"/"+category+"/"+page+"/"+count+") "+Integer.toString(from));
			
			StringBuffer buf = new StringBuffer();
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			if (!gubun.equals("my")) {
				buf.append(VbyP.getSQL("homeEmotiCatePageMMS"));
				
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, gubun);
				pq.setString(2, ""+category+"%");
				pq.setString(3, gubun);
				pq.setString(4, ""+category+"%");
				pq.setInt(5, from);
				pq.setInt(6, count);
			} else {
				buf.append(VbyP.getSQL("select_mymsgPage"));
				
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, SLibrary.IfNull( super.getSession() ));
				pq.setString(2, SLibrary.IfNull( super.getSession() ));
				pq.setInt(3, from);
				pq.setInt(4, count);
			}
			
			
			al = pq.ExecuteQueryArrayList();
			
		}catch (Exception e) {}	finally {			
			try { 
				if ( conn != null ) 
				conn.close();
			}catch(SQLException e) { VbyP.errorLog("getEmotiCatePageMMS >> conn.close() Exception!"); }
			conn = null;
		}
		
		return al;
	}
	
	public String[] getEmotiCate(String gubun, String category, int page) {

		
		Connection conn = null;
		String [] arr = null;
		int count = 8;
		
		int from = 0;
		
		try {
			
			conn = VbyP.getDB();
			
			page += 1;
			from = count * (page -1);
			
			VbyP.accessLog(" >>  �̸�Ƽ�� ��û("+gubun+"/"+category+") "+Integer.toString(from));
			
			StringBuffer buf = new StringBuffer();
			buf.append(VbyP.getSQL("homeEmotiCate"));
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, buf.toString() );
			pq.setString(1, gubun);
			pq.setString(2, "%"+category+"%");
			pq.setInt(3, from);
			pq.setInt(4, count);
			
			arr = pq.ExecuteQuery();
			
		}catch (Exception e) {}	finally {			
			try { 
				if ( conn != null ) 
				conn.close();
			}catch(SQLException e) { VbyP.errorLog("getEmotiCate >> conn.close() Exception!"); }
			conn = null;
		}
		
		return arr;
	}
	
	public ArrayList<HashMap<String, String>> getMymsg(int page) {

		
		Connection conn = null;
		ArrayList<HashMap<String, String>> al = null;
		int count = 8;
		
		int from = 0;
		
		try {
			

			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			
			conn = VbyP.getDB();
			
			page += 1;
			from = count * (page -1);
			
			VbyP.accessLog(" >>  ��Ϲ��� ��û "+Integer.toString(from));
			
			StringBuffer buf = new StringBuffer();
			buf.append(VbyP.getSQL("select_mymsg"));
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, buf.toString() );
			pq.setString(1, user_id);
			pq.setInt(2, from);
			pq.setInt(3, count);
			
			al = pq.ExecuteQueryArrayList();
			
		}catch (Exception e) {}	finally {			
			try { 
				if ( conn != null ) 
				conn.close();
			}catch(SQLException e) { VbyP.errorLog("getMymsg >> conn.close() Exception!"); }
			conn = null;
		}
		
		return al;
	}
	
	public String[] getHomeEmotiCate(String gubun, String category, int page) {

		
		Connection conn = null;
		String [] arr = null;
		int count = 4;
		
		int from = 0;
		
		try {
			
			conn = VbyP.getDB();
			
			page += 1;
			from = count * (page -1);
			
			VbyP.accessLog(" >>  Ȩ �̸�Ƽ�� ��û("+gubun+"/"+category+") "+Integer.toString(from));
			
			StringBuffer buf = new StringBuffer();
			buf.append(VbyP.getSQL("homeEmotiCate"));
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, buf.toString() );
			pq.setString(1, gubun);
			pq.setString(2, "%"+category+"%");
			pq.setInt(3, from);
			pq.setInt(4, count);
			
			arr = pq.ExecuteQuery();
			
		}catch (Exception e) {}	finally {			
			try { 
				if ( conn != null ) 
					conn.close();
			}catch(SQLException e) { VbyP.errorLog("getHomeEmotiCate >> conn.close() Exception!"); }
			conn = null;
		}
		
		return arr;
	}
	
	public String[] getHomeMymsg(int page) {

		
		Connection conn = null;
		String [] arr = null;
		int count = 4;
		
		int from = 0;
		
		try {
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			
			conn = VbyP.getDB();
			
			page += 1;
			from = count * (page -1);
			
			VbyP.accessLog(" >>  Ȩ ��Ϲ��� ��û "+Integer.toString(from));
			
			StringBuffer buf = new StringBuffer();
			buf.append(VbyP.getSQL("select_mymsg"));
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, buf.toString() );
			pq.setPrepared( conn, buf.toString() );
			pq.setString(1, user_id);
			pq.setInt(2, from);
			pq.setInt(3, count);
			
			arr = pq.ExecuteQuery();
			
		}catch (Exception e) {}	finally {			
			try { 
				if ( conn != null ) 
					conn.close();
			}catch(SQLException e) { VbyP.errorLog("getHomeMymsg >> conn.close() Exception!"); }
			conn = null;
		}
		
		return arr;
	}
	
	public String[] getEmoti(int page, String category) {
		

		Connection conn = null;
		String [] arr = null;
		int count = 8;
		
		int from = 0;
		
		try {
			
			conn = VbyP.getDB();
			
			if (page == 0) page = 1;
			from = count * (page -1);
			
			VbyP.accessLog(" >>  �̸�Ƽ�� ��û("+category+") "+Integer.toString(from));
			
			StringBuffer buf = new StringBuffer();
			buf.append(VbyP.getSQL("emoti"));
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, buf.toString() );
			pq.setString(1, category);
			pq.setInt(2, from);
			pq.setInt(3, count);
			
			arr = pq.ExecuteQuery();
			
		}catch (Exception e) {}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("getEmoti >> conn.close() Exception!"); }
			conn = null;
		}
		
		return arr;
	}
	
	
	
	// DS
	public ArrayList<NoticVO> getNotic() {
		
		Connection conn = null;
		ArrayList<NoticVO> rslt = null;
		NoticDAO nd = null;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  �������� ����Ʈ ��û");
			
			rslt = nd.getList(conn);
			
		}catch (Exception e) {}	
		finally {
			try { 
				if ( conn != null ) 
					conn.close();
			}catch(SQLException e) { VbyP.errorLog("getNotic >> conn.close() Exception!"); }
			conn = null;
		}
		
		return rslt;
	}
	
	// FAQ
	public ArrayList<NoticVO> getFaq() {
			
		Connection conn = null;
		ArrayList<NoticVO> rslt = null;
		NoticDAO nd = null;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  FAQ ����Ʈ ��û");
			
			rslt = nd.getListFAQ(conn);
			
		}catch (Exception e) {}	
		finally {
			try { 
				if ( conn != null ) 
					conn.close();
			}catch(SQLException e) { VbyP.errorLog("getFaq >> conn.close() Exception!"); }
			conn = null;
		}
		
		return rslt;
	}
	
	public ArrayList<NoticVO> getNoticMain(int cnt) {
		
		Connection conn = null;
		ArrayList<NoticVO> rslt = null;
		NoticDAO nd = null;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  �������� ����Ʈ ��û(main)");
			
			rslt = nd.getList(conn,cnt);
			
		}catch (Exception e) {}	
		finally {
			try { 
				if ( conn != null ) 
					conn.close();
			}catch(SQLException e) { VbyP.errorLog("getNoticMain >> conn.close() Exception!"); }
			conn = null;
		}
		
		return rslt;
	}
	
	
	
	public int updateCntNotic(int idx) {
		
		Connection conn = null;
		NoticDAO nd = null;
		int rslt = 0;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  �������� ī��Ʈ ���� ��û");
			
			rslt = nd.updateCnt(conn, idx);
			
		}catch (Exception e) {}	
		finally {
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("updateCntNotic >> conn.close() Exception!"); }
		}
		
		return rslt;
	}
	
	/**
	 * ��� ���� ����
	 */
	public BooleanAndDescriptionVO addMymsg(String msg) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> ��Ϲ��� �߰� "+msg);
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		

		try {
			conn = VbyP.getDB();
			
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			
			StringBuffer buf = new StringBuffer();
			buf.append(VbyP.getSQL("insert_mymsg"));
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, buf.toString() );
			pq.setString(1, user_id);
			pq.setString(2, msg);
			pq.executeUpdate();
			
			rvo.setbResult(true);
		}catch (Exception e) {
			
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			
		}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("addMymsg >> conn.close() Exception!"); }
		}

		return rvo;
		
	}
	
	/**
	 * ��� ���� ����
	 */
	public BooleanAndDescriptionVO delMymsg(int idx) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> ��Ϲ��� ���� "+idx);
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		

		try {
			conn = VbyP.getDB();
			
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			
			StringBuffer buf = new StringBuffer();
			buf.append(VbyP.getSQL("delete_mymsg"));
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, buf.toString() );
			pq.setString(1, user_id);
			pq.setInt(2, idx);
			pq.executeUpdate();
			
			rvo.setbResult(true);
		}catch (Exception e) {
			
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			
		}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("delMymsg >> conn.close() Exception!"); }
		}

		return rvo;
		
	}
	
	/*###############################
	#	coupon						#
	###############################*/
	public String setCoupon(String key) {
		
		Connection conn = null;
		String resultString = "";
		
		try {
			String user_id = getSession();
			if (user_id == null || user_id.equals("")) throw new Exception("�α��� �Ǿ� ���� �ʽ��ϴ�.");
			conn = VbyP.getDB();
			if (conn == null) throw new Exception("DB������ �Ǿ� ���� �ʽ��ϴ�.");
			if (SLibrary.isNull(key)) throw new Exception("������ȣ�� �Է��ϼ���.");
			
			VbyP.accessLog(" >> ���� ��û "+ user_id+" : "+ key);
			
			String SQL = VbyP.getSQL( "couponUserSelect" );
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared(conn, SQL);
			pq.setString(1, user_id);
			String usedKey = pq.ExecuteQueryString();
			if (!SLibrary.isNull(usedKey)) {
				VbyP.accessLog(" >> ���� ��û "+ user_id+" : ����� key "+ usedKey);
				throw new Exception("���� id�� ������ ��� �ϼ̽��ϴ�.");
			}
			SQL = VbyP.getSQL( "couponKeySelect" );
			pq.setPrepared(conn, SQL);
			pq.setString(1, key);
			
			String keyCheck = pq.ExecuteQueryString();
			if (SLibrary.isNull(keyCheck)) {
				VbyP.accessLog(" >> ���� ��û "+ user_id+" : ���ǰų� ���� key "+ usedKey);
				throw new Exception("�������� �ʰų� ���� �����Դϴ�.");
			}
			
			SQL = VbyP.getSQL( "couponUse" );
			pq.setPrepared(conn, SQL);
			pq.setString(1, user_id);
			pq.setString(2, key);
			int rslt = pq.executeUpdate();
			
			if (rslt < 1) throw new Exception("���� ��Ͽ� �����Ͽ����ϴ�.");
			
			int addCount = SLibrary.intValue( VbyP.getValue("couponCount") );
			
			UserInformationVO uvo = new SessionManagement().getUserInformation(conn, user_id);
			PointManager pm = PointManager.getInstance();
			rslt = pm.insertUserPoint(conn, uvo, 96, addCount * PointManager.DEFULT_POINT);
			
			if (rslt != 1)
				throw new Exception("�Ǽ� ������ ���� �Ͽ����ϴ�.");
			
			
			
		}catch (Exception e) {
			resultString = e.getMessage();
			VbyP.errorLogDaily("setCoupon >>"+e.toString()); 
		}	
		finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("setCoupon >> conn.close() Exception!"); }
			conn = null;
		}
		return resultString;
	}
	
	
	public BooleanAndDescriptionVO setMMSUpload(byte[] bytes, String fileName){
		
		VbyP.accessLog(" >> MMS ���ε� ��û ");
		String path = VbyP.getValue("mmsOrgPath");
		BooleanAndDescriptionVO bvo = new BooleanAndDescriptionVO();
		bvo.setbResult(false);
		
		try {
			FileUtils fu = new FileUtils();
			//���� Ȯ���ڰ� ������ϰ�츦 ����ؼ� �ҹ��ڷ� ��ȯ
			fileName = fileName.toLowerCase();
			fileName.endsWith(".jpg");
			if ( !fileName.endsWith(".jpg") ) throw new Exception("jpg Ȯ���ڸ� ���� �մϴ�.");
			
			String uploadName = fu.doUploadRename(bytes, path, fileName);
			Thumbnail2 tmb = new Thumbnail2();
			tmb.createThumbnail(path+uploadName, VbyP.getValue("mmsPath")+ uploadName, 176);
			tmb.createThumbnail(path+uploadName, VbyP.getValue("mmsPathPP")+ uploadName, 176);
			bvo.setstrDescription( VbyP.getValue("mmsURL")+uploadName );
			bvo.setbResult(true);
			
		}catch(Exception e){
			bvo.setbResult(false);
			bvo.setstrDescription("�̹��� ������ ���ε� ���� �ʾҽ��ϴ�.\r\n"+e.getMessage());
		}
	    
		return bvo;
	}
	
	public BooleanAndDescriptionVO setMMSEmtUpload(byte[] bytes, String fileName){
		
		VbyP.accessLog(" >> MMS ���ε� ��û ");
		String path = VbyP.getValue("mmsEmtPath");
		BooleanAndDescriptionVO bvo = new BooleanAndDescriptionVO();
		bvo.setbResult(false);
		String temp = "";
		try {
			FileUtils fu = new FileUtils();
			//���� Ȯ���ڰ� ������ϰ�츦 ����ؼ� �ҹ��ڷ� ��ȯ
			temp = fileName.toLowerCase();
			temp.endsWith(".jpg");
			if ( !temp.endsWith(".jpg") ) throw new Exception("jpg Ȯ���ڸ� ���� �մϴ�.");
			
			fileName = SLibrary.replaceAll(fileName, VbyP.getValue("mmsURL"), "");
			System.out.println(fileName);
			fu.doUpload(bytes, path, fileName);
			
			bvo.setstrDescription( VbyP.getValue("mmsEmtURL")+fileName );
			bvo.setbResult(true);
			
		}catch(Exception e){
			bvo.setbResult(false);
			bvo.setstrDescription("�̹��� ������ ���ε� ���� �ʾҽ��ϴ�.\r\n"+e.getMessage());
		}
	    
		return bvo;
	}
	
	public void addEmotiCate(String gubun, String cate, String msg) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> ������ �̸�Ƽ�� SMS �߰� "+gubun+"/"+cate+" "+msg);
		
		if (isAdminLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminEmoticonInsertCate"));
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, gubun);
				pq.setString(2, cate);
				pq.setString(3, msg);
				pq.executeUpdate();
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("addEmotiCate >> conn.close() Exception!"); }
			}
		}
		
	}
	
	public void addEmotiCateLMS(String gubun, String cate, String msg) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> ������ �̸�Ƽ�� LMS �߰� "+gubun+"/"+cate+" "+msg);
		
		if (isAdminLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminEmoticonInsertCateLMS"));
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, gubun);
				pq.setString(2, cate);
				pq.setString(3, msg);
				pq.executeUpdate();
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("addEmotiCateLMS >> conn.close() Exception!"); }
			}
		}
		
	}
	
	public void addEmotiCateMMS(String gubun, String cate, String title, String msg) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> ������ �̸�Ƽ�� MMS �߰� "+gubun+" "+title+" "+cate+" "+msg);
		
		if (isAdminLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminEmoticonInsertCateMMS"));
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, gubun);
				pq.setString(2, cate);
				pq.setString(3, msg);
				pq.setString(4, title);
				pq.executeUpdate();
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("addEmotiCateMMS >> conn.close() Exception!"); }
			}
		}
		
	}
	
	public void updateEmotiCate(String mode, String gubun, String oldCate, String newCate) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> ������ �̸�Ƽ�� ī�װ� ���� "+mode+":"+oldCate+"->"+newCate);
		
		if (isAdminLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				if (mode.equals("SMS")) buf.append(VbyP.getSQL("adminEmoticonUpdateCate"));
				else if (mode.equals("LMS")) buf.append(VbyP.getSQL("adminEmoticonUpdateCateLMS"));
				else if (mode.equals("MMS")) buf.append(VbyP.getSQL("adminEmoticonUpdateCateMMS"));
				
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, newCate);
				pq.setString(2, gubun);
				pq.setString(3, oldCate);
				pq.executeUpdate();
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("updateEmotiCate >> conn.close() Exception!"); }
			}
		}
		
	}
	
	public BooleanAndDescriptionVO isAdminLogin() {
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();			
		if (this.bAdminSession()) {
			rvo.setbResult(true);
		}
		else {
			rvo.setbResult(false);
		}
		
		return rvo;
	}
}
 