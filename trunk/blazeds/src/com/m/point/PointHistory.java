package com.m.point;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.common.VbyP;
import com.common.db.PreparedExecuteQueryManager;
import com.common.util.SLibrary;

public class PointHistory implements PointHistoryAble {
	
	public int totalCnt = 0;
	static PointHistory ph = new PointHistory();
	
	public static PointHistory getInstance(){
		return ph;
	}
	
	@Override
	public List<PointHistoryVO> getPointHistoryList(Connection conn, String userId, String whenMonth) {

		
		ArrayList<PointHistoryVO> rslt = new ArrayList<PointHistoryVO>();
		
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		
		pq.setPrepared(conn, VbyP.messageFormat(VbyP.getSQL("selectPointHistoryLog"), new Object[]{whenMonth}) );
		pq.setString(1, userId);
		
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		al = pq.ExecuteQueryArrayList();
		
		int count = al.size();
		if (count > 0) {
			
			PointHistoryVO vo = null;
			HashMap<String, String> h = null;
			
			try {
				
				for (int i = 0; i < count; i++) {
					
					vo = new PointHistoryVO();
					h = al.get(i);
					String _pointTemp = SLibrary.IfNull(h, "point");
					int point = Integer.parseInt(_pointTemp) / 25;
					
					vo.setAll( i+1, SLibrary.IfNull(h, "memo"), point, SLibrary.IfNull(h, "timeWrite") );
					
					rslt.add(vo);
				}
			}catch(Exception e){System.out.println("getSentGroupList Error!");}
			
			h = null;
			al = null;
		}
		
		
		return rslt;
	}
	
	
	public ArrayList<HashMap<String, String>> getPointHistoryList(Connection conn, String userId, int start, int end) {

		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, VbyP.getSQL("selectPointHistoryLogCnt") );
		pq.setString(1, userId);
		this.totalCnt = pq.ExecuteQueryNum();
		
		pq.setPrepared(conn, VbyP.getSQL("selectPointHistoryLog") );
		pq.setString(1, userId);
		pq.setInt(2, start);
		pq.setInt(3, end);
		
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		al = pq.ExecuteQueryArrayList();
		
		
		return al;
	}

}