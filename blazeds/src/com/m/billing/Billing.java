package com.m.billing;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.common.VbyP;
import com.common.db.PreparedExecuteQueryManager;
import com.common.util.SLibrary;
import com.m.common.AdminSMS;
import com.m.common.BooleanAndDescriptionVO;
import com.m.common.PointManager;
import com.m.member.SessionManagement;
import com.m.member.UserInformationVO;

public class Billing {

	public int totalCnt = 0;
	static Billing bill = new Billing();
	Billing(){}
	public static Billing getInstance(){
		return bill;
	}
	
	public HashMap<String, String> getBilling(Connection conn, int idx) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();	
		pq.setPrepared(conn, VbyP.getSQL("selectBillingTax") );
		pq.setInt(1, idx);
		
		return pq.ExecuteQueryCols();
	}
	
	public int setTax(Connection conn,int billing_idx, String user_id, String comp_name, String comp_no, String name, String addr, String upte, String upjong, String email, String yn) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();	
		pq.setPrepared(conn, VbyP.getSQL("insertTax") );
		pq.setString(1, user_id);
		pq.setInt(2, billing_idx);
		pq.setString(3, comp_name);
		pq.setString(4, comp_no);
		pq.setString(5, name);
		pq.setString(6, addr);
		pq.setString(7, upte);
		pq.setString(8, upjong);
		pq.setString(9, email);
		pq.setString(10, yn);

		return pq.executeUpdate();
	}
	
	public ArrayList<HashMap<String, String>> getBillingList(Connection conn, String userId, int start, int end) {

		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();	
		pq.setPrepared(conn, VbyP.getSQL("selectBillingCnt") );
		pq.setString(1, userId);
		this.totalCnt = pq.ExecuteQueryNum();
		
		pq.setPrepared(conn, VbyP.getSQL("selectBilling") );
		pq.setString(1, userId);
		pq.setInt(2, start);
		pq.setInt(3, end);
		
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		al = pq.ExecuteQueryArrayList();
		
		return al;
	}
	
	public BooleanAndDescriptionVO setCash(Connection conn, String user_id, String account, String amount, String method, String reqName) {
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		int rslt = 0;
		
		
		try {
			
			
			VbyP.accessLog(" >> 무통장 입금 요청 "+ user_id +" , "+ account+" , "+ amount+" , "+ method+" , "+reqName);
			
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, VbyP.getSQL("insertCash") );
			pq.setString(1, user_id);
			pq.setString(2, account);
			pq.setInt(3, SLibrary.intValue(amount));
			pq.setString(4, method);
			pq.setString(5, reqName);
			pq.setString(6, SLibrary.getDateTimeString());
						
			rslt =  pq.executeUpdate();
			
			if ( rslt < 1)
				throw new Exception("무통장 등록에 실패 하였습니다.");
			
			rvo.setbResult(true);
			
		}catch (Exception e) {
			
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			
		}
		return rvo;
	}
	
	public BooleanAndDescriptionVO setCashBilling( Connection conn, BillingVO bvo, int count, boolean bSMS) {
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		UserInformationVO uvo = null;
		int rslt = 0;
		
		
		try {
			String user_id = bvo.getUser_id();
			if (user_id == null || user_id.equals("")) throw new Exception("로그인 되어 있지 않습니다.");
			if (conn == null) throw new Exception("DB연결이 되어 있지 않습니다.");
			
			
			VbyP.accessLog(" >> 결제등록 요청 "+ user_id +" , "+ Integer.toString(bvo.getAmount())+" , "+ bvo.getMethod());
			
			uvo = new SessionManagement().getUserInformation(conn, bvo.getUser_id());
			
			bvo.setPoint(count);
			bvo.setRemain_point( SLibrary.intValue(uvo.getPoint())+count);
			bvo.setTimeWrite(SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss"));
			bvo.setUnit_cost(Integer.toString(uvo.getUnit_cost()));
			
			if ( bill.insert(conn, bvo) < 1)
				throw new Exception("결제 등록에 실패 하였습니다.");
			
			
			PointManager pm = PointManager.getInstance();
			rslt = pm.insertUserPoint(conn, uvo, 03, count * PointManager.DEFULT_POINT);
			if (rslt != 1)
				throw new Exception("건수 충전에 실패 하였습니다.");

			rvo.setbResult(true);
			
			if (bSMS == true && !SLibrary.isNull( uvo.getHp() ) ) {

				AdminSMS asms = AdminSMS.getInstance();
				String tempMessage = "[munja119] 무통장 입금 "+SLibrary.addComma( bvo.getAmount() )+" 원 충전이 완료 되었습니다.";
				asms.sendAdmin(conn, tempMessage , uvo.getHp() , "16000816");
			}
			
		}catch (Exception e) {
			
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			
		}
		return rvo;
	}
	
	public String getUnit(Connection conn, String user_id) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();	
		pq.setPrepared(conn, VbyP.getSQL("selectBillingUnit") );
		pq.setString(1, user_id);
		
		return pq.ExecuteQueryString();
	}
	
	public BooleanAndDescriptionVO setBilling( Connection conn, BillingVO bvo) {
		
		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();
		rvo.setbResult(false);
		UserInformationVO uvo = null;
		int point = 0;
		int rslt = 0;
		
		
		try {
			String user_id = bvo.getUser_id();
			if (user_id == null || user_id.equals("")) throw new Exception("로그인 되어 있지 않습니다.");
			if (conn == null) throw new Exception("DB연결이 되어 있지 않습니다.");
			
			
			VbyP.accessLog(" >> 결제등록 요청 "+ user_id +" , "+ Integer.toString(bvo.getAmount())+" , "+ bvo.getMethod());
			
			uvo = new SessionManagement().getUserInformation(conn, bvo.getUser_id());
			
			/*
			if (bvo.getAmount() == ( 2000+( 2000 *0.1 ) ) ) point = 100;
			else if (bvo.getAmount() == ( 5700+( 5700 *0.1 ) ) ) point = 300;
			else if (bvo.getAmount() == ( 9000+( 9000 *0.1 ) ) ) point = 500;
			else if (bvo.getAmount() == ( 17000+( 17000 *0.1 ) ) ) point = 1000;
			else if (bvo.getAmount() == ( 24600+( 24600 *0.1 ) ) ) point = 1500;
			else if (bvo.getAmount() == ( 46500+( 46500 *0.1 ) ) ) point = 3000;
			else if (bvo.getAmount() == ( 74000+( 74000 *0.1 ) ) ) point = 5000;
			else if (bvo.getAmount() == ( 139000+( 139000 *0.1 ) ) ) point = 10000;
			else if (bvo.getAmount() == ( 260000+( 260000 *0.1 ) ) ) point = 20000;
			else if (bvo.getAmount() == ( 375000+( 375000 *0.1 ) ) ) point = 30000;
			else if (bvo.getAmount() == ( 600000+( 600000 *0.1 ) ) ) point = 50000;
			else if (bvo.getAmount() == ( 1150000+( 1150000 *0.1 ) ) ) point = 100000;
			else if (bvo.getAmount() == ( 3300000+( 3300000 *0.1 ) ) ) point = 300000;
			else if (bvo.getAmount() == ( 5350000+( 5350000 *0.1 ) ) ) point = 500000;
			else point = SLibrary.intValue( SLibrary.fmtBy.format( Math.ceil(bvo.getAmount()/uvo.getUnit_cost()) ) );
			*/
//			if (bvo.getAmount() == ( 2000+( 2000 *0.1 ) ) ) point = 166;
//			else if (bvo.getAmount() == ( 5700+( 5700 *0.1 ) ) ) point = 475;
//			else if (bvo.getAmount() == ( 9000+( 9000 *0.1 ) ) ) point = 750;
//			else if (bvo.getAmount() == ( 17000+( 17000 *0.1 ) ) ) point = 1416;
//			else if (bvo.getAmount() == ( 24600+( 24600 *0.1 ) ) ) point = 2050;
//			else if (bvo.getAmount() == ( 46500+( 46500 *0.1 ) ) ) point = 3875;
//			else if (bvo.getAmount() == ( 74000+( 74000 *0.1 ) ) ) point = 6166;
//			else if (bvo.getAmount() == ( 139000+( 139000 *0.1 ) ) ) point = 11583;
//			else if (bvo.getAmount() == ( 260000+( 260000 *0.1 ) ) ) point = 21666;
//			else if (bvo.getAmount() == ( 375000+( 375000 *0.1 ) ) ) point = 31250;
//			else if (bvo.getAmount() == ( 600000+( 600000 *0.1 ) ) ) point = 50000;
//			else if (bvo.getAmount() == ( 1150000+( 1150000 *0.1 ) ) ) point = 100000;
//			else if (bvo.getAmount() == ( 3300000+( 3300000 *0.1 ) ) ) point = 300000;
//			else if (bvo.getAmount() == ( 5350000+( 5350000 *0.1 ) ) ) point = 500000;
//			else point = SLibrary.intValue( SLibrary.fmtBy.format( Math.ceil(bvo.getAmount()/uvo.getUnit_cost()) ) );
			
			if (bvo.getAmount() == ( 5000+( 5000 *0.1 ) ) ) point = 417;
			else if (bvo.getAmount() == ( 10000+( 10000 *0.1 ) ) ) point = 833;
			else if (bvo.getAmount() == ( 30000+( 30000 *0.1 ) ) ) point = 2500;
			else if (bvo.getAmount() == ( 50000+( 50000 *0.1 ) ) ) point = 4167;
			else if (bvo.getAmount() == ( 100000+( 100000 *0.1 ) ) ) point = 8333;
			else if (bvo.getAmount() == ( 300000+( 300000 *0.1 ) ) ) point = 25000;
			else if (bvo.getAmount() == ( 500000+( 500000 *0.1 ) ) ) point = 43103;
			else if (bvo.getAmount() == ( 1000000+( 1000000 *0.1 ) ) ) point = 90909;
			else if (bvo.getAmount() == ( 3000000+( 3000000 *0.1 ) ) ) point = 280374;
			else if (bvo.getAmount() == ( 5000000+( 5000000 *0.1 ) ) ) point = 485437;
			else if (bvo.getAmount() == ( 10000000+( 10000000 *0.1 ) ) ) point = 1000000;
			else point = SLibrary.intValue( SLibrary.fmtBy.format( Math.ceil(bvo.getAmount()/uvo.getUnit_cost()) ) );
			
			bvo.setPoint(point);
			bvo.setRemain_point( SLibrary.intValue(uvo.getPoint())+point);
			bvo.setTimeWrite(SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss"));
			bvo.setUnit_cost(Integer.toString(uvo.getUnit_cost()));
			
			
			if ( bill.insert(conn, bvo) < 1)
				throw new Exception("결제 등록에 실패 하였습니다.");
			
			
			PointManager pm = PointManager.getInstance();
			rslt = pm.insertUserPoint(conn, uvo, 03, point * PointManager.DEFULT_POINT);
			if (rslt != 1)
				throw new Exception("건수 충전에 실패 하였습니다.");

			rvo.setbResult(true);
			
		}catch (Exception e) {
			
			rvo.setbResult(false);
			rvo.setstrDescription(e.getMessage());
			
		}
		return rvo;
	}
	
	private int insert(Connection conn, BillingVO vo) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("insertBilling") );
		pq.setString(1, vo.getUser_id());
		pq.setString(2, vo.getMethod());
		pq.setInt(3, vo.getAmount());
		pq.setString(4, vo.getOrder_no());
		pq.setString(5, vo.getUnit_cost());
		pq.setInt(6, vo.getPoint());
		pq.setInt(7, vo.getRemain_point());
		pq.setString(8, SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss"));
		pq.setString(9, vo.getTid());
		pq.setString(10, vo.getTimestamp());

		
		return pq.executeUpdate();
	}
	
	
}
