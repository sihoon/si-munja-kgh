package com.m;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.common.VbyP;
import com.common.db.PreparedExecuteQueryManager;
import com.common.util.SLibrary;
import com.m.billing.Billing;
import com.m.billing.BillingVO;
import com.m.common.AdminSMS;
import com.m.common.BooleanAndDescriptionVO;
import com.m.common.PointManager;
import com.m.member.MemberVO;
import com.m.member.SessionManagement;
import com.m.member.UserInformationVO;
import com.m.notic.NoticDAO;
import com.m.notic.NoticVO;

public class Admin extends SessionManagement {

	public Admin() {}
	
	/*###############################
	#	Member						#
	###############################*/
	public BooleanAndDescriptionVO login(String user_id, String password) {

		Connection conn = null;
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		
		try {
			
			conn = VbyP.getDB();
			if ( SLibrary.isNull(user_id) ) {
				rvo.setbResult(false);
				rvo.setstrDescription("로그인 실패.");
			}else if ( SLibrary.isNull(password) ) {
				rvo.setbResult(false);
				rvo.setstrDescription("로그인 실패.");
			}else {
				rvo = super.loginAdmin(conn, user_id, password);
			}
		}catch (Exception e) {}
		finally {
			
			try {
				if ( conn != null )
					conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("login >> conn.close() Exception!"); 
			}
		}
		
		return rvo;
	}
	public BooleanAndDescriptionVO isLogin() {
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();			
		if (this.bAdminSession()) {
			rvo.setbResult(true);
		}
		else {
			rvo.setbResult(false);
		}
		
		return rvo;
	}
	
	public List<HashMap<String, String>> getMember() {

		
		Connection conn = null;
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		VbyP.accessLog(getAdminSession()+" >> 회원 리스트");
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				
				
				if (getAdminSession() != null && !getAdminSession().equals("")) {
					
					
					StringBuffer buf = new StringBuffer();

					buf.append( VbyP.getSQL("memberList") );
							
					PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
					pq.setPrepared( conn, buf.toString() );
					
					
					al = pq.ExecuteQueryArrayList();
					
					
					return al;
				}
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("getMember >> conn.close() Exception!"); }
			}
		}
		
		return al;
	}
	
	public int updateMember(MemberVO mvo) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 회원 수정"+mvo.getUser_id());
		int rslt = 0;
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminMemberUpdateLog"));
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setInt(1, mvo.getIdx());
				pq.setString(2, mvo.getUser_id());
				pq.executeUpdate();
				
				buf.setLength(0);
				buf.append( VbyP.getSQL("adminMemberUpdate") );
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, mvo.getUser_name());
				pq.setString(2, mvo.getJumin_no());
				pq.setString(3, mvo.getEmail());
				pq.setString(4, mvo.getPhone_return());
				pq.setString(5, mvo.getHp());
				pq.setString(6, mvo.getUnit_cost());
				pq.setString(7, mvo.getLine());
				pq.setString(8, mvo.getMemo());
				pq.setString(9, mvo.getTimeLogin());
				pq.setString(10, mvo.getTimeJoin());
				pq.setString(11, mvo.getLeaveYN());
				pq.setInt(12, mvo.getIdx());
				pq.setString(13, mvo.getUser_id());
				
				rslt = pq.executeUpdate();
					
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("getMember >> conn.close() Exception!"); }
			}
		}
		
		return rslt;
	}
	
	public int updateMemberPasswd(String user_id) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 회원 비밀번호 초기화 "+user_id);
		int rslt = 0;
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminMemberInitPasswd"));
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, VbyP.getValue("initPassword"));
				pq.setString(2, user_id);
				rslt = pq.executeUpdate();
				
				
					
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("updateMemberPasswd >> conn.close() Exception!"); }
			}
		}
		
		return rslt;
	}
	
	public int deleteMember(String user_id) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 회원 삭제 "+user_id);
		int rslt = 0;
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminMemberDelete"));
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, user_id);
				rslt = pq.executeUpdate();
				
				buf.setLength(0);
				buf.append(VbyP.getSQL("adminPointDelete"));
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, user_id);
				rslt = pq.executeUpdate();
				
				
					
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("deleteMember >> conn.close() Exception!"); }
			}
		}
		
		return rslt;
	}
	
	public List<HashMap<String, String>> getPoint() {

		
		Connection conn = null;
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		VbyP.accessLog(getAdminSession()+" >> 포인트 리스트");
		
		if (isLogin().getbResult()) {		
		
			try {
				conn = VbyP.getDB();
				if (getAdminSession() != null && !getAdminSession().equals("")) {
					
					
					StringBuffer buf = new StringBuffer();

					buf.append( VbyP.getSQL("pointList") );
							
					PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
					pq.setPrepared( conn, buf.toString() );
					
					
					al = pq.ExecuteQueryArrayList();
					
					
					return al;
				}
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("getMember >> conn.close() Exception!"); }
			}
		}
		
		return al;
	}
	
	public int setPoint(String user_id, int point) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 포인트 설정");
		
		SessionManagement sm = new SessionManagement();
		UserInformationVO mvo = null;
		
		int rslt = 0;
		
		
		if (isLogin().getbResult()) {		
			
			try {
				conn = VbyP.getDB();
				mvo = sm.getUserInformation(conn, user_id);
				
				PointManager pm = PointManager.getInstance();		
				rslt = pm.insertUserPoint(conn, mvo, 90, point * PointManager.DEFULT_POINT);
				
				if (point * PointManager.DEFULT_POINT > 0 && !SLibrary.isNull( mvo.getHp() ) ) {
					AdminSMS asms = AdminSMS.getInstance();
					String tempMessage = "[munja119] "+SLibrary.addComma( point * PointManager.DEFULT_POINT )+" 건 충전이 완료 되었습니다.";
					asms.sendAdmin(conn, tempMessage , mvo.getHp() , "16000816");
				}
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("setPoint >> conn.close() Exception!"); }
			}
		}
		return rslt;
	}
	
	public Double getUnit(String user_id) {
		
		String rslt = "0";
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 관리자 사용자 단가 확인 : "+ user_id);
		
		if (isLogin().getbResult()) {		
			
			try {
				conn = VbyP.getDB();
				
				
				rslt = Billing.getInstance().getUnit(conn, user_id);
				
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("getUnit >> conn.close() Exception!"); }
			}
		}
		Double d = 0.0;
		try {
			d = Double.parseDouble(rslt);
		}catch(Exception e) {d = 0.0;}
		
		return d;
	}
	
	public BooleanAndDescriptionVO setCash(String user_id, int amount, int point, boolean bSMS) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 관리자 충전");

		BooleanAndDescriptionVO badvo = null;
		
		if (isLogin().getbResult()) {		
			
			try {
				conn = VbyP.getDB();
				BillingVO bvo = new BillingVO();
				bvo.setUser_id(user_id);
				bvo.setAdmin_id("admin");
				bvo.setAmount( amount );
				bvo.setMemo("");
				bvo.setMethod("cash");
				bvo.setOrder_no("");
				
				badvo = Billing.getInstance().setCashBilling(conn, bvo, point, bSMS);
				
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("setCash >> conn.close() Exception!"); }
			}
		}
		return badvo;
	}
	
	public List<HashMap<String, String>> getPointLog() {

		
		Connection conn = null;
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		VbyP.accessLog(getAdminSession()+" >> 로인트 로그 리스트");
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				
				
				if (getAdminSession() != null && !getAdminSession().equals("")) {
					
					
					StringBuffer buf = new StringBuffer();

					buf.append( VbyP.getSQL("pointLogList") );
							
					PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
					pq.setPrepared( conn, buf.toString() );
					
					
					al = pq.ExecuteQueryArrayList();
					
					
					return al;
				}
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("getMember >> conn.close() Exception!"); }
			}
		}
		
		return al;
	}
	public List<HashMap<String, String>> getSentLog() {

		
		Connection conn = null;
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		VbyP.accessLog(getAdminSession()+" >> 전송내역");
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				
				
				if (getAdminSession() != null && !getAdminSession().equals("")) {
					
					
					StringBuffer buf = new StringBuffer();

					buf.append( VbyP.getSQL("pointLogList") );
							
					PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
					pq.setPrepared( conn, buf.toString() );
					
					
					al = pq.ExecuteQueryArrayList();
					
					
					return al;
				}
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("getMember >> conn.close() Exception!"); }
			}
		}
		
		return al;
	}
	
	public List<HashMap<String, String>> getSentGroupList( String fromDate, String endDate, boolean bReservation) {

		
		Connection conn = null;
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				String user_id = getAdminSession();
				VbyP.accessLog(user_id+" >> 상세내역:"+fromDate+"~"+endDate+","+bReservation);
				
				if (user_id != null && !user_id.equals("")) {
					
					
					StringBuffer buf = new StringBuffer();
					buf.append( (!bReservation)?VbyP.getSQL("adminSelectSentLog"):VbyP.getSQL("adminSelectSentLogRes") );
							
					PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
					pq.setPrepared( conn, buf.toString() );
					pq.setString(1, fromDate+" 00:00:00");
					pq.setString(2, endDate+" 23:59:59");
					
					
					al = pq.ExecuteQueryArrayList();

				}
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("getSentGroupList >> conn.close() Exception!"); }
			}
		}
		
		return al;
	}
	
	public List<HashMap<String, String>> getSentList( String user_id, int groupIndex, String line) {

		
		Connection connSMS = null;
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		
		if (isLogin().getbResult()) {		
		
			try {
				
				connSMS = VbyP.getDB(line);
				VbyP.accessLog(getAdminSession()+" >> "+line+" 전송내역:"+ Integer.toString(groupIndex));
				
				if (user_id != null && !user_id.equals("")) {
							
					PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
					if (SLibrary.IfNull(line).equals("sk")) pq.setPrepared( connSMS, VbyP.getSQL("selectSentDataSK") );
					else if (SLibrary.IfNull(line).equals("mms")) pq.setPrepared( connSMS, VbyP.getSQL("selectSentDataLMS") );
					else if (SLibrary.IfNull(line).equals("skmms")) pq.setPrepared( connSMS, VbyP.getSQL("selectSentDataSK") );
					else if (SLibrary.IfNull(line).equals("kt")) pq.setPrepared( connSMS, VbyP.getSQL("selectSentDataKT") );
					else if (SLibrary.IfNull(line).equals("ktmms")) pq.setPrepared( connSMS, VbyP.getSQL("selectLMSSentDataKT") );
					else if (SLibrary.IfNull(line).equals("han")) pq.setPrepared( connSMS, VbyP.getSQL("selectSentDataHN") );
					else if (SLibrary.IfNull(line).equals("hanr")) pq.setPrepared( connSMS, VbyP.getSQL("selectSentDataHNR") );
					else if (SLibrary.IfNull(line).equals("it")) pq.setPrepared( connSMS, VbyP.getSQL("selectSentDataIT") );
					else pq.setPrepared( connSMS, VbyP.getSQL("selectSentData") );
					pq.setString(1, user_id);
					pq.setString(2, Integer.toString(groupIndex));
					pq.setString(3, user_id);
					pq.setString(4, Integer.toString(groupIndex));
					
					al = pq.ExecuteQueryArrayList();

				}
			}catch (Exception e) {}	finally {			
				try { if ( connSMS != null ) connSMS.close();
				}catch(SQLException e) { VbyP.errorLog("getSentList >> conn.close() Exception!"); }
			}
		}
		
		return al;
	}
	
	//Billing
	public List<HashMap<String, String>> getBilling() {

		
		Connection conn = null;
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		VbyP.accessLog(getAdminSession()+" >> 결제내역");
		
		if (isLogin().getbResult()) {		
		
			try {
				conn = VbyP.getDB();
				if (getAdminSession() != null && !getAdminSession().equals("")) {
					
					
					StringBuffer buf = new StringBuffer();

					buf.append( VbyP.getSQL("adminBillingList") );
							
					PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
					pq.setPrepared( conn, buf.toString() );
					
					
					al = pq.ExecuteQueryArrayList();
					
					
					return al;
				}
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("getBilling >> conn.close() Exception!"); }
			}
		}
		
		return al;
	}
	
	public int updateBilling(BillingVO bvo) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 결제 수정"+bvo.getUser_id());
		int rslt = 0;
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminBillingUpdate"));

				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, bvo.getMethod());
				pq.setInt(2, bvo.getAmount());
				pq.setString(3, bvo.getOrder_no());
				pq.setString(4, bvo.getUnit_cost());
				pq.setInt(5, bvo.getRemain_point());
				pq.setString(6, bvo.getMemo());
				pq.setString(7, bvo.getAdmin_id());
				pq.setString(8, bvo.getTimeWrite());
				pq.setInt(9, bvo.getIdx());
				pq.setString(10, bvo.getUser_id());
				
				rslt = pq.executeUpdate();
					
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("updateBilling >> conn.close() Exception!"); }
			}
		}
		
		return rslt;
	}
	public int deleteBilling(int idx) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 결제 삭제"+Integer.toString(idx));
		int rslt = 0;
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminBillingDelete"));
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setInt(1, idx);
				rslt = pq.executeUpdate();
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("deleteBilling >> conn.close() Exception!"); }
			}
		}
		
		return rslt;
	}
	
	public List<HashMap<String, String>> getCashList() {

		Connection conn = null;
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		VbyP.accessLog(getAdminSession()+" >> 무통장입금 예약");
		
		if (isLogin().getbResult()) {		
		
			try {
				conn = VbyP.getDB();
				if (getAdminSession() != null && !getAdminSession().equals("")) {
					
					
					StringBuffer buf = new StringBuffer();

					buf.append( VbyP.getSQL("adminCashList") );
							
					PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
					pq.setPrepared( conn, buf.toString() );
					
					
					al = pq.ExecuteQueryArrayList();
					
					
					return al;
				}
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("getCashList >> conn.close() Exception!"); }
			}
		}
		
		return al;
	}
	
	public List<HashMap<String, String>> getTaxList() {

		Connection conn = null;
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		VbyP.accessLog(getAdminSession()+" >> 세금계산서");
		
		if (isLogin().getbResult()) {		
		
			try {
				conn = VbyP.getDB();
				if (getAdminSession() != null && !getAdminSession().equals("")) {
					
					
					StringBuffer buf = new StringBuffer();

					buf.append( VbyP.getSQL("adminTaxList") );
							
					PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
					pq.setPrepared( conn, buf.toString() );
					
					
					al = pq.ExecuteQueryArrayList();
					
					
					return al;
				}
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("getTaxList >> conn.close() Exception!"); }
			}
		}
		
		return al;
	}
	
	public int deleteCash(int idx) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 무통장 예약 삭제"+Integer.toString(idx));
		int rslt = 0;
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminCashDelete"));
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setInt(1, idx);
				rslt = pq.executeUpdate();
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("deleteCash >> conn.close() Exception!"); }
			}
		}
		
		return rslt;
	}
	
	public int setTaxDelete(int idx) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 세금계산서 삭제"+Integer.toString(idx));
		int rslt = 0;
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminTaxDelete"));
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setInt(1, idx);
				rslt = pq.executeUpdate();
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("setTaxDelete >> conn.close() Exception!"); }
			}
		}
		
		return rslt;
	}
	
	public int setTaxComplet(int idx) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 세금계산서 완료"+Integer.toString(idx));
		int rslt = 0;
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminTaxComplet"));
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setInt(1, idx);
				rslt = pq.executeUpdate();
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("setTaxComplet >> conn.close() Exception!"); }
			}
		}
		
		return rslt;
	}
	
	
	public String[] getEmoti(int page, String category) {
		

		Connection conn = null;
		ArrayList<HashMap<String, String>> al = null;
		String [] arr = null;
		
		int alCount = 0;
		
		int count = 20;
		int from = 0;
		
		try {
			
			conn = VbyP.getDB();
			
			if (page == 0) page = 1;
			from = count * (page -1);
			
			VbyP.accessLog(" >>  관리자 이모티콘 요청("+category+") "+Integer.toString(from));
			
			StringBuffer buf = new StringBuffer();
			buf.append(VbyP.getSQL("adminEmoticon"));
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, buf.toString() );
			pq.setString(1, category);
			pq.setInt(2, from);
			pq.setInt(3, count);
			
			al = pq.ExecuteQueryArrayList();
			alCount = al.size();
			HashMap<String, String> hm = null;
			arr = new String[alCount];
			
			for (int i = 0; i < alCount; i++) {
				hm = al.get(i);
				arr[i] = SLibrary.padL(hm.get("idx"),7, "0")+hm.get("msg");
			}
			
			
		}catch (Exception e) {}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("getEmoti >> conn.close() Exception!"); }
		}
		
		return arr;
	}
	
	public String[] getEmotiAdmin(String Gubun, String category, int page) {
		

		Connection conn = null;
		ArrayList<HashMap<String, String>> al = null;
		String [] arr = null;
		
		int alCount = 0;
		
		int count = 20;
		int from = 0;
		
		try {
			
			conn = VbyP.getDB();
			
			if (page == 0) page = 1;
			from = count * (page -1);
			
			VbyP.accessLog(" >>  관리자 이모티콘 요청("+Gubun+"/"+category+") "+Integer.toString(from));
			
			StringBuffer buf = new StringBuffer();
			buf.append(VbyP.getSQL("adminEmoticonCate"));
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, buf.toString() );
			pq.setString(1, Gubun);
			pq.setString(2, "%"+category+"%");
			pq.setInt(3, from);
			pq.setInt(4, count);
			
			al = pq.ExecuteQueryArrayList();
			alCount = al.size();
			HashMap<String, String> hm = null;
			arr = new String[alCount];
			
			for (int i = 0; i < alCount; i++) {
				hm = al.get(i);
				arr[i] = SLibrary.padL(hm.get("idx"),7, "0")+hm.get("msg");
			}
			
			
		}catch (Exception e) {}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("getEmoti >> conn.close() Exception!"); }
		}
		
		return arr;
	}
	
	public ArrayList<HashMap<String, String>> getEmotiAdminHm(String Gubun, String category, int page) {
		

		Connection conn = null;
		ArrayList<HashMap<String, String>> al = null;

		int count = 20;
		int from = 0;
		
		try {
			
			conn = VbyP.getDB();
			
			if (page == 0) page = 1;
			from = count * (page -1);
			
			VbyP.accessLog(" >>  관리자 이모티콘 요청("+Gubun+"/"+category+") "+Integer.toString(from));
			
			StringBuffer buf = new StringBuffer();
			buf.append(VbyP.getSQL("adminEmoticonCate"));
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, buf.toString() );
			pq.setString(1, Gubun);
			pq.setString(2, category+"%");
			pq.setInt(3, from);
			pq.setInt(4, count);
			
			al = pq.ExecuteQueryArrayList();
			
			
			
		}catch (Exception e) {}	finally {			
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("getEmoti >> conn.close() Exception!"); }
		}
		
		return al;
	}
	
	
	public void updateEmoti(int idx, String msg) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 이모티콘 업데이트 "+Integer.toString(idx));
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminEmoticonUpdate"));
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, msg);
				pq.setInt(2, idx);
				pq.executeUpdate();
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("updateEmoti >> conn.close() Exception!"); }
			}
		}
		
	}
	
	public void updateEmotiCate(int idx, String gubun, String category, String msg) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 이모티콘 업데이트 "+Integer.toString(idx));
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminEmoticonUpdate2"));
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, gubun);
				pq.setString(2, category);
				pq.setString(3, msg);
				pq.setInt(4, idx);
				pq.executeUpdate();
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("updateEmotiCate >> conn.close() Exception!"); }
			}
		}
		
	}
	
	public void updateEmotiCateLMS(int idx, String msg) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 이모티콘 LMS 업데이트 "+Integer.toString(idx));
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminEmoticonUpdateLMS"));
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, msg);
				pq.setInt(2, idx);
				pq.executeUpdate();
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("updateEmotiCateLMS >> conn.close() Exception!"); }
			}
		}
		
	}
	
	public void deleteEmoti(int idx) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 이모티콘 삭제 "+Integer.toString(idx));
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminEmoticonDelete"));
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setInt(1, idx);
				pq.executeUpdate();
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("deleteEmoti >> conn.close() Exception!"); }
			}
		}
		
	}
	
	public void deleteEmotiLMS(int idx) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 이모티콘 LMS 삭제 "+Integer.toString(idx));
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminEmoticonDeleteLMS"));
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setInt(1, idx);
				pq.executeUpdate();
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("deleteEmotiLMS >> conn.close() Exception!"); }
			}
		}
		
	}
	
	public void deleteEmotiMMS(int idx) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 이모티콘 MMS 삭제 "+Integer.toString(idx));
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminEmoticonDeleteMMS"));
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setInt(1, idx);
				pq.executeUpdate();
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("deleteEmotiMMS >> conn.close() Exception!"); }
			}
		}
		
	}
	
	public void addEmoti(String cate, String msg) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 이모티콘 추가 "+cate+" "+msg);
		
		if (isLogin().getbResult()) {		
		
			try {
				
				conn = VbyP.getDB();
				StringBuffer buf = new StringBuffer();
				buf.append(VbyP.getSQL("adminEmoticonInsert"));
				PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
				pq.setPrepared( conn, buf.toString() );
				pq.setString(1, cate);
				pq.setString(2, msg);
				pq.executeUpdate();
				
			}catch (Exception e) {}	finally {			
				try { if ( conn != null ) conn.close();
				}catch(SQLException e) { VbyP.errorLog("addEmoti >> conn.close() Exception!"); }
			}
		}
		
	}
	
	public void addEmotiCate(String gubun, String cate, String msg) {
		
		Connection conn = null;
		VbyP.accessLog(getAdminSession()+" >> 이모티콘 추가 "+gubun+"/"+cate+" "+msg);
		
		if (isLogin().getbResult()) {		
		
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
	
	public ArrayList<NoticVO> getNotic() {
		
		Connection conn = null;
		ArrayList<NoticVO> rslt = null;
		NoticDAO nd = null;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  공지사항 리스트 요청");
			
			rslt = nd.getList(conn);
			
		}catch (Exception e) {}	
		finally {
			try { 
				if ( conn != null ) 
					conn.close();
			}catch(SQLException e) { VbyP.errorLog("getNotic >> conn.close() Exception!"); }
		}
		
		return rslt;
	}
	
	public int insertNotic(NoticVO vo) {
		
		Connection conn = null;
		NoticDAO nd = null;
		int rslt = 0;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  공지사항 추가 요청");
			
			rslt = nd.insert(conn, vo);
			
		}catch (Exception e) {}	
		finally {
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("insertNotic >> conn.close() Exception!"); }
		}
		
		return rslt;
	}
	
	public int modifyNotic(NoticVO vo) {
		
		Connection conn = null;
		NoticDAO nd = null;
		int rslt = 0;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  공지사항 수정 요청");
			
			rslt = nd.modify(conn, vo);
			
		}catch (Exception e) {}	
		finally {
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("modifyNotic >> conn.close() Exception!"); }
		}
		
		return rslt;
	}
	
	public int deleteNotic(int idx) {
		
		Connection conn = null;
		NoticDAO nd = null;
		int rslt = 0;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  공지사항 삭제 요청");
			
			rslt = nd.delete(conn, idx);
			
		}catch (Exception e) {}	
		finally {
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("deleteNotic >> conn.close() Exception!"); }
		}
		
		return rslt;
	}
	
	// FAQ
	public ArrayList<NoticVO> getFAQ() {
		
		Connection conn = null;
		ArrayList<NoticVO> rslt = null;
		NoticDAO nd = null;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  FAQ 리스트 요청");
			
			rslt = nd.getListFAQ(conn);
			
		}catch (Exception e) {}	
		finally {
			try { 
				if ( conn != null ) 
					conn.close();
			}catch(SQLException e) { VbyP.errorLog("getFAQ >> conn.close() Exception!"); }
		}
		
		return rslt;
	}
	
	public int insertFAQ(NoticVO vo) {
		
		Connection conn = null;
		NoticDAO nd = null;
		int rslt = 0;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  FAQ 추가 요청");
			
			rslt = nd.insertFAQ(conn, vo);
			
		}catch (Exception e) {}	
		finally {
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("insertFAQ >> conn.close() Exception!"); }
		}
		
		return rslt;
	}
	
	public int modifyFAQ(NoticVO vo) {
		
		Connection conn = null;
		NoticDAO nd = null;
		int rslt = 0;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  FAQ 수정 요청");
			
			rslt = nd.modifyFAQ(conn, vo);
			
		}catch (Exception e) {}	
		finally {
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("modifyFAQ >> conn.close() Exception!"); }
		}
		
		return rslt;
	}
	
	public int deleteFAQ(int idx) {
		
		Connection conn = null;
		NoticDAO nd = null;
		int rslt = 0;
		
		try {
			
			nd = new NoticDAO();
			conn = VbyP.getDB();
			
			VbyP.accessLog(" >>  FAQ 삭제 요청");
			
			rslt = nd.deleteFAQ(conn, idx);
			
		}catch (Exception e) {}	
		finally {
			try { if ( conn != null ) conn.close();
			}catch(SQLException e) { VbyP.errorLog("deleteFAQ >> conn.close() Exception!"); }
		}
		
		return rslt;
	}
}
