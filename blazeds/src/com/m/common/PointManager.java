package com.m.common;

import java.sql.Connection;
import java.sql.SQLException;

import com.common.VbyP;
import com.common.db.PreparedExecuteQueryManager;
import com.common.util.SLibrary;
import com.m.member.UserInformationVO;

public class PointManager {
	
	public static final int DEFULT_POINT=1;
	static PointManager pm = new PointManager();
	
	
	
	public static PointManager getInstance() {
		return pm;
	}
	private PointManager(){};
	
	public int initPoint(String user_id, int cnt) {
		
		Connection conn = null;
		int count = 0;

		try {
			conn = VbyP.getDB();
			PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
			pq.setPrepared( conn, VbyP.getSQL("initPoint") );
			pq.setString(1, user_id);
			pq.setInt(2, cnt);
			count = pq.executeUpdate();
			
			
			
		}catch(Exception e) {}
		finally {
			try {
				if ( conn != null )
					conn.close();
			}catch(SQLException e) {
				VbyP.errorLog("initPoint >> conn.close() Exception!"); 
			}
		}
		
		return count;
	}
	
	public int getUserPoint(Connection conn, String user_id) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("userPoint") );
		pq.setString(1, user_id);
		
		return pq.ExecuteQueryNum();
	}
	
	public int insertUserPoint(Connection conn, UserInformationVO mvo, int code, int point ) {
		
		System.out.println(mvo.getUser_id() + " insertUserPoint->"+ Integer.toString(code)+ "/ " +Integer.toString(point));
		int pcount = insertPointLog(conn, mvo, code, point);
		System.out.println(mvo.getUser_id() + " pointLog->"+ Integer.toString(pcount));
		if ( pcount == 1 ) {
			pcount = insertPoint(conn, mvo.getUser_id(), code, point);
			System.out.println(mvo.getUser_id() + "point->"+ Integer.toString(pcount));
			return pcount;
		}
		else
			return 0;
	}
	
	public int insertUserPoint(Connection conn, String user_id, int code, int point, String pay_type, String memo ) {
		
		if ( insertPointLog(conn, user_id, code, point, pay_type, memo) == 1 )
			return insertPoint(conn, user_id, code, point, memo);
		else
			return 0;
	}
	
	private int insertPoint(Connection conn, String user_id, int code, int point) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("insertPoint") );
		pq.setInt(1, point);
		pq.setString(2, SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss") );
		pq.setString(3, user_id);
		
		return pq.executeUpdate();
	}
	
	private int insertPointLog(Connection conn, UserInformationVO mvo, int code, int point) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("insertPointLog") );
		pq.setString(1, mvo.getUser_id());
		pq.setInt(2, point);
		pq.setString(3, SLibrary.addTwoSizeNumber(code));
		pq.setString(4, pointMemoFactory(code, point));
		pq.setString(5, SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss") );
		pq.setInt(6, SLibrary.intValue(mvo.getPoint()));
		pq.setInt(7, SLibrary.intValue(mvo.getPoint())+point);
		
		return pq.executeUpdate();
	}
	
	private int insertPoint(Connection conn, String user_id, int code, int point, String memo) {
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("insertPoint") );
		pq.setInt(1, point);
		pq.setString(2, SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss") );
		pq.setString(3, user_id);
		return pq.executeUpdate();
	}
	
	private int insertPointLog(Connection conn, String user_id, int code, int point, String pay_type, String memo) {
		
		createPointLogTable(conn);
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, SLibrary.messageFormat( VbyP.getSQL("insertPointLog"), new Object[]{SLibrary.getDateTimeString("yyyyMM")}) );
		pq.setString(1, user_id);
		pq.setString(2, pay_type);
		pq.setString(3, SLibrary.addTwoSizeNumber(code));
		pq.setString(4, memo);
		pq.setInt(5, point);
		pq.setInt(6, Integer.parseInt( SLibrary.getUnixtimeStringSecond() ) );
		
		return pq.executeUpdate();
	}
	
	private void createPointLogTable(Connection conn) {
		
		String logTable = "point_log_"+SLibrary.getDateTimeString("yyyyMM");
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared( conn, VbyP.getSQL("isPointLogTable") );
		pq.setString(1, logTable);
		
		if ( pq.ExecuteQueryString() == null ) {
			
			pq.setPrepared( conn, SLibrary.messageFormat( VbyP.getSQL("createPointLogTable"), new Object[]{logTable}) );
			int rslt = pq.executeUpdate();
			
			if (rslt == 1)
				VbyP.accessLog("create Table ....."+logTable);
		}
	}
	
	private String pointMemoFactory(int code, int point) {
		
		return VbyP.getValue("point_code_"+SLibrary.addTwoSizeNumber(code) ) + SLibrary.fmtBy.format( point/DEFULT_POINT ) ;
	}
}
